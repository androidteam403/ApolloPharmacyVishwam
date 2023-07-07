package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.adapter.GetSurveyDetailsAdapter
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
//    private var getStoreWiseDetails: GetStoreWiseDetailsModelResponse? = null
    var surveyRecDetailsList = ArrayList<String>()
    var surveyCCDetailsList = ArrayList<String>()
    var siteName: String? = ""
    private var getStoreWiseEmpidDetails: GetStoreWiseEmpIdResponse? = null
    private var storeId: String = ""
    private var address: String = ""
    private var storeCity: String = ""
    private var region:String=""
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
//        getStoreWiseDetails =
//            intent.getSerializableExtra("getStoreWiseDetails") as GetStoreWiseDetailsModelResponse?
        surveyRecDetailsList =
            intent.getStringArrayListExtra("surveyRecDetailsList")!!
        getStoreWiseEmpidDetails =
            intent.getSerializableExtra("getStoreWiseEmpidDetails") as GetStoreWiseEmpIdResponse?
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

    override fun onSuccessSurveyList(getSurvetDetailsModelResponse: GetSurveyDetailsModelResponse) {
        if(getSurvetDetailsModelResponse!=null && getSurvetDetailsModelResponse.storeDetails!=null &&
                getSurvetDetailsModelResponse.storeDetails.size>0){
            activityGetSurveyDetailsBinding.noListFound.visibility= View.GONE
            activityGetSurveyDetailsBinding.recyclerViewList.visibility=View.VISIBLE
            getSurveyDetailsAdapter =
                GetSurveyDetailsAdapter(getSurvetDetailsModelResponse, applicationContext, this
                )
            activityGetSurveyDetailsBinding.recyclerViewList.setLayoutManager(
                LinearLayoutManager(this)
            )
            activityGetSurveyDetailsBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
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
//        intent.putExtra("getStoreWiseDetails", getStoreWiseDetails)
        intent.putExtra("address", address)
        intent.putExtra("storeId", storeId)
        intent.putExtra("getStoreWiseEmpidDetails", getStoreWiseEmpidDetails)
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
//        intent.putExtra("getStoreWiseDetails", getStoreWiseDetails)
        intent.putExtra("address", address)
        intent.putExtra("storeId", storeId)
        intent.putExtra("siteName", siteName)
        intent.putExtra("getStoreWiseEmpidDetails", getStoreWiseEmpidDetails)
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
}