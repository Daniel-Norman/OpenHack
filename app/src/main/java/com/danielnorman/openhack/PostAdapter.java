package com.danielnorman.openhack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends ArrayAdapter<ParseObject> {

    private final Context context;

    MainActivity mMainActivity;


    public PostAdapter(Context context, MainActivity mainActivity, ArrayList<ParseObject> posts)
    {
        super(context, R.layout.fragment_list_post, posts);
        this.context = context;
        this.mMainActivity = mainActivity;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_list_post, parent, false);
        TextView timeStamp = (TextView) rowView.findViewById(R.id.time_stamp);
        TextView textView = (TextView) rowView.findViewById(R.id.caption);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.post_image);


        if (position >= mMainActivity.mParseHandler.getPostArrayList().size()) return rowView;
        ParseObject post = mMainActivity.mParseHandler.getPostArrayList().get(position);

        textView.setText(post.getString("caption"));
        timeStamp.setText(timeDifferenceFromPost(post));

        if (!mMainActivity.mParseHandler.getPostBitmapsArrayList().isEmpty() &&
                mMainActivity.mParseHandler.getPostBitmapsArrayList().get(position) != null) {
            imageView.setImageBitmap(mMainActivity.mParseHandler.getPostBitmapsArrayList().get(position));
        }


        //Scroll to refresh
        if (position == mMainActivity.mParseHandler.getPostArrayList().size() - 1) {
            mMainActivity.mParseHandler.findPosts(false);
        }


        return rowView;
    }

    public String timeDifferenceFromPost(ParseObject post)
    {
        double difference = ((new Date()).getTime() - post.getCreatedAt().getTime()) / 1000;
        if (difference > 60 * 60 * 24) {
            int days = (int) Math.round(difference / (60 * 60 * 24));
            return days + (days > 1 ? " days " : " day ") + "ago";
        }
        if (difference > 60 * 60) {
            int hours = (int) Math.round(difference / (60 * 60));
            return hours + (hours > 1 ? " hours " : " hour ") + "ago";
        }
        if (difference > 60 * 2) {
            int minutes = (int) Math.round(difference / 60);
            return minutes + " minutes ago";
        }
        return "just now";
    }
}
