package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Tony on 3/30/14.
 */
public class SetPrefActivity extends Activity{
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new PrefFragment())
                .commit();
    }
}
