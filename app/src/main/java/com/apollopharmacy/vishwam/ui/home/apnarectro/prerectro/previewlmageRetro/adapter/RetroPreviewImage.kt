package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.bumptech.glide.Glide
import org.apache.commons.lang3.text.WordUtils


class RetroPreviewImage(
    val mContext: Context,
    val imageUrl: List<GetImageUrlResponse.ImageUrl>,
    val retroId: String?,
    val stage: String,
) :
    PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewpagerPreviewImageBinding: RetroPreviewImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.retro_preview_image,
            container,
            false
        )
        viewpagerPreviewImageBinding.retroId = retroId
        viewpagerPreviewImageBinding.stageReviewMessage.setText("This Apna  " + WordUtils.capitalizeFully(stage.replace("-"," ")) + "   review" + "\n" + "completed sucessfully")


        var accepted = 0
        var rejected = 0

        Glide.with(mContext).load(imageUrl.get(position).url)
            .error(R.drawable.placeholder_image)
            .into(viewpagerPreviewImageBinding.viewpagerImage)
        for (i in imageUrl) {

            if (i.status.equals("1")) {
                accepted++
            } else if (i.status.equals("2")) {
                rejected++
            }
        }



        viewpagerPreviewImageBinding.totalImages = "${imageUrl.size}"
        viewpagerPreviewImageBinding.accepted = "$accepted"
        viewpagerPreviewImageBinding.rejected = "$rejected"
        if (position == imageUrl.size - 1) {

            viewpagerPreviewImageBinding.isLastPos = accepted == imageUrl.size || rejected == imageUrl.size || accepted + rejected == imageUrl.size

        }else{
            viewpagerPreviewImageBinding.isLastPos=false
        }

        container.addView(viewpagerPreviewImageBinding.root)

        return viewpagerPreviewImageBinding.root
    }


    override fun getCount(): Int {
        return imageUrl.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
        //super.getItemPosition(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}