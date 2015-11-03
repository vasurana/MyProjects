package com.example.newvideofeed;

import android.graphics.SurfaceTexture;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[] dataSource;
    public RecyclerAdapter(String[] dataArgs){
        dataSource = dataArgs;
 
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
 
        ViewHolder holder = new ViewHolder(view); 
        return holder;
 
    }
 
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(dataSource[position]);
    }
 
    @Override
    public int getItemCount() {
        return dataSource.length;
    }
 
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public TextureView textureView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

}