package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.adapter

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
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectChampsSiteIdCallback

class SiteIdListChampsAdapter(
    val applicationContext: Context,
    var siteData: ArrayList<StoreDetailsModelResponse.Row>,
    val selectSiteIdCallback: SelectChampsSiteIdCallback,
) :
    RecyclerView.Adapter<SiteIdListChampsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterSwachhSiteidListBinding: AdapterSwachhSiteidListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_swachh_siteid_list,
                parent,
                false
            )
        return ViewHolder(adapterSwachhSiteidListBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = siteData.get(position)
        holder.adapterSwachhSiteidListBinding.itemName.text = " ${items.site}, ${items.storeName}"

//        if(siteData.get(position).isSelected!=null &&siteData.get(position).isSelected!!.equals(true)){
//            holder.adapterSwachhSiteidListBinding.tickMark.visibility= View.VISIBLE
//        }else if(siteData.get(position).isSelected!=null && siteData.get(position).isSelected!!.equals(false)) {
//            holder.adapterSwachhSiteidListBinding.tickMark.visibility= View.GONE
//        }

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