package in.com.app.data;


import in.com.app.domain.DisplayLayout;
import in.com.app.domain.DisplayLayoutFiles;
import in.com.app.domain.DisplayLayoutSchedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * This class maintains layout data in HashMap
 * @author Ravi@Xvidia technologies
 * @since version 1.0
 *
 */
public class SignageData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2222777107947481827L;


	private SignageData(){}

//	ArrayList<String> data = null;
	static SignageData obj =null;
//	static SignageData oldObj =null;
//	HashMap<String, RawData> dataRaw = null;
	ArrayList<DisplayLayout> dataLayout = null;


	/**
	 * This method maintains the singleton refrence the SignageData
	 * @return single object of SignageData
	 * @see SignageData
	 * @since version 1.0
	 */
	public static  SignageData getInstance(){
		if(obj == null){
			obj = new SignageData();
//			obj.dataRaw = new HashMap<String, RawData>();
//			obj.dataLayout = new ArrayList<DisplayLayout>();
		}
		return obj;
	}
	
	
//	 public static  void setInstance(SignageData signageDataObject){
//		obj = signageDataObject;
//	}

	 /**
		 * This method resets SignageData
		 * @see SignageData
		 * @since version 1.0
		 */
	public static void resetSignageData(){
		obj = null;
	}

	/**
	 * This method adds a row to HashMap {@link HashMap} 
	 * @param id fileid
	 * @param filename
	 * @param filetype
	 * @param bytes
	 * @param len length of file
	 * @since version 1.0
	 */
//	public void addRawDataNode(String id, String filename, String filetype, byte[] bytes, int len){
//		try{
//			dataRaw.put(id, new RawData(id, filename, filetype, bytes, len));
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	
	/**
	 * This method adds a row to {@link HashMap}
	 * @param id
	 * @param filename
	 * @param filetype
	 * @param len length of file
	 * @since version 1.0
	 */
//	public void addRawDataNode(String id, String filename, String filetype, int len){
//		try{
//			dataRaw.put(id, new RawData(id, filename, filetype, len));
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	/**
	 * This method returns HashMap of file data
	 * @return hashmap of the {@link RawData}
	 * @since version 1.0 
	 */
//	public HashMap<String, RawData> getRawDataNode(){
//		return dataRaw;
//	}

	/**
	 * This method gets {@link RawData} for the specified id  from the {@link HashMap}
	 * @param id fileid of {@link HashMap}
	 * @return for the specified id get {@link RawData} from the {@link HashMap}
	 * @since version 1.0
	 */
//	public RawData getDataFromId(String id){
//		RawData obj = null;
//		try{
//			obj = (RawData)dataRaw.get(id);
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
//		return obj;
//	}




	DisplayLayoutFiles lstFiles = null;
	/**
	 * This method sets the {@link DisplayLayoutFiles}
	 * @param files set {@link DisplayLayoutFiles}
	 * @since version 1.0
	 */
	public void setLayoutFiles(DisplayLayoutFiles files){
		lstFiles = files;
	}

	/**
	 * This method gets the {@link DisplayLayoutFiles}
	 * @return instance of {@link DisplayLayoutFiles}
	 * @since version 1.0
	 */
	public DisplayLayoutFiles getLayoutFiles(){
		return lstFiles;
	}



	DisplayLayoutSchedule layoutScheduleData = null;

	/**
	 * This method sets the {@link DisplayLayoutSchedule}
	 * @param layoutSchedule set {@link DisplayLayoutSchedule}
	 * @since version 1.0
	 */
	public void setLayoutSchedule(DisplayLayoutSchedule layoutSchedule){
		layoutScheduleData = layoutSchedule;	
	}
	
	/**
	 * This method gets the {@link DisplayLayoutSchedule}
	 * @return {@link DisplayLayoutSchedule}
	 * @since version 1.0
	 */
	public DisplayLayoutSchedule getLayoutSchedule(){
		return layoutScheduleData;
	}

	String defaulLayoutFile = "";
	String currentLayoutFile = "";
	ArrayList<String> dependents = null;
	
	public void setCurrentLayoutFiles(ArrayList<String> list){
		dependents = list;
	} 
	
	public ArrayList<String> getCurrentLayoutFiles() {
		return dependents;
	}
	/**
	 * This method sets the layout String
	 * @param defaulLayout layout to be set
	 * @since version 1.0
	 */
	public void setDefaultLayout(String defaulLayout){
		defaulLayoutFile = defaulLayout;
	} 
	
	/**
	 * This method gets the current layout string
	 * @return default layout sring
	 * @since version 17
	 */
	public String getCurrentLayout() {
		return currentLayoutFile;
	}
	/**
	 * This method sets the current layout String
	 * @param defaulLayout layout to be set
	 * @since version 1.7
	 */
	public void setCurrentLayout(String defaulLayout){
		if(defaulLayout.isEmpty()){
			currentLayoutFile = defaulLayoutFile;
		}else{
			currentLayoutFile = defaulLayout;
		}
	} 
	
	/**
	 * This method gets the default layout string
	 * @return default layout sring
	 * @since version 1.0
	 */
	public String getdefaulLayout() {
		return defaulLayoutFile;
	}
	/**
	 * This method sets the {@link SignageData} instance used
	 *  before new layout files were downloaded
	 * @return instance of {@link SignageData} 
	 * @since version 1.0
	 */
//	public static  void setOldInstance(){
////		if(oldObj== null){
//			oldObj = getInstance();
////		}
//	}
	
	/**
	 * This method gets the {@link SignageData} instance used
	 *  before new layout files were downloaded 
	 * @return {@link SignageData} instance
	 */
//	public static  SignageData getOldInstance(){
//		return oldObj;
//	}
	
}
