package com.danielnorman.openhack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BottomBar extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.bottom_bar, container, false);
    }
}
