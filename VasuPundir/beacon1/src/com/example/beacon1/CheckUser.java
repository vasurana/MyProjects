package com.example.beacon1;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class CheckUser extends Activity{
	
	String userName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		UserDatabase entry = new UserDatabase(CheckUser.this);
		
		entry.open();
		entry.addUser("isareonalltheifcategoriesss", "vivek.nith007@gmail.com", "8439640224");
		userName = entry.getUserDetails();
		entry.close();
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2000);
				} catch (InterruptedException e){
					e.printStackTrace();
				} finally{
					if(userName.equals("isareonalltheifcategoriesss")){
						Intent intent = new Intent(CheckUser.this,
								Register.class);
						finish();
						startActivity(intent);
					} else {
						Intent intent = new Intent(CheckUser.this,
								WelcomeUser.class);
						finish();
						startActivity(intent);
					}
				}
			}
		};
		timer.start();
		
		
	}

	
	

}
