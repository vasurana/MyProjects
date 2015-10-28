package in.com.app.data;

import android.os.Handler;

import in.com.app.domain.DisplayLayout;
import in.com.app.model.MediaTimeData;

import java.util.Date;
import java.util.List;


public class RefreshMediaData {
	String duration = null;
	long timestamp ;
	DisplayLayout.Region region=null;
	int mediaCount = 0;

	int viewId = 0;
	Handler callbackHandler;
	Runnable callbackRunnable;
	MediaTimeData mediaTimeData;

	public Runnable getCallbackRunnable() {
		return callbackRunnable;
	}
	public void setCallbackRunnable(Runnable callbackRunnable) {
		this.callbackRunnable = callbackRunnable;
	}

	public int getViewId() {
		return viewId;
	}
	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	public Handler getCallbackHandler() {
		return callbackHandler;
	}
	public void setCallbackHandler(Handler callbackHandler) {
		this.callbackHandler = callbackHandler;
	}

	public MediaTimeData getMediaTimeData() {
		return mediaTimeData;
	}
	public void setMediaTimeData(MediaTimeData mediaTimeData) {
		this.mediaTimeData = mediaTimeData;
	}
	
	public int getMediaCount() {
		return mediaCount;
	}
	public void setMediaCount(int mediaCount) {
		this.mediaCount = mediaCount;
	}

	public String getDuration() {
		return getValidatedString(duration);
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public DisplayLayout.Region getRegion() {
		return region;
	}
	public void setRegion(DisplayLayout.Region region) {
		this.region = region;
	}

	//	static Vector<MusicData> musicList;
	protected String getValidatedString(String valStr){
		if(valStr == null){
			return "";
		}else{
			return valStr.trim();
		}
	}
	
	
	
}
