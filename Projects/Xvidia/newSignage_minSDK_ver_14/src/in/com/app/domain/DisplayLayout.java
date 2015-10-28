package in.com.app.domain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

 /**
  * This class serialises xml for layout
  * @author Ravi@xvidia
  * @since version 1.0
  *
  */
@Root(name = "layout")
public class DisplayLayout {
	
	

	@ElementList(entry = "region", inline = true)
	protected List<DisplayLayout.Region> region;
	@ElementList(entry = "tags", inline = true, required = false)
	protected List<DisplayLayout.Tags> tags;
	@Attribute(name = "schemaVersion")
	protected BigInteger schemaVersion;
	@Attribute(name = "width", required = true)
	protected float width;
	@Attribute(name = "height", required = true)
	protected float height;
	@Attribute(name = "bgcolor")
	protected String bgcolor;
	@Attribute(name = "background", required = false)
	protected String background;
	@Attribute(name = "resolutionid", required = false)
	protected String resolutionid;
	
	public String getResolutionid() {
		return resolutionid;
	}

	public void setResolutionid(String resolutionid) {
		this.resolutionid = resolutionid;
	}

	public List<DisplayLayout.Region> getRegion() {
		if (region == null) {
			region = new ArrayList<DisplayLayout.Region>();
		}
		return this.region;
	}
	
	public List<DisplayLayout.Tags> getTag() {
		if (tags == null) {
			tags = new ArrayList<DisplayLayout.Tags>();
		}
		return this.tags;
	}

	public BigInteger getSchemaVersion() {
		if (schemaVersion == null) {
			return new BigInteger("1");
		} else {
			return schemaVersion;
		}
	}

	public void setSchemaVersion(BigInteger value) {
		this.schemaVersion = value;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float value) {
		this.width = value;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float value) {
		this.height = value;
	}

	public String getBgcolor() {
		if (bgcolor == null) {
			return "#000000";
		} else {
			return bgcolor;
		}
	}

	public void setBgcolor(String value) {
		this.bgcolor = value;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String value) {
		this.background = value;
	}

	
	public static class Tags{
		@ElementList(entry = "tag", inline = true)
		protected List<Tag> tag;
		
	}
	

	public static class Tag {}
	
	
	public static class Region {
		@ElementList(entry = "media", inline = true)
		protected List<Media> media;
		@ElementList(entry = "options", inline = true, required = false)
		protected List<RegionOptions> options;
		@Attribute(name = "id", required = true)
		protected String id;
		@Attribute(name = "width", required = true)
		protected float width;
		@Attribute(name = "height", required = true)
		protected float height;
		@Attribute(name = "top", required = true)
		protected float top;
		@Attribute(name = "left", required = true)
		protected float left;
		@Attribute(name = "userId", required = false)
		protected String userId;
		@Attribute(name = "name", required = false)
		protected String name;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public List<Media> getMedia() {
			if (media == null) {
				media = new ArrayList<Media>();
			}
			return this.media;
		}

		public String getId() {
			return id;
		}

		public void setId(String value) {
			this.id = value;
		}

		public float getWidth() {
			return width;
		}

		public void setWidth(float value) {
			this.width = value;
		}

		public float getHeight() {
			return height;
		}

		public void setHeight(float value) {
			this.height = value;
		}

		public float getTop() {
			return top;
		}
		public void setTop(float value) {
			this.top = value;
		}

		public float getLeft() {
			return left;
		}

		public void setLeft(float value) {
			this.left = value;
		}

		@Override
		public String toString() {
			return "Region [media=" + media + ", id=" + id + ", width=" + width
					+ ", height=" + height + ", top=" + top + ", left=" + left
					+ "]";
		}

	}

