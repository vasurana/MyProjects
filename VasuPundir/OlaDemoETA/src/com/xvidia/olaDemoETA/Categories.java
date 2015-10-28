package com.xvidia.olaDemoETA;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Categories {

	private String id;
	private String display_name;
	private String currency;
	private String distance_unit;
	private String time_unit, image;
	private int eta;
	private String distance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDistance_unit() {
		return distance_unit;
	}

	public void setDistance_unit(String distance_unit) {
		this.distance_unit = distance_unit;
	}

	public String getTime_unit() {
		return time_unit;
	}

	public void setTime_unit(String time_unit) {
		this.time_unit = time_unit;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getEta() {
		return eta;
	}

	public void setEta(int eta) {
		this.eta = eta;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("ID=" + getId() + "\n");
		sb.append("Display Name=" + getDisplay_name() + "\n");
		sb.append("Currency=" + getCurrency() + "\n");
		sb.append("Distance Unit=" + getDistance_unit() + "\n");
		sb.append("Time Unit=" + getTime_unit() + "\n");
		sb.append("ETA=" + getEta() + "\n");
		sb.append("Distance=" + getDistance() + "\n");
		sb.append("Image=" + getImage() + "\n");

		return sb.toString();
	}

}
