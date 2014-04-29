package org.souza.obdmobilereader.obdmobilereader.mobileobdreader.BTSession;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata.DataGen;

public class BluetoothSession {
    public final int    CONNECTED    = 1;
    public final int    DISCONNECTED = -1;
    public final int    BUSY         = 2;
    public final UUID   myUUID       = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ArrayList<DataGen> gauges;
    private final BluetoothAdapter localBT;
    private final Handler gHandler;
    private BluetoothTransmission transmissionThread;
    private BluetoothConnect connectThread;
    private int bluetoothState;


    public BluetoothSession(ArrayList<DataGen> gauges, Handler gHanlder, BluetoothAdapter localBT) {
        this.gauges         = gauges;
        this.gHandler       = gHanlder;
        this.localBT        = localBT;
        this.bluetoothState = -1;
    }


    public synchronized void setState(int state){
        this.bluetoothState = state;
    }

    private class BluetoothConnect extends Thread {
        private final BluetoothDevice mmDevice;
        private final BluetoothSocket  mmSocket;

        @SuppressLint("NewApi")
        public BluetoothConnect(BluetoothDevice device) {
           this.mmDevice = device;

            BluetoothSocket tmp = null;

            try{
                tmp = device.createRfcommSocketToServiceRecord(myUUID);
            }catch ( IOException ex){
                this.mmSocket = null;
                setState(DISCONNECTED);
                return;
            }
            this.mmSocket = tmp;
            if (!this.mmSocket.isConnected()) {
                return;
            }
            setState(CONNECTED);
        }

        @Override
        public void run(){
            //Cancel Discovery because it slows down the connection
            localBT.cancelDiscovery();

            try{
                setState(BUSY);
                this.mmSocket.connect();
            }catch ( IOException ex){
                setState(DISCONNECTED);
                return;
            }
            setState(CONNECTED);
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) { }
        }

    }

    private class BluetoothTransmission extends Thread {
        private BluetoothSocket mSocket;
        private InputStream     readStream;
        private OutputStream    writeStream;

        /**
         * Set Connected socket. Attempt to retrieve
         * input output streams from socket.
         * @param socket
         */
        public BluetoothTransmission(BluetoothSocket socket){
            this.mSocket         = socket;
            InputStream tempIn   = null;
            OutputStream tempOut = null;
            try{
                tempIn  = mSocket.getInputStream();
                tempOut = mSocket.getOutputStream();
            }catch(IOException ex){
                Log.e("BluetoothTransmission","Error retrieving input/output streams");
            }

            this.readStream  = tempIn;
            this.writeStream = tempOut;
        }
        @Override
        public void run(){
            int readCnt;
            byte[] readBuffer = new byte[512];
            try {
                while(true) {
                    readCnt = this.readStream.read(readBuffer);

                    //gHandler.obtainMessage()
                }
            }catch (IOException ex){

            }
        }

        public void writeTo(byte[] buffer){
            try{
                this.writeStream.write(buffer);
                Log.d("BTXmission:writeTo","messagae written to device");
            }catch (IOException ex){

            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                this.mSocket.close();
            } catch (IOException e) { }
        }
    }
}
