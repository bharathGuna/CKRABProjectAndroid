package com.finalproject.cs4962.whale.Networking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Conversation
{
    public List<User> users;
    public String lastMessage;
    public String lastDateTime;
    public String convoID;

    public Conversation(Networking.User[] users, String lm, String ldt, String id)
    {
        this.users = new ArrayList<>();
        for(Networking.User user : users)
        {
            User usr = new User(user.name, user.userID, user.profilePic);
            this.users.add(usr);
        }
        this.lastMessage = lm;
        this.lastDateTime = ldt;
        this.convoID = id;
    }

    public Conversation(String friendName, String friendID, String pic, String convoID)
    {
        User user = new User(friendName, friendID, pic);
        User[] users = new User[] {user};

        this.users = new ArrayList<>(Arrays.asList(users));
        this.lastMessage = "";
        this.lastDateTime = "";
        this.convoID = convoID;
    }

    public class User
    {
        public String name;
        public String id;
        public String pic;

        public User(String name, String id, String pic)
        {
            this.name = name;
            this.id = id;
            this.pic = pic;
        }
    }
}
