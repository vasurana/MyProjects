package in.com.app.updater;


import in.com.app.AppMain;
import in.com.app.data.LogData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
 
/**
 * This class maintains information about the Network Status
 * @author Ravi@Xvidia
 *@since version 1.0
 */
public class NetworkUtil {
     
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_ETHERNET = 3;
    public static int TYPE_NOT_CONNECTED = 0;
//    public static String TYPE_INFO;
     
     /**
      * This method sets the type of network connected
      * @param context ApplicationContext
      * @return pre defined integer value for the network type connected
      */
    public static int getConnectivityStatus(Context context) {
    	try{
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
 
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork && activeNetwork.isConnectedOrConnecting()) {
//        	TYPE_INFO = activeNetwork.getSubtypeName();
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
             
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
            if(activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET)
                return TYPE_ETHERNET;
        } 
    	}catch(Exception e){
    		
    	}
        return TYPE_NOT_CONNECTED;
    }
    
    /**
     * This method checks if networkis connected or not
     * @param context
     * @return true is connected to internet else false
     */
    public static boolean checkNetwrk(Context context){
		boolean nwFlag = false;
		try{		
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				nwFlag = true;
			}
			if(!nwFlag){
				nwFlag = LogData.getInstance().getInternetConnection(AppMain.getAppMainContext());
			}
		}catch (Exception e) {
			//e.printStackTrace();
		}

		return nwFlag;
	}
    
    /**
     * This method get the string for the type of network connected
      * @param context ApplicationContext
     * @return type of the network
     */
    public static String getConnectivityStatusString(Context context) {
    	
        String status = "Not connected to Internet";
        try{
	        int conn = NetworkUtil.getConnectivityStatus(context);
	        if (conn == NetworkUtil.TYPE_WIFI) {
	            status = "Wifi connected";//+getWifiName(context);
	        } else if (conn == NetworkUtil.TYPE_MOBILE) {
	            status = "Mobile data "+getMobileDataName(context);
	        } else if (conn == NetworkUtil.TYPE_ETHERNET) {
	            status = "Ethernet ";
	        }else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
	            status = "Not connected to Internet";
	        }
		}catch(Exception e){
			
		}
        return status;
    }
//    public static String getWifiName(Context context) {
//    	String ssid = "none";
//    	try{
//	    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//	    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//	    	if (wifiInfo != null){//.getDetailedStateOf(wifiInfo.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED) {
//	    		ssid = wifiInfo.getSSID();
//	    	}
//    	}catch(Exception e){
//    		
//    	}
//    	return ssid;
//    }
    public static String getMobileDataName(Context context) {
    	String carrierName = "none";
    	try{
	    	TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	        carrierName = manager.getNetworkOperatorName();
    	}catch(Exception e){
    		
    	}
    	return carrierName;
    }
    
}