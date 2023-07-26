package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.ActivityChampsSurveyBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.adapter.CategoryDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.PreviewActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateRequest
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateRequest.CmsChampsSurveyQa
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.model.*
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class ChampsSurveyActivity : AppCompatActivity(), ChampsSurveyCallBack {

    private lateinit var activityChampsSurveyBinding: ActivityChampsSurveyBinding
    private var getStoreWiseEmpIdResponse: GetStoreWiseEmpIdResponse? = null
    private lateinit var champsSurveyViewModel: ChampsSurveyViewModel
    private var getTrainingAndColorDetailss: GetTrainingAndColorDetailsModelResponse? = null
    private var i = 0
    private val handler = Handler()
    private var categoryDetailsAdapter: CategoryDetailsAdapter? = null
    private var getCategoryAndSubCategoryDetails: GetCategoryDetailsModelResponse? = null
    private var getCategoryAndSubCategoryDetailsNew: GetCategoryDetailsModelResponse? = null
    private var categoryPosition: Int = 0
    private var storeId: String = ""
    private var address: String = ""
    private var getStoreWiseDetails: GetStoreWiseDetailsModelResponse? = null
    var surveyRecDetailsList = ArrayList<String>()
    private var isPending: Boolean = false
    var surveyCCDetailsList = ArrayList<String>()
    var siteName: String? = ""
    var sumOfCategoriess: Float? = 0f
    private var storeCity: String = ""
    private var region: String = ""
    private lateinit var dialog: Dialog
    private lateinit var dialogSubmit: Dialog
    private var overAllprogressBarCount = 0.0f
    private var countUp: Long? = null
    private var asText: String? = null
    private var issuedOn: String? = null
    private var status: String? = null
    private var champsRefernceId: String? = null
    private var technicalFilled = false
    private var softSkillsFilled = false
    private var otherTrainingFilled = false
    private var issuesToBeResolved = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChampsSurveyBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_champs_survey

        )
        champsSurveyViewModel = ViewModelProvider(this)[ChampsSurveyViewModel::class.java]
        setUp()
        checkListeners()
        activityChampsSurveyBinding.chrono.setOnChronometerTickListener(Chronometer.OnChronometerTickListener { arg0 ->
            countUp = (SystemClock.elapsedRealtime() - arg0.base) / 1000
            asText = (countUp!! / 60).toString() + ":" + countUp!! % 60
            //                pickupProcessBinding.timer.setText(asText);
            //                 asText1 = stopWatch.getFormat();
            //                int h = (int)(countUp /3600000);
            //                int m = (int)(countUp - h*3600000)/60000;
            //                int s= (int)(countUp - h*3600000- m*60000);
        })

        activityChampsSurveyBinding.chrono.start()
    }

    private fun setUp() {
        activityChampsSurveyBinding.callback = this
        getStoreWiseEmpIdResponse =
            intent.getSerializableExtra("getStoreWiseEmpIdResponse") as GetStoreWiseEmpIdResponse?
        getStoreWiseDetails =
            intent.getSerializableExtra("getStoreWiseDetails") as GetStoreWiseDetailsModelResponse?
        surveyRecDetailsList =
            intent.getStringArrayListExtra("surveyRecDetailsList")!!
        surveyCCDetailsList = intent.getStringArrayListExtra("surveyCCDetailsList")!!
        address = intent.getStringExtra("address")!!
        storeId = intent.getStringExtra("storeId")!!
        siteName = intent.getStringExtra("siteName")
        storeCity = intent.getStringExtra("storeCity")!!
        status = intent.getStringExtra("status")
        champsRefernceId = intent.getStringExtra("champsRefernceId")
        region = intent.getStringExtra("region")!!
        val userData = LoginRepo.getProfile()
        if (userData != null) {
            activityChampsSurveyBinding.employeeName.text = userData.EMPNAME
        }
        activityChampsSurveyBinding.employeeId.text = Preferences.getValidatedEmpId()
//        activityChampsSurveyBinding.siteId.text = storeId

        activityChampsSurveyBinding.storeName.text = siteName

        activityChampsSurveyBinding.storeId.text = storeId

        activityChampsSurveyBinding.address.text = storeId + ", " + siteName
        activityChampsSurveyBinding.storeCity.text = storeCity
        activityChampsSurveyBinding.region.text = region
        activityChampsSurveyBinding.percentageSum.text = "0"

        val currentTime: Date = Calendar.getInstance().getTime()
        activityChampsSurveyBinding.issuedOn.text = currentTime.toString()
        if (status.equals("NEW")) {
            val strDate = currentTime.toString()
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
            val date = dateFormat.parse(strDate)
            val dateNewFormat =
                SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
            activityChampsSurveyBinding.issuedOn.text = dateNewFormat
        }

//        disableSaveSubmit()

//
//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            champsSurveyViewModel.getCategoryDetailsChamps(this);
//
//        } else {
//            Toast.makeText(
//                context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }
//
//

//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            champsSurveyViewModel.getTrainingAndColorDetails(this, "TECH");
//
//        } else {
//            Toast.makeText(
//                context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }
//

//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            champsSurveyViewModel.getSurveyList(this);
//
//        } else {
//            Toast.makeText(
//                context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }


        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            champsSurveyViewModel.getCategoryDetailsChampsApi(this);

        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }




        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
            champsSurveyViewModel.getTrainingAndColorDetailsApi(this, "TECH");

        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            champsSurveyViewModel.getTrainingAndColorDetailsApi(this, "COLOUR");
//
//        } else {
//            Toast.makeText(
//                context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }


    }

    private fun retreiveSubCategoryData() {
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null) {
            for (i in getCategoryAndSubCategoryDetails?.categoryDetails?.indices!!) {
                champsSurveyViewModel.getSubCategoryDetailsChampsApi(
                    this,
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).categoryName!!
                )
            }
        }

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkListeners() {

//        activityChampsSurveyBinding.enterTextTechnicalEdittext.filters = arrayOf(InputFilter.LengthFilter(getTrainingAndColorDetailss!!.trainingDetails.get(0).length.toInt()))
        activityChampsSurveyBinding.enterTextTechnicalEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (getTrainingAndColorDetailss != null) {
                    var charLeft: String =
                        ((getTrainingAndColorDetailss!!.trainingDetails.get(0).length).toInt() - charSequence.length).toString()
                    activityChampsSurveyBinding.charLeftTechnical.setText(charLeft)
                }

                if (activityChampsSurveyBinding.enterTextTechnicalEdittext.text!!.isEmpty()) {
                    technicalFilled = false
                } else {
                    technicalFilled = true
                }
//                disableSaveSubmit()
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterSoftSkillsEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (getTrainingAndColorDetailss != null) {
                    var charLeft: String =
                        ((getTrainingAndColorDetailss!!.trainingDetails.get(1).length).toInt() - charSequence.length).toString()
                    activityChampsSurveyBinding.charLeftSoftskills.setText(charLeft)

                }
                if (activityChampsSurveyBinding.enterSoftSkillsEdittext.text!!.isEmpty()) {
                    softSkillsFilled = false
                } else {
                    softSkillsFilled = true
                }
//                disableSaveSubmit()
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterOtherTrainingEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (getTrainingAndColorDetailss != null) {
                    var charLeft: String =
                        ((getTrainingAndColorDetailss!!.trainingDetails.get(2).length).toInt() - charSequence.length).toString()
                    activityChampsSurveyBinding.charLeftOtherTraining.setText(charLeft)

                }
                if (activityChampsSurveyBinding.enterOtherTrainingEdittext.text!!.isEmpty()) {
                    otherTrainingFilled = false
                } else {
                    otherTrainingFilled = true
                }
//                disableSaveSubmit()
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (getTrainingAndColorDetailss != null) {
                    var charLeft: String =
                        ((getTrainingAndColorDetailss!!.trainingDetails.get(3).length).toInt() - charSequence.length).toString()
                    activityChampsSurveyBinding.charLeftIssuestoBeResolved.setText(charLeft)

                }
                if (activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.text!!.isEmpty()) {
                    issuesToBeResolved = false
                } else {
                    issuesToBeResolved = true
                }
//                disableSaveSubmit()
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.technicalCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityChampsSurveyBinding.technicalEdittext.visibility = View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutTechnical.visibility = View.VISIBLE
            } else {
                activityChampsSurveyBinding.technicalEdittext.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutTechnical.visibility = View.GONE
            }
        }

        activityChampsSurveyBinding.softskillsCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityChampsSurveyBinding.softSkillsEdittext.visibility = View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutSoftskills.visibility = View.VISIBLE
            } else {
                activityChampsSurveyBinding.softSkillsEdittext.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutSoftskills.visibility = View.GONE
            }
        }

        activityChampsSurveyBinding.otherTrainingCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityChampsSurveyBinding.otherTrainingEdittext.visibility = View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutOtherrTraining.visibility = View.VISIBLE
            } else {
                activityChampsSurveyBinding.otherTrainingEdittext.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutOtherrTraining.visibility = View.GONE
            }
        }

