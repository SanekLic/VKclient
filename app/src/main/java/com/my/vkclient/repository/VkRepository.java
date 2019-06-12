package com.my.vkclient.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.response.LikesResponse;
import com.my.vkclient.entities.response.NewsResponse;
import com.my.vkclient.utils.ResultCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;
import static com.my.vkclient.Constants.STRING_EMPTY;
import static com.my.vkclient.Constants.SharedPreferences.ACCESS_TOKEN_SHARED_KEY;
import static com.my.vkclient.Constants.SharedPreferences.APP_SETTINGS;
import static com.my.vkclient.Constants.SharedPreferences.NEWS_START_FROM_SHARED_KEY;

public class VkRepository {
    private static VkRepository instance;
    private String newsStartFrom = STRING_EMPTY;
    private Executor executor;
    private boolean offlineAccess;
    private DatabaseRepositoryHelper databaseRepositoryHelper;
    private SharedPreferences sharedPreferences;
    private HttpRepositoryHelper httpRepositoryHelper;
    private List<News> newsList = new ArrayList<>();
    private List<User> friendList = new ArrayList<>();

    private VkRepository() {
        executor = Executors.newCachedThreadPool();
        httpRepositoryHelper = new HttpRepositoryHelper();
    }

    public static VkRepository getInstance() {
        if (instance == null) {
            instance = new VkRepository();
        }

        return instance;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void refreshNews() {
        newsList.clear();
        setNewsStartFrom(STRING_EMPTY);
        databaseRepositoryHelper.clearNews();
    }

    public List<User> getFriendList() {
        return friendList;
    }

    public void refreshFriends() {
        friendList.clear();
        databaseRepositoryHelper.clearFriends();
    }

    public boolean isOfflineAccess() {
        return offlineAccess;
    }

    public void initialContext(@NonNull final Context context) {
        databaseRepositoryHelper = new DatabaseRepositoryHelper(context);

        sharedPreferences = context.getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);
        String newAccessToken = sharedPreferences.getString(ACCESS_TOKEN_SHARED_KEY, null);
        newsStartFrom = sharedPreferences.getString(NEWS_START_FROM_SHARED_KEY, STRING_EMPTY);

        if (newAccessToken != null) {
            httpRepositoryHelper.setAccessToken(newAccessToken);
            offlineAccess = true;
        }
    }

    public void setAccessToken(String newAccessToken) {
        httpRepositoryHelper.setAccessToken(newAccessToken);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_SHARED_KEY, newAccessToken);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        databaseRepositoryHelper.clearAllTables();
        clearData();

        offlineAccess = false;
        httpRepositoryHelper.setAccessToken(null);
    }

    public void setLikeToNews(final News news, final boolean like, final ResultCallback<News> newsResultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                httpRepositoryHelper.setLikeNews(news.getSourceId(), news.getId(), like, new ResultCallback<LikesResponse.Response>() {
                    @Override
                    public void onResult(LikesResponse.Response likesResponse) {
                        if (likesResponse != null) {
                            news.getLikes().setUserLikes(like);
                            news.getLikes().setCount(likesResponse.getLikesCount());

                            newsResultCallback.onResult(news);

                            databaseRepositoryHelper.putNewsWithoutAttachments(news);
                        }
                    }
                });
            }
        });
    }

    public void getUser(final int userId, final ResultCallback<User> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseRepositoryHelper.getUser(userId, new ResultCallback<User>() {
                    @Override
                    public void onResult(User result) {
                        if (result != null) {
                            resultCallback.onResult(result);
                        } else {
                            httpRepositoryHelper.getUser(userId, new ResultCallback<User>() {
                                @Override
                                public void onResult(User user) {
                                    resultCallback.onResult(user);

                                    if (user != null) {
                                        databaseRepositoryHelper.putUser(user);
                                    }
                                }
                            });
                        }
                    }
                });

            }
        });
    }

    public void getGroup(final int groupId, final ResultCallback<Group> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseRepositoryHelper.getGroup(groupId, new ResultCallback<Group>() {
                    @Override
                    public void onResult(Group result) {
                        if (result != null) {
                            resultCallback.onResult(result);
                        } else {
                            httpRepositoryHelper.getGroup(groupId, new ResultCallback<Group>() {
                                @Override
                                public void onResult(Group group) {
                                    resultCallback.onResult(group);

                                    if (group != null) {
                                        databaseRepositoryHelper.putGroup(group);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void getFriends(final int startPosition, final int size, final ResultCallback<List<User>> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseRepositoryHelper.getFriends(startPosition, size, new ResultCallback<List<User>>() {
                    @Override
                    public void onResult(List<User> result) {
                        if (result != null && result.size() > 0) {
                            friendList.addAll(result);
                            resultCallback.onResult(result);
                        } else {
                            httpRepositoryHelper.getFriends(startPosition, size, new ResultCallback<List<User>>() {
                                @Override
                                public void onResult(List<User> result) {
                                    resultCallback.onResult(result);

                                    if (result != null) {
                                        friendList.addAll(result);
                                        databaseRepositoryHelper.putFriendList(result);
                                        databaseRepositoryHelper.putUserList(result);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void getNews(final int startPosition, final int size, final ResultCallback<List<News>> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseRepositoryHelper.getNews(startPosition, size, new ResultCallback<List<News>>() {
                    @Override
                    public void onResult(List<News> result) {
                        if (result != null && result.size() > 0) {
                            newsList.addAll(result);
                            resultCallback.onResult(result);
                        } else {
                            httpRepositoryHelper.getNews(newsStartFrom, size, new ResultCallback<NewsResponse.Response>() {
                                @Override
                                public void onResult(NewsResponse.Response newsResponse) {

                                    if (newsResponse != null) {
                                        setNewsStartFrom(newsResponse.getNextFrom());
                                        resultCallback.onResult(newsResponse.getNewsList());

                                        if (newsResponse.getGroupList() != null) {
                                            databaseRepositoryHelper.putGroupList(newsResponse.getGroupList());
                                        }

                                        if (newsResponse.getUserList() != null) {
                                            databaseRepositoryHelper.putUserList(newsResponse.getUserList());
                                        }

                                        if (newsResponse.getNewsList() != null) {
                                            newsList.addAll(newsResponse.getNewsList());
                                            databaseRepositoryHelper.putNewsList(newsResponse.getNewsList());
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void setNewsStartFrom(String newsNextFrom) {
        newsStartFrom = newsNextFrom;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NEWS_START_FROM_SHARED_KEY, newsStartFrom);
        editor.apply();
    }

    private void clearData() {
        newsList.clear();
        friendList.clear();

        newsStartFrom = STRING_EMPTY;
    }
}
