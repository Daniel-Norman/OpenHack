package com.danielnorman.openhack;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "QFGpI1loRkUxQSqPq6L3BRvMczGjsQGh1halYtej", "TuPPTR97s9hZbvcQi21Cfy5bpJJam4VKUhvfyMbm");



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
        ParseObject post = new ParseObject("Post");
        ParseGeoPoint testGeoPoint = new ParseGeoPoint(40.0, -30.0);
        post.put("imageURL", "http://i.imgur.com/0LcGMKl.jpg");
        post.put("caption", "Go Bruins!");
        post.put("locationGeoPoint", testGeoPoint);

        post.saveInBackground();
    }

}
