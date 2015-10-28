package in.com.app.updater;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.com.app.AppMain;
import in.com.app.IDisplayLayout;
import in.com.app.data.LogData;
import in.com.app.model.LayoutTimeData;
import in.com.app.model.MediaTimeData;
import in.com.app.model.OnOffTimeData;
import in.com.app.model.IAPIConstants;
import in.com.app.network.ServiceURLManager;
import in.com.app.network.VolleySingleton;
import in.com.app.storage.caching.sqlight.MySqliteHelper;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView.BufferType;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class extends {@link BroadcastReceiver} to update nework change information
 * @author Ravi@xvidia
 * @since Version 1.0i7
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
 boolean hitRequest = false;
// boolean noRequestAgain = false;
    @Override
    public void onReceive(final Context context, final Intent intent) {
		 try{
			 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS",Locale.ENGLISH);
				Date now = new Date();
		        String status = NetworkUtil.getConnectivityStatusString(context);
		        if(status.equalsIgnoreCase("Not connected to Internet")){
//		        	HeartBeatData.getInstance().setNeworkType(status);
		        	hitRequest = false;
		        	String  data =  "Time: "+formatter.format(now);
		        	LogData.getInstance().setInternetConnection(context, false);
		        	DataCacheManager.getInstance(context).saveSettingData(IDisplayLayout._KEY_XVIDIA_STATE_NETWORK_OFF_TIME, data);
		        }else{
		        	String  data =  "Time: "+formatter.format(now);
		        	Log.d("NETWORK STATUS", ""+status);
		        	hitRequest = true;
		        	LogData.getInstance().setInternetConnection(context, true);
//		        	HeartBeatData.getInstance().setNeworkType(status);
		        	DataCacheManager.getInstance(context).saveSettingData(IDisplayLayout._KEY_XVIDIA_STATE_NETWORK_ON_TIME, data);
		        	DataCacheManager.getInstance(context).saveSettingData(IDisplayLayout._KEY_XVIDIA_STATE_NETWORK_TYPE, status);
		        	
			        
		        }
		        if(hitRequest && 
			        	LogData.getInstance().getRequestSend(context)){

		        	LogData.getInstance().setRequestSend(context, false);
			       try{
			    	   syncSavedListMediaLayoutData(context);
			       }catch (IOException e) {
			       }catch (Exception e) {
					}
			        boolean flag = LogData.getInstance().setNetworkType(context, status);
			        AppMain.connectionStatus.setText(status, BufferType.NORMAL);
			        try{
			        	if(flag){
//			        		sendLogRequest(context,LogUtility.getInstance().getNetworkJson(context),LogUtility.getNewtorkLogUrl());
			        	}

			        }catch (Exception e) {
					}
		        }
		        //Toast.makeText(context, status, Toast.LENGTH_LONG).show();
		 }catch(Exception e){
			 
		 }
    }
    private void syncSavedListMediaLayoutData(Context context) throws IOException{
		  Vector<MediaTimeData> mList = new Vector<MediaTimeData>();
	       Vector<LayoutTimeData> mListLayout = new Vector<LayoutTimeData>();
	       Vector<OnOffTimeData> onOffListBox = new Vector<OnOffTimeData>();
	       Vector<OnOffTimeData> onOffListScreen = new Vector<OnOffTimeData>();
	       Vector<OnOffTimeData> onOffListAPP = new Vector<OnOffTimeData>();
	       
	       onOffListBox = DataCacheManager.getInstance(context).getAllOnOffTimeBoxData();
	       onOffListAPP= DataCacheManager.getInstance(context).getAllOnOffTimeAppData();
	       onOffListScreen = DataCacheManager.getInstance(context).getAllOnOffTimeScreenData();
	       
	       mListLayout = DataCacheManager.getInstance(context).getAllLayoutTimeData();
	       mList = DataCacheManager.getInstance(context).getAllMediaTimeData();
	       
	       if(onOffListScreen !=null&& onOffListScreen.size()>0){
               ObjectMapper mapper = new ObjectMapper();
               String jsonRequestString = null;
               try {
                   jsonRequestString = mapper.writeValueAsString(onOffListScreen);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_SCREEN_OFFLINE);
               sendSyncRequest(context,url, jsonRequestString);

           }
	       if(onOffListBox !=null&& onOffListBox.size()>0){
               ObjectMapper mapper = new ObjectMapper();
               String jsonRequestString = null;
               try {
                   jsonRequestString = mapper.writeValueAsString(onOffListBox);
               } catch (IOException e) {
//                   e.printStackTrace();
               }
               String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_BOX_OFFLINE);
               sendSyncRequest(context,url, jsonRequestString);
          }
	       if(onOffListAPP !=null&& onOffListAPP.size()>0) {
               ObjectMapper mapper = new ObjectMapper();
               String jsonRequestString = null;
               try {
                   jsonRequestString = mapper.writeValueAsString(onOffListAPP);
               } catch (IOException e) {
//                   e.printStackTrace();
               }
               String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_APP_OFFLINE);
               sendSyncRequest(context,url, jsonRequestString);
           }
	       if(mList !=null&& mList.size()>0){
              ObjectMapper mapper = new ObjectMapper();
               String jsonRequestString = null;
               try {
                   jsonRequestString = mapper.writeValueAsString(mList);
               } catch (IOException e) {
//                   e.printStackTrace();
               }
               String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_MEDIA_TIME_OFFLINE);
               sendSyncRequest(context,url,jsonRequestString);
           }
	       if(mListLayout !=null&&  mListLayout.size()>0){
               ObjectMapper mapper = new ObjectMapper();
               String jsonRequestString = null;
               try {
                   jsonRequestString = mapper.writeValueAsString(mListLayout);
               } catch (IOException e) {
//                   e.printStackTrace();
               }
                String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_LAYOUT_TIME_OFFLINE);
               sendSyncRequest(context,url,jsonRequestString);
           }
	  }
    private void sendLogRequest(Context ctx, JSONObject jsonObject, String urlStr){
  	  try {
  		  if(LogUtility.checkNetwrk(ctx)){
              JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlStr, jsonObject, new Response.Listener<JSONObject>() {

                  @Override
                  public void onResponse(JSONObject response) {
//
//                      Log.i("Volley JSONObject ", response.toString());
//                      if (urlStr.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_LAYOUT_TIME))) {
//                          ObjectMapper mapper = new ObjectMapper();
//                          LayoutTimeData obj = null;
//                          try {
//                              obj = mapper.readValue(response.toString(), LayoutTimeData.class);
//                          } catch (IOException e) {
//                              e.printStackTrace();
//                          }
////                            ModelManager.getInstance().setLayoutTimeModel(responseJSON);
//                          String deviceId = obj.getId();
//                          String longitude = obj.getLongitude();
//                          if (!longitude.isEmpty()) {
//                              DataCacheManager.getInstance(AppMain.getAppMainContext()).removeLayoutTimeById(deviceId);
//                          }

//                      }
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
    private void sendSyncRequest(final Context ctx, final String url,String arrayJson){
        JSONArray jsonRequest = null;

        try {
            jsonRequest = new JSONArray(arrayJson);
        } catch (JSONException e) {
        }
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.i("NetwrkRecr JSONObject ", response.toString());
                if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_SCREEN_OFFLINE))) {

                    DataCacheManager.getInstance(AppMain.getAppMainContext()).removeOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_SCREEN_TABLE);

                }else if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_APP_OFFLINE))) {

                    DataCacheManager.getInstance(AppMain.getAppMainContext()).removeOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_APP_TABLE);

                }else if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_BOX_OFFLINE))) {

                    DataCacheManager.getInstance(AppMain.getAppMainContext()).removeOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_BOX_TABLE);

                }else if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_LAYOUT_TIME_OFFLINE))) {

                    DataCacheManager.getInstance(AppMain.getAppMainContext()).removeLayoutTimeData();

                }else if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_MEDIA_TIME_OFFLINE))) {

                    DataCacheManager.getInstance(AppMain.getAppMainContext()).removeMediaTimeData();
                }
                LogData.getInstance().setRequestSend(ctx, true);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                LogData.getInstance().setRequestSend(ctx, true);
            }
        });
        VolleySingleton.getInstance(ctx).addToRequestQueue(request);
    }
}
