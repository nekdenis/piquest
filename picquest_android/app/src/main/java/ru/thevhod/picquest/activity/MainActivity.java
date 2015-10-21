package ru.thevhod.picquest.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.thevhod.picquest.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverList = new ArrayList<ServerObj>();
        initView();
        initListeners();
    }

    private void initView(){
        searchButton = (Button) findViewById(R.id.main_scan_button);
        listView = (ListView) findViewById(R.id.main_list);
        serverAdapter = new ServerAdapter(this, serverList);
        listView.setAdapter(serverAdapter);
    }

    private void initListeners() {
        searchButton.setOnClickListener(new SearchButtonListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ServerObj server = serverList.get(position);
                GridActivity.startActivity(MainActivity.this, server.getIpAddress());
            }
        });
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

    public class SearchTask extends AsyncTask<String,String, ServerExplorer> {

        long endTime;

        public SearchTask() {
            this.endTime = System.currentTimeMillis()+ SEARCH_DURATION;
        }

        @Override
        protected ServerExplorer doInBackground(String... message)
        {
            WifiManager wifi = (WifiManager) getSystemService(getApplicationContext().WIFI_SERVICE);
            WifiManager.MulticastLock mLock = wifi.createMulticastLock("mylock");
            mLock.setReferenceCounted(true);
            mLock.acquire();
            serverExplorer = new ServerExplorer(new ServerExplorer.OnMessageReceived() {

                @Override
                public void messageReceived(String message)
                {
                	try
					{
                		publishProgress(message);
					}
					catch (Exception e)
					{
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