package in.com.app.utility;

import in.com.app.AppMain;
import in.com.app.AppState;
import in.com.app.FileManager;
import in.com.app.IDisplayLayout;
import in.com.app.data.MediaData;
import in.com.app.data.SignageData;
import in.com.app.domain.DisplayScheduleLayout;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.updater.LogUtility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
/**
 * This is a utility class 
 * @author Ravi@xvidia
 *
 */
public class Utility {

	public static final String REGISTERED_DISPLAY_COMPLETE = "registerComplete";
	public static final String REGISTERED_DISPLAY_PENDING = "registerPending";
	public static final String REGISTERED_DISPLAY_FAILED = "registerFailed";

	public static final String FILE_LIST_DOWNLOADED = "downloadedFileList";

	public static final String FILES_IN_FILE_LIST_DOWNLOADED = "downloadedFilesInFileList";

	public static final long MINIMUM_MEMORY_CHECK = 256;

	static String nextLayout = "";
	static long nextLayoutStartTime = 0;
	static long nextLayoutChangeTime = 0;
	
	static String readTextFromFile(String filename){
		String retMesg = "";
		try {
//			File myFile = new File("/sdcard/mysdfile.txt");
			String destPath = Environment.getExternalStorageDirectory()
					+ AppState.FILE_FOLDER;
			File myFile = new File(destPath+filename);
			if(myFile.exists()){
				FileInputStream fIn = new FileInputStream(myFile);
				BufferedReader myReader = new BufferedReader(
						new InputStreamReader(fIn));
				String aDataRow = "";
				String aBuffer = "";
				while ((aDataRow = myReader.readLine()) != null) {
					aBuffer += aDataRow;
				}
				myReader.close();
				retMesg = aBuffer;
			}
		} catch (Exception e) {
		}
		return retMesg;
	}
	/**
	 * This method find layout file among the display files
	 * @return true if file is found else false
	 */
	public static boolean IsFileExists(String fileName,boolean downloadFile) {
		boolean retval = false;
		try {
			String destPath = Environment.getExternalStorageDirectory()
					+ AppState.DISPLAY_FOLDER;
			File dir = new File(destPath);
			if (dir.isDirectory()) {
				String[] children = dir.list();
				File temp;
				for (int i = 0; i < children.length; i++) {
					temp = new File(dir, children[i]);

					if (temp.getName().equalsIgnoreCase(fileName)) {
						if (downloadFile) {
							if (!temp.getName().contains(".")) {
								retval = false;
							} else {
								retval = true;
							}
						} else {
							retval = true;
						}
						break;
					}
				}
			}
		} catch (Exception e) {

		}
		return retval;
	}
	
	/**
	 * This method finds the layout file among the display files
	 * @return true if file is found else false
	 */
	public static boolean IsFileExistsInDownload(String fileName) {
		boolean retval = false;
		try {
			String destPath = Environment.getExternalStorageDirectory() + AppState.DOWNLOAD_FOLDER;
			File dir = new File(destPath);
			if (dir.isDirectory()) {
				String[] children = dir.list();
				File temp;
				for (int i = 0; i < children.length; i++) {
					temp = new File(dir, children[i]);

					if (temp.getName().equalsIgnoreCase(fileName)) {
						if (!temp.getName().contains(".")) {
							retval = false;
						} else {
							retval = true;
						}
						break;
					}
				}
			}
		}catch(Exception e){

		}
	   return retval;
	}
	public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }
    public static long getAvailableExternalMemorySize() {
    	long availableSize = MINIMUM_MEMORY_CHECK;
        final long SIZE_KB = 1024L;
        final long SIZE_MB = SIZE_KB * SIZE_KB;
    	try{
	        if (externalMemoryAvailable()) {
	            File path = Environment.getExternalStorageDirectory();
	            StatFs stat = new StatFs(path.getPath());
	            long blockSize = stat.getBlockSize();
	            long availableBlocks = stat.getAvailableBlocks();
	            availableSize = (availableBlocks * blockSize);
//	            availableSize = availableSize/1024;
	            availableSize = availableSize/SIZE_MB;
//	            return availableSize;
	        }
    	}catch(Exception e){
//           e.printStackTrace();
        }
    	 return availableSize;
    }
