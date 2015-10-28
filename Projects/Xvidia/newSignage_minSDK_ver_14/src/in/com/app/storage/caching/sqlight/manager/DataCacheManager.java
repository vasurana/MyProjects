package in.com.app.storage.caching.sqlight.manager;

import java.util.Vector;

import android.content.Context;

import in.com.app.data.MediaData;
import in.com.app.model.LayoutTimeData;
import in.com.app.model.MediaTimeData;
import in.com.app.model.OnOffTimeData;
import in.com.app.storage.caching.sqlight.CacheDataSource;

/**
 * This is wrapper class of {@link CacheDataSource} that  manages (update/delete) data in sqllite database
 * @author Ravi@xvidia
 *	@since version 1.0
 */
public class DataCacheManager {

	private static DataCacheManager Instance = null;// new DataCacheManager();
	static CacheDataSource dataSource = null;//CacheDataSource.getInstance();

	/**
	 * This method maintains the singleton refrence the DataCacheManager
	 * @return single object of DataCacheManager
	 * @see {@link DataCacheManager}
	 * @since version 1.0
	 */
	public static DataCacheManager getInstance(Context ctx) {
		if (Instance == null){
			Instance = new DataCacheManager();
			dataSource = CacheDataSource.getInstance(ctx);
		}
		return Instance;
	}

	private DataCacheManager() {
	}

	/**
	 * This method opens database
	 */
	public synchronized void open() {
		dataSource.open();
	}
	
	/**
	 * Thismethod closes database
	 */
	public synchronized void  close() {
		dataSource.close();
	}


	/**
	 * Thismethod saves setting data
	 * @param  apiValue
	 * @param  apiData
	 * @return
	 */
	public synchronized boolean saveSettingData(int apiValue/*APIVAL MUST BE MORE THAN 0*/, String apiData) {
		boolean retVal = false;
		try{
			if(apiValue>0){
				dataSource.open();
				dataSource.insertSettingData(apiValue, apiData);
				dataSource.close();
				retVal= true;		
			}else{
				return false;
			}
		}catch (Exception e) {
			retVal = false;
		}		
		return retVal;
	}

	/**
	 * This method returns setting data for a particular apiId
	 * @param apiValue
	 * @return string data
	 */
	public final synchronized String readSettingData(int apiValue) {
		dataSource.open();
		String apiData = dataSource.getSettingData(apiValue);
		dataSource.close();
		if(apiData==null){
			apiData = "";
		}
		return apiData;
	}

	

	/**
	 * This method clears all setting database
	 */
	public final synchronized void clearSettingCache(){
		dataSource.open();
		dataSource.removeSettingData();
		dataSource.close();
	}


	public final synchronized boolean removeSettingDataByID(int apiValue){
		dataSource.open();
		boolean retVal = dataSource.removeSettingDataByApiId(apiValue);
		dataSource.close();
		return retVal;
	}


	public synchronized boolean saveMediaData(String fileName) {
		boolean retVal = false;
		try{
				dataSource.open();
				dataSource.insertMediaData(fileName);
				dataSource.close();
				retVal= true;		
			
		}catch (Exception e) {
			retVal = false;
		}		
		return retVal;
	}


	public final synchronized MediaData readMediaData(String filename) {
		dataSource.open();
		MediaData apiData = dataSource.getMediaData(filename);
		dataSource.close();
//		if(apiData==null){
//			apiData = new MediaData();
//		}
		return apiData;
	}


	public final synchronized Vector<MediaData> getAllMediaData() {
		dataSource.open();
		Vector<MediaData> mList = dataSource.getMediaList();
		dataSource.close();
//		if(apiData==null){
//			apiData = new MediaData();
//		}
		return mList;
	}

	/**
	 * This method clears all setting database
	 */
	public  final synchronized void removeMediaData(){
		dataSource.open();
		dataSource.removeMediaData();
		dataSource.close();
	}


	public final synchronized boolean removeMediaByName(String fileName){
		dataSource.open();
		boolean retVal = dataSource.removeMediaByName(fileName);
		dataSource.close();
		return retVal;
	}

	public synchronized boolean  saveMediaTimeData(MediaTimeData obj) {
		boolean retVal = false;
		try{
				dataSource.open();
				dataSource.insertMediaTimeData(obj);
				dataSource.close();
				retVal= true;		
			
		}catch (Exception e) {
			retVal = false;
		}		
		return retVal;
	}


	public final synchronized MediaTimeData getMediaData(String mediaId) {
		dataSource.open();
		MediaTimeData apiData = dataSource.getMediaTimeData(mediaId);
		dataSource.close();
//		if(apiData==null){
//			apiData = new MediaData();
//		}
		return apiData;
	}


	public final synchronized Vector<MediaTimeData> getAllMediaTimeData() {
		dataSource.open();
		Vector<MediaTimeData> mList = dataSource.getMediaTimeList();
		dataSource.close();
//		if(apiData==null){
//			apiData = new MediaData();
//		}
		return mList;
	}

	/**
	 * This method clears all setting database
	 */
	public final synchronized void removeMediaTimeData(){
		dataSource.open();
		dataSource.removeMediaTimeData();
		dataSource.close();
	}


