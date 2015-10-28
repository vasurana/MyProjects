package com.xvidia.network;


public class ServiceURL {

	public static final String API_KEY_BOX_GET_INVENTORY_DATA = " https://devapi.olacabs.com/v1/products?pickup_lat=28.4602882&pickup_lng=77.0530689";

 public static String getCabStatusURL(String latitude, String longitude){
	 return  "https://devapi.olacabs.com/v1/products?pickup_lat="+ latitude+"&pickup_lng="+ longitude;
 }

}
