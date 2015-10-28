package in.com.app;

import in.com.app.background.StateMachineDisplay;
import in.com.app.data.LogData;
import in.com.app.data.SignageData;
import in.com.app.model.InventoryData;
import in.com.app.model.LocationData;
import in.com.app.model.OnOffTimeData;
import in.com.app.model.IAPIConstants;
import in.com.app.network.ServiceURLManager;
import in.com.app.network.VolleySingleton;
import in.com.app.storage.caching.sqlight.MySqliteHelper;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.updater.AlarmReceiver;
import in.com.app.updater.HeartBeatReceiver;
import in.com.app.updater.LogUtility;
import in.com.app.updater.NetworkUtil;
import in.com.app.utility.MyExceptionHandler;
import in.com.app.utility.Utility;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Process;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * 
 * @author Ravi@Xvidia Technologies
 * @version 1.0 This is the Main class where registration is processed and
 *          layout is downloaded. IpAddress, MacId, Location, Network Type,
 *          Network Status information is fetched.
 *
 */
public class AppMain extends Activity implements IDisplayLayout {

	int layoutWidth = 0;
	int layoutHeight = 0;
	public static Handler UIHandler;
	static ProgressBar pb;
	static TextView dialogtext;
	static Dialog dialog;
	static TextView cur_val;
	static Handler handler = null;
	static private Runnable runnableBackground;
	public final String TAG = AppMain.class.getCanonicalName();
	TextView textViewRegID = null, textViewMacId = null,
			textViewIpAddress = null, textViewLocation = null,
			textViewAppVersion = null, textViewResolution = null;
	public static TextView textViewInfo = null;
	public static TextView connectionStatus = null;
	private LinearLayout locationlayout;
	PopupWindow pwindo;
	Button btnCancel, btnSubmit, btnSave, buttonWifiSetting;
	EditText userName, password;
	boolean cancelPopup = false;
	final int toastTime = 3000;
	final int HEARTBEAT_TIME = 60 * 1000;
	final int MIN_TIME_LOCATION_UPDATES = 5 * 1000;
	final int TIMER_TIME = 30000;
	String android_id;
	private LocationManager mLocationManager;
	private String provider;
	private MyLocationListener mylistener;
	private Criteria criteria;
	EditText editBoxDisplayName, editBoxAddress, editBoxAssetId;
	int currentApiVersion;
	private Spinner spinnerOrientation;
	static private Runnable refreshRunnable;
	static Handler refreshHandler = null;
	final int _TIME_TO_REHIT_SERVER = 15 * 1000;// 5*60*1000;

	@TargetApi(19)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		currentApiVersion = android.os.Build.VERSION.SDK_INT;

		final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

		// This work only for android 4.4+
		if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

			getWindow().getDecorView().setSystemUiVisibility(flags);

			// Code below is to handle presses of Volume up or Volume down.
			// Without this, after pressing volume buttons, the navigation bar
			// will
			// show up and won't hide
			final View decorView = getWindow().getDecorView();
			decorView
					.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