	public static class Media {
		@Attribute(name="id")
		protected String id;
		@Attribute(name="duration")
		protected String duration;
		@Attribute(name="type")
		protected String type;
		@Attribute(name="lkid")
		protected String lkid;
		@Attribute(name="schemaVersion")
		protected String schemaVersion;
		@Attribute(name="userId" , required=false)
		protected String userId;
		@Attribute(name="render")
		protected String render;
		public String getRender() {
			return render;
		}
		public void setRender(String render) {
			this.render = render;
		}
		@ElementList(entry = "options", inline = true)
		protected List<MediaOptions> media;
		@ElementList(entry = "raw", inline = true)
		protected List<Raw> raw;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getLkid() {
			return lkid;
		}
		public void setLkid(String lkid) {
			this.lkid = lkid;
		}
		public String getSchemaVersion() {
			return schemaVersion;
		}
		public void setSchemaVersion(String schemaVersion) {
			this.schemaVersion = schemaVersion;
		}
		
		public List<MediaOptions> getMedia() {
			return media;
		}
		public void setMedia(List<MediaOptions> media) {
			this.media = media;
		}
		public List<Raw> getRaw() {
			return raw;
		}
		public void setRaw(List<Raw> raw) {
			this.raw = raw;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		@Override
		public String toString() {
			return "Media [id=" + id + ", duration=" + duration + ", type="
					+ type + ", lkid=" + lkid + ", schemaVersion="
					+ schemaVersion + ", userId=" + userId + ", media=" + media
					+ ", raw=" + raw + "]";
		}
	}

	public static class Raw {
		@Element(name = "template",required=false,data=true)
		protected String template;
		
		@Element(name = "css",required=false,data=true)
		protected String css;
		
		@Element(name = "text",required=false,data=true)
		protected String text;
		@Element(name = "embedHtml",required=false,data=true)
		protected String embedHtml;
		@Element(name = "embedScript",required=false,data=true)
		protected String embedScript;
		@Element(name = "embedStyle",required=false,data=true)
		protected String embedStyle;

		public String getEmbedStyle() {
			return embedStyle;
		}

		public void setEmbedStyle(String embedStyle) {
			this.embedStyle = embedStyle;
		}

		public String getCss() {
			return css;
		}
		public void setCss(String css) {
			this.css = css;
		}
		public String getTemplate() {
			return template;
		}
		public void setTemplate(String template) {
			this.template = template;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getEmbedHtml() {
			return embedHtml;
		}
		public void setEmbedHtml(String embedHtml) {
			this.embedHtml = embedHtml;
		}
		public String getEmbedScript() {
			return embedScript;
		}
		public void setEmbedScript(String embedScript) {
			this.embedScript = embedScript;
		}
		@Override
		public String toString() {
			return "Raw [template=" + template + ", text=" + text
					+ ", embedHtml=" + embedHtml + ", embedScript="
					+ embedScript + "]";
		}
	}
	public static class RegionOptions {
		@Element(name = "transOut", required = false)
		protected String transOut;
		@Element(name="transOutDuration",required=false )
		protected String transOutDuration;
		@Element(name="transOutDirection",required=false )
		protected String transOutDirection;
		@Element(name = "loop", required = false)
		protected String loop;
		public String getTransOut() {
			return transOut;
		}
		public void setTransOut(String transOut) {
			this.transOut = transOut;
		}
		public String getTransOutDuration() {
			return transOutDuration;
		}
		public void setTransOutDuration(String transOutDuration) {
			this.transOutDuration = transOutDuration;
		}
		public String getTransOutDirection() {
			return transOutDirection;
		}
		public void setTransOutDirection(String transOutDirection) {
			this.transOutDirection = transOutDirection;
		}
		public String getLoop() {
			return loop;
		}
		public void setLoop(String loop) {
			this.loop = loop;
		}
	}
	public static class MediaOptions {

