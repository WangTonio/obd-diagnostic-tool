package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


public class SetPrefActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new PrefFragment())
                .commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.unregisterOnSharedPreferenceChangeListener(this);
   }

    /* Causes Null Pointer Exception At the moment */
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ScrollView tempp = (ScrollView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_live_data,null);
        if (key.equals("showtemp")) {
            RelativeLayout temp = (RelativeLayout) tempp.findViewById(R.id.tempLayout);
            boolean checked = sharedPreferences.getBoolean(key, true);
            if (checked) {
                temp.setVisibility(View.VISIBLE);
            } else {
                temp.setVisibility(View.GONE);
            }
        } else if (key.equals("showspeed")) {
            RelativeLayout speed = (RelativeLayout) findViewById(R.id.tempLayout);
        } else if (key.equals("showrpm")) {
            RelativeLayout rpm = (RelativeLayout) findViewById(R.id.tempLayout);
        } else if (key.equals("showengineload")) {
            RelativeLayout egnl = (RelativeLayout) findViewById(R.id.tempLayout);
        } else if (key.equals("showintaketemp")) {
            RelativeLayout intemp = (RelativeLayout) findViewById(R.id.tempLayout);
        } else if (key.equals("showvolt")) {
            RelativeLayout colt = (RelativeLayout) findViewById(R.id.tempLayout);
        }

    }
}
