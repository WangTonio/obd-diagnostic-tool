package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.dtclookup.DtcListAdapter;
import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.dtclookup.DtcListItem;
import org.souza.obdmobilereader.obdmobilereader.mobileobdreader.obddata.Dtc_CodeList;

import java.util.ArrayList;
import java.util.Random;



public class DtcLookUpFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String BASEWEBADDR = "https://www.google.com/search?q=diagnostic+trouble+code+";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public DtcListAdapter codeAdapter;
    ArrayList<DtcListItem> codes;

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

    public DtcListAdapter getCodeAdapter(){return this.codeAdapter;}

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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = BASEWEBADDR+codes.get(i).getCode();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                Log.d("ListView onItemSelected",i + " "  + codes.get(i).getCode() );
            }
        });


        codes =  new ArrayList<DtcListItem>();
        codeAdapter = new DtcListAdapter(codes,getActivity());
        lv.setAdapter(codeAdapter);

        return rv;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            Context ctxt = getActivity();
            ((OBD_Main)ctxt).dtcLookUpCom = (OnFragmentInteractionListener) activity;
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

        public void onFragmentInteraction(Uri uri);
        public void btn_DtcLookUpCheckCodes();
        public void btn_DtcLookUpClearCodes();
    }

    public void clearCodes(){
        Log.d("clearCodes -->", " Clearing Codes");
        codeAdapter.clearCodes();
    }

    public void addCode(){
        /* For Demo */
        DtcListItem tmp;
        int  code_cnt = (new Random()).nextInt(4);
        for(int i = 0; i < code_cnt;i++){
            tmp = Dtc_CodeList.getRandomCode();
            codeAdapter.addCode(tmp.getCode(),tmp.getSummary());
        }
        /* End Demo Code */
    }




}
