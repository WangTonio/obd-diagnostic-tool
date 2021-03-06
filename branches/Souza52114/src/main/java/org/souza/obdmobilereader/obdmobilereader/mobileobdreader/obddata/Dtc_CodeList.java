package org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata;

import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.dtclookup.DtcListItem;

import java.util.ArrayList;
import java.util.Random;

public class Dtc_CodeList {

    private static final ArrayList<DtcListItem> trblCodeList;
    static{
        trblCodeList = new ArrayList<DtcListItem>();
        trblCodeList.add(new DtcListItem("P1103","Turbocharger Wastegate Actuator"));
        trblCodeList.add(new DtcListItem("P1104","Turbocharger Westgate Solenoid"));
        trblCodeList.add(new DtcListItem("P1105","Fuel Pressure Solenoid"));
        trblCodeList.add(new DtcListItem("P1300","Ignition Timing Adjustment circuit"));
        trblCodeList.add(new DtcListItem("P1400","Manifold Differential Pressure Sensor Circuit"));
        trblCodeList.add(new DtcListItem("P1500","Alternator FR Terminal circuit"));
        trblCodeList.add(new DtcListItem("P1600","Serial Communication"));
        trblCodeList.add(new DtcListItem("P1715","Pulse Generator Assembly"));
        trblCodeList.add(new DtcListItem("P1750","Solenoid Assembly"));
        trblCodeList.add(new DtcListItem("P1751","A/T Control Relay"));
        trblCodeList.add(new DtcListItem("P1791","Engine Coolant Temperature Level Input circuit"));
        trblCodeList.add(new DtcListItem("P1795","Throttle Position Input circuit to DCM"));
    }

    public static DtcListItem getRandomCode(){
        return trblCodeList.get(new Random().nextInt(trblCodeList.size()-1));
    }

    /**
     * Code will be sent in and the corresponding summary
     * will be fetched from the static Hash Map
     * @param code Trouble Code passed in from Bluetooth device
     * @return short summary of given trouble code
     */
    public static String getSummary(String code){
        return null;
    }
}
