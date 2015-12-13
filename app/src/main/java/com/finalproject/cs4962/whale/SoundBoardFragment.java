package com.finalproject.cs4962.whale;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class SoundBoardFragment extends Fragment
{

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static SoundBoardFragment newInstance()
    {
        SoundBoardFragment fragment = new SoundBoardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_soundboard_list, container, false);
        tabLayout = (TabLayout) rootLayout.findViewById(R.id.soundboard_tabs);
        viewPager = (ViewPager) rootLayout.findViewById(R.id.soundboard_viewpager);

        viewPager.setAdapter(null);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        return rootLayout;
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(SelfSoundboardFragment.newInstance(), "Personal");
        adapter.addFragment(GlobalSoundboardFragment.newInstance(), "Global");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentNames = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String name)
        {
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