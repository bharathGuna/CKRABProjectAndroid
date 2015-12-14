package com.finalproject.cs4962.whale.Networking;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bharath on 12/7/2015.
 */
public class Friend implements Parcelable
{

    public String name;
    public String userID;
    public Bitmap profilePic;
    public boolean online;

    public Friend(String _name, String _userID, Bitmap _profilePic, boolean _online)
    {
        name = _name;
        userID = _userID;
        online = _online;
        profilePic = _profilePic;
    }

    protected Friend(Parcel in)
    {
        name = in.readString();
        userID = in.readString();
        profilePic = in.readParcelable(Bitmap.class.getClassLoader());
        online = in.readByte() != 0;
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>()
    {
        @Override
        public Friend createFromParcel(Parcel in)
        {
            return new Friend(in);
        }

        @Override
        public Friend[] newArray(int size)
        {
            return new Friend[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(name);
        parcel.writeString(userID);
        parcel.writeParcelable(profilePic, i);
        parcel.writeByte((byte) (online ? 1 : 0));
    }
}
