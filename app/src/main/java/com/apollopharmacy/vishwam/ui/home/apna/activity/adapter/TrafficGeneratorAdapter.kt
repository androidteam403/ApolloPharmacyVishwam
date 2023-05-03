package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.TrafficGeneratorsResponse

class TrafficGeneratorAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var trafficGenerators: ArrayList<TrafficGeneratorsResponse.Data.ListData.Row>,
): RecyclerView.Adapter<TrafficGeneratorAdapter.ViewHolder>() {

    class ViewHolder(var viewItemRowBinding: ViewItemRowBinding) :
        RecyclerView.ViewHolder(viewItemRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItemRowBinding = DataBindingUtil.inflate<ViewItemRowBinding>(
            LayoutInflater.from(mContext),
            R.layout.view_item_row,
            parent,
            false
        )
        return ViewHolder(viewItemRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewItemRowBinding.itemName.text = trafficGenerators[position].name

        holder.viewItemRowBinding.itemName.setOnClickListener {
            if (trafficGenerators[position].isSelected == true) {
                holder.viewItemRowBinding.itemName.setTextColor(Color.parseColor("#000000"))
                trafficGenerators[position].isSelected = false
            } else {
                holder.viewItemRowBinding.itemName.setTextColor(Color.parseColor("#00a651"))
                trafficGenerators[position].isSelected = true
            }
            mCallback.onTrafficGeneratorItemSelect(position, trafficGenerators[position].name.toString(), trafficGenerators[position].isSelected)
        }
        if (trafficGenerators[position].isSelected == true) {
            holder.viewItemRowBinding.itemName.setTextColor(Color.parseColor("#00a651"))
        } else if (trafficGenerators[position].isSelected == false) {
            holder.viewItemRowBinding.itemName.setTextColor(Color.parseColor("#000000"))
        }
    }
        override fun getItemCount(): Int {
            return trafficGenerators.size
        }

        fun filter(filteredList: ArrayList<TrafficGeneratorsResponse.Data.ListData.Row>) {
            this.trafficGenerators = filteredList
            notifyDataSetChanged()
        }

}