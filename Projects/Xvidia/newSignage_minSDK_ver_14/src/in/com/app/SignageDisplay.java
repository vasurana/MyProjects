package in.com.app;

import in.com.app.background.BackgroundServerDownloadIndividualFilesDisplay;
import in.com.app.background.StateMachineDisplay;
import in.com.app.data.LogData;
import in.com.app.data.RefreshMediaData;
import in.com.app.data.SignageData;
import in.com.app.data.SyncMediaData;
import in.com.app.domain.DisplayLayout;
import in.com.app.domain.DisplayLayout.MediaOptions;
import in.com.app.domain.DisplayLayoutFile;
import in.com.app.domain.DisplayLayoutFiles;
import in.com.app.domain.DisplayLayoutSchedule;
import in.com.app.domain.DisplayLayoutScheduleDefaultFile;
import in.com.app.model.LayoutTimeData;
import in.com.app.model.LocationData;
import in.com.app.model.MediaTimeData;
import in.com.app.model.OnOffTimeData;
import in.com.app.model.IAPIConstants;
import in.com.app.network.ServiceURLManager;
import in.com.app.network.VolleySingleton;
import in.com.app.storage.caching.sqlight.MySqliteHelper;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.updater.LogUtility;
import in.com.app.updater.SyncLayoutReceiver;
import in.com.app.utility.MyExceptionHandler;
import in.com.app.utility.Utility;
import in.com.app.wsdl.XMDS;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.google.gson.Gson;

/**
 *
 * @author Ravi@Xvidia Technologies
 * @version 1.0
 * This is the display class where layout is dynamically created according the layout received from the server.
 *
 */
public class SignageDisplay extends Activity implements IDisplayLayout{

	final int _TIME_TO_REHIT_SERVER =1*30*1000;//5*60*1000;
	public static boolean backGroundDownloadStarted = false;
	static boolean nextLayoutchange = false;
	static boolean videoRefresh = false;
	static String displayedLayout = "";
	static String nextLayout = "";

	static long nextLayoutTime = 0,nextStartTime = 0,nextEndTime = 0;
	public static boolean alarmRefresh = false;
	static Handler handler = null, timerTaskHandler =null, timerTaskMediaUpdate = null, refreshHandler = null, nextLayoutHandler = null,currentFileHandler = null;
	static private Runnable runnable, runnableTimerTask, runnableMediaUpdate,refreshRunnable,nextLayoutRunnable, currentFileRunnble;
	Vector<OnOffTimeData> onOffListBox = new Vector<OnOffTimeData>();
	Vector<OnOffTimeData> onOffListScreen = new Vector<OnOffTimeData>();
	Vector<OnOffTimeData> onOffListAPP = new Vector<OnOffTimeData>();
	Vector<MediaTimeData> mList = new Vector<MediaTimeData>();
	Vector<LayoutTimeData> mListLayout = new Vector<LayoutTimeData>();
	static LayoutTimeData layoutTimeSync;
	static MediaTimeData mediaTimeDataSync;
	ArrayList<SyncMediaData> syncMediaList= null;
	int currentApiVersion;
	static int viewId=5;
    static int regionCountFinal = 1;
	ArrayList<RefreshMediaData> refreshMediaList= null;
	HashMap<Integer, RefreshMediaData> regionMediaMap= null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateUi();
	}


	private void onCreateUi(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		currentApiVersion = android.os.Build.VERSION.SDK_INT;

		final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

		// This work only for android 4.4+
		if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
		{

			getWindow().getDecorView().setSystemUiVisibility(flags);
			final View decorView = getWindow().getDecorView();
			decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
			{

				@Override
				public void onSystemUiVisibilityChange(int visibility)
				{
					if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
					{
						decorView.setSystemUiVisibility(flags);
					}
				}
			});
		}
		Log.i("SignageDisplay","On Creat function ");
