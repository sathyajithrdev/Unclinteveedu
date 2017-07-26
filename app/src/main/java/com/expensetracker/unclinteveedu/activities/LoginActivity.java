package com.expensetracker.unclinteveedu.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expensetracker.unclinteveedu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends BaseActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mUsersReference = database.getReference("users");


    // UI references.
    private AutoCompleteTextView mTvUserName;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getIsUserLoggedIn()) {
            moveToHomeScreen();
        }
        // Set up the login form.
        mTvUserName = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {

        // Reset errors.
        mTvUserName.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mTvUserName.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userName address.
        if (TextUtils.isEmpty(userName)) {
            mTvUserName.setError(getString(R.string.error_field_required));
            focusView = mTvUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Query userQuery = mUsersReference.orderByChild("userName").equalTo(userName);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isValid = false;
                    if (dataSnapshot != null) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            if (d.child("password").getValue().toString().equals(password)) {
                                setIsUserLoggedIn(true);
                                setLoggedInUserId(d.child("userId").getValue().toString());
                                showProgress(false);
                                isValid = true;
                                moveToHomeScreen();
                                break;
                            }
                        }
                    }

                    if (!isValid) {
                        Toast.makeText(LoginActivity.this, "Invalid user name or password", Toast.LENGTH_LONG).show();
                        showProgress(false);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    showProgress(false);
                }
            });
        }
    }

    private void moveToHomeScreen() {
        startActivity(new Intent(this, MainActivity.class));
        supportFinishAfterTransition();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }
}

