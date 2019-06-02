package com.my.vkclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.my.vkclient.Constants;
import com.my.vkclient.database.fields.dbParcelable;
import com.my.vkclient.database.fields.dbFloat;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;
import com.my.vkclient.database.model.GroupTable;
import com.my.vkclient.database.model.UserTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseOperation {

    private static final String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s);";

    public DatabaseHelper(@Nullable final Context context, @Nullable final SQLiteDatabase.CursorFactory cursorFactory, final int version) {
        super(context, Constants.Database.DATABASE_NAME, cursorFactory, version);
    }

    private static List<Class<?>> getTables() {
        return Arrays.asList(UserTable.class, GroupTable.class);
    }

    private String getCreateTableString(final Class<?> tableClass) {
        final String tableName = getTableName(tableClass);

        if (tableName != null) {
            final Field[] fields = tableClass.getFields();

            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                String type = "";

                final dbPrimaryKey primaryKeyAnnotation = field.getAnnotation(dbPrimaryKey.class);

                final Annotation[] annotations = field.getAnnotations();

                for (final Annotation typeAnnotation : annotations) {
                    if (typeAnnotation instanceof dbString) {
                        type = ((dbString) typeAnnotation).name();
                    } else if (typeAnnotation instanceof dbInt) {
                        type = ((dbInt) typeAnnotation).name();
                    } else if (typeAnnotation instanceof dbFloat) {
                        type = ((dbFloat) typeAnnotation).name();
                    } else if (typeAnnotation instanceof dbParcelable) {
                        type = ((dbParcelable) typeAnnotation).name();
                    } else if (!(typeAnnotation instanceof dbPrimaryKey)) {
                        throw new IllegalStateException("Field don't have type annotation");
                    }
                }

                if (!type.isEmpty()) {

                    if (!builder.toString().isEmpty()) {
                        builder.append(",");
                    }

                    final String fieldName = field.getName();

                    String primaryKey = "";

                    if (primaryKeyAnnotation != null) {
                        primaryKey = Constants.Database.PRIMARY_KEY;
                    }

                    final String template = fieldName + " " + type + " " + primaryKey;

                    builder.append(template);
                }
            }

            return String.format(SQL_TABLE_CREATE_TEMPLATE, tableName, builder.toString());
        } else {
            return "";
        }
    }

    private String getTableName(final Class<?> tableClass) {
        final Table annotation = tableClass.getAnnotation(Table.class);

        if (annotation != null) {
            return annotation.name();
        }

        return null;
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        for (final Class<?> table : getTables()) {

            final String createTableString = getCreateTableString(table);

            if (!createTableString.equals("")) {
                sqLiteDatabase.execSQL(createTableString);
            }
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        throw new UnsupportedOperationException("Upgrade not supported");
    }

    @Override
    public Cursor query(final String sql, final String... params) {
        final SQLiteDatabase readableDatabase = getReadableDatabase();

        readableDatabase.beginTransaction();

        final Cursor cursor;

        try {
            cursor = readableDatabase.rawQuery(sql, params);
            readableDatabase.setTransactionSuccessful();
        } finally {
            readableDatabase.endTransaction();
        }

        return cursor;
    }

    @Override
    public long insert(final String tableName, final ContentValues contentValues) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();

        writableDatabase.beginTransaction();

        final long insert;

        try {
            insert = writableDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }

        return insert;
    }

    @Override
    public long delete(final String tableName, final String sql, final String... params) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        final int delete;

        try {
            delete = writableDatabase.delete(tableName, sql, params);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }

        return delete;
    }
}
