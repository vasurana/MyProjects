package in.com.app;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;

import in.com.app.data.LogData;
import in.com.app.domain.DisplayLayoutFile;
import in.com.app.model.DownloadStatusData;
import in.com.app.model.IAPIConstants;
import in.com.app.network.ServiceURLManager;
import in.com.app.network.VolleySingleton;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.updater.LogUtility;
import in.com.app.utility.Utility;
import in.com.app.wsdl.XMDS;


/**
 * This class is wrapper for a asyncTask that downloads individual files from server.
 * @author Ravi@Xvidia
 * @since version 1.0
 *
 */
    public class StartDownloadIndividualFiles extends
    AsyncTask<ArrayList<DisplayLayoutFile>, Void, ArrayList<String>> implements IDisplayLayout {

    AppMain appmainInstance = null;
    boolean downloadFail = false;
    int downloadedSize = 0;
    int totalSize = 0;
    ArrayList<DisplayLayoutFile> filesToDownload;
    String download_file_path = "";

    StartDownloadIndividualFiles(AppMain appmain) {
        appmainInstance = appmain;
    }


    @Override
    protected ArrayList<String> doInBackground(ArrayList<DisplayLayoutFile>... params) {
        try {
            filesToDownload = params[0];
            ArrayList<String> fileList = new ArrayList<String>();
            int totalCount = filesToDownload.size();
//            totalCount = totalCount -11;
            totalCount--;
            int count = 0;
            appmainInstance.runOnUiThread(new Runnable() {
                public void run() {
                    appmainInstance.textViewInfo.setVisibility(View.GONE);
                    appmainInstance.dialog.show();
                }
            });

            downloadFail = false;
            for (DisplayLayoutFile file : filesToDownload) {
                if (!file.getType().equalsIgnoreCase(_BLACKLIST_CATEGORY)) {
                    String fileID = file.getId();
                    String fileType = file.getType();
                    String fileName = file.getPath();
                    
                    if (fileType != null && fileType.contains("resource")) {
                        totalCount--;
                        continue;
                    }
                    if (fileName == null) {
                        totalCount--;
                        continue;
                    } else if (fileName.isEmpty()) {
                        totalCount--;
                        continue;
                    }
                    if (fileName.endsWith(".js")) {
                        totalCount--;
                        continue;
                    }
                    if (fileType != null && !fileType.equals(_FILE_TYPE_LAYOUT)) {
                        fileList.add(fileName);
                    }

                    if (file.getSize() != null) {
                        int fileSize = Integer.parseInt(file.getSize());
                        boolean downloadFileFlag = false;
                        if (Utility.IsFileExists(fileName, true)) {
                            String PATH = Environment.getExternalStorageDirectory()
                                    + AppState.DISPLAY_FOLDER;
                            File checkFile = new File(PATH, fileName);
                            long size = checkFile.length();
                            if (size < fileSize) {
                                downloadFileFlag = true;
                            }
                        }
                        if (!Utility.IsFileExists(fileName, true) || downloadFileFlag) {
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
                                        count++;
										final String message = "Downloading "
												+ count + " / " + totalCount + "\nFile name: " + fileName;
                                        appmainInstance.runOnUiThread(new Runnable() {
                                            public void run() {
                                                appmainInstance.dialogtext.setText(message);
                                            }
                                        });
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
                                        //downloadedSize = 0;
                                        totalSize = 0;
                                        count++;
                                        String PATH = Environment.getExternalStorageDirectory()
                                                + AppState.DOWNLOAD_FOLDER;
                                        final String message = "Downloading " + count + " / " + totalCount + "\nFile name: " + fileName;
                                        appmainInstance.runOnUiThread(new Runnable() {
                                            public void run() {
                                                appmainInstance.dialogtext.setText(message);
                                            }
                                        });
                                        sendMediaDownloadStatusRequest(true, fileID);
                                        download_file_path = new ServiceURLManager().getDownloadBaseUrl() + fileName;
                                        downloadedSize = (int) new File(PATH + fileName).length();
                                        URL url = new URL(download_file_path);
                                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                        urlConnection.setRequestProperty("Content-Disposition","inline; filename=" + fileName);
                                        urlConnection.setRequestProperty("Last-Modified", String.valueOf(new File(PATH + fileName).lastModified()));
                                        urlConnection.setRequestProperty("Downloaded", String.valueOf(downloadedSize));
                                        urlConnection.setDoOutput(true);
                                        urlConnection.connect();

                                       /* String PATH = Environment.getExternalStorageDirectory()
                                                + AppState.DOWNLOAD_FOLDER;*/
                                        File checkFile = new File(PATH, fileName);
                                        try {
                                            checkFile.delete();
                                        } catch (Exception e) {
                                        }

                                        File saveFile = new File(PATH);
                                        if (!saveFile.exists())
                                            saveFile.mkdirs();
                                        int respCode = urlConnection.getResponseCode();
                                        if (respCode == HttpURLConnection.HTTP_OK) {
                                            Log.e("IP download_file_path", "" + respCode);
                                            File outputFile = new File(PATH, fileName);
                                            FileOutputStream fileOutput = new FileOutputStream(outputFile);
                                            InputStream inputStream = urlConnection.getInputStream();
                                            totalSize = urlConnection.getContentLength();
                                            if (totalSize > 0) {
                                                totalSize = (totalSize / 1024);
                                            }
                                            appmainInstance.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    appmainInstance.pb.setMax(totalSize);
                                                }
                                            });
                                            //create a buffer...
                                            byte[] buffer = new byte[1024];
                                            int bufferLength = 0;

                                            while ((bufferLength = inputStream.read(buffer)) > 0) {
                                                fileOutput.write(buffer, 0, bufferLength);
                                                downloadedSize += bufferLength;
                                                // update the progressbar //
                                                final int downloadSize = (downloadedSize / 1024);
                                                appmainInstance.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        appmainInstance.pb.setProgress(downloadSize);
                                                        float per = ((float) downloadSize / totalSize) * 100;
                                                        if (per > 99) {
                                                            per = 100;
                                                        }
                                                        appmainInstance.cur_val.setText("Download in progress " + downloadSize + "KB / " + totalSize + "KB (" + (int) per + "%)");
                                                    }
                                                });
                                            }
                                            fileOutput.close();
                                            if(urlConnection.getHeaderField("HashValue").equals(hashFile(fileName))){
                                            	Log.i(" updated successfully",fileName);
                                            }
                                        } else {
                                            downloadFail = true;
                                        }
                                    } catch (final MalformedURLException e) {
                                        downloadFail = true;
                                        Log.e("MalformedURLException", "" + e.getMessage());
                                    } catch (final IOException e) {
                                        downloadFail = true;
                                        Log.e("IOException", "" + e.getMessage());
                                    } catch (final Exception e) {
                                        downloadFail = true;
                                        Log.e("Exception", "" + e.getMessage());
                                    }
                                }


                            } else {
                                count++;
                                final String message = "Downloading " + count + " / " + totalCount + "\nFile name: " + fileName;
                                appmainInstance.runOnUiThread(new Runnable() {
                                    public void run() {
                                        appmainInstance.dialogtext.setText(message);
                                    }
                                });
                            }
                        } else {
                            count++;
                            final String message = "Downloading " + count + " / " + totalCount + "\nFile name: " + fileName;
                            appmainInstance.runOnUiThread(new Runnable() {
                                public void run() {
                                    appmainInstance.dialogtext.setText(message);
                                }
                            });
                        }

                    }
                } else {
                    final String message = "Downloaded " + count + " / " + totalCount;
                    appmainInstance.runOnUiThread(new Runnable() {
                        public void run() {
                            appmainInstance.dialogtext.setText(message);
                        }
                    });
                }
            }
            FileManager.setFileArrayListNottoDelete(fileList);
        } catch (Exception e) {
            downloadFail = true;
        }
        return null;
    }
    private String hashFile(String file){
	    try {
	    	File fileName =new File("/sdcard/"+file);
	        MessageDigest digest = MessageDigest.getInstance("MD5");
	        FileInputStream inputStream= new FileInputStream(fileName);
	        byte[] bytesBuffer = new byte[1024];
	        int bytesRead = -1;
	 
	        while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
	            digest.update(bytesBuffer, 0, bytesRead);
	        }
	 
	        if(inputStream != null){
	        	inputStream.close();
	        }
	        byte[] hashedBytes = digest.digest();
	 
	        return convertByteArrayToHexString(hashedBytes);
	    } catch (Exception e) {
	        
	                System.out.println("Could not generate hash from file");
	                return null;
	    }
	}
    private String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    private String saveFileToDisc(String filename, byte[] byteData) throws IOException {

        String PATH = Environment.getExternalStorageDirectory()
                + AppState.DOWNLOAD_FOLDER;
        File checkFile = new File(PATH, filename);

        try {
            checkFile.delete();
        } catch (Exception e) {
        }

        File file = new File(PATH);
        if (!file.exists())
            file.mkdirs();
        File outputFile = new File(file, filename);
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(byteData);
        fos.close();

        return PATH + filename;
    }

    @Override
    protected void onPostExecute(final ArrayList<String> data) {
        boolean nextStepFlag = false;
        try {
            sendMediaDownloadStatusRequest(false, "");
            if (!downloadFail) {
                nextStepFlag = true;
                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE, FLAG_TRUE);
//				
                String destPath = Environment.getExternalStorageDirectory()
                        + AppState.DISPLAY_FOLDER;
                String sourcePath = Environment.getExternalStorageDirectory()
                        + AppState.DOWNLOAD_FOLDER;
                File sourceLocation = new File(sourcePath);
                File targetLocation = new File(destPath);

                FileManager.copyDirectoryOneLocationToAnotherLocation(sourceLocation, targetLocation);
                FileManager.deleteDir(sourceLocation);

                String xml = DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_NEW_DISPLAY_XML);
                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_CURRENT_DISPLAY_XML, xml);
