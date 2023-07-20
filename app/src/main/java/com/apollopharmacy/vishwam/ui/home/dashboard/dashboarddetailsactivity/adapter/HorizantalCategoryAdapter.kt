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
                false
            )
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


        //https://pharmteststorage.blob.core.windows.net/test/vendor/SENSING/1689831004658.jpg?sv=2022-11-02&se=9999-12-31T23:59:59Z&sr=b&sp=r&sig=oggBsfDrGAbdYs6e0jHStYvzA0m6iFT5ws2XdnqKZ9I%3D
//https://pharmteststorage.blob.core.windows.net/test/vendor/SENSING/1689831008735.jpg?sv=2022-11-02&se=9999-12-31T23:59:59Z&sr=b&sp=r&sig=KV%2FER5zxHDJ6lux3UeqvM2RWJTDxZU59eupFOk8XbZo%3D

//        categoryList.get(0).javaClass.declaredFields[3].name
//        categoryList.get(0).javaClass.declaredFields.get(0).annotations
        for (i in headerList) {
            if (i.name.equals(categoryList[position].acessoriesOnlineSales.javaClass.annotations)) {
//                val fields: Field = Field::class.java.getDeclaredField("name")
//                val sName = fields.getAnnotation(SerializedName::class.java)
//                println(sName.value)
                holder.adapterHorizantalCategoryBinding.count.text =
                    categoryList[position].toString()
            }
        }

    }


    override fun getItemCount(): Int {
        return headerList.size
    }

    class ViewHolder(var adapterHorizantalCategoryBinding: AdapterHorizantalCategoryBinding) :
        RecyclerView.ViewHolder(adapterHorizantalCategoryBinding.root) {}
}