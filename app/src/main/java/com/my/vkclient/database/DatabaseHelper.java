package com.my.vkclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.my.vkclient.Constants;
import com.my.vkclient.database.fields.dbAutoincrement;
import com.my.vkclient.database.fields.dbFloat;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbParcelable;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;
import com.my.vkclient.database.model.FriendTable;
import com.my.vkclient.database.model.GroupTable;
import com.my.vkclient.database.model.NewsTable;
import com.my.vkclient.database.model.UserTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.my.vkclient.Constants.Database.FIELD_DONT_HAVE_TYPE_ANNOTATION;
import static com.my.vkclient.Constants.Database.UPGRADE_NOT_SUPPORTED;
import static com.my.vkclient.Constants.STRING_COMMA;
import static com.my.vkclient.Constants.STRING_EMPTY;
import static com.my.vkclient.Constants.STRING_SPACE;

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseOperation {

    public DatabaseHelper(@Nullable final Context context, @Nullable final SQLiteDatabase.CursorFactory cursorFactory, final int version) {
        super(context, Constants.Database.DATABASE_NAME, cursorFactory, version);
    }

    private static List<Class<?>> getTables() {
        return Arrays.asList(UserTable.class, GroupTable.class, FriendTable.class, NewsTable.class);
    }

    private String getCreateTableString(final Class<?> tableClass) {
        final String tableName = getTableName(tableClass);

        if (tableName != null) {
            final Field[] fields = tableClass.getFields();

            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                String type = STRING_EMPTY;

                final dbPrimaryKey primaryKeyAnnotation = field.getAnnotation(dbPrimaryKey.class);
                final dbAutoincrement autoincrementAnnotation = field.getAnnotation(dbAutoincrement.class);

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
                    } else if (!(typeAnnotation instanceof dbPrimaryKey) && !(typeAnnotation instanceof dbAutoincrement)) {
                        throw new IllegalStateException(FIELD_DONT_HAVE_TYPE_ANNOTATION);
                    }
                }

                if (!type.isEmpty()) {

                    if (!builder.toString().isEmpty()) {
                        builder.append(STRING_COMMA);
                    }

                    final String fieldName = field.getName();

                    String primaryKey = STRING_EMPTY;

                    if (primaryKeyAnnotation != null) {
                        primaryKey = Constants.Database.PRIMARY_KEY;
                    }

                    String autoincrement = STRING_EMPTY;

                    if (autoincrementAnnotation != null) {
                        autoincrement = Constants.Database.AUTOINCREMENT;
                    }

                    final String template = new StringBuilder()
                            .append(fieldName).append(STRING_SPACE)
                            .append(type).append(STRING_SPACE)
                            .append(primaryKey).append(STRING_SPACE)
                            .append(autoincrement).toString();

                    builder.append(template);
                }
            }

            return String.format(Constants.Database.SQL_TABLE_CREATE_TEMPLATE, tableName, builder.toString());
        } else {
            return STRING_EMPTY;
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

            if (!createTableString.isEmpty()) {
                sqLiteDatabase.execSQL(createTableString);
            }
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        throw new UnsupportedOperationException(UPGRADE_NOT_SUPPORTED);
    }

    @Override
    public Cursor query(final String sql, final String... selectionArgs) {
        final SQLiteDatabase readableDatabase = getReadableDatabase();

        readableDatabase.beginTransaction();

        final Cursor cursor;

        try {
            cursor = readableDatabase.rawQuery(sql, selectionArgs);
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
