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

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;
import static com.my.vkclient.Constants.SharedPreferences.ACCESS_TOKEN_SHARED_KEY;
import static com.my.vkclient.Constants.SharedPreferences.APP_SETTINGS;

public class VkRepository {
    private static VkRepository instance;
    private Executor executor;
    private boolean offlineAccess;
    private DatabaseRepositoryHelper databaseRepositoryHelper;
    private SharedPreferences sharedPreferences;
    private HttpRepositoryHelper httpRepositoryHelper;
    private List<News> newsList;

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

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public boolean isOfflineAccess() {
        return offlineAccess;
    }

    public void initialContext(@NonNull final Context context) {
        databaseRepositoryHelper = new DatabaseRepositoryHelper(context);

        sharedPreferences = context.getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);
        String newAccessToken = sharedPreferences.getString(ACCESS_TOKEN_SHARED_KEY, null);

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
        editor.remove(ACCESS_TOKEN_SHARED_KEY);
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
                if (databaseRepositoryHelper.getUser(userId, resultCallback)) {
                    return;
                }

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
        });
    }

    public void getGroup(final int groupId, final ResultCallback<Group> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (databaseRepositoryHelper.getGroup(groupId, resultCallback)) {
                    return;
                }

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
        });
    }

    public void getFriends(final int startPosition, final int size, final ResultCallback<List<User>> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (databaseRepositoryHelper.getFriends(startPosition, size, resultCallback)) {
                    return;
                }

                httpRepositoryHelper.getFriends(startPosition, size, new ResultCallback<List<User>>() {
                    @Override
                    public void onResult(List<User> friends) {
                        resultCallback.onResult(friends);

                        if (friends != null) {
                            databaseRepositoryHelper.putFriendList(friends);
                            databaseRepositoryHelper.putUserList(friends);
                        }
                    }
                });
            }
        });
    }

    public void getNews(final String startFrom, final int size, final ResultCallback<NewsResponse.Response> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (databaseRepositoryHelper.getNews(startFrom, size, resultCallback)) {
                    return;
                }

                httpRepositoryHelper.getNews(startFrom, size, new ResultCallback<NewsResponse.Response>() {
                    @Override
                    public void onResult(NewsResponse.Response newsResponse) {
                        resultCallback.onResult(newsResponse);

                        if (newsResponse != null) {
                            if (newsResponse.getGroupList() != null) {
                                databaseRepositoryHelper.putGroupList(newsResponse.getGroupList());
                            }

                            if (newsResponse.getUserList() != null) {
                                databaseRepositoryHelper.putUserList(newsResponse.getUserList());
                            }

                            if (newsResponse.getNewsList() != null) {
                                databaseRepositoryHelper.putNewsList(newsResponse.getNewsList());
                            }
                        }
                    }
                });
            }
        });
    }

    private void clearData() {
        newsList = null;
    }
}
