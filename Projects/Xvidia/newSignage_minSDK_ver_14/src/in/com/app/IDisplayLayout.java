package in.com.app;

public interface IDisplayLayout {


//	public final String _MEDIATYPE_HTML = "";
	public final String _MEDIATYPE_IMAGE = "image";
	public final String _MEDIATYPE_TEXT = "text";
//	public final String _MEDIATYPE_FLASH = "flash";
	public final String _MEDIATYPE_EMBEDEDHTML = "embedded";
	public final String _MEDIATYPE_WEBPAGE = "webpage";
//	public final String _MEDIATYPE_MICROBLOG_SEARCH = "microblog";
//	public final String _MEDIATYPE_POWERPOINT = "powerpoint";
	public final String _MEDIATYPE_TICKER = "ticker";
	public final String _MEDIATYPE_VIDEO = "video";
	public final String _MEDIATYPE_URL_STREAM_VIDEO = "localvideo";
	public final String _FILE_TYPE_LAYOUT= "layout";


	/////////CONFIG PARAMS///
	public final String _BLACKLIST_CATEGORY = "blacklist";
//	public final String _VERSION = "4";
	public final String _MEDIA = "media";

	public final int _CHUNK_SIZE_512_KB = 512000; // 500kb 


	///NOTE DONOT ASSIGN VALUE 0 TO ANY KEY////
	///DB WILLNOT SAVE THAT VALUE WITH KEY VALUE EQUALS 0
	public final int _KEY_XVIDIA_SERVER_KEY = 1;
	public final int _KEY_XVIDIA_DISPLAY_NAME = 2;
	public final int _KEY_XVIDIA_HARDWARE_KEY = 3; 
	public final int _KEY_XVIDIA_SERVER_IP = 4;
	public final int _KEY_XVIDIA_CLIENT_VERSION = 5;
	public final int _KEY_XVIDIA_NEW_LAYOUT = 5;
	public final int _KEY_XVIDIA_CURRENT_DISPLAY_XML = 6;	
	public final int _KEY_XVIDIA_STATE_REG_COMPLETE = 7;
	public final int _KEY_XVIDIA_STATE_SCHEDULE_COMPLETE= 8;
	public final int _KEY_XVIDIA_STATE_FILE_COMPLETE = 9;
	public final int _KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE = 10;
	public final int _KEY_XVIDIA_STATE_NETWORK_ON_TIME = 11;
	public final int _KEY_XVIDIA_STATE_NETWORK_OFF_TIME= 12;
	public final int _KEY_XVIDIA_STATE_POWER_ON_TIME =13;
	public final int _KEY_XVIDIA_STATE_POWER_OFF_TIME = 14;
	public final int _KEY_XVIDIA_STATE_USB_ATTACH_TIME =15;
	public final int _KEY_XVIDIA_STATE_USB_DETACH_TIME= 16;
	public final int _KEY_XVIDIA_STATE_NETWORK_TYPE= 17;
	public final int _KEY_XVIDIA_STATE_LOCATION= 18;
	public final int _KEY_XVIDIA_NEW_DISPLAY_XML= 19;
	public final int _KEY_XVIDIA_BACKGROUND_FILE_DOWNLOAD_COMPLETE= 20;
	public final int _KEY_XVIDIA_ADDRESS= 21;
	public final int _KEY_XVIDIA_STATE_SCHEDULE_CURRENTFILE= 22;
	public final int _KEY_XVIDIA_STATE_SCHEDULE_XML= 23;
	
	public final int ORIENTATION_PORTRAIT= 1;
	public final int ORIENTATION_REVERSE_PORTRAIT= 2;
	public final int ORIENTATION_LANSCAPE = 3;
	public final int ORIENTATION_REVERSE_LANSCAPE = 4;
	//	_DISPLAYNAME
	//	public final String _SERIALIZED_OBJ = new String ("SERIALIZED_OBJ");

	final String _ACTION_FROM_SIGNAGE = "ACTION_FROM_SIGNAGE";


	public final String DISPLAY_STATE_REG_COMPLETE = "_DISPLAY_STATE_REG_COMPLETE";
	public final String DISPLAY_STATE_SCHEDULE_COMPLETE= "_DISPLAY_STATE_SCHEDULE_COMPLETE";
	public final String DISPLAY_STATE_FILE_COMPLETE = "_DISPLAY_STATE_FILE_COMPLETE";
//	public final String DISPLAY_STATE_INDIVIDUALFILE_COMPLETE = "_DISPLAY_STATE_INDIVIDUALFILE_COMPLETE";


	public final String FLAG_TRUE = "_TRUE";
	public final String FLAG_FALSE = "_FALSE";

}
