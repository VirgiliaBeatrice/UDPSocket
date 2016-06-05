package com.example.haoyanli.udpsocket;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
//import android.support.v7.util.SortedList;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.SortedSet;


public class UdpClientService extends IntentService {
    public final static String EXTRA_ADDRESS = "com.lapis.ble.EXTRA_ADDRESS";
    public final static String EXTRA_DATA = "com.lapis.ble.EXTRA_DATA";
    public final static String EXTRA_TS = "com.lapis.ble.EXTRA_TS";
    public final static String UDP_DEV_NO = "com.example.haoyanli.udpsocket.UDP_DEV_NO";

    public static volatile boolean loopFlag = false;
    public SortedSet<String> bleDevices = null;
    public String currDevNo = "";


    public UdpClientService() {
        super("UdpClientService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences preferences = getSharedPreferences(this.getPackageName() + "_preferences", MODE_PRIVATE);

        int remotePort = preferences.getInt("Remote Port", 0);
        String remoteIP = preferences.getString("Remote IP", "0.0.0.0");
        int sendingInterval = preferences.getInt("Sending Interval", 1000);

        System.out.println("Client: UDP Service has started.");
//        System.out.println(remoteIP);
//        System.out.println(Integer.toString(remotePort));
//        System.out.println(Integer.toString(sendingInterval));

        sendPacket(remoteIP, remotePort, sendingInterval, intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Log.d("UDPClient", "onDestory");
    }

    private byte[] dataProcess(String addr, float[] values, float timeStamp)
    {
//        if (bleDevices.contains(addr))
//            bleDevices.add(addr);
//
//        SortedList<String> devlist = null;
//        devlist.addAll(bleDevices);
//        currDevNo = "LAPIS_" + Integer.toString(devlist.indexOf(addr), 1);
        if (addr.contains("F5"))
            currDevNo = "LAPIS_RAW0";
        else if (addr.contains("E9"))
            currDevNo = "LAPIS_RAW1";
        else if (addr.contains("D9"))
            currDevNo = "LAPIS_RAW2";

//        Intent intent = new Intent(UDP_DEV_NO);
//        intent.putExtra("Device No.", currDevNo);
//        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
//        broadcaster.sendBroadcast(intent);

        String combinedString = currDevNo;
        combinedString += " " + Float.toString(timeStamp);
        if (values != null)
        {
            for (float value : values) {
                combinedString += " " + Float.toString(value);
            }
        }

        System.out.println(combinedString);
        return combinedString.getBytes();
    }

    private void sendPacket(String remoteIP, int remotePort, int sendingInterval, Intent intent)
    {
        DatagramSocket socket = null;
        DatagramPacket packet = null;
        byte[] buf = null;
        Bundle extras = intent.getExtras();
        String addr = extras.getString(EXTRA_ADDRESS);
        float[] values = extras.getFloatArray(EXTRA_DATA);
        float timeStamp = extras.getFloat(EXTRA_TS);

        if (extras.getBoolean("Debug Mode")){
            System.out.println("Client: Start to send debug data.");
            loopFlag = true;
            buf = "Hello world.".getBytes();
        }
        else {
            System.out.println("Client: Start to send data from Ble device.");
            sendingInterval = 0;
            buf = dataProcess(addr, values, timeStamp);
        }

//        System.out.println(Byte.toString(buf);
        System.out.println(Integer.toString(remotePort));
        System.out.println(remoteIP);

        try
        {
            packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(remoteIP), remotePort);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        try
        {
            socket = new DatagramSocket(9091);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        if (socket != null) {
            do {
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Client: Message sent.\n");
                System.out.println("Client: IP Address:Port - " + remoteIP + ":" + Integer.toString(remotePort) + "\n");

                if (sendingInterval != 0)
                    SystemClock.sleep(sendingInterval);
                System.out.println("Client: Sending Interval is " + Integer.toString(sendingInterval) + " ms.\n");
            }
            while (loopFlag);
            System.out.println("Client: Quit from sending.\n");
            socket.close();
        }
    }
}
