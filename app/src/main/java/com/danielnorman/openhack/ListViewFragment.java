package com.danielnorman.openhack;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.app.ListFragment;

import com.parse.ParseObject;

import java.util.ArrayList;

public class ListViewFragment extends ListFragment {

    MainActivity mMainActivity;
    public PostAdapter mPostAdapter;

    public void setMainActivity(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPostAdapter = new PostAdapter( getActivity(), mMainActivity, new ArrayList<ParseObject>()); //mMainActivity.mParseHandler.getPostArrayList());
        setListAdapter(mPostAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
} 