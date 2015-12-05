package com.finalproject.cs4962.whale;

import android.os.AsyncTask;

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

    private String userID;
    private OnAccountCreatedListener onAccountCreatedListener;
    private OnUserLoggedInListener onUserLoggedInListener;
    private GetFriendsListener getFriendsListener;

    private DataManager()
    {

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
}
