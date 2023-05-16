package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterGetsurveyDetailsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.GetSurveyDetailsListActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.GetSurveyDetailsListCallback
import com.apollopharmacy.vishwam.ui.home.model.GetSurveyDetailsModelResponse

class GetSurveyDetailsAdapter(
    private var getSurvetDetailsModelResponse: GetSurveyDetailsModelResponse,
    private var applicationContext: Context,
    private var getSurveyDetailsListCallback: GetSurveyDetailsListCallback
) : RecyclerView.Adapter<GetSurveyDetailsAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GetSurveyDetailsAdapter.ViewHolder {
        val adapterGetSurveyDetailsBinding: AdapterGetsurveyDetailsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_getsurvey_details,
                parent,
                false
            )
        return ViewHolder(adapterGetSurveyDetailsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       var surveyList = getSurvetDetailsModelResponse.storeDetails.get(position)
        holder.adapterGetSurveyDetailsBinding.champsRefId.text=surveyList.champsRefernceId
        holder.adapterGetSurveyDetailsBinding.siteName.text=surveyList.sitename + "" + surveyList.city
        holder.adapterGetSurveyDetailsBinding.status.text= surveyList.visitDate
        holder.adapterGetSurveyDetailsBinding.visitDate.text=surveyList.visitDate
        holder.adapterGetSurveyDetailsBinding.cardView.setOnClickListener {
            getSurveyDetailsListCallback.onClickCardView(surveyList.status, surveyList.champsRefernceId)
        }

    }


    override fun getItemCount(): Int {
       return getSurvetDetailsModelResponse.storeDetails.size
    }


    class ViewHolder(val adapterGetSurveyDetailsBinding: AdapterGetsurveyDetailsBinding) :
        RecyclerView.ViewHolder(adapterGetSurveyDetailsBinding.root)
}