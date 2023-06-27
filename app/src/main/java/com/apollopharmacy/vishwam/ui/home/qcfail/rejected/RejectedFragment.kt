package com.apollopharmacy.vishwam.ui.home.qcfail.rejected

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentRejectedQcBinding
import com.apollopharmacy.vishwam.dialog.QcListSizeDialog
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.rejected.adapter.QcRejectedListAdapter
import com.apollopharmacy.vishwam.ui.login.Command
import org.apache.commons.collections4.ListUtils
import java.text.SimpleDateFormat
import java.util.*

class RejectedFragment : BaseFragment<QcRejectedViewModel, FragmentRejectedQcBinding>(),
    MainActivityCallback,
    QcListsCallback,
    QcFilterFragment.QcFilterClicked, QcListSizeDialog.GstDialogClickListners, Filterable {
    var qcListsResponse : QcListsResponse? = null
    var adapter: QcRejectedListAdapter? = null
    public var isBulkChecked: Boolean = false
    var pageSize:Int=0
    var getitemList: List<QcItemListResponse>? = null
    var itemsList = ArrayList<QcItemListResponse>()
    var getRejectitemList: List<QcItemListResponse.Item>? = null
    var getRejectList: ArrayList<QcListsResponse.Reject>? = null
    public var storeStringList=ArrayList<String>()
    public var regionStringList=ArrayList<String>()
    var qcRejectItemsList = ArrayList<QcAcceptRejectRequest.Item>()
    var orderId: String = ""
    var reason: String = ""
    var qcreason: String = ""
    var getStatusList: List<ActionResponse>? = null
    var statusList = ArrayList<ActionResponse>()
    private var filterRejectList = ArrayList<QcListsResponse.Reject>()
    var subList= ArrayList<ArrayList<QcListsResponse.Reject>>()
    var pageSizeList = ArrayList<String>()
    var charString: String? = ""
    private var rejectedListList=ArrayList<QcListsResponse.Reject>()
    private var rejectedFilterList=ArrayList<QcListsResponse.Reject>()
    //     var reason= String
    var fromDate = String()
    var currentDate = String()
    var itemList = ArrayList<QcItemListResponse.Item>()
    var increment: Int = 0
    var pageNo: Int = 1

    var names = ArrayList<QcListsResponse.Pending>();

    override val layoutRes: Int
        get() = R.layout.fragment_rejected_qc

    override fun retrieveViewModel(): QcRejectedViewModel {
        return ViewModelProvider(this).get(QcRejectedViewModel::class.java)
    }

    override fun setup() {
        pageSize=Preferences.getQcRejectedPageSiz()
        viewBinding.selectfiltertype.setText("Rows: " +Preferences.getQcRejectedPageSiz().toString())
        Preferences.setQcFromDate("")
        Preferences.setQcToDate("")
        Preferences.setQcSite("")
        Preferences.setQcRegion("")
        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE
        MainActivity.mInstance.qcfilterIcon.visibility = View.VISIBLE
//        MainActivity.mInstance.headerTitle.setText("Rejected List")
        showLoading()
        MainActivity.mInstance.mainActivityCallback = this
        pageSizeList.add("5")
        pageSizeList.add("10")
        pageSizeList.add("15")

        viewBinding.selectfiltertype.setOnClickListener {
            QcListSizeDialog().apply {
                arguments = QcListSizeDialog().generateParsedData(pageSizeList)
            }.show(childFragmentManager  , "")
        }

        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        currentDate = simpleDateFormat.format(Date())


        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)


        fromDate = simpleDateFormat.format(cal.time)


//        viewModel.getQcRegionList()
//        viewModel.getQcStoreist()
        viewModel.getQcRejectList(Preferences.getToken(), fromDate, currentDate, "", "")


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
                } else if (viewBinding.searchView.getText().toString().equals("")) {
                    viewBinding.close.visibility = View.VISIBLE
                    viewBinding.closeArrow.visibility = View.GONE

//                    if (adapter != null) {

                    getFilter()!!.filter("")
//                    }
                } else {
                    viewBinding.close.visibility = View.VISIBLE
                    viewBinding.closeArrow.visibility = View.GONE

//                    if (adapter != null) {
                    getFilter()!!.filter("")
//                    }
                }
            }
        })
        viewModel.qcStatusLists.observe(viewLifecycleOwner, Observer {
            hideLoading()
            getStatusList = listOf(it)
            val status: ActionResponse.Hsitorydetail


            for (i in getStatusList!!) {
                val items = ActionResponse()
                items.hsitorydetails = i.hsitorydetails
                items.setorder(orderId)


                statusList.add(items)
                adapter?.notifyDataSetChanged()
            }
        })



        viewModel.qcRejectItemsLists.observe(viewLifecycleOwner, Observer {
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

        viewModel.qcRejectLists.observe(viewLifecycleOwner, { it ->
            qcListsResponse = it
            rejectedListList= it.rejectedlist!!
            setQcRejectedListResponse(it.rejectedlist!!)
            hideLoading()

        })






        viewBinding.refreshSwipe.setOnRefreshListener {
            Preferences.setQcFromDate("")
            Preferences.setQcToDate("")
            Preferences.setQcSite("")
            Preferences.setQcRegion("")
            MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE

            viewModel.getQcRejectList(Preferences.getToken(), fromDate, currentDate, "", "")

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
                adapter =
                    context?.let { it1 ->
                        QcRejectedListAdapter(it1, this,
                            subList!!.get(increment),

                            itemsList,
                            statusList)
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

                adapter =
                    context?.let { it1 ->
                        QcRejectedListAdapter(it1, this,
                            subList!!.get(increment),

                            itemsList,
                            statusList)
                    }
                viewBinding.recyclerViewPending.adapter = adapter
            } else {

                Toast.makeText(requireContext(), "No More Data To Load", Toast.LENGTH_SHORT).show()
                viewBinding.prevPage.visibility = View.INVISIBLE

            }
        }

        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is Command.ShowButtonSheet -> {
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
                else -> {}
            }
        }

    }


    override fun orderno(position: Int, orderno: String) {
        showLoading()
        orderId = orderno
        viewModel.getQcRejectItemsList(orderno)

        viewModel.getQcStatusList(orderno)

        adapter?.notifyDataSetChanged()

    }

    override fun notify(position: Int, orderno: String) {
        adapter?.notifyDataSetChanged()
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
        TODO("Not yet implemented")
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

    override fun isChecked(
        array: List<QcListsResponse.Pending>,
        position: Int,
        pending: QcListsResponse.Pending,
    ) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 210) {
            if (resultCode == Activity.RESULT_OK) {

                if (data != null) {
                    showLoading()
                    viewModel.getQcRejectList(Preferences.getToken(),
                        data.getStringExtra("fromQcDate").toString(),
                        data.getStringExtra("toDate").toString(),
                        data.getStringExtra("siteId").toString(),
                        data.getStringExtra("regionId").toString())

                    if (data.getStringExtra("fromQcDate").toString()
                            .equals(fromDate) && data.getStringExtra("toDate").toString()
                            .equals(currentDate) && data.getStringExtra("regionId").toString()
                            .isNullOrEmpty()
                    ) {
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE
                    } else {
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.VISIBLE

                    }
                    if (data.getStringExtra("reset").toString().equals("reset")) {
                        showLoading()
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE
                        viewModel.getQcRejectList(Preferences.getToken(),
                            fromDate,
                            currentDate,
                            "",
                            "")

                    }
                }

            }


        }
    }



    override fun clickedApply(
        selectedData: String,
        data: ArrayList<QcStoreList.Store>,
        regiondata: ArrayList<QcRegionList.Store>,
        tag: Int,
        toDate: String,
    ) {

    }

    override fun onClickFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickQcFilterIcon() {
        val i = Intent(context, QcFilterActivity::class.java)
        i.putExtra("activity", "3")
        i.putStringArrayListExtra("storeList",storeStringList)
        i.putStringArrayListExtra("regionList",regionStringList)
        i.putExtra("fragmentName", "reject")
        startActivityForResult(i, 210)
    }

    override fun selectListSize(listSize: String) {
        Preferences.setQcRejectedPageSize(listSize.toInt());
        viewBinding.selectfiltertype.setText("Rows: " +listSize)
        pageSize=Preferences.getQcRejectedPageSiz()
        viewModel.setRejectedList(qcListsResponse!!)
//        Toast.makeText(context, "selected", Toast.LENGTH_SHORT).show()
    }

