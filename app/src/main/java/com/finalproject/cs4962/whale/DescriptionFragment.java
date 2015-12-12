package com.finalproject.cs4962.whale;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
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
    private  EditText text;
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
        text = new EditText(getContext());
        text.setTextSize(30f);
        text.setTextColor(getResources().getColor(R.color.textColorPrimary));
        text.setGravity(Gravity.TOP);
        return text;
    }

    public void setAboutText(String _text)
    {
        text.setText(_text);
    }
    @Override
    public void onStart()
    {
        super.onStart();
    }
}
