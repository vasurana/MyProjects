package in.com.app;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
    
    public static void setAppContext(Context ctx) {
        context = ctx;
    }
   
}
