package com.finalproject.cs4962.whale.Activities;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.finalproject.cs4962.whale.Views.CircularImageView;
import com.finalproject.cs4962.whale.Networking.DataManager;
import com.finalproject.cs4962.whale.Networking.Networking;
import com.finalproject.cs4962.whale.R;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity implements ListAdapter, AdapterView.OnItemClickListener, DataManager.OnUserFoundListener
{
    private ListView listView;
    private EditText editText;
    private ViewFlipper viewFlipper;
    private boolean flipped = false;
    private List<Networking.FindUserResponse> foundUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        foundUser = new ArrayList<>();

        listView = (ListView)findViewById(R.id.search_list_view);
        listView.setAdapter(this);
        listView.setOnItemClickListener(this);

        editText = (EditText)findViewById(R.id.search_text);
        viewFlipper = (ViewFlipper)findViewById(R.id.search_flipper);

        DataManager.getInstance().setOnUserFoundListener(this);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void searchPressed(View view)
    {
        String name = editText.getText().toString();
        if (name == "")
            return;

        DataManager.getInstance().findUserByUsername(name);

    }

    @Override
    public void onUserFound(Networking.FindUserResponse response)
    {
        if (response.userID.equals("NOT FOUND"))
        {
            flipped = true;
            viewFlipper.showNext();
        }
        else
        {
            if (flipped)
                viewFlipper.showPrevious();
            foundUser = new ArrayList<>();
            foundUser.add(response);
            listView.invalidateViews();
        }
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
        return foundUser.size();
    }

    @Override
    public Object getItem(int i)
    {
        return foundUser.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        Networking.FindUserResponse user = (Networking.FindUserResponse) getItem(i);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);

        CircularImageView pic = new CircularImageView(this);
        Bitmap bm = DataManager.getInstance().stringToBitmap(user.profilePic);
        pic.setImageBitmap(bm);
        pic.setName(user.username);

        TextView name = new TextView(this);
        name.setText(user.username);
        name.setTextColor(getResources().getColor(R.color.textColorPrimary));
        name.setTextSize(getResources().getDisplayMetrics().scaledDensity * 6);
        name.setGravity(Gravity.CENTER_VERTICAL);
        int padding = (int) (4.0f * getResources().getDisplayMetrics().density);
        name.setPadding(3*padding, 0, 0, 0);

        rootLayout.addView(pic, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
        rootLayout.addView(name, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 7));
        rootLayout.setPadding(3*padding, 2*padding, padding, 2*padding);
        return rootLayout;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Networking.FindUserResponse user = (Networking.FindUserResponse)getItem(i);

        finish();
        Intent toProfileIntent = new Intent();
        toProfileIntent.setClass(this, ProfileActivity.class);
        toProfileIntent.putExtra(ProfileActivity.USERID, user.userID);
        startActivity(toProfileIntent);
    }

}
