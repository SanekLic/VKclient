package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbLong;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;

@Table(name = Constants.Database.USER_PHOTO_TABLE_NAME)
public class UserPhotoTable {
    @dbInt
    @dbPrimaryKey
    public static final String ID = "ID";

    @dbLong
    public static final String LAST_UPDATE = "LAST_UPDATE";

    @dbInt
    public static final String USER_ID = "USER_ID";

    @dbString
    public static final String PHOTO_URL = "PHOTO_URL";

    @dbInt
    public static final String PHOTO_WIDTH = "PHOTO_WIDTH";

    @dbInt
    public static final String PHOTO_HEIGHT = "PHOTO_HEIGHT";
}
