package com.miymayster.olya.picturesqlite.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.miymayster.olya.picturesqlite.R;

/**
 * Created by Olga on 27.06.2017.
 */

public class ImageCursorAdapter extends CursorAdapter {
    public ImageCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.image_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(ImageContract.ImageEntry.COLUMN_IMAGE));
        ((ImageView) view.findViewById(R.id.image)).setImageBitmap(bytesToBitmap(imageBytes));
    }

    private Bitmap bytesToBitmap(byte[] imageTile) {
        return BitmapFactory.decodeByteArray(imageTile, 0, imageTile.length);
    }
}