//        activityChampsSurveyBinding.progressBar.setOnClickListener {
//            // Before clicking the button the progress bar will invisible
//            // so we have to change the visibility of the progress bar to visible
//            // setting the progressbar visibility to visible
//            progressBar.visibility = View.VISIBLE
//            getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//            );
//            i = progressBar.progress
//
//            Thread(Runnable {
//                // this loop will run until the value of i becomes 99
//                if (i <= 5) {
////                    i += 1
//                    activityChampsSurveyBinding.progressBar.progressDrawable =
//                        getResources().getDrawable(R.drawable.progress_drawable_green)
//                    // Update the progress bar and display the current value
//                    handler.post(Runnable {
//                        progressBar.progress = i
//                        // setting current progress to the textview
////                        txtView!!.text = i.toString() + "/" + progressBar.max
//                    })
//                    try {
//                        Thread.sleep(100)
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//                    }
//                } else if (i > 5 && i <= 10) {
//                    activityChampsSurveyBinding.progressBar.progressDrawable =
//                        getResources().getDrawable(R.drawable.progress_bar_orange)
//                } else if (i > 10 && i == progressBar.max) {
//                    activityChampsSurveyBinding.progressBar.progressDrawable =
//                        getResources().getDrawable(R.drawable.progress_drawable_green)
//                }
//
//                // setting the visibility of the progressbar to invisible
//                // or you can use View.GONE instead of invisible
//                // View.GONE will remove the progressbar
////                progressBar.visibility = View.INVISIBLE
//
//            }).start()
//        }
    }

    private fun disableSaveSubmit() {
        if (technicalFilled && softSkillsFilled && otherTrainingFilled) {
            activityChampsSurveyBinding.submitButton.background =
                context.getDrawable(R.drawable.background_for_champs_green)
            activityChampsSurveyBinding.saveDraft.background =
                context.getDrawable(R.drawable.background_for_champs_green)
        } else {
            activityChampsSurveyBinding.submitButton.background =
                context.getDrawable(R.drawable.grey_background_champs)
            activityChampsSurveyBinding.saveDraft.background =
                context.getDrawable(R.drawable.grey_background_champs)
        }
    }

    override fun onClickBack() {
        super.onBackPressed()
    }

    override fun onClickCategory(categoryName: String, position: Int) {
        getCategoryAndSubCategoryDetails?.storeIdP =
            activityChampsSurveyBinding.storeId.text.toString()
        getCategoryAndSubCategoryDetails?.addressP =
            activityChampsSurveyBinding.address.text.toString()
        getCategoryAndSubCategoryDetails?.issuedOnP =
            activityChampsSurveyBinding.issuedOn.text.toString()
        getCategoryAndSubCategoryDetails?.storeNameP =
            activityChampsSurveyBinding.storeName.text.toString()
        getCategoryAndSubCategoryDetails?.storeCityP =
            activityChampsSurveyBinding.storeCity.text.toString()
        val intent = Intent(context, ChampsDetailsandRatingBarActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("categoryName", categoryName)
        intent.putExtra("getCategoryAndSubCategoryDetails", getCategoryAndSubCategoryDetails)
        intent.putExtra("position", position)
        intent.putExtra("isPending", isPending)
        intent.putExtra("status", status)
        startActivityForResult(intent, 111)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickSubmit() {
//     ChampsSurveyDialog().show(supportFragmentManager, "")
        if (technicalFilled && softSkillsFilled && otherTrainingFilled) {
            saveApiRequest("submit")
        } else {
            Toast.makeText(applicationContext, "Please fill all the details", Toast.LENGTH_SHORT)
                .show()
        }


    }

    private fun saveApiRequest(type: String) {
        if (NetworkUtil.isNetworkConnected(context)) {
            Utlis.showLoading(this)
            var submit = SaveSurveyModelRequest()
            var headerDetails = SaveSurveyModelRequest.HeaderDetails()
            headerDetails.state = ""
            headerDetails.city = activityChampsSurveyBinding.storeCity.text.toString()
            headerDetails.storeId = activityChampsSurveyBinding.storeId.text.toString()

            if (!status.equals("COMPLETED")) {
                val strDate = activityChampsSurveyBinding.issuedOn.text.toString()
                val dateFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a");
                val date = dateFormat.parse(strDate)
//            023-01-23 17:32:16
                val dateNewFormat =
                    SimpleDateFormat("dd-MM-yy kk:mm:ss").format(date)
                headerDetails.dateOfVisit = dateNewFormat
            } else if (status.equals("PENDING")) {
                val strDate = activityChampsSurveyBinding.issuedOn.text.toString()
                val dateFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a");
                val date = dateFormat.parse(strDate)
//            023-01-23 17:32:16
                val dateNewFormat =
                    SimpleDateFormat("dd-MM-yy kk:mm:ss").format(date)
                headerDetails.dateOfVisit = dateNewFormat
            } else {
                val strDate = activityChampsSurveyBinding.issuedOn.text.toString()
                val dateFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a");
                val date = dateFormat.parse(strDate)
//            023-01-23 17:32:16
                val dateNewFormat =
                    SimpleDateFormat("dd-MM-yy kk:mm:ss").format(date)
                headerDetails.dateOfVisit = dateNewFormat
            }

            if (getStoreWiseEmpIdResponse != null &&
                getStoreWiseEmpIdResponse?.storeWiseDetails != null &&
                getStoreWiseEmpIdResponse?.storeWiseDetails?.trainerEmail != null
            ) {
                headerDetails.emailIdOfTrainer =
                    getStoreWiseEmpIdResponse?.storeWiseDetails?.trainerEmail
            } else {
                headerDetails.emailIdOfTrainer = ""
            }
            if (getStoreWiseDetails != null && getStoreWiseDetails!!.data != null &&
                getStoreWiseDetails!!.data.executive != null
            ) {
                headerDetails.emailIdOfExecutive =
                    getStoreWiseDetails!!.data.executive.email
            } else {
                headerDetails.emailIdOfExecutive = ""
            }
            if (getStoreWiseDetails != null && getStoreWiseDetails!!.data != null &&
                getStoreWiseDetails!!.data.manager != null
            ) {
                headerDetails.emailIdOfManager =
                    getStoreWiseDetails!!.data.manager.email
            } else {
                headerDetails.emailIdOfManager = ""
            }

            if (getStoreWiseDetails != null && getStoreWiseDetails!!.data != null &&
                getStoreWiseDetails!!.data.regionHead != null
            ) {
                headerDetails.emailIdOfRegionalHead =
                    getStoreWiseDetails!!.data.regionHead.email
            } else {
                headerDetails.emailIdOfRegionalHead = ""
            }

            if (surveyRecDetailsList.get(0) != null) {
                headerDetails.emailIdOfRecipients = surveyRecDetailsList.get(0)
            } else {
                headerDetails.emailIdOfRecipients = ""
            }

            if (surveyCCDetailsList.get(0) != null) {
                headerDetails.emailIdOfCc = surveyCCDetailsList.get(0)
            } else {
                headerDetails.emailIdOfCc = ""
            }


            headerDetails.champAutoId = champsRefernceId

            headerDetails.techinalDetails =
                activityChampsSurveyBinding.enterTextTechnicalEdittext.text.toString()
            headerDetails.softSkills =
                activityChampsSurveyBinding.enterSoftSkillsEdittext.text.toString()
            headerDetails.otherTraining =
                activityChampsSurveyBinding.enterOtherTrainingEdittext.text.toString()
            headerDetails.issuesToBeResolved =
                activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.text.toString()
            headerDetails.total = activityChampsSurveyBinding.percentageSum.text.toString()
            headerDetails.createdBy = Preferences.getValidatedEmpId()
            if (type.equals("submit")) {
                headerDetails.status = "1"
            } else {
                headerDetails.status = "0"
            }

            submit.headerDetails = headerDetails

            var categoryDetails = SaveSurveyModelRequest.CategoryDetails()

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                    0
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        0
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.appearanceStore =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(0)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        0
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.offerDisplay =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(0)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        0
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.storeFrontage =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(0)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        0
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.groomingStaff =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(0)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        0
                    )?.imageDataLists != null
                ) {
                    var commaSeparatorUrls = ""
                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(0)?.imageDataLists!!.indices) {
                        var indiviualUrl =
                            getCategoryAndSubCategoryDetails!!.categoryDetails?.get(0)?.imageDataLists!!.get(
                                i
                            ).imageUrl
                        if (commaSeparatorUrls != "") {
                            commaSeparatorUrls = indiviualUrl + "," + commaSeparatorUrls
                        } else {
                            commaSeparatorUrls = indiviualUrl!!
                        }

                    }
                    categoryDetails.cleanlinessImages = commaSeparatorUrls

                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                    1
                )?.subCategoryDetails != null
            ) {


                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        1
                    )?.imageDataLists != null
                ) {
                    var commaSeparatorUrls = ""
                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(1)?.imageDataLists!!.indices) {
                        var indiviualUrl =
                            getCategoryAndSubCategoryDetails!!.categoryDetails?.get(1)?.imageDataLists!!.get(
                                i
                            ).imageUrl
                        if (commaSeparatorUrls != "") {
                            commaSeparatorUrls = indiviualUrl + "," + commaSeparatorUrls
                        } else {
                            commaSeparatorUrls = indiviualUrl!!
                        }
                    }
                    categoryDetails.hospitalityImages = commaSeparatorUrls

                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        1
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.greetingCustomers =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(1)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        1
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.customerEngagement =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(1)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        1
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.customerHandling =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(1)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        1
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.reminderCalls =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(1)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                    2
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        2
                    )?.imageDataLists != null
                ) {
                    var commaSeparatorUrls = ""
                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.imageDataLists!!.indices) {
                        var indiviualUrl =
                            getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.imageDataLists!!.get(
                                i
                            ).imageUrl
                        if (commaSeparatorUrls != "") {
                            commaSeparatorUrls = indiviualUrl + "," + commaSeparatorUrls
                        } else {
                            commaSeparatorUrls = indiviualUrl!!
                        }
                    }
                    categoryDetails.accuracyImages = commaSeparatorUrls

                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.billingSkusDispensed =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.interpretationRecheckPrescription =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.bankDeposits =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.expiryFifoPolicy =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(4).givenRating != null
                ) {
                    categoryDetails.rsCheckInternalAuditing =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.subCategoryDetails!!.get(
                            4
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(5).givenRating != null
                ) {
                    categoryDetails.oneApolloDrConnect =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.subCategoryDetails!!.get(
                            5
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(6).givenRating != null
                ) {
                    categoryDetails.cashCheckingEvery2Hours =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.subCategoryDetails!!.get(
                            6
                        ).givenRating.toString()
                }


//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                    3
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.imageDataLists != null
                ) {
                    var commaSeparatorUrls = ""
                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.imageDataLists!!.indices) {
                        var indiviualUrl =
                            getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.imageDataLists!!.get(
                                i
                            ).imageUrl
                        if (commaSeparatorUrls != "") {
                            commaSeparatorUrls = indiviualUrl + "," + commaSeparatorUrls
                        } else {
                            commaSeparatorUrls = indiviualUrl!!
                        }
                    }
                    categoryDetails.maintenanceImages = commaSeparatorUrls

                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                            4
                        )?.imageDataLists != null
                    ) {
                        var commaSeparatorUrls = ""
                        for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.imageDataLists!!.indices) {
                            var indiviualUrl =
                                getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.imageDataLists!!.get(
                                    i
                                ).imageUrl
                            if (commaSeparatorUrls != "") {
                                commaSeparatorUrls = indiviualUrl + "," + commaSeparatorUrls
                            } else {
                                commaSeparatorUrls = indiviualUrl!!
                            }
                        }
                        categoryDetails.productsImages = commaSeparatorUrls

                    }
                    categoryDetails.stockArrangementRefrigerator =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.acWorkingCondition =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.lighting =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.planogram =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(4).givenRating != null
                ) {
                    categoryDetails.licensesRenewal =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            4
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(5).givenRating != null
                ) {
                    categoryDetails.biometric =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            5
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(6).givenRating != null
                ) {
                    categoryDetails.maintenanceHdRegister =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            6
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(7).givenRating != null
                ) {
                    categoryDetails.dutyRostersAllotment =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            7
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(8).givenRating != null
                ) {
                    categoryDetails.internet =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            8
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(9).givenRating != null
                ) {
                    categoryDetails.swipingMachineWorking =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            9
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(10).givenRating != null
                ) {
                    categoryDetails.theCcCamerasWorking =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            10
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(11).givenRating != null
                ) {
                    categoryDetails.printersWorkingCondition =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.subCategoryDetails!!.get(
                            11
                        ).givenRating!!.toString()
                }


