package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterPreviewSubCategoryBinding
import com.apollopharmacy.vishwam.databinding.AdapterSubCategoryBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarCallBack
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.PreviewActivityCallback
import com.apollopharmacy.vishwam.ui.home.model.GetSubCategoryDetailsModelResponse
import kotlin.math.roundToInt

class SubCategoryPreviewAdapter(
    private var subCategoryDetails: List<GetSubCategoryDetailsModelResponse.SubCategoryDetail>,
    private var applicationContext: Context,
    private var previewActivityCallback: PreviewActivityCallback
) : RecyclerView.Adapter<SubCategoryPreviewAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SubCategoryPreviewAdapter.ViewHolder {
        val adapterPreviewSubCategoryBinding: AdapterPreviewSubCategoryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_preview_sub_category,
                parent,
                false
            )
        return ViewHolder(adapterPreviewSubCategoryBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val subCategory = subCategoryDetails.get(position)
        holder.adapterPreviewSubCategoryBinding.subCategoryHeading.text = subCategory.subCategoryName
        holder.adapterPreviewSubCategoryBinding.seekbar1.max=
            ((subCategoryDetails.get(position).rating)!!.toFloat()*2).toInt()
        holder.adapterPreviewSubCategoryBinding.seekbar1.isEnabled=false

        if(subCategory.givenRating!=null){
            var value = (subCategory.givenRating)!!.toFloat()*2
            holder.adapterPreviewSubCategoryBinding.seekbar1.progress=(value.roundToInt())
        }

    }
    private fun getConvertedValue(intVal: Float): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
    override fun getItemCount(): Int {
        return subCategoryDetails.size
    }

    class ViewHolder(val  adapterPreviewSubCategoryBinding: AdapterPreviewSubCategoryBinding):
        RecyclerView.ViewHolder(adapterPreviewSubCategoryBinding.root)
}