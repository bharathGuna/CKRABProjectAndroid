package com.finalproject.cs4962.whale;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements DataManager.GetOtherProfileListener, ListAdapter
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

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int i)
    {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver)
    {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver)
    {

    }

    @Override
    public int getCount()
    {
        return friends.size(); // Data manager .count
    }

    @Override
    public Object getItem(int i)
    {
        return friends.get(i); // Object associated with that position
    }

    @Override
    public long getItemId(int i)
    {
        return 0; // Conversion from grid view position to data position
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        Friend friend = (Friend)getItem(i);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        CircularImageView imageView;
        TextView name;
      //  int size = (int) (getResources().getDisplayMetrics().widthPixels/gridView.getNumColumns() * .8f );
        imageView = new CircularImageView(this);
        imageView.setImageBitmap(friend.profilePic);
        imageView.setName(friend.name);
        name = new TextView(this);
        name.setText(friend.name);
        name.setLines(3);
        name.setTextSize(getResources().getDisplayMetrics().density * 5f);
        name.setGravity(Gravity.CENTER);
        name.setTextColor(Color.WHITE);
        //layout.addView(imageView, new LinearLayout.LayoutParams(size,size,3));
        layout.addView(name, new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));


        return layout;
    }

    @Override
    public int getItemViewType(int i)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        return getCount() > 0;
    }
}
