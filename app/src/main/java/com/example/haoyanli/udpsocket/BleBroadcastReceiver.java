package com.example.haoyanli.udpsocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class BleBroadcastReceiver extends BroadcastReceiver {
    public final static String  ACTION_SENSOR_VALUE_NOTIFY = "com.lapis.ble.ACTION_SENSOR_VALUE_NOTIFY";

    public final static String EXTRA_ADDRESS = "com.lapis.ble.EXTRA_ADDRESS";
    public final static String EXTRA_DATA = "com.lapis.ble.EXTRA_DATA";
    public final static String EXTRA_TS = "com.lapis.ble.EXTRA_TS";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SENSOR_VALUE_NOTIFY))
        {
            Bundle extras = intent.getExtras();
            String addr = extras.getString(EXTRA_ADDRESS);
            float[] values = extras.getFloatArray(EXTRA_DATA);
            float timeStamp = extras.getFloat(EXTRA_TS);

            Intent intentUdpService = new Intent(context, UdpClientService.class);
            intentUdpService.putExtra("Debug Mode", false);
            intentUdpService.putExtra(EXTRA_ADDRESS, addr);
            intentUdpService.putExtra(EXTRA_DATA, values);
            intentUdpService.putExtra(EXTRA_TS, timeStamp);

            context.startService(intentUdpService);
            System.out.println("Client: Receive a new data package from Ble.\n");
        }
    }

//    private byte[] dataProcess(Intent intent)
//    {
//        Bundle extras = intent.getExtras();
//        String addr = extras.getString(EXTRA_ADDRESS);
//        float[] values = extras.getFloatArray(EXTRA_DATA);
//        float timeStamp = extras.getFloat(EXTRA_TS);
//
//        String combinedString = "Data From " + addr + " :";
//        combinedString += " (" + Float.toString(timeStamp) + ")";
//        if (values != null)
//        {
//            for (float value : values)
//                combinedString += " " + Float.toString(value);
//        }
//
//        System.out.println(combinedString);
//        return combinedString.getBytes();
//    }
}
