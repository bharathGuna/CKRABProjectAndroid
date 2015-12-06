package com.finalproject.cs4962.whale;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ConversationFragment extends Fragment implements ListAdapter, AdapterView.OnItemClickListener
{

    public static ConversationFragment newInstance()
    {
        ConversationFragment fragment = new ConversationFragment();
        return fragment;
    }

    private Networking.Conversation[] conversations = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_convo_list, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ListView listView = (ListView) getActivity().findViewById(R.id.convo_list_view);
        listView.setAdapter(this);
        listView.setOnItemClickListener(this);
        int[] colors = {0, getResources().getColor(R.color.textColorPrimary), 0}; // red for the example
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        int height = (int) (2 * getResources().getDisplayMetrics().density);
        listView.setDividerHeight(height);
    }

    @Override
    public boolean isEmpty()
    {
        return getCount() > 0;
    }

    @Override
    public int getCount()
    {
        return 9;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public Object getItem(int i)
    {
        return conversations[i];
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public int getItemViewType(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        //Networking.Conversation convo = (Networking.Conversation) getItem(i);
        LinearLayout rootLayout = new LinearLayout(getActivity());
        CircularImageView profile = new CircularImageView(getActivity());
        profile.setImageResource(R.drawable.whale);
        WaveView msg = new WaveView(getActivity(), false);
        int padding = (int) (8.0f * getResources().getDisplayMetrics().density);
        rootLayout.addView(profile, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
        rootLayout.addView(msg, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 12));

        rootLayout.setPadding(padding, 2*padding, padding, 2*padding);
        return rootLayout;
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        //Networking.Conversation convo = (Networking.Conversation) getItem(i);
        /* Request for that conversation */
        Intent toConversationIntent = new Intent();
        toConversationIntent.setClass(getActivity(), ConversationActivity.class);
        startActivity(toConversationIntent);

    }
}