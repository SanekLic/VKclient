package com.my.vkclient.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public interface DatabaseOperation {

    Cursor query(String sql, String... params);

    long insert(final String tableName, ContentValues contentValues);

    long insertWithoutTransaction(final SQLiteDatabase writableDatabase, final String tableName, ContentValues contentValues);

    long delete(final String tableName, String sql, String... params);
}
