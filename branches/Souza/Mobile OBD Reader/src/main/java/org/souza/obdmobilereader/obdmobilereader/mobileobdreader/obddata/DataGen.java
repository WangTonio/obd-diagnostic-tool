package org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata;

import android.util.Log;

import java.util.ArrayList;

public class DataGen  {
    private int gaugeID;
    private int maxVal;
    private boolean state;

    public  final static ArrayList<Integer> gaugesMaxValues = new ArrayList<Integer>();
    static{
        gaugesMaxValues.add(250);
        gaugesMaxValues.add(160);
        gaugesMaxValues.add(8);
        gaugesMaxValues.add(100);
        gaugesMaxValues.add(130);
        gaugesMaxValues.add(16);
    }

    public  final static ArrayList<Integer> gaugesMaxDegrees = new ArrayList<Integer>();
    static{
        gaugesMaxDegrees.add(110);
        gaugesMaxDegrees.add(90);
        gaugesMaxDegrees.add(88);
        gaugesMaxDegrees.add(110);
        gaugesMaxDegrees.add(110);
        gaugesMaxDegrees.add(110);
    }




    public int getGaugeID() {
        return gaugeID;
    }

    public void setGaugeID(int gaugeID) {
        this.gaugeID = gaugeID;
    }

    public float getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public boolean isActive() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public DataGen(int gaugeId){
        this.gaugeID = gaugeId;
        this.maxVal = gaugesMaxValues.get(gaugeId);
        this.state = true;
    }

    public double getValue(double x){
        return normalizedValue(this.maxVal * Math.sin(x));
        //return this.maxVal * Math.sin(x);
    }

    public double normalizedValue(double x){
        int id = this.gaugeID;
        double maxVal = gaugesMaxValues.get(id);
        double minD = -(gaugesMaxDegrees.get(id));
        double maxD = gaugesMaxDegrees.get(id);
        double val = x/(maxVal);
        val *= (maxD-minD)+minD;
        //Log.d("normalized -->", "Value: "+val);
        return val;
    }



}
