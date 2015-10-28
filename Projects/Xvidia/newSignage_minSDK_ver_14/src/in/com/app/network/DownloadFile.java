package in.com.app.network;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.com.app.AppMain;
import in.com.app.AppState;

/**
 * Created by Ravi_office on 24-Jun-15.
 */
public class DownloadFile {
    int downloadedSize = 0;
    int totalSize = 0;
    String fileName;
    String download_file_path = "http://54.251.255.172/demo-repo/";
    static AppMain  appMainObject = null;
    public void DownloadFile(String file, AppMain appMain){
        appMainObject = appMain;
        fileName = file;
        download_file_path = download_file_path +fileName;
        startDownload();
    }
    void startDownload(){
        new Thread(new Runnable() {
            public void run() {
                downloadFile();
            }
        }).start();
    }
    void downloadFile(){

        try {
            URL url = new URL(download_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            String PATH = Environment.getExternalStorageDirectory()
                    + AppState.DOWNLOAD_FOLDER;
            //create a new file, to save the downloaded file
            File checkFile = new File(PATH,fileName);
            try {
                checkFile.delete();
            } catch (Exception e) {
//			e.printStackTrace();
            }

            File file = new File(PATH);
            //if(file.isDirectory()){
            if(!file.exists())
                file.mkdirs();

            FileOutputStream fileOutput = new FileOutputStream(file);
            int respCode = urlConnection.getResponseCode();
            //Stream used for reading the data from the internet
            if (respCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file which we are downloading
                totalSize = urlConnection.getContentLength();

//            runOnUiThread(new Runnable() {
//                public void run() {
//                    pb.setMax(totalSize);
//                }
//            });

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    // update the progressbar //
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        pb.setProgress(downloadedSize);
//                        float per = ((float)downloadedSize/totalSize) * 100;
//                        cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + (int)per + "%)" );
//                    }
//                });
                }
                //close the output stream when complete //
                fileOutput.close();
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    dialog.dismiss(); // if you want close it..
//                }
//            });
            }
        } catch (final MalformedURLException e) {
//            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
//            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
//            showError("Error : Please check your internet connection " + e);
        }
    }

}
