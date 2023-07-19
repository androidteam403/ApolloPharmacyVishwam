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

        if (position % 2 == 0) {
            holder.adapterHorizantalCategoryBinding.backgroundForLinear.background =
                (context.getDrawable(R.drawable.background_for_champs_names))
//            holder.adapterHorizantalCategoryBinding.middleColor.background =
//                context.getDrawable(R.color.light_ashh)
        } else {
            holder.adapterHorizantalCategoryBinding.backgroundForLinear.background =
                (context.getDrawable(R.drawable.background_for_champs_names))
//            holder.adapterHorizantalCategoryBinding.middleColor.background =
//                (context.getDrawable(R.drawable.background_for_white))
        }
//        var categoryLists = categoryList.get(position)
        for(i in headerList){
            if(i.name.equals(categoryList[position].acessoriesOnlineSales.javaClass.annotations)){
//                val fields: Field = Field::class.java.getDeclaredField("name")
//                val sName = fields.getAnnotation(SerializedName::class.java)
//                println(sName.value)
                holder.adapterHorizantalCategoryBinding.count.text = categoryList[position].toString()
            }
        }

    }


    override fun getItemCount(): Int {
        return headerList.size
    }

    class ViewHolder(var adapterHorizantalCategoryBinding: AdapterHorizantalCategoryBinding) :
        RecyclerView.ViewHolder(adapterHorizantalCategoryBinding.root) {}
}