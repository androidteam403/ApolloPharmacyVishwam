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
import com.apollopharmacy.vishwam.databinding.AdapterHospitalsListBinding
import com.apollopharmacy.vishwam.databinding.ImageDeleteConfirmDialogBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.ApnaNewSurveyCallBack
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.HospitalData
import java.io.File

class HospitalsAdapter(
    var mContext: Context,
    var mCallBack: ApnaNewSurveyCallBack,
    var hospitalsList: ArrayList<HospitalData>,
): RecyclerView.Adapter<HospitalsAdapter.ViewHolder>() {

    class ViewHolder(val adapterHospitalsListBinding: AdapterHospitalsListBinding) :
        RecyclerView.ViewHolder(adapterHospitalsListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterHospitalsListBinding = DataBindingUtil.inflate<AdapterHospitalsListBinding>(
            LayoutInflater.from(mContext),
            R.layout.adapter_hospitals_list,
            parent,
            false
        )
        return ViewHolder(adapterHospitalsListBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.adapterHospitalsListBinding.hospitalName.text = hospitalsList[position].hospitalName

        if (hospitalsList.get(position).hospitalName.isNotEmpty()) {
            holder.adapterHospitalsListBinding.hospitalName.setText(hospitalsList[position].hospitalName)
        } else {
            holder.adapterHospitalsListBinding.hospitalName.setText("-")
        }

        if (hospitalsList.get(position).beds.isNotEmpty()) {
            holder.adapterHospitalsListBinding.beds.setText(hospitalsList[position].beds)
        } else {
            holder.adapterHospitalsListBinding.beds.setText("-")
        }

        if (hospitalsList.get(position).speciality.isNotEmpty()) {
            holder.adapterHospitalsListBinding.speciality.setText(hospitalsList[position].speciality)
        } else {
            holder.adapterHospitalsListBinding.speciality.setText("-")
        }

        if (hospitalsList.get(position).noOfOpd.isNotEmpty()) {
            holder.adapterHospitalsListBinding.noOfOpd.setText(hospitalsList[position].noOfOpd)
        } else {
            holder.adapterHospitalsListBinding.noOfOpd.setText("-")
        }

        if (hospitalsList.get(position).occupancy.isNotEmpty()) {
            holder.adapterHospitalsListBinding.occupancy.setText(hospitalsList[position].occupancy)
        } else {
            holder.adapterHospitalsListBinding.occupancy.setText("-")
        }

        holder.adapterHospitalsListBinding.deleteHospital.setOnClickListener {
//            mCallBack.onClickDeleteHospital(position)
            openConfirmDialog(position, hospitalsList[position])
        }
    }

    private fun openConfirmDialog(position: Int, hospitalData: HospitalData) {
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
            mCallBack.onClickDeleteHospital(position)
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return hospitalsList.size
    }
}