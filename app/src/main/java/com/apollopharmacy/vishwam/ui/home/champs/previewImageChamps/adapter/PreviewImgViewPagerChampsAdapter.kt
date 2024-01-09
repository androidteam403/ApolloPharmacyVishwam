package com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.previewImage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewpagerPreviewAdapterBinding
import com.apollopharmacy.vishwam.ui.home.champs.previewImageChamps.PreviewImageCallbackChamps
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.bumptech.glide.Glide

class PreviewImgViewPagerChampsAdapter(
    val context: Context,
    val imageUrlsList: MutableList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>?,
    val previewImageCallbackChamps: PreviewImageCallbackChamps,
) : PagerAdapter() {


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewpagerPreviewAdapterBinding: ViewpagerPreviewAdapterBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.viewpager_preview_adapter,
                container,
                false
            )


        if (imageUrlsList!!.get(position).file != null) {
            Glide.with(context).load(imageUrlsList!!.get(position).file)
                .error(R.drawable.placeholder_image)
                .into(viewpagerPreviewAdapterBinding.viewpagerImage)
        } else if (!imageUrlsList!!.get(position).imageUrl.isNullOrEmpty()) {
            Glide.with(context).load(imageUrlsList!!.get(position).imageUrl)
                .error(R.drawable.placeholder_image)
                .into(viewpagerPreviewAdapterBinding.viewpagerImage)
        }

//        previewImageCallback.statusDisplay(position,imageUrlsList.get(position).status)


        container.addView(viewpagerPreviewAdapterBinding.root)

        return viewpagerPreviewAdapterBinding.root
    }


    override fun getCount(): Int {
        return imageUrlsList!!.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}