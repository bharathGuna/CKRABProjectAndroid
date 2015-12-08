package com.finalproject.cs4962.whale;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends Activity implements ListAdapter, View.OnTouchListener, AdapterView.OnItemClickListener
{
    private String FILE_PATH;
    private ListView listView;
    private MediaRecorder audioRecorder;
    private List<Message> messages = new ArrayList<>();
    private String convoID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        listView = (ListView) findViewById(R.id.messages_list);
        listView.setAdapter(this);
        listView.setOnItemClickListener(this);

        View separator = findViewById(R.id.separator);
        int[] colors = {0, getResources().getColor(R.color.textColorPrimary), 0}; // red for the example
        separator.setBackground(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));

        TextView name = (TextView)findViewById(R.id.title_text);
        name.setText("Charles Khong");

        ImageButton recordButton = (ImageButton)findViewById(R.id.record_button);
        recordButton.setOnTouchListener(this);

        FILE_PATH = getFilesDir().getAbsolutePath();
        //convoID = getIntent().getExtras().getString("convoID");
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

    public void soundboardPressed(View view)
    {
        if (messages.size() == 0)
            return;
        byte[] msg1 = null;

        try
        {
            File file = new File(FILE_PATH + "/0");
            long size = file.length();
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            DataInputStream in = new DataInputStream(fileInputStream);
            msg1 = new byte[(int)size];
            in.readFully(msg1, 0, (int)size);
            Log.i("File read", "File Size: " + msg1.length);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (view instanceof ImageButton)
        {
            if (view == findViewById(R.id.record_button))
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    try
                    {
                        audioRecorder = new MediaRecorder();
                        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                        String path = FILE_PATH + "/" + messages.size();
                        audioRecorder.setOutputFile(path);

                        audioRecorder.prepare();
                        audioRecorder.start();
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        audioRecorder = null;
                    }
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP ||
                        motionEvent.getAction() == MotionEvent.ACTION_CANCEL)
                {

                    if (audioRecorder != null)
                    {
                        audioRecorder.stop();
                        audioRecorder.release();
                        audioRecorder = null;
                        Message msg = new Message("" + messages.size());
                        messages.add(msg);
                        listView.invalidateViews();

                        Toast.makeText(getApplicationContext(), "Recording finished", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty()
    {
        return getCount() > 0;
    }

    @Override
    public int getCount()
    {
        return messages.size();
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public Object getItem(int i)
    {
        return messages.get(i);
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
        WaveView msg = new WaveView(this, false);
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
        Message msg = (Message) getItem(i);
        String path = FILE_PATH + "/" + msg.messageID;
        MediaPlayer player = new MediaPlayer();
        try
        {
            player.setDataSource(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            player.prepare();
            player.start();
            Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
