package com.xvidia.network;

//import in.com.app.MyApplication;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
	private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;
    private static Context mCtx;
    private VolleySingleton() {
//    	mRequestQueue =Volley.newRequestQueue(MyApplication.getAppContext());
    }
    public static synchronized VolleySingleton getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new VolleySingleton();
            mCtx = ctx;
        }
        return mInstance;
    }
    
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx);
        }
        return mRequestQueue;
    }
    
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
