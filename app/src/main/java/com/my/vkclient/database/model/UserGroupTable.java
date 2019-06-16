package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbAutoincrement;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbLong;
import com.my.vkclient.database.fields.dbPrimaryKey;

@Table(name = Constants.Database.USER_GROUP_TABLE_NAME)
public class UserGroupTable {
    @dbInt
    @dbPrimaryKey
    @dbAutoincrement
    public static final String GROUP_COUNTER = "GROUP_COUNTER";

    @dbInt
    public static final String GROUP_ID = "GROUP_ID";

    @dbLong
    public static final String LAST_UPDATE = "LAST_UPDATE";
}
