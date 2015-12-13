package com.finalproject.cs4962.whale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ConversationFragment extends Fragment implements ListAdapter, AdapterView.OnItemClickListener, View.OnClickListener, DataManager.GetConvoListListener, DataManager.OnConvoListChangedListener, SwipeRefreshLayout.OnRefreshListener
{

    public static ConversationFragment newInstance()
    {
        ConversationFragment fragment = new ConversationFragment();
        return fragment;
    }

    private String[] names = null;
    private String[] ids = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DataManager.getInstance().refreshConvoList();
        DataManager.getInstance().setGetConvoListListener(this);
        DataManager.getInstance().setOnConvoListChangedListener(this);

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

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.convo_list_refresh);
        refreshLayout.setOnRefreshListener(this);

        FloatingActionButton addButton = (FloatingActionButton) getActivity().findViewById(R.id.add_convo_button);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        Intent createConvoIntent = new Intent();
        createConvoIntent.setClass(getActivity(), AddConversationActivity.class);
        startActivityForResult(createConvoIntent, 1);
    }


    @Override
    public void onGetConvoList()
    {
        ListView listView = (ListView) getActivity().findViewById(R.id.convo_list_view);
        listView.invalidateViews();
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.convo_list_refresh);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                names = (String [])data.getExtras().get("return_names");
                ids = (String [])data.getExtras().get("return_ids");
                List<String> userIDs = new ArrayList<>();
                userIDs.addAll(Arrays.asList(ids));

                DataManager.getInstance().createConversation(userIDs);
            }

        }
    }

    @Override
    public void onConvoCreated(String convoID)
    {
        Intent toConversationIntent = new Intent();
        toConversationIntent.setClass(getActivity(), ConversationActivity.class);
        toConversationIntent.putExtra("convoID", convoID);
        toConversationIntent.putExtra("names", names);
        startActivity(toConversationIntent);
    }

    @Override
    public void onConvoDeleted(Networking.GenericResponse response)
    {

    }

    @Override
    public boolean isEmpty()
    {
        return getCount() > 0;
    }

    @Override
    public int getCount()
    {
        return DataManager.getInstance().getConversationCount();
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public Object getItem(int i)
    {
        return DataManager.getInstance().getConversationAt(i);
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
        Conversation convo = (Conversation) getItem(i);

        LinearLayout rootLayout = new LinearLayout(getActivity());
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        CircularImageView profile = new CircularImageView(getActivity());
        Bitmap bm = DataManager.getInstance().stringToBitmap(convo.users.get(0).pic);
        profile.setImageBitmap(bm);
        profile.setName(convo.users.get(0).name);

        List<Conversation.User> users = convo.users;
        String names = "";
        for(Conversation.User user : users)
            names += user.name + ", ";
        names = names.substring(0, names.length() - 2);

        TextView username = new TextView(getActivity());
        username.setText(names);
        username.setGravity(Gravity.CENTER_VERTICAL);
        username.setSingleLine();
        int padding = (int) (8.0f * getResources().getDisplayMetrics().density);
        username.setPadding(padding, 0, 0, padding);
        username.setTextColor(getResources().getColor(R.color.textColorPrimary));
        username.setTextSize(getResources().getDisplayMetrics().scaledDensity * 5);

        LinearLayout messageLayout = new LinearLayout(getActivity());
        WaveView msg = new WaveView(getActivity(), false);
        rootLayout.addView(username, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));
        messageLayout.addView(profile, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));
        messageLayout.addView(msg, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 12));
        rootLayout.addView(messageLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3));

        rootLayout.setPadding(padding, padding, padding, padding);
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
        Conversation convo = (Conversation) getItem(i);
        /* Request for that conversation */
        Intent toConversationIntent = new Intent();
        toConversationIntent.setClass(getActivity(), ConversationActivity.class);
        toConversationIntent.putExtra("convoID", convo.convoID);
        List<Conversation.User> users = convo.users;
        List<String> _names = new ArrayList<>();
        for (Conversation.User user : users)
        {
            _names.add(user.name);
        }
        String[] names = new String[_names.size()];
        _names.toArray(names);
        toConversationIntent.putExtra("names", names);
        startActivity(toConversationIntent);
    }

    @Override
    public void onRefresh()
    {
        DataManager.getInstance().refreshConvoList();
    }
}