package com.my.vkclient.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.my.vkclient.Constants;
import com.my.vkclient.database.DatabaseHelper;
import com.my.vkclient.database.model.AttachmentTable;
import com.my.vkclient.database.model.FriendTable;
import com.my.vkclient.database.model.GroupTable;
import com.my.vkclient.database.model.NewsTable;
import com.my.vkclient.database.model.UserPhotoTable;
import com.my.vkclient.database.model.UserTable;
import com.my.vkclient.entities.Attachment;
import com.my.vkclient.entities.AttachmentPhoto;
import com.my.vkclient.entities.Audio;
import com.my.vkclient.entities.Comments;
import com.my.vkclient.entities.CropPhoto;
import com.my.vkclient.entities.Doc;
import com.my.vkclient.entities.Group;
import com.my.vkclient.entities.Likes;
import com.my.vkclient.entities.Link;
import com.my.vkclient.entities.News;
import com.my.vkclient.entities.Photo;
import com.my.vkclient.entities.Podcast;
import com.my.vkclient.entities.Reposts;
import com.my.vkclient.entities.User;
import com.my.vkclient.entities.UserCounters;
import com.my.vkclient.entities.UserLastSeen;
import com.my.vkclient.entities.Video;
import com.my.vkclient.entities.Views;
import com.my.vkclient.utils.ResultCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.my.vkclient.Constants.Database.ATTACHMENT_TABLE_NAME;
import static com.my.vkclient.Constants.Database.DATABASE_JOIN;
import static com.my.vkclient.Constants.Database.DATABASE_LIMIT;
import static com.my.vkclient.Constants.Database.DATABASE_ORDER_BY_ASC;
import static com.my.vkclient.Constants.Database.DATABASE_ORDER_BY_DESC;
import static com.my.vkclient.Constants.Database.DATABASE_WHERE;
import static com.my.vkclient.Constants.Database.FRIEND_TABLE_NAME;
import static com.my.vkclient.Constants.Database.GROUP_TABLE_NAME;
import static com.my.vkclient.Constants.Database.NEWS_TABLE_NAME;
import static com.my.vkclient.Constants.Database.SELECT_FROM;
import static com.my.vkclient.Constants.Database.USER_PHOTO_TABLE_NAME;
import static com.my.vkclient.Constants.Database.USER_TABLE_NAME;
import static com.my.vkclient.Constants.INT_ZERO;
import static com.my.vkclient.Constants.STRING_EQUALS;

class DatabaseRepositoryHelper {
    private final DatabaseHelper databaseHelper;
    private boolean userColumnIndexesReady;
    private boolean friendColumnIndexesReady;
    private boolean groupColumnIndexesReady;
    private boolean newsColumnIndexesReady;
    private boolean attachmentColumnIndexesReady;
    private boolean userPhotosColumnIndexesReady;
    private int columnIndexUserId;
    private int columnIndexUserFirstName;
    private int columnIndexUserLastName;
    private int columnIndexUserOnline;
    private int columnIndexUserPhoto100Url;
    private int columnIndexUserPhotoMaxUrl;
    private int columnIndexUserCropPhotoUrl;
    private int columnIndexUserCropPhotoHeight;
    private int columnIndexUserCropPhotoWidth;
    private int columnIndexUserCropRectX;
    private int columnIndexUserCropRectX2;
    private int columnIndexUserCropRectY;
    private int columnIndexUserCropRectY2;
    private int columnIndexUserBIRTH_DATE;
    private int columnIndexUserABOUT;
    private int columnIndexUserCOMMON_FRIENDS_COUNT;
    private int columnIndexUserFRIENDS_COUNT;
    private int columnIndexUserPHOTO_COUNT;
    private int columnIndexUserUNIVERSITY_NAME;
    private int columnIndexUserFACULTY_NAME;
    private int columnIndexUserFOLLOWERS_COUNT;
    private int columnIndexUserGAMES;
    private int columnIndexUserHOME_TOWN;
    private int columnIndexUserINTERESTS;
    private int columnIndexUserLAST_SEEN;
    private int columnIndexUserMOVIES;
    private int columnIndexUserMUSIC;
    private int columnIndexUserSTATUS;
    private int columnIndexUserVERIFIED;
    private int columnIndexGroupId;
    private int columnIndexGroupName;
    private int columnIndexGroupPhoto100Url;
    private int columnIndexNewsId;
    private int columnIndexNewsType;
    private int columnIndexNewsSourceId;
    private int columnIndexNewsFromId;
    private int columnIndexNewsDate;
    private int columnIndexNewsText;
    private int columnIndexNewsCopyNewsId;
    private int columnIndexNewsCommentsCount;
    private int columnIndexNewsLikesCount;
    private int columnIndexNewsUserLikes;
    private int columnIndexNewsCanLikes;
    private int columnIndexNewsRepostsCount;
    private int columnIndexNewsViewsCount;
    private int columnIndexAttachmentType;
    private int columnIndexAttachmentVideoTitle;
    private int columnIndexAttachmentDocUrl;
    private int columnIndexAttachmentAudioArtist;
    private int columnIndexAttachmentAudioTitle;
    private int columnIndexAttachmentAudioUrl;
    private int columnIndexAttachmentLinkUrl;
    private int columnIndexAttachmentPodcastTitle;
    private int columnIndexAttachmentPodcastUrl;
    private int columnIndexAttachmentPhotoUrl;
    private int columnIndexAttachmentPhotoHeight;
    private int columnIndexAttachmentPhotoWidth;
    private int columnIndexAttachmentDocTitle;
    private int columnIndexAttachmentDocExt;
    private int columnIndexAttachmentLinkTitle;
    private int columnIndexPhotoUrl;
    private int columnIndexPhotoHeight;
    private int columnIndexPhotoWidth;

