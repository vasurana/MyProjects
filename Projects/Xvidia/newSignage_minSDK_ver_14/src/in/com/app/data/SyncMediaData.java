package in.com.app.data;

import in.com.app.domain.DisplayLayout;



public class SyncMediaData {
	long duration = 0;
	long startTimeStamp, endTimeStamp ;
	DisplayLayout.Media media;
	DisplayLayout.Region region;
	
	public DisplayLayout.Region getRegion() {
		return region;
	}

	public void setRegion(DisplayLayout.Region region) {
		this.region = region;
	}

	public DisplayLayout.Media getMedia() {
		return media;
	}

	public void setMedia(DisplayLayout.Media media) {
		this.media = media;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}

	public long getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(long endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

//	public String getMediaName() {
//		return getValidatedString(mediaName);
//	}
//
//	public void setMediaName(String mediaName) {
//		this.mediaName = mediaName;
//	}

//	
//	public int getMediaCount() {
//		return mediaCount;
//	}
//
//	public void setMediaCount(int mediaCount) {
//		this.mediaCount = mediaCount;
//	}

//	public List<DisplayLayout.Media> getMedialist() {
//		return medialist;
//	}
//
//	public void setMedialist(List<DisplayLayout.Media> medialist) {
//		this.medialist = medialist;
//	}

	

	//	static Vector<MusicData> musicList;
	private String getValidatedString(String valStr){
		if(valStr == null){
			return "";
		}else{
			return valStr.trim();
		}
	}

	@Override
	public String toString() {
		return "SyncMediaData [duration=" + duration + ", startTimeStamp="
				+ startTimeStamp + ", endTimeStamp=" + endTimeStamp
				+ ", media=" + media.getMedia().get(0).getUri() + "]";
	}
	
	
}
