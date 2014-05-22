package org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata;

import android.os.Handler;
import java.util.ArrayList;


public class DataControl {

    //Instance Vales

    private ArrayList<DataGen> gauges;
    private valueGenerator valGen;
    private final Handler gHanlder;

    public DataControl() {
        this.gHanlder = null;
        this.gauges   = null;
        this.valGen   = null;
    }

    public DataControl(ArrayList<DataGen> gauges, Handler gHanlder) {
        this.gauges   = gauges;
        this.gHanlder = gHanlder;
        this.valGen   = null;
    }

    public ArrayList<DataGen> getGauges() {
        return gauges;
    }

    public void setGauges(ArrayList<DataGen> gauges) {
        this.gauges = gauges;
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
        private Object mPauseLock;

        public valueGenerator(boolean state){
            this.isRunning = state;
            this.mPauseLock = new Object();
        }

        public boolean getIsRunning(){return this.isRunning;}

        @Override
        public void run(){
            boolean valInc = true;
            DataGen tmp;
            float val = 0.f;
            this.onResume();
            int x = 15;
            int size = gauges.size();
            try {
                while(this.isRunning){
                    val = (float) Math.cos(((float)x/10));
                    for(int i =0;i< size;i++) {
                        tmp = gauges.get(i);
                        int valInt = (int) (val*tmp.getMaxVal());
                        gHanlder.obtainMessage(0, tmp.getGaugeID(), valInt).sendToTarget();
                    }
                    if (valInc) {
                        x -= 1;
                    } else{
                        x += 1;
                    }
                    if(  x == 0 && valInc){
                        valInc = false;
                    }else if( x == 15 && !valInc){
                        valInc = true;
                    }
                    Thread.sleep(1500);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onPause() {
            synchronized (mPauseLock) {
                isRunning = false;
            }

        }

        public void onResume() {
            synchronized (mPauseLock) {
                isRunning = true;
                mPauseLock.notifyAll();
            }
        }


    }


}

