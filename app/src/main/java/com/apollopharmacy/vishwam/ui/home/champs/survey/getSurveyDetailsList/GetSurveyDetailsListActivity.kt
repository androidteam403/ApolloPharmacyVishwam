package com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.ActivityGetSurveyDetailsBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.getSurveyDetailsList.adapter.GetSurveyDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSurveyDetailsModelResponse
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import java.util.ArrayList

class GetSurveyDetailsListActivity : AppCompatActivity() , GetSurveyDetailsListCallback{
    private lateinit var activityGetSurveyDetailsBinding: ActivityGetSurveyDetailsBinding
    private lateinit var getSurveyDetailsListViewModel: GetSurveyDetailsListViewModel
    private lateinit var getSurveyDetailsAdapter: GetSurveyDetailsAdapter
    private var getStoreWiseDetails: GetStoreWiseDetailsModelResponse? = null
    var surveyRecDetailsList = ArrayList<String>()
    var surveyCCDetailsList = ArrayList<String>()
    var siteName: String? = ""
    private var storeId: String = ""
    private var address: String = ""
    private var storeCity: String = ""
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
        getStoreWiseDetails =
            intent.getSerializableExtra("getStoreWiseDetails") as GetStoreWiseDetailsModelResponse?
        surveyRecDetailsList =
            intent.getStringArrayListExtra("surveyRecDetailsList")!!
        surveyCCDetailsList = intent.getStringArrayListExtra("surveyCCDetailsList")!!
        address = intent.getStringExtra("address")!!
        storeId = intent.getStringExtra("storeId")!!
        siteName = intent.getStringExtra("siteName")
        storeCity = intent.getStringExtra("storeCity")!!
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            getSurveyDetailsListViewModel.getSurveyListApi(this, "2023-01-23", "2023-05-19", "APL48627");

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
            getSurveyDetailsAdapter =
                GetSurveyDetailsAdapter(getSurvetDetailsModelResponse, applicationContext, this
                )
            activityGetSurveyDetailsBinding.recyclerViewList.setLayoutManager(
                LinearLayoutManager(this)
            )
            activityGetSurveyDetailsBinding.recyclerViewList.setAdapter(getSurveyDetailsAdapter)
        }
        Utlis.hideLoading()

    }

    override fun onFailureSurveyList(getSurvetDetailsModelResponse: GetSurveyDetailsModelResponse) {
    Toast.makeText(applicationContext, ""+getSurvetDetailsModelResponse.message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickCardView(status: String?, champsRefernceId: String?) {
        val intent = Intent(ViswamApp.context, ChampsSurveyActivity::class.java)
        intent.putExtra("getStoreWiseDetails", getStoreWiseDetails)
        intent.putExtra("address", address)
        intent.putExtra("storeId", storeId)
        intent.putExtra("siteName", siteName)
        intent.putExtra("storeCity", storeCity)
        intent.putExtra("status", status)
        intent.putExtra("champsRefernceId", champsRefernceId)
        intent.putStringArrayListExtra("surveyRecDetailsList", surveyRecDetailsList)
        intent.putStringArrayListExtra("surveyCCDetailsList", surveyCCDetailsList)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPlusIcon() {
        val intent = Intent(ViswamApp.context, ChampsSurveyActivity::class.java)
        intent.putExtra("getStoreWiseDetails", getStoreWiseDetails)
        intent.putExtra("address", address)
        intent.putExtra("storeId", storeId)
        intent.putExtra("siteName", siteName)
        intent.putExtra("storeCity", storeCity)
        intent.putExtra("status", "new")
        intent.putStringArrayListExtra("surveyRecDetailsList", surveyRecDetailsList)
        intent.putStringArrayListExtra("surveyCCDetailsList", surveyCCDetailsList)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickback() {
       super.onBackPressed()
    }
}