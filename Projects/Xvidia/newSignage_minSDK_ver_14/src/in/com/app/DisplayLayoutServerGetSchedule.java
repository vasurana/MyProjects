package in.com.app;

import java.util.ArrayList;
import java.util.Date;

import in.com.app.data.SignageData;
import in.com.app.domain.DisplayLayoutSchedule;
import in.com.app.domain.DisplayLayoutScheduleDefaultFile;
import in.com.app.domain.DisplayScheduleLayout;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.utility.Utility;
import in.com.app.wsdl.XMDS;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.os.AsyncTask;
import android.util.Log;
/**
 * This class is wrapper for a asyncTask that downloads schedule of the layout to be displayed.
 * @author Ravi@Xvidia
 * @since version 1.0
 *
 */
public class DisplayLayoutServerGetSchedule extends
AsyncTask<String, Void, DisplayLayoutSchedule> implements IDisplayLayout{
	
	
AppMain appmainInstance = null;
	
DisplayLayoutServerGetSchedule(AppMain appmain){
		appmainInstance = appmain;
	}

	// TODO:String args should not be hardcoded but send as argument to this
	// function
	@Override
	protected DisplayLayoutSchedule doInBackground(String... args) {

		XMDS xmds = new XMDS(ClientConnectionConfig._SERVERURL);
		String strReqFiles = "";
		if(ClientConnectionConfig._UNIQUE_SERVER_KEY!=null && ClientConnectionConfig._HARDWAREKEY!=null){
			strReqFiles = xmds.Schedule(ClientConnectionConfig._UNIQUE_SERVER_KEY, ClientConnectionConfig._HARDWAREKEY, ClientConnectionConfig._VERSION);
		}else{
			return null;
		}
		Log.d(appmainInstance.TAG, " Schedule::: " + strReqFiles);

		Serializer serializer = new Persister();
		DisplayLayoutSchedule displayLayoutSchedule=null;

		try {
			if(strReqFiles!=null){
				displayLayoutSchedule = serializer.read(DisplayLayoutSchedule.class, strReqFiles);
				DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_SCHEDULE_XML, strReqFiles);
				DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_SCHEDULE_COMPLETE, FLAG_TRUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(displayLayoutSchedule!=null){
			Log.d(appmainInstance.TAG, "CountofGotFiles " + displayLayoutSchedule.toString());
			SignageData.getInstance().setLayoutSchedule(displayLayoutSchedule);
			fillDataOfSchedule();
		}
		return displayLayoutSchedule;
	}

	@Override
	protected void onPostExecute(DisplayLayoutSchedule result) {
		boolean nextStepFlag = true;
		if (result == null) {
			nextStepFlag = false;
		} else {
			//textViewStatus.setText(result.toString());
		}
		//stopProgress();
		StateMachine.gi(appmainInstance).initProcess(nextStepFlag, StateMachine.GETFILE);	
	}


	void fillDataOfSchedule(){
		DisplayLayoutSchedule displayLayoutSchedule = SignageData.getInstance().getLayoutSchedule();
		if(displayLayoutSchedule !=null){
			
			DisplayLayoutScheduleDefaultFile displayLayoutScheduleDefault = displayLayoutSchedule.getScheduleDefault();
			SignageData.getInstance().setDefaultLayout(displayLayoutScheduleDefault.getFile());
			SignageData.getInstance().setCurrentLayout(Utility.getCurrentFile(displayLayoutSchedule.getLayout()));

			DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_SCHEDULE_CURRENTFILE, SignageData.getInstance().getCurrentLayout());
		}
	}
	



}