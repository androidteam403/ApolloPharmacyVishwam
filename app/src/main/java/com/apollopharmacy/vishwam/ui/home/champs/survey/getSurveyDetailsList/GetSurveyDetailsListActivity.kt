package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityGetSurveyDetailsBinding
import com.apollopharmacy.vishwam.databinding.DialoFilterChampsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.adapter.GetSurveyDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseEmpIdResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurveyDetailsModelResponse
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class GetSurveyDetailsListActivity : AppCompatActivity() , GetSurveyDetailsListCallback{
    private lateinit var activityGetSurveyDetailsBinding: ActivityGetSurveyDetailsBinding
    private lateinit var getSurveyDetailsListViewModel: GetSurveyDetailsListViewModel
    private lateinit var getSurveyDetailsAdapter: GetSurveyDetailsAdapter
    private var getStoreWiseDetails: GetStoreWiseDetailsModelResponse? = null
    private var getStoreWiseEmpIdResponse : GetStoreWiseEmpIdResponse?=null
    var surveyRecDetailsList = ArrayList<String>()
    var surveyCCDetailsList = ArrayList<String>()
    var siteName: String? = ""
    private var storeId: String = ""
    private var address: String = ""
    private var storeCity: String = ""
    private var region:String=""
    var champsStatus: String = "Pending, Completed"
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_start_survey2)

        activityGetSurveyDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_get_survey_details

        )
        getSurveyDetailsListViewModel = ViewModelProvider(this)[GetSurveyDetailsListViewModel::class.java]

        setUp()

    }

    private fun setUp() {
        activityGetSurveyDetailsBinding.callback=this
        getStoreWiseEmpIdResponse=intent.getSerializableExtra("getStoreWiseEmpIdResponse") as GetStoreWiseEmpIdResponse?
        getStoreWiseDetails =
            intent.getSerializableExtra("getStoreWiseDetails") as GetStoreWiseDetailsModelResponse?
        surveyRecDetailsList =
            intent.getStringArrayListExtra("surveyRecDetailsList")!!
        surveyCCDetailsList = intent.getStringArrayListExtra("surveyCCDetailsList")!!
        address = intent.getStringExtra("address")!!
        storeId = intent.getStringExtra("storeId")!!
        siteName = intent.getStringExtra("siteName")
        storeCity = intent.getStringExtra("storeCity")!!
        region=intent.getStringExtra("region")!!
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        val currentDate: String = simpleDateFormat.format(Date())

        var fromdate = simpleDateFormat.format(cal.time)
        var toDate = currentDate
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            getSurveyDetailsListViewModel.getSurveyListApi(this, fromdate, toDate, Preferences.getValidatedEmpId());

        } else {
            Toast.makeText(
                ViswamApp.context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
    var getSurvetDetailsModelResponses: GetSurveyDetailsModelResponse?=null
    override fun onSuccessSurveyList(getSurvetDetailsModelResponse: GetSurveyDetailsModelResponse) {
        getSurvetDetailsModelResponses=getSurvetDetailsModelResponse
        if(getSurvetDetailsModelResponse!=null && getSurvetDetailsModelResponse.storeDetails!=null &&
                getSurvetDetailsModelResponse.storeDetails.size>0){
            getSurvetDetailsModelResponse.storeDetails.sortByDescending { it.visitDate }
            activityGetSurveyDetailsBinding.noListFound.visibility= View.GONE
            activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.VISIBLE
            if(champsStatus.contains("Pending") && champsStatus.contains("Completed")){
                activityGetSurveyDetailsBinding.noListFound.visibility= View.GONE
                activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.VISIBLE
                getSurveyDetailsAdapter =
                    GetSurveyDetailsAdapter(getSurvetDetailsModelResponse.storeDetails, applicationContext, this
                    )
                activityGetSurveyDetailsBinding.recyclerViewList.setLayoutManager(
                    LinearLayoutManager(this)
                )
                activityGetSurveyDetailsBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
            }
            else if(champsStatus.contains("Completed")){
                if (getSurvetDetailsModelResponse.storeDetails.filter { it.status.equals("COMPLETED") }.size > 0) {
                    activityGetSurveyDetailsBinding.noListFound.visibility= View.GONE
                    activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.VISIBLE
                    getSurveyDetailsAdapter =
                        GetSurveyDetailsAdapter(getSurvetDetailsModelResponse.storeDetails.filter { it.status.equals("COMPLETED") }, applicationContext, this
                        )
                    activityGetSurveyDetailsBinding.recyclerViewList.setLayoutManager(
                        LinearLayoutManager(this)
                    )
                    activityGetSurveyDetailsBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
                }else{
                    activityGetSurveyDetailsBinding.noListFound.visibility= View.VISIBLE
                    activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.GONE
                }
            }
            else if(champsStatus.contains("Pending")){
                if (getSurvetDetailsModelResponse.storeDetails.filter { it.status.equals("PENDING") }.size > 0) {
                    activityGetSurveyDetailsBinding.noListFound.visibility= View.GONE
                    activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.VISIBLE
                    getSurveyDetailsAdapter =
                        GetSurveyDetailsAdapter(getSurvetDetailsModelResponse.storeDetails.filter { it.status.equals("PENDING") }, applicationContext, this
                        )
                    activityGetSurveyDetailsBinding.recyclerViewList.setLayoutManager(
                        LinearLayoutManager(this)
                    )
                    activityGetSurveyDetailsBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
                }else{
                    activityGetSurveyDetailsBinding.noListFound.visibility= View.VISIBLE
                    activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.GONE
                }
            }


        }else{
            activityGetSurveyDetailsBinding.noListFound.visibility= View.VISIBLE
            activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.GONE
        }
        Utlis.hideLoading()

    }

    override fun onFailureSurveyList(getSurvetDetailsModelResponse: GetSurveyDetailsModelResponse) {
//    Toast.makeText(applicationContext, ""+getSurvetDetailsModelResponse.message, Toast.LENGTH_SHORT).show()
        activityGetSurveyDetailsBinding.noListFound.visibility= View.VISIBLE
        activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.GONE
        Utlis.hideLoading()
    }

    override fun onClickCardView(status: String?, champsRefernceId: String?) {
        val intent = Intent(ViswamApp.context, ChampsSurveyActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("getStoreWiseDetails", getStoreWiseDetails)
        intent.putExtra("getStoreWiseEmpIdResponse", getStoreWiseEmpIdResponse)
        intent.putExtra("address", address)
        intent.putExtra("storeId", storeId)
        intent.putExtra("siteName", siteName)
        intent.putExtra("storeCity", storeCity)
        intent.putExtra("status", status)
        intent.putExtra("champsRefernceId", champsRefernceId)
        intent.putStringArrayListExtra("surveyRecDetailsList", surveyRecDetailsList)
        intent.putStringArrayListExtra("surveyCCDetailsList", surveyCCDetailsList)
        intent.putExtra("region", region)
        startActivityForResult(intent, 781)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPlusIcon() {
        val intent = Intent(ViswamApp.context, ChampsSurveyActivity::class.java)
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
        startActivityForResult(intent, 781)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickback() {
       super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==781 && resultCode== RESULT_OK){
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -7)
            val currentDate: String = simpleDateFormat.format(Date())

            var fromdate = simpleDateFormat.format(cal.time)
            var toDate = currentDate
            if (NetworkUtil.isNetworkConnected(this)) {
                Utlis.showLoading(this)
                getSurveyDetailsListViewModel.getSurveyListApi(this, fromdate, toDate, Preferences.getValidatedEmpId());

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

    override fun onClickFilterIcon() {
        val uploadStatusFilterDialog = this?.let { Dialog(it) }
        val dialogFilterUploadBinding: DialoFilterChampsBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this), R.layout.dialo_filter_champs, null, false)
        uploadStatusFilterDialog!!.setContentView(dialogFilterUploadBinding.root)
        uploadStatusFilterDialog.getWindow()
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogFilterUploadBinding.closeDialog.setOnClickListener {
            uploadStatusFilterDialog.dismiss()
        }
        if (this.champsStatus.contains("Pending")) {
            dialogFilterUploadBinding.isPendingChecked = true
        } else {
            dialogFilterUploadBinding.isPendingChecked = false
        }
        if (this.champsStatus.contains("Completed")) {
            dialogFilterUploadBinding.isCompletedChecked = true
        } else {
            dialogFilterUploadBinding.isCompletedChecked = false
        }


        submitButtonEnable(dialogFilterUploadBinding)


        dialogFilterUploadBinding.approvedStatus.setOnCheckedChangeListener { compoundButton, b ->
            submitButtonEnable(dialogFilterUploadBinding)
        }

        dialogFilterUploadBinding.pendingStatus.setOnCheckedChangeListener { compoundButton, b ->
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
            this.champsStatus = ""
            if (dialogFilterUploadBinding.pendingStatus.isChecked) {
                this.champsStatus = "Pending"
            }
            if (dialogFilterUploadBinding.approvedStatus.isChecked) {
                if (this.champsStatus.isEmpty()) {
                    this.champsStatus = "Completed"
                } else {
                    this.champsStatus = "${this.champsStatus},Completed"
                }

            }

            if(champsStatus.contains("Pending") && champsStatus.contains("Completed")){
                activityGetSurveyDetailsBinding.noListFound.visibility= View.GONE
                activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.VISIBLE
                getSurveyDetailsAdapter =
                    GetSurveyDetailsAdapter(getSurvetDetailsModelResponses!!.storeDetails, applicationContext, this
                    )
                activityGetSurveyDetailsBinding.recyclerViewList.setLayoutManager(
                    LinearLayoutManager(this)
                )
                activityGetSurveyDetailsBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
            }
            else if(champsStatus.contains("Completed")){
                if (getSurvetDetailsModelResponses!!.storeDetails.filter { it.status.equals("COMPLETED") }.size > 0) {

                    activityGetSurveyDetailsBinding.noListFound.visibility= View.GONE
                    activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.VISIBLE
                    getSurveyDetailsAdapter =
                        GetSurveyDetailsAdapter( getSurvetDetailsModelResponses!!.storeDetails.filter { it.status.equals("COMPLETED") }, applicationContext, this
                        )
                    activityGetSurveyDetailsBinding.recyclerViewList.setLayoutManager(
                        LinearLayoutManager(this)
                    )
                    activityGetSurveyDetailsBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
                }else{
                    activityGetSurveyDetailsBinding.noListFound.visibility= View.VISIBLE
                    activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.GONE
                }
            }
            else if(champsStatus.contains("Pending")){
                if (getSurvetDetailsModelResponses!!.storeDetails.filter { it.status.equals("PENDING") }.size > 0) {
                    getSurvetDetailsModelResponses!!.storeDetails.filter { it.status.equals("PENDING") }
                    activityGetSurveyDetailsBinding.noListFound.visibility= View.GONE
                    activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.VISIBLE
                    getSurveyDetailsAdapter =
                        GetSurveyDetailsAdapter(getSurvetDetailsModelResponses!!.storeDetails.filter { it.status.equals("PENDING") }!!, applicationContext, this
                        )
                    activityGetSurveyDetailsBinding.recyclerViewList.setLayoutManager(
                        LinearLayoutManager(this)
                    )
                    activityGetSurveyDetailsBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
                }else{
                    activityGetSurveyDetailsBinding.noListFound.visibility= View.VISIBLE
                    activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.GONE
                }
            }

//            complaintListStatus.length


            if (uploadStatusFilterDialog != null && uploadStatusFilterDialog.isShowing) {
                uploadStatusFilterDialog.dismiss()

            }
        }
        uploadStatusFilterDialog.show()
    }

    fun submitButtonEnable(dialogFilterUploadBinding: DialoFilterChampsBinding) {
        if (!dialogFilterUploadBinding.approvedStatus.isChecked
            && !dialogFilterUploadBinding.pendingStatus.isChecked
        ) {
            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.apply_btn_disable_bg)
            dialogFilterUploadBinding.isSubmitEnable = false
        } else {
            dialogFilterUploadBinding.submit.setBackgroundResource(R.drawable.yellow_drawable)
            dialogFilterUploadBinding.isSubmitEnable = true
        }
    }


}