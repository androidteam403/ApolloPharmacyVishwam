package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterHorizantalCategoryBinding
import com.apollopharmacy.vishwam.databinding.AdapterHorizantalCategoryHeaderBinding

class HorizantalCategoryHeaderAdapter: RecyclerView.Adapter<HorizantalCategoryHeaderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HorizantalCategoryHeaderAdapter.ViewHolder {
        val adapterHorizantalCategoryBinding =
            DataBindingUtil.inflate<AdapterHorizantalCategoryHeaderBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_horizantal_category_header,
                parent,
                false)
        return HorizantalCategoryHeaderAdapter.ViewHolder(adapterHorizantalCategoryBinding)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: HorizantalCategoryHeaderAdapter.ViewHolder, position: Int) {

    }
    class ViewHolder(var adapterHorizantalCategoryBinding: AdapterHorizantalCategoryHeaderBinding) :
        RecyclerView.ViewHolder(adapterHorizantalCategoryBinding.root) {}
}