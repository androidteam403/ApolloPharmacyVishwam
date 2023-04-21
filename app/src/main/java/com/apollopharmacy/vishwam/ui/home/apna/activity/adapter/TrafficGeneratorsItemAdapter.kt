package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.TrafficGeneratorsLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack

class TrafficGeneratorsItemAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var items: ArrayList<String>,
): RecyclerView.Adapter<TrafficGeneratorsItemAdapter.ViewHolder>() {

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
        holder.trafficGeneratorsLayoutBinding.trafficGeneratorsItem.text = items[position]

        holder.trafficGeneratorsLayoutBinding.deleteItem.setOnClickListener {
            mCallback.onClickTrafficGeneratorItemDelete(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}