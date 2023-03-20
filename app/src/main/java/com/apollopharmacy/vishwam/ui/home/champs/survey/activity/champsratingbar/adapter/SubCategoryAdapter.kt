package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterSubCategoryBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarCallBack
import com.apollopharmacy.vishwam.ui.home.model.GetSubCategoryDetailsModelResponse
import kotlin.math.roundToInt


class SubCategoryAdapter(
    private var subCategoryDetails: List<GetSubCategoryDetailsModelResponse.SubCategoryDetail>,
    private var applicationContext: Context,
    private var champsDetailsandRatingBarCallBack: ChampsDetailsandRatingBarCallBack,
) : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SubCategoryAdapter.ViewHolder {
        val adapterSubCategoryAdapterBinding:AdapterSubCategoryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_sub_category,
                parent,
                false
            )
        return ViewHolder(adapterSubCategoryAdapterBinding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val subCategory = subCategoryDetails.get(position)
        holder.adapterSubCategoryAdapterBinding.subCategoryHeading.text = subCategory.subCategoryName
        holder.adapterSubCategoryAdapterBinding.seekbar1.max=
            ((subCategoryDetails.get(position).rating)!!.toFloat()*2).toInt()


        if(subCategory.givenRating!=null){
            var value = (subCategory.givenRating)!!.toFloat()*2
            holder.adapterSubCategoryAdapterBinding.seekbar1.progress=(value.roundToInt())

            holder.adapterSubCategoryAdapterBinding.firstRange.text="0"
            holder.adapterSubCategoryAdapterBinding.secondRange.text=(subCategory.rating!!.toFloat()/3).toString()
            holder.adapterSubCategoryAdapterBinding.thirdRange.text=(subCategory.rating!!.toFloat()/2).toString()
            holder.adapterSubCategoryAdapterBinding.fourthRange.text=subCategory.rating.toString()

        }

        holder.adapterSubCategoryAdapterBinding.seekbar1.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val width = (seekBar.width
                        - seekBar.paddingLeft
                        - seekBar.paddingRight)
                val thumbPos: Float = width * (seekBar.getProgress() / seekBar.getMax().toFloat())

                holder.adapterSubCategoryAdapterBinding.tvProgress.setTranslationX(thumbPos);
                holder.adapterSubCategoryAdapterBinding.tvProgress.text = getConvertedValue(progress.toFloat()).toString()
//                Toast.makeText(applicationContext,
//                    "Value: " + getConvertedValue((progress).toFloat()),
//                    Toast.LENGTH_SHORT).show()
                champsDetailsandRatingBarCallBack.onClickSeekBar(getConvertedValue((progress).toFloat()),
                    position)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {


                holder.adapterSubCategoryAdapterBinding.tvProgress.visibility= View.VISIBLE
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                holder.adapterSubCategoryAdapterBinding.tvProgress.visibility= View.INVISIBLE

            }
        })
    }



    private fun getConvertedValue(intVal: Float): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
    override fun getItemCount(): Int {
        return subCategoryDetails.size
    }

    class ViewHolder(val adapterSubCategoryAdapterBinding: AdapterSubCategoryBinding):
        RecyclerView.ViewHolder(adapterSubCategoryAdapterBinding.root)
}