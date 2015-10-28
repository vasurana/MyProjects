package in.com.app.data;

import in.com.app.IDisplayLayout;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * This class has setters and getters of HearBeat data
 * @author Ravi@xvidia
 *@since version 1.0
 */
public class LogData {
	
	public static final int TIME_OUT = 20000;
	public static final String MY_PREFERENCES = "LOGPREFERENCE";
//	public static final String TAG_NETWORK = "networkStatus";
	public static final String TAG_TIME = "timeStamp";
	public static final String TAG_APPLICATION_STATUS = "appStatus";
	public static final String TAG_NETWORKTYPE = "networkType";
	public static final String TAG_ACTIVE_LAYOUT = "activeLayout";
	public static final String TAG_IPADDRESS = "ipAddress";
	public static final String TAG_MAC_ADDRESS = "macAddress";
	public static final String TAG_LOCATION = "location";
	public static final String TAG_APPVERSION = "appVersion";
	public static final String TAG_APPID = "stbId";
	public static final String TAG_DisplayName = "displayName";
	public static final String TAG_ADDRESS = "storeAddress";
	public static final String TAG_ASSETID = "assetId";
//	public static final String TAG_GEOPIP = "location_geopip";
	public static final String TAG_SCREEN_RESOLUTION = "screenResolution";
	public static final String TAG_PREV_LAYOUT = "prevActiveLayout";
	public static final String TAG_PREV_APP_STATUS = "prevAppStatus";
	public static final String TAG_PREV_LOCATION = "prevLocation";
	public static final String TAG_PREV_VERSION = "prevAppVersions";
	public static final String TAG_PREV_IPADDRESS = "prevIpAddress";
	public static final String TAG_PREV_MAC_ADDRESS = "prevMacAddress";
	public static final String TAG_PREV_NETWORK_TYPE = "prevnetworkType";
	public static final String TAG_PREV_SCREEN_RESOLUTION = "prevScreenResolution";
	public static final String TAG_USB = "usbStatus";
	public static final String TAG_CURRENT_FILES_XML = "currentDisplayXml";
	public static final String TAG_NEW_FILES_XML = "newDisplayFilesXml";
	public static final String TAG_CURRENT_LAYOUT_XML = "currentLayoutXml";
	public static final String TAG_ONOFF_BOX = "BOX";
	public static final String TAG_ONOFF_APP = "APP";
	public static final String TAG_ONOFF_SCREEN= "SCREEN";
	public static final String TAG_DOWNLOAD_PENDING= "downloadPending";
	public static final String TAG_OFFTIME_ID= "offTimeId";
	public static final String TAG_ONTIME_ID= "onTimeId";
	public static final String TAG_ORIENTATION= "orientation";
	public static final String TAG_INTERNET= "internet";
	public static final String TAG_OFFLINE_TIMEOUT= "timeOut";
//	String ipAddress, macAddress, currenLayout,location, neworkType, timeStamp, appStatus;

	public final static String TAG_REQUEST_SEND = "SEND_REQUEST";
	public final static String APP_ON = "APP_ON";
	public final static String STB_ON = "APP_OFF_STB_ON";
	public static final String STR_UNKNOWN = "Unknown";
	private static  LogData instance = null;
	 SharedPreferences sharedpreferences;

	public static  LogData getInstance() {
		if (instance == null) {
			instance = new LogData();
		}
		return instance;
	}

