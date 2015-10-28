package com.example.demo1;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class video extends Activity implements OnClickListener,
		SurfaceHolder.Callback {
	
	MediaRecorder recorder;
	
	Button back, preview;
	SurfaceHolder holder;
	boolean recording = false;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/
		
		recorder = new MediaRecorder();
		setInitials();
		setContentView(R.layout.video);

		SurfaceView cameraView = (SurfaceView) findViewById(R.id.sv_record);
		back = (Button) findViewById(R.id.b_back);
		preview = (Button) findViewById(R.id.b_preview);
		holder = cameraView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		

		preview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent k = new Intent(video.this,Preview.class);
				startActivity(k);

			}
		});

		cameraView.setClickable(true);
		cameraView.setOnClickListener(this);
	}

	

	private void setInitials() {
		
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

		CamcorderProfile cpHigh = CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH);
		recorder.setProfile(cpHigh);
		recorder.setOutputFile("/sdcard/videocapture_example.mp4");
		recorder.setMaxDuration(5000); // 5 seconds
		recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
	}

	private void prepareRecorder() {
		recorder.setPreviewDisplay(holder.getSurface());
	
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			//finish();
		} catch (IOException e) {
			e.printStackTrace();
			//finish();
		}
	}

	public void onClick(View v) {
		if (recording) {
			recorder.stop();
			recording = false;

			// Let's initRecorder so we can record again
			//setInitials();
			//prepareRecorder();
		} else {
			recording = true;
			recorder.start();
			
		}
	//	recorder.start();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		prepareRecorder();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (recording) {
			recorder.stop();
			recording = false;
		}
		recorder.release();
	}

}
