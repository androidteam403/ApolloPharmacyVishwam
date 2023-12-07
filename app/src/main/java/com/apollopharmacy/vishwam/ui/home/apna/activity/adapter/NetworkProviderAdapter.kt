package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewItemRowBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.NetworkProvidersResponse

class NetworkProviderAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var networkProviders: ArrayList<NetworkProvidersResponse.Data.ListData.Row>,
) : RecyclerView.Adapter<NetworkProviderAdapter.ViewHolder>() {
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
        holder.viewItemRowBinding.itemName.text = networkProviders.get(position).value
        if (networkProviders[position].isSelected == true) {
            holder.viewItemRowBinding.itemName.setTextColor(Color.parseColor("#00a651"))
        } else if (networkProviders[position].isSelected == false) {
            holder.viewItemRowBinding.itemName.setTextColor(ContextCompat.getColor(mContext,
                R.color.black))
        }
        holder.viewItemRowBinding.itemName.setOnClickListener {
            /*if (networkProviders[position].isSelected == true) {
                holder.viewItemRowBinding.itemName.setTextColor(ContextCompat.getColor(mContext,
                    R.color.black))
                networkProviders[position].isSelected = false
            } else {
                holder.viewItemRowBinding.itemName.setTextColor(Color.parseColor("#00a651"))
                networkProviders[position].isSelected = true
            }*/
            mCallback.onNetworkProviderSelect(position,
                networkProviders[position],
                networkProviders[position].isSelected)
        }
    }

    override fun getItemCount(): Int {
        return networkProviders.size
    }

    fun filter(filteredList: ArrayList<NetworkProvidersResponse.Data.ListData.Row>) {
        this.networkProviders = filteredList
        notifyDataSetChanged()
    }
}