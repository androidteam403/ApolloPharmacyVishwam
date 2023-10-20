package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.FragmentPreRectroBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.adapter.ListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListReq
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListRes
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.PostRetroUploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.selectapnasite.SelectApnaSiteIDActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectSwachhSiteIDActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.Comparator
import kotlin.collections.ArrayList


class PreRectroFragment() : BaseFragment<PreRectroViewModel, FragmentPreRectroBinding>(),
    PreRectroCallback, MainActivityCallback {
    private var fragmentName: String = ""
    private var listAdapter: ListAdapter? = null
    var storeList: ArrayList<GetStorePendingAndApprovedListRes.Get>? = null
    var hashMap: HashMap<Int, List<GetStorePendingAndApprovedListRes.Get>> =
        HashMap<Int, List<GetStorePendingAndApprovedListRes.Get>>()

    override val layoutRes: Int
        get() = R.layout.fragment_pre_rectro

    override fun retrieveViewModel(): PreRectroViewModel {
        return ViewModelProvider(this).get(PreRectroViewModel::class.java)
    }

    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    override fun setup() {
        MainActivity.mInstance.mainActivityCallback = this
//        Preferences.savingToken("APL67949")
        viewBinding.callback = this
//        Toast.makeText(context, "" + Preferences.getAppLevelDesignationApnaRetro(), Toast.LENGTH_SHORT).show()
        viewBinding.pullToRefreshApproved.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            submitClickApproved()
        })
        if (this.arguments?.getBoolean("fromPreRectro") == true) {

            if (Preferences.getApnaSiteId().isEmpty()) {
                showLoading()
                val i = Intent(context, SelectApnaSiteIDActivity::class.java)
                startActivityForResult(i, 781)
            } else {
                viewBinding.storeId.text = Preferences.getApnaSiteId()
                viewBinding.storeName.text =
                    Preferences.getApnaSiteId() + " - " + Preferences.getApnaSiteName()
            }

            if (this.arguments?.getBoolean("fromPreRectro") == true) {
                fragmentName = "fromPreRectro"
            } else if (this.arguments?.getBoolean("fromPostRectro") == true) {
                fragmentName = "fromPostRectro"
            } else {
                fragmentName = "fromAfterCompletion"
            }
            if (fragmentName.equals("fromPreRectro")) {
                viewBinding.createNewRectroLayout.visibility = View.VISIBLE
                viewBinding.recordsUploaded.visibility = View.VISIBLE
            } else {
                viewBinding.createNewRectroLayout.visibility = View.GONE
                viewBinding.recordsUploaded.visibility = View.GONE
            }
            viewBinding.incharge.text = Preferences.getToken()
            viewBinding.storeName.text =
                Preferences.getApnaSiteId() + " - " + Preferences.getApnaSiteName()
            if (NetworkUtil.isNetworkConnected(requireContext())) {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cal = Calendar.getInstance()
                cal.add(Calendar.DATE, -7)
                val currentDate: String = simpleDateFormat.format(Date())
                val fromdate = simpleDateFormat.format(cal.time)
                val toDate = currentDate
                showLoading()
                val getStorePendingApprovedRequest = GetStorePendingAndApprovedListReq()
                getStorePendingApprovedRequest.storeid = Preferences.getApnaSiteId()
                getStorePendingApprovedRequest.empid = Preferences.getToken()
                getStorePendingApprovedRequest.fromdate = fromdate
                getStorePendingApprovedRequest.todate = toDate
                viewModel.getStorePendingApprovedListApiCallApnaRetro(
                    getStorePendingApprovedRequest,
                    this
                )

            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }


        }


    }

    fun submitClickApproved() {


        if (!viewBinding.pullToRefreshApproved.isRefreshing)
            Utlis.showLoading(requireContext())

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        val currentDate: String = simpleDateFormat.format(Date())
        var fromdate = simpleDateFormat.format(cal.time)
        var toDate = currentDate
        showLoading()
        var getStorePendingApprovedRequest = GetStorePendingAndApprovedListReq()
        getStorePendingApprovedRequest.storeid = Preferences.getApnaSiteId()
        getStorePendingApprovedRequest.empid = Preferences.getToken()
        getStorePendingApprovedRequest.fromdate = fromdate
        getStorePendingApprovedRequest.todate = toDate
        viewModel.getStorePendingApprovedListApiCallApnaRetro(
            getStorePendingApprovedRequest, this
        )


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
//            Toast.makeText(context, "" + Preferences.getRectroSiteId(), Toast.LENGTH_SHORT).show()
            if (requestCode == 781) {
                viewBinding.storeId.text = Preferences.getApnaSiteId()
                viewBinding.storeName.text =
                    Preferences.getApnaSiteId() + " - " + Preferences.getApnaSiteName()
//                Toast.makeText(context, "" + Preferences.getRectroSiteId(), Toast.LENGTH_SHORT)
//                    .show()
                hideLoading()
                if (NetworkUtil.isNetworkConnected(requireContext())) {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.DATE, -7)
                    val currentDate: String = simpleDateFormat.format(Date())
                    var fromdate = simpleDateFormat.format(cal.time)
                    var toDate = currentDate
                    showLoading()
                    var getStorePendingApprovedRequest = GetStorePendingAndApprovedListReq()
                    getStorePendingApprovedRequest.storeid = Preferences.getApnaSiteId()
                    getStorePendingApprovedRequest.empid = Preferences.getToken()
                    getStorePendingApprovedRequest.fromdate = fromdate
                    getStorePendingApprovedRequest.todate = toDate
                    viewModel.getStorePendingApprovedListApiCallApnaRetro(
                        getStorePendingApprovedRequest, this
                    )

                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.label_network_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else if (requestCode == 779) {
                if (NetworkUtil.isNetworkConnected(requireContext())) {
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.DATE, -7)
                    val currentDate: String = simpleDateFormat.format(Date())
                    var fromdate = simpleDateFormat.format(cal.time)
                    var toDate = currentDate
                    showLoading()
                    var getStorePendingApprovedRequest = GetStorePendingAndApprovedListReq()
                    getStorePendingApprovedRequest.storeid = Preferences.getApnaSiteId()
                    getStorePendingApprovedRequest.empid = Preferences.getToken()
                    getStorePendingApprovedRequest.fromdate = fromdate
                    getStorePendingApprovedRequest.todate = toDate
                    viewModel.getStorePendingApprovedListApiCallApnaRetro(
                        getStorePendingApprovedRequest, this
                    )

                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.label_network_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }

    }


    override fun onClickContinue() {
        val intent = Intent(context, UploadImagesActivity::class.java)
        intent.putExtra("fragmentName", fragmentName)
        startActivityForResult(intent, 779)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPreRetrPending(
        stage: String,
        status: String,
        retroid: String,
        uploadedOn: String,
        uploadedBy: String,
        storeId: String,


        uploadStage: String,
        approvedby: String?,
        approvedDate: String?,
        partiallyApprovedBy: String?,
        partiallyApprovedDate: String?,
        reshootDate: String?,
        reshootBy: String?, retroStage: String?,
    ) {
        val intent = Intent(context, PostRetroUploadImagesActivity::class.java)
        intent.putExtra("fragmentName", fragmentName)
        intent.putExtra("stage", stage)
        intent.putExtra("retroid", retroid)
        intent.putExtra("uploadedOn", uploadedOn)
        intent.putExtra("uploadedBy", uploadedBy)
        intent.putExtra("storeId", storeId)
        intent.putExtra("storeList", storeList)
        intent.putExtra("retroStage", retroStage)

        intent.putExtra("uploadStage", uploadStage)
        intent.putExtra("approvedby", approvedby)
//
        intent.putExtra("status", status)
//        intent.putExtra("partiallyApprovedBy", partiallyApprovedBy)
//        intent.putExtra("partiallyApprovedDate", partiallyApprovedDate)
//        intent.putExtra("reshootDate", reshootDate)
//        intent.putExtra("reshootBy", reshootBy)

        startActivityForResult(intent, 779)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPreRetroPending(stage: String) {
//        val intent = Intent(context, PreRetroPreviewActivity::class.java)
//        intent.putExtra("fragmentName", "nonApprovalFragment")
//        intent.putExtra("stage", stage)
//        startActivity(intent)
//        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPostRetroPending(
        stage: String,
        status: String,
        retroid: String,
        uploadedOn: String,
        uploadedBy: String,
        storeId: String,
        uploadStage: String,
        approvedby: String?,
        approvedDate: String?,
        partiallyApprovedBy: String?,
        partiallyApprovedDate: String?,
        reshootBy: String?,
        reshootDate: String?, retroStage: String?,
    ) {
        val intent = Intent(context, PostRetroUploadImagesActivity::class.java)
        intent.putExtra("fragmentName", fragmentName)
        intent.putExtra("stage", stage)
        intent.putExtra("retroid", retroid)
        intent.putExtra("uploadedOn", uploadedOn)
        intent.putExtra("storeList", storeList)
        intent.putExtra("uploadedBy", uploadedBy)
        intent.putExtra("storeId", storeId)

        intent.putExtra("retroStage", retroStage)
        intent.putExtra("uploadStage", uploadStage)
        intent.putExtra("approvedby", approvedby)
        intent.putExtra("status", status)
//        intent.putExtra("partiallyApprovedBy", partiallyApprovedBy)
//        intent.putExtra("partiallyApprovedDate", partiallyApprovedDate)
//        intent.putExtra("reshootDate", reshootDate)
//        intent.putExtra("reshootBy", reshootBy)
        startActivityForResult(intent, 779)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

    }


    override fun onSuccessgetStorePendingApprovedApiCall(getStorePendingApprovedList: GetStorePendingAndApprovedListRes) {


        if (getStorePendingApprovedList.status.equals(true) && getStorePendingApprovedList.getList.size > 0) {
            hideLoading()
            viewBinding.listRecyclerView.visibility = View.VISIBLE
            viewBinding.noOrdersFound.visibility = View.GONE
            viewBinding.recordsUploaded.visibility = View.VISIBLE
            if (viewBinding.pullToRefreshApproved.isRefreshing) {
//            Toast.makeText(context, "Refresh", Toast.LENGTH_LONG).show()
                viewBinding.pullToRefreshApproved.isRefreshing = false
            }
            storeList =
                getStorePendingApprovedList.getList as ArrayList<GetStorePendingAndApprovedListRes.Get>?


            val retroIdsGroupedList: Map<String, List<GetStorePendingAndApprovedListRes.Get>> =
                getStorePendingApprovedList.getList.stream()
                    .collect(Collectors.groupingBy { w -> w.retroid })
//            Toast.makeText(context, "" + retroIdsGroupedList.size, Toast.LENGTH_SHORT).show()

            var getStorePendingApprovedListDummys =
                ArrayList<ArrayList<GetStorePendingAndApprovedListRes.Get>>()
            for (entry in retroIdsGroupedList.entries) {
                getStorePendingApprovedListDummys.addAll(listOf(entry.value as ArrayList<GetStorePendingAndApprovedListRes.Get>))
            }
            getStorePendingApprovedList.groupByRetrodList =
                getStorePendingApprovedListDummys as List<MutableList<GetStorePendingAndApprovedListRes.Get>>?
//            Collections.sort(getStorePendingApprovedList.groupByRetrodList,
//                Comparator<Any?> { s1, s2 ->
//                    -s1.getOnholddatetime().compareToIgnoreCase(s2.getOnholddatetime())
//                })

            listAdapter =
                ListAdapter(getStorePendingApprovedList.groupByRetrodList.sortedByDescending {
                    it.get(
                        0
                    ).uploadedDate
                }, requireContext(), this)
            val layoutManager = LinearLayoutManager(ViswamApp.context)
            viewBinding.listRecyclerView.layoutManager = layoutManager
            viewBinding.listRecyclerView.itemAnimator = DefaultItemAnimator()
            viewBinding.listRecyclerView.adapter = listAdapter

        } else {
            hideLoading()
            viewBinding.recordsUploaded.visibility = View.GONE
            viewBinding.listRecyclerView.visibility = View.GONE
            viewBinding.noOrdersFound.visibility = View.VISIBLE
        }

    }

    override fun onClickSearch() {

    }

    class AdapterList(
        var transactionId: String,
        var storeId: String,
        var storeName: String,
        var uploadedOn: String,
        var uploadedBy: String,
        var approvedOn: String,
        var approvedBy: String,
        var preRetostatus: String,
        var postRetostatus: String,
        var afterCompletionstatus: String,
        var overAlStatus: String,
    )

    override fun onClickFilterIcon() {

    }

    override fun onClickSiteIdIcon() {
        showLoading()
        val i = Intent(context, SelectApnaSiteIDActivity::class.java)
        startActivityForResult(i, 781)
    }

    override fun onClickQcFilterIcon() {
    }

    override fun onSelectApprovedFragment(listSize: String?) {
        TODO("Not yet implemented")
    }

    override fun onSelectRejectedFragment() {
        TODO("Not yet implemented")
    }

    override fun onSelectPendingFragment() {
        TODO("Not yet implemented")
    }

    override fun onClickSpinnerLayout() {
        TODO("Not yet implemented")
    }

    override fun onClickSubmenuItem(
        menuName: String?,
        submenus: java.util.ArrayList<MenuModel>?,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

}