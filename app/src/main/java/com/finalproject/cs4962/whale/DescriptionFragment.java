package com.finalproject.cs4962.whale;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Khong on 12/5/15.
 */
public class DescriptionFragment extends Fragment
{
    public static DescriptionFragment newInstance()
    {
        return new DescriptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return new EditText(getContext());
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }
}