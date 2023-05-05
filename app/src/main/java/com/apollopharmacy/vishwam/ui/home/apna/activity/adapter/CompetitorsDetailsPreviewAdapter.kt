package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterChemistListPreviewBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class CompetitorsDetailsPreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.Chemist>,
): RecyclerView.Adapter<CompetitorsDetailsPreviewAdapter.ViewHolder>() {

    class ViewHolder(val adapterChemistListPreviewBinding: AdapterChemistListPreviewBinding) :
        RecyclerView.ViewHolder(adapterChemistListPreviewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterChemistListPreviewBinding = DataBindingUtil.inflate<AdapterChemistListPreviewBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_chemist_list_preview,
            parent,
            false
        )
        return ViewHolder(adapterChemistListPreviewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.adapterChemistListPreviewBinding.name.setText(data[position].chemist)
        holder.adapterChemistListPreviewBinding.organised.setText(data[position].organised!!.uid)
        holder.adapterChemistListPreviewBinding.organisedAvgsale.setText(data[position].orgAvgSale.toString())
        holder.adapterChemistListPreviewBinding.unorganised.setText(data[position].unorganised!!.uid)
        holder.adapterChemistListPreviewBinding.unorganisedAvgSale.setText(data[position].unorgAvgSale.toString())
    }

    override fun getItemCount(): Int {
        return data.size
    }
}