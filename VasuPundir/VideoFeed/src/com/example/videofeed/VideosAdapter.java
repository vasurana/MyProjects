package com.xvidia.vowme.test;


import android.content.Context;
import android.content.res.Configuration;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private static String TAG = "VideosAdapter";

    Context context;
    private ArrayList<Video> urls;
    public VideoPlayerController videoPlayerController;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ProgressBar progressBar;
        public RelativeLayout layout;

        public ViewHolder(View v) {
            super(v);
            layout = (RelativeLayout) v.findViewById(R.id.layout);
            textView = (TextView) v.findViewById(R.id.textView);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        }
    }

    public VideosAdapter(Context context, final ArrayList<Video> urls) {

        this.context = context;
        this.urls = urls;
        videoPlayerController = new VideoPlayerController(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main, parent, false);

        Configuration configuration = context.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp; //The current width of the available screen space, in dp units, corresponding to screen width resource qualifier.
        int smallestScreenWidthDp = configuration.smallestScreenWidthDp; //The smallest screen size an application will see in normal operation, corresponding to smallest screen width resource qualifier.

        ViewHolder viewHolder = new ViewHolder(v);

        int screenWidthPixels = Utils.getInstance().convertDpToPixel(screenWidthDp, context);
        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, screenWidthPixels);
        viewHolder.layout.setLayoutParams(rel_btn);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Video video = urls.get(position);
        holder.textView.setText("Video " + video.getId());

        final VideoPlayer videoPlayer = new VideoPlayer(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        videoPlayer.setLayoutParams(params);

        holder.layout.addView(videoPlayer);
        videoPlayerController.loadVideo(video, videoPlayer, holder.progressBar);
        videoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoPlayer.changePlayState();
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d(TAG, "onViewRecycledCalled");
        holder.layout.removeAllViews();

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

}