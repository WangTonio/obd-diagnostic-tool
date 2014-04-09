package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.ActionBar;
import android.app.FragmentTransaction;
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
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;


public class OBD_Main extends FragmentActivity implements DtcLookUpFragment.OnFragmentInteractionListener {
    public static final int TAB_CNT  = 3;
    public static final int MAIN_TAB = 0;
    public static final int LD_TAB   = 1;
    public static final int DTC_TAB  = 2;

    public MainFragment      mainF;
    public LiveDataFragment  liveDataF;
    public DtcLookUpFragment dtcLUF;
    Fragment[]               arrFragment;
    MyAdapter                myFragmentPageAdapter;
    ViewPager                myViewPager;

    /* Gauge values & utilities */
    private final Handler gaugesHandler =  new Handler();
    public int tempOrgVa      = 0;
    public int spdOrgVal      = 0;
    public int rpmOrgVal      = 0;
    public int egnLoadOrgVal  = 0;
    public int intkTempOrgVal = 0;
    public int voltOrgVal     = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd__main);

        /* Check if bluetooth is enabled */

        //Create App Fragments
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


        final ImageView tempNdle    = (ImageView) findViewById(R.id.tempNeedle);
        final ImageView spdNdle     = (ImageView) findViewById(R.id.spdNeedle);
        final ImageView rpmNdle     = (ImageView) findViewById(R.id.rpmNeedle);
        final ImageView egnLdNdle   = (ImageView) findViewById(R.id.engloadneedle);
        final ImageView intTempNdle = (ImageView) findViewById(R.id.inttempneedle);
        final ImageView voltNdle    = (ImageView) findViewById(R.id.voltneedle);


        @Override
        public void handleMessage(Message msg) {
            /* Bluetooth messaging would go here */
            /*                                   */

            /* Rotate Animations would go here   */
            /* Gauge Numbers
                  0 --> Engine Temperature gauge
                  1 --> Speedometer Gauge
                  2 --> Revolutions per Minute Gauge
                  3 --> Engine Load Gauge
                  4 --> Intake Temperature Gauge
                  5 --> Voltage Gauge
             */
            int gaugeNum = msg.arg1;
            RotateAnimation rotator;
            switch (gaugeNum) {
                case 0:
                    Toast.makeText(getApplicationContext(),"Arg 2:" + msg.arg2, Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
            }
        }
    };
}
