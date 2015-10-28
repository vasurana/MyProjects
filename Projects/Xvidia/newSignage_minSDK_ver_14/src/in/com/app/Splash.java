package in.com.app;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import in.com.app.utility.MyExceptionHandler;

public class Splash extends Activity{
int currentApiVersion;
static Context ctx;
//String user_name = System.getProperty("user.name", "n/a");
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		try{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		  currentApiVersion = android.os.Build.VERSION.SDK_INT;

		    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		        | View.SYSTEM_UI_FLAG_FULLSCREEN
		        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

		    // This work only for android 4.4+
		    if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
		    {
		        getWindow().getDecorView().setSystemUiVisibility(flags);

		        // Code below is to handle presses of Volume up or Volume down.
		        // Without this, after pressing volume buttons, the navigation bar will
		        // show up and won't hide
		        final View decorView = getWindow().getDecorView();
		        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
		            {

		                @Override
		                public void onSystemUiVisibilityChange(int visibility)
		                {
		                    if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
		                    {
		                        decorView.setSystemUiVisibility(flags);
		                    }
		                }
		            });
		    }
		} catch(Exception e){
//			e.printStackTrace();
		}
		setContentView(R.layout.layout_splash);
		ctx = this;

		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this,Splash.class));
		try {
				updateNewFolderStructure();
//				start();
//				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//				Date now = new Date();
//				String timeNew = formatter.format(now);
//				String time = LogData.getInstance().getOntimeId(ctx);
//				if(time==null){
//					time = timeNew;
//				}else if(time.isEmpty()){
//					time = timeNew;
//				}
//				LogData.getInstance().setOntimeId(ctx, time);
//				String id = LogData.getInstance().getAppID(ctx)+"_"+"BOX"+"_"+now.getTime();
//				OnOffTimeData object = new OnOffTimeData();
//		    	object.setId(id);
//		    	object.setBoxId(LogData.getInstance().getAppID(ctx));
//		    	object.setOnTime(LogData.getInstance().getOntimeId(ctx));
//		    	object.setSavingTime(time);
//		    	object.setOffTime("");
////		    	Log.e("BOOTRECIEVR",time);
////		    	object.setBoxId(LogData.getInstance().getAppID(context));
//		    	DataCacheManager.getInstance(ctx).saveOnOffTimeData(MySqliteHelper.TABLE_NAME_ONOFF_TIME_BOX_TABLE, object);
		 
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {  
					startApp();
				}
			}, 2000);
		} catch(Exception e){
//			e.printStackTrace();
		}
	}
@Override
public void onWindowFocusChanged(boolean hasFocus)
{
    super.onWindowFocusChanged(hasFocus);
    if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
    {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
	private void updateNewFolderStructure(){
		try{
			String destPath = Environment.getExternalStorageDirectory()
					+ AppState.DISPLAY_FOLDER;
			String sourcePath = Environment.getExternalStorageDirectory()
					+ AppState.OLD_DISPLAY_FOLDER;
			 File sourceLocation = new File (sourcePath);
			 File targetLocation = new File (destPath);
			 if (sourceLocation.isDirectory() && sourceLocation.exists()) {		 
				 try {
					FileManager.copyDirectoryOldToNew(sourceLocation, targetLocation);		
					FileManager.deleteDir(sourceLocation);
				 } catch (IOException e) {
					 e.printStackTrace();
					}
			 }
		} catch(Exception e){
//			e.printStackTrace();
		}
	}
	void startApp(){
		Intent i = new Intent(Splash.this, AppMain.class);
		startActivity(i);
		finish();
	}

//	   public static void showMesg(String msg){
//		   Toast.makeText(ctx,msg, 5000).show();
//	    }
	@Override
	public void onBackPressed() {
		
	}
}
