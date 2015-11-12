package com.example.videofeed;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Ravi_office on 05-Nov-15.
 */
public class FileCache {
    private File file ;
    Context context;

    public File getFile(String url) {
        String localPath = Environment.getExternalStorageDirectory().getPath() + "/" + url;
        file = new File(localPath);
        return file ;
    }

    public FileCache(Context context) {
        this.context = context;

    }
}
