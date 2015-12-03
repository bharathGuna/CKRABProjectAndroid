package com.finalproject.cs4962.whale;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FriendFragment extends Fragment implements ListAdapter
{

    public static FriendFragment newInstance()
    {
        FriendFragment fragment = new FriendFragment();
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
        return inflater.inflate(R.layout.fragment_friends_list, container, false);

//        RelativeLayout mainLayout = new RelativeLayout(getContext());
//
//        LinearLayout topBanner = new LinearLayout(getContext());
//        topBanner.setOrientation(LinearLayout.VERTICAL);
//        //fragment title
//        CustomTextView title = new CustomTextView(getContext(), "Friends", false);
//        title.setTextSize(getResources().getDisplayMetrics().density * 10);
//        topBanner.addView(title, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
//
//
//        CustomTextView friendsOnline = new CustomTextView(getContext());
//        friendsOnline.setText("0 of 0 friends online");
//        topBanner.addView(friendsOnline, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
//
//        RelativeLayout.LayoutParams bannerPrams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        bannerPrams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        topBanner.setLayoutParams(bannerPrams);
//        topBanner.setId(0);
//
//        GridView myFriends = new GridView(getContext());
//        int spacing = (int) (getResources().getDisplayMetrics().density * 5);
//        myFriends.setNumColumns(GridView.AUTO_FIT);
//        myFriends.setVerticalSpacing(spacing);
//        myFriends.setHorizontalSpacing(spacing);
//        myFriends.setGravity(Gravity.CENTER);
//        myFriends.setAdapter(new ImageAdapter(getContext()));
//        mainLayout.addView(topBanner, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//
//        return mainLayout;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        GridView gridView = (GridView) getActivity().findViewById(R.id.friend_list_grid);
        gridView.setAdapter(this);
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
        return 9; // Data manager .count
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
        CircularImageView imageView;
        // if it's not recycled, initialize some attributes
        imageView = new CircularImageView(getActivity());
    //        imageView.setLayoutParams(new GridView.LayoutParams(85, 85));

        imageView.setImageResource(R.drawable.whale);
        return imageView;
        // Build circle view
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

    //    public class ImageAdapter extends BaseAdapter
//    {
//        private Context mContext;
//
//        public ImageAdapter(Context c)
//        {
//            mContext = c;
//        }
//
//        public int getCount()
//        {
//            return 9;
//        }
//
//        public Object getItem(int position)
//        {
//            return null;
//        }
//
//        public long getItemId(int position)
//        {
//            return 0;
//        }
//
//        // create a new ImageView for each item referenced by the Adapter
//        public View getView(int position, View convertView, ViewGroup parent)
//        {
//            CircularImageView imageView;
//            if (convertView == null)
//            {
//                // if it's not recycled, initialize some attributes
//                imageView = new CircularImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
//            }
//            else
//            {
//                imageView = (CircularImageView) convertView;
//            }
//
//            imageView.setImageResource(R.drawable.whale);
//            return imageView;
//        }
//
//    }
}