package com.finalproject.cs4962.whale.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.finalproject.cs4962.whale.CircularImageView;
import com.finalproject.cs4962.whale.DataManager;
import com.finalproject.cs4962.whale.Networking;
import com.finalproject.cs4962.whale.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bharath on 11/22/2015.
 */
//TODO: The issue with the tabs is that the tablayout shares the same memory space as the friends list so
    //need to create a new tablayout or xml file.
public class ProfileFragment extends Fragment implements DataManager.GetUserProfileListener
{

    //components of the fragment
    CircularImageView profilePic;
    TextView name, totalMessage;
    TabLayout tabLayout;
    ViewPager viewPager;

    private static final int SELECT_SINGLE_PICTURE = 101;
    public static final String IMAGE_TYPE = "image/*";
    public static ProfileFragment newInstance()
    {
        ProfileFragment fragment = new ProfileFragment();
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_profile,container,false);
        ViewFlipper layout = (ViewFlipper)inflater.inflate(R.layout.fragment_profile,container,false);
        profilePic = (CircularImageView)layout.findViewById(R.id.profilePic);
        name = (TextView) layout.findViewById(R.id.name);
        totalMessage = (TextView) layout.findViewById(R.id.totalMessages);
        tabLayout = (TabLayout) layout.findViewById(R.id.profileTabs);
        viewPager = (ViewPager) layout.findViewById(R.id.profileViewpager);

        //setting up the profile pic
        //depending on users or friends

        //setting up the buttons


       viewPager.removeAllViews();
       viewPager.setAdapter(null);
       setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        profilePic.setOnClickListener(selectPicture());
        return layout;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        DataManager dm = DataManager.getInstance();
        dm.setGetUserProfileListener(this);
        dm.getUserProfile();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private View.OnClickListener selectPicture()
    {
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Image"), SELECT_SINGLE_PICTURE);
            }
        };

        return listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1)
        {
            if (requestCode == SELECT_SINGLE_PICTURE)
            {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap pic = BitmapFactory.decodeFile(picturePath);
                profilePic.setImageBitmap(pic);
                String img = DataManager.getInstance().bitmapToString(pic);
                DataManager.getInstance().updateUserProfile(img, "");

            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(DescriptionFragment.newInstance(), "About Me");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onGetProfile(Networking.PersonalProfileResponse profile)
    {
        //need to set the profile with the correct information

        name.setText(profile.name);
        totalMessage.setText("Total Messages: "+ profile.messages);
        //setting the about text
        ViewPagerAdapter adapter = (ViewPagerAdapter)viewPager.getAdapter();
        DescriptionFragment fragment = (DescriptionFragment)adapter.getItem(0);
        fragment.setAboutText(profile.about);

        Bitmap bm = DataManager.getInstance().stringToBitmap(profile.profilePic);
        profilePic.setImageBitmap(bm);
        profilePic.setName(profile.name);

        ViewFlipper flipper = (ViewFlipper)getActivity().findViewById(R.id.viewflipper);
        flipper.showNext();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter
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