	public final synchronized boolean removeMediaTimeById(String mediaId){
		dataSource.open();
		boolean retVal = dataSource.removeMediaTimeById(mediaId);
		dataSource.close();
		return retVal;
	}

	public synchronized boolean saveLayoutTimeData(LayoutTimeData obj) {
		boolean retVal = false;
		try{
				dataSource.open();
				dataSource.insertLayoutTimeData(obj);
				dataSource.close();
				retVal= true;		
			
		}catch (Exception e) {
			retVal = false;
		}		
		return retVal;
	}


	public final synchronized LayoutTimeData getLayoutData(String mediaId) {
		dataSource.open();
		LayoutTimeData apiData = dataSource.getLayoutTimeData(mediaId);
		dataSource.close();
		return apiData;
	}


	public final synchronized Vector<LayoutTimeData> getAllLayoutTimeData() {
		dataSource.open();
		Vector<LayoutTimeData> mList = dataSource.getLayoutTimeList();
		dataSource.close();
		return mList;
	}

	public final synchronized void removeLayoutTimeData(){
		dataSource.open();
		dataSource.removeLayoutTimeData();
		dataSource.close();
	}

	public final synchronized boolean removeLayoutTimeById(String mediaId){
		dataSource.open();
		boolean retVal = dataSource.removeLayoutTimeById(mediaId);
		dataSource.close();
		return retVal;
	}

	public synchronized boolean saveOnOffTimeData(String TableName,OnOffTimeData obj) {
		boolean retVal = false;
		try{
				dataSource.open();
				dataSource.insertONOFFTimeData(obj,TableName);
				dataSource.close();
				retVal= true;		
			
		}catch (Exception e) {
			retVal = false;
		}		
		return retVal;
	}


	public final synchronized OnOffTimeData getOnOffData(String TableName,String mediaId) {
		dataSource.open();
		OnOffTimeData apiData = dataSource.getOnOffTimeData(TableName,mediaId);
		dataSource.close();
		return apiData;
	}


	public final synchronized Vector<OnOffTimeData> getAllOnOffTimeScreenData() {
		dataSource.open();
		Vector<OnOffTimeData> mList = dataSource.getONOFFTimeScreenList();
		dataSource.close();
		return mList;
	}
	public final synchronized Vector<OnOffTimeData> getAllOnOffTimeBoxData() {
		dataSource.open();
		Vector<OnOffTimeData> mList = dataSource.getONOFFTimeBoxList();
		dataSource.close();
		return mList;
	}
	public final synchronized Vector<OnOffTimeData> getAllOnOffTimeAppData() {
		dataSource.open();
		Vector<OnOffTimeData> mList = dataSource.getONOFFTimeAppList();
		dataSource.close();
		return mList;
	}
	/**
	 * This method clears all setting database
	 */
	public final synchronized void removeOnOffTimeData(String TableName){
		dataSource.open();
		dataSource.removeOnOffTimeData(TableName);
		dataSource.close();
	}


	public final synchronized boolean removeOnOffTimeById(String TableName,String mediaId){
		dataSource.open();
		boolean retVal = dataSource.removeOnOffTimeById(TableName,mediaId);
		dataSource.close();
		return retVal;
	}
	/**
	 * This method saves resource data
	 * @param apiValue
	 * @param apiData
	 * @return
	 * @deprecated
	 */
	public synchronized boolean saveResourceData(String apiValue/*APIVAL MUST BE MORE THAN 0*/, byte[] apiData) {
		boolean retVal = false;
		try{
			if(!apiValue.equals("")){
				dataSource.open();
				dataSource.insertResourceData(apiValue, apiData);
				dataSource.close();
				retVal= true;		
			}else{
				return false;
			}
		}catch (Exception e) {
			retVal = false;
		}		
		return retVal;
	}

	/**
	 * This method returns resourcedata for a particular id
	 * @param apiValue
	 * @return
	 * @throws NullPointerException
	 * @deprecated
	 */
	public final synchronized byte[] readResourceData(String apiValue) throws NullPointerException{
		dataSource.open();
		byte[] apiData = dataSource.getResourceData(apiValue);
		dataSource.close();		
		if(apiData==null){
			throw new NullPointerException();
		}
		return apiData;
	}

	

	/**
	 * This method clears resource data
	 * @deprecated
	 */
	public final synchronized void clearResourceCache(){
		dataSource.open();
		dataSource.removeResourceData();
		dataSource.close();
	}

	/**
	 * This method removes resource data for a particular id
	 * @param apiValue
	 * @return
	 * @deprecated
	 */
	public final synchronized boolean removeResourceDataByID(String apiValue){
		dataSource.open();
		boolean retVal = dataSource.removeResourceDataByApiId(apiValue);
		dataSource.close();
		return retVal;
	}
	
	/**
	 * This method removes all resurce data
	 * @return
	 * 
	 */
	public final synchronized boolean removeResourceDataAll(){
		dataSource.open();
		boolean retVal = dataSource.removeResourceData();
		dataSource.close();
		return retVal;
	}




}
