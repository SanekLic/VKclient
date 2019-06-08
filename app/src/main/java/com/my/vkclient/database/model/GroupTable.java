package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
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
    public static final String PHOTO_100 = "PHOTO_100";
}
