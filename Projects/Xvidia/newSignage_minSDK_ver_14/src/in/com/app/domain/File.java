package in.com.app.domain;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * This class serialises xml for layout scheduled
 * @author Ravi@xvidia
 * @since version 1.0
 *
 */

@Root
public class File {
    private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	   
	
}
