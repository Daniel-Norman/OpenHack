package com.danielnorman.openhack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListFragment;

import com.parse.ParseObject;

public class ListViewFragment extends ListFragment {

    MainActivity mMainActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Post p1 = new Post("aasdf","This is a nice picture.");
        Post p2 = new Post("asd","I really like this pic of me and the bae.");
        Post p3 = new Post("", "Nomnomnom! Delicious sandwich");
        Post[] posts = new Post[] {p1, p2, p3};

        PostAdapter adapter1 = new PostAdapter( getActivity(), mMainActivity,
                mMainActivity.mParseHandler.getPostArrayList().toArray(new ParseObject[0]));
        setListAdapter(adapter1);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
} 