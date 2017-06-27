package com.miymayster.olya.picturesqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.miymayster.olya.picturesqlite.data.ImageContract;
import com.miymayster.olya.picturesqlite.data.ImageCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int GET_IMAGE_INTENT = 100;
    private static final int OPEN_IMAGE_LOADER = 101;
    private static final int GET_IMAGES_LOADER = 102;

    private Uri currentFileUri;
    private Button mChooseAndUploadImage;
    private ImageCursorAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChooseAndUploadImage = (Button) findViewById(R.id.choose_and_upload_image);
        mChooseAndUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, GET_IMAGE_INTENT);
                }
            }
        });
        mImageAdapter = new ImageCursorAdapter(this, null);
        GridView grid = (GridView) findViewById(R.id.images_list);
        grid.setAdapter(mImageAdapter);
        getSupportLoaderManager().initLoader(GET_IMAGES_LOADER, null, new ImageGetFromDBLoaderCallbacks());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_IMAGE_INTENT:
                if (resultCode == RESULT_OK) {
                    currentFileUri = data.getData();
                    mChooseAndUploadImage.setClickable(false);
                    getSupportLoaderManager().initLoader(OPEN_IMAGE_LOADER, null, new ImageOpenLoaderCallbacks());
                }
                break;
        }
    }

    private void saveFileInDB(ArrayList<Byte> data) {
        ContentValues values = new ContentValues();
        int length = data.size();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = data.get(i);
        }
        values.put(ImageContract.ImageEntry.COLUMN_IMAGE, bytes);
        getContentResolver().insert(ImageContract.ImageEntry.CONTENT_URI, values);
    }

    private class ImageGetFromDBLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch (id) {
                case GET_IMAGES_LOADER:
                    String[] projection = new String[]{
                            ImageContract.ImageEntry._ID,
                            ImageContract.ImageEntry.COLUMN_IMAGE
                    };
                    return new CursorLoader(getApplicationContext(), ImageContract.ImageEntry.CONTENT_URI, projection, null, null, null);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (loader.getId() == GET_IMAGES_LOADER) {
                if (data != null) {
                    mImageAdapter.swapCursor(data);
                } else {
                    Log.i(MainActivity.class.getSimpleName(), "no rows found");
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            if (loader.getId() == GET_IMAGES_LOADER) {
                mImageAdapter.swapCursor(null);
            }
        }
    }

    private class ImageOpenLoaderCallbacks implements LoaderManager.LoaderCallbacks<ArrayList<Byte>> {

        @Override
        public Loader<ArrayList<Byte>> onCreateLoader(int id, Bundle args) {
            if (id == OPEN_IMAGE_LOADER) {
                Loader<ArrayList<Byte>> loader = new ImageLoader(getApplicationContext(), currentFileUri);
                currentFileUri = null;
                return loader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Byte>> loader, ArrayList<Byte> data) {
            if (loader.getId() == OPEN_IMAGE_LOADER) {
                mChooseAndUploadImage.setClickable(true);
                getSupportLoaderManager().destroyLoader(OPEN_IMAGE_LOADER);

                if (data == null) {
                    Toast.makeText(getApplicationContext(), "Sorry, can't save this image.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(MainActivity.class.getSimpleName(), "now in bytes");
                    Toast.makeText(getApplicationContext(), "Saving image... \nWait for it to appear on screen.", Toast.LENGTH_SHORT).show();
                    saveFileInDB(data);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Byte>> loader) {

        }

    }
}
