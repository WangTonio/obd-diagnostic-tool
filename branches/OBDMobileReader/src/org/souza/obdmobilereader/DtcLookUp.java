package org.souza.obdmobilereader;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DtcLookUp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dtc_look_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dtc_look_up, menu);
		return true;
	}

}