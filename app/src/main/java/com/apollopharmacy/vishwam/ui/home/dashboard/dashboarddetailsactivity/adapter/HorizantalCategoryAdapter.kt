package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterHorizantalCategoryBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.model.ReasonWiseTicketCountByRoleResponse
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Field

class HorizantalCategoryAdapter(
    private var context: Context,
    var categoryList: ArrayList<ReasonWiseTicketCountByRoleResponse.Data.ListData.Row>,
    var headerList: ArrayList<ReasonWiseTicketCountByRoleResponse.Data.ListData.ZcExtra.Data1>,
) :
    RecyclerView.Adapter<HorizantalCategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HorizantalCategoryAdapter.ViewHolder {
        val adapterHorizantalCategoryBinding =
            DataBindingUtil.inflate<AdapterHorizantalCategoryBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_horizantal_category,
                parent,
                false)
        return ViewHolder(adapterHorizantalCategoryBinding)
    }


    override fun onBindViewHolder(holder: HorizantalCategoryAdapter.ViewHolder, position: Int) {
        var name = ""
        holder.adapterHorizantalCategoryBinding.backgroundForLinear.background =
            context.getDrawable(R.color.light_ashh_trans)
        var categoryListForloop= ArrayList<ReasonWiseTicketCountByRoleResponse.Data.ListData.Row>()
        categoryListForloop.add(categoryList.get(position))
//        for(i in categoryListForloop){
//
//            val fields: Field = ReasonWiseTicketCountByRoleResponse.Data.ListData.Row::class.java.getDeclaredField(categoryListForloop.get())
//            val sName = fields.getAnnotationsByType(SerializedName::class.java).get(position).value
//            println(sName)
//            name=sName
//        }
//
//
//        for(i in headerList){
//            if(i.name.equals(name)){
//                holder.adapterHorizantalCategoryBinding.count.text = categoryList[position].toString()
//            }
//        }

    }


    override fun getItemCount(): Int {
        return headerList.size
    }

    class ViewHolder(var adapterHorizantalCategoryBinding: AdapterHorizantalCategoryBinding) :
        RecyclerView.ViewHolder(adapterHorizantalCategoryBinding.root) {}
}