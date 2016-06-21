package com.example.haoyanli.udpsocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    public InetAddress remoteIP = null;
    public int remotePort = 0;
    public int sendingInterval = 0;
//    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Message comfirmMsg = Message.obtain();


//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String currDevNo = intent.getStringExtra("Device No.");
//                TextView tvDeviceNo = (TextView) findViewById(R.id.tvDeviceNo);
//                tvDeviceNo.setText(currDevNo);
//            }
//        };
//        EditText etRemoteIP = (EditText) findViewById(R.id.etRemoteIP);
//        EditText etRemotePort = (EditText) findViewById(R.id.etRemotePort);
//        EditText etSendingInterval = (EditText) findViewById(R.id.etSendingInterval);
//
//        try {
//            SharedPreferences preferences = getSharedPreferences(this.getPackageName() + "_preferences", MODE_PRIVATE);
//        } catch ()
//
//        int savedRemotePort = preferences.getInt("Remote Port", 0);
//        etRemotePort.setText(savedRemotePort);
//        String savedRemoteIP = preferences.getString("Remote IP", "0.0.0.0");
//        etRemoteIP.setText(savedRemoteIP);
//        int savedSendingInterval = preferences.getInt("Sending Interval", 1000);
//        etSendingInterval.setText(savedSendingInterval);

//        Button btRemoteIP = (Button) findViewById(R.id.btRemoteIP);
//        Button btRemotePort = (Button) findViewById(R.id.btRemotePort);
        Button btRemoteIPandPort = (Button) findViewById(R.id.btRemoteIPandPort);
        Button btSendingInterval = (Button) findViewById(R.id.btSendingInterval);

/*        btRemoteIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InetAddress tempRemoteIP;
                EditText etRemoteIP = (EditText) findViewById(R.id.etRemoteIP);

                try
                {
                    tempRemoteIP = InetAddress.getByName(etRemoteIP.getText().toString());
                } catch (UnknownHostException e)
                {
                    e.printStackTrace();
                    etRemoteIP.setText("0.0.0.0");
                    System.out.println("Illegal input.");
                    return;
                }

                remoteIP = tempRemoteIP;
                SharedPreferences preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Remote IP", remoteIP.toString().substring(1));
//                editor.commit();
                editor.apply();
                System.out.println(remoteIP.toString());
            }
        });*/

/*        btRemotePort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etRemotePort = (EditText) findViewById(R.id.etRemotePort);

                if (Integer.parseInt(etRemotePort.getText().toString()) > 65535)
                {
                    etRemotePort.setText("1000");
                    System.out.println("Illegal input.");
                    return;
                }

                remotePort = Integer.parseInt(etRemotePort.getText().toString());
                SharedPreferences preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Remote Port", remotePort);
//                editor.
                editor.apply();
                System.out.println(String.valueOf(remotePort));
            }
        });*/

        btRemoteIPandPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InetAddress tempRemoteIP;
                EditText etRemoteIP = (EditText) findViewById(R.id.etRemoteIP);
                EditText etRemotePort = (EditText) findViewById(R.id.etRemotePort);

                try
                {
                    tempRemoteIP = InetAddress.getByName(etRemoteIP.getText().toString());
                } catch (UnknownHostException e)
                {
                    e.printStackTrace();
                    etRemoteIP.setText("0.0.0.0");
                    System.out.println("Illegal input.");
                    return;
                }

                remoteIP = tempRemoteIP;
                SharedPreferences preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Remote IP", remoteIP.toString().substring(1));
                editor.apply();
                System.out.println(remoteIP.toString());

                if (Integer.parseInt(etRemotePort.getText().toString()) > 65535)
                {
                    etRemotePort.setText("1000");
                    System.out.println("Illegal input.");
                    return;
                }

                remotePort = Integer.parseInt(etRemotePort.getText().toString());
                editor.putInt("Remote Port", remotePort);
                editor.apply();
                System.out.println(String.valueOf(remotePort));
            }
        });

        btSendingInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etSendingInterval = (EditText) findViewById(R.id.etSendingInterval);

                sendingInterval = Integer.parseInt(etSendingInterval.getText().toString());
                SharedPreferences preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Sending Interval", sendingInterval);
                editor.apply();
                System.out.println(String.valueOf(sendingInterval));
            }
        });

//        if (remoteIP != null) {
//            try
//            {
//                remoteIP = InetAddress.getByName(etRemoteIP.getText().toString());
//            } catch (UnknownHostException e)
//            {
//                e.printStackTrace();
//            }
//        }

//        remotePort = Integer.parseInt(etRemotePort.getText().toString());
//        sendingInterval = Integer.parseInt(etSendingInterval.getText().toString());

        Switch swSendingStatus = (Switch) findViewById(R.id.swSendingStatus);
        swSendingStatus.setChecked(false);
        swSendingStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intentUdpService = new Intent(getApplicationContext(), UdpClientService.class);
                intentUdpService.putExtra("Debug Mode", true);
                if (buttonView.isChecked()) {
                    System.out.println("Button Status is checked.");
                    startService(intentUdpService);
                }
                else {
                    System.out.println("Button Status is not checked.");
                    UdpClientService.loopFlag = false;
                    stopService(intentUdpService);
                }
            }
        });

//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt("Remote Port", remotePort);
//        editor.putString("Remote IP", remoteIP.toString());
//        editor.putInt("Sending Interval", sendingInterval);
//        editor.apply();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
//                new IntentFilter(UdpClientService.UDP_DEV_NO)
//        );
//    }
//
//    @Override
//    protected void onStop() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
//        super.onStop();
//    }
}



