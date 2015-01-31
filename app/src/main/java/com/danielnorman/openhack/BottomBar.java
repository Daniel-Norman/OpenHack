package com.danielnorman.openhack;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Bottom Bar class fragment
 */
public class BottomBar extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.bottom_bar, container, false);
    }

    public void toMap(View view) {

    }

    public void toList(View view) {
        
    }


}
