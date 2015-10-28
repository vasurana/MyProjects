package com.example.vasu.volleyapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity implements OnClickListener, SurfaceHolder.Callback {

    ImageView imageView;
    Button image, record, background, send, get;
    TextView tvSend, tvGet;
    private static final int CAMERA_REQUEST = 1888;

    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        recorder = new MediaRecorder();
        initRecorder();
        setContentView(R.layout.record);

        SurfaceView cameraView = (SurfaceView) findViewById(R.id.CameraView);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);

        setContentView(R.layout.activity_main);
        tvSend = (TextView) findViewById(R.id.textView_send);
        tvGet = (TextView) findViewById(R.id.textView_get);
        image = (Button) findViewById(R.id.button_captureImage);
        record = (Button) findViewById(R.id.button_record);
        background = (Button) findViewById(R.id.button_StartBackgroundService);
        send = (Button) findViewById(R.id.button_send);
        get = (Button) findViewById(R.id.button_get);
        image.setOnClickListener(this);
        record.setOnClickListener(this);
        background.setOnClickListener(this);
        send.setOnClickListener(this);
        get.setOnClickListener(this);

    }


    private void initRecorder() {
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH);
        recorder.setProfile(cpHigh);
        recorder.setOutputFile("/sdcard/videocapture_example.mp4");
        recorder.setMaxDuration(10000); // 10 seconds
        recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId())

        {

            case R.id.button_captureImage:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            case R.id.button_record:

                if (recording) {
                    recorder.stop();
                    recording = false;

                    // Let's initRecorder so we can record again
                    initRecorder();
                    prepareRecorder();
                } else {
                    recording = true;
                    recorder.start();
                }



            case R.id.button_StartBackgroundService:


            case R.id.button_send:


            case R.id.button_get:


    }

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
        finish();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap imageData = null;
            if (resultCode == RESULT_OK)
            {
                imageData = (Bitmap) data.getExtras().get("data");

                Intent i = new Intent(this, picture.class);
                i.putExtra("name", imageData );
                startActivity(i);

            }
        }
    }
}

