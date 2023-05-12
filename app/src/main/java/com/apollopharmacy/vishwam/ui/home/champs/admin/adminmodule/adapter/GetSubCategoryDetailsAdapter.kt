package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterGetSubCategoryDetailsBinding
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.AdminModuleCallBack
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetSubCategoryDetailsResponse

class GetSubCategoryDetailsAdapter(
    var subCategoryDetailsList: List<GetSubCategoryDetailsResponse.SubCategoryDetails>?,
    var mContext: Context?,
    var mCallback: AdminModuleCallBack?,
    var categoryPosition: Int,
) : RecyclerView.Adapter<GetSubCategoryDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GetSubCategoryDetailsAdapter.ViewHolder {
        val adapterGetSubCategoryDetailsBinding =
            DataBindingUtil.inflate<AdapterGetSubCategoryDetailsBinding>(LayoutInflater.from(parent.context),
                R.layout.adapter_get_sub_category_details,
                parent,
                false)
        return GetSubCategoryDetailsAdapter.ViewHolder(adapterGetSubCategoryDetailsBinding)
    }

    override fun onBindViewHolder(holder: GetSubCategoryDetailsAdapter.ViewHolder, position: Int) {
        var getCategoryDetails = subCategoryDetailsList!!.get(position)
        holder.adapterGetSubCategoryDetailsBinding.position = categoryPosition
        holder.adapterGetSubCategoryDetailsBinding.mCallback = mCallback
        holder.adapterGetSubCategoryDetailsBinding.model = getCategoryDetails
        getCategoryDetails.isRatingInDecimal = getCategoryDetails.rating!!.contains(".")
        if (getCategoryDetails.rating!!.contains(".")) {
            holder.adapterGetSubCategoryDetailsBinding.seekbar.max =
                (getCategoryDetails.rating!!.toDouble() + getCategoryDetails.rating!!.toDouble()).toInt()
        } else {
            holder.adapterGetSubCategoryDetailsBinding.seekbar.max =
                getCategoryDetails.rating!!.toInt()
        }
    }

    override fun getItemCount(): Int {
        return subCategoryDetailsList!!.size
    }

    class ViewHolder(val adapterGetSubCategoryDetailsBinding: AdapterGetSubCategoryDetailsBinding) :
        RecyclerView.ViewHolder(adapterGetSubCategoryDetailsBinding.root) {

    }
}