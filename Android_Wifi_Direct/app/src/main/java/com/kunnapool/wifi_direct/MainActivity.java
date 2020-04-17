package com.kunnapool.wifi_direct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button wifiToggleBttn, discoverBttn, sendBttn;
    ListView listView;
    TextView readMsgBox, connStatus;
    EditText writeMsg;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWork();
        exqListner();
    }

    private void exqListner()
    {
        wifiToggleBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiManager.isWifiEnabled())
                {
                    wifiManager.setWifiEnabled(false);
                    wifiToggleBttn.setText("Turn on wifi");
                }else
                {
                    wifiManager.setWifiEnabled(true);
                    wifiToggleBttn.setText("Turn off wifi");
                }
            }
        });
    }

    private void initWork()
    {
        wifiToggleBttn = (Button) findViewById(R.id.wifiButton);
        discoverBttn = (Button) findViewById(R.id.discoverButton);
        sendBttn = (Button) findViewById(R.id.sendButton);

        listView = (ListView) findViewById(R.id.peersListV);

        readMsgBox = (TextView) findViewById(R.id.sendMessgeBox);
        connStatus = (TextView) findViewById(R.id.connStatus);

        writeMsg = (EditText) findViewById(R.id.sendMessgeBox);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
