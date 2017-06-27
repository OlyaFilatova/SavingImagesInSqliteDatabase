package com.miymayster.olya.picturesqlite.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Olga on 27.06.2017.
 */

public class ImageContract {
    private ImageContract() {

    }

    public static final String CONTENT_AUTHORITY = "com.miymayster.olya.picturesqlite";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_IMAGES = "images";

    public static class ImageEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_IMAGES);
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_IMAGES;
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_IMAGES;
        public static final String TABLE_NAME = "images";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_IMAGE = "image";
    }
}
