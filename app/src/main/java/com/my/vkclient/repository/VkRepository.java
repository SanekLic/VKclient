package com.my.vkclient.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.my.vkclient.Constants;
import com.my.vkclient.database.DatabaseHelper;
import com.my.vkclient.database.model.FriendTable;
import com.my.vkclient.database.model.GroupTable;
import com.my.vkclient.database.model.NewsTable;
import com.my.vkclient.database.model.UserTable;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.User;
import com.my.vkclient.utils.GsonAdapter;
import com.my.vkclient.utils.ResultCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.my.vkclient.Constants.Database.DATABASE_JOIN_LIMIT;
import static com.my.vkclient.Constants.Database.DATABASE_LIMIT;
import static com.my.vkclient.Constants.Database.DATABASE_WHERE;
import static com.my.vkclient.Constants.Database.FRIEND_TABLE_NAME;
import static com.my.vkclient.Constants.Database.GROUP_TABLE_NAME;
import static com.my.vkclient.Constants.Database.NEWS_TABLE_NAME;
import static com.my.vkclient.Constants.Database.SELECT_FROM;
import static com.my.vkclient.Constants.Database.USER_TABLE_NAME;
import static com.my.vkclient.Constants.STRING_COMMA;
import static com.my.vkclient.Constants.STRING_EQUALS;
import static com.my.vkclient.Constants.STRING_QUESTION;

public class VkRepository {
    private static VkRepository instance;
    private Executor executor;
    private DatabaseHelper databaseHelper;
    private String accessToken;

    private VkRepository() {
        executor = Executors.newCachedThreadPool();
    }

    public static VkRepository getInstance() {
        if (instance == null) {
            instance = new VkRepository();
        }

        return instance;
    }

    public void initialContext(@NonNull final Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String newAccessToken) {
        accessToken = newAccessToken;
    }

