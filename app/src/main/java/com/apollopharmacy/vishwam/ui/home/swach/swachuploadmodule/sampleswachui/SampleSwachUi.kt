package com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.databinding.DialogFilterUploadBinding
import com.apollopharmacy.vishwam.databinding.FragmentSampleuiSwachBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.swach.model.DayOfCharArrayListModel
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.reshootactivity.ReShootActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.reviewratingactivity.RatingReviewActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.adapter.GetStorePersonAdapter
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.sampleswachui.model.LastUploadedDateResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectChampsSiteIDActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.uploadnowactivity.UploadNowButtonActivity
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.*
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetStorePersonHistoryodelResponse.Get
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.collections.ArrayList


class SampleSwachUi : BaseFragment<SampleSwachViewModel, FragmentSampleuiSwachBinding>(),
    GetStorePersonAdapter.getStoreHistory, MainActivityCallback, SampleSwachUiCallback {
    private var swacchApolloList = ArrayList<SwachModelResponse>()

    //    private lateinit var configListAdapter: ConfigListAdapterSwach
    var dayofCharArray: String? = null
    var alreadyUploadedMessage: String? = null
    var backPressed: Boolean = false
    private var isLoading: Boolean = false
    private var isFirstTime: Boolean = true
    var isSiteIdEmpty: Boolean = false
    var getStoreLists = ArrayList<Get>()
    private lateinit var getStorePersonAdapter: GetStorePersonAdapter
    var positionofday: String? = null
    var nextUploadDate: LocalDate? = null
    lateinit var layoutManager: LinearLayoutManager
    var handler: Handler = Handler()
    var dayWiseAccessMessage:String?=null
    var dayofTheWeekPosition:Int=0
    var positionOfTheDayWeekNew:String=""

    var userDesignation = ""

    private var charArray = ArrayList<String>()
   private var positionofftheDay = ArrayList<Int>()
    private var positionofftheDayNewForWeek = ArrayList<String>()
    private var dayOfCharArrayList = ArrayList<String>()
    private var dayOfCharArrayListNew = ArrayList<DayOfCharArrayListModel>()
    private var dayOfCharArrayListNewTemp = ArrayList<DayOfCharArrayListModel>()



    var complaintListStatus: String = "0,1,2,3"
    var isApprovedTab: Boolean = true
    var isPendingTab: Boolean = false
    var startPage: Int = 0
    var endPageNum: Int = 10
    override val layoutRes: Int
        get() = R.layout.fragment_sampleui_swach

    override fun retrieveViewModel(): SampleSwachViewModel {
        return ViewModelProvider(this).get(SampleSwachViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setup() {
        MainActivity.mInstance.mainActivityCallback = this

//        var submit = OnUploadSwachModelRequest()
//        submit.actionEvent = "SUBMIT"
//        submit.storeid = Preferences.getSwachhSiteId()
//        submit.userid = Preferences.getValidatedEmpId()
//        showLoading()
//        viewModel.onUploadSwach(submit,this)


        if (Preferences.getSwachhSiteId().isEmpty()) {
            showLoading()
            val i = Intent(context, SelectChampsSiteIDActivity::class.java)
            startActivityForResult(i, 781)
        }
        else {

//       viewBinding.callback=this
            viewModel.getLastUploadedDate(this)
            viewModel.swachImagesRegisters()
            setFilterIndication()
            viewBinding.storeId.text = Preferences.getSwachhSiteId()
            viewBinding.userId.text = Preferences.getToken()




            onSuccessLastUpdatedDate()

            val sdf = SimpleDateFormat("dd MMM, yyyy, EEEE")
            val todaysUpdate = sdf.format(Date())

//            viewBinding.todaysDate.text = todaysUpdate
            viewBinding.todaysDateLayout.text = todaysUpdate

            showLoading()
            viewModel.checkDayWiseAccess(this)

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

            layoutManager = LinearLayoutManager(context)
            //attaches LinearLayoutManager with RecyclerView
            viewBinding.imageRecyclerView.layoutManager = layoutManager

            callAPI(startPage, endPageNum, complaintListStatus)
//        showLoading()
//        var getStoreHistoryRequest = GetStorePersonHistoryodelRequest()
//        getStoreHistoryRequest.storeid = Preferences.getSiteId()
//        getStoreHistoryRequest.empid = Preferences.getToken()
//        getStoreHistoryRequest.fromdate = fromdate
//        getStoreHistoryRequest.todate = toDate
//        getStoreHistoryRequest.startpageno = 0
//        getStoreHistoryRequest.endpageno = 100
//        getStoreHistoryRequest.status = complaintListStatus
//        viewModel.getStorePersonHistory(getStoreHistoryRequest)

//        if (NetworkUtil.isNetworkConnected(requireContext())) {
//            showLoading()
//        } else {
//            Toast.makeText(
//                requireContext(),
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }
            userDesignation = Preferences.getAppLevelDesignationSwach()
            viewModel.checkDayWiseAccess.observeForever {
//                if (it != null) {
//                    var checkDayWiseAccessResponse = it
//                    val sdf = SimpleDateFormat("EEEE")
//                    val d = Date()
//                    charArray.clear()
//                    positionofftheDay.clear()
//                    dayOfCharArrayList.clear()
////
//              it.monday = false
//                it.tuesday=true
//////             it.thursday = true
////
////             it.friday = false
//                    val dayOfTheWeek: String = sdf.format(d)
//                    charArray.add(checkDayWiseAccessResponse.sunday.toString())
//                    charArray.add(checkDayWiseAccessResponse.monday.toString())
//                    charArray.add(checkDayWiseAccessResponse.tuesday.toString())
//                    charArray.add(checkDayWiseAccessResponse.wednesday.toString())
//                    charArray.add(checkDayWiseAccessResponse.thursday.toString())
//                    charArray.add(checkDayWiseAccessResponse.friday.toString())
//                    charArray.add(checkDayWiseAccessResponse.saturday.toString())
//
//                    for (i in charArray.indices) {
//                        if (charArray.get(i).equals("true")) {
////                        positionofday = i
//                            positionofftheDay.add(i)
//                        }
//                    }
//                    for (i in positionofftheDay.indices) {
//                        if (positionofftheDay.get(i) == 0) {
//                            dayofCharArray = "Sunday"
//                        } else if (positionofftheDay.get(i) == 1) {
//                            dayofCharArray = "Monday"
//                        } else if (positionofftheDay.get(i) == 2) {
//                            dayofCharArray = "Tuesday"
//                        } else if (positionofftheDay.get(i) == 3) {
//                            dayofCharArray = "Wednesday"
//                        } else if (positionofftheDay.get(i) == 4) {
//                            dayofCharArray = "Thursday"
//                        } else if (positionofftheDay.get(i) == 5) {
//                            dayofCharArray = "Friday"
//                        } else if (positionofftheDay.get(i) == 6) {
//                            dayofCharArray = "Saturday"
//                        }
//                        dayOfCharArrayList.add(dayofCharArray!!)
//                    }
//
//                    val dt = LocalDate.now()
//
//
////                Toast.makeText(context, "" + dayofCharArray, Toast.LENGTH_SHORT).show()
//                    var isSameDay: Boolean = false;
//                    for (i in dayOfCharArrayList.indices) {
//                        if (dayOfCharArrayList.get(0) == dayOfTheWeek) {
//                            positionofday = dayOfCharArrayList.get(1)
//                        } else {
//                            positionofday = dayOfCharArrayList.get(0)
//                        }
//                        if (dayOfCharArrayList.get(i) == dayOfTheWeek) {
//                            isSameDay = true
//                            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//                                var submit = OnUploadSwachModelRequest()
//                                submit.actionEvent = "SUBMIT"
//                                submit.storeid = Preferences.getSwachhSiteId()
//                                submit.userid = Preferences.getValidatedEmpId()
////            var imageUrlsList = ArrayList<OnUploadSwachModelRequest.ImageUrl>()
////
////            for (i in swacchApolloList.get(0).configlist!!.indices) {
////                for (j in swacchApolloList.get(0).configlist!!.get(i).imageDataDto!!.indices) {
////                    var imageUrl = submit.ImageUrl()
////                    imageUrl.url =
////                        swacchApolloList.get(0).configlist!!.get(i).imageDataDto?.get(j)?.base64Images
////                    imageUrl.categoryid = swacchApolloList.get(0).configlist!!.get(i).categoryId
////                    imageUrlsList.add(imageUrl)
////                }
////
////            }
////            submit.imageUrls = imageUrlsList
//                                viewModel.onUploadSwach(submit)
//
//                            }
//
//
//                        }
//                    }
//                    if (!isSameDay && positionofday != null) {
//                        calcNextFriday(dt, positionofday!!)
//                        viewBinding.todaysDateLayout.visibility = View.GONE
//                        viewBinding.todaysUpdateLayout.visibility = View.GONE
//                        viewBinding.alreadyUploadedlayout.visibility = View.GONE
//                        viewBinding.uploadNowLayout.visibility = View.GONE
//                        viewBinding.uploadOnLayout.visibility = View.VISIBLE
//                        val strDate = calcNextFriday(dt, positionofday!!).toString()
//                        val dateFormat = SimpleDateFormat("yyyy-MM-dd");
//                        val date = dateFormat.parse(strDate.toString())
//                        val dateNewFormat = SimpleDateFormat("dd MMM, yyyy, EEEE").format(date)
//                        viewBinding.dayoftheweekLayout.text = dateNewFormat
//                    }
//                }
//                hideLoading()
            }


            viewModel.swachhapolloModel.observeForever {
                if (it != null && it.status ?: null == true) {
                    swacchApolloList.clear()
                    swacchApolloList.add(it)

                    for ((index, value) in it.configlist!!.withIndex()) {
                        val countUpload = value.categoryImageUploadCount?.toInt()
                        var dtcl_list = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
                        for (count in 1..countUpload!!) {

                            dtcl_list.add(SwachModelResponse.Config.ImgeDtcl(null, count, "", 0))

                        }
                        swacchApolloList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

                    }

                    //  hideLoading()

//                }
//            }
//            viewModel.uploadSwachModel.observeForever {
//                alreadyUploadedMessage = it.message
////           it.message = "ALREADY UPLAODED"
                }
            }

            viewBinding.pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                submitClick()
            })


//            viewModel.checkDayWiseAccess.observeForever {
//                if (it != null) {
//                    val sdf = SimpleDateFormat("EEEE")
//                    val d = Date()
//                    charArray.clear()
//                    positionofftheDay.clear()
//                    dayOfCharArrayList.clear()
////
////               it.wednesday = false
//////             it.thursday = true
////
////             it.friday = false
//                    val dayOfTheWeek: String = sdf.format(d)
//                    charArray.add(it.sunday.toString())
//                    charArray.add(it.monday.toString())
//                    charArray.add(it.tuesday.toString())
//                    charArray.add(it.wednesday.toString())
//                    charArray.add(it.thursday.toString())
//                    charArray.add(it.friday.toString())
//                    charArray.add(it.saturday.toString())
//
//                    for (i in charArray.indices) {
//                        if (charArray.get(i).equals("true")) {
////                        positionofday = i
//                            positionofftheDay.add(i)
//                        }
//                    }
//                    for (i in positionofftheDay.indices) {
//                        if (positionofftheDay.get(i) == 0) {
//                            dayofCharArray = "Sunday"
//                        } else if (positionofftheDay.get(i) == 1) {
//                            dayofCharArray = "Monday"
//                        } else if (positionofftheDay.get(i) == 2) {
//                            dayofCharArray = "Tuesday"
//                        } else if (positionofftheDay.get(i) == 3) {
//                            dayofCharArray = "Wednesday"
//                        } else if (positionofftheDay.get(i) == 4) {
//                            dayofCharArray = "Thursday"
//                        } else if (positionofftheDay.get(i) == 5) {
//                            dayofCharArray = "Friday"
//                        } else if (positionofftheDay.get(i) == 6) {
//                            dayofCharArray = "Saturday"
//                        }
//                        dayOfCharArrayList.add(dayofCharArray!!)
//                    }
//
//                    val dt = LocalDate.now()
//
//
////                Toast.makeText(context, "" + dayofCharArray, Toast.LENGTH_SHORT).show()
//                    var isSameDay: Boolean = false;
//                    for (i in dayOfCharArrayList.indices) {
//                        if (dayOfCharArrayList.get(0) == dayOfTheWeek) {
//                            positionofday = dayOfCharArrayList.get(1)
//                        } else {
//                            positionofday = dayOfCharArrayList.get(0)
//                        }
//                        if (dayOfCharArrayList.get(i) == dayOfTheWeek) {
//                            isSameDay = true
//                            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//                                var submit = OnUploadSwachModelRequest()
//                                submit.actionEvent = "SUBMIT"
//                                submit.storeid = Preferences.getSwachhSiteId()
//                                submit.userid = Preferences.getValidatedEmpId()
////            var imageUrlsList = ArrayList<OnUploadSwachModelRequest.ImageUrl>()
////
////            for (i in swacchApolloList.get(0).configlist!!.indices) {
////                for (j in swacchApolloList.get(0).configlist!!.get(i).imageDataDto!!.indices) {
////                    var imageUrl = submit.ImageUrl()
////                    imageUrl.url =
////                        swacchApolloList.get(0).configlist!!.get(i).imageDataDto?.get(j)?.base64Images
////                    imageUrl.categoryid = swacchApolloList.get(0).configlist!!.get(i).categoryId
////                    imageUrlsList.add(imageUrl)
////                }
////
////            }
////            submit.imageUrls = imageUrlsList
//                                viewModel.onUploadSwach(submit)
//
//                            }
//
//
//                        }
//                    }
//                    if (!isSameDay && positionofday != null) {
//                        calcNextFriday(dt, positionofday!!)
//                        viewBinding.todaysDateLayout.visibility = View.GONE
//                        viewBinding.todaysUpdateLayout.visibility = View.GONE
//                        viewBinding.alreadyUploadedlayout.visibility = View.GONE
//                        viewBinding.uploadNowLayout.visibility = View.GONE
//                        viewBinding.uploadOnLayout.visibility = View.VISIBLE
//                        val strDate = calcNextFriday(dt, positionofday!!).toString()
//                        val dateFormat = SimpleDateFormat("yyyy-MM-dd");
//                        val date = dateFormat.parse(strDate.toString())
//                        val dateNewFormat = SimpleDateFormat("dd MMM, yyyy, EEEE").format(date)
//                        viewBinding.dayoftheweekLayout.text = dateNewFormat
//                    }
//                }
//                hideLoading()
//            }

//simpleDateFormat,siteid,transperent


            viewModel.getStorePersonHistory.observeForever {



//            getStorePersonHistoryList.clear()
//            hideLoading()
//            if (it != null) {
//                getStorePersonHistoryList.add(it)
//                viewBinding.storeId.text = Preferences.getSiteId()
//                viewBinding.userId.text = Preferences.getToken()
//
//                if (getStorePersonHistoryList != null && getStorePersonHistoryList.size > 0 && getStorePersonHistoryList.get(
//                        0).getList != null && getStorePersonHistoryList.get(0).getList!!.size > 0
//                ) {
//                    viewBinding.noOrdersFound.visibility = View.GONE
//                    viewBinding.imageRecyclerView.visibility = View.VISIBLE
//
//
//                    if (isLoading) {
//                        hideLoading()
//                        getStorePersonAdapter.getData()
//                            ?.removeAt(getStorePersonAdapter.getData()?.size!! - 1)
//                        var listSize = getStorePersonAdapter.getData()?.size
//                        getStorePersonAdapter.notifyItemRemoved(listSize!!)
//                        getStorePersonAdapter.getData()
//                            ?.addAll(getStorePersonHistoryList.get(0).getList!!)
//                        getStorePersonAdapter.notifyDataSetChanged()
//                        isLoading = false
//                    } else {
//                        getStorePersonAdapter =
//                            GetStorePersonAdapter(getStorePersonHistoryList.get(0).getList as java.util.ArrayList<GetStorePersonHistoryodelResponse.Get>?,
//                                this
//                            )
//                        layoutManager = LinearLayoutManager(ViswamApp.context)
//                        viewBinding.imageRecyclerView.layoutManager = layoutManager
//                        viewBinding.imageRecyclerView.itemAnimator =
//                            DefaultItemAnimator()
//                        viewBinding.imageRecyclerView.adapter = getStorePersonAdapter
////                    Toast.makeText(context, "success api, ${getStorePersonHistoryList.get(0).getList?.size}", Toast.LENGTH_SHORT).show()
//                        hideLoading()
//                    }
//
//
//                } else {
//                    viewBinding.imageRecyclerView.visibility = View.GONE
//                    viewBinding.noOrdersFound.visibility = View.VISIBLE
//                    hideLoading()
//
//                }
//
//            } getStorePersonHistoryList.clear()
//            hideLoading()
//            if (it != null) {
//                getStorePersonHistoryList.add(it)
//                viewBinding.storeId.text = Preferences.getSiteId()
//                viewBinding.userId.text = Preferences.getToken()
//
//                if (getStorePersonHistoryList != null && getStorePersonHistoryList.size > 0 && getStorePersonHistoryList.get(
//                        0).getList != null && getStorePersonHistoryList.get(0).getList!!.size > 0
//                ) {
//                    viewBinding.noOrdersFound.visibility = View.GONE
//                    viewBinding.imageRecyclerView.visibility = View.VISIBLE
//
//
//                    if (isLoading) {
//                        hideLoading()
//                        getStorePersonAdapter.getData()
//                            ?.removeAt(getStorePersonAdapter.getData()?.size!! - 1)
//                        var listSize = getStorePersonAdapter.getData()?.size
//                        getStorePersonAdapter.notifyItemRemoved(listSize!!)
//                        getStorePersonAdapter.getData()
//                            ?.addAll(getStorePersonHistoryList.get(0).getList!!)
//                        getStorePersonAdapter.notifyDataSetChanged()
//                        isLoading = false
//                    } else {
//                        getStorePersonAdapter =
//                            GetStorePersonAdapter(getStorePersonHistoryList.get(0).getList as java.util.ArrayList<GetStorePersonHistoryodelResponse.Get>?,
//                                this
//                            )
//                        layoutManager = LinearLayoutManager(ViswamApp.context)
//                        viewBinding.imageRecyclerView.layoutManager = layoutManager
//                        viewBinding.imageRecyclerView.itemAnimator =
//                            DefaultItemAnimator()
//                        viewBinding.imageRecyclerView.adapter = getStorePersonAdapter
////                    Toast.makeText(context, "success api, ${getStorePersonHistoryList.get(0).getList?.size}", Toast.LENGTH_SHORT).show()
//                        hideLoading()
//                    }
//
//
//                } else {
//                    viewBinding.imageRecyclerView.visibility = View.GONE
//                    viewBinding.noOrdersFound.visibility = View.VISIBLE
//                    hideLoading()
//
//                }
//
//            }
            }

//        viewBinding.reshootCardView.setOnClickListener {
//            val intent = Intent(context, ReShootActivity::class.java)
//            startActivity(intent)
//        }

//            viewBinding.uploadNowLayout.setOnClickListener {
//                val intent = Intent(context, UploadNowButtonActivity::class.java)
//                startActivityForResult(intent, 779)
//            }


            addScrollerListener()
        }
        viewBinding.uploadNowLayout.setOnClickListener {
            val intent = Intent(context, UploadNowButtonActivity::class.java)
            startActivityForResult(intent, 779)
        }
    }

    private fun addScrollerListener() {
        //attaches scrollListener with RecyclerView
        viewBinding.imageRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isFirstTime) {
                    //findLastCompletelyVisibleItemPostition() returns position of last fully visible view.
                    ////It checks, fully visible view is the last one.
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == getStorePersonAdapter.getData()?.size!! - 1) {
                        loadMore()
                    }
                }
            }
        })
    }

    fun submitClick() {
        startPage = 0
        endPageNum = 10
        getStorePersonAdapter.getData()!!.clear()
//        complaintListStatus="0,1,2,3"
        if (!viewBinding.pullToRefresh.isRefreshing)
            Utlis.showLoading(requireContext())


        callAPI(startPage, endPageNum, complaintListStatus)


    }

    private fun loadMore() {
        //notify adapter using Handler.post() or RecyclerView.post()
        handler.post(Runnable
        {

            if (getStorePersonAdapter.getData()!!.size >= 10) {
                var addemptyObject = GetStorePersonHistoryodelResponse.Get()
                addemptyObject.swachhid = null
                getStorePersonAdapter.getData()?.add(addemptyObject)
                getStorePersonAdapter.notifyItemInserted(getStorePersonAdapter.getData()?.size!! - 1)


                isLoading = true
                startPage = startPage + 10
//               endPageNum = endPageNum + 10
                callAPI(startPage, endPageNum, complaintListStatus)
            }
//            var emptyObject= ArrayList<GetStorePersonHistoryodelResponse.Get>()


        })
    }

    fun setFilterIndication() {

    }

    fun callAPI(startPageNo: Int, endpageno: Int, updatedComplaintListStatus: String) {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            isFirstTime = false

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -7)
            val currentDate: String = simpleDateFormat.format(Date())

            var fromdate = simpleDateFormat.format(cal.time)
            var toDate = currentDate
