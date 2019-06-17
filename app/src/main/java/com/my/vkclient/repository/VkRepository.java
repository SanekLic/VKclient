package com.my.vkclient.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.response.LikeResponse;
import com.my.vkclient.entities.response.NewsResponse;
import com.my.vkclient.entities.response.ProfileEditResponse;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.my.vkclient.Constants.STRING_EMPTY;

public class VkRepository {
    private static VkRepository instance;
    private String newsStartFrom;
    private Executor executor;
    private boolean offlineAccess;
    private DatabaseRepositoryHelper databaseRepositoryHelper;
    private HttpRepositoryHelper httpRepositoryHelper;
    private List<News> newsList = new ArrayList<>();
    private List<User> friendList = new ArrayList<>();
    private List<Group> userGroupList = new ArrayList<>();
    private List<Photo> userPhotoList = new ArrayList<>();
    private User user;
    private Group group;

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

    public List<Photo> getLastUserPhotoList() {
        return userPhotoList;
    }

    public void refreshUser(int userId) {
        userPhotoList.clear();
        user = null;
        databaseRepositoryHelper.clearUser(userId);
        databaseRepositoryHelper.clearUserPhoto(userId);
    }

    public List<Group> getUserGroupList() {
        return userGroupList;
    }

    public void refreshUserGroups() {
        userGroupList.clear();
        group = null;
        databaseRepositoryHelper.clearUserGroups();
    }

    public boolean isOfflineAccess() {
        return offlineAccess;
    }

    public void initializeContext(@NonNull final Context context) {
        databaseRepositoryHelper = new DatabaseRepositoryHelper(context);

        String newAccessToken = SharedPreferencesHelper.getInstance().getAccessToke();
        newsStartFrom = SharedPreferencesHelper.getInstance().getNewsStartFrom();

        if (newAccessToken != null) {
            httpRepositoryHelper.setAccessToken(newAccessToken);
            offlineAccess = true;
        }
    }

    public void setAccessToken(String newAccessToken) {
        httpRepositoryHelper.setAccessToken(newAccessToken);
        offlineAccess = true;
        SharedPreferencesHelper.getInstance().setAccessToken(newAccessToken);
    }

    public void logout() {
        SharedPreferencesHelper.getInstance().clear();

        databaseRepositoryHelper.clearAllTables();
        clearData();

        offlineAccess = false;
        httpRepositoryHelper.setAccessToken(null);
    }

