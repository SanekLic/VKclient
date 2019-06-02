package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbParcelable;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;

@Table(name = Constants.Database.USER_TABLE_NAME)
public class UserTable {
    @dbInt
    @dbPrimaryKey
    public static final String ID = "ID";

    @dbInt
    public static final String ONLINE = "ONLINE";

    @dbString
    public static final String FIRST_NAME = "FIRST_NAME";

    @dbString
    public static final String LAST_NAME = "LAST_NAME";

    @dbString
    public static final String PHOTO_MAX_URL = "PHOTO_MAX_URL";

    @dbString
    public static final String PHOTO_100_URL = "PHOTO_100_URL";

    @dbParcelable
    public static final String CROP_PHOTO = "CROP_PHOTO";
}
