package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.databinding.AdapterSiteIdListmoduleBinding
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.siteIdselect.SelectSiteIdCallback
import kotlin.collections.ArrayList

class SiteIdAdapter(
    val applicationContext: Context,
    var siteData: ArrayList<StoreListItem>,
    val selectSiteIdCallback: SelectSiteIdCallback,
    val selectsiteIdList: ArrayList<String>,
) :
    RecyclerView.Adapter<SiteIdAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterSiteIdListmoduleBinding: AdapterSiteIdListmoduleBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_site_id_listmodule,
                parent,
                false
            )
        return ViewHolder(adapterSiteIdListmoduleBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = siteData.get(position)
        holder.adapterSiteIdListmoduleBinding.itemName.text = "${items.site}, ${items.store_name}"


        if(siteData.get(position).isSelected!=null &&siteData.get(position).isSelected!!.equals(true)){
            holder.adapterSiteIdListmoduleBinding.tickMark.visibility=View.VISIBLE
        }else if(siteData.get(position).isSelected!=null && siteData.get(position).isSelected!!.equals(false)) {
            holder.adapterSiteIdListmoduleBinding.tickMark.visibility=View.GONE
        }


        holder.itemView.setOnClickListener {
            if(selectsiteIdList.size<=9){

                selectSiteIdCallback.onClickSiteId(items.site, position)

            }else{
                Toast.makeText(applicationContext,
                    "You can select only 10 SiteId's",
                    Toast.LENGTH_SHORT).show()
            }


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
                    for (row in storeArrayList!! ) {
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
//                siteData =
//                    filterResults.values as ArrayList<StoreListItem>
//                notifyDataSetChanged()

                if (siteData != null && !siteData.isEmpty()) {
                    siteData =
                        filterResults.values as ArrayList<StoreListItem>
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


    class ViewHolder(val adapterSiteIdListmoduleBinding: AdapterSiteIdListmoduleBinding) :
        RecyclerView.ViewHolder(adapterSiteIdListmoduleBinding.root)

}