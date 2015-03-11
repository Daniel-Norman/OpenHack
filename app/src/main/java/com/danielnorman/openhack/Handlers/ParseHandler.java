package com.danielnorman.openhack.Handlers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.danielnorman.openhack.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseHandler {

    MainActivity mMainActivity;
    Map<String, PostContainer> mPostMap;

    public ParseHandler(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
        this.mPostMap = new HashMap<>();
    }

    public void postToParse(final String caption, final String imageLink) {
        final ParseGeoPoint location = mMainActivity.mLocationHandler.getGeoPoint();
        if (location != null) {
            ParseObject postObject = new ParseObject("Post");
            postObject.put("imageURL", imageLink);
            postObject.put("caption", caption);
            postObject.put("locationGeoPoint", location);

            postObject.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        showAlert();
                        mMainActivity.addFragment(mMainActivity.mListViewFragment);
                        mMainActivity.enableSubmitButton(true);
                        findPosts(true);
                    } else {
                        System.out.println("Error saving post: " + e);
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
            } else {
                if (mMainActivity.mListViewFragment.mPostAdapter != null) {
                    query.setSkip(mMainActivity.mListViewFragment.mPostAdapter.getCount());
                }
            }
            query.whereWithinMiles("locationGeoPoint", location, 10);
            query.setLimit(10);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> postList, ParseException e) {
                    if (e == null) {
                        for (ParseObject o : postList) {
                            mMainActivity.mListViewFragment.mPostAdapter.add(o.getObjectId());
                            if (!mPostMap.containsKey(o.getObjectId())) {
                                loadPost(o);
                            }
                        }
                    } else {
                        Log.d("Parse", "Error: " + e.getMessage());
                    }
                }
            });

        }
    }

    /*
    TODO: Make each post only load the small image. Load big images when in proper spot in ListView.
     */
    public void loadPost(ParseObject post) {
        PostContainer container = new PostContainer(post);
        mPostMap.put(post.getObjectId(), container);

        ImageDownloader downloader = new ImageDownloader(mMainActivity);
        downloader.execute(container);
    }

    public Map<String, PostContainer> getPostMap() {
        return this.mPostMap;
    }
}
