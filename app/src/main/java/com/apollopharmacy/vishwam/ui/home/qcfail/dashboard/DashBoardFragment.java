package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;

import com.anychart.AnyChart;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.apollopharmacy.vishw.PendingFragment;
import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.base.BaseFragment;
import com.apollopharmacy.vishwam.databinding.FragmentDashBoardBinding;
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.ApprovedFragment;
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter.SiteTableAdapter;
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.model.SiteData;
import com.apollopharmacy.vishwam.ui.home.qcfail.rejected.RejectedFragment;

import java.util.ArrayList;
import java.util.List;

public class DashBoardFragment extends BaseFragment<QcDashBoardViewModel, FragmentDashBoardBinding> {
    ArrayList<SiteData> siteDataList = new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_dash_board;
    }

    @NonNull
    @Override
    public QcDashBoardViewModel retrieveViewModel() {
        return new QcDashBoardViewModel();
    }

    @Override
    public void setup() {
        siteDataList.add(new SiteData("16001", "24", "12", "7", "5"));
        siteDataList.add(new SiteData("14055", "71", "16", "49", "6"));
        siteDataList.add(new SiteData("14068", "580", "266", "211", "103"));
        siteDataList.add(new SiteData("12357", "45", "12", "20", "13"));

        SiteTableAdapter siteTableAdapter = new SiteTableAdapter(siteDataList, getContext());
        viewBinding.siteTableRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        viewBinding.siteTableRcv.setAdapter(siteTableAdapter);

        // Pie chart
        Pie pie = AnyChart.pie();
        pie.labels().format("{%value}");

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Pending", 257));
        data.add(new ValueDataEntry("Approved", 321));
        data.add(new ValueDataEntry("Rejected", 142));
        pie.data(data);

        viewBinding.pieChart.setChart(pie);


        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Log.i("PIE_CHART", "onClick: " + event.getData().get("value"));
                Log.i("PIE_CHART", "onClick: " + event.getData().get("x"));
                if (event.getData().get("x").equals("Pending")) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new PendingFragment()).commit();
                } else if (event.getData().get("x").equals("Approved")) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ApprovedFragment()).commit();
                } else if (event.getData().get("x").equals("Rejected")) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RejectedFragment()).commit();
                }
            }
        });
    }
}