//		alarmRefresh = true;
/////////
		AppState.ACTION_ON_PAUSE= false;
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this,AppMain.class));
		AppState.BACKPRESSED_SIGNAGE_SCREEN = false;
		backGroundDownloadStarted = false;
        nextStartTime = System.currentTimeMillis();
		createUI();
		setScreenOn();
		sendMediaUpdate();
		cancelLayoutAlarm(this);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
		{
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

	private void sendLogRequest(Context ctx, JSONObject jsonObject, String urlStr){
		try {
			if(LogUtility.checkNetwrk(ctx)){

				Log.d("Request sending", ""+urlStr);
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlStr, jsonObject, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
				VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);
			}
		}catch(Exception e){

		}

	}


	public void setScreenOn(){
		try {
			getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		} catch (Exception e) {
		}
	}

	private void setSyncLayoutAlarm(Context context) {
		cancelLayoutAlarm(context);
		Calendar updateTime = Calendar.getInstance();
		int hr = updateTime.get(Calendar.HOUR_OF_DAY);
		int min = updateTime.get(Calendar.MINUTE);
//		hr = hr+1;
		min = min+3;
//		if(hr!=12){
//
//		}
		updateTime.set(Calendar.HOUR_OF_DAY, hr);

		updateTime.set(Calendar.MINUTE, min);
		Intent downloader = new Intent(context, SyncLayoutReceiver.class);
		PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
				0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarms = (AlarmManager) getSystemService(
				Context.ALARM_SERVICE);

//		alarms.setExact(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(), recurringDownload);

	}

	private void cancelLayoutAlarm(Context context) {
		try{
			alarmRefresh = true;
			Intent downloader = new Intent(context, SyncLayoutReceiver.class);
			PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
					0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager alarms = (AlarmManager) getSystemService(
					Context.ALARM_SERVICE);
			alarms.cancel(recurringDownload);
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * In this method layout string is converted to DisplayLayout
	 * @see DisplayLayout
	 */
	public void createUI(){
		try{
//            Log.i("nextlayout time", ""+nextStartTime+" Utility.getNextLayoutStartTime()"+Utility.getNextLayoutStartTime());
            nextLayoutchange = false;
            setContentView(R.layout.layout_signage);
			displayedLayout = SignageData.getInstance().getCurrentLayout();
			DisplayLayout ly = null;
			Serializer srLayout = new Persister();
			String layoutStaring ="";
			try{
				//						ly = srLayout.read(XiboLayout.class, getLayoutString(xiboLayoutRawData.dataBytes));
				layoutStaring = LogUtility.getLayoutStringFromFile(displayedLayout);
				if(layoutStaring == null || layoutStaring.length()==0){
					if(!LogData.getInstance().getCurrentLayoutXml(AppMain.getAppMainContext()).equalsIgnoreCase(LogData.STR_UNKNOWN)){
						layoutStaring = LogData.getInstance().getCurrentLayoutXml(AppMain.getAppMainContext());
						String destPath = Environment.getExternalStorageDirectory()
								+ AppState.DISPLAY_FOLDER;
						File sourceLocation = new File (destPath,displayedLayout);
						FileManager.deleteDir(sourceLocation);
						Serializer serializer = new Persister();
						DisplayLayoutFiles lstFiles = null;

						try {
							String strFiles = LogData.getInstance().getCurrentDisplayFiles(AppMain.getAppMainContext());
							lstFiles = serializer.read(DisplayLayoutFiles.class, strFiles);
							if(lstFiles != null)
								LogData.getInstance().setDownloadPending(AppMain.getAppMainContext(), false);
							new BackgroundServerDownloadIndividualFilesDisplay(getApplicationContext(), SignageDisplay.this).execute(lstFiles);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else {
					LogData.getInstance().setCurrentLayoutXml(AppMain.getAppMainContext(), layoutStaring);

					try {
						ly = srLayout.read(DisplayLayout.class, layoutStaring);
					} catch (IOException io) {
						io.printStackTrace();
						ly = null;
					} catch (Exception e) {
						ly = null; e.printStackTrace();
					}
					if (ly != null) {
//						nextStartTime = System.currentTimeMillis();
						displayLayout(ly, false);
					} else {
						SignageData.resetSignageData();
						StateMachineDisplay.gi(getApplicationContext(), SignageDisplay.this).initProcess(true, StateMachineDisplay.GETSCHEDULE);
					}
//							break;
				}
			}catch (Exception e) {

				Utility.writeLogFile(e.getMessage());
				Toast.makeText(this, "error in CreateUI srLayout "+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			getCurrentFileHandler();
			refreshScreen();
		}catch (Exception e) {
			Utility.writeLogFile(e.getMessage());
//			Toast.makeText(this, "error in CreateUI  "+e.getMessage(), 3000).show();
//			e.printStackTrace();
		}
	}

	/**
	 * The nework connection status is returned
	 * @return true if network is connected else false
	 * @since version 1.0
	 */
	boolean checkNetwork(){
		boolean nwFlag = false;
		try{
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				nwFlag = true;
			}
			if(!nwFlag){
				nwFlag = LogData.getInstance().getInternetConnection(AppMain.getAppMainContext());
			}
		}catch (Exception e) {
		}

		return nwFlag;
	}


	DisplayLayoutServerGetFiles objXXiboServerGetFiles = null;
	/**
	 * This method queries server to check if any new layout has been assigned the STB
	 *  and starts downloading the updated layout file in the background
	 *  This method is called after certain interval.
	 *  @see DisplayLayoutServerGetFiles
	 *  @since version 1.0
	 */
	void refreshScreen(){
		if(AppState.BACKPRESSED_SIGNAGE_SCREEN)
			return;
		try {
//			if(refreshHandler != null){
//				refreshHandler.removeCallbacks(refreshRunnable);
//				refreshHandler = null;
//				}
			if(refreshHandler == null){
				refreshRunnable = new Runnable() {
					@Override
					public void run() {
						if(checkNetwork()){
							LogData.getInstance().setOfflineSinceTimeStamp(AppMain.getAppMainContext(),0);

							objXXiboServerGetFiles = new DisplayLayoutServerGetFiles();
							objXXiboServerGetFiles.execute();
						}else{
							if(LogData.getInstance().getOfflineSinceTimeStamp(AppMain.getAppMainContext())== 0){
								LogData.getInstance().setOfflineSinceTimeStamp(AppMain.getAppMainContext(),System.currentTimeMillis());
							}
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
				if(refreshRunnable != null){
					refreshHandler.postDelayed(refreshRunnable,10000);
				}
			}
		}catch (Exception e) {
			refreshScreen();
		}

	}

	void getCurrentFileHandler(){
		try {
			if(currentFileHandler != null){
				currentFileHandler.removeCallbacks(currentFileRunnble);
				currentFileHandler = null;
			}
			currentFileRunnble = new Runnable() {
				@Override
				public void run() {
					getCurrentFile();
					if(currentFileHandler != null && currentFileRunnble != null){
						currentFileHandler.postDelayed(currentFileRunnble, 10000);
					}else{
						currentFileHandler =  new Handler();
						currentFileHandler.postDelayed(currentFileRunnble, 10000);
					}
				}
			};
			currentFileHandler =  new Handler();
			currentFileHandler.postDelayed(currentFileRunnble, 10000);

		}catch (Exception e) {
			getCurrentFileHandler();
		}

	}

	private void nextLayout(){
		try {
			boolean resetHandler = false;
			if(nextLayoutHandler != null){
				if(!nextLayout.equalsIgnoreCase(Utility.getNextLayout())){
					nextLayout = Utility.getNextLayout();
					nextLayoutTime = Utility.getNextLayoutChangeTime();
					nextLayoutHandler.removeCallbacks(refreshRunnable);
					nextLayoutHandler = null;
					resetHandler = true;
				}
			}else{
				resetHandler = true;
			}

			if(resetHandler){
				nextLayout = Utility.getNextLayout();
				nextLayoutTime = Utility.getNextLayoutChangeTime();

				if(!nextLayout.isEmpty() && nextLayoutTime >0){
					nextLayoutRunnable = new Runnable() {
						@Override
						public void run() {
							if(!nextLayout.isEmpty()){
								if(checkIfFileExistsElseDownload()) {
									SignageData.getInstance().setCurrentLayout(nextLayout);
									DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_SCHEDULE_CURRENTFILE, SignageData.getInstance().getCurrentLayout());
									updateMediaEndTimeOnServer(true);
                                    nextLayoutchange = true;
//                                    nextStartTime = Utility.getNextLayoutStartTime();
//                                    if(nextStartTime == 0){
                                        nextStartTime = System.currentTimeMillis();
//                                    }
                                    createUI();
								}else{
									if(!backGroundDownloadStarted){
										backGroundDownloadStarted = true;
										SignageData.resetSignageData();
										StateMachineDisplay.gi(getApplicationContext(), SignageDisplay.this).initProcess(true, StateMachineDisplay.GETSCHEDULE);
									}
								}
							}
						}
					};
					nextLayoutHandler =  new Handler();
					nextLayoutHandler.postDelayed(nextLayoutRunnable, nextLayoutTime);
				}
			}


		}catch (Exception e) {
		}

	}

	/**
	 * This method checks whether layout currently assigned to the STB on server
	 *  is same as the layout being displyed now.
	 * @return true  layout has been updated on server else false
	 */
	boolean makeRefreshRequest(){

		boolean flag =  true;
		try{
			ActivityManager activityManager = (ActivityManager)getApplicationContext()
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
			RunningTaskInfo object = runningTasks.get(runningTasks.size()-1);
			if(object.topActivity.getClass().equals(AppMain.class)){
                nextStartTime = System.currentTimeMillis();
				createUI();
			}
		}catch (Exception e) {
		}
		try{
			String serverUrl = ClientConnectionConfig._SERVERURL;
			String uniqueKey = ClientConnectionConfig._UNIQUE_SERVER_KEY;
			String hardwareKey = ClientConnectionConfig._HARDWAREKEY;
			String ver =  ClientConnectionConfig._VERSION;
			XMDS xmds = new XMDS(serverUrl);
			if(checkNetwork()){
				if(LogData.getInstance().getDownloadPending(AppMain.getAppMainContext())){
					return true;
				}
				String strReqFiles = "";
				String strScheduleFiles = "";
				if(ClientConnectionConfig._UNIQUE_SERVER_KEY!=null && ClientConnectionConfig._HARDWAREKEY!=null){

					strReqFiles = xmds.RequiredFiles(uniqueKey, hardwareKey, ver );
					strScheduleFiles = xmds.Schedule(ClientConnectionConfig._UNIQUE_SERVER_KEY, ClientConnectionConfig._HARDWAREKEY, ClientConnectionConfig._VERSION);

					if(strReqFiles!=null){
						strReqFiles = strReqFiles.trim();
						LogData.getInstance().setNewDisplayFilesXml(AppMain.getAppMainContext(),strReqFiles);

					}else{
						strReqFiles = "";
					}
					//				String lastSchedule = DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_SCHEDULE_XML);

					if(strScheduleFiles!=null){
						strScheduleFiles = strScheduleFiles.trim();
						DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_SCHEDULE_XML,strScheduleFiles);

					}else{
						strScheduleFiles = "";
					}
					//				if(lastSchedule!=null){
					//					lastSchedule = lastSchedule.trim();
					//				}else{
					//					lastSchedule = "";
					//				}
					ClientConnectionConfig.LAST_REQUEST_DATA = LogData.getInstance().getCurrentDisplayFiles(AppMain.getAppMainContext());
					ClientConnectionConfig.LAST_REQUEST_DATA = ClientConnectionConfig.LAST_REQUEST_DATA.trim();
					Serializer serializer = new Persister();
					DisplayLayoutFiles lstFiles = null, lstFilesOld = null;

					try {
						lstFilesOld = serializer.read(DisplayLayoutFiles.class, ClientConnectionConfig.LAST_REQUEST_DATA);
					} catch (Exception e) {
					}
					try {
						lstFiles = serializer.read(DisplayLayoutFiles.class, strReqFiles);
					} catch (Exception e) {
					}
					ArrayList<DisplayLayoutFile> fileListold = new ArrayList<DisplayLayoutFile>();
					ArrayList<DisplayLayoutFile> fileList = new ArrayList<DisplayLayoutFile>();

					if(lstFiles != null && lstFilesOld != null){
						fileList = lstFiles.getFileList();
						fileListold = lstFilesOld.getFileList();
						if((fileList != null && fileListold != null) && (fileList.size()== fileListold.size())){
							for(int i = 0; i<fileListold.size();i++){
								if(fileListold.get(i).getType().equalsIgnoreCase("resource")){
									fileListold.remove(i);
									fileList.remove(i);
								}

							}

							if(fileList.size()== fileListold.size()){
								int count1 = fileList.size();
								flag = false;
								for(int i = 0; i<count1;i++){
									if(fileList.get(i).getPath()== null && fileListold.get(i).getPath()== null){
										continue;
									}
									if(!fileList.get(i).getPath().equalsIgnoreCase(fileListold.get(i).getPath())){
										flag= true;
										break;
									}
								}
							}
						}else{
							flag =  true;
						}
					}else{
						flag =  false;
					}
				}else{
					flag =  false;
				}
				//			Log.d(getClass().getCanonicalName(),"Result makeRefreshRequest "+ flag);
			}else{
				flag =  false;
			}
		}catch (Exception e) {
			flag =  false;
		}
		return flag;
	}


	void getCurrentFile(){
		Serializer serializer = new Persister();
		DisplayLayoutSchedule displayLayoutSchedule=null;
		String strReqFiles = DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_SCHEDULE_XML);
		try {
			if(strReqFiles!=null){
				displayLayoutSchedule = serializer.read(DisplayLayoutSchedule.class, strReqFiles);
			}
			if(displayLayoutSchedule!=null){
//				Log.d(appmainInstance.TAG, "CountofGotFiles " + displayLayoutSchedule.toString());
				SignageData.getInstance().setLayoutSchedule(displayLayoutSchedule);
				fillDataOfSchedule();
			}
		} catch (Exception e) {
		}
	}

	boolean checkIfFileExistsElseDownload(){
		boolean changeLayout = true;
		ArrayList<String> fileList = SignageData.getInstance().getCurrentLayoutFiles();
		if(fileList != null) {
			for (String filename : fileList) {
				if (!Utility.IsFileExists(filename, false)) {
					changeLayout = false;
					break;
				}
			}
		}else{
			String strFiles = LogData.getInstance().getNewDisplayFiles(AppMain.getAppMainContext());
			if(!strFiles.equalsIgnoreCase(LogData.STR_UNKNOWN)) {
				changeLayout = isFileExistToDisplay(strFiles);
			}else{
				changeLayout = false;
			}
		}
		return changeLayout;
	}

	void fillDataOfSchedule(){
		DisplayLayoutSchedule displayLayoutSchedule = SignageData.getInstance().getLayoutSchedule();
		if(displayLayoutSchedule !=null) {
			DisplayLayoutScheduleDefaultFile displayLayoutScheduleDefault = displayLayoutSchedule.getScheduleDefault();
			SignageData.getInstance().setDefaultLayout(displayLayoutScheduleDefault.getFile());
//			SignageData.getInstance().setCurrentLayout(Utility.getCurrentFile(displayLayoutSchedule.getLayout()));
			String currentFile = displayedLayout;
            if(Utility.getNextLayoutStartTime()>0 && (Utility.getNextLayoutStartTime() - System.currentTimeMillis()>10000)) {
                currentFile = Utility.getCurrentFile(displayLayoutSchedule.getLayout());
            }else if(System.currentTimeMillis()-Utility.getNextLayoutStartTime()>8000){
                currentFile = Utility.getCurrentFile(displayLayoutSchedule.getLayout());
            }
			nextLayout();
			// SignageData.getInstance().getCurrentLayout();
			if (!nextLayoutchange) {
				if (!displayedLayout.equalsIgnoreCase(currentFile)) {
					if (checkIfFileExistsElseDownload()) {
						SignageData.getInstance().setCurrentLayout(currentFile);
						DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_SCHEDULE_CURRENTFILE, SignageData.getInstance().getCurrentLayout());
                        nextStartTime = System.currentTimeMillis();
                        createUI();
					} else {
						if (!backGroundDownloadStarted) {
							backGroundDownloadStarted = true;
							SignageData.resetSignageData();
							StateMachineDisplay.gi(getApplicationContext(), SignageDisplay.this).initProcess(true, StateMachineDisplay.GETSCHEDULE);
						}
					}
//
				}
			}
		}
	}

	boolean isFileExistToDisplay(String data){
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
					if(!fileName.endsWith(".js")){
						if (!Utility.IsFileExists(fileName, false)) {
							Log.d("fileName doe not exits", fileName);
							retVal = false;
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			retVal = false;
		}
		return retVal;
	}


	private class SynMediaDataListTask extends AsyncTask <DisplayLayout.Region, Void, Void > {
		@Override
		protected Void doInBackground(DisplayLayout.Region... arg0) {
			DisplayLayout.Region region = arg0[0];
			long endTime,startTime;
			startTime = nextStartTime;
			syncMediaList = new ArrayList<SyncMediaData>();
			for(DisplayLayout.Media media: region.getMedia()){  //GET REGION
				SyncMediaData mediaData = new SyncMediaData();
				mediaData.setStartTimeStamp(startTime);
				mediaData.setMedia(media);
				///GET MEDIA
				long duration = Long.parseLong(media.getDuration());
				endTime = startTime+(duration*1000);
				mediaData.setEndTimeStamp(endTime);
				mediaData.setDuration(duration);
				mediaData.setRegion(region);
				syncMediaList.add(mediaData);
				startTime = endTime;
//				Log.e("SyncDataList", mediaData.toString());
			}
			return null;
		}
	}

	/**
	 * This class is a asyncTask that downloads the latest layout available on server for the STB
	 * @author Ravi_office
	 * @seeversion 1.0
	 *
	 */
	private class DisplayLayoutServerGetFiles extends AsyncTask <String, Void, String > {

		@Override
		protected String doInBackground(String... arg0) {

			if (makeRefreshRequest()) {
				return null;
			} else{
				return "";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result == null){
				if(!backGroundDownloadStarted){
					backGroundDownloadStarted = true;
					SignageData.resetSignageData();
					StateMachineDisplay.gi(getApplicationContext(), SignageDisplay.this).initProcess(true, StateMachineDisplay.GETSCHEDULE);
				}

			}
			refreshScreen();
		}
	}


	boolean failRefreshFlag = false;
	static Bitmap previousBitmap = null;
	static String mediaUri = "";
	/**
	 * This method dynamically creates layout to be displayed on the STB according to displayLayout
	 * @param ly object of the layout string
	 * @see DisplayLayout
	 * @since version 1.0
	 */
	@TargetApi(17)
	void displayLayout(DisplayLayout ly, boolean refreshFlag){
		try{
			regionMediaMap = new HashMap<Integer, RefreshMediaData>();
			/// CALCULATE SCREEN SIZE
			float prodWidth = (float)ly.getWidth();
			float prodHeight = (float)ly.getHeight();
			if(!refreshFlag){
				if(prodWidth<prodHeight){
					if(LogData.getInstance().getOrientation(AppMain.getAppMainContext())== ORIENTATION_REVERSE_PORTRAIT){
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					}else{
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					}
				}else{
					if(LogData.getInstance().getOrientation(AppMain.getAppMainContext())== ORIENTATION_REVERSE_LANSCAPE){
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					}else{
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
					}
				}
			}
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			if(currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1){
				display.getRealSize(size);
			}else{
				display.getSize(size);
			}
			int layoutWidth = size.x;
			int layoutHeight = size.y;

//
			float widthMultiplierFactor = (float)layoutWidth/prodWidth;
			float heightMultiplierFactor = (float)layoutHeight/prodHeight;

			int callBackTime = 0;
			RelativeLayout parentLayout = (RelativeLayout) SignageDisplay.this.findViewById(R.id.relative_id);
			if(ly.getBackground()!=null){
				String PATH ="";
				try{
					PATH = Environment.getExternalStorageDirectory()
							+ AppState.DISPLAY_FOLDER + ly.getBackground();
					Bitmap bmImg = Utility.decodeScaledBitmapFromSdCard(PATH,
							layoutWidth, layoutHeight);
					Drawable background = new BitmapDrawable(getResources(),
							bmImg);
					if (background != null) {
						parentLayout.setBackground(background);
					}
				} catch (OutOfMemoryError e) {
					Log.i("Out of memmory error", "" + PATH);
				} catch (Exception e) {

				}
			}
//			}
			int regionCount = 0;
			int maxMediaCount = 0,mediaCounterToDisplay=0;
//			ArrayList<RefreshMediaData> tempRefreshMediaList= new ArrayList<RefreshMediaData>();
			syncMediaList = new ArrayList<SyncMediaData>();
			for(DisplayLayout.Region region : ly.getRegion()){  //GET REGION
				maxMediaCount = (region.getMedia()).size();
				RefreshMediaData refreshMediaData = new RefreshMediaData();
				refreshMediaData.setRegion(region);
				refreshMediaData.setMediaCount(0);
				///////////////////////////////////////////////////////////////////
//				if(nextLayoutchange){
//
//				}
//                if(regionCountFinal==1)
//				    new SynMediaDataListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, region);
				int regionWidth =(int)Math.floor(region.getWidth());
				int regionHeight = (int)Math.floor(region.getHeight());
				int layoutMarginLeft = (int)Math.floor(region.getLeft());
				int layoutMarginTop =  (int)Math.floor(region.getTop());
				try{
					float newRegionWidth = regionWidth*widthMultiplierFactor;
					float newRegionHeight = regionHeight*heightMultiplierFactor;
					float newlayoutMarginLeft = layoutMarginLeft*widthMultiplierFactor;
					float newlayoutMarginTop = layoutMarginTop*heightMultiplierFactor;
					regionWidth = (int)newRegionWidth;
					regionHeight = (int)newRegionHeight;
					layoutMarginLeft = (int)newlayoutMarginLeft;
					layoutMarginTop = (int)newlayoutMarginTop;
				}catch(NumberFormatException e){

				}catch(Exception e){

				}
				///GET MEDIA
				int count = 0;
				int regionMediaCount = region.getMedia().size();
				if( mediaCounterToDisplay < regionMediaCount){
					count = mediaCounterToDisplay;
				}
				////////

				if(regionCount == 0){
					layoutTimeSync = new LayoutTimeData();
					layoutTimeSync.setId("");
					layoutTimeSync.setBoxId(ClientConnectionConfig._HARDWAREKEY);
					layoutTimeSync.setLayoutId(displayedLayout);
					layoutTimeSync.setStartTime(LogUtility.getTimeStamp());
					layoutTimeSync.setEndTime("");
					DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeSync);
					sendLayoutTimeRequest(layoutTimeSync);
				}
				//////////////////


				DisplayLayout.Media media =  (region.getMedia()).get(count);
				nextEndTime = nextStartTime+(Integer.parseInt(media.getDuration()))*1000;
				String id = (_MEDIA+"_"+media.getId());
				String type = media.getType();
                int viewId = getViewId();
				RelativeLayout v = (RelativeLayout)parentLayout.findViewById(viewId);
				refreshMediaData.setTimestamp(nextEndTime);
                refreshMediaData.setViewId(viewId);
				int timediff  = (int)(nextEndTime -nextStartTime);
				if(callBackTime == 0) {
					callBackTime = timediff;
				}else if(callBackTime > timediff){
					callBackTime = timediff;
				}
				mediaTimeDataSync = new MediaTimeData();
//				mediaTimeDataSync.setId("");
//				mediaTimeDataSync.setBoxId(ClientConnectionConfig._HARDWAREKEY);
//				mediaTimeDataSync.setLayoutId(displayedLayout);

				if(count==0 && mediaCounterToDisplay > 0 ){

				}else{
					try{
						boolean flagAction = true;

						if(flagAction){
							if(v!=null){
								try{
                                    ViewGroup parent = (ViewGroup) v.getParent();
                                    parent.removeView(v);
								}catch(Exception e){
									e.printStackTrace();
								}
							}
							if(type.equalsIgnoreCase(_MEDIATYPE_IMAGE)) {
								callBackTime = drawImageViewRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, callBackTime, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_EMBEDEDHTML)){
								drawEmbeddedHtmlRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_TEXT)){
								drawTextRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_TICKER)){
								drawWebViewTickerRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_WEBPAGE)){
								drawWebViewRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_URL_STREAM_VIDEO)){
								callBackTime = drawVideoViewStreamingRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, callBackTime, refreshMediaData);
//						}else if(type.equalsIgnoreCase(_MEDIATYPE_HDMI_STREAMING)) {
//							callBackTime = drawHDMIRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, callBackTime, regionCount);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_VIDEO)){
								callBackTime = drawVideoViewRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, callBackTime, refreshMediaData);
							}
						}
					}catch (Exception e) {
						failRefreshFlag = true;
						Utility.writeLogFile("error in displayLayout ()  " + e.getMessage());
						showToastMessage("error in main Layout  " + e.getMessage());
						break;
					}

				}
				regionMediaMap.put(regionCount,refreshMediaData);
				regionCount++;
//				tempRefreshMediaList.add(refreshMediaData);
			}