//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.size!! > 4 && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                    4
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        4
                    )?.imageDataLists != null
                ) {
                    var commaSeparatorUrls = ""
                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.imageDataLists!!.indices) {
                        var indiviualUrl =
                            getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.imageDataLists!!.get(
                                i
                            ).imageUrl
                        if (commaSeparatorUrls != "") {
                            commaSeparatorUrls = indiviualUrl + "," + commaSeparatorUrls
                        } else {
                            commaSeparatorUrls = indiviualUrl!!
                        }
                    }
                    categoryDetails.productsImages = commaSeparatorUrls

                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.availabilityStockGood =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.substitutionOfferedRegularly =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.serviceRecoveryDone90 =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.bounceTracking =
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails!!.size > 5 && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                    5
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        5
                    )?.imageDataLists != null
                ) {
                    var commaSeparatorUrls = ""
                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(5)?.imageDataLists!!.indices) {
                        var indiviualUrl =
                            getCategoryAndSubCategoryDetails!!.categoryDetails?.get(5)?.imageDataLists!!.get(
                                i
                            ).imageUrl
                        if (commaSeparatorUrls != "") {
                            commaSeparatorUrls = indiviualUrl + "," + commaSeparatorUrls
                        } else {
                            commaSeparatorUrls = indiviualUrl!!
                        }
                    }
                    categoryDetails.speedServiceSalesPromotionImages = commaSeparatorUrls

                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        5
                    )?.subCategoryDetails!!.get(0).givenRating != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        5
                    )?.subCategoryDetails!!.size > 0
                ) {
                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(5)?.subCategoryDetails!!) {
                        if (i.subCategoryName.equals("Speed of service  - 5 to 10 minutes")) {
                            categoryDetails.speedService5To10Minutes =
                                i.givenRating.toString()
                            break
                        }
                    }

                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        5
                    )?.subCategoryDetails!!.size > 1 && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        5
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(5)?.subCategoryDetails!!) {
                        if (i.subCategoryName.equals("Home Delivery - commitment fulfilled on time")) {
                            categoryDetails.homeDeliveryCommitmentFulfilledTime =
                                getCategoryAndSubCategoryDetails!!.categoryDetails?.get(5)?.subCategoryDetails!!.get(
                                    1
                                ).givenRating.toString()
                            break
                        }
                    }

                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        5
                    )?.subCategoryDetails!!.size > 2 && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
                        5
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {

                    for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.get(5)?.subCategoryDetails!!) {
                        if (i.subCategoryName.equals("Sales Promotion")) {
                            categoryDetails.salesPromotion =
                                getCategoryAndSubCategoryDetails!!.categoryDetails?.get(5)?.subCategoryDetails!!.get(
                                    2
                                ).givenRating.toString()
                            break
                        }
                    }


                }



                submit.categoryDetails = categoryDetails


