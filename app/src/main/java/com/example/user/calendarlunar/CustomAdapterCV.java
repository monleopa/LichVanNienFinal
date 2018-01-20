package com.example.user.calendarlunar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 1/19/2018.
 */

public class CustomAdapterCV extends BaseAdapter {

    Context context;
    ArrayList<CongViecCustom> arrayListCV;

    public CustomAdapterCV(Context context, ArrayList<CongViecCustom> arrayListCV) {
        this.context = context;
        this.arrayListCV = arrayListCV;
    }

    @Override
    public int getCount() {
        return arrayListCV.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListCV.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.listviewcv,null);

        CongViecCustom cv  = arrayListCV.get(i);

        TextView txtTGbt, txtTitlecv, txtContentCV;
        txtTGbt = view.findViewById(R.id.txtTgbt);
        txtTitlecv = view.findViewById(R.id.txtTitlecv);
        txtContentCV = view.findViewById(R.id.txtContentCV);

        txtTGbt.setText(cv.timecv);
        txtTitlecv.setText(cv.titlecv);
        txtContentCV.setText(cv.cv);
        return view;
    }
}
