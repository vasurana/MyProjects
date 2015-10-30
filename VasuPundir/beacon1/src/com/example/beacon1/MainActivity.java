package com.example.beacon1;

import java.util.ArrayList;

import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;
import com.sensoro.beacon.kit.Beacon;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	SensoroManager sensoroManager;
	ListView listView;
	ArrayAdapter<String> adapter;
	FrameLayout layoutBeacon;
	LinearLayout layoutList, layoutBeacon2, layoutWhenBlank;
	TextView tvDistance1, tvDistance2, tvNearestBeacon, tvOffer, tvInfo, tvListHeader;
	String beaconDistance1;
	String beaconDistance2;
	double distance1 = 0.00;
	double distance2 = 0.00;
	String userName;
	ProgressDialog dialog;
	ImageView iv;
	Handler mHandler = new Handler();
	Vibrator vibrator;
	Notification AdidasNote, NikeNote;
	NotificationManager mgr;
	static private boolean first = false;
	static private boolean second = false;
	Runnable offer;
	String infoNike, infoAdidas, listHeader;
	Runnable adidas = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mgr.notify(8888, AdidasNote);
			vibrator.vibrate(300);
			tvOffer.setText("Use your COUPON to avail 50% off on Adidas products. HURRY!!");
			tvOffer.setTextSize(22);
			tvOffer.setVisibility(View.VISIBLE);
			/* tvOffer.setBackgroundColor(Color.BLACK); */
		}
	};
	Runnable nike = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mgr.notify(8888, NikeNote);
			vibrator.vibrate(300);
			tvOffer.setText("Use your COUPON to avail 45% off on Nike products. HURRY!!");
			tvOffer.setTextSize(22);
			tvOffer.setVisibility(View.VISIBLE);
			/* tvOffer.setBackgroundColor(Color.BLACK); */
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// to make some notification
		mgr = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		AdidasNote = new Notification(R.drawable.notification,
				"50% off on Adidas shoes. Hurry!!", System.currentTimeMillis());
		NikeNote = new Notification(R.drawable.notification,
				"45% off on Nike T-shirts. Hurry!!", System.currentTimeMillis());

		PendingIntent OpenNotificationActivity = PendingIntent.getActivity(
				this, 0, new Intent(this, MainActivity.class), 0);
		AdidasNote.setLatestEventInfo(this, "Adidas Offer!!!",
				"50% off on Adidas shoes. Hurry !!", OpenNotificationActivity);

		NikeNote.setLatestEventInfo(this, "Nike Offer!!!",
				"45% off on Nike T-shirts. Grab them. Hurry!",
				OpenNotificationActivity);

		getSupportActionBar().hide();
		listView = (ListView) findViewById(R.id.list);
		String[] values = new String[] { "Adidas Offer 50% off",
				"Nike Offer 45% off", "Reebok Offer 50% off",
				"Reebok Classic Offer 50% off", "Puma Offer 50% off",
				"UCB Offer 50% off", "Bata Offer 50% off",
				"Armani Offer 50% off" };
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		layoutBeacon = (FrameLayout) findViewById(R.id.layoutBeacon);
		layoutBeacon2 = (LinearLayout) findViewById(R.id.layoutBeacon2);
		layoutList = (LinearLayout) findViewById(R.id.listLayout);
		//layoutWhenBlank = (LinearLayout) findViewById(R.id.ifBlank);
		tvListHeader = (TextView) findViewById(R.id.listHeader);
		tvOffer = (TextView) findViewById(R.id.offer);
		tvOffer.setVisibility(View.GONE);
		tvInfo = (TextView) findViewById(R.id.info);
		tvInfo.setVisibility(View.GONE);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		iv = (ImageView) findViewById(R.id.image);

		UserDatabase entry = new UserDatabase(MainActivity.this);

		entry.open();
		userName = entry.getUserDetails();
		entry.close();

		infoAdidas = "Hello "
				+ userName
				+ ". Use your coupon shown here to avail your offer. Hope you enjoy the time."+ "<b>"+" COUPON: OFFER50"+"</b>";
		infoNike = "Hello "
				+ userName
				+ ". Use your coupon shown here to avail your offer. Hope you enjoy the time."+"<b>"+" COUPON: OFFER45"+"</b>";
		listHeader ="<b>" +"Today's special offers for you."+"</b>";
		tvListHeader.setTextSize(19);
		tvListHeader.setText(Html.fromHtml(listHeader));
		
		layoutList.setVisibility(View.GONE);

		Intent bluetoothIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(bluetoothIntent, 0);

		sensoroManager = SensoroManager.getInstance(getBaseContext());
		sensoroManager.setBackgroundBetweenScanPeriod(120000);
		sensoroManager.setBackgroundScanPeriod(30000);

		if (sensoroManager.isBluetoothEnabled()) {
			/**
			 * Enable cloud service (upload sensor data, including battery
			 * status, UMM, etc.)。Without setup, it keeps in closed status as
			 * default.
			 **/
			sensoroManager.setCloudServiceEnable(true);

			try {
				sensoroManager.startService();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		BeaconManagerListener beaconManagerListener = new BeaconManagerListener() {

			@Override
			public void onUpdateBeacon(
					ArrayList<com.sensoro.beacon.kit.Beacon> beacons) {
				// TODO Auto-generated method stub

				for (com.sensoro.beacon.kit.Beacon beacon : beacons) {

					if (beacon.getAccuracy() > 5) {
						runOnUiThread(new Runnable() {
							public void run() {
								 dialog.dismiss();
								layoutBeacon.setVisibility(View.GONE);
								layoutBeacon2.setVisibility(View.GONE);
								layoutList.setVisibility(View.VISIBLE);
								listView.setAdapter(adapter);
							}
						});

					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub

						         dialog.dismiss();
								layoutList.setVisibility(View.GONE);
								layoutBeacon.setVisibility(View.VISIBLE);
								layoutBeacon2.setVisibility(View.VISIBLE);
							}
						});
						if (beacon.getSerialNumber().equals("0117C5377507")) {
							distance1 = beacon.getAccuracy();
							// beaconDistance1 = Double.toString(distance1);
						}
						if (beacon.getSerialNumber().equals("0117C5382EAD")) {
							distance2 = beacon.getAccuracy();
							// beaconDistance2 = Double.toString(distance2);
						}
						if (distance1 > distance2) {

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub

									if (first == false) {
										tvOffer.setVisibility(View.GONE);
										first = true;
										second = false;
										vibrator.vibrate(500);
										tvInfo.setVisibility(View.VISIBLE);
										tvInfo.setText(Html.fromHtml(infoAdidas));
										tvInfo.setTextSize(19);
										tvInfo.setBackgroundColor(Color.WHITE);
										iv.setImageResource(R.drawable.adidas);
										if (mHandler != null) {
											mHandler.removeCallbacks(offer);
										}
										offer = adidas;
										mHandler.postDelayed(offer, 10000L);

									}
								}
							});
						} else {

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub

									if (second == false) {
										tvOffer.setVisibility(View.GONE);
										second = true;
										first = false;
										vibrator.vibrate(500);
										tvInfo.setVisibility(View.VISIBLE);
										tvInfo.setText(Html.fromHtml(infoNike));
										tvInfo.setTextSize(19);
										tvInfo.setBackgroundColor(Color.WHITE);
										iv.setImageResource(R.drawable.nike);
										if (mHandler != null) {
											mHandler.removeCallbacks(offer);
										}
										offer = nike;
										mHandler.postDelayed(offer, 10000L);
									}
								}
							});

						}
					}
				}

			}

			@Override
			public void onNewBeacon(com.sensoro.beacon.kit.Beacon beacon) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGoneBeacon(com.sensoro.beacon.kit.Beacon beacon) {
				// TODO Auto-generated method stub

			}
		};
		
		 dialog = new ProgressDialog(MainActivity.this);
         dialog.setMessage("Taking long enough? Press Home. We'll notify you later.");
         dialog.show();
         sensoroManager.setBeaconManagerListener(beaconManagerListener);
         
		/*if(){
			sensoroManager.setBeaconManagerListener(beaconManagerListener);
		}else{
			layoutBeacon.setVisibility(View.GONE);
			layoutList.setVisibility(View.VISIBLE);
			listView.setAdapter(adapter);
		}*/

	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Exit")
				.setMessage("Do you really want to exit ?")
				.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								android.os.Process
										.killProcess(android.os.Process.myPid());
								System.exit(0);
							}
						}).setNegativeButton("NO", null).show();

	}

	@Override
	protected void onPause() {
		super.onPause();
		/*
		 * try { if (sensoroManager != null) sensoroManager.stopService();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}
}
