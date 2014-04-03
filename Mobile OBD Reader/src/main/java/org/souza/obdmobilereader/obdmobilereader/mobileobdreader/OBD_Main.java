package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;



public class OBD_Main extends FragmentActivity implements DtcLookUpFragment.OnFragmentInteractionListener {
    public static final int TAB_CNT = 3;
    public static final int MAIN_TAB = 0;
    public static final int LD_TAB = 1;
    public static final int DTC_TAB = 2;

    public MainFragment mainF;
    public LiveDataFragment liveDataF;
    public DtcLookUpFragment dtcLUF;
    Fragment[] arrFragment;
    MyAdapter myFragmentPageAdapter;
    ViewPager myViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd__main);

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
}
