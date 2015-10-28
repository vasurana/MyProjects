package in.com.app;

import java.util.ArrayList;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import in.com.app.data.LogData;
import in.com.app.data.SignageData;
import in.com.app.domain.DisplayLayoutFile;
import in.com.app.domain.DisplayLayoutFiles;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.utility.Utility;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

/**
 * This is Class that changes state of processes
 * @author Ravi@xvidia
 * @since version 1.0
 */
public class StateMachine implements IDisplayLayout{

	final static int REGISTER = 0;
	final static int GETFILE = 1;
	final static int GETINDIVIDUALFILE = 2;
	final static int GETSCHEDULE = 3;
	final static int SHOWDISPLAY = 4;
	final static int OFFLINE = 5;
	final static int CONNECTION_ERROR = 6;
	static boolean displayScreen = true;
//	final static int STATEMACHINE_CALL_APPMAIN = 0;
//	final static int STATEMACHINE_CALL_DISPLAYSIGNAGE = 1;
//	public DisplayLayoutFiles receivedXiboFiles = null;
	public ArrayList<DisplayLayoutFile> receivedDownloadFiles = null;
	public ArrayList<DisplayLayoutFile> receivedFilesDownloadLater = null;
	AppMain appmainInstance = null;
	static private Runnable refreshRunnable;
	static Handler refreshHandler = null;
	final int _TIME_TO_REHIT_SERVER =15*1000;//5*60*1000;
	static StateMachine instance = null;
	
	public static StateMachine gi(AppMain appmain){
		if(instance == null){
			instance =  new StateMachine(appmain);
		}
		return instance;
	}
	
	
	private StateMachine(){
		
	}
	
	private StateMachine(AppMain appmain){
		appmainInstance = appmain;
	}
	public void updateStatus( final int count, final int totalCount){
		try{
			String msg;//= appmainInstance.getResources().getString(R.string.process_text);
			msg = "Files downloading "+count+" / "+totalCount;
			AppMain.textViewInfo.setText(msg);
			AppMain.textViewInfo.setVisibility(View.VISIBLE);
//			AppMain.progressBar.setMax(totalCount);
//			AppMain.progressBar.setProgress(count);
//			AppMain.progressBar.setVisibility(View.VISIBLE);
			Toast t = Toast.makeText(appmainInstance, msg, Toast.LENGTH_SHORT);
			t.show();
		}catch(Exception e){
			
		}
	}
	/**
	 * This method switches between process
	 * @param nextRequest boolean value to process next Request
	 * @param processId type of process to be called
	 */
	public void initProcess(final boolean nextRequest, final int processId){
//		appmainInstance.stopProgress();
		
		if(!displayingContentInOffileMode && processId == OFFLINE){
			String xml = LogData.getInstance().getCurrentLayoutXml(AppMain.getAppMainContext());
			
			if(xml!=null && !xml.equalsIgnoreCase("") && !xml.equalsIgnoreCase(LogData.STR_UNKNOWN)){
				String  currentFile = DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_SCHEDULE_CURRENTFILE);
				SignageData.getInstance().setDefaultLayout(currentFile);
				SignageData.getInstance().setCurrentLayout(currentFile);
				displayContentInOfflineMode();
			}else{
				refreshScreen();
				}
		}else if(!displayingContentInOffileMode ){
			if(!checkNetwrk()){
				String xml = LogData.getInstance().getCurrentLayoutXml(AppMain.getAppMainContext());
				
				if(xml!=null && !xml.equalsIgnoreCase("") && !xml.equalsIgnoreCase(LogData.STR_UNKNOWN)){
					displayContentInOfflineMode();
				}else{
					refreshScreen();
				}
			}
		}

