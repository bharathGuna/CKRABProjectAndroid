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

    public static ProfileFragment newInstance()
    {
        ProfileFragment fragment = new ProfileFragment();
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        LinearLayout mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        //creating the profile pic
        //eventually will pull this from memory
        LinearLayout profileBar = new LinearLayout(getContext());
        profileBar.setOrientation(LinearLayout.HORIZONTAL);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.whale);
        //RoundImage roundImage = new RoundImage(bm);
        //ImageView profilePic = new ImageView(getContext());
        //profilePic.setImageDrawable(roundImage);
        CircularImageView profilePic = new CircularImageView(getContext());
        profilePic.setImageResource(R.drawable.whale2);
        profileBar.addView(profilePic, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //adding the profile information
        LinearLayout profileInfo = new LinearLayout(getContext());


        return profileBar ;

    }


}
