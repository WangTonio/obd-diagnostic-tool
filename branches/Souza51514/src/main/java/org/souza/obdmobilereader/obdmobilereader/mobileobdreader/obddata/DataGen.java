package org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata;

public class DataGen  {
    private int     gaugeID;
    private int     maxVal;
    private int     minVal;
    private boolean state;


    public DataGen(int gaugeId,int maxVal,int minVal){
        this.gaugeID  = gaugeId;
        this.maxVal   = maxVal;
        this.minVal   = minVal;
        this.state    = true;

    }


    public int getGaugeID() {
        return gaugeID;
    }

    public void setGaugeID(int gaugeID) {
        this.gaugeID = gaugeID;
    }

    public boolean isActive() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getMinVal() {
        return minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

}
