package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

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
        View rootView = inflater.inflate(R.layout.fragment_live_data,container,false);


        ImageView tempNeedle = (ImageView) rootView.findViewById(R.id.tempNeedle);
        RotateAnimation rotator = new RotateAnimation(-110,-110,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.79f);
        rotator.setInterpolator(new LinearInterpolator());
        rotator.setFillAfter(true);
        rotator.setDuration(100);
        tempNeedle.startAnimation(rotator);

        ImageView spdNeedle = (ImageView) rootView.findViewById(R.id.spdNeedle);
        RotateAnimation rotator1 = new RotateAnimation(-90,-90,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.79f);
        rotator1.setInterpolator(new LinearInterpolator());
        rotator1.setFillAfter(true);
        rotator1.setDuration(100);
        spdNeedle.startAnimation(rotator1);

        ImageView rpmNeedle = (ImageView) rootView.findViewById(R.id.rpmNeedle);
        RotateAnimation rpmrotator = new RotateAnimation(-85,-88,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.79f);
        rpmrotator.setInterpolator(new LinearInterpolator());
        rpmrotator.setFillAfter(true);
        rpmrotator.setDuration(100);
        rpmNeedle.startAnimation(rpmrotator);

        ImageView egnLNeedle = (ImageView) rootView.findViewById(R.id.engloadneedle);
        RotateAnimation egnLRotator = new RotateAnimation(-110,-110,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.79f);
        egnLRotator.setInterpolator(new LinearInterpolator());
        egnLRotator.setFillAfter(true);
        egnLRotator.setDuration(100);
        egnLNeedle.startAnimation(egnLRotator);

        ImageView inTempNeedle = (ImageView) rootView.findViewById(R.id.inttempneedle);
        RotateAnimation inTempRotator = new RotateAnimation(-110,-110,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.79f);
        inTempRotator.setInterpolator(new LinearInterpolator());
        inTempRotator.setFillAfter(true);
        inTempRotator.setDuration(100);
        inTempNeedle.startAnimation(inTempRotator);

        ImageView voltNeedle = (ImageView) rootView.findViewById(R.id.voltneedle);
        RotateAnimation voltRotator = new RotateAnimation(-110,-110,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.79f);
        voltRotator.setInterpolator(new LinearInterpolator());
        voltRotator.setFillAfter(true);
        voltRotator.setDuration(100);
        voltNeedle.startAnimation(voltRotator);

/*
        final Button btn = (Button)rootView.findViewById(R.id.gaugeBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        assert rootView != null;

        return rootView;

    }



}
