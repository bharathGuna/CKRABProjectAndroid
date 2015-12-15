package com.finalproject.cs4962.whale.Activities;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.finalproject.cs4962.whale.CircularImageView;
import com.finalproject.cs4962.whale.DataManager;
import com.finalproject.cs4962.whale.Message;
import com.finalproject.cs4962.whale.Networking;
import com.finalproject.cs4962.whale.R;
import com.finalproject.cs4962.whale.SoundbiteView;
import com.finalproject.cs4962.whale.WaveView;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ConversationActivity extends Activity implements ListAdapter, View.OnTouchListener, AdapterView.OnItemClickListener, DataManager.OnMessageSentListener, DataManager.GetNewMessagesListener, DataManager.OnUserFoundListener
{
    private String FILE_PATH;
    private String BOARD_PATH;
    private ListView listView;
    private GridView gridView;
    private ViewFlipper flipper;
    private MediaRecorder audioRecorder;
    private List<Message> messages = new ArrayList<>();
    private String convoID = "";
    private String[] ids = null;
    private String[] names = null;
    /* <K, V> => <ID, Pic> */
    private Map<String, UserPair> pictures = new HashMap<String, UserPair>();
    private AsyncTask<Void, Void, Void> recordingTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        listView = (ListView) findViewById(R.id.messages_list);
        listView.setAdapter(this);
        listView.setOnItemClickListener(this);

        flipper = (ViewFlipper) findViewById(R.id.convo_flipper);

        GridViewAdapter adapter = new GridViewAdapter(this);

        gridView = (GridView) findViewById(R.id.convo_board_grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(adapter);

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

        DataManager.getInstance().setOnUserFoundListener(this);
        ids = getIntent().getExtras().getStringArray("ids");
        for (String id : ids)
            DataManager.getInstance().findUserByID(id);
        DataManager.getInstance().findUserByID(DataManager.getInstance().getUserID());

        ImageButton recordButton = (ImageButton) findViewById(R.id.record_button);
        recordButton.setOnTouchListener(this);

        DataManager.getInstance().setOnMessageSentListener(this);
        DataManager.getInstance().setGetNewMessagesListener(this);

        convoID = getIntent().getExtras().getString("convoID");

        BOARD_PATH = getFilesDir().getAbsolutePath() + "/global";
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
            String username = DataManager.getInstance().getUsername();
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

    @Override
    public void onUserFound(Networking.FindUserResponse response)
    {
        UserPair pair = new UserPair(response.profilePic, response.username);
        pictures.put(response.userID, pair);
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

    public void toSoundboard(View view)
    {
        flipper.setInAnimation(this, R.anim.slide_in_from_right);
        flipper.setOutAnimation(this, R.anim.slide_out_to_left);
        flipper.showNext();
    }

    public void toRecord(View view)
    {
        flipper.setInAnimation(this, R.anim.slide_in_from_left);
        flipper.setOutAnimation(this, R.anim.slide_out_to_right);
        flipper.showPrevious();
    }

    public void sendBiteClicked(View view)
    {
        // Send, for now updates
        GridViewAdapter adapter = (GridViewAdapter) gridView.getAdapter();
        if (adapter.messagePath.equals(""))
            return;

        LinearLayout lt = (LinearLayout) adapter.getView(adapter.previousSelected, null, null);
        SoundbiteView sbv = (SoundbiteView) lt.getChildAt(0);
        sbv.select();

        byte[] msg = null;
        try
        {
            File file = new File(BOARD_PATH + "/" + adapter.messagePath);
            long size = file.length();
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            DataInputStream in = new DataInputStream(fileInputStream);
            msg = new byte[(int) size];
            in.readFully(msg, 0, (int) size);

            File save = new File(FILE_PATH + "/" + DataManager.getInstance().getUserID() + messages.size());
            FileOutputStream fileOutputStream = new FileOutputStream(save);
            BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
            out.write(msg);
            out.flush();
            out.close();

            String message = DataManager.getInstance().messageBytesToString(msg);
            if (message == null)
                throw new Exception("Soundbite could not be converted from bytes to string");
            Toast.makeText(getApplicationContext(), "Sending to server: " + size + " bytes", Toast.LENGTH_SHORT).show();
            DataManager.getInstance().sendMessageToConvo(convoID, message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        adapter.messagePath = "";
        //        DataManager.getInstance().getNewMessagesInConvo(convoID);

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
                        audioRecorder.setMaxDuration(30000);

                        recordingTimer = new AsyncTask<Void, Void, Void>()
                        {
                            @Override
                            protected Void doInBackground(Void... voids)
                            {
                                try
                                {
                                    Thread.sleep(30000);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid)
                            {
                                super.onPostExecute(aVoid);
                                if (audioRecorder != null)
                                {
                                    audioRecorder.stop();
                                    audioRecorder.release();
                                    sendMessage();
                                    audioRecorder = null;
                                    Toast.makeText(getApplicationContext(), "Limit reached", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };

                        audioRecorder.prepare();
                        audioRecorder.start();
                        recordingTimer.execute();
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        audioRecorder = null;
                    }
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL)
                {

                    if (audioRecorder != null)
                    {
                        try
                        {
                            audioRecorder.stop();
                            audioRecorder.release();
                            recordingTimer.cancel(true);
                            sendMessage();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Hold to record, not tap", Toast.LENGTH_SHORT).show();

                        }
                        finally
                        {
                            audioRecorder = null;
                        }
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
        UserPair pair = pictures.get(message.senderID);
        Bitmap bm = DataManager.getInstance().stringToBitmap(pair.pic);
        profile.setName(pair.username);
        profile.setImageBitmap(bm);

        WaveView msg = new WaveView(this, false);
        int padding = (int) (8.0f * getResources().getDisplayMetrics().density);
        if (!message.senderID.equals(DataManager.getInstance().getUserID()))
        {
            /* TODO: Get user image with their id at beginning and use that image here */

            rootLayout.addView(profile, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
            rootLayout.addView(msg, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 12));
        }
        else
        {
            /* TODO: Use self image */
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
        String path = FILE_PATH + "/" + msg.senderID + msg.messageID;
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

    private class UserPair
    {
        public String pic;
        public String username;

        public UserPair(String pic, String username)
        {
            this.pic = pic;
            this.username = username;
        }
    }

    private class GridViewAdapter implements ListAdapter, AdapterView.OnItemClickListener
    {
        private Context context;
        public String messagePath = "";
        private int previousSelected = -1;

        public GridViewAdapter(Context context)
        {
            this.context = context;
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
            return DataManager.getInstance().getGlobalBoardCount();
        }

        @Override
        public Object getItem(int i)
        {
            return DataManager.getInstance().getGlobalBite(i);
        }

        @Override
        public long getItemId(int i)
        {
            return 0;
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            Networking.Soundbite sb = (Networking.Soundbite) getItem(i);

            LinearLayout biteLayout = new LinearLayout(context);
            biteLayout.setOrientation(LinearLayout.VERTICAL);

            SoundbiteView bite = new SoundbiteView(context);

            int padding = (int) (8.0f * getResources().getDisplayMetrics().density);

            TextView biteName = new TextView(context);
            biteName.setText(sb.soundbiteName);
            biteName.setPadding(padding / 2, 0, 0, 0);
            biteName.setTextColor(getResources().getColor(R.color.textColorPrimary));
            biteName.setLines(1);
            biteName.setEllipsize(TextUtils.TruncateAt.END);
            biteName.setHorizontallyScrolling(true);

            biteLayout.addView(bite, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 4));
            biteLayout.addView(biteName, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1));
            biteLayout.setPadding(padding, padding, padding, padding);
            return biteLayout;
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
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            LinearLayout ll;
            SoundbiteView sbv;

            if (i == previousSelected)
            {
                // Deselect
                ll = (LinearLayout) gridView.getChildAt(i);
                sbv = (SoundbiteView) ll.getChildAt(0);
                sbv.select();
                messagePath = "";
                previousSelected = -1;
            }
            else
            {
                ll = (LinearLayout) gridView.getChildAt(i);
                sbv = (SoundbiteView) ll.getChildAt(0);
                sbv.select();

                ll = (LinearLayout) gridView.getChildAt(previousSelected);
                if (ll != null)
                {
                    sbv = (SoundbiteView) ll.getChildAt(0);
                    sbv.select();
                }

                Networking.Soundbite bite = (Networking.Soundbite) getItem(i);
                messagePath = bite.uploaderID + bite.soundbiteName;
                previousSelected = i;
            }


        }
    }
}
