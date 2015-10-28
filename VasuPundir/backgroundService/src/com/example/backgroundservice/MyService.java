package com.example.backgroundservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

	private static final String TAG = null;
    MediaPlayer player;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		player = MediaPlayer.create(this, R.raw.sound);
		player.setLooping(true);
		player.setVolume(100, 100);
	}
	/*public int onStartCommand(Intent intent, int flags, int startid){
		player.start();
		
		return 1;
	}*/
    
    

 public IBinder onBind(Intent intent) {
  throw new UnsupportedOperationException("Not yet implemented");
 }

  @Override
 public void onStart(Intent intent, int startId) {
  Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
  player.start();
 }

  @Override
 public void onDestroy() {
  Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
  player.stop();
 }

}
