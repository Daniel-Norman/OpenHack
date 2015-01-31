package com.danielnorman.openhack;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    LocationHandler mLocationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "QFGpI1loRkUxQSqPq6L3BRvMczGjsQGh1halYtej", "TuPPTR97s9hZbvcQi21Cfy5bpJJam4VKUhvfyMbm");

        mLocationHandler = new LocationHandler(this);
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


    public void postToParse(View view) {
        findParsePosts();
        /*
        ParseGeoPoint location = mLocationHandler.getGeoPoint();
        if (location != null) {
            ParseObject postObject = new ParseObject("Post");
            postObject.put("imageURL", "http://i.imgur.com/0LcGMKl.jpg");
            postObject.put("caption", "Go Bruins!");
            postObject.put("locationGeoPoint", location);

            postObject.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        System.out.println("Saved Post successfully.");
                    } else {
                        System.out.println("Error saving post: " + e);
                    }
                }
            });
        }*/
    }

    public void findParsePosts() {
        ParseGeoPoint location = mLocationHandler.getGeoPoint();
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
