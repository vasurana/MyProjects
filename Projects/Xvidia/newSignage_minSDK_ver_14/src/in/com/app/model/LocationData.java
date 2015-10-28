package in.com.app.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationData extends DataParent{

    String id = null;
    String lat = null;
    String longitude = null;

	public String getId() {
			return getValidatedString(id);
		}
		public void setId(String id) {
			this.id = id;
		}


		public String getLat() {
			return getValidatedString(lat);
		}
		public void setLat(String lat) {
			this.lat = lat;
		}
		public String getLongitude() {
			return getValidatedString(longitude);
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		
	}