    DatabaseRepositoryHelper(@NonNull final Context context) {
        List<Class<?>> tableClasses = Arrays.asList(UserTable.class, GroupTable.class, FriendTable.class,
                NewsTable.class, AttachmentTable.class, UserPhotoTable.class);

        databaseHelper = new DatabaseHelper(context, tableClasses);
    }

    void getUser(final int userId, final ResultCallback<User> resultCallback) {
        try (Cursor userCursor = databaseHelper.query(getSelectDatabaseQuery(USER_TABLE_NAME, UserTable.ID, String.valueOf(userId)))) {
            if (userCursor.moveToFirst()) {
                resultCallback.onResult(getUserFromCursor(userCursor, false));

                return;
            }
        }

        resultCallback.onResult(null);
    }

    void putUser(final User user) {
        ContentValues contentValues = new ContentValues();
        putUserToContentValue(user, contentValues);

        databaseHelper.insert(USER_TABLE_NAME, contentValues);
    }

    void putUserList(final List<User> userList) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            for (User user : userList) {
                putUserToContentValue(user, contentValues);

                databaseHelper.insertWithoutTransaction(writableDatabase, USER_TABLE_NAME, contentValues);
                contentValues.clear();
            }

            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    void getGroup(final int groupId, final ResultCallback<Group> resultCallback) {
        try (Cursor groupCursor = databaseHelper.query(getSelectDatabaseQuery(GROUP_TABLE_NAME, GroupTable.ID, String.valueOf(groupId)))) {
            if (groupCursor.moveToFirst()) {
                resultCallback.onResult(getGroupFromCursor(groupCursor));

                return;
            }
        }

        resultCallback.onResult(null);
    }

    void putGroup(final Group group) {
        ContentValues contentValues = new ContentValues();
        putGroupToContentValue(group, contentValues);
        databaseHelper.insert(GROUP_TABLE_NAME, contentValues);
    }

    void putGroupList(final List<Group> groupList) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            for (Group group : groupList) {
                putGroupToContentValue(group, contentValues);

                databaseHelper.insertWithoutTransaction(writableDatabase, GROUP_TABLE_NAME, contentValues);
                contentValues.clear();
            }

            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    void getUserPhotos(final int userId, int startPosition, int size, ResultCallback<List<Photo>> resultCallback) {
        try (Cursor userPhotosCursor = databaseHelper.query(getUserPhotoLimitDatabaseQuery(userId, startPosition, size))) {
            if (userPhotosCursor.getCount() > 0) {
                List<Photo> photoList = new ArrayList<>();
                prepareIndexesUserPhotoColumns(userPhotosCursor);

                while (userPhotosCursor.moveToNext()) {
                    Photo photo = new Photo();

                    photo.setPhotoUrl(userPhotosCursor.getString(columnIndexPhotoUrl));
                    photo.setPhotoHeight(userPhotosCursor.getInt(columnIndexPhotoHeight));
                    photo.setPhotoWidth(userPhotosCursor.getInt(columnIndexPhotoWidth));

                    photoList.add(photo);
                }

                resultCallback.onResult(photoList);

                return;
            }
        }

        resultCallback.onResult(null);
    }

