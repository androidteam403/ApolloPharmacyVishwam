package com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.DialogResetBinding
import com.apollopharmacy.vishwam.databinding.QcOrderLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.PendingFragmentCallback

class QcPendingOrderDetailsAdapter(
    val mContext: Context,
    val imageClicklistner: QcListsCallback,

    var itemsList: List<QcItemListResponse.Item>?,
    var pos: Int,
    var pendingFragmentCallback: PendingFragmentCallback,
    var orderId: String,

    ) :
    RecyclerView.Adapter<QcPendingOrderDetailsAdapter.ViewHolder>() {
    var count: Int = 0
    private var customDialog: AlertDialog? = null


    var isClickReason: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val orderLayoutBinding: QcOrderLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.qc_order_layout,
                parent,
                false
            )
        return ViewHolder(orderLayoutBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = itemsList!!.get(position)
        if (items.approvedqty == null) {
            items.approvedqty = 0
        }

        if (items.category.equals("FMCG")) {
            holder.orderdetailsBinding.categoryImage.setImageResource(R.drawable.fmcg)

        } else if (items.category.equals("PHARMA")) {
            holder.orderdetailsBinding.categoryImage.setImageResource(R.drawable.pharma)

        }
        val allItemsNotClicked = itemsList!!.all { !it.isClick }

        if (allItemsNotClicked) {

            if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                    .equals("EXECUTIVE", true)
            ) {
                items.approveQtyText = items.qty

            } else {
                holder.orderdetailsBinding.approveQtyText.setText(items.approvedqty.toString())
                items.approveQtyText = items.approvedqty

            }

        }
        holder.orderdetailsBinding.approveQtyText.setText(items.approveQtyText.toString())


        if (items.qty == null) {
            holder.orderdetailsBinding.quantityText.setText("-")

        } else {
            holder.orderdetailsBinding.quantityText.setText(items.qty.toString())

        }
        if (Preferences.getAppLevelDesignationQCFail().equals("EXECUTIVE", true)) {
            if (items.approveQtyText == 0) {
                holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                holder.orderdetailsBinding.reason.setText("Select")

                holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.rounded_dropdown_qcfail_bg)
                holder.orderdetailsBinding.selectResonItem.isEnabled = true
                holder.orderdetailsBinding.selectResonItem.alpha = 1f
            } else {
                holder.orderdetailsBinding.selectResonItem.isEnabled = false
                holder.orderdetailsBinding.selectResonItem.alpha = 0.5f
                holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)

            }

        } else {
            if (items.approvedqty == null) {
                if (items.approvedqty == null || items.approvedqty == 0 || holder.orderdetailsBinding.approveQtyText.text.toString()
                        .toInt() == 0
                ) {
                    holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                    holder.orderdetailsBinding.reason.setText("Select")

                    holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.rounded_dropdown_qcfail_bg)
                    holder.orderdetailsBinding.selectResonItem.isEnabled = true
                    holder.orderdetailsBinding.selectResonItem.alpha = 1f
                } else {
                    holder.orderdetailsBinding.selectResonItem.isEnabled = false
                    holder.orderdetailsBinding.selectResonItem.alpha = 0.5f
                    holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                    holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)

                }
            } else if (items.approvedqty == 0 || holder.orderdetailsBinding.approveQtyText.text.toString()
                    .toInt() == 0
            ) {
                holder.orderdetailsBinding.selectResonItem.isEnabled = true
                holder.orderdetailsBinding.selectResonItem.alpha = 1f
                holder.orderdetailsBinding.reason.setText("Select")

                holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.rounded_dropdown_qcfail_bg)
            } else {
                holder.orderdetailsBinding.selectResonItem.isEnabled = false
                holder.orderdetailsBinding.selectResonItem.alpha = 0.5f
                holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)

            }
        }

        if (items.remarks.isNullOrEmpty()) {

        } else {
            holder.orderdetailsBinding.reason.setText(items.remarks.toString())
            pendingFragmentCallback.onNotify()
//            Toast.makeText(ViswamApp.context, "Reject Reasons not available", Toast.LENGTH_LONG)
//
//            holder.orderdetailsBinding.reason.setText("Select")
        }




        holder.orderdetailsBinding.medicineName.setText(items.itemname)
        holder.orderdetailsBinding.categoryName.setText("( " + items.category + " )")
        holder.orderdetailsBinding.price.setText(items.price.toString())

        holder.orderdetailsBinding.approveQtyText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            @SuppressLint("ShowToast")
            override fun afterTextChanged(p0: Editable?) {
                if (!p0.toString().isNullOrEmpty()) {
                    items.approveQtyText = p0.toString().toInt()

                    if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                            .equals("EXECUTIVE", true)
                    ) {
                        if (Integer.parseInt(p0.toString()) > items.qty!!) {

                            items.approveQtyText = items.qty
                            items.remarks=""
                            pendingFragmentCallback.onNotify()

                            holder.orderdetailsBinding.approveQtyText.setText(items.qty.toString())
                            showDialog()

                        } else if (Integer.parseInt(p0.toString()) > 0) {

                            holder.orderdetailsBinding.selectResonItem.isEnabled = false
                            holder.orderdetailsBinding.selectResonItem.alpha = 0.5f
                            holder.orderdetailsBinding.reason.text = "Select"
                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)
                            items.remarks=""
                            pendingFragmentCallback.onNotify()
                            //                            showDialog()


                        } else if (Integer.parseInt(p0.toString()) == 0) {
                            holder.orderdetailsBinding.selectResonItem.isEnabled = true
                            holder.orderdetailsBinding.selectResonItem.alpha = 1f
                            holder.orderdetailsBinding.reason.setText("Select")

                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.rounded_dropdown_qcfail_bg)

                        }

                    } else {

                        if (Integer.parseInt(p0.toString()) > items.approvedqty!!) {


                            items.approveQtyText = items.approvedqty
                            holder.orderdetailsBinding.selectResonItem.isEnabled = false
                            holder.orderdetailsBinding.selectResonItem.alpha = 0.5f
                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)
                            items.remarks=""
                            pendingFragmentCallback.onNotify()

                            holder.orderdetailsBinding.approveQtyText.setText(items.approvedqty.toString())
                            showDialog()


                        } else if (Integer.parseInt(p0.toString()) > 0) {


                            holder.orderdetailsBinding.selectResonItem.isEnabled = false
                            holder.orderdetailsBinding.selectResonItem.alpha = 0.5f
                            holder.orderdetailsBinding.reason.text = "Select"
                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)
                            items.remarks=""
                            pendingFragmentCallback.onNotify()

                        } else if (Integer.parseInt(p0.toString()) == 0) {
                            holder.orderdetailsBinding.selectResonItem.isEnabled = true
                            holder.orderdetailsBinding.selectResonItem.alpha = 1f
                            holder.orderdetailsBinding.reason.setText("Select")

                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)
                            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.rounded_dropdown_qcfail_bg)

                        }

                    }


                }


            }

        })

        if (items.imageurls.toString().isNullOrEmpty()) {
            holder.orderdetailsBinding.eyeImage.setColorFilter(
                R.color.dark_grey,
                android.graphics.PorterDuff.Mode.MULTIPLY
            )


        } else {
            holder.orderdetailsBinding.eyeImage.setOnClickListener {
                if (items.imageurls.toString().isNullOrEmpty()) {
                    Toast.makeText(mContext, "No Image Urls", Toast.LENGTH_LONG)
                } else {
                    imageClicklistner.imageData(
                        position,
                        orderId,
                        items.itemname.toString(),
                        items.imageurls.toString()
                    )
                }

            }
        }

        holder.orderdetailsBinding.selectResonItem.setOnClickListener {
            holder.orderdetailsBinding.approveQtyText.setText("0")
            items.setisClick(true)
//            items.approvedqty = holder.orderdetailsBinding.approveQtyText.text.toString().toInt()
            pendingFragmentCallback.onClickReason(pos, position, items.orderno)
        }

        holder.orderdetailsBinding.subtract.setOnClickListener {

            if (items.approveQtyText!!.toInt()!! <= 0) {

            } else {
                items.approveQtyText = items.approveQtyText!! - 1
//                items.qty=count!!
//                items.approvedqty=count!!
                holder.orderdetailsBinding.approveQtyText.setText(items.approveQtyText.toString())
            }

        }

        holder.orderdetailsBinding.add.setOnClickListener {

            if (!holder.orderdetailsBinding.reason.text.toString().equals("Select")) {
                if (Integer.parseInt(holder.orderdetailsBinding.approveQtyText.text.toString()) >= 0) {
                    holder.orderdetailsBinding.reason.setText("Select")
                    holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)

                }
            }


            if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                    .equals("EXECUTIVE", true)
            ) {
                if (Integer.parseInt(holder.orderdetailsBinding.approveQtyText.text.toString()) > items.qty!!
                    !!

                ) {
                    Toast.makeText(
                        mContext,
                        "Approval Qty should not exceed the Requested Qty",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    items.approveQtyText = items.approveQtyText!! + 1;
                    holder.orderdetailsBinding.approveQtyText.setText(items.approveQtyText.toString())
                }

            } else {
                if (Integer.parseInt(holder.orderdetailsBinding.approveQtyText.text.toString()) > items.approvedqty!!
                    !!

                ) {
                    Toast.makeText(
                        mContext,
                        "Approval Qty should not exceed the Requested Qty",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    items.approveQtyText = items.approveQtyText!! + 1;

                    holder.orderdetailsBinding.approveQtyText.setText(items.approveQtyText.toString())
                }

            }
        }
    }

    private fun showDialog() {
        if (customDialog == null) {
            val dialogBinding: DialogResetBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.dialog_reset,
                    null,
                    false
                )
            customDialog = AlertDialog.Builder(mContext, 0).create().apply {
                setView(dialogBinding.root)
                setCancelable(false)

                dialogBinding.yesBtn.setOnClickListener {
                    dismiss()
                }
            }
        }
        customDialog?.show()
    }

    fun subtractDiscount(discount: Double): Double {
        if (discount >= 1.00) {
            return discount - 1.00
        }
        return discount
    }

    override fun getItemCount(): Int {
        return itemsList!!.size
    }

    class ViewHolder(val orderdetailsBinding: QcOrderLayoutBinding) :
        RecyclerView.ViewHolder(orderdetailsBinding.root)
}

