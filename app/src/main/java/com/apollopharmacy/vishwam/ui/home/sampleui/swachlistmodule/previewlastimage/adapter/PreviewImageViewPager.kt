package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewlastimage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewpagerPreviewImageBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse

class PreviewImageViewPager(
    val mContext: Context,
    val imageUrl: List<GetImageUrlsResponse.ImageUrl>
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewPagerBinding: ViewpagerPreviewImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.viewpager_preview_image,
            container,
            false
        )




        container.addView(viewPagerBinding.root)
        return viewPagerBinding.root
    }

    override fun getCount(): Int {
        return imageUrl.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}