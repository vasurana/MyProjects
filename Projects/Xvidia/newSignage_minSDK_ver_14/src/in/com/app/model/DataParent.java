package in.com.app.model;

public class DataParent {
	protected String getValidatedString(String value){
		if(value!=null)
			return value;
		else
			return "";
	}
	
}
