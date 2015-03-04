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
            Bitmap myBitmap = decodeSampledBitmapFromStream(input,
                    mMainActivity.mScreenWidth / mMainActivity.IMAGE_SHRINK_FACTOR,
                    mMainActivity.mScreenWidth / mMainActivity.IMAGE_SHRINK_FACTOR);
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

    public static int calculateInSampleSize(int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = 1024;
        final int width = 1024;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream is, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(reqWidth, reqHeight);

        return BitmapFactory.decodeStream(is, null, options);
    }
}
