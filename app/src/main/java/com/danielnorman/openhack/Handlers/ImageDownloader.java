package com.danielnorman.openhack.Handlers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.danielnorman.openhack.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel on 2/28/15.
 */
public class ImageDownloader extends AsyncTask<PostContainer, Void, Boolean> {

    MainActivity mMainActivity;

    public ImageDownloader(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Override
    public Boolean doInBackground(PostContainer... params) {
        try {
            URL url = new URL(params[0].getParseObject().getString("imageURL"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            if (myBitmap != null) {
                params[0].setBitmap(myBitmap);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        mMainActivity.mListViewFragment.mPostAdapter.notifyDataSetChanged();
    }
}
