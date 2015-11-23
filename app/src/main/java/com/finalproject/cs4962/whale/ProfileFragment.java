package com.finalproject.cs4962.whale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by Bharath on 11/22/2015.
 */
public class ProfileFragment extends Fragment
{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        LinearLayout mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        //creating the profile pic
        LinearLayout profileInfo = new LinearLayout(getContext());
        profileInfo.setOrientation(LinearLayout.HORIZONTAL);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.whale);
        RoundImage roundImage = new RoundImage(bm);
        ImageView profilePic = new ImageView(getContext());
        profilePic.setImageDrawable(roundImage);
        profileInfo.addView(profilePic,new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));



        return profileInfo ;

    }
}
