package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.LocationListResponse

class LocationListItemAdapter(
    var mCallback: ApnaNewSurveyCallBack,
    var mContext: Context,
    var locationList: ArrayList<LocationListResponse.Data.ListData.Row>,
): RecyclerView.Adapter<LocationListItemAdapter.ViewHolder>() {

    class ViewHolder(val viewItemRowBinding: ViewItemRowBinding) :
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
        holder.viewItemRowBinding.itemName.text = locationList[position].name

        holder.viewItemRowBinding.itemName.setOnClickListener {
//            mCallback.onLocationListItemSelect(position, locationList[position].name.toString(), locationList[position].uid.toString())
            mCallback.onLocationListItemSelect(
                position,
                locationList[position].name.toString(),
                locationList[position].city!!.state!!.name.toString(),
                locationList[position].city!!.name.toString(),
                locationList[position].uid.toString(),
                locationList[position].city!!.state!!.uid.toString(),
                locationList[position].city!!.uid.toString()
            )
        }
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    fun filter(filteredList: ArrayList<LocationListResponse.Data.ListData.Row>) {
        this.locationList = filteredList
        notifyDataSetChanged()
    }
}