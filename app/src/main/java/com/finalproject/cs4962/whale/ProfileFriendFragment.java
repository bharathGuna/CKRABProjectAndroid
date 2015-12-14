package com.finalproject.cs4962.whale;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bharath on 12/5/2015.
 */
public class ProfileFriendFragment extends Fragment implements ListAdapter
{
    private GridView gridView;
    private ArrayList<Friend> friends;
    private static String FRIENDS = "FRIENDS";
    public static ProfileFriendFragment newInstance(ArrayList<Friend> friendsList)
    {
        ProfileFriendFragment fragment = new ProfileFriendFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(FRIENDS, friendsList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        if(getArguments() != null && getArguments().containsKey(FRIENDS))
        {
            friends = getArguments().getParcelableArrayList(FRIENDS);
        }
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_profile_friends_list, container, false);
        gridView = (GridView) layout.findViewById(R.id.profile_friend_list_grid);
        gridView.setAdapter(this);
        gridView.setOnItemClickListener(getOnItemClickListener());
        return layout;

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
        CircularImageView imageView;
        TextView name;
        int size = (int) (getResources().getDisplayMetrics().widthPixels/gridView.getNumColumns() * .8f );
        imageView = new CircularImageView(getContext());
        imageView.setImageBitmap(friend.profilePic);
        imageView.setName(friend.name);
        name = new TextView(getContext());
        name.setText(friend.name);
        name.setLines(3);
        name.setTextSize(getResources().getDisplayMetrics().density * 5f);
        name.setGravity(Gravity.CENTER);
        name.setTextColor(Color.WHITE);
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



}