//			refreshMediaList = tempRefreshMediaList;
			final DisplayLayout lyDisplay = ly;
			mediaCounterToDisplay++;
			if(mediaCounterToDisplay  == maxMediaCount){
				mediaCounterToDisplay = 0;
                    if (layoutTimeSync != null) {
                        layoutTimeSync.setEndTime(LogUtility.getTimeStamp());
                        DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeSync);
                        sendLayoutTimeRequest(layoutTimeSync);
                    }
			}

			final int nextMediaCounterToDisplay = mediaCounterToDisplay;
            regionCountFinal= regionCount;
			try {
				if(timerTaskHandler != null){
						timerTaskHandler.removeCallbacks(runnableTimerTask);
						timerTaskHandler = null;
					}

					runnableTimerTask = new Runnable() {
						@Override
						public void run() {
							updateMediaEndTimeOnServer(false);
							if (timerTaskHandler != null && runnableTimerTask != null)
								timerTaskHandler.postDelayed(runnableTimerTask, 3000);
						}
					};
					timerTaskHandler = new Handler();
					timerTaskHandler.postDelayed(runnableTimerTask, 1500);
//				}
			}catch (Exception e) {
			}
			if(failRefreshFlag){
				try {
					if(handler != null){
						handler.removeCallbacks(runnable);
						handler = null;
					}
					runnable = new Runnable() {
						@Override
						public void run() {
							failRefreshFlag = false;
							showToastMessage("Restarting Display");
                                updateMediaEndTimeOnServer(true);
                            nextStartTime = System.currentTimeMillis();
							createUI();
						}
					};
					handler =  new Handler();
					handler.post(runnable);

				}catch (Exception e) {
				}
			}else if(callBackTime>0){

				try {
					if(handler != null){
						handler.removeCallbacks(runnable);
						handler = null;
					}
					runnable = new Runnable() {
						@Override
						public void run() {
							if(lyDisplay !=null){
//								if(regionCountFinal>1) {
									refreshRegionLayout(lyDisplay);
//								}else{
//                                    if (mediaTimeDataSync != null) {
//                                        String time = LogUtility.getTimeStamp();
//                                        mediaTimeDataSync.setEndTime(time);
//                                        DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeDataSync);
//                                        sendMediaTimeRequest(mediaTimeDataSync);
//                                    }
//									refresh(lyDisplay,nextMediaCounterToDisplay);
//								}
							}
						}
					};
					handler =  new Handler();
					handler.postDelayed(runnable, callBackTime);

				}catch (Exception e) {
				}

			}else if(callBackTime==0){

				try {
					if(handler != null){
						handler.removeCallbacks(runnable);
						handler = null;
					}
					runnable = new Runnable() {
						@Override
						public void run() {
//							if(regionCountFinal>1) {
								refreshRegionLayout(lyDisplay);
//							}else {
//                                if (mediaTimeDataSync != null) {
//                                    String time = LogUtility.getTimeStamp();
//                                    mediaTimeDataSync.setEndTime(time);
//                                    DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeDataSync);
//                                    sendMediaTimeRequest(mediaTimeDataSync);
//                                }
//                                refresh(lyDisplay, nextMediaCounterToDisplay);
//                            }
						}
					};
					handler =  new Handler();
					handler.post(runnable);

				}catch (Exception e) {
				}

			}


		}catch (Exception e) {
		}
	}

	void showToastMessage(final String message){

		Utility.writeLogFile("error: "+message);
//		runOnUiThread(new Runnable() {
//			public void run() {
//				Toast.makeText(SignageDisplay.this,message , Toast.LENGTH_SHORT).show();
//			}
//		});
	}

	@TargetApi(17)
	void refresh(DisplayLayout ly,int mediaCount){
		try{

			Log.d("refresh refresh", "mediaCount " + mediaCount);
			/// CALCULATE SCREEN SIZE

            nextLayoutchange = false;
			float prodWidth = ly.getWidth();//.floatValue();
			float prodHeight = ly.getHeight();//.floatValue();
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			if(currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1){
				display.getRealSize(size);
			}else{
				display.getSize(size);
			}
			int layoutWidth = size.x;
			int layoutHeight = size.y;

			float widthMultiplierFactor = (float)layoutWidth/prodWidth;
			float heightMultiplierFactor = (float)layoutHeight/prodHeight;

			long callBackTime = 0;
			long startTime, endTime;
			RelativeLayout parentLayout = (RelativeLayout) SignageDisplay.this.findViewById(R.id.relative_id);
			//			if(mediaCounterToDisplay == 0){
			//				layout.removeAllViews();
			//			}
			if(ly.getBackground()!=null){
				String PATH ="";
				try{
					PATH = Environment.getExternalStorageDirectory()
							+ AppState.DISPLAY_FOLDER + ly.getBackground();
					Bitmap bmImg = Utility.decodeScaledBitmapFromSdCard(PATH,
							layoutWidth, layoutHeight);
					Drawable background = new BitmapDrawable(getResources(),
							bmImg);// Drawable.createFromPath(PATH);
					// File imgFile = new File(PATH);
					// if(imgFile.exists()){
					if (background != null) {
						parentLayout.setBackground(background);
					}
				} catch (OutOfMemoryError e) {
					Log.i("Out of memmory error", "" + PATH);
				} catch (Exception e) {

				}
			}
//			}
			int regionCount = 0,maxMediaCount=0;
			if(syncMediaList.size() == 0){
				displayLayout(ly, true);
				return;
			}
			maxMediaCount = syncMediaList.size();
			int regionWidth =(int)Math.floor(prodWidth);
			int regionHeight = (int)Math.floor(prodHeight);
			int layoutMarginLeft = (int)Math.floor(0);
			int layoutMarginTop =  (int)Math.floor(0);
			try{
				float newRegionWidth = regionWidth*widthMultiplierFactor;
				float newRegionHeight = regionHeight*heightMultiplierFactor;
				float newlayoutMarginLeft = layoutMarginLeft*widthMultiplierFactor;
				float newlayoutMarginTop = layoutMarginTop*heightMultiplierFactor;
				regionWidth = (int)newRegionWidth;
				regionHeight = (int)newRegionHeight;
				layoutMarginLeft = (int)newlayoutMarginLeft;
				layoutMarginTop = (int)newlayoutMarginTop;
			}catch(NumberFormatException e){

			}catch(Exception e){

			}
			///GET MEDIA
			int count = 0;
//				int regionMediaCount = region.getMedia().size();
			if( mediaCount < maxMediaCount){
				count = mediaCount;
			}
			SyncMediaData dataObject = syncMediaList.get(count);


			DisplayLayout.Media media =  dataObject.getMedia();
			startTime = dataObject.getStartTimeStamp();
			endTime = dataObject.getEndTimeStamp();
            long currentTime = System.currentTimeMillis();
//			currentTime = currentTime + 250;
            Log.i("viewId", "" + viewId + " regionCount" + regionCount);
            if (startTime <= currentTime && endTime>currentTime) {


                if (mediaCount == 0) {
                    layoutTimeSync = new LayoutTimeData();
                    layoutTimeSync.setId("");
                    layoutTimeSync.setBoxId(ClientConnectionConfig._HARDWAREKEY);
                    layoutTimeSync.setLayoutId(displayedLayout);
                    layoutTimeSync.setStartTime(LogUtility.getTimeStamp());
                    layoutTimeSync.setEndTime("");
                    sendLayoutTimeRequest(layoutTimeSync);
                }
                //////////////////
                mediaTimeDataSync = new MediaTimeData();
//			mediaTimeDataSync.setId("");
//			mediaTimeDataSync.setBoxId(ClientConnectionConfig._HARDWAREKEY);
//			mediaTimeDataSync.setLayoutId(displayedLayout);
                ////////////////////////////////////////////
                String id = (_MEDIA + "_" + media.getId());
                String type = media.getType();
//				Log.d(getClass().getCanonicalName(),"regionCount before "+ regionCount);
                RelativeLayout v = (RelativeLayout) parentLayout.findViewById(regionCount);
                long delta = System.currentTimeMillis() - startTime;
                Log.d("Time diff delta", "delta " + delta);
                startTime = System.currentTimeMillis();
                callBackTime = (endTime - startTime);

                if (count == 0 && mediaCount > 0) {
                } else {
                    try {
                        boolean flagAction = true;
                        if (count == 0 && v != null && maxMediaCount == 1) {
                            flagAction = false;
                        }
                        if (count == 0 && v != null && maxMediaCount == 1 && (type.equalsIgnoreCase(_MEDIATYPE_IMAGE) || type.equalsIgnoreCase(_MEDIATYPE_TEXT) || type.equalsIgnoreCase(_MEDIATYPE_TICKER))) {
                            flagAction = false;
                            if (mediaTimeDataSync != null) {
                                String time = LogUtility.getTimeStamp();
                                mediaTimeDataSync.setEndTime(time);
                                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeDataSync);
                                sendMediaTimeRequest(mediaTimeDataSync);
                            }
                        }
                        if (flagAction) {
                            if (v != null) {
                                try {
                                    ViewGroup parent = (ViewGroup) v.getParent();
                                    parent.removeView(v);
                                } catch (Exception e) {
                                }
                            }
                            if (type.equalsIgnoreCase(_MEDIATYPE_IMAGE)) {
                                callBackTime = drawImageViewRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, null);
                            } else if (type.equalsIgnoreCase(_MEDIATYPE_EMBEDEDHTML)) {
                                drawEmbeddedHtmlRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, null);
                            } else if (type.equalsIgnoreCase(_MEDIATYPE_TEXT)) {

                                drawTextRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, null);
                            } else if (type.equalsIgnoreCase(_MEDIATYPE_TICKER)) {
                                drawWebViewTickerRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, null);
//
                            } else if (type.equalsIgnoreCase(_MEDIATYPE_URL_STREAM_VIDEO)) {
                                callBackTime = drawVideoViewStreamingRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, null);
//						}else if(type.equalsIgnoreCase(_MEDIATYPE_HDMI_STREAMING)) {
//							callBackTime = drawHDMIResetRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, regionCount);
                            } else if (type.equalsIgnoreCase(_MEDIATYPE_VIDEO)) {
                                callBackTime = drawVideoViewRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, null);
                            }
                        }
                        //layout.refreshDrawableState();
                    } catch (Exception e) {
                        failRefreshFlag = true;
                        showToastMessage("error in refresh Layout  " + e.getMessage());
                    }

