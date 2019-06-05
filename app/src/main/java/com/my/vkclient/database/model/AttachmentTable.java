package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;

@Table(name = Constants.Database.ATTACHMENT_TABLE_NAME)
public class AttachmentTable {
    @dbInt
    @dbPrimaryKey
    public static final String ID = "ID";

    @dbInt
    public static final String NEWS_ID = "NEWS_ID";

    @dbString
    public static final String TYPE = "TYPE";

    @dbString
    public static final String PHOTO_URL = "PHOTO_URL";

    @dbInt
    public static final String PHOTO_WIDTH = "PHOTO_WIDTH";

    @dbInt
    public static final String PHOTO_HEIGHT = "PHOTO_HEIGHT";

    @dbString
    public static final String VIDEO_URL = "VIDEO_URL";

    @dbInt
    public static final String VIDEO_WIDTH = "VIDEO_WIDTH";

    @dbInt
    public static final String VIDEO_HEIGHT = "VIDEO_HEIGHT";

}
