package com.finalproject.cs4962.whale.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.finalproject.cs4962.whale.R;

public class AboutMeActivity extends AppCompatActivity
{

    public static final String CURRENTTEXT = "CURRENTTEXT";
    public static final String NEWTEXT = "NEWTEXT";
    public static final int EDITTEXT = 1;
    private EditText editText;
    private Button ok,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        final Intent intent = getIntent();
        final String text = (String) intent.getExtras().get(CURRENTTEXT);
        editText = (EditText) findViewById(R.id.editme);
        editText.setText(text);

        ok = (Button) findViewById(R.id.ok_action);
        cancel = (Button) findViewById(R.id.cancel_action);

        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent returnIntent = getIntent();
                returnIntent.putExtra(NEWTEXT, editText.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent returnIntent = getIntent();
                returnIntent.putExtra(CURRENTTEXT, text);
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        String text = (String) getIntent().getExtras().get(CURRENTTEXT);
        Intent returnIntent = getIntent();
        returnIntent.putExtra(CURRENTTEXT, text);
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
