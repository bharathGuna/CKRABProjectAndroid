package com.finalproject.cs4962.whale;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationActivity extends Activity implements ListAdapter
{
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        listView = (ListView) findViewById(R.id.messages_list);
        listView.setAdapter(this);

        View separator = findViewById(R.id.separator);
        int[] colors = {0, getResources().getColor(R.color.textColorPrimary), 0}; // red for the example
        separator.setBackground(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));

        TextView name = (TextView)findViewById(R.id.title_text);
        name.setText("Charles Khong");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void backPressed(View view)
    {
        super.onBackPressed();
    }

    public void recordPressed(View view)
    {

    }

    public void soundboardPressed(View view)
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
        return 13;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public Object getItem(int i)
    {
        return null;
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
        LinearLayout rootLayout = new LinearLayout(this);
        CircularImageView profile = new CircularImageView(this);
        profile.setImageResource(R.drawable.whale);
        WaveView msg = new WaveView(this, true);
        int padding = (int) (8.0f * getResources().getDisplayMetrics().density);
        if (i % 2 == 0)
        {
            rootLayout.addView(profile, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
            rootLayout.addView(msg, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 12));
        }
        else
        {
            rootLayout.addView(msg, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 12));
            rootLayout.addView(profile, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
        }
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
        return false;
    }

    @Override
    public boolean isEnabled(int i)
    {
        return false;
    }
}