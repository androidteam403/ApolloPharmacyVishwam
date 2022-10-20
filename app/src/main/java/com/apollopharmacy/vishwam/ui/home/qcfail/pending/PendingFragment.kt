package com.apollopharmacy.vishw


import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.RejectReasonsDialog
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.QcPendingViewModel
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter.QcPendingListAdapter
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity.TAG


class PendingFragment : BaseFragment<QcPendingViewModel, QcFragmentPendingBinding>(),
    QcListsCallback, RejectReasonsDialog.ResaonDialogClickListner,
    QcFilterFragment.QcFilterClicked {
    var dialogBinding: DialogRejectQcBinding? = null
    var adapter: QcPendingListAdapter? = null
    public var isBulkChecked: Boolean = false
    var getPendingqcitemList: List<QcItemListResponse.Item>? = null
    var qcAccepttList = ArrayList<QcAcceptRejectRequest.Order>()
    var qcRejectList = ArrayList<QcAcceptRejectRequest.Order>()
    var getitemList: List<QcItemListResponse>? = null
    var itemsList = ArrayList<QcItemListResponse>()
    var getRejectitemList: List<QcItemListResponse.Item>? = null

    var qcRejectItemsList = ArrayList<QcAcceptRejectRequest.Item>()
    var orderId: String = ""
    var reason: String = ""
    var qcreason: String = ""
//     var reason= String

    var itemList = ArrayList<QcItemListResponse.Item>()

    var names = ArrayList<QcListsResponse.Pending>();

    override val layoutRes: Int
        get() = R.layout.qc_fragment_pending

    override fun retrieveViewModel(): QcPendingViewModel {
        return ViewModelProvider(this).get(QcPendingViewModel::class.java)
    }

    override fun setup() {
        showLoading()
        viewModel.getQcRejectionList()
        viewModel.getQcRegionList()
        viewModel.getQcStoreist()
        viewModel.getQcPendingList("APL48627", "2022-10-01", "2022-10-19", "16001", "")



        val qcreject = QcAcceptRejectRequest.Order("RV000012",
            "10",
            "EXECUTIVE",
            "APL48627",
            "16001",
            qcRejectItemsList)
        qcRejectList.add(qcreject)


        viewModel.qcAcceptRejectRequestList.observe(viewLifecycleOwner, {
            hideLoading()
        })




        viewModel.qcPendingItemsLists.observe(viewLifecycleOwner, Observer {
            hideLoading()
            getitemList = listOf(it)
            val getQcItemListResponse: QcItemListResponse
            getQcItemListResponse = it
            getitemList = listOf(getQcItemListResponse)
            for (i in getitemList!!) {
                val items = QcItemListResponse()
                items.itemlist = i.itemlist
                items.setorderno(orderId)
                items.status = i.status
                itemsList.add(items)
                adapter?.notifyDataSetChanged()
            }


            val itemsList: QcItemListResponse.Item
            getRejectitemList = it.itemlist
            for (i in getRejectitemList!!) {
                val rejItems = QcAcceptRejectRequest.Item()
                rejItems.itemid = i.itemid
                rejItems.qty = 1
                rejItems.remarks = "Qty Mismatch"
                qcRejectItemsList.add(rejItems)

            }


            adapter?.notifyDataSetChanged()


        })



        viewModel.qcPendingLists.observe(viewLifecycleOwner, { it ->
            hideLoading()
            names = it.pendinglist as ArrayList<QcListsResponse.Pending>
            if (it.pendinglist.isNullOrEmpty()) {
                viewBinding.emptyList.visibility = View.VISIBLE
                viewBinding.recyclerViewPending.visibility = View.GONE
                Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT).show()
            } else {
                adapter = context?.let { it1 ->
                    QcPendingListAdapter(it1,it.pendinglist as ArrayList<QcListsResponse.Pending>,
                        this,
                        itemsList)
                }
            }
            viewBinding.recyclerViewPending.adapter = adapter


        })



        viewBinding.filter.setOnClickListener {
            viewModel.filterClicked()

        }
        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is Command.ShowQcButtonSheet -> {
                    var dialog = command.fragment.newInstance()
                    dialog.arguments = command.arguments

                    dialog.setTargetFragment(this, 0)
                    activity?.supportFragmentManager?.let { dialog.show(it, "") }
                }
                is Command.ShowToast -> {
                    hideLoading()
                    if (command.message.equals("no data found.please check empid")) {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.bulkAppRejLayout.visibility = View.GONE
                        viewBinding.recyclerViewPending.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


//    class PendingRecycleView(
//        var orderData: ArrayList<QcListsResponse.Pending>,
//        val imageClicklistner: QcImagesListner,
//        var qcItemList: ArrayList<QcItemListResponse>,
//        var qcrejList: ArrayList<QcAcceptRejectRequest.Order>,
//
//
//        ) :
//
//        SimpleRecyclerView<QcPendingLayoutBinding, QcListsResponse.Pending>(orderData,
//            R.layout.qc_pending_layout) {
//        override fun bindItems(
//            binding: QcPendingLayoutBinding,
//            items: QcListsResponse.Pending,
//            position: Int,
//        ) {
//            val ischecked: Boolean = false
//            var value: Double = 0.0
//
//            if (items.dcCode == null) {
//                binding.locationText.setText("-")
//
//            } else {
//                binding.locationText.setText("-"+items.dcCode)
//                              }
//
//            if (items.storeid == null) {
//                binding.storeId.setText("-")
//
//            } else {
//                binding.storeId.setText(items.storeid)
//            }
////            binding.storeId.setText(items.storeid)
//            if (items.qcfaildate == null) {
//                binding.postedDate.setText("-")
//
//            } else {
//                binding.postedDate.setText(items.qcfaildate!!.substring(0,
//                    Math.min(items.qcfaildate!!.length, 10)))            }
//            binding.reqDate.setText(items.requesteddate!!.substring(0,
//                Math.min(items.requesteddate!!.length, 10)))
////            binding.locationText.setText(items.dcCode)
//            binding.custName.setText(items.custname)
////        binding.storeNameText.setText(items.omsorderno)
//            binding.custNumber.setText("-" + items.mobileno)
//            binding.orderid.setText(items.orderno)
//
//            binding.acceptClick.setOnClickListener {
//
//                imageClicklistner.accept(position,
//                    items.orderno.toString(),
//                    binding.writeRemarks.text.toString())
//            }
//
//            binding.rejectClick.setOnClickListener {
//                imageClicklistner.reject(position,
//                    items.orderno.toString(),
//                    binding.writeRemarks.text.toString())
//            }
//
//            if (qcItemList.isNotEmpty()) {
//
//                var item = QcItemListResponse()
//                for (i in qcItemList) {
//                    if (i.orderno!!.equals(orderData.get(position).orderno)) {
//                        item = i
//                    }
//                }
//                var totalPrice = ArrayList<Double>()
//                var discount = ArrayList<Double>()
//                var qty = ArrayList<Double>()
//
////        var k= items.itemlist?.size
//                if (item.itemlist?.isNotEmpty() == true) {
//                    for (k in item.itemlist!!) {
//                        var items: Double
//                        var disc: Double
//                        var qt: Double
//
//                        items = (k.price!!)
//                        totalPrice.add(items)
//                        disc= k.discamount!!
//                        discount.add(disc)
//                        if (k.qty!==null){
//                            qt= k.qty as Double
//                            qty.add(qt)
//
//                        }
//
//
//                    }
//                }
//                if (totalPrice.isNotEmpty()) {
//                    binding.totalCost.setText(totalPrice.sum().toString())
//                }
//
//                if (discount.isNotEmpty()) {
//                    binding.discountTotal.setText(discount.sum().toString())
//                }
//                if (qty.isNotEmpty()) {
//                    value=qty.sum()
//                    binding.remainingPayment.setText(String.format("%.2f",(value*totalPrice.sum())-discount.sum()).toString())
//                }
//
//                binding.recyclerView.adapter =
//                    item.itemlist?.let {
//                        OrderDetailsRecycleView(it,
//                            position,
//                            orderData,
//                            qcrejList)
//                    }
//                binding.recyclerView.scrollToPosition(position)
//            }
//
//            if (orderData[position].isItemChecked) {
//                binding.checkBox.setImageResource(R.drawable.qcright)
//            } else {
//                binding.checkBox.setImageResource(R.drawable.qc_checkbox)
//            }
//
//
//            binding.checkBox.setOnClickListener {
//                imageClicklistner.isChecked(orderData, position)
//            }
//
//
//            binding.arrow.setOnClickListener {
//                items.orderno?.let { imageClicklistner.orderno(position, it) }
//                binding.arrowClose.visibility = View.VISIBLE
//                binding.arrow.visibility = View.GONE
//                binding.extraData.visibility = View.VISIBLE
//
//
//            }
//
//            binding.arrowClose.setOnClickListener {
//                binding.arrowClose.visibility = View.GONE
//                binding.arrow.visibility = View.VISIBLE
//                binding.extraData.visibility = View.GONE
//
//            }
//
//        }
//    }
//
//
//    class OrderDetailsRecycleView(
//        var itemsList: List<QcItemListResponse.Item>,
//        var pos: Int,
//        var qcList: ArrayList<QcListsResponse.Pending>,
//        var qcrejList: ArrayList<QcAcceptRejectRequest.Order>,
//
//
//        ) :
//        SimpleRecyclerView<QcOrderLayoutBinding, QcItemListResponse.Item>(
//            itemsList,
//            R.layout.qc_order_layout
//        ) {
//        override fun bindItems(
//            binding: QcOrderLayoutBinding,
//            items: QcItemListResponse.Item,
//            position: Int,
//        ) {
//
//            if (items != null)
//
//                binding.quantityText.setText(items.qty.toString())
//            binding.medicineName.setText(items.itemname)
//            binding.categoryName.setText("- " + items.category)
//            binding.price.setText(items.price.toString())
////            binding.discountTotal.setText(items.discamount.toString())
//
//
//        }
//    }


    override fun orderno(position: Int, orderno: String) {
        showLoading()
        orderId = orderno
        viewModel.getQcPendingItemsList(orderno)



        adapter?.notifyDataSetChanged()

    }

    override fun notify(position: Int, orderno: String) {
        TODO("Not yet implemented")
    }

    override fun accept(position: Int, orderno: String, remarks: String) {


        val dialogBinding: DialogAcceptQcBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_accept_qc,
                null,
                false
            )
        val customDialog = android.app.AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {
            showLoading()
            customDialog.dismiss()
//            viewModel.getAcceptRejectResult(QcAcceptRejectRequest("ACCEPT", remarks, qcRejectList))

        }

        if (dialogBinding != null) {
            dialogBinding.message.setText("You are accepting the Order Id " +
                    orderno + " for QC Fail Do You Want to Proceed ?")
        }

        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun reject(position: Int, orderno: String, remarks: String) {
        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
            R.layout.dialog_reject_qc,
            null,
            false
        )
        val customDialog = android.app.AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()

        dialogBinding?.submit?.setOnClickListener {
            if (dialogBinding!!.siteIdSelectQc.text.isNotEmpty()) {
                dialogBinding!!.submit.visibility = View.GONE
                dialogBinding!!.siteIdLayout.visibility = View.GONE
                dialogBinding!!.message.visibility = View.VISIBLE
                dialogBinding!!.buttomLayout.visibility = View.VISIBLE
            }


        }

        dialogBinding?.siteIdSelectQc?.setOnClickListener {
            RejectReasonsDialog().apply {
                arguments =
                        //CustomDialog().generateParsedData(viewModel.getDepartmentData())
                    RejectReasonsDialog().generateParsedData(viewModel.getReasons())
            }.show(childFragmentManager, "")

        }



        dialogBinding?.message?.setText("You are rejecting the Order Id " +
                orderno + " for QC Fail Do You Want to Proceed ?")

        dialogBinding?.yesBtn?.setOnClickListener {
            showLoading()
            customDialog.dismiss()
            if (itemsList != null) {
                viewModel.getAcceptRejectResult(QcAcceptRejectRequest("REJECT",
                    "QTYMISMATCH",
                    "REJ0001",
                    qcRejectList))
            }
//            viewModel.getAcceptRejectResult(QcAcceptRejectRequest("REJECT", remarks, qcRejectList))

        }
        dialogBinding?.close?.setOnClickListener {
            customDialog.dismiss()
        }
        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun isChecked(array: ArrayList<QcListsResponse.Pending>, position: Int) {
        if (array[position].isItemChecked) {
            names[position].setisItemChecked(false)
        } else {
            names[position].setisItemChecked(true)
        }
        adapter?.notifyDataSetChanged()
    }

    override fun selectReason(gst: String) {
        reason = gst
        qcreason = gst
        dialogBinding?.siteIdSelectQc?.setText(gst)
        adapter?.notifyDataSetChanged()
    }




    override fun clickedApply(
        selectedData: String,
        data: ArrayList<QcStoreList.Store>,
        regiondata: ArrayList<QcRegionList.Store>,
        tag: Int,
        toDate: String,
    ) {

//        viewModel.getQcPendingList("APL48627", selectedData, toDate, data[0].siteid.toString(), "")
    }


}