//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }
            Gson().toJson(submit)

            champsSurveyViewModel.getSaveDetailsApi(submit, this, type)

        }
    }

    override fun onClickSaveDraft() {
//        if(technicalFilled && softSkillsFilled && otherTrainingFilled){
        dialog = Dialog(this)
        dialog.setContentView(R.layout.savedraft_dialogue)
        val close = dialog.findViewById<TextView>(R.id.no_btnSiteChange)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnSiteChange)
        ok.setOnClickListener {
            dialog.dismiss()
            saveApiRequest("saveDraft")
//            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            dialog.show()
//            val intent = Intent()
//            setResult(Activity.RESULT_OK, intent)
//            finish()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
//        }else{
//            Toast.makeText(applicationContext, "Please fill all the details", Toast.LENGTH_SHORT).show()
//        }
//        saveApiRequest("saveDraft")
//        val intent = Intent(context, SurveyListActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

    }

    override fun onClickPreview() {
        getCategoryAndSubCategoryDetails?.storeIdP =
            activityChampsSurveyBinding.storeId.text.toString()
        getCategoryAndSubCategoryDetails?.addressP =
            activityChampsSurveyBinding.address.text.toString()
        getCategoryAndSubCategoryDetails?.issuedOnP =
            activityChampsSurveyBinding.issuedOn.text.toString()
        getCategoryAndSubCategoryDetails?.storeNameP =
            activityChampsSurveyBinding.storeName.text.toString()
        getCategoryAndSubCategoryDetails?.storeCityP =
            activityChampsSurveyBinding.storeCity.text.toString()
        getCategoryAndSubCategoryDetails?.storeStateP =
            activityChampsSurveyBinding.region.text.toString()
//        getCategoryAndSubCategoryDetails?.storeStateP=activityChampsSurveyBinding.State.text.toString()
        if (getTrainingAndColorDetailss != null && getTrainingAndColorDetailss!!.trainingDetails != null && getTrainingAndColorDetailss!!.trainingDetails.size != null) {
            getCategoryAndSubCategoryDetails?.technicalDetails =
                getTrainingAndColorDetailss!!.trainingDetails.get(0).name
            getCategoryAndSubCategoryDetails?.softSkills =
                getTrainingAndColorDetailss!!.trainingDetails.get(1).name
            getCategoryAndSubCategoryDetails?.otherTraining =
                getTrainingAndColorDetailss!!.trainingDetails.get(2).name
            getCategoryAndSubCategoryDetails?.issuesToBeResolved =
                getTrainingAndColorDetailss!!.trainingDetails.get(3).name
        }
        getCategoryAndSubCategoryDetails?.technicalText =
            activityChampsSurveyBinding.enterTextTechnicalEdittext.text.toString()
        getCategoryAndSubCategoryDetails?.softSkillsText =
            activityChampsSurveyBinding.enterSoftSkillsEdittext.text.toString()
        getCategoryAndSubCategoryDetails?.otherTrainingText =
            activityChampsSurveyBinding.enterOtherTrainingEdittext.text.toString()
        getCategoryAndSubCategoryDetails?.issuesToBeResolvedText =
            activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.text.toString()
        getCategoryAndSubCategoryDetails!!.totalProgressP = sumOfCategoriess

        val intent = Intent(context, PreviewActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("getCategoryAndSubCategoryDetails", getCategoryAndSubCategoryDetails)
        intent.putExtra("getSubCategoryResponses", getSubCategoryResponses)

        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }


    override fun onSuccessgetCategoryDetails(getCategoryDetails: GetCategoryDetailsModelResponse) {

        if (getCategoryDetails != null && getCategoryDetails.categoryDetails != null) {
            getCategoryAndSubCategoryDetails = getCategoryDetails

            if (NetworkUtil.isNetworkConnected(this)) {
//                Utlis.showLoading(this)`
//                champsSurveyViewModel.getSubCategoryDetailsChamps(this, "Cleanliness");
                for (k in getCategoryDetails.categoryDetails!!.indices) {
                    champsSurveyViewModel.getSubCategoryDetailsChampsApi(
                        this,
                        getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(k).categoryName!!
                    )
                }


            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }


        } else {
            Toast.makeText(applicationContext, "Please try again", Toast.LENGTH_SHORT).show()
        }

        if (status.equals("PENDING") || status.equals("COMPLETED")) {
            if (status.equals("PENDING")) {
                activityChampsSurveyBinding.warningLayout.visibility = View.VISIBLE
                activityChampsSurveyBinding.saveSaveDraft.visibility = View.VISIBLE
                activityChampsSurveyBinding.preview.visibility = View.VISIBLE
            } else {
                activityChampsSurveyBinding.warningLayout.visibility = View.GONE
                activityChampsSurveyBinding.saveSaveDraft.visibility = View.GONE
                activityChampsSurveyBinding.preview.visibility = View.GONE
                activityChampsSurveyBinding.technicalCheckbox.isChecked = true
                activityChampsSurveyBinding.technicalCheckbox.isEnabled = false
                activityChampsSurveyBinding.softskillsCheckbox.isChecked = true
                activityChampsSurveyBinding.softskillsCheckbox.isEnabled = false
                activityChampsSurveyBinding.otherTrainingCheckbox.isChecked = true
                activityChampsSurveyBinding.otherTrainingCheckbox.isEnabled = false
                activityChampsSurveyBinding.technicalEdittext.visibility = View.GONE
                activityChampsSurveyBinding.softSkillsEdittext.visibility = View.GONE
                activityChampsSurveyBinding.otherTrainingEdittext.visibility = View.GONE
                activityChampsSurveyBinding.issuesTobeResolvedEdittext.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutTechnical.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutSoftskills.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutOtherrTraining.visibility = View.GONE
                activityChampsSurveyBinding.charIssuesLeftBox.visibility = View.GONE
                activityChampsSurveyBinding.issuesTobeResolvedTextView.visibility = View.VISIBLE
                activityChampsSurveyBinding.technicalTextView.visibility = View.VISIBLE
                activityChampsSurveyBinding.softskillsTextView.visibility = View.VISIBLE
                activityChampsSurveyBinding.othertrainingTextview.visibility = View.VISIBLE
            }

        } else {

        }


    }

    override fun onFailuregetCategoryDetails(value: GetCategoryDetailsModelResponse) {
        if (value != null && value.message != null) {
            Toast.makeText(applicationContext, value.message, Toast.LENGTH_SHORT).show()
        }
        Utlis.hideLoading()
    }

    override fun onSuccessgetTrainingDetails(getTrainingAndColorDetails: GetTrainingAndColorDetailsModelResponse) {
        if (getTrainingAndColorDetails != null && getTrainingAndColorDetails.trainingDetails != null && getTrainingAndColorDetails.trainingDetails.size != null) {
            getTrainingAndColorDetailss = getTrainingAndColorDetails;
            activityChampsSurveyBinding.enterTextTechnicalEdittext.filters =
                arrayOf(InputFilter.LengthFilter(getTrainingAndColorDetails.trainingDetails.get(0).length.toInt()))
            activityChampsSurveyBinding.enterSoftSkillsEdittext.filters =
                arrayOf(InputFilter.LengthFilter(getTrainingAndColorDetails.trainingDetails.get(1).length.toInt()))
            activityChampsSurveyBinding.enterOtherTrainingEdittext.filters =
                arrayOf(InputFilter.LengthFilter(getTrainingAndColorDetails.trainingDetails.get(2).length.toInt()))
            activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.filters =
                arrayOf(InputFilter.LengthFilter(getTrainingAndColorDetails.trainingDetails.get(3).length.toInt()))


            activityChampsSurveyBinding.technicalCheckbox.text =
                getTrainingAndColorDetails.trainingDetails.get(0).name
            activityChampsSurveyBinding.charLeftTechnical.text =
                getTrainingAndColorDetails.trainingDetails.get(0).length

            activityChampsSurveyBinding.softskillsCheckbox.text =
                getTrainingAndColorDetails.trainingDetails.get(1).name
            activityChampsSurveyBinding.charLeftSoftskills.text =
                getTrainingAndColorDetails.trainingDetails.get(1).length


            activityChampsSurveyBinding.otherTrainingCheckbox.text =
                getTrainingAndColorDetails.trainingDetails.get(2).name
            activityChampsSurveyBinding.charLeftOtherTraining.text =
                getTrainingAndColorDetails.trainingDetails.get(2).length



            activityChampsSurveyBinding.issuessTobeResolvedText.text =
                getTrainingAndColorDetails.trainingDetails.get(3).name
            activityChampsSurveyBinding.charLeftIssuestoBeResolved.text =
                getTrainingAndColorDetails.trainingDetails.get(3).length

        }
        Utlis.hideLoading()

    }

    override fun onFailuregetTrainingDetails(value: GetTrainingAndColorDetailsModelResponse) {
        if (value != null && value.message != null) {
            Toast.makeText(applicationContext, "" + value.message, Toast.LENGTH_SHORT).show()
        }
        Utlis.hideLoading()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onSuccessgetColorDetails(getTrainingAndColorDetails: GetTrainingAndColorDetailsModelResponse) {
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null) {
            var sumOfCategories: Float = 0f
            for (i in getCategoryAndSubCategoryDetails!!.categoryDetails?.indices!!) {
                if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).sumOfSubCategoryRating != null) {
                    var indiviualCount: Float =
                        (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).sumOfSubCategoryRating)!!.toFloat()
                    sumOfCategories = indiviualCount + sumOfCategories
                    overallProgressBarCount(sumOfCategories)
                }


            }
            sumOfCategoriess = sumOfCategories
        }

        Utlis.hideLoading()

    }

    private fun overallProgressBarCount(sumOfCategories: Float) {
        overAllprogressBarCount = sumOfCategories
        if (sumOfCategories <= 100 && sumOfCategories >= 80) {
            activityChampsSurveyBinding.progressBarTotalGreen.progress =
                sumOfCategories.roundToInt()
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    getResources().getDrawable(R.drawable.progress_bar_green)
            activityChampsSurveyBinding.progressBarTotalGreen.visibility = View.VISIBLE
            activityChampsSurveyBinding.progressBarTotalRed.visibility = View.GONE
            activityChampsSurveyBinding.progressBarTotalOrange.visibility = View.GONE
            activityChampsSurveyBinding.percentageSum.text = sumOfCategories.toString()
        } else if (sumOfCategories <= 80 && sumOfCategories >= 60) {
            activityChampsSurveyBinding.progressBarTotalOrange.progress =
                sumOfCategories.roundToInt()
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    getResources().getDrawable(R.drawable.progress_bar_orange)
            activityChampsSurveyBinding.progressBarTotalGreen.visibility = View.GONE
            activityChampsSurveyBinding.progressBarTotalRed.visibility = View.GONE
            activityChampsSurveyBinding.progressBarTotalOrange.visibility = View.VISIBLE
            activityChampsSurveyBinding.percentageSum.text = sumOfCategories.toString()
        } else {
            activityChampsSurveyBinding.progressBarTotalRed.progress =
                sumOfCategories.roundToInt()
            activityChampsSurveyBinding.progressBarTotalGreen.visibility = View.GONE
            activityChampsSurveyBinding.progressBarTotalRed.visibility = View.VISIBLE
            activityChampsSurveyBinding.progressBarTotalOrange.visibility = View.GONE
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    applicationContext.getDrawable(R.drawable.progress_bar_red)
//                   getResources().getDrawable(R.drawable.progress_bar_red)
            activityChampsSurveyBinding.percentageSum.text = sumOfCategories.toString()
        }
    }

    override fun onSuccessSaveDetailsApi(
        saveSurveyResponse: SaveSurveyModelResponse,
        type: String,
    ) {
        Utlis.hideLoading()

        dialogSubmit = Dialog(this)
        dialogSubmit.setContentView(R.layout.dialog_champs_survey)
        val close = dialogSubmit.findViewById<ImageView>(R.id.close_dialog_save_)
//        val siteId = dialogSubmit.findViewById<TextView>(R.id.site_id_save_)
        val address = dialogSubmit.findViewById<TextView>(R.id.address_save_)
        val startTime = dialogSubmit.findViewById<TextView>(R.id.started_survey_on_save_)
        val endTime = dialogSubmit.findViewById<TextView>(R.id.survey_ended_on_save_)
        val progressBar = dialogSubmit.findViewById<ProgressBar>(R.id.seekbar1_save_)
        val timeTaken = dialogSubmit.findViewById<TextView>(R.id.total_time_taken_save_)
        val champsId = dialogSubmit.findViewById<TextView>(R.id.champs_id)
        val message = dialogSubmit.findViewById<TextView>(R.id.successfull_text)
        if (type.equals("saveDraft")) {
            message.setText("Draft Saved Successfully")
        } else {
            message.setText("Successful")
        }
        val percentageSum =
            dialogSubmit.findViewById<TextView>(R.id.progress_percentage_display_save_)
//        siteId.setText(activityChampsSurveyBinding.siteId.text.toString())
        address.setText(activityChampsSurveyBinding.address.text.toString())
        startTime.setText(activityChampsSurveyBinding.issuedOn.text.toString())
        if (saveSurveyResponse.champReferenceId != null) {
            champsId.text = saveSurveyResponse.champReferenceId
        } else {
            champsId.text = champsRefernceId
        }


//        timeTaken.text = asText
        progressBar.progress = overAllprogressBarCount.roundToInt()
        if (overAllprogressBarCount <= 100 && overAllprogressBarCount >= 80) {
            activityChampsSurveyBinding.progressBarTotalGreen.progress =
                overAllprogressBarCount.roundToInt()
            progressBar.progressDrawable =
                resources.getDrawable(R.drawable.bordered_seekbar_progress_style_green)
            percentageSum.text = overAllprogressBarCount.toString()
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    getResources().getDrawable(R.drawable.progress_bar_green)
//            activityChampsSurveyBinding.progressBarTotalGreen.visibility = View.VISIBLE
//            activityChampsSurveyBinding.progressBarTotalRed.visibility = View.GONE
//            activityChampsSurveyBinding.progressBarTotalOrange.visibility = View.GONE

        } else if (overAllprogressBarCount <= 80 && overAllprogressBarCount >= 60) {
            activityChampsSurveyBinding.progressBarTotalGreen.progress =
                overAllprogressBarCount.roundToInt()
            progressBar.progressDrawable =
                resources.getDrawable(R.drawable.bordered_seekbar_progress_style)
            percentageSum.text = overAllprogressBarCount.toString()
        } else {
            activityChampsSurveyBinding.progressBarTotalGreen.progress =
                overAllprogressBarCount.roundToInt()
            progressBar.progressDrawable =
                resources.getDrawable(R.drawable.bordered_seekbar_progress_style_red)
            percentageSum.text = overAllprogressBarCount.toString()
        }
        val currentTime: Date = Calendar.getInstance().getTime()
        val strDate = currentTime.toString()
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        val endDate = dateFormat.parse(strDate)
        val dateNewFormat =
            SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(endDate)
        endTime.setText(dateNewFormat)

        val dtStart = activityChampsSurveyBinding.issuedOn.text.toString()
        val format = SimpleDateFormat("dd MMM, yyyy - hh:mm a")
//        try {
        val startDate = format.parse(dtStart)
//            System.out.println(date)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }

//        val startDate = dateFormat.parse(activityChampsSurveyBinding.issuedOn.text.toString())
        var different = endDate!!.time - startDate!!.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val elapsedDays = different / daysInMilli
        different = different % daysInMilli
        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        System.out.printf(
            "%d days, %d hours, %d minutes, %d seconds%n",
            elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds
        );
        if (elapsedDays > 0) {
            timeTaken.text =
                "$elapsedDays days, $elapsedHours hours, $elapsedMinutes minutes, $elapsedSeconds seconds"
        } else if (elapsedHours > 0) {
            timeTaken.text = "$elapsedHours hours, $elapsedMinutes minutes, $elapsedSeconds seconds"

        } else if (elapsedMinutes > 0) {
            timeTaken.text = "$elapsedMinutes minutes, $elapsedSeconds seconds"
        } else {
            timeTaken.text = "$elapsedSeconds seconds"
        }

        if (!type.equals("saveDraft")) {


            var saveUpdateRequest = SaveUpdateRequest()
            val strDates = activityChampsSurveyBinding.issuedOn.text.toString()
            val dateFormats = SimpleDateFormat("dd MMM, yyyy - hh:mm a");
            val date = dateFormats.parse(strDates)
//            023-01-23 17:32:16
            val dateNewFormats =
                SimpleDateFormat("yyyy-MM-dd").format(date)

            saveUpdateRequest.date = dateNewFormats
            saveUpdateRequest.issue =
                activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.text.toString()
            if (surveyRecDetailsList.get(0) != null) {
                saveUpdateRequest.email = surveyRecDetailsList.get(0)
            } else {
                saveUpdateRequest.email = ""
            }

            if (getStoreWiseEmpIdResponse != null &&
                getStoreWiseEmpIdResponse?.storeWiseDetails != null &&
                getStoreWiseEmpIdResponse?.storeWiseDetails?.trainerEmail != null
            ) {
                saveUpdateRequest.trainerEmail =
                    getStoreWiseEmpIdResponse?.storeWiseDetails?.trainerEmail
            } else {
                saveUpdateRequest.trainerEmail = ""
            }


            saveUpdateRequest.trainerId = Preferences.getValidatedEmpId()
            val userData = LoginRepo.getProfile()
            if (userData != null) {
                saveUpdateRequest.trainerName = userData.EMPNAME
            }

            saveUpdateRequest.trimpOther =
                activityChampsSurveyBinding.enterOtherTrainingEdittext.text.toString()
            saveUpdateRequest.trimpSoftSkill =
                activityChampsSurveyBinding.enterSoftSkillsEdittext.text.toString()
//            saveUpdateRequest.trimpSoftSkills =
//                activityChampsSurveyBinding.enterSoftSkillsEdittext.text.toString()
            saveUpdateRequest.trimpTech =
                activityChampsSurveyBinding.enterTextTechnicalEdittext.text.toString()
            saveUpdateRequest.store = activityChampsSurveyBinding.storeId.text.toString()
            saveUpdateRequest.totalScore = activityChampsSurveyBinding.percentageSum.text.toString()

            var cmsChampsSurveQaList = ArrayList<CmsChampsSurveyQa>()


            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null) {
                for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!) {
                    for (j in i.subCategoryDetails!!) {
                        var cmsChampsSurveQa = CmsChampsSurveyQa()
                        cmsChampsSurveQa.categoryName = j.categoryName
                        if (j.givenRating != null
                        ) {
                            cmsChampsSurveQa.maxScore = j.rating
                            cmsChampsSurveQa.answer =
                                j.givenRating.toString()
                        } else {
                            cmsChampsSurveQa.answer = ""
                        }
                        cmsChampsSurveQa.question = j.subCategoryName + " " + (j.rating)
                        cmsChampsSurveQa.answerType = "text"
                        cmsChampsSurveQaList.add(cmsChampsSurveQa)
                    }

                    if (i.imageDataLists != null && i.imageDataLists!!.size > 0) {
                        var imagesExits: Boolean = false
                        for (k in i.imageDataLists!!) {
                            if (k.imageUrl != null && !k.imageUrl!!.isEmpty()) {
                                imagesExits = true
                                break
                            }
                        }
                        if (imagesExits) {
                            var answerImage = CmsChampsSurveyQa.AnswerImage()
                            var imagesList = ArrayList<CmsChampsSurveyQa.AnswerImage.Image>()
                            var image = answerImage.Image()
                            var cmsChampsSurveQa = CmsChampsSurveyQa()
                            cmsChampsSurveQa.categoryName = i.categoryName
                            cmsChampsSurveQa.question = "Upload Images"
                            cmsChampsSurveQa.answerType = "Images"
                            for (k in i.imageDataLists!!) {
                                if (k.imageUrl != null && !k.imageUrl!!.isEmpty()) {
                                    image.url = k.imageUrl
                                    imagesList.add(image)
                                }
                            }
                            answerImage.images = imagesList
                            cmsChampsSurveQa.answerImage = answerImage
                            cmsChampsSurveQaList.add(cmsChampsSurveQa)
                        }

                    }


                }
            }
            saveUpdateRequest.cmsChampsSurveyQa = cmsChampsSurveQaList


            saveUpdateRequest.champsId = saveSurveyResponse.champReferenceId
            champsSurveyViewModel.saveUpdateApi(this, saveUpdateRequest)
        } else {
            Utlis.hideLoading()
        }

        close.setOnClickListener {
            dialogSubmit.dismiss()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        dialogSubmit.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogSubmit.show()
    }

    override fun onFailureSaveDetailsApi(saveSurveyResponse: SaveSurveyModelResponse) {
        if (saveSurveyResponse != null && saveSurveyResponse.message != null) {
            Toast.makeText(applicationContext, "" + saveSurveyResponse.message, Toast.LENGTH_SHORT)
                .show()
        }
        Utlis.hideLoading()
    }

    override fun onSuccessSurveyList(getSurveyDetailsResponse: GetSurveyDetailsModelResponse) {
        if (getSurveyDetailsResponse != null && getSurveyDetailsResponse.storeDetails != null && getSurveyDetailsResponse.storeDetails.get(
                0
            ).status != null
        ) {
            Toast.makeText(
                applicationContext,
                "" + getSurveyDetailsResponse.storeDetails.get(0).status,
                Toast.LENGTH_SHORT
            ).show()
//            getSurveyDetailsResponse.storeDetails.get(0).status = "COMPLETED"
            if (getSurveyDetailsResponse.storeDetails.get(0).status.equals("PENDING") || getSurveyDetailsResponse.storeDetails.get(
                    0
                ).status.equals("COMPLETED")
            ) {
                isPending = true
                if (champsRefernceId != null) {
//
//                    Utlis.showLoading(this)
                    champsSurveyViewModel.getSurveyListByChampsIDApi(
                        this,
                        champsRefernceId!!
                    )
//                    champsSurveyViewModel.getSurveyListByChampsID(
//                        this
//                    )


                }
            }
        }


    }

    override fun onFailureSurveyList(getSurveyDetailsResponse: GetSurveyDetailsModelResponse) {
        if (getSurveyDetailsResponse != null && getSurveyDetailsResponse.message != null) {
            Toast.makeText(
                applicationContext,
                "" + getSurveyDetailsResponse.message,
                Toast.LENGTH_SHORT
            ).show()
        }
        Utlis.hideLoading()
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onSuccessGetSurveyDetailsByChampsId(getSurveyDetailsByChapmpsId: GetSurevyDetailsByChampsIdResponse) {
        if (getSurveyDetailsByChapmpsId != null && getSurveyDetailsByChapmpsId.headerDetails != null) {
            activityChampsSurveyBinding.issuesTobeResolvedTextView.setText(
                getSurveyDetailsByChapmpsId!!.headerDetails.issuesToBeResolved
            )
            activityChampsSurveyBinding.technicalTextView.setText(getSurveyDetailsByChapmpsId!!.headerDetails.techinalDetails)
            activityChampsSurveyBinding.softskillsTextView.setText(getSurveyDetailsByChapmpsId!!.headerDetails.softSkills)
            activityChampsSurveyBinding.othertrainingTextview.setText(getSurveyDetailsByChapmpsId!!.headerDetails.otherTraining)

//            activityChampsSurveyBinding.siteId.text =
//                getSurveyDetailsByChapmpsId.headerDetails.storeId
            val currentTime: Date = Calendar.getInstance().getTime()
            activityChampsSurveyBinding.issuedOn.text =
                getSurveyDetailsByChapmpsId!!.headerDetails.dateOfVisit

            val strDate = getSurveyDetailsByChapmpsId!!.headerDetails.dateOfVisit
            val dateFormat = SimpleDateFormat("dd-MM-yy hh:mm:ss");
            val date = dateFormat.parse(strDate)
            val dateNewFormat =
                SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
            activityChampsSurveyBinding.issuedOn.text = dateNewFormat

            activityChampsSurveyBinding.issuedOn.text = dateNewFormat
            activityChampsSurveyBinding.storeName.text =
                siteName
            //            activityChampsSurveyBinding.State.text = getSurveyDetailsByChapmpsId.headerDetails.state
            activityChampsSurveyBinding.storeCity.text =
                getSurveyDetailsByChapmpsId.headerDetails.city
            activityChampsSurveyBinding.enterTextTechnicalEdittext.setText(
                getSurveyDetailsByChapmpsId.headerDetails.techinalDetails.toString()
            )
            activityChampsSurveyBinding.enterSoftSkillsEdittext.setText(getSurveyDetailsByChapmpsId.headerDetails.softSkills.toString())
            activityChampsSurveyBinding.enterOtherTrainingEdittext.setText(
                getSurveyDetailsByChapmpsId.headerDetails.otherTraining.toString()
            )
            activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.setText(
                getSurveyDetailsByChapmpsId.headerDetails.issuesToBeResolved.toString()
            )
            overallProgressBarCount((getSurveyDetailsByChapmpsId.headerDetails.total).toFloat())
            sumOfCategoriess = ((getSurveyDetailsByChapmpsId.headerDetails.total).toFloat())
            activityChampsSurveyBinding.employeeId.setText(getSurveyDetailsByChapmpsId.headerDetails.createdBy)
            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null) {
                if (getSurveyDetailsByChapmpsId.categoryDetails.appearanceStore != null
                    && getSurveyDetailsByChapmpsId.categoryDetails.appearanceStore != "null"
                ) {

                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.sumOfSubCategoryRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.appearanceStore).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.offerDisplay).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.storeFrontage).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.groomingStaff).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.clickedSubmit = true
