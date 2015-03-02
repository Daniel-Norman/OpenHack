package com.danielnorman.openhack;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.danielnorman.openhack.Handlers.ImageUploader;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class PostFragment extends Fragment {

    ImageView mImageView;
    EditText mCaptionEditText;
    Bitmap mBitmap;
    MainActivity mMainActivity;
    String mImagePath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    public void refreshViewWithImage(Bitmap image) {
        mBitmap = image;
        mImageView = (ImageView) getView().findViewById(R.id.image_view);
        if (mImageView != null) mImageView.setImageBitmap(mBitmap);

        mCaptionEditText = (EditText) getView().findViewById(R.id.caption_edittext);
        if (mCaptionEditText != null) mCaptionEditText.setText("");
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public void startImageUpload() {
        if (mBitmap != null) {
            try {
                OutputStream stream = new FileOutputStream(mImagePath);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                ImageUploader uploader = new ImageUploader(mMainActivity);
                uploader.execute(mImagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPost(String imageLink) {
        mMainActivity.mParseHandler.postToParse(mCaptionEditText.getText().toString(), imageLink);
    }
}
