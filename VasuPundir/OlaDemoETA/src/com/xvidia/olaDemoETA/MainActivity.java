package com.xvidia.olaDemoETA;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xvidia.network.ServiceURL;
import com.xvidia.network.VolleySingleton;

public class MainActivity extends ActionBarActivity implements LocationListener, OnCheckedChangeListener, OnClickListener {
	
	RequestQueue requestQueue;
	LinearLayout autoLinearlayout, manualLinearLayout;
	EditText editTextLatitude, editTextLongitude;
	TextView autoLongitude, autolatitude;
	RadioGroup selectionList;
	Button result;
	String longitude, latitude;
	private String provider; 
	private Criteria criteria;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		requestQueue = Volley.newRequestQueue(this);
//		getBoxNameAndAssetIdData(getApplicationContext());
		autoLongitude = (TextView)findViewById(R.id.tvLongitude);
		editTextLatitude = (EditText)findViewById(R.id.etLatitude);
		editTextLongitude = (EditText)findViewById(R.id.etLongitude);
		autolatitude = (TextView)findViewById(R.id.tvLatitude);
		autoLinearlayout = (LinearLayout)findViewById(R.id.linearLayoutAuto);
		manualLinearLayout = (LinearLayout)findViewById(R.id.linearLayoutManual);
		selectionList = (RadioGroup)findViewById(R.id.rgButtons);
		selectionList.setOnCheckedChangeListener(this);
		result = (Button)findViewById(R.id.bProceed);
		result.setOnClickListener(this);
		initializeLocationManager();
		
}		
	private void getBoxNameAndAssetIdData(Context ctx){
		try {
				String lat = latitude;
				String lng = longitude;
				
				String url = ServiceURL.getCabStatusURL(lat,lng); //(IAPIConstants.API_KEY_BOX_GET_INVENTORY_DATA);
				Log.i("url", url);
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.i("Response", response.toString());
						
						ObjectMapper mapper = new ObjectMapper();
						Root obj = null;
						try {
							obj = mapper.readValue(response.toString(), Root.class);
						} catch (IOException e) {
							e.printStackTrace();
						}
						}
					 
					
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

                        Log.e("VolleyError ", error.getMessage());
                        
					}
				}){
					@Override
					public Map getHeaders() throws AuthFailureError {
					Map headers = new HashMap();
					headers.put("X-APP-TOKEN", "7b9cd445494f408a8d3e2ec9fb304bbc");
					return headers;
					}
				
				
				};
				
				VolleySingleton.getInstance(ctx).addToRequestQueue(request);
//			}
		}catch(Exception e){

		}
}
	private void initializeLocationManager(){
		// Get the location manager
//		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		  // Define the criteria how to select the location provider
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);	//default

		criteria.setCostAllowed(false);
		try {
			LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			//mylistener = new MyLocationListener();
			provider = mLocationManager.getBestProvider(criteria, false);
			Location location = mLocationManager.getLastKnownLocation(provider);
			// getting GPS status
			boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
              
			} else {
				// First get location from Network Provider
				if (isNetworkEnabled) {
					if (mLocationManager != null) {
					mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, this);
						location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
//							Toast.makeText(this, "Location service", Toast.LENGTH_LONG).show();
//							mylistener.onLocationChanged(location);
							latitude =String.valueOf(location.getLatitude());
							longitude= String.valueOf(location.getLongitude());
						
                        }
					}
//				}else{
//					Toast.makeText(this, "No Location service Network", Toast.LENGTH_LONG).show();
				}
				//get the location by gps
				if (isGPSEnabled) {
					if (location == null) {
                        if (mLocationManager != null) {
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
//						Log.d("GPS Enabled", "GPS Enabled");
//						if (mLocationManager != null) {
//                            Toast.makeText(this, "Location service GPS", Toast.LENGTH_LONG).show();
//							mylistener.onLocationChanged(location);
                            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
    							latitude =String.valueOf(location.getLatitude());
    							longitude= String.valueOf(location.getLongitude());
                              
                            }
                        }
                    }
				}else{
//					Toast.makeText(this, "no Location service GPS", Toast.LENGTH_LONG).show();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1){
		case R.id.rbManual:
			
			autoLinearlayout.setVisibility(View.GONE);
			manualLinearLayout.setVisibility(View.VISIBLE);
			
			
			break;
			
		case R.id.rbAutomatic:
			
			autoLinearlayout.setVisibility(View.VISIBLE);
			manualLinearLayout.setVisibility(View.GONE);
			autoLongitude.setText(longitude);
			autolatitude.setText(latitude);
			
			break;
		
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//this data has to be send to another activity (opened.java) which will be opened after the Proceed button will be clicked
		// i am using bundle to transfer.
		// i don't remember the layout of the next activity you told me. So i have done upto this far. 
		String stringLatitude = editTextLatitude.getText().toString();
		String stringLongitude = editTextLongitude.getText().toString();
		double doubleLatitude = Double.parseDouble(stringLatitude);
		double doubleLongitude = Double.parseDouble(stringLongitude);
		
		if(doubleLatitude >= -90 && doubleLatitude <=90){
			
			if(doubleLongitude >= -180 && doubleLongitude <= 180){
				/*Bundle sendBundle = new Bundle();
				sendBundle.putString("keyLatitude", stringLatitude);
				sendBundle.putString("keyLongitude", stringLongitude);
				Intent intent = new Intent(MainActivity.this, Opened.class);
				intent.putExtras(sendBundle);
				startActivity(intent);*/
				Toast tStatus = Toast.makeText(this, "Proceeding...",Toast.LENGTH_SHORT);
				tStatus.show();
				
			}else{
				Toast tStatus = Toast.makeText(this, "Please enter the correct Longitude value",Toast.LENGTH_SHORT);
				tStatus.show();
			}
		}else if(doubleLongitude >= -180 && doubleLongitude <= 180){
			Toast tStatus = Toast.makeText(this, "Please enter the correct Latitude value",Toast.LENGTH_SHORT);
			tStatus.show();
			
		}else{
			Toast tStatus = Toast.makeText(this, "Please enter the correct values",Toast.LENGTH_SHORT);
			tStatus.show();
		}
		
		
		 
	}
}
			
			
	

