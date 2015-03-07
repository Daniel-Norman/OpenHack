package com.danielnorman.openhack;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.danielnorman.openhack.Handlers.CameraHandler;
import com.danielnorman.openhack.Handlers.LocationHandler;
import com.danielnorman.openhack.Handlers.ParseHandler;
import com.parse.Parse;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity {
    public static int REQUEST_TAKE_PHOTO = 2;
    public static int REQUEST_CROP_PHOTO = 3;
    public final int IMAGE_SHRINK_FACTOR = 2; //1 for displaying images as big as the ImageViews, 2 for half as big, etc

    public LocationHandler mLocationHandler;
    public CameraHandler mCameraHandler;
    public ParseHandler mParseHandler;

    public MapFragment mMapFragment;
    public ListViewFragment mListViewFragment;
    public PostFragment mPostFragment;

    private Timer mLoadPostsTimer;

    public int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "QFGpI1loRkUxQSqPq6L3BRvMczGjsQGh1halYtej", "TuPPTR97s9hZbvcQi21Cfy5bpJJam4VKUhvfyMbm");

        mLocationHandler = new LocationHandler(this);
        mCameraHandler = new CameraHandler(this);
        mParseHandler = new ParseHandler(this);

        mMapFragment = new MapFragment();
        mListViewFragment = new ListViewFragment();
        mPostFragment = new PostFragment();

        mPostFragment.setMainActivity(this);
        mMapFragment.setMainActivity(this);
        mListViewFragment.setMainActivity(this);

        mParseHandler.findPosts(true);

        addFragment(mListViewFragment);

        mLoadPostsTimer = new Timer();
        mLoadPostsTimer.schedule(new LoadTask(), 0, 100);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationHandler.mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationHandler.mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    class LoadTask extends TimerTask {
        public void run() {
            if (mLocationHandler.getGeoPoint() != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mParseHandler.findPosts(true);
                    }
                });
                mLoadPostsTimer.cancel();
            }
        }
    }


    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_layout, fragment).commit();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_button:
                addFragment(mPostFragment);
                mCameraHandler.dispatchTakePictureIntent();
                break;
            case R.id.list_button:
                addFragment(mListViewFragment);
                mParseHandler.findPosts(true);
                break;
            case R.id.map_button:
                addFragment(mMapFragment);
                mParseHandler.findPosts(true);
                break;
            case R.id.submit_button:
                if (mPostFragment.mImageView == null) {
                    //Don't have an image, so go back to list view
                    addFragment(mListViewFragment);
                    mParseHandler.findPosts(true);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPostFragment.mCaptionEditText.getWindowToken(), 0);
                    mPostFragment.startImageUpload();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_CANCELED)
        {
            addFragment(mListViewFragment);
            mParseHandler.findPosts(true);
        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = mCameraHandler.getImageUri();

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(imageUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 1024);
            cropIntent.putExtra("outputY", 1024);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, REQUEST_CROP_PHOTO);
        }
        if (requestCode == REQUEST_CROP_PHOTO && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mCameraHandler.getImageUri());
                mPostFragment.refreshViewWithImage(bitmap);
                mPostFragment.setImagePath(mCameraHandler.getImageUri().getPath());
            } catch (Exception e) {}
        }
    }
}
