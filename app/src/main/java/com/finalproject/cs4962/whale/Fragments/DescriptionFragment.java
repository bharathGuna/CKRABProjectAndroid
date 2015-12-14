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
public class DescriptionFragment extends Fragment implements View.OnClickListener, DataManager.OnProfileUpdatedListener
{
    private TextView text;
    FloatingActionButton button;
    private final String ABOUTME = "ABOUTME";
    private String backlog;
    public static DescriptionFragment newInstance()
    {
        return new DescriptionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DataManager.getInstance().setOnProfileUpdatedListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_description,container,false);

        text = (TextView)layout.findViewById(R.id.aboutme);
        text.setOnClickListener(this);
        if(savedInstanceState != null && savedInstanceState.containsKey(ABOUTME))
        {
            setAboutText((String) savedInstanceState.get(ABOUTME));
        }

        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if( backlog != null)
        {
            setAboutText(backlog);
        }
    }

    public void setAboutText(String _text)
    {
        if(text != null)
        {
            text.setText(_text);
        }
        else
        {
            backlog = _text;
        }


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
        startActivityForResult(intent,3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String updatedText = (String)data.getExtras().get(AboutMeActivity.NEWTEXT);
                setAboutText(updatedText);
                DataManager.getInstance().updateUserProfile("",updatedText);
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                String updatedText = (String)data.getExtras().get(AboutMeActivity.CURRENTTEXT);
                setAboutText(updatedText);
            }
        }
    }

    @Override
    public void onProfileUpdated(Networking.GenericResponse response)
    {
        String s = response.description + " " + response.success;
        Toast toast = Toast.makeText(getContext(),s,Toast.LENGTH_LONG);
    }
}
