package in.com.app.background;


import in.com.app.AppMain;
import in.com.app.IDisplayLayout;
import in.com.app.SignageDisplay;
import in.com.app.domain.DisplayLayoutFiles;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.utility.Utility;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
/**
 * This is Class that changes state of processes in the background
 * @author Ravi@xvidia
 *
 */
public class StateMachineDisplay implements IDisplayLayout{

	public final static int REGISTER = 0;
	public final static int GETFILE = 1;
	public final static int GETINDIVIDUALFILE = 2;
	public final static int GETSCHEDULE = 3;
	public final static int SHOWDISPLAY = 4;
	
	public DisplayLayoutFiles receivedXiboFiles = null;
	
	
	static StateMachineDisplay instance = null;
	Context context = null;
	Activity activity = null;
	public static StateMachineDisplay gi(Context ctx, Activity act ){
		if(instance == null){
			instance =  new StateMachineDisplay();
			instance.context = ctx;
			instance.activity = act;
		}
		return instance;
	}
	
	
	private StateMachineDisplay(){
		
	}
	
	
	/**
	 * This method switches between process and updates the display layout in the background
	 * @param nextRequest boolean value to process next Request
	 * @param processId
	 */
	public void initProcess(final boolean nextRequest, final int processId){
		if (!checkNetwrk()) {
			return;
		}
		
		if(!nextRequest){
			return;
		}
		switch (processId) {
		
		case GETSCHEDULE:			
			new BackgroundServerGetScheduleDisplay(context, activity).execute("");
			break;
		case GETFILE:			
			new BackgroundServerGetFilesDisplay(context, activity).execute();
			break;
		case GETINDIVIDUALFILE:			
			new BackgroundServerDownloadIndividualFilesDisplay(context, activity).execute(receivedXiboFiles);
			break;		
		case SHOWDISPLAY:
			try{
				boolean individualFileSuccess = Utility.checkBooleanState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_BACKGROUND_FILE_DOWNLOAD_COMPLETE));
				if(individualFileSuccess){
//					SignageDisplay.backGroundFlag = false;
					SignageDisplay.backGroundDownloadStarted = false;
//					Intent intent = mContext.getIntent();
////					intent.putExtra("name", "STATEMACHINeBAckground");
//					mContext.finish();
//					mContext.startActivity(intent);
//					DeleteFileTask deleteFileTask = new DeleteFileTask();
//					deleteFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
				}else if(checkNetwrk()){
//					SignageDisplay.backGroundFlag = true;
					SignageDisplay.backGroundDownloadStarted = true;
						initProcess(true, GETSCHEDULE);
				}else{
//					SignageDisplay.backGroundFlag = false;
					SignageDisplay.backGroundDownloadStarted = false;
				}
//				SignageDisplay displayObj = (SignageDisplay)mContext;
//				displayObj.createUI();
			}catch (Exception e) {
//				e.printStackTrace();
			}

			break;
		}

	}
	
	
//	
//	boolean displayingContentInOffileMode = false;
//	
//	void displayContentInOfflineMode(){
//		boolean regSuccess = checkState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_REG_COMPLETE));
//		boolean scheduleSuccess = checkState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_SCHEDULE_COMPLETE));
//		boolean fileSuccess = checkState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_FILE_COMPLETE));
//		boolean individualFileSuccess = checkState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE));
//		if(regSuccess && scheduleSuccess && fileSuccess && individualFileSuccess){
//			displayingContentInOffileMode = true;
//			ClientConnectionConfig.LAST_REQUEST_DATA = DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_LAST_DISPLAY_XML);
//			if(!ClientConnectionConfig.LAST_REQUEST_DATA.equals("")){
//				fillResourceMapIfOffline(ClientConnectionConfig.LAST_REQUEST_DATA);
//				initProcess(true, SHOWDISPLAY);
//			}
//		}
//	}
//	
//	
	/**
	 * This method check network connectivity status
	 * @return boolean value for network status
	 */
	boolean checkNetwrk(){
		boolean nwFlag = false;
		try{		
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				nwFlag = true;
			}
		}catch (Exception e) {
//			e.printStackTrace();
		}

		return nwFlag;
	}
//	
//	
//
//	boolean fillResourceMapIfOffline(String data){
//		boolean retVal = false;
//		Serializer serializer = new Persister();
//		DisplayLayoutFiles lstFiles = null;
//
//		try {
//			lstFiles = serializer.read(DisplayLayoutFiles.class, data);
//			for (DisplayLayoutFile file : lstFiles.getFileList()) {
//
//				if (!file.getType().equalsIgnoreCase(_BLACKLIST_CATEGORY) ) {
//					String fileID = file.getId();
//					String fileType = file.getType();
//					String fileName = file.getPath();
//					if(file.getSize()!=null){
//						fileSize = Integer.parseInt(file.getSize());
//					}
//
////					if(fileType.equalsIgnoreCase(_FILE_TYPE_LAYOUT)){
////						fileName = fileName+".txt";
////					}
//
//					SignageData.getInstance().addRawDataNode(getUniqueFileId(fileType, fileID), fileName, fileType, fileSize);
//
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return retVal;
//	}
//
//	
//	boolean checkState(String state){
//		boolean retVal = false;
//		if(state!=null && state.equalsIgnoreCase(FLAG_TRUE)){
//			retVal = true;
//		}
//		return retVal;
//	}
//
//
		private class DeleteFileTask extends AsyncTask<Void, Void, Void> {
		
				public DeleteFileTask() {
					
				}
		
				@Override
				protected Void doInBackground(Void... params) {
					try {
//						String destPath = Environment.getExternalStorageDirectory()
//								+ AppState.DISPLAY_FOLDER;
//						File targetLocation = new File (destPath);
						Utility.deleteLeastUsedFileOnLowMemory();
//						FileManager.deleteDirIfFileNotInList(targetLocation);
					} catch (Exception e) {
//						e.printStackTrace();
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
	public String getUniqueFileId(String fileType, String fileID ){
		return fileType+"_"+fileID;
	}

}
