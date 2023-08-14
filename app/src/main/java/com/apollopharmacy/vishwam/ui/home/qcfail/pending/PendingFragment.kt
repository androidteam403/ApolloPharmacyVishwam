package com.apollopharmacy.vishw


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.DialogAcceptQcBinding
import com.apollopharmacy.vishwam.databinding.DialogRejectQcBinding
import com.apollopharmacy.vishwam.databinding.QcFragmentPendingBinding
import com.apollopharmacy.vishwam.dialog.QcListSizeDialog
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
import java.text.SimpleDateFormat
import java.util.*


class PendingFragment : BaseFragment<QcPendingViewModel, QcFragmentPendingBinding>(),
    MainActivityCallback, QcListsCallback, RejectReasonsDialog.ResaonDialogClickListner,
    QcFilterFragment.QcFilterClicked, PendingFragmentCallback,
    QcListSizeDialog.GstDialogClickListners, Filterable {
    var dialogBinding: DialogRejectQcBinding? = null
    var adapter: QcPendingListAdapter? = null
    public var isBulkChecked: Boolean = false
    public var isBulk: Boolean = false
    public var orderTypeList = ArrayList<String>()

    public var storeList = ArrayList<String>()
    public var regionList = ArrayList<String>()
    var pageSize: Int = 0
    var charString: String? = ""
    var siteId: String = ""
    var regionId: String = ""
    private var pendingListMain = ArrayList<QcListsResponse.Pending>()
    var storeIdList = ArrayList<String>()
    var regionIdList = ArrayList<String>()

    private var pendingListList = ArrayList<QcListsResponse.Pending>()
    private var pendingFilterList = ArrayList<QcListsResponse.Pending>()
    var qcListsResponse: QcListsResponse? = null
    var qcListsResponseForFilter: QcListsResponse? = null
    var TIME = (1 * 6000).toLong()
    var pageSizeList = ArrayList<String>()
    var getPendingqcitemList: List<QcItemListResponse.Item>? = null
    var qcAccepttList = ArrayList<QcAcceptRejectRequest.Order>()
    var qcBundleAccepttList = ArrayList<QcAcceptRejectRequest.Order>()
    var dummyqcAccepttList = ArrayList<QcAcceptRejectRequest.Order>()

    var qcRejectList = ArrayList<QcAcceptRejectRequest.Order>()
    var itemsList = ArrayList<QcItemListResponse>()
    var bulkList = ArrayList<QcListsResponse.Pending>()


    var qcAcceptItemsList = ArrayList<QcAcceptRejectRequest.Item>()

    var qcRejectItemsList = ArrayList<QcAcceptRejectRequest.Item>()
    var orderId: String = ""
    var reason: String = "test"
    var qcreasonCode: String = ""
    private var filterPendingList = ArrayList<QcListsResponse.Pending>()
    var subList: java.util.ArrayList<java.util.ArrayList<QcListsResponse.Pending>>? =
        java.util.ArrayList()

    //    var subListTempFilter: java.util.ArrayList<QcListsResponse.Pending>? =
//        java.util.ArrayList()
    public var subListTempFilter = ArrayList<QcListsResponse.Pending>()

    //     var reason= String
    var headerPos: Int? = -1
    var itemPos: Int? = -1
    var itemList = ArrayList<QcItemListResponse.Item>()
    var pos: Int = 0
    var pageNo: Int = 1
    var lastIndex = 0
    var increment: Int = 0
    var names = ArrayList<QcListsResponse.Pending>();
    var selectedCount: Int = 0
    var typeString = ""
    var currentDate = String()
    var fromDate = String()


    override val layoutRes: Int
        get() = R.layout.qc_fragment_pending

    override fun retrieveViewModel(): QcPendingViewModel {
        return ViewModelProvider(this).get(QcPendingViewModel::class.java)
    }

    @SuppressLint("ResourceType", "SetTextI18n")
    override fun setup() {
        pageSize = Preferences.getQcPendingPageSiz()
//        MainActivity.mInstance.updateQcListCount(Preferences.getQcPendingPageSiz().toString())
        viewBinding.selectfiltertype.setText(
            "Per page: " + Preferences.getQcPendingPageSiz().toString()
        )
        Preferences.setQcFromDate("")
        Preferences.setQcToDate("")
        Preferences.setQcSite("")
        Preferences.setQcOrderType("")
        Preferences.setQcRegion("")
        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE
        MainActivity.mInstance.qcfilterIcon.visibility = View.VISIBLE
        viewBinding.searchView.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))
        showLoading()
        MainActivity.mInstance.mainActivityCallback = this
        pageSizeList.add("5")
        pageSizeList.add("10")
        pageSizeList.add("15")
