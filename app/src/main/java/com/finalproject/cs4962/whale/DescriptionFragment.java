package com.finalproject.cs4962.whale;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Khong on 12/5/15.
 */
public class DescriptionFragment extends Fragment implements View.OnClickListener
{
    private TextView text;
    FloatingActionButton button;
    private final String ABOUTME = "ABOUTME";
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
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_description,container,false);

        text = (TextView)layout.findViewById(R.id.aboutme);
        text.setOnClickListener(this);
        return layout;
    }

    public void setAboutText(String _text)
    {
        text.setText(_text);

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(ABOUTME, text.getText().toString());
    }


    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AboutMeActivity.class);
        intent.putExtra(AboutMeActivity.CURRENTTEXT, text.getText().toString());
        startActivityForResult(intent,AboutMeActivity.EDITTEXT);
    }
}
