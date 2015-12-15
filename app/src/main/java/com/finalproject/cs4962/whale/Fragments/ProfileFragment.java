package com.finalproject.cs4962.whale.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.cs4962.whale.Activities.AboutMeActivity;
import com.finalproject.cs4962.whale.CircularImageView;
import com.finalproject.cs4962.whale.DataManager;
import com.finalproject.cs4962.whale.Networking;
import com.finalproject.cs4962.whale.R;


/**
 * Created by Bharath on 11/22/2015.
 */
//TODO: The issue with the tabs is that the tablayout shares the same memory space as the friends list so
//need to create a new tablayout or xml file.
public class ProfileFragment extends Fragment implements DataManager.GetUserProfileListener, View.OnClickListener
{
    private static final int SELECT_SINGLE_PICTURE = 101;
    public static final String IMAGE_TYPE = "image/*";
    private static int RESULT_LOAD_IMG = 1;


    public static ProfileFragment newInstance()
    {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_profile, container, false);
        CircularImageView pic = (CircularImageView) rootLayout.findViewById(R.id.profile_picture);
        pic.setOnClickListener(selectPicture());
        TextView name = (TextView) rootLayout.findViewById(R.id.profile_name);
        TextView totalMessages = (TextView) rootLayout.findViewById(R.id.profile_total);
        TextView about = (TextView) rootLayout.findViewById(R.id.about_me_textbox);
        about.setOnClickListener(this);
        return rootLayout;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        DataManager.getInstance().getUserProfile();
        DataManager.getInstance().setGetUserProfileListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private View.OnClickListener selectPicture()
    {
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        };

        return listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String updated = data.getExtras().getString(AboutMeActivity.NEWTEXT);
                TextView description = (TextView) getActivity().findViewById(R.id.about_me_textbox);
                description.setText(updated);
                DataManager.getInstance().updateUserProfile("", updated);

            }
        }
        else if (requestCode == RESULT_LOAD_IMG)
        {
            try
            {
                // When an Image is picked
                if (resultCode == Activity.RESULT_OK && null != data)
                {
                    // Get the Image from data
                    String imgDecodableString;
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap pic = BitmapFactory.decodeFile(imgDecodableString);
                    CircularImageView profilePic = (CircularImageView) getActivity().findViewById(R.id.profile_picture);
                    profilePic.setImageBitmap(pic);
                    String img = DataManager.getInstance().bitmapToString(pic);
                    DataManager.getInstance().updateUserProfile(img, "");

                }
                else
                {
                    Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e)
            {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onGetProfile(Networking.PersonalProfileResponse profile)
    {
        TextView name = (TextView) getActivity().findViewById(R.id.profile_name);
        TextView totalMessages = (TextView) getActivity().findViewById(R.id.profile_total);
        TextView description = (TextView) getActivity().findViewById(R.id.about_me_textbox);
        CircularImageView pic = (CircularImageView) getActivity().findViewById(R.id.profile_picture);

        name.setText(profile.name);
        totalMessages.setText("Total Messages: " + profile.messages);
        description.setText(profile.about);

        Bitmap bm = DataManager.getInstance().stringToBitmap(profile.profilePic);
        pic.setImageBitmap(bm);
        pic.setName(profile.name);

    }

    @Override
    public void onClick(View view)
    {
        if (view instanceof TextView)
        {
            if (view == getActivity().findViewById(R.id.about_me_textbox))
            {
                Intent toEditIntent = new Intent();
                toEditIntent.setClass(getActivity(), AboutMeActivity.class);
                String current = ((TextView) view).getText().toString();
                toEditIntent.putExtra(AboutMeActivity.CURRENTTEXT, current);
                startActivityForResult(toEditIntent, 2);
            }
        }
    }

}
