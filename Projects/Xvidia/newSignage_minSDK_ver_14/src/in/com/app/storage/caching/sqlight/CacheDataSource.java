package in.com.app.storage.caching.sqlight;

import java.util.Date;
import java.util.Vector;

import in.com.app.data.MediaData;
import in.com.app.model.LayoutTimeData;
import in.com.app.model.MediaTimeData;
import in.com.app.model.OnOffTimeData;
import in.com.app.updater.LogUtility;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This is a class of that  manages (update/delete) data in sqlite database
 * @author Ravi@xvidia
 *	@since version 1.0
 */
public class CacheDataSource {
	private SQLiteDatabase database;
	private MySqliteHelper dbHelper;

	private Vector<MediaData> mediaList;
	private Vector<MediaTimeData> mediaTimeList;
	private Vector<LayoutTimeData> layoutTimeList;
	private Vector<OnOffTimeData> onOffTimeBoxList;
	private Vector<OnOffTimeData> onOffTimeAppList;
	private Vector<OnOffTimeData> onOffTimeScreenList;
	private static CacheDataSource INSTANCE = null;//new CacheDataSource();

	public static CacheDataSource getInstance(Context ctx) {
		if (INSTANCE == null)
			INSTANCE = new CacheDataSource(ctx);
		return INSTANCE;
	}

	private CacheDataSource(Context ctx) {
		dbHelper = MySqliteHelper.getInstance(ctx);
	}
	/**
	 * This method opens database
	 */
	public void open() throws SQLException {
		try {
			if (database==null)				
				database = dbHelper.getWritableDatabase();
			else if(!database.isOpen()){
				database = dbHelper.getWritableDatabase();
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method closes database
	 */
	public void close() {
//		Log.d("CachedDataSource", "Closed");
		dbHelper.close();
		database.close();
	}

	/**
	 * Thismethod saves setting data in setting databse
	 * @param rowId value set apiValue
	 * @param data apiData
	 * @return
	 */
	public void insertSettingData(int rowId, String data) {
		ContentValues values = new ContentValues();
		if (rowId > 0){
			values.put(MySqliteHelper.COLUMN_CACHING_API_ID, rowId);
			values.put(MySqliteHelper.COLUMN_CACHING_DATA, data);
		}
		if (rowId > 0){
			database.delete(MySqliteHelper.TABLE_NAME_SETTING, MySqliteHelper.COLUMN_CACHING_API_ID + "='" + rowId+"'", null);
		}
		database.insert(MySqliteHelper.TABLE_NAME_SETTING, null, values);
	}
	/**
	 * This method returns setting data for a particular apiId
	 * @param apiId
	 * @return string data
	 */
	public String getSettingData(int apiId) {
		String data = null;
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_SETTING, null,
				MySqliteHelper.COLUMN_CACHING_API_ID + "='" + apiId+"'", null, null,
				null, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			data = cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_CACHING_DATA));
			} else {
				removeSettingDataByApiId(apiId);
			}
		

		if (cursor != null)
			cursor.close();
		return data;
	}
	/**
	 * This method clears all setting database
	 */
	public void removeSettingData() {
		database.delete(MySqliteHelper.TABLE_NAME_SETTING, null, null);
	}

	/**
	 * This method removes single data from database for a particular id
	 * @param apiId
	 * @return boolean value true or false
	 */
	public boolean removeSettingDataByApiId(int apiId) {
		int deletedRows = database.delete(MySqliteHelper.TABLE_NAME_SETTING,
				MySqliteHelper.COLUMN_CACHING_API_ID + "='" + apiId+"'", null);
		if(deletedRows>0)
			return true;
		else
			return false;
	}
/////////////////////////////////
	///////////////////////
	//////////////
	

