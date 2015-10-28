package com.example.demo1;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.VideoView;

public class Preview extends Activity {

	 VideoView v;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);

           setContentView(R.layout.preview);
           
           MediaController mediaController= new MediaController(this); 
           mediaController.setAnchorView(v);          

           v=(VideoView)findViewById(R.id.vv_preview);
           
           
           Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/videocapture_example.mp4");          

           v.setVideoURI(uri);          
           v.requestFocus();  
           v.start();
    
     }
     
}

