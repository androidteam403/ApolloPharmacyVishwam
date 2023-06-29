package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.StateListResponse

class StateItemAdapter(
    var mCallback: ApnaNewSurveyCallBack,
    var mContext: Context,
    var stateList: ArrayList<StateListResponse.Data.ListData.Row>,
) : RecyclerView.Adapter<StateItemAdapter.ViewHolder>() {

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
        holder.viewItemRowBinding.itemName.text = stateList[position].name

        holder.viewItemRowBinding.itemName.setOnClickListener {
            mCallback.onSelectState(position,
                stateList[position].name.toString(),
                stateList[position].uid.toString())
        }
    }

    override fun getItemCount(): Int {
        return stateList.size
    }

    fun filter(filteredList: ArrayList<StateListResponse.Data.ListData.Row>) {
        this.stateList = filteredList
        notifyDataSetChanged()
    }
}