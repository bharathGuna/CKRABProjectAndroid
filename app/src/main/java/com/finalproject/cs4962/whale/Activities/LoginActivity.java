package com.finalproject.cs4962.whale.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.finalproject.cs4962.whale.Networking.DataManager;
import com.finalproject.cs4962.whale.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,DataManager.OnAccountCreatedListener
{

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "Bharath:Bharath", "Charles:Charles", "Rajul:Rajul", "Aaron:Aaron"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView username;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences preferences;
    private final String LOGIN = "LOGIN";
    private final String USERID = "USERID";
    private final String USERNAME = "USERNAME";
    private boolean login;
    private String userid;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        preferences = getPreferences(0);

        /*if(preferences != null && preferences.contains(LOGIN))
        {
            login = preferences.getBoolean(LOGIN,false);
            userid = preferences.getString(USERID, "");
            name = preferences.getString(USERNAME,"");
            if(login)
            {
                DataManager.getInstance().setUserID(userid);
                DataManager.getInstance().setUsername(name);
                finish();
                Intent toMainActivityIntent = new Intent();
                toMainActivityIntent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(toMainActivityIntent);
            }
        }
        */
        DataManager.getInstance().setOnAccountCreatedListener(this);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        username = (AutoCompleteTextView) findViewById(R.id.username);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //attemptLogin();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                name = username.getText().toString();
                // Check for a valid email address.
                if (TextUtils.isEmpty(name))
                {
                    username.setError(getString(R.string.error_field_required));
                    username.requestFocus();
                }
                else
                {
                    showProgress(true);
                    DataManager.getInstance().createAccount(name);
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onAccountCreated()
    {
        showProgress(false);
        String id = DataManager.getInstance().getUserID();
        //String id = "testing purposes";
        if (id.equals("FAILED"))
        {
            username.setError("Failed to connect to server.");
            username.requestFocus();
        }
        else if (id.equals("TAKEN"))
        {
            username.setError("Username is already taken.");
            username.requestFocus();
        }
        else
        {
            finish();
        /*    SharedPreferences preferences = getPreferences(0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(LOGIN, true);
            editor.putString(USERID,id);
            editor.putString(USERNAME,name);
            editor.commit();*/
            Intent toMainActivityIntent = new Intent();
            toMainActivityIntent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(toMainActivityIntent);
        }
    }

    private void populateAutoComplete()
    {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        username.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = username.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if ( !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            username.setError(getString(R.string.error_field_required));
            focusView = username;
            cancel = true;
        }
        else if (!isEmailValid(email))
        {
            username.setError(getString(R.string.error_invalid_email));
            focusView = username;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email)
    {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password)
    {
        //TODO: Replace this with your own logic
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection)
    {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        username.setAdapter(adapter);
    }

    private interface ProfileQuery
    {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String username;
        private final String mPassword;

        UserLoginTask(String email, String password)
        {
            username = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            // TODO: attempt authentication against a network service.

            try
            {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                return false;
            }

            //checking if valid login
            /*
            for (String credential : DUMMY_CREDENTIALS)
            {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(username))
                {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
*/
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            showProgress(false);

            if (success)
            {
                finish();
                Intent toMainActivityIntent = new Intent();
                toMainActivityIntent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(toMainActivityIntent);
            }
            else
            {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

