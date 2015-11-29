package com.finalproject.cs4962.whale;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by Bharath on 11/22/2015.
 */
public class ProfileFragment extends Fragment
{

    //components of the fragment
    LinearLayout mainLayout,profileBar,profileInfo;
    CircularImageView profilePic;

    public static ProfileFragment newInstance()
    {
        ProfileFragment fragment = new ProfileFragment();
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        mainLayout = new LinearLayout(getContext());
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        //creating the profile pic
        //eventually will pull this from memory
        profileBar = new LinearLayout(getContext());
        profileBar.setOrientation(LinearLayout.HORIZONTAL);
        //creating the profile pic
        profilePic = new CircularImageView(getContext());
        profilePic.setImageResource(R.drawable.whale2);
        profileBar.addView(profilePic, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //adding the profile information
        profileInfo = new LinearLayout(getContext());
        profileInfo.setOrientation(LinearLayout.VERTICAL);
        String name = "Rajul The Whale";
        String totalMessages = "12321";
        String friendFrom = "05/05/05";
        setUpProfileInfo(profileInfo, name, totalMessages, friendFrom);
        profileBar.addView(profileInfo,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,3));
        return profileBar ;

    }

    //really bad work around to make everything line up
    private LinearLayout setUpProfileInfo(LinearLayout layout, String _name, String _totalMessages, String _friendFrom)
    {

        int maxSize = (int)(getResources().getDisplayMetrics().density * 140f);
        int height = (int)(maxSize * .6f);
        CustomTextView name = new CustomTextView(getContext(),_name,true);
        layout.addView(name, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        CustomTextView totalMessages = new CustomTextView(getContext(),"Messages: "+_totalMessages, false);
        height = (int)(maxSize * .2f);
        layout.addView(totalMessages, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        CustomTextView friendFrom = new CustomTextView(getContext(), "Friended: " + _friendFrom, false);
        layout.addView(friendFrom, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return layout;

    }

}
