package com.example.thenewboston;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SharedPrefs extends Activity implements OnClickListener{
	EditText sharedData;
	TextView dataResults;
	SharedPreferences someData;
	public static String filename = "mySharedString";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharedpreferences);
		Button save = (Button)findViewById(R.id.buttonSave);
		Button load = (Button)findViewById(R.id.buttonLoad);
		sharedData = (EditText) findViewById(R.id.etSharedPrefs);
		dataResults = (TextView) findViewById(R.id.tvLoadSharedPrefs);
		save.setOnClickListener(this);
		load.setOnClickListener(this);
		someData = getSharedPreferences(filename,MODE_PRIVATE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.buttonSave:
			String stringData = sharedData.getText().toString();
			Editor editor = someData.edit();
			editor.putString("sharedString", stringData);
			editor.commit();			
			break;
			
		case R.id.buttonLoad:
			someData = getSharedPreferences(filename, MODE_PRIVATE);
			String dataReturned  = someData.getString("sharedString", "Couldn't Load Data");
			dataResults.setText(dataReturned);
			
			break;
		}
	}
	

}
