package com.example.countdowntimer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView displaytext = findViewById(R.id.displaytext);
        final EditText messagetext = findViewById(R.id.messagetext);
        final EditText iptext = findViewById(R.id.iptext);
        final EditText porttext = findViewById(R.id.porttext);

        final ConnectTask ssocket = new ConnectTask();

        final Button sendbutton = findViewById(R.id.sendbutton);
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssocket.mTcpClient.sendMessage(messagetext.getText().toString());
                Toast.makeText(getApplicationContext(),messagetext.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

        final Button connectbutton = findViewById(R.id.connectbutton);
        connectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Notify","trying connect TCP Client");
                Log.d("Notify",ssocket.started + "");

                try {
                    ssocket.execute("", iptext.getText().toString(),porttext.getText().toString());
                    displaytext.setText("Connecting");
                } catch (Exception e) {
                    Log.d("Exception kk ConnectTask",e.getMessage() + "");
                    ssocket.cancel(true);
                    displaytext.setText("Connection Destroyed");
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending : " + messagetext.getText().toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

            started = true;
            Log.d("Checking cancel", this.isCancelled() + "");

            mTcpClient.run1();

            Log.d("ConnectTask returns", "Here");

            return null;
        }

        @Override
        protected void onCancelled() {
            Log.d("onCancelled reached", "response ");


        }

        @Override


        protected void onProgressUpdate(String... values) {
            final TextView displaytext = findViewById(R.id.displaytext);

            displaytext.setText("-> " + values[0]);

            super.onProgressUpdate(values);
            Log.d("ConnectTask", "response in onProgressUpdate " + values[0]);
        }
    }
}


