package com.apollopharmacy.vishwam.ui.home.qcfail.rejected

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentRejectedQcBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter.QcApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter.QcPendingListAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.rejected.adapter.QcRejectedListAdapter
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utlis
import org.apache.commons.collections4.ListUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RejectedFragment : BaseFragment<QcRejectedViewModel, FragmentRejectedQcBinding>(),
    MainActivityCallback,
    QcListsCallback,
    QcFilterFragment.QcFilterClicked {

    var adapter: QcRejectedListAdapter? = null
    public var isBulkChecked: Boolean = false

    var getitemList: List<QcItemListResponse>? = null
    var itemsList = ArrayList<QcItemListResponse>()
    var getRejectitemList: List<QcItemListResponse.Item>? = null
    var getRejectList: ArrayList<QcListsResponse.Reject>? = null

    var qcRejectItemsList = ArrayList<QcAcceptRejectRequest.Item>()
    var orderId: String = ""
    var reason: String = ""
    var qcreason: String = ""
    var getStatusList: List<ActionResponse>? = null
    var statusList = ArrayList<ActionResponse>()
    private var filterRejectList = ArrayList<QcListsResponse.Reject>()
    var subList: List<List<QcListsResponse.Reject>>? = null

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
        showLoading()
        MainActivity.mInstance.mainActivityCallback = this

        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        currentDate = simpleDateFormat.format(Date())


        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)


        fromDate = simpleDateFormat.format(cal.time)


        viewModel.getQcRegionList()
        viewModel.getQcStoreist()
        viewModel.getQcRejectList(Preferences.getToken(), fromDate, currentDate, "", "")



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
            hideLoading()

             if (it.rejectedlist != null && it.rejectedlist!!.size > 0) {
                viewBinding.recyclerViewPending.visibility = View.VISIBLE
                viewBinding.emptyList.visibility = View.GONE
                 filterRejectList = (it.rejectedlist as ArrayList<QcListsResponse.Reject>?)!!



                subList = ListUtils.partition(it.rejectedlist, 5)


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



                if (subList?.size == 1) {
                    viewBinding.continueBtn.visibility = View.GONE
                } else {
                    viewBinding.continueBtn.visibility = View.VISIBLE

                }
                viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")

                viewBinding.refreshSwipe.isRefreshing = false



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
                     Toast.makeText(requireContext(), "No Rejected Data", Toast.LENGTH_SHORT).show()
             }
        })






        viewBinding.refreshSwipe.setOnRefreshListener {

            viewModel.getQcRejectList(Preferences.getToken(), fromDate, currentDate, "", "")

        }

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
                        QcRejectedListAdapter(it1, this,
                            subList!!.get(increment),

                            itemsList,
                            statusList)
                    }
                viewBinding.recyclerViewPending.adapter = adapter
            } else {

                Toast.makeText(requireContext(), "No More Data To Load", Toast.LENGTH_SHORT).show()
                viewBinding.prevPage.visibility = View.GONE

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


    override fun accept(
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
    ) {
        TODO("Not yet implemented")
    }


    override fun reject(
        position: Int,
        orderno: String,
        remarks: String,
        itemlist: List<QcItemListResponse.Item>,
        storeId: String,
        status: String,
    ) {
        TODO("Not yet implemented")
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

        startActivityForResult(i, 210)
    }


}