						@Override
						public void onSystemUiVisibilityChange(int visibility) {
							if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
								decorView.setSystemUiVisibility(flags);
							}
						}
					});
		}
		setContentView(R.layout.layout_appmain);
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this,
				AppMain.class));
		context = getApplicationContext();
		showProgress();
		startUpdater();
		UIHandler = new Handler(Looper.getMainLooper());
		AppState.ACTION_ON_PAUSE = false;
		android_id = "" + System.currentTimeMillis();
		Random r = new Random();
		int temp = r.nextInt(999999999) + 65;
		android_id = android_id + temp;
		LogData.getInstance().setRequestSend(context, true);
		ClientConnectionConfig._DISPLAYNAME = LogData.getInstance()
				.getDisplayname(getApplicationContext());
		ClientConnectionConfig._HARDWAREKEY = LogData.getInstance().getAppID(
				getApplicationContext());// (_KEY_XVIDIA_HARDWARE_KEY);
		ClientConnectionConfig._ADDRESS = LogData.getInstance().getAddress(
				getApplicationContext());
		ClientConnectionConfig._ASSETID = FileManager
				.readTextFromFile(AppState.FILE_NAME_ASSET);

		if (ClientConnectionConfig._ASSETID.isEmpty()
				|| ClientConnectionConfig._ASSETID
						.equalsIgnoreCase(LogData.STR_UNKNOWN)) {
			ClientConnectionConfig._ASSETID = "";
		}
		if (ClientConnectionConfig._DISPLAYNAME.isEmpty()
				|| ClientConnectionConfig._DISPLAYNAME
						.equalsIgnoreCase(LogData.STR_UNKNOWN)) {
			String appIdStr = DataCacheManager.getInstance(getAppMainContext())
					.readSettingData(_KEY_XVIDIA_DISPLAY_NAME);
			// String tempStr = appIdStr;
			if (!appIdStr.isEmpty()) {
				LogData.getInstance().setDisplayname(getAppMainContext(),
						appIdStr);
				DataCacheManager.getInstance(getAppMainContext())
						.saveSettingData(_KEY_XVIDIA_DISPLAY_NAME, "");
			} else if (!ClientConnectionConfig._ASSETID.isEmpty()) {
				appIdStr = ClientConnectionConfig._ASSETID;
				LogData.getInstance().setDisplayname(getAppMainContext(),
						appIdStr);
			}
			ClientConnectionConfig._DISPLAYNAME = appIdStr;

		}
		if (ClientConnectionConfig._HARDWAREKEY.isEmpty()
				|| ClientConnectionConfig._HARDWAREKEY
						.equalsIgnoreCase(LogData.STR_UNKNOWN)) {
			String appIdStr = DataCacheManager.getInstance(getAppMainContext())
					.readSettingData(_KEY_XVIDIA_HARDWARE_KEY);
			if (!appIdStr.isEmpty()) {
				LogData.getInstance().setAppID(getAppMainContext(), appIdStr);
				DataCacheManager.getInstance(getAppMainContext())
						.saveSettingData(_KEY_XVIDIA_HARDWARE_KEY, "");
			}
			ClientConnectionConfig._HARDWAREKEY = appIdStr;

		}
		if (ClientConnectionConfig._ADDRESS.isEmpty()
				|| ClientConnectionConfig._ADDRESS
						.equalsIgnoreCase(LogData.STR_UNKNOWN)) {

			ClientConnectionConfig._ADDRESS = "";
		}
		locationlayout = (LinearLayout) findViewById(R.id.layout_Location);
		spinnerOrientation = (Spinner) findViewById(R.id.spinner_orientation);
		String[] orientation = getResources().getStringArray(
				R.array.menu_orientation);
		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, orientation);
		adapter_state
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOrientation.setAdapter(adapter_state);
		spinnerOrientation
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						spinnerOrientation.setSelection(position);
						String selState = (String) spinnerOrientation
								.getSelectedItem();
						if (position == 0) {
							LogData.getInstance().setOrientation(
									getAppMainContext(), ORIENTATION_LANSCAPE);
						} else if (position == 1) {
							LogData.getInstance().setOrientation(
									getAppMainContext(),
									ORIENTATION_REVERSE_LANSCAPE);
						} else if (position == 2) {
							LogData.getInstance().setOrientation(
									getAppMainContext(), ORIENTATION_PORTRAIT);
						} else if (position == 3) {
							LogData.getInstance().setOrientation(
									getAppMainContext(),
									ORIENTATION_REVERSE_PORTRAIT);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		if (LogData.getInstance().getOrientation(getAppMainContext()) == ORIENTATION_LANSCAPE) {
			// landscape.setChecked(true);
			spinnerOrientation.setSelection(0);
		} else if (LogData.getInstance().getOrientation(getAppMainContext()) == ORIENTATION_REVERSE_LANSCAPE) {
			// landscapeReverse.setChecked(true);
			spinnerOrientation.setSelection(1);
		} else if (LogData.getInstance().getOrientation(getAppMainContext()) == ORIENTATION_PORTRAIT) {
			// portrait.setChecked(true);
			spinnerOrientation.setSelection(2);
		} else if (LogData.getInstance().getOrientation(getAppMainContext()) == ORIENTATION_REVERSE_PORTRAIT) {
			// portraitReverse.setChecked(true);
			spinnerOrientation.setSelection(3);
		}
		editBoxDisplayName = (EditText) findViewById(R.id.id_txtDisplayName);
		editBoxDisplayName.setText(ClientConnectionConfig._DISPLAYNAME);
		// editBoxDisplayName.setOnEditorActionListener(new
		// OnEditorActionListener() {
		// //initialiseInternalCamera();
		// @Override
		// public boolean onEditorAction(TextView v, int actionId,
		// KeyEvent event) {
		// int result = actionId & EditorInfo.IME_MASK_ACTION;
		// switch(result) {
		// case EditorInfo.IME_ACTION_DONE:
		// FileManager.writeTextToFile(editBoxDisplayName.getText().toString(),AppState.FILE_NAME_BOX);
		// hideSoftKeyboard(editBoxDisplayName);
		// break;
		// case EditorInfo.IME_ACTION_NEXT:
		// FileManager.writeTextToFile(editBoxDisplayName.getText().toString(),AppState.FILE_NAME_BOX);
		// break;
		// }
		// return false;
		// }
		//
		//
		// });
		editBoxDisplayName.clearFocus();
		editBoxDisplayName.setKeyListener(null);
		editBoxAddress = (EditText) findViewById(R.id.id_txtAddress);
		editBoxAddress.setText(ClientConnectionConfig._ADDRESS);
		editBoxAddress.setOnEditorActionListener(new OnEditorActionListener() {
			// initialiseInternalCamera();
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				int result = actionId & EditorInfo.IME_MASK_ACTION;
				switch (result) {
				case EditorInfo.IME_ACTION_DONE:
					hideSoftKeyboard(editBoxAddress);
					break;
				case EditorInfo.IME_ACTION_NEXT:
					if (!ClientConnectionConfig._ASSETID.isEmpty()) {
						hideSoftKeyboard(editBoxAddress);
					}
					break;
				}
				return false;
			}

		});
		editBoxAddress.clearFocus();
		editBoxAssetId = (EditText) findViewById(R.id.id_txtAssetId);
		btnSave = (Button) findViewById(R.id.btnSaveAsset);
		editBoxAssetId.setText(ClientConnectionConfig._ASSETID);
		if (!ClientConnectionConfig._ASSETID.isEmpty()) {
			// editBoxAssetId.setOnClickListener(this);
			// }else{
			editBoxAssetId.setKeyListener(null);
			btnSave.setVisibility(View.GONE);
		}
		editBoxAssetId.setOnEditorActionListener(new OnEditorActionListener() {
			// initialiseInternalCamera();
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				int result = actionId & EditorInfo.IME_MASK_ACTION;
				switch (result) {
				case EditorInfo.IME_ACTION_DONE:
					if (!editBoxAssetId.getText().toString().isEmpty()) {
						FileManager.writeTextToFile(editBoxAssetId.getText()
								.toString(), AppState.FILE_NAME_ASSET);
						// DataCacheManager.getInstance().saveSettingData(_KEY_XVIDIA_ASSETID,
						// editBoxAssetId.getText().toString());
						LogData.getInstance().setAssetID(
								getApplicationContext(),
								editBoxAssetId.getText().toString());
					} else {
						Toast tStatus = Toast.makeText(AppMain.this,
								"Please enter asset ID", Toast.LENGTH_SHORT);
						tStatus.show();
					}
					hideSoftKeyboard(editBoxAssetId);
					break;
				case EditorInfo.IME_ACTION_NEXT:
					if (!editBoxAssetId.getText().toString().isEmpty()) {
						LogData.getInstance().setAssetID(
								getApplicationContext(),
								editBoxAssetId.getText().toString());
						FileManager.writeTextToFile(editBoxAssetId.getText()
								.toString(), AppState.FILE_NAME_ASSET);
					}
					hideSoftKeyboard(editBoxAssetId);
					break;
				}
				return false;
			}

		});
		editBoxAssetId.clearFocus();
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!editBoxAssetId.getText().toString().isEmpty()) {
					ClientConnectionConfig._ASSETID = editBoxAssetId.getText()
							.toString();
					LogData.getInstance().setAssetID(getAppMainContext(),
							ClientConnectionConfig._ASSETID);
					FileManager.writeTextToFile(
							ClientConnectionConfig._ASSETID,
							AppState.FILE_NAME_ASSET);
					// DataCacheManager.getInstance().saveSettingData(_KEY_XVIDIA_ASSETID,
					// ClientConnectionConfig._ASSETID);

					editBoxAssetId.setKeyListener(null);
					btnSave.setVisibility(View.GONE);
				} else {
					Toast tStatus = Toast.makeText(AppMain.this,
							"Please enter asset ID", Toast.LENGTH_SHORT);
					tStatus.show();
				}
			}
		});
		// updateNewFolderStructure();
		Button btnRegister = (Button) findViewById(R.id.btnRegister);
		Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
		Button btnRoot = (Button) findViewById(R.id.btnRoot);
		// internetConnectionStatus = (View)
		// findViewById(R.id.id_connectionStatus);
		textViewRegID = (TextView) findViewById(R.id.id_txtRegistrationId);
		textViewMacId = (TextView) findViewById(R.id.id_txtMacId);
		textViewIpAddress = (TextView) findViewById(R.id.id_txtIpId);
		textViewLocation = (TextView) findViewById(R.id.id_txtLocation);
		textViewResolution = (TextView) findViewById(R.id.id_txtResolution);
		textViewInfo = (TextView) findViewById(R.id.id_txtInfo);
		textViewAppVersion = (TextView) findViewById(R.id.id_version);
		buttonWifiSetting = (Button) findViewById(R.id.btnwifi_setting);
		connectionStatus = (TextView) findViewById(R.id.id_txtConnection);
		// progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		// progressBar.setVisibility(View.GONE);
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(5000); // You can manage the blinking time with this
								// parameter
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		textViewInfo.startAnimation(anim);
		textViewAppVersion.setText(getAppVersion());
		buttonWifiSetting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AppMain.this,
						InternetConnectionSetting.class);
				startActivity(i);
				finish();
			}
		});
		btnRoot.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				installNewApk();

			}
		});
		btnRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				registerDevice();
			}
		});
		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DataCacheManager.getInstance(getAppMainContext())
						.removeResourceDataAll();
				LogData.getInstance().setCurrentLayoutXml(getAppMainContext(),
						LogData.STR_UNKNOWN);
				SignageData.resetSignageData();
				DataCacheManager.getInstance(getAppMainContext())
						.clearSettingCache();
				registerDevice();
				// installNewApk();
			}
		});

		refreshScreen();
		new InternetAccessible().executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
		/**
		 * 
		 * Timer is started to if returned from display screen to reinvoke
		 * display if screen is idle for 180 seconds
		 *
		 **/
		if (AppState.BACKPRESSED_SIGNAGE_SCREEN) {
			btnRegister.setText(getResources().getString(R.string.btnStart));
			startTimer(TIMER_TIME);
		}
		textViewInfo.setVisibility(View.GONE);
		/**
		 * 
		 * It Switches to display layout in offline mode if STB is already
		 * registered and layout previously downloaded.
		 *
		 **/
		if (ClientConnectionConfig._HARDWAREKEY.equals("")
				|| ClientConnectionConfig._HARDWAREKEY
						.equalsIgnoreCase(LogData.STR_UNKNOWN)) {
			try {
				android_id = Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);
			} catch (Exception e) {

			}
			setInitialValue(android_id);
			ClientConnectionConfig._HARDWAREKEY = android_id;
			registerDevice();
		} else {
			android_id = ClientConnectionConfig._HARDWAREKEY;
			setInitialValue(android_id);
			getBoxNameAndAssetIdData(getAppMainContext());
			// if(checkOfflineTimedOut(30)){
			// textViewInfo.setText(R.string.register_complete);
			// textViewInfo.setTextColor(getResources().getColor(R.color.red_color));
			// textViewInfo.setVisibility(View.VISIBLE);
			// // progressBar.setVisibility(View.GONE);
			// }else
			if (!AppState.BACKPRESSED_SIGNAGE_SCREEN) {
				cancelPopup = true;
				String xml = LogData.getInstance().getCurrentLayoutXml(
						getAppMainContext());

				if (xml != null && !xml.equalsIgnoreCase("")
						&& !xml.equalsIgnoreCase(LogData.STR_UNKNOWN)) {
					StateMachine.displayingContentInOffileMode = false;
					btnRegister.setText(getResources().getString(
							R.string.btnStart));
					StateMachine.gi(AppMain.this).initProcess(false,
							StateMachine.OFFLINE);
				} else {
					String username = editBoxDisplayName.getText().toString();
					if (username != null && username.equals("")
							|| username.length() == 0) {
						editBoxDisplayName.requestFocus();
					} else {
						hideSoftKeyboard(editBoxDisplayName);
					}
					btnRegister.setText(getResources().getString(
							R.string.btnStart));
					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					if (currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
						display.getRealSize(size);
					} else {
						display.getSize(size);
					}
					layoutWidth = size.x;
					layoutHeight = size.y;
					String resolution = "" + layoutWidth + "X" + layoutHeight;
					// String mesg = ClientConnectionConfig._DISPLAYNAME
					// +"##"+ClientConnectionConfig._HARDWAREKEY+"##"+ClientConnectionConfig._ADDRESS+"##"+resolution;
					// FileManager.writeTextToFile(mesg,AppState.FILE_NAME_DISPLAY);
					LogData.getInstance().setDisplayname(getAppMainContext(),
							ClientConnectionConfig._DISPLAYNAME);
					LogData.getInstance().setAppID(getAppMainContext(),
							ClientConnectionConfig._HARDWAREKEY);
					LogData.getInstance().setAddress(getAppMainContext(),
							ClientConnectionConfig._ADDRESS);
					// boolean logFlagres =
					// LogData.getInstance().setResolution(getAppMainContext(),
					// resolution);
					LogData.getInstance().setResolution(getAppMainContext(),
							resolution);
					// if(logFlagres){
					// sendLogRequest(getApplicationContext(),
					// LogUtility.getInstance().getScreenReolutionJson(getApplicationContext()),
					// LogUtility.getScreenResolutionLogUrl());
					// }
					LogData.getInstance().setAssetID(getAppMainContext(),
							ClientConnectionConfig._ASSETID);
					StateMachine.gi(AppMain.this).initProcess(true,
							StateMachine.REGISTER);
				}
				// }else{
				// registerDevice();
			}
			return;
		}
	}

	// private void updateNewFolderStructure(){
	// String destPath = Environment.getExternalStorageDirectory()
	// + AppState.DISPLAY_FOLDER;
	// String sourcePath = Environment.getExternalStorageDirectory()
	// + AppState.OLD_DISPLAY_FOLDER;
	// File sourceLocation = new File (sourcePath);
	// File targetLocation = new File (destPath);
	// if (sourceLocation.isDirectory() && sourceLocation.exists()) {
	// try {
	// FileManager.copyDirectoryOldToNew(sourceLocation, targetLocation);
	// FileManager.deleteDir(sourceLocation);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// private void enableGPS(){
	// try {
	// boolean gpsStatus =
	// mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	// if (!gpsStatus) {
	// Settings.Secure.putInt(getContentResolver(), Secure.LOCATION_MODE,
	// Secure.LOCATION_MODE_HIGH_ACCURACY);
	// }
	// }catch(Exception e){
	// Toast.makeText(this, "eroorlocation "+e.getMessage(),
	// Toast.LENGTH_LONG).show();
	//
	// }
	// }

	/**
	 * Checks if the application is running in offline mode for more than a
	 * stipulated time
	 * 
	 * @param days
	 * @return
	 */
	private boolean checkOfflineTimedOut(int days) {
		boolean returnValue = false;
		if (LogUtility.checkNetwrk(getAppMainContext())) {
			LogData.getInstance().setOfflineSinceTimeStamp(getAppMainContext(),
					0);
			return false;
		} else {
			long timeInMilliseconds = LogData.getInstance()
					.getOfflineSinceTimeStamp(AppMain.getAppMainContext());
			if (timeInMilliseconds > 0) {
				long currentTime = System.currentTimeMillis();
				long diffInMilliSecs = currentTime - timeInMilliseconds;
				int diffInDays = (int) (diffInMilliSecs / (1000 * 60 * 60 * 24));
				if (diffInDays >= days) {
					returnValue = true;
				}
			}
		}
		return returnValue;

	}

	private boolean isInternetAccessible(Context context) {
		try {
			HttpURLConnection urlc = (HttpURLConnection) (new URL(
					"http://www.google.com").openConnection());
			urlc.setRequestProperty("User-Agent", "Test");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(2000);
			urlc.connect();
			return (urlc.getResponseCode() >= 200 && urlc.getResponseCode() < 300);
		} catch (IOException e) {
			Log.e("AppMain", "Couldn't check internet connection", e);
		}

		return false;
	}

	/**
	 * Returns application version
	 * 
	 * @return
	 */
	private String getAppVersion() {
		String version = "1.0";
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info;
			info = manager.getPackageInfo(context.getPackageName(), 0);
			version = info.versionName;
			String savedVersion = LogData.getInstance().getAppVersion(
					getAppMainContext());
			if (!savedVersion.equalsIgnoreCase("")) {
				Double savedVal = Utility
						.convertToDoubleFromString(savedVersion);
				Double verClient = Utility.convertToDoubleFromString(version);
				if (verClient < savedVal) {
					version = savedVal.toString();
				}
			}
		} catch (NameNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (Exception e) {

		}
		return version;
	}

	/**
	 * This function sends the Box data request to server
	 * 
	 * @param ctx
	 */
	private void getBoxNameAndAssetIdData(Context ctx) {
		try {

			String url = new ServiceURLManager()
					.getUrl(IAPIConstants.API_KEY_BOX_GET_INVENTORY_DATA);
			url = url + ClientConnectionConfig._HARDWAREKEY;
			JsonObjectRequest request = new JsonObjectRequest(
					Request.Method.GET, url, null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							ObjectMapper mapper = new ObjectMapper();
							InventoryData obj = null;
							try {
								obj = mapper.readValue(response.toString(),
										InventoryData.class);
							} catch (IOException e) {
								e.printStackTrace();
							}
							String deviceId = obj.getBoxId();
							String boxName = obj.getBoxName();
							String assetId = obj.getAssetId();
							if (!deviceId.isEmpty()
									&& deviceId
											.equals(ClientConnectionConfig._HARDWAREKEY)
									&& !assetId.isEmpty()
									&& assetId
											.equals(ClientConnectionConfig._ASSETID)) {
								LogData.getInstance().setDisplayname(
										getAppMainContext(), boxName);
								ClientConnectionConfig._DISPLAYNAME = boxName;
								editBoxDisplayName.setText(boxName);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {

							// Log.e("VolleyError ", error.getMessage());
						}
					});
			VolleySingleton.getInstance(MyApplication.getAppContext())
					.addToRequestQueue(request);
			// }
		} catch (Exception e) {

		}
	}

	/**
	 * This function sends the log request to server
	 * 
	 * @param ctx
	 * @param locationdata
	 */
	private void sendLocationRequest(Context ctx, LocationData locationdata) {
		try {
			if (LogUtility.checkNetwrk(ctx)) {
				ObjectMapper mapper = new ObjectMapper();
				String jsonRequestString = null;
				try {
					jsonRequestString = mapper.writeValueAsString(locationdata);
				} catch (IOException e) {
					e.printStackTrace();
				}
				JSONObject jsonRequest = null;
				try {
					jsonRequest = new JSONObject(jsonRequestString);
				} catch (JSONException e) {
					// e.printStackTrace();
				}
				final String url = new ServiceURLManager()
						.getUrl(IAPIConstants.API_KEY_BOX_LOCATION);
				JsonObjectRequest request = new JsonObjectRequest(
						Request.Method.POST, url, jsonRequest,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								ObjectMapper mapper = new ObjectMapper();
								LocationData obj = null;
								try {
									obj = mapper.readValue(response.toString(),
											LocationData.class);
								} catch (IOException e) {
									e.printStackTrace();
								}
								String deviceId = obj.getId();
								if (!deviceId.isEmpty()) {
									LogData.getInstance().setLocation(
											getAppMainContext(),
											LogData.STR_UNKNOWN);
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {

								// Log.e("VolleyError ", error.getMessage());
							}
						});
				VolleySingleton.getInstance(MyApplication.getAppContext())
						.addToRequestQueue(request);
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

	private void setClientVersion() {
		try {
			String clientVersion = getAppVersion();
			boolean logFlag = LogData.getInstance().setAppVersion(
					getApplicationContext(), clientVersion); //
			// boolean logFlag =
			// LogData.getInstance().setAppStatus(getApplicationContext(),LogData.APP_ON);
			if (logFlag) {
				// sendLogRequest(getApplicationContext(),
				// LogUtility.getInstance().getAppVersionJson(getApplicationContext()),
				// LogUtility.getAppVersionLogUrl());
			}
			// DataCacheManager.getInstance().saveSettingData(_KEY_XVIDIA_CLIENT_VERSION,
			// clientVersion);
		} catch (Exception e) {
		}
	}

	private void registerDevice() {
		hideSoftKeyboard(editBoxDisplayName);
		setClientVersion();
		SignageData.resetSignageData();
		saveData();
	}

	/**
	 * This method installs and restarts apk only on rooted STB
	 * 
	 * @deprecated
	 */
	public void installNewApk() {
		String path = Environment.getExternalStorageDirectory()
				+ "/Download/dummy.apk";
		final String libs = "LD_LIBRARY_PATH=/vendor/lib:/system/lib ";
		final String[] commands = {
				libs + "pm install -r " + path,
				libs + "am start -n "
						+ AppMain.getAppMainContext().getPackageName() + "/"
						+ get_main_activity() };
		try {
			// Runtime.getRuntime().exec(new String[] {"su", "-c",
			// "pm install -r "+path});
			// Toast.makeText(AppMain.this, "installing ", toastTime).show();
			// Do the magic
			Process p = Runtime.getRuntime().exec("su");
			InputStream es = p.getErrorStream();
			DataOutputStream os = new DataOutputStream(p.getOutputStream());

			for (String command : commands) {
				// Log.i(TAG,command);
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

			// Toast.makeText(this, output.trim() + " (" + p.exitValue() + ")",
			// Toast.LENGTH_LONG).show();
		} catch (IOException e) {

		} catch (InterruptedException e) {

		}
	}

	/**
	 * This method returns the package info of current project
	 * 
	 * @return package name
	 * @deprecated
	 */
	private String get_main_activity() {
		PackageManager pm = AppMain.getAppMainContext().getPackageManager();
		String packageName = AppMain.getAppMainContext().getPackageName();

		try {
			final int flags = PackageManager.GET_ACTIVITIES;
			PackageInfo packageInfo = pm.getPackageInfo(packageName, flags);
			for (ActivityInfo ai : packageInfo.activities) {
				if (ai.exported) {
					return ai.name;
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Toast.makeText(this, "get_main_activity() failed", Toast.LENGTH_LONG)
				.show();
		return "";
	}

	/**
	 * In this method date string is formatted in particular date format
	 * 
	 * @param dateString
	 *            simple date String to be formatted
	 * @return the dateString formated in "yyyy-MM-dd HH:mm:ss.SSS"
	 * @deprecated
	 * @since version 1.0
	 */
	String getMillseconds(String dateString) {

		if (dateString != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
			Date testDate = null;
			try {
				testDate = sdf.parse(dateString);
				long millisec = testDate.getTime();
				dateString = "" + millisec;
			} catch (ParseException e) {
			}

		}
		return dateString;
	}

	/**
	 * This function hides the soft Keyboard
	 * 
	 * @param editbox
	 *            editbox -reference to editext for which soft keyboard is to be
	 *            hidden
	 * @since version 1.0
	 */
	private void hideSoftKeyboard(EditText editbox) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editbox.getWindowToken(), 0);
	}

	/**
	 * In this method Location services is initialised
	 */
	private void initializeLocationManager() {
		// Get the location manager
		// mLocationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// // Define the criteria how to select the location provider
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE); // default

		criteria.setCostAllowed(false);
		// // get the best provider depending on the criteria
		//
		// // the last known location of this provider
		// Location location = mLocationManager.getLastKnownLocation(provider);
		//
		// mylistener = new MyLocationListener();
		//
		// if (location != null) {
		// mylistener.onLocationChanged(location);
		// mLocationManager.requestLocationUpdates(provider, 60000, 1,
		// mylistener);
		// } else {
		// // leads to the settings because there is no last known location
		// // Intent intent = new
		// Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		// // startActivity(intent);
		// }
		try {
			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			mylistener = new MyLocationListener();
			provider = mLocationManager.getBestProvider(criteria, false);
			Location location = mLocationManager.getLastKnownLocation(provider);
			// getting GPS status
			boolean isGPSEnabled = mLocationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				locationlayout.setVisibility(View.GONE);
				Toast.makeText(this,
						"Location service failed. Enable Location Service",
						Toast.LENGTH_LONG).show();
			} else {
				// First get location from Network Provider
				if (isNetworkEnabled) {
					if (mLocationManager != null) {
						mLocationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER, 60000, 1,
								mylistener);
						location = mLocationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							String lat = String.valueOf(location.getLatitude());
							String longitude = String.valueOf(location
									.getLongitude());
							textViewLocation.setText(lat + ", " + longitude,
									BufferType.NORMAL);
							locationlayout.setVisibility(View.VISIBLE);
							LocationData locationData = new LocationData();
							locationData.setId(LogData.getInstance().getAppID(
									AppMain.getAppMainContext()));
							locationData.setLat(lat);
							locationData.setLongitude(longitude);
							sendLocationRequest(AppMain.getAppMainContext(),
									locationData);
						} else {
							locationlayout.setVisibility(View.GONE);
						}
					}
					// }else{
					// Toast.makeText(this, "No Location service Network",
					// Toast.LENGTH_LONG).show();
				}
				// get the location by gps
				if (isGPSEnabled) {
					if (location == null) {
						if (mLocationManager != null) {
							mLocationManager.requestLocationUpdates(
									LocationManager.GPS_PROVIDER,
									MIN_TIME_LOCATION_UPDATES, 1, mylistener);

							location = mLocationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								String lat = String.valueOf(location
										.getLatitude());
								String longitude = String.valueOf(location
										.getLongitude());
								textViewLocation.setText(
										lat + ", " + longitude,
										BufferType.NORMAL);
								LogData.getInstance().setLocation(
										AppMain.getAppMainContext(),
										lat + "," + longitude);
								LocationData locationData = new LocationData();
								locationData.setId(LogData.getInstance()
										.getAppID(AppMain.getAppMainContext()));
								locationData.setLat(lat);
								locationData.setLongitude(longitude);
								sendLocationRequest(
										AppMain.getAppMainContext(),
										locationData);
								locationlayout.setVisibility(View.VISIBLE);
							} else {
								locationlayout.setVisibility(View.GONE);
							}
						}
					}
				} else {
					// Toast.makeText(this, "no Location service GPS",
					// Toast.LENGTH_LONG).show();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * THis function returns IMEI
	 * 
	 * @return IMEI of the STB if exists
	 * @deprecated
	 * 
	 */
	String getIMEI() {
		String imei = "";
		try {
			String ts = Context.TELEPHONY_SERVICE;
			TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
			imei = mTelephonyMgr.getDeviceId();
		} catch (Exception e) {

		}
		return imei;
	}

	/**
	 * In this method it is checked if the STB is already is registered if yes
	 * then it is switched to display mode if layout exist else registration is
	 * checked.
	 * 
	 * @since version 1.0
	 */
	@TargetApi(19)
	void startApp() {
		Toast tStatus = null;
		if (ClientConnectionConfig._DISPLAYNAME.equals("")
				|| ClientConnectionConfig._HARDWAREKEY.equals("")) {
			tStatus = Toast.makeText(AppMain.this, "Enter valid display name",
					Toast.LENGTH_SHORT);
			tStatus.show();
		} else {
			cancelPopup = true;
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			if (currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
				display.getRealSize(size);
			} else {
				display.getSize(size);
			}
			int layoutWidth = size.x;
			int layoutHeight = size.y;
			String resolution = "" + layoutWidth + "X" + layoutHeight;
			LogData.getInstance().setDisplayname(getAppMainContext(),
					ClientConnectionConfig._DISPLAYNAME);
			LogData.getInstance().setAppID(getAppMainContext(),
					ClientConnectionConfig._HARDWAREKEY);
			LogData.getInstance().setAddress(getAppMainContext(),
					ClientConnectionConfig._ADDRESS);
			LogData.getInstance()
					.setResolution(getAppMainContext(), resolution);
			LogData.getInstance().setAssetID(getAppMainContext(),
					ClientConnectionConfig._ASSETID);
			String xml = LogData.getInstance().getCurrentLayoutXml(
					getAppMainContext());

			if (xml != null && !xml.equalsIgnoreCase("")
					&& !xml.equalsIgnoreCase(LogData.STR_UNKNOWN)) {
				StateMachine.displayingContentInOffileMode = false;
				StateMachine.gi(AppMain.this).initProcess(false,
						StateMachine.OFFLINE);
			} else {
				StateMachine.displayingContentInOffileMode = true;
				StateMachine.gi(AppMain.this).initProcess(true,
						StateMachine.REGISTER);
			}
		}

	}

	void refreshScreen() {
		try {
			if (refreshRunnable == null) {
				refreshRunnable = new Runnable() {
					@Override
					public void run() {
						new InternetAccessible().executeOnExecutor(
								AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
						refreshHandler.postDelayed(refreshRunnable,
								_TIME_TO_REHIT_SERVER);
					}
				};
				refreshHandler = new Handler();
				refreshHandler.postDelayed(refreshRunnable,
						_TIME_TO_REHIT_SERVER);
			} else {
				if (refreshHandler != null) {
					refreshHandler.postDelayed(refreshRunnable,
							_TIME_TO_REHIT_SERVER);
				} else {
					refreshHandler = new Handler();
					refreshHandler.postDelayed(refreshRunnable,
							_TIME_TO_REHIT_SERVER);
				}
			}
		} catch (Exception e) {
			refreshScreen();
		}

	}

	/**
	 * In this method AlarmService is started for Update and Heartbeat service
	 * 
	 * @since version 1.0
	 */
	private void startUpdater() {
		// //Update service is registered
		// Toast.makeText(context, "Alarm manager available",
		// Toast.LENGTH_LONG).show();
		Context context = getApplicationContext();
		setRecurringAlarm(context);
		setHeartbeatAlarm(context);

	}

	/**
	 * Heartbeat alarm service is registered to send heartbeat to server every
	 * 120 seconds
	 * 
	 * @param context
	 *            application context
	 * @since version 1.0
	 * 
	 */
	private void setHeartbeatAlarm(Context context) {
		Calendar updateTime = Calendar.getInstance();
		Intent downloader = new Intent(context, HeartBeatReceiver.class);
		PendingIntent recurringHeartbeat = PendingIntent.getBroadcast(context,
				0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(), HEARTBEAT_TIME,
				recurringHeartbeat);
	}

	/**
	 * APK Update alarm service is registered to check for updates and download
	 * and install every 300 Seconds
	 * 
	 * @param context
	 *            application Context
	 * @since version 1.0
	 */
	private void setRecurringAlarm(Context context) {
		Calendar updateTime = Calendar.getInstance();
		Intent downloader = new Intent(context, AlarmReceiver.class);
		PendingIntent recurringDownload = PendingIntent.getBroadcast(context,
				0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(),
				AlarmManager.INTERVAL_FIFTEEN_MINUTES, recurringDownload);
	}

	/**
	 * In this method the display name is saved to database and registration
	 * process is started if not registered else data is displayed in offline
	 * mode
	 * 
	 * @return true is data saved successfully elese false
	 * @since version 1.0
	 */
	boolean saveData() {
		boolean dbSaveStatus = true;
		ClientConnectionConfig._ADDRESS = editBoxAddress.getText().toString()
				.trim();
		ClientConnectionConfig._ASSETID = editBoxAssetId.getText().toString()
				.trim();
		ClientConnectionConfig._DISPLAYNAME = editBoxDisplayName.getText()
				.toString().trim();

		if (ClientConnectionConfig._DISPLAYNAME.equals("")) {
			Toast tStatus = Toast.makeText(AppMain.this,
					getString(R.string.enter_diplay_name), Toast.LENGTH_SHORT);
			tStatus.show();

		} else {
			LogData.getInstance().setDisplayname(getApplicationContext(),
					ClientConnectionConfig._DISPLAYNAME);
			LogData.getInstance().setAssetID(getApplicationContext(),
					ClientConnectionConfig._ASSETID);
			LogData.getInstance().setAppID(getApplicationContext(),
					ClientConnectionConfig._HARDWAREKEY);
			startApp();
		}

		return dbSaveStatus;
	}

	/**
	 * This method gets MacId of the STB
	 * 
	 * @since version 1.0
	 */
	private void getMacaddress() {
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		String address = "";
		if (wifiManager.isWifiEnabled()) {
			// WIFI ALREADY ENABLED. GRAB THE MAC ADDThis method HERE
			WifiInfo info = wifiManager.getConnectionInfo();
			address = info.getMacAddress();
			address = trimStringText(address, "%");
			textViewMacId.setText(address, BufferType.NORMAL);
		} else {
			// ENABLE THE WIFI FIRST
			wifiManager.setWifiEnabled(true);

			// WIFI IS NOW ENABLED. GRAB THE MAC ADDRESS HERE
			WifiInfo info = wifiManager.getConnectionInfo();
			address = info.getMacAddress();
			address = trimStringText(address, "%");
			textViewMacId.setText(address, BufferType.NORMAL);
			wifiManager.setWifiEnabled(false);
		}
		LogData.getInstance().setMacAddress(AppMain.getAppMainContext(),
				address);

	}

	/**
	 * In this method the string is trimmed till a specific string separator
	 * 
	 * @param strToTrim
	 *            string To Trim
	 * @param separator
	 *            string separator
	 * @return trimmed string
	 * @since version 1.0
	 */
	private String trimStringText(String strToTrim, String separator) {
		String temp = strToTrim;
		int indx = temp.indexOf(separator);
		if (indx > -1) {
			temp = temp.substring(0, indx);
		}

		return temp;

	}

	/**
	 * In this method IpAddress(IP4 or IP6) of the network connected is fetched
	 * 
	 * @since version 1.0
	 */
	public String getIpAddress() {
		String address = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					address = inetAddress.getHostAddress().toString();
					if (!inetAddress.isLoopbackAddress()) {
						address = inetAddress.getHostAddress().toString();
						address = trimStringText(address, "%");
						LogData.getInstance().setIpAddress(
								AppMain.getAppMainContext(), address);
						return address;
					}
				}
			}
		} catch (Exception ex) {
			Log.e("IP Address", ex.toString());
		}
		address = trimStringText(address, "%");
		LogData.getInstance()
				.setIpAddress(AppMain.getAppMainContext(), address);
		return address;
	}

	/**
	 * In this method the initial textview value is initialised
	 * 
	 * @param androidID
	 *            device id or androidID
	 * @since version 1.0
	 */
	private void setInitialValue(String androidID) {
		initializeLocationManager();
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int layoutWidth = size.x;
		int layoutHeight = size.y;
		textViewResolution.setText("width = " + "" + layoutWidth + "\t "
				+ "height = " + "" + layoutHeight, BufferType.NORMAL);
		String regId = androidID;// Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);

		// String verClientStr =
		// DataCacheManager.getInstance().readSettingData(IDisplayLayout._KEY_XVIDIA_CLIENT_VERSION);
		textViewRegID.setText(regId, BufferType.NORMAL);
		getMacaddress();
		textViewIpAddress.setText(getIpAddress(), BufferType.NORMAL);
		LogData.getInstance().setNetworkType(AppMain.getAppMainContext(),
				NetworkUtil.getConnectivityStatusString(this));
		connectionStatus.setText(NetworkUtil.getConnectivityStatusString(this),
				BufferType.NORMAL);
	}

	static Context context = null;

	/**
	 * 
	 * @return returns AppMain Context
	 * @see AppMain
	 * @since version 1.0
	 */
	public static Context getAppMainContext() {
		return context;

	}

	public boolean noChange = false;
	public boolean downloadFailed = false;

	/**
	 * On Backpress a pop up is displayed for authentication to exit from the
	 * application
	 * 
	 * @since version 1.0
	 */
	@Override
	public void onBackPressed() {
		try {
			cancelPopup = true;
			sendAppStatusRequest(LogData.STB_ON);
			// initiatePopupWindow();

			// if(handler != null){
			// handler.removeCallbacks(runnableBackground);
			// handler = null;
			// }
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception ex) {
		}
	}

	@Override
	protected void onPause() {
		try {
			// if(!cancelPopup){
			// initiatePopupWindow();
			// }
			// boolean logFlag =
			// LogData.getInstance().setAppStatus(getApplicationContext(),LogData.STB_ON);
			//
			// if (logFlag) {
			// sendLogRequest(
			// AppMain.getAppMainContext(),
			// LogUtility.getInstance().getAppStatusJSON(
			// AppMain.getAppMainContext()),
			// LogUtility.getAppStatusLogUrl());
			// }
			if (refreshHandler != null) {
				refreshHandler.removeCallbacks(refreshRunnable);
			}
			if (!AppState.ACTION_ON_PAUSE) {
				sendAppStatusRequest(LogData.STB_ON);
				AppState.ACTION_ON_PAUSE = true;
			}
			// LogData.getInstance().setAppStatus(getAppMainContext(),
			// LogData.STB_ON);
			// DataCacheManager.getInstance().saveSettingData(_KEY_XVIDIA_APP_STATE,
			// LogData.STB_ON);
			super.onPause();
		} catch (Exception ex) {
		}
	}

	@Override
	protected void onResume() {
		try {
			// if(!cancelPopup){
			// initiatePopupWindow();
			// }
			refreshScreen();
			if (!AppState.BACKPRESSED_SIGNAGE_SCREEN)
				sendAppStatusRequest(LogData.APP_ON);
			// LogData.getInstance().setAppStatus(getAppMainContext(),
			// LogData.APP_ON);
			// DataCacheManager.getInstance().saveSettingData(_KEY_XVIDIA_APP_STATE,
			// LogData.APP_ON);
			super.onResume();
		} catch (Exception ex) {
		}
	}

	void sendAppStatusRequest(String appStatus) {
		boolean logFlag = LogData.getInstance().setAppStatus(
				getApplicationContext(), appStatus);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date now = new Date();
		String data = formatter.format(now);
		boolean appState = false;
		if (appStatus.equalsIgnoreCase(LogData.APP_ON)) {
			appState = true;
		}
		if (logFlag) {
			String id = LogData.getInstance().getAppID(
					AppMain.getAppMainContext())
					+ "_" + LogData.TAG_ONOFF_APP + "_" + now.getTime();
			OnOffTimeData object = new OnOffTimeData();
			object.setId(id);
			object.setBoxId(LogData.getInstance().getAppID(
					AppMain.getAppMainContext()));
			if (appState) {
				object.setOnTime(data);
				object.setOffTime("");
			} else {
				object.setOnTime("");
				object.setOffTime(data);
			}
			DataCacheManager.getInstance(AppMain.getAppMainContext())
					.saveOnOffTimeData(
							MySqliteHelper.TABLE_NAME_ONOFF_TIME_APP_TABLE,
							object);

			if (LogUtility.checkNetwrk(AppMain.getAppMainContext())) {
				// RequestQueue mRequestQue =
				// VolleySingleton.getInstance().getRequestQueue();
				String url = new ServiceURLManager()
						.getUrl(IAPIConstants.API_KEY_ONOFF_APP);
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
				JsonObjectRequest request = new JsonObjectRequest(
						Request.Method.POST, url, jsonRequest,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								ObjectMapper mapper = new ObjectMapper();
								OnOffTimeData obj = null;
								try {
									obj = mapper.readValue(response.toString(),
											OnOffTimeData.class);
								} catch (IOException e) {
									e.printStackTrace();
								}
								String deviceId = obj.getId();
								if (!deviceId.isEmpty()) {
									DataCacheManager
											.getInstance(
													AppMain.getAppMainContext())
											.removeOnOffTimeById(
													MySqliteHelper.TABLE_NAME_ONOFF_TIME_APP_TABLE,
													deviceId);
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
							}
						});
				VolleySingleton.getInstance(MyApplication.getAppContext())
						.addToRequestQueue(request);
			}

		}
	}

	@Override
	protected void onRestart() {
		try {
			// if(!cancelPopup){
			// initiatePopupWindow();
			// }
			// LogData.getInstance().setAppStatus(getAppMainContext(),
			// LogData.APP_ON);
			// boolean logFlag =
			// LogData.getInstance().setAppStatus(getApplicationContext(),LogData.APP_ON);
			//
			// if (logFlag) {
			// sendLogRequest(
			// AppMain.getAppMainContext(),
			// LogUtility.getInstance().getAppStatusJSON(
			// AppMain.getAppMainContext()),
			// LogUtility.getAppStatusLogUrl());
			// }
			sendAppStatusRequest(LogData.APP_ON);
			// DataCacheManager.getInstance().saveSettingData(_KEY_XVIDIA_APP_STATE,
			// LogData.APP_ON);
			super.onRestart();
		} catch (Exception ex) {
		}
	}

	void showProgress() {
		try {
			dialog = new Dialog(AppMain.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					layoutWidth - 15, ViewGroup.LayoutParams.WRAP_CONTENT);
			param.gravity = Gravity.CENTER;
			View popupView = getLayoutInflater().inflate(
					R.layout.progressdialog, null);
			dialog.setContentView(popupView, param);
			dialog.setCancelable(false);

			cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
			dialogtext = (TextView) dialog.findViewById(R.id.tv1);
			pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);

			pb.setProgress(0);
			pb.setProgressDrawable(context.getResources().getDrawable(
					R.drawable.green_progress));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateShowProgress(String file_path, String message) {
		try {
			cur_val.setText("Starting download...");
			dialog.setTitle(message);

			// TextView text = (TextView) dialog.findViewById(R.id.tv1);
			// text.setText("Downloading file from ... " + file_path);
			// pb.setProgress(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * TImer is initialised to run for a specific time
	 * 
	 * @param milSec
	 *            time for which the timer should run
	 * @since version 1.0
	 */
	private void startTimer(int milSec) {
		try {
			if (handler != null) {
				handler.removeCallbacks(runnableBackground);
				handler = null;
			}
			runnableBackground = new Runnable() {
				@Override
				public void run() {
					if (!ClientConnectionConfig._HARDWAREKEY.equals("")) {
						if (AppState.BACKPRESSED_SIGNAGE_SCREEN) {
							cancelPopup = true;
							// StateMachine.gi(AppMain.this).initProcess(true,
							// StateMachine.REGISTER);
							startApp();
						}
					} else {
						registerDevice();
					}
					if (handler != null) {
						handler.removeCallbacks(runnableBackground);
						handler = null;
					}
				}
			};
			handler = new Handler();
			handler.postDelayed(runnableBackground, milSec);
		} catch (Exception e) {

		}
	}

	void stopPackageProcess(final String packageName, final Context context) {

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				final ActivityManager am = (ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE);

				List<ActivityManager.RunningAppProcessInfo> mTasks = am
						.getRunningAppProcesses();

				for (int i = 0; i < mTasks.size(); i++) {
					String name = mTasks.get(i).processName;
					// .getComponent().getPackageName();

					if (name.equals(packageName)) {
						int id = mTasks.get(i).pid;
						android.os.Process.killProcess(id);
						// ActivityManager am = (ActivityManager)
						// getSystemService(Context.ACTIVITY_SERVICE);
						am.killBackgroundProcesses(packageName);
						break;
					}
				}
			}
		}, 5000);

	}

	void openLinkInChrome(String urlToOpen) {
		try {
			// Intent intent = new Intent(Intent.ACTION_VIEW);
			// i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
			// i.addCategory("android.intent.category.LAUNCHER");
			// i.setData(Uri.parse(urlToOpen));
			// startActivity(i);
			final String appPackageName = "com.android.chrome";// "xdk.intel.blank.ad.template.crosswalk";
			Intent intent = new Intent("android.intent.action.VIEW",
					Uri.parse(urlToOpen));
			// Intent intent =
			// getPackageManager().getLaunchIntentForPackage(appPackageName);
			if (intent != null) {

				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// intent.setClassName(appPackageName,"Main");
				// intent.addCategory("android.intent.category.LAUNCHER");
				// intent.setComponent(new ComponentName(appPackageName,
				// "com.android.chrome.Main"));
				// intent.setAction("android.intent.action.VIEW");
				// intent.addCategory("android.intent.category.BROWSABLE");
				// intent.setData(Uri.parse(urlToOpen));
				startActivity(intent);

			}
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// Chrome is probably not installed
		}
	}

	/**
	 * PopUp is initialised
	 * 
	 * @since version 1.0
	 */
	private void initiatePopupWindow() {
		try {
			startTimer(TIMER_TIME);
			// if(!cancelPopup){
			// We need to get the instance of the LayoutInflater
			View popupView = getLayoutInflater().inflate(R.layout.screen_popup,
					null);
			int width = 500;
			int height = 550; // getResources().getDimensionPixelSize(R.dimen.seekbar_width);
			pwindo = new PopupWindow(popupView, width, height, true);
			pwindo.showAtLocation(popupView, Gravity.CENTER, 0, 0);
			// If the PopupWindow should be focusable
			pwindo.setFocusable(true);
			userName = (EditText) popupView.findViewById(R.id.username);
			password = (EditText) popupView.findViewById(R.id.password);
			Button btnCancel = (Button) popupView
					.findViewById(R.id.button_cancel);
			Button btnSubmit = (Button) popupView
					.findViewById(R.id.button_submit);
			btnCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (handler != null) {
						handler.removeCallbacks(runnableBackground);
						handler = null;
					}
					pwindo.dismiss();
				}
			});
			btnSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (handler != null) {
						handler.removeCallbacks(runnableBackground);
						handler = null;
					}
					String username = userName.getText().toString();
					String passwod = password.getText().toString();
					if ((!username.isEmpty() && username
							.equalsIgnoreCase("admin"))
							&& (!passwod.isEmpty() && passwod
									.equalsIgnoreCase("admin"))) {
						android.os.Process.killProcess(android.os.Process
								.myPid());
					} else {
						Toast.makeText(AppMain.this,
								"Enter valid username/passwor",
								Toast.LENGTH_SHORT).show();
					}
					pwindo.dismiss();

				}
			});
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Location Listener is registered. It is invoked whenever location is
	 * changed if location permission is granted
	 * 
	 * @author Ravi@xvidia
	 * @since version 1.0
	 */
	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			String lat = String.valueOf(location.getLatitude());
			String longitude = String.valueOf(location.getLongitude());
			textViewLocation.setText(lat + ", " + longitude, BufferType.NORMAL);
			boolean logFlag = LogData.getInstance().setLocation(
					AppMain.getAppMainContext(), lat + "," + longitude);

			if (logFlag) {
				LocationData locationData = new LocationData();
				locationData.setId(LogData.getInstance().getAppID(
						AppMain.getAppMainContext()));
				locationData.setLat(lat);
				locationData.setLongitude(longitude);
				sendLocationRequest(AppMain.getAppMainContext(), locationData);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	}

	private class InternetAccessible extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {

			if (LogUtility.checkNetwrk(getAppMainContext())) {
				if (isInternetAccessible(getAppMainContext())) {
					return 1;
				} else {
					return 0;
				}
			} else {
				return -1;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			int colorCode = R.color.greenEnd;
			String message = NetworkUtil
					.getConnectivityStatusString(getAppMainContext());
			if (result == 1) {
				colorCode = R.color.greenEnd;
				;
			} else if (result == 0) {
				colorCode = R.color.orange_color;
				message = message + " but no internet access";
			} else {
				colorCode = R.color.red_color;
			}
			connectionStatus.setTextColor(getResources().getColor(colorCode));
			connectionStatus.setText(message, BufferType.NORMAL);
		}
	}
}