package in.com.app.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * This class serialises xml for layout scheduled
 * @author Ravi@xvidia
 * @since version 1.0
 *
 */

@Root(name = "layout")
public class DisplayScheduleLayout {

	/**
	 * <layout file="1" fromdt="2050-12-31 00:00:00" todt="2050-12-31 00:00:00"
	 * scheduleid=""/> <layout file="2" fromdt="2008-12-08 08:00:00"
	 * todt="2008-12-11 21:19:00" scheduleid="1"/>
	 */
	

	@Attribute(required = false)
	String file;
	@Attribute(required = false)
	String fromdt;
	@Attribute(required = false)
	String todt;

	@Attribute(required = false)
	String scheduleid;
	
	@Attribute(required = false)
	String priority;
	
	@Attribute(required = false)
	String dependents;

	@Attribute(required = false)
	String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFromdt() {
		return fromdt;
	}

	public void setFromdt(String fromdt) {
		this.fromdt = fromdt;
	}

	public String getTodt() {
		return todt;
	}

	public void setTodt(String todt) {
		this.todt = todt;
	}

	public String getScheduleid() {
		return scheduleid;
	}

	public void setScheduleid(String scheduleid) {
		this.scheduleid = scheduleid;
	}

	@Override
	public String toString() {
		return "XiboScheduleLayout [file=" + file + ", fromdt=" + fromdt
				+ ", todt=" + todt + ", scheduleid=" + scheduleid + "]";
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDependents() {
		return dependents;
	}

	public void setDependents(String dependents) {
		this.dependents = dependents;
	}
}