//					sendMediaTimeRequest(mediaTimeDataSync);
                }

                try {
                    if (timerTaskHandler != null) {
                        timerTaskHandler.removeCallbacks(runnableTimerTask);
                        timerTaskHandler = null;
                    }
                    runnableTimerTask = new Runnable() {
                        @Override
                        public void run() {
                            if (mediaTimeDataSync != null) {
                                mediaTimeDataSync.setEndTime(LogUtility.getTimeStamp());
                                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeDataSync);

                            }
                            if (layoutTimeSync != null) {
                                layoutTimeSync.setEndTime(LogUtility.getTimeStamp());
                                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeSync);
//								sendLayoutTimeRequest(layoutTimeSync);
                            }
                            if (timerTaskHandler != null && runnableTimerTask != null)
                                timerTaskHandler.postDelayed(runnableTimerTask, 1000);
                        }
                    };
                    timerTaskHandler = new Handler();
                    timerTaskHandler.postDelayed(runnableTimerTask, 1500);

                } catch (Exception e) {
                }

                final DisplayLayout lyDisplay = ly;
                if (mediaCount == maxMediaCount - 1) {
//                    nextStartTime = syncMediaList.get(mediaCount).getEndTimeStamp() + delta;
                    nextStartTime = syncMediaList.get(mediaCount).getEndTimeStamp();
                    new SynMediaDataListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, syncMediaList.get(maxMediaCount - 1).getRegion());

                }
                mediaCount++;
                if (mediaCount == maxMediaCount) {
                    mediaCount = 0;
                    if (layoutTimeSync != null) {
                        layoutTimeSync.setEndTime(LogUtility.getTimeStamp());
                        DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeSync);
                        sendLayoutTimeRequest(layoutTimeSync);
                    }
                }

                final int nextMediaCount = mediaCount;
                if (failRefreshFlag) {
                    try {
                        if (handler != null) {
                            handler.removeCallbacks(runnable);
                            handler = null;
                        }
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                showToastMessage("RESTART ACTIVITY in refresh Layout  " + nextMediaCount);
                               updateMediaEndTimeOnServer(true);
                                failRefreshFlag = false;
                                nextStartTime = System.currentTimeMillis();
                                createUI();
                            }
                        };
                        handler = new Handler();
                        handler.post(runnable);

                    } catch (Exception e) {
                    }
                } else if (callBackTime > 0) {

                    try {
                        if (handler != null) {
                            handler.removeCallbacks(runnable);
                            handler = null;
                        }
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (lyDisplay != null) {
                                    if (mediaTimeDataSync != null) {
                                        String time = LogUtility.getTimeStamp();
                                        mediaTimeDataSync.setEndTime(time);
                                        DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeDataSync);
                                        sendMediaTimeRequest(mediaTimeDataSync);
                                    }
                                    showToastMessage("REFRESHA  Layout  " + nextMediaCount);
                                    refresh(lyDisplay, nextMediaCount);
                                }
                            }
                        };
                        handler = new Handler();
                        handler.postDelayed(runnable, callBackTime);

                    } catch (Exception e) {
                    }

                } else if (callBackTime == 0) {

                    try {
                        if (handler != null) {
                            handler.removeCallbacks(runnable);
                            handler = null;
                        }
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                if (lyDisplay != null) {
                                    if (mediaTimeDataSync != null) {
                                        String time = LogUtility.getTimeStamp();
                                        mediaTimeDataSync.setEndTime(time);
                                        DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeDataSync);
                                        sendMediaTimeRequest(mediaTimeDataSync);
                                    }
                                    showToastMessage("REFRESHA  Layout error " + nextMediaCount);
                                    refresh(lyDisplay, nextMediaCount);
                                }

                            }
                        };
                        handler = new Handler();
                        handler.post(runnable);

                    } catch (Exception e) {
                    }

                }
            }else{
                if (mediaCount == maxMediaCount - 1) {
                    nextStartTime = syncMediaList.get(mediaCount).getEndTimeStamp();
                    new SynMediaDataListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, syncMediaList.get(maxMediaCount - 1).getRegion());

                }
                mediaCount++;
                if(mediaCount  == maxMediaCount){
                    mediaCount = 0;
                }
                refresh(ly, mediaCount);
            }

		}catch (Exception e) {
		}
	}

	void refreshRegionLayout(DisplayLayout ly){
	int count = 0;
		for(Map.Entry<Integer, RefreshMediaData> regionMapObject : regionMediaMap.entrySet()){
			refreshRegionFromMap(ly,count);
			count++;
		}
	}

	@TargetApi(17)
	void refreshRegionLayout1(DisplayLayout ly){
		try{

			/// CALCULATE SCREEN SIZE
			float prodWidth = (float)ly.getWidth();//.floatValue();
			float prodHeight = (float)ly.getHeight();//.floatValue();
			int maxMediaCount = 0;
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			if(currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1){
				display.getRealSize(size);
			}else{
				display.getSize(size);
			}
			int layoutWidth = size.x;
			int layoutHeight = size.y;

//			float prodWidth = (float)ly.getWidth();//.floatValue();
//			float prodHeight = (float)ly.getHeight();//.floatValue();
//
			float widthMultiplierFactor = (float)layoutWidth/prodWidth;
			float heightMultiplierFactor = (float)layoutHeight/prodHeight;

			long callBackTime = 0;
			RelativeLayout parentLayout = (RelativeLayout) SignageDisplay.this.findViewById(R.id.relative_id);

			int regionCount = 0;
			long timeDiff = 0;
			int mediaCounterToDisplay= 0;
			ArrayList<RefreshMediaData> tempRefreshMediaList= new ArrayList<RefreshMediaData>();
			for(RefreshMediaData refreshMediaData : refreshMediaList){  //GET REGION
//				Date now  = new Date();
				long currentTime = System.currentTimeMillis();
				long  nextMediaTime = refreshMediaData.getTimestamp();
				currentTime = currentTime+500;
//				Log.i("currentTime", "" + currentTime);
				Log.i("nextMediaTime", "" + nextMediaTime);

				DisplayLayout.Region region = refreshMediaData.getRegion();
				maxMediaCount = (region.getMedia()).size();
				mediaCounterToDisplay = refreshMediaData.getMediaCount()+1;
				if(mediaCounterToDisplay>=maxMediaCount){
					mediaCounterToDisplay = 0;
				}
				////////////////////////Region height/width is scaled as per the display/layout ratio///////////////////////////////////
				int regionWidth =(int)Math.floor(region.getWidth());
				int regionHeight = (int)Math.floor(region.getHeight());
				int layoutMarginLeft = (int)Math.floor(region.getLeft());
				int layoutMarginTop =  (int)Math.floor(region.getTop());
				try{
					float newRegionWidth = regionWidth*widthMultiplierFactor;
					float newRegionHeight = regionHeight*heightMultiplierFactor;
					float newLayoutMarginLeft = layoutMarginLeft*widthMultiplierFactor;
					float newLayoutMarginTop = layoutMarginTop*heightMultiplierFactor;
					regionWidth = (int)newRegionWidth;
					regionHeight = (int)newRegionHeight;
					layoutMarginLeft = (int)newLayoutMarginLeft;
					layoutMarginTop = (int)newLayoutMarginTop;
				}catch(NumberFormatException e){

				}catch(Exception e){

				}



				DisplayLayout.Media media =  (region.getMedia()).get(mediaCounterToDisplay);
//				String id = (_MEDIA+"_"+media.getId());
				String type = media.getType();
				if(type.equalsIgnoreCase(_MEDIATYPE_URL_STREAM_VIDEO)){
					if(!videoRefresh){
						if(nextMediaTime>currentTime){
							timeDiff = nextMediaTime - currentTime;
							if(callBackTime==0){
								callBackTime = timeDiff;
							}else if(timeDiff < callBackTime){
								callBackTime = timeDiff;
							}
							Log.i("callBackTime ", "" + callBackTime);
//					Log.i("mediaData ", "" + mediaData.getScheduledDuration());
							tempRefreshMediaList.add(refreshMediaData);
							regionCount++;
							continue;
						}
					}
				}else{
					if(nextMediaTime>currentTime){
						timeDiff = nextMediaTime - currentTime;
						if(callBackTime==0){
							callBackTime = timeDiff;
						}else if(timeDiff < callBackTime){
							callBackTime = timeDiff;
						}
						tempRefreshMediaList.add(refreshMediaData);
						regionCount++;
						continue;
					}
				}

				refreshMediaData.setMediaCount(mediaCounterToDisplay);
				MediaTimeData oldMedia = refreshMediaData.getMediaTimeData();
				if(oldMedia != null && !oldMedia.getId().isEmpty()) {
					oldMedia.setEndTime(LogUtility.getTimeStamp());

					DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(oldMedia);
					sendMediaTimeRequest(oldMedia);
					if(layoutTimeSync!= null) {
						layoutTimeSync.setEndTime(LogUtility.getTimeStamp());
						DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeSync);
						sendLayoutTimeRequest(layoutTimeSync);
					}
				}
				RelativeLayout v = (RelativeLayout)parentLayout.findViewById(regionCount);
				long nextCallBack = System.currentTimeMillis();
				currentTime = System.currentTimeMillis();
				int duration = Integer.parseInt(media.getDuration());
				if(!media.getDuration().isEmpty()){
					nextCallBack =nextCallBack +(duration*1000)+250;
				}
				refreshMediaData.setTimestamp(nextCallBack);
				timeDiff = nextCallBack-currentTime;
				if(timeDiff<callBackTime){
					callBackTime = timeDiff;
				}

				if(mediaCounterToDisplay >= maxMediaCount){

				}else{
					try{
						boolean flagAction = true;
//						if(mediaCounterToDisplay ==0 && v!=null && maxMediaCount == 1 ){
//							//DO NOTHING*** To be discussed
//							flagAction = false;
//						}
						if(mediaCounterToDisplay ==0 && v!=null && maxMediaCount == 1 && (type.equalsIgnoreCase(_MEDIATYPE_IMAGE) || type.equalsIgnoreCase(_MEDIATYPE_TEXT)|| type.equalsIgnoreCase(_MEDIATYPE_TICKER)) ){
							//DO NOTHING*** To be discussed
							flagAction = false;
							if (oldMedia != null && !oldMedia.getId().isEmpty()) {
								setAndSendMediaInfoToServer(oldMedia.getMediaId(), oldMedia.getScheduledDuration(), refreshMediaData);
							}
						}
						if(flagAction){
							if(v!=null){
								try{
									parentLayout.removeViewAt(regionCount);

//								((RelativeLayout)v.getParent()).removeView(v);
								}catch(Exception e){
//									e.printStackTrace();
								}
							}
							if(type.equalsIgnoreCase(_MEDIATYPE_IMAGE)){
								callBackTime = drawImageViewRegion(parentLayout,regionWidth,regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_EMBEDEDHTML)){
								drawEmbeddedHtmlRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_TEXT)){
								drawTextRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_TICKER)){
								drawWebViewTickerRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, refreshMediaData);
							} else if (type.equalsIgnoreCase(_MEDIATYPE_WEBPAGE)){
								drawWebViewRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, refreshMediaData);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_URL_STREAM_VIDEO)){
								callBackTime = drawVideoViewStreamingRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, refreshMediaData);
//							}else if(type.equalsIgnoreCase(_MEDIATYPE_HDMI_STREAMING)){
//								callBackTime = drawHDMIResetRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, (int)callBackTime, regionCount);
							}else if(type.equalsIgnoreCase(_MEDIATYPE_VIDEO)){
								callBackTime = drawVideoViewRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, (int)callBackTime, refreshMediaData);
							}
						}
						//layout.refreshDrawableState();
					}catch (Exception e) {
						failRefreshFlag = true;
						showToastMessage("error in region Layout  " + e.getMessage());
//						Toast.makeText(this, "error in refresh Layout  "+e.getMessage(),Toast.LENGTH_SHORT).show();

					}

//					sendMediaTimeRequest(mediaTimeObj);
				}
//				if(counnnntt== 1)
				regionCount++;
//				try {
//					if(timerTaskHandler != null){
//						timerTaskHandler.removeCallbacks(runnableTimerTask);
//						timerTaskHandler = null;
//						}
//					 runnableTimerTask = new Runnable() {
//				       	   @Override
//				       	   public void run() {
//							if(mediaTimeObj !=null){
//								mediaTimeObj.setLongitude(LogUtility.getTimeStamp());
////								DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeObj);
//
//							}
//							if(layoutTimeObj !=null){
//								layoutTimeObj.setLongitude(LogUtility.getTimeStamp());
////								DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeObj);
////								sendLayoutTimeRequest(layoutTimeObj);
//							}
//							if(timerTaskHandler != null && runnableTimerTask != null)
//								timerTaskHandler.postDelayed(runnableTimerTask, 1000);
//				       	   }
//				       	};
//				       	timerTaskHandler =  new Handler();
//				       	timerTaskHandler.postDelayed(runnableTimerTask, 1500);

//				}catch (Exception e) {
//				}
				tempRefreshMediaList.add(refreshMediaData);
			}
			refreshMediaList = tempRefreshMediaList;
//			backGroundRefresh = false;
//			scheduleRefresh = false;
			final DisplayLayout lyDisplay = ly;

