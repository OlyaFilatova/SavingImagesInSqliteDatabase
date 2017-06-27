package com.miymayster.olya.picturesqlite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Olga on 27.06.2017.
 */

public class ImageLoader extends AsyncTaskLoader<ArrayList<Byte>> {
    Uri imageUri;
    private static final int MAX_IMAGES_SIZE_IN_BYTES = 2097152;

    public ImageLoader(Context context, Uri uri) {
        super(context);
        imageUri = uri;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<Byte> loadInBackground() {
        ArrayList<Byte> imageBytes = null;
        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                imageBytes = getBitmapAsByteArray(bitmap);

            } catch (IOException e) {
                Log.e(MainActivity.class.getSimpleName(), "couldn't get image");
            }
        }
        return imageBytes;
    }

    public static ArrayList<Byte> getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        int bytesCount = outputStream.size();
        // making sure that we save only images that are less than 2mb
        if (bytesCount > MAX_IMAGES_SIZE_IN_BYTES) {
            return null;
        }
        byte[] byteBitmap = outputStream.toByteArray();
        ArrayList<Byte> bytes = new ArrayList<>();
        for (byte b : byteBitmap) {
            bytes.add(b);
        }
        Log.i("ImageLoader", "now in bytes");
        return bytes;
    }
}
