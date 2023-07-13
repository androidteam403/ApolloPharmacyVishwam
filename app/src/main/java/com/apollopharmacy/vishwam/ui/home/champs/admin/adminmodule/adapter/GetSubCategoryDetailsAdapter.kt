package com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.AdapterGetCategoryDetailsBinding
import com.apollopharmacy.vishwam.databinding.AdapterGetSubCategoryDetailsBinding
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.AdminModuleCallBack
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.model.GetSubCategoryDetailsResponse

class GetSubCategoryDetailsAdapter(
    var subCategoryDetailsList: List<GetSubCategoryDetailsResponse.SubCategoryDetails>?,
    var mContext: Context?,
    var mCallback: AdminModuleCallBack?,
    var categoryPosition: Int,
    var categoryRating: String?,
    var adapter: GetCategoryDetailsAdapter,
    var adapterGetCategoryDetailsBinding: AdapterGetCategoryDetailsBinding
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

        holder.adapterGetSubCategoryDetailsBinding.subCatgoryRatingRangeEdittext.addTextChangedListener( object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(!s!!.isEmpty()){
                    var sumOfRange: Double = 0.0
                    for(i in subCategoryDetailsList!!.indices){
                        if(!subCategoryDetailsList!!.get(i).subCategoryName!!.equals(getCategoryDetails.subCategoryName)){
                            var indiviualRange: Double= subCategoryDetailsList!!.get(i).rating!!.toDouble()
                            sumOfRange = indiviualRange + sumOfRange
                        }

                    }
                    sumOfRange= sumOfRange + (s.toString()).toDouble()
//                    if(sumOfRange<=((categoryRating)!!.toDouble())){
                        getCategoryDetails.rating=s.toString()


                        adapter.sumOfSubCategoriesRatings(categoryPosition,
                            subCategoryDetailsList!!,adapterGetCategoryDetailsBinding )
//                        mCallback!!.updateSumOfSubCat()
//                    }else{
//                        Toast.makeText(context,
//                            "Please enter a value with in the range",
//                            Toast.LENGTH_SHORT).show()
//                       holder.adapterGetSubCategoryDetailsBinding.subCatgoryRatingRangeEdittext.setText("")
//                    }
                }


            }

        })
    }

    override fun getItemCount(): Int {
        return subCategoryDetailsList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(val adapterGetSubCategoryDetailsBinding: AdapterGetSubCategoryDetailsBinding) :
        RecyclerView.ViewHolder(adapterGetSubCategoryDetailsBinding.root) {

    }
}