	public void insertMediaData(String fileName) {
		int count = 1;
		Date currentDate = new Date();
		long dateLong = currentDate.getTime();
			MediaData data = getMediaData(fileName);
			if (data!= null){
				count = data.getMediaDownloadCount();
				count = count+1;
				updateMediaData(fileName, count,dateLong);
			}else{
				ContentValues values = new ContentValues();
				values.put(MySqliteHelper.COLUMN_MEDIA_NAME, fileName);
				values.put(MySqliteHelper.COLUMN_MEDIA_DOWNLOAD_COUNT,count);
				values.put(MySqliteHelper.COLUMN_MEDIA_DATE,dateLong);
				database.insert(MySqliteHelper.TABLE_NAME_MEDIA_TABLE, null, values);			
			}
	}
	public void updateMediaData(String fileName, int count, long dateLong) {
		try {
			ContentValues values = new ContentValues();
			values.put(MySqliteHelper.COLUMN_MEDIA_NAME,fileName);
			values.put(MySqliteHelper.COLUMN_MEDIA_DOWNLOAD_COUNT,count);
			values.put(MySqliteHelper.COLUMN_MEDIA_DATE,dateLong);
				database.update(MySqliteHelper.TABLE_NAME_MEDIA_TABLE, values,
						MySqliteHelper.COLUMN_MEDIA_NAME + "='" + fileName + "'",null);
		
		} catch (Exception e) {

		}
	}
	
	public void getAllMediaData() {
		try {
			Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_MEDIA_TABLE, null, null, null, null,
					null, MySqliteHelper.COLUMN_MEDIA_DOWNLOAD_COUNT+" DESC, "+MySqliteHelper.COLUMN_MEDIA_DATE+" DESC");
			MediaData mediaObj = null;
			Vector<MediaData> mList = new Vector<MediaData>();
			if (cursor!= null && cursor.getCount() != 0) {

				cursor.moveToPosition(0);
				for (int i = cursor.getPosition(); i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					mediaObj = new MediaData();
					mediaObj.setMediaName(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_NAME)));
					mediaObj.setDownloadCount(cursor.getInt(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_DOWNLOAD_COUNT)));
					mediaObj.setDownloadDate(cursor.getLong(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_DATE)));
					mList.addElement(mediaObj);
				}
				setMediaList(mList);
				if (cursor != null)
					cursor.close();
			} else {
				setMediaList(new Vector<MediaData>());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Vector<MediaData> getMediaList() {
		getAllMediaData();
		return mediaList;
	}

	private void setMediaList(Vector<MediaData> mList) {
		this.mediaList = new Vector<MediaData>();
		this.mediaList = mList;
	}
	/**
	 * This method clears all setting database
	 */
	public void removeMediaData() {
		database.delete(MySqliteHelper.TABLE_NAME_MEDIA_TABLE, null, null);
	}

	/**
	 * This method removes single data from database for a particular id
	 * @param filename apiId
	 * @return boolean value true or false
	 */
	public boolean removeMediaByName(String filename) {
		int deletedRows = database.delete(MySqliteHelper.TABLE_NAME_MEDIA_TABLE,
				MySqliteHelper.COLUMN_MEDIA_NAME + "='" + filename+"'", null);
		if(deletedRows>0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * This method returns setting data for a particular apiId
	 * @param fileName
	 * @return string data
	 */
	public MediaData getMediaData(String fileName) {

		MediaData obj = null;
		try{
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_MEDIA_TABLE, null,
				MySqliteHelper.COLUMN_MEDIA_NAME + "='" + fileName+"'", null, null,
				null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			obj = new MediaData();
			obj.setMediaName(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_NAME)));
			obj.setDownloadCount(cursor.getInt(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_DOWNLOAD_COUNT)));
			obj.setDownloadDate(cursor.getLong(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_DATE)));
			} else {
				removeMediaByName(fileName);
			}
		

		if (cursor != null)
			cursor.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return obj;
	}

	
