package com.finalproject.cs4962.whale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager
{
    private static DataManager instance;

    public static DataManager getInstance()
    {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    public interface OnAccountCreatedListener
    {
        void onAccountCreated();
    }

    public interface OnUserLoggedInListener
    {
        void onUserLoggedIn();
    }

    public interface GetFriendsListener
    {
        void onGetFriends(List<Friend> friends);
    }

    public interface GetUserProfileListener
    {
        void onGetProfile(Networking.PersonalProfileResponse profile);
    }

    public interface GetOtherProfileListener
    {
        void onGetOtherProfile(OtherProfileInfo profile);
    }

    public interface GetConvoListListener
    {
        void onGetConvoList();
    }

    public interface GetNewMessagesListener
    {
        void onGetNewMessages(Networking.ConversationMessagesResponse conversationMessagesResponse);
    }

    public interface GetGlobalSoundboardListener
    {
        void onGetGlobalBoard();
    }

    public interface OnFriendshipChangeListener
    {
        void onFriendAdded(Networking.GenericResponse response);
        void onFriendRemoved(Networking.GenericResponse response);
    }

    public interface OnConvoListChangedListener
    {
        void onConvoCreated(String convoID);
        void onConvoDeleted(Networking.GenericResponse response);
    }

    public interface OnMessageSentListener
    {
        void onMessageSent(Networking.GenericResponse response);
    }

    public interface OnProfileUpdatedListener
    {
        void onProfileUpdated(Networking.GenericResponse response);
    }

    public interface OnSoundbiteUploadedListener
    {
        void onSoundbiteUploaded(Networking.GenericResponse response);
    }

    public interface OnUpdateReceivedListener
    {
        void onUpdateReceived(Networking.UpdateResponse response);
    }

    public interface OnUserFoundListener
    {
        void onUserFound(Networking.FindUserResponse response);
    }

    private String userID;
    /* TODO: Convert to usable data objects */
    private List<Conversation> conversationList;
    private List<Networking.Soundbite> globalSoundboard;
    //? current convo messages list

    private OnAccountCreatedListener onAccountCreatedListener;
    private OnUserLoggedInListener onUserLoggedInListener;
    private GetFriendsListener getFriendsListener;
    private GetUserProfileListener getUserProfileListener;
    private GetOtherProfileListener getOtherProfileListener;
    private GetConvoListListener getConvoListListener;
    private GetNewMessagesListener getNewMessagesListener;
    private GetGlobalSoundboardListener getGlobalSoundboardListener;
    private OnFriendshipChangeListener onFriendshipChangeListener;
    private OnConvoListChangedListener onConvoListChangedListener;
    private OnMessageSentListener onMessageSentListener;
    private OnProfileUpdatedListener onProfileUpdatedListener;
    private OnSoundbiteUploadedListener onSoundbiteUploadedListener;
    private OnUpdateReceivedListener onUpdateReceivedListener;
    private OnUserFoundListener onUserFoundListener;

    private DataManager()
    {
        conversationList = new ArrayList<>();
        globalSoundboard = new ArrayList<>();
    }

    public String getUserID()
    {
        return userID;
    }

    public int getConversationCount()
    {
        return conversationList.size();
    }

    public Conversation getConversationAt(int index)
    {
        return conversationList.get(index);
    }

    public void setOnAccountCreatedListener(OnAccountCreatedListener onAccountCreatedListener)
    {
        this.onAccountCreatedListener = onAccountCreatedListener;
    }

    public void setOnUserLoggedIn(OnUserLoggedInListener onUserLoggedInListener)
    {
        this.onUserLoggedInListener = onUserLoggedInListener;
    }

    public void setGetFriendsListener(GetFriendsListener getFriendsListener)
    {
        this.getFriendsListener = getFriendsListener;
    }

    public void setGetUserProfileListener(GetUserProfileListener getUserProfileListener)
    {
        this.getUserProfileListener = getUserProfileListener;
    }

    public void setGetOtherProfileListener(GetOtherProfileListener getOtherProfileListener)
    {
        this.getOtherProfileListener = getOtherProfileListener;
    }

    public void setGetConvoListListener(GetConvoListListener getConvoListListener)
    {
        this.getConvoListListener = getConvoListListener;
    }

    public void setGetNewMessagesListener(GetNewMessagesListener getNewMessagesListener)
    {
        this.getNewMessagesListener = getNewMessagesListener;
    }

    public void setGetGlobalSoundboardListener(GetGlobalSoundboardListener getGlobalSoundboardListener)
    {
        this.getGlobalSoundboardListener = getGlobalSoundboardListener;
    }

    public void setOnFriendshipChangeListener(OnFriendshipChangeListener onFriendshipChangeListener)
    {
        this.onFriendshipChangeListener = onFriendshipChangeListener;
    }

    public void setOnConvoListChangedListener(OnConvoListChangedListener onConvoListChangedListener)
    {
        this.onConvoListChangedListener = onConvoListChangedListener;
    }

    public void setOnMessageSentListener(OnMessageSentListener onMessageSentListener)
    {
        this.onMessageSentListener = onMessageSentListener;
    }

    public void setOnProfileUpdatedListener(OnProfileUpdatedListener onProfileUpdatedListener)
    {
        this.onProfileUpdatedListener = onProfileUpdatedListener;
    }

    public void setOnSoundbiteUploadedListener(OnSoundbiteUploadedListener onSoundbiteUploadedListener)
    {
        this.onSoundbiteUploadedListener = onSoundbiteUploadedListener;
    }

    public void setOnUpdateReceivedListener(OnUpdateReceivedListener onUpdateReceivedListener)
    {
        this.onUpdateReceivedListener = onUpdateReceivedListener;
    }

    public void setOnUserFoundListener(OnUserFoundListener onUserFoundListener)
    {
        this.onUserFoundListener = onUserFoundListener;
    }

    public List<Message> loadPreviousMessagesInConvo(String path)
    {
//        int counter = 0;
//        String id = "";
//
//        List<Message> messages = new ArrayList<>();
//        File file = new File(path + "/" + counter);
//        while (file.exists())
//        {
//            messages.add(new Message(""+counter,));
//            counter++;
//            file = new File(path + "/" + counter);
//        }
//        return messages;

        File[] files = new File(path + "/").listFiles();
                List<Message> messages = new ArrayList<>();

        for (File f : files)
        {
            if (f.exists())
            {
                String id = f.getName().substring(0, 36);
                int count = Integer.parseInt(f.getName().substring(36, f.getName().length()));
                messages.add(new Message("" + count, id));
                Log.i("file", "id");
            }
        }
        return messages;
    }

    public void createAccount(String username)
    {
        AsyncTask<String, Integer, Networking.CreateAccountResponse> createTask = new AsyncTask<String, Integer, Networking.CreateAccountResponse>()
        {
            @Override
            protected Networking.CreateAccountResponse doInBackground(String... strings)
            {
                return Networking.createNewAccount(strings[0]);
            }

            @Override
            protected void onPostExecute(Networking.CreateAccountResponse createAccountResponse)
            {
                super.onPostExecute(createAccountResponse);

                if (createAccountResponse == null)
                {
                    userID = "FAILED";
                }
                else
                    /* Username was taken */
                    userID = createAccountResponse.userID;

                if (onAccountCreatedListener != null)
                    onAccountCreatedListener.onAccountCreated();
            }
        };
        createTask.execute(username);
    }

    public void login()
    {
        AsyncTask<String, Integer, Networking.GenericResponse> loginTask = new AsyncTask<String, Integer, Networking.GenericResponse>()
        {
            @Override
            protected Networking.GenericResponse doInBackground(String... strings)
            {
                return Networking.loginToServer(strings[0]);
            }

            @Override
            protected void onPostExecute(Networking.GenericResponse genericResponse)
            {
                super.onPostExecute(genericResponse);

                if (genericResponse == null)
                    return;
                if (genericResponse.success == false)
                    return;

                /* Notify logged in */
                if(onUserLoggedInListener != null)
                    onUserLoggedInListener.onUserLoggedIn();
            }
        };

        loginTask.execute(userID);
    }

    public void logout()
    {
        AsyncTask<String, Integer, Void> logoutTask = new AsyncTask<String, Integer, Void>()
        {
            @Override
            protected Void doInBackground(String... strings)
            {
                Networking.logoutOfServer(strings[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);

                /* Notify logged out */
            }
        };

        logoutTask.execute(userID);
    }

    public void getFriendsList(String userID)
    {
        AsyncTask<String, Integer, Networking.FriendListResponse> friendsTask = new AsyncTask<String, Integer, Networking.FriendListResponse>()
        {
            @Override
            protected Networking.FriendListResponse doInBackground(String... strings)
            {
                return Networking.getFriendsList(strings[0]);
            }

            @Override
            protected void onPostExecute(Networking.FriendListResponse friendListResponse)
            {
                super.onPostExecute(friendListResponse);

                if (friendListResponse == null)
                    return;
                List<Friend> friends = new ArrayList<>();
                for(Networking.Friend friend : friendListResponse.friends)
                {
                    String name = friend.name;
                    String id = friend.userID;
                    String pic = friend.profilePic;
                    Bitmap ppic = stringToBitmap(pic);
                    boolean online = friend.online;

                    if (ppic == null)
                    {
                        Log.i("String to Bitmap", "Error occurred converting string to bitmap");
                    //    return;
                    }

                    Friend frnd = new Friend(name, id, ppic, online);
                    friends.add(frnd);
                }

                /* Notify listener */
                if (getFriendsListener != null)
                    getFriendsListener.onGetFriends(friends);
            }
        };


        friendsTask.execute(userID);
    }

    public void getUserProfile()
    {
        AsyncTask<String, Integer, Networking.PersonalProfileResponse> profileTask = new AsyncTask<String, Integer, Networking.PersonalProfileResponse>()
        {
            @Override
            protected Networking.PersonalProfileResponse doInBackground(String... strings)
            {
                return Networking.getUserProfile(strings[0]);
            }

            @Override
            protected void onPostExecute(Networking.PersonalProfileResponse personalProfileResponse)
            {
                super.onPostExecute(personalProfileResponse);

                if (personalProfileResponse == null)
                    return;
                if (getUserProfileListener != null)
                    getUserProfileListener.onGetProfile(personalProfileResponse);
            }
        };

        profileTask.execute(userID);
    }

    public void getOtherProfile(String otherID)
    {
        AsyncTask<String, Integer, Networking.OtherProfileResponse> profileTask = new AsyncTask<String, Integer, Networking.OtherProfileResponse>()
        {
            @Override
            protected Networking.OtherProfileResponse doInBackground(String... strings)
            {
                return Networking.getOtherUserProfile(strings[0], strings[1]);
            }

            @Override
            protected void onPostExecute(Networking.OtherProfileResponse otherProfileResponse)
            {
                super.onPostExecute(otherProfileResponse);


                List<Friend> friends = new ArrayList<>();
                for(Networking.Friend friend : otherProfileResponse.friends)
                {
                    String name = friend.name;
                    String id = friend.userID;
                    String pic = friend.profilePic;
                    Bitmap ppic = stringToBitmap(pic);
                    boolean online = friend.online;

                    if (ppic == null)
                    {
                        Log.i("String to Bitmap", "Error occurred converting string to bitmap");
                        //    return;
                    }

                    Friend frnd = new Friend(name, id, ppic, online);
                    friends.add(frnd);
                }

                String name = otherProfileResponse.name;
                Bitmap profilePic = stringToBitmap(otherProfileResponse.profilePic);
                int messages = otherProfileResponse.messages;
                String friended = otherProfileResponse.friended;
                String about = otherProfileResponse.about;
                List<Friend> friend = friends;

                OtherProfileInfo otherProfileInfo = new OtherProfileInfo(name,profilePic,messages,friended,about,friend);
                if (otherProfileResponse == null)
                    return;
                if (getOtherProfileListener != null)
                    getOtherProfileListener.onGetOtherProfile(otherProfileInfo);
            }
        };

        profileTask.execute(userID, otherID);
    }

    public void refreshConvoList()
    {
        AsyncTask<String, Integer, Networking.ConversationListResponse> convoListTask = new AsyncTask<String, Integer, Networking.ConversationListResponse>()
        {
            @Override
            protected Networking.ConversationListResponse doInBackground(String... strings)
            {
                return Networking.getConversationsList(strings[0]);
            }

            @Override
            protected void onPostExecute(Networking.ConversationListResponse conversationListResponse)
            {
                super.onPostExecute(conversationListResponse);

                if (conversationListResponse == null)
                    return;

                conversationList = new ArrayList<>();

                for (Networking.Conversation conv : conversationListResponse.conversations)
                {
                    Networking.User[] users = conv.users;

                    String lm = conv.lastMessage;
                    String ldt = conv.lastDateTime;
                    String convoID = conv.convoID;

                    Conversation convo = new Conversation(users, lm, ldt, convoID);
                    conversationList.add(convo);
                }

                if (getConvoListListener != null)
                    getConvoListListener.onGetConvoList();
            }
        };

        convoListTask.execute(userID);
    }

    public void getNewMessagesInConvo(String convoID)
    {
        AsyncTask<String, Integer, Networking.ConversationMessagesResponse> task = new AsyncTask<String, Integer, Networking.ConversationMessagesResponse>()
        {
            @Override
            protected Networking.ConversationMessagesResponse doInBackground(String... strings)
            {
                return Networking.getMessagesFromConversation(strings[0], strings[1]);
            }

            @Override
            protected void onPostExecute(Networking.ConversationMessagesResponse conversationMessagesResponse)
            {
                super.onPostExecute(conversationMessagesResponse);
                if (conversationMessagesResponse == null)
                    return;
                /* TODO: Save these to their folder designated by convoID
                    or let the convo activity do that, regardless
                    the messages must be converted and saved */

                if (getNewMessagesListener != null)
                    getNewMessagesListener.onGetNewMessages(conversationMessagesResponse);
            }
        };

        task.execute(userID, convoID);
    }

    public void refreshGlobalSoundboard()
    {
        AsyncTask<Void, Integer, Networking.SoundboardResponse> soundboardTask = new AsyncTask<Void, Integer, Networking.SoundboardResponse>()
        {
            @Override
            protected Networking.SoundboardResponse doInBackground(Void... voids)
            {
                return Networking.getGlobalSoundboard();
            }

            @Override
            protected void onPostExecute(Networking.SoundboardResponse soundboardResponse)
            {
                super.onPostExecute(soundboardResponse);

                if (soundboardResponse == null)
                    return;
                /* TODO: The board can contain the bites which have an id for where to look for the soundbite */
                globalSoundboard = new ArrayList<>(Arrays.asList(soundboardResponse.soundbites));

                if (getGlobalSoundboardListener != null)
                    getGlobalSoundboardListener.onGetGlobalBoard();
            }
        };

        soundboardTask.execute();
    }

    public void addFriend(String friendID)
    {
        AsyncTask<String, Integer, Networking.GenericResponse> addTask = new AsyncTask<String, Integer, Networking.GenericResponse>()
        {
            @Override
            protected Networking.GenericResponse doInBackground(String... strings)
            {
                return Networking.addFriend(strings[0], strings[1]);
            }

            @Override
            protected void onPostExecute(Networking.GenericResponse genericResponse)
            {
                super.onPostExecute(genericResponse);
                if (genericResponse == null)
                    return;
                if (onFriendshipChangeListener != null)
                    onFriendshipChangeListener.onFriendAdded(genericResponse);
            }
        };

        addTask.execute(userID, friendID);
    }

    public void removeFriend(String friendID)
    {
        AsyncTask<String, Integer, Networking.GenericResponse> removeTask = new AsyncTask<String, Integer, Networking.GenericResponse>()
        {
            @Override
            protected Networking.GenericResponse doInBackground(String... strings)
            {
                return Networking.removeFriend(strings[0], strings[1]);
            }

            @Override
            protected void onPostExecute(Networking.GenericResponse genericResponse)
            {
                super.onPostExecute(genericResponse);
                if (genericResponse == null)
                    return;
                if (onFriendshipChangeListener != null)
                    onFriendshipChangeListener.onFriendRemoved(genericResponse);
            }
        };

        removeTask.execute(userID, friendID);
    }

    public void createConversation(List<String> ids)
    {
        AsyncTask<List<String>, Integer, Networking.CreateConversationResponse> createTask = new AsyncTask<List<String>, Integer, Networking.CreateConversationResponse>()
        {
            @Override
            protected Networking.CreateConversationResponse doInBackground(List<String>... lists)
            {
                String userID = lists[0].get(0);
                lists[0].remove(0);
                return Networking.createNewConversation(userID, lists[0]);
            }

            @Override
            protected void onPostExecute(Networking.CreateConversationResponse createConversationResponse)
            {
                super.onPostExecute(createConversationResponse);

                if (createConversationResponse == null)
                    return;
                if (!createConversationResponse.success)
                    return;

                /* Refresh convo list */
                refreshConvoList();

                /* TODO: Consider adding a folder for this new convo for messages to be stored in */

                /* Should go into the conversation */
                if (onConvoListChangedListener != null)
                    onConvoListChangedListener.onConvoCreated(createConversationResponse.convoID);
            }
        };

        ids.add(0, userID);
        createTask.execute(ids);
    }

    public void deleteConversation(String convoID)
    {
        AsyncTask<String, Integer, Networking.GenericResponse> deleteTask = new AsyncTask<String, Integer, Networking.GenericResponse>()
        {
            @Override
            protected Networking.GenericResponse doInBackground(String... strings)
            {
                return Networking.deleteConversation(strings[0], strings[1]);
            }

            @Override
            protected void onPostExecute(Networking.GenericResponse genericResponse)
            {
                super.onPostExecute(genericResponse);
                if (genericResponse == null)
                    return;

                refreshConvoList();

                /* TODO: Remove that folder */

                if (onConvoListChangedListener != null)
                    onConvoListChangedListener.onConvoDeleted(genericResponse);
            }
        };

        deleteTask.execute(userID, convoID);
    }

    public void sendMessageToConvo(String convoID, String msg)
    {
        AsyncTask<String, Integer, Networking.GenericResponse> sendTask = new AsyncTask<String, Integer, Networking.GenericResponse>()
        {
            @Override
            protected Networking.GenericResponse doInBackground(String... strings)
            {
                return Networking.sendMessageToConversation(strings[0], strings[1], strings[2]);
            }

            @Override
            protected void onPostExecute(Networking.GenericResponse genericResponse)
            {
                super.onPostExecute(genericResponse);
                if (genericResponse == null)
                    return;

                // ? append to current convo list
                /* TODO: Add this message to the convo, if sent successfully */

                if (onMessageSentListener != null)
                    onMessageSentListener.onMessageSent(genericResponse);
            }
        };

        sendTask.execute(userID, convoID, msg);
    }

    public void updateUserProfile(String pic, String desc)
    {
        AsyncTask<String, Integer, Networking.GenericResponse> updateTask = new AsyncTask<String, Integer, Networking.GenericResponse>()
        {
            @Override
            protected Networking.GenericResponse doInBackground(String... strings)
            {
                return Networking.updateUserProfile(strings[0], strings[1], strings[2]);
            }

            @Override
            protected void onPostExecute(Networking.GenericResponse genericResponse)
            {
                super.onPostExecute(genericResponse);
                if (genericResponse == null)
                    return;
                if(onProfileUpdatedListener != null)
                    onProfileUpdatedListener.onProfileUpdated(genericResponse);
            }
        };

        updateTask.execute(userID, pic, desc);
    }

    public void uploadSoundbite(String biteName, String bite)
    {
        AsyncTask<String, Integer, Networking.GenericResponse> uploadTask = new AsyncTask<String, Integer, Networking.GenericResponse>()
        {
            @Override
            protected Networking.GenericResponse doInBackground(String... strings)
            {
                return Networking.uploadSoundbite(strings[0], strings[1], strings[2]);
            }

            @Override
            protected void onPostExecute(Networking.GenericResponse genericResponse)
            {
                super.onPostExecute(genericResponse);
                if (genericResponse == null)
                    return;
                /* Add to soundboard */
                /* TODO: Save this clip on the phone? */
                if (onSoundbiteUploadedListener != null)
                    onSoundbiteUploadedListener.onSoundbiteUploaded(genericResponse);
            }
        };

        uploadTask.execute(userID, biteName, bite);
    }

    public void requestForUpdates()
    {
        AsyncTask<String, Integer, Networking.UpdateResponse> requestTask = new AsyncTask<String, Integer, Networking.UpdateResponse>()
        {
            @Override
            protected Networking.UpdateResponse doInBackground(String... strings)
            {
                return Networking.requestForUpdates(strings[0]);
            }

            @Override
            protected void onPostExecute(Networking.UpdateResponse updateResponse)
            {
                super.onPostExecute(updateResponse);
                if (updateResponse == null)
                    return;

                /* iterate through every update */

                if (onUpdateReceivedListener != null)
                    onUpdateReceivedListener.onUpdateReceived(updateResponse);
            }
        };

        requestTask.execute(userID);
    }

    public void findUserByUsername(String username)
    {
        AsyncTask<String, Integer, Networking.FindUserResponse> findTask = new AsyncTask<String, Integer, Networking.FindUserResponse>()
        {
            @Override
            protected Networking.FindUserResponse doInBackground(String... strings)
            {
                return Networking.findUser(strings[0]);
            }

            @Override
            protected void onPostExecute(Networking.FindUserResponse findUserResponse)
            {
                super.onPostExecute(findUserResponse);
                if (findUserResponse == null)
                    return;

                if (onUserFoundListener != null)
                    onUserFoundListener.onUserFound(findUserResponse);
            }
        };

        findTask.execute(username);
    }

    /**
     * Returns null if something went wrong
     * @param bm
     * @return
     */
    public String bitmapToString(Bitmap bm)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bmBytes = stream.toByteArray();
        byte[] b64 = Base64.encode(bmBytes, Base64.DEFAULT);
        String pic = null;
        try
        {
            pic = new String(b64, "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return pic;
        }
    }

    /**
     * Returns null if something went wrong
     * @param pic
     * @return
     */
    public Bitmap stringToBitmap(String pic)
    {
        byte[] b64, bitmapBytes;
        Bitmap bm = null;
        try
        {
            b64 = pic.getBytes("UTF-8");
            bitmapBytes = Base64.decode(b64, Base64.DEFAULT);
            bm = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return bm;
        }
    }

    /**
     * Returns null if something went wrong
     * @param message
     * @return
     */
    public String messageBytesToString(byte[] message)
    {
        byte[] b64 = Base64.encode(message, Base64.DEFAULT);
        String msg = "";
        try
        {
            msg = new String(b64, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return msg;
        }
    }

    /**
     * Returns null if something went wrong
     * @param message
     * @return
     */
    public byte[] stringToMessageBytes(String message)
    {
        byte[] b64, msg = null;
        try
        {
            b64 = message.getBytes("UTF-8");
            msg = Base64.decode(b64, Base64.DEFAULT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return msg;
        }
    }

}
