package in.com.app.domain;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * This class serialises xml for schedule
 * @author Ravi@xvidia
 * @since version 1.0
 *
 */

@Root(name = "schedule")
public class DisplayLayoutSchedule {

	@ElementList(name="layout",inline=true)
		private ArrayList<DisplayScheduleLayout> layout;

	@Element(name="default")
	public DisplayLayoutScheduleDefaultFile scheduleDefault;
	
	@ElementList
	private List<File>dependants;
	
	
	
	public ArrayList<DisplayScheduleLayout> getLayout() {
		return layout;
	}



	



	public List<File> getDependants() {
		return dependants;
	}







	public void setDependants(List<File> dependants) {
		this.dependants = dependants;
	}







	public void setLayout(ArrayList<DisplayScheduleLayout> layout) {
		this.layout = layout;
	}

	
	public DisplayLayoutScheduleDefaultFile getScheduleDefault() {
		return scheduleDefault;
	}

	public void setScheduleDefault(DisplayLayoutScheduleDefaultFile scheduleDefault) {
		this.scheduleDefault = scheduleDefault;
	}

	@Override
	public String toString() {
		return "XiboSchedule [layout=" + layout + ", scheduleDefault="
				+ scheduleDefault + "]";
	}
	
	
	
	
	
	
	

}
