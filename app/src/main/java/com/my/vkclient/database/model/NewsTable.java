package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbAutoincrement;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbParcelable;
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

    @dbParcelable
    public static final String COPY_HISTORY = "COPY_HISTORY";

    @dbParcelable
    public static final String COMMENTS = "COMMENTS";

    @dbParcelable
    public static final String LIKES = "LIKES";

    @dbParcelable
    public static final String REPOSTS = "REPOSTS";

    @dbParcelable
    public static final String VIEWS = "VIEWS";

    @dbParcelable
    public static final String ATTACHMENTS = "ATTACHMENTS";
}
