package in.com.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaTimeData extends DataParent {
	String id = null;
	String boxId = null;
	String layoutId = null;
	String scheduledDuration = null;
	String mediaId = null;
	String startTime = null;
	String endTime = null;

	public String getId() {
		return getValidatedString(id);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBoxId() {
		return getValidatedString(boxId);
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public String getLayoutId() {
		return getValidatedString(layoutId);
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getMediaId() {
		return getValidatedString(mediaId);
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getScheduledDuration() {
		return getValidatedString(scheduledDuration);
	}

	public void setScheduledDuration(String scheduledDuration) {
		this.scheduledDuration = scheduledDuration;
	}

	public String getStartTime() {
		return getValidatedString(startTime);
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return getValidatedString(endTime);
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
