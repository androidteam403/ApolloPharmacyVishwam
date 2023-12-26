package com.apollopharmacy.vishwam.ui.home.communityadvisor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterServicesCustomerResponseBinding
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.GetServicesCustomerResponse
import com.apollopharmacy.vishwam.ui.home.communityadvisor.servicescustomerinteraction.ServicesCustomerCallback

class ServicesCustomerResponseAdapter(
    var context: Context,
    var servicesCustomerReqList: ArrayList<GetServicesCustomerResponse.ListServices>,
    var servicesCustomerCallback: ServicesCustomerCallback,
) :
    RecyclerView.Adapter<ServicesCustomerResponseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val adapterServicesCustomerResponseBinding: AdapterServicesCustomerResponseBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_services_customer_response,
                parent,
                false
            )
        return ViewHolder(adapterServicesCustomerResponseBinding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        val servicesCustomer = servicesCustomerReqList.get(position)
        if (servicesCustomer.type.equals("CATEGORY")) {
            holder.adapterServicesCustomerResponseBinding.checkBoxes.text = servicesCustomer.serviceName
            holder.adapterServicesCustomerResponseBinding.checkBoxes.isChecked =
                servicesCustomer.isSelected!!
            holder.adapterServicesCustomerResponseBinding.radioButtons.visibility = View.GONE
            holder.adapterServicesCustomerResponseBinding.checkBoxes.visibility = View.VISIBLE
        } else {
            holder.adapterServicesCustomerResponseBinding.radioButtons.text = servicesCustomer.serviceName
            holder.adapterServicesCustomerResponseBinding.radioButtons.isChecked =
                servicesCustomer.isSelected!!
            holder.adapterServicesCustomerResponseBinding.checkBoxes.visibility = View.GONE
            holder.adapterServicesCustomerResponseBinding.radioButtons.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            servicesCustomerCallback.onItemClick(servicesCustomer)
        }


        // if (isServicesTab){
//            holder.adapterServicesCustomerResponseBinding.checkBoxes.text= servicesCustomer.serviceName
        // val intent:Intent?=null
        //if (intent != null) {
        //   var services = intent.putExtra("SERVICES_ID", servicesCustomer.serviceId)
        //}
        //  holder.adapterServicesCustomerResponseBinding.checkBoxes.visibility=View.VISIBLE
        //holder.adapterServicesCustomerResponseBinding.radioButtons.visibility=View.GONE
        //}else {
        //  holder.adapterServicesCustomerResponseBinding.radioButtons.text=servicesCustomer.serviceName
        //    holder.adapterServicesCustomerResponseBinding.radioButtons.visibility=View.VISIBLE
        //  holder.adapterServicesCustomerResponseBinding.checkBoxes.visibility=View.GONE
        //}
    }

    override fun getItemCount(): Int {
        return servicesCustomerReqList.size
    }

    class ViewHolder(val adapterServicesCustomerResponseBinding: AdapterServicesCustomerResponseBinding) :
        RecyclerView.ViewHolder(adapterServicesCustomerResponseBinding.root)
}