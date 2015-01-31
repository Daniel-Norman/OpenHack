package com.danielnorman.openhack.Handlers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.danielnorman.openhack.MainActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ParseHandler {

    MainActivity mMainActivity;

    ArrayList<ParseObject> mPostArrayList;
    ArrayList<Bitmap> mPostBitmapsArrayList;


    public ParseHandler(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
        this.mPostArrayList = new ArrayList<>();
        this.mPostBitmapsArrayList = new ArrayList<>();
    }

    public void postToParse(final String caption, byte[] imageData) {
        final ParseGeoPoint location = mMainActivity.mLocationHandler.getGeoPoint();
        if (location != null) {
            final ParseFile imageFile = new ParseFile("image.jpeg", imageData);
            imageFile.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        System.out.println("Saved File successfully.");

                        ParseObject postObject = new ParseObject("Post");
                        postObject.put("imageURL", "http://i.imgur.com/0LcGMKl.jpg");
                        postObject.put("caption", caption);
                        postObject.put("locationGeoPoint", location);
                        postObject.put("imageFile", imageFile);

                        postObject.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
                                    showAlert();
                                    mMainActivity.addFragment(mMainActivity.mListViewFragment);
                                    findPosts(true);
                                    System.out.println("Saved Post successfully.");
                                } else {
                                    System.out.println("Error saving post: " + e);
                                }
                            }
                        });
                    } else {
                        System.out.println("Error saving file: " + e);
                    }
                }
            });
        }
    }

    public void showAlert() {
        new AlertDialog.Builder(mMainActivity)
                .setTitle("Post Submitted")
                .setMessage("Your post has been submitted!")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public void findPosts(boolean refreshPosts) {
        ParseGeoPoint location = mMainActivity.mLocationHandler.getGeoPoint();
        if (location != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            if (refreshPosts) {
                if (mMainActivity.mListViewFragment.mPostAdapter != null) mMainActivity.mListViewFragment.mPostAdapter.clear();
                mPostArrayList.clear();
                for (Bitmap bmp : mPostBitmapsArrayList) bmp.recycle();
                mPostBitmapsArrayList.clear();
            } else {
                query.setSkip(mPostArrayList.size());
            }
            query.whereWithinMiles("locationGeoPoint", location, 10);
            query.setLimit(10);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> postList, ParseException e) {
                    if (e == null) {
                        mPostArrayList.addAll(postList);
                        loadImage(mPostBitmapsArrayList.size());
                    } else {
                        Log.d("Parse", "Error: " + e.getMessage());
                    }
                }
            });

        }
    }

    public void loadImage(final int index) {
        if (index >= mPostArrayList.size()) return;
        ParseFile imageParseFile = (ParseFile) mPostArrayList.get(index).get("imageFile");
        imageParseFile.getDataInBackground(new GetDataCallback() {
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (index >= mPostArrayList.size()) return;
                    mMainActivity.mListViewFragment.mPostAdapter.add(mPostArrayList.get(index));
                    mPostBitmapsArrayList.add(bmp);
                    if (mPostBitmapsArrayList.size() < mPostArrayList.size()) {
                        loadImage(index + 1);
                    } else {
                        mMainActivity.mMapFragment.addMarkers();
                    }
                } else {
                    // something went wrong
                }
            }
        });
    }

    public ArrayList<ParseObject> getPostArrayList() {
        return this.mPostArrayList;
    }
    public ArrayList<Bitmap> getPostBitmapsArrayList() {
        return this.mPostBitmapsArrayList;
    }
}
