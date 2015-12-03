package com.finalproject.cs4962.whale;

import android.content.res.ColorStateList;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bharath on 11/22/2015.
 */
public class ProfileFragment extends Fragment
{

    //components of the fragment
    LinearLayout mainLayout,profileBar,profileInfo;
    CircularImageView profilePic;
    TabLayout tabLayout;

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
        profilePic.setName("Bharath");
        //profilePic.setImageResource(R.drawable.whale2);
        profileBar.addView(profilePic, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //adding the profile information
        profileInfo = new LinearLayout(getContext());
        profileInfo.setOrientation(LinearLayout.VERTICAL);

        //will need to pull this stuff from the server
        String name = "Rajul The Whale";
        String totalMessages = "12321";
        String friendFrom = "05/05/05";
        setUpProfileInfo(profileInfo, name, totalMessages, friendFrom);
        profileBar.addView(profileInfo, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3));
        mainLayout.addView(profileBar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        RelativeLayout buttonLayout = new RelativeLayout(getContext());

        DrawButton addButton = new DrawButton(getContext());
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addButton.setLayoutParams(params2);
        addButton.setDrawSymbol(drawAddSymbol());
        addButton.setId(1);
        buttonLayout.addView(addButton);


        DrawButton deleteButton = new DrawButton(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.LEFT_OF, 1);
        deleteButton.setLayoutParams(params);
        deleteButton.setDrawSymbol(drawDeleteSymbol());
        buttonLayout.addView(deleteButton);


        mainLayout.addView(buttonLayout);

        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setupViewPager(viewPager);
        tabLayout = new TabLayout(getContext());
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        mainLayout.addView(tabLayout,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return mainLayout ;

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


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(ConversationFragment.newInstance(), "About Me");
        adapter.addFragment(FriendFragment.newInstance(), "Friends");
        viewPager.setAdapter(adapter);
    }

    private void setTabLayout()
    {
        int[][] states = new int[][] {
                new int[] {1}, // enabled
        };

        int[] colors = new int[] {
               Color.WHITE
        };

        ColorStateList myList = new ColorStateList(states, colors);
        tabLayout.setTabTextColors(myList);
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
