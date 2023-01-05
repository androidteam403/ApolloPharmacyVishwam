package com.apollopharmacy.vishwam.ui.home.qcfail.approved


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.Intent.getIntent
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentApprovedQcBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter.QcApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewImageActivity
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.collections4.ListUtils
import java.text.SimpleDateFormat
import java.util.*

class ApprovedFragment : BaseFragment<QcApprovedViewModel, FragmentApprovedQcBinding>(),
    MainActivityCallback,
    QcSiteDialog.NewDialogSiteClickListner,
    QcListsCallback, QcFilterFragment.QcFilterClicked, QcFilterListCallBacks {
    var adapter: QcApproveListAdapter? = null
    var filterAdapter: QcFilterItemsAdapter? = null
    var storeAdapter: QcStoreListAdapter? = null
    var regionAdapter: QcRegionListAdapter? = null
    private lateinit var dialog: BottomSheetDialog
    var pageNo: Int = 1
    var lastIndex = 0
    var increment: Int = 0

    public var storeStringList=ArrayList<String>()
    public var regionStringList=ArrayList<String>()
    public var isBulkChecked: Boolean = false
    var getStatusList: List<ActionResponse>? = null
    var statusList = ArrayList<ActionResponse>()
    var getStoreList: List<QcStoreList.Store>? = null
    var storeList = ArrayList<QcStoreList.Store>()
    var regionList = ArrayList<QcRegionList.Store>()
    private var filterApproveList = ArrayList<QcListsResponse.Approved>()
    var subList: List<List<QcListsResponse.Approved>>? = null
    var stuff: List<List<String>> = ArrayList()

    var getitemList: List<QcItemListResponse>? = null
    var itemsList = ArrayList<QcItemListResponse>()
    var names = ArrayList<String>()
    var mainMenuList = ArrayList<MainMenuList>()
    var list: ArrayList<String>? = null
    private var drawer: DrawerLayout? = null

    public var mInstance: ApprovedFragment? =
        null

    var orderId: String = ""
    var reason: String = ""
    var qcreason: String = ""
    var currentDate = String()
    var fromDate = String()

    override val layoutRes: Int
        get() = R.layout.fragment_approved_qc

    override fun retrieveViewModel(): QcApprovedViewModel {
        return ViewModelProvider(this).get(QcApprovedViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    override fun setup() {
        showLoading()
        Preferences.setQcFromDate("")
        Preferences.setQcToDate("")
        Preferences.setQcSite("")
        Preferences.setQcRegion("")
        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE
        MainActivity.mInstance.qcfilterIcon.visibility = View.VISIBLE
        MainActivity.mInstance.headerTitle.setText("Approved List")




        MainActivity.mInstance.mainActivityCallback = this

        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        currentDate = simpleDateFormat.format(Date())

        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        fromDate = simpleDateFormat.format(cal.time)
//        viewModel.getQcRegionList()
//        viewModel.getQcStoreist()
        viewModel.getQcList(Preferences.getToken(), fromDate, currentDate, "", "")
        var intent = Intent()
        if (!list.isNullOrEmpty()) {
            val i = getIntent("")
            list = i.getStringArrayListExtra("selectsiteIdList")!!

        }

        viewModel.qcRegionLists.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (!it.storelist.isNullOrEmpty()) {

                for (i in it.storelist!!) {
                    val items = QcRegionList.Store()
                    items.siteid = i.siteid
                    items.sitename = i.sitename

                    regionList.add(items)
                    regionAdapter?.notifyDataSetChanged()
                }

            }
        })
        viewModel.qcStoreList.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (!it.storelist.isNullOrEmpty()) {

                for (i in it.storelist!!) {
                    val items = QcStoreList.Store()
                    items.siteid = i.siteid
                    items.sitename = i.sitename


                    storeList.add(items)
                    storeAdapter?.notifyDataSetChanged()
                }

            }
        })


//1stapril2019   default code=pendinglist


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
        viewModel.qcItemsLists.observe(viewLifecycleOwner, Observer {
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





            adapter?.notifyDataSetChanged()


        })
        viewModel.qcLists.observe(viewLifecycleOwner, { it ->
            viewBinding.refreshSwipe.isRefreshing = false
            storeStringList.clear();
            regionStringList.clear();
            hideLoading()
            if (it.approvedlist.isNullOrEmpty()) {
                viewBinding.emptyList.visibility = View.VISIBLE
                viewBinding.recyclerViewApproved.visibility = View.GONE
                viewBinding.continueBtn.visibility = View.GONE
//                Toast.makeText(requireContext(), "No Approved Data", Toast.LENGTH_SHORT).show()
            } else {

                filterApproveList = (it.approvedlist as ArrayList<QcListsResponse.Approved>?)!!

                for (i in filterApproveList.indices) {
                    storeStringList.add(filterApproveList[i].storeid.toString())
                    regionStringList.add(filterApproveList[i].dcCode.toString())
                }
                val regionListSet: MutableSet<String> = LinkedHashSet()
                val stroreListSet: MutableSet<String> = LinkedHashSet()
                stroreListSet.addAll(storeStringList)
                regionListSet.addAll(regionStringList)
                storeStringList.clear()
                regionStringList.clear()
                regionStringList.addAll(regionListSet)
                storeStringList.addAll(stroreListSet)
                viewBinding.recyclerViewApproved.visibility = View.VISIBLE
                viewBinding.emptyList.visibility = View.GONE
                filterApproveList = (it.approvedlist as ArrayList<QcListsResponse.Approved>?)!!
                subList = ListUtils.partition(it.approvedlist, 5)
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


//                filterApproveList.subList(startPageApproved, endPageNumApproved)
                if (subList != null) {
                    viewBinding.pgno.setText("Total Pages" + " ( " + pageNo + " / " + subList!!.size + " )")

                }

                if (increment == 0) {
                    viewBinding.prevPage.visibility = View.GONE
                }
                if (subList.isNullOrEmpty()) {
                } else {
                    adapter =
                        context?.let { it1 ->
                            QcApproveListAdapter(it1,
                                subList!!.get(increment),
                                this,
                                itemsList,
                                statusList,
                                filterApproveList)
                        }
                    viewBinding.recyclerViewApproved.adapter = adapter
                }
            }

        })

        viewBinding.refreshSwipe.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClickApproved()
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


                adapter =
                    context?.let { it1 ->
                        QcApproveListAdapter(it1,
                            subList!!.get(increment),
                            this,
                            itemsList,
                            statusList,
                            filterApproveList)
                    }
                viewBinding.recyclerViewApproved.adapter = adapter

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
                        QcApproveListAdapter(it1,
                            subList!!.get(increment),
                            this,
                            itemsList,
                            statusList,
                            filterApproveList)
                    }
                viewBinding.recyclerViewApproved.adapter = adapter
            } else {

                Toast.makeText(requireContext(), "No More Data To Load", Toast.LENGTH_SHORT).show()
                viewBinding.prevPage.visibility = View.GONE

            }
        }
