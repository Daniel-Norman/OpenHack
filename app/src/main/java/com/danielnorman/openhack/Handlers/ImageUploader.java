package com.danielnorman.openhack.Handlers;

import android.os.AsyncTask;

import com.danielnorman.openhack.MainActivity;
import com.parse.entity.mime.HttpMultipartMode;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.FileBody;
import com.parse.entity.mime.content.StringBody;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Daniel on 2/23/15.
 */
public class ImageUploader extends AsyncTask<String, Void, String> {

    MainActivity mMainActivity;

    public ImageUploader(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Override
    public String doInBackground(String... params) {
        final String upload_to = "https://api.imgur.com/3/upload.json";
        final String API_key = "API_KEY";

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(upload_to);

        httpPost.setHeader("Authorization", "Client-ID "+ "536eb3e24597369");

        try {
            final MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("image", new FileBody(new File(params[0])));
            entity.addPart("key", new StringBody(API_key));

            httpPost.setEntity(entity);

            final HttpResponse response = httpClient.execute(httpPost,
                    localContext);

            final String response_string = EntityUtils.toString(response
                    .getEntity());

            final JSONObject json = new JSONObject(response_string);


            if (json.getBoolean("success")) {
                JSONObject results = (JSONObject) json.get("data");
                System.out.println("Success! Image link: " + results.optString("link"));
                return results.getString("link");
            }
            else {
                System.out.println("Error posting image. :(");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            mMainActivity.mPostFragment.uploadPost(result);
        }
    }
}