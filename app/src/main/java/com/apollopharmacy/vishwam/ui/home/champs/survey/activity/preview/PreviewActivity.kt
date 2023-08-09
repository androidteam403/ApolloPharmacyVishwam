package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.ActivityChampsSurveyBinding
import com.apollopharmacy.vishwam.databinding.ActivityPreviewBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.adapter.CategoryDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.adapter.CategoryDetailsPreviewAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsViewModel
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist.SurveyListViewModel
import com.apollopharmacy.vishwam.ui.home.model.GetCategoryDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.GetSubCategoryDetailsModelResponse
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class PreviewActivity : AppCompatActivity(), PreviewActivityCallback {
    private lateinit var activityPreviewBinding: ActivityPreviewBinding
    private var getCategoryAndSubCategoryDetails: GetCategoryDetailsModelResponse? = null
    private lateinit var previewActivityViewModel: PreviewActivityViewModel
    private var categoryDetailsPreviewAdapter: CategoryDetailsPreviewAdapter? = null
    private var getSubCategoryResponses: GetSubCategoryDetailsModelResponse?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_preview)

        activityPreviewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_preview
        )

        previewActivityViewModel = ViewModelProvider(this)[PreviewActivityViewModel::class.java]
        setUp()
        checkListeners()
    }

    private fun setUp() {
        activityPreviewBinding.callback=this
        getCategoryAndSubCategoryDetails =
            intent.getSerializableExtra("getCategoryAndSubCategoryDetails") as GetCategoryDetailsModelResponse?
        activityPreviewBinding.employeeId.text= Preferences.getValidatedEmpId()
        getSubCategoryResponses = intent.getSerializableExtra("getSubCategoryResponses") as GetSubCategoryDetailsModelResponse
        val userData = LoginRepo.getProfile()
        if(userData!=null){
             activityPreviewBinding.employeeName.text = userData.EMPNAME
        }

        if (getCategoryAndSubCategoryDetails != null) {

            activityPreviewBinding.storeId.setText(getCategoryAndSubCategoryDetails?.storeIdP)
            activityPreviewBinding.address.setText(getCategoryAndSubCategoryDetails?.addressP)
            activityPreviewBinding.issuedOn.setText(getCategoryAndSubCategoryDetails?.issuedOnP)
            activityPreviewBinding.storeName.setText(getCategoryAndSubCategoryDetails?.storeNameP)
            activityPreviewBinding.storeCity.setText(getCategoryAndSubCategoryDetails?.storeCityP)
//            activityPreviewBinding.siteId.setText(getCategoryAndSubCategoryDetails?.storeIdP)
            activityPreviewBinding.state.setText(getCategoryAndSubCategoryDetails?.storeStateP)

            activityPreviewBinding.technicalCheckbox.setText(getCategoryAndSubCategoryDetails?.technicalDetails)
            activityPreviewBinding.softskillsCheckbox.setText(getCategoryAndSubCategoryDetails?.softSkills)
            activityPreviewBinding.otherTrainingCheckbox.setText(getCategoryAndSubCategoryDetails?.otherTraining)
            activityPreviewBinding.issuessTobeResolvedText.setText(getCategoryAndSubCategoryDetails?.issuesToBeResolved)

            if(!getCategoryAndSubCategoryDetails?.technicalText!!.isEmpty()){
                activityPreviewBinding.technicalCheckbox.isChecked=true
                activityPreviewBinding.enterTextTechnicalEdittext.visibility=View.VISIBLE
                activityPreviewBinding.enterTextTechnicalEdittext.setText(getCategoryAndSubCategoryDetails?.technicalText)

            }else{
                activityPreviewBinding.technicalCheckbox.isChecked=false
                activityPreviewBinding.enterTextTechnicalEdittext.visibility=View.GONE
            }
            if(!getCategoryAndSubCategoryDetails?.softSkillsText!!.isEmpty()){
                activityPreviewBinding.softskillsCheckbox.isChecked=true
                activityPreviewBinding.enterSoftSkillsEdittext.visibility=View.VISIBLE
                activityPreviewBinding.enterSoftSkillsEdittext.setText(getCategoryAndSubCategoryDetails?.softSkillsText)
            }else{
                activityPreviewBinding.softskillsCheckbox.isChecked=false
                activityPreviewBinding.enterSoftSkillsEdittext.visibility=View.GONE
            }
            if(!getCategoryAndSubCategoryDetails?.otherTrainingText!!.isEmpty()){
                activityPreviewBinding.otherTrainingCheckbox.isChecked=true
                activityPreviewBinding.enterOtherTrainingEdittext.visibility=View.VISIBLE
                activityPreviewBinding.enterOtherTrainingEdittext.setText(getCategoryAndSubCategoryDetails?.otherTrainingText)
            }else{
                activityPreviewBinding.otherTrainingCheckbox.isChecked=false
                activityPreviewBinding.enterOtherTrainingEdittext.visibility=View.GONE
            }
            if(!getCategoryAndSubCategoryDetails?.issuesToBeResolvedText!!.isEmpty()){
                activityPreviewBinding.enterIssuesTobeResolvedEdittext.setText(getCategoryAndSubCategoryDetails?.issuesToBeResolvedText)
            }else{
                activityPreviewBinding.enterIssuesTobeResolvedEdittext.setText("--")
            }
            overallProgressBarCount(getCategoryAndSubCategoryDetails!!.totalProgressP!!)

        }
//        for(i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.indices){
//            if(!getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).clickedSubmit!!){
//                getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).subCategoryDetails=
//                    getSubCategoryResponses!!.subCategoryDetails
//            }
//        }


        categoryDetailsPreviewAdapter =
            CategoryDetailsPreviewAdapter(
                getCategoryAndSubCategoryDetails!!.categoryDetails,
                applicationContext,
                this
            )
        activityPreviewBinding.categoryRecyclerView.setLayoutManager(
            LinearLayoutManager(this)
        )
        activityPreviewBinding.categoryRecyclerView.setAdapter(categoryDetailsPreviewAdapter)
    }

    override fun onClickCategory(categoryName: String, position: Int) {

    }

    override fun onClickBack() {
        onBackPressed()
    }

    override fun onClickImageView(it: View?, imageUrl: String?) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun checkListeners(){
//        activityPreviewBinding.technicalCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                activityPreviewBinding.technicalEdittext.visibility = View.VISIBLE
////                activityPreviewBinding.charLeftLayoutTechnical.visibility = View.VISIBLE
//            } else {
//                activityPreviewBinding.technicalEdittext.visibility = View.GONE
////                activityPreviewBinding.charLeftLayoutTechnical.visibility = View.GONE
//            }
//        }
//
//        activityPreviewBinding.softskillsCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                activityPreviewBinding.softSkillsEdittext.visibility = View.VISIBLE
////                activityPreviewBinding.charLeftLayoutSoftskills.visibility = View.VISIBLE
//            } else {
//                activityPreviewBinding.softSkillsEdittext.visibility = View.GONE
//                //              activityPreviewBinding.charLeftLayoutSoftskills.visibility = View.GONE
//            }
//        }
//
//        activityPreviewBinding.otherTrainingCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked) {
//                activityPreviewBinding.otherTrainingEdittext.visibility = View.VISIBLE
//                //          activityPreviewBinding.charLeftLayoutOtherrTraining.visibility = View.VISIBLE
//            } else {
//                activityPreviewBinding.otherTrainingEdittext.visibility = View.GONE
//                //        activityPreviewBinding.charLeftLayoutOtherrTraining.visibility = View.GONE
//            }
//        }
    }

    private fun overallProgressBarCount(sumOfCategories: Float) {
        if (sumOfCategories <= 100 && sumOfCategories >= 80) {
            activityPreviewBinding.progressBarTotalGreen.progress =
                sumOfCategories.roundToInt()
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    getResources().getDrawable(R.drawable.progress_bar_green)
            activityPreviewBinding.progressBarTotalGreen.visibility = View.VISIBLE
            activityPreviewBinding.progressBarTotalRed.visibility = View.GONE
            activityPreviewBinding.progressBarTotalOrange.visibility = View.GONE
            if(sumOfCategories==0.0f){
                activityPreviewBinding.percentageSum.text = "0%"
            }else{
                activityPreviewBinding.percentageSum.text = sumOfCategories.toString() + "%"
            }

        }
        else if (sumOfCategories <= 80 && sumOfCategories >= 60) {
            activityPreviewBinding.progressBarTotalOrange.progress =
                sumOfCategories.roundToInt()
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    getResources().getDrawable(R.drawable.progress_bar_orange)
            activityPreviewBinding.progressBarTotalGreen.visibility = View.GONE
            activityPreviewBinding.progressBarTotalRed.visibility = View.GONE
            activityPreviewBinding.progressBarTotalOrange.visibility = View.VISIBLE
            if(sumOfCategories==0.0f){
                activityPreviewBinding.percentageSum.text = "0%"
            }else{
                activityPreviewBinding.percentageSum.text = sumOfCategories.toString() + "%"
            }
//            activityPreviewBinding.percentageSum.text = sumOfCategories.toString() + "%"
        }
        else {
            activityPreviewBinding.progressBarTotalRed.progress =
                sumOfCategories.roundToInt()
            activityPreviewBinding.progressBarTotalGreen.visibility = View.GONE
            activityPreviewBinding.progressBarTotalRed.visibility = View.VISIBLE
            activityPreviewBinding.progressBarTotalOrange.visibility = View.GONE
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    applicationContext.getDrawable(R.drawable.progress_bar_red)
//                   getResources().getDrawable(R.drawable.progress_bar_red)
            if(sumOfCategories==0.0f){
                activityPreviewBinding.percentageSum.text = "0%"
            }else{
                activityPreviewBinding.percentageSum.text = sumOfCategories.toString() + "%"
            }
//            activityPreviewBinding.percentageSum.text = sumOfCategories.toString() + "%"
        }
    }
}