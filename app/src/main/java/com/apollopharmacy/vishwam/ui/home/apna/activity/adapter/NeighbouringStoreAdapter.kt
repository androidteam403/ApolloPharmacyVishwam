package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.LayoutNeighbouringStoreBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.NeighbouringLocationResponse

class NeighbouringStoreAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var neighbouringList: ArrayList<NeighbouringLocationResponse.Data.ListData.Row>,
): RecyclerView.Adapter<NeighbouringStoreAdapter.ViewHolder>() {
    class ViewHolder(val layoutNeighbouringStoreBinding: LayoutNeighbouringStoreBinding) :
        RecyclerView.ViewHolder(layoutNeighbouringStoreBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutNeighbouringStoreBinding = DataBindingUtil.inflate<LayoutNeighbouringStoreBinding>(
            LayoutInflater.from(mContext),
            R.layout.layout_neighbouring_store,
            parent,
            false
        )
        return ViewHolder(layoutNeighbouringStoreBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.layoutNeighbouringStoreBinding.sideText.text = neighbouringList[position].name
    }

    override fun getItemCount(): Int {
        return neighbouringList.size
    }
}