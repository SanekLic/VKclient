package com.my.vkclient.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.my.vkclient.Constants;
import com.my.vkclient.database.DatabaseHelper;
import com.my.vkclient.database.model.AttachmentTable;
import com.my.vkclient.database.model.FriendTable;
import com.my.vkclient.database.model.GroupTable;
import com.my.vkclient.database.model.NewsTable;
import com.my.vkclient.database.model.UserTable;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.Audio;
import com.my.vkclient.entities.Comments;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Doc;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.Likes;
import com.my.vkclient.entities.Link;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.NewsResponse;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.Podcast;
import com.my.vkclient.entities.Reposts;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.Video;
import com.my.vkclient.entities.Views;
import com.my.vkclient.entities.VkDate;
import com.my.vkclient.gson.GsonAdapter;
import com.my.vkclient.utils.ResultCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.my.vkclient.Constants.Database.ATTACHMENT_TABLE_NAME;
import static com.my.vkclient.Constants.Database.DATABASE_JOIN;
import static com.my.vkclient.Constants.Database.DATABASE_LIMIT;
import static com.my.vkclient.Constants.Database.DATABASE_WHERE;
import static com.my.vkclient.Constants.Database.FRIEND_TABLE_NAME;
import static com.my.vkclient.Constants.Database.GROUP_TABLE_NAME;
import static com.my.vkclient.Constants.Database.NEWS_TABLE_NAME;
import static com.my.vkclient.Constants.Database.SELECT_FROM;
import static com.my.vkclient.Constants.Database.USER_TABLE_NAME;
import static com.my.vkclient.Constants.INT_ZERO;
import static com.my.vkclient.Constants.STRING_EQUALS;
import static com.my.vkclient.Constants.STRING_SLASH;

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

    public void getUser(final int userId, final ResultCallback<User> resultCallback) {
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
                            putUserInDatabase(user);
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

                if (getGroupFromDatabase(groupId, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getGroupRequest(groupId), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        Group group = GsonAdapter.getInstance().getGroupFromJson(result);
                        resultCallback.onResult(group);

                        if (group != null) {
                            putGroupInDatabase(group);
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
                if (getFriendsFromDatabase(startPosition, size, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getFriendsRequest(startPosition, size), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        List<User> friends = GsonAdapter.getInstance().getFriendsFromJson(result);
                        resultCallback.onResult(friends);

                        if (friends != null) {
                            for (User user : friends) {
                                putFriendInDatabase(user);
                                putUserInDatabase(user);
                            }
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
                if (getNewsFromDatabase(startFrom, size, resultCallback)) {
                    return;
                }

                getResultStringFromUrl(getNewsRequest(startFrom, size), new ResultCallback<String>() {
                    @Override
                    public void onResult(String result) {
                        NewsResponse.Response news = GsonAdapter.getInstance().getNewsFromJson(result);

                        if (news != null) {
                            for (Group group : news.getGroupList()) {
                                putGroupInDatabase(group);
                            }

                            for (User user : news.getUserList()) {
                                putUserInDatabase(user);
                            }

                            for (News putNews : news.getNewsList()) {
                                putNewsInDatabase(putNews);
                            }
                        }

                        resultCallback.onResult(news);
                    }
                });
            }
        });
    }

    private boolean getUserFromDatabase(final int userId, final ResultCallback<User> resultCallback) {
        try (Cursor userCursor = databaseHelper.query(getSelectDatabaseQuery(USER_TABLE_NAME, UserTable.ID, STRING_EQUALS, String.valueOf(userId)))) {
            if (userCursor.moveToFirst()) {
                resultCallback.onResult(getUserFromCursor(userCursor));

                return true;
            }

            return false;
        }
    }

    private void putUserInDatabase(final User user) {
        ContentValues contentValues = new ContentValues();
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

    private boolean getGroupFromDatabase(final int groupId, final ResultCallback<Group> resultCallback) {
        try (Cursor groupCursor = databaseHelper.query(getSelectDatabaseQuery(GROUP_TABLE_NAME, GroupTable.ID, STRING_EQUALS, String.valueOf(groupId)))) {
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

    private void putGroupInDatabase(final Group group) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GroupTable.ID, group.getId());
        contentValues.put(GroupTable.NAME, group.getName());
        contentValues.put(GroupTable.PHOTO_100, group.getPhoto100());
        databaseHelper.insert(GROUP_TABLE_NAME, contentValues);
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
        String cropPhotoUrl = userCursor.getString(userCursor.getColumnIndex(UserTable.CROP_PHOTO_URL));

        if (cropPhotoUrl != null) {
            user.setCropPhoto(new CropPhoto());
            user.getCropPhoto().setCropPhotoUrl(cropPhotoUrl);
            user.getCropPhoto().setCropPhotoHeight(userCursor.getInt(userCursor.getColumnIndex(UserTable.CROP_PHOTO_HEIGHT)));
            user.getCropPhoto().setCropPhotoWidth(userCursor.getInt(userCursor.getColumnIndex(UserTable.CROP_PHOTO_WIDTH)));
            user.getCropPhoto().setCropRectX(userCursor.getFloat(userCursor.getColumnIndex(UserTable.CROP_RECT_X)));
            user.getCropPhoto().setCropRectX2(userCursor.getFloat(userCursor.getColumnIndex(UserTable.CROP_RECT_X_2)));
            user.getCropPhoto().setCropRectY(userCursor.getFloat(userCursor.getColumnIndex(UserTable.CROP_RECT_Y)));
            user.getCropPhoto().setCropRectY2(userCursor.getFloat(userCursor.getColumnIndex(UserTable.CROP_RECT_Y_2)));
        }

        return user;
    }

    private void putFriendInDatabase(final User friend) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FriendTable.USER_ID, friend.getId());
        contentValues.put(FriendTable.FRIEND_LAST_UPDATE, Calendar.getInstance().getTime().getTime());
        databaseHelper.insert(FRIEND_TABLE_NAME, contentValues);
    }

    private boolean getNewsFromDatabase(String startFrom, int size, ResultCallback<NewsResponse.Response> resultCallback) {
        int startPosition = 0;

        if (!startFrom.isEmpty() && startFrom.contains(STRING_SLASH)) {
            startPosition = Integer.parseInt(startFrom.substring(0, startFrom.indexOf(STRING_SLASH)));
        }

        try (Cursor newsCursor = databaseHelper.query(getNewsLimitDatabaseQuery(startPosition, size))) {
            if (newsCursor.getCount() > 0) {
                List<News> newsList = new ArrayList<>();

                while (newsCursor.moveToNext()) {
                    News news = getNewsFromCursor(newsCursor);

                    int copyNewsId = newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.COPY_NEWS_ID));
                    if (copyNewsId != 0) {
                        try (Cursor copyNewsCursor = databaseHelper.query(getSelectDatabaseQuery(NEWS_TABLE_NAME, NewsTable.ID, STRING_EQUALS, String.valueOf(copyNewsId)))) {
                            if (copyNewsCursor.moveToFirst()) {
                                news.setCopyHistory(Collections.singletonList(getNewsFromCursor(copyNewsCursor)));
                            }
                        }
                    }

                    newsList.add(news);
                }

                NewsResponse newsResponse = new NewsResponse();

                newsResponse.getResponse().setNewsList(newsList);
                newsResponse.getResponse().setNextFrom((startPosition + newsList.size()) + STRING_SLASH);

                resultCallback.onResult(newsResponse.getResponse());

                return true;
            }
        }
        return false;
    }

    private Attachment getAttachmentFromCursor(Cursor attachmentCursor) {
        Attachment attachment = new Attachment();
        attachment.setType(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.TYPE)));

        if (Attachment.Type.Photo.equals(attachment.getType())) {
            Photo photo = new Photo();
            photo.setUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.PHOTO_URL)));
            photo.setHeight(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.PHOTO_HEIGHT)));
            photo.setWidth(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.PHOTO_WIDTH)));
            attachment.setPhoto(photo);
        } else if (Attachment.Type.Video.equals(attachment.getType())) {
            Video video = new Video();
            video.setTitle(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.VIDEO_TITLE)));
            video.setPhotoUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.VIDEO_PHOTO_URL)));
            video.setPhotoHeight(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.VIDEO_PHOTO_HEIGHT)));
            video.setPhotoWidth(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.VIDEO_PHOTO_WIDTH)));
            attachment.setVideo(video);
        } else if (Attachment.Type.Doc.equals(attachment.getType())) {
            Doc doc = new Doc();
            doc.setUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.DOC_URL)));
            doc.setPhotoUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.DOC_PHOTO_URL)));
            doc.setPhotoHeight(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.DOC_PHOTO_HEIGHT)));
            doc.setPhotoWidth(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.DOC_PHOTO_WIDTH)));
            attachment.setDoc(doc);
        } else if (Attachment.Type.Audio.equals(attachment.getType())) {
            Audio audio = new Audio();
            audio.setArtist(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.AUDIO_ARTIST)));
            audio.setTitle(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.AUDIO_TITLE)));
            audio.setUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.AUDIO_URL)));
            attachment.setAudio(audio);
        } else if (Attachment.Type.Link.equals(attachment.getType())) {
            Link link = new Link();
            link.setUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.LINK_URL)));
            link.setPhotoUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.LINK_PHOTO_URL)));
            link.setPhotoHeight(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.LINK_PHOTO_HEIGHT)));
            link.setPhotoWidth(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.LINK_PHOTO_WIDTH)));
            attachment.setLink(link);
        } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
            Podcast podcast = new Podcast();
            podcast.setTitle(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.PODCAST_TITLE)));
            podcast.setUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.PODCAST_URL)));
            podcast.setPhotoUrl(attachmentCursor.getString(attachmentCursor.getColumnIndex(AttachmentTable.PODCAST_PHOTO_URL)));
            podcast.setPhotoHeight(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.PODCAST_PHOTO_HEIGHT)));
            podcast.setPhotoWidth(attachmentCursor.getInt(attachmentCursor.getColumnIndex(AttachmentTable.PODCAST_PHOTO_WIDTH)));
            attachment.setPodcast(podcast);
        }
        return attachment;
    }

    private News getNewsFromCursor(Cursor newsCursor) {
        News news = new News();

        news.setType(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.TYPE)));
        news.setSourceId(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.SOURCE_ID)));
        news.setFromId(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.FROM_ID)));
        news.setDate(new VkDate(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.DATE))));
        news.setText(newsCursor.getString(newsCursor.getColumnIndex(NewsTable.TEXT)));

        Comments comments = new Comments();
        comments.setCount(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.COMMENTS_COUNT)));
        news.setComments(comments);

        Likes likes = new Likes();
        likes.setCount(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.LIKES_COUNT)));
        likes.setUserLikes(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.USER_LIKES)) > 0);
        likes.setCanLike(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.CAN_LIKE)) > 0);
        news.setLikes(likes);

        Reposts reposts = new Reposts();
        reposts.setCount(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.REPOSTS_COUNT)));
        news.setReposts(reposts);

        Views views = new Views();
        views.setCount(newsCursor.getInt(newsCursor.getColumnIndex(NewsTable.VIEWS_COUNT)));
        news.setViews(views);

        try (Cursor attachmentCursor = databaseHelper.query(
                getSelectDatabaseQuery(ATTACHMENT_TABLE_NAME, AttachmentTable.NEWS_ID, STRING_EQUALS,
                        newsCursor.getString(newsCursor.getColumnIndex(NewsTable.ID))))) {
            if (attachmentCursor.getCount() > 0) {
                List<Attachment> attachmentList = new ArrayList<>();

                while (attachmentCursor.moveToNext()) {
                    attachmentList.add(getAttachmentFromCursor(attachmentCursor));
                }

                news.setAttachments(attachmentList);
            }
        }

        return news;
    }

    private long putNewsInDatabase(final News news) {
        long copyNewsId = 0;
        if (news.getCopyHistory() != null) {
            copyNewsId = putNewsInDatabase(news.getCopyHistory().get(0));
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsTable.TYPE, news.getType());
        contentValues.put(NewsTable.SOURCE_ID, news.getSourceId());
        contentValues.put(NewsTable.FROM_ID, news.getFromId());
        contentValues.put(NewsTable.DATE, news.getDate().toString());
        contentValues.put(NewsTable.TEXT, news.getText());

        if (copyNewsId > 0) {
            contentValues.put(NewsTable.COPY_NEWS_ID, copyNewsId);
        }

        if (news.getComments() != null) {
            contentValues.put(NewsTable.COMMENTS_COUNT, news.getComments().getCount());
        }

        if (news.getLikes() != null) {
            contentValues.put(NewsTable.LIKES_COUNT, news.getLikes().getCount());
            contentValues.put(NewsTable.USER_LIKES, news.getLikes().getUserLikes());
            contentValues.put(NewsTable.CAN_LIKE, news.getLikes().getCanLike());
        }

        if (news.getReposts() != null) {
            contentValues.put(NewsTable.REPOSTS_COUNT, news.getReposts().getCount());
        }

        if (news.getViews() != null) {
            contentValues.put(NewsTable.VIEWS_COUNT, news.getViews().getCount());
        }

        long newsId = databaseHelper.insert(NEWS_TABLE_NAME, contentValues);

        if (news.getAttachments() != null) {
            putAttachmentsInDatabase(news.getAttachments(), newsId);
        }

        return newsId;
    }

    private void putAttachmentsInDatabase(List<Attachment> attachmentList, long newsId) {
        ContentValues contentValues = new ContentValues();
        for (Attachment attachment : attachmentList) {
            contentValues.put(AttachmentTable.NEWS_ID, newsId);
            contentValues.put(AttachmentTable.TYPE, attachment.getType());

            if (Attachment.Type.Photo.equals(attachment.getType())) {
                contentValues.put(AttachmentTable.PHOTO_URL, attachment.getPhoto().getUrl());
                contentValues.put(AttachmentTable.PHOTO_HEIGHT, attachment.getPhoto().getHeight());
                contentValues.put(AttachmentTable.PHOTO_WIDTH, attachment.getPhoto().getWidth());
            } else if (Attachment.Type.Video.equals(attachment.getType())) {
                contentValues.put(AttachmentTable.VIDEO_TITLE, attachment.getVideo().getTitle());
                contentValues.put(AttachmentTable.VIDEO_PHOTO_URL, attachment.getVideo().getPhotoUrl());
                contentValues.put(AttachmentTable.VIDEO_PHOTO_WIDTH, attachment.getVideo().getPhotoWidth());
                contentValues.put(AttachmentTable.VIDEO_PHOTO_HEIGHT, attachment.getVideo().getPhotoHeight());
            } else if (Attachment.Type.Doc.equals(attachment.getType())) {
                contentValues.put(AttachmentTable.DOC_URL, attachment.getDoc().getUrl());
                contentValues.put(AttachmentTable.DOC_PHOTO_URL, attachment.getDoc().getPhotoUrl());
                contentValues.put(AttachmentTable.DOC_PHOTO_WIDTH, attachment.getDoc().getPhotoWidth());
                contentValues.put(AttachmentTable.DOC_PHOTO_HEIGHT, attachment.getDoc().getPhotoHeight());
            } else if (Attachment.Type.Audio.equals(attachment.getType())) {
                contentValues.put(AttachmentTable.AUDIO_TITLE, attachment.getAudio().getTitle());
                contentValues.put(AttachmentTable.AUDIO_ARTIST, attachment.getAudio().getArtist());
                contentValues.put(AttachmentTable.AUDIO_URL, attachment.getAudio().getUrl());
            } else if (Attachment.Type.Link.equals(attachment.getType())) {
                contentValues.put(AttachmentTable.LINK_URL, attachment.getLink().getUrl());
                contentValues.put(AttachmentTable.LINK_PHOTO_URL, attachment.getLink().getPhotoUrl());
                contentValues.put(AttachmentTable.LINK_PHOTO_WIDTH, attachment.getLink().getPhotoWidth());
                contentValues.put(AttachmentTable.LINK_PHOTO_HEIGHT, attachment.getLink().getPhotoHeight());
            } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
                contentValues.put(AttachmentTable.PODCAST_TITLE, attachment.getPodcast().getTitle());
                contentValues.put(AttachmentTable.PODCAST_URL, attachment.getPodcast().getUrl());
                contentValues.put(AttachmentTable.PODCAST_PHOTO_URL, attachment.getPodcast().getPhotoUrl());
                contentValues.put(AttachmentTable.PODCAST_PHOTO_WIDTH, attachment.getPodcast().getPhotoWidth());
                contentValues.put(AttachmentTable.PODCAST_PHOTO_HEIGHT, attachment.getPodcast().getPhotoHeight());
            }

            databaseHelper.insert(ATTACHMENT_TABLE_NAME, contentValues);
            contentValues.clear();
        }
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
        byte[] buffer = new byte[Constants.INT_ONE_KB];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            resultOutputStream.write(buffer, 0, length);
        }

        return resultOutputStream.toString();
    }

    private String getLimitDatabaseQuery(String tableName, int startPosition, int size) {
        return SELECT_FROM + tableName + String.format(DATABASE_LIMIT, startPosition, size);
    }

    private String getNewsLimitDatabaseQuery(int startPosition, int size) {
        return SELECT_FROM + NEWS_TABLE_NAME + String.format(DATABASE_WHERE, NewsTable.FROM_ID, STRING_EQUALS, INT_ZERO) + String.format(DATABASE_LIMIT, startPosition, size);
    }

    private String getFriendsJoinUserDatabaseQuery(int startPosition, int size) {
        return SELECT_FROM + FRIEND_TABLE_NAME +
                String.format(DATABASE_JOIN, USER_TABLE_NAME, FriendTable.USER_ID,
                        UserTable.ID, UserTable.FIRST_NAME, UserTable.LAST_NAME) +
                String.format(DATABASE_LIMIT, startPosition, size);
    }

    private String getSelectDatabaseQuery(String tableName, String columnName, String condition, String value) {
        return SELECT_FROM + tableName + String.format(DATABASE_WHERE, columnName, condition, value);
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
}
