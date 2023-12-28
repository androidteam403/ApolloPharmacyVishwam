package com.apollopharmacy.vishwam.ui.home.communityadvisor.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterServicesListBinding
import com.apollopharmacy.vishwam.ui.home.communityadvisor.CommunityAdvisorFragmentCallback
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServiceDetailsResponse

class ServicesListAdapter(
    var mClickListener: CommunityAdvisorFragmentCallback,
    var servicesList: ArrayList<HomeServiceDetailsResponse.Detlist>,
) :
    RecyclerView.Adapter<ServicesListAdapter.ViewHolder>() {
    lateinit var intent: Intent
    var isServicesTab = true
    private var filteredList: List<HomeServiceDetailsResponse.Detlist> = servicesList
    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().toLowerCase().trim()
                val results = FilterResults()

                if (query.isEmpty()) {
                    results.values = servicesList
                } else {
                    val filtered = servicesList.filter { item ->
                        item.uniqueId!!.contains(query, ignoreCase = true) ||
                                item.customerName!!.contains(query, ignoreCase = true) ||
                                item.customerMobileno!!.contains(query, ignoreCase = true)
                    }
                    results.values = filtered
                }

                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<HomeServiceDetailsResponse.Detlist>
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(query: String) {
        val filteredList = servicesList.filter { item ->
            item.uniqueId!!.contains(query, ignoreCase = true) ||
                    item.customerName!!.contains(query, ignoreCase = true) ||
                    item.customerMobileno!!.contains(query, ignoreCase = true)
        }
        this.filteredList = filteredList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterServicesListBinding: AdapterServicesListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.adapter_services_list, parent, false
        )
        return ViewHolder(adapterServicesListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filteredList.get(position)
        if (currentItem.serviceType!!.equals("CUSTOMER")) {
            holder.adapterServicesListBinding.idServiceValue.text =
                currentItem.uniqueId
            holder.adapterServicesListBinding.customerServices.text =
                currentItem.serviceDate
        } else if (currentItem.serviceType!!.equals("SERVICE")) {
            holder.adapterServicesListBinding.idServiceValue.text =
                currentItem.uniqueId
            holder.adapterServicesListBinding.customerServices.text =
                currentItem.serviceDate

        }
        holder.itemView.setOnClickListener {
            mClickListener.onClickServicesItems(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class ViewHolder(val adapterServicesListBinding: AdapterServicesListBinding) :
        RecyclerView.ViewHolder(adapterServicesListBinding.root)
}