//			mediaCounterToDisplay++;
//			if(mediaCounterToDisplay  == maxMediaCount){
//				mediaCounterToDisplay = 0;
//				layoutTimeObj.setLongitude(LogUtility.getTimeStamp());
////				DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeObj);
////				sendLayoutTimeRequest(layoutTimeObj);
//			}

			if(failRefreshFlag){
				try {
					if(handler != null){
						handler.removeCallbacks(runnable);
						handler = null;
					}
					runnable = new Runnable() {
                        @Override
                        public void run() {
//							scheduleRefresh = true;
                            showToastMessage("RESTART ACTIVITY in region Layout  ");

//							Utility.writeLogFile("RESTART ACTIVITY in region Layout  ");
                            failRefreshFlag = false;
							updateMediaEndTimeOnServer(true);
                            nextStartTime = System.currentTimeMillis();
                            createUI();
                        }
                    };
					handler =  new Handler();
					handler.post(runnable);

				}catch (Exception e) {
				}
			}else if(callBackTime>0){

				try {
					if(handler != null){
						handler.removeCallbacks(runnable);
						handler = null;
					}
					runnable = new Runnable() {
						@Override
						public void run() {
//							scheduleRefresh = true;
//							showToastMessage("refreshRegionLayout in Region Layout  ");
							if(lyDisplay !=null){
//								mediaTimeObj.setLongitude(LogUtility.getTimeStamp());
//								DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeObj);
//								sendMediaTimeRequest(mediaTimeObj);
								refreshRegionLayout(lyDisplay);
							}
						}
					};
					handler =  new Handler();
					handler.postDelayed(runnable, callBackTime);
//					showToastMessage("Region Layout callBackTime " + callBackTime);
				}catch (Exception e) {
					showToastMessage("Region Layout callBack error "+callBackTime);
				}

			}else if(callBackTime==0){

				try {
					if(handler != null){
						handler.removeCallbacks(runnable);
						handler = null;
					}
					runnable = new Runnable() {
						@Override
						public void run() {
//							scheduleRefresh = true;
							if(lyDisplay !=null){
								showToastMessage("Region Layout callBack zero ");
								refreshRegionLayout(lyDisplay);
							}
						}
					};
					handler =  new Handler();
					handler.post(runnable);

				}catch (Exception e) {
				}

			}



		}catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "error == in refreshRegionLayout"+e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@TargetApi(17)
	void refreshRegionFromMap(DisplayLayout ly, final int regionCount) {
		try {

			/// CALCULATE SCREEN SIZE
			float prodWidth = (float) ly.getWidth();//.floatValue();
			float prodHeight = (float) ly.getHeight();//.floatValue();
			int maxMediaCount = 0;

            nextLayoutchange = false;
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			if(currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1){
				display.getRealSize(size);
			}else{
				display.getSize(size);
			}
			int layoutWidth = size.x;
			int layoutHeight = size.y;
			int viewId = 0;
//
			float widthMultiplierFactor = (float) layoutWidth / prodWidth;
			float heightMultiplierFactor = (float) layoutHeight / prodHeight;

			long callBackTime = 0;
			RelativeLayout parentLayout = (RelativeLayout) SignageDisplay.this.findViewById(R.id.relative_id);

			long timeDiff = 0;
			int mediaCounterToDisplay = 0;
			final RefreshMediaData refreshMediaData = regionMediaMap.get((Integer) regionCount);
			if (refreshMediaData == null) {
				Log.i(" error", "refreshMediaData = null");
				return;
			}
			viewId = refreshMediaData.getViewId();
			long currentTime = System.currentTimeMillis();
			long nextMediaTime = refreshMediaData.getTimestamp();
//			currentTime = currentTime + 250;
			Log.i("viewId", "" + viewId + " regionCount" + regionCount);
			if (nextMediaTime > currentTime) {
				timeDiff = nextMediaTime - currentTime;
				if (callBackTime == 0) {
					callBackTime = timeDiff;
				} else if (timeDiff < callBackTime) {
					callBackTime = timeDiff;
				}
			} else {
				Log.i("Drawing region ", "" + regionCount);
				DisplayLayout.Region region = refreshMediaData.getRegion();
				maxMediaCount = (region.getMedia()).size();
				mediaCounterToDisplay = refreshMediaData.getMediaCount() + 1;
				if (mediaCounterToDisplay >= maxMediaCount) {
					mediaCounterToDisplay = 0;
				}
				refreshMediaData.setMediaCount(mediaCounterToDisplay);

				////////////////////////Region height/width is scaled as per the display/layout ratio///////////////////////////////////
				int regionWidth = (int) Math.floor(region.getWidth());
				int regionHeight = (int) Math.floor(region.getHeight());
				int layoutMarginLeft = (int) Math.floor(region.getLeft());
				int layoutMarginTop = (int) Math.floor(region.getTop());
				try {
					float newRegionWidth = regionWidth * widthMultiplierFactor;
					float newRegionHeight = regionHeight * heightMultiplierFactor;
					float newLayoutMarginLeft = layoutMarginLeft * widthMultiplierFactor;
					float newLayoutMarginTop = layoutMarginTop * heightMultiplierFactor;
					regionWidth = (int) newRegionWidth;
					regionHeight = (int) newRegionHeight;
					layoutMarginLeft = (int) newLayoutMarginLeft;
					layoutMarginTop = (int) newLayoutMarginTop;
				} catch (NumberFormatException e) {

				} catch (Exception e) {

				}


				MediaTimeData oldMedia = refreshMediaData.getMediaTimeData();
				if (oldMedia != null && !oldMedia.getId().isEmpty()) {
					oldMedia.setEndTime(LogUtility.getTimeStamp());

					DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(oldMedia);
					sendMediaTimeRequest(oldMedia);
					if(layoutTimeSync!= null) {
						layoutTimeSync.setEndTime(LogUtility.getTimeStamp());
						DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeSync);
						sendLayoutTimeRequest(layoutTimeSync);
					}

				}
				DisplayLayout.Media media = (region.getMedia()).get(mediaCounterToDisplay);
//				String id = (_MEDIA+"_"+media.getId());
				String type = media.getType();
				RelativeLayout v = (RelativeLayout) parentLayout.findViewById(viewId);
				long nextCallBack = System.currentTimeMillis();
				currentTime = System.currentTimeMillis();
				int duration = Integer.parseInt(media.getDuration());
				if (!media.getDuration().isEmpty()) {
					nextCallBack = nextCallBack + (duration * 1000);
				}
				refreshMediaData.setTimestamp(nextCallBack);
				timeDiff = nextCallBack - currentTime;
				if (timeDiff < callBackTime) {
					callBackTime = timeDiff;
				}

				if (mediaCounterToDisplay >= maxMediaCount) {

				} else {
					try {
						boolean flagAction = true;
						if (mediaCounterToDisplay == 0 && v != null && maxMediaCount == 1 && (type.equalsIgnoreCase(_MEDIATYPE_IMAGE) || type.equalsIgnoreCase(_MEDIATYPE_TEXT) || type.equalsIgnoreCase(_MEDIATYPE_TICKER))) {
							flagAction = false;
							Log.i("single media ", "" + regionCount + " maxMediaCount " + maxMediaCount + "  mediaCounterToDisplay " + mediaCounterToDisplay + " type " + type);
							if (oldMedia != null && !oldMedia.getId().isEmpty()) {
								setAndSendMediaInfoToServer(oldMedia.getMediaId(), oldMedia.getScheduledDuration(), refreshMediaData);
							}
						}
						if (flagAction) {
							if (v != null) {
								try {
									ViewGroup parent = (ViewGroup) v.getParent();
									parent.removeView(v);
								} catch (Exception e) {
								}
							}
							if (type.equalsIgnoreCase(_MEDIATYPE_IMAGE)) {
								callBackTime = drawImageViewRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, refreshMediaData);
							} else if (type.equalsIgnoreCase(_MEDIATYPE_EMBEDEDHTML)) {
								drawEmbeddedHtmlRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, refreshMediaData);
							} else if (type.equalsIgnoreCase(_MEDIATYPE_TEXT)) {
								drawTextRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, refreshMediaData);
							} else if (type.equalsIgnoreCase(_MEDIATYPE_TICKER)) {
								drawWebViewTickerRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, refreshMediaData);
							} else if (type.equalsIgnoreCase(_MEDIATYPE_WEBPAGE)) {
								drawWebViewRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, refreshMediaData);
							} else if (type.equalsIgnoreCase(_MEDIATYPE_URL_STREAM_VIDEO)) {
								callBackTime = drawVideoViewStreamingRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, refreshMediaData);
//							}else if(type.equalsIgnoreCase(_MEDIATYPE_HDMI_STREAMING)){
//								callBackTime = drawHDMIResetRegion(parentLayout,regionWidth,regionHeight,layoutMarginLeft,layoutMarginTop,media, (int)callBackTime, regionCount);
							} else if (type.equalsIgnoreCase(_MEDIATYPE_VIDEO)) {
								callBackTime = drawVideoViewRegion(parentLayout, regionWidth, regionHeight, layoutMarginLeft, layoutMarginTop, media, (int) callBackTime, refreshMediaData);
							}
						}
					} catch (Exception e) {
						failRefreshFlag = true;

						showToastMessage("error in refreshRegionFromMap ()  " + e.getMessage());
					}

				}

			}
			final DisplayLayout lyDisplay = ly;
			Handler callBackHandler = refreshMediaData.getCallbackHandler();
			Runnable callBackRunnable = refreshMediaData.getCallbackRunnable();

			if (failRefreshFlag) {
				try {
					if (callBackHandler != null) {
						callBackHandler.removeCallbacks(callBackRunnable);
						callBackHandler = null;
					}
					callBackRunnable = new Runnable() {
						@Override
						public void run() {
							showToastMessage("RESTART ACTIVITY in refreshRegionFromMap() ");
							failRefreshFlag = false;
							updateMediaEndTimeOnServer(true);
                            nextStartTime = System.currentTimeMillis();
							createUI();
						}
					};
					callBackHandler = new Handler();
					callBackHandler.post(callBackRunnable);
					refreshMediaData.setCallbackRunnable(callBackRunnable);
					refreshMediaData.setCallbackHandler(callBackHandler);

				} catch (Exception e) {
				}
			} else if (callBackTime > 0) {

				try {
					if (callBackHandler != null) {
						callBackHandler.removeCallbacks(callBackRunnable);
						callBackHandler = null;
					}
					callBackRunnable = new Runnable() {
						@Override
						public void run() {
							if (lyDisplay != null) {
								refreshRegionFromMap(lyDisplay, regionCount);
							}
						}
					};
					callBackHandler = new Handler();
					callBackHandler.postDelayed(callBackRunnable, callBackTime);
					refreshMediaData.setCallbackRunnable(callBackRunnable);
					refreshMediaData.setCallbackHandler(callBackHandler);
				} catch (Exception e) {
				}

			} else if (callBackTime == 0) {

				try {
					if (callBackHandler != null) {
						callBackHandler.removeCallbacks(callBackRunnable);
						callBackHandler = null;
					}
					callBackRunnable = new Runnable() {
						@Override
						public void run() {
							if (lyDisplay != null) {
								showToastMessage("Region Layout callBack zero refreshRegionFromMap()");

								refreshRegionFromMap(lyDisplay, regionCount);
							}
						}
					};
					callBackHandler = new Handler();
					refreshMediaData.setCallbackRunnable(callBackRunnable);
					refreshMediaData.setCallbackHandler(callBackHandler);
					callBackHandler.post(callBackRunnable);
				} catch (Exception e) {
				}

			}

			regionMediaMap.put(regionCount, refreshMediaData);
		} catch (Exception e) {
		}
	}

	void updateMediaEndTimeOnServer(boolean updateServerFlag ){
		int count = 0;
		if(regionMediaMap != null) {
			for (Map.Entry<Integer, RefreshMediaData> regionMapObject : regionMediaMap.entrySet()) {
				RefreshMediaData refreshMediaData = regionMapObject.getValue();
				MediaTimeData oldMedia = refreshMediaData.getMediaTimeData();
				if (oldMedia != null && !oldMedia.getId().isEmpty()) {
					oldMedia.setEndTime(LogUtility.getTimeStamp());
					if (layoutTimeSync != null) {
						layoutTimeSync.setEndTime(LogUtility.getTimeStamp());
						DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeSync);
					}
					DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(oldMedia);
					if (updateServerFlag) {
						sendMediaTimeRequest(oldMedia);
						sendLayoutTimeRequest(layoutTimeSync);
					}
				}
				count++;
			}
		}else{
            if (layoutTimeSync != null) {
                layoutTimeSync.setEndTime(LogUtility.getTimeStamp());
                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveLayoutTimeData(layoutTimeSync);
            }
            if (mediaTimeDataSync != null) {
                String time = LogUtility.getTimeStamp();
                mediaTimeDataSync.setEndTime(time);
                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeDataSync);

            }
            if (updateServerFlag) {
                sendMediaTimeRequest(mediaTimeDataSync);
                sendLayoutTimeRequest(layoutTimeSync);
            }
        }
	}


	/**
	 * Adds WebView to the parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
	 */
	private void drawWebViewRegion(RelativeLayout parentLayout,int regionWidth,int regionHeight, int layoutMarginLeft, int layoutMarginTop,DisplayLayout.Media media, RefreshMediaData refreshMediaData) {
		List<MediaOptions> mediaOptionList = media.getMedia();
		MediaOptions mediaOption = mediaOptionList.get(0);
		String uri = mediaOption.getUri();
		try {
			uri = new java.net.URI(uri).getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
//		openLinkInChrome(uri);
		RelativeLayout web_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.child_web_layout, parentLayout, false);
        int viewId = getViewId();
        web_layout.setId(viewId);
        refreshMediaData.setViewId(viewId);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(regionWidth, regionHeight);
		param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);
		web_layout.setLayoutParams(param);
		WebView web = (WebView) web_layout.findViewById(R.id.web_view);
		web.loadUrl(uri);
		//web.refreshDrawableState();
		web.setBackgroundColor(getResources().getColor(android.R.color.black));
		parentLayout.addView(web_layout);
		setAndSendMediaInfoToServer(uri, media.getDuration(), refreshMediaData);
	}

	/**
	 * Adds webView to include Embedded html component to parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
	 * @param refreshMediaData
	 */
	private void drawEmbeddedHtmlRegion(RelativeLayout parentLayout,int regionWidth,int regionHeight, int layoutMarginLeft, int layoutMarginTop,DisplayLayout.Media media,RefreshMediaData refreshMediaData) {
		List<MediaOptions> mediaOptionList =  media.getMedia();
		MediaOptions mediaOption = mediaOptionList.get(0);


		DisplayLayout.Raw raw = media.getRaw().get(0);
		String embedHtml = raw.getEmbedHtml();
		String embedScript = raw.getEmbedScript();
		String embedStyle = raw.getEmbedStyle();
		String htmlText = "<html><head>"+embedStyle+"</head><body>"+embedHtml+"</body>"+embedScript+"</html>";

		RelativeLayout web_layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.child_web_layout, parentLayout, false);
        int viewId = getViewId();
        web_layout.setId(viewId);
        refreshMediaData.setViewId(viewId);
		RelativeLayout.LayoutParams param =  new RelativeLayout.LayoutParams(regionWidth,regionHeight);
		param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);
		web_layout.setLayoutParams(param);
		WebView web = (WebView) web_layout.findViewById(R.id.web_view);
		web.setBackgroundColor(getResources().getColor(android.R.color.black));
		if(web!=null){
			web.loadData(htmlText, "text/html; charset=UTF-8", null);
		}
		//web.refreshDrawableState();
		parentLayout.addView(web_layout);
	}

	/**
	 * Adds Ticker region to the parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
     * @param refreshMediaData
	 */
	private void drawWebViewTickerRegion(RelativeLayout parentLayout, int regionWidth, int regionHeight, int layoutMarginLeft, int layoutMarginTop, DisplayLayout.Media media, RefreshMediaData refreshMediaData) {
		List<MediaOptions> mediaOptionList =  media.getMedia();
		MediaOptions mediaOption = mediaOptionList.get(0);
		String scrollSpeed = mediaOption.getScrollSpeed();
		String direction = mediaOption.getDirection();
		String speed = mediaOption.getSpeed();
		String textDirection = mediaOption.getTextDirection();

		String uri = mediaOption.getUri();
		if(scrollSpeed==null || (Integer.parseInt(scrollSpeed) <0)){
			if(speed!=null && (Integer.parseInt(speed) >0)){
				scrollSpeed = speed;
			}else{
				scrollSpeed = "0";
			}
		}

		if(direction==null){
			if(textDirection!= null){
				if(textDirection.equalsIgnoreCase("ltr")||textDirection.toLowerCase().contains("right")){
					direction = "RIGHT";
				}else{
					direction = "LEFT";
				}
			}else{
				direction = "NONE";
				scrollSpeed = "0";
			}
		}
		DisplayLayout.Raw raw = media.getRaw().get(0);
		String template = raw.getTemplate();
//								String htmlText = "<html><body bgcolor='#00000'><marquee behavior='scroll' direction="+direction+" scrollamount="+scrollSpeed+">"+template+"</MARQUEE></body></html>";
		String htmlText ="<html><head><script type='text/javascript'>document.write(\\x3Cscript type=text/javascript src='' + (https: == document.location.protocol ? https:// : http://) + feed.mikle.com/js/rssmikle.js>\\x3C/script>\");</script><script type='text/javascript'>(function() {var params = {rssmikle_url: 'http://indiatoday.intoday.in/rss/homepage-topstories.jsp',rssmikle_frame_width: '300',rssmikle_frame_height: '400',frame_height_by_article: '0',rssmikle_target: '_blank',rssmikle_font: 'Arial, Helvetica, sans-serif',rssmikle_font_size: '12',rssmikle_border: 'off',responsive: 'off',rssmikle_css_url: ,text_align:'left',text_align2: 'left',corner: 'off',scrollbar: 'on',autoscroll: 'on',scrolldirection: 'up',scrollstep: '3',mcspeed: '20',sort: 'Off',rssmikle_title: 'on',rssmikle_title_sentence: '',rssmikle_title_link: 'http://http%3A%2F%2Findiatoday.intoday.in%2Frss%2Fhomepage-topstories.jsp',rssmikle_title_bgcolor: '#0066FF',rssmikle_title_color: '#FFFFFF',rssmikle_title_bgimage: '',rssmikle_item_bgcolor: '#FFFFFF',rssmikle_item_bgimage: '',rssmikle_item_title_length:'55',rssmikle_item_title_color: '#0066FF',rssmikle_item_border_bottom: 'on',rssmikle_item_description: 'on',item_link:'off',rssmikle_item_description_length: '150',rssmikle_item_description_color: '#666666',rssmikle_item_date: 'gl1',rssmikle_timezone: 'Etc/GMT',datetime_format: '%b %e, %Y %l:%M:%S %p',item_description_style: 'text+tn',item_thumbnail: 'full',item_thumbnail_selection: 'auto',article_num: '15',rssmikle_item_podcast: 'off',keyword_inc: '',keyword_exc: ''};feedwind_show_widget_iframe(params);})();</script><div style='font-size:10px; text-align:center; width:300px;'><a href='http://feed.mikle.com/' target='_blank' style='color:#CCCCCC;'>RSS Feed Widget</a></div></head><body bgcolor='#00000'><marquee behavior='scroll' direction=NONE scrollamount=0><p style='font-family: Arial, Verdana, sans-serif;'><span style='font-size:48px;'><span style='color: #ffffff;'><strong>[Title]</strong></span></span></p></MARQUEE></body></html>";
		RelativeLayout web_layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.child_web_layout, parentLayout, false);
        int viewId = getViewId();
        web_layout.setId(viewId);
        refreshMediaData.setViewId(viewId);
		RelativeLayout.LayoutParams param =  new RelativeLayout.LayoutParams(regionWidth,regionHeight);
		param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);
		web_layout.setLayoutParams(param);
		WebView web = (WebView) web_layout.findViewById(R.id.web_view);