//            getStorePersonHistoryList.clear()
            if (!isLoading)
                showLoading()
            // Utlis.showLoading(requireContext())

            var getStoreHistoryRequest = GetStorePersonHistoryodelRequest()
            getStoreHistoryRequest.storeid = Preferences.getSwachhSiteId()
            getStoreHistoryRequest.empid = Preferences.getToken()
            getStoreHistoryRequest.fromdate = fromdate
            getStoreHistoryRequest.todate = toDate
            getStoreHistoryRequest.startpageno = startPageNo
            getStoreHistoryRequest.endpageno = endpageno
            getStoreHistoryRequest.status = updatedComplaintListStatus
            viewModel.getStorePersonHistory(getStoreHistoryRequest, this)

        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun onSuccessLastUpdatedDate() {
//        viewModel.lastUploadedDateResponse.observeForever {
//            if (it != null && it.status == true) {
//                if (it.uploadedDate != null && !it.uploadedDate!!.isEmpty()) {
//                    val strDate = it.uploadedDate
//                    val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//                    val date = dateFormat.parse(strDate)
//                    val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)// - hh:mm a
//                    viewBinding.uploadedOn.text = dateNewFormat
//                } else {
//                    viewBinding.uploadedOn.text = "--"
//
//                }
//            }
//        }
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

    override fun onClickReview(swachhid: String?, storeId: String?) {
        val intent = Intent(context, RatingReviewActivity::class.java)
        intent.putExtra("swachhid", swachhid)
        intent.putExtra("storeId", storeId)
        intent.putExtra("userDesignation", userDesignation)
        startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            isSiteIdEmpty = data!!.getBooleanExtra("isSiteIdEmpty", isSiteIdEmpty)
            if (requestCode == 779 || requestCode == 780) {
                startPage = 0
                endPageNum = 10
                complaintListStatus = "0,1,2,3"
                callAPI(startPage, endPageNum, complaintListStatus)
                viewModel.checkDayWiseAccess(this)
            } else if (requestCode == 781) {
                hideLoading()
                Utlis.hideLoading()
                if (isSiteIdEmpty) {
                    MainActivity.mInstance.onBackPressed()
//                    hideLoadingTemp()
                    hideLoading()
                } else {
                    showLoading()
                    // showLoadingTemp(con)
//                Handler().postDelayed(Runnable {
//                    getStorePersonAdapter.getData()!!.clear()
                    startPage = 0
                    endPageNum = 10
                    complaintListStatus = "0,1,2,3"
                    viewBinding.storeId.text = Preferences.getSwachhSiteId()
                    viewBinding.userId.text=Preferences.getToken()
                    callAPI(startPage, endPageNum, complaintListStatus)
                    viewModel.checkDayWiseAccess(this)
                    viewModel.swachImagesRegisters()

//                    startPage = 0
//                    endPageNum = 10
//                    setup()
//                }, 1000)
                }


            }
//            getStorePersonAdapter.notifyDataSetChanged()


        }
    }


    override fun onClickFilterIcon() {
        val uploadStatusFilterDialog = context?.let { Dialog(it) }
        val dialogFilterUploadBinding: DialogFilterUploadBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.dialog_filter_upload, null, false)
        uploadStatusFilterDialog!!.setContentView(dialogFilterUploadBinding.root)
        uploadStatusFilterDialog.getWindow()
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogFilterUploadBinding.closeDialog.setOnClickListener {
            uploadStatusFilterDialog.dismiss()
        }
        if (this.complaintListStatus.contains("0")) {
            dialogFilterUploadBinding.isPendingChecked = true
        } else {
            dialogFilterUploadBinding.isPendingChecked = false
        }
        if (this.complaintListStatus.contains("1")) {
            dialogFilterUploadBinding.isApprovedChecked = true
        } else {
            dialogFilterUploadBinding.isApprovedChecked = false
        }
        if (this.complaintListStatus.contains("2")) {
            dialogFilterUploadBinding.isReshootChecked = true
        } else {
            dialogFilterUploadBinding.isReshootChecked = false
        }
        if (this.complaintListStatus.contains("3")) {
            dialogFilterUploadBinding.isPartiallyApprovedChecked = true
        } else {
            dialogFilterUploadBinding.isPartiallyApprovedChecked = false
        }


        submitButtonEnable(dialogFilterUploadBinding)


        dialogFilterUploadBinding.approvedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogFilterUploadBinding)
        }
        dialogFilterUploadBinding.partialyApprovedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogFilterUploadBinding)
        }
        dialogFilterUploadBinding.pendingStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogFilterUploadBinding)
        }
        dialogFilterUploadBinding.reshootStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogFilterUploadBinding)
        }


