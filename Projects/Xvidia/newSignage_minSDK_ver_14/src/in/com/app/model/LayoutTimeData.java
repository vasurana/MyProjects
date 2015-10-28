package in.com.app.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LayoutTimeData extends DataParent {

	String id = null;
	String boxId = null;
	String layoutId = null;
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