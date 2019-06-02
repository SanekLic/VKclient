package com.my.vkclient.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.my.vkclient.Constants;
import com.my.vkclient.database.DatabaseHelper;
import com.my.vkclient.database.model.GroupTable;
import com.my.vkclient.database.model.UserTable;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.User;
import com.my.vkclient.utils.ResultCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.my.vkclient.Constants.Database.GROUP_TABLE_NAME;
import static com.my.vkclient.Constants.Database.SELECT_FROM;
import static com.my.vkclient.Constants.Database.STRING_QUESTION;
import static com.my.vkclient.Constants.Database.USER_TABLE_NAME;
import static com.my.vkclient.Constants.Database.WHERE;
import static com.my.vkclient.utils.GsonAdapter.getFriendsFromJson;
import static com.my.vkclient.utils.GsonAdapter.getGroupFromJson;
import static com.my.vkclient.utils.GsonAdapter.getNewsFromJson;
import static com.my.vkclient.utils.GsonAdapter.getUserFromJson;

public class VkRepository {
    private static final Executor executor = Executors.newCachedThreadPool();
    private static DatabaseHelper databaseHelper;
    private static String accessToken;

    public static void initializeDatabaseHelper(@Nullable final Context context, @Nullable final SQLiteDatabase.CursorFactory cursorFactory, final int version) {
        databaseHelper = new DatabaseHelper(context, cursorFactory, version);
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String newAccessToken) {
        accessToken = newAccessToken;
    }

