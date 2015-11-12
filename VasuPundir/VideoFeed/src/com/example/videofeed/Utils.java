package com.xvidia.vowme.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

/**
 * Created by Ravi_office on 05-Nov-15.
 */
public class Utils {

    public static final String MY_PREFERENCES = "VIDEOPREFERENCE";
    SharedPreferences sharedpreferences;

    private static  Utils instance = null;
    public static  Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }
    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)(dp * (metrics.densityDpi / 160f));
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public boolean hasConnection(Context context) {
        boolean nwFlag = false;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                nwFlag = true;
            }
        } catch (Exception e) {
//			 e.printStackTrace();
        }

        return nwFlag;
    }

    public void removeAllPreferenceData(Context ctx){
        SharedPreferences settings = ctx.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }
    public void removePreferenceData(Context ctx, String preferenceName){
        SharedPreferences settings = ctx.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        settings.edit().remove(preferenceName).apply();
    }
    private  SharedPreferences getSharedPreference(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(MY_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedpreferences;
    }


    public void savePreferences(Context ctx,String key, boolean prefVal) {
        try {
            SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
            editor.putBoolean(key, prefVal);
            editor.apply();
        } catch (Exception e) {
        }
    }
    public  boolean readPreferences(Context ctx,String key, boolean prefVal) {
        boolean retVal=false;
        try {
            retVal = getSharedPreference(ctx).getBoolean(key, prefVal);
        } catch (Exception e) {
        }
        return retVal;
    }

}
