package in.com.app.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * This class serialises xml for file
 * @author Ravi@xvidia
 * @since version 1.0
 *
 */
@Root(name="file")
public class DisplayLayoutFile {
	
	@Attribute(required=false)
	String id;
	@Attribute(required=false)
	String path;
	@Attribute(required=false)
	String size;
	@Attribute(required=false)
	String md5;
	@Attribute
	String type;
	@Attribute(required=false)
	String download;

	@Attribute(required=false)
	String layoutid;
	@Attribute(required=false)
	String regionid;
	@Attribute(required=false)
	String mediaid;
	@Attribute(required=false)
	String updated;
	
	public String getLayoutid() {
		return layoutid;
	}
	public void setLayoutid(String layoutid) {
		this.layoutid = layoutid;
	}
	public String getRegionid() {
		return regionid;
	}
	public void setRegionid(String regionid) {
		this.regionid = regionid;
	}
	public String getMediaid() {
		return mediaid;
	}
	public void setMediaid(String mediaid) {
		this.mediaid = mediaid;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	@Override
	public String toString() {
		return "XiboFile [id=" + id + ", path=" + path + ", size=" + size
				+ ", md5=" + md5 + ", type=" + type + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
	    return (obj instanceof DisplayLayoutFile) && this.path == ((DisplayLayoutFile)obj).getPath() && this.type != "resource";
	}
	

}
