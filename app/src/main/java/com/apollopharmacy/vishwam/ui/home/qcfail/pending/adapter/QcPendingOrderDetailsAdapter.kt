package com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.DialogAcceptQcBinding
import com.apollopharmacy.vishwam.databinding.DialogResetBinding
import com.apollopharmacy.vishwam.databinding.QcOrderLayoutBinding
import com.apollopharmacy.vishwam.databinding.QcPendingLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.PendingFragmentCallback

class QcPendingOrderDetailsAdapter(
    val mContext: Context,
    val imageClicklistner: QcListsCallback,

    var itemsList: List<QcItemListResponse.Item>,
    var pos: Int,
    var qcPendingList: List<QcListsResponse.Pending>,
    var pendingFragmentCallback: PendingFragmentCallback,
    var qcPendingListAdapter: QcPendingListAdapter,
    var orderId: String,

    val pendingLayoutBinding: QcPendingLayoutBinding,
) :
    RecyclerView.Adapter<QcPendingOrderDetailsAdapter.ViewHolder>() {


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
        var count: Int = 0
        var aprqty: Int = 0
        var qcaprqty: Int = 0

        val items = itemsList.get(position)
//        holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.color.grey)
        holder.orderdetailsBinding.reason.setText("Select")
        holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)

        if (Preferences.getAppLevelDesignationQCFail().replace(" ", "").equals("EXECUTIVE", true)) {
            holder.orderdetailsBinding.approveQtyText.setText(items.qty.toString())
            aprqty = items.qty!!
            qcaprqty = items.qty!!


        } else if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                .equals("MANAGER", true)
        ) {
            holder.orderdetailsBinding.approveQtyText.setText(items.approvedqty.toString())
            aprqty = items.approvedqty!!
            qcaprqty = items.approvedqty!!

        } else if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                .equals("GENERALMANAGER", true)
        ) {
            holder.orderdetailsBinding.approveQtyText.setText(items.approvedqty.toString())
            if (items.approvedqty != null) {
                aprqty = items.approvedqty!!
                qcaprqty = items.approvedqty!!


            } else {
                aprqty = 0

            }

        }
        if (items.qty == null) {

        } else {
            count = aprqty

        }
        if (items != null)
            if (items.qty == null) {
                holder.orderdetailsBinding.quantityText.setText("-")

            } else {
                holder.orderdetailsBinding.quantityText.setText(items.qty.toString())

            }
        if (items.remarks != null) {
            holder.orderdetailsBinding.reason.setText(items.remarks.toString())
        } else {
            holder.orderdetailsBinding.reason.setText("Select")
        }
        holder.orderdetailsBinding.medicineName.setText(items.itemname)
        holder.orderdetailsBinding.categoryName.setText("- " + items.category)
        holder.orderdetailsBinding.price.setText(items.price.toString())



        if (items.approvedqty == 0) {


            holder.orderdetailsBinding.selectResonItem.isEnabled = true
//            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.color.white)

            holder.orderdetailsBinding.selectResonItem.alpha = 1f
//                        holder.orderdetailsBinding.reasonHeaderLabelLayout.visibility = View.VISIBLE
//                        holder.orderdetailsBinding.reasonValueLayout.visibility = View.VISIBLE
        } else {

//            holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.color.grey)
            holder.orderdetailsBinding.selectResonItem.isEnabled = false
            holder.orderdetailsBinding.selectResonItem.alpha = 0.5f

//                        holder.orderdetailsBinding.reasonHeaderLabelLayout.visibility = View.GONE
//                        holder.orderdetailsBinding.reasonValueLayout.visibility = View.GONE
        }


        holder.orderdetailsBinding.approveQtyText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {


            }

            @SuppressLint("ShowToast")
            override fun afterTextChanged(p0: Editable?) {
                if (!p0.toString().isNullOrEmpty()) {

                    if (Integer.parseInt(p0.toString()) > aprqty) {

                        val dialogBinding: DialogResetBinding? =
                            DataBindingUtil.inflate(
                                LayoutInflater.from(mContext),
                                R.layout.dialog_reset,
                                null,
                                false
                            )
                        val customDialog = android.app.AlertDialog.Builder(mContext, 0).create()
                        customDialog.apply {

                            setView(dialogBinding?.root)
                            setCancelable(false)
                        }.show()


                        if (dialogBinding != null) {
                            dialogBinding.yesBtn.setOnClickListener {
                                holder.orderdetailsBinding.approveQtyText.setText(aprqty.toString())

                                holder.orderdetailsBinding.approveQtyText.clearFocus()

                                customDialog.dismiss()
                            }
                        }
//                        Toast.makeText(mContext,"Approve Qty Should be less than or equal to Req qty ",Toast.LENGTH_LONG).show()
//                        if (Preferences.getAppLevelDesignationQCFail().replace(" ","").equals("EXECUTIVE",true)){
//                            holder.orderdetailsBinding.approveQtyText.setText(items.qty.toString())
//
//                        }
//                        else if (Preferences.getAppLevelDesignationQCFail().replace(" ","").equals("MANAGER",true)){
//                            holder.orderdetailsBinding.approveQtyText.setText(items.approvedqty.toString())
//
//                        }
//
//                        else if (Preferences.getAppLevelDesignationQCFail().replace(" ","").equals("GENERAL MANAGER",true)){
//                            holder.orderdetailsBinding.approveQtyText.setText(items.approvedqty.toString())
//
//                        }
                    }
                }


                if (p0.toString().length > 0) {
                    items.approvedqty = p0.toString().toInt()
                    if (items.approvedqty == 0) {
                        holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)

                        holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.rounded_dropdown_qcfail_bg)

                        holder.orderdetailsBinding.selectResonItem.isEnabled = true
                        holder.orderdetailsBinding.selectResonItem.alpha = 1f
//                        holder.orderdetailsBinding.reasonHeaderLabelLayout.visibility = View.VISIBLE
//                        holder.orderdetailsBinding.reasonValueLayout.visibility = View.VISIBLE
                    } else {
                        holder.orderdetailsBinding.selectResonItem.setBackgroundResource(0)

                        holder.orderdetailsBinding.selectResonItem.setBackgroundResource(R.drawable.qc_rounded_dropdown_qcfail_bg)

                        holder.orderdetailsBinding.selectResonItem.isEnabled = false
                        holder.orderdetailsBinding.selectResonItem.alpha = 0.5f

//                        holder.orderdetailsBinding.reasonHeaderLabelLayout.visibility = View.GONE
//                        holder.orderdetailsBinding.reasonValueLayout.visibility = View.GONE
                    }

                    qcPendingListAdapter.setQcItemLists(items.orderno!!, pendingLayoutBinding)
                }
            }

        })


        if (items.imageurls.toString().isNullOrEmpty()) {
            holder.orderdetailsBinding.eyeImage.setColorFilter(R.color.dark_grey,
                android.graphics.PorterDuff.Mode.MULTIPLY)


        } else {
            holder.orderdetailsBinding.eyeImage.setOnClickListener {
                if (items.imageurls.toString().isNullOrEmpty()) {
                    Toast.makeText(mContext, "No Image Urls", Toast.LENGTH_LONG)
                } else {
                    imageClicklistner.imageData(position,
                        orderId,
                        items.itemname.toString(),
                        items.imageurls.toString())
                }

            }
        }



        holder.orderdetailsBinding.selectResonItem.setOnClickListener {
            holder.orderdetailsBinding.approveQtyText.setText("0")

            pendingFragmentCallback.onClickReason(pos, position, items.orderno)
        }

        holder.orderdetailsBinding.subtract.setOnClickListener {

            if (count <= 0) {

            } else {
                count--;
                holder.orderdetailsBinding.approveQtyText.setText(count.toString())
            }

        }

        holder.orderdetailsBinding.add.setOnClickListener {
            if (Integer.parseInt(holder.orderdetailsBinding.approveQtyText.text.toString()) >=qcaprqty !!
                !!

            ) {
                Toast.makeText(mContext,
                    "Approve quantity cannot be more than Required quantity",
                    Toast.LENGTH_LONG).show()
            } else {
                count++;
                holder.orderdetailsBinding.approveQtyText.setText(count.toString())
            }

        }


    }

    fun subtractDiscount(discount: Double): Double {
        if (discount >= 1.00) {
            return discount - 1.00
        }
        return discount
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    class ViewHolder(val orderdetailsBinding: QcOrderLayoutBinding) :
        RecyclerView.ViewHolder(orderdetailsBinding.root)
}

