package com.finalproject.cs4962.whale;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConversationActivity extends Activity implements ListAdapter, View.OnTouchListener, AdapterView.OnItemClickListener, DataManager.OnMessageSentListener, DataManager.GetNewMessagesListener
{
    private String FILE_PATH;
    private ListView listView;
    private MediaRecorder audioRecorder;
    private List<Message> messages = new ArrayList<>();
    private String convoID = "";
    private String[] names = null;

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

        names = getIntent().getExtras().getStringArray("names");
        String title = "";
        for (String n : names)
            title += n + ", ";
        title = title.substring(0, title.length() - 2);
        TextView name = (TextView) findViewById(R.id.title_text);
        name.setText(title);
        name.setSingleLine();

        ImageButton recordButton = (ImageButton)findViewById(R.id.record_button);
        recordButton.setOnTouchListener(this);

        DataManager.getInstance().setOnMessageSentListener(this);
        DataManager.getInstance().setGetNewMessagesListener(this);

        convoID = getIntent().getExtras().getString("convoID");

        FILE_PATH = getFilesDir().getAbsolutePath() + "/" + convoID;
        File dir = new File(FILE_PATH);
        if (!dir.exists())
            dir.mkdir();

        messages = DataManager.getInstance().loadPreviousMessagesInConvo(FILE_PATH);
                                DataManager.getInstance().getNewMessagesInConvo(convoID);
//        pollForNewMessages();
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

    private void pollForNewMessages()
    {
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask()
//        {
//            @Override
//            public void run()
//            {
//                AsyncTask<Void, Void, Void> pollTask = new AsyncTask<Void, Void, Void>()
//                {
//                    @Override
//                    protected Void doInBackground(Void... voids)
//                    {
//                        DataManager.getInstance().getNewMessagesInConvo(convoID);
//                        return null;
//                    }
//                }.execute();
//            }
//        };
//        timer.schedule(task, 0, 2000);
    }

    @Override
    public void onMessageSent(Networking.GenericResponse response)
    {
        if (response.success)
        {
            Toast.makeText(getApplicationContext(), "Sent successfully", Toast.LENGTH_SHORT).show();
            String id = DataManager.getInstance().getUserID();
            Message msg = new Message("" + messages.size(), id);
            messages.add(msg);
            listView.invalidateViews();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Sent unsuccessfully", Toast.LENGTH_SHORT).show();
            File file = new File(FILE_PATH + "/" + DataManager.getInstance().getUserID() + messages.size());
            file.delete();
        }
    }

    @Override
    public void onGetNewMessages(Networking.ConversationMessagesResponse conversationMessagesResponse)
    {
        for (int i = 0; i < conversationMessagesResponse.newMessages.length; i++)
        {
            String message = conversationMessagesResponse.newMessages[i].message;
            String senderID = conversationMessagesResponse.newMessages[i].senderID;
            byte[] clip = DataManager.getInstance().stringToMessageBytes(message);
            if (clip != null)
                Log.i("Get messages", "String could not be converted to byte array");
            writeBytesToFile(clip, senderID);
        }
        listView.invalidateViews();
    }

    private void writeBytesToFile(byte[] clip, String senderID)
    {
        try
        {
            File file = new File(FILE_PATH + "/" + senderID + messages.size());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
            out.write(clip);
            out.flush();
            out.close();
            messages.add(new Message("" + messages.size(), senderID));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void backPressed(View view)
    {
        super.onBackPressed();
    }

    public void soundboardPressed(View view)
    {
        DataManager.getInstance().getNewMessagesInConvo(convoID);
    }

    private void sendMessage()
    {
        byte[] msg = null;
        try
        {
            File file = new File(FILE_PATH + "/" + DataManager.getInstance().getUserID() + messages.size());
            long size = file.length();
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            DataInputStream in = new DataInputStream(fileInputStream);
            msg = new byte[(int) size];
            in.readFully(msg, 0, (int) size);
            String message = DataManager.getInstance().messageBytesToString(msg);
            if (message == null)
                throw new Exception("Message could not be converted from bytes to string");
            Toast.makeText(getApplicationContext(), "Sending to server: " + size + " bytes", Toast.LENGTH_SHORT).show();
            DataManager.getInstance().sendMessageToConvo(convoID, message);
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
                        String path = FILE_PATH + "/" + DataManager.getInstance().getUserID() + messages.size();
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

                        sendMessage();

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
        Message message = (Message) getItem(i);
        LinearLayout rootLayout = new LinearLayout(this);
        CircularImageView profile = new CircularImageView(this);
        profile.setImageResource(R.drawable.whale);
        WaveView msg = new WaveView(this, false);
        int padding = (int) (8.0f * getResources().getDisplayMetrics().density);
        if (!message.senderID.equals(DataManager.getInstance().getUserID()))
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
        String path = FILE_PATH + "/" + msg.senderID +msg.messageID;
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
