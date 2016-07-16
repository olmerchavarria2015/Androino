package com.example.adtjc.arduinocontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    final int ENABLED_BLUETOOTH = 1;
    ListView listView;
    ConnectThread connectThread;
    Object[] btDevices;
    BluetoothDevice btdevice ;
    Context context = this;
    TextView textView1;
    TextView textView2;
    Button button1;
    EditText editText1;
    ConnectedThread connectedThread;
    int superior;
    int pollo;
    staHandler mhandler;
    Handler recHandler;


    //BroadcastReceiver broadRec;
    //PreferenceManager.OnActivityResultListener OARL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView = (ListView)findViewById(R.id.listView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        button1 = (Button)findViewById(R.id.button1);
        editText1 =(EditText)findViewById(R.id.editText1);
        textView1 = (TextView)findViewById(R.id.textView);


        mhandler = new staHandler(context);


        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "no bluetooth capable",Toast.LENGTH_LONG).show();
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,ENABLED_BLUETOOTH);

        }
        populateList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int position;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position;

                try {
                    connectThread = new ConnectThread(getDevice(position), refinedUUID(getDevice(position)));
                    //Toast.makeText(context, "CONNECTED", Toast.LENGTH_LONG).show();

                    if (!connectThread.mmSocket.isConnected()) {
                        connectThread.mmSocket.connect();
                        //Toast.makeText(context, "CONNECTED", Toast.LENGTH_LONG).show();
                        connectedThread = new ConnectedThread(connectThread.mmSocket, mhandler);
                        connectedThread.start();
                        textView2.setText("CONECTED TO : " + getDevice(position).getName());
                    } else {
                        Toast.makeText(context, "CONNECTION FAILED", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, " DID NOT WORK ", Toast.LENGTH_LONG).show();

                }

            }
        });




    }





    public void sendToDevice(View v){
        try {
            byte[] bytes = editText1.getText().toString().getBytes();
            connectedThread.write(bytes);
        }catch (Exception e){
            Toast.makeText(context,"Unable to send : no devices connected",Toast.LENGTH_LONG).show();
        }

    }





    public void disconnect(View v){
        try {
            connectThread.mmSocket.close();
            textView2.setText("DEVICE");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"Unable to Disconnect",Toast.LENGTH_SHORT).show();
        }

    }





    public void populateList() {
        if (mBluetoothAdapter.isEnabled()) {

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            String[] strings = new String[pairedDevices.size()];
            ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,
                    R.layout.mytextview, android.R.id.text1);
// If there are paired devices
            if (pairedDevices.size() > 0) {
                int count = 1;
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {

                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(" DEVICE # " + count + " NAME :  " + device.getName() + "\n ADDRESS : " + device.getAddress());
                    count = count + 1;
                }
                listView.setAdapter(mArrayAdapter);
                //listView.setOnItemClickListener();


            }
            mBluetoothAdapter.cancelDiscovery();
            btDevices = pairedDevices.toArray();
            BluetoothDevice bTDEVIZ = (BluetoothDevice) btDevices[2];
            ParcelUuid [] parUUS = bTDEVIZ.getUuids();
            //superior = parUUS.length;
            //pollo = 11;
            //textView2.setText(parUUS[0].toString());
           // Toast.makeText(this, parUUS[0].toString(),Toast.LENGTH_LONG).show();

            //String UUID = btdevice.getUuid().toString();


            //Toast.makeText(this, UUID + "  "+ UUidMero[0].getUuid(), Toast.LENGTH_LONG).show();
        }

    }







    public BluetoothDevice getDevice(int position){
        try{
            btdevice = (BluetoothDevice) btDevices[position];
            return btdevice;

        }catch(Exception e){
            Toast.makeText(this,"something went worng",Toast.LENGTH_LONG).show();
        }
        return null;
    }







    public UUID refinedUUID(BluetoothDevice bTD){
        ParcelUuid[] UUidMero = bTD.getUuids();
        //textView1.setText(UUidMero.length);
        UUID realUUid = UUidMero[0].getUuid();
        return realUUid;
    }




}
