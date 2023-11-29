package com.apollopharmacy.vishwam.ui.home.qcfail.pending

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.ActivityQcPendingBinding
import com.apollopharmacy.vishwam.databinding.DialogAcceptQcBinding
import com.apollopharmacy.vishwam.databinding.DialogRejectQcBinding
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.RejectReasonsDialog
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter.QcApprovedOrderDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter.StatusAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.model.ActionResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcAcceptRejectRequest
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcAcceptRejectResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcListsResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter.QcPendingOrderDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.rejected.adapter.QcRejectedOrderDetailsAdapter
import com.apollopharmacy.vishwam.util.Utlis
import java.util.ArrayList

class QcPendingActivity : AppCompatActivity(), PendingActivityCallback, QcListsCallback,
    PendingFragmentCallback, RejectReasonsDialog.ResaonDialogClickListner {
    lateinit var activityQcPendingBinding: ActivityQcPendingBinding
    lateinit var viewModel: QcPendingActivityViewModel
    var itemsList = ArrayList<QcItemListResponse>()
    var statusList = ArrayList<ActionResponse>()
    var pendingList = ArrayList<QcListsResponse.Pending>()
    var qcAcceptItemsList = ArrayList<QcAcceptRejectRequest.Item>()
    var TIME = (1 * 6000).toLong()
    var qcAccepttList = ArrayList<QcAcceptRejectRequest.Order>()
    var isReasonsFound = true

    var qcRejectItemsList = ArrayList<QcAcceptRejectRequest.Item>()
    var orderId: String = ""
    var omsOrderno: String = ""
    var status: String = ""
    var reason: String = ""

    var qcRejectList = ArrayList<QcAcceptRejectRequest.Order>()

    var fragment: String = ""
    var rejectAdapter: QcRejectedOrderDetailsAdapter? = null

    var approvedAdapter: QcApprovedOrderDetailsAdapter? = null
    var statusAdapter: StatusAdapter? = null

    var adapter: QcPendingOrderDetailsAdapter? = null
    var dialogBinding: DialogRejectQcBinding? = null
    var headerPos: Int? = -1
    var itemPos: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityQcPendingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_qc_pending)
        setUp()

    }

    private fun setUp() {
        activityQcPendingBinding.callback = this
        viewModel = ViewModelProvider(this)[QcPendingActivityViewModel::class.java]
        if (intent != null) {
            viewModel.getQcRejectionList()
            itemsList = intent.getSerializableExtra("itemsList") as ArrayList<QcItemListResponse>
            orderId = intent.getStringExtra("orderNo").toString()
            fragment = intent.getStringExtra("fragment").toString()
            activityQcPendingBinding.orderid.setText(orderId)

            if (itemsList!=null){
                for (k in itemsList) {
                    if (k.orderno.equals(orderId)) {
                        for (i in k.itemlist!!.indices) {
                            if (k.itemlist!!.get(i).remarks.isNullOrEmpty()) {
                                isReasonsFound = false
                            }
                        }
                    }

                }
            }
            if (fragment.equals("approved")) {
                activityQcPendingBinding.status.visibility=View.VISIBLE

                activityQcPendingBinding.actionLayout.visibility = View.GONE
                activityQcPendingBinding.recyclerViewPending.visibility = View.GONE
                activityQcPendingBinding.recyclerViewApproved.visibility = View.VISIBLE
                activityQcPendingBinding.statusRecyleview.visibility = View.VISIBLE
                activityQcPendingBinding.recyclerViewReject.visibility = View.GONE

                activityQcPendingBinding.heaader.setBackgroundColor(ContextCompat.getColor(this, R.color.approved_list_qc))

                statusList = intent.getSerializableExtra("statusList") as ArrayList<ActionResponse>
                activityQcPendingBinding.listName.setText("Approved List")

                if (itemsList != null) {
                    for (i in itemsList.indices) {
                        if (itemsList[i].orderno.equals(orderId)) {

                            approvedAdapter = QcApprovedOrderDetailsAdapter(
                                this,
                                itemsList[i].itemlist!!, 0, this, orderId
                            )
                            activityQcPendingBinding.recyclerViewApproved.adapter = approvedAdapter



                            var totalPrices = 0.0
                            var discounts = 0.0


                            for (k in itemsList.get(i).itemlist!!.indices) {
                                if (itemsList.get(i).itemlist!!.get(k).qty != null && itemsList.get(
                                        i
                                    ).itemlist!!.get(k).price != null
                                ) {
                                    totalPrices =
                                        totalPrices + ((itemsList.get(i).itemlist!!.get(k).qty.toString()).toDouble() * itemsList.get(
                                            i
                                        ).itemlist!!.get(k).price!!)
                                    discounts =
                                        discounts + itemsList.get(i).itemlist!!.get(k).discamount!!

//                        discounts = discounts + ((k.qty.toString()).toDouble() * k.discamount!!)
                                }

                            }


                            if (totalPrices.toString().isNotEmpty()) {
                                activityQcPendingBinding.total.setText(
                                    " " + String.format(
                                        "%.2f", totalPrices
                                    )
                                )
                            } else {
                                activityQcPendingBinding.total.setText("-")
                            }

                            if (discounts.toString().isNotEmpty()) {
                                activityQcPendingBinding.discount.setText(
                                    " " + String.format(
                                        "%.2f", discounts
                                    )
                                )
                            } else {
                                activityQcPendingBinding.discount.setText("-")

                            }

                            var netPayment = totalPrices - discounts
                            if (netPayment.toString().isNotEmpty()) {

                                activityQcPendingBinding.payment.setText(
                                    " " + String.format(
                                        "%.2f", netPayment
                                    )
                                )
                            } else {
                                activityQcPendingBinding.payment.setText("-")
                            }






                        }
                    }

                }


                if (statusList != null) {
                    for (i in statusList.indices) {
                        if (statusList.get(i).order.equals(orderId)) {
                            statusAdapter = StatusAdapter(
                                this,
                                statusList.get(i).hsitorydetails as ArrayList<ActionResponse.Hsitorydetail>
                            )
                            activityQcPendingBinding.statusRecyleview.adapter = statusAdapter

                        }
                    }

                }


            }
            else if (fragment.equals("reject")) {
                activityQcPendingBinding.status.visibility=View.VISIBLE

                activityQcPendingBinding.actionLayout.visibility = View.GONE
                activityQcPendingBinding.recyclerViewPending.visibility = View.GONE
                activityQcPendingBinding.recyclerViewApproved.visibility = View.GONE
                activityQcPendingBinding.recyclerViewReject.visibility = View.VISIBLE

                activityQcPendingBinding.statusRecyleview.visibility = View.VISIBLE
                activityQcPendingBinding.heaader.setBackgroundColor(ContextCompat.getColor(this, R.color.reject_list_qc))

                statusList = intent.getSerializableExtra("statusList") as ArrayList<ActionResponse>
                activityQcPendingBinding.listName.setText("Rejected List")
                if (itemsList != null) {
                    for (i in itemsList.indices) {
                        if (itemsList[i].orderno.equals(orderId)) {
                            var totalPrices = 0.0
                            var discounts = 0.0
                            for (k in itemsList.get(i).itemlist!!.indices) {
                                if (itemsList.get(i).itemlist!!.get(k).qty != null && itemsList.get(
                                        i
                                    ).itemlist!!.get(k).price != null
                                ) {
                                    totalPrices =
                                        totalPrices + ((itemsList.get(i).itemlist!!.get(k).qty.toString()).toDouble() * itemsList.get(
                                            i
                                        ).itemlist!!.get(k).price!!)
                                    discounts =
                                        discounts + itemsList.get(i).itemlist!!.get(k).discamount!!

//                        discounts = discounts + ((k.qty.toString()).toDouble() * k.discamount!!)
                                }

                            }
                            if (totalPrices.toString().isNotEmpty()) {
                                activityQcPendingBinding.total.setText(
                                    " " + String.format(
                                        "%.2f", totalPrices
                                    )
                                )
                            } else {
                                activityQcPendingBinding.total.setText("-")
                            }

                            if (discounts.toString().isNotEmpty()) {
                                activityQcPendingBinding.discount.setText(
                                    " " + String.format(
                                        "%.2f", discounts
                                    )
                                )
                            } else {
                                activityQcPendingBinding.discount.setText("-")

                            }

                            var netPayment = totalPrices - discounts
                            if (netPayment.toString().isNotEmpty()) {

                                activityQcPendingBinding.payment.setText(
                                    " " + String.format(
                                        "%.2f", netPayment
                                    )
                                )
                                rejectAdapter = QcRejectedOrderDetailsAdapter(
                                    this,
                                    itemsList[i].itemlist!!, 0, this, orderId
                                )
                                activityQcPendingBinding.recyclerViewReject.adapter = rejectAdapter

                            } else {
                                activityQcPendingBinding.payment.setText("-")

                            }
                        }

                    }

                }


                if (statusList != null) {
                    for (i in statusList.indices) {
                        if (statusList.get(i).order.equals(orderId)) {
                            statusAdapter = StatusAdapter(
                                this,
                                statusList.get(i).hsitorydetails as ArrayList<ActionResponse.Hsitorydetail>
                            )
                            activityQcPendingBinding.statusRecyleview.adapter = statusAdapter

                        }
                    }

                }


            }
            else if (fragment.equals("pending")) {
                activityQcPendingBinding.status.visibility=View.GONE
                pendingList =
                    intent.getSerializableExtra("pendingList") as ArrayList<QcListsResponse.Pending>
                if (pendingList != null) {
                    for (i in pendingList.indices) {
                        if (orderId.equals(pendingList.get(i).orderno)) {
                            omsOrderno = pendingList.get(i).omsorderno.toString()
                            status = pendingList.get(i).status.toString()
                            activityQcPendingBinding.storeId.setText(pendingList.get(i).storeid)
                            activityQcPendingBinding.custmerName.setText(pendingList.get(i).custname)
                            activityQcPendingBinding.phoneNumber.setText(pendingList.get(i).mobileno)
                            activityQcPendingBinding.dcName.setText(pendingList.get(i).dcCode)

                            var reqDate = pendingList.get(i).requesteddate.toString()!!.substring(
                                0,
                                Math.min(pendingList.get(i).requesteddate.toString()!!.length, 10)
                            )
                            activityQcPendingBinding.postedDate.setText(Utlis.formatdate(reqDate))
                        }
                    }
                }
                if (itemsList != null) {
                    for (i in itemsList.indices) {
                        if (itemsList[i].orderno.equals(orderId)) {


                            var totalPrices = 0.0
                            var discounts = 0.0


                            for (k in itemsList.get(i).itemlist!!.indices) {
                                if (itemsList.get(i).itemlist!!.get(k).qty != null && itemsList.get(
                                        i
                                    ).itemlist!!.get(k).price != null
                                ) {
                                    totalPrices =
                                        totalPrices + ((itemsList.get(i).itemlist!!.get(k).qty.toString()).toDouble() * itemsList.get(
                                            i
                                        ).itemlist!!.get(k).price!!)
                                    discounts =
                                        discounts + itemsList.get(i).itemlist!!.get(k).discamount!!

//                        discounts = discounts + ((k.qty.toString()).toDouble() * k.discamount!!)
                                }

                            }


                            if (totalPrices.toString().isNotEmpty()) {
                                activityQcPendingBinding.total.setText(
                                    " " + String.format(
                                        "%.2f", totalPrices
                                    )
                                )
                            } else {
                                activityQcPendingBinding.total.setText("-")
                            }

                            if (discounts.toString().isNotEmpty()) {
                                activityQcPendingBinding.discount.setText(
                                    " " + String.format(
                                        "%.2f", discounts
                                    )
                                )
                            } else {
                                activityQcPendingBinding.discount.setText("-")

                            }

                            var netPayment = totalPrices - discounts
                            if (netPayment.toString().isNotEmpty()) {

                                activityQcPendingBinding.payment.setText(
                                    " " + String.format(
                                        "%.2f", netPayment
                                    )
                                )
                            } else {
                                activityQcPendingBinding.payment.setText("-")
                            }





                            adapter = QcPendingOrderDetailsAdapter(
                                this, this,
                                itemsList[i].itemlist, 0, this, orderId,
                            )
                            activityQcPendingBinding.recyclerViewPending.adapter = adapter

                        }
                    }

                }



                activityQcPendingBinding.actionLayout.visibility = View.VISIBLE
                activityQcPendingBinding.recyclerViewReject.visibility = View.GONE
                activityQcPendingBinding.heaader.setBackgroundColor(ContextCompat.getColor(this, R.color.pendency_list_qc))

                activityQcPendingBinding.listName.setText("Pending List")
                activityQcPendingBinding.recyclerViewPending.visibility = View.VISIBLE
                activityQcPendingBinding.recyclerViewApproved.visibility = View.GONE
                activityQcPendingBinding.statusRecyleview.visibility = View.GONE


            }

        }
        if (isReasonsFound){
            activityQcPendingBinding.rejectClick.alpha = 1F
            activityQcPendingBinding.rejectClick.setEnabled(true)
            activityQcPendingBinding.acceptClick.alpha = 0.5F
            activityQcPendingBinding.acceptClick.setEnabled(false)
        }else{
            activityQcPendingBinding.acceptClick.alpha = 1F
            activityQcPendingBinding.acceptClick.setEnabled(true)
            activityQcPendingBinding.rejectClick.alpha = 0.5F
            activityQcPendingBinding.rejectClick.setEnabled(false)
        }



    }

    override fun onClickBack() {
        finish()
    }

    override fun accept() {
        qcRejectItemsList.clear()
        for (j in itemsList.indices) {
            for (i in itemsList.get(j).itemlist!!) {

                val rejItems = QcAcceptRejectRequest.Item()
                rejItems.itemid = i.itemid
                if (i.approvedqty != null) {
                    rejItems.qty = i.approvedqty
                } else {
                    rejItems.qty = i.qty
                }
                rejItems.remarks = ""
                rejItems.recId = i.recid
                qcRejectItemsList.add(rejItems)

            }

        }
        qcAccepttList.clear()
        val qcreject = QcAcceptRejectRequest.Order(
            orderId,
            status,
            Preferences.getAppLevelDesignationQCFail(),
            Preferences.getToken(),
            activityQcPendingBinding.storeId.text.toString(),
            qcRejectItemsList
        )
        qcAccepttList.add(qcreject)

        val dialogBinding: DialogAcceptQcBinding? = DataBindingUtil.inflate(
            LayoutInflater.from(this), R.layout.dialog_accept_qc, null, false
        )
        val customDialog = android.app.AlertDialog.Builder(this, 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {
            Utlis.showLoading(this)
            dialogBinding.yesBtn.setEnabled(false)

            Handler().postDelayed({ dialogBinding.yesBtn.setEnabled(true) }, TIME)
            customDialog.dismiss()
//            viewModel.getQcPendingItemsList(orderId)
            viewModel.getAcceptRejectResult(
                QcAcceptRejectRequest("ACCEPT", "", "", qcAccepttList),
                this
            )

        }

        if (dialogBinding != null) {
            dialogBinding.message.setText(
                "You are accepting the Order Id " + omsOrderno + " for QC Fail Do You Want to Proceed ?"
            )
        }

        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun reject() {
        var isAllReasonsFound = true
        for (k in itemsList) {
            if (k.orderno.equals(orderId)) {
                for (i in k.itemlist!!.indices) {
                    if (k.itemlist!!.get(i).remarks.isNullOrEmpty()) {
                        isAllReasonsFound = false
                    }
                }
            }

        }


        if (isAllReasonsFound) {
            dialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this), R.layout.dialog_reject_qc, null, false
            )
            val customDialog = android.app.AlertDialog.Builder(this, 0).create()
            customDialog.apply {

                setView(dialogBinding?.root)
                setCancelable(false)
            }.show()
            dialogBinding!!.submit.visibility = View.GONE
            dialogBinding!!.siteIdLayout.visibility = View.GONE
            dialogBinding!!.message.visibility = View.VISIBLE
            dialogBinding!!.buttomLayout.visibility = View.VISIBLE
            qcRejectItemsList.clear()
//        getRejectitemList = it.itemlist
            for (k in itemsList) {
                if (k.orderno.equals(orderId)) {
                    for (i in k.itemlist!!.indices) {
                        val rejItems = QcAcceptRejectRequest.Item()
                        rejItems.itemid = k.itemlist!!.get(i).itemid
                        if (k.itemlist!!.get(i).approvedqty != null) {
                            rejItems.qty = k.itemlist!!.get(i).approvedqty
                        } else {
                            rejItems.qty = k.itemlist!!.get(i).qty
                        }
                        rejItems.remarks = k.itemlist!!.get(i).remarks
                        rejItems.recId = k.itemlist!!.get(i).recid
                        qcRejectItemsList.add(rejItems)

                    }

                }

            }


            qcRejectList.clear()
            val qcreject = QcAcceptRejectRequest.Order(
                orderId,
                status,
                Preferences.getAppLevelDesignationQCFail(),
                Preferences.getValidatedEmpId(),
                activityQcPendingBinding.storeId.text.toString(),
                qcRejectItemsList
            )
            qcRejectList.add(qcreject)


            dialogBinding?.message?.setText(
                "You are rejecting the Order Id " + omsOrderno + " for QC Fail Do You Want to Proceed ?"
            )

            dialogBinding?.yesBtn?.setOnClickListener {
                Utlis.showLoading(this)
                dialogBinding!!.yesBtn.setEnabled(false)

                Handler().postDelayed({ dialogBinding!!.yesBtn.setEnabled(true) }, TIME)
                customDialog.dismiss()
                if (itemsList != null) {
                    viewModel.getAcceptRejectResult(
                        QcAcceptRejectRequest(
                            "REJECT",
                            reason,
                            "REJ0001",
                            qcRejectList
                        ), this
                    )
                }
//            viewModel.getAcceptRejectResult(QcAcceptRejectRequest("REJECT", remarks, qcRejectList))

            }
            dialogBinding?.close?.setOnClickListener {
                customDialog.dismiss()
            }
            dialogBinding?.cancelButton?.setOnClickListener {
                customDialog.dismiss()
            }
        } else {
            Toast.makeText(this, "Please select reasons for all items", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onSuccessSaveAccept(acceptRejectList: QcAcceptRejectResponse) {
        Utlis.hideLoading()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    override fun orderno(position: Int, orderno: String) {
    }

    override fun notify(position: Int, orderno: String) {
    }

    override fun accept(
        view: View,
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
        omsOrderno: String,
    ) {
    }

    override fun reject(
        view: View,
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
        omsOrderno: String,
    ) {
    }

    override fun imageData(position: Int, orderno: String, itemName: String, imageUrl: String) {
        if (imageUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Images Urls is empty", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, QcPreviewImageActivity::class.java)

            intent.putExtra("itemList", imageUrl)
            intent.putExtra("orderid", orderno)
            intent.putExtra("itemName", itemName)
            intent.putExtra("position", position)
            startActivity(intent)
        }
    }

    override fun isChecked(
        array: List<QcListsResponse.Pending>,
        position: Int,
        pending: QcListsResponse.Pending,
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickReason(headerPos: Int, itemPos: Int, orderId: String?) {


        this.headerPos = headerPos
        this.itemPos = itemPos
        RejectReasonsDialog().apply {
            arguments =
                    //CustomDialog().generateParsedData(viewModel.getDepartmentData())
                RejectReasonsDialog().generateParsedData(viewModel.getReasons())
        }.show(supportFragmentManager, "")
    }

    override fun onNotify() {
        var isReasonsFound = true

        if (itemsList!=null){
            for (k in itemsList) {
                if (k.orderno.equals(orderId)) {
                    for (i in k.itemlist!!.indices) {
                        if (k.itemlist!!.get(i).remarks.isNullOrEmpty()) {
                            isReasonsFound = false
                        }
                    }
                }

            }
        }
        if (isReasonsFound){
            activityQcPendingBinding.rejectClick.alpha = 1F
            activityQcPendingBinding.rejectClick.setEnabled(true)
            activityQcPendingBinding.acceptClick.alpha = 0.5F
            activityQcPendingBinding.acceptClick.setEnabled(false)
        }else{
            activityQcPendingBinding.acceptClick.alpha = 1F
            activityQcPendingBinding.acceptClick.setEnabled(true)
            activityQcPendingBinding.rejectClick.alpha = 0.5F
            activityQcPendingBinding.rejectClick.setEnabled(false)
        }    }

    override fun onFailureGetPendingAndAcceptAndRejectList(message: String) {
        TODO("Not yet implemented")
    }

    override fun selectReason(gst: String) {
        dialogBinding?.siteIdSelectQc?.setText(gst)

        for (i in itemsList) {
            if (i.orderno.equals(orderId)) {
                i.itemlist!!.get(itemPos!!).remarks = gst
            }
        }


        adapter?.notifyDataSetChanged()
    }

    override fun reasonCode(reasonCode: String) {
        reason = reasonCode


    }
}

