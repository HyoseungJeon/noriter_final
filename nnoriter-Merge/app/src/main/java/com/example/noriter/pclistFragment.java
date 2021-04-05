package com.example.noriter;

/**
 * Created by 전효승 on 2018-03-05.
 */

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class pclistFragment extends Fragment {


    public pclistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pc_listview, container, false);
    }
}
