package com.finalproject.cs4962.whale;

import android.os.AsyncTask;

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
        void onGetFriends(List<Networking.Friend> friends);
    }

    public interface GetUserProfileListener
    {
        void onGetProfile(Networking.PersonalProfileResponse profile);
    }

    public interface GetOtherProfileListener
    {
        void onGetOtherProfile(Networking.OtherProfileResponse profile);
    }

    public interface GetConvoListListener
    {
        void onGetConvoList();
    }

    public interface GetNewMessagesListener
    {
        void onGetNewMessages();
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

    private String userID;
    /* TODO: Convert to usable data objects */
    private List<Networking.Conversation> conversationList;
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

    private DataManager()
    {
        conversationList = new ArrayList<>();
        globalSoundboard = new ArrayList<>();
    }

    public String getUserID()
    {
        return userID;
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
                    return;
                /* Username was taken */
                if (createAccountResponse.userID.equals("TAKEN"))
                    return;

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
                List<Networking.Friend> friends = Arrays.asList(friendListResponse.friends);

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

                if (otherProfileResponse == null)
                    return;
                if (getOtherProfileListener != null)
                    getOtherProfileListener.onGetOtherProfile(otherProfileResponse);
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

                /* Set the conversation list */
                conversationList = new ArrayList<>(Arrays.asList(conversationListResponse.conversations));

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
                if (getNewMessagesListener != null)
                    getNewMessagesListener.onGetNewMessages();
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
                /* Set the board, might need to save all the messages as well */
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


}