//		web.setId(regionCount);
		web.setBackgroundColor(getResources().getColor(android.R.color.black));
		if(web!=null && template!=null){
			web.loadData(htmlText, "text/html; charset=UTF-8", null);
		}
		setAndSendMediaInfoToServer("Ticker", media.getDuration(), refreshMediaData);
		//web.refreshDrawableState();
		parentLayout.addView(web_layout);
	}

	/**
	 * Add Text ticker region to parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
	 */
	private void drawTextRegion(RelativeLayout parentLayout,int regionWidth,int regionHeight, int layoutMarginLeft, int layoutMarginTop,DisplayLayout.Media media, RefreshMediaData refreshMediaData) {
		List<MediaOptions> mediaOptionList =  media.getMedia();
		MediaOptions mediaOption = mediaOptionList.get(0);
		String scrollSpeed = mediaOption.getScrollSpeed();
		String direction = mediaOption.getDirection();
		String speed = mediaOption.getSpeed();
		String effect = mediaOption.getEffect();
		String textDirection = mediaOption.getTextDirection();
		if(scrollSpeed==null || (Integer.parseInt(scrollSpeed) <0)){
			if(speed!=null && (Integer.parseInt(speed) >0)){
				scrollSpeed = speed;
			}else{
				scrollSpeed = "0";
			}
		}

		if(direction==null){
			if(textDirection!= null){
				if(textDirection.equalsIgnoreCase("ltr")||textDirection.toLowerCase(Locale.ENGLISH).contains("right")){
					direction = "right";
				}else{
					direction = "left";
				}
			}else if (effect != null){
				if(effect.toLowerCase(Locale.ENGLISH).contains("right")){
					direction = "RIGHT";
				}else if(effect.toLowerCase(Locale.ENGLISH).contains("left")){
					direction = "left";
				}else if(effect.toLowerCase(Locale.ENGLISH).contains("up")){
					direction = "up";
				}else if(effect.toLowerCase(Locale.ENGLISH).contains("down")){
					direction = "down";
				}else{
					direction = "none";
					scrollSpeed = "0";
				}
			}
		}
//								if(scrollSpeed==null || (Integer.parseInt(scrollSpeed) <0)){
//									scrollSpeed = "0";
//								}
//
//								if(direction==null){
//									direction = "NONE";
//								}
		DisplayLayout.Raw raw = media.getRaw().get(0);
		String text = raw.getText();
		String htmlText = "<html><body bgcolor='#000000'><marquee behavior='scroll' direction="+direction+" scrollamount="+scrollSpeed+" height=100% width=100%>"+text+"</MARQUEE></body></html>";

		RelativeLayout web_layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.child_web_layout, parentLayout, false);
        int viewId = getViewId();
        web_layout.setId(viewId);
        refreshMediaData.setViewId(viewId);
		RelativeLayout.LayoutParams param =  new RelativeLayout.LayoutParams(regionWidth,regionHeight);
		param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);
		web_layout.setLayoutParams(param);
		WebView web = (WebView) web_layout.findViewById(R.id.web_view);
//		web.setId(regionCount);
		web.setBackgroundColor(getResources().getColor(android.R.color.black));
		if(web!=null && text!=null){
			web.loadData(htmlText, "text/html; charset=UTF-8", null);
		}
		//web.refreshDrawableState();
		parentLayout.addView(web_layout);
//		web.bringToFront();
//		parentLayout.invalidate();
		setAndSendMediaInfoToServer("TEXT", media.getDuration(),refreshMediaData);
	}

	/**
	 * Adds ImageView Region to the parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
	 * @param callBackTime
	 * @return
	 */
	private int drawImageViewRegion(RelativeLayout parentLayout,int regionWidth,int regionHeight, int layoutMarginLeft, int layoutMarginTop,DisplayLayout.Media media, int callBackTime, RefreshMediaData refreshMediaData) {
		ImageView img = null;
		RelativeLayout img_layout = null;
		try {
			Bitmap bmp = null;

			img_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.child_image_layout, parentLayout, false);
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(regionWidth, regionHeight);
			param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);

			List<MediaOptions> mediaOptionList = media.getMedia();
			MediaOptions mediaOption = mediaOptionList.get(0);

			String scaleType = mediaOption.getScaleType();
//									String align = mediaOption.getAlign();
//									String vAlign = mediaOption.getValign();
			String transIn = mediaOption.getTransIn();
			String transInDuration = mediaOption.getTransInDuration();
			String name = mediaOption.getUri();
			img_layout.setLayoutParams(param);
            int viewId = getViewId();
            img_layout.setId(viewId);
			if(refreshMediaData != null)
           	 	refreshMediaData.setViewId(viewId);
			img = (ImageView) img_layout.findViewById(R.id.img_view);
			if (scaleType != null && scaleType.equalsIgnoreCase("center")) {
				img.setScaleType(ScaleType.FIT_CENTER);
			} else {
				img.setScaleType(ScaleType.FIT_XY);
			}
			if (img != null) {
				img.setImageBitmap(null);
				if (bmp != null) {
					img.setImageBitmap(bmp);
				} else {
					if (name == null) {
                        name="";
//                        failRefreshFlag = true;
//                        callBackTime = 0;
                    }else if( name.length() == 0){
//                        failRefreshFlag = true;
//                        callBackTime = 0;
                        name="";
                    }
				}
				String PATH = Environment.getExternalStorageDirectory() + AppState.DISPLAY_FOLDER + name;
				File imgFile = new File(PATH);
				if (imgFile.exists()) {
					bmp = Utility.decodeScaledBitmapFromSdCard(PATH, regionWidth, regionHeight);
					try {
						if (bmp != null) {
							if (transIn != null && !transIn.isEmpty() && transIn.equalsIgnoreCase("fadeIn")) {
								long transDuration = 1000;
								if (transInDuration != null && !transInDuration.isEmpty()) {
									transDuration = Long.parseLong(transInDuration);
								}
								setImageFadeInFadeOutAnimation(img, bmp, transDuration, 3000);
							} else {
								img.setImageBitmap(bmp);
							}
							previousBitmap = bmp;
						}
					} catch (OutOfMemoryError e) {
						showToastMessage("error in Image Out of memmory" + PATH);
					} catch (Exception e) {

					}
				} else {
                    callBackTime = 0;
					failRefreshFlag = false;
                    img.setImageBitmap(previousBitmap);
                    parentLayout.addView(img_layout);
                    img.requestFocus();
					showToastMessage("error in Image file not found" + PATH);
					LogData.getInstance().setDownloadPending(AppMain.getAppMainContext(), true);
				}
			}
			parentLayout.addView(img_layout);
				setAndSendMediaInfoToServer(name, media.getDuration(), refreshMediaData);
		}catch (Exception e){
			failRefreshFlag = false;
			callBackTime = 0;
            showToastMessage("error in drawImageViewRegion() " + e.getMessage());
            img.setImageBitmap(previousBitmap);
			parentLayout.addView(img_layout);
            img.requestFocus();
			e.printStackTrace();
		}

		return callBackTime;
	}

	/**
	 * Adds VideoView streaming Region to parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
	 * @param callBackTime
	 * @return
	 */
	private  int drawVideoViewStreamingRegion(RelativeLayout parentLayout, int regionWidth, int regionHeight, int layoutMarginLeft, int layoutMarginTop, DisplayLayout.Media media, int callBackTime, RefreshMediaData refreshMediaData){
		RelativeLayout video_layout = null;
		VideoView videoHolder =  null;
		String name = mediaUri;
		String mute= "0";
		try{
			video_layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.child_video_layout, parentLayout, false);
            int viewId = getViewId();
            video_layout.setId(viewId);

			if(refreshMediaData != null)
           	 refreshMediaData.setViewId(viewId);
			RelativeLayout.LayoutParams param =  new RelativeLayout.LayoutParams(regionWidth,regionHeight);
			param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);
			video_layout.setLayoutParams(param);

			videoHolder = (VideoView) video_layout.findViewById(R.id.video_view);
			try{
				List<MediaOptions> mediaOptionList =  media.getMedia();
				MediaOptions mediaOption = mediaOptionList.get(0);
				mute = mediaOption.getMute();
				name = mediaOption.getUri();
				name = new java.net.URI(name).getPath();
//                                        name = "rtsp://admin:orange@axislajpatnagar.selfip.com:554/cam/realmonitor?channel=1&subtype=1";
			}catch(Exception e){
				showToastMessage("error in Video mediaOptionList "+e.getMessage());
			}
			try{
				final String muteFlag = mute;
				if(name == null) {
                    failRefreshFlag = false;
                    callBackTime = 0;
                    name = mediaUri;
                    showToastMessage("error in Video data ");
                }else if( name.length()==0){
                    failRefreshFlag = false;
                    callBackTime = 0;
                    name = mediaUri;
				}else{
					mediaUri = name;
//					showToastMessage("mediaUri Video "+mediaUri);
				}

				videoHolder.setMediaController(null);
				final Uri video = Uri.parse(name);
				videoHolder.setVideoURI(video);
				videoHolder.requestFocus();
				final VideoView vv = videoHolder;
//				videoHolder.setOnInfoListener(new OnInfoListener() {
//
//					@Override
//					public boolean onInfo(MediaPlayer mp, int what, int extra) {
////
////													Log.i("VIDEO on onInfo", "what "+what +" extra "+extra);
//						return true;
//					}
//				});
				videoHolder.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
//													Log.i("VIDEO", "onPrepared "+video);
						//											preview.setVisibility(View.GONE);
//                                                    mp.setLooping(true);
						if (muteFlag != null && muteFlag.equalsIgnoreCase("1")) {
							mp.setVolume(0, 0);
						}
						vv.setVisibility(View.VISIBLE);
						vv.setZOrderOnTop(true);
						vv.requestFocus();
						vv.start();
						//											vv.start();
					}
				});
				videoHolder.setOnErrorListener(new OnErrorListener() {

					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						showToastMessage("VIDEO on Error what " + what + " extra " + extra);
						if (what == 1 && extra == -38) {

						}
						return true;
					}
				});
				parentLayout.addView(video_layout);
				videoHolder.setZOrderOnTop(true);
				videoHolder.requestFocus();
				videoHolder.start();
					setAndSendMediaInfoToServer(name, media.getDuration(), refreshMediaData);
			}catch(Exception e){
				failRefreshFlag = false;
				callBackTime = 0;
				showToastMessage("Vide oerror1 region "+name);

			}
		}catch(Exception e){
			failRefreshFlag = false;
			callBackTime = 0;

			showToastMessage("Vide oerror2 region " + name);
		}
		return callBackTime;
	}

	/**
	 * Adds VideoView streaming Region to parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
	 * @param callBackTime
	 * @param regionCount
	 * @return
	 */
