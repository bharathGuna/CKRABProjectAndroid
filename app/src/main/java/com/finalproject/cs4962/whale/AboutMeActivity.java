package com.finalproject.cs4962.whale;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AboutMeActivity extends AppCompatActivity
{

    public static final String CURRENTTEXT = "CURRENTTEXT";
    public static final String NEWTEXT = "NEWTEXT";
    public static final int EDITTEXT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

    }

}
