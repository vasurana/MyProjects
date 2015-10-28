package in.com.app.updater;

import in.com.app.network.VolleySingleton;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * This class extends {@link BroadcastReceiver} to send hearbeat info to server
 * @author Ravi@xvidia
 * @since Version 1.0
 */
public class HeartBeatReceiver extends BroadcastReceiver {
    private static final String DEBUG_TAG = "HeartBeatReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d(DEBUG_TAG, "HEARTBEAT");
//        String updateUrl ="http://192.168.1.5:9000/log";
//        String updateUrl ="http://54.251.108.112:9000/log";
        try{

        	LogUtility.getInstance().setPlayerStatus(context);
//        	if(AppState.ACTION_ON_PAUSE){
//        		Intent myIntent = new Intent(context, AppMain.class);
//				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(myIntent);
//        		
//        	}
//        	sendLogRequest(context,LogUtility.getInstance().getHeartBeatJSON(context),LogUtility.getHearbeatUrl());
        }catch (Exception e) {
		}
        //String updateStr = "{\"xiboData\":{\"businessOutput\":{\"updateVersion\": \"0.1\",\"updateUrl\": \"http://122.160.145.138:9090/mc/getsmartclient?filePath=android/5.1.4/1.0.0.1/MyVodafone.apk\",\"opStatus\":\"0\",\"errorMessage\":\"An updated version is available\"}}}";
    	//String updateStr = "<?xml version='1.0' encoding='UTF-8' standalone='yes'><xiboData><businessOutput><updateAvailable>true</updateAvailable><opStatus>0</opStatus><errorMessage>An updated version is available</errorMessage></businessOutput></xiboData>";
 
    }
    
    private void sendLogRequest(Context ctx, JSONObject jsonObject, String urlStr){
          try {
              if(LogUtility.checkNetwrk(ctx)){
                  JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlStr, jsonObject, new Response.Listener<JSONObject>() {

                      @Override
                      public void onResponse(JSONObject response) {
                      }
                  }, new Response.ErrorListener() {

                      @Override
                      public void onErrorResponse(VolleyError error) {

//                        Log.e("VolleyError ", error.getMessage());
                      }
                  });
                  VolleySingleton.getInstance(ctx).addToRequestQueue(request);
              }
          }catch(Exception e){

          }
	}
}
