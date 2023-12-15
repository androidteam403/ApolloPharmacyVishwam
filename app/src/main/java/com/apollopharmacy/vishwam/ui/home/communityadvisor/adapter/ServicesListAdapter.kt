package com.apollopharmacy.vishwam.ui.home.communityadvisor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterServicesListBinding
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.ServicesList

class ServicesListAdapter(var serviceList: List<ServicesList>) :
    RecyclerView.Adapter<ServicesListAdapter.ViewHolder>() {
  /*  fun updatedServiceList(newList: List<ServicesList>) {
        serviceList = newList
        notifyDataSetChanged()
    }*/

    /*  fun servicesData(newServiceList: List<ServicesList>) {
          serviceList = newServiceList
          notifyDataSetChanged()
      }
      companion object {
          @BindingAdapter("android:clickable")
          @JvmStatic
          fun setClickable(textView: TextView, clickable: () -> Unit) {
              textView.setOnClickListener { clickable.invoke() }
          }
      }*/

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
        val currentItem = serviceList.get(position)
        holder.adapterServicesListBinding.idServiceValue.text =
            currentItem.servicesCustomerInteractionId
        holder.adapterServicesListBinding.customerServices.text =
            currentItem.servicesCustomerInteractionName
        holder.itemView.setOnClickListener {
            //  mClickListener.onClickServicesCommunityAdvisor(position,currentItem)
        }

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class ViewHolder(val adapterServicesListBinding: AdapterServicesListBinding) :
        RecyclerView.ViewHolder(adapterServicesListBinding.root)


}