    public void getUserById(final int userId, final ResultCallback<User> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (getUserFromDatabase(userId, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getUserRequest(userId), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        User user = GsonAdapter.getInstance().getUserFromJson(result);
                        resultCallback.onResult(user);

                        if (user != null) {
                            putUserInDatabase(user, null);
                        }
                    }
                });
            }
        });
    }

    private boolean getUserFromDatabase(final int userId, final ResultCallback<User> resultCallback) {
        try (Cursor userCursor = databaseHelper.query(getSelectDatabaseQuery(USER_TABLE_NAME, UserTable.ID), String.valueOf(userId))) {
            if (userCursor.moveToFirst()) {
                resultCallback.onResult(getUserFromCursor(userCursor));

                return true;
            }

            return false;
        }
    }

    private void putUserInDatabase(final User user, @Nullable final ContentValues customContentValues) {
        final ContentValues contentValues = customContentValues != null ? customContentValues : new ContentValues();
        contentValues.clear();
        contentValues.put(UserTable.ID, user.getId());
        contentValues.put(UserTable.FIRST_NAME, user.getFirstName());
        contentValues.put(UserTable.LAST_NAME, user.getLastName());
        contentValues.put(UserTable.ONLINE, user.getOnline());
        contentValues.put(UserTable.PHOTO_100_URL, user.getPhoto100Url());
        contentValues.put(UserTable.PHOTO_MAX_URL, user.getPhotoMaxUrl());

        if (user.getCropPhoto() != null) {
            contentValues.put(UserTable.CROP_PHOTO_URL, user.getCropPhoto().getCropPhotoUrl());
            contentValues.put(UserTable.CROP_PHOTO_HEIGHT, user.getCropPhoto().getCropPhotoHeight());
            contentValues.put(UserTable.CROP_PHOTO_WIDTH, user.getCropPhoto().getCropPhotoWidth());
            contentValues.put(UserTable.CROP_RECT_X, user.getCropPhoto().getCropRectX());
            contentValues.put(UserTable.CROP_RECT_X_2, user.getCropPhoto().getCropRectX2());
            contentValues.put(UserTable.CROP_RECT_Y, user.getCropPhoto().getCropRectY());
            contentValues.put(UserTable.CROP_RECT_Y_2, user.getCropPhoto().getCropRectY2());
        }

        databaseHelper.insert(USER_TABLE_NAME, contentValues);
    }

    public void getGroupById(final int groupId, final ResultCallback<Group> resultCallback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                if (getGroupFromDatabase(groupId, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getGroupRequest(groupId), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        Group group = GsonAdapter.getInstance().getGroupFromJson(result);
                        resultCallback.onResult(group);

                        if (group != null) {
                            putGroupInDatabase(group, null);
                        }
                    }
                });
            }
        });
    }

    private boolean getGroupFromDatabase(final int groupId, final ResultCallback<Group> resultCallback) {
        try (Cursor groupCursor = databaseHelper.query(getSelectDatabaseQuery(GROUP_TABLE_NAME, GroupTable.ID), String.valueOf(groupId))) {
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
    }

    private void putGroupInDatabase(final Group group, @Nullable final ContentValues customContentValues) {
        final ContentValues contentValues = customContentValues != null ? customContentValues : new ContentValues();
        contentValues.clear();
        contentValues.put(GroupTable.ID, group.getId());
        contentValues.put(GroupTable.NAME, group.getName());
        contentValues.put(GroupTable.PHOTO_100, group.getPhoto100());
        databaseHelper.insert(GROUP_TABLE_NAME, contentValues);
    }

    public void getFriends(final int startPosition, final int size, final ResultCallback<List<User>> resultCallback) {
        if (getFriendsFromDatabase(startPosition, size, resultCallback)) {
            return;
        }

        getResultStringFromUrl(getFriendsRequest(startPosition, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                List<User> friends = GsonAdapter.getInstance().getFriendsFromJson(result);
                resultCallback.onResult(friends);

                if (friends != null) {
                    final ContentValues contentValues = new ContentValues();

                    for (User user : friends) {
                        putFriendInDatabase(user, contentValues);
                        putUserInDatabase(user, contentValues);
                    }
                }
            }
        });
    }

    private boolean getFriendsFromDatabase(int startPosition, int size, ResultCallback<List<User>> resultCallback) {
        try (Cursor friendsCursor = databaseHelper.query(getFriendsJoinUserDatabaseQuery(startPosition, size))) {

            if (friendsCursor.getCount() > 0) {
                List<User> friendList = new ArrayList<>();

                while (friendsCursor.moveToNext()) {
                    friendList.add(getUserFromCursor(friendsCursor));
                }

                resultCallback.onResult(friendList);

                return true;
            }

            return false;
        }
    }

    private User getUserFromCursor(Cursor userCursor) {
        User user = new User();
        user.setId(userCursor.getInt(userCursor.getColumnIndex(UserTable.ID)));
        user.setFirstName(userCursor.getString(userCursor.getColumnIndex(UserTable.FIRST_NAME)));
        user.setLastName(userCursor.getString(userCursor.getColumnIndex(UserTable.LAST_NAME)));
        user.setOnline(userCursor.getInt(userCursor.getColumnIndex(UserTable.ONLINE)) > 0);
        user.setPhoto100Url(userCursor.getString(userCursor.getColumnIndex(UserTable.PHOTO_100_URL)));
        user.setPhotoMaxUrl(userCursor.getString(userCursor.getColumnIndex(UserTable.PHOTO_MAX_URL)));
        user.setCropPhoto(new CropPhoto());
        user.getCropPhoto().setCropPhotoUrl(userCursor.getString(userCursor.getColumnIndex(UserTable.CROP_PHOTO_URL)));
        user.getCropPhoto().setCropPhotoHeight(userCursor.getInt(userCursor.getColumnIndex(UserTable.CROP_PHOTO_HEIGHT)));
        user.getCropPhoto().setCropPhotoWidth(userCursor.getInt(userCursor.getColumnIndex(UserTable.CROP_PHOTO_WIDTH)));
        user.getCropPhoto().setCropRectX(userCursor.getFloat(userCursor.getColumnIndex(UserTable.CROP_RECT_X)));
        user.getCropPhoto().setCropRectX2(userCursor.getFloat(userCursor.getColumnIndex(UserTable.CROP_RECT_X_2)));
        user.getCropPhoto().setCropRectY(userCursor.getFloat(userCursor.getColumnIndex(UserTable.CROP_RECT_Y)));
        user.getCropPhoto().setCropRectY2(userCursor.getFloat(userCursor.getColumnIndex(UserTable.CROP_RECT_Y_2)));

        return user;
    }

    private void putFriendInDatabase(final User friend, @NonNull final ContentValues contentValues) {
        contentValues.clear();
        contentValues.put(FriendTable.USER_ID, friend.getId());
        contentValues.put(FriendTable.FRIEND_LAST_UPDATE, Calendar.getInstance().getTime().getTime());
        databaseHelper.insert(FRIEND_TABLE_NAME, contentValues);
    }

    public void getNews(final String startFrom, final int size, final ResultCallback<NewsResponse.Response> resultCallback) {
//        int startPosition = 0;
//
//        if (!startFrom.isEmpty() && startFrom.contains(STRING_SLASH)) {
//            startPosition = Integer.parseInt(startFrom.substring(0, startFrom.indexOf(STRING_SLASH)));
//        }
//
//        try (Cursor newsCursor = databaseHelper.query(getLimitDatabaseQuery(NEWS_TABLE_NAME, startPosition, size))) {
//            if (newsCursor.getCount() > 0) {
//                List<News> newsList = new ArrayList<>();
//
//                while (newsCursor.moveToNext()) {
//                    News news = new News();
//                    news.setType(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.TYPE)));
//                    news.setSourceId(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.SOURCE_ID)));
//                    news.setFromId(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.FROM_ID)));
//                    news.setDate(new VkDate(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.DATE))));
//                    news.setText(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.TEXT)));
//
//                    Parcel parcel = Parcel.obtain();
//                    byte[] bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.COPY_HISTORY));
//                    if (bytes != null) {
//                        parcel.unmarshall(bytes, 0, bytes.length);
//                        parcel.setDataPosition(0);
//                        try {
//                            news.setCopyHistory(Arrays.asList((News[]) parcel.readParcelableArray(News.class.getClassLoader())));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    parcel.recycle();
//
//                    parcel = Parcel.obtain();
//                    bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.COMMENTS));
//                    parcel.unmarshall(bytes, 0, bytes.length);
//                    parcel.setDataPosition(0);
//                    news.setComments((Comments) parcel.readParcelable(Comments.class.getClassLoader()));
//                    parcel.recycle();
//
//                    parcel = Parcel.obtain();
//                    bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.LIKES));
//                    parcel.unmarshall(bytes, 0, bytes.length);
//                    parcel.setDataPosition(0);
//                    news.setLikes((Likes) parcel.readParcelable(Likes.class.getClassLoader()));
//                    parcel.recycle();
//
//                    parcel = Parcel.obtain();
//                    bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.REPOSTS));
//                    parcel.unmarshall(bytes, 0, bytes.length);
//                    parcel.setDataPosition(0);
//                    news.setReposts((Reposts) parcel.readParcelable(Reposts.class.getClassLoader()));
//                    parcel.recycle();
//
//                    parcel = Parcel.obtain();
//                    bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.VIEWS));
//                    parcel.unmarshall(bytes, 0, bytes.length);
//                    parcel.setDataPosition(0);
//                    news.setViews((Views) parcel.readParcelable(Views.class.getClassLoader()));
//                    parcel.recycle();
//
//                    parcel = Parcel.obtain();
//                    bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.ATTACHMENTS));
//                    if (bytes != null) {
//                        parcel.unmarshall(bytes, 0, bytes.length);
//                        parcel.setDataPosition(0);
//                        try {
//                            news.setAttachments(Arrays.asList((Attachment[]) parcel.readParcelableArray(Attachment.class.getClassLoader())));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    parcel.recycle();
//
//                    newsList.add(news);
//                }
//
//                NewsResponse newsResponse = new NewsResponse();
//
//                newsResponse.getResponse().setNewsList(newsList);
//                newsResponse.getResponse().setNextFrom(startPosition + newsList.size() + STRING_SLASH);
//
//                resultCallback.onResult(newsResponse.getResponse());
//
//                return;
//            }
//        }

        getResultStringFromUrl(getNewsRequest(startFrom, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                NewsResponse.Response news = GsonAdapter.getInstance().getNewsFromJson(result);

                if (news != null) {
                    final ContentValues contentValues = new ContentValues();

                    for (Group group : news.getGroupList()) {
                        putGroupInDatabase(group, contentValues);
                    }

                    for (User user : news.getUserList()) {
                        putUserInDatabase(user, contentValues);
                    }

                    for (News putNews : news.getNewsList()) {
                        putNewsInDatabase(putNews, contentValues);
                    }
                }

                resultCallback.onResult(news);
            }
        });
    }

    private void putNewsInDatabase(final News news, @NonNull final ContentValues contentValues) {
        contentValues.clear();
        contentValues.put(NewsTable.TYPE, news.getType());
        contentValues.put(NewsTable.SOURCE_ID, news.getSourceId());
        contentValues.put(NewsTable.FROM_ID, news.getFromId());
        contentValues.put(NewsTable.DATE, news.getDate().toString());
        contentValues.put(NewsTable.TEXT, news.getText());
        Parcel parcel;

        if (news.getCopyHistory() != null) {
            parcel = Parcel.obtain();
            parcel.writeParcelableArray(news.getCopyHistory().toArray(new News[0]), 0);
            contentValues.put(NewsTable.COPY_HISTORY, parcel.marshall());
            parcel.recycle();
        }

        parcel = Parcel.obtain();
        parcel.writeParcelable(news.getComments(), 0);
        contentValues.put(NewsTable.COMMENTS, parcel.marshall());
        parcel.recycle();

        parcel = Parcel.obtain();
        parcel.writeParcelable(news.getLikes(), 0);
        contentValues.put(NewsTable.LIKES, parcel.marshall());
        parcel.recycle();

        parcel = Parcel.obtain();
        parcel.writeParcelable(news.getReposts(), 0);
        contentValues.put(NewsTable.REPOSTS, parcel.marshall());
        parcel.recycle();

        parcel = Parcel.obtain();
        parcel.writeParcelable(news.getViews(), 0);
        contentValues.put(NewsTable.VIEWS, parcel.marshall());
        parcel.recycle();

        if (news.getAttachments() != null) {
            parcel = Parcel.obtain();
            parcel.writeParcelableArray(news.getAttachments().toArray(new Attachment[0]), 0);
            contentValues.put(NewsTable.ATTACHMENTS, parcel.marshall());
            parcel.recycle();
        }

        databaseHelper.insert(NEWS_TABLE_NAME, contentValues);
    }

    private void getResultStringFromUrl(final String requestUrl, final ResultCallback<String> resultCallback) {
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

    private String readStream(final InputStream inputStream) throws IOException {
        ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            resultOutputStream.write(buffer, 0, length);
        }

        return resultOutputStream.toString();
    }

    private String getLimitDatabaseQuery(String tableName, int startPosition, int size) {
        return new StringBuilder()
                .append(SELECT_FROM)
                .append(tableName)
                .append(DATABASE_LIMIT)
                .append(startPosition)
                .append(STRING_COMMA)
                .append(size).toString();
    }

    private String getFriendsJoinUserDatabaseQuery(int startPosition, int size) {
        return String.format(SELECT_FROM + FRIEND_TABLE_NAME + DATABASE_JOIN_LIMIT, USER_TABLE_NAME,
                FriendTable.USER_ID, UserTable.ID, UserTable.FIRST_NAME, UserTable.LAST_NAME, startPosition, size);
    }

    private String getSelectDatabaseQuery(String tableName, String param) {
        return new StringBuilder()
                .append(SELECT_FROM)
                .append(tableName)
                .append(DATABASE_WHERE)
                .append(param)
                .append(STRING_EQUALS)
                .append(STRING_QUESTION).toString();
    }

    private String getFriendsRequest(int startPosition, int size) {
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

    private String getUserRequest(int userId) {
        return new StringBuilder()
                .append(Constants.API_VK.API_VK_GET_USER_URL)
                .append(Constants.API_VK.USER_ID)
                .append(userId)
                .append(Constants.API_VK.USER_FIELDS)
                .append(Constants.API_VK.ACCESS_TOKEN)
                .append(accessToken).toString();
    }

    private String getNewsRequest(String startFrom, int size) {
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

    private String getGroupRequest(int userId) {
        return new StringBuilder()
                .append(Constants.API_VK.API_VK_GET_GROUP_URL)
                .append(userId)
                .append(Constants.API_VK.ACCESS_TOKEN)
                .append(accessToken).toString();
    }
}
