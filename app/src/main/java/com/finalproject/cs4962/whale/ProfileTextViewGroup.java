package com.finalproject.cs4962.whale;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Bharath on 11/27/2015.
 */
public class ProfileTextViewGroup extends ViewGroup
{

    private final int MAXNUMBERVIEWS = 3;

    public ProfileTextViewGroup(Context context)
    {
        super(context);
    }

    @Override
    public void addView(View child)
    {
        if(child instanceof TextView)
        {
            super.addView(child);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {

    }
}
