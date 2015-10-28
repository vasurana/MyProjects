package in.com.app.updater;

import in.com.app.AppMain;
import in.com.app.AppState;
import in.com.app.FileManager;
import in.com.app.MyApplication;
import in.com.app.data.LogData;
import in.com.app.model.IAPIConstants;
import in.com.app.model.OnOffTimeData;
import in.com.app.network.ServiceURLManager;
import in.com.app.network.VolleySingleton;
import in.com.app.storage.caching.sqlight.MySqliteHelper;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility class
 * 
 * @author Ravi@xvidia
 * 
 */
public class LogUtility {

	static String FILE_NAME_ASSET = "assetId.txt";
	static String FILE_NAME_DISPLAY = "display.txt";
	static JSONArray object = new JSONArray();
	JSONObject heartBeatObject = new JSONObject();
	JSONObject activeLayoutObject = new JSONObject();
	JSONObject appStatusObject = new JSONObject();
	JSONObject appVersionObject = new JSONObject();
	JSONObject locationObject = new JSONObject();
	JSONObject newtorkObject = new JSONObject();
	JSONObject screenResolutionObject = new JSONObject();
	static String timeStamp; 
//	static String offTimeId; 
	private static  LogUtility instance = null;

	public static  LogUtility getInstance() {
		if (instance == null) {
			instance = new LogUtility();
		}
		return instance;
	}
	
	/**
	 * This method replaces substring between two strings
	 * 
	 * @param builder StringBuilder builder
	 * @param from string
	 * @param to string
	 * @return
	 * @deprecated
	 * @since version1.0
	 */
	public static String replaceAll(StringBuilder builder, String from,
			String to) {
		int index = builder.indexOf(from);
		while (index != -1) {
			builder.replace(index, index + from.length(), to);
			index += to.length(); // Move to the end of the replacement
			index = builder.indexOf(from, index);
		}
		return builder.toString();
	}

	/**
	 * This method write JSONObject
	 * 
	 * @param time variable for time string
	 * @param tag JSon Tag
	 * @param value JSon value
	 * @since version1.0
	 */
	public static void writeJSON(String time, String tag, String value) {
		JSONObject object = new JSONObject();
		try {
			object.put(LogData.TAG_TIME, time);
			object.put(tag, value);
		} catch (JSONException e) {
			// e.printStackTrace();
		}
		System.out.println(object);
		writeJSONArray(object);
	}

