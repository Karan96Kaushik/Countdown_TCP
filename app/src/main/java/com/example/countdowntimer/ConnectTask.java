package com.example.countdowntimer;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectTask extends AsyncTask<String, String, TcpClient> {

    TcpClient mTcpClient;
    public boolean started = false;

    @Override
    protected TcpClient doInBackground(String... message) {
        Log.d("test", "response " + message[0]);

        //we create a TCPClient object1
        mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
            @Override
            public void messageReceived(String message) {
                Log.d("ConnectTask", "response from server ----------->" + message);
                publishProgress(message);
            }
        });

        mTcpClient.SERVER_IP = message[1];
        mTcpClient.SERVER_PORT = Integer.parseInt(message[2]);

        mTcpClient.run1();
        started = true;

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("ConnectTask", "response in onProgressUpdate " + values[0]);
    }

    @Override
    protected void onCancelled() {
        Log.d("ConnectTask", "response in Cancel ");
    }
}