    void putUserPhotos(final int userId, final List<Photo> photoList) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            for (Photo photo : photoList) {
                contentValues.put(UserPhotoTable.ID, photo.getId());
                contentValues.put(UserPhotoTable.LAST_UPDATE, Calendar.getInstance().getTime().getTime());
                contentValues.put(UserPhotoTable.USER_ID, userId);
                contentValues.put(UserPhotoTable.PHOTO_URL, photo.getPhotoUrl());
                contentValues.put(UserPhotoTable.PHOTO_HEIGHT, photo.getPhotoHeight());
                contentValues.put(UserPhotoTable.PHOTO_WIDTH, photo.getPhotoWidth());

                databaseHelper.insertWithoutTransaction(writableDatabase, USER_PHOTO_TABLE_NAME, contentValues);
                contentValues.clear();
            }

            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    void getFriends(int startPosition, int size, ResultCallback<List<User>> resultCallback) {
        try (Cursor friendsCursor = databaseHelper.query(getFriendsJoinUserDatabaseQuery(startPosition, size))) {
            if (friendsCursor.getCount() > 0) {
                List<User> friendList = new ArrayList<>();

                while (friendsCursor.moveToNext()) {
                    friendList.add(getUserFromCursor(friendsCursor, true));
                }

                resultCallback.onResult(friendList);

                return;
            }
        }

        resultCallback.onResult(null);
    }

    void putFriendList(final List<User> friendList) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            for (User user : friendList) {
                contentValues.put(FriendTable.USER_ID, user.getId());
                contentValues.put(FriendTable.LAST_UPDATE, Calendar.getInstance().getTime().getTime());

                databaseHelper.insertWithoutTransaction(writableDatabase, FRIEND_TABLE_NAME, contentValues);
                contentValues.clear();
            }

            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    void getNews(int startPosition, int size, ResultCallback<List<News>> resultCallback) {
        try (Cursor newsCursor = databaseHelper.query(getNewsLimitDatabaseQuery(startPosition, size))) {
            if (newsCursor.getCount() > 0) {
                List<News> newsList = new ArrayList<>();

                while (newsCursor.moveToNext()) {
                    News news = getNewsFromCursor(newsCursor);

                    int copyNewsId = newsCursor.getInt(columnIndexNewsCopyNewsId);
                    if (copyNewsId != 0) {
                        try (Cursor copyNewsCursor = databaseHelper.query(getSelectDatabaseQuery(NEWS_TABLE_NAME,
                                NewsTable.ID, String.valueOf(copyNewsId)))) {
                            if (copyNewsCursor.moveToFirst()) {
                                news.setCopyHistory(Collections.singletonList(getNewsFromCursor(copyNewsCursor)));
                            }
                        }
                    }

                    newsList.add(news);
                }

                resultCallback.onResult(newsList);

                return;
            }
        }

        resultCallback.onResult(null);
    }

