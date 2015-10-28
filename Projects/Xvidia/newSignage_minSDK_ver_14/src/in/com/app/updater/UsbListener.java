package in.com.app.updater;
import in.com.app.IDisplayLayout;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
 
/**
 * This class extends {@link BroadcastReceiver} to update info abt attached USB
 * @author Ravi@xvidia
 * @since Version 1.0
 */
public class UsbListener extends BroadcastReceiver {
	@Override
    public void onReceive(Context context, Intent intent) {
		 String action = intent.getAction();

	        UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);


	        String message = ", action:"+action+", device:"+device.getProductId()+", Vendor:"+device.getVendorId();

	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
			Date now = new Date();
	        Log.i(device.getDeviceName(), ""+device.getDeviceProtocol());
	            if(action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED))
	            {
	    			String data =  "Time: "+formatter.format(now) + message;
	            	
	            	DataCacheManager.getInstance(context).saveSettingData(IDisplayLayout._KEY_XVIDIA_STATE_USB_ATTACH_TIME, data);
//	            	Toast.makeText(context, "USB CONNECTED"+message, 3000).show();   
	            }
	            if(action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED))
	            {
	    			String data =  "Time: "+formatter.format(now) + message;
	            	
	            	DataCacheManager.getInstance(context).saveSettingData(IDisplayLayout._KEY_XVIDIA_STATE_USB_DETACH_TIME, data);
//	            	
//	            	Toast.makeText(context, "USB Disconected "+message, 3000).show();
	            }
	            getDeviceList(context);
	}
	
	private void getDeviceList(Context context){
		UsbManager manager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
//		HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
//		UsbDevice device = deviceList.get("deviceName");
		HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
		Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
		while(deviceIterator.hasNext()){
		    UsbDevice device = deviceIterator.next();
		    Log.i(device.getDeviceName(), ""+device.getDeviceProtocol());
		    //your code
		}
	}
}