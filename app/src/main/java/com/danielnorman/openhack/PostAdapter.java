package com.danielnorman.openhack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielnorman.openhack.Handlers.ParseHandler;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by jtink_000 on 1/31/2015.
 */
public class PostAdapter extends ArrayAdapter<ParseObject> {

    private final Context context;

    MainActivity mMainActivity;


    public PostAdapter(Context context, MainActivity mainActivity, ParseObject[] posts)
    {
        super(context, R.layout.fragment_list_post, posts);
        this.context = context;
        this.mMainActivity = mainActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_list_post, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.caption);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.post_image);


        textView.setText(mMainActivity.mParseHandler.getPostArrayList().get(position).getString("caption"));  //set caption
        if (mMainActivity.mParseHandler.getPostBitmapsArrayList().get(position) != null) {
            System.out.println(mMainActivity.mParseHandler.getPostBitmapsArrayList().get(position).getWidth() + " " +
                    mMainActivity.mParseHandler.getPostBitmapsArrayList().get(position).getHeight());
            imageView.setImageBitmap(mMainActivity.mParseHandler.getPostBitmapsArrayList().get(position));
        }
        //For now set the image to just the simple image from drawable
        //imageView.setImageResource(R.drawable.dumba);
        /*
        String path = posts[position].getPathToImage();
        //Set resource from image
         */

        return rowView;
    }
}
