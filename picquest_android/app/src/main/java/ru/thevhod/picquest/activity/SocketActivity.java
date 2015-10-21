package ru.thevhod.picquest.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import ru.thevhod.picquest.socket.client.TCPClient;

public abstract class SocketActivity extends Activity {

    private static final String EXTRA_IP = "EXTRA_IP";

    private TCPClient tcpClient = null;
    private connectTask conctTask = null;

    private String ipAddress;


    protected static void addIpExtra(Intent intent, String ipAddress) {
        intent.putExtra(EXTRA_IP, ipAddress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ipAddress = getIntent().getStringExtra(EXTRA_IP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initConnection();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeConnection();
    }

    protected void sendCommand(String command, String value) {
        tcpClient.sendMessage(command + ":" + value);
    }

    @SuppressLint("NewApi")
    private void initConnection() {
        conctTask = new connectTask();
        conctTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void closeConnection() {
        try {
            tcpClient.stopClient();
            conctTask.cancel(true);
            conctTask = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class connectTask extends AsyncTask<String, String, TCPClient> {
        @Override
        protected TCPClient doInBackground(String... message) {
            tcpClient = new TCPClient(new TCPClient.OnMessageReceived() {

                @Override
                public void messageReceived(String message) {
                    try {
                        publishProgress(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, ipAddress);
            tcpClient.run();
            if (tcpClient != null) {
                publishProgress("connected");
                tcpClient.sendMessage("Hello!");
            } else {
                publishProgress("disconnected");

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("Connection", values[0]);
        }
    }
}