	/**
	 * This method gets the JSON for the {@link in.com.app.updater.LogUtility}
	 *
	 * @return JSONObject of {@link in.com.app.updater.LogUtility}
	 * @since version1.0
	 */
	public void setPlayerStatus(Context ctx) {
		try{
			saveOffTimeData(ctx);
			getHdmiConnectState();
		}catch(Exception e){

		}
	}
	/**
	 * This method gets the JSON for the {@link in.com.app.updater.LogUtility}
	 * 
	 * @return JSONObject of {@link in.com.app.updater.LogUtility}
	 * @since version1.0
	 */
	public JSONObject getHeartBeatJSON(Context ctx) {
		heartBeatObject = new JSONObject();
		try {
//			getFileInfo();
			heartBeatObject.put(LogData.TAG_APPID, LogData.getInstance().getAppID(ctx));
			heartBeatObject.put(LogData.TAG_TIME, getTimeStamp());
			heartBeatObject.put(LogData.TAG_ASSETID, LogData.getInstance().getAssetId(ctx));
			heartBeatObject.put(LogData.TAG_DisplayName, LogData.getInstance().getDisplayname(ctx));
		} catch (JSONException e) {
			 e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (heartBeatObject == null) {
			heartBeatObject = new JSONObject();
		}

		return heartBeatObject;
	}

	public JSONObject getActiveLayoutJSON(Context ctx) {
		activeLayoutObject = new JSONObject();

		try {
			activeLayoutObject.put(LogData.TAG_APPID, LogData.getInstance().getAppID(ctx));
			activeLayoutObject.put(LogData.TAG_TIME,getTimeStamp());
			activeLayoutObject.put(LogData.TAG_ASSETID, LogData.getInstance().getAssetId(ctx));
			activeLayoutObject.put(LogData.TAG_DisplayName, LogData.getInstance().getDisplayname(ctx));
			activeLayoutObject.put(LogData.TAG_PREV_LAYOUT, LogData.getInstance().getPrevLayout(ctx));
			activeLayoutObject.put(LogData.TAG_ACTIVE_LAYOUT, LogData.getInstance().getActiveLayout(ctx));
		} catch (JSONException e) {
			// e.printStackTrace();
		} catch (Exception e) {

		}
		if (activeLayoutObject == null) {
			activeLayoutObject = new JSONObject();
		}

		return activeLayoutObject;
	}
	
	public  JSONObject getAppStatusJSON(Context ctx) {
		appStatusObject = new JSONObject();

		try {
			appStatusObject.put(LogData.TAG_APPID, LogData.getInstance().getAppID(ctx));
			appStatusObject.put(LogData.TAG_TIME,getTimeStamp());
			appStatusObject.put(LogData.TAG_ASSETID, LogData.getInstance().getAssetId(ctx));
			appStatusObject.put(LogData.TAG_DisplayName, LogData.getInstance().getDisplayname(ctx));
			appStatusObject.put(LogData.TAG_PREV_APP_STATUS, LogData.getInstance().getPrevAppStatus(ctx));
			appStatusObject.put(LogData.TAG_APPLICATION_STATUS, LogData.getInstance().getAppStatus(ctx));
		} catch (JSONException e) {
			// e.printStackTrace();
		} catch (Exception e) {

		}
		if (appStatusObject == null) {
			appStatusObject = new JSONObject();
		}

		return appStatusObject;
	}
	
	public  JSONObject getLocationJson(Context ctx) {
		locationObject = new JSONObject();

		try {
			locationObject.put(LogData.TAG_APPID, LogData.getInstance().getAppID(ctx));
			locationObject.put(LogData.TAG_TIME,getTimeStamp());
			locationObject.put(LogData.TAG_ASSETID, LogData.getInstance().getAssetId(ctx));
			locationObject.put(LogData.TAG_DisplayName, LogData.getInstance().getDisplayname(ctx));
			locationObject.put(LogData.TAG_PREV_LOCATION, LogData.getInstance().getPrevLocation(ctx));
			locationObject.put(LogData.TAG_LOCATION, LogData.getInstance().getLocation(ctx));
		} catch (JSONException e) {
			// e.printStackTrace();
		} catch (Exception e) {

		}
		if (locationObject == null) {
			locationObject = new JSONObject();
		}

		return locationObject;
	}
	public  JSONObject getAppVersionJson(Context ctx) {
		appVersionObject = new JSONObject();

		try {
			appVersionObject.put(LogData.TAG_APPID, LogData.getInstance().getAppID(ctx));
			appVersionObject.put(LogData.TAG_TIME,getTimeStamp());
			appVersionObject.put(LogData.TAG_ASSETID, LogData.getInstance().getAssetId(ctx));
			appVersionObject.put(LogData.TAG_DisplayName, LogData.getInstance().getDisplayname(ctx));
			appVersionObject.put(LogData.TAG_PREV_LOCATION, LogData.getInstance().getPrevAppVersion(ctx));
			appVersionObject.put(LogData.TAG_LOCATION, LogData.getInstance().getAppVersion(ctx));
		} catch (JSONException e) {
			// e.printStackTrace();
		} catch (Exception e) {

		}
		if (appVersionObject == null) {
			appVersionObject = new JSONObject();
		}

		return appVersionObject;
	}
	public  JSONObject getNetworkJson(Context ctx) {
		newtorkObject = new JSONObject();

		try {
			newtorkObject.put(LogData.TAG_APPID, LogData.getInstance().getAppID(ctx));
			newtorkObject.put(LogData.TAG_TIME,getTimeStamp());
			newtorkObject.put(LogData.TAG_ASSETID, LogData.getInstance().getAssetId(ctx));
			newtorkObject.put(LogData.TAG_DisplayName, LogData.getInstance().getDisplayname(ctx));
			newtorkObject.put(LogData.TAG_PREV_IPADDRESS, LogData.getInstance().getPrevIpAddress(ctx));
			newtorkObject.put(LogData.TAG_IPADDRESS, LogData.getInstance().getIpAddress(ctx));
			newtorkObject.put(LogData.TAG_PREV_MAC_ADDRESS, LogData.getInstance().getPrevMacAddress(ctx));
			newtorkObject.put(LogData.TAG_MAC_ADDRESS, LogData.getInstance().getMacAddress(ctx));
			newtorkObject.put(LogData.TAG_PREV_NETWORK_TYPE, LogData.getInstance().getPrevNetworkType(ctx));
			newtorkObject.put(LogData.TAG_NETWORKTYPE, LogData.getInstance().getNetworkType(ctx));
		} catch (JSONException e) {
			// e.printStackTrace();
		} catch (Exception e) {

		}
		if (newtorkObject == null) {
			newtorkObject = new JSONObject();
		}

		return newtorkObject;
	}
	
	public  JSONObject getScreenReolutionJson(Context ctx) {
		screenResolutionObject = new JSONObject();

		try {
			screenResolutionObject.put(LogData.TAG_APPID, LogData.getInstance().getAppID(ctx));
			screenResolutionObject.put(LogData.TAG_TIME,getTimeStamp());
			screenResolutionObject.put(LogData.TAG_ASSETID, LogData.getInstance().getAssetId(ctx));
			screenResolutionObject.put(LogData.TAG_DisplayName, LogData.getInstance().getDisplayname(ctx));
			screenResolutionObject.put(LogData.TAG_PREV_SCREEN_RESOLUTION, LogData.getInstance().getPrevScreenResolution(ctx));
			screenResolutionObject.put(LogData.TAG_SCREEN_RESOLUTION, LogData.getInstance().getScreenResolution(ctx));
		} catch (JSONException e) {
			// e.printStackTrace();
		} catch (Exception e) {

		}
		if (screenResolutionObject == null) {
			screenResolutionObject = new JSONObject();
		}

		return screenResolutionObject;
	}
	/**
	 * This method sets the JSON for the {@link in.com.app.updater.LogUtility }
	 * 
	 * @since version1.0
	 */
	// static void setHeartBeatJSON() {
	// heartBeatObject = new JSONObject();
	// try {
	// getFileInfo();
	// heartBeatObject.put(TAG_TIME,
	// HeartBeatData.getInstance().getTimeStamp());
	// heartBeatObject.put(TAG_ASSETID, getAssetId());
	// heartBeatObject.put(TAG_DisplayName, getDisplayname());
	// heartBeatObject.put(TAG_SCREEN_RESOLUTION, getResolution());
	// heartBeatObject.put(TAG_ADDRESS, getAddress());
	// heartBeatObject.put(TAG_APPLICATION_STATUS, getAppStatus());
	// heartBeatObject.put(TAG_NETWORKTYPE,
	// HeartBeatData.getInstance().getneworkType());
	// heartBeatObject.put(TAG_ACTIVE_LAYOUT,
	// HeartBeatData.getInstance().getCurrenLayout());
	// heartBeatObject.put(TAG_IPADDRESS,
	// HeartBeatData.getInstance().getIpAddress());
	// heartBeatObject.put(TAG_MAC_ADDRESS,
	// HeartBeatData.getInstance().getMacAddress());
	// heartBeatObject.put(TAG_LOCATION,
	// HeartBeatData.getInstance().getLocation());
	// heartBeatObject.put(TAG_GEOPIP,
	// HeartBeatData.getInstance().getLocation());
	// heartBeatObject.put(TAG_APPVERSION, getAppVersion());
	// heartBeatObject.put(TAG_APPID, getAppID());
	// Log.i("HEARTBEAT value", ""+heartBeatObject.toString());
	// } catch (JSONException e) {
	// // e.printStackTrace();
	// }
	// }
	
	public static String getTimeStamp() {
		setTimeStamp();
		return timeStamp;
	}

	 static void setTimeStamp() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
		Date now = new Date();
		timeStamp = "" + formatter.format(now);

	}
	
//	 public static String getOffTimeId() {
//			return offTimeId;
//		}
//
//		public static void setOffTimeId(String id) {
//			offTimeId = id;
//
//		}
	/**
	 * This method creates array of JSONObject
	 * 
	 * @param obj JSONObject  tobe added to array
	 */
	private static void writeJSONArray(JSONObject obj) {
		object.put(obj);
//		System.out.println(object);
	}

	