	private String getValidatedString(String valStr) {
		if (valStr == null) {
			return STR_UNKNOWN;
		} else {
			return valStr.trim();
		}
	}
	public void removeAllPreferenceData(Context ctx){
		SharedPreferences settings = ctx.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
		settings.edit().clear().commit();
	}
	public void removePreferenceData(Context ctx, String preferenceName){
		SharedPreferences settings = ctx.getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
		settings.edit().remove(preferenceName).commit();
	}
	private  SharedPreferences getSharedPreference(Context ctx) {
		sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCES,
				Context.MODE_PRIVATE);
		return sharedpreferences;
	}
	public  String getAssetId(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_ASSETID, retVal);
			// retVal = readTextFromFile(FILE_NAME_ASSET);
		} catch (Exception e) {
		}
		return getValidatedString(retVal);
	}

	public  String getAddress(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_ADDRESS, retVal);
			// retVal = address;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}

	public  String getDisplayname(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx)
					.getString(TAG_DisplayName, retVal);
			// retVal = name;

		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}

	public  String getScreenResolution(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_SCREEN_RESOLUTION,
					retVal);
			// retVal = resolution;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}

	public  String getAppStatus(Context ctx) {
		String retVal = STB_ON;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_APPLICATION_STATUS,
					retVal);
			// String appIdStr = DataCacheManager.getInstance().readSettingData(
			// IDisplayLayout._KEY_XVIDIA_APP_STATE);
			// if (appIdStr.equals("")) {
			// retVal = HeartBeatData.STB_ON;
			// } else {
			// retVal = appIdStr;
			// }
		} catch (Exception e) {
			retVal = STB_ON;
		}
		return retVal;
	}

	public  String getAppVersion(Context ctx) {
		String retVal = "Could not be determined";

		try {
			retVal = getSharedPreference(ctx).getString(TAG_APPVERSION, retVal);
			// String verClientStr = DataCacheManager.getInstance()
			// .readSettingData(IDisplayLayout._KEY_XVIDIA_CLIENT_VERSION);
			//
			// if (!verClientStr.equals("")) {
			// retVal = verClientStr;
			// }
		} catch (Exception e) {
			retVal = "Could not be determined";
		}
		return retVal;
	}

	public  String getAppID(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_APPID, retVal);
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getIpAddress(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_IPADDRESS, retVal);
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getMacAddress(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_MAC_ADDRESS, retVal);
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	
	public  String getLocation(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_LOCATION, retVal);
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getNetworkType(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_NETWORKTYPE, retVal);
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	
	//TODO discuss the logic
	public  String getPrevLayout(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_PREV_LAYOUT, retVal);
//			if(!prevLayout.equalsIgnoreCase(getCurrentLayout())){
//				
//			}else{
//				
//			}
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getActiveLayout(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_ACTIVE_LAYOUT, retVal);
//			if(!prevLayout.equalsIgnoreCase(getCurrentLayout())){
//				
//			}else{
//				
//			}
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	//TODO discuss the logic
	public  String getPrevAppStatus(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_PREV_APP_STATUS, retVal);
