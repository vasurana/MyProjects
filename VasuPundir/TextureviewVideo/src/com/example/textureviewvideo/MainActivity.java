package com.example.textureviewvideo;

import java.io.IOException;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Surface;
import android.view.TextureView;

public class MainActivity extends Activity implements
		TextureView.SurfaceTextureListener {
	
	MediaPlayer mediaPlayer;
	Uri myUri;

	@Override
	public void onCreate(Bundle arg) {
		super.onCreate(arg);
		setContentView(R.layout.activity_main);

		TextureView textureView = (TextureView) findViewById(R.id.textureview);
		mediaPlayer = new MediaPlayer();
		textureView.setSurfaceTextureListener(this);
		myUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test);
		
	}

	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		
		mediaPlayer.setSurface(new Surface(surface));

		try {
			mediaPlayer.setDataSource(getApplicationContext(), myUri);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
			int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
		// TODO Auto-generated method stub

	}
}
