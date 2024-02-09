package com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apollopharmacy.vishwam.R;

import java.util.List;

public class DropdownSpinner extends BaseAdapter {
    Activity activity;
    List<String> StatusList;
    LayoutInflater inflter;

    public DropdownSpinner(Activity activity, List<String> StatusList) {
        this.activity = activity;
        this.StatusList = StatusList;
        inflter = (LayoutInflater.from(activity));
    }


    @Override
    public int getCount() {
        return StatusList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_custom_spinner_qr, parent, false);
        TextView dropDownItemText = view.findViewById(R.id.status_idqr);
        dropDownItemText.setText(StatusList.get(position));
        return view;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.view_custom_spinner_selected_item_qr, null);
        TextView selectedStatusText = view.findViewById(R.id.selected_status_idqr);
        selectedStatusText.setText(StatusList.get(position));
        return view;
    }
}
