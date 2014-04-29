package org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata;

import android.os.Message;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;


public class DataControl {

    //Instance Vales
    private ArrayList<DataGen> gauges;
    private final Handler gHandler;
    private valueGenerator valGen;

    public DataControl() {
        gauges = null;
        gHandler = null;
        valGen = null;
    }

    public DataControl(ArrayList<DataGen> gauges, Handler mHandler) {
        this.gauges = gauges;
        this.gHandler = mHandler;
        this.valGen = null;
    }

    public ArrayList<DataGen> getGauges() {
        return gauges;
    }

    public void setGauges(ArrayList<DataGen> gauges) {
        this.gauges = gauges;
    }

    public Handler getgHandler() {
        return gHandler;
    }


    public DataGen getSpecificGauge(int id) {

        return gauges.get(id);
    }

    public void setGaugeState(int id, boolean state) {
        gauges.get(id).setState(state);
    }

    public void startGenerator(){
        this.valGen =  new valueGenerator(false);
        this.valGen.start();
    }

    private class valueGenerator extends Thread {
        private boolean isRunning;

        public valueGenerator(boolean state){
            this.isRunning = state;
        }

        public boolean getIsRunning(){return this.isRunning;}
        @Override
        public void run(){
            this.isRunning = true;
            double val;
            Message msg;
            try {
                for (double x = 0.1; ; x += .1) {
                   for (int i = 0; i < gauges.size(); i++) {
                        if (gauges.get(i).isActive()) {
                            val = gauges.get(i).getValue(x);
                            gHandler.obtainMessage(0,i,((int)val)).sendToTarget();
                        }
                   }
                    Thread.sleep(1100);
                   if (x == 31.4) x = .1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

/*
            for (int i = 0; i < gauges.size(); i++) {
                if (gauges.get(i).isActive()) {
                    val = gauges.get(i).getValue((new Random()).nextDouble()*3.14);
                    //gHandler.obtainMessage(0,i,((int)val)).sendToTarget();
                    msg = gHandler.obtainMessage();
                    msg.arg1 = i;
                    msg.arg2 = (int) val;
                    Log.d("Sending Message -- >", "" + i + "  " + (int) val);
                    //msg.sendToTarget();

                    gHandler.dispatchMessage(msg);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
        }
/*
        public void onPause() {
            synchronized (mPauseLock) {
                mPaused = true;
            }

        }

        public void onResume() {
            synchronized (mPauseLock) {
                mPaused = false;
                mPauseLock.notifyAll();
            }
        }
*/
    }
}

