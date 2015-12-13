package com.finalproject.cs4962.whale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SelfSoundboardFragment extends Fragment implements ListAdapter, AdapterView.OnItemClickListener, View.OnTouchListener
{
    public static SelfSoundboardFragment newInstance()
    {
        SelfSoundboardFragment fragment = new SelfSoundboardFragment();
        return fragment;
    }

    private String FILE_PATH;
    private final String tempName = "ck129bk201902k0234k0990234lk23423";
    private MediaRecorder audioRecorder;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_self_soundboard, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        GridView gridView = (GridView) getActivity().findViewById(R.id.self_board_grid);
        gridView.setAdapter(this);
        gridView.setOnItemClickListener(this);

        registerForContextMenu(gridView);

        FloatingActionButton button = (FloatingActionButton) getActivity().findViewById(R.id.add_soundbite_button);
        button.setOnTouchListener(this);

        FILE_PATH = getActivity().getFilesDir() + "/personal";
        File dir = new File(FILE_PATH);
        if (!dir.exists())
            dir.mkdir();

        DataManager.getInstance().loadPersonalSoundboard(FILE_PATH);
    }

    private void saveSoundbite(String filename)
    {
        try
        {
            File file = new File(FILE_PATH + "/" + tempName);
            if (filename == "")
                file.delete();
            else if (!file.renameTo(new File(FILE_PATH + "/" + filename)))
                throw new Exception("Failed to rename file");
            else
            {
                DataManager.getInstance().addPersonalBite(filename);
                GridView gridView = (GridView) getActivity().findViewById(R.id.self_board_grid);
                gridView.invalidateViews();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Failed to save file", Toast.LENGTH_SHORT);
            File file = new File(FILE_PATH + "/" + tempName);
            file.delete();
        }
    }

    private void deleteBite(String bitename)
    {
        try
        {
            File file = new File(FILE_PATH + "/" + bitename);
            file.delete();
            DataManager.getInstance().deletePersonalBite(bitename);
            GridView gridView = (GridView) getActivity().findViewById(R.id.self_board_grid);
            gridView.invalidateViews();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void uploadSoundbite(String bitename)
    {
        byte[] bite = null;
        try
        {
            File file = new File(FILE_PATH + "/" + bitename);
            long size = file.length();
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            DataInputStream in = new DataInputStream(fileInputStream);
            bite = new byte[(int) size];
            in.readFully(bite, 0, (int) size);
            String soundbite = DataManager.getInstance().messageBytesToString(bite);
            if (soundbite == null)
                throw new Exception("Message could not be converted from bytes to string");
            Toast.makeText(getActivity().getApplicationContext(), "Uploading to server: " + size + " bytes", Toast.LENGTH_SHORT).show();
            DataManager.getInstance().uploadSoundbite(bitename, soundbite);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_soundboard, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.menu_upload:
                int position = info.position;
                String bitename = (String) getItem(position);
                uploadSoundbite(bitename);
                return true;
            case R.id.menu_delete:
                position = info.position;
                bitename = (String) getItem(position);
                deleteBite(bitename);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        return DataManager.getInstance().getPersonalBoardCount();
    }

    @Override
    public Object getItem(int i)
    {
        return DataManager.getInstance().getPersonalBiteAt(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        String name = (String) getItem(i);

        LinearLayout biteLayout = new LinearLayout(getActivity());
        biteLayout.setOrientation(LinearLayout.VERTICAL);

        SoundbiteView bite = new SoundbiteView(getActivity());

        int padding = (int) (8.0f * getResources().getDisplayMetrics().density);

        TextView biteName = new TextView(getActivity());
        biteName.setText(name);
        biteName.setPadding(padding/2, 0, 0, 0);
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
        String bite = (String) getItem(i);
        String path = FILE_PATH + "/" + bite;
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
            Toast.makeText(getActivity().getApplicationContext(), "Playing", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (view instanceof FloatingActionButton)
        {
            if (view == getActivity().findViewById(R.id.add_soundbite_button))
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    try
                    {
                        audioRecorder = new MediaRecorder();
                        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        audioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                        String path = FILE_PATH + "/" + tempName;
                        audioRecorder.setOutputFile(path);

                        audioRecorder.prepare();
                        audioRecorder.start();
                        Toast.makeText(getActivity().getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
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

                        promptForName();


                    }
                }
            }
        }
        return true;
    }

    private void promptForName()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText biteName = new EditText(getActivity());
        biteName.setHint("Soundbite name");

        builder.setTitle("Provide a name").setView(biteName).setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                saveSoundbite(biteName.getText().toString());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                saveSoundbite("");
            }
        }).show();
    }

}
