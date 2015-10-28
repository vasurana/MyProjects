package com.example.demo1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Image extends Activity {
	
	ImageView imageview;
    private static final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        
        imageview = (ImageView) this.findViewById(R.id.iv_image);  
        Button photoButton = (Button) this.findViewById(R.id.b_capture);
        
        photoButton.setOnClickListener(new View.OnClickListener() {  
        	  
            @Override  
            public void onClick(View v) {  
                 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
                 startActivityForResult(cameraIntent, CAMERA_REQUEST);  
            }  
           });  
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST) {  
         Bitmap photo = (Bitmap) data.getExtras().get("data");  
         imageview.setImageBitmap(photo);  
        }  
     } 


    public void onClick(View v) {
        super.onBackPressed();
    }
}
