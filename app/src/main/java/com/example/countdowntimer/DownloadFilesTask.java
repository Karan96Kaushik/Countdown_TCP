package com.example.countdowntimer;

import android.os.AsyncTask;
import android.util.Log;


public class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
    protected Long doInBackground(String... urls) {
        Log.d("Notify","Downloading");

        long totalSize = 0;
        Log.d("Dow",urls[0]);
        return totalSize;
    }
}
