package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        final Button btn = (Button)rootView.findViewById(R.id.gaugeBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        assert rootView != null;

        return rootView;

    }



}
