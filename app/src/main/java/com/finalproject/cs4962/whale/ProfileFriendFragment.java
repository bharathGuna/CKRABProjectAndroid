package com.finalproject.cs4962.whale;

import android.support.v4.app.Fragment;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by Bharath on 12/5/2015.
 */
public class ProfileFriendFragment extends Fragment implements ListAdapter
{
    private GridView gridView;
    public static ProfileFriendFragment newInstance()
    {
        ProfileFriendFragment fragment = new ProfileFriendFragment();
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
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_profile_friends_list, container, false);
        gridView = (GridView) layout.findViewById(R.id.profile_friend_list_grid);
        gridView.setAdapter(this);
        return layout;

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
        return 5; // Data manager .count
    }

    @Override
    public Object getItem(int i)
    {
        return null; // Object associated with that position
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
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        CircularImageView imageView;
        TextView name;
        int size = (int) (getResources().getDisplayMetrics().widthPixels/gridView.getNumColumns() );
        imageView = new CircularImageView(getActivity());
        imageView.setImageResource(R.drawable.whale);
        name = new TextView(getActivity());
        name.setText("Bharath Gunasekaran");
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
