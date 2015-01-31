package com.danielnorman.openhack.Handlers;

import android.util.Log;

import com.danielnorman.openhack.Handlers.LocationHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class ParseHandler {

    public static void postToParse(LocationHandler locationHandler, byte[] imageData) {
        final ParseGeoPoint location = locationHandler.getGeoPoint();
        if (location != null) {
            final ParseFile imageFile = new ParseFile("image.jpeg", imageData);
            imageFile.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        System.out.println("Saved File successfully.");

                        ParseObject postObject = new ParseObject("Post");
                        postObject.put("imageURL", "http://i.imgur.com/0LcGMKl.jpg");
                        postObject.put("caption", "Go Bruins!");
                        postObject.put("locationGeoPoint", location);
                        postObject.put("imageFile", imageFile);

                        postObject.saveInBackground(new SaveCallback() {
                            public void done(ParseException e) {
                                if (e == null) {
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

    public static void findParsePosts(LocationHandler locationHandler) {
        ParseGeoPoint location = locationHandler.getGeoPoint();
        if (location != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.whereWithinMiles("locationGeoPoint", location, 1);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> postList, ParseException e) {
                    if (e == null) {
                        for (ParseObject post : postList) {
                            Log.d("Parse", post.getString("caption"));
                        }
                    } else {
                        Log.d("Parse", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }
}