//        var complaintListStatusTemp = this.complaintListStatus
//        dialogComplaintListFilterBinding.status = complaintListStatusTemp

//        dialogComplaintListFilterBinding.statusRadioGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
//            if (i == R.id.new_status) {
//                complaintListStatusTemp = "new"
//            } else if (i == R.id.in_progress_status) {
//                complaintListStatusTemp = "inprogress"
//            } else if (i == R.id.resolved_status) {
//                complaintListStatusTemp = "solved"
//            } else if (i == R.id.reopen_status) {
//                complaintListStatusTemp = "reopened"
//            } else if (i == R.id.closed_status) {
//                complaintListStatusTemp = "closed"
//            }
//        }


        dialogFilterUploadBinding.submit.setOnClickListener {
//            this.complaintListStatus = complaintListStatusTemp
            this.complaintListStatus = ""
            if (dialogFilterUploadBinding.pendingStatus.isChecked) {
                this.complaintListStatus = "0"
            }
            if (dialogFilterUploadBinding.approvedStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "1"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},1"
                }
            }
            if (dialogFilterUploadBinding.reshootStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "2"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},2"
                }
            }
            if (dialogFilterUploadBinding.partialyApprovedStatus.isChecked) {
                if (this.complaintListStatus.isEmpty()) {
                    this.complaintListStatus = "3"
                } else {
                    this.complaintListStatus = "${this.complaintListStatus},3"
                }
            }

