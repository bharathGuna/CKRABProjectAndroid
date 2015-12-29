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

import com.finalproject.cs4962.whale.DataManager;
import com.finalproject.cs4962.whale.Networking;
import com.finalproject.cs4962.whale.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A login screen that offers login via username/ip. You only have to login once now. 
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,DataManager.OnAccountCreatedListener
{

    // UI references.
    private AutoCompleteTextView username;
    private EditText ipView;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences preferences;
    private final String USERID = "USERID";
    private final String USERNAME = "USERNAME";
    //Debugging
    private final String IP = "IP";
    private boolean login;
    private String userid;
    private String name;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        DataManager.getInstance().setOnAccountCreatedListener(this);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        username = (AutoCompleteTextView) findViewById(R.id.username);
        populateAutoComplete();

        ipView = (EditText) findViewById(R.id.ip);
        Button signInRegisterButton = (Button) findViewById(R.id.email_sign_in_button);

        ipView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE)
                {
                    if (username.getText().length() > 0 && ipView.length() > 0)
                    {
                        String ip = ipView.getText().toString();
                        authenticateIP(ip);
                    }

                }
                return false;
            }
        });


        signInRegisterButton.setOnClickListener(new OnClickListener()
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

        preferences = getPreferences(0);

        if(preferences != null && preferences.contains(USERNAME) && preferences.contains(USERID) && preferences.contains(IP))
        {
            userid = preferences.getString(USERID, "");
            name = preferences.getString(USERNAME,"");
            ip = preferences.getString(IP,"");

            if(!name.isEmpty() )
            {
                DataManager.getInstance().setUserID(userid);
                DataManager.getInstance().setUsername(name);
                username.setText(name);
                signInRegisterButton.setText("Login");
                ipView.requestFocus();
            }
            if(!ip.isEmpty())
            {
                if(authenticateIP(ip))
                {
                    signInRegisterButton.performClick();
                }
                ipView.setText(ip);
            }
        }

    }

    private boolean authenticateIP(String ip)
    {
        Matcher matcher = IP_ADDRESS.matcher(ip);
        if (matcher.matches())
        {
            Networking.setServerIp(ip);
            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setEnabled(true);
            return true;
        }
        else
        {
            ipView.setError("IP is invalid");
            return false;
        }
    }

    @Override
    public void onAccountCreated()
    {
        showProgress(false);
        String id = DataManager.getInstance().getUserID();
        //String id = "testing purposes";
        if (id.equals("FAILED"))
        {
            ipView.setError("Failed to connect to server.");
            ipView.setText("");
            ipView.requestFocus();
        }
        else if (id.equals("TAKEN"))
        {
            username.setError("Username is already taken.");
            username.requestFocus();
        }
        else
        {
            finish();
            DataManager.getInstance().login();
            SharedPreferences preferences = getPreferences(0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(USERID,id);
            editor.putString(USERNAME,name);
            editor.putString(IP,ipView.getText().toString());
            editor.commit();
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
    private static final Pattern IP_ADDRESS
            = Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                    + "|[1-9][0-9]|[0-9]))");


}

