package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apollopharmacy.vishwam.R;
import com.apollopharmacy.vishwam.databinding.SurveyListCompletedLayoutBinding;
import com.apollopharmacy.vishwam.databinding.SurveyListPendingLayoutBinding;
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist.model.SurveyDetailsModelClass;


import java.util.List;

import static com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist.model.SurveyDetailsModelClass.COMPLETED_LAYOUT;
import static com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist.model.SurveyDetailsModelClass.PENDING_LAYOUT;


public class SurveyListAdapter extends RecyclerView.Adapter {

    private List<SurveyDetailsModelClass> surveyDetailsList;
    private Context mContext;

    public SurveyListAdapter(List<SurveyDetailsModelClass> surveyDetailsList, Context mContext) {
        this.surveyDetailsList = surveyDetailsList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        switch (surveyDetailsList.get(position).getViewTye()) {
            case 0:
                return PENDING_LAYOUT;
            case 1:
                return COMPLETED_LAYOUT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                SurveyListPendingLayoutBinding pendingLayoutBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(mContext),
                        R.layout.survey_list_pending_layout,
                        parent,
                        false
                );
                return new PendingViewHolder(pendingLayoutBinding);
            case 1:
                SurveyListCompletedLayoutBinding completedLayoutBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(mContext),
                        R.layout.survey_list_completed_layout,
                        parent,
                        false
                );
                return new CompletedViewHolder(completedLayoutBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (surveyDetailsList.get(position).getViewTye()) {
            case PENDING_LAYOUT:
                ((PendingViewHolder)holder).pendingLayoutBinding.storeIdText.setText(surveyDetailsList.get(position).getStoreId());
                ((PendingViewHolder)holder).pendingLayoutBinding.location.setText(surveyDetailsList.get(position).getLocation());
                ((PendingViewHolder)holder).pendingLayoutBinding.statusText.setText(surveyDetailsList.get(position).getStatus());
                ((PendingViewHolder)holder).pendingLayoutBinding.executiveEmailText.setText(surveyDetailsList.get(position).getExecutive());
                break;
            case COMPLETED_LAYOUT:
                ((CompletedViewHolder)holder).completedLayoutBinding.storeIdText.setText(surveyDetailsList.get(position).getStoreId());
                ((CompletedViewHolder)holder).completedLayoutBinding.location.setText(surveyDetailsList.get(position).getLocation());
                ((CompletedViewHolder)holder).completedLayoutBinding.status.setText(surveyDetailsList.get(position).getStatus());
                ((CompletedViewHolder)holder).completedLayoutBinding.dateOfVisit.setText(surveyDetailsList.get(position).getDateOfVisit());
                ((CompletedViewHolder)holder).completedLayoutBinding.executiveEmailText.setText(surveyDetailsList.get(position).getExecutive());
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return surveyDetailsList.size();
    }


    class PendingViewHolder extends RecyclerView.ViewHolder {
        SurveyListPendingLayoutBinding pendingLayoutBinding;

        public PendingViewHolder(@NonNull SurveyListPendingLayoutBinding pendingLayoutBinding) {
            super(pendingLayoutBinding.getRoot());
            this.pendingLayoutBinding = pendingLayoutBinding;
        }
    }

    class CompletedViewHolder extends RecyclerView.ViewHolder {
        SurveyListCompletedLayoutBinding completedLayoutBinding;

        public CompletedViewHolder(@NonNull SurveyListCompletedLayoutBinding completedLayoutBinding) {
            super(completedLayoutBinding.getRoot());
            this.completedLayoutBinding = completedLayoutBinding;
        }
    }
}