//            complaintListStatus.length


            if (uploadStatusFilterDialog != null && uploadStatusFilterDialog.isShowing) {
                uploadStatusFilterDialog.dismiss()
                startPage = 0
                endPageNum = 10
                callAPI(startPage, endPageNum, complaintListStatus)


            }
            setFilterIndication()
        }
        uploadStatusFilterDialog.show()
    }

    override fun onClickSiteIdIcon() {
        showLoading()
        val i = Intent(context, SelectChampsSiteIDActivity::class.java)
        startActivityForResult(i, 781)
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    fun submitButtonEnable(dialogFilterUploadBinding: DialogFilterUploadBinding) {
        if (!dialogFilterUploadBinding.approvedStatus.isChecked
            && !dialogFilterUploadBinding.partialyApprovedStatus.isChecked
            && !dialogFilterUploadBinding.pendingStatus.isChecked
            && !dialogFilterUploadBinding.reshootStatus.isChecked
        ) {
            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
            dialogFilterUploadBinding.isSubmitEnable = false
        } else {
            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.yellow_drawable)
            dialogFilterUploadBinding.isSubmitEnable = true
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun calcNextFriday(d: LocalDate, dayName: String): LocalDate? {
        var nameOfDayy: DayOfWeek? = null
        if (dayName.equals("Sunday")|| dayName.equals("0")) {
            nameOfDayy = DayOfWeek.SUNDAY
            return d.with(TemporalAdjusters.next(nameOfDayy))
        } else if (dayName.equals("Monday") || dayName.equals("1")) {
            nameOfDayy = DayOfWeek.MONDAY
            return d.with(TemporalAdjusters.next(nameOfDayy))
        } else if (dayName.equals("Tuesday") || dayName.equals("2")) {
            nameOfDayy = DayOfWeek.TUESDAY
            return d.with(TemporalAdjusters.next(nameOfDayy))
        } else if (dayName.equals("Wednesday")|| dayName.equals("3")) {
            nameOfDayy = DayOfWeek.WEDNESDAY
            return d.with(TemporalAdjusters.next(nameOfDayy))
        } else if (dayName.equals("Thursday")|| dayName.equals("4")) {
            nameOfDayy = DayOfWeek.THURSDAY
            return d.with(TemporalAdjusters.next(nameOfDayy))
        } else if (dayName.equals("Friday") || dayName.equals("5")) {
            nameOfDayy = DayOfWeek.FRIDAY
            return d.with(TemporalAdjusters.next(nameOfDayy))
        } else if (dayName.equals("Saturday") || dayName.equals("6")) {
            nameOfDayy = DayOfWeek.SATURDAY
            return d.with(TemporalAdjusters.next(nameOfDayy))
        }else{
            return null
        }
    }

    override fun onClickApproved() {

    }

    override fun onClickPending() {
    }
   var checkDayWiseAccessResponses: CheckDayWiseAccessResponse?=null

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSuccessDayWiseAccesss(checkDayWiseAccessResponse: CheckDayWiseAccessResponse) {
        var isAlreadyUpload:Boolean=false
        if (checkDayWiseAccessResponse != null) {
            val sdf = SimpleDateFormat("EEEE")
            val d = Date()
            charArray.clear()
            positionofftheDayNewForWeek.clear()
            positionofftheDay.clear()
            dayOfCharArrayList.clear()
            dayOfCharArrayListNew.clear()
            val dayOfTheWeek: String = sdf.format(d)
            val sdfc = SimpleDateFormat("dd MMM, yyyy")
            val todaysDate = sdfc.format(Date())


            var lastUploadedDate=""
            if (checkDayWiseAccessResponse.lastUploadedDate != null && !checkDayWiseAccessResponse.lastUploadedDate!!.isEmpty()) {
                val strDate = checkDayWiseAccessResponse.lastUploadedDate
                val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                val date = dateFormat.parse(strDate)
                 lastUploadedDate = SimpleDateFormat("dd MMM, yyyy").format(date)  +" - "+SimpleDateFormat("hh:mm:ss a").format(date)
                viewBinding.uploadedOn.text = lastUploadedDate
                if(lastUploadedDate!=null && !lastUploadedDate.isEmpty() && todaysDate.equals(lastUploadedDate)){
                    isAlreadyUpload=true
                }
            } else {
                viewBinding.uploadedOn.text = "--"
                hideLoading()

            }
            if(lastUploadedDate!=null && !lastUploadedDate.isEmpty() && todaysDate.equals(lastUploadedDate)){
                isAlreadyUpload=true
            }


            charArray.add(checkDayWiseAccessResponse.sunday.toString())
            charArray.add(checkDayWiseAccessResponse.monday.toString())
            charArray.add(checkDayWiseAccessResponse.tuesday.toString())
            charArray.add(checkDayWiseAccessResponse.wednesday.toString())
            charArray.add(checkDayWiseAccessResponse.thursday.toString())
            charArray.add(checkDayWiseAccessResponse.friday.toString())
            charArray.add(checkDayWiseAccessResponse.saturday.toString())

            for (i in charArray.indices) {
                if (charArray.get(i).equals("true")) {
                    positionofftheDay.add(i)
                }
            }

            for (i in positionofftheDay.indices) {
                if (positionofftheDay.get(i)==0) {
                    positionofftheDayNewForWeek.add("Sunday")
                } else if (positionofftheDay.get(i)==1) {
                    positionofftheDayNewForWeek.add("Monday")
                } else if (positionofftheDay.get(i)==2) {
                    positionofftheDayNewForWeek.add("Tuesday")
                } else if (positionofftheDay.get(i)==3) {
                    positionofftheDayNewForWeek.add("Wednesday")
                } else if (positionofftheDay.get(i)==4) {
                    positionofftheDayNewForWeek.add("Thursday")
                } else if (positionofftheDay.get(i)==5) {
                    positionofftheDayNewForWeek.add("Friday")
                } else if (positionofftheDay.get(i)==6) {
                    positionofftheDayNewForWeek.add("Saturday")
                }
            }



            if(positionofftheDayNewForWeek.size>1 && positionofftheDayNewForWeek.contains(dayOfTheWeek) && isAlreadyUpload){
                var removePos:Int=0
                positionofftheDayNewForWeek.size
                for (i in positionofftheDayNewForWeek.indices) {
                    if (positionofftheDayNewForWeek.get(i)==dayOfTheWeek) {
                        removePos=i
                    }
                }
                positionofftheDayNewForWeek.removeAt(removePos)
            }

            for (i in positionofftheDayNewForWeek.indices) {
                var dayOfCharArrayListModel = DayOfCharArrayListModel()
                if (positionofftheDayNewForWeek.get(i) == "Sunday") {
                    dayofCharArray = "Sunday"
                    dayOfCharArrayListModel!!.weekname="Sunday"
                    dayOfCharArrayListModel!!.weekposition=0
                    dayOfCharArrayListModel!!.localDate = nextComingdayDate(LocalDate.now(),"Sunday")
                } else if (positionofftheDayNewForWeek.get(i) == "Monday") {
                    dayofCharArray = "Monday"
                    dayOfCharArrayListModel!!.weekname="Monday"
                    dayOfCharArrayListModel!!.weekposition=1
                    dayOfCharArrayListModel!!.localDate = nextComingdayDate(LocalDate.now(),"Monday")
                } else if (positionofftheDayNewForWeek.get(i) == "Tuesday") {
                    dayofCharArray = "Tuesday"
                    dayOfCharArrayListModel!!.weekname="Tuesday"
                    dayOfCharArrayListModel!!.weekposition=2
                    dayOfCharArrayListModel!!.localDate = nextComingdayDate(LocalDate.now(),"Tuesday")
                } else if (positionofftheDayNewForWeek.get(i) == "Wednesday") {
                    dayofCharArray = "Wednesday"
                    dayOfCharArrayListModel!!.weekname="Wednesday"
                    dayOfCharArrayListModel!!.weekposition=3
                    dayOfCharArrayListModel!!.localDate = nextComingdayDate(LocalDate.now(),"Wednesday")
                } else if (positionofftheDayNewForWeek.get(i) == "Thursday") {
                    dayofCharArray = "Thursday"
                    dayOfCharArrayListModel!!.weekname="Thursday"
                    dayOfCharArrayListModel!!.weekposition=4
                    dayOfCharArrayListModel!!.localDate = nextComingdayDate(LocalDate.now(),"Thursday")
                } else if (positionofftheDayNewForWeek.get(i) == "Friday") {
                    dayofCharArray = "Friday"
                    dayOfCharArrayListModel!!.weekname="Friday"
                    dayOfCharArrayListModel!!.weekposition=5
                    dayOfCharArrayListModel!!.localDate = nextComingdayDate(LocalDate.now(),"Friday")
                } else if (positionofftheDayNewForWeek.get(i) == "Saturday") {
                    dayofCharArray = "Saturday"
                    dayOfCharArrayListModel!!.weekname="Saturday"
                    dayOfCharArrayListModel!!.weekposition=6
                    dayOfCharArrayListModel!!.localDate = nextComingdayDate(LocalDate.now(),"Saturday")
                }
//                dayOfCharArrayList.add(dayofCharArray!!)
                dayOfCharArrayListNew.add(dayOfCharArrayListModel!!)

            }

            var nearestDate : LocalDate? = null;
            var isCurrentDay = false
            for (i in dayOfCharArrayListNew){
                if (nearestDate != null){
                    if (i.localDate!!.isBefore(nearestDate)){
                        nearestDate = i.localDate
                        positionofday =i.weekposition.toString()

                    }
                }else{
                    nearestDate = i.localDate
                    positionofday =i.weekposition.toString()

                }
                if (dayOfCharArrayListNew.get(dayOfCharArrayListNew.indexOf(i)).weekname.equals(dayOfTheWeek)) {
                    isCurrentDay = true
                }
            }

            val dt = LocalDate.now()
            if (isCurrentDay) {
                    if (isAlreadyUpload){
                        viewBinding.todaysDateLayout.visibility = View.GONE
                        viewBinding.todaysUpdateLayout.visibility = View.GONE
                        viewBinding.alreadyUploadedlayout.visibility = View.GONE
                        viewBinding.uploadNowLayout.visibility = View.GONE
                        viewBinding.uploadOnLayout.visibility = View.VISIBLE
                        val strDate = calcNextFriday(dt, positionofday!!).toString()
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd");
                        val date = dateFormat.parse(strDate.toString())
                        val dateNewFormat = SimpleDateFormat("dd MMM, yyyy, EEEE").format(date)
                        viewBinding.dayoftheweekLayout.text = dateNewFormat
                    }else{
                        viewBinding.todaysDateLayout.visibility = View.GONE
                        viewBinding.todaysUpdateLayout.visibility = View.GONE
                        viewBinding.alreadyUploadedlayout.visibility = View.GONE
                        viewBinding.uploadNowLayout.visibility = View.VISIBLE
                        viewBinding.uploadOnLayout.visibility = View.GONE

                    }
            }
            if (!isCurrentDay && positionofday != null) {
                viewBinding.todaysDateLayout.visibility = View.GONE
                viewBinding.todaysUpdateLayout.visibility = View.GONE
                viewBinding.uploadNowLayout.visibility = View.GONE
                viewBinding.uploadOnLayout.visibility = View.VISIBLE
                val strDate = calcNextFriday(dt, positionofday!!).toString()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy, EEEE").format(date)
                viewBinding.dayoftheweekLayout.text = dateNewFormat
            }


        }




        else{

        }
        // hideLoading()

    }

    override fun onBackPressedUpload() {

    }

    override fun onSuccessLastUploadedDate(value: LastUploadedDateResponse) {
        if (value != null && value.status == true) {
            if (value.uploadedDate != null && !value.uploadedDate!!.isEmpty()) {
                val strDate = value.uploadedDate
                val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                val date = dateFormat.parse(strDate)
                val dateNewFormat = SimpleDateFormat("dd MMM, yyyy").format(date)// - hh:mm a
                viewBinding.uploadedOn.text = dateNewFormat
            } else {
                viewBinding.uploadedOn.text = "--"
                hideLoading()

            }
        }
        if(!Preferences.getToken().isEmpty()){
            viewBinding.userId.text = Preferences.getToken()
        }else{
            viewBinding.userId.text = "--"
        }
    }

    override fun onSuccessgetStorePersonHistory(value: GetStorePersonHistoryodelResponse) {
        hideLoading()

        if (viewBinding.pullToRefresh.isRefreshing) {
            viewBinding.pullToRefresh.isRefreshing = false


            if (value.getList != null && value.getList!!.size > 0) {

                viewBinding.noOrdersFound.visibility = View.GONE
                viewBinding.imageRecyclerView.visibility = View.VISIBLE
                getStorePersonAdapter =
                    GetStorePersonAdapter(value.getList,
                        this
                    )
                layoutManager = LinearLayoutManager(ViswamApp.context)
                viewBinding.imageRecyclerView.layoutManager = layoutManager
                viewBinding.imageRecyclerView.itemAnimator =
                    DefaultItemAnimator()
                viewBinding.imageRecyclerView.adapter = getStorePersonAdapter
            } else {
                viewBinding.noOrdersFound.visibility = View.VISIBLE
                viewBinding.imageRecyclerView.visibility = View.GONE
            }
        }
        else {

            if (isLoading) {
                viewBinding.noOrdersFound.visibility = View.GONE
                viewBinding.imageRecyclerView.visibility = View.VISIBLE
                //  hideLoading()
                getStorePersonAdapter.getData()
                    ?.removeAt(getStorePersonAdapter.getData()!!.size - 1)
                var listSize = getStorePersonAdapter.getData()!!.size
                getStorePersonAdapter.notifyItemRemoved(listSize)
                getStorePersonAdapter.getData()
                    ?.addAll((value.getList!! as ArrayList<Get>?)!!)
                getStorePersonAdapter.notifyDataSetChanged()

//                        if (getStorePersonAdapter.getData() != null && getStorePersonAdapter.getData()?.size!! > 0) {
//                            viewBinding.imageRecyclerView.visibility = View.VISIBLE
//                            viewBinding.noOrdersFound.visibility = View.GONE
//                        } else {
//                            viewBinding.imageRecyclerView.visibility = View.GONE
//                            viewBinding.noOrdersFound.visibility = View.VISIBLE
//                        }
                isLoading = false
            } else {
                viewBinding.noOrdersFound.visibility = View.GONE
                viewBinding.imageRecyclerView.visibility = View.VISIBLE
                getStorePersonAdapter =
                    GetStorePersonAdapter(value.getList,
                        this
                    )
                layoutManager = LinearLayoutManager(ViswamApp.context)
                viewBinding.imageRecyclerView.layoutManager = layoutManager
                viewBinding.imageRecyclerView.itemAnimator =
                    DefaultItemAnimator()
                viewBinding.imageRecyclerView.adapter = getStorePersonAdapter

                if (value.getList != null && value.getList?.size!! > 0) {
                    viewBinding.imageRecyclerView.visibility = View.VISIBLE
                    viewBinding.noOrdersFound.visibility = View.GONE
                } else {
                    viewBinding.imageRecyclerView.visibility = View.GONE
                    viewBinding.noOrdersFound.visibility = View.VISIBLE
                }
//                    Toast.makeText(context, "success api, ${getStorePersonHistoryList.get(0).getList?.size}", Toast.LENGTH_SHORT).show()
                //  hideLoading()
            }
        }
    }

    override fun onSuccessOnUploadSwach(value: OnUploadSwachModelResponse) {
            hideLoading()


            if (value != null && value.status == true) {
//                Toast.makeText(ViswamApp.context, "" + it.message, Toast.LENGTH_SHORT).show()
                hideLoading()

            } else if (value != null && value.status == false && value.message == "ALREADY UPLAODED") {

//                if(positionofftheDayNewForWeek.size==1){
//                    val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy, EEEE")
//                    val cal = Calendar.getInstance()
//                    cal.add(Calendar.DATE, +7)
//                    val nextUploadDate = simpleDateFormat.format(cal.time)
//
//
////                    viewBinding.uploadNowLayout.visibility = View.GONE
////                    viewBinding.alreadyUploadedlayout.visibility = View.GONE
////                    viewBinding.uploadOnLayout.visibility = View.GONE
////                    viewBinding.uploadNowGrey.visibility = View.VISIBLE
////                    viewBinding.nextUploadDate.text = nextUploadDate
////                    Utlis.hideLoading()
//                    viewBinding.uploadNowLayout.visibility = View.GONE
//                    viewBinding.todaysDateLayout.visibility = View.GONE
//                    viewBinding.todaysUpdateLayout.visibility = View.GONE
//                    viewBinding.alreadyUploadedlayout.visibility = View.GONE
//                    viewBinding.uploadOnLayout.visibility = View.GONE
//                    viewBinding.uploadNowGrey.visibility = View.VISIBLE
//                    viewBinding.nextUploadDate.text = nextUploadDate
//                    hideLoading()
//                }
//                    else if(positionofftheDayNewForWeek.size>1){
//                        viewBinding.uploadNowLayout.visibility = View.GONE
//                        viewBinding.todaysDateLayout.visibility = View.GONE
//                        viewBinding.todaysUpdateLayout.visibility = View.GONE
//                        viewBinding.alreadyUploadedlayout.visibility = View.GONE
//                        viewBinding.uploadOnLayout.visibility = View.GONE
//                        viewBinding.uploadNowGrey.visibility = View.VISIBLE
//                        viewBinding.nextUploadDate.text = positionofday
//                        hideLoading()
//                    }
//                Toast.makeText(ViswamApp.context, "" + it.message, Toast.LENGTH_SHORT).show()


            } else{

            }

    }

    var mProgressDialogTemp: ProgressDialog? = null
    fun showLoadingTemp(context: Context) {
        hideLoadingTemp()
        mProgressDialogTemp = showLoadingDialogTemp(context)
    }

    fun hideLoadingTemp() {
        if (mProgressDialogTemp != null && mProgressDialogTemp!!.isShowing()) {
            mProgressDialogTemp!!.dismiss()
        }
    }

    fun showLoadingDialogTemp(context: Context?): ProgressDialog? {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }
    @SuppressLint("NewApi")
    private fun nextComingdayDate(d: LocalDate, dayName : String): LocalDate? {
        var nameOfDayy: DayOfWeek? = null
        if (dayName == "Sunday") {

            return d.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
        } else if (dayName == "Monday") {

            return d.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        } else if (dayName == "Tuesday") {

            return d.with(TemporalAdjusters.next(DayOfWeek.TUESDAY))
        } else if (dayName == "Wednesday") {

            return d.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY))
        } else if (dayName == "Thursday") {

            return d.with(TemporalAdjusters.next(DayOfWeek.THURSDAY))
        } else if (dayName == "Friday") {

            return d.with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
        } else if (dayName == "Saturday") {

            return d.with(TemporalAdjusters.next(DayOfWeek.SATURDAY))
        }else{
            return null
        }

    }
}