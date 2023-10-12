package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.adapter

import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.Image
import com.apollopharmacy.vishwam.databinding.ImageviewDrugBinding
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.AttendanceFragmentCallback
import com.bumptech.glide.Glide
import java.util.*

class AttendenceImageRecycleView(

    val mContext: Context,
    var orderData: ArrayList<Image>,
    val imageClicklistner: AttendanceFragmentCallback
) :

    RecyclerView.Adapter<AttendenceImageRecycleView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val imageViewBinding: ImageviewDrugBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.imageview_drug,
                parent,
                false
            )
        return ViewHolder(imageViewBinding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = orderData.get(position)

        holder.imageViewBinding.image.setImageURI(Uri.parse(items.file.toString()))
        Glide.with(ViswamApp.context).load(items.file.toString())
            .placeholder(R.drawable.thumbnail_image)
            .into(holder.imageViewBinding.image)
        holder.imageViewBinding.image.setOnClickListener {
            items.file.toString()
                .let { it1 -> imageClicklistner.onItemClick(position, it1, "Image") }
        }

        holder.imageViewBinding.deleteImage.setOnClickListener {
            imageClicklistner.deleteImage(position)
        }

    }

    override fun getItemCount(): Int {
        return orderData.size
    }

    class ViewHolder(val imageViewBinding: ImageviewDrugBinding) :
        RecyclerView.ViewHolder(imageViewBinding.root)

}

