package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbAutoincrement;
import com.my.vkclient.database.fields.dbBoolean;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;

@Table(name = Constants.Database.NEWS_TABLE_NAME)
public class NewsTable {
    @dbInt
    @dbPrimaryKey
    @dbAutoincrement
    public static final String ID = "ID";

    @dbString
    public static final String TYPE = "TYPE";

    @dbInt
    public static final String SOURCE_ID = "SOURCE_ID";

    @dbInt
    public static final String FROM_ID = "FROM_ID";

    @dbString
    public static final String DATE = "DATE";

    @dbString
    public static final String TEXT = "TEXT";

    @dbInt
    public static final String COPY_NEWS_ID = "COPY_NEWS_ID";

    @dbInt
    public static final String COMMENTS_COUNT = "COMMENTS_COUNT";

    @dbInt
    public static final String LIKES_COUNT = "LIKES_COUNT";

    @dbBoolean
    public static final String USER_LIKES = "USER_LIKES";

    @dbBoolean
    public static final String CAN_LIKE = "CAN_LIKE";

    @dbInt
    public static final String REPOSTS_COUNT = "REPOSTS_COUNT";

    @dbInt
    public static final String VIEWS_COUNT = "VIEWS_COUNT";
}
