package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class LiveDataFragment extends Fragment {


    public LiveDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live_data, container, false);
    }


}
