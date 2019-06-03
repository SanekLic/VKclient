package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbPrimaryKey;

@Table(name = Constants.Database.FRIEND_TABLE_NAME)
public class FriendTable {
    @dbInt
    @dbPrimaryKey
    public static final String USER_ID = "USER_ID";

    @dbInt
    public static final String FRIEND_LAST_UPDATE = "FRIEND_LAST_UPDATE";
}
