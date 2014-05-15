package org.souza.obdmobilereader.obdmobilereader.mobileobdreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class DtcListAdapter extends BaseAdapter {

    private ArrayList<DtcListItem> _codes;
    Context cntxt;

    public DtcListAdapter(ArrayList<DtcListItem> codes, Context c){
        _codes = codes;
        cntxt = c;
    }
    @Override
    public int getCount() {
        return _codes.size();
    }

    @Override
    public Object getItem(int idx) {
        return _codes.get(idx);
    }

    @Override
    public long getItemId(int idx) {
        return idx;
    }

    public void clearCodes(){
        _codes.clear();
        this.notifyDataSetChanged();
    }

    public void addCode(String code, String summary){
        _codes.add(new DtcListItem(code,summary));
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int idx, View view, ViewGroup viewGroup) {
        View dtcv = view;
        if(dtcv == null){
            LayoutInflater lf = (LayoutInflater) cntxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dtcv = lf.inflate(R.layout.dtc_row,null);
        }

        TextView txtCode = (TextView) dtcv.findViewById(R.id.dtcode);
        TextView txtSummary = (TextView) dtcv.findViewById(R.id.dtcsum);
        DtcListItem item = _codes.get(idx);
        txtCode.setText(item.code);
        txtSummary.setText(item.summary);

        return dtcv;
    }
}
