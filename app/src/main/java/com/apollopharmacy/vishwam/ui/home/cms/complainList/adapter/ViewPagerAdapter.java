package com.apollopharmacy.vishwam.ui.home.cms.complainList.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.apollopharmacy.vishwam.data.model.cms.ResponseNewTicketlist;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.ImageClickListener;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.ComplaintsListDetailsActivity;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.ComplaintsListDetailsCallback;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.AttachmentsFragment;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.DetailsFragment;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.HistoryFragment;
import com.apollopharmacy.vishwam.ui.home.cms.complainList.activity.fragments.callback.HistoryCallback;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    ResponseNewTicketlist.Row orderDataWp;
    ArrayList<ResponseNewTicketlist.Row> orderData;
    int positionOfAdapter;
    ImageClickListener imageClickListener;
    HistoryCallback historyCallback;
    Boolean isFromApprovalList;
    ComplaintsListDetailsCallback complaintsListDetailsCallback;


    public ViewPagerAdapter(@NonNull ComplaintsListDetailsActivity complaintsListDetailsActivity, ResponseNewTicketlist.Row orderDataWp, ArrayList<ResponseNewTicketlist.Row> orderData, int position, ImageClickListener imageClickListener, HistoryCallback historyCallback, Boolean isFromApprovalList
            , ComplaintsListDetailsCallback complaintsListDetailsCallback) {
        super(complaintsListDetailsActivity);
        this.orderDataWp = orderDataWp;
        this.orderData = orderData;
        this.positionOfAdapter = position;
        this.imageClickListener = imageClickListener;
        this.historyCallback = historyCallback;
        this.isFromApprovalList = isFromApprovalList;
        this.complaintsListDetailsCallback=complaintsListDetailsCallback;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DetailsFragment(orderDataWp);
            case 1:
                return new HistoryFragment(orderDataWp, orderData, positionOfAdapter, historyCallback, isFromApprovalList, complaintsListDetailsCallback);
            case 2:
                return new AttachmentsFragment(orderDataWp, imageClickListener, position);
            default:
                return new DetailsFragment(orderDataWp);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
