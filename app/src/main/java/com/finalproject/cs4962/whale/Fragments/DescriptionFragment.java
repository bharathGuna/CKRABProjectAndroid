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
    private final String ABOUTME = "ABOUTME";
    private String current = "";
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
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_description, container, false);

        TextView text = (TextView)layout.findViewById(R.id.aboutme);
        text.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        DataManager.getInstance().setOnProfileUpdatedListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent();
        intent.setClass(getActivity(), AboutMeActivity.class);
        TextView text = (TextView) view;
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
                TextView text = (TextView)getActivity().findViewById(R.id.aboutme);
                text.setText(updatedText);
                DataManager.getInstance().updateUserProfile("",updatedText);
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                String updatedText = (String)data.getExtras().get(AboutMeActivity.CURRENTTEXT);
                TextView text = (TextView)getActivity().findViewById(R.id.aboutme);
                text.setText(updatedText);
            }
        }
    }

    @Override
    public void onProfileUpdated(Networking.GenericResponse response)
    {
        String s = response.description + " " + response.success;
    }
}
