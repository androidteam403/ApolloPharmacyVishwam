package com.apollopharmacy.vishwam.ui.home.cashcloser.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
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
import com.apollopharmacy.vishwam.databinding.DeleteConfirmDialogBinding
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
    private val cashDeposit: ArrayList<CashDepositDetailsResponse.Cashdeposit>,
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
        val date = LocalDate.parse(closingDate!!.trim())
        val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy")
        val formattedDate = date.format(formatter)
        holder.cashCloserLayoutBinding.closingDate.text = formattedDate

        holder.cashCloserLayoutBinding.amountDeposit.setText(cashDeposit[position].amountEdit.toString())
        

        if (cashDeposit[position].amount!!.isNotEmpty()) {
            holder.cashCloserLayoutBinding.amount.text =
                DecimalFormat("#,###.00").format(cashDeposit[position].amount!!.toDouble())
                    .toString()
        }
        holder.cashCloserLayoutBinding.remarks.text = cashDeposit[position].remarks
        holder.cashCloserLayoutBinding.amountDeposit.setText(cashDeposit[position].amount)
        //holder.cashCloserLayoutBinding.amountDeposit.setText(cashDeposit[position].amount!!.toString())
        holder.cashCloserLayoutBinding.uploadButton.setOnClickListener {
            if (cashDeposit[position].isClicked!!) {
                cashDeposit[position].amount =
                    holder.cashCloserLayoutBinding.amountDeposit.text.toString()
                openDialog(position, cashDeposit[position].amount!!)
            }
        }

        // image 1
        if (cashDeposit[position].imageurl!!.isNotEmpty() && cashDeposit[position].imageurl!! != "URL1"
        ) {
            if (cashDeposit[position].imageurl!!.contains(",")) {
                holder.cashCloserLayoutBinding.amountLayout.visibility = View.GONE
                holder.cashCloserLayoutBinding.aftercapturelayout1.visibility = View.VISIBLE
                Glide.with(mContext)
                    .load(cashDeposit[position].imageurl!!.split(",")[0])
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.cashCloserLayoutBinding.aftercapturedimage1)
                holder.cashCloserLayoutBinding.redTrash1.visibility = View.GONE
                holder.cashCloserLayoutBinding.eyeImage1.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.beforecapturelayout1.visibility = View.GONE
                holder.cashCloserLayoutBinding.status.text = "Completed"
                holder.cashCloserLayoutBinding.uploadReceiptText.setText("Receipt Details")
                holder.cashCloserLayoutBinding.status.setTextColor(Color.parseColor("#00c853"))
                holder.cashCloserLayoutBinding.remarksLayout.visibility = View.VISIBLE
            } else {
                holder.cashCloserLayoutBinding.secondImageLayout.visibility = View.GONE
                holder.cashCloserLayoutBinding.amountLayout.visibility = View.GONE
                holder.cashCloserLayoutBinding.aftercapturelayout1.visibility = View.VISIBLE
                Glide.with(mContext)
                    .load(cashDeposit[position].imageurl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.cashCloserLayoutBinding.aftercapturedimage1)
                holder.cashCloserLayoutBinding.redTrash1.visibility = View.GONE
                holder.cashCloserLayoutBinding.eyeImage1.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.beforecapturelayout1.visibility = View.GONE
                holder.cashCloserLayoutBinding.status.text = "Completed"
                holder.cashCloserLayoutBinding.uploadReceiptText.setText("Receipt Details")
                holder.cashCloserLayoutBinding.status.setTextColor(Color.parseColor("#00c853"))
                holder.cashCloserLayoutBinding.remarksLayout.visibility = View.VISIBLE
            }
        } else if (cashDeposit[position].imagePath != null) {
            if (cashDeposit[position].imagePath!!.exists()) {
                val myBitmap: Bitmap =
                    BitmapFactory.decodeFile(cashDeposit[position].imagePath!!.absolutePath)
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
        if (cashDeposit[position].imageurl!!.isNotEmpty() && cashDeposit[position].imageurl!! != "URL1"
        ) {
            if (cashDeposit[position].imageurl!!.contains(",")) {
                holder.cashCloserLayoutBinding.aftercapturelayout.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.redTrash.visibility = View.GONE
                holder.cashCloserLayoutBinding.eyeImage.visibility = View.VISIBLE
                Glide.with(mContext)
                    .load(cashDeposit[position].imageurl!!.split(",")[1])
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.cashCloserLayoutBinding.aftercapturedimage)
                holder.cashCloserLayoutBinding.beforecapturelayout.visibility = View.GONE
            } else {
                holder.cashCloserLayoutBinding.aftercapturelayout.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.redTrash.visibility = View.GONE
                holder.cashCloserLayoutBinding.eyeImage.visibility = View.VISIBLE
                Glide.with(mContext)
                    .load(cashDeposit[position].imageurl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.cashCloserLayoutBinding.aftercapturedimage)
                holder.cashCloserLayoutBinding.beforecapturelayout.visibility = View.GONE
            }
        } else if (cashDeposit[position].imagePathTwo != null) {
            if (cashDeposit[position].imagePathTwo!!.exists()) {
                val bitmap: Bitmap =
                    BitmapFactory.decodeFile(cashDeposit[position].imagePathTwo!!.absolutePath)
                holder.cashCloserLayoutBinding.aftercapturedimage.setImageBitmap(bitmap)
                holder.cashCloserLayoutBinding.aftercapturelayout.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.redTrash.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.eyeImage.visibility = View.VISIBLE
                holder.cashCloserLayoutBinding.beforecapturelayout.visibility = View.GONE
            }
        } else {
            holder.cashCloserLayoutBinding.beforecapturelayout.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.aftercapturelayout.visibility = View.GONE
            holder.cashCloserLayoutBinding.redTrash.visibility = View.GONE
            holder.cashCloserLayoutBinding.eyeImage.visibility = View.GONE
        }


        // image 1
        holder.cashCloserLayoutBinding.beforecapturelayout1.setOnClickListener {
            mCallback.addImage(cashDeposit[position].siteid!!, position, 1)
        }

        // image 2
        holder.cashCloserLayoutBinding.beforecapturelayout.setOnClickListener {
            mCallback.addImage(cashDeposit[position].siteid!!, position, 2)
        }

        holder.cashCloserLayoutBinding.redTrash1.setOnClickListener {
            confirmDialog(position, 1)
        }

        holder.cashCloserLayoutBinding.redTrash.setOnClickListener {
            confirmDialog(position, 2)
        }

        holder.cashCloserLayoutBinding.eyeImage1.setOnClickListener {

            if (cashDeposit[position].imageurl!!.isNotEmpty() && !cashDeposit[position].imageurl.equals(
                    "URL1"
                )
            ) {
                if (cashDeposit[position].imageurl!!.contains(",")) {
                    mCallback.previewImage(
                        cashDeposit[position].imageurl!!.split(",")[0],
                        position
                    )
                } else {
                    mCallback.previewImage(cashDeposit[position].imageurl!!, position)
                }
            } else {
                mCallback.previewImage(cashDeposit[position].imagePath!!.toString(), position)
            }
        }

        holder.cashCloserLayoutBinding.eyeImage.setOnClickListener {
            if (cashDeposit[position].imageurl!!.isNotEmpty() && !cashDeposit[position].imageurl.equals(
                    "URL1"
                )
            ) {
                if (cashDeposit[position].imageurl!!.contains(",")) {
                    mCallback.previewImage(
                        cashDeposit[position].imageurl!!.split(",")[1],
                        position
                    )
                } else {
                    mCallback.previewImage(cashDeposit[position].imageurl!!, position)
                }
            } else {
                mCallback.previewImage(
                    cashDeposit[position].imagePathTwo!!.toString(),
                    position
                )
            }
        }

        if (cashDeposit[position].isExpanded) {
            holder.cashCloserLayoutBinding.arrow.visibility = View.GONE
            holder.cashCloserLayoutBinding.arrowClose.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.uploadReceiptLayout.setBackgroundColor(Color.parseColor("#ffffff"))
            holder.cashCloserLayoutBinding.uploadReceiptText.setTextColor(Color.parseColor("#808080"))
            holder.cashCloserLayoutBinding.extraData.visibility = View.VISIBLE
        } else {
            holder.cashCloserLayoutBinding.arrow.visibility = View.VISIBLE
            holder.cashCloserLayoutBinding.arrowClose.visibility = View.GONE
            holder.cashCloserLayoutBinding.uploadReceiptLayout.setBackgroundColor(Color.parseColor("#01bec0"))
            holder.cashCloserLayoutBinding.uploadReceiptText.setTextColor(Color.parseColor("#ffffff"))
            holder.cashCloserLayoutBinding.extraData.visibility = View.GONE
        }


        holder.cashCloserLayoutBinding.arrow.setOnClickListener {
            mCallback.headrItemClickListener(cashDeposit[position].siteid!!, position)
        }

        holder.cashCloserLayoutBinding.arrowClose.setOnClickListener {
            mCallback.headrItemClickListener(cashDeposit[position].siteid!!, position)
        }

        holder.cashCloserLayoutBinding.pendingLayout.setOnClickListener {
            mCallback.headrItemClickListener(cashDeposit[position].siteid!!, position)
        }

        holder.cashCloserLayoutBinding.amountDeposit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().isNullOrEmpty()) {
                    cashDeposit[position].amountEdit = s.toString().toInt()
                } else {
                    cashDeposit[position].amountEdit = 0
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        if (cashDeposit[position].imagePath != null || cashDeposit[position].imagePathTwo != null) {
            cashDeposit[position].setIsClicked(true)
        } else if (cashDeposit[position].imagePath == null || cashDeposit[position].imagePathTwo == null) {
            cashDeposit[position].setIsClicked(false)
        }

        if (cashDeposit[position].isClicked!!) {
            holder.cashCloserLayoutBinding.uploadButton.setBackgroundResource(R.drawable.upload_button)
        } else {
            holder.cashCloserLayoutBinding.uploadButton.setBackgroundResource(R.drawable.upload_button_disable)
        }

        holder.cashCloserLayoutBinding.uploadButton.setOnClickListener {
            if (cashDeposit[position].isClicked!!) {
                openDialog(position, holder.cashCloserLayoutBinding.amountDeposit.text.toString())
            }
        }
        holder.cashCloserLayoutBinding.headerLayout.setOnClickListener {
            mCallback.headrItemClickListener(cashDeposit[position].siteid!!, position)
        }
    }

    private fun confirmDialog(position: Int, imageState: Int) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val deleteConfirmDialogBinding: DeleteConfirmDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.delete_confirm_dialog,
            null,
            false
        )
        dialog.setContentView(deleteConfirmDialogBinding.root)
        deleteConfirmDialogBinding.noButton.setOnClickListener {
            dialog.dismiss()
        }
        deleteConfirmDialogBinding.yesButton.setOnClickListener {
            mCallback.deleteImage(position, imageState)
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun openDialog(position: Int, amount: String) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogUploadCommentBinding: DialogUploadCommentBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.dialog_upload_comment,
                null,
                false
            )
        dialog.setContentView(dialogUploadCommentBinding.root)
        dialogUploadCommentBinding.commentText.setText(amount)

        dialogUploadCommentBinding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogUploadCommentBinding.submitButton.setOnClickListener {
            if (dialogUploadCommentBinding.commentText.text.toString().isEmpty()) {
                Toast.makeText(mContext, "Please enter comment", Toast.LENGTH_LONG).show()
            } else {
                mCallback.onClickUpload(
                    cashDeposit[position].siteid!!,
                    cashDeposit[position].imagePath,
                    cashDeposit[position].imagePathTwo,
                    amount,
                    dialogUploadCommentBinding.commentText.text.toString(),
                    cashDeposit[position].dcid!!,
                    LoginRepo.getProfile()!!.EMPID
                )
                dialog.dismiss()
            }
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getItemCount(): Int {
        return cashDeposit.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(val cashCloserLayoutBinding: QcCashCloserLayoutBinding) :
        RecyclerView.ViewHolder(cashCloserLayoutBinding.root)
}