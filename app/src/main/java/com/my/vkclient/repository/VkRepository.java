package com.my.vkclient.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.my.vkclient.Constants;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.response.LikesResponse;
import com.my.vkclient.entities.response.NewsResponse;
import com.my.vkclient.gson.GsonHelper;
import com.my.vkclient.utils.ResultCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.Context.MODE_PRIVATE;
import static com.my.vkclient.Constants.API_VK.API_VK_SET_DISLIKE_POST;
import static com.my.vkclient.Constants.API_VK.API_VK_SET_LIKE_POST;
import static com.my.vkclient.Constants.SharedPreferences.ACCESS_TOKEN_SHARED_KEY;
import static com.my.vkclient.Constants.SharedPreferences.APP_SETTINGS;

public class VkRepository {
    private static VkRepository instance;
    private Executor executor;
    private String accessToken;
    private boolean offlineAccess;
    private DatabaseRepositoryHelper databaseRepositoryHelper;
    private SharedPreferences sharedPreferences;

    private VkRepository() {
        executor = Executors.newCachedThreadPool();
    }

    public static VkRepository getInstance() {
        if (instance == null) {
            instance = new VkRepository();
        }

        return instance;
    }

    public boolean isOfflineAccess() {
        return offlineAccess;
    }

    public void initialContext(@NonNull final Context context) {
        databaseRepositoryHelper = new DatabaseRepositoryHelper(context);

//        sharedPreferences = context.getSharedPreferences(APP_SETTINGS, MODE_PRIVATE);
//        String newAccessToken = sharedPreferences.getString(ACCESS_TOKEN_SHARED_KEY, null);
//
//        if (newAccessToken != null) {
//            accessToken = newAccessToken;
//            offlineAccess = true;
//        }
    }

    public void setAccessToken(String newAccessToken) {
        accessToken = newAccessToken;

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(ACCESS_TOKEN_SHARED_KEY,accessToken);
//        editor.apply();
    }

    public void setLikeToNews(final News news, final boolean like, final ResultCallback<News> newsResultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getResultStringFromUrl(getLikeRequest(news.getSourceId(), news.getId(), like), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        LikesResponse.Response likesResponse = GsonHelper.getInstance().getLikesResponseFromJson(result);

                        if (likesResponse != null) {
                            news.getLikes().setUserLikes(like);
                            news.getLikes().setCount(likesResponse.getLikesCount());

                            newsResultCallback.onResult(news);

                            databaseRepositoryHelper.putNewsInDatabaseWithoutAttachments(news);
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
                if (databaseRepositoryHelper.getUserFromDatabase(userId, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getUserRequest(userId), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        User user = GsonHelper.getInstance().getUserFromJson(result);
                        resultCallback.onResult(user);

                        if (user != null) {
                            databaseRepositoryHelper.putUserInDatabase(user);
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
                if (databaseRepositoryHelper.getGroupFromDatabase(groupId, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getGroupRequest(groupId), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        Group group = GsonHelper.getInstance().getGroupFromJson(result);
                        resultCallback.onResult(group);

                        if (group != null) {
                            databaseRepositoryHelper.putGroupInDatabase(group);
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
                if (databaseRepositoryHelper.getFriendsFromDatabase(startPosition, size, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getFriendsRequest(startPosition, size), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        List<User> friends = GsonHelper.getInstance().getFriendsFromJson(result);
                        resultCallback.onResult(friends);

                        if (friends != null) {
                            databaseRepositoryHelper.putFriendListInDatabase(friends);
                            databaseRepositoryHelper.putUserListInDatabase(friends);
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
                if (databaseRepositoryHelper.getNewsFromDatabase(startFrom, size, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getNewsRequest(startFrom, size), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        NewsResponse.Response newsResponse = GsonHelper.getInstance().getNewsFromJson(result);

                        resultCallback.onResult(newsResponse);

                        if (newsResponse != null) {
                            if (newsResponse.getGroupList() != null) {
                                databaseRepositoryHelper.putGroupListInDatabase(newsResponse.getGroupList());
                            }

                            if (newsResponse.getUserList() != null) {
                                databaseRepositoryHelper.putUserListInDatabase(newsResponse.getUserList());
                            }

                            if (newsResponse.getNewsList() != null) {
                                databaseRepositoryHelper.putNewsListInDatabase(newsResponse.getNewsList());
                            }
                        }
                    }
                });
            }
        });
    }

    private void getResultStringFromUrl(final String requestUrl, final ResultCallback<String> resultCallback) {
        try (InputStream inputStream = new URL(requestUrl).openStream();
             ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[Constants.INT_ONE_KB];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                resultOutputStream.write(buffer, 0, length);
            }

            String result = resultOutputStream.toString();
            resultCallback.onResult(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFriendsRequest(int startPosition, int size) {
        return Constants.API_VK.API_VK_GET_FRIENDS_URL +
                Constants.API_VK.FRIENDS_COUNT +
                size +
                Constants.API_VK.FRIENDS_OFFSET +
                startPosition +
                Constants.API_VK.FRIENDS_FIELDS +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getUserRequest(int userId) {
        return Constants.API_VK.API_VK_GET_USER_URL +
                Constants.API_VK.USER_ID +
                userId +
                Constants.API_VK.USER_FIELDS +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getNewsRequest(String startFrom, int size) {
        return Constants.API_VK.API_VK_GET_NEWS_URL +
                Constants.API_VK.NEWS_START_FROM +
                startFrom +
                Constants.API_VK.NEWS_COUNT +
                size +
                Constants.API_VK.NEWS_FIELDS +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getGroupRequest(int userId) {
        return Constants.API_VK.API_VK_GET_GROUP_URL +
                userId +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getLikeRequest(int sourceId, int newsId, boolean like) {
        if (like) {
            return String.format(API_VK_SET_LIKE_POST + accessToken, sourceId, newsId);
        } else {
            return String.format(API_VK_SET_DISLIKE_POST + accessToken, sourceId, newsId);
        }
    }
}
