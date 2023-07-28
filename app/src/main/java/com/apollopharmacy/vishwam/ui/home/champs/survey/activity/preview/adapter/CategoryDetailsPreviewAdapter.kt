package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterCategoryDetailsBinding
import com.apollopharmacy.vishwam.databinding.AdapterCategoryPreviewDetailsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter.SubCategoryAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.PreviewActivityCallback
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse

class CategoryDetailsPreviewAdapter(
    private var categoryDetails: List<GetCategoryDetailsModelResponse.CategoryDetail
            >?,
    private var applicationContext: Context,
    private var previewActivityCallback: PreviewActivityCallback,
) : RecyclerView.Adapter<CategoryDetailsPreviewAdapter.ViewHolder>() {

    private var subCategoryPreviewAdapter: SubCategoryPreviewAdapter? = null
    private var recyclerViewVisible = false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CategoryDetailsPreviewAdapter.ViewHolder {
        val adapterCategoryPreviewBinding: AdapterCategoryPreviewDetailsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_category_preview_details,
                parent,
                false
            )
        return ViewHolder(adapterCategoryPreviewBinding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CategoryDetailsPreviewAdapter.ViewHolder, position: Int) {
        val categoryDetailss = categoryDetails!!.get(position)
        holder.adapterCategoryPreviewBinding.categoryName.text = categoryDetailss.categoryName
        holder.adapterCategoryPreviewBinding.categoryNumber.text = categoryDetailss.id.toString()

//
        if (categoryDetailss.sumOfSubCategoryRating != null && categoryDetailss.clickedSubmit!!) {

//            if (categoryDetailss.sumOfSubCategoryRating == 0.0f) {
//                holder.adapterCategoryPreviewBinding.ratingBarVisible.visibility=View.VISIBLE
//                holder.adapterCategoryPreviewBinding.outOfRating.visibility=View.VISIBLE
//                holder.adapterCategoryPreviewBinding.progressBar.visibility=View.VISIBLE
//                holder.adapterCategoryPreviewBinding.progressBar.progress = 0
////                holder.adapterCategoryPreviewBinding.progressBar.setProgressDrawable(applicationContext.resources.getDrawable(R.drawable.progrss_drawable_skyblue))
//                holder.adapterCategoryPreviewBinding.progressBar.max=(categoryDetailss.rating)!!.toInt()
//                holder.adapterCategoryPreviewBinding.outOfRating.text= ((categoryDetailss.sumOfSubCategoryRating)).toString()+ "/" + categoryDetailss.rating
//
//            } else {
            holder.adapterCategoryPreviewBinding.ratingBarVisible.visibility = View.VISIBLE
            holder.adapterCategoryPreviewBinding.outOfRating.visibility = View.VISIBLE
            holder.adapterCategoryPreviewBinding.progressBar.visibility = View.VISIBLE
            holder.adapterCategoryPreviewBinding.progressBar.progress =
                (categoryDetailss.sumOfSubCategoryRating)!!.toInt()
//                holder.adapterCategoryPreviewBinding.progressBar.setProgressDrawable(applicationContext.resources.getDrawable(R.drawable.progress_drawable_green))
            holder.adapterCategoryPreviewBinding.progressBar.max =
                (categoryDetailss.rating)!!.toDouble().toInt()
            if (categoryDetailss.sumOfSubCategoryRating == 0.0f) {
                holder.adapterCategoryPreviewBinding.outOfRating.text =
                    ("0" + "/" + categoryDetailss.rating)

            } else {
                holder.adapterCategoryPreviewBinding.outOfRating.text =
                    ((categoryDetailss.sumOfSubCategoryRating)).toString() + "/" + categoryDetailss.rating

            }

//          }
        } else {
            holder.adapterCategoryPreviewBinding.outOfRating.visibility = View.GONE
            holder.adapterCategoryPreviewBinding.progressBar.visibility = View.GONE
            holder.adapterCategoryPreviewBinding.tickMarkGreen.visibility = View.GONE
//            holder.adapterCategoryPreviewBinding.ratingBarVisible.visibility=View.GONE
            holder.adapterCategoryPreviewBinding.rightArrow.visibility = View.VISIBLE
            holder.adapterCategoryPreviewBinding.categoryName.setTextColor(
                applicationContext.getColor(
                    R.color.black
                )
            )
            holder.adapterCategoryPreviewBinding.categoryIdBg.setBackgroundResource(R.drawable.grey_backgroundup)
            holder.adapterCategoryPreviewBinding.applyBackgroundLayout.setBackgroundResource(R.drawable.background_for_champs_names)

        }
        holder.adapterCategoryPreviewBinding.categoryLayout.setOnClickListener {
            if (!recyclerViewVisible && categoryDetailss.subCategoryDetails != null) {
//                if(categoryDetailss.clickedSubmit!!){
//            holder.adapterCategoryPreviewBinding.ratingBarVisible.visibility=View.VISIBLE
//            holder.adapterCategoryPreviewBinding.outOfRating.visibility=View.VISIBLE
//            holder.adapterCategoryPreviewBinding.progressBar.setProgressDrawable(applicationContext.resources.getDrawable(R.drawable.progress_drawable_green))
//            holder.adapterCategoryPreviewBinding.outOfRating.text= ((categoryDetailss.sumOfSubCategoryRating)).toString()+ "/" + categoryDetailss.rating
//            holder.adapterCategoryPreviewBinding.progressBar.visibility=View.VISIBLE
//            holder.adapterCategoryPreviewBinding.progressBar.progress= (categoryDetailss.sumOfSubCategoryRating)!!.toInt()
//        }
//                else{
//            holder.adapterCategoryPreviewBinding.ratingBarVisible.visibility=View.GONE
//            holder.adapterCategoryPreviewBinding.outOfRating.visibility=View.GONE
//            holder.adapterCategoryPreviewBinding.progressBar.visibility=View.GONE
//        }
///

                holder.adapterCategoryPreviewBinding.rightArrow.rotation = 90f
                holder.adapterCategoryPreviewBinding.categoryRecyclerView.visibility = View.VISIBLE
                recyclerViewVisible = true
                subCategoryPreviewAdapter =
                    SubCategoryPreviewAdapter(
                        categoryDetailss.subCategoryDetails!!,
                        applicationContext,
                        previewActivityCallback
                    )
                holder.adapterCategoryPreviewBinding.categoryRecyclerView.setLayoutManager(
                    LinearLayoutManager(applicationContext)
                )
                holder.adapterCategoryPreviewBinding.categoryRecyclerView.setAdapter(
                    subCategoryPreviewAdapter
                )
            } else {

                holder.adapterCategoryPreviewBinding.rightArrow.rotation = 0f
                holder.adapterCategoryPreviewBinding.categoryRecyclerView.visibility = View.GONE
                recyclerViewVisible = false
            }
        }


    }

    override fun getItemCount(): Int {
        return categoryDetails!!.size
    }

    class ViewHolder(val adapterCategoryPreviewBinding: AdapterCategoryPreviewDetailsBinding) :
        RecyclerView.ViewHolder(adapterCategoryPreviewBinding.root)

}