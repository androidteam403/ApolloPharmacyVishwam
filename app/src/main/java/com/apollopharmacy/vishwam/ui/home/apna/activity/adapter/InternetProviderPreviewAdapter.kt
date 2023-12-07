package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ProviderAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class InternetProviderPreviewAdapter(
    var mContext: Context,
    var internetProviders: ArrayList<SurveyCreateRequest.InternetServiceProvider>,
) : RecyclerView.Adapter<InternetProviderPreviewAdapter.ViewHolder>() {
    class ViewHolder(val providerAdapterLayoutBinding: ProviderAdapterLayoutBinding) :
        RecyclerView.ViewHolder(providerAdapterLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val providerAdapterLayoutBinding: ProviderAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.provider_adapter_layout,
            parent,
            false)
        return ViewHolder(providerAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val internetServiceProvider = internetProviders[position]
        if (internetServiceProvider.name != null && internetServiceProvider.name!!.isNotEmpty()) {
            holder.providerAdapterLayoutBinding.name.text = internetServiceProvider.name
        }
    }

    override fun getItemCount(): Int {
        return internetProviders.size
    }
}