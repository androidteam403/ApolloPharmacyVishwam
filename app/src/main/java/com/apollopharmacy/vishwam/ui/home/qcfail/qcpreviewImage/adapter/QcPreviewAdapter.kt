package com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.QcViewpagerPreviewAdapterBinding
import com.apollopharmacy.vishwam.databinding.ViewpagerPreviewAdapterBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewCallbacks
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.approvelist.model.GetImageUrlsResponse
import com.apollopharmacy.vishwam.ui.home.sampleui.swachlistmodule.previewImage.PreviewImageCallback
import com.bumptech.glide.Glide
import com.google.android.gms.common.util.CollectionUtils.listOf
import java.sql.Array
import java.util.*
import java.util.Arrays.asList
import kotlin.collections.ArrayList

class QcPreviewAdapter(
    val context: Context,
    val imagesList: ArrayList<String>,
    val itemList: ArrayList<QcItemListResponse.Item>,
    val previewImageCallback: QcPreviewCallbacks,
    val currentPosition: Int,
   val imageUrlList : List<String>
) : PagerAdapter() {

    var list = ArrayList<String>()
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewpagerPreviewAdapterBinding: QcViewpagerPreviewAdapterBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.qc_viewpager_preview_adapter,
                container,
                false
            )
//        for (i in imageUrlsList.imageurls.toString().split(";")) {
//            list.add(i)
//        }





    Glide.with(context).load(imageUrlList.get(position)).error(R.drawable.placeholder_image).into(viewpagerPreviewAdapterBinding.viewpagerImage)
//    previewImageCallback.statusDisplay(position, list[i])






        container.addView(viewpagerPreviewAdapterBinding.root)

        return viewpagerPreviewAdapterBinding.root
    }


    override fun getCount(): Int {
        return imageUrlList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}
