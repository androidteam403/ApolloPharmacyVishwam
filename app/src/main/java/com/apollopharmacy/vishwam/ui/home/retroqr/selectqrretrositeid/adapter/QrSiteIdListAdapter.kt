package com.apollopharmacy.vishwam.ui.home.retroqr.selectqrretrositeid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.AdapterSwachhSiteidListBinding
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.retroqr.selectqrretrositeid.SelectQrRetroSiteIdCallback
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectChampsSiteIdCallback

class QrSiteIdListAdapter(
    val applicationContext: Context,
    var siteData: ArrayList<StoreDetailsModelResponse.Row>,
    val selectSiteIdCallback: SelectQrRetroSiteIdCallback,
) :
    RecyclerView.Adapter<QrSiteIdListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterSwachhSiteidListBinding: AdapterSwachhSiteidListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_swachh_siteid_list,
                parent,
                false
            )
        return ViewHolder(adapterSwachhSiteidListBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = siteData.get(position)
        holder.adapterSwachhSiteidListBinding.itemName.text = " ${items.site}, ${items.storeName}"


        holder.itemView.setOnClickListener {
            if (selectSiteIdCallback != null) {
                selectSiteIdCallback.onItemClick(siteData.get(position).site!!,
                    siteData.get(position).storeName!!
                )
            }
        }
    }


    override fun getItemCount(): Int {
        return siteData.size
    }

    val storeArrayList: ArrayList<StoreDetailsModelResponse.Row> = siteData
    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {

                    siteData = storeArrayList!!
                } else {
                    var filteredList = ArrayList<StoreDetailsModelResponse.Row>()
                    for (row in storeArrayList!!) {
                        if (row.site?.contains(charString)!! || row.storeName?.contains(charString.toUpperCase())!!
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
//                siteData =
//                    filterResults.values as ArrayList<StoreListItem>
//                notifyDataSetChanged()

                if (siteData != null && !siteData.isEmpty()) {
                    siteData =
                        filterResults.values as ArrayList<StoreDetailsModelResponse.Row>
                    try {
                        selectSiteIdCallback.noOrdersFound(siteData.size)
                        notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.e("FullfilmentAdapter", e.message!!)
                    }
                } else {
                    selectSiteIdCallback.noOrdersFound(0)
                    notifyDataSetChanged()
                }
            }
        }
    }

    class ViewHolder(val adapterSwachhSiteidListBinding: AdapterSwachhSiteidListBinding) :
        RecyclerView.ViewHolder(adapterSwachhSiteidListBinding.root)
}