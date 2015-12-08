package com.finalproject.cs4962.whale;

import android.graphics.Bitmap;

/**
 * Created by Bharath on 12/7/2015.
 */
public class Friend
    {

    public String name;
    public String userID;
    public Bitmap profilePic;
    public boolean online;

    public Friend(String _name, String _userID, Bitmap _profilePic,boolean _online)
    {
        name = _name;
        userID = _userID;
        online = _online;
        profilePic = _profilePic;
    }

}
