package com.example.vasu.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class StatusActivity extends Activity {

    EditText editStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

        editStatus = (EditText) findViewById(R.id.edit_status);

    }

    public void onClick(View v) {
        final String statusText = editStatus.getText().toString();
        new Thread() {
            public void run() {
                try {
                    Twitter twitter = new Twitter("student", "password");
                    twitter.setAPIRootUrl("http://yamba.marakana.com/api");
                    twitter.setStatus(statusText);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}