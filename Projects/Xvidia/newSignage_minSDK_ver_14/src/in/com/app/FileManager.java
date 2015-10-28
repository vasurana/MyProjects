package in.com.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.os.Environment;

/**
 * This class manages manipulation of files.
 * @author Ravi@Xvidia
 *	@since version 1.0
 */
public class FileManager {

	static ArrayList<String> fileList;
	
	/**
	 * This methoad copies all the files forma source location to the target location
	 * @param sourceLocation file Location
	 * @param targetLocation file Location
	 * @throws IOException
	 */
	public static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
	        throws IOException {

	    if (sourceLocation.isDirectory()) {
	        if (!targetLocation.exists()) {
	            targetLocation.mkdir();
	        }else{
//	        	if(deleteDirIfFileNotInList(targetLocation)){
//	        		targetLocation.mkdir();
//	        	}
	        }

	        String[] children = sourceLocation.list();
			if(children != null) {
				for (int i = 0; i < sourceLocation.listFiles().length; i++) {

					copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
							new File(targetLocation, children[i]));
				}
			}
	    } else {
//	    	Log.e("File Coping",sourceLocation+" targetLocation "+targetLocation);
	        InputStream in = new FileInputStream(sourceLocation);

	        OutputStream out = new FileOutputStream(targetLocation);

	        // Copy the bits from instream to outstream
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        in.close();
	        out.close();
	    }

	}
	
	public static void copyDirectoryOldToNew(File sourceLocation, File targetLocation)
	        throws IOException {

	    if (sourceLocation.isDirectory()) {
	        if (!targetLocation.exists()) {
	            targetLocation.mkdir();
	        }

	        String[] children = sourceLocation.list();
	        for (int i = 0; i < sourceLocation.listFiles().length; i++) {

	        	copyDirectoryOldToNew(new File(sourceLocation, children[i]),
	                    new File(targetLocation, children[i]));
	        }
	    } else {
	    	File file = new File(targetLocation.getParent());	    	
			//if(file.isDirectory()){
				if(!file.exists())
					file.mkdirs();
			//}
			File outputFile = new File(file, targetLocation.getName());



	        InputStream in = new FileInputStream(sourceLocation);

//	        OutputStream out = new FileOutputStream(targetLocation);

			FileOutputStream out = new FileOutputStream(outputFile);
	        // Copy the bits from instream to outstream
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        in.close();
	        out.close();
	    }

	}
	
	/**
	 * This method deletes the folder directory.
	 * @param dir directory to be deleted
	 * @return boolean value true is successfully deleted else false
	 */
	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(dir, children[i]));
				
			}
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}
	
	/**
	 * This method deletes the folder directory if file is not in the list.
	 * @param dir directory to be deleted
	 * @return boolean value true is successfully deleted else false
	 */
	public static boolean deleteDirIfFileNotInList(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				if (!fileInList(children[i])) {
//					Log.e("File Deleting"," Path "+children[i]);
					deleteDirIfFileNotInList(new File(dir, children[i]));
				}
			}
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}
	
	private static boolean fileInList(String fileName) {
		if(fileList != null){
			for (int i = 0; i < fileList.size(); i++) {
				if (fileName.equalsIgnoreCase(fileList.get(i))) {
					return true;
				}
			}
		}
		return false;
	}
	public static void setFileArrayListNottoDelete(ArrayList<String> arayList){
		fileList = new ArrayList<String>();
		fileList = arayList;
	}
	

	public static boolean writeTextToFile(String msg,String filename){
		boolean retVal = false;
		try {
			String PATH = Environment.getExternalStorageDirectory()
					+ AppState.FILE_FOLDER;
			//filename = filename.concat(".mp4");
			File checkFile = new File(PATH, filename);

			try {
				checkFile.delete();
			} catch (Exception e) {
//				e.printStackTrace();
			}

			File file = new File(PATH);
			//if(file.isDirectory()){
				if(!file.exists())
					file.mkdirs();
			
			File myFile = new File(PATH,filename);
//			retVal = myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(msg);
			myOutWriter.close();
			fOut.close();
			retVal = true;
//			Log.i("ASSEST IDDDDD", readTextFromFile(filename));
		} catch (Exception e) {
		}
		return retVal;
	}
	
	public static String readTextFromFile(String filename){
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
				String aDataRow;
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
}
