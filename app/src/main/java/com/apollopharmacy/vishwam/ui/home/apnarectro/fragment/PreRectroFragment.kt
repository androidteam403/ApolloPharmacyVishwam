package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.FragmentPreRectroBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.fragment.adapter.ListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListReq
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetStorePendingAndApprovedListRes
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.PostRetroUploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerectropending.PreRetroPendingReviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.reviewingscreens.PreRetroPreviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors


class PreRectroFragment() : BaseFragment<PreRectroViewModel, FragmentPreRectroBinding>(),
    PreRectroCallback {
    private var fragmentName:String=""
    private var listAdapter: ListAdapter? = null
    var hashMap: HashMap<Int, List<GetStorePendingAndApprovedListRes.Get>> = HashMap<Int, List<GetStorePendingAndApprovedListRes.Get>>()

    override val layoutRes: Int
        get() = R.layout.fragment_pre_rectro

    override fun retrieveViewModel(): PreRectroViewModel {
        return ViewModelProvider(this).get(PreRectroViewModel::class.java)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun setup() {
        viewBinding.callback=this
        Toast.makeText(context, ""+Preferences.getAppLevelDesignationApnaRetro(), Toast.LENGTH_SHORT).show()

        if(this.arguments?.getBoolean("fromPreRectro") == true){
            fragmentName= "fromPreRectro"
        }else if(this.arguments?.getBoolean("fromPostRectro") == true){
            fragmentName= "fromPostRectro"
        }else{
            fragmentName= "fromAfterCompletion"
        }
        if(fragmentName.equals("fromPreRectro")){
            viewBinding.createNewRectroLayout.visibility= View.VISIBLE
            viewBinding.recordsUploaded.visibility=View.VISIBLE
        }else{
            viewBinding.createNewRectroLayout.visibility= View.GONE
            viewBinding.recordsUploaded.visibility=View.GONE
        }
//        viewBinding.storeId.text= Preferences.getSwachhSiteId()
        viewBinding.incharge.text= Preferences.getValidatedEmpId()
        viewBinding.storeName.text= Preferences.getSwachSiteName()
//        viewBinding.secondReshoot.setOnClickListener {
//            val intent = Intent(context, PostRectroReviewScreen::class.java)
//            startActivity(intent)
//            activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
//        }

        if (NetworkUtil.isNetworkConnected(requireContext())) {
//            isFirstTime = false

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -14)
            val currentDate: String = simpleDateFormat.format(Date())

            var fromdate = simpleDateFormat.format(cal.time)
            var toDate = currentDate
//            getStorePersonHistoryList.clear()
//            if (!isLoading)
                showLoading()
            // Utlis.showLoading(requireContext())

            var getStorePendingApprovedRequest = GetStorePendingAndApprovedListReq()
            getStorePendingApprovedRequest.storeid = "16001"
            getStorePendingApprovedRequest.empid = "APL0001"
            getStorePendingApprovedRequest.fromdate = fromdate
            getStorePendingApprovedRequest.todate = toDate
//            getStoreHistoryRequest.startpageno = startPageNo
//            getStoreHistoryRequest.endpageno = endpageno
//            getStoreHistoryRequest.status = updatedComplaintListStatus
            viewModel.getStorePendingApprovedListApiCallApnaRetro(getStorePendingApprovedRequest, this)

        }
        else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

//        var configLst = ArrayList<AdapterList>()
//        configLst.add(AdapterList("14001_APNARECTRO_06042023", "14001", "Ameerpet, Hyderabad", "07 Apr, 2023 - 12 :30PM", "APL48627", "", "", "PENDING", "---", "---", "INCOMPLETE"))
//        configLst.add(AdapterList("16001_APNARECTRO_96042023", "16001", "Hydernagar, Hyderabad", "15 Apr, 2023 - 11 :45AM", "APL49299", "", "", "APPROVED", "RE-SHOOT", "---", "INCOMPLETE"))
//        configLst.add(AdapterList("47852_APNARECTRO_96042023", "18624", "Chanda Nagar, Hyderabad", "05 Apr, 2023 - 04:25PM", "APL48574", "07 apr, 2023 -04:25PM", "APL48627", "APPROVED", "APPROVED", "COMPLETED", "COMPLETE"))


    }

    override fun onClickContinue() {
        val intent = Intent(context, UploadImagesActivity::class.java)
        intent.putExtra("fragmentName", fragmentName)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickCardView() {
        val intent = Intent(context, PreRetroPendingReviewActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPreRetroPending(stage: String) {
        val intent = Intent(context, PreRetroPreviewActivity::class.java)
        intent.putExtra("fragmentName", "nonApprovalFragment")
        intent.putExtra("stage", stage)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPostRetroPending(stage: String, postRetostatus: String, retroid: String) {
        if(postRetostatus.equals("Pending") || postRetostatus.equals("---")){
            val intent = Intent(context, PostRetroUploadImagesActivity::class.java)
            intent.putExtra("fragmentName", fragmentName)
            intent.putExtra("stage", stage)
            intent.putExtra("retroid", retroid)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }else{
            val intent = Intent(context, PreRetroPreviewActivity::class.java)
            intent.putExtra("fragmentName", "nonApprovalFragment")
            intent.putExtra("stage", stage)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

    }



    override fun onSuccessgetStorePendingApprovedApiCall(getStorePendingApprovedList: GetStorePendingAndApprovedListRes) {
        if(getStorePendingApprovedList!=null && getStorePendingApprovedList.status.equals(true)){
            hideLoading()
            viewBinding.listRecyclerView.visibility=View.VISIBLE
            viewBinding.noOrdersFound.visibility=View.GONE

            val retroIdsGroupedList: Map<String, List<GetStorePendingAndApprovedListRes.Get>> =
                getStorePendingApprovedList.getList.stream().collect(Collectors.groupingBy { w -> w.retroid })
//           getStorePendingApprovedList.getList.clear()
            Toast.makeText(context, ""+retroIdsGroupedList.size, Toast.LENGTH_SHORT).show()

            var getStorePendingApprovedListDummys = ArrayList<ArrayList<GetStorePendingAndApprovedListRes.Get>>()
            for (entry in retroIdsGroupedList.entries) {
                    getStorePendingApprovedListDummys.addAll(listOf(entry.value as java.util.ArrayList<GetStorePendingAndApprovedListRes.Get>))
            }
            getStorePendingApprovedList.groupByRetrodList= getStorePendingApprovedListDummys as List<MutableList<GetStorePendingAndApprovedListRes.Get>>?
            listAdapter =
                ListAdapter(getStorePendingApprovedList.groupByRetrodList, requireContext(), this)
            val layoutManager = LinearLayoutManager(ViswamApp.context)
            viewBinding.listRecyclerView.layoutManager = layoutManager
            viewBinding.listRecyclerView.itemAnimator =
                DefaultItemAnimator()
            viewBinding.listRecyclerView.adapter = listAdapter

        }
        else{
            hideLoading()
            viewBinding.listRecyclerView.visibility=View.GONE
            viewBinding.noOrdersFound.visibility=View.VISIBLE
        }

    }

    class AdapterList(var transactionId: String, var storeId: String,var storeName: String, var uploadedOn: String, var uploadedBy: String, var approvedOn:String, var approvedBy:String,var preRetostatus:String, var postRetostatus:String,  var afterCompletionstatus:String , var overAlStatus:String)

}