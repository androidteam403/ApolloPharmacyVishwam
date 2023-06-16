package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ParkingTypeResponse

class UnOrganisedAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var unorganisedList: ArrayList<ParkingTypeResponse.Data.ListData.Row>,
) : RecyclerView.Adapter<UnOrganisedAdapter.ViewHolder>() {

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
        holder.viewItemRowBinding.itemName.text = unorganisedList[position].name

        holder.viewItemRowBinding.itemName.setOnClickListener {
            mCallback.onUnorganisedItemSelect(position, unorganisedList[position].name.toString())
        }
    }

    override fun getItemCount(): Int {
        return unorganisedList.size
    }

    fun filter(filteredList: ArrayList<ParkingTypeResponse.Data.ListData.Row>) {
        this.unorganisedList = filteredList
        notifyDataSetChanged()
    }
}