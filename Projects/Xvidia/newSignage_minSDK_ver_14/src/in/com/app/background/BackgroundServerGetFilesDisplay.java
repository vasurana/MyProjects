package in.com.app.background;

import in.com.app.AppMain;
import in.com.app.ClientConnectionConfig;
import in.com.app.IDisplayLayout;
import in.com.app.data.SignageData;
import in.com.app.domain.DisplayLayoutFiles;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.wsdl.XMDS;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This class is wrapper for a asyncTask that downloads list of files used in the current layout from server in background.
 * @author Ravi@Xvidia
 * @since version 1.0
 *
 */
public class BackgroundServerGetFilesDisplay extends AsyncTask<String, Void, DisplayLayoutFiles> implements IDisplayLayout{


	
	Context context = null;
	Activity activity = null;
	
	BackgroundServerGetFilesDisplay(Context ctx, Activity act){
		context = ctx;
		activity = act;
	}
	
	
	// TODO:String args should not be hardcoded but send as argument to this
	// function
	@Override
	protected DisplayLayoutFiles doInBackground(String... arg0) {
		
		// 1) Get the XML List of Files

		// 2) Store the XML in the localcache.

		// TODO::Should we always download it .(Advantage:: Always in Sync
		// Disadvantge::Network , Slow App )

		// 3) Convert the XML into a file XiboFiles and return it !!

		// 4) Post Execution:: Display the List of files receieved.

		XMDS xmds = new XMDS(ClientConnectionConfig._SERVERURL);
		String strReqFiles = "";
		if(ClientConnectionConfig._UNIQUE_SERVER_KEY!=null && ClientConnectionConfig._HARDWAREKEY!=null && ClientConnectionConfig._VERSION !=null){
			strReqFiles = xmds.RequiredFiles(ClientConnectionConfig._UNIQUE_SERVER_KEY, ClientConnectionConfig._HARDWAREKEY, ClientConnectionConfig._VERSION);

			/////IF 1st REQUEST ASSISGN VALUE FROM DB///////////////////////////////////////////////
//			if( (ClientConnectionConfig.LAST_REQUEST_DATA).trim().equals("")){
//				ClientConnectionConfig.LAST_REQUEST_DATA = DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_LAST_DISPLAY_XML);
//				if(!ClientConnectionConfig.LAST_REQUEST_DATA.equals("")){
//					StateMachineDisplay.gi(context, mContext).fillResourceMapIfOffline(ClientConnectionConfig.LAST_REQUEST_DATA);
//				}
//			} 
			//////////////////////////////////////////////////////////////////
			
				ClientConnectionConfig.LAST_REQUEST_DATA = strReqFiles;	
				DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_NEW_DISPLAY_XML, strReqFiles);
				DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_FILE_COMPLETE, FLAG_TRUE);
			
		}else{
			return null;
		}
		Serializer serializer = new Persister();
		DisplayLayoutFiles lstFiles = null;

		try {
			lstFiles = serializer.read(DisplayLayoutFiles.class, strReqFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}


		SignageData.getInstance().setLayoutFiles(lstFiles);
		return lstFiles;
	}

	@Override
	protected void onPostExecute(DisplayLayoutFiles lstFiles) {
		boolean nextStepFlag = false;
		if(lstFiles!=null){
			StateMachineDisplay.gi(context, activity).receivedXiboFiles = lstFiles;
			nextStepFlag = true;
		}
		StateMachineDisplay.gi(context, activity).initProcess(nextStepFlag, StateMachineDisplay.GETINDIVIDUALFILE);
		
	}
}

