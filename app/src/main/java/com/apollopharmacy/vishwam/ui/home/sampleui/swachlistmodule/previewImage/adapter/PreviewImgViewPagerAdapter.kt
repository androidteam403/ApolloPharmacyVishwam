package com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewImage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ViewpagerPreviewAdapterBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewImage.PreviewImageCallback
import com.bumptech.glide.Glide

class PreviewImgViewPagerAdapter(
    val  context: Context,
    val imageUrlsList: ArrayList<GetImageUrlsResponse.ImageUrl>,
    val previewImageCallback: PreviewImageCallback
) : PagerAdapter()  {


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewpagerPreviewAdapterBinding: ViewpagerPreviewAdapterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.viewpager_preview_adapter,
            container,
            false
        )



        Glide.with(context).load(imageUrlsList.get(position).url)
                .error(R.drawable.placeholder_image)
                .into(viewpagerPreviewAdapterBinding.viewpagerImage)
        previewImageCallback.statusDisplay(position,imageUrlsList.get(position).status)




        container.addView(viewpagerPreviewAdapterBinding.root)

        return viewpagerPreviewAdapterBinding.root
    }


    override fun getCount(): Int {
        return imageUrlsList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}