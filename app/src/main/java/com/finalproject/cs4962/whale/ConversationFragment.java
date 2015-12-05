package com.finalproject.cs4962.whale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ConversationFragment extends Fragment
{

    public static ConversationFragment newInstance()
    {
        ConversationFragment fragment = new ConversationFragment();
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
        /* GOtta get this to line up */
        LinearLayout rootLayout = new LinearLayout(getActivity());
        CircularImageView profile = new CircularImageView(getActivity());
        profile.setImageResource(R.drawable.whale);
        WaveView msg = new WaveView(getActivity());
        rootLayout.addView(profile, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
        rootLayout.addView(msg, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 10));
        return rootLayout;
    }


}