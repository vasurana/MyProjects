package com.example.thenewboston;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SQLiteExample extends Activity implements OnClickListener {
	
	Button sqlUpdate, sqlView, sqlGetInfo, sqlModify, sqlDelete;
	EditText sqlName, sqlHotness, sqlRow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqliteexample);
		sqlUpdate = (Button)findViewById(R.id.bSQLUpdate);
		sqlView = (Button)findViewById(R.id.bSQLOpenView);
		sqlDelete = (Button)findViewById(R.id.bSQLdelete);
		sqlGetInfo = (Button)findViewById(R.id.bGetInfo);
		sqlModify = (Button)findViewById(R.id.bSQLmodify);
		sqlName = (EditText)findViewById(R.id.etSQLName);
		sqlHotness = (EditText)findViewById(R.id.etSQLHotness);
		sqlRow = (EditText)findViewById(R.id.etSQLrowInfo);
		sqlUpdate.setOnClickListener(this);
		sqlView.setOnClickListener(this);
		sqlModify.setOnClickListener(this);
		sqlDelete.setOnClickListener(this);
		sqlGetInfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bSQLUpdate:
			
			boolean didItWork = true;
			
			try {
				String name = sqlName.getText().toString();
				String hotness = sqlHotness.getText().toString();
				
				HotOrNot entry = new HotOrNot(SQLiteExample.this);
				entry.open();
				entry.createEntry(name, hotness);
				entry.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				didItWork = false;
			}finally{
				if(didItWork){
					Dialog d = new Dialog(this);
					d.setTitle("Your data has been stored");
					TextView tv = new TextView(this);
					tv.setText("Success");
					tv.setGravity(Gravity.CENTER);
					d.setContentView(tv);
					d.show();
				}
			}
			
			break;
		case R.id.bSQLOpenView:
			
			Intent i = new Intent("com.example.thenewboston.SQLVIEW");
			startActivity(i);
			
			break;
		case R.id.bGetInfo:
			
			String s = sqlRow.getText().toString();
			long l = Long.parseLong(s);
			HotOrNot hon = new HotOrNot(this);
			hon.open();
			String returnedName = hon.getName(l);
			String returnedHotness = hon.getHotness(l);
			hon.close();
			
			sqlName.setText(returnedName);
			sqlHotness.setText(returnedHotness);
			
			break;
		case R.id.bSQLmodify:
			
			String mName = sqlName.getText().toString();
			String mHotness = sqlHotness.getText().toString();
			String sRow = sqlRow.getText().toString();
			long lRow = Long.parseLong(sRow);
			HotOrNot ex = new HotOrNot(this);
			ex.open();
			ex.updateEntry(lRow, mName, mHotness);
			ex.close();
			
			break;
		case R.id.bSQLdelete:
			
			String sRow1 = sqlRow.getText().toString();
			long lRow1 = Long.parseLong(sRow1);
			HotOrNot ex1 = new HotOrNot(this);
			ex1.open();
			ex1.deleteEntry(lRow1);
			ex1.close();
			
			break;
		}
		
	}
	

}
