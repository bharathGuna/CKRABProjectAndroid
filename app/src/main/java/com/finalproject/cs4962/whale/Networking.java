package com.finalproject.cs4962.whale;

import android.util.Log;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Networking
{
    public class CreateAccountResponse
    {
        public String userID;
    }

    public class GenericResponse
    {
        public boolean success;
        public String description;
    }

    public class FriendListResponse
    {
        public Friend[] friends;
    }

    public class Friend
    {
        public String name;
        public String userID;
        public String profilePic;
        public boolean online;
    }

    public class PersonalProfileResponse
    {
        public String name;
        public String profilePic;
        public int messages;
        public String about;
    }

    public class OtherProfileResponse
    {
        public String name;
        public String profilePic;
        public int messages;
        public String friended;
        public String about;
        public Friend[] friends;
    }

    public class ConversationListResponse
    {
        public Conversation[] conversations;
    }

    public class Conversation
    {
        public User[] users;
        public String lastMessage;
        public String lastDateTime;
        public String convoID;
    }

    public class User
    {
        public String name;
        public String userID;
        public String profilePic;
    }

    public class ConversationMessagesResponse
    {
        public Message[] newMessages;
    }

    public class Message
    {
        public String senderID;
        public String dateTime;
        public String message;
    }

    public class SoundboardResponse
    {
        public Soundbite[] soundbites;
    }

    public class Soundbite
    {
        public String soundbiteName;
        public String uploaderID;
        public String soundbite;
    }

    public class CreateConversationResponse
    {
        public boolean success;
        public String description;
        public String convoID;
    }

    public class UpdateResponse
    {
        public boolean newUpdate;
        public ConversationUpdate[] updates;
    }

    public class ConversationUpdate
    {
        public String convoID;
        public Sender sender;
    }

    public class Sender
    {
        public String name;
        public String userID;
    }

    public class FindUserResponse
    {
        public String username;
        public String userID;
    }

    public class ImageResponse
    {
        public String profilePic;
    }

    public static final String SERVER_IP = "155.99.161.123";

    public static final int SERVER_PORT = 2000;

    public static CreateAccountResponse createNewAccount(String username)
    {
        try
        {
            //Socket connection = new Socket(SERVER_IP, SERVER_PORT);
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "newacct";
            String message = String.format("{ \"username\" : \"%s\" }", username);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            CreateAccountResponse createAccountResponse = gson.fromJson(json, CreateAccountResponse.class);
            return createAccountResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error creating account for user: " + e);
            return null;
        }
    }

    public static GenericResponse loginToServer(String userID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "connect";
            String message = String.format("{ \"userID\" : \"%s\" }", userID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            GenericResponse genericResponse = gson.fromJson(json, GenericResponse.class);
            return genericResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error logging user in: " + e);
            return null;
        }
    }

    public static void logoutOfServer(String userID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "disconn";
            String message = String.format("{ \"userID\" : \"%s\" }", userID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            connection.close();
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error logging user out: " + e);
        }
    }

    public static FriendListResponse getFriendsList(String userID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "friends";
            String message = String.format("{ \"userID\" : \"%s\" }", userID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.readFully(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            FriendListResponse friendListResponse = gson.fromJson(json, FriendListResponse.class);
            return friendListResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error getting user's friends: " + e);
            return null;
        }
    }

    public static PersonalProfileResponse getUserProfile(String userID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "usrprof";
            String message = String.format("{ \"userID\" : \"%s\" }", userID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.readFully(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            PersonalProfileResponse personalProfileResponse = gson.fromJson(json, PersonalProfileResponse.class);
            return personalProfileResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error grabbing user's profile: " + e);
            return null;
        }
    }

    public static OtherProfileResponse getOtherUserProfile(String userID, String otherID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "othprof";
            String message = String.format("{ \"userID\" : \"%s\", \"otherID\" : \"%s\" }", userID, otherID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.readFully(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            OtherProfileResponse otherProfileResponse = gson.fromJson(json, OtherProfileResponse.class);
            return otherProfileResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error grabbing other user's (" + otherID + ") profile: " + e);
            return null;
        }
    }

    public static ConversationListResponse getConversationsList(String userID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "conlist";
            String message = String.format("{ \"userID\" : \"%s\" }", userID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.readFully(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            ConversationListResponse conversationListResponse = gson.fromJson(json, ConversationListResponse.class);
            return conversationListResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error grabbing user's conversations list: " + e);
            return null;
        }
    }

    public static ConversationMessagesResponse getMessagesFromConversation(String userID, String convoID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "convers";
            String message = String.format("{ \"userID\" : \"%s\", \"convoID\" : \"%s\" }", userID, convoID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.readFully(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            ConversationMessagesResponse conversationMessagesResponse = gson.fromJson(json, ConversationMessagesResponse.class);
            return conversationMessagesResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error grabbing messages in conversation (" + convoID + "): " + e);
            return null;
        }
    }

    public static SoundboardResponse getGlobalSoundboard()
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "sdboard";
            String message = "";
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.readFully(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            SoundboardResponse soundboardResponse = gson.fromJson(json, SoundboardResponse.class);
            return soundboardResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error grabbing global soundboard: " + e);
            return null;
        }
    }

    public static GenericResponse addFriend(String userID, String friendID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "newpair";
            String message = String.format("{ \"userID\" : \"%s\", \"friendID\" : \"%s\" }", userID, friendID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.readFully(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            GenericResponse genericResponse = gson.fromJson(json, GenericResponse.class);
            return genericResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error adding friend (" + friendID + "): " + e);
            return null;
        }
    }

    public static GenericResponse removeFriend(String userID, String friendID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "delpair";
            String message = String.format("{ \"userID\" : \"%s\", \"friendID\" : \"%s\" }", userID, friendID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            GenericResponse genericResponse = gson.fromJson(json, GenericResponse.class);
            return genericResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error removing friend (" + friendID + "): " + e);
            return null;
        }
    }

    public static CreateConversationResponse createNewConversation(String userID, List<String> otherIDs)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "newconv";

            String others = "[";
            for(String id : otherIDs)
                others = others + "\"" +id + "\",";
            /* Trim the comma */
            if (others.length() > 1)
                others = others.substring(0, others.length() - 1);
            others += "]";

            String message = String.format("{ \"userID\" : \"%s\", \"otherUsers\" : %s }", userID, others);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            CreateConversationResponse createConversationResponse = gson.fromJson(json, CreateConversationResponse.class);
            return createConversationResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error creating conversation: " + e);
            return null;
        }
    }

    public static GenericResponse deleteConversation(String userID, String convoID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "delconv";
            String message = String.format("{ \"userID\" : \"%s\", \"convoID\" : \"%s\" }", userID, convoID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            GenericResponse genericResponse = gson.fromJson(json, GenericResponse.class);
            return genericResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error deleting conversation (" + convoID + "): " + e);
            return null;
        }
    }

    public static GenericResponse sendMessageToConversation(String userID, String convoID, String msg)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "sendmsg";
            String message = String.format("{ \"userID\" : \"%s\", \"convoID\" : \"%s\", \"message\" : \"%s\"}", userID, convoID, msg);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            GenericResponse genericResponse = gson.fromJson(json, GenericResponse.class);
            return genericResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error sending message to conversation (" + convoID + "): " + e);
            return null;
        }
    }

    public static GenericResponse updateUserProfile(String userID, String profilePic, String desc)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "updprof";
            String message = String.format("{ \"userID\" : \"%s\", \"profilePic\" : \"%s\", \"description\" : \"%s\" }",
                    userID, profilePic, desc);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            GenericResponse genericResponse = gson.fromJson(json, GenericResponse.class);
            return genericResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error updating user profile: " + e);
            return null;
        }
    }

    public static GenericResponse uploadSoundbite(String userID, String name, String soundbite)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "addbite";
            String message = String.format("{ \"userID\" : \"%s\", \"soundbiteName\" : \"%s\", \"soundbite\" : \"%s\" }",
                    userID, name, soundbite);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            GenericResponse genericResponse = gson.fromJson(json, GenericResponse.class);
            return genericResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error uploading soundbite to server: " + e);
            return null;
        }
    }

    public static UpdateResponse requestForUpdates(String userID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "request";
            String message = String.format("{ \"userID\" : \"%s\" }", userID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            UpdateResponse updateResponse = gson.fromJson(json, UpdateResponse.class);
            return updateResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error requesting for updates for user: " + e);
            return null;
        }
    }

    public static FindUserResponse findUser(String username)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "findusr";
            String message = String.format("{ \"username\" : \"%s\" }", username);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            FindUserResponse findUserResponse = gson.fromJson(json, FindUserResponse.class);
            return findUserResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error trying to find user: " + e);
            return null;
        }
    }

    public static ImageResponse getImage(String userID)
    {
        try
        {
            Socket connection = new Socket();
            connection.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 5000);
            DataInputStream in = new DataInputStream(connection.getInputStream());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            /* Build the message */
            String type = "getimag";
            String message = String.format("{ \"userID\" : \"%s\" }", userID);
            byte[] payload = packMessage(type, message);

            /* Write the message to server */
            out.write(payload);
            out.flush();

            /* Read the response from server */
            byte byte1 = in.readByte();
            byte byte2 = in.readByte();
            byte byte3 = in.readByte();
            byte byte4 = in.readByte();
            //short inLen = toShort(byte1, byte2);
            int inLen = toInt(byte1, byte2, byte3, byte4);
            byte[] response = new byte[inLen];
            in.read(response, 0, inLen);

            connection.close();
            String json = byteArrayToString(response);
            Gson gson = new Gson();

            ImageResponse imageResponse = gson.fromJson(json, ImageResponse.class);
            return imageResponse;
        }
        catch (Exception e)
        {
            Log.i("Networking", "Error trying to get user image: " + e);
            return null;
        }
    }

    private static byte[] packMessage(String mt, String msg)
    {
        byte[] type = stringToByteArray(mt);
        byte[] payload = stringToByteArray(msg);
        int outLen = payload.length;
        //short outLen = (short) payload.length;
        byte[] length = fromInt(outLen);

        List<Byte> out = new ArrayList<>();

        for (int i = 0; i < type.length; i++)
            out.add(type[i]);
        for (int i = 0; i < 4; i++)
            out.add(length[i]);
        for (int i = 0; i < payload.length; i++)
            out.add(payload[i]);

        byte[] send = toPrimitives(out.toArray(new Byte[out.size()]));
        return send;
    }

    private static byte[] stringToByteArray(String payload)
    {
        try
        {
            return payload.getBytes("UTF-8");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private static String byteArrayToString(byte[] payload)
    {
        try
        {
            return new String(payload, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    private static byte[] toPrimitives(Byte[] oBytes)
    {
        byte[] bytes = new byte[oBytes.length];
        for (int i = 0; i < oBytes.length; i++)
            bytes[i] = oBytes[i];
        return bytes;
    }

    public static byte[] fromInt(int num)
    {
        byte byte4 = (byte) (num >> 24);
        byte byte3 = (byte) (num >> 16);
        byte byte2 = (byte) (num >> 8);
        byte byte1 = (byte) (num & 0xFF);
        return new byte[]{byte1, byte2, byte3, byte4};
    }

    private static int toInt(byte byte1, byte byte2, byte byte3, byte byte4)
    {
        int b1, b2, b3, b4;
        b1 = byte1 < 0 ? byte1 + 256 : byte1;
        b2 = byte2 < 0 ? byte2 + 256 : byte2;
        b3 = byte3 < 0 ? byte3 + 256 : byte3;
        b4 = byte4 < 0 ? byte4 + 256 : byte4;
        return (b4 << 24) + (b3 << 16) + (b2 << 8) + b1;
    }

    private static byte[] fromShort(short num)
    {
        byte byte2 = (byte) (num >> 8);
        byte byte1 = (byte) (num & 0xFF);
        return new byte[]{byte1, byte2};
    }

    private static short toShort(byte byte1, byte byte2)
    {
        int b1 = byte1 < 0 ? byte1 + 256 : byte1;
        return (short) ((byte2 << 8) + byte1);
    }
}
