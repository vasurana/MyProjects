package com.xvidia.vowme.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.user.IdentityProvider;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText mFullnameEditText;
    private AutoCompleteTextView mEmailView;
    private EditText mPhoneNumberView;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        mFullnameEditText = (EditText) findViewById(R.id.register_full_name);
        nextButton = (Button) findViewById(R.id.button_name);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFullName();
            }
        });
        setFullName();

    }

    private void initialiseEmailPhoneView() {
        setContentView(R.layout.activity_email_phonenumber);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPhoneNumberView = (EditText) findViewById(R.id.register_phone_number);

        nextButton = (Button) findViewById(R.id.button_email_phone);
        mPhoneNumberView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.ime_action_next || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        setEmailId();
    }

    private void setFullName() {

            final IdentityManager identityManager =
                    AWSMobileClient.defaultMobileClient().getIdentityManager();
            final IdentityProvider identityProvider =
                    identityManager.getCurrentIdentityProvider();

            if (identityProvider != null) {
                mFullnameEditText.setText(identityManager.getUserName());
            }
        }
    private void setEmailId() {

        final IdentityManager identityManager =
                AWSMobileClient.defaultMobileClient().getIdentityManager();
        final IdentityProvider identityProvider =
                identityManager.getCurrentIdentityProvider();

        if (identityProvider != null) {
            mEmailView.setText(identityManager.getUserEmailId());
        }
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPhoneNumberView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPhoneNumberView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPhoneNumberView.setError(getString(R.string.error_invalid_password));
            focusView = mPhoneNumberView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }else{
            Intent desire = new Intent();
            desire.setClass(RegisterActivity.this,ProfileActivity.class);
            startActivity(desire);
//                startActivityForResult(desire, 500);
//                overridePendingTransition(android.support.design.R.anim.abc_slide_in_bottom,android.support.design.R.anim.abc_slide_in_top);
            finish();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void saveFullName() {

        // Reset errors.
        mFullnameEditText.setError(null);

        // Store values at the time of the login attempt.
        String name = mFullnameEditText.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(name)) {
            mFullnameEditText.setError(getString(R.string.error_invalid_name));
            focusView = mFullnameEditText;
            cancel = true;
        } else {
            initialiseEmailPhoneView();
        }


        if (cancel) {
            focusView.requestFocus();
        }
    }

}
