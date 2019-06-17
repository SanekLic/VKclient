package com.my.vkclient.repository;

import com.my.vkclient.Constants;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.response.LikeResponse;
import com.my.vkclient.entities.response.NewsResponse;
import com.my.vkclient.entities.response.ProfileEditResponse;
import com.my.vkclient.gson.GsonHelper;
import com.my.vkclient.utils.ResultCallback;
import com.my.vkclient.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static com.my.vkclient.Constants.Http.COUNT_RE_REQUEST;
import static com.my.vkclient.Constants.Http.TIME_TO_RE_REQUEST;

class HttpRepositoryHelper {
    private String accessToken;

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    void setLikeNews(int sourceId, int newsId, boolean like, final ResultCallback<LikeResponse.Response> resultCallback) {
        getResultStringFromUrl(getLikeRequest(sourceId, newsId, like), 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getLikesResponseFromJson(result));
            }
        });
    }

    void setProfileStatus(final String status, final ResultCallback<ProfileEditResponse.Response> resultCallback) {
        getResultStringFromUrl(getEditStatusRequest(status), 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getProfileEditResponseFromJson(result));
            }
        });
    }

    void setProfileHomeTown(final String homeTown, final ResultCallback<ProfileEditResponse.Response> resultCallback) {
        getResultStringFromUrl(getEditHomeTownRequest(homeTown), 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getProfileEditResponseFromJson(result));
            }
        });
    }

    void getUser(final int userId, final ResultCallback<User> resultCallback) {
        String requestUrl = userId == 0 ? getProfileRequest() : getUserRequest(userId);
        getResultStringFromUrl(requestUrl, 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getUserFromJson(result));
            }
        });
    }

    void getGroup(final int groupId, final ResultCallback<Group> resultCallback) {
        getResultStringFromUrl(getGroupRequest(groupId), 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getGroupFromJson(result));
            }
        });
    }

    void getFriends(final int startPosition, final int size, final ResultCallback<List<User>> resultCallback) {
        getResultStringFromUrl(getFriendsRequest(startPosition, size), 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getFriendsFromJson(result));
            }
        });
    }

    void getUserPhotos(final int userId, final int startPosition, final int size, final ResultCallback<List<Photo>> resultCallback) {
        getResultStringFromUrl(getUserPhotosRequest(userId, startPosition, size), 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getUserPhotosFromJson(result));
            }
        });
    }

    void getNews(final String startFrom, final int size, final ResultCallback<NewsResponse.Response> resultCallback) {
        getResultStringFromUrl(getNewsRequest(startFrom, size), 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getNewsFromJson(result));
            }
        });
    }

    void getUserGroups(final int startPosition, final int size, final ResultCallback<List<Group>> resultCallback) {
        getResultStringFromUrl(getUserGroupsRequestRequest(startPosition, size), 0, new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(GsonHelper.getInstance().getUserGroupsFromJson(result));
            }
        });
    }

    private void getResultStringFromUrl(final String requestUrl, int counter, final ResultCallback<String> resultCallback) {
        try (InputStream inputStream = new URL(requestUrl).openStream();
             ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[Constants.INT_ONE_KB];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                resultOutputStream.write(buffer, 0, length);
            }

            String result = resultOutputStream.toString();
            resultCallback.onResult(result);

            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (counter < COUNT_RE_REQUEST) {
            Utils.getInstance().threadSaveSleep(TIME_TO_RE_REQUEST);
            counter++;

            getResultStringFromUrl(requestUrl, counter, resultCallback);
        }
    }

    private String getFriendsRequest(int startPosition, int size) {
        return Constants.API_VK.API_VK_GET_FRIENDS_URL +
                Constants.API_VK.REQUEST_COUNT +
                size +
                Constants.API_VK.REQUEST_OFFSET +
                startPosition +
                Constants.API_VK.FIELDS +
                Constants.API_VK.FRIENDS_FIELDS +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getUserPhotosRequest(int userId, int startPosition, int size) {
        return Constants.API_VK.API_VK_GET_PHOTOS_URL +
                Constants.API_VK.PHOTOS_OWNER_ID +
                userId +
                Constants.API_VK.PHOTOS_OFFSET +
                startPosition +
                Constants.API_VK.PHOTOS_COUNT +
                size +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getProfileRequest() {
        return Constants.API_VK.API_VK_GET_USER_URL +
                Constants.API_VK.FIELDS +
                Constants.API_VK.USER_FIELDS +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getUserRequest(int userId) {
        return Constants.API_VK.API_VK_GET_USER_URL +
                Constants.API_VK.USER_ID +
                userId +
                Constants.API_VK.FIELDS +
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
                Constants.API_VK.FIELDS +
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

    private String getUserGroupsRequestRequest(int startPosition, int size) {
        return Constants.API_VK.API_VK_GET_GROUPS_URL +
                Constants.API_VK.REQUEST_COUNT +
                size +
                Constants.API_VK.REQUEST_OFFSET +
                startPosition +
                Constants.API_VK.FIELDS +
                Constants.API_VK.GROUPS_FIELDS +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getLikeRequest(int ownerId, int itemId, boolean like) {
        String likePost = like ? Constants.API_VK.LIKE_POST : Constants.API_VK.DISLIKE_POST;

        return Constants.API_VK.API_VK_METHOD +
                likePost +
                Constants.API_VK.LIKE_OWNER_ID +
                ownerId +
                Constants.API_VK.LIKE_ITEM_ID +
                itemId +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;
    }

    private String getEditStatusRequest(String status) {
        return Constants.API_VK.API_VK_SAVE_PROFILE +
                Constants.API_VK.SAVE_STATUS +
                status +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;

    }

    private String getEditHomeTownRequest(String homeTown) {
        return Constants.API_VK.API_VK_SAVE_PROFILE +
                Constants.API_VK.SAVE_HOME_TOWN +
                homeTown +
                Constants.API_VK.ACCESS_TOKEN +
                accessToken;

    }
}
