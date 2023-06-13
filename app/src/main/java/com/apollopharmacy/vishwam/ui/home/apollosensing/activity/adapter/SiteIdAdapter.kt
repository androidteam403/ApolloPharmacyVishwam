package com.apollopharmacy.vishwam.ui.home.apollosensing.activity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.AdapterSiteIdBinding
import com.apollopharmacy.vishwam.ui.home.apollosensing.activity.ApolloSensingStoreCallback

class SiteIdAdapter(
    var mContext: Context,
    var siteData: ArrayList<StoreListItem>,
    var mCallback: ApolloSensingStoreCallback,
) : RecyclerView.Adapter<SiteIdAdapter.ViewHolder>() {

    class ViewHolder(val adapterSiteIdBinding: AdapterSiteIdBinding) :
        RecyclerView.ViewHolder(adapterSiteIdBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterSiteIdBinding = DataBindingUtil.inflate<AdapterSiteIdBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_site_id,
            parent,
            false
        )
        return ViewHolder(adapterSiteIdBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = siteData.get(position)
        holder.adapterSiteIdBinding.itemName.setText("${item.site},${item.store_name}")
        holder.adapterSiteIdBinding.itemName.setOnClickListener {
            mCallback.onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return siteData.size
    }

    val storeArrayList: ArrayList<StoreListItem>? = siteData
    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {

                    siteData = storeArrayList!!
                } else {
                    var filteredList = ArrayList<StoreListItem>()
                    for (row in storeArrayList!!) {
                        if (row.site?.contains(charString)!! || row.store_name?.contains(charString.toUpperCase())!!
                        ) {
                            filteredList.add(row)
                        }
                    }
                    siteData = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = siteData
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                if (siteData != null && !siteData.isEmpty()) {
                    siteData =
                        filterResults.values as ArrayList<StoreListItem>
                    try {
                        mCallback.noOrdersFound(siteData.size)
                        notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.e("SiteIdAdapter", e.message!!)
                    }
                } else {
                    mCallback.noOrdersFound(0)
                    notifyDataSetChanged()
                }
            }
        }
    }
}