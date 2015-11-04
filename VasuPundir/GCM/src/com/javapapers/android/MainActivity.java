package com.javapapers.android;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {


	static final String TAG = "Main Activity";
	ShareExternalServer appUtil1;
	ShareServer appUtil;
	String regId;
	AsyncTask<Void, Void, String> shareRegidTask;
	String result;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appUtil = new ShareServer();

		regId = getIntent().getStringExtra("regId");
		Log.d("MainActivity", "regId: " + regId);

		final Context context = this;
		shareRegidTask = new AsyncTask<Void, Void, String>() {
			
			@Override
			protected String doInBackground(Void... params) {
				shareRegIdWithAppServer(context, regId);
				return regId;
				
				
			}

			@Override
			protected void onPostExecute(String regId) {
				/*shareRegidTask = null;
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();*/
			}

		};
		shareRegidTask.execute(null, null, null);
	}
	
	public void shareRegIdWithAppServer(final Context context,
			final String regId) {
	
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("regId", regId);

		String serverUrl = null;
		serverUrl = new String(Config.APP_SERVER_URL);

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, serverUrl, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						result = "RegId shared with Application Server. RegId: "
								+ regId;
						Toast.makeText(getApplicationContext(), result,
								Toast.LENGTH_LONG).show();

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						Integer v = error.networkResponse.statusCode;
						if (v != null) {
							result = "Post Failure." + " Status: "
									+ error.networkResponse.statusCode;
							
						} else {
							Log.d(TAG, "Volley Error: error object equals NULL");
						}
					}
				}) {
			@Override
			public HashMap<String, String> getParams() {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");

				return params;
			}
		};
		AppController.getInstance().addToRequestQueue(jsObjRequest);


	

	}
	
}
