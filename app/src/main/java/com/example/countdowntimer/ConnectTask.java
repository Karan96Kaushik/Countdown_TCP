package com.example.countdowntimer;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectTask extends AsyncTask<String, String, TcpClient> {

    TcpClient mTcpClient;


    @Override
    protected TcpClient doInBackground(String... message) {
        Log.d("test", "response " + message[0]);

        //we create a TCPClient object1
        mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {
                //this method calls the onProgressUpdate
                publishProgress(message);
            }
        });
        mTcpClient.run1();

        return null;
    }

    @Override


    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //response received from server
        Log.d("ConnectTask", "response " + values[0]);
        //process server response here....

    }
}