package com.finalproject.cs4962.whale.Fragments;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.finalproject.cs4962.whale.Activities.AddFriendActivity;
import com.finalproject.cs4962.whale.Activities.ProfileActivity;
import com.finalproject.cs4962.whale.Views.CircularImageView;
import com.finalproject.cs4962.whale.Networking.DataManager;
import com.finalproject.cs4962.whale.Networking.Friend;
import com.finalproject.cs4962.whale.OnlineIndicatorView;
import com.finalproject.cs4962.whale.R;

import java.util.ArrayList;
import java.util.List;


public class FriendFragment extends Fragment implements ListAdapter, DataManager.GetFriendsListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener
{
    private  GridView gridView;
    private List<Friend> friends;
    public static FriendFragment newInstance()
    {
        FriendFragment fragment = new FriendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DataManager manager = DataManager.getInstance();
        manager.setGetFriendsListener(this);
        manager.getFriendsList(manager.getUserID());
        friends = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        FrameLayout layout = (FrameLayout)inflater.inflate(R.layout.fragment_friends_list, container, false);
        gridView = (GridView) layout.findViewById(R.id.friend_list_grid);
        gridView.setAdapter(this);
        gridView.setOnItemClickListener(getOnItemClickListener());

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)layout.findViewById(R.id.friend_list_refresh);
        refreshLayout.setOnRefreshListener(this);

        FloatingActionButton button = (FloatingActionButton)layout.findViewById(R.id.findFriend);
        button.setOnClickListener(this);

        return layout;

    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    private AdapterView.OnItemClickListener getOnItemClickListener()
    {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Friend friend = (Friend)getItem(i);
                Intent toProfile = new Intent();
                toProfile.setClass(getActivity(), ProfileActivity.class);
                toProfile.putExtra(ProfileActivity.USERID, friend.userID);
                //send the activity person's information
                startActivity(toProfile);
            }
        };



         return listener;
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
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        OnlineIndicatorView indicatorView = new OnlineIndicatorView(getContext());
        CircularImageView imageView;
        TextView name;
        int size = (int) (getResources().getDisplayMetrics().widthPixels/gridView.getNumColumns() * .8f );

        RelativeLayout stateLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (size * .15f), (int) (size * .15f));
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        indicatorView.setLayoutParams(params);
        stateLayout.addView(indicatorView);
        indicatorView.setState(true);
        imageView = new CircularImageView(getContext());
        imageView.setImageBitmap(friend.profilePic);
        imageView.setName(friend.name);

        name = new TextView(getContext());
        name.setText(friend.name);
        name.setLines(3);
        name.setTextSize(getResources().getDisplayMetrics().density * 5f);
        name.setGravity(Gravity.CENTER);
        name.setTextColor(Color.WHITE);
        layout.addView(stateLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(imageView, new LinearLayout.LayoutParams(size,size,3));
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


    @Override
    public void onGetFriends(List<Friend> friends)
    {
        this.friends = friends;
        gridView.invalidateViews();
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.friend_list_refresh);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh()
    {
        DataManager.getInstance().getFriendsList(DataManager.getInstance().getUserID());
    }

    @Override
    public void onClick(View view)
    {
        if (view instanceof FloatingActionButton)
        {
            if (view == getActivity().findViewById(R.id.findFriend))
            {
                Intent toSearchActivity = new Intent();
                toSearchActivity.setClass(getActivity(), AddFriendActivity.class);
                startActivity(toSearchActivity);
            }
        }
    }
}