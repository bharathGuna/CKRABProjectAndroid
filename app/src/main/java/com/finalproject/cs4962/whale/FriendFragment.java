package com.finalproject.cs4962.whale;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class FriendFragment extends Fragment implements ListAdapter
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
        Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.whale);
        friends = new ArrayList<>();
        for(int i = 0; i < 9; i ++)
        {
            Friend f = new Friend(""+i, ""+i, image, true );
            friends.add(f);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_friends_list, container, false);

    }

    @Override
    public void onStart()
    {
        super.onStart();

        gridView = (GridView) getActivity().findViewById(R.id.friend_list_grid);
        int spacing = (int) (getResources().getDisplayMetrics().density * 5);
        gridView.setHorizontalSpacing(spacing);
        gridView.setVerticalSpacing(spacing);
        gridView.setAdapter(this);
        gridView.setOnItemClickListener(getOnItemClickListener());

    }

    private AdapterView.OnItemClickListener getOnItemClickListener()
    {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent toProfile = new Intent();
                toProfile.setClass(getActivity(), ProfileActivity.class);
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
        int size = (int) (getResources().getDisplayMetrics().widthPixels/gridView.getNumColumns() );
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