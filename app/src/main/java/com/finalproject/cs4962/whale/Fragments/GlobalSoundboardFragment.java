package com.finalproject.cs4962.whale.Fragments;

import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.cs4962.whale.Networking.DataManager;
import com.finalproject.cs4962.whale.Networking.Networking;
import com.finalproject.cs4962.whale.R;
import com.finalproject.cs4962.whale.Views.SoundbiteView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GlobalSoundboardFragment extends Fragment implements ListAdapter, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, DataManager.GetGlobalSoundboardListener
{
    public static GlobalSoundboardFragment newInstance()
    {
        GlobalSoundboardFragment fragment = new GlobalSoundboardFragment();
        return fragment;
    }

    private String FILE_PATH;
    private MediaRecorder audioRecorder;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_global_board, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        GridView gridView = (GridView) getActivity().findViewById(R.id.global_board_grid);
        gridView.setAdapter(this);
        gridView.setOnItemClickListener(this);

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.global_board_refresh);
        refreshLayout.setOnRefreshListener(this);

        FILE_PATH = getActivity().getFilesDir() + "/global";
        File dir = new File(FILE_PATH);
        if (!dir.exists())
            dir.mkdir();

        DataManager.getInstance().setGetGlobalSoundboardListener(this);
        DataManager.getInstance().refreshGlobalSoundboard();

    }

    @Override
    public void onRefresh()
    {
        DataManager.getInstance().refreshGlobalSoundboard();
    }

    @Override
    public void onGetGlobalBoard()
    {
        for (int i = 0; i < DataManager.getInstance().getGlobalBoardCount(); i++)
        {
            Networking.Soundbite sb = DataManager.getInstance().getGlobalBite(i);
            byte[] bite = DataManager.getInstance().stringToMessageBytes(sb.soundbite);
            writeBytesToFile(bite, sb.uploaderID, sb.soundbiteName);
        }
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.global_board_refresh);
        refreshLayout.setRefreshing(false);
        GridView gridView = (GridView) getActivity().findViewById(R.id.global_board_grid);
gridView.invalidateViews();

    }

    private void writeBytesToFile(byte[] bytes, String uploaderID, String name)
    {
        try
        {
            File file = new File(FILE_PATH + "/" + uploaderID + name);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
            out.write(bytes);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

        LinearLayout biteLayout = new LinearLayout(getActivity());
        biteLayout.setOrientation(LinearLayout.VERTICAL);

        SoundbiteView bite = new SoundbiteView(getActivity());

        int padding = (int) (8.0f * getResources().getDisplayMetrics().density);

        TextView biteName = new TextView(getActivity());
        biteName.setText(sb.soundbiteName);
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
        Networking.Soundbite bite = (Networking.Soundbite) getItem(i);
        String path = FILE_PATH + "/" + bite.uploaderID + bite.soundbiteName;
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

}