//        viewBinding.recyclerViewApproved.adapter = adapter

        viewBinding.filter.setOnClickListener {
//            val i = Intent(context, QcFilterActivity::class.java)
//            startActivityForResult(i, 210)
////
//                }
//                QcSiteDialog.show()
//            }
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
                        viewBinding.recyclerViewApproved.visibility = View.GONE
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
//        viewModel.getQcItemsList("RV000053")
        viewModel.getQcItemsList(orderno)
        viewModel.getQcStatusList(orderno)
        adapter?.notifyDataSetChanged()


    }

    fun submitClickApproved() {
        Preferences.setQcFromDate("")
        Preferences.setQcToDate("")
        Preferences.setQcSite("")
        Preferences.setQcRegion("")
        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE

//        if (!viewBinding.refreshSwipe.isRefreshing)
//            Utlis.showLoading(requireContext())

        viewModel.getQcList(Preferences.getToken(), fromDate, currentDate, "", "")


    }

    @SuppressLint("ResourceAsColor")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 210) {
            if (resultCode == Activity.RESULT_OK) {

                if (data != null) {
                    showLoading()
                    viewModel.getQcList(Preferences.getToken(),
                        data.getStringExtra("fromQcDate").toString(),
                        data.getStringExtra("toDate").toString(),
                        data.getStringExtra("siteId").toString(),
                        data.getStringExtra("regionId").toString())

                    if (data.getStringExtra("fromQcDate").toString()
                            .equals(fromDate) && data.getStringExtra(
                            "toDate").toString()
                            .equals(currentDate) && data.getStringExtra("regionId").toString()
                            .isNullOrEmpty()
                    ) {
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.VISIBLE
                    } else {
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.VISIBLE

                    }

                    if (data.getStringExtra("reset").toString().equals("reset")) {
                        showLoading()
                        MainActivity.mInstance.qcfilterIndicator.visibility = View.GONE
                        viewModel.getQcList(Preferences.getToken(), fromDate, currentDate, "", "")

                    }


//                    if (!list.isNullOrEmpty()) {
//
//
//
//                        viewModel.getQcList(Preferences.getToken(),
//                            list!!.get(0),
//                            list!![1],
//                            list!![2],
//                            list!![3])
//
//                    }

                }


            }
        }
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






    override fun clickedApply(
        selectedData: String,
        data: ArrayList<QcStoreList.Store>,
        regiondata: ArrayList<QcRegionList.Store>,
        tag: Int,
        toDate: String,
    ) {

    }


    override fun clickMenu(clickedMenu: Int, name: ArrayList<MainMenuList>) {
//        if (clickedMenu == 0) {
//            name[0].setisClicked(true)
//            dialogBinding?.dateLayout?.visibility ?: View.VISIBLE
//        } else {
//            name[0].setisClicked(false)
//        }
//        if (clickedMenu == 1) {
//            name[1].setisClicked(true)
//            dialogBinding?.dateLayout?.visibility ?: View.GONE
//            dialogBinding?.storeIdsRecyclerView?.visibility ?: View.VISIBLE
//
//        } else {
//            name[1].setisClicked(false)
//
//        }
//        if (clickedMenu == 2) {
//            name[2].setisClicked(true)
//
//        } else {
//            name[2].setisClicked(false)
//        }
//
//        filterAdapter?.notifyDataSetChanged()
    }

    override fun onselectMultipleSitesStore(list: ArrayList<String>, position: Int) {
    }

    override fun selectSite(departmentDto: QcStoreList.Store) {
    }

    override fun onClickFilterIcon() {
//        val i = Intent(context, QcFilterActivity::class.java)
//        startActivityForResult(i, 210)
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickQcFilterIcon() {
        val i = Intent(context, QcFilterActivity::class.java)
        i.putExtra("activity", "2")
        i.putStringArrayListExtra("storeList", storeStringList)
        i.putStringArrayListExtra("regionList", regionStringList)
        startActivityForResult(i, 210)
    }


}



