package com.danielnorman.openhack;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.danielnorman.openhack.Handlers.CameraHandler;
import com.danielnorman.openhack.Handlers.LocationHandler;
import com.danielnorman.openhack.Handlers.ParseHandler;
import com.parse.Parse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    public static int REQUEST_TAKE_PHOTO = 2;
    public static int REQUEST_CROP_PHOTO = 3;


    public LocationHandler mLocationHandler;
    CameraHandler mCameraHandler;
    ParseHandler mParseHandler;
    MapFragment mMapFragment;
    public ListViewFragment mListViewFragment;
    PostFragment mPostFragment;

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
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_layout, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_button:
                addFragment(mPostFragment);
                mCameraHandler.dispatchTakePictureIntent();
                break;
            case R.id.list_button:
                addFragment(mListViewFragment);
                break;
            case R.id.map_button:
                addFragment(mMapFragment);
                break;
            case R.id.submit_button:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPostFragment.mCatpionEditText.getWindowToken(), 0);
                mPostFragment.submitPost();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                mPostFragment.setImage(bitmap);
            } catch (Exception e) {}
        }
    }


}
