package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DeviceListFragment extends DialogFragment {

    private BluetoothAdapter mBTA;
    private List<BluetoothDevice> pairedDevices;
    private String[] devInfo;
    onDeviceSelectionListener comm;
    public DeviceListFragment(){
        this.mBTA =  BluetoothAdapter.getDefaultAdapter();

        if(this.mBTA == null){
            Toast.makeText(getActivity(),"Bluetooth Not Supported",Toast.LENGTH_LONG).show();
            dismiss();
        }

        if(!this.mBTA.isEnabled()){
            Toast.makeText(getActivity(),"Bluetooth is Not Enabled",Toast.LENGTH_LONG).show();
            Intent enableBT =  new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);
        }
        //Get list of paired devices
        pairedDevices = new ArrayList<BluetoothDevice>(this.mBTA.getBondedDevices());
        devInfo = new String[pairedDevices.size()];
        for( int i = 0; i < this.devInfo.length; i++){
            devInfo[i] = pairedDevices.get(i).getName() + "\n" + pairedDevices.get(i).getAddress();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select OBD Device").setItems(devInfo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                comm.onDeviceSelect(pairedDevices.get(i));
            }
        });

        return  builder.create();
    }

    @Override
    public void onAttach(Activity act){
        super.onAttach(act);
        try{
            comm = (onDeviceSelectionListener) act;
        }catch (ClassCastException e){
            throw new ClassCastException(act.toString() + " must implement onSomeEventListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        comm = null;
    }

    public interface onDeviceSelectionListener {
        public abstract void onDeviceSelect(BluetoothDevice dev);
    }

}
