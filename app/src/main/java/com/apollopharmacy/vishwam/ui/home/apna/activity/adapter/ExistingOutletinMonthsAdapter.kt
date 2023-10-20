package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack

class ExistingOutletinMonthsAdapter(
    var mContext: Context,
    var mCallBack: ApnaNewSurveyCallBack,
    var monthsList: ArrayList<String>,
) : RecyclerView.Adapter<ExistingOutletinMonthsAdapter.ViewHolder>() {

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
        holder.viewItemRowBinding.itemName.setText(monthsList.get(position))
        holder.viewItemRowBinding.itemName.setOnClickListener {
            if (mCallBack != null) mCallBack.onSelectedExistingOutletinMonth(monthsList.get(position))
        }
    }

    override fun getItemCount(): Int {
        return monthsList.size
    }

    fun filter(filteredList: ArrayList<String>) {
        monthsList = filteredList
        notifyDataSetChanged()
    }
}