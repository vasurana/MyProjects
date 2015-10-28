package in.com.app.network;


import in.com.app.model.IAPIConstants;


public class ServiceURLManager implements IAPIConstants {

	final String base_URL ="http://54.251.255.172:9898";//demo
//	final String base_URL ="http://54.251.108.112:9797"; // xonecast
//	final String base_URL = "http://54.251.108.112:9898";//idea
	public String getURL(int SERVICE_API){
		String service = "";
		switch (SERVICE_API) {
			case API_KEY_MEDIA_TIME:
				service = base_URL + ServiceURL.API_KEY_MEDIA_TIME;
				break;
			case API_KEY_MEDIA_TIME_OFFLINE:
				service = base_URL + ServiceURL.API_KEY_MEDIA_TIME_OFFLINE;
				break;
			case API_KEY_LAYOUT_TIME:
				service = base_URL + ServiceURL.API_KEY_LAYOUT_TIME;
				break;
			case API_KEY_LAYOUT_TIME_OFFLINE:
				service = base_URL + ServiceURL.API_KEY_LAYOUT_TIME_OFFLINE;
				break;
			case API_KEY_ONOFF_SCREEN:
				service = base_URL + ServiceURL.API_KEY_ONOFF_SCREEN;
				break;
			case API_KEY_ONOFF_APP:
				service = base_URL + ServiceURL.API_KEY_ONOFF_APP;
				break;
			case API_KEY_ONOFF_BOX:
				service = base_URL + ServiceURL.API_KEY_ONOFF_BOX;
				break;
			case API_KEY_ONOFF_SCREEN_OFFLINE:
				service = base_URL + ServiceURL.API_KEY_ONOFF_SCREEN_OFFLINE;
				break;
			case API_KEY_ONOFF_APP_OFFLINE:
				service = base_URL + ServiceURL.API_KEY_ONOFF_APP_OFFLINE;
				break;
			case API_KEY_ONOFF_BOX_OFFLINE:
				service = base_URL + ServiceURL.API_KEY_ONOFF_BOX_OFFLINE;
				break;
			case API_KEY_BOX_LOCATION:
				service = base_URL + ServiceURL.API_KEY_BOX_LOCATION;
				break;
			case API_KEY_BOX_INVENTORY_DATA:
				service = base_URL + ServiceURL.API_KEY_BOX_INVENTORY_DATA;
				break;
			case API_KEY_BOX_GET_INVENTORY_DATA:
				service = base_URL + ServiceURL.API_KEY_BOX_GET_INVENTORY_DATA;
				break;
			case API_KEY_BOX_DOWNLOADING_STATUS:
				service = base_URL + ServiceURL.API_KEY_BOX_DOWNLOADING_STATUS;
				break;
			case API_KEY_BOX_UPDATE_APK:
				service = base_URL + ServiceURL.API_KEY_BOX_UPDATE_APK;
				break;
		}
		return service;	
	}




	public String getUrl(int urlKey){
		return getURL(urlKey);
	}

	public String getDownloadBaseUrl(){
		//String demo = "http://54.251.255.172/demo-repo/";
		String demo = "http://192.168.10.146/demo-repo/";
//		String xonecast = "http://repo.xonecast.com/";
//		String idea = "http://ideasignagerepo.xvidiaglobal.com/";
		return demo;
	}


}