//                getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.id=getSurveyDetailsByChapmpsId!!.categoryDetails.id
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.champ_auto_id =
                        getSurveyDetailsByChapmpsId!!.categoryDetails.champAutoId
//                getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.type=
//                    getSurveyDetailsByChapmpsId!!.categoryDetails.type.toString()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.subCategoryDetails?.get(
                        0
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.appearanceStore).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.subCategoryDetails?.get(
                        1
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.offerDisplay).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.subCategoryDetails?.get(
                        2
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.storeFrontage).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(0)?.subCategoryDetails?.get(
                        3
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.groomingStaff).toFloat()

                    if (getSurveyDetailsByChapmpsId.categoryDetails.cleanlinessImages != null) {
                        val cleanlinessImages =
                            getSurveyDetailsByChapmpsId.categoryDetails.cleanlinessImages
                        val cleanlinessImagesList =
                            cleanlinessImages.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        var imageUrlsCleanliness =
                            ArrayList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>()


                        for (i in cleanlinessImagesList.indices) {
                            var imageDatas =
                                GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
                            imageDatas!!.imageUrl = cleanlinessImagesList.get(i)
                            imageDatas.file = null
                            imageDatas.imageFilled = true
                            imageUrlsCleanliness!!.add(imageDatas)
                        }


                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(0)?.imageDataLists =
                            imageUrlsCleanliness

                    }

                }
                if (getSurveyDetailsByChapmpsId.categoryDetails.greetingCustomers != null
                    && getSurveyDetailsByChapmpsId.categoryDetails.greetingCustomers != "null"
                ) {


                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(1)?.sumOfSubCategoryRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.greetingCustomers).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.customerEngagement).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.customerHandling).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.reminderCalls).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(1)?.clickedSubmit = true

                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(1)?.subCategoryDetails?.get(
                        0
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.greetingCustomers).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(1)?.subCategoryDetails?.get(
                        1
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.customerEngagement).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(1)?.subCategoryDetails?.get(
                        2
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.customerHandling).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(1)?.subCategoryDetails?.get(
                        3
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.reminderCalls).toFloat()

                    if (getSurveyDetailsByChapmpsId.categoryDetails.hospitalityImages != null) {
                        val hosptalityImages =
                            getSurveyDetailsByChapmpsId.categoryDetails.hospitalityImages
                        val hosptalityImagesList =
                            hosptalityImages.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        var imageUrlsHospitality =
                            ArrayList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>()


                        for (i in hosptalityImagesList.indices) {
                            var imageDatas =
                                GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
                            imageDatas!!.imageUrl = hosptalityImagesList.get(i)
                            imageDatas.file = null
                            imageDatas.imageFilled = true
                            imageUrlsHospitality!!.add(imageDatas)
                        }


                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(1)?.imageDataLists =
                            imageUrlsHospitality

                    }
                }
                if (getSurveyDetailsByChapmpsId.categoryDetails.billingSkusDispensed != null
                    && getSurveyDetailsByChapmpsId.categoryDetails.billingSkusDispensed != "null"
                ) {


                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.sumOfSubCategoryRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.billingSkusDispensed).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.interpretationRecheckPrescription).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.bankDeposits).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.expiryFifoPolicy).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.rsCheckInternalAuditing).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.oneApolloDrConnect).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.cashCheckingEvery2Hours).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.clickedSubmit = true
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.subCategoryDetails?.get(
                        0
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.billingSkusDispensed).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.subCategoryDetails?.get(
                        1
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.interpretationRecheckPrescription).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.subCategoryDetails?.get(
                        2
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.bankDeposits).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.subCategoryDetails?.get(
                        3
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.expiryFifoPolicy).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.subCategoryDetails?.get(
                        4
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.rsCheckInternalAuditing).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.subCategoryDetails?.get(
                        5
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.oneApolloDrConnect).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(2)?.subCategoryDetails?.get(
                        6
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.cashCheckingEvery2Hours).toFloat()


                    if (getSurveyDetailsByChapmpsId.categoryDetails.accuracyImages != null) {
                        val accuracyImages =
                            getSurveyDetailsByChapmpsId.categoryDetails.accuracyImages
                        val accuracyImagesList =
                            accuracyImages.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        var imageUrlsAccuracy =
                            ArrayList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>()


                        for (i in accuracyImagesList.indices) {
                            var imageDatas =
                                GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
                            imageDatas!!.imageUrl = accuracyImagesList.get(i)
                            imageDatas.file = null
                            imageDatas.imageFilled = true
                            imageUrlsAccuracy!!.add(imageDatas)
                        }


                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(2)?.imageDataLists =
                            imageUrlsAccuracy

                    }
                }
                if (getSurveyDetailsByChapmpsId.categoryDetails.stockArrangementRefrigerator != null
                    && getSurveyDetailsByChapmpsId.categoryDetails.stockArrangementRefrigerator != "null"
                ) {
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.sumOfSubCategoryRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.stockArrangementRefrigerator).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.acWorkingCondition).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.lighting).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.planogram).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.licensesRenewal).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.biometric).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.maintenanceHdRegister).toFloat()
                    (getSurveyDetailsByChapmpsId.categoryDetails.dutyRostersAllotment).toFloat()
                    (getSurveyDetailsByChapmpsId.categoryDetails.internet).toFloat()
                    (getSurveyDetailsByChapmpsId.categoryDetails.swipingMachineWorking).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.clickedSubmit = true
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        0
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.stockArrangementRefrigerator).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        1
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.acWorkingCondition).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        2
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.lighting).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        3
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.planogram).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        4
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.licensesRenewal).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        5
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.biometric).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        6
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.maintenanceHdRegister).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        7
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.dutyRostersAllotment).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        8
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.internet).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        9
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.swipingMachineWorking).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        10
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.theCcCamerasWorking).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(3)?.subCategoryDetails?.get(
                        11
                    )?.givenRating =
                        ((getSurveyDetailsByChapmpsId!!.categoryDetails.printersWorkingCondition).toFloat())




                    if (getSurveyDetailsByChapmpsId.categoryDetails.maintenanceImages != null) {
                        val maintenanceImages =
                            getSurveyDetailsByChapmpsId.categoryDetails.maintenanceImages
                        val maintenanceImagesList =
                            maintenanceImages.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        var imageUrlsMaintainence =
                            ArrayList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>()


                        for (i in maintenanceImagesList.indices) {
                            var imageDatas =
                                GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
                            imageDatas!!.imageUrl = maintenanceImagesList.get(i)
                            imageDatas.file = null
                            imageDatas.imageFilled = true
                            imageUrlsMaintainence!!.add(imageDatas)
                        }


                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(3)?.imageDataLists =
                            imageUrlsMaintainence

                    }
                }
                if (getSurveyDetailsByChapmpsId.categoryDetails.availabilityStockGood != null
                    && getSurveyDetailsByChapmpsId.categoryDetails.availabilityStockGood != "null"
                ) {


                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(4)?.sumOfSubCategoryRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.availabilityStockGood).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.substitutionOfferedRegularly).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.serviceRecoveryDone90).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.bounceTracking).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(4)?.clickedSubmit = true

                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(4)?.subCategoryDetails?.get(
                        0
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.availabilityStockGood).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(4)?.subCategoryDetails?.get(
                        1
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.substitutionOfferedRegularly).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(4)?.subCategoryDetails?.get(
                        2
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.serviceRecoveryDone90).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(4)?.subCategoryDetails?.get(
                        3
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.bounceTracking).toFloat()



                    if (getSurveyDetailsByChapmpsId.categoryDetails.productsImages != null) {
                        val productsImages =
                            getSurveyDetailsByChapmpsId.categoryDetails.productsImages
                        val productsImagesList =
                            productsImages.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        var imageUrlsProducts =
                            ArrayList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>()


                        for (i in productsImagesList.indices) {
                            var imageDatas =
                                GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
                            imageDatas!!.imageUrl = productsImagesList.get(i)
                            imageDatas.file = null
                            imageDatas.imageFilled = true
                            imageUrlsProducts!!.add(imageDatas)
                        }
                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(4)?.imageDataLists =
                            imageUrlsProducts
                    }

                }
                if (getSurveyDetailsByChapmpsId!!.categoryDetails.speedService5To10Minutes != null
                    && getSurveyDetailsByChapmpsId.categoryDetails.speedService5To10Minutes != "null"
                ) {

                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(5)?.sumOfSubCategoryRating =
                        ((getSurveyDetailsByChapmpsId!!.categoryDetails.speedService5To10Minutes).toFloat()).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.homeDeliveryCommitmentFulfilledTime).toFloat() +
                                (getSurveyDetailsByChapmpsId.categoryDetails.salesPromotion).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(5)?.clickedSubmit = true
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(5)?.subCategoryDetails?.get(
                        0
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.speedService5To10Minutes).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(5)?.subCategoryDetails?.get(
                        1
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.homeDeliveryCommitmentFulfilledTime).toFloat()
                    getCategoryAndSubCategoryDetails?.categoryDetails?.get(5)?.subCategoryDetails?.get(
                        2
                    )?.givenRating =
                        (getSurveyDetailsByChapmpsId!!.categoryDetails.salesPromotion).toFloat()

                    if (getSurveyDetailsByChapmpsId.categoryDetails.speedServiceSalesPromotionImages != null) {
                        val speedServiceSalesPromotionImages =
                            getSurveyDetailsByChapmpsId.categoryDetails.speedServiceSalesPromotionImages
                        val speedServiceSalesPromotionImagesList =
                            speedServiceSalesPromotionImages.split(",".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        var imageUrlSpeedServiceSalesPromotion =
                            ArrayList<GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas>()


                        for (i in speedServiceSalesPromotionImagesList.indices) {
                            var imageDatas =
                                GetCategoryDetailsModelResponse.CategoryDetail.ImagesDatas()
                            imageDatas!!.imageUrl = speedServiceSalesPromotionImagesList.get(i)
                            imageDatas.file = null
                            imageDatas.imageFilled = true
                            imageUrlSpeedServiceSalesPromotion!!.add(imageDatas)
                        }


                        getCategoryAndSubCategoryDetails!!.categoryDetails?.get(5)?.imageDataLists =
                            imageUrlSpeedServiceSalesPromotion
                    }
                }


