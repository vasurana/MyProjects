package com.example.beacon1;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	private static final String TAG = Register.class.getSimpleName();
	private Button btnRegister;
	private EditText inputEmail;
	private EditText inputPhoneNumber;
	private EditText inputName;
	private ProgressDialog pDialog;
	private SessionManager session;
	private UserDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register);
		inputName = (EditText) findViewById(R.id.name);
		inputEmail = (EditText) findViewById(R.id.email);
		inputPhoneNumber = (EditText) findViewById(R.id.number);
		btnRegister = (Button) findViewById(R.id.register);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// SQLite database handler
		

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String name = inputName.getText().toString().trim();
				String email = inputEmail.getText().toString().trim();
				String number = inputPhoneNumber.getText().toString().trim();
				UserDatabase entry = new UserDatabase(Register.this);
				entry.open();		
				entry.updateEntry(name, email, number);
				entry.close();
				if (!name.isEmpty() && !email.isEmpty() && !number.isEmpty()) {
					
					Intent intent = new Intent(Register.this,
							WelcomeUser.class);
					finish();
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(),
							"Please enter your details!", Toast.LENGTH_LONG)
							.show();
				}

			}

		});

	}


	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