    public static void getUserById(final int userId, final ResultCallback<User> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (getUserFromDatabase(userId, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getUserRequest(userId), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        User user = getUserFromJson(result);
                        resultCallback.onResult(user);

                        if (user != null) {
                            putUserInDatabase(null, user);
                        }
                    }
                });
            }
        });
    }

    private static boolean getUserFromDatabase(final int userId, final ResultCallback<User> resultCallback) {
        final Cursor userCursor = databaseHelper.query(getSelectDatabaseQuery(USER_TABLE_NAME, UserTable.ID), String.valueOf(userId));

        if (userCursor.moveToFirst()) {
            User user = new User();
            user.setId(userCursor.getInt(userCursor.getColumnIndex(UserTable.ID)));
            user.setFirstName(userCursor.getString(userCursor.getColumnIndex(UserTable.FIRST_NAME)));
            user.setLastName(userCursor.getString(userCursor.getColumnIndex(UserTable.LAST_NAME)));
            user.setOnline(userCursor.getInt(userCursor.getColumnIndex(UserTable.ONLINE)));
            user.setPhoto100Url(userCursor.getString(userCursor.getColumnIndex(UserTable.PHOTO_100_URL)));
            user.setPhotoMaxUrl(userCursor.getString(userCursor.getColumnIndex(UserTable.PHOTO_MAX_URL)));
            Parcel parcel = Parcel.obtain();
            byte[] bytes = userCursor.getBlob(userCursor.getColumnIndex(UserTable.CROP_PHOTO));
            parcel.unmarshall(bytes, 0, bytes.length);
            parcel.setDataPosition(0);
            user.setCropPhoto((CropPhoto) parcel.readParcelable(CropPhoto.class.getClassLoader()));
            parcel.recycle();

            resultCallback.onResult(user);

            return true;
        }

        return false;
    }

    public static void getGroupById(final int groupId, final ResultCallback<Group> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                if (getGroupFromDatabase(groupId, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getGroupRequest(groupId), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        Group group = getGroupFromJson(result);
                        resultCallback.onResult(group);

                        if (group != null) {
                            putGroupInDatabase(null, group);
                        }
                    }
                });
            }
        });
    }

    private static boolean getGroupFromDatabase(final int groupId, final ResultCallback<Group> resultCallback) {
        final Cursor groupCursor = databaseHelper.query(getSelectDatabaseQuery(GROUP_TABLE_NAME, GroupTable.ID), String.valueOf(groupId));

        if (groupCursor.moveToFirst()) {
            Group group = new Group();
            group.setId(groupCursor.getInt(groupCursor.getColumnIndex(GroupTable.ID)));
            group.setName(groupCursor.getString(groupCursor.getColumnIndex(GroupTable.NAME)));
            group.setPhoto100(groupCursor.getString(groupCursor.getColumnIndex(GroupTable.PHOTO_100)));

            resultCallback.onResult(group);

            return true;
        }

        return false;
    }

    public static void getFriends(final int startPosition, final int size, final ResultCallback<List<User>> resultCallback) {
        getResultStringFromUrl(getFriendsRequest(startPosition, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                resultCallback.onResult(getFriendsFromJson(result));
            }
        });
    }

    public static void getNews(final String startFrom, final int size, final ResultCallback<NewsResponse.Response> resultCallback) {
        getResultStringFromUrl(getNewsRequest(startFrom, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                NewsResponse.Response news = getNewsFromJson(result);

                if (news != null) {
                    final ContentValues contentValues = new ContentValues();

                    for (Group group : news.getGroupList()) {
                        putGroupInDatabase(contentValues, group);
                    }

                    for (User user : news.getUserList()) {
                        putUserInDatabase(contentValues, user);
                    }
                }

                resultCallback.onResult(news);
            }
        });
    }

    private static void putUserInDatabase(@Nullable final ContentValues customContentValues, final User user) {
        final ContentValues contentValues = customContentValues != null ? customContentValues : new ContentValues();
        contentValues.clear();
        contentValues.put(UserTable.ID, user.getId());
        contentValues.put(UserTable.FIRST_NAME, user.getFirstName());
        contentValues.put(UserTable.LAST_NAME, user.getLastName());
        contentValues.put(UserTable.ONLINE, user.getOnline());
        contentValues.put(UserTable.PHOTO_100_URL, user.getPhoto100Url());
        contentValues.put(UserTable.PHOTO_MAX_URL, user.getPhotoMaxUrl());
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(user.getCropPhoto(), 0);
        contentValues.put(UserTable.CROP_PHOTO, parcel.marshall());
        parcel.recycle();
        databaseHelper.insert(USER_TABLE_NAME, contentValues);
    }

    private static void putGroupInDatabase(@Nullable final ContentValues customContentValues, final Group group) {
        final ContentValues contentValues = customContentValues != null ? customContentValues : new ContentValues();
        contentValues.clear();
        contentValues.put(GroupTable.ID, group.getId());
        contentValues.put(GroupTable.NAME, group.getName());
        contentValues.put(GroupTable.PHOTO_100, group.getPhoto100());
        databaseHelper.insert(GROUP_TABLE_NAME, contentValues);
    }

    private static void getResultStringFromUrl(final String requestUrl, final ResultCallback<String> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(requestUrl);
                    String result = readStream(url.openStream());
                    resultCallback.onResult(result);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static String readStream(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            resultOutputStream.write(buffer, 0, length);
        }

        return resultOutputStream.toString();
    }

    private static String getSelectDatabaseQuery(String userTableName, String id) {
        return new StringBuilder()
                .append(SELECT_FROM)
                .append(userTableName)
                .append(WHERE)
                .append(id)
                .append(STRING_QUESTION).toString();
    }


    private static String getFriendsRequest(int startPosition, int size) {
        return new StringBuilder()
                .append(Constants.API_VK.API_VK_GET_FRIENDS_URL)
                .append(Constants.API_VK.FRIENDS_COUNT)
                .append(size)
                .append(Constants.API_VK.FRIENDS_OFFSET)
                .append(startPosition)
                .append(Constants.API_VK.FRIENDS_FIELDS)
                .append(Constants.API_VK.ACCESS_TOKEN)
                .append(accessToken).toString();
    }

    private static String getUserRequest(int userId) {
        return new StringBuilder()
                .append(Constants.API_VK.API_VK_GET_USER_URL)
                .append(Constants.API_VK.USER_ID)
                .append(userId)
                .append(Constants.API_VK.USER_FIELDS)
                .append(Constants.API_VK.ACCESS_TOKEN)
                .append(accessToken).toString();
    }

    private static String getNewsRequest(String startFrom, int size) {
        return new StringBuilder()
                .append(Constants.API_VK.API_VK_GET_NEWS_URL)
                .append(Constants.API_VK.NEWS_START_FROM)
                .append(startFrom)
                .append(Constants.API_VK.NEWS_COUNT)
                .append(size)
                .append(Constants.API_VK.NEWS_FIELDS)
                .append(Constants.API_VK.ACCESS_TOKEN)
                .append(accessToken).toString();
    }

    private static String getGroupRequest(int userId) {
        return new StringBuilder()
                .append(Constants.API_VK.API_VK_GET_GROUP_URL)
                .append(userId)
                .append(Constants.API_VK.ACCESS_TOKEN)
                .append(accessToken).toString();
    }
}
