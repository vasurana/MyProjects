package in.com.app.domain;

import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * This class serialises xml for file list
 * @author Ravi@xvidia
 * @since version 1.0
 *
 */ 
@Root(name="files")
public class DisplayLayoutFiles {
	
	@ElementList(name="file",inline=true)
	private ArrayList<DisplayLayoutFile> file;

	public ArrayList<DisplayLayoutFile> getFileList() {
		return file;
	}

	public void setFileList(ArrayList<DisplayLayoutFile> fileList) {
		this.file = fileList;
	}
	
}
