package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.TrafficStreetTypeResponse

class TrafficStreetAdapter(
    var mCallBack: ApnaNewSurveyCallBack,
    var mContext: Context,
    var trafficStreetTypes: ArrayList<TrafficStreetTypeResponse.Data.ListData.Row>,
) : RecyclerView.Adapter<TrafficStreetAdapter.ViewHolder>() {

//    private var filteredTrafficStreetData: ArrayList<TrafficStreetTypeResponse.Data.ListData.Row> =
//        trafficStreetTypes

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
        holder.viewItemRowBinding.itemName.text = trafficStreetTypes[position].name

        holder.viewItemRowBinding.itemName.setOnClickListener {
            mCallBack.onTrafficStreetItemSelect(position,
                trafficStreetTypes[position].uid.toString())
        }
    }

    override fun getItemCount(): Int {
        return trafficStreetTypes.size
    }

    fun filter(filteredList: ArrayList<TrafficStreetTypeResponse.Data.ListData.Row>) {

        this.trafficStreetTypes = filteredList
        notifyDataSetChanged()

        /*if (searchText.isEmpty()) {
            filteredTrafficStreetData = trafficStreetTypes
        } else {
            for (i in trafficStreetTypes.indices) {
                if (trafficStreetTypes[i].name!!.contains(searchText, true)) {
                    filteredTrafficStreetData.add(trafficStreetTypes[i])
                }
            }
            notifyDataSetChanged()
        }
        notifyDataSetChanged()*/
    }
}