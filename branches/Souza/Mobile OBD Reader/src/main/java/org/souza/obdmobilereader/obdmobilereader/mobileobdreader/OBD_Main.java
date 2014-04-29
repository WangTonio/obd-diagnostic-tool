package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.BTSession.BluetoothSession;
import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata.DataControl;
import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata.DataGen;

import java.util.ArrayList;


public class OBD_Main extends FragmentActivity implements DtcLookUpFragment.OnFragmentInteractionListener {
    public static final int TAB_CNT  = 3;
    public static final int MAIN_TAB = 0;
    public static final int LD_TAB   = 1;
    public static final int DTC_TAB  = 2;

    BluetoothAdapter mBTA;
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
    final private Handler gHandler =  new GaugeHandler();
    /* Gauge values & utilities */
    public int animationSpeed = 150;
    public int tempOrgVa      = 0;
    public int spdOrgVal      = 0;
    public int rpmOrgVal      = 0;
    public int egnLoadOrgVal  = 0;
    public int intkTempOrgVal = 0;
    public int voltOrgVal     = 0;

    public int minIntTempDeg    = -110;
    public int minEnginLoadDeg  = -90;
    public int minEnginTempDeg  = -90;
    public int minRPMDeg        = -110;
    public int minSpeedDeg      = -110;
    public int minVoltDeg       = -110;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd__main);
        setupFragmentPager();
        checkBluetooth();
        setupDataGenerator();
    }
    public void setupDataGenerator(){
        ArrayList<DataGen> temp =  new ArrayList<DataGen>();
        for(int i = 0; i < 6;i++){
            temp.add(new DataGen(i));
        }

        dc = new DataControl(temp,gHandler);
    }
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

        actioBarSetup();

        myViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int position){
                getActionBar().setSelectedNavigationItem(position);
                if(position == 1){
                    Toast ts = Toast.makeText(getApplication(),"Live Data Tab",Toast.LENGTH_LONG);
                    ts.show();
                    if(!isLoaded && isConnected){
                        Log.d("DataControl Run", "Starting thread");
                       // dc.startGenerator();
                        isLoaded = true;
                    }else if(position == 2){

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent;
            intent = new Intent(this,SetPrefActivity.class);
            startActivity(intent);
            Log.d("Options Menu Item Selected", "");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actioBarSetup(){
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
    private static int pressed = 0;
    public void onClickCon(View view){
        Button btn =  (Button) view;
        TextView tv = (TextView) findViewById(R.id.conStatus);
        if(pressed == 0){
            btn.setBackgroundResource(R.drawable.consucc);
            tv.setText("Connected");
        }else if( pressed == 1){
            btn.setBackgroundResource(R.drawable.confail);
            tv.setText("Disconnected");
        }
        Log.d("onClick -->"," "+pressed);
        pressed = 1 - pressed;
        Log.d("onClick -->"," "+isConnected);
        isConnected ^= true;
    }
    public void onClickGauge(View view){
        dc.startGenerator();
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

    private final class GaugeHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            //Log.d("OBD_Main->handleMessage","Before Switch");
            /* Bluetooth messaging would go here */
            /*                                   */

            /* Rotate Animations would go here   */
            // Gauge Numbers
            //      0 --> Engine Temperature gauge
                    final ImageView tempNdle    = (ImageView) findViewById(R.id.tempNeedle);
            //      1 --> Speedometer Gauge
                    final ImageView spdNdle     = (ImageView) findViewById(R.id.spdNeedle);
            //      2 --> Revolutions per Minute Gauge
                    final ImageView rpmNdle     = (ImageView) findViewById(R.id.rpmNeedle);
            //      3 --> Engine Load Gauge
                    final ImageView egnLdNdle   = (ImageView) findViewById(R.id.engloadneedle);
            //      4 --> Intake Temperature Gauge
                    final ImageView intTempNdle = (ImageView) findViewById(R.id.inttempneedle);
            //      5 --> Voltage Gauge
                    final ImageView voltNdle    = (ImageView) findViewById(R.id.voltneedle);

            int gaugeNum = msg.arg1;
            int val =  msg.arg2;
            RotateAnimation rotator;
            switch (gaugeNum) {
                case 0:
                    val =  val - minEnginTempDeg;
                    if(tempOrgVa == 0) rotator = new RotateAnimation(minEnginTempDeg,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    else rotator = new RotateAnimation(tempOrgVa,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    tempOrgVa = val;
                    rotator.setInterpolator(new LinearInterpolator());
                    rotator.setFillAfter(true);
                    rotator.setDuration(animationSpeed);
                    tempNdle.startAnimation(rotator);
                    break;
                case 1:
                    val =  val - minSpeedDeg;
                    if(spdOrgVal == 0) rotator = new RotateAnimation(minSpeedDeg,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    else rotator = new RotateAnimation(spdOrgVal,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    spdOrgVal = val;
                    rotator.setInterpolator(new LinearInterpolator());
                    rotator.setFillAfter(true);
                    rotator.setDuration(animationSpeed);
                    spdNdle.startAnimation(rotator);
                    break;
                case 2:
                    val =  val - minRPMDeg;
                    if(rpmOrgVal == 0) rotator = new RotateAnimation(minRPMDeg,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    else rotator = new RotateAnimation(rpmOrgVal,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    rpmOrgVal = val;
                    rotator.setInterpolator(new LinearInterpolator());
                    rotator.setFillAfter(true);
                    rotator.setDuration(animationSpeed);
                    rpmNdle.startAnimation(rotator);
                    break;

                case 3:
                    val =  val - minEnginLoadDeg;
                    if(egnLoadOrgVal == 0) rotator = new RotateAnimation(minEnginLoadDeg,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    else rotator = new RotateAnimation(egnLoadOrgVal,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    egnLoadOrgVal = val;
                    rotator.setInterpolator(new LinearInterpolator());
                    rotator.setFillAfter(true);
                    rotator.setDuration(animationSpeed);
                    egnLdNdle.startAnimation(rotator);
                    break;
                case 4:
                    val =  val - minIntTempDeg;
                    if(intkTempOrgVal == 0) rotator = new RotateAnimation(minIntTempDeg,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    else rotator = new RotateAnimation(intkTempOrgVal,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    intkTempOrgVal = val;
                    rotator.setInterpolator(new LinearInterpolator());
                    rotator.setFillAfter(true);
                    rotator.setDuration(animationSpeed);
                    intTempNdle.startAnimation(rotator);
                    break;
                case 5:
                    val =  val - minVoltDeg;
                    if(voltOrgVal == 0) rotator = new RotateAnimation(minVoltDeg,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    else rotator = new RotateAnimation(voltOrgVal,val, Animation.RELATIVE_TO_SELF,.52f,Animation.RELATIVE_TO_SELF,.785f);
                    voltOrgVal = val;
                    rotator.setInterpolator(new LinearInterpolator());
                    rotator.setFillAfter(true);
                    rotator.setDuration(animationSpeed);
                    voltNdle.startAnimation(rotator);
                    break;
                default:
            }
        }
    };
}
