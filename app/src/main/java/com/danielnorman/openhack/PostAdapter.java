package com.danielnorman.openhack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jtink_000 on 1/31/2015.
 */
public class PostAdapter extends ArrayAdapter<Post> {

    private final Context context;
    private final Post[] posts;

    public PostAdapter(Context context, Post[] posts)
    {
        super(context, R.layout.fragment_list_post, posts);
        this.context = context;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_list_post, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.caption);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.post_image);
        textView.setText(posts[position].getCaption());  //set caption
        //For now set the image to just the simple image from drawable
        imageView.setImageResource(R.drawable.dumba);
        /*
        String path = posts[position].getPathToImage();
        //Set resource from image
         */

        return rowView;
    }
}
