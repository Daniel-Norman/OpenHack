package com.danielnorman.openhack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielnorman.openhack.Handlers.PostContainer;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends ArrayAdapter<String> {

    private final Context context;

    MainActivity mMainActivity;


    public PostAdapter(Context context, MainActivity mainActivity, ArrayList<String> postIDs)
    {
        super(context, R.layout.fragment_list_post, postIDs);
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


        if (position >= getCount()) return rowView;

        /*
        TODO get PostContainer by looking at Dictionary, using getItem(position) as key
         */

        PostContainer post = mMainActivity.mParseHandler.getPostMap().get(getItem(position));
        if (post == null) return rowView;

        textView.setText(post.getParseObject().getString("caption"));
        timeStamp.setText(timeDifferenceFromPost(post.getParseObject()));

        if (post.getBitmap() != null) imageView.setImageBitmap(post.getBitmap());


        //Scroll to refresh
        if (position == getCount() - 1) {
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
