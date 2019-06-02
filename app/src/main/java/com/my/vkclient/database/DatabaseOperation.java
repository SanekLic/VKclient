package com.my.vkclient.database;

import android.content.ContentValues;
import android.database.Cursor;

public interface DatabaseOperation {

    Cursor query(String sql, String... params);

    long insert(final String tableName, ContentValues contentValues);

    long delete(final String tableName, String sql, String... params);
}
