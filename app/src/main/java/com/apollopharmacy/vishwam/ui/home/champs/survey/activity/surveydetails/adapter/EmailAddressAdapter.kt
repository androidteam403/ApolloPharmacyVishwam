package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterEmailaddressChampsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsCallback


class EmailAddressAdapter(
    private  var surveyEmailDetailsList: MutableList<String>,
    private  var applicationContext: Context,
    private var surveyDetailscallback: SurveyDetailsCallback
) : RecyclerView.Adapter<EmailAddressAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterEmailaddressChampsBinding:AdapterEmailaddressChampsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_emailaddress_champs,
                parent,
                false
            )
        return ViewHolder(adapterEmailaddressChampsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = surveyEmailDetailsList.get(position)
        holder.adapterEmailaddressChampsBinding.setEmailAddress.text = surveyEmailDetailsList.get(position)

        holder.adapterEmailaddressChampsBinding.deleteEmailAddressRec.setOnClickListener {
            surveyDetailscallback.deleteEmailAddressRec(surveyEmailDetailsList.get(position))
        }
    }

    override fun getItemCount(): Int {
        return surveyEmailDetailsList.size
    }

    class ViewHolder(val adapterEmailaddressChampsBinding:AdapterEmailaddressChampsBinding):
        RecyclerView.ViewHolder(adapterEmailaddressChampsBinding.root)

}