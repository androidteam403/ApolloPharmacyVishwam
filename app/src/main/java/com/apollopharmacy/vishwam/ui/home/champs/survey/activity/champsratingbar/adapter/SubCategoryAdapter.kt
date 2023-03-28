package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
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
) : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SubCategoryAdapter.ViewHolder {
        val adapterSubCategoryAdapterBinding: AdapterSubCategoryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(applicationContext),
                R.layout.adapter_sub_category,
                parent,
                false
            )
        return ViewHolder(adapterSubCategoryAdapterBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val subCategory = subCategoryDetails.get(position)
        holder.adapterSubCategoryAdapterBinding.subCategoryHeading.text =
            subCategory.subCategoryName
        holder.adapterSubCategoryAdapterBinding.seekbar1.max =
            ((subCategoryDetails.get(position).rating)!!.toFloat() * 2).toInt()

        if (subCategory.givenRating != null) {
            var value = (subCategory.givenRating)!!.toFloat() * 2
            holder.adapterSubCategoryAdapterBinding.seekbar1.progress = (value.roundToInt())
        }

        holder.adapterSubCategoryAdapterBinding.seekbar1.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Toast.makeText(applicationContext,
                    "Value: " + getConvertedValue((progress).toFloat()),
                    Toast.LENGTH_SHORT).show()

                holder.adapterSubCategoryAdapterBinding.seekbar1.thumb =
                    getThumb(getConvertedValue(progress.toFloat()))

                champsDetailsandRatingBarCallBack.onClickSeekBar(getConvertedValue((progress).toFloat()),
                    position)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        holder.adapterSubCategoryAdapterBinding.firstRange.setText("0")
        holder.adapterSubCategoryAdapterBinding.fourthRange.setText(subCategory.rating)

    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun getThumb(progress: Float): Drawable {
        var thumb: View = LayoutInflater.from(applicationContext).inflate(
            R.layout.layout_seekbar_thumb,
            null,
            false
        )
        (thumb.findViewById<View>(R.id.progress) as TextView).text =
            progress.toString() + ""
        thumb.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap =
            Bitmap.createBitmap(thumb.measuredWidth, thumb.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        thumb.layout(0, 0, thumb.measuredWidth, thumb.measuredHeight)
        thumb.draw(canvas)

        return BitmapDrawable(Resources.getSystem(), bitmap)
    }

    private fun getConvertedValue(intVal: Float): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }

    override fun getItemCount(): Int {
        return subCategoryDetails.size
    }

    class ViewHolder(val adapterSubCategoryAdapterBinding: AdapterSubCategoryBinding) :
        RecyclerView.ViewHolder(adapterSubCategoryAdapterBinding.root)
}