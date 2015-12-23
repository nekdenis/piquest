package ru.thevhod.picquest.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.thevhod.picquest.R;
import ru.thevhod.picquest.SPHelper;
import ru.thevhod.picquest.adapter.ServerAdapter;
import ru.thevhod.picquest.data.ServerObj;
import ru.thevhod.picquest.socket.client.ServerExplorer;

public class MainActivity extends Activity {
    public static final int SEARCH_DURATION = 1000 * 5;
    private ServerExplorer serverExplorer = null;

    private ListView listView;
    private ServerAdapter serverAdapter;
    private Button searchButton;
    private List<ServerObj> serverList;
    private Button manualConnectButton;
    private EditText manualConnectIp;
    private SPHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverList = new ArrayList<ServerObj>();
        spHelper = new SPHelper(this);
        initView();
        initListeners();
    }

    private void initView() {
        searchButton = (Button) findViewById(R.id.main_scan_button);
        listView = (ListView) findViewById(R.id.main_list);
        manualConnectButton = (Button) findViewById(R.id.main_manual_connect);
        manualConnectIp = (EditText) findViewById(R.id.main_manual_ip);
        manualConnectIp.setText(spHelper.getIp());
        serverAdapter = new ServerAdapter(this, serverList);
        listView.setAdapter(serverAdapter);
    }

    private void initListeners() {
        searchButton.setOnClickListener(new SearchButtonListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServerObj server = serverList.get(position);
                String ipAddress = server.getIpAddress();
                startGridActivity(ipAddress);
            }
        });
        manualConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToConnect();
            }
        });
    }

    private void startGridActivity(String ipAddress) {
        GridActivity.startActivity(MainActivity.this, ipAddress);
        finish();
    }

    private void tryToConnect() {
        String ipAddress = manualConnectIp.getText().toString();
        if (Patterns.IP_ADDRESS.matcher(ipAddress).matches()) {
            spHelper.putIp(ipAddress);
            startGridActivity(ipAddress);
        } else {
            Toast.makeText(this, "IP_MALFORMED", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    private void startSearch() {
        SearchTask searchTask = new SearchTask();
        searchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        setSearching();
    }

    private void setSearchFinished() {
        searchButton.setVisibility(View.VISIBLE);
    }

    private void setSearching() {
        searchButton.setVisibility(View.GONE);
    }

    public class SearchTask extends AsyncTask<String, String, ServerExplorer> {

        long endTime;

        public SearchTask() {
            this.endTime = System.currentTimeMillis() + SEARCH_DURATION;
        }

        @Override
        protected ServerExplorer doInBackground(String... message) {
            WifiManager wifi = (WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE);
            WifiManager.MulticastLock mLock = wifi.createMulticastLock("mylock");
            mLock.setReferenceCounted(true);
            mLock.acquire();
            serverExplorer = new ServerExplorer(new ServerExplorer.OnMessageReceived() {

                @Override
                public void messageReceived(String message) {
                    try {
                        publishProgress(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            serverExplorer.run(endTime);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            processResponse(values[0]);
            serverExplorer.stopClient();
            this.cancel(true);
            setSearchFinished();
        }

        @Override
        protected void onPostExecute(ServerExplorer serverExplorer) {
            super.onPostExecute(serverExplorer);
            setSearchFinished();
        }
    }

    private void processResponse(String value) {
        serverList.add(new ServerObj(value));
        serverAdapter.notifyDataSetChanged();
    }

    private class SearchButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startSearch();
        }
    }
}