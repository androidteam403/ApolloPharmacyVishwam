package com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewlmageRetro.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DialogResetBinding
import com.apollopharmacy.vishwam.databinding.DialogResetQcBinding
import com.apollopharmacy.vishwam.databinding.QcViewpagerPreviewAdapterBinding
import com.apollopharmacy.vishwam.databinding.RetroPreviewImageBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewCallbacks
import com.bumptech.glide.Glide
import java.io.BufferedOutputStream
import java.io.File
import java.net.MalformedURLException
import java.net.URL


class RetroPreviewImage(
    val context: Context,

    val activity: Activity,
    val imageUrlList: List<String>,
) : PagerAdapter() {

    var list = ArrayList<String>()

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val viewpagerPreviewAdapterBinding: RetroPreviewImageBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.retro_preview_image,
                container,
                false
            )



            Glide.with(context).load(imageUrlList.get(position)).error(R.drawable.placeholder_image)
                .into(viewpagerPreviewAdapterBinding.viewpagerImage)









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
