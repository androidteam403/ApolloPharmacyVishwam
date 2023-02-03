package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterCcEmailBinding
import com.apollopharmacy.vishwam.databinding.AdapterEmailaddressChampsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsCallback

class EmailAddressCCAdapter(
    private var surveyCCDetailsList: MutableList<String>,
    private var surveyDetailscallback: SurveyDetailsCallback,
    private var applicationContext: Context,
) : RecyclerView.Adapter<EmailAddressCCAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EmailAddressCCAdapter.ViewHolder {
        val adapterCcEmailAddressCCAdapter:AdapterCcEmailBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_cc_email,
                parent,
                false
            )
        return ViewHolder(adapterCcEmailAddressCCAdapter)
    }

    override fun onBindViewHolder(holder: EmailAddressCCAdapter.ViewHolder, position: Int) {
        val items = surveyCCDetailsList.get(position)
        holder.adapterCcEmailAddressCCAdapter.setEmailAddress.text =
            surveyCCDetailsList.get(position)

        holder.adapterCcEmailAddressCCAdapter.ccEmailDelete.setOnClickListener {
            surveyDetailscallback.deleteEmailAddressCC(surveyCCDetailsList.get(position))
        }
    }

    override fun getItemCount(): Int {
        return surveyCCDetailsList.size
    }

    class ViewHolder(val adapterCcEmailAddressCCAdapter: AdapterCcEmailBinding) :

        RecyclerView.ViewHolder(adapterCcEmailAddressCCAdapter.root)
}