package com.apollopharmacy.vishwam.ui.home.cashcloser.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DialogUploadCommentBinding
import com.apollopharmacy.vishwam.databinding.QcCashCloserLayoutBinding
import com.apollopharmacy.vishwam.ui.home.cashcloser.CashCloserFragmentCallback
import com.apollopharmacy.vishwam.ui.home.cashcloser.model.CashDepositDetailsResponse
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CashCloserPendingAdapter(
    val mContext: Context,
    val cashDeposit: ArrayList<CashDepositDetailsResponse.Cashdeposit>,
    val mCallback: CashCloserFragmentCallback,
) :
    RecyclerView.Adapter<CashCloserPendingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val cashCloserLayoutBinding: QcCashCloserLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_cash_closer_layout,
                parent,
                false
            )
        return ViewHolder(cashCloserLayoutBinding)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cashCloserLayoutBinding.siteId.text = cashDeposit[position].siteid
        holder.cashCloserLayoutBinding.dcId.text = cashDeposit[position].dcid

        val closingDate = cashDeposit[position].closingdate
        val date = LocalDate.parse(closingDate)
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
        val formattedDate = date.format(formatter)
        holder.cashCloserLayoutBinding.closingDate.text = formattedDate

        holder.cashCloserLayoutBinding.amount.setText(DecimalFormat("#,###.00").format(cashDeposit[position].amount!!.toDouble())
            .toString())
        holder.cashCloserLayoutBinding.remarks.setText(cashDeposit[position].remarks)

        if (holder.cashCloserLayoutBinding.amountDeposit.text.toString().isEmpty()) {
            holder.cashCloserLayoutBinding.amountDeposit.setText("0")
        }


        // image 1
        if (cashDeposit.get(position).imageurl!!.isNotEmpty() && !cashDeposit.get(position).imageurl!!.equals(
                "URL1")
        ) {
            holder.cashCloserLayoutBinding.aftercapturelayout1.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.redTrash1.visibility = View.GONE
            holder.cashCloserLayoutBinding.eyeImage1.visibility = View.VISIBLE
            Glide.with(mContext)
                .load(cashDeposit.get(position).imageurl)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.cashCloserLayoutBinding.aftercapturedimage1)
            holder.cashCloserLayoutBinding.beforecapturelayout1.visibility = View.GONE
        } else if (cashDeposit.get(position).imagePath != null) {
            if (cashDeposit.get(position).imagePath!!.exists()) {
                val myBitmap: Bitmap =
                    BitmapFactory.decodeFile(cashDeposit.get(position).imagePath!!.getAbsolutePath())
                holder.cashCloserLayoutBinding.aftercapturedimage1.setImageBitmap(myBitmap)
                holder.cashCloserLayoutBinding.aftercapturelayout1.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.redTrash1.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.eyeImage1.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.beforecapturelayout1.visibility = View.GONE
            }
        } else {
            holder.cashCloserLayoutBinding.beforecapturelayout1.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.aftercapturelayout1.visibility = View.GONE
            holder.cashCloserLayoutBinding.redTrash1.visibility = View.GONE
            holder.cashCloserLayoutBinding.eyeImage1.visibility = View.GONE
        }


        // image 2
        holder.cashCloserLayoutBinding.plusSysmbol1.setOnClickListener {
            mCallback.addImage(cashDeposit.get(position).siteid!!, position)
        }

//        if (cashDeposit.get(position).imageurl!!.isNotEmpty()) {
//            holder.cashCloserLayoutBinding.aftercapturelayout.visibility = View.VISIBLE
//            holder.cashCloserLayoutBinding.redTrash.visibility = View.VISIBLE
//            holder.cashCloserLayoutBinding.eyeImage.visibility = View.VISIBLE
//            Glide.with(mContext)
//                .load(cashDeposit.get(position).imageurl)
//                .placeholder(R.drawable.placeholder_image)
//                .into(holder.cashCloserLayoutBinding.aftercapturedimage)
//            holder.cashCloserLayoutBinding.beforecapturelayout.visibility = View.GONE
//        }


//        if (
//            holder.cashCloserLayoutBinding.aftercapturedimage1.drawable != null &&
//            holder.cashCloserLayoutBinding.aftercapturedimage.drawable != null
//        ) {
//            holder.cashCloserLayoutBinding.status.text = "Completed"
//            holder.cashCloserLayoutBinding.status.setTextColor(Color.parseColor("#00c853"))
//        } else {
//            holder.cashCloserLayoutBinding.status.text = "Pending"
//            holder.cashCloserLayoutBinding.status.setTextColor(Color.parseColor("#EA0D0D"))
//        }


        if (cashDeposit[position].isExpanded) {
            holder.cashCloserLayoutBinding.arrow.visibility = View.GONE
            holder.cashCloserLayoutBinding.arrowClose.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.extraData.visibility = View.VISIBLE
        } else {
            holder.cashCloserLayoutBinding.arrow.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.arrowClose.visibility = View.GONE
            holder.cashCloserLayoutBinding.extraData.visibility = View.GONE
        }

        holder.cashCloserLayoutBinding.arrow.setOnClickListener {
            mCallback.headrItemClickListener(cashDeposit[position].siteid!!, position)

        }
        holder.cashCloserLayoutBinding.arrowClose.setOnClickListener {
            mCallback.headrItemClickListener(cashDeposit[position].siteid!!, position)
        }


        holder.cashCloserLayoutBinding.uploadButton.setOnClickListener {
            mCallback.saveCashDepositDetails(
                cashDeposit.get(position).siteid!!,
                cashDeposit.get(position).imageurl!!,
                cashDeposit.get(position).amount!!,
                holder.cashCloserLayoutBinding.remarks.text.toString(),
                cashDeposit.get(position).dcid!!,
                LoginRepo.getProfile()!!.EMPID
            )
        }

    }

    private fun openDialog() {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var dialogUploadCommentBinding: DialogUploadCommentBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.dialog_upload_comment,
                null,
                false
            )
        dialog.setContentView(dialogUploadCommentBinding.root)

        dialogUploadCommentBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogUploadCommentBinding.submitButton.setOnClickListener {
            if (dialogUploadCommentBinding.commentText.text.toString().isEmpty()) {
                Toast.makeText(mContext, "Please enter comment", Toast.LENGTH_LONG).show()
            } else {
                dialog.dismiss()
            }
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return cashDeposit.size
    }

    class ViewHolder(val cashCloserLayoutBinding: QcCashCloserLayoutBinding) :
        RecyclerView.ViewHolder(cashCloserLayoutBinding.root)
}