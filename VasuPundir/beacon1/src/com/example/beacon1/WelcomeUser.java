package com.example.beacon1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeUser extends Activity {

	String userName;
	TextView user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		user = (TextView) findViewById(R.id.welcome);
		UserDatabase entry = new UserDatabase(WelcomeUser.this);

		entry.open();
		userName = entry.getUserDetails();
		entry.close();
		user.setText("Welcome " + userName);
		user.setTextSize(22);
		user.setTextColor(Color.WHITE);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2000);
				} catch (InterruptedException e){
					e.printStackTrace();
				} finally{
					
						Intent intent = new Intent(WelcomeUser.this,
								MainActivity.class);
						finish();
						startActivity(intent);
					
					
				}
			}
		};
		timer.start();
	}

}
