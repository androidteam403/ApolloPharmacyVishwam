package com.apollopharmacy.vishwam.ui.home.communityadvisor.siteid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterSiteidListCommunityAdvisorBinding
import com.apollopharmacy.vishwam.ui.home.communityadvisor.siteid.SelectCommunityAdvisorSiteIdCallback
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.planogram.siteid.SelectPlanogramSiteIdCallback

class SiteIdListCommunityAdvisorAdapter(
    val applicationContext: Context,
    var siteData: ArrayList<StoreDetailsModelResponse.Row>,
    val selectCommunityAdvisorSiteIdCallback: SelectCommunityAdvisorSiteIdCallback,
) :
    RecyclerView.Adapter<SiteIdListCommunityAdvisorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterSiteIdListCommunityAdvisorSiteidBinding: AdapterSiteidListCommunityAdvisorBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_siteid_list_community_advisor,
                parent,
                false
            )
        return ViewHolder(adapterSiteIdListCommunityAdvisorSiteidBinding)
    }

    override fun getItemCount(): Int {
        return siteData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = siteData.get(position)
        holder.adapterSiteIdListCommunityAdvisorSiteidBinding.itemName.text =
            " ${items.site}, ${items.storeName}"

        holder.itemView.setOnClickListener {
            if (selectCommunityAdvisorSiteIdCallback != null) {
                selectCommunityAdvisorSiteIdCallback.onItemClick(siteData.get(position))

            }
        }
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
                if (siteData != null && !siteData.isEmpty()) {
                    siteData =
                        filterResults.values as ArrayList<StoreDetailsModelResponse.Row>
                    try {
                        selectCommunityAdvisorSiteIdCallback.noOrdersFound(siteData.size)
                        notifyDataSetChanged()
                    } catch (e: Exception) {
                        Log.e("FullfilmentAdapter", e.message!!)
                    }
                } else {
                    selectCommunityAdvisorSiteIdCallback.noOrdersFound(0)
                    notifyDataSetChanged()
                }
            }
        }
    }

    class ViewHolder(val adapterSiteIdListCommunityAdvisorSiteidBinding: AdapterSiteidListCommunityAdvisorBinding) :
        RecyclerView.ViewHolder(adapterSiteIdListCommunityAdvisorSiteidBinding.root)


}