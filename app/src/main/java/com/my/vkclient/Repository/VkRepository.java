package com.my.vkclient.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.my.vkclient.entities.Comments;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.Likes;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.Reposts;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.Views;
import com.my.vkclient.entities.VkDate;
import com.my.vkclient.utils.ResultCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import static com.my.vkclient.Constants.STRING_SLASH;
import static com.my.vkclient.utils.GsonAdapter.getFriendsFromJson;
import static com.my.vkclient.utils.GsonAdapter.getGroupFromJson;
import static com.my.vkclient.utils.GsonAdapter.getNewsFromJson;
import static com.my.vkclient.utils.GsonAdapter.getUserFromJson;

//TODO rename to database
public class VkRepository {
    private static final Executor executor = Executors.newCachedThreadPool();
    //TODO use singletone pattern
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
                            putUserInDatabase(user, null);
                        }
                    }
                });
            }
        });
    }

    private static boolean getUserFromDatabase(final int userId, final ResultCallback<User> resultCallback) {
        //TODO close cursor
        //TODO try to implement cache expiration time
        final Cursor userCursor = databaseHelper.query(getSelectDatabaseQuery(USER_TABLE_NAME, UserTable.ID), String.valueOf(userId));

        if (userCursor.moveToFirst()) {
            User user = new User();
            user.setId(userCursor.getInt(userCursor.getColumnIndex(UserTable.ID)));
            user.setFirstName(userCursor.getString(userCursor.getColumnIndex(UserTable.FIRST_NAME)));
            user.setLastName(userCursor.getString(userCursor.getColumnIndex(UserTable.LAST_NAME)));
            user.setOnline(userCursor.getInt(userCursor.getColumnIndex(UserTable.ONLINE)));
            user.setPhoto100Url(userCursor.getString(userCursor.getColumnIndex(UserTable.PHOTO_100_URL)));
            user.setPhotoMaxUrl(userCursor.getString(userCursor.getColumnIndex(UserTable.PHOTO_MAX_URL)));

            //TODO remove this logic. Save cropped image
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

    private static void putUserInDatabase(final User user, @Nullable final ContentValues customContentValues) {
        final ContentValues contentValues = customContentValues != null ? customContentValues : new ContentValues();
        //TODO remove clear call for all contentValues if not required
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
                            putGroupInDatabase(group, null);
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

    private static void putGroupInDatabase(final Group group, @Nullable final ContentValues customContentValues) {
        final ContentValues contentValues = customContentValues != null ? customContentValues : new ContentValues();
        contentValues.clear();
        contentValues.put(GroupTable.ID, group.getId());
        contentValues.put(GroupTable.NAME, group.getName());
        contentValues.put(GroupTable.PHOTO_100, group.getPhoto100());
        databaseHelper.insert(GROUP_TABLE_NAME, contentValues);
    }

    public static void getFriends(final int startPosition, final int size, final ResultCallback<List<User>> resultCallback) {
        if (getFriendsFromDatabase(startPosition, size, resultCallback)) {
            return;
        }

        getResultStringFromUrl(getFriendsRequest(startPosition, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                List<User> friends = getFriendsFromJson(result);
                resultCallback.onResult(friends);

                if (friends != null) {
                    final ContentValues contentValues = new ContentValues();

                    ///TODo start transaction
                    for (User user : friends) {
                        putFriendInDatabase(user, contentValues);
                        putUserInDatabase(user, contentValues);
                    }

                    //TODO finish transaction
                }
            }
        });
    }

    private static boolean getFriendsFromDatabase(int startPosition, int size, ResultCallback<List<User>> resultCallback) {
        final Cursor friendsCursor = databaseHelper.query(getFriendsJoinUserDatabaseQuery(startPosition, size));

        //TODO possible NPE
        if (friendsCursor.getCount() > 0) {
            List<User> friendList = new ArrayList<>();

            while (friendsCursor.moveToNext()) {
                User user = new User();
                //TODo calculate index once
                user.setId(friendsCursor.getInt(friendsCursor.getColumnIndex(UserTable.ID)));
                user.setFirstName(friendsCursor.getString(friendsCursor.getColumnIndex(UserTable.FIRST_NAME)));
                user.setLastName(friendsCursor.getString(friendsCursor.getColumnIndex(UserTable.LAST_NAME)));
                user.setOnline(friendsCursor.getInt(friendsCursor.getColumnIndex(UserTable.ONLINE)));
                user.setPhoto100Url(friendsCursor.getString(friendsCursor.getColumnIndex(UserTable.PHOTO_100_URL)));
                user.setPhotoMaxUrl(friendsCursor.getString(friendsCursor.getColumnIndex(UserTable.PHOTO_MAX_URL)));


                Parcel parcel = Parcel.obtain();
                byte[] bytes = friendsCursor.getBlob(friendsCursor.getColumnIndex(UserTable.CROP_PHOTO));
                parcel.unmarshall(bytes, 0, bytes.length);
                parcel.setDataPosition(0);
                user.setCropPhoto((CropPhoto) parcel.readParcelable(CropPhoto.class.getClassLoader()));
                parcel.recycle();

                friendList.add(user);
            }

            resultCallback.onResult(friendList);

            return true;
        }

        return false;
    }

    private static void putFriendInDatabase(final User friend, @NonNull final ContentValues contentValues) {
        contentValues.clear();
        contentValues.put(FriendTable.USER_ID, friend.getId());
        contentValues.put(FriendTable.FRIEND_LAST_UPDATE, Calendar.getInstance().getTime().getTime());
        databaseHelper.insert(FRIEND_TABLE_NAME, contentValues);
    }

    public static void getNews(final String startFrom, final int size, final ResultCallback<NewsResponse.Response> resultCallback) {
//        int startPosition = 0;
//
//        if (!startFrom.isEmpty() && startFrom.contains(STRING_SLASH)) {
//            startPosition = Integer.parseInt(startFrom.substring(0, startFrom.indexOf(STRING_SLASH)));
//        }
//
//        final Cursor newsCursor = databaseHelper.query(getLimitDatabaseQuery(NEWS_TABLE_NAME, startPosition, size));
//
//        if (newsCursor.getCount() > 0) {
//            List<News> newsList = new ArrayList<>();
//
//            while (newsCursor.moveToNext()) {
//                News news = new News();
//                news.setType(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.TYPE)));
//                news.setSourceId(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.SOURCE_ID)));
//                news.setFromId(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.FROM_ID)));
//                news.setDate(new VkDate(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.DATE))));
//                news.setText(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.TEXT)));
//
//                Parcel parcel = Parcel.obtain();
//                byte[] bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.COPY_HISTORY));
//                if (bytes != null) {
//                    parcel.unmarshall(bytes, 0, bytes.length);
//                    parcel.setDataPosition(0);
//                    try {
//                        news.setCopyHistory(Arrays.asList((News[]) parcel.readParcelableArray(News.class.getClassLoader())));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                parcel.recycle();
//
//                parcel = Parcel.obtain();
//                bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.COMMENTS));
//                parcel.unmarshall(bytes, 0, bytes.length);
//                parcel.setDataPosition(0);
//                news.setComments((Comments) parcel.readParcelable(Comments.class.getClassLoader()));
//                parcel.recycle();
//
//                parcel = Parcel.obtain();
//                bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.LIKES));
//                parcel.unmarshall(bytes, 0, bytes.length);
//                parcel.setDataPosition(0);
//                news.setLikes((Likes) parcel.readParcelable(Likes.class.getClassLoader()));
//                parcel.recycle();
//
//                parcel = Parcel.obtain();
//                bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.REPOSTS));
//                parcel.unmarshall(bytes, 0, bytes.length);
//                parcel.setDataPosition(0);
//                news.setReposts((Reposts) parcel.readParcelable(Reposts.class.getClassLoader()));
//                parcel.recycle();
//
//                parcel = Parcel.obtain();
//                bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.VIEWS));
//                parcel.unmarshall(bytes, 0, bytes.length);
//                parcel.setDataPosition(0);
//                news.setViews((Views) parcel.readParcelable(Views.class.getClassLoader()));
//                parcel.recycle();
//
//                parcel = Parcel.obtain();
//                bytes = newsCursor.getBlob(newsCursor.getColumnIndex(NewsTable.ATTACHMENTS));
//                if (bytes != null) {
//                    parcel.unmarshall(bytes, 0, bytes.length);
//                    parcel.setDataPosition(0);
//                    try {
//                        news.setAttachments(Arrays.asList((Attachment[]) parcel.readParcelableArray(Attachment.class.getClassLoader())));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                parcel.recycle();
//
//                newsList.add(news);
//            }
//
//            NewsResponse newsResponse = new NewsResponse();
//
//            newsResponse.getResponse().setNewsList(newsList);
//            newsResponse.getResponse().setNextFrom(startPosition + newsList.size() + STRING_SLASH);
//
//            resultCallback.onResult(newsResponse.getResponse());
//
//            return;
//        }

        getResultStringFromUrl(getNewsRequest(startFrom, size), new ResultCallback<String>() {
            @Override
            public void onResult(String result) {
                NewsResponse.Response news = getNewsFromJson(result);

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

    private static void putNewsInDatabase(final News news, @NonNull final ContentValues contentValues) {
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


    //TODO move to HttpClient/Network class
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

    //TOVO move to SQL query class
    private static String getLimitDatabaseQuery(String tableName, int startPosition, int size) {
        return new StringBuilder()
                .append(SELECT_FROM)
                .append(tableName)
                .append(DATABASE_LIMIT)
                .append(startPosition)
                .append(STRING_COMMA)
                .append(size).toString();
    }

    private static String getFriendsJoinUserDatabaseQuery(int startPosition, int size) {
        return String.format(SELECT_FROM + FRIEND_TABLE_NAME + DATABASE_JOIN_LIMIT, USER_TABLE_NAME,
                FriendTable.USER_ID, UserTable.ID, UserTable.FIRST_NAME, UserTable.LAST_NAME, startPosition, size);
    }

    //TODO move to API class
    private static String getSelectDatabaseQuery(String tableName, String param) {
        return new StringBuilder()
                .append(SELECT_FROM)
                .append(tableName)
                .append(DATABASE_WHERE)
                .append(param)
                .append(STRING_EQUALS)
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
