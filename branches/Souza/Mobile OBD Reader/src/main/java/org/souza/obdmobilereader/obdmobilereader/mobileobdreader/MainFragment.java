package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }
     public void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);

     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        assert rootView != null;

        TextView tv =  (TextView) rootView.findViewById(R.id.conStatus);
        tv.setText(R.string.stat_disconnect);
        return rootView;
    }

}