//    override fun getFilter(): Filter? {
//        return object : Filter() {
//            override fun performFiltering(charSequence: CharSequence): FilterResults {
//                charString = charSequence.toString()
//                if (charString!!.isEmpty()) {
//                    qcListsResponse!!.rejectedlist = rejectedListList
//                } else {
//                    rejectedFilterList.clear()
//                    for (row in rejectedListList) {
//                        if (!rejectedFilterList.contains(row) && row.omsorderno!!.toUpperCase()
//                                .contains(
//                                    charString!!.toUpperCase(
//                                        Locale.getDefault()
//                                    )
//                                )
//                        ) {
//                            rejectedFilterList.add(row)
//                        }
//                    }
//                    qcListsResponse!!.rejectedlist = rejectedFilterList
//                }
//                val filterResults = FilterResults()
//                filterResults.values = qcListsResponse!!.rejectedlist
//                return filterResults
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
//                if (qcListsResponse!!.rejectedlist != null && !qcListsResponse!!.rejectedlist!!.isEmpty()) {
//                    qcListsResponse!!.rejectedlist =
//                        filterResults.values as java.util.ArrayList<QcListsResponse.Reject>
//                    try {
//                        viewModel.setRejectedList(qcListsResponse!!)
//                    } catch (e: Exception) {
//                        Log.e("FullfilmentAdapter", e.message!!)
//                    }
//                } else {
//                    viewModel.setRejectedList(qcListsResponse!!)
//                }
//            }
//        }
//    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                charString = charSequence.toString()
                if (charString!!.isEmpty()) {
                    qcListsResponse!!.rejectedlist = rejectedListList
//                    setQcPedningListResponse(qcListsResponse!!.pendinglist!!)
                } else {
                    rejectedFilterList.clear()
                    for (row in rejectedListList) {
                        if (!rejectedFilterList.contains(row) && row.omsorderno!!.toUpperCase()
                                .contains(
                                    charString!!.toUpperCase(
                                        Locale.getDefault()
                                    )
                                )
                        ) {
                            rejectedFilterList.add(row)
                        }
                    }
                    qcListsResponse!!.rejectedlist = rejectedFilterList
                }
                val filterResults = FilterResults()
                filterResults.values = qcListsResponse!!.rejectedlist
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                if (qcListsResponse!!.rejectedlist != null && !qcListsResponse!!.rejectedlist!!.isEmpty()) {
                    qcListsResponse!!.rejectedlist =
                        filterResults.values as java.util.ArrayList<QcListsResponse.Reject>
                    try {
                        setQcRejectedListResponse(qcListsResponse!!.rejectedlist!!)
                    } catch (e: Exception) {
                        Log.e("FullfilmentAdapter", e.message!!)
                    }
                } else {
                    setQcRejectedListResponse(qcListsResponse!!.rejectedlist!!)
                }
            }
        }
    }

    private fun setQcRejectedListResponse(rejectedlist: List<QcListsResponse.Reject>) {
        viewBinding.refreshSwipe.isRefreshing = false
        storeStringList.clear()
        regionStringList.clear()
        if (rejectedlist != null && rejectedlist!!.size > 0) {

            viewBinding.recyclerViewPending.visibility = View.VISIBLE
            viewBinding.emptyList.visibility = View.GONE
            filterRejectList = (rejectedlist as ArrayList<QcListsResponse.Reject>?)!!


            filterRejectList = (rejectedlist as ArrayList<QcListsResponse.Reject>?)!!

            for (i in filterRejectList.indices) {
                storeStringList.add(filterRejectList[i].storeid.toString())
                regionStringList.add(filterRejectList[i].dcCode.toString())
            }
            val regionListSet: MutableSet<String> = LinkedHashSet()
            val stroreListSet: MutableSet<String> = LinkedHashSet()
            stroreListSet.addAll(storeStringList)
            regionListSet.addAll(regionStringList)
            storeStringList.clear()
            regionStringList.clear()
            regionStringList.addAll(regionListSet)
            storeStringList.addAll(stroreListSet)
            splitTheArrayList(rejectedlist)
//            subList = ListUtils.partition(rejectedlist, pageSize)
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



            if (subList?.size == 1) {
                viewBinding.continueBtn.visibility = View.GONE
            } else {
                viewBinding.continueBtn.visibility = View.VISIBLE

            }
            viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")




            adapter =
                context?.let { it1 ->
                    QcRejectedListAdapter(it1, this,
                        subList!!.get(increment),

                        itemsList,
                        statusList)
                }
            viewBinding.recyclerViewPending.adapter = adapter

        }

        else  {
            viewBinding.emptyList.visibility = View.VISIBLE
            viewBinding.recyclerViewPending.visibility = View.GONE
            viewBinding.continueBtn.visibility=View.GONE
//                     Toast.makeText(requireContext(), "No Rejected Data", Toast.LENGTH_SHORT).show()
        }
    }

    fun splitTheArrayList(rejectedList: ArrayList<QcListsResponse.Reject>?) {
        subList?.clear()
        var rejectedSubList: ArrayList<QcListsResponse.Reject>? = ArrayList()
        var pageStartPos = 0;
        var pageEndPos = pageSize
        for (i in rejectedList!!) {
            rejectedSubList!!.add(i)
            if (rejectedList.indexOf(i) == (rejectedList.size - 1)) {
                val list: ArrayList<QcListsResponse.Reject> =
                    ArrayList<QcListsResponse.Reject>(
                        rejectedList.subList(
                            pageStartPos,
                            rejectedList.size
                        )
                    )
                subList!!.add(list)
            } else if ((rejectedList.indexOf(i) + 1) % pageSize == 0) {
                val list: ArrayList<QcListsResponse.Reject> =
                    ArrayList<QcListsResponse.Reject>(
                        rejectedList.subList(
                            pageStartPos,
                            pageEndPos
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


}