		@Element(name = "xmds", required = false)
		protected String xmds;
		@Element(name = "sourceId", required = false)
		protected String sourceId;
		@Element(name = "datasetid", required = false)
		protected String datasetid;
		@Element(name = "overrideTemplate", required = false)
		protected String overrideTemplate;
		@Element(name = "name", required = false)
		protected String name;
		@Element(name = "loop", required = false)
		protected String loop;
		@Element(name = "effect", required = false)
		protected String effect;
		@Element(name = "term", required = false)
		protected String term;
		@Element(name="copyright",required=false )
		protected String copyright;
		@Element(name="scrollSpeed",required=false )
		protected String scrollSpeed;
		@Element(name = "updateInterval", required = false)
		protected String updateInterval;
		@Element(name = "numItems", required = false)
		protected String numItems;
		@Element(name = "takeItemsFrom", required = false)
		protected String takeItemsFrom;
		@Element(name = "durationIsPerItem", required = false)
		protected String durationIsPerItem;
		@Element(name = "itemsSideBySide", required = false)
		protected String itemsSideBySide;
		@Element(name = "upperLimit", required = false)
		protected String upperLimit;
		@Element(name = "lowerLimit", required = false)
		protected String lowerLimit;
		@Element(name = "filter", required = false)
		protected String filter;
		@Element(name = "ordering", required = false)
		protected String ordering;
		@Element(name = "itemsPerPage", required = false)
		protected String itemsPerPage;
		@Element(name = "dateFormat", required = false)
		protected String dateFormat;
		@Element(name = "stripTags", required = false)
		protected String stripTags;
		@Element(name = "disableDateSort", required = false)
		protected String disableDateSort;
		@Element(name = "textDirection", required = false)
		protected String textDirection;
		@Element(name = "templateId", required = false)
		protected String templateId;
		@Element(name = "allowedAttributes", required = false)
		protected String allowedAttributes;
		@Element(name = "fitText", required = false)
		protected String fitText;
		@Element(name = "speed", required = false)
		protected String speed;
		@Element(name = "length", required = false)
		protected String length;
		@Element(name = "twitter", required = false)
		protected String twitter;
		@Element(name = "identica", required = false)
		protected String identica;
		@Element(name = "direction", required = false)
		protected String direction;
		@Element(name = "uri", required = false)
		protected String uri;
		@Element(name = "backgroundColor", required = false)
		protected String backgroundColor;
		@Element(name = "mute", required = false)
		protected String mute;
		@Element(name = "align", required = false)
		protected String align;
		@Element(name = "scaleType", required = false)
		protected String scaleType;
		@Element(name = "valign", required = false)
		protected String valign;
		@Element(name = "transIn", required = false)
		protected String transIn;
		@Element(name = "transInDuration", required = false)
		protected String transInDuration;
		@Element(name = "transInDirection", required = false)
		protected String transInDirection;
		@Element(name = "transOut", required = false)
		protected String transOut;
		@Element(name = "transOutDuration", required = false)
		protected String transOutDuration;
		@Element(name = "transOutDirection", required = false)
		protected String transOutDirection;
		@Element(name = "scaling", required = false)
		protected String scaling;
		@Element(name = "transparency", required = false)
		protected String transparency;
		@Element(name = "offsetLeft", required = false)
		protected String offsetLeft;
		@Element(name = "offsetTop", required = false)
		protected String offsetTop;

		@Element(name = "pageWidth", required = false)
		protected String pageWidth;
		@Element(name = "pageHeight", required = false)
		protected String pageHeight;
		@Element(name = "modeid", required = false)
		protected String modeid;
		@Element(name = "scaleContent", required = false)
		protected String scaleContent;

		public String getScaleContent() {
			return scaleContent;
		}

		public void setScaleContent(String scaleContent) {
			this.scaleContent = scaleContent;
		}

		public String getScaling() {
			return scaling;
		}

		public void setScaling(String scaling) {
			this.scaling = scaling;
		}

		public String getModeid() {
			return modeid;
		}

		public void setModeid(String modeid) {
			this.modeid = modeid;
		}

