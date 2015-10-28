package in.com.app.data;

/**
 * This class maintains information of the currently displayed downloaded file 
 * @author Ravi@xvidia
 *
 */
public class RawData {

	public String type = "";
	public String filename = "";
	public byte[] dataBytes = null;
	public int dataLength = 0;
	public String id = "";
	public boolean dataSoredInDB = false;
	 public RawData(String idImage, String fileName, String fileType, byte[] bytes, int datalen){
		 id = idImage;
		 filename = fileName;
		 type = fileType;
		 dataBytes = bytes;		 
		 dataLength = datalen;
	 }
	 
	 public RawData(String idImage, String fileName, String fileType, int datalen){
		 id = idImage;
		 filename = fileName;
		 type = fileType;
		 dataBytes = null;		 
		 dataLength = datalen;
		 dataSoredInDB = true;
	 }
}
