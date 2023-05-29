package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApartmentAdapterLayoutBinding
import com.apollopharmacy.vishwam.databinding.HospitalAdapterLayoutBinding
import com.apollopharmacy.vishwam.databinding.TrafficAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList

class PreviewTrafficAdapter(
    val mContext: Context,
    private val trafficGenerators: ArrayList<SurveyDetailsList.TrafficGenerator>,

    ) : RecyclerView.Adapter<PreviewTrafficAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val apartmentAdapterLayoutBinding: TrafficAdapterLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.traffic_adapter_layout,
            parent,
            false
        )
        return ViewHolder(apartmentAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items=trafficGenerators.get(position)
        holder.apartmentAdapterLayoutBinding.name.setText(items.name)


    }

    override fun getItemCount(): Int {
        return trafficGenerators.size
    }

    class ViewHolder(val apartmentAdapterLayoutBinding: TrafficAdapterLayoutBinding) :
        RecyclerView.ViewHolder(apartmentAdapterLayoutBinding.root)
}