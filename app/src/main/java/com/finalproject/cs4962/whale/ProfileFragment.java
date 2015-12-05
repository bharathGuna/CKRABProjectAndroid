package com.finalproject.cs4962.whale;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bharath on 11/22/2015.
 */
//TODO: The issue with the tabs is that the tablayout shares the same memory space as the friends list so
    //need to create a new tablayout or xml file.
public class ProfileFragment extends Fragment
{

    //components of the fragment
    CircularImageView profilePic;
    TextView name, totalMessage, friendDate;
    DrawButton delete, add;
    TabLayout tabLayout;
    ViewPager viewPager;
    String userid;
    boolean mode;

    final static String USERID = "USERID";
    final static String MODE = "MODE";

    public static ProfileFragment newInstance(String id, boolean mode)
    {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USERID,id);
        bundle.putBoolean(MODE, mode);
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //unpacking the bundle.
        Bundle temp = getArguments();
        if(temp != null && temp.containsKey(MODE) && temp.containsKey(USERID))
        {
            //asking manager for my id and check if id is the same
            //if same get the picture that is cached in memory
        }
        else
        {
            //ask the server for information
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_profile,container,false);
    }


    @Override
    public void onStart()
    {
        super.onStart();
        //getting the views.
        profilePic = (CircularImageView)getActivity().findViewById(R.id.profilePic);
        name = (TextView) getActivity().findViewById(R.id.name);
        totalMessage = (TextView) getActivity().findViewById(R.id.totalMessages);
        friendDate = (TextView) getActivity().findViewById(R.id.friended);
        add = (DrawButton)getActivity().findViewById(R.id.addFriend);
        delete = (DrawButton)getActivity().findViewById(R.id.deleteFriend);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.profileTabs);
        viewPager = (ViewPager) getActivity().findViewById(R.id.profileViewpager);

        //setting up the profile pic
        //depending on users or friends

        //setting up the buttons


        //setting up the tablayout
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE,Color.WHITE);

    }

    private DrawButton.DrawSymbol drawDeleteSymbol()
    {
        DrawButton.DrawSymbol drawSymbol = new DrawButton.DrawSymbol()
        {
            @Override
            public void draw(Canvas canvas)
            {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.WHITE);
                int width = canvas.getWidth();
                int height = canvas.getHeight();

                float strokeWidth = width * .1f;
                paint.setStrokeWidth(strokeWidth);
                paint.setStyle(Paint.Style.STROKE);

                float padding = width * .1f;
                canvas.drawLine(padding, padding, width-padding, height-padding, paint);
                canvas.drawLine(padding, height - padding, width-padding, padding,paint);
            }
        };

        return drawSymbol;
    }

    private DrawButton.DrawSymbol drawAddSymbol()
    {
        DrawButton.DrawSymbol drawSymbol = new DrawButton.DrawSymbol()
        {
            @Override
            public void draw(Canvas canvas)
            {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.WHITE);
                int width = canvas.getWidth();
                int height = canvas.getHeight();

                float strokeWidth = width * .1f;
                paint.setStrokeWidth(strokeWidth);
                paint.setStyle(Paint.Style.STROKE);

                float padding = width * .1f;
                canvas.drawLine(width/2, padding, width/2, height-padding, paint);
                canvas.drawLine(padding, height / 2, width - padding, height / 2, paint);
            }
        };

        return drawSymbol;
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(ConversationFragment.newInstance(), "About Me");
        adapter.addFragment(ProfileFriendFragment.newInstance(), "Friends");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentNames = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String name) {
            mFragmentList.add(fragment);
            mFragmentNames.add(name);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
           return mFragmentNames.get(position);
        }
    }

}
