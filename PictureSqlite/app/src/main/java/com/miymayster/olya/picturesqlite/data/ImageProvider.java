package com.miymayster.olya.picturesqlite.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Olga on 27.06.2017.
 */

public class ImageProvider extends ContentProvider {
    private ImageDBHelper imageDBHelper;

    private static final int IMAGES = 100;
    private static final int IMAGE_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ImageContract.CONTENT_AUTHORITY, ImageContract.PATH_IMAGES, IMAGES);
        uriMatcher.addURI(ImageContract.CONTENT_AUTHORITY, ImageContract.PATH_IMAGES + "/#", IMAGES);
    }

    @Override
    public boolean onCreate() {
        imageDBHelper = new ImageDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) throws IllegalArgumentException {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = imageDBHelper.getReadableDatabase();
        Cursor cursor;
        switch (match) {
            case IMAGES:
                cursor = db.query(ImageContract.ImageEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case IMAGE_ID:
                long id = ContentUris.parseId(uri);
                selection = ImageContract.ImageEntry._ID + "=?";
                selectionArgs = new String[]{
                        String.valueOf(id)
                };
                cursor = db.query(ImageContract.ImageEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Query is not supported for " + uri.toString());
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case IMAGES:
                return ImageContract.ImageEntry.CONTENT_LIST_TYPE;
            case IMAGE_ID:
                return ImageContract.ImageEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalArgumentException(uri + " is not supported");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case IMAGES:
                SQLiteDatabase db = imageDBHelper.getWritableDatabase();
                long id = db.insert(ImageContract.ImageEntry.TABLE_NAME, null, values);
                Uri newUri = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(newUri, null);
                return newUri;
            default:
                throw new IllegalArgumentException("Insert is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new IllegalArgumentException("Delete is not supported");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new IllegalArgumentException("Update is not supported");
    }
}