		public String getPageHeight() {
			return pageHeight;
		}

		public void setPageHeight(String pageHeight) {
			this.pageHeight = pageHeight;
		}

		public String getPageWidth() {
			return pageWidth;
		}

		public void setPageWidth(String pageWidth) {
			this.pageWidth = pageWidth;
		}

		public String getOffsetLeft() {
			return offsetLeft;
		}

		public void setOffsetLeft(String offsetLeft) {
			this.offsetLeft = offsetLeft;
		}

		public String getOffsetTop() {
			return offsetTop;
		}

		public void setOffsetTop(String offsetTop) {
			this.offsetTop = offsetTop;
		}

		public String getTransparency() {
			return transparency;
		}

		public void setTransparency(String transparency) {
			this.transparency = transparency;
		}

		public String getTransOutDirection() {
			return transOutDirection;
		}

		public void setTransOutDirection(String transOutDirection) {
			this.transOutDirection = transOutDirection;
		}

		public String getTransOutDuration() {
			return transOutDuration;
		}

		public void setTransOutDuration(String transOutDuration) {
			this.transOutDuration = transOutDuration;
		}

		public String getTransOut() {
			return transOut;
		}

		public void setTransOut(String transOut) {
			this.transOut = transOut;
		}

		public String getTransInDirection() {
			return transInDirection;
		}

		public void setTransInDirection(String transInDirection) {
			this.transInDirection = transInDirection;
		}

		public String getTransInDuration() {
			return transInDuration;
		}

		public void setTransInDuration(String transInDuration) {
			this.transInDuration = transInDuration;
		}

		public String getTransIn() {
			return transIn;
		}

		public void setTransIn(String transIn) {
			this.transIn = transIn;
		}


