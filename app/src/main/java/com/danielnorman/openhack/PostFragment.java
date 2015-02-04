package com.danielnorman.openhack;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class PostFragment extends Fragment {

    ImageView mImageView;
    EditText mCaptionEditText;
    Bitmap mBitmap;
    MainActivity mMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    public void setImage(Bitmap image) {
        mBitmap = image;
        if (mImageView == null) {
            mImageView = (ImageView) getView().findViewById(R.id.image_view);
        }
        if (mCaptionEditText == null) {
            mCaptionEditText = (EditText) getView().findViewById(R.id.caption_edittext);
        }

        mImageView.setImageBitmap(mBitmap);
    }

    public void submitPost() {
        if (mBitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            byte[] imageData = outputStream.toByteArray();

            mMainActivity.mParseHandler.postToParse(mCaptionEditText.getText().toString(), imageData);
        }
    }
}
