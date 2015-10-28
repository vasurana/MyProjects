package in.com.app.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * This class serialises xml for default layout
 * @author Ravi@xvidia
 * @since version 1.0
 *
 */

@Root(name="default")
public class DisplayLayoutScheduleDefaultFile {

	@Attribute(name="file")
	public String file;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "XiboScheduleDefaultFile [file=" + file + "]";
	}
	
	
}
