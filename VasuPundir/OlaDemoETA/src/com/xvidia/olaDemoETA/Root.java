package com.xvidia.olaDemoETA;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {
	
	
	private List<Categories> categories;
	private Ride_estimate ride_estimate;
	
	public List<Categories> getCategories() {
		return categories;
	}
	public void setCategories(List<Categories> categories) {
		this.categories = categories;
	}
	public Ride_estimate getRide_estimate() {
		return ride_estimate;
	}
	public void setRide_estimate(Ride_estimate ride_estimate) {
		this.ride_estimate = ride_estimate;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getCategories()+", "+getRide_estimate();
	}

}