//    /**
//     * @return Number of Mega bytes available on External storage
//     */
//    public static long getAvailableSpaceInMB(){
//        final long SIZE_KB = 1024L;
//        final long SIZE_MB = SIZE_KB * SIZE_KB;
//        long availableSpace = -1L;
//        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
//        availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
//        return availableSpace/SIZE_MB;
//    }
    public static void deleteLeastUsedFileOnLowMemory(){

		String PATH = Environment.getExternalStorageDirectory()+ AppState.DISPLAY_FOLDER;
//		Date currentDate = new Date();
//		long dateLong = currentDate.getTime();
		boolean nextFlag = false;
		int next = 1;
    	while (getAvailableExternalMemorySize()<MINIMUM_MEMORY_CHECK){
    		Vector<MediaData> obj = DataCacheManager.getInstance(AppMain.getAppMainContext()).getAllMediaData();
    		if(obj!= null && obj.size()>0){
    			int objIndex = obj.size()-1;
    			if(nextFlag){
    				objIndex = objIndex-next;
    			}
    			if(objIndex>0){
	    			String filename = obj.elementAt(objIndex).getMediaName();
//	    			long downloadDate = obj.elementAt(objIndex).getMediaDownloadDate();
//	    			downloadDate += 1000 * 60 * 60 * 24 * 2;
//	    			if(downloadDate < dateLong){
		    			File checkFile = new File(PATH, filename);
		    			boolean retFlag = FileManager.deleteDir(checkFile);
		    			if(retFlag){
		    				DataCacheManager.getInstance(AppMain.getAppMainContext()).removeMediaByName(filename);
		    			}else{
		    				nextFlag = true;
		    				next = next+1;
		    			}
//	    			}else{
//	    				nextFlag = true;
//	    				next = next+1;
//	    			}
    			}else{
    				break;
    			}
    		}
    	}
    }
    
	/**
	 * This method returns boolean true/false depending on string true/false
	 * @param state variable for true/false
	 * @return boolean value  true or false
	 */
	public static boolean checkBooleanState(String state){
		boolean retVal = false;
		if(state!=null && state.equalsIgnoreCase(IDisplayLayout.FLAG_TRUE)){
			retVal = true;
		}
		return retVal;
	}
	
	public static boolean checkStringMatch(String string1,String string2){
		boolean retVal = false;
		if(string1 !=null && string1.equalsIgnoreCase(string2)){
			retVal = true;
		}
		return retVal;
	}
	
	public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
	        int reqWidth, int reqHeight) {
		Bitmap returnBitmap = null;
		try{
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    returnBitmap =BitmapFactory.decodeFile(filePath, options);
		}catch(Exception e){
			
		}
	    return returnBitmap;
	}

	public static int calculateInSampleSize(
	        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image

	    int inSampleSize = 1;
		try{
			
	    final int height = options.outHeight;
	    final int width = options.outWidth;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
		}catch(Exception e){
			
		}
	    return inSampleSize;
	}
	
	static long converDateStringToDateLong(String dateInString){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
//		String dateInString = "Fri, June 7 2013";		
		long dateLong = 0;
		try {
	 
			Date date = formatter.parse(dateInString);
			dateLong = date.getTime();
//			System.out.println(date);
//			System.out.println(formatter.format(date));
	 
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateLong;
	}
	
	public static String getCurrentFile( ArrayList<DisplayScheduleLayout> layoutList){
		long now = System.currentTimeMillis();
		long fromDate =0;
		long todate = 0;
		long timediff = 0;
		long nextTimediff = 0;
		long nextlayoutTime = 0;
		boolean schecdule = false;
		String fileName="";
		String nextFileName="";
		String dependents = "";
		DisplayScheduleLayout obj = null;
		for(int i=0;i<layoutList.size();i++){
			long temp =0;
			obj = layoutList.get(i);
			fromDate = converDateStringToDateLong(obj.getFromdt());
			todate = converDateStringToDateLong(obj.getTodt());
			
			if(now>= fromDate && now< todate){
				
				 temp = now-fromDate;
				 if(!schecdule){
					 schecdule =true;
					 timediff = temp;
					 fileName = obj.getFile();
					 dependents = obj.getDependents();
				 }else if(timediff>temp){
					 timediff = temp;
					 fileName = obj.getFile();
					 dependents = obj.getDependents();
				 }				 
			}else{
				 temp = fromDate-now;
				 nextFileName = obj.getFile();
				 if(nextTimediff == 0){
					 nextTimediff = temp;
					 nextlayoutTime  = fromDate;
				 }else if(nextTimediff> temp){
					 nextTimediff = temp;
					 nextlayoutTime  = fromDate;
				 }
			}
		}
		if(Utility.nextLayout.isEmpty() && !nextFileName.isEmpty() ){
			setNextLayout(nextFileName);
			setNextLayoutChangeTime(nextTimediff-250);
			setNextLayoutStartTime(nextlayoutTime);
		}else if(!Utility.nextLayout.isEmpty() && !Utility.nextLayout.equalsIgnoreCase(nextFileName)){
			setNextLayout(nextFileName);
			setNextLayoutChangeTime(nextTimediff-250);
			setNextLayoutStartTime(nextlayoutTime);
		}
		if(dependents !=null && !dependents.isEmpty()){
			ArrayList<String> fileList =Utility.getStringArrayList(dependents, ",");
			fileList.add(0,fileName);
			SignageData.getInstance().setCurrentLayoutFiles(fileList);
		}
		return fileName;
	}
	
	public static ArrayList<String> getStringArrayList( String stringTxt, String seperator){		
		ArrayList<String> fileList = new ArrayList<String>();
		if(!stringTxt.isEmpty()){
			String strArray[] = stringTxt.split(seperator);
			for(String temp : strArray){
				fileList.add(temp);
			}
		}
		return fileList;
	}
	
	 public static String getNextLayout() {
			return nextLayout;
		}
		public static void setNextLayout(String nextLayout) {
			Utility.nextLayout = nextLayout;
		}
		public static long getNextLayoutChangeTime() {
			return nextLayoutChangeTime;
		}
		public static void setNextLayoutChangeTime(long nextLayoutChangeTime) {
			Utility.nextLayoutChangeTime = nextLayoutChangeTime;
		}


	public static long getNextLayoutStartTime() {
		return nextLayoutStartTime;
	}

	public static void setNextLayoutStartTime(long nextLayoutStartTime) {
		Utility.nextLayoutStartTime = nextLayoutStartTime;
	}

	public static double convertToDoubleFromString(String value){
		double returnVal = 1.0;
		try{
			if(!value.equalsIgnoreCase("")){
				returnVal= Double.parseDouble(value);
			}
		}catch(Exception e){

		}
		return returnVal;
	}

	public static void writeLogFile(String log){
		String destPath = Environment.getExternalStorageDirectory() +AppState.FILE_FOLDER+ AppState.FILE_NAME_Log;
		File logFile = new File(destPath);
			if (!logFile.exists())
			{
				try
				{
					logFile.createNewFile();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		BufferedWriter buf=null;
			try
			{
				//BufferedWriter for performance, true to set append to file flag
				 buf = new BufferedWriter(new FileWriter(logFile, true));
				String time = "========"+LogUtility.getTimeStamp()+"==============";
				buf.append(time);
				buf.append(log);
				buf.newLine();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(buf!= null) {
					try {
						buf.flush();
						buf.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
}
