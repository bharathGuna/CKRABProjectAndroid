package com.finalproject.cs4962.whale;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddConversationActivity extends Activity implements ListAdapter, DataManager.GetFriendsListener, AdapterView.OnItemClickListener, View.OnClickListener
{
    private ListView listView;
    private List<Friend> friends;
    private HashMap<String, String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conversation);

        friends = new ArrayList<>();
        users = new HashMap<>();

        listView = (ListView)findViewById(R.id.add_convo_list);
        listView.setAdapter(this);
        listView.setOnItemClickListener(this);

        FloatingActionButton button = (FloatingActionButton)findViewById(R.id.create_convo_button);
        button.setOnClickListener(this);

        DataManager.getInstance().setGetFriendsListener(this);
        DataManager.getInstance().getFriendsList(DataManager.getInstance().getUserID());
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
        return friends.size();
    }

    @Override
    public Object getItem(int i)
    {
        return friends.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
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
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);

        CircularImageView pic = new CircularImageView(this);
        pic.setImageBitmap(friend.profilePic);
        pic.setName(friend.name);

        TextView name = new TextView(this);
        name.setText(friend.name);
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
    public void onGetFriends(List<Friend> friends)
    {
        this.friends = friends;
        listView.invalidateViews();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Friend friend = (Friend)getItem(i);
        if (users.containsKey(friend.userID))
        {
            users.remove(friend.userID);
            view.setBackgroundColor(getResources().getColor(R.color.background_material_light));

        }
        else
        {
            users.put(friend.userID, friend.name);
            view.setBackgroundColor(getResources().getColor(R.color.selected_item_color));
        }

    }

    @Override
    public void onClick(View view)
    {
        if (view instanceof FloatingActionButton)
        {
            if (users.size() > 0)
            {
                ArrayList<String> ids = new ArrayList<>();
                ids.addAll(users.keySet());

                Intent returnIntent = new Intent();
                String[] names = new String[ids.size()];
                ids.toArray(names);
                returnIntent.putExtra("names", names);
                returnIntent.putExtra("ids", ids);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