	static String readTextFromFile(String filename) {
		String retMesg = "";
		try {
			// File myFile = new File("/sdcard/mysdfile.txt");
			String destPath = Environment.getExternalStorageDirectory()
					+ AppState.FILE_FOLDER;
			File myFile = new File(destPath + filename);
			if (myFile.exists()) {
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow;
				}
				myReader.close();
				retMesg = aBuffer;
			}
		} catch (Exception e) {
		}
		return retMesg;
	}

	private void getHdmiConnectState(){
	    String result = "0";
		 try
		    {

		    File file = new File("/sys/class/display/display0.HDMI/connect");
		    InputStream in = new FileInputStream(file);
		    byte[] re = new byte[32768];
		    int read = 0;
		    while ( (read = in.read(re, 0, 32768)) != -1)
		    {
		        String string="Empty";
		        string = new String(re, 0, read);
//		        Log.v("String_whilecondition","string="+string);
		        result = string;

		    }
		   in.close();
		   } 
	        catch (IOException ex) { 
	        	result = "";//""+ex.getMessage();
	        }
		 catch (Exception e) { 
	        	result = "";
	        }
		 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date now = new Date();
			String  data =  formatter.format(now);
			result = result.trim();
			String oldmesg = FileManager.readTextFromFile(AppState.FILE_NAME_HDMI);
			if(!oldmesg.equalsIgnoreCase(result)&& result.length()==1){
				
				String id = LogData.getInstance().getAppID(AppMain.getAppMainContext())+"_"+LogData.TAG_ONOFF_SCREEN+"_"+now.getTime();
				OnOffTimeData object = new OnOffTimeData();
	        	object.setId(id);
	        	object.setBoxId(LogData.getInstance().getAppID(AppMain.getAppMainContext()));
	        	if(result.equalsIgnoreCase("0")){
		        	object.setOnTime("");
		        	object.setOffTime(data);
	        	}else{
	        		object.setOnTime(data);
		        	object.setOffTime("");
	        	}
				DataCacheManager.getInstance(AppMain.getAppMainContext()).saveOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_SCREEN_TABLE, object);
				  if(checkNetwrk(AppMain.getAppMainContext())){
                      ObjectMapper mapper = new ObjectMapper();
                  String jsonRequestString = null;
                  try {
                      jsonRequestString = mapper.writeValueAsString(object);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                      JSONObject jsonRequest = null;
                      try {
                          jsonRequest = new JSONObject(jsonRequestString);
                      } catch (JSONException e) {
//                          e.printStackTrace();
                      }
                      final String url =new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_SCREEN);
                      JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

                          @Override
                          public void onResponse(JSONObject response) {
                              ObjectMapper mapper = new ObjectMapper();
                              OnOffTimeData obj = null;
                              try {
                                  obj = mapper.readValue(response.toString(), OnOffTimeData.class);
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                              String deviceId = obj.getId();
                              if (!deviceId.isEmpty()) {
                                  DataCacheManager.getInstance(AppMain.getAppMainContext()).removeOnOffTimeById(MySqliteHelper.TABLE_NAME_ONOFF_TIME_SCREEN_TABLE,deviceId );
                              }
                          }
                      }, new Response.ErrorListener() {

                          @Override
                          public void onErrorResponse(VolleyError error) {

                          }
                      });
                      VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);
				  }

			 		FileManager.writeTextToFile(result,AppState.FILE_NAME_HDMI);
			}

