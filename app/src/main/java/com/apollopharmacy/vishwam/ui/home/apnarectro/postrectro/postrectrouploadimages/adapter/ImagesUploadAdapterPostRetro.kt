package com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.AdapterImagesuploadApnaBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.PostRetroUploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.PostRetroUploadImagesCallback
import com.bumptech.glide.Glide

class ImagesUploadAdapterPostRetro(
    private var configList: ArrayList<PostRetroUploadImagesActivity.ImgeDtcl>,
    private var pos: Int,
    private var context: Context,
    private var uploadImagesCallback: PostRetroUploadImagesCallback
) : RecyclerView.Adapter<ImagesUploadAdapterPostRetro.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ImagesUploadAdapterPostRetro.ViewHolder {
        val adapterImagesuploadApnaBinding: AdapterImagesuploadApnaBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.adapter_imagesupload_apna,
                parent,
                false
            )
        return ViewHolder(adapterImagesuploadApnaBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category  = configList.get(pos).file
        var myInt: Int = 1

        holder.adapterImagesuploadApnaBinding.imageTextandNumber.text = "Image" +position.plus(myInt).toString()
        if(configList.get(pos)?.file!=null){
            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
            Glide.with(ViswamApp.context).load(configList.get(pos).file)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
            if(configList.get(pos).postRetroUploaded){
                holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=1.0f
            }else{
                holder.adapterImagesuploadApnaBinding.aftercapturedimage.alpha=0.5f
            }

//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.VISIBLE
            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.VISIBLE
        }else{
            Glide.with(ViswamApp.context).load(R.drawable.capture)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.adapterImagesuploadApnaBinding.aftercapturedimage)
            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.GONE
            holder.adapterImagesuploadApnaBinding.aftercapturelayout.visibility = View.VISIBLE
            holder.adapterImagesuploadApnaBinding.cameraIcon.visibility = View.VISIBLE

//            holder.afterCaptureImage.setImageURI(Uri.fromFile(SwachModelResponse?.file))
//            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.aftercapturedimage.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.eyeImage.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.redTrash.visibility = View.GONE
//            holder.adapterImagesuploadApnaBinding.beforecapturelayout.visibility = View.VISIBLE
//            holder.adapterImagesuploadApnaBinding.tickMarkGreen.visibility = View.GONE
        }
//        holder.adapterImagesuploadApnaBinding.eyeImage.setOnClickListener {
//            uploadImagesCallback.onClickEyeImage()
//        }
        holder.adapterImagesuploadApnaBinding.aftercapturedimage.setOnClickListener {
            uploadImagesCallback.onClickImageView()
        }

        holder.adapterImagesuploadApnaBinding.cameraIcon.setOnClickListener {
            uploadImagesCallback.onClickCameraIcon(pos)
        }
    }

    override fun getItemCount(): Int {
        return configList.size-4
    }
    class ViewHolder(val adapterImagesuploadApnaBinding: AdapterImagesuploadApnaBinding) :
        RecyclerView.ViewHolder(adapterImagesuploadApnaBinding.root)
}