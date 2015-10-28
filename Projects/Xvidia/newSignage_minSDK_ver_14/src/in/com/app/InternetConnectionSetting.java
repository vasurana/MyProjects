package in.com.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import in.com.app.utility.MyExceptionHandler;

/**
 * Created by Ravi_office on 17-Sep-15.
 */
public class InternetConnectionSetting extends Activity {
    ListView wifiListview;
    Button scanWifiButton;
    ToggleButton wifiOnOff;
    int currentApiVersion;
    WifiList_BaseAdapter listAdapter;
    WifiManager wifi;
    int size = 0;
    static String ssid;
    static String passkey;
   static List<ScanResult> results;
    private static Activity activity;
    TextView scanning_textView;
    BroadcastReceiver wifiBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
        setContentView(R.layout.wifi_list);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, AppMain.class));
        activity = this;
        scanning_textView = (TextView) findViewById(R.id.wifi_scanning);
        wifiListview = (ListView) findViewById(R.id.wifiList);
        scanWifiButton = (Button) findViewById(R.id.wifi_scan_btn);
        wifiOnOff = (ToggleButton) findViewById(R.id.wifi_on_off);
        wifiOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                    setWifiStatus(isChecked);
            }
        });
        scanWifiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (wifi.isWifiEnabled()) {
                    wifi.startScan();
                    size = size - 1;
                    if (size > 0) {
                        scanning_textView.setVisibility(View.GONE);
                        wifiListview.setVisibility(View.VISIBLE);
                        setWifiList();
                    } else {
                        scanning_textView.setVisibility(View.VISIBLE);
                        wifiListview.setVisibility(View.GONE);
                    }
                    while (size >= 0) {
                        size--;
                        listAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        initialiseList();
    }

    private void setWifiStatus(boolean onOff){
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(onOff) {
            if (wifi.isWifiEnabled() == false) {
                Toast.makeText(getApplicationContext(),"Wifi is disabled. It is being enabled.", Toast.LENGTH_LONG).show();
            }
            wifi.setWifiEnabled(true);
            scanning_textView.setVisibility(View.GONE);
            wifiListview.setVisibility(View.VISIBLE);
            wifiOnOff.setChecked(true);
            wifiOnOff.setBackgroundColor(getResources().getColor(R.color.greenEnd));
        }else{

            Toast.makeText(getApplicationContext(),"Wifi is disabled.", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(false);
            wifiOnOff.setChecked(false);
            wifiOnOff.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            scanning_textView.setVisibility(View.GONE);
            wifiListview.setVisibility(View.GONE);
        }
    }
    private void initList(){
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(),
                    "Wifi is disabled. It is being enabled.", Toast.LENGTH_LONG)
                    .show();
            wifi.setWifiEnabled(true);
        }

        setWifiStatus(true);
        wifi.startScan();

        try {
            size = size - 1;
            if (size > 0) {
                scanning_textView.setVisibility(View.GONE);
                wifiListview.setVisibility(View.VISIBLE);
                setWifiList();
            }else{
                scanning_textView.setVisibility(View.VISIBLE);
                wifiListview.setVisibility(View.GONE);
            }
            while (size >= 0) {
                size--;

                listAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
        }

    }
    private void initialiseList(){
        initList();
        wifi.startScan();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                wifi.startScan();
                size = size - 1;
                if (size > 0) {
                    scanning_textView.setVisibility(View.GONE);
                    wifiListview.setVisibility(View.VISIBLE);
                    setWifiList();
                } else {
                    scanning_textView.setVisibility(View.VISIBLE);
                    wifiListview.setVisibility(View.GONE);
                }
                while (size >= 0) {
                    size--;
                    listAdapter.notifyDataSetChanged();
                }
            }
        }, 1000);
    }

    private void setWifiList() {
        try {
                wifiListview.setVisibility(View.VISIBLE);
                listAdapter = new WifiList_BaseAdapter(this, results);
                wifiListview.setAdapter(listAdapter);
                wifiListview.setItemsCanFocus(false);
                wifiListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            ((BaseAdapter) wifiListview.getAdapter()).notifyDataSetChanged();

        } catch (Exception ex) {
        }
    }
    private void registerBroadcastReciever(){
        wifiBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                results = wifi.getScanResults();
                size = results.size();
            }
        };

        registerReceiver(wifiBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }
    private void unRegisterBroadcastReciever(){
        if(wifiBroadcastReceiver != null){
            unregisterReceiver(wifiBroadcastReceiver);
        }
    }
    public static void initialiseIntervalRoundDialog(final int position){
        ScanResult obj = null;
        if(results != null && results.size()>position) {
            obj = results.get(position);
        }else{
            return;
        }
        View popupView = activity.getLayoutInflater().inflate(R.layout.wifi_credential_dialog, null);
        final TextView wifiTextName = (TextView) popupView.findViewById(R.id.dialog_wifi_name);
        final EditText wifiPassword = (EditText) popupView.findViewById(R.id.editText_wifi_password);
        final Button cancelButton = (Button) popupView.findViewById(R.id.wifi_cancel_btn);
        final Button connectButton = (Button) popupView.findViewById(R.id.wifi_connect_btn);

        ssid = obj.SSID;
        wifiTextName.setText(ssid);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int layoutWidth = size.x;
        int layoutHeight = size.y;
        final PopupWindow popupWindow = new PopupWindow();
        popupWindow.setContentView(popupView);
        popupWindow.setWindowLayoutMode(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(layoutHeight);
        popupWindow.setWidth(layoutWidth);
        popupWindow.setFocusable(true);


        wifiPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //initialiseInternalCamera();
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        passkey = wifiPassword.getText().toString();
                        hideSoftKeyboard(wifiPassword);
                        break;
                }
                return false;
            }


        });
        wifiPassword.clearFocus();
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        connectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(passkey.isEmpty()||ssid.isEmpty()){
                    Toast.makeText(activity, "Please enter valid wifi data", Toast.LENGTH_LONG).show();
                }
                connectToAP1(ssid, passkey);
                popupWindow.dismiss();
            }
        });

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    /**
     * This function hides the soft Keyboard
     * @param editbox editbox -reference to editext for which soft keyboard is to be hidden
     * @since version 1.0
     */
    private static void hideSoftKeyboard(EditText editbox){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editbox.getWindowToken(), 0);
    }
    public static void connectToAP1(String ssid, String passkey) {
       new InternetConnectionSetting().connectToAP(ssid, passkey, activity) ;
    }
    public void connectToAP(String ssid, String passkey,Context ctx) {
//	    Log.i(TAG, "* connectToAP");

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        WifiManager wifiManager = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
        String networkSSID = ssid;
        String networkPass = passkey;

//	    Log.d(TAG, "# password " + networkPass);
        if(results != null && results.size()>0){
            for (ScanResult result : results) {
                if (result.SSID.equals(networkSSID)) {

                    String securityMode = getScanResultSecurity(result);

                    if (securityMode.equalsIgnoreCase("OPEN")) {

                        wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        int res = wifiManager.addNetwork(wifiConfiguration);
//	                Log.d(TAG, "# add Network returned " + res);

                        wifiManager.enableNetwork(res, true);
//	                Log.d(TAG, "# enableNetwork returned " + b);

                        wifiManager.setWifiEnabled(true);

                    } else if (securityMode.equalsIgnoreCase("WEP")) {

                        wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                        wifiConfiguration.wepKeys[0] = "\"" + networkPass + "\"";
                        wifiConfiguration.wepTxKeyIndex = 0;
                        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        int res = wifiManager.addNetwork(wifiConfiguration);
//	                Log.d(TAG, "### 1 ### add Network returned " + res);

                        wifiManager.enableNetwork(res, true);
//	                Log.d(TAG, "# enableNetwork returned " + b);

                        wifiManager.setWifiEnabled(true);
                    }

                    wifiConfiguration.SSID = "\"" + networkSSID + "\"";
                    wifiConfiguration.preSharedKey = "\"" + networkPass + "\"";
                    wifiConfiguration.hiddenSSID = true;
                    wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                    wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

                    int res = wifiManager.addNetwork(wifiConfiguration);

                    wifiManager.enableNetwork(res, true);

                    boolean changeHappen = wifiManager.saveConfiguration();

                    wifiManager.setWifiEnabled(true);
                    if(res != -1 && changeHappen){

                        if(!checkNetwrk())
                            Toast.makeText(ctx, "wifi password is set but no internet access. Restart the router", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(ctx, "wifi is connected", Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        Intent i = new Intent(ctx, InternetConnectionSetting.class);
                        startActivity(i);
                        finish();
                    }


                }
            }
        }else{
            getWifiList();
        }
    }

    public String getScanResultSecurity(ScanResult scanResult) {
//	    Log.i(TAG, "* getScanResultSecurity");

        final String cap = scanResult.capabilities;
        final String[] securityModes = { "WEP", "PSK", "EAP" };

        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                return securityModes[i];
            }
        }

        return "OPEN";
    }

    boolean checkNetwrk(){
        boolean nwFlag = false;
        try{
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                nwFlag = true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return nwFlag;
    }

    private void getWifiList(){
        wifi.startScan();
        if(results == null || results.size()<1){
            getWifiList();

        }else{
            connectToAP(ssid,passkey,activity) ;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReciever();

    }
    @Override
    protected void onPause() {
        super.onPause();
        unRegisterBroadcastReciever();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(InternetConnectionSetting.this, AppMain.class);
        startActivity(i);
        finish();
    }
}
    class  WifiList_BaseAdapter extends BaseAdapter{
        public static Context mContext;
        public static LayoutInflater inflater = null;
        public static List<ScanResult> results;


        public WifiList_BaseAdapter(Context context,List<ScanResult> resultsList) {
            mContext = context;
                results = resultsList;
                inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                if(results != null){
                    return results.size();
                }else{
                    return 0;
                }
            }

            @Override
        public Object getItem(int position) {
            return null ;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;

            if (convertView == null) {
                view	 = inflater.inflate(R.layout.wifi_list_items, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) view.findViewById(R.id.wifi_network_name);

                view.setTag(viewHolder);
                viewHolder.textView.setTag(results.get(position).SSID);

            } else {
                view = convertView;
                ((ViewHolder) view.getTag()).textView.setTag(results.get(position).SSID);
            }

            ViewHolder holder = (ViewHolder) view.getTag();
            holder.textView.setText(results.get(position).SSID);
//            holder.textView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    InternetConnectionSetting.initialiseIntervalRoundDialog(position);
//                }
//            });
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    InternetConnectionSetting.initialiseIntervalRoundDialog(position);
                }
            });
            return view;
        }

        class ViewHolder{
            TextView textView;
        }
    }
