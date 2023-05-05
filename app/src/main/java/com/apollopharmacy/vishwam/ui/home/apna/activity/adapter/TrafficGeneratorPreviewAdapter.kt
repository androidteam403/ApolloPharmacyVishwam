package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.TrafficGeneratorsLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest

class TrafficGeneratorPreviewAdapter(
    var mContext: Context,
    var data: ArrayList<SurveyCreateRequest.TrafficGenerator>,
): RecyclerView.Adapter<TrafficGeneratorPreviewAdapter.ViewHolder>() {

    class ViewHolder(val trafficGeneratorsLayoutBinding: TrafficGeneratorsLayoutBinding) :
        RecyclerView.ViewHolder(trafficGeneratorsLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val trafficGeneratorsLayoutBinding = DataBindingUtil.inflate<TrafficGeneratorsLayoutBinding>(
            LayoutInflater.from(mContext),
            R.layout.traffic_generators_layout,
            parent,
            false
        )
        return ViewHolder(trafficGeneratorsLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.trafficGeneratorsLayoutBinding.name.setText(data[position].uid)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}