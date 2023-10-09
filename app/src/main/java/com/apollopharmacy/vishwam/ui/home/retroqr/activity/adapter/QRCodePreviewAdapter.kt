package com.apollopharmacy.vishwam.ui.home.retroqr.activity.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.AdapterQrcodePreviewBinding
import com.apollopharmacy.vishwam.ui.home.retroqr.activity.model.StoreWiseRackDetails


class QRCodePreviewAdapter(
    var mContext: Context,
    var reviewImagesList: ArrayList<StoreWiseRackDetails.StoreDetail>
) : RecyclerView.Adapter<QRCodePreviewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QRCodePreviewAdapter.ViewHolder {
        val adapterQrcodePreviewBinding = DataBindingUtil.inflate<AdapterQrcodePreviewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.adapter_qrcode_preview, parent, false
        )
        return QRCodePreviewAdapter.ViewHolder(adapterQrcodePreviewBinding)
    }

    override fun onBindViewHolder(holder: QRCodePreviewAdapter.ViewHolder, position: Int) {
        var storeDetail: StoreWiseRackDetails.StoreDetail = reviewImagesList.get(position)
        var byteArray = storeDetail.byteArray

//        holder.adapterQrcodePreviewBinding.qrcodeBitmap.setImageBitmap(storeDetail.bitmap)

        val data: ByteArray = byteArray
        val bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
        holder.adapterQrcodePreviewBinding.qrcodeBitmap.setImageBitmap(bmp)

        /*if (byteArray.size !== 0) {
            val `is`: InputStream = ByteArrayInputStream(byteArray)
            val bitMap = BitmapFactory.decodeStream(`is`)
            holder.adapterQrcodePreviewBinding.qrcodeBitmap.setImageBitmap(bitMap)
        }*/
        holder.adapterQrcodePreviewBinding.rackSiteName.text =
            "${storeDetail.rackno}-${Preferences.getQrSiteId()}"
    }

    override fun getItemCount(): Int {
        return reviewImagesList.size
    }

    class ViewHolder(val adapterQrcodePreviewBinding: AdapterQrcodePreviewBinding) :
        RecyclerView.ViewHolder(adapterQrcodePreviewBinding.root)
}