package com.apollopharmacy.vishwam.ui.rider.complaints.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.AdapterComplaintsListBinding;
import com.apollopharmacy.vishwam.databinding.LoadingProgressLazyBinding;
import com.apollopharmacy.vishwam.ui.rider.complaints.ComplaintsFragmentCallback;
import com.apollopharmacy.vishwam.ui.rider.complaints.model.ComplaintsResponse;
import com.apollopharmacy.vishwam.ui.rider.reports.adapter.OrdersCodStatusAdapter;
import com.apollopharmacy.vishwam.util.Utlis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComplaintsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ComplaintsResponse.Row> complaintsList;
    private ComplaintsFragmentCallback mListener;


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public ComplaintsListAdapter(Context mContext, List<ComplaintsResponse.Row> complaintsList, ComplaintsFragmentCallback mListener) {
        this.mContext = mContext;
        this.complaintsList = complaintsList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            AdapterComplaintsListBinding complaintsListBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_complaints_list, parent, false);
            return new ComplaintViewHolder(complaintsListBinding);
        } else {
            LoadingProgressLazyBinding loadingProgressbarComplaintBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.loading_progress_lazy, parent, false);
            return new LoadingViewHolder(loadingProgressbarComplaintBinding);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ComplaintsResponse.Row complaint = complaintsList.get(position);
        if (holder instanceof ComplaintViewHolder) {
            complaintsOnBindViewHolder((ComplaintViewHolder) holder, position, complaint);
        } else if (holder instanceof OrdersCodStatusAdapter.LoadingViewHolder) {

        }
    }

    private void complaintsOnBindViewHolder(ComplaintViewHolder holder, int position, ComplaintsResponse.Row complaint) {
        holder.complaintsListBinding.complaintNumber.setText(complaint.getComplaintNo() == null ? "-" : complaint.getComplaintNo());
        holder.complaintsListBinding.reason.setText(complaint.getReason().getName());
        holder.complaintsListBinding.comment.setText(complaint.getComments());
        holder.complaintsListBinding.status.setText(complaint.getStatus().getName());
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",  Locale.ENGLISH);
            Date orderDates = formatter.parse(complaint.getCreatedTime());
            long orderDateMills = orderDates.getTime();
            holder.complaintsListBinding.complaintedDate.setText(Utlis.INSTANCE.getTimeFormatter(orderDateMills));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return complaintsList.size();
    }

    public int getItemViewType(int position) {
        return complaintsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ComplaintViewHolder extends RecyclerView.ViewHolder {
        AdapterComplaintsListBinding complaintsListBinding;

        public ComplaintViewHolder(@NonNull AdapterComplaintsListBinding complaintsListBinding) {
            super(complaintsListBinding.getRoot());
            this.complaintsListBinding = complaintsListBinding;
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingProgressLazyBinding loadingProgressbarComplaintBinding;

        public LoadingViewHolder(@NonNull LoadingProgressLazyBinding loadingProgressbarComplaintBinding) {
            super(loadingProgressbarComplaintBinding.getRoot());
            this.loadingProgressbarComplaintBinding = loadingProgressbarComplaintBinding;
        }
    }
}