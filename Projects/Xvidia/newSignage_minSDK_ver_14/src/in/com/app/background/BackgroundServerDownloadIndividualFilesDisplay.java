package in.com.app.background;

import in.com.app.AppMain;
import in.com.app.AppState;
import in.com.app.ClientConnectionConfig;
import in.com.app.FileManager;
import in.com.app.IDisplayLayout;
import in.com.app.MyApplication;
import in.com.app.SignageDisplay;
import in.com.app.data.LogData;
import in.com.app.domain.DisplayLayoutFile;
import in.com.app.domain.DisplayLayoutFiles;
import in.com.app.model.DownloadStatusData;
import in.com.app.model.IAPIConstants;
import in.com.app.network.ServiceURLManager;
import in.com.app.network.VolleySingleton;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.updater.LogUtility;
import in.com.app.utility.Utility;
import in.com.app.wsdl.XMDS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is wrapper for a asyncTask that downloads individualfiles from server in the background.
 * @author Ravi@Xvidia
 * @since version 1.0
 *
 */
public class BackgroundServerDownloadIndividualFilesDisplay extends
AsyncTask<DisplayLayoutFiles, Void, ArrayList<String>> implements IDisplayLayout {

	
	Context context = null;
	Activity activity = null;
	boolean downloadFail = false;
	int totalSize = 0;
	String download_file_path = "";
	public BackgroundServerDownloadIndividualFilesDisplay(Context ctx, Activity act){
		context = ctx;
		activity = act;
	}


	@Override
	protected ArrayList<String> doInBackground(DisplayLayoutFiles... params) {			
		try{
			DisplayLayoutFiles files = params[0];
			for (DisplayLayoutFile file : files.getFileList()) {

				downloadFail = false;
				if (!file.getType().equalsIgnoreCase(_BLACKLIST_CATEGORY) ) {
					String fileID = file.getId();
					String fileType = file.getType();
					String fileName = file.getPath();
					if(fileType != null && fileType.contains("resource")){
						continue;
					}
					if (fileName == null) {
						continue;
					} else if (fileName.isEmpty()) {
						continue;
					}
					if (fileName.endsWith(".js")) {
						continue;
					}

					if(file.getSize()!= null){
						int fileSize = Integer.parseInt(file.getSize());
						boolean downloadFileFlag = false;
						if(Utility.IsFileExists(fileName,true)) {
							String PATH = Environment.getExternalStorageDirectory()
									+ AppState.DISPLAY_FOLDER;
							File checkFile = new File(PATH,fileName);
							long size = checkFile.length();
							if(size<fileSize){
								downloadFileFlag = true;
							}
						}
						if(!Utility.IsFileExists(fileName,true)|| downloadFileFlag) {
							if (Utility.IsFileExistsInDownload(fileName)) {
								String PATH = Environment.getExternalStorageDirectory()
										+ AppState.DOWNLOAD_FOLDER;
								File checkFile = new File(PATH, fileName);
								long size = checkFile.length();
								if (size < fileSize) {
									downloadFileFlag = true;
								}
							}
							if (!Utility.IsFileExistsInDownload(fileName) || downloadFileFlag) {
								if (fileType.equals(_FILE_TYPE_LAYOUT)) {
									byte[] bytesData = new byte[fileSize];
									try {
										XMDS xmds = new XMDS(ClientConnectionConfig._SERVERURL);
										bytesData = (xmds.GetFile(ClientConnectionConfig._UNIQUE_SERVER_KEY, ClientConnectionConfig._HARDWAREKEY, fileID, fileType, 0, fileSize/*file size*/, ClientConnectionConfig._VERSION)).toBytes();
										if (bytesData != null) {
											saveFileToDisc(fileName, bytesData);
										}
									} catch (Exception e) {
										downloadFail = true;
									}
								} else {
									downloadFileFlag = false;
									try {
										sendMediaDownloadStatusRequest(true, fileID);
										download_file_path = new ServiceURLManager().getDownloadBaseUrl() + fileName;
										URL url = new URL(download_file_path);
										HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
										urlConnection.setRequestMethod("GET");
										urlConnection.setDoOutput(true);
										urlConnection.connect();

										String PATH = Environment.getExternalStorageDirectory()
												+ AppState.DOWNLOAD_FOLDER;
										File checkFile = new File(PATH, fileName);
										try {
											checkFile.delete();
										} catch (Exception e) {
										}

										File saveFile = new File(PATH);
										if (!saveFile.exists())
											saveFile.mkdirs();
										File outputFile = new File(PATH, fileName);
										FileOutputStream fileOutput = new FileOutputStream(outputFile);
										int respCode = urlConnection.getResponseCode();
										if (respCode == HttpURLConnection.HTTP_OK) {
											InputStream inputStream = urlConnection.getInputStream();
											totalSize = urlConnection.getContentLength();
											byte[] buffer = new byte[1024];
											int bufferLength;

											while ((bufferLength = inputStream.read(buffer)) > 0) {
												fileOutput.write(buffer, 0, bufferLength);

											}
											fileOutput.close();
										}
									} catch (final MalformedURLException e) {
										downloadFail = true;
										e.printStackTrace();
									} catch (final IOException e) {
										downloadFail = true;
										e.printStackTrace();
									} catch (final Exception e) {
										downloadFail = true;
									}
								}
							}
						}

					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}



	private String saveFileToDisc(String filename, byte[] byteData) throws IOException{
		String PATH = Environment.getExternalStorageDirectory()+ AppState.DOWNLOAD_FOLDER;
		File checkFile = new File(PATH, filename);

		try {
			checkFile.delete();
		} catch (Exception e) {
		}

		File file = new File(PATH);
		if(!file.exists())
			file.mkdirs();
		File outputFile = new File(file, filename);


		FileOutputStream fos = new FileOutputStream(outputFile);

		fos.write(byteData);
		fos.close();

		return PATH + filename;

	}

	@Override
	protected void onPostExecute(final ArrayList<String> data) {
		CopyFileTask copyFileTask = new CopyFileTask();
		copyFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void)null);
	}

	private class CopyFileTask extends AsyncTask<Void, Void, String> {
		
		public CopyFileTask() {
			
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				String destPath = Environment.getExternalStorageDirectory()
						+ AppState.DISPLAY_FOLDER;
				String sourcePath = Environment.getExternalStorageDirectory()
						+ AppState.DOWNLOAD_FOLDER;
				File sourceLocation = new File (sourcePath);
				File targetLocation = new File (destPath);
				FileManager.copyDirectoryOneLocationToAnotherLocation(sourceLocation, targetLocation);
				FileManager.deleteDir(sourceLocation);
				return null;
			} catch (IOException e) {
				return "";
			}catch (Exception e) {
				return "";
			}
			
		}
		
		@Override
		protected void onPostExecute(final String data) {
			boolean nextStepFlag = false;
				try {

					sendMediaDownloadStatusRequest(false, "");
					if(data == null){
						if(!downloadFail){
							nextStepFlag = true;
							String xml = DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_NEW_DISPLAY_XML);
							LogData.getInstance().setCurrentDisplayFilesXml(AppMain.getAppMainContext(), xml);
							DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_NEW_DISPLAY_XML,"");
							DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_BACKGROUND_FILE_DOWNLOAD_COMPLETE, FLAG_TRUE);
						
						}else{
							DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_BACKGROUND_FILE_DOWNLOAD_COMPLETE, FLAG_FALSE);
							
						}

					}else{
						DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_BACKGROUND_FILE_DOWNLOAD_COMPLETE, FLAG_FALSE);
						
					}

					SignageDisplay.backGroundDownloadStarted = false;
				} catch (Exception e) {
					DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_BACKGROUND_FILE_DOWNLOAD_COMPLETE, FLAG_FALSE);			
				}
			if(!LogData.getInstance().getDownloadPending(AppMain.getAppMainContext())){
				LogData.getInstance().setDownloadPending(AppMain.getAppMainContext(),true);
				StateMachineDisplay.gi(context, activity).initProcess(nextStepFlag, StateMachineDisplay.SHOWDISPLAY);
			}else{
				LogData.getInstance().setDownloadPending(AppMain.getAppMainContext(),false);
			}

		}
		
	}


	void sendMediaDownloadStatusRequest(boolean downloadingStatus, String mediaId){
		DownloadStatusData object = new DownloadStatusData();
		object.setBoxId(LogData.getInstance().getAppID(AppMain.getAppMainContext()));
		object.setDownloadingStatus(downloadingStatus);
		object.setDownloadingMediaId(mediaId);
		if(LogUtility.checkNetwrk(AppMain.getAppMainContext())){
			String url = new ServiceURLManager().getUrl(IAPIConstants.API_KEY_BOX_DOWNLOADING_STATUS);
			JSONObject jsonRequest = null;
			ObjectMapper mapper = new ObjectMapper();
			String jsonRequestString = null;
			try {
				jsonRequestString = mapper.writeValueAsString(object);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				jsonRequest = new JSONObject(jsonRequestString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
				}
			});
			VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);
		}

	}

}