package in.com.app.background;

import in.com.app.AppMain;
import in.com.app.ClientConnectionConfig;
import in.com.app.IDisplayLayout;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.wsdl.XMDS;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This class is wrapper for a asyncTask that checks if STB is registered on server.
 * @author Ravi@Xvidia
 * @since version 1.0
 *
 */
public class BackgroundServerConnectionManagerDisplay extends
AsyncTask<String, Void, String> implements IDisplayLayout {

	
	
	Context context = null;
	Activity activity = null;
	
	BackgroundServerConnectionManagerDisplay(Context ctx, Activity act){
		context = ctx;
		activity = act;
	}

	
	public String TAG = getClass().getCanonicalName();
	
	
	
	@Override
	protected String doInBackground(String... params) {
		XMDS xmds = new XMDS(ClientConnectionConfig._SERVERURL);

		String str = xmds.RegisterDisplay(ClientConnectionConfig._UNIQUE_SERVER_KEY, ClientConnectionConfig._HARDWAREKEY,
				ClientConnectionConfig._DISPLAYNAME, ClientConnectionConfig._VERSION);

		return str;
	}

	@Override
	protected void onPostExecute(String result) {			
		Log.d(TAG, "Got response " + result);
		boolean nextStepFlag = false;
		if (result!=null && result.contains("Display is active and ready to start")) {
			DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_REG_COMPLETE, FLAG_TRUE);
			nextStepFlag = true;
		}
		try{
			
				StateMachineDisplay.gi(context, activity).initProcess(nextStepFlag, StateMachineDisplay.GETSCHEDULE);
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}