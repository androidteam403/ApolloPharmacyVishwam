package com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.adapter

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
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewCallbacks
import com.bumptech.glide.Glide
import java.io.BufferedOutputStream
import java.io.File
import java.net.MalformedURLException
import java.net.URL


class QcPreviewAdapter(
    val context: Context,
    val imagesList: ArrayList<String>,
    val itemList: ArrayList<QcItemListResponse.Item>,
    val previewImageCallback: QcPreviewCallbacks,
    val currentPosition: Int,
    val activity: Activity,
    val imageUrlList: List<String>,
) : PagerAdapter() {

    var list = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.Q)
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


        if (imageUrlList.get(position).endsWith(".docx") || imageUrlList.get(position)
                .endsWith(".pdf")
        ) {
            viewpagerPreviewAdapterBinding.buttondownload.visibility = View.VISIBLE
            viewpagerPreviewAdapterBinding.previewmsg.visibility = View.VISIBLE
            viewpagerPreviewAdapterBinding.viewpagerImage.visibility = View.GONE

        } else {
            viewpagerPreviewAdapterBinding.buttondownload.visibility = View.GONE
            viewpagerPreviewAdapterBinding.previewmsg.visibility = View.GONE
            viewpagerPreviewAdapterBinding.viewpagerImage.visibility = View.VISIBLE
            Glide.with(context).load(imageUrlList.get(position)).error(R.drawable.placeholder_image)
                .into(viewpagerPreviewAdapterBinding.viewpagerImage)
//    previewImageCallback.statusDisplay(position, list[i])
        }


        viewpagerPreviewAdapterBinding.buttondownload.setOnClickListener {

            if (imageUrlList.get(position).endsWith(".pdf")) {
                var url: URL? = null
                var file: String? = null
                url = URL(imageUrlList.get(position))
                file = url.path
                file = file.substring(file.lastIndexOf('/') + 1)
                val request = DownloadManager.Request(Uri.parse(url.toString() + ""))
                request.setTitle(file)
                request.setMimeType("application/pdf")
                request.allowScanningByMediaScanner()
                request.setAllowedOverMetered(true)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file)
                val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)
                val dialogBinding: DialogResetQcBinding? =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.dialog_reset_qc,
                        null,
                        false
                    )
                val customDialog = android.app.AlertDialog.Builder(activity, 0).create()
                customDialog.apply {

                    setView(dialogBinding?.root)
                    setCancelable(false)
                }.show()


                if (dialogBinding != null) {
                    dialogBinding.message.setText("Please check your Phone Status bar for Download Progress.")
                }


                if (dialogBinding != null) {
                    dialogBinding.yesBtn.setOnClickListener {


                        customDialog.dismiss()
                    }
                }


            } else if (imageUrlList.get(position).endsWith(".docx")) {
                var url: URL? = null
                var file: String? = null
                url = URL(imageUrlList.get(position))
                file = url.path
                file = file.substring(file.lastIndexOf('/') + 1)
                val request = DownloadManager.Request(Uri.parse(url.toString() + ""))
                request.setTitle(file)
                request.setMimeType("application/docx")
                request.allowScanningByMediaScanner()
                request.setAllowedOverMetered(true)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file)
                val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)
                val dialogBinding: DialogResetQcBinding? =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(context),
                        R.layout.dialog_reset_qc,
                        null,
                        false
                    )
                val customDialog = android.app.AlertDialog.Builder(activity, 0).create()
                customDialog.apply {

                    setView(dialogBinding?.root)
                    setCancelable(false)
                }.show()


                if (dialogBinding != null) {
                    dialogBinding.message.setText("Please check your Phone Status bar for Download Progress.")
                }


                if (dialogBinding != null) {
                    dialogBinding.yesBtn.setOnClickListener {


                        customDialog.dismiss()
                    }
                }
//                Toast.makeText(context,"Approve quantity cannot be more than Required quantity",
//                    Toast.LENGTH_LONG).show()

            }

        }


//        viewpagerPreviewAdapterBinding.previewmsg.setOnClickListener {
//                var url: URL? = null
//                var file: String? = null
//                url = URL(imageUrlList.get(position))
//                file = url!!.path
//                val request = DownloadManager.Request(Uri.parse(url.toString() + ""))
//            val file1 = File(Environment.getExternalStorageDirectory().absolutePath + "/" + file)
//            val target = Intent(Intent.ACTION_VIEW)
//            target.setDataAndType(Uri.fromFile(file1), "application/pdf")
//            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//            val intent: Intent = Intent.createChooser(target, "Open File")
//            try {
//                context.startActivity(intent)
//            } catch (e: ActivityNotFoundException) {
//                // Instruct the user to install a PDF reader here, or something
//            }
//        }
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
