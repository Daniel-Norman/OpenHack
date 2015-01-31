package com.danielnorman.openhack;

import android.util.Log;

import com.parse.entity.mime.HttpMultipartMode;
import com.parse.entity.mime.MultipartEntity;
import com.parse.entity.mime.content.FileBody;
import com.parse.entity.mime.content.StringBody;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 1/30/15.
 */
class ImgurHandler {

    public static void post(String path) {


        List<NameValuePair> postContent = new ArrayList<NameValuePair>(2);
        postContent.add(new BasicNameValuePair("key", "536eb3e24597369"));
        postContent.add(new BasicNameValuePair("image", path));


        String url = "http://api.imgur.com/2/upload";
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < postContent.size(); index++) {
                if(postContent.get(index).getName().equalsIgnoreCase("image")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(postContent.get(index).getName(), new FileBody(new File (postContent.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(postContent.get(index).getName(), new StringBody(postContent.get(index).getValue()));
                }
            }

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            Map<String,String> mImgurResponse = parseResponse (response);


            Iterator it = mImgurResponse.entrySet().iterator();
            while(it.hasNext()){
                HashMap.Entry pairs = (HashMap.Entry)it.next();

                Log.i("INFO",pairs.getKey().toString());
                if(pairs.getValue()!=null){

                    //reviewEdit.setText(pairs.getValue().toString());

                    Log.i("INFO",pairs.getValue().toString());
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String,String> parseResponse(HttpResponse response) {
        String xmlResponse = null;

        try {
            xmlResponse = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (xmlResponse == null) return null;

        HashMap<String, String> ret = new HashMap<String, String>();
        ret.put("error", getXMLElementValue(xmlResponse, "error_msg"));
        ret.put("delete", getXMLElementValue(xmlResponse, "delete_page"));
        ret.put("original", getXMLElementValue(xmlResponse, "original_image"));

        return ret;
    }

    private static String getXMLElementValue(String xml, String elementName) {
        if (xml.indexOf(elementName) >= 0)
            return xml.substring(xml.indexOf(elementName) + elementName.length() + 1,
                    xml.lastIndexOf(elementName) - 2);
        else
            return null;
    }

}