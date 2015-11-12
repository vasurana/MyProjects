package com.xvidia.vowme.test;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.user.IdentityProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IVideoDownloadListener {

    private static String TAG = "MainActivity";

    private Context context;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private VideosAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Video> urls;
    VideosDownloader videosDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        urls = new ArrayList<Video>();
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VideosAdapter(MainActivity.this, urls);
        mRecyclerView.setAdapter(mAdapter);

        videosDownloader = new VideosDownloader(context);
        videosDownloader.setOnVideoDownloadListener(this);

        if(Utils.getInstance().hasConnection(context))
        {
            getVideoUrls();

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        int findFirstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

                        Video video;
                        if (urls != null && urls.size() > 0)
                        {
                            if (findFirstCompletelyVisibleItemPosition >= 0) {
                                video = urls.get(findFirstCompletelyVisibleItemPosition);
                                mAdapter.videoPlayerController.setcurrentPositionOfItemToPlay(findFirstCompletelyVisibleItemPosition);
                                mAdapter.videoPlayerController.handlePlayBack(video);
                            }
                            else
                            {
                                video = urls.get(firstVisiblePosition);
                                mAdapter.videoPlayerController.setcurrentPositionOfItemToPlay(firstVisiblePosition);
                                mAdapter.videoPlayerController.handlePlayBack(video);
                            }
                        }
                    }
                }
            });
        }
        else
            Toast.makeText(context, "No internet available", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVideoDownloaded(Video video) {
        mAdapter.videoPlayerController.handlePlayBack(video);
    }

    private void getVideoUrls()
    {
        Video video1 = new Video("0", "1", "11.flv");
        urls.add(video1);
        Video video2 = new Video("1", "2", "webm.webm");
        urls.add(video2);
        Video video3 = new Video("2", "3", "33.avi");
        urls.add(video3);
        Video video4 = new Video("3", "4", "FormatMPG.mpg");
        urls.add(video4);
        Video video5 = new Video("4", "5", "5.mp4");
        urls.add(video5);
       /* Video video6 = new Video("5", "6", "6.mp4");
        urls.add(video6);
        Video video7 = new Video("6", "7", "7.mp4");
        urls.add(video7);*/

        mAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        videosDownloader.startVideosDownloading(urls);
    }
}