package in.com.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.com.app.data.LogData;
import in.com.app.model.OnOffTimeData;
import in.com.app.storage.caching.sqlight.MySqliteHelper;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.updater.LogUtility;
import in.com.app.updater.SyncLayoutReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
/**
 * This class extends {@link BroadcastReceiver} to start app as soon as boot is completed
 * @author Ravi@xvidia
 * @since Version 1.0
 */
public class BootReceiver extends BroadcastReceiver
{
	OnOffTimeData object = null;
	@Override
	public void onReceive(Context context, Intent intent) {
final Context ctx = context;
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				Date now = new Date();
				String time = formatter.format(now);
				LogData.getInstance().setOntimeId(ctx, time);
				String id = LogData.getInstance().getAppID(ctx)+"_"+"BOX"+"_"+now.getTime();
		    	object = new OnOffTimeData();
		    	object.setId(id);
		    	object.setBoxId(LogData.getInstance().getAppID(ctx));
		    	object.setOnTime(time);
		    	object.setSavingTime(time);
		    	object.setOffTime("");
//		    	Log.e("BOOTRECIEVR",time);
//		    	object.setBoxId(LogData.getInstance().getAppID(context));
		    	DataCacheManager.getInstance(ctx).saveOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_BOX_TABLE, object);
				setSyncLayoutAlarm(ctx);
				Intent myIntent = new Intent(ctx, Splash.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ctx.startActivity(myIntent);
			}
		}, 2000);
		
 
	}
	private void setSyncLayoutAlarm(Context context) {
		Calendar updateTime = Calendar.getInstance();
		Intent downloader = new Intent(context, SyncLayoutReceiver.class);
		PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
				0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(),
				60000, recurringDownload);
//		alarms.setExact(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(), recurringDownload);

	}
}