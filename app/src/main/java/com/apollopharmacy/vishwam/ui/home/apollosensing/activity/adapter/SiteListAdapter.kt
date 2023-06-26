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
import com.apollopharmacy.vishwam.databinding.AdapterSiteIdBinding
import com.apollopharmacy.vishwam.ui.home.apollosensing.activity.ApolloSensingStoreCallback
import com.apollopharmacy.vishwam.ui.home.apollosensing.model.SiteListResponse

class SiteListAdapter(
    var mContext: Context,
    var siteListData: ArrayList<SiteListResponse.Data.ListData.Row>,
    var mCallback: ApolloSensingStoreCallback,
) : RecyclerView.Adapter<SiteListAdapter.ViewHolder>() {

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
        val item = siteListData.get(position)
        holder.adapterSiteIdBinding.itemName.setText("${item.site},${item.storeName}")
        holder.adapterSiteIdBinding.itemName.setOnClickListener {
//            mCallback.onItemClick(item)
            mCallback.onSiteListItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return siteListData.size
    }

    val storeArrayList: ArrayList<SiteListResponse.Data.ListData.Row>? = siteListData
    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {

                    siteListData = storeArrayList!!
                } else {
                    var filteredList = ArrayList<SiteListResponse.Data.ListData.Row>()
                    for (row in storeArrayList!!) {
                        if (row.site?.contains(charString)!! || row.storeName?.contains(charString.toUpperCase())!!
                        ) {
                            filteredList.add(row)
                        }
                    }
                    siteListData = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = siteListData
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                if (siteListData != null && !siteListData.isEmpty()) {
                    siteListData =
                        filterResults.values as ArrayList<SiteListResponse.Data.ListData.Row>
                    try {
                        mCallback.noOrdersFound(siteListData.size)
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