//			if(!prevLayout.equalsIgnoreCase(getCurrentLayout())){
//				
//			}else{
//				
//			}
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}

	//TODO discuss the logic
	public  String getPrevLocation(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_PREV_LOCATION, retVal);
			
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	
	//TODO discuss the logic
	public  String getPrevAppVersion(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_PREV_VERSION, retVal);
			
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	
	public  String getPrevNetworkType(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_PREV_NETWORK_TYPE, retVal);
			
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getPrevIpAddress(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_PREV_IPADDRESS, retVal);
//			if(!prevLayout.equalsIgnoreCase(getCurrentLayout())){
//				
//			}else{
//				
//			}
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getPrevMacAddress(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_PREV_MAC_ADDRESS, retVal);
//			if(!prevLayout.equalsIgnoreCase(getCurrentLayout())){
//				
//			}else{
//				
//			}
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getPrevScreenResolution(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_PREV_SCREEN_RESOLUTION, retVal);
//			if(!prevLayout.equalsIgnoreCase(getCurrentLayout())){
//				
//			}else{
//				
//			}
			// retVal = stbID;
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getCurrentLayoutXml(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_CURRENT_LAYOUT_XML, retVal);

		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getCurrentDisplayFiles(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_CURRENT_FILES_XML, retVal);
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  String getNewDisplayFiles(Context ctx) {
		String retVal = STR_UNKNOWN;
		try {
			retVal = getSharedPreference(ctx).getString(TAG_NEW_FILES_XML, retVal);
		} catch (Exception e) {
			retVal = STR_UNKNOWN;
		}
		return getValidatedString(retVal);
	}
	public  boolean getRequestSend(Context ctx) {
		boolean retVal = false;
		try {
			retVal = getSharedPreference(ctx).getBoolean(TAG_REQUEST_SEND, true);
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	public  boolean setRequestSend(Context ctx, boolean prefVal) {
		boolean retVal = false;
			try {
					Editor editor = getSharedPreference(ctx).edit();
					editor.putBoolean(TAG_REQUEST_SEND, prefVal);
					editor.commit();
					retVal = true;
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
			return retVal;
	}
	public  boolean getDownloadPending(Context ctx) {
		boolean retVal = false;
		try {
			retVal = getSharedPreference(ctx).getBoolean(TAG_DOWNLOAD_PENDING, true);
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	public  boolean setDownloadPending(Context ctx, boolean prefVal) {
		boolean retVal = false;
			try {
					Editor editor = getSharedPreference(ctx).edit();
					editor.putBoolean(TAG_DOWNLOAD_PENDING, prefVal);
					editor.commit();
					retVal = true;
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
			return retVal;
	}
	public  int getOrientation(Context ctx) {
		int retVal = IDisplayLayout.ORIENTATION_LANSCAPE;
		try {
			retVal = getSharedPreference(ctx).getInt(TAG_ORIENTATION, IDisplayLayout.ORIENTATION_LANSCAPE);
		} catch (Exception e) {
					}
		return retVal;
	}
	
	public  boolean setOrientation(Context ctx, int prefVal) {
		boolean retVal = false;
			try {
					Editor editor = getSharedPreference(ctx).edit();
					editor.putInt(TAG_ORIENTATION, prefVal);
					editor.commit();
					retVal = true;
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
			return retVal;
	}
	public  String getOntimeId(Context ctx) {
		String retVal = "";
		try {
			retVal = getSharedPreference(ctx).getString(TAG_ONTIME_ID, "");
		} catch (Exception e) {
			retVal = "";
		}
		return retVal;
	}
	
	public  boolean setOntimeId(Context ctx, String prefVal) {
		boolean retVal = false;
			try {
					Editor editor = getSharedPreference(ctx).edit();
					editor.putString(TAG_ONTIME_ID, prefVal);
					editor.commit();
					retVal = true;
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
			return retVal;
	}

	public  long getOfflineSinceTimeStamp(Context ctx) {
		long retVal = 0;
		try {
			retVal = getSharedPreference(ctx).getLong(TAG_OFFLINE_TIMEOUT, 0);
		} catch (Exception e) {
			retVal = 0;
		}
		return retVal;
	}

	public  boolean setOfflineSinceTimeStamp(Context ctx, long prefVal) {
		boolean retVal = false;
		try {
			Editor editor = getSharedPreference(ctx).edit();
			editor.putLong(TAG_OFFLINE_TIMEOUT, prefVal);
			editor.commit();
			retVal = true;
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
		return retVal;
	}
	public  String getOfftimeId(Context ctx) {
		String retVal = "";
		try {
			retVal = getSharedPreference(ctx).getString(TAG_OFFTIME_ID, "");
		} catch (Exception e) {
			retVal = "";
		}
		return retVal;
	}
	
	public  boolean setOfftimeId(Context ctx, String prefVal) {
		boolean retVal = false;
			try {
					Editor editor = getSharedPreference(ctx).edit();
					editor.putString(TAG_OFFTIME_ID, prefVal);
					editor.commit();
					retVal = true;
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
			return retVal;
	}
	
	public  boolean getInternetConnection(Context ctx) {
		boolean retVal = false;
		try {
			retVal = getSharedPreference(ctx).getBoolean(TAG_INTERNET, false);
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}
	
	public  boolean setInternetConnection(Context ctx, boolean prefVal) {
		boolean retVal = false;
			try {
					Editor editor = getSharedPreference(ctx).edit();
					editor.putBoolean(TAG_INTERNET, prefVal);
					editor.commit();
					retVal = true;
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
			return retVal;
	}
	public  boolean setLocation(Context ctx, String prefVal) {
		boolean retVal = false;
			try {
				String previousVal = getLocation(ctx);
				if(!previousVal.equalsIgnoreCase(prefVal)){
					Editor editor = getSharedPreference(ctx).edit();
					editor.putString(TAG_LOCATION, prefVal);
					editor.commit();
					setPrevLocation(ctx, previousVal);
					retVal = true;
				}
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
			return retVal;
	}
	
	public  boolean setIpAddress(Context ctx, String prefVal) {		
		boolean retVal = false;
		try {
			String previousVal =  getIpAddress(ctx);
			if(!previousVal.equalsIgnoreCase(prefVal)){
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_IPADDRESS, prefVal);
				editor.commit();
				setPrevIpAddress(ctx, previousVal);
				retVal = true;
			}
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
		return retVal;
	}
	
	public  boolean setMacAddress(Context ctx, String prefVal) {
		
		boolean retVal = false;
		try {
			String previousVal =  getMacAddress(ctx);
			if(!previousVal.equalsIgnoreCase(prefVal)){
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_MAC_ADDRESS, prefVal);
				editor.commit();
				setPrevMacAddress(ctx, previousVal);
				retVal = true;
			}
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
		return retVal;
	}
	
	public  boolean setNetworkType(Context ctx, String prefVal) {
		boolean retVal = false;
		try {
			String previousVal =  getNetworkType(ctx);
			if(!previousVal.equalsIgnoreCase(prefVal)){
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_NETWORKTYPE, prefVal);
				editor.commit();
				setPrevNetworkType(ctx, previousVal);
				retVal = true;
			}
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
		return retVal;
	}
	
	public  boolean setActiveLayout(Context ctx, String prefVal) {
		boolean retVal = false;
		try {
			String previousVal =  getActiveLayout(ctx);
			if(!previousVal.equalsIgnoreCase(prefVal)){
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_ACTIVE_LAYOUT, prefVal);
				editor.commit();
				setPrevLayout(ctx, previousVal);
				retVal = true;
			}
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
		return retVal;
	}
	public  void setAssetID(Context ctx, String prefVal) {
		try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_ASSETID, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
	}

	public  void setAddress(Context ctx, String prefVal) {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_ADDRESS, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
	}

	public  void setDisplayname(Context ctx, String prefVal) {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_DisplayName, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
	}
	public  void setCurrentLayoutXml(Context ctx, String prefVal) {
		try {
			Editor editor = getSharedPreference(ctx).edit();
			editor.putString(TAG_CURRENT_LAYOUT_XML, prefVal);
			editor.commit();
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
}
	public  void setCurrentDisplayFilesXml(Context ctx, String prefVal) {
		try {
			Editor editor = getSharedPreference(ctx).edit();
			editor.putString(TAG_CURRENT_FILES_XML, prefVal);
			editor.commit();
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
	}
	public  void setNewDisplayFilesXml(Context ctx, String prefVal) {
		try {
			Editor editor = getSharedPreference(ctx).edit();
			editor.putString(TAG_NEW_FILES_XML, prefVal);
			editor.commit();
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
	}
	public  boolean setResolution(Context ctx, String prefVal) {
		boolean retVal = false;
		try {
			String previousVal =  getScreenResolution(ctx);
			if(!previousVal.equalsIgnoreCase(prefVal)){
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_SCREEN_RESOLUTION, prefVal);
				editor.commit();
				setPrevScreenResolution(ctx, previousVal);
				retVal = true;
			}
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
		return retVal;
		
	}

	public  boolean setAppStatus(Context ctx, String prefVal) {
		boolean retVal = false;
		try {
			String previousVal =  getAppStatus(ctx);
			if(!previousVal.equalsIgnoreCase(prefVal)){
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_APPLICATION_STATUS, prefVal);
				editor.commit();
				setPrevAppStatus(ctx, previousVal);
				retVal = true;
			}
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
		return retVal;
		
	}

	public  boolean setAppVersion(Context ctx, String prefVal) {
		boolean retVal = false;
		try {
			String previousVal =  getAppVersion(ctx);
			if(!previousVal.equalsIgnoreCase(prefVal)){
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_APPVERSION, prefVal);
				editor.commit();
				setPrevAppVersion(ctx, previousVal);
				retVal = true;
			}
		} catch (Exception e) {
			// retVal = STR_UNKNOWN;
		}
		return retVal;
		
	}

	public  void setAppID(Context ctx, String prefVal) {
		try {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_APPID, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
		} catch (Exception e) {
		}
	}
	
	public  void setPrevScreenResolution(Context ctx, String prefVal) {
		try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_SCREEN_RESOLUTION, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
		}
	}
	public  void setPrevAppStatus(Context ctx, String prefVal) {
		try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_APP_STATUS, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
		}
	}
	public  void setPrevActiveLayout(Context ctx, String prefVal) {
		try {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_LAYOUT, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
		} catch (Exception e) {
		}
	}
	public  void setPrevLocation(Context ctx, String prefVal) {
		try {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_LOCATION, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
		} catch (Exception e) {
		}
	}
	public  void setPrevAppVersion(Context ctx, String prefVal) {
		try {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_VERSION, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
		} catch (Exception e) {
		}
	}
	
	public  void setPrevNetworkType(Context ctx, String prefVal) {
		try {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_NETWORK_TYPE, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
		} catch (Exception e) {
		}
	}
	
	public  void setPrevMacAddress(Context ctx, String prefVal) {
		try {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_MAC_ADDRESS, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
		} catch (Exception e) {
		}
	}
	
	public  void setPrevIpAddress(Context ctx, String prefVal) {
		try {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_IPADDRESS, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
		} catch (Exception e) {
		}
	}
	public  void setPrevLayout(Context ctx, String prefVal) {
		try {
			try {
				Editor editor = getSharedPreference(ctx).edit();
				editor.putString(TAG_PREV_LAYOUT, prefVal);
				editor.commit();
			} catch (Exception e) {
				// retVal = STR_UNKNOWN;
			}
		} catch (Exception e) {
		}
	}
}
