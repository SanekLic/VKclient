package com.my.vkclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.my.vkclient.Constants;
import com.my.vkclient.database.fields.dbAutoincrement;
import com.my.vkclient.database.fields.dbBoolean;
import com.my.vkclient.database.fields.dbFloat;
import com.my.vkclient.database.fields.dbInt;
import com.my.vkclient.database.fields.dbLong;
import com.my.vkclient.database.fields.dbPrimaryKey;
import com.my.vkclient.database.fields.dbString;
import com.my.vkclient.database.model.AttachmentTable;
import com.my.vkclient.database.model.FriendTable;
import com.my.vkclient.database.model.GroupTable;
import com.my.vkclient.database.model.NewsTable;
import com.my.vkclient.database.model.UserTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.my.vkclient.Constants.Database.DROP_TABLE_IF_EXISTS;
import static com.my.vkclient.Constants.Database.FIELD_DONT_HAVE_TYPE_ANNOTATION;
import static com.my.vkclient.Constants.Database.SQL_TABLE_CREATE_TEMPLATE;
import static com.my.vkclient.Constants.STRING_COMMA;
import static com.my.vkclient.Constants.STRING_EMPTY;
import static com.my.vkclient.Constants.STRING_SPACE;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@NonNull final Context context) {
        super(context, Constants.Database.DATABASE_NAME, null, Constants.Database.DATABASE_VERSION);
    }

    private static List<Class<?>> getTables() {
        return Arrays.asList(UserTable.class, GroupTable.class, FriendTable.class,
                NewsTable.class, AttachmentTable.class);
    }

    private String getCreateTableString(final Class<?> tableClass) {
        final String tableName = getTableName(tableClass);

        if (tableName != null) {
            final Field[] fields = tableClass.getFields();

            final StringBuilder builder = new StringBuilder();

            for (final Field field : fields) {
                String type = STRING_EMPTY;

                final dbPrimaryKey primaryKeyAnnotation = field.getAnnotation(dbPrimaryKey.class);
                final dbAutoincrement autoincrementAnnotation = field.getAnnotation(dbAutoincrement.class);

                final Annotation[] annotations = field.getAnnotations();

                for (final Annotation typeAnnotation : annotations) {
                    if (typeAnnotation instanceof dbString) {
                        type = ((dbString) typeAnnotation).name();
                    } else if (typeAnnotation instanceof dbBoolean) {
                        type = ((dbBoolean) typeAnnotation).name();
                    } else if (typeAnnotation instanceof dbInt) {
                        type = ((dbInt) typeAnnotation).name();
                    } else if (typeAnnotation instanceof dbFloat) {
                        type = ((dbFloat) typeAnnotation).name();
                    } else if (typeAnnotation instanceof dbLong) {
                        type = ((dbLong) typeAnnotation).name();
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

                    final String template =
                            fieldName + STRING_SPACE +
                                    type + STRING_SPACE +
                                    primaryKey + STRING_SPACE +
                                    autoincrement;

                    builder.append(template);
                }
            }

            return String.format(SQL_TABLE_CREATE_TEMPLATE, tableName, builder.toString());
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
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (final Class<?> table : getTables()) {

            final String createTableString = getCreateTableString(table);

            if (!createTableString.isEmpty()) {
                sqLiteDatabase.execSQL(createTableString);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropAllTable(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropAllTable(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    public Cursor query(final String sql, final String... selectionArgs) {
        final SQLiteDatabase readableDatabase = getReadableDatabase();

        return readableDatabase.rawQuery(sql, selectionArgs);
    }

    public void insert(final String tableName, final ContentValues contentValues) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();

        writableDatabase.beginTransaction();

        try {
            writableDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public void insertWithoutTransaction(final SQLiteDatabase writableDatabase, String tableName, ContentValues contentValues) {
        writableDatabase.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void dropAllTable(@Nullable SQLiteDatabase sqLiteDatabase) {

        final SQLiteDatabase writableDatabase = sqLiteDatabase != null ? sqLiteDatabase : getWritableDatabase();

        for (final Class<?> table : getTables()) {
            String format = String.format(DROP_TABLE_IF_EXISTS, getTableName(table));
            writableDatabase.execSQL(format);
        }

        onCreate(writableDatabase);
    }

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