//                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails?.get(
//                        0
//                    )?.subCategoryDetails != null
//                ) {
//                    getCategoryAndSubCategoryDetails?.categoryDetails!!.get(0).subCategoryDetails!!.get(
//                        0
//                    ).givenRating =
//                        (getSurveyDetailsByChapmpsId.categoryDetails.appearanceStore).toFloat()
//                    getCategoryAndSubCategoryDetails?.categoryDetails!!.get(0).subCategoryDetails!!.get(
//                        1
//                    ).givenRating =
//                        (getSurveyDetailsByChapmpsId.categoryDetails.offerDisplay).toFloat()
//                    getCategoryAndSubCategoryDetails?.categoryDetails!!.get(0).subCategoryDetails!!.get(
//                        2
//                    ).givenRating =
//                        (getSurveyDetailsByChapmpsId.categoryDetails.storeFrontage).toFloat()
//                    getCategoryAndSubCategoryDetails?.categoryDetails!!.get(0).subCategoryDetails!!.get(
//                        3
//                    ).givenRating =
//                        (getSurveyDetailsByChapmpsId.categoryDetails.groomingStaff).toFloat()
////                    subCategoryAdapter!!.notifyDataSetChanged()
//                }

            }

//            LoadRecyclerView()


        }
        categoryDetailsAdapter =
            CategoryDetailsAdapter(
                getCategoryAndSubCategoryDetails!!.categoryDetails,
                applicationContext,
                this, status
            )
        activityChampsSurveyBinding.categoryRecyclerView.setLayoutManager(
            LinearLayoutManager(this)
        )
        activityChampsSurveyBinding.categoryRecyclerView.setAdapter(categoryDetailsAdapter)
        Utlis.hideLoading()

    }

    override fun onFailureGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse) {
        if (value != null && value.message != null) {
            Toast.makeText(applicationContext, "" + value.message, Toast.LENGTH_SHORT).show()
        }
        Utlis.hideLoading()
    }

    private var getSubCategoryResponses: GetSubCategoryDetailsModelResponse? = null
    override fun onSuccessgetSubCategoryDetails(
        getSubCategoryResponse: GetSubCategoryDetailsModelResponse,
        categoryName: String,
    ) {
        getSubCategoryResponses = getSubCategoryResponse

        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null) {
            for (i in getCategoryAndSubCategoryDetails?.categoryDetails?.indices!!) {
//                if(getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).subCategoryDetails!=null &&
//                    getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).subCategoryDetails?.size!=null){
                if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).categoryName.equals(
                        categoryName
                    )
                ) {
                    getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(i).subCategoryDetails =
                        getSubCategoryResponse.subCategoryDetails
                }
