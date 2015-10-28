package in.com.app.data;

import java.util.Date;


public class MediaData {
	String fileName = null;
	int downloadAttempt = 0;
	long downloadDate=0;
	boolean isChecked;
//	static Vector<MusicData> musicList;
	private String getValidatedString(String valStr){
		if(valStr == null){
			return "";
		}else{
			return valStr.trim();
		}
	}
	
	
	
	public String getMediaName() {
		return getValidatedString(fileName);
	}

	public void setMediaName(String cntctName) {
		this.fileName = cntctName;
	}

	public int getMediaDownloadCount() {
		return downloadAttempt;
	}

	public void setDownloadCount(int count) {
		this.downloadAttempt = count;
	}

	public long getMediaDownloadDate() {
		return downloadDate;
	}

	public void setDownloadDate(long date) {
//		Date currentDate = new Date();
//		long dateLong = currentDate.getTime();
//		if(date != null && !date.isEmpty()){
//			dateLong = Long.parseLong(date);
//		}
		this.downloadDate = date;
	}
	
	
}
