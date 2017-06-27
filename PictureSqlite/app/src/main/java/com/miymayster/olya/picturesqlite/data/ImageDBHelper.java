package com.miymayster.olya.picturesqlite.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Olga on 27.06.2017.
 */

public class ImageDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "images.db";
    private static final SQLiteDatabase.CursorFactory FACTORY = null;
    private static final int VERSION = 1;

    private static final String SQL_CREATE_TABLES =
            "CREATE TABLE " + ImageContract.ImageEntry.TABLE_NAME + " (" +
                    ImageContract.ImageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ImageContract.ImageEntry.COLUMN_IMAGE + " BLOB NOT NULL);";
    private static final String SQL_DROP_TABLES =
            "DROP TABLE " + ImageContract.ImageEntry.TABLE_NAME + ";";

    public ImageDBHelper(Context context) {
        super(context, DATABASE_NAME, FACTORY, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