    void putNewsWithoutAttachments(final News news) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            putNewsWithoutTransaction(writableDatabase, contentValues, news, false);

            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    void putNewsList(final List<News> newsList) {
        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();

            for (News news : newsList) {
                putNewsWithoutTransaction(writableDatabase, contentValues, news, true);
                contentValues.clear();
            }

            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    void clearAllTables() {
        databaseHelper.dropAllTable(null);
    }

    void clearNews() {
        databaseHelper.delete(NEWS_TABLE_NAME, null);
        databaseHelper.delete(ATTACHMENT_TABLE_NAME, null);
    }

    void clearFriends() {
        databaseHelper.delete(FRIEND_TABLE_NAME, null);
    }

    private void putUserToContentValue(User user, ContentValues contentValues) {
        contentValues.put(UserTable.ID, user.getId());
        contentValues.put(UserTable.LAST_UPDATE, Calendar.getInstance().getTime().getTime());
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

        contentValues.put(UserTable.BIRTH_DATE, user.getBirthDate());
        contentValues.put(UserTable.ABOUT, user.getAbout());
        contentValues.put(UserTable.COMMON_FRIENDS_COUNT, user.getCommonFriendsCount());

        if (user.getCounters() != null) {
            contentValues.put(UserTable.FRIENDS_COUNT, user.getCounters().getFriends());
            contentValues.put(UserTable.PHOTO_COUNT, user.getCounters().getPhotos());
        }

        contentValues.put(UserTable.UNIVERSITY_NAME, user.getUniversityName());
        contentValues.put(UserTable.FACULTY_NAME, user.getFacultyName());
        contentValues.put(UserTable.FOLLOWERS_COUNT, user.getFollowersCount());
        contentValues.put(UserTable.GAMES, user.getGames());
        contentValues.put(UserTable.HOME_TOWN, user.getHomeTown());
        contentValues.put(UserTable.INTERESTS, user.getInterests());

        if (user.getLastSeen() != null) {
            contentValues.put(UserTable.LAST_SEEN, user.getLastSeen().getTime());
            contentValues.put(UserTable.MOVIES, user.getMovies());
        }

        contentValues.put(UserTable.MUSIC, user.getMusic());
        contentValues.put(UserTable.STATUS, user.getStatus());
        contentValues.put(UserTable.VERIFIED, user.getVerified());
    }

    private void putGroupToContentValue(Group group, ContentValues contentValues) {
        contentValues.put(GroupTable.ID, group.getId());
        contentValues.put(GroupTable.LAST_UPDATE, Calendar.getInstance().getTime().getTime());
        contentValues.put(GroupTable.NAME, group.getName());
        contentValues.put(GroupTable.PHOTO_100_URL, group.getPhoto100Url());
    }

    private void putNewsWithoutTransaction(SQLiteDatabase writableDatabase, ContentValues contentValues, News news, boolean withAttachments) {
        contentValues.put(NewsTable.ID, news.getId());
        contentValues.put(NewsTable.LAST_UPDATE, Calendar.getInstance().getTime().getTime());
        contentValues.put(NewsTable.TYPE, news.getType());
        contentValues.put(NewsTable.SOURCE_ID, news.getSourceId());
        contentValues.put(NewsTable.FROM_ID, news.getFromId());
        contentValues.put(NewsTable.DATE, news.getDate());
        contentValues.put(NewsTable.TEXT, news.getText());

        if (news.getCopyHistory() != null) {
            news.getCopyHistory().get(0).setId(news.getId() * -1);
            contentValues.put(NewsTable.COPY_NEWS_ID, news.getCopyHistory().get(0).getId());
            ContentValues contentValuesCopyNews = new ContentValues();
            putNewsWithoutTransaction(writableDatabase, contentValuesCopyNews, news.getCopyHistory().get(0), withAttachments);
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

        if (withAttachments && news.getAttachments() != null) {
            putAttachmentsWithoutTransaction(writableDatabase, news.getAttachments(), news.getId());
        }

        databaseHelper.insertWithoutTransaction(writableDatabase, NEWS_TABLE_NAME, contentValues);
    }

    private Group getGroupFromCursor(Cursor groupCursor) {
        Group group = new Group();
        prepareIndexesGroupColumns(groupCursor);

        group.setId(groupCursor.getInt(columnIndexGroupId));
        group.setName(groupCursor.getString(columnIndexGroupName));
        group.setPhoto100Url(groupCursor.getString(columnIndexGroupPhoto100Url));

        return group;
    }

    private User getUserFromCursor(Cursor userCursor, boolean isFriendsTable) {
        User user = new User();

        synchronized (databaseHelper) {
            prepareIndexesUserColumns(userCursor, isFriendsTable);

            user.setId(userCursor.getInt(columnIndexUserId));
            user.setFirstName(userCursor.getString(columnIndexUserFirstName));
            user.setLastName(userCursor.getString(columnIndexUserLastName));
            user.setOnline(userCursor.getInt(columnIndexUserOnline) > 0);
            user.setPhoto100Url(userCursor.getString(columnIndexUserPhoto100Url));
            user.setPhotoMaxUrl(userCursor.getString(columnIndexUserPhotoMaxUrl));
            String cropPhotoUrl = userCursor.getString(columnIndexUserCropPhotoUrl);

            if (cropPhotoUrl != null) {
                user.setCropPhoto(new CropPhoto());
                user.getCropPhoto().setCropPhotoUrl(cropPhotoUrl);
                user.getCropPhoto().setCropPhotoHeight(userCursor.getInt(columnIndexUserCropPhotoHeight));
                user.getCropPhoto().setCropPhotoWidth(userCursor.getInt(columnIndexUserCropPhotoWidth));
                user.getCropPhoto().setCropRectX(userCursor.getFloat(columnIndexUserCropRectX));
                user.getCropPhoto().setCropRectX2(userCursor.getFloat(columnIndexUserCropRectX2));
                user.getCropPhoto().setCropRectY(userCursor.getFloat(columnIndexUserCropRectY));
                user.getCropPhoto().setCropRectY2(userCursor.getFloat(columnIndexUserCropRectY2));
            }

            user.setBirthDate(userCursor.getLong(columnIndexUserBIRTH_DATE));
            user.setAbout(userCursor.getString(columnIndexUserABOUT));
            user.setCommonFriendsCount(userCursor.getInt(columnIndexUserCOMMON_FRIENDS_COUNT));

            UserCounters userCounters = new UserCounters();
            userCounters.setFriends(userCursor.getInt(columnIndexUserFRIENDS_COUNT));
            userCounters.setPhotos(userCursor.getInt(columnIndexUserPHOTO_COUNT));
            user.setCounters(userCounters);

            user.setUniversityName(userCursor.getString(columnIndexUserUNIVERSITY_NAME));
            user.setFacultyName(userCursor.getString(columnIndexUserFACULTY_NAME));
            user.setFollowersCount(userCursor.getInt(columnIndexUserFOLLOWERS_COUNT));
            user.setGames(userCursor.getString(columnIndexUserGAMES));
            user.setHomeTown(userCursor.getString(columnIndexUserHOME_TOWN));
            user.setInterests(userCursor.getString(columnIndexUserINTERESTS));

            UserLastSeen userLastSeen = new UserLastSeen(userCursor.getLong(columnIndexUserLAST_SEEN));
            user.setLastSeen(userLastSeen);

            user.setMovies(userCursor.getString(columnIndexUserMOVIES));
            user.setMusic(userCursor.getString(columnIndexUserMUSIC));
            user.setStatus(userCursor.getString(columnIndexUserSTATUS));
            user.setVerified(userCursor.getInt(columnIndexUserVERIFIED) > 0);
        }

        return user;
    }

    private News getNewsFromCursor(Cursor newsCursor) {
        News news = new News();
        prepareIndexesNewsColumns(newsCursor);

        news.setId(newsCursor.getInt(columnIndexNewsId));
        news.setType(newsCursor.getString(columnIndexNewsType));
        news.setSourceId(newsCursor.getInt(columnIndexNewsSourceId));
        news.setFromId(newsCursor.getInt(columnIndexNewsFromId));
        news.setDate(newsCursor.getLong(columnIndexNewsDate));
        news.setText(newsCursor.getString(columnIndexNewsText));

        Comments comments = new Comments();
        comments.setCount(newsCursor.getInt(columnIndexNewsCommentsCount));
        news.setComments(comments);

        Likes likes = new Likes();
        likes.setCount(newsCursor.getInt(columnIndexNewsLikesCount));
        likes.setUserLikes(newsCursor.getInt(columnIndexNewsUserLikes) > 0);
        likes.setCanLike(newsCursor.getInt(columnIndexNewsCanLikes) > 0);
        news.setLikes(likes);

        Reposts reposts = new Reposts();
        reposts.setCount(newsCursor.getInt(columnIndexNewsRepostsCount));
        news.setReposts(reposts);

        Views views = new Views();
        views.setCount(newsCursor.getInt(columnIndexNewsViewsCount));
        news.setViews(views);

        try (Cursor attachmentCursor = databaseHelper.query(
                getSelectDatabaseQuery(ATTACHMENT_TABLE_NAME, AttachmentTable.NEWS_ID,
                        newsCursor.getString(columnIndexNewsId)))) {
            if (attachmentCursor.getCount() > 0) {
                List<Attachment> attachmentList = new ArrayList<>();

                while (attachmentCursor.moveToNext()) {
                    attachmentList.add(getAttachmentFromCursor(attachmentCursor));
                }

                news.setAttachments(attachmentList);
            }
        }

        int ownerId = news.getSourceId() == 0 ? news.getFromId() : news.getSourceId();

        if (ownerId > 0) {
            try (Cursor userCursor = databaseHelper.query(
                    getSelectDatabaseQuery(USER_TABLE_NAME, UserTable.ID, String.valueOf(ownerId)))) {
                if (userCursor.moveToFirst()) {
                    news.setUser(getUserFromCursor(userCursor, false));
                }
            }
        } else {
            ownerId *= -1;
            try (Cursor groupCursor = databaseHelper.query(
                    getSelectDatabaseQuery(GROUP_TABLE_NAME, GroupTable.ID, String.valueOf(ownerId)))) {
                if (groupCursor.moveToFirst()) {
                    news.setGroup(getGroupFromCursor(groupCursor));
                }
            }
        }

        return news;
    }

    private Attachment getAttachmentFromCursor(Cursor attachmentCursor) {
        Attachment attachment = new Attachment();
        prepareIndexesAttachmentColumns(attachmentCursor);
        attachment.setType(attachmentCursor.getString(columnIndexAttachmentType));

        if (Attachment.Type.Photo.equals(attachment.getType())) {
            Photo photo = new Photo();
            getAttachmentPhotoFromCursor(attachmentCursor, photo);
            attachment.setPhoto(photo);
        } else if (Attachment.Type.Video.equals(attachment.getType())) {
            Video video = new Video();
            video.setTitle(attachmentCursor.getString(columnIndexAttachmentVideoTitle));
            getAttachmentPhotoFromCursor(attachmentCursor, video);
            attachment.setVideo(video);
        } else if (Attachment.Type.Doc.equals(attachment.getType())) {
            Doc doc = new Doc();
            doc.setUrl(attachmentCursor.getString(columnIndexAttachmentDocUrl));
            doc.setTitle(attachmentCursor.getString(columnIndexAttachmentDocTitle));
            doc.setExt(attachmentCursor.getString(columnIndexAttachmentDocExt));
            getAttachmentPhotoFromCursor(attachmentCursor, doc);
            attachment.setDoc(doc);
        } else if (Attachment.Type.Audio.equals(attachment.getType())) {
            Audio audio = new Audio();
            audio.setArtist(attachmentCursor.getString(columnIndexAttachmentAudioArtist));
            audio.setTitle(attachmentCursor.getString(columnIndexAttachmentAudioTitle));
            audio.setUrl(attachmentCursor.getString(columnIndexAttachmentAudioUrl));
            attachment.setAudio(audio);
        } else if (Attachment.Type.Link.equals(attachment.getType())) {
            Link link = new Link();
            link.setUrl(attachmentCursor.getString(columnIndexAttachmentLinkUrl));
            link.setTitle(attachmentCursor.getString(columnIndexAttachmentLinkTitle));
            getAttachmentPhotoFromCursor(attachmentCursor, link);
            attachment.setLink(link);
        } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
            Podcast podcast = new Podcast();
            podcast.setTitle(attachmentCursor.getString(columnIndexAttachmentPodcastTitle));
            podcast.setUrl(attachmentCursor.getString(columnIndexAttachmentPodcastUrl));
            getAttachmentPhotoFromCursor(attachmentCursor, podcast);
            attachment.setPodcast(podcast);
        }
        return attachment;
    }

    private void getAttachmentPhotoFromCursor(Cursor attachmentCursor, AttachmentPhoto attachmentPhoto) {
        attachmentPhoto.setPhotoUrl(attachmentCursor.getString(columnIndexAttachmentPhotoUrl));
        attachmentPhoto.setPhotoHeight(attachmentCursor.getInt(columnIndexAttachmentPhotoHeight));
        attachmentPhoto.setPhotoWidth(attachmentCursor.getInt(columnIndexAttachmentPhotoWidth));
    }

    private void putAttachmentsWithoutTransaction(final SQLiteDatabase writableDatabase, List<Attachment> attachmentList, long newsId) {
        ContentValues contentValues = new ContentValues();
        for (Attachment attachment : attachmentList) {
            putAttachmentToContentValues(newsId, contentValues, attachment);

            databaseHelper.insertWithoutTransaction(writableDatabase, ATTACHMENT_TABLE_NAME, contentValues);
            contentValues.clear();
        }
    }

    private void putAttachmentToContentValues(long newsId, ContentValues contentValues, Attachment attachment) {
        contentValues.put(AttachmentTable.NEWS_ID, newsId);
        contentValues.put(AttachmentTable.TYPE, attachment.getType());

        if (Attachment.Type.Photo.equals(attachment.getType())) {
            putAttachmentPhotoToContentValues(contentValues, attachment.getPhoto());
        } else if (Attachment.Type.Video.equals(attachment.getType())) {
            contentValues.put(AttachmentTable.VIDEO_TITLE, attachment.getVideo().getTitle());
            putAttachmentPhotoToContentValues(contentValues, attachment.getVideo());
        } else if (Attachment.Type.Doc.equals(attachment.getType())) {
            contentValues.put(AttachmentTable.DOC_URL, attachment.getDoc().getUrl());
            contentValues.put(AttachmentTable.DOC_TITLE, attachment.getDoc().getTitle());
            contentValues.put(AttachmentTable.DOC_EXT, attachment.getDoc().getExt());
            putAttachmentPhotoToContentValues(contentValues, attachment.getDoc());
        } else if (Attachment.Type.Audio.equals(attachment.getType())) {
            contentValues.put(AttachmentTable.AUDIO_TITLE, attachment.getAudio().getTitle());
            contentValues.put(AttachmentTable.AUDIO_ARTIST, attachment.getAudio().getArtist());
            contentValues.put(AttachmentTable.AUDIO_URL, attachment.getAudio().getUrl());
        } else if (Attachment.Type.Link.equals(attachment.getType())) {
            contentValues.put(AttachmentTable.LINK_URL, attachment.getLink().getUrl());
            contentValues.put(AttachmentTable.LINK_TITLE, attachment.getLink().getTitle());
            putAttachmentPhotoToContentValues(contentValues, attachment.getLink());
        } else if (Attachment.Type.Podcast.equals(attachment.getType())) {
            contentValues.put(AttachmentTable.PODCAST_TITLE, attachment.getPodcast().getTitle());
            contentValues.put(AttachmentTable.PODCAST_URL, attachment.getPodcast().getUrl());
            putAttachmentPhotoToContentValues(contentValues, attachment.getPodcast());
        }
    }

    private void putAttachmentPhotoToContentValues(ContentValues contentValues, AttachmentPhoto attachmentPhoto) {
        contentValues.put(AttachmentTable.PHOTO_URL, attachmentPhoto.getPhotoUrl());
        contentValues.put(AttachmentTable.PHOTO_HEIGHT, attachmentPhoto.getPhotoHeight());
        contentValues.put(AttachmentTable.PHOTO_WIDTH, attachmentPhoto.getPhotoWidth());
    }

    private String getNewsLimitDatabaseQuery(int startPosition, int size) {
        return SELECT_FROM + NEWS_TABLE_NAME + String.format(DATABASE_WHERE, NewsTable.FROM_ID, STRING_EQUALS, INT_ZERO)
                + String.format(DATABASE_ORDER_BY_DESC, NewsTable.DATE) + String.format(DATABASE_LIMIT, startPosition, size);
    }

    private String getFriendsJoinUserDatabaseQuery(int startPosition, int size) {
        return SELECT_FROM + FRIEND_TABLE_NAME +
                String.format(DATABASE_JOIN, USER_TABLE_NAME, FriendTable.USER_ID,
                        UserTable.ID, UserTable.FIRST_NAME, UserTable.LAST_NAME) +
                String.format(DATABASE_LIMIT, startPosition, size);
    }

    private String getSelectDatabaseQuery(String tableName, String columnName, String value) {
        return SELECT_FROM + tableName + String.format(DATABASE_WHERE, columnName, Constants.STRING_EQUALS, value);
    }

    private String getUserPhotoLimitDatabaseQuery(int userId, int startPosition, int size) {
        return SELECT_FROM + USER_PHOTO_TABLE_NAME + String.format(DATABASE_WHERE, UserPhotoTable.USER_ID, STRING_EQUALS, userId)
                + String.format(DATABASE_ORDER_BY_ASC, UserPhotoTable.LAST_UPDATE) + String.format(DATABASE_LIMIT, startPosition, size);
    }

    private void prepareIndexesUserColumns(Cursor cursor, boolean isFriendsTable) {
        if ((!isFriendsTable && !userColumnIndexesReady) || (isFriendsTable && !friendColumnIndexesReady)) {
            columnIndexUserId = cursor.getColumnIndex(UserTable.ID);
            columnIndexUserFirstName = cursor.getColumnIndex(UserTable.FIRST_NAME);
            columnIndexUserLastName = cursor.getColumnIndex(UserTable.LAST_NAME);
            columnIndexUserOnline = cursor.getColumnIndex(UserTable.ONLINE);
            columnIndexUserPhoto100Url = cursor.getColumnIndex(UserTable.PHOTO_100_URL);
            columnIndexUserPhotoMaxUrl = cursor.getColumnIndex(UserTable.PHOTO_MAX_URL);
            columnIndexUserCropPhotoUrl = cursor.getColumnIndex(UserTable.CROP_PHOTO_URL);
            columnIndexUserCropPhotoHeight = cursor.getColumnIndex(UserTable.CROP_PHOTO_HEIGHT);
            columnIndexUserCropPhotoWidth = cursor.getColumnIndex(UserTable.CROP_PHOTO_WIDTH);
            columnIndexUserCropRectX = cursor.getColumnIndex(UserTable.CROP_RECT_X);
            columnIndexUserCropRectX2 = cursor.getColumnIndex(UserTable.CROP_RECT_X_2);
            columnIndexUserCropRectY = cursor.getColumnIndex(UserTable.CROP_RECT_Y);
            columnIndexUserCropRectY2 = cursor.getColumnIndex(UserTable.CROP_RECT_Y_2);
            columnIndexUserBIRTH_DATE = cursor.getColumnIndex(UserTable.BIRTH_DATE);
            columnIndexUserABOUT = cursor.getColumnIndex(UserTable.ABOUT);
            columnIndexUserCOMMON_FRIENDS_COUNT = cursor.getColumnIndex(UserTable.COMMON_FRIENDS_COUNT);
            columnIndexUserFRIENDS_COUNT = cursor.getColumnIndex(UserTable.FRIENDS_COUNT);
            columnIndexUserPHOTO_COUNT = cursor.getColumnIndex(UserTable.PHOTO_COUNT);
            columnIndexUserUNIVERSITY_NAME = cursor.getColumnIndex(UserTable.UNIVERSITY_NAME);
            columnIndexUserFACULTY_NAME = cursor.getColumnIndex(UserTable.FACULTY_NAME);
            columnIndexUserFOLLOWERS_COUNT = cursor.getColumnIndex(UserTable.FOLLOWERS_COUNT);
            columnIndexUserGAMES = cursor.getColumnIndex(UserTable.GAMES);
            columnIndexUserHOME_TOWN = cursor.getColumnIndex(UserTable.HOME_TOWN);
            columnIndexUserINTERESTS = cursor.getColumnIndex(UserTable.INTERESTS);
            columnIndexUserLAST_SEEN = cursor.getColumnIndex(UserTable.LAST_SEEN);
            columnIndexUserMOVIES = cursor.getColumnIndex(UserTable.MOVIES);
            columnIndexUserMUSIC = cursor.getColumnIndex(UserTable.MUSIC);
            columnIndexUserSTATUS = cursor.getColumnIndex(UserTable.STATUS);
            columnIndexUserVERIFIED = cursor.getColumnIndex(UserTable.VERIFIED);

            if (isFriendsTable) {
                userColumnIndexesReady = false;
                friendColumnIndexesReady = true;
            } else {
                userColumnIndexesReady = true;
                friendColumnIndexesReady = false;
            }
        }
    }

    private void prepareIndexesUserPhotoColumns(Cursor cursor) {
        if (!userPhotosColumnIndexesReady) {
            columnIndexPhotoUrl = cursor.getColumnIndex(UserPhotoTable.PHOTO_URL);
            columnIndexPhotoHeight = cursor.getColumnIndex(UserPhotoTable.PHOTO_HEIGHT);
            columnIndexPhotoWidth = cursor.getColumnIndex(UserPhotoTable.PHOTO_WIDTH);

            userPhotosColumnIndexesReady = true;
        }
    }

    private void prepareIndexesGroupColumns(Cursor cursor) {
        if (!groupColumnIndexesReady) {
            columnIndexGroupId = cursor.getColumnIndex(GroupTable.ID);
            columnIndexGroupName = cursor.getColumnIndex(GroupTable.NAME);
            columnIndexGroupPhoto100Url = cursor.getColumnIndex(GroupTable.PHOTO_100_URL);

            groupColumnIndexesReady = true;
        }
    }

    private void prepareIndexesNewsColumns(Cursor cursor) {
        if (!newsColumnIndexesReady) {
            columnIndexNewsId = cursor.getColumnIndex(NewsTable.ID);
            columnIndexNewsType = cursor.getColumnIndex(NewsTable.TYPE);
            columnIndexNewsSourceId = cursor.getColumnIndex(NewsTable.SOURCE_ID);
            columnIndexNewsFromId = cursor.getColumnIndex(NewsTable.FROM_ID);
            columnIndexNewsDate = cursor.getColumnIndex(NewsTable.DATE);
            columnIndexNewsText = cursor.getColumnIndex(NewsTable.TEXT);
            columnIndexNewsCopyNewsId = cursor.getColumnIndex(NewsTable.COPY_NEWS_ID);
            columnIndexNewsCommentsCount = cursor.getColumnIndex(NewsTable.COMMENTS_COUNT);
            columnIndexNewsLikesCount = cursor.getColumnIndex(NewsTable.LIKES_COUNT);
            columnIndexNewsUserLikes = cursor.getColumnIndex(NewsTable.USER_LIKES);
            columnIndexNewsCanLikes = cursor.getColumnIndex(NewsTable.CAN_LIKE);
            columnIndexNewsRepostsCount = cursor.getColumnIndex(NewsTable.REPOSTS_COUNT);
            columnIndexNewsViewsCount = cursor.getColumnIndex(NewsTable.VIEWS_COUNT);

            newsColumnIndexesReady = true;
        }
    }

    private void prepareIndexesAttachmentColumns(Cursor cursor) {
        if (!attachmentColumnIndexesReady) {
            columnIndexAttachmentType = cursor.getColumnIndex(AttachmentTable.TYPE);
            columnIndexAttachmentVideoTitle = cursor.getColumnIndex(AttachmentTable.VIDEO_TITLE);
            columnIndexAttachmentDocUrl = cursor.getColumnIndex(AttachmentTable.DOC_URL);
            columnIndexAttachmentDocTitle = cursor.getColumnIndex(AttachmentTable.DOC_TITLE);
            columnIndexAttachmentDocExt = cursor.getColumnIndex(AttachmentTable.DOC_EXT);
            columnIndexAttachmentAudioArtist = cursor.getColumnIndex(AttachmentTable.AUDIO_ARTIST);
            columnIndexAttachmentAudioTitle = cursor.getColumnIndex(AttachmentTable.AUDIO_TITLE);
            columnIndexAttachmentAudioUrl = cursor.getColumnIndex(AttachmentTable.AUDIO_URL);
            columnIndexAttachmentLinkUrl = cursor.getColumnIndex(AttachmentTable.LINK_URL);
            columnIndexAttachmentLinkTitle = cursor.getColumnIndex(AttachmentTable.LINK_TITLE);
            columnIndexAttachmentPodcastTitle = cursor.getColumnIndex(AttachmentTable.PODCAST_TITLE);
            columnIndexAttachmentPodcastUrl = cursor.getColumnIndex(AttachmentTable.PODCAST_URL);
            columnIndexAttachmentPhotoUrl = cursor.getColumnIndex(AttachmentTable.PHOTO_URL);
            columnIndexAttachmentPhotoHeight = cursor.getColumnIndex(AttachmentTable.PHOTO_HEIGHT);
            columnIndexAttachmentPhotoWidth = cursor.getColumnIndex(AttachmentTable.PHOTO_WIDTH);

            attachmentColumnIndexesReady = true;
        }
    }
}
