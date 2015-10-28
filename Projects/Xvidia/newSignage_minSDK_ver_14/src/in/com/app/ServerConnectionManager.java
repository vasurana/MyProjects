package in.com.app;

import in.com.app.data.LogData;
import in.com.app.model.IAPIConstants;
import in.com.app.model.InventoryData;
import in.com.app.network.ServiceURLManager;
import in.com.app.network.VolleySingleton;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.updater.LogUtility;
import in.com.app.wsdl.XMDS;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * This class is wrapper for a asyncTask that checks if STB is registered on server in the background.
 * @author Ravi@Xvidia
 * @since version 1.0
 *
 */
public class ServerConnectionManager extends
AsyncTask<String, Void, String> implements IDisplayLayout {

	AppMain appmainInstance = null;
	public String TAG = getClass().getCanonicalName();
	
	ServerConnectionManager(AppMain appmain){
		appmainInstance = appmain;		
	}
	
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
		if (result==null) {
			StateMachine.gi(appmainInstance).initProcess(nextStepFlag, StateMachine.CONNECTION_ERROR);
		}else if (result.contains("Display is active and ready to start")) {
			DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_REG_COMPLETE, FLAG_TRUE);
			nextStepFlag = true;
			sendBoxNameAndAssetIdRequest(appmainInstance);
		}
		try{
			if(appmainInstance!=null && result != null){
				StateMachine.gi(appmainInstance).initProcess(nextStepFlag, StateMachine.GETSCHEDULE);
			}
		}catch (Exception e) {
		}
		
	}
	/**
	 * This function sends the box data request to server
	 * @param ctx
	 */
	private void sendBoxNameAndAssetIdRequest(Context ctx){
		try {
			InventoryData inventoryData = new InventoryData();
			inventoryData.setAssetId(ClientConnectionConfig._ASSETID);
			inventoryData.setBoxId(ClientConnectionConfig._HARDWAREKEY);
			inventoryData.setBoxName(ClientConnectionConfig._DISPLAYNAME);
			if(LogUtility.checkNetwrk(ctx)){
				ObjectMapper mapper = new ObjectMapper();
				String jsonRequestString = null;
				try {
					jsonRequestString = mapper.writeValueAsString(inventoryData);
				} catch (IOException e) {
					e.printStackTrace();
				}
				JSONObject jsonRequest = null;
				try {
					jsonRequest = new JSONObject(jsonRequestString);
				} catch (JSONException e) {
//                          e.printStackTrace();
				}
				final String url =new ServiceURLManager().getUrl(IAPIConstants.API_KEY_BOX_INVENTORY_DATA);
				JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {

//                        Log.e("VolleyError ", error.getMessage());
					}
				});
				VolleySingleton.getInstance(MyApplication.getAppContext()).addToRequestQueue(request);
			}
		}catch(Exception e){

		}
	}
}