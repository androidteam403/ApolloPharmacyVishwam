package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityGetSurveyDetailsBinding
import com.apollopharmacy.vishwam.databinding.DialoFilterChampsBinding
import com.apollopharmacy.vishwam.dialog.ComplaintListCalendarDialog
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MainActivityPlusIconCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.champs.admin.adminmodule.AdminModuleFragment
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.NewSurveyFragment
import com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.adapter.GetSurveyDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.GlobalConfigurationResponse
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseEmpIdResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurveyDetailsModelResponse
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class GetSurveyDetailsListActivity :
    BaseFragment<GetSurveyDetailsListViewModel, ActivityGetSurveyDetailsBinding>(),
    MainActivityCallback, GetSurveyDetailsListCallback, MainActivityPlusIconCallback,ComplaintListCalendarDialog.DateSelected {
    //    private lateinit var activityGetSurveyDetailsBinding: ActivityGetSurveyDetailsBinding
//    private lateinit var getSurveyDetailsListViewModel: GetSurveyDetailsListViewModel
    private lateinit var getSurveyDetailsAdapter: GetSurveyDetailsAdapter
    var isCompletedStatus = true
    var inProgressStatus = true
    private var getStoreWiseDetails: GetStoreWiseDetailsModelResponse? = null
    private var getStoreWiseEmpIdResponse: GetStoreWiseEmpIdResponse? = null
    var surveyRecDetailsList = ArrayList<String>()
    var surveyCCDetailsList = ArrayList<String>()
    var siteName: String? = ""
    private var storeId: String = ""
    private var address: String = ""
    var isFromDateSelected: Boolean = false
    private var storeCity: String = ""
    private var region: String = ""
    private var fromdate:String=""
    private var toDate:String=""
    var dialogFilterUploadBinding: DialoFilterChampsBinding?=null
    var champsStatus: String = "Pending, Completed"

//    @RequiresApi(Build.VERSION_CODES.O)
//    @SuppressLint("WrongViewCast")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_start_survey2)
//
//        activityGetSurveyDetailsBinding = DataBindingUtil.setContentView(
//            this,
//            R.layout.activity_get_survey_details
//
//        )
//        getSurveyDetailsListViewModel =
//            ViewModelProvider(this)[GetSurveyDetailsListViewModel::class.java]
//
//        setUp()
//
//    }

    override val layoutRes: Int
        get() = R.layout.activity_get_survey_details

    override fun retrieveViewModel(): GetSurveyDetailsListViewModel {
        return ViewModelProvider(this).get(GetSurveyDetailsListViewModel::class.java)
    }


    override fun setup() {
//        activityGetSurveyDetailsBinding.callback = this
        viewBinding.callback = this
        MainActivity.mInstance.mainActivityCallback = this
        MainActivity.mInstance.mainActivityPlusIconCallback = this
//        getStoreWiseEmpIdResponse =
//            intent.getSerializableExtra("getStoreWiseEmpIdResponse") as GetStoreWiseEmpIdResponse?
//        getStoreWiseDetails =
//            intent.getSerializableExtra("getStoreWiseDetails") as GetStoreWiseDetailsModelResponse?
//        surveyRecDetailsList =
//            intent.getStringArrayListExtra("surveyRecDetailsList")!!
//        surveyCCDetailsList = intent.getStringArrayListExtra("surveyCCDetailsList")!!
//        address = intent.getStringExtra("address")!!
//        storeId = intent.getStringExtra("storeId")!!
//        siteName = intent.getStringExtra("siteName")
//        storeCity = intent.getStringExtra("storeCity")!!
//        region = intent.getStringExtra("region")!!
//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
//        val cal = Calendar.getInstance()
//        cal.add(Calendar.DATE, -6)
//        val currentDate: String = simpleDateFormat.format(Date())
//
//        var fromdate = simpleDateFormat.format(cal.time)
//        var toDate = currentDate
        if (NetworkUtil.isNetworkConnected(requireActivity())) {
            Utlis.showLoading(requireActivity())
            viewModel.getGlobalConfigApi(
                this
            );

        } else {
            Toast.makeText(
                ViswamApp.context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false

            dialogFilterUploadBinding!!.fromDateText.setText(showingDate)
            val fromDate = dialogFilterUploadBinding!!.fromDateText.text.toString()
            val toDate = dialogFilterUploadBinding!!.toDateText.text.toString()
            if (Utils.getDateDifference(fromDate, toDate) == 0) {
                dialogFilterUploadBinding!!.toDateText.setText(Utils.getCurrentDate())
            }
        } else {
            dialogFilterUploadBinding!!.toDateText.setText(showingDate)
        }
    }

    override fun selectedDatefrom(dateSelected: String, showingDate: String) {
        TODO("Not yet implemented")
    }

    var getSurvetDetailsModelResponses: GetSurveyDetailsModelResponse? = null

    override fun onSuccessSurveyList(getSurvetDetailsModelResponse: GetSurveyDetailsModelResponse) {
        getSurvetDetailsModelResponses = getSurvetDetailsModelResponse
        var getSurvetDetailsModelResponsesList =
            ArrayList<GetSurveyDetailsModelResponse.StoreDetail>()
        getSurvetDetailsModelResponsesList =
            getSurvetDetailsModelResponse.storeDetails as ArrayList<GetSurveyDetailsModelResponse.StoreDetail>

        if (getSurvetDetailsModelResponse != null && getSurvetDetailsModelResponse.storeDetails != null &&
            getSurvetDetailsModelResponse.storeDetails.size > 0
        ) {
            getSurvetDetailsModelResponse.storeDetails.sortByDescending { it.visitDate }
            viewBinding.noListFound.visibility = View.GONE
            viewBinding.recyclerViewList.visibility = View.VISIBLE
            if (champsStatus.contains("Pending") && champsStatus.contains("Completed")) {
                viewBinding.noListFound.visibility = View.GONE
                viewBinding.recyclerViewList.visibility = View.VISIBLE
                getSurvetDetailsModelResponses!!.storeDetails =
                    getSurvetDetailsModelResponse.storeDetails
                val sortedStoreDetails =
                    getSurvetDetailsModelResponse?.storeDetails?.sortedByDescending { it.champsRefernceId }
                        ?: emptyList()
//                val sortedStoreDetails = getSurvetDetailsModelResponsesList.sortedWith(compareByDescending<GetSurveyDetailsModelResponse.StoreDetail> { it.visitDate }.thenByDescending { it.visitDate })

                getSurveyDetailsAdapter =
                    GetSurveyDetailsAdapter(
                        sortedStoreDetails, requireActivity(), this
                    )
                viewBinding.recyclerViewList.setLayoutManager(
                    LinearLayoutManager(requireActivity())
                )
                viewBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
            } else if (champsStatus.contains("Completed")) {
                if (getSurvetDetailsModelResponse.storeDetails.filter { it.status.equals("COMPLETED") }.size > 0) {
                    viewBinding.noListFound.visibility = View.GONE
                    viewBinding.recyclerViewList.visibility = View.VISIBLE
                    val sortedStoreDetails =
                        getSurvetDetailsModelResponse?.storeDetails?.sortedByDescending { it.champsRefernceId }
                            ?: emptyList()
                    getSurveyDetailsAdapter =
                        GetSurveyDetailsAdapter(
                            sortedStoreDetails.filter { it.status.equals("COMPLETED") },
                            requireActivity(),
                            this
                        )
                    viewBinding.recyclerViewList.setLayoutManager(
                        LinearLayoutManager(requireActivity())
                    )
                    viewBinding.recyclerViewList.setAdapter(
                        getSurveyDetailsAdapter
                    )
                } else {
                    viewBinding.noListFound.visibility = View.VISIBLE
                    viewBinding.recyclerViewList.visibility = View.GONE
                }
            } else if (champsStatus.contains("Pending")) {
                if (getSurvetDetailsModelResponse.storeDetails.filter { it.status.equals("PENDING") }.size > 0) {
                    viewBinding.noListFound.visibility = View.GONE
                    viewBinding.recyclerViewList.visibility = View.VISIBLE
                    val sortedStoreDetails =
                        getSurvetDetailsModelResponse?.storeDetails?.sortedByDescending { it.champsRefernceId }
                            ?: emptyList()
                    getSurveyDetailsAdapter =
                        GetSurveyDetailsAdapter(
                            sortedStoreDetails.filter { it.status.equals("PENDING") },
                            requireActivity(),
                            this
                        )
                    viewBinding.recyclerViewList.setLayoutManager(
                        LinearLayoutManager(requireActivity())
                    )
                    viewBinding.recyclerViewList.setAdapter(
                        getSurveyDetailsAdapter
                    )
                } else {
                    viewBinding.noListFound.visibility = View.VISIBLE
                    viewBinding.recyclerViewList.visibility = View.GONE
                }
            }


        } else {
            viewBinding.noListFound.visibility = View.VISIBLE
            viewBinding.recyclerViewList.visibility = View.GONE
        }
        Utlis.hideLoading()

    }

    override fun onFailureSurveyList(getSurvetDetailsModelResponse: GetSurveyDetailsModelResponse) {
//    Toast.makeText(applicationContext, ""+getSurvetDetailsModelResponse.message, Toast.LENGTH_SHORT).show()
        viewBinding.noListFound.visibility = View.VISIBLE
        viewBinding.recyclerViewList.visibility = View.GONE
        Utlis.hideLoading()
    }

    override fun onClickCardView(
        status: String?,
        champsRefernceId: String?,
        sitenames: String,
        visitDate: String,
    ) {
        val intent = Intent(ViswamApp.context, ChampsSurveyActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("getStoreWiseDetails", getStoreWiseDetails)
        intent.putExtra("getStoreWiseEmpIdResponse", getStoreWiseEmpIdResponse)
        intent.putExtra("address", address)
        intent.putExtra("storeId", storeId)
        intent.putExtra("siteNameForAddress", sitenames)
        intent.putExtra("siteName", siteName)
        intent.putExtra("storeCity", storeCity)
        intent.putExtra("status", status)

        intent.putExtra("visitDate", visitDate)

        intent.putExtra("champsRefernceId", champsRefernceId)
        intent.putStringArrayListExtra("surveyRecDetailsList", surveyRecDetailsList)
        intent.putStringArrayListExtra("surveyCCDetailsList", surveyCCDetailsList)
        intent.putExtra("region", region)
        requireActivity().startActivityForResult(intent, 781)
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPlusIcon() {
        val intent = Intent(ViswamApp.context, NewSurveyFragment::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("getStoreWiseDetails", getStoreWiseDetails)
        intent.putExtra("getStoreWiseEmpIdResponse", getStoreWiseEmpIdResponse)
        intent.putExtra("address", address)
        intent.putExtra("storeId", storeId)
        intent.putExtra("siteName", siteName)
        intent.putExtra("storeCity", storeCity)
        intent.putExtra("region", region)
        intent.putExtra("status", "NEW")
        intent.putStringArrayListExtra("surveyRecDetailsList", surveyRecDetailsList)
        intent.putStringArrayListExtra("surveyCCDetailsList", surveyCCDetailsList)
        requireActivity().startActivityForResult(intent, 891)
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickSettings() {
        val intent = Intent(ViswamApp.context, AdminModuleFragment::class.java)
        startActivity(intent)
    }

    override fun onClickback() {
        MainActivity.mInstance.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 781 && resultCode == RESULT_OK || requestCode == 891 && resultCode == RESULT_OK ) {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -6)
            val currentDate: String = simpleDateFormat.format(Date())

            var fromdate = simpleDateFormat.format(cal.time)
            var toDate = currentDate
            if (NetworkUtil.isNetworkConnected(requireActivity())) {
                Utlis.showLoading(requireActivity())
                viewModel.getSurveyListApi(
                    this,
                    fromdate,
                    toDate,
                    Preferences.getValidatedEmpId()
                );

            } else {
                Toast.makeText(
                    ViswamApp.context,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickFilterIcon() {
        val uploadStatusFilterDialog = this?.let { Dialog(requireActivity()) }
         dialogFilterUploadBinding=
            DataBindingUtil.inflate(
                LayoutInflater.from(requireActivity()), R.layout.dialo_filter_champs, null, false
            )
        uploadStatusFilterDialog!!.setContentView(dialogFilterUploadBinding!!.root)
        uploadStatusFilterDialog.getWindow()
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))




        val strFromate = fromdate
        val fromDateFormat = SimpleDateFormat("yyyy-MM-dd");
        val frommdate = fromDateFormat.parse(strFromate)
        val fromDateNewFormat = SimpleDateFormat("dd-MMM-yyyy").format(frommdate)

        dialogFilterUploadBinding!!.fromDateText.text = fromDateNewFormat.toString()

        val strToDate = toDate
        val dateFormat = SimpleDateFormat("yyyy-MM-dd");
        val tooDate = dateFormat.parse(strToDate)
        val dateNewFormat = SimpleDateFormat("dd-MMM-yyyy").format(tooDate)
        dialogFilterUploadBinding!!.toDateText.text = dateNewFormat.toString()

        dialogFilterUploadBinding!!.closeDialog.setOnClickListener {
            uploadStatusFilterDialog.dismiss()
        }
        dialogFilterUploadBinding!!.clearAllFilters.setOnClickListener {
            dialogFilterUploadBinding!!.selectAll.isChecked = true
            if (dialogFilterUploadBinding!!.selectAll.isChecked) {
                isCompletedStatus = true
                inProgressStatus = true
                dialogFilterUploadBinding!!.resolvedStatus.background = requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogFilterUploadBinding!!.resolvedStatus.setTextColor(requireContext().resources.getColor(R.color.white_for_both))
                dialogFilterUploadBinding!!.inProgressStatus.background =  requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogFilterUploadBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(R.color.white_for_both))
                dialogFilterUploadBinding!!.selectAll.isChecked = true
            }
            val strFromate = fromdate
            val fromDateFormat = SimpleDateFormat("yyyy-MM-dd");
            val frommdate = fromDateFormat.parse(strFromate)
            val fromDateNewFormat = SimpleDateFormat("dd-MMM-yyyy").format(frommdate)

            val strToDate = toDate
            val dateFormat = SimpleDateFormat("yyyy-MM-dd");
            val tooDate = dateFormat.parse(strToDate)
            val dateNewFormat = SimpleDateFormat("dd-MMM-yyyy").format(tooDate)

            dialogFilterUploadBinding!!.fromDateText.text = fromDateNewFormat
            dialogFilterUploadBinding!!.toDateText.text = dateNewFormat
            this.champsStatus = ""
            if (isCompletedStatus) {
                this.champsStatus = "Completed"
            }
            if (inProgressStatus) {
                if (this.champsStatus.isEmpty()) {
                    this.champsStatus = "Pending"
                } else {
                    this.champsStatus = "${this.champsStatus},Pending"
                }
            }

            if (dialogFilterUploadBinding!!.selectAll.isChecked) {
                this.champsStatus = "Pending, Completed"
//                    if (this.complaintListStatus.isEmpty()) {
//                        this.complaintListStatus = "new,inprogress,solved,rejected,reopened,closed,onHold"
//                    } else {
//                        this.complaintListStatus = "new,inprogress,solved,rejected,reopened,closed,onHold"
//                    }
            }
            submitButtonEnable(
                isCompletedStatus,
                inProgressStatus,
                dialogFilterUploadBinding!!
            )
//            setFilterIndication()

        }
        dialogFilterUploadBinding!!.fromDate.setOnClickListener {
            isFromDateSelected = true
            openDateDialog()
        }
        dialogFilterUploadBinding!!.toDate.setOnClickListener {
            isFromDateSelected = false
            openDateDialog()
        }

        if ( isCompletedStatus &&
            inProgressStatus
        ) {
            dialogFilterUploadBinding!!.selectAll.isChecked = true
        } else {
            dialogFilterUploadBinding!!.selectAll.isChecked = false
        }

        if(isCompletedStatus){
            dialogFilterUploadBinding!!.resolvedStatus.background = requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
            dialogFilterUploadBinding!!.resolvedStatus.setTextColor(requireContext().resources.getColor(R.color.white_for_both))
        }else{
            dialogFilterUploadBinding!!.resolvedStatus.background = requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
            dialogFilterUploadBinding!!.resolvedStatus.setTextColor(requireContext().resources.getColor(R.color.greyyy))
        }
        if(inProgressStatus){
            dialogFilterUploadBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(R.color.white_for_both))
            dialogFilterUploadBinding!!.inProgressStatus.background = requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
        }else{
            dialogFilterUploadBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(R.color.greyyy))
            dialogFilterUploadBinding!!.inProgressStatus.background = requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
        }

        dialogFilterUploadBinding!!.isSelectAllChecked =
            this.champsStatus.contains("Pending, Completed")

//        if (this.champsStatus.contains("Pending")) {
//            dialogFilterUploadBinding!!.isPendingChecked = true
//        } else {
//            dialogFilterUploadBinding!!.isPendingChecked = false
//        }
//        if (this.champsStatus.contains("Completed")) {
//            dialogFilterUploadBinding!!.isCompletedChecked = true
//        } else {
//            dialogFilterUploadBinding!!.isCompletedChecked = false
//        }

        submitButtonEnable(
            isCompletedStatus,
            inProgressStatus,
            dialogFilterUploadBinding!!
        )
//        submitButtonEnable(dialogFilterUploadBinding!!)

        dialogFilterUploadBinding!!.resolvedStatus.setOnClickListener{
            if(isCompletedStatus){
                isCompletedStatus=false
                dialogFilterUploadBinding!!.resolvedStatus.background = requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogFilterUploadBinding!!.resolvedStatus.setTextColor(requireContext().resources.getColor(R.color.greyyy))
            }else{
                isCompletedStatus=true
                dialogFilterUploadBinding!!.resolvedStatus.background = requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogFilterUploadBinding!!.resolvedStatus.setTextColor(requireContext().resources.getColor(R.color.white_for_both))

            }
            submitButtonEnable(
                isCompletedStatus,
                inProgressStatus,
                dialogFilterUploadBinding!!
            )
        }
        dialogFilterUploadBinding!!.inProgressStatus.setOnClickListener {
            if(inProgressStatus){
                inProgressStatus=false
                dialogFilterUploadBinding!!.inProgressStatus.background = requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogFilterUploadBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(R.color.greyyy))

            }else{
                inProgressStatus=true
                dialogFilterUploadBinding!!.inProgressStatus.background = requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogFilterUploadBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(R.color.white_for_both))

            }
            submitButtonEnable(
                isCompletedStatus,
                inProgressStatus,
                dialogFilterUploadBinding!!
            )
        }

        dialogFilterUploadBinding!!.selectAllCheckboxLayout.setOnClickListener {
            dialogFilterUploadBinding!!.selectAll.isChecked =
                !dialogFilterUploadBinding!!.selectAll.isChecked

            if (dialogFilterUploadBinding!!.selectAll.isChecked) {

                isCompletedStatus = true
                inProgressStatus = true
                dialogFilterUploadBinding!!.resolvedStatus.background = requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogFilterUploadBinding!!.resolvedStatus.setTextColor(requireContext().resources.getColor(R.color.white_for_both))
                dialogFilterUploadBinding!!.inProgressStatus.background =  requireContext().resources.getDrawable(R.drawable.skyblue_bgg)
                dialogFilterUploadBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(R.color.white_for_both))
                dialogFilterUploadBinding!!.selectAll.isChecked = true
            }
//            else{
//                dialogComplaintListFilterBinding.selectAll.isChecked=false
////            }
            else {
                isCompletedStatus = false
                inProgressStatus = false
                dialogFilterUploadBinding!!.resolvedStatus.background = requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogFilterUploadBinding!!.resolvedStatus.setTextColor(requireContext().resources.getColor(R.color.greyyy))
                dialogFilterUploadBinding!!.inProgressStatus.background =  requireContext().resources.getDrawable(R.drawable.checkbox_bgg)
                dialogFilterUploadBinding!!.inProgressStatus.setTextColor(requireContext().resources.getColor(R.color.greyyy))
                dialogFilterUploadBinding!!.selectAll.isChecked = true

//                /                if(isNewStatusClicked)
//                dialogComplaintListFilterBinding!!.newStatus.isChecked = false
//                dialogComplaintListFilterBinding!!.inProgressStatus.isChecked = false
//                dialogComplaintListFilterBinding!!.rejectedStatus.isChecked = false
//                dialogComplaintListFilterBinding!!.reopenStatus.isChecked = false
//                dialogComplaintListFilterBinding!!.closedStatus.isChecked = false
//                dialogComplaintListFilterBinding!!.resolvedStatus.isChecked = false
//                dialogComplaintListFilterBinding!!.onholdStatus.isChecked = false
//                dialogComplaintListFilterBinding!!.selectAll.isChecked = false
            }
            submitButtonEnable(
                isCompletedStatus,
                inProgressStatus,
                dialogFilterUploadBinding!!
            )
        }

        dialogFilterUploadBinding!!.submit.setOnClickListener {
            val strFromate = dialogFilterUploadBinding!!.fromDateText.text.toString()
            val fromDateFormat = SimpleDateFormat("dd-MMM-yyyy");
            val frommdate = fromDateFormat.parse(strFromate)
            val fromDateNewFormat = SimpleDateFormat("yyyy-MM-dd").format(frommdate)
            fromdate=fromDateNewFormat


            val strToDate = dialogFilterUploadBinding!!.toDateText.text.toString()
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy");
            val tooDate = dateFormat.parse(strToDate)
            val dateNewFormat = SimpleDateFormat("yyyy-MM-dd").format(tooDate)
            toDate=dateNewFormat


            if (NetworkUtil.isNetworkConnected(requireActivity())) {
                Utlis.showLoading(requireActivity())
                viewModel.getSurveyListApi(
                    this,
                    fromdate,
                    toDate,
                    Preferences.getValidatedEmpId()
                );

            }
            else {
                Toast.makeText(
                    ViswamApp.context,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            this.champsStatus = ""
            if (isCompletedStatus) {
                this.champsStatus = "Completed"
            }
            if (inProgressStatus) {
                if (this.champsStatus.isEmpty()) {
                    this.champsStatus = "Pending"
                } else {
                    this.champsStatus = "${this.champsStatus},Pending"
                }
            }
            if (dialogFilterUploadBinding!!.selectAll.isChecked) {
                this.champsStatus = "Pending, Completed"
//                    if (this.complaintListStatus.isEmpty()) {
//                        this.complaintListStatus = "new,inprogress,solved,rejected,reopened,closed,onHold"
//                    } else {
//                        this.complaintListStatus = "new,inprogress,solved,rejected,reopened,closed,onHold"
//                    }
            }
//            this.complaintListStatus = complaintListStatusTemp
//            this.champsStatus = ""
//            if (dialogFilterUploadBinding!!.inProgressStatus.isChecked) {
//                this.champsStatus = "Pending"
//            }
//            if (dialogFilterUploadBinding!!.resolvedStatus.isChecked) {
//                if (this.champsStatus.isEmpty()) {
//                    this.champsStatus = "Completed"
//                } else {
//                    this.champsStatus = "${this.champsStatus},Completed"
//                }
//
//            }

            if (champsStatus.contains("Pending") && champsStatus.contains("Completed")) {
                viewBinding.noListFound.visibility = View.GONE
                viewBinding.recyclerViewList.visibility = View.VISIBLE
                val sortedStoreDetails =
                    getSurvetDetailsModelResponses?.storeDetails?.sortedByDescending { it.champsRefernceId }
                        ?: emptyList()

                getSurveyDetailsAdapter =
                    GetSurveyDetailsAdapter(
                        sortedStoreDetails, requireActivity(), this
                    )
                viewBinding.recyclerViewList.setLayoutManager(
                    LinearLayoutManager(requireActivity())
                )
                viewBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
            } else if (champsStatus.contains("Completed")) {
                if (getSurvetDetailsModelResponses!!.storeDetails.filter { it.status.equals("COMPLETED") }.size > 0) {

                    viewBinding.noListFound.visibility = View.GONE
                    viewBinding.recyclerViewList.visibility = View.VISIBLE
                    val sortedStoreDetails =
                        getSurvetDetailsModelResponses?.storeDetails?.sortedByDescending { it.champsRefernceId }
                            ?: emptyList()

                    getSurveyDetailsAdapter =
                        GetSurveyDetailsAdapter(
                            sortedStoreDetails!!.filter { it.status.equals("COMPLETED") },
                            requireActivity(),
                            this
                        )
                    viewBinding.recyclerViewList.setLayoutManager(
                        LinearLayoutManager(requireActivity())
                    )
                    viewBinding.recyclerViewList.setAdapter(
                        getSurveyDetailsAdapter
                    )
                } else {
                    viewBinding.noListFound.visibility = View.VISIBLE
                    viewBinding.recyclerViewList.visibility = View.GONE
                }
            } else if (champsStatus.contains("Pending")) {
                if (getSurvetDetailsModelResponses!!.storeDetails.filter { it.status.equals("PENDING") }.size > 0) {
//                    getSurvetDetailsModelResponses!!.storeDetails.filter { it.status.equals("PENDING") }
                    viewBinding.noListFound.visibility = View.GONE
                    viewBinding.recyclerViewList.visibility = View.VISIBLE
                    val sortedStoreDetails =
                        getSurvetDetailsModelResponses?.storeDetails?.sortedByDescending { it.champsRefernceId }
                            ?: emptyList()

                    getSurveyDetailsAdapter =
                        GetSurveyDetailsAdapter(
                            sortedStoreDetails.filter { it.status.equals("PENDING") }!!,
                            requireActivity(),
                            this
                        )
                    viewBinding.recyclerViewList.setLayoutManager(
                        LinearLayoutManager(requireActivity())
                    )
                    viewBinding.recyclerViewList.setAdapter(
                        getSurveyDetailsAdapter
                    )
                } else {
                    viewBinding.noListFound.visibility = View.VISIBLE
                    viewBinding.recyclerViewList.visibility = View.GONE
                }
            }

//            complaintListStatus.length


            if (uploadStatusFilterDialog != null && uploadStatusFilterDialog.isShowing) {
                uploadStatusFilterDialog.dismiss()

            }
        }
        uploadStatusFilterDialog.show()
    }
    var visitDateValue=""
    var champsFilterValue=""
    override fun onSuccessGlobalConfigDetails(value: GlobalConfigurationResponse) {
        if(value.data!=null && value.data!!.listData!=null && value!!.data!!.listData!!.rows!=null && value!!.data!!.listData!!.rows!!.size>0){
            for(i in value!!.data!!.listData!!.rows!!){
                if(i.key.equals("visit_date")){
                    visitDateValue = i.value!!
                    Preferences.setVisitDateValueChamps(visitDateValue)
                }else if(i.key.equals("champs_filter")){
                    champsFilterValue=i.value!!
                }
            }
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            if(!champsFilterValue.isNullOrEmpty()){
                cal.add(Calendar.DATE, -champsFilterValue.toInt())
            }else{
                cal.add(Calendar.DATE, -10)
            }

            val currentDate: String = simpleDateFormat.format(Date())

            fromdate = simpleDateFormat.format(cal.time)
            toDate = currentDate
            if (NetworkUtil.isNetworkConnected(requireActivity())) {
//                Utlis.showLoading(requireActivity())
                viewModel.getSurveyListApi(
                    this,
                    fromdate,
                    toDate,
                    Preferences.getValidatedEmpId()
                );

            } else {
                Toast.makeText(
                    ViswamApp.context,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
//        hideLoading()
    }

    override fun onFailureGlobalConfigDetails(value: GlobalConfigurationResponse) {
        Toast.makeText(requireContext(), value.message.toString(), Toast.LENGTH_SHORT).show()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -10)
        val currentDate: String = simpleDateFormat.format(Date())

         fromdate = simpleDateFormat.format(cal.time)
         toDate = currentDate

        if (NetworkUtil.isNetworkConnected(requireActivity())) {
//            Utlis.showLoading(requireActivity())
            viewModel.getSurveyListApi(
                this,
                fromdate,
                toDate,
                Preferences.getValidatedEmpId()
            );

        } else {
            Toast.makeText(
                ViswamApp.context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
//        hideLoading()
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    fun openDateDialog() {
        if (isFromDateSelected) {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    dialogFilterUploadBinding!!.fromDateText.text.toString(),
                    false,
                    dialogFilterUploadBinding!!.fromDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        } else {
            ComplaintListCalendarDialog().apply {
                arguments = generateParsedData(
                    dialogFilterUploadBinding!!.toDateText.text.toString(),
                    true,
                    dialogFilterUploadBinding!!.fromDateText.text.toString()
                )
            }.show(childFragmentManager, "")
        }
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
        submenus: ArrayList<MenuModel>?,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onclickHelpIcon() {
        TODO("Not yet implemented")
    }

    fun submitButtonEnable( isCompletedStatus: Boolean,
                            inProgressStatus: Boolean,
                            dialogFilterUploadBinding: DialoFilterChampsBinding) {
        if (!isCompletedStatus && !inProgressStatus) {
            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
            dialogFilterUploadBinding.isSubmitEnable = false
            dialogFilterUploadBinding.isSelectAllChecked= false
        } else if (isCompletedStatus && inProgressStatus) {
            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.dark_blue_bg_for_btn)
            dialogFilterUploadBinding.isSubmitEnable = true
            dialogFilterUploadBinding.isSelectAllChecked = true
        } else {
            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.dark_blue_bg_for_btn)
            dialogFilterUploadBinding.isSubmitEnable = true
            dialogFilterUploadBinding.isSelectAllChecked = false
        }
//        if (!dialogFilterUploadBinding.approvedStatus.isChecked
//            && !dialogFilterUploadBinding.pendingStatus.isChecked
//        ) {
//            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
//            dialogFilterUploadBinding.isSubmitEnable = false
//        } else {
//            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.search_button_bg)
//            dialogFilterUploadBinding.isSubmitEnable = true
//        }
    }


}