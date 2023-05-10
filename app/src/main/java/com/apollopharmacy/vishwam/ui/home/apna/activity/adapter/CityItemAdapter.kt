package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.CityListResponse

class CityItemAdapter(
    var mCallBack: ApnaNewSurveyCallBack,
    var mContext: Context,
    var cityList: ArrayList<CityListResponse.Data.ListData.Row>,
): RecyclerView.Adapter<CityItemAdapter.ViewHolder>() {

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
        holder.viewItemRowBinding.itemName.text = cityList[position].name

        holder.viewItemRowBinding.itemName.setOnClickListener {
            mCallBack.onCityItemSelect(position, cityList[position].name.toString(), cityList[position].uid.toString())
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun filter(filteredList: ArrayList<CityListResponse.Data.ListData.Row>) {
        this.cityList = filteredList
        notifyDataSetChanged()
    }
}