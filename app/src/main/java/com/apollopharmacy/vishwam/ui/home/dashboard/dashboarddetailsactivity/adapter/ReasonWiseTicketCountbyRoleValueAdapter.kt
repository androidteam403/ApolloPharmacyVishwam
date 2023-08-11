package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterReasonWiseTicketCountbyRoleValueBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.model.ReasonTicketCountbyRoleCategoriesModel

class ReasonWiseTicketCountbyRoleValueAdapter(
    var rows: List<ReasonTicketCountbyRoleCategoriesModel>, val categoryName: String, val context: Context, val isSelected: Boolean,
) : RecyclerView.Adapter<ReasonWiseTicketCountbyRoleValueAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReasonWiseTicketCountbyRoleValueAdapter.ViewHolder {
        val adapterReasonWiseTicketCountbyRoleValueBinding =
            DataBindingUtil.inflate<AdapterReasonWiseTicketCountbyRoleValueBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_reason_wise_ticket_countby_role_value,
                parent,
                false
            )
        return ReasonWiseTicketCountbyRoleValueAdapter.ViewHolder(
            adapterReasonWiseTicketCountbyRoleValueBinding
        )
    }

    override fun onBindViewHolder(
        holder: ReasonWiseTicketCountbyRoleValueAdapter.ViewHolder,
        position: Int,
    ) {
        val row = rows.get(position)

        if (categoryName.equals("Name")) {
            if (isSelected) {
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(
                    context.resources.getColor(R.color.green)
                )
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.names.setTextColor(
                    context.getColor(
                        R.color.white
                    )
                )
            } else {
                if (position % 2 == 0) {
                    holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(
                        context.resources.getColor(R.color.lite_grey_ceo_dashboard)
                    )
                } else {
                    holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(
                        Color.TRANSPARENT
                    )
                }
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.names.setTextColor(
                    context.getColor(
                        R.color.black
                    )
                )
            }

            holder.adapterReasonWiseTicketCountbyRoleValueBinding.bgWhite.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.names.visibility = View.VISIBLE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.total.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.category.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.names.text = row.row
        } else if (categoryName.equals("Total")) {
            if (isSelected) {
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(
                    context.resources.getColor(R.color.green)
                )
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.totalText.setTextColor(
                    context.getColor(
                        R.color.white
                    )
                )
            } else {
                if (position % 2 == 0) {
                    holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(
                        context.resources.getColor(R.color.lite_grey_ceo_dashboard)
                    )
                } else {
                    holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(
                        Color.TRANSPARENT
                    )
                }
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.totalText.setTextColor(
                    context.getColor(
                        R.color.white
                    )
                )
            }
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.bgWhite.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.names.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.total.visibility = View.VISIBLE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.category.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.totalText.text = row.row
        } else {
            if (isSelected) {
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(Color.parseColor("#4CAF50"))
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.category.setTextColor(
                    context.getColor(
                        R.color.white
                    )
                )
            } else {
                if (position % 2 == 0) {
                    holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(
                        context.resources.getColor(R.color.dark_grey_ceo_dashboard)
                    )
                } else {
                    holder.adapterReasonWiseTicketCountbyRoleValueBinding.reasonWiseTicketCountByRoleRecyclerviewValueParentLayout.setBackgroundColor(
                        context.resources.getColor(R.color.lite_grey_ceo_dashboard)
                    )
                }
                holder.adapterReasonWiseTicketCountbyRoleValueBinding.category.setTextColor(
                    context.getColor(
                        R.color.black
                    )
                )
            }
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.bgWhite.visibility = View.VISIBLE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.names.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.total.visibility = View.GONE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.category.visibility = View.VISIBLE
            holder.adapterReasonWiseTicketCountbyRoleValueBinding.category.text = row.row
        }
    }

    override fun getItemCount(): Int {
        return rows.size
    }

    class ViewHolder(val adapterReasonWiseTicketCountbyRoleValueBinding: AdapterReasonWiseTicketCountbyRoleValueBinding) :
        RecyclerView.ViewHolder(adapterReasonWiseTicketCountbyRoleValueBinding.root) {

    }

}