//	private  int drawHDMIRegion(RelativeLayout parentLayout, int regionWidth, int regionHeight, int layoutMarginLeft, int layoutMarginTop, DisplayLayout.Media media, int callBackTime, int regionCount){
//		RelativeLayout hdmi_layout = null;
//
//		try {
//			hdmi_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.child_hdmi_layout, parentLayout, false);
//
//    int viewId = getViewId();
//    hdmi_layout.setId(viewId);
//    refreshMediaData.setViewId(viewId);
//			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(regionWidth, regionHeight);
//			param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);
//			hdmi_layout.setLayoutParams(param);
//
//			window = (SurfaceView) hdmi_layout.findViewById(R.id.window);
//			mEventHandler.sendMessage(mEventHandler
//					.obtainMessage(HDMI_IN_MESSAGE_CREATE));
//			hdmi_layout.setId(regionCount);
//			parentLayout.addView(hdmi_layout);
//		} catch (Exception e) {
//			failRefreshFlag = true;
//			callBackTime = 0;
////									Toast.makeText(this, "error in Video  "+e.getMessage(),2000).show();
//
//		}
//		return callBackTime;
//	}

	/**
	 * Adds HDMI RESET streaming Region to parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
	 * @param callBackTime
	 * @param regionCount
	 * @return
	 */
/*	private  int drawHDMIResetRegion(RelativeLayout parentLayout, int regionWidth, int regionHeight, int layoutMarginLeft, int layoutMarginTop, DisplayLayout.Media media, int callBackTime, int regionCount){
		RelativeLayout hdmi_layout = null;

		try {
			hdmi_layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.child_hdmi_layout, parentLayout, false);
			hdmi_layout.setId(regionCount);
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(regionWidth, regionHeight);
			param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);
			hdmi_layout.setLayoutParams(param);

			window = (SurfaceView) hdmi_layout.findViewById(R.id.window);
			mEventHandler.sendMessage(mEventHandler
					.obtainMessage(HDMI_IN_MESSAGE_PLUGON));
			hdmi_layout.setId(regionCount);
			parentLayout.addView(hdmi_layout);
		} catch (Exception e) {
			failRefreshFlag = false;
			callBackTime = 0;
//									Toast.makeText(this, "error in Video  "+e.getMessage(),2000).show();

		}
		return callBackTime;
	}
	*/

	/**
	 * Adds VideoView Region to the parent layout
	 * @param parentLayout
	 * @param regionWidth
	 * @param regionHeight
	 * @param layoutMarginLeft
	 * @param layoutMarginTop
	 * @param media
	 * @param callBackTime
	 * @return callbacktime
	 */
	private int drawVideoViewRegion(RelativeLayout parentLayout, int regionWidth, int regionHeight, int layoutMarginLeft, int layoutMarginTop, DisplayLayout.Media media, int callBackTime,RefreshMediaData refreshMediaData){

		RelativeLayout video_layout = null;
		VideoView videoHolder =  null;
		String name = mediaUri;
        int viewId;
		String mute= "0";
		try{
			video_layout = (RelativeLayout)getLayoutInflater().inflate(R.layout.child_video_layout, parentLayout, false);
//
			RelativeLayout.LayoutParams param =  new RelativeLayout.LayoutParams(regionWidth,regionHeight);
			param.setMargins(layoutMarginLeft, layoutMarginTop, 0, 0);
			video_layout.setLayoutParams(param);
            viewId = getViewId();
            video_layout.setId(viewId);

			if(refreshMediaData != null)
             refreshMediaData.setViewId(viewId);
			videoHolder = (VideoView) video_layout.findViewById(R.id.video_view);
			try{
				List<MediaOptions> mediaOptionList =  media.getMedia();
				MediaOptions mediaOption = mediaOptionList.get(0);
				mute = mediaOption.getMute();
				name = mediaOption.getUri();
			}catch(Exception e){

				showToastMessage("error in Video mediaOptionList "+e.getMessage());
			}
			try{
				final String muteFlag = mute;
				if(name == null){
                    name=mediaUri;
                    showToastMessage("error in Video data ");
                }else if( name.length() == 0){
                    name=mediaUri;
                    showToastMessage("error in Video data ");
                }else {
                     mediaUri = name;
                }

				String PATH = Environment.getExternalStorageDirectory()
						+ AppState.DISPLAY_FOLDER+mediaUri;
				final File file = new File(PATH);
				if(file.exists()){
					videoHolder.setVideoURI(null);
					videoHolder.setMediaController(null);
					final Uri video = Uri.parse(PATH);
					videoHolder.setVideoURI(video);
					videoHolder.requestFocus();
					final VideoView vv = videoHolder;
					videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mediaPlayer) {
							videoRefresh =true;
						}
					});
//					videoHolder.setOnInfoListener(new OnInfoListener() {
//
//						@Override
//						public boolean onInfo(MediaPlayer mp, int what, int extra) {
//							return true;
//						}
//					});
					videoHolder.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer mp) {
							videoRefresh = false;
							mp.setLooping(false);
							if (muteFlag != null && muteFlag.equalsIgnoreCase("1")) {
								mp.setVolume(0, 0);
							}
							vv.setVisibility(View.VISIBLE);
							vv.setZOrderOnTop(true);
							vv.requestFocus();
							vv.start();
							//											vv.start();
						}
					});
					videoHolder.setOnErrorListener(new OnErrorListener() {

						@Override
						public boolean onError(MediaPlayer mp, int what, int extra) {
							showToastMessage("VIDEO on Error what " + what + " extra " + extra);
							if (what == 1 && extra == -38) {
								try {

									showToastMessage("Video file deleting " + file.getName());
									file.delete();
									LogData.getInstance().setDownloadPending(AppMain.getAppMainContext(), true);
								} catch (Exception e) {
									showToastMessage("Error    Video file deleting");
								}
							}
							return true;
						}
					});
					parentLayout.addView(video_layout);
					videoHolder.setZOrderOnTop(true);
					videoHolder.requestFocus();
					videoHolder.start();

					 setAndSendMediaInfoToServer(name, media.getDuration(), refreshMediaData);
				}else{
					failRefreshFlag = false;
					callBackTime = 0;
					showToastMessage("Video file does not exist");
					LogData.getInstance().setDownloadPending(AppMain.getAppMainContext(),true);
				}
			}catch(Exception e){
				failRefreshFlag = false;
				callBackTime = 0;
				showToastMessage("error1 in drawVideoViewRegion()  " + e.getMessage());
			}
		}catch(Exception e){
			e.printStackTrace();
			failRefreshFlag = false;
			callBackTime = 0;
			showToastMessage("error2 in drawVideoViewRegion()  " + e.getMessage());
		}
		return callBackTime;
	}

	/**
	 * Fade in fade out Animation effect on Image
	 * @param imgView
	 * @param image
	 * @param fadeInDuration
	 * @param fadeOutDuration
	 */
	void   setImageFadeInFadeOutAnimation(final ImageView imgView,final Bitmap image,long fadeInDuration, long fadeOutDuration){
		final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		animationFadeIn.setDuration(fadeInDuration);
		final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		animationFadeOut.setDuration(fadeOutDuration);

		Animation.AnimationListener animListener = new Animation.AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				imgView.setImageBitmap(image);
				//            imgView.startAnimation(animationFadeIn);
			}
		};
		imgView.setImageBitmap(image);
		imgView.startAnimation(animationFadeIn);
		//    imgView.startAnimation(animationFadeOut);
		//    animationFadeOut.setAnimationListener(animListener);
	}

	void openLinkInChrome(String urlToOpen){
		try {
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
//			i.addCategory("android.intent.category.LAUNCHER");
//			i.setData(Uri.parse(urlToOpen));
//			startActivity(i);
			final String appPackageName = "com.android.chrome";
			Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(urlToOpen));
//            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
			if (intent != null) {

//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.setClassName(appPackageName,"Main");
//				intent.addCategory("android.intent.category.LAUNCHER");
//				intent.setComponent(new ComponentName(appPackageName, "com.android.chrome.Main"));
//				intent.setAction("android.intent.action.VIEW");
//				intent.addCategory("android.intent.category.BROWSABLE");
//                intent.setData(Uri.parse(urlToOpen));
				startActivity(intent);

			}
		}
		catch(ActivityNotFoundException e) {
			e.printStackTrace();
		}catch(Exception e) {
			// Chrome is probably not installed
		}
	}

	//	@Override