/////////////////////////////////
///////////////////////
//////////////


	public void insertMediaTimeData(MediaTimeData mediaObject) {
		
		MediaTimeData data = getMediaTimeData(mediaObject.getId());
		if (data!= null){
			updateMediaTimeData(mediaObject);
		}else{
			ContentValues values = new ContentValues();
			values.put(MySqliteHelper.COLUMN_MEDIA_TIME_ID, mediaObject.getId());
			values.put(MySqliteHelper.COLUMN_MEDIA_TIME_BOXID, mediaObject.getBoxId());
			values.put(MySqliteHelper.COLUMN_MEDIA_TIME_LAYOUT_ID, mediaObject.getLayoutId());
			values.put(MySqliteHelper.COLUMN_MEDIA_TIME_MEDIA_ID, mediaObject.getMediaId());
			values.put(MySqliteHelper.COLUMN_MEDIA_TIME_START_TIME, mediaObject.getStartTime());
			values.put(MySqliteHelper.COLUMN_MEDIA_TIME_END_TIME, mediaObject.getEndTime());
			values.put(MySqliteHelper.COLUMN_MEDIA_TIME_DURATION, mediaObject.getScheduledDuration());
			long i = database.insert(MySqliteHelper.TABLE_NAME_MEDIA_TIME_TABLE, null, values);	
//			Log.i("Data saved"," row id "+i);
		}
	}
	public void updateMediaTimeData(MediaTimeData mediaObject) {
	try {
		String id =  mediaObject.getId();
		ContentValues values = new ContentValues();
		values.put(MySqliteHelper.COLUMN_MEDIA_TIME_ID, id);
		values.put(MySqliteHelper.COLUMN_MEDIA_TIME_BOXID, mediaObject.getBoxId());
		values.put(MySqliteHelper.COLUMN_MEDIA_TIME_LAYOUT_ID, mediaObject.getLayoutId());
		values.put(MySqliteHelper.COLUMN_MEDIA_TIME_MEDIA_ID, mediaObject.getMediaId());
		values.put(MySqliteHelper.COLUMN_MEDIA_TIME_START_TIME, mediaObject.getStartTime());
		values.put(MySqliteHelper.COLUMN_MEDIA_TIME_END_TIME, mediaObject.getEndTime());
		values.put(MySqliteHelper.COLUMN_MEDIA_TIME_DURATION, mediaObject.getScheduledDuration());
		int row = database.update(MySqliteHelper.TABLE_NAME_MEDIA_TIME_TABLE, values,	MySqliteHelper.COLUMN_MEDIA_TIME_ID + "='" + id + "'",	null);
//		Log.i("Data update"," id "+id+" update id "+row);
	} catch (Exception e) {
	
	}
	}
	
	public void getAllMediaTimeData() {
	try {
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_MEDIA_TIME_TABLE, null, null, null, null,
		null, null);
		MediaTimeData obj = null;
		Vector<MediaTimeData> mList = new Vector<MediaTimeData>();
		if (cursor!= null && cursor.getCount() != 0) {
		
		cursor.moveToPosition(0);
		for (int i = cursor.getPosition(); i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			obj = new MediaTimeData();
			obj.setId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_ID)));
			obj.setBoxId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_BOXID)));
			obj.setLayoutId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_LAYOUT_ID)));
			obj.setMediaId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_MEDIA_ID)));
			obj.setStartTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_START_TIME)));
			obj.setEndTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_END_TIME)));
			obj.setScheduledDuration(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_DURATION)));
			mList.addElement(obj);
		}
		setMediaTimeList(mList);
		if (cursor != null)
			cursor.close();
		} else {
			setMediaTimeList(new Vector<MediaTimeData>());
		}
		} catch (Exception ex) {
		ex.printStackTrace();
		}
	}
	
	public Vector<MediaTimeData> getMediaTimeList() {
		getAllMediaTimeData();
		return mediaTimeList;
	}
	
	private void setMediaTimeList(Vector<MediaTimeData> mList) {
		this.mediaTimeList = new Vector<MediaTimeData>();
		this.mediaTimeList = mList;
	}
	/**
	* This method clears all setting database
	*/
	public void removeMediaTimeData() {
	database.delete(MySqliteHelper.TABLE_NAME_MEDIA_TIME_TABLE, null, null);
	}
	
	/**
	* This method removes single data from database for a particular id
	* @param mediaId apiId
	* @return boolean value true or false
	*/
	public boolean removeMediaTimeById(String mediaId) {
		int deletedRows = database.delete(MySqliteHelper.TABLE_NAME_MEDIA_TIME_TABLE,
		MySqliteHelper.COLUMN_MEDIA_TIME_ID + "='" + mediaId+"'" , null);
        Log.i("Data Deleted"," row id "+deletedRows);
		if(deletedRows>0)
		return true;
		else
		return false;
	}
	
	

	public MediaTimeData getMediaTimeData(String mediaId) {
	
		MediaTimeData obj = null;
		try{
			Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_MEDIA_TIME_TABLE, null,
			MySqliteHelper.COLUMN_MEDIA_TIME_ID + "='" + mediaId+"'", null, null,
			null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				obj = new MediaTimeData();
				obj.setId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_ID)));
				obj.setBoxId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_BOXID)));
				obj.setLayoutId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_LAYOUT_ID)));
				obj.setMediaId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_MEDIA_ID)));
				obj.setStartTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_START_TIME)));
				obj.setEndTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_END_TIME)));
				obj.setScheduledDuration(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_MEDIA_TIME_DURATION)));
	//		} else {
	//			removeMediaTimeById(mediaId);
			}
			
			
			if (cursor != null)
				cursor.close();
		}catch(Exception e){
		e.printStackTrace();
		}
		return obj;
	}
	


