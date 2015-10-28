package com.example.cameraorientation;

import java.io.IOException;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

@SuppressWarnings("deprecation")
public class CustomCameraActivity extends Activity implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView)findViewById(R.id.camerapreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

    }
   
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	
    	//Camera.Parameters parameters = camera.getParameters();
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
        //    parameters.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
           
          //  parameters.setRotation(90);
       }
       /*     else {
                 parameters.set("orientation", "landscape");
                 camera.setDisplayOrientation(0);
                 parameters.setRotation(0);
       }*/
        
        if(previewing){
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        
       
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }
    

}