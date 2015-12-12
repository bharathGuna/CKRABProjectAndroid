package com.finalproject.cs4962.whale;

import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements DataManager.GetOtherProfileListener
{

    public static String USERID = "USERID";
    private CircularImageView profilePic;
    private TextView name;
    private TextView totalMessage;
    private TextView friendDate;
    private List<Friend> friends;
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
        DrawButton delete = (DrawButton) findViewById(R.id.deleteFriend_activity);
        DrawButton add = (DrawButton) findViewById(R.id.addFriend_activity);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.profileTabs_activity);
        ViewPager viewPager = (ViewPager) findViewById(R.id.profileViewpager_activity);


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

    }

    private View.OnClickListener goToProfile()
    {
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        };

        return listener;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(DescriptionFragment.newInstance(), "About Me");
        adapter.addFragment(ProfileFriendFragment.newInstance(), "Friends");
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
