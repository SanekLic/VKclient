package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbBoolean;
import com.my.vkclient.database.fields.dbFloat;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbLong;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;

@Table(name = Constants.Database.USER_TABLE_NAME)
public class UserTable {
    @dbInt
    @dbPrimaryKey
    public static final String ID = "ID";

    @dbLong
    public static final String LAST_UPDATE = "LAST_UPDATE";

    @dbBoolean
    public static final String ONLINE = "ONLINE";

    @dbString
    public static final String FIRST_NAME = "FIRST_NAME";

    @dbString
    public static final String LAST_NAME = "LAST_NAME";

    @dbString
    public static final String PHOTO_MAX_URL = "PHOTO_MAX_URL";

    @dbString
    public static final String PHOTO_100_URL = "PHOTO_100_URL";

    @dbString
    public static final String CROP_PHOTO_URL = "CROP_PHOTO_URL";

    @dbInt
    public static final String CROP_PHOTO_WIDTH = "CROP_PHOTO_WIDTH";

    @dbInt
    public static final String CROP_PHOTO_HEIGHT = "CROP_PHOTO_HEIGHT";

    @dbFloat
    public static final String CROP_RECT_X = "CROP_RECT_X";

    @dbFloat
    public static final String CROP_RECT_Y = "CROP_RECT_Y";

    @dbFloat
    public static final String CROP_RECT_X_2 = "CROP_RECT_X_2";

    @dbFloat
    public static final String CROP_RECT_Y_2 = "CROP_RECT_Y_2";

    @dbString
    public static final String ABOUT = "ABOUT";

    @dbInt
    public static final String COMMON_FRIENDS_COUNT = "COMMON_FRIENDS_COUNT";

    @dbInt
    public static final String FRIENDS_COUNT = "FRIENDS_COUNT";

    @dbInt
    public static final String PHOTO_COUNT = "PHOTO_COUNT";

    @dbString
    public static final String UNIVERSITY_NAME = "UNIVERSITY_NAME";

    @dbString
    public static final String FACULTY_NAME = "FACULTY_NAME";

    @dbInt
    public static final String FOLLOWERS_COUNT = "FOLLOWERS_COUNT";

    @dbString
    public static final String GAMES = "GAMES";

    @dbString
    public static final String HOME_TOWN = "HOME_TOWN";

    @dbString
    public static final String INTERESTS = "INTERESTS";

    @dbLong
    public static final String LAST_SEEN = "LAST_SEEN";

    @dbString
    public static final String MOVIES = "MOVIES";

    @dbString
    public static final String MUSIC = "MUSIC";

    @dbString
    public static final String STATUS = "STATUS";

    @dbBoolean
    public static final String VERIFIED = "VERIFIED";
}
