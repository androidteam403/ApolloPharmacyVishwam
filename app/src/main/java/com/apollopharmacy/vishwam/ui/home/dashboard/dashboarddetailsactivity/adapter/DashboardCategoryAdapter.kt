package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterCategoryDashboardBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.DashboardDetailsCallback
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.model.ReasonWiseTicketCountbyRoleResponse
import com.google.gson.JsonObject

class DashboardCategoryAdapter(
    val context: Context,
    var categoryList: ArrayList<ReasonWiseTicketCountbyRoleResponse.Data1>,
    val callback: DashboardDetailsCallback,
    val rowsList: ArrayList<JsonObject>,
    val role: String,
) : RecyclerView.Adapter<DashboardCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DashboardCategoryAdapter.ViewHolder {
        val adapterCategoryDashboardBinding =
            DataBindingUtil.inflate<AdapterCategoryDashboardBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_category_dashboard,
                parent,
                false
            )
        return ViewHolder(adapterCategoryDashboardBinding)
    }

    override fun onBindViewHolder(holder: DashboardCategoryAdapter.ViewHolder, position: Int) {
        /* if (position == 0 || position == 1) {
             holder.itemView.visibility = View.GONE
         } else {*/
//            holder.itemView.visibility = View.VISIBLE
        val category = categoryList.get(position)
        if (category.isSelsected == true) {
            holder.adapterCategoryDashboardBinding.parentLayout.setBackgroundResource(R.drawable.dashboard_grid_category_bg)
            holder.adapterCategoryDashboardBinding.category.setTextColor(context.getColor(R.color.white))
            holder.adapterCategoryDashboardBinding.arrowToindicate.visibility = View.VISIBLE
            if (category.isDescending == true) {
                holder.adapterCategoryDashboardBinding.arrowToindicate.rotation = 180f
            } else {
                holder.adapterCategoryDashboardBinding.arrowToindicate.rotation = 0f
            }
        } else {
            holder.adapterCategoryDashboardBinding.parentLayout.setBackgroundResource(0)
            holder.adapterCategoryDashboardBinding.category.setTextColor(context.getColor(R.color.black))
            holder.adapterCategoryDashboardBinding.arrowToindicate.visibility = View.GONE
        }
        /* if (position == 0) {
             var rows = ArrayList<String>()
             for (i in rowsList) {
                 rows.add(i.get("name").toString())
             }
             if (role.equals("ceo")) {
                 holder.adapterCategoryDashboardBinding.category.text =
                     "Regional Head (${rows.size})"
             } else if (role.equals("regional_head")) {
                 holder.adapterCategoryDashboardBinding.category.text = "Manager (${rows.size})"
             } else if (role.equals("store_manager")) {
                 holder.adapterCategoryDashboardBinding.category.text =
                     "Executives (${rows.size})"
             } else {
                 holder.adapterCategoryDashboardBinding.category.text = "- (${rows.size})"
             }
         } else if (position == 1) {
             holder.adapterCategoryDashboardBinding.category.text = "MTD"
         } else {*/
        holder.adapterCategoryDashboardBinding.categoryPos.text = "${position + 1}"
        holder.adapterCategoryDashboardBinding.category.text = "${category.name}"
//            }

        holder.itemView.setOnClickListener {
            callback.onClickCategoryItem(category, position, false)
        }
        /* }*/
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class ViewHolder(var adapterCategoryDashboardBinding: AdapterCategoryDashboardBinding) :
        RecyclerView.ViewHolder(adapterCategoryDashboardBinding.root) {}
}