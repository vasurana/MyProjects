package in.com.app.storage.caching.sqlight.manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class DataCaching {

	private static DataCaching caching;
	public static final String DATA = "data";
	public static final String TIME = "time";
	public static final String CACHE_TIME = "cache";

	synchronized public static DataCaching getInstance() {
		if (caching == null) {
			caching = new DataCaching();

		}
		if (cachedData == null)
			cachedData = new HashMap<String, Object>();
		return caching;
	}

	static HashMap<String, Object> cachedData;

	synchronized private boolean saveObject(Object obj, Context context) {
		String filePath = "xvidia";
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		boolean keep = true;
		try {
			fos = context.openFileOutput(filePath, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (fos != null)
					fos.close();
			} catch (Exception e) { /* do nothing */
			}
		}

		return keep;
	}

	@SuppressWarnings("unchecked")
	synchronized private HashMap<String, Object> getObject(Context context) {
		String filePath = "xvidia";
		HashMap<String, Object> data = null;
		FileInputStream fis = null;
		ObjectInputStream is = null;
		try {
			fis = context.openFileInput(filePath);
			is = new ObjectInputStream(fis);
			data = (HashMap<String, Object>) is.readObject();
//			System.out.println("Read object : " + data);
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (is != null)
					is.close();
			} catch (Exception e) {
			}
		}

		return data;
	}

	public void savedata(String key, HashMap<String, Object> object,
			Context context) {
		
		Log.d(getClass().getName(),"key :" + key + "data" + object + "cached data : "
				+ cachedData);
		cachedData.put(key, object);
		saveObject(cachedData, context);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getData(String key, Context context) {

		HashMap<String, Object> object = null;
		try {
			cachedData = getObject(context);
			if (cachedData != null) {
				
				Log.d(getClass().getName(),"cached data not null");
				object = (HashMap<String, Object>) cachedData.get(key);
			} else {
				Log.d(getClass().getName(),"cached data null");
			}
		} catch (Exception e) {
			object = null;
		}
		return object;
	}

	public void savecachingTime(String key, String time, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, time);
		editor.commit();
	}

	public String getCachingTime(String key, Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String time = preferences.getString(key, "0");
		return time;
	}

	public void cacheData(String key, String cacheTime, Object data,
			Context context) {
		if (!cacheTime.equals("")) {
			HashMap<String, Object> cachedData = new HashMap<String, Object>();
			cachedData.put(DataCaching.DATA, data);
			cachedData.put(DataCaching.CACHE_TIME, cacheTime);
			cachedData.put(TIME, System.currentTimeMillis());
			DataCaching.getInstance().savedata(key, cachedData, context);
		}
	}

	public boolean isDataAvailable(HashMap<String, Object> data) {
		boolean dataAvailable = false;
		try {
			HashMap<String, Object> cached = data;
			long cachedTime = (Long) cached.get(TIME);
			if(cached.get(CACHE_TIME).equals(DataKey.DATA_AVAILABLE)){
				return true;
			}
			long actualTime = Integer.parseInt((String) cached.get(CACHE_TIME))
					* (60 * 1000);
			long currentTime = System.currentTimeMillis();
			if ((currentTime - cachedTime) < actualTime) {
				dataAvailable = true;
			}
		} catch (Exception e) {
			dataAvailable = false;
			e.printStackTrace();
		}
		return dataAvailable;
	}

	public interface DataKey {
//		public static String USER_PROFILE = "userprofile";
//		public static String POST_PAID_HOME = "post_home";
//		public static String PRE_PAID_HOME = "pre_home";
//		public static String LL_PAID_HOME = "ll_home";
//		public static String DSL_PAID_HOME = "dsl_home";
//		public static String DTH_PAID_HOME = "dth_home";
//		public static String POST_BIILDETAIL = "post_bill";
//		public static String LL_BIILDETAIL = "ll_bill";
//		public static String DSL_BIILDETAIL = "dsl_bill";
//		public static String POST_SR = "post_sr";
//		public static String PRE_SR = "pre_sr";
//		public static String LL_SR = "ll_sr";
//		public static String DSL_SR = "dsl_sr";
//		public static String DTH_SR = "dth_sr";
//		public static String POST_3G = "post_3g";
//		public static String PRE_3G = "pre_3g";
//		public static String POST_3G_ALL = "post_3g_all";
//		public static String PRE_3G_ALL = "pre_3g_all";
//		public static String DTH_REH = "dthrecharge";
//		public static String PRE_REH = "prerecharge";
//		public static String ORDER_GAME = "order_game";
//		public static String ORDER_MOVIE = "order_movie";
//		public static String POST_MNGSR = "postmngsr";
//		public static String PRE_MNGSR = "premngsr";
//		public static String MY_PKG = "mypkg";
//		public static String BILL_PLAN = "billplan";
//		public static String POST_HISTORY = "post_history";
//		public static String LL_HISTORY = "ll_history";
//		public static String DSL_HISTORY = "dsl_history";
//		
		public static String MSIDN = "msidn";
		public static String DATA_AVAILABLE = "##";
		public static String DEVICE_CLIENT_ID = "clindid";
//		
//		
	}

}
