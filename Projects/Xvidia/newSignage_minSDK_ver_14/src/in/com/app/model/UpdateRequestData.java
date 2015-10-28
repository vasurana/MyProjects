package in.com.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import in.com.app.AppMain;
import in.com.app.data.LogData;
import in.com.app.utility.Utility;

/**
 * This class parse update response from server.
 * @author Ravi@Xvidia
 *	@version 1.0
 *	@since version 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateRequestData extends DataParent {
    public String apkVersion = null;
    public String downloadPath = null;
    public String boxId =null;
    public String boxName =null;

	public boolean getUpdateAvailable(){
		String verStr = getValidatedString(apkVersion);
		String verClientStr =LogData.getInstance().getAppVersion(AppMain.getAppMainContext());// DataCacheManager.getInstance().readSettingData(IDisplayLayout._KEY_XVIDIA_CLIENT_VERSION);
		
		boolean retFlag = false;
		try{
			if(!verStr.equalsIgnoreCase("")){
				Double ver= Utility.convertToDoubleFromString(verStr);
				Double verClient= Utility.convertToDoubleFromString(verClientStr);
					if(ver>verClient){
						retFlag = true;
					}
			}
		}catch(Exception e){
			
		}
		return retFlag;
	}

	/**
	 * This method returns url for the apk to be downloaded
	 * @return string url path to the updated apk
	 *	@since version 1.0
	 */
	public String  getUpdateUrl(){
		return getValidatedString(downloadPath);
		
	}
	
	/**
	 * This method returns version of apk on server
	 * @return string value for version
	 *	@since version 1.0
	 */
	public String  getUpdateVersion(){
		return getValidatedString(apkVersion);
		
	}

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }
}