//                }

            }

        }
        if (status.equals("NEW")) {
            categoryDetailsAdapter =
                CategoryDetailsAdapter(
                    getCategoryAndSubCategoryDetails!!.categoryDetails,
                    applicationContext,
                    this,
                    status
                )
            activityChampsSurveyBinding.categoryRecyclerView.setLayoutManager(
                LinearLayoutManager(this)
            )
            activityChampsSurveyBinding.categoryRecyclerView.setAdapter(categoryDetailsAdapter)
            Utlis.hideLoading()
        } else {
            Utlis.hideLoading()
            if (champsRefernceId != null) {
//
                Utlis.showLoading(this)
                champsSurveyViewModel.getSurveyListByChampsIDApi(
                    this,
                    champsRefernceId!!
                )
//                    champsSurveyViewModel.getSurveyListByChampsID(
//                        this
//                    )


            }
        }


    }

    override fun onFailuregetSubCategoryDetails(getSubCategoryResponse: GetSubCategoryDetailsModelResponse) {
        Utlis.hideLoading()
    }

    override fun onSuccessSaveUpdateApi(value: SaveUpdateResponse) {
        Toast.makeText(context, "" + value.message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
    }

    override fun onFailureSaveUpdateApi(value: SaveUpdateResponse) {
        Toast.makeText(context, "" + value.message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            getCategoryAndSubCategoryDetails =
                data!!.getSerializableExtra("getCategoryAndSubCategoryDetails") as GetCategoryDetailsModelResponse?
//            Toast.makeText(context, ""+ getCategoryAndSubCategoryDetails?.emailDetails?.size,Toast.LENGTH_SHORT).show()
            categoryPosition = data!!.getIntExtra("categoryPosition", 0)
            var sumOfRange: Float = 0f
            for (i in getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).subCategoryDetails!!.indices) {
                if (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).subCategoryDetails!!.get(
                        i
                    ).givenRating != null
                ) {
                    var indiviualRange: Float =
                        (getCategoryAndSubCategoryDetails!!.categoryDetails!!.get(categoryPosition).subCategoryDetails!!.get(
                            i
                        ).givenRating)!!.toFloat()
                    sumOfRange = indiviualRange + sumOfRange
                    getCategoryAndSubCategoryDetails!!.categoryDetails?.get(categoryPosition)?.sumOfSubCategoryRating =
                        sumOfRange
                }

            }
            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.categoryDetails != null && categoryDetailsAdapter != null) {
                categoryDetailsAdapter =
                    CategoryDetailsAdapter(
                        getCategoryAndSubCategoryDetails!!.categoryDetails,
                        applicationContext,
                        this,
                        status
                    )
                activityChampsSurveyBinding.categoryRecyclerView.setLayoutManager(
                    LinearLayoutManager(this)
                )
                activityChampsSurveyBinding.categoryRecyclerView.setAdapter(categoryDetailsAdapter)
            }

            Utlis.hideLoading()

//
//            if (NetworkUtil.isNetworkConnected(this)) {
//                Utlis.showLoading(this)
//                champsSurveyViewModel.getTrainingAndColorDetailsApi(this, "COLOUR");
//
//            } else {
//                Toast.makeText(
//                    context,
//                    resources.getString(R.string.label_network_error),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
            if (NetworkUtil.isNetworkConnected(this)) {
//                Utlis.showLoading(this)
                champsSurveyViewModel.getTrainingAndColorDetailsApi(this, "COLOUR");

            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }

    }
}