//
        viewBinding.selectfiltertype.setOnClickListener {
            QcListSizeDialog().apply {
                arguments = QcListSizeDialog().generateParsedData(pageSizeList)
            }.show(childFragmentManager, "")
        }




        viewModel.getQcRejectionList()
//        viewModel.getQcRegionList()
//        viewModel.getQcStoreist()
//        Preferences.setQcToDate(Utlis.getCurrentDate("dd-MMM-yyy")!!)
//        Preferences.setQcFromDate("1-Apr-2019")
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        currentDate = simpleDateFormat.format(Date())

        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        fromDate = simpleDateFormat.format(cal.time)

        viewModel.getQcPendingList(
            Preferences.getToken(), "1-Apr-2019", currentDate, "", "", this
        )



        viewBinding.closeArrow.setOnClickListener {
            viewBinding.searchView.setText("")
        }



        viewBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 2) {
                    viewBinding.closeArrow.visibility = View.VISIBLE

                    viewBinding.close.visibility = View.GONE
//                    if (adapter != null) {
                    getFilter()!!.filter(s)
//                    }
                }
//                else if (viewBinding.searchView.getText().toString().equals("")) {
//                    viewBinding.close.visibility = View.VISIBLE
//                    viewBinding.closeArrow.visibility = View.GONE
//
////                    if (adapter != null) {
//
//                       getFilter()!!.filter("")
////                    }
//               }
                else {
                    viewBinding.close.visibility = View.VISIBLE
                    viewBinding.closeArrow.visibility = View.GONE

//                    if (adapter != null) {
                    getFilter()!!.filter("")
//                    }
                }
            }
        })


        viewModel.qcAcceptRejectRequestList.observe(viewLifecycleOwner) {
            hideLoading()
            isBulkChecked = false
            viewBinding.bulkAppRejLayout.visibility = View.GONE
            var i: Int = 0
            if (isBulk) {
                viewModel.getQcPendingList(
                    Preferences.getToken(),
                    "1 - Apr - 2019",
                    Utlis.getCurrentDate("yyyy-MM-dd")!!,
                    "",
                    "",
                    this
                )
//
//                while (i < names.size) {
//                    if (names[i].isItemChecked) {
//                        names.removeAt(i)
//                        i = 0
//                    } else {
//                        i++
//                    }
//                }
//                var pos: Int = 0
//                while (pos < subList!!.get(increment).size) {
//                    if (subList!!.get(increment)[pos].isItemChecked) {
//                        subList!!.get(increment).removeAt(pos)
//                        pos = 0
//                    } else {
//                        pos++
//                    }
//                }
//                adapter!!.notifyDataSetChanged()
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
        }



        viewModel.qcPendingItemsLists.observe(viewLifecycleOwner, Observer {
            hideLoading()
            it.setorderno(orderId)
            for (j in it.itemlist!!) {
                j.orderno = orderId

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
            Preferences.setQcFromDate("")
            Preferences.setQcToDate("")
            Preferences.setQcSite("")
            siteId = ""
            regionId = ""
            Preferences.setQcRegion("")
            Preferences.setQcOrderType("")
            typeString = ""
            pendingListList.clear()
            pendingListMain.clear()
            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
            currentDate = simpleDateFormat.format(Date())

            MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE
            viewModel.getQcPendingList(
                Preferences.getToken(), "1-Apr-2019", currentDate, "", "", this
            )
        }




        viewModel.qcPendingLists.observe(viewLifecycleOwner) { it ->
            qcListsResponse = it
            pendingListList = it.pendinglist!!
            pendingListMain = it.pendinglist!!

            setQcPedningListResponse(it.pendinglist!!)


        }





        viewBinding.nextPage.setOnClickListener {
            if (increment < subList?.size?.minus(1)!!) {
                increment++
                pageNo++


                if (pageNo == 1) {
                    viewBinding.prevPage.visibility = View.INVISIBLE
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
                for (i in subList!!.get(increment)) {
                    i.setisItemChecked(false)
                }
                for (i in names) {
                    if (i.isItemChecked) {
                        i.setisItemChecked(false)
                    }
                }
                adapter = context?.let { it1 ->
                    QcPendingListAdapter(it1, subList!!.get(increment), this, itemsList, this)
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
                    viewBinding.prevPage.visibility = View.INVISIBLE
                } else {
                    viewBinding.prevPage.visibility = View.VISIBLE

                }
                if (increment == subList?.size!!.minus(1)) {
                    viewBinding.nextPage.visibility = View.GONE
                } else {
                    viewBinding.nextPage.visibility = View.VISIBLE

                }
                viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")
                for (i in subList!!.get(increment)) {
                    i.setisItemChecked(false)
                }
                for (i in names) {
                    if (i.isItemChecked) {
                        i.setisItemChecked(false)
                    }
                }
                adapter = context?.let { it1 ->
                    QcPendingListAdapter(it1, subList!!.get(increment), this, itemsList, this)
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

            it.alpha = 0.5F

            it.setEnabled(false)

            Handler().postDelayed(Runnable {
                it.setEnabled(true)
                it.alpha = 1F
            }, TIME)


            val dialogBinding: DialogAcceptQcBinding? = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()), R.layout.dialog_accept_qc, null, false
            )
            val customDialog = android.app.AlertDialog.Builder(requireContext(), 0).create()
            customDialog.apply {

                setView(dialogBinding?.root)
                setCancelable(false)
            }.show()
            dialogBinding?.yesBtn?.setOnClickListener {
                dialogBinding.yesBtn.setEnabled(false)

                Handler().postDelayed(Runnable { dialogBinding.yesBtn.setEnabled(true) }, TIME)
                isBulk = true
                for (i in names.indices) {
                    if (names[i].isItemChecked) {
                        val qcaccept = QcAcceptRejectRequest.Order(
                            names[i].orderno,
                            names[i].status,
                            Preferences.getAppLevelDesignationQCFail(),
                            Preferences.getToken(),
                            names[i].storeid,
                            qcAcceptItemsList
                        )
                        qcBundleAccepttList.add(qcaccept)
                    }
                }
                showLoading()
                viewModel.getAcceptRejectResult(
                    QcAcceptRejectRequest(
                        "ACCEPT", "", "", qcBundleAccepttList
                    )
                )

                customDialog.dismiss()

            }

            if (dialogBinding != null) {
                dialogBinding.message.setText("You are accepting the multiple Orders" + " for Qc Fail Do You Want to Proceed ?")
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

                else -> {}
            }
        }
    }

    fun setQcPedningListResponse(qcPendingList: ArrayList<QcListsResponse.Pending>) {
//    pendingListList= qcListsResponse!!.pendinglist!!
        viewBinding.refreshSwipe.isRefreshing = false
        storeList.clear()
        orderTypeList.clear()
        regionList.clear()
        hideLoading()
        if (qcPendingList.isNullOrEmpty()) {
            viewBinding.emptyList.visibility = View.VISIBLE
            viewBinding.recyclerViewPending.visibility = View.GONE
            viewBinding.continueBtn.visibility = View.GONE

//                Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT).show()
        } else {

            filterPendingList = (qcPendingList as ArrayList<QcListsResponse.Pending>?)!!

            for (i in filterPendingList.indices) {


                orderTypeList.add(filterPendingList[i].omsorderno.toString())

                storeList.add(filterPendingList[i].storeid.toString())
                regionList.add(filterPendingList[i].dcCode.toString())
            }
            val regionListSet: MutableSet<String> = LinkedHashSet()
            val stroreListSet: MutableSet<String> = LinkedHashSet()
            stroreListSet.addAll(storeList)
            regionListSet.addAll(regionList)
            storeList.clear()
            regionList.clear()
            regionList.addAll(regionListSet)
            storeList.addAll(stroreListSet)

//            subList = ListUtils.partition(it.pendinglist, 3)
            filterbyOrderType(qcPendingList)

            splitTheArrayList(filterbyOrderType(qcPendingList) as ArrayList<QcListsResponse.Pending>?)

//                splitTheArrayList(it.pendinglist as ArrayList<QcListsResponse.Pending>?)
            pageNo = 1
            increment = 0
            if (pageNo == 1) {
                viewBinding.prevPage.visibility = View.INVISIBLE
            } else {
                viewBinding.prevPage.visibility = View.VISIBLE

            }
            if (increment == subList?.size!!.minus(1)) {
                viewBinding.nextPage.visibility = View.GONE
            } else {
                viewBinding.nextPage.visibility = View.VISIBLE

            }

            names = qcPendingList as ArrayList<QcListsResponse.Pending>
            viewBinding.emptyList.visibility = View.GONE

            viewBinding.recyclerViewPending.visibility = View.VISIBLE
            if (subList?.size == 1) {
                viewBinding.continueBtn.visibility = View.GONE
            } else {
                viewBinding.continueBtn.visibility = View.VISIBLE

            }
            viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")

            adapter = context?.let { it1 ->
                QcPendingListAdapter(it1, subList!!.get(increment), this, itemsList, this)
            }
        }
        viewBinding.recyclerViewPending.adapter = adapter
    }

    @SuppressLint("SuspiciousIndentation")
    fun filterbyOrderType(pendinglist: ArrayList<QcListsResponse.Pending>): ArrayList<QcListsResponse.Pending> {
        var orderTypeFilteredPendinglist = ArrayList<QcListsResponse.Pending>()
        var storeList: List<String>
        var regionList: List<String>
        if (regionId.isNotEmpty()) {
            regionList = regionId.split(",")
            if (regionList.size > 1) {


                regionIdList = regionList as ArrayList<String>

            }
        }
        if (siteId.isNotEmpty()) {
            storeList = siteId.split(",")
            if (storeList.size > 1) {


                storeIdList = storeList as ArrayList<String>

            }
        }



        if (typeString.isNotEmpty()&&regionId.isEmpty()&&siteId.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {
            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                if (typeString.equals("FORWARD RETURN") && omsOrderno.contains("FL")) {
                    orderTypeFilteredPendinglist.add(i)
                } else if (typeString.equals("REVERSE RETURN") && omsOrderno.contains("RT")) {
                    orderTypeFilteredPendinglist.add(i)
                }
            }
            return orderTypeFilteredPendinglist
        }
        else if (storeIdList.isEmpty()&&siteId.isNotEmpty()&&regionId.isEmpty()&&typeString.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode


                if (site.contains(siteId)) {
                    orderTypeFilteredPendinglist.add(i)

                }

            }
            return orderTypeFilteredPendinglist
        }
        else if (regionIdList.isEmpty()&&regionId.isNotEmpty()&&siteId.isEmpty()&&typeString.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode


                if (region!!.contains(regionId)) {
                    orderTypeFilteredPendinglist.add(i)

                }

            }
            return orderTypeFilteredPendinglist
        }
        else if (storeIdList.isEmpty() && regionIdList.isEmpty()&&typeString.isNotEmpty()&&siteId.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode
                if (typeString.equals("FORWARD RETURN") && omsOrderno.contains("FL") && site.contains(
                        siteId
                    ) && region!!.contains(regionId)
                ) {
                    orderTypeFilteredPendinglist.add(i)
                } else if (typeString.equals("REVERSE RETURN") && omsOrderno.contains("RT") && site.contains(
                        siteId
                    ) && region!!.contains(region)
                ) {
                    orderTypeFilteredPendinglist.add(i)

                }

            }
            return orderTypeFilteredPendinglist
        }
        else if (typeString.isNotEmpty() && storeIdList.size > 1 && regionIdList.size > 1&&fromDate.isEmpty()&&currentDate.isEmpty()) {
            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode
                for (j in storeIdList.indices) {

                    for (k in regionIdList.indices) {


                        if (typeString.equals("FORWARD RETURN") && omsOrderno.contains("FL") && site.contains(
                                storeIdList.get(j)
                            ) && region!!.contains(regionIdList.get(k))
                        ) {
                            orderTypeFilteredPendinglist.add(i)
                        } else if (typeString.equals("REVERSE RETURN") && omsOrderno.contains("RT") && site.contains(
                                storeIdList.get(j)
                            ) && region!!.contains(regionIdList.get(k))
                        ) {
                            orderTypeFilteredPendinglist.add(i)

                        }

                    }
                }
            }
            return orderTypeFilteredPendinglist
        }
        else if (regionIdList.size > 1&&typeString.isEmpty()&&siteId.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode

                for (j in regionIdList.indices) {


                    if (region!!.contains(regionIdList.get(j))) {
                        orderTypeFilteredPendinglist.add(i)

                    }
                }
            }
            return orderTypeFilteredPendinglist
        }
        else if (storeIdList.size > 1&&typeString.isEmpty()&&regionId.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode

                for (j in storeIdList.indices) {


                    if (site.contains(storeIdList.get(j))) {
                        orderTypeFilteredPendinglist.add(i)

                    } else if (site.contains(storeIdList.get(j))) {
                        orderTypeFilteredPendinglist.add(i)

                    }
                }
            }
            return orderTypeFilteredPendinglist
        }
        else if (regionIdList.size > 1 && storeIdList.size > 1&&typeString.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode


                for (k in storeIdList.indices) {

                    for (j in regionIdList.indices) {


                        if (region!!.contains(regionIdList.get(j)) && site.contains(
                                storeIdList.get(
                                    k
                                )
                            )
                        ) {
                            orderTypeFilteredPendinglist.add(i)

                        }
                    }
                }
            }
            return orderTypeFilteredPendinglist
        }
        else if (regionIdList.size > 1 && typeString.isNotEmpty()&&siteId.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {
            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode

                for (k in regionIdList.indices) {


                    if (typeString.equals("FORWARD RETURN") && omsOrderno.contains("FL") && region!!.contains(regionIdList.get(k))
                    ) {
                        orderTypeFilteredPendinglist.add(i)
                    } else if (typeString.equals("REVERSE RETURN") && omsOrderno.contains("RT") && region!!.contains(regionIdList.get(k))
                    ) {
                        orderTypeFilteredPendinglist.add(i)

                    }


                }
            }
            return orderTypeFilteredPendinglist
        }
        else if (storeIdList.size > 1 && typeString.isNotEmpty()&&regionId.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {
            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode

                for (k in storeIdList.indices) {


                    if (typeString.equals("FORWARD RETURN") && omsOrderno.contains("FL") && site!!.contains(storeIdList.get(k))
                    ) {
                        orderTypeFilteredPendinglist.add(i)
                    } else if (typeString.equals("REVERSE RETURN") && omsOrderno.contains("RT") && site!!.contains(storeIdList.get(k))
                    ) {
                        orderTypeFilteredPendinglist.add(i)

                    }


                }
            }
            return orderTypeFilteredPendinglist
        }
        else if (storeIdList.isEmpty() && regionIdList.isEmpty()&&fromDate.isEmpty()&&typeString.isEmpty()&&typeString.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode
                if (site.contains(siteId) && region!!.contains(regionId)) {
                    orderTypeFilteredPendinglist.add(i)
                }

            }
            return orderTypeFilteredPendinglist
        }
        else if (regionIdList.isEmpty()&&typeString.isNotEmpty()&&siteId.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode


                if (typeString.equals("FORWARD RETURN") && omsOrderno.contains("FL")  && region!!.contains(regionId)
                ) {
                    orderTypeFilteredPendinglist.add(i)
                } else if (typeString.equals("REVERSE RETURN") && omsOrderno.contains("RT") && region!!.contains(regionId)
                ) {
                    orderTypeFilteredPendinglist.add(i)

                }



            }
            return orderTypeFilteredPendinglist
        }
        else if (storeIdList.isEmpty()&&typeString.isNotEmpty()&&regionId.isEmpty()&&fromDate.isEmpty()&&currentDate.isEmpty()) {

            for (i in pendinglist) {
                var omsOrderno = i.omsorderno!!.toUpperCase()
                var site = i.storeid!!.toUpperCase()
                var region = i.dcCode


                if (typeString.equals("FORWARD RETURN") && omsOrderno.contains("FL")  && site!!.contains(siteId)
                ) {
                    orderTypeFilteredPendinglist.add(i)
                } else if (typeString.equals("REVERSE RETURN") && omsOrderno.contains("RT") && site!!.contains(siteId)
                ) {
                    orderTypeFilteredPendinglist.add(i)

                }



            }
            return orderTypeFilteredPendinglist
        }


        else {
            return pendinglist
        }
    }

    private fun showPendingList() {
        viewModel.qcPendingLists.observe(viewLifecycleOwner) { it ->
            viewBinding.refreshSwipe.isRefreshing = false
            storeList.clear()
            orderTypeList.clear()
            regionList.clear()
            hideLoading()
            if (it.pendinglist.isNullOrEmpty()) {
                viewBinding.emptyList.visibility = View.VISIBLE
                viewBinding.recyclerViewPending.visibility = View.GONE
                viewBinding.continueBtn.visibility = View.GONE

//                Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT).show()
            } else {
                viewBinding.emptyList.visibility = View.GONE
                viewBinding.recyclerViewPending.visibility = View.VISIBLE
                filterPendingList = (it.pendinglist as ArrayList<QcListsResponse.Pending>?)!!
                for (i in filterPendingList.indices) {
                    storeList.add(filterPendingList[i].storeid.toString())
                    orderTypeList.add(filterPendingList.get(i).omsorderno.toString())
                    regionList.add(filterPendingList[i].dcCode.toString())
                }
                val regionListSet: MutableSet<String> = LinkedHashSet()
                val stroreListSet: MutableSet<String> = LinkedHashSet()
                stroreListSet.addAll(storeList)
                regionListSet.addAll(regionList)
                storeList.clear()
                regionList.clear()
                regionList.addAll(regionListSet)
                storeList.addAll(stroreListSet)

//            subList = ListUtils.partition(it.pendinglist, 3)
                splitTheArrayList(it.pendinglist as ArrayList<QcListsResponse.Pending>?)
                pageNo = 1
                increment = 0
                if (pageNo == 1) {
                    viewBinding.prevPage.visibility = View.INVISIBLE
                } else {
                    viewBinding.prevPage.visibility = View.VISIBLE

                }
                if (increment == subList?.size!!.minus(1)) {
                    viewBinding.nextPage.visibility = View.GONE
                } else {
                    viewBinding.nextPage.visibility = View.VISIBLE

                }

                names = it.pendinglist as ArrayList<QcListsResponse.Pending>
                viewBinding.emptyList.visibility = View.GONE

                viewBinding.recyclerViewPending.visibility = View.VISIBLE
                if (subList?.size == 1) {
                    viewBinding.continueBtn.visibility = View.GONE
                } else {
                    viewBinding.continueBtn.visibility = View.VISIBLE

                }
                viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")

                adapter = context?.let { it1 ->
                    QcPendingListAdapter(it1, subList!!.get(increment), this, itemsList, this)
                }

            }
            viewBinding.recyclerViewPending.adapter = adapter

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 210) {
            if (resultCode == Activity.RESULT_OK) {

                if (data != null) {
                    fromDate = data.getStringExtra("fromQcDate").toString()
                    currentDate = data.getStringExtra("toDate").toString()
                    siteId = data.getStringExtra("siteId").toString()
                    regionId = data.getStringExtra("regionId").toString()
                    typeString = data.getStringExtra("orderType").toString()

                    if (currentDate.isNotEmpty() && fromDate.isNotEmpty()) {
                        showLoading()
                        viewModel.getQcPendingList(
                            Preferences.getToken(),
                            data.getStringExtra("fromQcDate").toString(),
                            data.getStringExtra("toDate").toString(),
                            data.getStringExtra("siteId").toString(),
                            data.getStringExtra("regionId").toString(),
                            this
                        )

                        MainActivity.mInstance.qcfilterIndicator.visibility = View.VISIBLE


                    } else if (pendingListList.size == pendingListMain.size) {
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.VISIBLE
                        setQcPedningListResponse(pendingListList)
                        adapter!!.notifyDataSetChanged()

                    } else {
                        pendingListList.clear()
                        pendingListList = pendingListMain
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.VISIBLE
                        setQcPedningListResponse(pendingListList)
                        adapter!!.notifyDataSetChanged()
                    }


                    if (data.getStringExtra("reset").toString().equals("reset")) {
                        showLoading()
                        Preferences.setQcFromDate("")
                        Preferences.setQcToDate("")
                        Preferences.setQcSite("")
                        siteId = ""
                        regionId = ""
                        Preferences.setQcRegion("")
                        Preferences.setQcOrderType("")
                        typeString = ""
                        pendingListList.clear()
                        pendingListMain.clear()
                        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
                        currentDate = simpleDateFormat.format(Date())
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE

                        viewModel.getQcPendingList(
                            Preferences.getToken(), "1-Apr-2019", currentDate, "", "", this
                        )
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


    fun splitTheArrayList(pendingList: ArrayList<QcListsResponse.Pending>?) {
        subList?.clear()
        var pendingSubList: ArrayList<QcListsResponse.Pending>? = ArrayList()
        var pageStartPos = 0;
        var pageEndPos = pageSize
        for (i in pendingList!!) {
            pendingSubList!!.add(i)
            if (pendingList.indexOf(i) == (pendingList.size - 1)) {
                val list: ArrayList<QcListsResponse.Pending> = ArrayList<QcListsResponse.Pending>(
                    pendingList.subList(
                        pageStartPos, pendingList.size
                    )
                )
                subList!!.add(list)
            } else if ((pendingList.indexOf(i) + 1) % pageSize == 0) {
                val list: ArrayList<QcListsResponse.Pending> = ArrayList<QcListsResponse.Pending>(
                    pendingList.subList(
                        pageStartPos, pageEndPos
                    )
                )
                subList!!.add(list)
                pageStartPos = pageStartPos + pageSize
                pageEndPos = pageEndPos + pageSize
            }
        }
//        if(adapter!=null){
//            adapter!!.notifyDataSetChanged()
//        }

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
        v: View,
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
        omsOrderno: String,


        ) {
        v.alpha = 0.5F

        v.setEnabled(false)

        Handler().postDelayed({
            v.setEnabled(true)
            v.alpha = 1F

        }, TIME)



        acceptOrRejectItemPos = position
        qcRejectItemsList.clear()
        for (i in itemlist!!) {
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
        qcAccepttList.clear()
        val qcreject = QcAcceptRejectRequest.Order(
            orderno,
            status,
            Preferences.getAppLevelDesignationQCFail(),
            Preferences.getToken(),
            storeId,
            qcRejectItemsList
        )
        qcAccepttList.add(qcreject)

        val dialogBinding: DialogAcceptQcBinding? = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()), R.layout.dialog_accept_qc, null, false
        )
        val customDialog = android.app.AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {
            showLoading()
            dialogBinding.yesBtn.setEnabled(false)

            Handler().postDelayed({ dialogBinding.yesBtn.setEnabled(true) }, TIME)
            customDialog.dismiss()
//            viewModel.getQcPendingItemsList(orderId)
            viewModel.getAcceptRejectResult(
                QcAcceptRejectRequest(
                    "ACCEPT", remarks, "", qcAccepttList
                )
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

        view.alpha = 0.5F
        view.setEnabled(false)

        Handler().postDelayed({
            view.alpha = 1F

            view.setEnabled(true)
        }, TIME)

        acceptOrRejectItemPos = position
        var isAllReasonsFound = true
        for (k in itemlist) {
            if (k.remarks.isNullOrEmpty()) {
                isAllReasonsFound = false
            }
        }


        if (isAllReasonsFound) {
            dialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()), R.layout.dialog_reject_qc, null, false
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
                if (i.approvedqty != null) {
                    rejItems.qty = i.approvedqty
                } else {
                    rejItems.qty = i.qty
                }
                rejItems.remarks = i.remarks
                rejItems.recId = i.recid
                qcRejectItemsList.add(rejItems)

            }

            qcRejectList.clear()
            val qcreject = QcAcceptRejectRequest.Order(
                orderno,
                status,
                Preferences.getAppLevelDesignationQCFail(),
                Preferences.getValidatedEmpId(),
                storeId,
                qcRejectItemsList
            )
            qcRejectList.add(qcreject)


            dialogBinding?.message?.setText(
                "You are rejecting the Order Id " + omsOrderno + " for QC Fail Do You Want to Proceed ?"
            )

            dialogBinding?.yesBtn?.setOnClickListener {
                showLoading()
                dialogBinding!!.yesBtn.setEnabled(false)

                Handler().postDelayed({ dialogBinding!!.yesBtn.setEnabled(true) }, TIME)
                customDialog.dismiss()
                if (itemsList != null) {
                    viewModel.getAcceptRejectResult(
                        QcAcceptRejectRequest(
                            "REJECT", reason, "REJ0001", qcRejectList
                        )
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
            Toast.makeText(context, "Please select reasons for all items", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun isChecked(
        array: List<QcListsResponse.Pending>,
        position: Int,
        pending: QcListsResponse.Pending,
    ) {
        var pendingItemChecked: Boolean = false;
        var items = QcListsResponse.Pending()
        var count: Int = 0
        var user: QcListsResponse.Pending? = names.find { it.orderno.equals(pending.orderno) }


        if (user!!.isItemChecked) {

            user!!.setisItemChecked(false)

        } else {
            user.setisItemChecked(true)
        }

        for (i in names.indices) {
            if (names[i].isItemChecked) {
                count++;
            }

        }

        if (count > 0) {
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

    override fun onFailureGetPendingAndAcceptAndRejectList(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
        i.putStringArrayListExtra("storeList", storeList)
        i.putStringArrayListExtra("orderTypeList", orderTypeList)

        i.putStringArrayListExtra("regionList", regionList)
        i.putExtra("fragmentName", "pending")
        i.putExtra("activity", "1")
        startActivityForResult(i, 210)


    }

    override fun onSelectApprovedFragment(listSize: String) {
    }

    override fun onSelectRejectedFragment() {

    }

    override fun onSelectPendingFragment() {

    }

    override fun onClickSpinnerLayout() {
        QcListSizeDialog().apply {
            arguments = QcListSizeDialog().generateParsedData(pageSizeList)
        }.show(childFragmentManager, "")
    }


    override fun selectListSize(listSize: String) {
        Preferences.setQcPendingPageSize(listSize.toInt());
        pageSize = Preferences.getQcPendingPageSiz()
//        MainActivity.mInstance.updateQcListCount(listSize)
        viewBinding.selectfiltertype.setText("Per page: " + listSize)
        viewModel.setPendingList(qcListsResponse!!)
//        adapter!!.notifyDataSetChanged()
//        Toast.makeText(context, "selected", Toast.LENGTH_SHORT).show()
    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                charString = charSequence.toString()
                if (charString!!.isEmpty()) {
                    qcListsResponse!!.pendinglist = pendingListList
//                    setQcPedningListResponse(qcListsResponse!!.pendinglist!!)
                } else {
                    pendingFilterList.clear()
                    for (row in pendingListList) {
                        if (!pendingFilterList.contains(row) && row.omsorderno!!.toUpperCase()
                                .contains(
                                    charString!!.toUpperCase(
                                        Locale.getDefault()
                                    )
                                )
                        ) {
                            pendingFilterList.add(row)
                        }
                    }
                    qcListsResponse!!.pendinglist = pendingFilterList
                }
                val filterResults = FilterResults()
                filterResults.values = qcListsResponse!!.pendinglist
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                if (qcListsResponse!!.pendinglist != null && !qcListsResponse!!.pendinglist!!.isEmpty()) {
                    qcListsResponse!!.pendinglist =
                        filterResults.values as java.util.ArrayList<QcListsResponse.Pending>
                    try {
                        setQcPedningListResponse(qcListsResponse!!.pendinglist!!)
                    } catch (e: Exception) {
                        Log.e("FullfilmentAdapter", e.message!!)
                    }
                } else {
                    setQcPedningListResponse(qcListsResponse!!.pendinglist!!)
                }
            }
        }
    }

}




