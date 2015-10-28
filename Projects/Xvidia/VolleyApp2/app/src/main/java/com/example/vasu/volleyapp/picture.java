package com.example.vasu.volleyapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Vasu on 7/22/2015.
 */
public class picture extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
        Bitmap bitmap  = getIntent().getExtras().getParcelable("name");
        ImageView view = (ImageView) findViewById(R.id.imageView_picture);
        view.setImageBitmap(bitmap);
    }



    public void onClick(View v) {
        super.onBackPressed();
    }
}
