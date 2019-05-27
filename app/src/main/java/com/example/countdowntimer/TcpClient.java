package com.example.countdowntimer;

import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TcpClient {

    public static final String TAG = TcpClient.class.getSimpleName();
    public String SERVER_IP; //server IP address
    public int SERVER_PORT;

    public Boolean connected = false;
    private String mServerMessage;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private PrintWriter mBufferOut;
    private BufferedReader mBufferIn;

    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    public void sendMessage(final String message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mBufferOut != null) {
                    try {
                        Log.d(TAG, "Sending: " + message);
                        mBufferOut.print(message);
                        mBufferOut.flush();
                    } catch (Exception e) {
                        Log.d(TAG, "Exception Sending: " + e.getMessage());

                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void stopClient() {

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run1() {
        try {
            mRun = true;

            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            Log.d("TCP Client", "C: Connecting... to " + serverAddr + " " + SERVER_PORT);

            Socket socket = new Socket(serverAddr, SERVER_PORT);

            try {

                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                sendMessage("Connected to my Phone");

                while (mRun) {
                    mServerMessage = mBufferIn.readLine();
                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }
                }
                Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");
                //socket.close();

            } catch (Exception e) {
                Log.d("TCP", "S: Error" + e.getMessage());
            } finally {
                socket.close();
            }

        } catch (Exception e) {
            Log.d("TCP", "Connectionss: Error" + e.getMessage());
        }

    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d("TCP Client", "C: Connecting... to " + serverAddr + " " + SERVER_PORT);

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);

            try {

                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                sendMessage("coolllesss");
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    mServerMessage = mBufferIn.readLine();
                    sendMessage("codsdsdsdlesss");
                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

                Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");


            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {
            Log.e("TCP", "Connectionns: Error", e);
        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}
