package in.com.app.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DownloadStatusData extends DataParent {

	String boxId = null;
	boolean downloadingStatus = false;
	String downloadingMediaId = null;

	public String getDownloadingMediaId() {
		return downloadingMediaId;
	}

	public void setDownloadingMediaId(String downloadingMediaId) {
		this.downloadingMediaId = downloadingMediaId;
	}

	public boolean isDownloadingStatus() {
		return downloadingStatus;
	}

	public void setDownloadingStatus(boolean downloadingStatus) {
		this.downloadingStatus = downloadingStatus;	}


	public String getBoxId() {
		return getValidatedString(boxId);
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
}
