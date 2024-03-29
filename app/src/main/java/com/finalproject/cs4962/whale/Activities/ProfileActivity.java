package com.finalproject.cs4962.whale.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.finalproject.cs4962.whale.Views.CircularImageView;
import com.finalproject.cs4962.whale.Networking.DataManager;
import com.finalproject.cs4962.whale.Fragments.DescriptionFragment;
import com.finalproject.cs4962.whale.Views.DrawButton;
import com.finalproject.cs4962.whale.Networking.Friend;
import com.finalproject.cs4962.whale.Networking.OtherProfileInfo;
import com.finalproject.cs4962.whale.Fragments.ProfileFriendFragment;
import com.finalproject.cs4962.whale.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements DataManager.GetOtherProfileListener
{

    public static String USERID = "USERID";
    private CircularImageView profilePic;
    private TextView name;
    private TextView totalMessage;
    private TextView friendDate;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawButton delete;
    private DrawButton add;
    private ArrayList<Friend> friends;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //need to launch an async task to retrieve the profileInfo
         profilePic = (CircularImageView) findViewById(R.id.profilePic_activity);
         name = (TextView) findViewById(R.id.name_activity);
         totalMessage = (TextView) findViewById(R.id.totalMessages_activity);
         friendDate = (TextView) findViewById(R.id.friended_activity);



        //check if the totalMessage sent is 0

        delete = (DrawButton) findViewById(R.id.deleteFriend_activity);
        add = (DrawButton) findViewById(R.id.addFriend_activity);
        add.setDrawSymbol(drawAddSymbol());
        delete.setDrawSymbol(drawDeleteSymbol());

        tabLayout = (TabLayout) findViewById(R.id.profileTabs_activity);
        viewPager = (ViewPager) findViewById(R.id.profileViewpager_activity);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        Intent intent = getIntent();
        String userId = (String) intent.getExtras().get(USERID);


        DataManager.getInstance().setGetOtherProfileListener(this);
        DataManager.getInstance().getOtherProfile(userId);

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
                canvas.drawLine(width / 2, padding, width / 2, height - padding, paint);
                canvas.drawLine(padding, height / 2, width - padding, height / 2, paint);
            }
        };

        return drawSymbol;
    }

    @Override
    public void onGetOtherProfile(OtherProfileInfo profile)
    {

        profilePic.setImageBitmap(profile.profilePic);
        profilePic.setName(profile.name);
        name.setText(profile.name);
        totalMessage.append(" " + profile.messages);
        friendDate.append(" " + profile.friended);
        friends = (ArrayList)profile.friends;
        if(profile.messages == 0)
        {
            delete.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
        }
        else
        {
            delete.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(DescriptionFragment.newInstance(), "About Me");
        adapter.addFragment(ProfileFriendFragment.newInstance(friends), "Friends");
        viewPager.setAdapter(adapter);
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
