package com.example.adtjc.arduinocontroller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * Created by adtjc on 7/15/16.
 */
public class staHandler extends Handler{
    Context context;
    public staHandler(Context context){
        this.context = context;
    }
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 99:
                Toast.makeText(context, " unable to send message", Toast.LENGTH_LONG).show();
                break;
            case 95:
                //ConnectedThread coneccted = new ConnectedThread((BluetoothSocket)msg.obj,this);
                byte [] kuduro = (byte[])msg.obj;
                //String mensage = kuduro.toString();
                Toast.makeText(context,"message from device: "+ kuduro[0],Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
