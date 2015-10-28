package in.com.app.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryData extends DataParent {

	String boxId = null;
	String boxName = null;
	String assetId = null;

	public String getBoxId() {
		return getValidatedString(boxId);
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}


	public String getBoxName() {
		return getValidatedString(boxName);
	}

	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	public String getAssetId() {
		return getValidatedString(assetId);
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}


}
