package com.apollopharmacy.vishwam.ui.home.apna.activity.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.AdapterApartmentsListBinding
import com.apollopharmacy.vishwam.databinding.ImageDeleteConfirmDialogBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ApartmentData

class ApartmentTypeItemAdapter(
    var mContext: Context,
    var mCallBack: ApnaNewSurveyCallBack,
    var data: ArrayList<ApartmentData>,
) : RecyclerView.Adapter<ApartmentTypeItemAdapter.ViewHolder>() {

    class ViewHolder(val adapterApartmentsListBinding: AdapterApartmentsListBinding) :
        RecyclerView.ViewHolder(adapterApartmentsListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterApartmentsListBinding = DataBindingUtil.inflate<AdapterApartmentsListBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_apartments_list,
            parent,
            false
        )
        return ViewHolder(adapterApartmentsListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data.get(position).apartments.isNotEmpty()) {
            holder.adapterApartmentsListBinding.apartmentsText.setText(data[position].apartments)
        } else {
            holder.adapterApartmentsListBinding.apartmentsText.setText("-")
        }

        if (data.get(position).apartmentType.isNotEmpty()) {
            holder.adapterApartmentsListBinding.apartmentTypeText.setText(data[position].apartmentType)
        } else {
            holder.adapterApartmentsListBinding.apartmentTypeText.setText("-")
        }

        if (data[position].noOfHouses.isNotEmpty()) {
            holder.adapterApartmentsListBinding.noOfHousesText.setText(data[position].noOfHouses)
        } else {
            holder.adapterApartmentsListBinding.noOfHousesText.setText("-")
        }

        if (data[position].distance.isNotEmpty()) {
            holder.adapterApartmentsListBinding.distanceText.setText(data[position].distance + " m")
        } else {
            holder.adapterApartmentsListBinding.distanceText.setText("-")
        }

        holder.adapterApartmentsListBinding.deleteApartment.setOnClickListener {
//            mCallBack.onClickApartmentDelete(position)
            openConfirmDialog(position, data[position])
        }
    }

    private fun openConfirmDialog(position: Int, apartmentData: ApartmentData) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imageDeleteConfirmDialogBinding =
            DataBindingUtil.inflate<ImageDeleteConfirmDialogBinding>(
                LayoutInflater.from(mContext),
                R.layout.image_delete_confirm_dialog,
                null,
                false
            )
        dialog.setContentView(imageDeleteConfirmDialogBinding.root)
        imageDeleteConfirmDialogBinding.noButton.setOnClickListener {
            dialog.dismiss()
        }
        imageDeleteConfirmDialogBinding.yesButton.setOnClickListener {
            mCallBack.onClickApartmentDelete(position)
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}