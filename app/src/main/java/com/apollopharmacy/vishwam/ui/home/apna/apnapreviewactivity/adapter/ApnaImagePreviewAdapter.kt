package com.apollopharmacy.vishwam.ui.home.apna.apnapreviewactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ApnaPreviewImageLayoutBinding
import com.apollopharmacy.vishwam.ui.home.apna.model.SurveyDetailsList
import com.bumptech.glide.Glide

class ApnaImagePreviewAdapter(
    var mContext: Context,
    var images: ArrayList<SurveyDetailsList.Image>,
    var currentPosition: Int,
    var isMobileCreated: Boolean,
    var siteImageList: ArrayList<SurveyDetailsList.SiteImage>,
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val apnaPreviewImageLayoutBinding: ApnaPreviewImageLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.apna_preview_image_layout,
            container,
            false
        )
        if (isMobileCreated) {
            Glide.with(mContext).load(images.get(position).url).error(R.drawable.placeholder_image)
                .into(apnaPreviewImageLayoutBinding.image)

            container.addView(apnaPreviewImageLayoutBinding.root)
            return apnaPreviewImageLayoutBinding.root
        } else {
            Glide.with(mContext).load(siteImageList.get(position).fullPath)
                .error(R.drawable.placeholder_image)
                .into(apnaPreviewImageLayoutBinding.image)

            container.addView(apnaPreviewImageLayoutBinding.root)
            return apnaPreviewImageLayoutBinding.root
        }
    }

    override fun getCount(): Int {
        return if (isMobileCreated) images.size else siteImageList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}