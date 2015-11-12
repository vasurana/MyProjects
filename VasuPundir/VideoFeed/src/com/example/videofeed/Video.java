package com.xvidia.vowme.test;

/**
 * Created by Ravi_office on 05-Nov-15.
 */
public class Video {
    private String mIndexPosition;
    private String mUrl;
    private String mId;
    public Video(String indexPosition,String id,String url){
        mIndexPosition = indexPosition;
        mUrl = url;
        mId = id;
    }

    public String getIndexPosition() {
        return mIndexPosition;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getId() {
        return mId;
    }
}