		if(!nextRequest && processId == GETSCHEDULE){
			AppMain.textViewInfo.setText(R.string.register_complete);
			AppMain.textViewInfo.setTextColor(appmainInstance.getResources().getColor(R.color.buttonBackground));
			AppMain.textViewInfo.setVisibility(View.VISIBLE);
			new ServerConnectionManager(appmainInstance).execute();
			return;
		}else if(!nextRequest&& processId != OFFLINE){
			refreshScreen();
			return;
		}
		if(checkNetwrk()|| processId == SHOWDISPLAY){
			AppMain.textViewInfo.setTextColor(appmainInstance.getResources().getColor(R.color.buttonBackground));
			AppMain.textViewInfo.setText(R.string.process_text);
		switch (processId) {
			case REGISTER:
				AppMain.textViewInfo.setVisibility(View.VISIBLE);
				Toast.makeText(appmainInstance, "registering", Toast.LENGTH_SHORT).show();
				new ServerConnectionManager(appmainInstance).execute();
				break;
			case GETSCHEDULE:
				AppMain.textViewInfo.setVisibility(View.VISIBLE);
				Toast.makeText(appmainInstance, "downloading schedule", Toast.LENGTH_SHORT).show();
				new DisplayLayoutServerGetSchedule(appmainInstance).execute("");
				break;
			case GETFILE:
				AppMain.textViewInfo.setVisibility(View.GONE);
				new ServerGetFiles(appmainInstance).execute();
				break;
			case GETINDIVIDUALFILE:
				AppMain.textViewInfo.setVisibility(View.GONE);
                new StartDownloadIndividualFiles(appmainInstance).execute(receivedDownloadFiles);
                break;
			case SHOWDISPLAY:
				AppMain.textViewInfo.setVisibility(View.GONE);
				boolean individualFileSuccess = Utility.checkBooleanState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE));
				if(individualFileSuccess){
					displayingContentInOffileMode = false;
					if(displayScreen){
						AppState.ACTION_ON_PAUSE= true;
						Intent i = new Intent(appmainInstance, SignageDisplay.class);
						appmainInstance.startActivity(i);
						appmainInstance.finish();
						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								displayScreen = true;
							}
						}, 4000);
					}
					DeleteFileTask deleteFileTask = new DeleteFileTask();
					deleteFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
				}else if(checkNetwrk()){
					initProcess(true, GETSCHEDULE);
				}else{
					refreshScreen();
				}
				break;
			}
		}else{
			refreshScreen();
		}

	}
	
	
	public static boolean displayingContentInOffileMode = true;
	
	/**
	 * This method sends data to {@link SignageDisplay} to display layoutif it exists
	 */
	void displayContentInOfflineMode(){
		boolean regSuccess = Utility.checkBooleanState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_REG_COMPLETE));
		boolean scheduleSuccess = Utility.checkBooleanState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_SCHEDULE_COMPLETE));
		boolean fileSuccess = Utility.checkBooleanState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_FILE_COMPLETE));
		boolean individualFileSuccess = Utility.checkBooleanState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE));
		if(regSuccess && scheduleSuccess && fileSuccess && individualFileSuccess){
			displayingContentInOffileMode = true;
			ClientConnectionConfig.LAST_REQUEST_DATA = LogData.getInstance().getCurrentDisplayFiles(AppMain.getAppMainContext());

			if(!ClientConnectionConfig.LAST_REQUEST_DATA.equals("") && !ClientConnectionConfig.LAST_REQUEST_DATA.equals(LogData.STR_UNKNOWN)){
				if(fillResourceMapIfOffline(ClientConnectionConfig.LAST_REQUEST_DATA)){
					LogData.getInstance().setNewDisplayFilesXml(AppMain.getAppMainContext(),ClientConnectionConfig.LAST_REQUEST_DATA);
					initProcess(true, SHOWDISPLAY);
				}else{
					refreshScreen();
				}
			}else{
				refreshScreen();
			}
		}else{
			displayingContentInOffileMode = true;
			refreshScreen();
		}
	}

	void refreshScreen(){
		try {
			if(refreshRunnable == null){
				refreshRunnable = new Runnable() {
					@Override
					public void run() {
						if(checkNetwrk()){
							initProcess(true, REGISTER);
						}else{
							AppMain.textViewInfo.setText(AppMain.getAppMainContext().getResources().getString(R.string.no_internet_connection));
							AppMain.textViewInfo.setTextColor(appmainInstance.getResources().getColor(R.color.red_color));
							AppMain.textViewInfo.setVisibility(View.VISIBLE);
//							AppMain.progressBar.setVisibility(View.GONE);
							if(refreshHandler != null && refreshRunnable != null){
								refreshHandler.postDelayed(refreshRunnable, _TIME_TO_REHIT_SERVER);
							}else{
								refreshHandler =  new Handler();
								refreshHandler.postDelayed(refreshRunnable, _TIME_TO_REHIT_SERVER);
							}
						}
					}
				};
				refreshHandler =  new Handler();
				refreshHandler.postDelayed(refreshRunnable, _TIME_TO_REHIT_SERVER);
			}else{
				if(refreshHandler != null) {
					refreshHandler.postDelayed(refreshRunnable, _TIME_TO_REHIT_SERVER);
				}else{
				refreshHandler =  new Handler();
				refreshHandler.postDelayed(refreshRunnable, _TIME_TO_REHIT_SERVER);
			}
			}
		}catch (Exception e) {
			refreshScreen();
		}

	}
	/**
	 * This method check network connectivity status
	 * @return boolean value for network status
	 */
	boolean checkNetwrk(){
		boolean nwFlag = false;
		try{		
			ConnectivityManager connMgr = (ConnectivityManager) appmainInstance.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				nwFlag = true;
			}
		}catch (Exception e) {
			//e.printStackTrace();
		}

		return nwFlag;
	}
	
	
	/**
	 * This method add a row to Signage data of the files to be displayed
	 * @param data
	 * @return boolean
	 */
	boolean fillResourceMapIfOffline(String data){
		boolean retVal = true;
		Serializer serializer = new Persister();
		DisplayLayoutFiles lstFiles = null;

		try {
			lstFiles = serializer.read(DisplayLayoutFiles.class, data);
			for (DisplayLayoutFile file : lstFiles.getFileList()) {

				if (!file.getType().equalsIgnoreCase(_BLACKLIST_CATEGORY) ) {
					String fileID = file.getId();
					String fileType = file.getType();
					String fileName = file.getPath();
					int fileSize= 0;
					if(file.getSize()!=null){
						fileSize = Integer.parseInt(file.getSize());
					}

					if(fileType != null && fileType.contains("resource")){
						continue;
					}
					if(fileName == null){
						continue;
					}else if(fileName.isEmpty()){
						continue;
					}
					if(!fileName.endsWith(".js") ){
						if (!Utility.IsFileExists(fileName, false)) {
							retVal = false;
							break;
						}
					}
//					SignageData.getInstance().addRawDataNode(getUniqueFileId(fileType, fileID), fileName, fileType, fileSize);

				}
			}
		} catch (Exception e) {
			retVal = false;
			e.printStackTrace();
		}
		return retVal;
	}


	private class DeleteFileTask extends AsyncTask<Void, Void, Void> {

		public DeleteFileTask() {

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
//				String destPath = Environment.getExternalStorageDirectory()
//						+ AppState.DISPLAY_FOLDER;
//				File targetLocation = new File(destPath);
				Utility.deleteLeastUsedFileOnLowMemory();
//				FileManager.deleteDirIfFileNotInList(targetLocation);
			} catch (Exception e) {
//				e.printStackTrace();
			}

			return null;
		}
	}
	/**
	 * This method returns unique file id
	 * @param fileType
	 * @param fileID
	 * @return
	 */
	String getUniqueFileId(String fileType, String fileID ){
		return fileType+"_"+fileID;
	}

}