    public void setLikeToNews(final News news, final boolean like, final ResultCallback<News> newsResultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                httpRepositoryHelper.setLikeNews(news.getSourceId(), news.getId(), like, new ResultCallback<LikeResponse.Response>() {
                    @Override
                    public void onResult(LikeResponse.Response likesResponse) {
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

    public void setStatusToUser(final User editUser, final String status, final ResultCallback<User> newsResultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                httpRepositoryHelper.setProfileStatus(status, new ResultCallback<ProfileEditResponse.Response>() {
                    @Override
                    public void onResult(ProfileEditResponse.Response profileEditResponse) {
                        if (profileEditResponse != null) {
                            editUser.setStatus(status);
                            user = editUser;

                            newsResultCallback.onResult(editUser);

                            databaseRepositoryHelper.putUser(editUser);
                        }
                    }
                });
            }
        });
    }

    public void setHomeTownToUser(final User editUser, final String homeTown, final ResultCallback<User> newsResultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                httpRepositoryHelper.setProfileHomeTown(homeTown, new ResultCallback<ProfileEditResponse.Response>() {
                    @Override
                    public void onResult(ProfileEditResponse.Response profileEditResponse) {
                        if (profileEditResponse != null) {
                            editUser.setHomeTown(homeTown);
                            user = editUser;

                            newsResultCallback.onResult(editUser);

                            databaseRepositoryHelper.putUser(editUser);
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
                if (user != null && user.getId() == userId) {
                    resultCallback.onResult(user);
                } else {
                    userPhotoList.clear();
                    databaseRepositoryHelper.getUser(userId, new ResultCallback<User>() {
                        @Override
                        public void onResult(User result) {
                            if (result != null) {
                                user = result;
                                resultCallback.onResult(result);
                            }

                            httpRepositoryHelper.getUser(userId, new ResultCallback<User>() {
                                @Override
                                public void onResult(User result) {
                                    resultCallback.onResult(result);

                                    if (result != null) {
                                        user = result;
                                        databaseRepositoryHelper.putUser(result);
                                    }
                                }
                            });

                        }
                    });
                }
            }
        });
    }

    public void getUserPhotos(final int userId, final int startPosition, final int size, final ResultCallback<List<Photo>> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseRepositoryHelper.getUserPhotos(userId, startPosition, size, new ResultCallback<List<Photo>>() {
                    @Override
                    public void onResult(List<Photo> result) {
                        if (result != null && result.size() > 0) {
                            userPhotoList.addAll(result);
                            resultCallback.onResult(result);
                        } else {
                            httpRepositoryHelper.getUserPhotos(userId, startPosition, size, new ResultCallback<List<Photo>>() {
                                @Override
                                public void onResult(List<Photo> result) {
                                    resultCallback.onResult(result);

                                    if (result != null) {
                                        userPhotoList.addAll(result);
                                        databaseRepositoryHelper.putUserPhotos(userId, result);
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
                if (group != null && group.getId() == groupId) {
                    resultCallback.onResult(group);
                } else {
                    databaseRepositoryHelper.getGroup(groupId, new ResultCallback<Group>() {
                        @Override
                        public void onResult(Group result) {
                            if (result != null) {
                                group = result;
                                resultCallback.onResult(result);
                            } else {
                                httpRepositoryHelper.getGroup(groupId, new ResultCallback<Group>() {
                                    @Override
                                    public void onResult(Group result) {
                                        resultCallback.onResult(result);

                                        if (result != null) {
                                            group = result;
                                            databaseRepositoryHelper.putGroup(result);
                                        }
                                    }
                                });
                            }

                        }
                    });
                }
            }
        });
    }

    public void getUserGroups(final int startPosition, final int size, final ResultCallback<List<Group>> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseRepositoryHelper.getUserGroups(startPosition, size, new ResultCallback<List<Group>>() {
                    @Override
                    public void onResult(List<Group> result) {
                        if (result != null && result.size() > 0) {
                            addUserGroups(result);
                            resultCallback.onResult(result);
                        } else {
                            httpRepositoryHelper.getUserGroups(startPosition, size, new ResultCallback<List<Group>>() {
                                @Override
                                public void onResult(List<Group> result) {
                                    resultCallback.onResult(result);

                                    if (result != null) {
                                        addUserGroups(result);
                                        databaseRepositoryHelper.putUserGroupList(result);
                                        databaseRepositoryHelper.putGroupList(result);
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
                            addFriends(result);
                            resultCallback.onResult(result);
                        } else {
                            httpRepositoryHelper.getFriends(startPosition, size, new ResultCallback<List<User>>() {
                                @Override
                                public void onResult(List<User> result) {
                                    resultCallback.onResult(result);

                                    if (result != null) {
                                        addFriends(result);
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
                            addNews(result);
                            resultCallback.onResult(result);
                        } else {
                            httpRepositoryHelper.getNews(newsStartFrom, size, new ResultCallback<NewsResponse.Response>() {
                                @Override
                                public void onResult(NewsResponse.Response newsResponse) {
                                    if (newsResponse != null) {
                                        setNewsStartFrom(newsResponse.getNextFrom());
                                        resultCallback.onResult(newsResponse.getNewsList());

                                        if (newsResponse.getNewsList() != null) {
                                            addNews(newsResponse.getNewsList());
                                            databaseRepositoryHelper.putNewsList(newsResponse.getNewsList());
                                        }

                                        if (newsResponse.getGroupList() != null) {
                                            databaseRepositoryHelper.putGroupList(newsResponse.getGroupList());
                                        }

                                        if (newsResponse.getUserList() != null) {
                                            databaseRepositoryHelper.putUserList(newsResponse.getUserList());
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

    private void addNews(List<News> result) {
        boolean isExist;
        for (News newNews : result) {
            isExist = false;

            for (News news : newsList) {
                if (newNews.getId() == news.getId()) {
                    isExist = true;

                    break;
                }
            }

            if (!isExist) {
                newsList.add(newNews);
            }
        }
    }

    private void addFriends(List<User> result) {
        boolean isExist;
        for (User newUser : result) {
            isExist = false;

            for (User user : friendList) {
                if (newUser.getId() == user.getId()) {
                    isExist = true;

                    break;
                }
            }

            if (!isExist) {
                friendList.add(newUser);
            }
        }
    }

    private void addUserGroups(List<Group> result) {
        boolean isExist;
        for (Group newGroup : result) {
            isExist = false;

            for (Group group : userGroupList) {
                if (newGroup.getId() == group.getId()) {
                    isExist = true;

                    break;
                }
            }

            if (!isExist) {
                userGroupList.add(newGroup);
            }
        }
    }

    private void setNewsStartFrom(String newsNextFrom) {
        newsStartFrom = newsNextFrom;

        SharedPreferencesHelper.getInstance().setNewsStartFrom(newsStartFrom);
    }

    private void clearData() {
        newsList.clear();
        friendList.clear();
        userGroupList.clear();
        userPhotoList.clear();
        user = null;
        group = null;

        newsStartFrom = STRING_EMPTY;
    }
}