//			Toast.makeText(AppMain.this, "HDMI CONNECTED **** "+result, Toast.LENGTH_SHORT).show();
			
	}
	
	private void saveOffTimeData(Context context){
		String idNew = LogData.getInstance().getOfftimeId(context);//LogData.getInstance().getAppID(context)+"_"+"BOX"+"_"+(now.getTime()+1000);
		if(idNew == null){
			Date now = new Date();
			idNew=LogData.getInstance().getAppID(context)+"_"+LogData.TAG_ONOFF_BOX+"_"+now.getTime();
		}else if(idNew.isEmpty()){
			Date now = new Date();
			idNew=LogData.getInstance().getAppID(context)+"_"+LogData.TAG_ONOFF_BOX+"_"+now.getTime();
		}
		LogData.getInstance().setOfftimeId(context, idNew);
		OnOffTimeData objectOff = new OnOffTimeData();
    	objectOff.setId(idNew);
    	objectOff.setBoxId(LogData.getInstance().getAppID(context));
    	objectOff.setOnTime("");
    	objectOff.setOffTime(getTimeStamp());
    	DataCacheManager.getInstance(context).saveOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_BOX_TABLE, objectOff);
    
	}
	/**
	 * This method checks if networkis connected or not
	 * 
	 * @param context
	 * @return true is connected to internet else false
	 */
	public static boolean checkNetwrk(Context context) {
		boolean nwFlag = false;
		try {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				nwFlag = true;
			}
		} catch (Exception e) {
//			 e.printStackTrace();
		}

		return nwFlag;
	}
	public static String getLayoutStringFromFile(String fileName){

		String PATH = Environment.getExternalStorageDirectory()
				+ AppState.DISPLAY_FOLDER;
		File file = new File(PATH, fileName);

		//Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
			}
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
				e.printStackTrace();
			}
		
		String layoutString = "";
		if(text != null ){
			layoutString = new String(text);
		}
		if(!layoutString.contains("layout")){
			layoutString = "";
		}
		return layoutString;
	}
	static String getbaseUrl() {
		return "http://54.251.108.112:9000";
	}

	public static String getHearbeatUrl() {
		return getbaseUrl() + "/logApp/heartBeat";

	}

	public static String getActiveLayoutLogtUrl() {
		return getbaseUrl() + "/logApp/activeLayoutLog";

	}

	public static String getAppStatusLogUrl() {
		return getbaseUrl() + "/logApp/appStatusLog";

	}

	public static String getAppVersionLogUrl() {
		return getbaseUrl() + "/logApp/appVersionLog";

	}

	public static String getLocationLogUrl() {
		return getbaseUrl() + "/logApp/locationLog";

	}

	public static String getNewtorkLogUrl() {
		return getbaseUrl() + "/logApp/newtorkLog";

	}

	public static String getScreenResolutionLogUrl() {
		return getbaseUrl() + "/logApp/screenResolutionLog";

	}

}