/////////////////////////////////
///////////////////////
	//LAYOUT TIME DATA
//////////////

public void insertLayoutTimeData(LayoutTimeData mediaObject) {
	LayoutTimeData data = getLayoutTimeData(mediaObject.getId());
	if (data!= null){
		updateLayoutTimeData(mediaObject);
	}else{
		ContentValues values = new ContentValues();
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_ID, mediaObject.getId());
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_BOXID, mediaObject.getBoxId());
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_LAYOUT_ID, mediaObject.getLayoutId());
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_START_TIME, mediaObject.getStartTime());
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_END_TIME, mediaObject.getEndTime());
		long i = database.insert(MySqliteHelper.TABLE_NAME_LAYOUT_TIME_TABLE, null, values);	
//		Log.i("Data saved"," row id "+i);
	}
}
public void updateLayoutTimeData(LayoutTimeData mediaObject) {
	try {
		ContentValues values = new ContentValues();
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_ID, mediaObject.getId());
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_BOXID, mediaObject.getBoxId());
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_LAYOUT_ID, mediaObject.getLayoutId());
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_START_TIME, mediaObject.getStartTime());
		values.put(MySqliteHelper.COLUMN_LAYOUT_TIME_END_TIME, mediaObject.getEndTime());
		database.update(MySqliteHelper.TABLE_NAME_LAYOUT_TIME_TABLE, values,
		MySqliteHelper.COLUMN_LAYOUT_TIME_ID + "='" + mediaObject.getId() + "'",null);

	} catch (Exception e) {
	
	}
}

public void getAllLayoutTimeData() {
	try {
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_LAYOUT_TIME_TABLE, null, null, null, null,
		null, null);
		LayoutTimeData obj = null;
		Vector<LayoutTimeData> mList = new Vector<LayoutTimeData>();
		if (cursor!= null && cursor.getCount() != 0) {
		
			cursor.moveToPosition(0);
			for (int i = cursor.getPosition(); i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				obj = new LayoutTimeData();
				obj.setId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_ID)));
				obj.setBoxId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_BOXID)));
				obj.setLayoutId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_LAYOUT_ID)));
				obj.setStartTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_START_TIME)));
				obj.setEndTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_END_TIME)));
				mList.addElement(obj);
			}
			setLayoutTimeList(mList);
			if (cursor != null)
				cursor.close();
		} else {
			setLayoutTimeList(new Vector<LayoutTimeData>());
		}
	} catch (Exception ex) {
	ex.printStackTrace();
	}
}

