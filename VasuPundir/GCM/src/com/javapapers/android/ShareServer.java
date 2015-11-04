package com.javapapers.android;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ShareServer extends Activity {

	String result = "";
	static final String TAG = "ShareServer Activity";

	public String shareRegIdWithAppServer(final Context context,
			final String regId) {

		RequestQueue queue = Volley.newRequestQueue(this);
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("regId", regId);

		String serverUrl = null;
		serverUrl = new String(Config.APP_SERVER_URL);

		StringBuilder postBody = new StringBuilder();
		Iterator<Entry<String, String>> iterator = paramsMap.entrySet()
				.iterator();

		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			postBody.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				postBody.append('&');
			}
		}

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, serverUrl, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						result = "RegId shared with Application Server. RegId: "
								+ regId;

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						NetworkResponse networkResponse = error.networkResponse;
						if (networkResponse != null) {
							result = "Post Failure." + " Status: "
									+ error.networkResponse.statusCode;
						} else {
							Log.d(TAG, "Volley Error object equals NULL");
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

		return result;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

}
