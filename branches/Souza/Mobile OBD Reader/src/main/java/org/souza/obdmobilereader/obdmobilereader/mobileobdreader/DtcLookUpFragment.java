package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata.Dtc_List;

import java.util.ArrayList;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DtcLookUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DtcLookUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DtcLookUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    DtcListAdapter codeAdapter;
    ArrayList<DtcListItem> codes;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DtcLookUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DtcLookUpFragment newInstance(String param1, String param2) {
        DtcLookUpFragment fragment = new DtcLookUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public DtcLookUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rv = inflater.inflate(R.layout.fragment_dtc_look_up, container, false);

        ListView lv =(ListView)rv.findViewById(R.id.dtCodeView);

        codes =  new ArrayList<DtcListItem>();
        codes.add(new DtcListItem("P0300","Random/Multiple Cylinder Misfire"));
        codes.add(new DtcListItem("P0135","Heated Oxygen Sensor Heater circuit Bank 1 Sensor 1."));
        codes.add(new DtcListItem("P0342","Warm-Up Catalyst Efficiency below threshold (Bank 1)."));

        codeAdapter = new DtcListAdapter(codes,getActivity());
        lv.setAdapter(codeAdapter);
        final Button btn = (Button) rv.findViewById(R.id.btn_cc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCodes(view);
            }
        });

        final Button btn_Add = (Button)rv.findViewById(R.id.btn_debug_add);
        btn_Add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                debug_addCode(view);
            }
        });
        return rv;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

    }

    public void clearCodes(View view){
        Log.d("clearCodes -->", " Clearing Codes");
        codeAdapter.clearCodes();
    }

    public void debug_addCode(View view){
        Log.d("debug_addCode -->", "Adding Code");
        DtcListItem temp =  Dtc_List.getRandomCode();
        codeAdapter.addCode(temp.getCode(),temp.getSummary());
    }

}