public Vector<LayoutTimeData> getLayoutTimeList() {
	getAllLayoutTimeData();
	return layoutTimeList;
}

private void setLayoutTimeList(Vector<LayoutTimeData> mList) {
	this.layoutTimeList = new Vector<LayoutTimeData>();
	this.layoutTimeList = mList;
}
/**
* This method clears all setting database
*/
public void removeLayoutTimeData() {
	database.delete(MySqliteHelper.TABLE_NAME_LAYOUT_TIME_TABLE, null, null);
}

/**
* This method removes single data from database for a particular id
* @param mediaId
* @return boolean value true or false
*/
public boolean removeLayoutTimeById(String mediaId) {
	int deletedRows = database.delete(MySqliteHelper.TABLE_NAME_LAYOUT_TIME_TABLE,
	MySqliteHelper.COLUMN_LAYOUT_TIME_ID + "='" + mediaId+"'", null);
    Log.i("LayoutTimeById Deleted"," row id "+deletedRows);
	if(deletedRows>0)
	return true;
	else
	return false;
}



public LayoutTimeData getLayoutTimeData(String mediaId) {

	LayoutTimeData obj = null;
	try{
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_LAYOUT_TIME_TABLE, null,
		MySqliteHelper.COLUMN_LAYOUT_TIME_ID + "='" + mediaId+"'", null, null,
		null, null);
	if (cursor != null && cursor.getCount() > 0) {
		cursor.moveToFirst();
		obj = new LayoutTimeData();
		obj.setId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_ID)));
		obj.setBoxId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_BOXID)));
		obj.setLayoutId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_LAYOUT_ID)));
		obj.setStartTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_START_TIME)));
		obj.setEndTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_LAYOUT_TIME_END_TIME)));
	}
	
	
	if (cursor != null)
		cursor.close();
	}catch(Exception e){
	e.printStackTrace();
	}
	return obj;
}


/////////////////////////////////
///////////////////////
//ONOFF TIME DATA
//////////////

