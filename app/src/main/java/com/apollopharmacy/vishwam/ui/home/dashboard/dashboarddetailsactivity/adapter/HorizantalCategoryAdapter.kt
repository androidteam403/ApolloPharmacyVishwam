package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterCategoryDashboardBinding
import com.apollopharmacy.vishwam.databinding.AdapterDetailsDashboardBinding
import com.apollopharmacy.vishwam.databinding.AdapterHorizantalCategoryBinding

class HorizantalCategoryAdapter(private var context: Context) : RecyclerView.Adapter<HorizantalCategoryAdapter.ViewHolder>() {
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

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: HorizantalCategoryAdapter.ViewHolder, position: Int) {

            if (position % 2 == 0) {
                holder.adapterHorizantalCategoryBinding.backgroundForLinear.background =
                    (context.getDrawable(R.drawable.background_for_qc_dashboard))
                holder.adapterHorizantalCategoryBinding.middleColor.background=context.getDrawable(R.color.light_ashh)
            } else {
                holder.adapterHorizantalCategoryBinding.backgroundForLinear.background=context.getDrawable(R.color.light_ashh)
                holder.adapterHorizantalCategoryBinding.middleColor.background =
                    (context.getDrawable(R.drawable.background_for_white))
            }


    }
    class ViewHolder(var adapterHorizantalCategoryBinding: AdapterHorizantalCategoryBinding) :
        RecyclerView.ViewHolder(adapterHorizantalCategoryBinding.root) {}
}