		public String getAlign() {
			return align;
		}
		public void setAlign(String align) {
			this.align = align;
		}
		public String getScaleType() {
			return scaleType;
		}
		public void setScaleType(String scaleType) {
			this.scaleType = scaleType;
		}
		public String getValign() {
			return valign;
		}
		public void setValign(String valign) {
			this.valign = valign;
		}
		public String getMute() {
			return mute;
		}
		public void setMute(String mute) {
			this.mute = mute;
		}
		public String getLoop() {
			return loop;
		}
		public void setLoop(String loop) {
			this.loop = loop;
		}
		public String getBackgroundColor() {
			return backgroundColor;
		}
		public void setBackgroundColor(String backgroundColor) {
			this.backgroundColor = backgroundColor;
		}
		public String getAllowedAttributes() {
			return allowedAttributes;
		}
		public void setAllowedAttributes(String allowedAttributes) {
			this.allowedAttributes = allowedAttributes;
		}
		public String getXmds() {
			return xmds;
		}
		public void setXmds(String xmds) {
			this.xmds = xmds;
		}
		public String getSourceId() {
			return sourceId;
		}
		public void setSourceId(String sourceId) {
			this.sourceId = sourceId;
		}
		public String getDatasetid() {
			return datasetid;
		}
		public void setDatasetid(String datasetid) {
			this.datasetid = datasetid;
		}
		public String getOverrideTemplate() {
			return overrideTemplate;
		}
		public void setOverrideTemplate(String overrideTemplate) {
			this.overrideTemplate = overrideTemplate;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEffect() {
			return effect;
		}
		public void setEffect(String effect) {
			this.effect = effect;
		}
		public String getItemsSideBySide() {
			return itemsSideBySide;
		}
		public void setItemsSideBySide(String itemsSideBySide) {
			this.itemsSideBySide = itemsSideBySide;
		}
		public String getUpperLimit() {
			return upperLimit;
		}
		public void setUpperLimit(String upperLimit) {
			this.upperLimit = upperLimit;
		}
		public String getLowerLimit() {
			return lowerLimit;
		}
		public void setLowerLimit(String lowerLimit) {
			this.lowerLimit = lowerLimit;
		}
		public String getFilter() {
			return filter;
		}
		public void setFilter(String filter) {
			this.filter = filter;
		}
		public String getOrdering() {
			return ordering;
		}
		public void setOrdering(String ordering) {
			this.ordering = ordering;
		}
		public String getItemsPerPage() {
			return itemsPerPage;
		}
		public void setItemsPerPage(String itemsPerPage) {
			this.itemsPerPage = itemsPerPage;
		}
		public String getDateFormat() {
			return dateFormat;
		}
		public void setDateFormat(String dateFormat) {
			this.dateFormat = dateFormat;
		}
		public String getStripTags() {
			return stripTags;
		}
		public void setStripTags(String stripTags) {
			this.stripTags = stripTags;
		}
		public String getDisableDateSort() {
			return disableDateSort;
		}
		public void setDisableDateSort(String disableDateSort) {
			this.disableDateSort = disableDateSort;
		}
		public String getTextDirection() {
			return textDirection;
		}
		public void setTextDirection(String textDirection) {
			this.textDirection = textDirection;
		}
		public String getTemplateId() {
			return templateId;
		}
		public void setTemplateId(String templateId) {
			this.templateId = templateId;
		}
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		public String getDirection() {
			return direction;
		}
		public void setDirection(String direction) {
			this.direction = direction;
		}
		public String getTerm() {
			return term;
		}
		public void setTerm(String term) {
			this.term = term;
		}
		public String getUpdateInterval() {
			return updateInterval;
		}
		public void setUpdateInterval(String updateInterval) {
			this.updateInterval = updateInterval;
		}
		public String getSpeed() {
			return speed;
		}
		public void setSpeed(String speed) {
			this.speed = speed;
		}
		public String getLength() {
			return length;
		}
		public void setLength(String length) {
			this.length = length;
		}
		public String getTwitter() {
			return twitter;
		}
		public void setTwitter(String twitter) {
			this.twitter = twitter;
		}
		public String getIdentica() {
			return identica;
		}
		public void setIdentica(String identica) {
			this.identica = identica;
		}
		public String getCopyright() {
			return copyright;
		}
		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}
		public String getScrollSpeed() {
			return scrollSpeed;
		}
		public void setScrollSpeed(String scrollSpeed) {
			this.scrollSpeed = scrollSpeed;
		}
		public String getNumItems() {
			return numItems;
		}
		public void setNumItems(String numItems) {
			this.numItems = numItems;
		}
		public String getTakeItemsFrom() {
			return takeItemsFrom;
		}
		public void setTakeItemsFrom(String takeItemsFrom) {
			this.takeItemsFrom = takeItemsFrom;
		}
		public String getDurationIsPerItem() {
			return durationIsPerItem;
		}
		public void setDurationIsPerItem(String durationIsPerItem) {
			this.durationIsPerItem = durationIsPerItem;
		}
		public String getFitText() {
			return fitText;
		}
		public void setFitText(String fitText) {
			this.fitText = fitText;
		}
		@Override
		public String toString() {
			return "MediaOptions [term=" + term + ", copyright=" + copyright
					+ ", scrollSpeed=" + scrollSpeed + ", updateInterval="
					+ updateInterval + ", numItems=" + numItems
					+ ", takeItemsFrom=" + takeItemsFrom
					+ ", durationIsPerItem=" + durationIsPerItem + ", fitText="
					+ fitText + ", speed=" + speed + ", length=" + length
					+ ", twitter=" + twitter + ", identica=" + identica
					+ ", direction=" + direction + ", uri=" + uri + "]";
		}
	}
	@Override
	public String toString() {
		return "Layout [region=" + region + ", schemaVersion=" + schemaVersion
				+ ", width=" + width + ", height=" + height + ", bgcolor="
				+ bgcolor + ", background=" + background + "]";
	}
}
