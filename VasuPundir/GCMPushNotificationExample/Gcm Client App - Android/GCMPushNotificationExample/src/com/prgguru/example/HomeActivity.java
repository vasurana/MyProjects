package com.prgguru.example;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	TextView msgET, usertitleET;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// Intent Message sent from Broadcast Receiver
		String str = getIntent().getStringExtra("msg");

		// Get Email ID from Shared preferences
		SharedPreferences prefs = getSharedPreferences("UserDetails",
				Context.MODE_PRIVATE);
		String eMailId = prefs.getString("eMailId", "");
		// Set Title
		usertitleET = (TextView) findViewById(R.id.usertitle);
		
		if (!checkPlayServices()) {
			Toast.makeText(
					getApplicationContext(),
					"This device doesn't support Play services, App will not work normally",
					Toast.LENGTH_LONG).show();
		}
		
		usertitleET.setText("Hello " + eMailId + " !");
		// When Message sent from Broadcase Receiver is not empty
		if (str != null) {
			msgET = (TextView) findViewById(R.id.message);
			msgET.setText(str);
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(
						getApplicationContext(),
						"This device doesn't support Play services, App will not work normally",
						Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		} else {
			Toast.makeText(
					getApplicationContext(),
					"This device supports Play services, App will work normally",
					Toast.LENGTH_LONG).show();
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}
}
