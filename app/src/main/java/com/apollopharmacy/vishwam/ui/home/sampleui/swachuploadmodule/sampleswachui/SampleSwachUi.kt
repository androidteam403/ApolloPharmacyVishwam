package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.databinding.FragmentSampleuiSwachBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reshootactivity.ReShootActivity
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reviewratingactivity.RatingReviewActivity
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.sampleswachui.adapter.GetStorePersonAdapter
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity.UploadNowButtonActivity
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.OnUploadSwachModelRequest
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SampleSwachUi : BaseFragment<SampleSwachViewModel, FragmentSampleuiSwachBinding>(),
    GetStorePersonAdapter.getStoreHistory {
    private var swacchApolloList = ArrayList<SwachModelResponse>()

    //    private lateinit var configListAdapter: ConfigListAdapterSwach
    var dayofCharArray: String? = null
    var alreadyUploadedMessage: String? = null
    var backPressed:Boolean = false
    private lateinit var getStorePersonAdapter: GetStorePersonAdapter
    var positionofday: Int? = null
    private var charArray = ArrayList<String>()
    private var getStorePersonHistoryList = ArrayList<GetStorePersonHistoryodelResponse>()


    override val layoutRes: Int
        get() = R.layout.fragment_sampleui_swach

    override fun retrieveViewModel(): SampleSwachViewModel {
        return ViewModelProvider(this).get(SampleSwachViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {
        viewModel.getLastUploadedDate()
        viewModel.swachImagesRegisters()

        viewBinding.storeId.text = Preferences.getSiteId()
        viewBinding.userId.text = Preferences.getToken()




        onSuccessLastUpdatedDate()
        val sdf = SimpleDateFormat("dd MMM, yyyy")
        val todaysUpdate = sdf.format(Date())

        viewBinding.todaysDate.text = todaysUpdate

        showLoading()
        viewModel.checkDayWiseAccess()

        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        val currentDate: String = simpleDateFormat.format(Date())

        var fromdate = simpleDateFormat.format(cal.time)
        var toDate = currentDate


        val loginJson = Preferences.getLoginJson()
        var loginData: LoginDetails? = null
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            loginData = gson.fromJson(loginJson, LoginDetails::class.java)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        showLoading()
        var getStoreHistoryRequest = GetStorePersonHistoryodelRequest()
        getStoreHistoryRequest.storeid = Preferences.getSiteId()
        getStoreHistoryRequest.empid = Preferences.getToken()
        getStoreHistoryRequest.fromdate = fromdate
        getStoreHistoryRequest.todate = toDate
        getStoreHistoryRequest.startpageno = 0
        getStoreHistoryRequest.endpageno = 100
        viewModel.getStorePersonHistory(getStoreHistoryRequest)

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        viewModel.swachhapolloModel.observeForever {
            if (it != null && it.status ?: null == true) {
                swacchApolloList.add(it)

                for ((index, value) in it.configlist!!.withIndex()) {
                    val countUpload = value.categoryImageUploadCount?.toInt()
                    var dtcl_list = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
                    for (count in 1..countUpload!!) {

                        dtcl_list.add(SwachModelResponse.Config.ImgeDtcl(null, count, "", 0))

                    }
                    swacchApolloList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

                }

                hideLoading()

            }
        }
        viewModel.uploadSwachModel.observeForever {
            alreadyUploadedMessage=it.message

            if (it != null && it.status == true) {
//                Toast.makeText(ViswamApp.context, "" + it.message, Toast.LENGTH_SHORT).show()
                Utlis.hideLoading()

            } else if (it != null && it.status == false && it.message == "ALREADY UPLAODED") {
//                Toast.makeText(ViswamApp.context, "" + it.message, Toast.LENGTH_SHORT).show()
                viewBinding.uploadNowLayout.visibility=View.GONE
                viewBinding.alreadyUploadedlayout.visibility = View.GONE
                viewBinding.uploadOnLayout.visibility = View.GONE
                viewBinding.uploadNowGrey.visibility=View.VISIBLE
                Utlis.hideLoading()

            } else {
//                Toast.makeText(ViswamApp.context, "Please try again!!", Toast.LENGTH_SHORT).show()
                viewBinding.uploadNowLayout.visibility = View.VISIBLE
                viewBinding.uploadNowGrey.visibility=View.GONE
                viewBinding.alreadyUploadedlayout.visibility = View.GONE
                viewBinding.uploadOnLayout.visibility = View.GONE
                Utlis.hideLoading()
            }
        }


        viewModel.checkDayWiseAccess.observeForever {
            if (it != null) {
                val sdf = SimpleDateFormat("EEEE")
                val d = Date()
//                it.thursday = false
//                it.friday = false
//                it.thursday = true
                val dayOfTheWeek: String = sdf.format(d)
                charArray.add(it.sunday.toString())
                charArray.add(it.monday.toString())
                charArray.add(it.tuesday.toString())
                charArray.add(it.wednesday.toString())
                charArray.add(it.thursday.toString())
                charArray.add(it.friday.toString())
                charArray.add(it.saturday.toString())

                for (i in charArray.indices) {
                    if (charArray.get(i).equals("true")) {
                        positionofday = i
                    }
                }
                if (positionofday == 0) {
                    dayofCharArray = "Sunday"
                } else if (positionofday == 1) {
                    dayofCharArray = "Monday"
                } else if (positionofday == 2) {
                    dayofCharArray = "Tuesday"
                } else if (positionofday == 3) {
                    dayofCharArray = "Wednesday"
                } else if (positionofday == 4) {
                    dayofCharArray = "Thursday"
                } else if (positionofday == 5) {
                    dayofCharArray = "Friday"
                } else if (positionofday == 6) {
                    dayofCharArray = "Saturday"
                }

//                Toast.makeText(context, "" + dayofCharArray, Toast.LENGTH_SHORT).show()
                if (dayofCharArray == dayOfTheWeek) {

                    if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                        var submit = OnUploadSwachModelRequest()
                        submit.actionEvent = "SUBMIT"
                        submit.storeid = Preferences.getSiteId()
                        submit.userid = Preferences.getValidatedEmpId()
//            var imageUrlsList = ArrayList<OnUploadSwachModelRequest.ImageUrl>()
//
//            for (i in swacchApolloList.get(0).configlist!!.indices) {
//                for (j in swacchApolloList.get(0).configlist!!.get(i).imageDataDto!!.indices) {
//                    var imageUrl = submit.ImageUrl()
//                    imageUrl.url =
//                        swacchApolloList.get(0).configlist!!.get(i).imageDataDto?.get(j)?.base64Images
//                    imageUrl.categoryid = swacchApolloList.get(0).configlist!!.get(i).categoryId
//                    imageUrlsList.add(imageUrl)
//                }
//
//            }
//            submit.imageUrls = imageUrlsList
                        viewModel.onUploadSwach(submit)

                    }


                } else {
                    viewBinding.alreadyUploadedlayout.visibility = View.GONE
                    viewBinding.uploadNowLayout.visibility = View.GONE
                    viewBinding.uploadOnLayout.visibility = View.VISIBLE
                    viewBinding.dayoftheweekLayout.text = dayofCharArray

                }
            }
            hideLoading()
        }

//simpleDateFormat,siteid,transperent



        viewModel.getStorePersonHistory.observeForever {
            getStorePersonHistoryList.clear()
            if (it != null) {
                getStorePersonHistoryList.add(it)
                viewBinding.storeId.text = Preferences.getSiteId()
                viewBinding.userId.text = Preferences.getToken()
//                if(getStorePersonHistoryList.get(0).getList?.get(0)?.uploadedDate!=null &&getStorePersonHistoryList.get(0).getList?.get(0)?.uploadedDate!="" ){
//                    viewBinding.uploadedOn.text = getStorePersonHistoryList.get(0).getList?.get(0)?.uploadedDate
//                }else{
//                    viewBinding.uploadedOn.text = "--"
//                }
                if (getStorePersonHistoryList != null && getStorePersonHistoryList.size > 0 && getStorePersonHistoryList.get(0).getList!=null && getStorePersonHistoryList.get(0).getList!!.size>0 ) {
                    viewBinding.noOrdersFound.visibility = View.GONE

                    getStorePersonAdapter =
                        GetStorePersonAdapter(getStorePersonHistoryList.get(0).getList, this
                        )
                    val layoutManager = LinearLayoutManager(ViswamApp.context)
                    viewBinding.imageRecyclerView.layoutManager = layoutManager
                    viewBinding.imageRecyclerView.itemAnimator =
                        DefaultItemAnimator()
                    viewBinding.imageRecyclerView.adapter = getStorePersonAdapter
//                    Toast.makeText(context, "success api, ${getStorePersonHistoryList.get(0).getList?.size}", Toast.LENGTH_SHORT).show()
                    hideLoading()
                } else {
                    viewBinding.noOrdersFound.visibility = View.VISIBLE
                    hideLoading()

                }

            }
        }

//        viewBinding.reshootCardView.setOnClickListener {
//            val intent = Intent(context, ReShootActivity::class.java)
//            startActivity(intent)
//        }

        viewBinding.uploadNowLayout.setOnClickListener {
            val intent = Intent(context, UploadNowButtonActivity::class.java)
            startActivityForResult(intent, 779)
        }


    }

    fun onSuccessLastUpdatedDate() {
        viewModel.lastUploadedDateResponse.observeForever {
            if (it != null && it.status == true) {
                if (it.uploadedDate != null && !it.uploadedDate!!.isEmpty()) {
                    val strDate = it.uploadedDate
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                    val date = dateFormat.parse(strDate)
                    val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)// - hh:mm a
                    viewBinding.uploadedOn.text = dateNewFormat
                } else {
                    viewBinding.uploadedOn.text = "--"

                }
            }
        }
    }

    override fun onClickStatus(
        position: Int,
        swachhid: String?,
        status: String?,
        approvedDate: String?,
        storeId: String?,
        uploadedDate: String?,
        reshootDate: String?,
        partiallyApprovedDate: String?,
    ) {
        val intent = Intent(context, ReShootActivity::class.java)
        intent.putExtra("swachhid", swachhid)
        intent.putExtra("status", status)
        intent.putExtra("approvedDate", approvedDate)
        intent.putExtra("storeId", storeId)
        intent.putExtra("uploadedDate", uploadedDate)
        intent.putExtra("reshootDate", reshootDate)
        intent.putExtra("partiallyApprovedDate", partiallyApprovedDate)
        startActivityForResult(intent, 780)
    }

    override fun onClickReview(swachhid: String?) {
        val intent = Intent(context, RatingReviewActivity::class.java)
        intent.putExtra("swachhid", swachhid)
        startActivity(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 779 || requestCode == 780) {
                val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
                val cal = Calendar.getInstance()
                cal.add(Calendar.DATE, -7)
                val currentDate: String = simpleDateFormat.format(Date())

                var fromdate = simpleDateFormat.format(cal.time)
                var toDate = currentDate
                getStorePersonHistoryList.clear()
                showLoading()
                var getStoreHistoryRequest = GetStorePersonHistoryodelRequest()
                getStoreHistoryRequest.storeid = Preferences.getSiteId()
                getStoreHistoryRequest.empid = Preferences.getToken()
                getStoreHistoryRequest.fromdate = fromdate
                getStoreHistoryRequest.todate = toDate
                getStoreHistoryRequest.startpageno = 0
                getStoreHistoryRequest.endpageno = 100
                viewModel.getStorePersonHistory(getStoreHistoryRequest)
                viewModel.checkDayWiseAccess()
            }
//            getStorePersonAdapter.notifyDataSetChanged()


        }
    }



}