public void insertONOFFTimeData(OnOffTimeData dataObject,String tableName) {
	OnOffTimeData data = getOnOffTimeData(tableName,dataObject.getId());
	
	dataObject.setSavingTime(LogUtility.getTimeStamp());
	if (data!= null){
		updateOnOffTimeData(dataObject,tableName);
	}else{
	ContentValues values = new ContentValues();
	values.put(MySqliteHelper.COLUMN_ONOFF_TIME_ID, dataObject.getId());
	values.put(MySqliteHelper.COLUMN_ONOFF_TIME_BOXID, dataObject.getBoxId());
	values.put(MySqliteHelper.COLUMN_ONOFF_TIME_SAVING, dataObject.getOffTime());
	values.put(MySqliteHelper.COLUMN_ONOFF_TIME_ON, dataObject.getOnTime());
	values.put(MySqliteHelper.COLUMN_ONOFF_TIME_OFF, dataObject.getOffTime());
	long i = database.insert(tableName, null, values);	
	Log.i("Data saved"," row id "+i);
	}
}
public void updateOnOffTimeData(OnOffTimeData dataObject,String tableName) {
	try {
		ContentValues values = new ContentValues();
		values.put(MySqliteHelper.COLUMN_ONOFF_TIME_ID, dataObject.getId());
		values.put(MySqliteHelper.COLUMN_ONOFF_TIME_BOXID, dataObject.getBoxId());
		values.put(MySqliteHelper.COLUMN_ONOFF_TIME_SAVING, dataObject.getOffTime());
		values.put(MySqliteHelper.COLUMN_ONOFF_TIME_ON, dataObject.getOnTime());
		values.put(MySqliteHelper.COLUMN_ONOFF_TIME_OFF, dataObject.getOffTime());
		database.update(tableName, values,
				MySqliteHelper.COLUMN_ONOFF_TIME_ID + "='" + dataObject.getId() + "'",null);
	
	} catch (Exception e) {
	
	}
}

 void getAllOnOffTimeBoxData() {
	try {
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_ONOFF_TIME_BOX_TABLE, null, null, null, null,
		null, null);
		OnOffTimeData obj = null;
		Vector<OnOffTimeData> mList = new Vector<OnOffTimeData>();
		if (cursor!= null && cursor.getCount() != 0) {
		
			cursor.moveToPosition(0);
			for (int i = cursor.getPosition(); i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				obj = new OnOffTimeData();
				obj.setId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_ID)));
				obj.setBoxId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_BOXID)));
				obj.setSavingTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_SAVING)));
				obj.setOnTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_ON)));
				obj.setOffTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_OFF)));
				mList.addElement(obj);
			}
			setOnOffTimeBoxList(mList);
			if (cursor != null)
				cursor.close();
		} else {
			setOnOffTimeBoxList(new Vector<OnOffTimeData>());
		}
	} catch (Exception ex) {
	ex.printStackTrace();
	}
}

	void getAllOnOffTimeScreenData() {
		try {
			Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_ONOFF_TIME_SCREEN_TABLE, null, null, null, null,
					null, null);
			OnOffTimeData obj = null;
			Vector<OnOffTimeData> mList = new Vector<OnOffTimeData>();
			if (cursor!= null && cursor.getCount() != 0) {

				cursor.moveToPosition(0);
				for (int i = cursor.getPosition(); i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					obj = new OnOffTimeData();
					obj.setId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_ID)));
					obj.setBoxId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_BOXID)));
					obj.setSavingTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_SAVING)));
					obj.setOnTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_ON)));
					obj.setOffTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_OFF)));
					mList.addElement(obj);
				}
				setOnOffTimeScreenList(mList);
				if (cursor != null)
					cursor.close();
			} else {
				setOnOffTimeScreenList(new Vector<OnOffTimeData>());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void getAllOnOffTimeAppData() {
		try {
			Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_ONOFF_TIME_APP_TABLE, null, null, null, null,
					null, null);
			OnOffTimeData obj = null;
			Vector<OnOffTimeData> mList = new Vector<OnOffTimeData>();
			if (cursor!= null && cursor.getCount() != 0) {

				cursor.moveToPosition(0);
				for (int i = cursor.getPosition(); i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					obj = new OnOffTimeData();
					obj.setId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_ID)));
					obj.setBoxId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_BOXID)));
					obj.setSavingTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_SAVING)));
					obj.setOnTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_ON)));
					obj.setOffTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_OFF)));
					mList.addElement(obj);
				}
				setOnOffTimeAppList(mList);
				if (cursor != null)
					cursor.close();
			} else {
				setOnOffTimeAppList(new Vector<OnOffTimeData>());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
public Vector<OnOffTimeData> getONOFFTimeBoxList() {
	getAllOnOffTimeBoxData();
	return onOffTimeBoxList;
}
	public Vector<OnOffTimeData> getONOFFTimeAppList() {
		getAllOnOffTimeAppData();
		return onOffTimeAppList;
	}
	public Vector<OnOffTimeData> getONOFFTimeScreenList() {
		getAllOnOffTimeScreenData();
		return onOffTimeScreenList;
	}
private void setOnOffTimeBoxList(Vector<OnOffTimeData> mList) {
	this.onOffTimeBoxList = new Vector<OnOffTimeData>();
this.onOffTimeBoxList = mList;
}

	private void setOnOffTimeAppList(Vector<OnOffTimeData> mList) {
		this.onOffTimeAppList = new Vector<OnOffTimeData>();
		this.onOffTimeAppList = mList;
	}

	private void setOnOffTimeScreenList(Vector<OnOffTimeData> mList) {
		this.onOffTimeScreenList = new Vector<OnOffTimeData>();
		this.onOffTimeScreenList = mList;
	}
/**
* This method clears all setting database
*/
public void removeOnOffTimeData(String TableName) {
	database.delete(TableName, null, null);
}

/**
* This method removes single data from database for a particular id
* @param mediaId
* @return boolean value true or false
*/
public boolean removeOnOffTimeById(String TableName,String mediaId) {
	int deletedRows = database.delete(TableName,
	MySqliteHelper.COLUMN_ONOFF_TIME_ID + "='" + mediaId+"'", null);
	if(deletedRows>0)
	return true;
	else
	return false;
}


/**
* This method returns setting data for a particular apiId
* @param mediaId
* @return string data
*/
public OnOffTimeData getOnOffTimeData(String TableName,String mediaId) {

	OnOffTimeData obj = null;
	try{
	Cursor cursor = database.query(TableName, null,
	MySqliteHelper.COLUMN_ONOFF_TIME_ID + "='" + mediaId+"'", null, null,
	null, null);
	if (cursor != null && cursor.getCount() > 0) {
		cursor.moveToFirst();
		obj = new OnOffTimeData();
		obj.setId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_ID)));
		obj.setBoxId(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_BOXID)));
		obj.setSavingTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_SAVING)));		
		obj.setOnTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_ON)));
		obj.setOffTime(cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_ONOFF_TIME_OFF)));
	}
	if (cursor != null)
		cursor.close();
	}catch(Exception e){
	e.printStackTrace();
	}
	return obj;
}
////////////////////
	/////////////////////
	/////////////////
	

	public void insertResourceData(String rowId, byte[] bytes) {
		ContentValues values = new ContentValues();
		if (!rowId.equals("")){
			values.put(MySqliteHelper.COLUMN_CACHING_API_ID, rowId);
			values.put(MySqliteHelper.COLUMN_CACHING_DATA, bytes);
			database.delete(MySqliteHelper.TABLE_NAME_RESOURCE_TABLE, MySqliteHelper.COLUMN_CACHING_API_ID + "='" + rowId+"'", null);
		}
		 database.insert(MySqliteHelper.TABLE_NAME_RESOURCE_TABLE, null, values);
	}	
	
	/**
	 * This method clears resource data
	 *
	 */
	public boolean removeResourceData() {
		if(database.delete(MySqliteHelper.TABLE_NAME_RESOURCE_TABLE, null, null)>0){
			return true;
		}else
			return false;
	}

	/**
	 * This method removes resource data for a particular id
	 * @param apiId
	 * @return
	 * @deprecated
	 */
	public boolean removeResourceDataByApiId(String apiId) {
		int deletedRows = database.delete(MySqliteHelper.TABLE_NAME_RESOURCE_TABLE,
				MySqliteHelper.COLUMN_CACHING_API_ID + "='" + apiId+"'", null);
		if(deletedRows>0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * This method returns resourcedata for a particular id
	 * @param apiId
	 * @return
	 * @deprecated
	 */
	public byte[] getResourceData(String apiId) {
		byte[] data = null;
		Cursor cursor = database.query(MySqliteHelper.TABLE_NAME_RESOURCE_TABLE, null,
				MySqliteHelper.COLUMN_CACHING_API_ID + "='" + apiId+"'", null, null,
				null, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			data = cursor.getBlob(cursor
						.getColumnIndex(MySqliteHelper.COLUMN_CACHING_DATA));
			} else {
				removeResourceDataByApiId(apiId);
			}
		

		if (cursor != null)
			cursor.close();
		return data;
	}



}
