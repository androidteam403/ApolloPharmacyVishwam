package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.TrafficAdapterLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class TrafficGeneratorPreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.TrafficGenerator>,
) : RecyclerView.Adapter<TrafficGeneratorPreviewAdapter.ViewHolder>() {

    class ViewHolder(val trafficAdapterLayoutBinding: TrafficAdapterLayoutBinding) :
        RecyclerView.ViewHolder(trafficAdapterLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val trafficAdapterLayoutBinding = DataBindingUtil.inflate<TrafficAdapterLayoutBinding>(
            LayoutInflater.from(mContext),
            R.layout.traffic_adapter_layout,
            parent,
            false
        )
        return ViewHolder(trafficAdapterLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.trafficAdapterLayoutBinding.name.setText(data[position].name)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}