package in.com.app.updater;

import in.com.app.IDisplayLayout;
import in.com.app.data.LogData;
import in.com.app.model.OnOffTimeData;
import in.com.app.storage.caching.sqlight.MySqliteHelper;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class extends {@link BroadcastReceiver} to update shutdown and boot time
 * @author Ravi@xvidia
 * @since Version 1.0
 */
public class ShutdownReceiver extends BroadcastReceiver {
	OnOffTimeData object ;
    @Override
    public void onReceive(Context context, Intent intent) {
    	String action = intent.getAction();
//    	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date now = new Date();
		
		if(action.equals(Intent.ACTION_BOOT_COMPLETED))
        {
//			String  data = formatter.format(now);
//			DataCacheManager.getInstance().saveSettingData(IDisplayLayout._KEY_XVIDIA_STATE_POWER_ON_TIME, data);
	    }else if(action.equals(Intent.ACTION_SHUTDOWN))
        {
//        	String  data = LogUtility.getTimeStamp();//formatter.format(now);
//        	String id = LogData.getInstance().getAppID(context)+"_"+"BOX"+"_"+now.getTime();
//        	object = new OnOffTimeData();
//        	object.setId(id);
//        	object.setBoxId(LogData.getInstance().getAppID(context));
//        	object.setOnTime("");
//        	object.setOffTime(data);
//        	DataCacheManager.getInstance(context).saveOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_BOX_TABLE, object);
//    		DataCacheManager.getInstance(context).saveSettingData(IDisplayLayout._KEY_XVIDIA_STATE_POWER_OFF_TIME, data);
        }else if(action.equals(Intent.ACTION_POWER_CONNECTED))
        {
        	String  data = LogUtility.getTimeStamp();
//			String  data =  "Time: "+formatter.format(now);
//			DataCacheManager.getInstance().saveSettingData(IDisplayLayout._KEY_XVIDIA_STATE_POWER_ON_TIME, data);
	    }else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {   
//	    	String  data = formatter.format(now);
//	    	String  data = LogUtility.getTimeStamp();
//	    	String id = LogData.getInstance().getAppID(context)+"_"+"BOX"+"_"+now.getTime();
//	    	object = new OnOffTimeData();
//	    	object.setId(id);
//	    	object.setBoxId(LogData.getInstance().getAppID(context));
//	    	object.setOnTime("");
//	    	object.setOffTime(data);
//	    	DataCacheManager.getInstance(context).saveOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_BOX_TABLE, object);
        }
    }

}