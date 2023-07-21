package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterReasonWiseTicketCountbyRoleBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.DashboardDetailsCallback
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.model.ReasonTicketCountbyRoleCategoriesModel
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.model.ReasonWiseTicketCountbyRoleResponse

class ReasonWiseTicketCountbyRoleAdapter(
    val context: Context,
    val dataList: ArrayList<ReasonWiseTicketCountbyRoleResponse.Data1>,
    val reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountbyRoleResponse,
    val callback: DashboardDetailsCallback,
) : RecyclerView.Adapter<ReasonWiseTicketCountbyRoleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReasonWiseTicketCountbyRoleAdapter.ViewHolder {
        val adapterReasonWiseTicketCountbyRoleBinding =
            DataBindingUtil.inflate<AdapterReasonWiseTicketCountbyRoleBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_reason_wise_ticket_countby_role,
                parent,
                false
            )
        return ReasonWiseTicketCountbyRoleAdapter.ViewHolder(
            adapterReasonWiseTicketCountbyRoleBinding
        )
    }

    override fun onBindViewHolder(
        holder: ReasonWiseTicketCountbyRoleAdapter.ViewHolder,
        position: Int,
    ) {
        var rows: List<ReasonTicketCountbyRoleCategoriesModel>? = null

        var data1 = dataList.get(position)
        if (data1.name.equals("Name")) {
            rows = ArrayList<ReasonTicketCountbyRoleCategoriesModel>()
            for (i in reasonWiseTicketCountByRoleResponse.data!!.listData!!.rows!!) {
               var model = ReasonTicketCountbyRoleCategoriesModel()
                model.row = i["name"].asString
                rows.add(model)
            }
            if (data1.isSelsected == true) {
                holder.adapterReasonWiseTicketCountbyRoleBinding.nameParentLayout.setBackgroundResource(
                    R.drawable.dashboard_grid_category_bg
                )
                holder.adapterReasonWiseTicketCountbyRoleBinding.names.setTextColor(
                    context.getColor(
                        R.color.white
                    )
                )
                if (data1.isDescending == true) {
//                    rows = rows.sortedWith(compareByDescending({ it.row }))
                    holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateName.rotation =
                        180f
                } else {
//                    rows = rows.sortedWith(compareBy({ it.row }))
                    holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateName.rotation =
                        0f
                }
            } else {
                holder.adapterReasonWiseTicketCountbyRoleBinding.nameParentLayout.setBackgroundResource(
                    0
                )
                holder.adapterReasonWiseTicketCountbyRoleBinding.names.setTextColor(
                    context.getColor(
                        R.color.black
                    )
                )
            }

            holder.adapterReasonWiseTicketCountbyRoleBinding.names.text =
                "Executives (${rows.size})"
            holder.adapterReasonWiseTicketCountbyRoleBinding.names.visibility = View.VISIBLE
            if (data1.isSelsected == true) {
                holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateName.visibility =
                    View.VISIBLE
            } else {
                holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateName.visibility =
                    View.GONE
            }
            holder.adapterReasonWiseTicketCountbyRoleBinding.nameParentLayout.visibility =
                View.VISIBLE
            holder.adapterReasonWiseTicketCountbyRoleBinding.total.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateTotal.visibility =
                View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.totalParentLayout.visibility =
                View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.category.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateCategory.visibility =
                View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.categoryParentLayout.visibility =
                View.GONE


        } else if (data1.name.equals("Total")) {
            rows = ArrayList<ReasonTicketCountbyRoleCategoriesModel>()
            for (i in reasonWiseTicketCountByRoleResponse.data!!.listData!!.rows!!) {
                var model = ReasonTicketCountbyRoleCategoriesModel()
                model.row = i["Total"].asString
                rows.add(model)
            }
            if (data1.isSelsected == true) {
                holder.adapterReasonWiseTicketCountbyRoleBinding.totalParentLayout.setBackgroundResource(
                    R.drawable.dashboard_grid_category_bg
                )
                holder.adapterReasonWiseTicketCountbyRoleBinding.total.setTextColor(
                    context.getColor(
                        R.color.white
                    )
                )
                if (data1.isDescending == true) {
//                    rows = rows.sortedWith(compareByDescending({ it.row }))
                    holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateTotal.rotation =
                        180f
                } else {
//                    rows = rows.sortedWith(compareBy({ it.row }))
                    holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateTotal.rotation =
                        0f
                }
            } else {
                holder.adapterReasonWiseTicketCountbyRoleBinding.totalParentLayout.setBackgroundResource(
                    0
                )
                holder.adapterReasonWiseTicketCountbyRoleBinding.total.setTextColor(
                    context.getColor(
                        R.color.black
                    )
                )
            }
            holder.adapterReasonWiseTicketCountbyRoleBinding.names.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateName.visibility =
                View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.nameParentLayout.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.total.visibility = View.VISIBLE
            if (data1.isSelsected == true) {
                holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateTotal.visibility =
                    View.VISIBLE
            } else {
                holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateTotal.visibility =
                    View.GONE
            }
            holder.adapterReasonWiseTicketCountbyRoleBinding.totalParentLayout.visibility =
                View.VISIBLE
            holder.adapterReasonWiseTicketCountbyRoleBinding.category.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateCategory.visibility =
                View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.categoryParentLayout.visibility =
                View.GONE
        } else {
            rows = ArrayList<ReasonTicketCountbyRoleCategoriesModel>()
            for (i in reasonWiseTicketCountByRoleResponse.data!!.listData!!.rows!!) {
                var model = ReasonTicketCountbyRoleCategoriesModel()
                model.row = i[data1.name].asString
                rows.add(model)
            }
            if (data1.isSelsected == true) {
                holder.adapterReasonWiseTicketCountbyRoleBinding.categoryParentLayout.setBackgroundResource(
                    R.drawable.dashboard_grid_category_bg
                )
                holder.adapterReasonWiseTicketCountbyRoleBinding.category.setTextColor(
                    context.getColor(
                        R.color.white
                    )
                )
                if (data1.isDescending == true) {
//                    rows = rows.sortedWith(compareByDescending({ it.row }))
                    holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateCategory.rotation =
                        180f
                } else {
//                    rows = rows.sortedWith(compareBy({ it.row }))
                    holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateCategory.rotation =
                        0f
                }
            } else {
                holder.adapterReasonWiseTicketCountbyRoleBinding.categoryParentLayout.setBackgroundResource(
                    0
                )
                holder.adapterReasonWiseTicketCountbyRoleBinding.category.setTextColor(
                    context.getColor(
                        R.color.black
                    )
                )
            }
            holder.adapterReasonWiseTicketCountbyRoleBinding.names.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateName.visibility =
                View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.nameParentLayout.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.total.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateTotal.visibility =
                View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.totalParentLayout.visibility =
                View.GONE
            holder.adapterReasonWiseTicketCountbyRoleBinding.category.visibility = View.VISIBLE
            if (data1.isSelsected == true) {
                holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateCategory.visibility =
                    View.VISIBLE
            } else {
                holder.adapterReasonWiseTicketCountbyRoleBinding.arrowToindicateCategory.visibility =
                    View.GONE
            }
            holder.adapterReasonWiseTicketCountbyRoleBinding.categoryParentLayout.visibility =
                View.VISIBLE
            holder.adapterReasonWiseTicketCountbyRoleBinding.category.text = "${position}"
        }

        var reasonWiseTicketCountbyRoleValueAdapter = ReasonWiseTicketCountbyRoleValueAdapter(
            rows, data1.name!!, context, data1.isSelsected!!
        )
        val layoutManagerReasonWiseTicketCountbyRole =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.adapterReasonWiseTicketCountbyRoleBinding.reasonWiseTicketCountByRoleRecyclerviewValue.layoutManager =
            layoutManagerReasonWiseTicketCountbyRole
        holder.adapterReasonWiseTicketCountbyRoleBinding.reasonWiseTicketCountByRoleRecyclerviewValue.adapter =
            reasonWiseTicketCountbyRoleValueAdapter

        holder.itemView.setOnClickListener {
            callback.onClickCategoryItem(data1)
        }/*holder.adapterReasonWiseTicketCountbyRoleBinding.names.setOnClickListener {
            callback.onClickCategoryItem(data1)
        }
        holder.adapterReasonWiseTicketCountbyRoleBinding.total.setOnClickListener {
            callback.onClickCategoryItem(data1)
        }
        holder.adapterReasonWiseTicketCountbyRoleBinding.category.setOnClickListener {
            callback.onClickCategoryItem(data1)
        }*/
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(val adapterReasonWiseTicketCountbyRoleBinding: AdapterReasonWiseTicketCountbyRoleBinding) :
        RecyclerView.ViewHolder(adapterReasonWiseTicketCountbyRoleBinding.root) {

    }
}