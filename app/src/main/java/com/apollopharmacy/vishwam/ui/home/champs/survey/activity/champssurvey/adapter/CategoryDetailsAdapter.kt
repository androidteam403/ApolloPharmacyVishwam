package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterCategoryDetailsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse

class CategoryDetailsAdapter(
    private var categoryDetails: List<GetCategoryDetailsModelResponse.CategoryDetail>?,
    private var applicationContext: Context,
    private var champsSurveyCallBack: ChampsSurveyCallBack,
    private var status: String?,
) : RecyclerView.Adapter<CategoryDetailsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CategoryDetailsAdapter.ViewHolder {
        val adapterCategoryDetailsBinding: AdapterCategoryDetailsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_category_details,
                parent,
                false
            )
        return ViewHolder(adapterCategoryDetailsBinding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CategoryDetailsAdapter.ViewHolder, position: Int) {
        val categoryDetailss = categoryDetails!!.get(position)
        holder.adapterCategoryDetailsBinding.categoryName.text = categoryDetailss.categoryName
        holder.adapterCategoryDetailsBinding.categoryNumber.text = categoryDetailss.id.toString()
        holder.adapterCategoryDetailsBinding.progressBar.isEnabled=false


        if (categoryDetailss.sumOfSubCategoryRating != null && categoryDetailss.clickedSubmit!! ) {
            holder.adapterCategoryDetailsBinding.ratingBarVisible.visibility = View.VISIBLE
            if (categoryDetailss.sumOfSubCategoryRating == 0.0f) {
                holder.adapterCategoryDetailsBinding.progressBar.progress = 0
                holder.adapterCategoryDetailsBinding.progressBar.setProgressDrawable(applicationContext.resources.getDrawable(R.drawable.progrss_drawable_skyblue))
            } else {
                holder.adapterCategoryDetailsBinding.progressBar.progress =
                    (categoryDetailss.sumOfSubCategoryRating)!!.toInt()
                holder.adapterCategoryDetailsBinding.progressBar.setProgressDrawable(applicationContext.resources.getDrawable(R.drawable.progress_drawable_green))

            }
            holder.adapterCategoryDetailsBinding.progressBar.max =
                (categoryDetailss.rating)!!.toInt()
            holder.adapterCategoryDetailsBinding.outOfRating.text =
                ((categoryDetailss.sumOfSubCategoryRating)).toString() + "/" + categoryDetailss.rating
            holder.adapterCategoryDetailsBinding.applyBackgroundLayout.setBackgroundResource(R.drawable.background_for_champs_green)
            holder.adapterCategoryDetailsBinding.rightArrow.visibility = View.GONE
            holder.adapterCategoryDetailsBinding.categoryName.setTextColor(
                applicationContext.getColor(
                    R.color.white
                )
            )
            holder.adapterCategoryDetailsBinding.tickMarkGreen.visibility = View.VISIBLE
            holder.adapterCategoryDetailsBinding.categoryIdBg.setBackgroundResource(R.drawable.background_green)
        } else {
            holder.adapterCategoryDetailsBinding.tickMarkGreen.visibility = View.GONE
            holder.adapterCategoryDetailsBinding.ratingBarVisible.visibility = View.GONE
            holder.adapterCategoryDetailsBinding.rightArrow.visibility = View.VISIBLE
            holder.adapterCategoryDetailsBinding.categoryName.setTextColor(
                applicationContext.getColor(
                    R.color.black
                )
            )
            holder.adapterCategoryDetailsBinding.categoryIdBg.setBackgroundResource(R.drawable.grey_backgroundup)
            holder.adapterCategoryDetailsBinding.applyBackgroundLayout.setBackgroundResource(R.drawable.background_for_champs_names)

        }
        holder.adapterCategoryDetailsBinding.categoryLayout.setOnClickListener {
            champsSurveyCallBack.onClickCategory(categoryDetailss.categoryName!!, position)
        }

    }

    override fun getItemCount(): Int {
        return categoryDetails!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(val adapterCategoryDetailsBinding: AdapterCategoryDetailsBinding) :
        RecyclerView.ViewHolder(adapterCategoryDetailsBinding.root)

}