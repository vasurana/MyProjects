package in.com.app.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnOffTimeData extends DataParent {
     String id = null;
     String boxId = null;
     String offTime = null;
     String  onTime = null;
     String  savingTime = null;

	public String getSavingTime() {
		return savingTime;
	}
	public void setSavingTime(String savingTime) {
		this.savingTime = savingTime;
	}
	public String getId() {
		return getValidatedString(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getBoxId() {
		return getValidatedString(boxId);
	}
	public void setBoxId(String stbId) {
		this.boxId = stbId;
	}
	public String getOffTime() {
		return offTime;
	}
	public void setOffTime(String offTime) {
		this.offTime = offTime;
	}
	public String getOnTime() {
		return onTime;
	}
	public void setOnTime(String onTime) {
		this.onTime = onTime;
	}
	
	
	
}
