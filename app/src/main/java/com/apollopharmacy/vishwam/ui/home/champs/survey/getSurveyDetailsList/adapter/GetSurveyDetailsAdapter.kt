package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.AdapterGetsurveyDetailsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.GetSurveyDetailsListCallback
import com.apollopharmacy.vishwam.ui.home.model.GetSurveyDetailsModelResponse
import java.text.SimpleDateFormat
import java.util.*

class GetSurveyDetailsAdapter(
    private var getSurvetDetailsModelResponse: List<GetSurveyDetailsModelResponse.StoreDetail>,
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

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       var surveyList = getSurvetDetailsModelResponse.get(position)
        holder.adapterGetSurveyDetailsBinding.champsRefId.text=surveyList.champsRefernceId
        holder.adapterGetSurveyDetailsBinding.siteName.text=surveyList.sitename + "," + " " + surveyList.city

        val strDate = surveyList.visitDate
        val dateFormat = SimpleDateFormat("dd-MM-yy kk:mm:ss");
        val date = dateFormat.parse(strDate)
        val dateNewFormat =
            SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.getDefault()).format(date)
        if(surveyList.status.equals("PENDING")){
            holder.adapterGetSurveyDetailsBinding.status.setTextColor(context.getColor(R.color.pending_reshoot_color))
            holder.adapterGetSurveyDetailsBinding.statusLayout.setBackgroundColor((context.getColor(R.color.light_pink_for_reshoot_pending))
            )
            holder.adapterGetSurveyDetailsBinding.status.text= surveyList.status
        }else if(surveyList.status.equals("COMPLETED")){
            holder.adapterGetSurveyDetailsBinding.status.text= surveyList.status
            holder.adapterGetSurveyDetailsBinding.status.setTextColor(context.getColor(R.color.greenn))
            holder.adapterGetSurveyDetailsBinding.statusLayout.setBackgroundColor(context.getColor(R.color.green_bg_for_approved))
        }
        holder.adapterGetSurveyDetailsBinding.visitDate.text=dateNewFormat
        holder.adapterGetSurveyDetailsBinding.cardView.setOnClickListener {
            getSurveyDetailsListCallback.onClickCardView(surveyList.status, surveyList.champsRefernceId, holder.adapterGetSurveyDetailsBinding.siteName.text.toString(),   holder.adapterGetSurveyDetailsBinding.visitDate.text.toString())
        }

    }


    override fun getItemCount(): Int {
       return getSurvetDetailsModelResponse.size
    }


    class ViewHolder(val adapterGetSurveyDetailsBinding: AdapterGetsurveyDetailsBinding) :
        RecyclerView.ViewHolder(adapterGetSurveyDetailsBinding.root)
}