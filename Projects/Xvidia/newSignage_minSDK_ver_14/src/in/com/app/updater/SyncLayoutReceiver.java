package in.com.app.updater;
import in.com.app.AppMain;
import in.com.app.SignageDisplay;
import in.com.app.Splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * This class extends {@link BroadcastReceiver}
 * @author Ravi@xvidia
 * @since Version 1.0
 */
public class SyncLayoutReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	if(!SignageDisplay.alarmRefresh){
    		SignageDisplay.alarmRefresh = true;
	    	Intent myIntent = new Intent(context, AppMain.class);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(myIntent);
    	}
//  
    }
}
