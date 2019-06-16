package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbBoolean;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbLong;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;

@Table(name = Constants.Database.GROUP_TABLE_NAME)
public class GroupTable {
    @dbInt
    @dbPrimaryKey
    public static final String ID = "ID";

    @dbLong
    public static final String LAST_UPDATE = "LAST_UPDATE";

    @dbString
    public static final String NAME = "NAME";

    @dbString
    public static final String PHOTO_100_URL = "PHOTO_100_URL";

    @dbString
    public static final String ACTIVITY = "ACTIVITY";

    @dbString
    public static final String DESCRIPTION = "DESCRIPTION";

    @dbString
    public static final String STATUS = "STATUS";

    @dbString
    public static final String SITE = "SITE";

    @dbBoolean
    public static final String VERIFIED = "VERIFIED";
}
