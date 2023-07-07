package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterGetCategoryDetailsBinding
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.AdminModuleCallBack
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.adapter.GetCategoryDetailsAdapter.ViewHolder
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetCategoryDetailsResponse
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetSubCategoryDetailsResponse

class GetCategoryDetailsAdapter(
    val categoryDetailsList: List<GetCategoryDetailsResponse.CategoryDetails>?,
    var mContext: Context?,
    var mCallback: AdminModuleCallBack?,
//    var subCategoryDetailsList: List<GetSubCategoryDetailsResponse.SubCategoryDetails>?,
    var categoryPosForUpdate: String?,

    ) : RecyclerView.Adapter<ViewHolder>() {
    var adapterGetCategoryDetailsBindings: AdapterGetCategoryDetailsBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterGetCategoryDetailsBinding =
            DataBindingUtil.inflate<AdapterGetCategoryDetailsBinding>(
                LayoutInflater.from(parent.context),
                R.layout.adapter_get_category_details,
                parent,
                false
            )
        return GetCategoryDetailsAdapter.ViewHolder(adapterGetCategoryDetailsBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterGetCategoryDetailsBindings=holder.adapterGetCategoryDetailsBinding
        var getCategoryDetails = categoryDetailsList!!.get(position)
        if (getCategoryDetails.isItemExpanded!!){
            holder.adapterGetCategoryDetailsBinding.recyclerviewLayout.visibility = View.VISIBLE
        }else{
            holder.adapterGetCategoryDetailsBinding.recyclerviewLayout.visibility = View.GONE

        }
//        holder.adapterGetCategoryDetailsBinding.isItemExpanded = getCategoryDetails.isItemExpanded
//        getCategoryDetails.rating=holder.adapterGetCategoryDetailsBinding.overAllPoints.text.toString()
        holder.adapterGetCategoryDetailsBinding.model = getCategoryDetails
        holder.adapterGetCategoryDetailsBinding.mCallback = mCallback
//        holder.adapterGetCategoryDetailsBinding.sumOfSubCatAdmin=holder.adapterGetCategoryDetailsBinding.overAllPoints.text.toString()
        holder.adapterGetCategoryDetailsBinding.categoryName = getCategoryDetails.categoryName
        holder.adapterGetCategoryDetailsBinding.position = "${position + 1}"
        holder.adapterGetCategoryDetailsBinding.itemPos = position.toString()

        holder.adapterGetCategoryDetailsBinding.overAllPoints.setText(getCategoryDetails.sumOfSubCategoryRating.toString())

        holder.adapterGetCategoryDetailsBinding.sumOfSubCategoryMaxRatings =
            getCategoryDetails.sumOfSubCategoryRating!!.toDouble()
//        if (getCategoryDetails.isItemExpanded!!) {
////            getCategoryDetails.rating=holder.adapterGetCategoryDetailsBinding.overAllPoints.text.toString()
//            holder.adapterGetCategoryDetailsBinding.editTextAdmin.visibility = View.VISIBLE
//        } else {
//            holder.adapterGetCategoryDetailsBinding.editTextAdmin.visibility = View.INVISIBLE
//        }
        var sumOfSubCategoryMaxRating = 0.0

        if (getCategoryDetails.subCategoryDetailsList != null && getCategoryDetails.subCategoryDetailsList!!.size > 0) {
//
//            for (i in getCategoryDetails.subCategoryDetailsList!!) {
//                sumOfSubCategoryMaxRating = sumOfSubCategoryMaxRating + i.rating!!.toDouble()
//            }
//            getCategoryDetails.sumOfSubCategoryRating = sumOfSubCategoryMaxRating
//            if(!categoryPosForUpdate!!.isEmpty() && position.equals(categoryPosForUpdate!!.toInt())){

//           }

            var getSubCategoryDetailsAdapter =
                GetSubCategoryDetailsAdapter(
                    getCategoryDetails.subCategoryDetailsList!!,
                    mContext,
                    mCallback,
                    position,
                    getCategoryDetails.rating,
                    this,
                    holder.adapterGetCategoryDetailsBinding
                )
            var layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            holder.adapterGetCategoryDetailsBinding.getSubCategoryDetailsRecyclerview.layoutManager =
                layoutManager
            holder.adapterGetCategoryDetailsBinding.getSubCategoryDetailsRecyclerview.adapter =
                getSubCategoryDetailsAdapter
        }
//        else{
////            getCategoryDetails.sumOfSubCategoryRating = sumOfSubCategoryMaxRating
//            holder.adapterGetCategoryDetailsBinding.sumOfSubCategoryMaxRatings =
//                getCategoryDetails.sumOfSubCategoryRating
//        }

        holder.adapterGetCategoryDetailsBinding.overAllPoints.addTextChangedListener( object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
            mCallback!!.onValidateTotalSum(categoryDetailsList)
            }
        })
    }

    fun sumOfSubCategoriesRatings(
        position: Int,
        subCategoryDetailsList: List<GetSubCategoryDetailsResponse.SubCategoryDetails>,
        adapterGetCategoryDetailsBinding: AdapterGetCategoryDetailsBinding,
    ) {
        var sumOfSubCategoryMaxRating = 0.0
        if (subCategoryDetailsList != null && subCategoryDetailsList!!.size > 0) {

            for (i in subCategoryDetailsList!!) {
                sumOfSubCategoryMaxRating = sumOfSubCategoryMaxRating + i.rating!!.toDouble()
            }
//            getCategoryDetails.sumOfSubCategoryRating = sumOfSubCategoryMaxRating
//            if(!categoryPosForUpdate!!.isEmpty() && position.equals(categoryPosForUpdate!!.toInt())){
            adapterGetCategoryDetailsBinding.sumOfSubCategoryMaxRatings =
                sumOfSubCategoryMaxRating
            categoryDetailsList!!.get(position).sumOfSubCategoryRating = sumOfSubCategoryMaxRating
//            categoryDetailsList!!.get(position).rating= sumOfSubCategoryMaxRating.toString()
        }
    }

    fun getEditBoxValue(editBoxValue: Double) {
        adapterGetCategoryDetailsBindings!!.sumOfSubCategoryMaxRatings =
            editBoxValue
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return categoryDetailsList!!.size
    }

    class ViewHolder(var adapterGetCategoryDetailsBinding: AdapterGetCategoryDetailsBinding) :
        RecyclerView.ViewHolder(adapterGetCategoryDetailsBinding.root) {}
}