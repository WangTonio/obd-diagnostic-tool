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
/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class MainFragment extends Fragment {


    static int pressed = 0;

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

        Button btnCon = (Button) rootView.findViewById(R.id.btnConn);
        btnCon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFrag(view);
            }
        });

        TextView tv =  (TextView) rootView.findViewById(R.id.conStatus);
        tv.setText(R.string.stat_disconnect);
        return rootView;
    }


    public void onClickFrag(View view) {
        Button btn =  (Button) view;
        TextView tv = (TextView) getView().findViewById(R.id.conStatus);
        if(pressed == 0){
            btn.setBackgroundResource(R.drawable.consucc);
            tv.setText("Connected");
        }else if( pressed == 1){
            btn.setBackgroundResource(R.drawable.confail);
            tv.setText("Disconnected");
        }
        Log.d("onClick -->"," "+pressed);
        pressed = 1 - pressed;
    }
}
