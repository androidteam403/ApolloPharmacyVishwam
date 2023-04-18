package com.apollopharmacy.vishwam.ui.home.apna.survey.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApnaSurveyLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyListResponse
import com.apollopharmacy.vishwam.ui.home.apna.survey.ApnaSurveyCallback

class ApnaSurveyAdapter(
    val mContext: Context,
    var approveList: ArrayList<SurveyListResponse>,
    val mClicklistner: ApnaSurveyCallback,

    ) :

    RecyclerView.Adapter<ApnaSurveyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val apnaSurveyLayoutBinding: ApnaSurveyLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.apna_survey_layout,
                parent,
                false
            )
        return ViewHolder(apnaSurveyLayoutBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approvedOrders = approveList.get(position)
        holder.apnaSurveyLayoutBinding.status.setText(approvedOrders.data!!.listData!!.rows!!.get(0).status!!.name)
//        if(approvedOrders.equals("APPROVED")){
//            holder.apnaSurveyLayoutBinding.status.setText("Completed")
//            holder.apnaSurveyLayoutBinding.status.setTextColor(Color.parseColor("#39B54A"))
//
//            holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundResource(R.drawable.retro_background_approval)
//
//
//        }else if(approvedOrders.equals("PENDING")){
//            holder.apnaSurveyLayoutBinding.status.setText("In Progress(3/10)")
//            holder.apnaSurveyLayoutBinding.status.setTextColor(Color.parseColor("#f26522"))
//
//            holder.apnaSurveyLayoutBinding.statusLayout.setBackgroundResource(R.drawable.retro_background_pending)
//
//        }







        }

    override fun getItemCount(): Int {

        return approveList.size
    }

    class ViewHolder(val apnaSurveyLayoutBinding: ApnaSurveyLayoutBinding) :
        RecyclerView.ViewHolder(apnaSurveyLayoutBinding.root)

}