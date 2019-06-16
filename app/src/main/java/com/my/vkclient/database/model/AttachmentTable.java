package com.my.vkclient.database.model;

import com.my.vkclient.Constants;
import com.my.vkclient.database.Table;
import com.my.vkclient.database.fields.dbAutoincrement;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;

@Table(name = Constants.Database.ATTACHMENT_TABLE_NAME)
public class AttachmentTable {
    @dbInt
    @dbPrimaryKey
    @dbAutoincrement
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
    public static final String VIDEO_TITLE = "VIDEO_TITLE";

    @dbString
    public static final String DOC_TITLE = "DOC_TITLE";

    @dbString
    public static final String DOC_URL = "DOC_URL";

    @dbString
    public static final String DOC_EXT = "DOC_EXT";

    @dbString
    public static final String AUDIO_TITLE = "AUDIO_TITLE";

    @dbString
    public static final String AUDIO_ARTIST = "AUDIO_ARTIST";

    @dbString
    public static final String AUDIO_URL = "AUDIO_URL";

    @dbString
    public static final String LINK_TITLE = "LINK_TITLE";

    @dbString
    public static final String LINK_URL = "LINK_URL";

    @dbString
    public static final String PODCAST_TITLE = "PODCAST_TITLE";

    @dbString
    public static final String PODCAST_URL = "PODCAST_URL";
}
