package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.BTSession.BluetoothSession;
import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.Gauge.Gauge;
import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata.DataControl;
import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata.DataGen;

import java.util.ArrayList;
import java.util.Map;


public class OBD_Main extends FragmentActivity implements DtcLookUpFragment.OnFragmentInteractionListener,
                                                          DeviceListFragment.onDeviceSelectionListener{

    public DtcLookUpFragment.OnFragmentInteractionListener dtcLookUpCom;
    public static final int TAB_CNT  = 3;
    public static final int MAIN_TAB = 0;
    public static final int LD_TAB   = 1;
    public static final int DTC_TAB  = 2;

    BluetoothAdapter mBTA;
    BluetoothDevice  mDev;
    BluetoothSession mBTS;
    public static boolean isConnected = false;
    private static boolean isLoaded = false;

    public MainFragment      mainF;
    public LiveDataFragment  liveDataF;
    public DtcLookUpFragment dtcLUF;
    Fragment[]               arrFragment;
    MyAdapter                myFragmentPageAdapter;
    ViewPager                myViewPager;

    public DataControl dc;
    private final Handler gHandler = new GaugeHandler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd__main);
        setupFragmentPager();
        checkBluetooth();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d("OBD_Main", "onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("OBD_Main","onResume");
    }

    public void onPause(){
        super.onPause();
        if(this.dc != null){
            this.dc.killGenerator();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public void onDestroy(){
        super.onDestroy();
    }

    /**
     * Set up Gauge Objects for Data Control Object
     * We Give each gauge an ID, and retrieves
     * its respective min and max value
     */
    public void setupDataGenerator(){
        ArrayList<DataGen> temp =  new ArrayList<DataGen>();
        Gauge tmp;
        tmp = (Gauge) findViewById(R.id.tempgauge);
        temp.add((new DataGen(0,tmp.getMaxVal(),tmp.getMinVal())));

        tmp = (Gauge) findViewById(R.id.speedgauge);
        temp.add((new DataGen(1,tmp.getMaxVal(),tmp.getMinVal())));


        tmp = (Gauge) findViewById(R.id.rpmgauge);
        temp.add((new DataGen(2,tmp.getMaxVal(),tmp.getMinVal())));


        tmp = (Gauge) findViewById(R.id.engloadgauge);
        temp.add((new DataGen(3,tmp.getMaxVal(),tmp.getMinVal())));


        tmp = (Gauge) findViewById(R.id.intaketemp);
        temp.add((new DataGen(4,tmp.getMaxVal(),tmp.getMinVal())));


        tmp = (Gauge) findViewById(R.id.voltage);
        temp.add((new DataGen(5,tmp.getMaxVal(),tmp.getMinVal())));


        dc = new DataControl(temp, gHandler);
    }

    /**
     * Checks to see if device has bluetooth, and if
     * it does, see if its enabled. If its not ask to enable it
     */
    private void checkBluetooth(){
        mBTA = BluetoothAdapter.getDefaultAdapter();
        Log.d("checkBluetooh -->"," REQUEST_ENABLE_BT: " + mBTA.getBondedDevices());
        if(mBTA == null){
            new AlertDialog.Builder(this)
                    .setMessage("Bluetooth is not supported")
                    .setCancelable(false)
                    .setPositiveButton("OK",null)
                    .show();
        }

        if(!mBTA.isEnabled()){
            Intent enableBT =  new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);
        }
    }

    /**
     * Determines if each Specific gauge layout should be hidden
     * or shown depending on the value set in preferences
     */
    private void showHideGauges(){
        SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(this);
        RelativeLayout tmp;
        Map<String,?> keys = pref.getAll();
        if(keys == null) {
            return;
        }
        for(Map.Entry<String,?> entry: keys.entrySet()){
            String s = entry.getKey();
            if (s.equals("showtemp")) {
                tmp = (RelativeLayout) findViewById(R.id.tempLayout);
                if ((Boolean)entry.getValue()) {
                    tmp.setVisibility(View.VISIBLE);
                }else{
                    tmp.setVisibility(View.GONE);
                }
                if(this.dc != null){
                    this.dc.setGaugeState(0, (Boolean) entry.getValue());
                }
            } else if (s.equals("showspeed")) {
                tmp = (RelativeLayout) findViewById(R.id.speedLayout);
                if ((Boolean)entry.getValue()) {
                    tmp.setVisibility(View.VISIBLE);
                }else{
                    tmp.setVisibility(View.GONE);
                }
                if(this.dc != null){
                    this.dc.setGaugeState(1, (Boolean) entry.getValue());
                }
            } else if (s.equals("showrpm")) {
                tmp = (RelativeLayout) findViewById(R.id.rpmLayout);
                if ((Boolean)entry.getValue()) {
                    tmp.setVisibility(View.VISIBLE);
                }else{
                    tmp.setVisibility(View.GONE);
                }
                if(this.dc != null){
                    this.dc.setGaugeState(2, (Boolean) entry.getValue());
                }
            } else if (s.equals("showengineload")) {
                tmp = (RelativeLayout) findViewById(R.id.egnLoadLayout);
                if ((Boolean)entry.getValue()) {
                    tmp.setVisibility(View.VISIBLE);
                }else{
                    tmp.setVisibility(View.GONE);
                }
                if(this.dc != null){
                    this.dc.setGaugeState(3, (Boolean) entry.getValue());
                }
            } else if (s.equals("showintaketemp")) {
                tmp = (RelativeLayout) findViewById(R.id.intakeTempLayout);
                if ((Boolean)entry.getValue()) {
                    tmp.setVisibility(View.VISIBLE);
                }else{
                    tmp.setVisibility(View.GONE);
                }
                if(this.dc != null){
                    this.dc.setGaugeState(4, (Boolean) entry.getValue());
                }
            } else if (s.equals("showvolt")) {
                tmp = (RelativeLayout) findViewById(R.id.voltLayout);
                if ((Boolean)entry.getValue()) {
                    tmp.setVisibility(View.VISIBLE);
                }else{
                    tmp.setVisibility(View.GONE);
                }
                if(this.dc != null){
                    this.dc.setGaugeState(5, (Boolean) entry.getValue());
                }
            }
        }
    }

    private void setupFragmentPager(){
        mainF =  new MainFragment();
        liveDataF = new LiveDataFragment();
        dtcLUF = new DtcLookUpFragment();

        arrFragment =  new Fragment[4];

        arrFragment[0] = mainF;
        arrFragment[1] = liveDataF;
        arrFragment[2] = dtcLUF;

        myFragmentPageAdapter = new MyAdapter(getSupportFragmentManager());

        myViewPager =  (ViewPager) findViewById(R.id.pager);

        myViewPager.setOffscreenPageLimit(TAB_CNT);

        myViewPager.setAdapter(myFragmentPageAdapter);

        actionBarSetup();

        myViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                getActionBar().setSelectedNavigationItem(position);
                if(position == 1){
                    Toast.makeText(getApplication(),"Live Data Tab",Toast.LENGTH_LONG).show();
                    showHideGauges();
                    if(!isLoaded && isConnected){
                        Log.d("DataControl Run", "Starting thread");
                        isLoaded = true;
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.obd__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            //Intent intent;
            //intent = new Intent(this,SetPrefActivity.class);
            //startActivity(intent);
            //Log.d("Options Menu Item Selected", "");
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content,new PrefFragment())
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void actionBarSetup(){
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
              myViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };
        //Main Tab
        actionBar.addTab(actionBar.newTab().setText(R.string.title_Main).setTabListener(tabListener));
        //Live Data Tab
        actionBar.addTab(actionBar.newTab().setText(R.string.title_LD).setTabListener(tabListener));
        //Trouble Code Look Up tab
        actionBar.addTab(actionBar.newTab().setText(R.string.title_DTCLU).setTabListener(tabListener));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onClickChC(View view){
        if( dtcLookUpCom != null) {
            dtcLookUpCom.btn_DtcLookUpCheckCodes();
        }
    }

    public void onClickClC(View view){
        if(dtcLookUpCom != null) {
            dtcLookUpCom.btn_DtcLookUpClearCodes();
        }
    }
    @Override
    public void btn_DtcLookUpCheckCodes() {
        if(dtcLUF !=null) {
            dtcLUF.addCode();
        }
    }

    @Override
    public void btn_DtcLookUpClearCodes() {
        if(dtcLUF != null) {
            //Send message to Device to clear codes
            dtcLUF.clearCodes();
        }
    }

    /**
     * Function for main screen button
     * State 0 is disconnected
     *  Asks to select obd Device
     *  then attempts to make connection with device
     * State 1 is connected
     *  Kills connection and Data control object
     * Could not get android selector to work correctly
     *
     */
    private static int pressed = 0;
    public void onClickCon(View view){
        Button btn =  (Button) view;
        TextView tv = (TextView) findViewById(R.id.conStatus);
        switch(pressed){
            case 0:
                setupDataGenerator();
                //Select Paired Device
                android.app.FragmentManager manager = getFragmentManager();
                DeviceListFragment dlf = new DeviceListFragment();
                dlf.show(manager,null);

                //this.mBTS = new BluetoothSession(this.dc.getGauges(),this.mBTA,this.gHandler);
                btn.setBackgroundResource(R.drawable.consucc);
                tv.setText("Connected");
                break;
            case 1:
                //Remove Data Control Object
                this.dc = null;
                //Close Bluetooth Session
                this.mBTS = null;
                btn.setBackgroundResource(R.drawable.confail);
                tv.setText("Disconnected");
                break;
        }
        Log.d("onClick -->"," "+pressed);
        pressed = 1 - pressed;
        Log.d("onClick -->"," "+isConnected);
        isConnected ^= true;
    }

    public void onClickGauge(View view){
        if(this.dc != null) {
            dc.startGenerator();
        }
    }

    @Override
    public void onDeviceSelect(BluetoothDevice dev) {
        this.mDev = dev;
        Toast.makeText(this,"Dev: " +  dev.getName() + " Addr: " + dev.getAddress(),Toast.LENGTH_LONG).show();
    }


    public class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return arrFragment[position];
        }

        @Override
        public int getCount() {
            return TAB_CNT;
        }

        public CharSequence getPageTitle(int position){
          switch(position) {
              case MAIN_TAB:
                  return getResources().getString(R.string.title_Main);
              case LD_TAB:
                  return getResources().getString(R.string.title_LD);
              case DTC_TAB:
                  return getResources().getString(R.string.title_DTCLU);
              default:
                  return null;
          }
        }
    }

    /**
     * Handler that receives messages to update Gauges and textview for
     * respective gauge.
     */
    private final class GaugeHandler extends Handler {

        public void handleMessage(Message msg){
            int gaugeID = msg.arg1;
            int val = msg.arg2;
            Gauge tmpG;
            final TextView tmpV;
            //Will need to switch to PID for bluetooth access
            switch(gaugeID){
                case 0:
                    tmpG = (Gauge) findViewById(R.id.tempgauge);
                    tmpG.setHandTarget(val);
                    tmpV = (TextView)findViewById(R.id.temptxt);
                    tmpV.setText(val+"");
                    break;
                case 1:
                    tmpG = (Gauge) findViewById(R.id.speedgauge);
                    tmpG.setHandTarget(val);
                    tmpV = (TextView) findViewById(R.id.speedtxt);
                    tmpV.setText(val+"");
                    break;
                case 2:
                    tmpG = (Gauge) findViewById(R.id.rpmgauge);
                    tmpG.setHandTarget(val);
                    tmpV = (TextView) findViewById(R.id.rpmtxt);
                    tmpV.setText(val+"");
                    break;
                case 3:
                    tmpG = (Gauge) findViewById(R.id.engloadgauge);
                    tmpG.setHandTarget(val);
                    tmpV = (TextView) findViewById(R.id.engltxt);
                    tmpV.setText(val+"");
                    break;
                case 4:
                    tmpG = (Gauge) findViewById(R.id.intaketemp);
                    tmpG.setHandTarget(val);
                    tmpV = (TextView) findViewById(R.id.inttemptxt);
                    tmpV.setText(val+"");
                    break;
                case 5:
                    tmpG = (Gauge) findViewById(R.id.voltage);
                    tmpG.setHandTarget(val);
                    tmpV = (TextView) findViewById(R.id.volttxt);
                    tmpV.setText(val+"");
                    break;
            }

        }
    };

}
