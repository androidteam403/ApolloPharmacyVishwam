package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.LayoutSiteTableDataBinding;
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.model.SiteData;

import java.util.ArrayList;

public class SiteTableAdapter extends RecyclerView.Adapter<SiteTableAdapter.ViewHolder> {
    private ArrayList<SiteData> siteDataList;
    private Context mContext;

    public SiteTableAdapter(ArrayList<SiteData> siteDataList, Context mContext) {
        this.siteDataList = siteDataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutSiteTableDataBinding siteTableDataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_site_table_data, parent, false);
        return new SiteTableAdapter.ViewHolder(siteTableDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.siteTableDataBinding.siteIdText.setText(siteDataList.get(position).getSiteId());
        holder.siteTableDataBinding.requestCount.setText(siteDataList.get(position).getRequest());
        holder.siteTableDataBinding.pendingCount.setText(siteDataList.get(position).getPending());
        holder.siteTableDataBinding.approvedCount.setText(siteDataList.get(position).getApproved());
        holder.siteTableDataBinding.rejectedCount.setText(siteDataList.get(position).getRejected());
    }

    @Override
    public int getItemCount() {
        return siteDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutSiteTableDataBinding siteTableDataBinding;

        public ViewHolder(@NonNull LayoutSiteTableDataBinding siteTableDataBinding) {
            super(siteTableDataBinding.getRoot());
            this.siteTableDataBinding = siteTableDataBinding;
        }
    }
}
