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
import com.bumptech.glide.Glide

class PreviewImageViewPager(
    val mContext: Context,
    val imageUrl: List<GetImageUrlsResponse.ImageUrl>
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewpagerPreviewImageBinding: ViewpagerPreviewImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.viewpager_preview_image,
            container,
            false
        )

        Glide.with(mContext).load(imageUrl.get(position).url).error(R.drawable.placeholder_image)
            .into(viewpagerPreviewImageBinding.viewpagerImage)

        container.addView(viewpagerPreviewImageBinding.root)

        return viewpagerPreviewImageBinding.root
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