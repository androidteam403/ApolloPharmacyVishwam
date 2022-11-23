package com.apollopharmacy.vishw


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.DialogAcceptQcBinding
import com.apollopharmacy.vishwam.databinding.DialogRejectQcBinding
import com.apollopharmacy.vishwam.databinding.QcFragmentPendingBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.RejectReasonsDialog
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.PendingFragmentCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.QcPendingViewModel
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter.QcPendingListAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewImageActivity
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utlis
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.apache.commons.collections4.ListUtils


class PendingFragment : BaseFragment<QcPendingViewModel, QcFragmentPendingBinding>(),
    MainActivityCallback,
    QcListsCallback, RejectReasonsDialog.ResaonDialogClickListner,
    QcFilterFragment.QcFilterClicked, PendingFragmentCallback {
    var dialogBinding: DialogRejectQcBinding? = null
    var adapter: QcPendingListAdapter? = null
    public var isBulkChecked: Boolean = false
    public var isBulk: Boolean = false

    var getPendingqcitemList: List<QcItemListResponse.Item>? = null
    var qcAccepttList = ArrayList<QcAcceptRejectRequest.Order>()
    var qcBundleAccepttList = ArrayList<QcAcceptRejectRequest.Order>()
    var dummyqcAccepttList = ArrayList<QcAcceptRejectRequest.Order>()

    var qcRejectList = ArrayList<QcAcceptRejectRequest.Order>()
    var itemsList = ArrayList<QcItemListResponse>()
    var bulkList = ArrayList<QcListsResponse.Pending>()
    var accpetbulkList = ArrayList<QcListsResponse.Pending>()
    var qcAcceptItemsList = ArrayList<QcAcceptRejectRequest.Item>()

    var qcRejectItemsList = ArrayList<QcAcceptRejectRequest.Item>()
    var orderId: String = ""
    var reason: String = "test"
    var qcreasonCode: String = ""
    private var filterPendingList = ArrayList<QcListsResponse.Pending>()
    var subList: java.util.ArrayList<java.util.ArrayList<QcListsResponse.Pending>>? = java.util.ArrayList()

    //     var reason= String
    var headerPos: Int? = -1
    var itemPos: Int? = -1
    var itemList = ArrayList<QcItemListResponse.Item>()
    var pos: Int = 0
    var pageNo: Int = 1
    var lastIndex = 0
    var increment: Int = 0
    var names = ArrayList<QcListsResponse.Pending>();

    override val layoutRes: Int
        get() = R.layout.qc_fragment_pending

    override fun retrieveViewModel(): QcPendingViewModel {
        return ViewModelProvider(this).get(QcPendingViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    override fun setup() {
        showLoading()
        MainActivity.mInstance.mainActivityCallback = this

        viewModel.getQcRejectionList()
        viewModel.getQcRegionList()
        viewModel.getQcStoreist()


        viewModel.getQcPendingList(Preferences.getToken(),
            "1 - Apr - 2019",
            Utlis.getCurrentDate("yyyy-MM-dd")!!,
            "",
            "")


        viewModel.qcAcceptRejectRequestList.observe(viewLifecycleOwner, {
            hideLoading()



            isBulkChecked = false
            viewBinding.bulkAppRejLayout.visibility = View.GONE

            var i: Int = 0

            if (isBulk) {


                while (i < names.size) {
                    if (names[i].isItemChecked) {
                        names.removeAt(i)
                        i = 0
                        adapter!!.notifyDataSetChanged()


                    } else {
                        i++
                        adapter!!.notifyDataSetChanged()
                    }
                }
            } else if (subList?.size!! > acceptOrRejectItemPos) {

//                var na = ArrayList<QcListsResponse.Pending>()
//                na.addAll(subList!!.get(increment))
//
//                na.removeAt(acceptOrRejectItemPos)
                var subListTemp = ArrayList<ArrayList<QcListsResponse.Pending>>()
                subListTemp.addAll(subList!!.toList()!!)
                subListTemp!!.get(increment).removeAt(acceptOrRejectItemPos)

                subList = subListTemp

//                subList!!.get(increment).removeAt(acceptOrRejectItemPos)

//                names.removeAt(acceptOrRejectItemPos)
                adapter!!.notifyDataSetChanged()
            }
        })



        viewModel.qcPendingItemsLists.observe(viewLifecycleOwner, Observer {
            hideLoading()




            it.setorderno(orderId)
            for (j in it.itemlist!!) {
                j.orderno = orderId
                j.approvedqty = j.qty
            }
            var isItemAdded = false;
            for (i in itemsList) {
                if (i.orderno.equals(orderId)) {
                    isItemAdded = true
                }
            }
            if (!isItemAdded) {
                itemsList.add(it)
            }
            adapter?.notifyDataSetChanged()


        })
        viewBinding.refreshSwipe.setOnRefreshListener {

            viewModel.getQcPendingList(Preferences.getToken(),
                "1 - Apr - 2019",
                Utlis.getCurrentDate("yyyy-MM-dd")!!,
                "",
                "")
        }


        viewModel.qcPendingLists.observe(viewLifecycleOwner, { it ->
            hideLoading()

            filterPendingList = (it.pendinglist as ArrayList<QcListsResponse.Pending>?)!!







//            subList = ListUtils.partition(it.pendinglist, 3)
            splitTheArrayList(it.pendinglist as ArrayList<QcListsResponse.Pending>?)
            pageNo = 1
            increment = 0
            if (pageNo == 1) {
                viewBinding.prevPage.visibility = View.GONE
            } else {
                viewBinding.prevPage.visibility = View.VISIBLE

            }
            if (increment == subList?.size!!.minus(1)) {
                viewBinding.nextPage.visibility = View.GONE
            } else {
                viewBinding.nextPage.visibility = View.VISIBLE

            }

            names = it.pendinglist as ArrayList<QcListsResponse.Pending>
            if (it.pendinglist.isNullOrEmpty()) {
                viewBinding.emptyList.visibility = View.VISIBLE
                viewBinding.recyclerViewPending.visibility = View.GONE
                viewBinding.continueBtn.visibility = View.GONE

                Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT).show()
            } else {
                viewBinding.refreshSwipe.isRefreshing = false
                viewBinding.emptyList.visibility = View.GONE

                viewBinding.recyclerViewPending.visibility = View.VISIBLE
                if (subList?.size == 1) {
                    viewBinding.continueBtn.visibility = View.GONE
                } else {
                    viewBinding.continueBtn.visibility = View.VISIBLE

                }
                viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")

                adapter =
                    context?.let { it1 ->
                        QcPendingListAdapter(it1,
                            subList!!.get(increment),
                            this,
                            itemsList, this)
                    }

            }
            viewBinding.recyclerViewPending.adapter = adapter


        })





        viewBinding.nextPage.setOnClickListener {
            if (increment < subList?.size?.minus(1)!!) {


                increment++
                pageNo++


                if (pageNo == 1) {
                    viewBinding.prevPage.visibility = View.GONE
                } else {
                    viewBinding.prevPage.visibility = View.VISIBLE

                }
                if (increment == subList?.size!!.minus(1)) {
                    viewBinding.nextPage.visibility = View.GONE
                } else {
                    viewBinding.nextPage.visibility = View.VISIBLE

                }
                viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")
                if (subList?.size == 1) {
                    viewBinding.continueBtn.visibility = View.GONE
                } else {
                    viewBinding.continueBtn.visibility = View.VISIBLE

                }
                adapter =
                    context?.let { it1 ->
                        QcPendingListAdapter(it1,
                            subList!!.get(increment),
                            this,
                            itemsList, this)
                    }
                viewBinding.recyclerViewPending.adapter = adapter
            } else {
                Toast.makeText(requireContext(), "No More Data To Load", Toast.LENGTH_SHORT).show()

            }
        }

        viewBinding.prevPage.setOnClickListener {

            if (increment > 0) {

                increment--
                pageNo--
                if (pageNo == 1) {
                    viewBinding.prevPage.visibility = View.GONE
                } else {
                    viewBinding.prevPage.visibility = View.VISIBLE

                }
                if (increment == subList?.size!!.minus(1)) {
                    viewBinding.nextPage.visibility = View.GONE
                } else {
                    viewBinding.nextPage.visibility = View.VISIBLE

                }
                viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")

                adapter =
                    context?.let { it1 ->
                        QcPendingListAdapter(it1,
                            subList!!.get(increment),
                            this,
                            itemsList, this)
                    }
                viewBinding.recyclerViewPending.adapter = adapter
            } else {

                Toast.makeText(requireContext(), "No More Data To Load", Toast.LENGTH_SHORT).show()
                viewBinding.prevPage.visibility = View.GONE

            }
        }


        viewBinding.selectAllLayout.setOnClickListener {


            if (isBulkChecked) {
                isBulkChecked = false
                for (item in subList?.get(increment)?.toList()!!) {
                    item.isItemChecked = false
                }
                viewBinding.bulkSelectCheck.setImageResource(R.drawable.qc_checkbox)
                viewBinding.bulkAppRejLayout.visibility = View.GONE
            } else {
                isBulkChecked = true
                for (item in subList?.get(increment)?.toList()!!) {
                    item.isItemChecked = true
                }
                viewBinding.bulkSelectCheck.setImageResource(R.drawable.qcright)
            }
            adapter?.notifyDataSetChanged()
        }





        viewBinding.acceptClick.setOnClickListener {
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
                isBulk = true
                for (i in names.indices) {
                    if (names[i].isItemChecked) {
                        val qcaccept = QcAcceptRejectRequest.Order(names[i].orderno,
                            names[i].status,
                            Preferences.getAppDesignation(),
                            Preferences.getToken(),
                            names[i].storeid,
                            qcAcceptItemsList)
                        qcBundleAccepttList.add(qcaccept)
                    }
                }
                showLoading()

                viewModel.getAcceptRejectResult(QcAcceptRejectRequest("ACCEPT",
                    "",
                    "",
                    qcBundleAccepttList))

                customDialog.dismiss()

            }

            if (dialogBinding != null) {
                dialogBinding.message.setText("You are accepting the multiple Orders" +
                        " for Qc Fail Do You Want to Proceed ?")
            }

            dialogBinding?.cancelButton?.setOnClickListener {
                customDialog.dismiss()
            }
        }

        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is Command.ShowQcButtonSheet -> {
                    var dialog = command.fragment.newInstance()
                    dialog = BottomSheetDialog(requireContext(), R.layout.qc_filter_layout)

                    activity?.supportFragmentManager?.let { dialog.show() }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 210) {
            if (resultCode == Activity.RESULT_OK) {

                if (data != null) {
                    showLoading()
                    viewModel.getQcPendingList(Preferences.getToken(),
                        data.getStringExtra("fromQcDate").toString(),
                        data.getStringExtra("toDate").toString(),
                        data.getStringExtra("siteId").toString(),
                        data.getStringExtra("regionId").toString())


                    if (data.getStringExtra("fromQcDate").toString()
                            .equals(Utlis.getDateSevenDaysEarlier("dd-MMM-yyyy")) && data.getStringExtra(
                            "toDate").toString()
                            .equals(Utlis.getCurrentDate("dd-MMM-yyyy")) && data.getStringExtra("regionId")
                            .toString().isNullOrEmpty()
                    ) {
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE
                    } else {
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.VISIBLE

                    }

                    if (data.getStringExtra("reset").toString().equals("reset")) {
                        showLoading()
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE

                        viewModel.getQcPendingList(Preferences.getToken(),
                            "01-Apr-2019",
                            Utlis.getCurrentDate("yyyy-MM-dd")!!,
                            "",
                            "")
                    }


                }


            }
        }
    }


    override fun orderno(position: Int, orderno: String) {
        showLoading()
        orderId = orderno
        viewModel.getQcPendingItemsList(orderno)


//        adapter?.notifyDataSetChanged()

    }


    fun splitTheArrayList(pendingList : ArrayList<QcListsResponse.Pending>?){
        subList?.clear()
        var pendingSubList: ArrayList<QcListsResponse.Pending>? = ArrayList()
var pageStartPos = 0;
        var pageEndPos = 5
        for (i in  pendingList!!){
            pendingSubList!!.add(i)
            if (pendingList.indexOf(i) == (pendingList.size-1)){
               var pendingListttt = pendingList.subList(pageStartPos, pendingList.size-1)// ArrayList<QcListsResponse.Pending>()

                subList!!.add(pendingListttt as java.util.ArrayList<QcListsResponse.Pending>)

//                subList!!.add(pendingSubList)
//                pendingSubList.clear()
            }else if ((pendingList.indexOf(i)+1) % 5 == 0){


                subList!!.add(pendingList.subList(pageStartPos, pageEndPos) as java.util.ArrayList<QcListsResponse.Pending>)
                pageStartPos = pageStartPos +5
                pageEndPos = pageEndPos  + 5

//                subList!!.add(pendingSubList)
//                pendingSubList.clear()
            }
        }
    }
    override fun notify(position: Int, orderno: String) {
        TODO("Not yet implemented")
    }

    override fun imageData(position: Int, orderno: String, itemName: String, imageUrl: String) {
        if (imageUrl.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Images Urls is empty", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(context, QcPreviewImageActivity::class.java)

            intent.putExtra("itemList", imageUrl)
            intent.putExtra("orderid", orderno)
            intent.putExtra("itemName", itemName)
            intent.putExtra("position", position)
            startActivity(intent)
        }
    }

    var acceptOrRejectItemPos = -1
    override fun accept(
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
    ) {

        acceptOrRejectItemPos = position
        qcRejectItemsList.clear()
        for (i in itemlist!!) {
            val rejItems = QcAcceptRejectRequest.Item()
            rejItems.itemid = i.itemid
            rejItems.qty = i.approvedqty
            rejItems.remarks = ""
            qcRejectItemsList.add(rejItems)

        }
        qcAccepttList.clear()
        val qcreject = QcAcceptRejectRequest.Order(orderno,
            status,
            Preferences.getAppDesignation(),
            Preferences.getToken(),
            storeId,
            qcRejectItemsList)
        qcAccepttList.add(qcreject)

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
//            viewModel.getQcPendingItemsList(orderId)
            viewModel.getAcceptRejectResult(QcAcceptRejectRequest("ACCEPT",
                remarks,
                "",
                qcAccepttList))

        }

        if (dialogBinding != null) {
            dialogBinding.message.setText("You are accepting the Order Id " +
                    orderno + " for QC Fail Do You Want to Proceed ?")
        }

        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun reject(
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
    ) {
        acceptOrRejectItemPos = position
        var isAllReasonsFound = true
        for (k in itemlist) {
            if (k.remarks.isNullOrEmpty()) {
                isAllReasonsFound = false
            }
        }


        if (isAllReasonsFound) {
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
            dialogBinding!!.submit.visibility = View.GONE
            dialogBinding!!.siteIdLayout.visibility = View.GONE
            dialogBinding!!.message.visibility = View.VISIBLE
            dialogBinding!!.buttomLayout.visibility = View.VISIBLE
            qcRejectItemsList.clear()
//        getRejectitemList = it.itemlist
            for (i in itemlist!!) {
                val rejItems = QcAcceptRejectRequest.Item()
                rejItems.itemid = i.itemid
                rejItems.qty = i.approvedqty
                rejItems.remarks = i.remarks
                qcRejectItemsList.add(rejItems)

            }

            qcRejectList.clear()
            val qcreject = QcAcceptRejectRequest.Order(orderno,
                status,
                Preferences.getAppDesignation(),
                Preferences.getValidatedEmpId(),
                storeId,
                qcRejectItemsList)
            qcRejectList.add(qcreject)


            dialogBinding?.message?.setText("You are rejecting the Order Id " +
                    orderno + " for QC Fail Do You Want to Proceed ?")

            dialogBinding?.yesBtn?.setOnClickListener {
                showLoading()
                customDialog.dismiss()
                if (itemsList != null) {
                    viewModel.getAcceptRejectResult(QcAcceptRejectRequest("REJECT",
                        reason,
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
        } else {
            Toast.makeText(context, "Please select reasons for all items", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun isChecked(array: List<QcListsResponse.Pending>, position: Int) {
        var pendingItemChecked: Boolean = false;
        var items = QcListsResponse.Pending()
        var count: Int = 0
        if (names[position].isItemChecked) {

            names[position].setisItemChecked(false)

        } else {
            names[position].setisItemChecked(true)
        }

        for (i in names.indices) {
            if (names[i].isItemChecked) {
                count++;
            }

        }

        if (count > 1) {
            viewBinding.bulkAppRejLayout.visibility = View.VISIBLE
        } else {

            viewBinding.bulkAppRejLayout.visibility = View.GONE

        }


        if (count < names.size) {
            isBulkChecked = false
            viewBinding.bulkSelectCheck.setImageResource(R.drawable.qc_checkbox)

        }
        if (count == names.size) {
            isBulkChecked = true
            viewBinding.bulkSelectCheck.setImageResource(R.drawable.qcright)

        }


        adapter?.notifyDataSetChanged()
    }

    override fun selectReason(gst: String) {
        //  reason = gst
        dialogBinding?.siteIdSelectQc?.setText(gst)

        for (i in itemsList) {
            if (i.orderno.equals(orderId)) {
                i.itemlist!!.get(itemPos!!).remarks = gst
            }
        }


        adapter?.notifyDataSetChanged()
    }

    override fun reasonCode(reasonCode: String) {
        qcreasonCode = reasonCode

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

    override fun onClickReason(headerPos: Int, itemPos: Int, orderId: String?) {
        this.headerPos = headerPos
        this.itemPos = itemPos
        this.orderId = orderId.toString()
        RejectReasonsDialog().apply {
            arguments =
                    //CustomDialog().generateParsedData(viewModel.getDepartmentData())
                RejectReasonsDialog().generateParsedData(viewModel.getReasons())
        }.show(childFragmentManager, "")
    }

    override fun onClickFilterIcon() {
//        val i = Intent(context, QcFilterActivity::class.java)
//        i.putExtra("activity", "reset")
//
//        startActivityForResult(i, 210)
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickQcFilterIcon() {
//        showLoading()

        val i = Intent(context, QcFilterActivity::class.java)
        i.putExtra("activity", "1")
        startActivityForResult(i, 210)


    }


}




