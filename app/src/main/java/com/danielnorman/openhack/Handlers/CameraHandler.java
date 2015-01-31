package com.danielnorman.openhack.Handlers;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.danielnorman.openhack.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraHandler {
    MainActivity mMainActivity;
    String mCurrentPhotoPath;
    Uri mImageUri;
    public File mImageFile;


    public CameraHandler(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }


    public Uri getImageUri() {
        return mImageUri;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }



    public void dispatchTakePictureIntent() {
        //new Intent(Intent.ACTION_PICK, MediaStore.ACTION_IMAGE_CAPTURE);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mMainActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                mImageFile = photoFile;
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mImageUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                mMainActivity.startActivityForResult(takePictureIntent, mMainActivity.REQUEST_TAKE_PHOTO);
            }
        }
    }
}
