package in.com.app.updater;

import in.com.app.AppMain;
import in.com.app.data.LogData;
import in.com.app.model.IAPIConstants;
import in.com.app.model.LayoutTimeData;
import in.com.app.model.UpdateRequestData;
import in.com.app.network.ServiceURLManager;
import in.com.app.network.VolleySingleton;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class extends {@link BroadcastReceiver} to download updated APK if available
 * @author Ravi@xvidia
 * @since Version 1.0
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static String updateApk= "update.apk";
    @Override
    public void onReceive(Context context, Intent intent) {
//        String updateUrl ="http://54.251.255.172:9898/signage/apkStatus/"+LogData.getInstance().getAppID(context);
		String updateUrl =new ServiceURLManager().getURL(IAPIConstants.API_KEY_BOX_UPDATE_APK)+LogData.getInstance().getAppID(context);

		try {
			if (checkNetwrk(context)) {
				sendHttpGetUpdateRequest(updateUrl, context);
			}
		} catch (Exception e) {
		}
  
    }
    /**
     * This method checks if networkis connected or not
     * @param context
     * @return true is connected to internet else false
     */
      boolean checkNetwrk(Context context){
		boolean nwFlag = false;
		try{		
			ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				nwFlag = true;
			}
		}catch (Exception e) {
		}

		return nwFlag;
	}
    /**
     * Send request to download updated apk
     * @param url downoad url
     * @param context application context
     * @throws {@link IOException}
     */
	private void sendHttpGetRequest(String url, final Context context,final String clientVersion)
			throws IOException {
		final String apkurl = url;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							20000);

					DefaultHttpClient httpClient = new DefaultHttpClient(
							httpParameters);
					HttpGet httpGet = new HttpGet(apkurl);
					HttpResponse response = null;
					response = httpClient.execute(httpGet);// , localContext);
					int respCode = response.getStatusLine().getStatusCode();
					String PATH = Environment.getExternalStorageDirectory()
							+ "/Download/";
					File checkFile = new File(PATH, updateApk);
					try {
						checkFile.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}

					File file = new File(PATH);
					file.mkdirs();
					File outputFile = new File(file, updateApk);

					FileOutputStream fos = new FileOutputStream(outputFile);

					InputStream is = null;

					if (response != null
							&& respCode == HttpURLConnection.HTTP_OK) {
						// response.
						is = response.getEntity().getContent();
						
						byte[] buffer = new byte[1024];
						int len1 = 0;
						while ((len1 = is.read(buffer)) != -1) {
							fos.write(buffer, 0, len1);
						}
						fos.close();
						is.close();
						LogData.getInstance().setAppVersion(context, clientVersion);
						installNewApk();
					}
				} catch (Exception e) {
				}

				finally {

				}
			}
		});

		thread.start();

	}
	
	/**
	 * This method installs and restarts apk only on rooted STB
	 */
	public void installNewApk()
	{
		String path = Environment.getExternalStorageDirectory()+ "/Download/"+updateApk;
		final String libs = "LD_LIBRARY_PATH=/vendor/lib:/system/lib ";
		final String[] commands = {
				libs + "pm install -r " + path,
				libs + "am start -n " + AppMain.getAppMainContext().getPackageName() + "/" + get_main_activity()
		};
	        try
	        {
//	            Runtime.getRuntime().exec(new String[] {"su", "-c", "pm install -r "+path});
	          // Do the magic
	    			Process p = Runtime.getRuntime().exec( "su" );
	    			InputStream es = p.getErrorStream();
	    			DataOutputStream os = new DataOutputStream(p.getOutputStream());

	    			for( String command : commands ) {
	    				os.writeBytes(command + "\n");
	    			}
	    			os.writeBytes("exit\n");
	    			os.flush();

	    			int read;
	    			byte[] buffer = new byte[4096];
	    			String output = new String();
	    			while ((read = es.read(buffer)) > 0) {
	    			    output += new String(buffer, 0, read);
	    			}
	    			p.waitFor();
	    		} catch (IOException e) {
	    			
	    		} catch (InterruptedException e) {
	    			
	    		}
	}
	
	/**
	 * This method returns the package info of current project
	 * @return package name
	 */
	private String get_main_activity() {
		PackageManager pm = AppMain.getAppMainContext().getPackageManager();
		String packageName = AppMain.getAppMainContext().getPackageName();

		try {
			final int flags = PackageManager.GET_ACTIVITIES;
			PackageInfo packageInfo = pm.getPackageInfo(packageName, flags);
			for( ActivityInfo ai : packageInfo.activities ) {
				if( ai.exported ) {
					return ai.name;
				}
			}
		} catch (NameNotFoundException e) {
		}
		return "";
	}

	/**
	 * This method gets a JSON in response with APK version and download path for apk
	 * @param urlStr upadate query url
	 * @param context application context
	 * @throws {@link IOException}
	 */
  	private void sendHttpGetUpdateRequest(String urlStr, final Context context )throws IOException {
       try {
            if(LogUtility.checkNetwrk(context)){
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlStr, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ObjectMapper mapper = new ObjectMapper();
                        UpdateRequestData obj = null;
                        try {
                            obj = mapper.readValue(response.toString(), UpdateRequestData.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (obj != null) {
                            if (obj.getUpdateAvailable()) {
                                final String apkurl = obj.getUpdateUrl();
                                String clientVersion = obj.getUpdateVersion();
                                try {
                                    sendHttpGetRequest(apkurl, context,clientVersion);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                VolleySingleton.getInstance(context).addToRequestQueue(request);
            }
        }catch(Exception e){

        }

    }
    }