//	protected void onPause() {
//		try {
//			if(!AppState.BACKPRESSED_SIGNAGE_SCREEN){				
//				startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//			}
////			}else{
//				super.onPause();
////			}
//		} catch (Exception ex) {
//		}
//	}

	/**
	 * Sends current media information to server
	 * @param mediaName
	 * @param duration
	 */
	private void setAndSendMediaInfoToServer(String mediaName, String duration, RefreshMediaData refreshMediaDataObj) {
		if (refreshMediaDataObj != null){
			MediaTimeData mediaTimeObj = new MediaTimeData();
		mediaTimeObj.setId("");
		mediaTimeObj.setBoxId(ClientConnectionConfig._HARDWAREKEY);
		mediaTimeObj.setLayoutId(displayedLayout);
		mediaTimeObj.setStartTime(LogUtility.getTimeStamp());
		mediaTimeObj.setEndTime("");
		mediaTimeObj.setMediaId(mediaName);
		mediaTimeObj.setScheduledDuration(duration);
		String id = generateId(mediaTimeObj.getMediaId(), mediaTimeObj.getBoxId(), mediaTimeObj.getLayoutId());
		mediaTimeObj.setId(id);
		refreshMediaDataObj.setMediaTimeData(mediaTimeObj);
			DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeObj);
			sendMediaTimeRequest(mediaTimeObj);
	}else{
			mediaTimeDataSync.setId("");
			mediaTimeDataSync.setBoxId(ClientConnectionConfig._HARDWAREKEY);
			mediaTimeDataSync.setLayoutId(displayedLayout);
			mediaTimeDataSync.setStartTime(LogUtility.getTimeStamp());
			mediaTimeDataSync.setEndTime("");
			mediaTimeDataSync.setMediaId(mediaName);
			mediaTimeDataSync.setScheduledDuration(duration);
			String id = generateId(mediaTimeDataSync.getMediaId(), mediaTimeDataSync.getBoxId(), mediaTimeDataSync.getLayoutId());
			mediaTimeDataSync.setId(id);
			DataCacheManager.getInstance(AppMain.getAppMainContext()).saveMediaTimeData(mediaTimeDataSync);
			sendMediaTimeRequest(mediaTimeDataSync);
		}
	}

	private void sendMediaUpdate(){
		try {
			if(timerTaskMediaUpdate != null){
				timerTaskMediaUpdate.removeCallbacks(runnableMediaUpdate);
				timerTaskMediaUpdate = null;
			}
			runnableMediaUpdate = new Runnable() {
				@Override
				public void run() {
					if(LogUtility.checkNetwrk(AppMain.getAppMainContext())){
						try {
							syncSavedListMediaLayoutData();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(timerTaskMediaUpdate != null && runnableMediaUpdate != null)
						timerTaskMediaUpdate.postDelayed(runnableMediaUpdate, 120000);
				}
			};
			timerTaskMediaUpdate =  new Handler();
			timerTaskMediaUpdate.postDelayed(runnableMediaUpdate,5000);

		}catch (Exception e) {
		}
	}

	@Override
	public void onBackPressed() {
		AppState.BACKPRESSED_SIGNAGE_SCREEN = true;
		ClientConnectionConfig.LAST_REQUEST_DATA = "";
		try{
			if(objXXiboServerGetFiles!=null)
				objXXiboServerGetFiles.cancel(false);
		}catch (Exception e) {
		}
		removeAllHandlers();
		super.onBackPressed();
//		finish();
		Intent i = new Intent(SignageDisplay.this, AppMain.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

	@TargetApi(17)
	private int getViewId(){
		int returnViewId = 5;
		if(currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1){
			returnViewId = View.generateViewId();
		}else{
			returnViewId = viewId++;
		}
		return returnViewId;
	}

	void removeAllHandlers(){
		try{
			if(handler != null){
				handler.removeCallbacks(runnable);
			}
			if(timerTaskHandler != null){
				timerTaskHandler.removeCallbacks(runnableTimerTask);
			}
			if(timerTaskMediaUpdate != null){
				timerTaskMediaUpdate.removeCallbacks(runnableMediaUpdate);
			}
			if(refreshHandler != null){
				refreshHandler.removeCallbacks(refreshRunnable);
			}
			if(refreshHandler != null){
				refreshHandler.removeCallbacks(nextLayoutRunnable);
			}
			if(currentFileHandler != null){
				currentFileHandler.removeCallbacks(currentFileRunnble);
			}
		}catch(Exception e){

		}
	}
	@Override
	protected void onPause() {
		try {
//			if(!cancelPopup){				
//				initiatePopupWindow();
//			}
			LogData.getInstance().setAppStatus(AppMain.getAppMainContext(), LogData.STB_ON);
//			DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_APP_STATE, LogData.STB_ON);
			AppState.ACTION_ON_PAUSE= true;
			super.onPause();
		} catch (Exception ex) {
		}
	}
	@Override
	protected void onResume() {
		try {
//			if(!cancelPopup){				
//				initiatePopupWindow();
//			}
			LogData.getInstance().setAppStatus(AppMain.getAppMainContext(), LogData.APP_ON);
			super.onResume();
		} catch (Exception ex) {
		}
	}
	@Override
	protected void onRestart() {
		try {
//			if(!cancelPopup){				
//				initiatePopupWindow();
//			}
			LogData.getInstance().setAppStatus(AppMain.getAppMainContext(), LogData.APP_ON);
			super.onRestart();
		} catch (Exception ex) {
		}
	}

	private void sendMediaTimeRequest(MediaTimeData mediaTimeObj){
		if(AppState.BACKPRESSED_SIGNAGE_SCREEN)
			return;
		String id ="";
		if(mediaTimeObj != null){
			id = mediaTimeObj.getId();
			if(id.isEmpty()){
				id = generateId(mediaTimeObj.getMediaId(),mediaTimeObj.getBoxId(),mediaTimeObj.getLayoutId());
				mediaTimeObj.setId(id);
			}
			mediaTimeDataSync.setId(id);
			if(checkNetwork()){
				final String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_MEDIA_TIME);
				JSONObject jsonRequest = null;
				ObjectMapper mapper = new ObjectMapper();
				String jsonRequestString = null;
				try {
					jsonRequestString = mapper.writeValueAsString(mediaTimeObj);
					Log.i(" MediaTimeData ", jsonRequestString +"\n"+mediaTimeObj.getEndTime());
				} catch (IOException e) {
				}
				try {
					jsonRequest = new JSONObject(jsonRequestString);
				} catch (JSONException e) {
				}
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
							ObjectMapper mapper = new ObjectMapper();
							MediaTimeData obj = null;
							try {
								obj = mapper.readValue(response.toString(), MediaTimeData.class);
							} catch (IOException e) {
								e.printStackTrace();
							}
							String deviceId = obj.getId();
							String endTime = obj.getEndTime();
							if (!endTime.isEmpty()) {
								DataCacheManager.getInstance(AppMain.getAppMainContext()).removeMediaTimeById(deviceId);
							}

//						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null) {
							Log.e("VolleyError ", error.toString());
						}
					}
				});
				VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);
			}
		}
	}

	private void sendLayoutTimeRequest(LayoutTimeData mediaTimeObj){
		if(AppState.BACKPRESSED_SIGNAGE_SCREEN)
			return;
		String id ="";
		if(mediaTimeObj != null){
			id = mediaTimeObj.getId();
			if(id.isEmpty()){
				id = generateId("",mediaTimeObj.getBoxId(),mediaTimeObj.getLayoutId());
				mediaTimeObj.setId(id);
			}
			if(checkNetwork()){
				final String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_LAYOUT_TIME);
				JSONObject jsonRequest = null;
				ObjectMapper mapper = new ObjectMapper();
				String jsonRequestString = null;
				try {
					jsonRequestString = mapper.writeValueAsString(mediaTimeObj);
				} catch (IOException e) {
				}
				try {
					jsonRequest = new JSONObject(jsonRequestString);
					Log.i("V LayoutTimeData ", jsonRequest.toString());
				} catch (JSONException e) {
				}
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						Log.i("Volley JSONObject ", response.toString());
						if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_LAYOUT_TIME))) {
							ObjectMapper mapper = new ObjectMapper();
							LayoutTimeData obj = null;
							try {
								obj = mapper.readValue(response.toString(), LayoutTimeData.class);
							} catch (IOException e) {
							}
							String deviceId = obj.getId();
							String endTime = obj.getEndTime();
							if (!endTime.isEmpty()) {
								DataCacheManager.getInstance(AppMain.getAppMainContext()).removeLayoutTimeById(deviceId);
							}

						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null) {
							Log.e("VolleyError ", error.toString());
						}
					}
				});
				VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);
			}
		}
	}

	private String generateId(String media_Id, String box_Id, String layout_Id){

		Date date = new Date();
		String id = "";
		id = media_Id+"_"+box_Id+"_"+layout_Id+"_"+date.getTime();
		if(media_Id.isEmpty()){
			id = box_Id+"_"+layout_Id+"_"+date.getTime();
		}
		return id;
	}


	/**
	 * This function sends the log request to server
	 * @param ctx
	 */
	private void sendLocationRequest(Context ctx) {
		try {
			String location = LogData.getInstance().getLocation(AppMain.getAppMainContext());
			if (!location.equalsIgnoreCase(LogData.STR_UNKNOWN)) {
				String[] locationCoordinate = location.split(",");
				if (locationCoordinate != null && locationCoordinate.length == 2) {
					LocationData locationData = new LocationData();
					locationData.setId(LogData.getInstance().getAppID(AppMain.getAppMainContext()));
					locationData.setLat(locationCoordinate[0]);
					locationData.setLongitude(locationCoordinate[1]);
					if (LogUtility.checkNetwrk(ctx)) {
						ObjectMapper mapper = new ObjectMapper();
						String jsonRequestString = null;
						try {
							jsonRequestString = mapper.writeValueAsString(locationData);
						} catch (IOException e) {
							e.printStackTrace();
						}
						JSONObject jsonRequest = null;
						try {
							jsonRequest = new JSONObject(jsonRequestString);
						} catch (JSONException e) {
//                          e.printStackTrace();
						}
						final String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_BOX_LOCATION);
						JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								ObjectMapper mapper = new ObjectMapper();
								LocationData obj = null;
								try {
									obj = mapper.readValue(response.toString(), LocationData.class);
								} catch (IOException e) {
									e.printStackTrace();
								}
								String deviceId = obj.getId();
								if (!deviceId.isEmpty()) {
									LogData.getInstance().setLocation(AppMain.getAppMainContext(), LogData.STR_UNKNOWN);
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {

//                        Log.e("VolleyError ", error.getMessage());
							}
						});
						VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);
					}
				}
			}
		}catch(Exception e){

		}
	}

	private void syncSavedListMediaLayoutData(){
		sendLocationRequest(AppMain.getAppMainContext());
		DataBaseBackground dataBaseBackground = new DataBaseBackground();
		dataBaseBackground.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
	}

	private void sendSyncRequest(final String url,String arrayJson){
		if(checkNetwork()){
			JSONArray jsonRequest = null;

			try {
				jsonRequest = new JSONArray(arrayJson);
			} catch (JSONException e) {
			}
			JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONArray>() {

				@Override
				public void onResponse(JSONArray response) {

					Log.i("Volley Sync ", response.toString());
					if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_SCREEN_OFFLINE))) {

						DataCacheManager.getInstance(AppMain.getAppMainContext()).removeOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_SCREEN_TABLE);

					}else if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_APP_OFFLINE))) {

						DataCacheManager.getInstance(AppMain.getAppMainContext()).removeOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_APP_TABLE);

					}else if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_ONOFF_BOX_OFFLINE))) {

						DataCacheManager.getInstance(AppMain.getAppMainContext()).removeOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_BOX_TABLE);

					}else if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_LAYOUT_TIME_OFFLINE))) {

						DataCacheManager.getInstance(AppMain.getAppMainContext()).removeLayoutTimeData();

					}else if (url.equalsIgnoreCase(new ServiceURLManager().getUrl(IAPIConstants.API_KEY_MEDIA_TIME_OFFLINE))) {

						DataCacheManager.getInstance(AppMain.getAppMainContext()).removeMediaTimeData();
					}

				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {

//                        Log.e("VolleyError ", error.getMessage());
				}
			});
			VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);
		}
	}
	private class DataBaseBackground extends AsyncTask<Void, Void, String> {

		public DataBaseBackground() {

		}

		@Override
		protected String doInBackground(Void... params) {
			try {

				mList = new Vector<MediaTimeData>();
				mListLayout = new Vector<LayoutTimeData>();
				onOffListBox = new Vector<OnOffTimeData>();
				onOffListAPP = new Vector<OnOffTimeData>();
				onOffListScreen = new Vector<OnOffTimeData>();

				onOffListBox = DataCacheManager.getInstance(AppMain.getAppMainContext()).getAllOnOffTimeBoxData();
				onOffListAPP= DataCacheManager.getInstance(AppMain.getAppMainContext()).getAllOnOffTimeAppData();
				onOffListScreen = DataCacheManager.getInstance(AppMain.getAppMainContext()).getAllOnOffTimeScreenData();
				mListLayout = DataCacheManager.getInstance(AppMain.getAppMainContext()).getAllLayoutTimeData();
				mList = DataCacheManager.getInstance(AppMain.getAppMainContext()).getAllMediaTimeData();

				return null;
			} catch (Exception e) {
				return "";
			}

		}


		@Override
		protected void onPostExecute(final String data) {
			try {

				if(data == null){
					if(onOffListScreen !=null&& onOffListScreen.size()>0){
						ObjectMapper mapper = new ObjectMapper();
						String arrayJson = null;
						try {
							arrayJson = mapper.writeValueAsString(onOffListScreen);
						} catch (IOException e) {
							e.printStackTrace();
						}
						sendSyncRequest(new ServiceURLManager().getURL(IAPIConstants.API_KEY_ONOFF_SCREEN_OFFLINE),arrayJson);
					}
					if(onOffListAPP !=null&& onOffListAPP.size()>0){
						ObjectMapper mapper = new ObjectMapper();
						String arrayJson = null;
						try {
							arrayJson = mapper.writeValueAsString(onOffListAPP);
						} catch (IOException e) {
							e.printStackTrace();
						}
						sendSyncRequest(new ServiceURLManager().getURL(IAPIConstants.API_KEY_ONOFF_APP_OFFLINE),arrayJson);
					}
					if(onOffListBox !=null&& onOffListBox.size()>0){
						ObjectMapper mapper = new ObjectMapper();
						String arrayJson = null;
						try {
							arrayJson = mapper.writeValueAsString(onOffListBox);
						} catch (IOException e) {
							e.printStackTrace();
						}
						sendSyncRequest(new ServiceURLManager().getURL(IAPIConstants.API_KEY_ONOFF_BOX_OFFLINE),arrayJson);
					}
					if(mListLayout !=null&& mListLayout.size()>0){
						ObjectMapper mapper = new ObjectMapper();
						String arrayJson = null;
						try {
							arrayJson = mapper.writeValueAsString(mListLayout);
						} catch (IOException e) {
							e.printStackTrace();
						}
						sendSyncRequest(new ServiceURLManager().getURL(IAPIConstants.API_KEY_LAYOUT_TIME_OFFLINE),arrayJson);
					}
					if(mList !=null&& mList.size()>0){
						ObjectMapper mapper = new ObjectMapper();
						String arrayJson = null;
						try {
							arrayJson = mapper.writeValueAsString(mList);
						} catch (IOException e) {
							e.printStackTrace();
						}
						sendSyncRequest(new ServiceURLManager().getURL(IAPIConstants.API_KEY_MEDIA_TIME_OFFLINE),arrayJson);
					}
				}
			} catch (Exception e) {
			}
		}

	}

}
