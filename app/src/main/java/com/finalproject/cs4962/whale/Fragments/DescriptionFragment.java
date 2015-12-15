package com.finalproject.cs4962.whale.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.cs4962.whale.Activities.AboutMeActivity;
import com.finalproject.cs4962.whale.DataManager;
import com.finalproject.cs4962.whale.Networking;
import com.finalproject.cs4962.whale.R;

/**
 * Created by Khong on 12/5/15.
 */
public class DescriptionFragment extends Fragment
{
    private final static String ABOUTME = "ABOUTME";

    public static DescriptionFragment newInstance(String about)
    {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ABOUTME,about);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_description, container, false);

        return layout;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if(getArguments() != null && getArguments().containsKey(ABOUTME))
        {
            setAboutText((String)getArguments().get(ABOUTME));
        }
    }

    private void setAboutText(String _text)
    {
        TextView text = (TextView)getActivity().findViewById(R.id.aboutme);
        text.setText(_text);
    }



}
