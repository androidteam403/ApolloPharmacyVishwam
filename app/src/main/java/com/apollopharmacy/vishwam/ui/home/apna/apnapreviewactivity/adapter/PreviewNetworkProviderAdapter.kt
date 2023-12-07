package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ProviderAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

class PreviewNetworkProviderAdapter(
    var mContext: Context,
    var networkProviders: ArrayList<SurveyDetailsList.NetworkServiceProvider>,
) : RecyclerView.Adapter<PreviewNetworkProviderAdapter.ViewHolder>() {
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
        val networkServiceProvider = networkProviders[position]
        holder.providerAdapterLayoutBinding.name.text = networkServiceProvider.name
    }

    override fun getItemCount(): Int {
        return networkProviders.size
    }
}