//						DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_CURRENT_DISPLAY_XML,xml);
                LogData.getInstance().setCurrentDisplayFilesXml(AppMain.getAppMainContext(), xml);
                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_NEW_DISPLAY_XML, "");
                appmainInstance.downloadFailed = false;

            } else {
                appmainInstance.downloadFailed = true;
                DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE, FLAG_FALSE);
            }
        } catch (IOException e) {
            appmainInstance.downloadFailed = true;
            DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE, FLAG_FALSE);

        }
        if (!appmainInstance.downloadFailed) {
            appmainInstance.runOnUiThread(new Runnable() {
                public void run() {
                    appmainInstance.dialog.dismiss(); // if you want close it..
                }
            });
            StateMachine.gi(appmainInstance).initProcess(nextStepFlag, StateMachine.SHOWDISPLAY);
        } else {
            appmainInstance.downloadFailed = true;
            appmainInstance.runOnUiThread(new Runnable() {
                public void run() {
                    appmainInstance.dialog.dismiss();
                }
            });
            DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE, FLAG_FALSE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    StateMachine.gi(appmainInstance).initProcess(false, StateMachine.SHOWDISPLAY);
                }
            }, 10000);
        }

        if (StateMachine.gi(appmainInstance).receivedFilesDownloadLater != null) {
            LogData.getInstance().setDownloadPending(appmainInstance, true);
        }
    }


    void sendMediaDownloadStatusRequest(boolean downloadingStatus, String mediaId) {
        DownloadStatusData object = new DownloadStatusData();
        object.setBoxId(LogData.getInstance().getAppID(AppMain.getAppMainContext()));
        object.setDownloadingStatus(downloadingStatus);
        object.setDownloadingMediaId(mediaId);
        if (LogUtility.checkNetwrk(AppMain.getAppMainContext())) {
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