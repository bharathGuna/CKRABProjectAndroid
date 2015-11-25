package com.finalproject.cs4962.whale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SoundBoardFragment extends Fragment{

    public static SoundBoardFragment newInstance()
    {
        SoundBoardFragment fragment = new SoundBoardFragment();
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return new TextView(getContext());

    }


}