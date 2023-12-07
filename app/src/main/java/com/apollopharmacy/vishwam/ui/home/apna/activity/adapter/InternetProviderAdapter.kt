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
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.InternetProvidersResponse

class InternetProviderAdapter(
    var mContext: Context,
    var mCallback: ApnaNewSurveyCallBack,
    var internetProviders: ArrayList<InternetProvidersResponse.Data.ListData.Row>,
) : RecyclerView.Adapter<InternetProviderAdapter.ViewHolder>() {
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
        holder.viewItemRowBinding.itemName.text = internetProviders[position].value
        if (internetProviders[position].isSelected == true) {
            holder.viewItemRowBinding.itemName.setTextColor(Color.parseColor("#00a651"))
        } else if (internetProviders[position].isSelected == false) {
            holder.viewItemRowBinding.itemName.setTextColor(ContextCompat.getColor(mContext,
                R.color.black))
        }
        holder.viewItemRowBinding.itemName.setOnClickListener {
            /*if (internetProviders[position].isSelected == true) {
                holder.viewItemRowBinding.itemName.setTextColor(ContextCompat.getColor(mContext,
                    R.color.black))
                internetProviders[position].isSelected = false
            } else {
                holder.viewItemRowBinding.itemName.setTextColor(Color.parseColor("#00a651"))
                internetProviders[position].isSelected = true
            }*/
            mCallback.onInternetProviderSelect(position,
                internetProviders[position],
                internetProviders[position].isSelected)
        }
    }

    override fun getItemCount(): Int {
        return internetProviders.size
    }

    fun filter(filteredList: ArrayList<InternetProvidersResponse.Data.ListData.Row>) {
        this.internetProviders = filteredList
        notifyDataSetChanged()
    }
}