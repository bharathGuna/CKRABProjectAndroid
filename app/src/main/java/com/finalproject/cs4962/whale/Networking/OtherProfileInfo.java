package com.finalproject.cs4962.whale.Networking;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Bharath on 12/10/2015.
 */
public class OtherProfileInfo
{
        public String name;
        public Bitmap profilePic;
        public int messages;
        public String friended;
        public String about;
        public List<Friend> friends;

    public OtherProfileInfo(String _name, Bitmap bitmap, int _messages, String _friended, String _about, List<Friend> _friends)
    {
        name = _name;
        profilePic = bitmap;
        messages = _messages;
        friended = _friended;
        about = _about;
        friends = _friends;

    }

}
