package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.ActivityChampsSurveyBinding
import com.apollopharmacy.vishwam.dialog.ChampsSurveyDialog
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.adapter.CategoryDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.preview.PreviewActivity
import com.apollopharmacy.vishwam.ui.home.model.*
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class ChampsSurveyActivity : AppCompatActivity(), ChampsSurveyCallBack {

    private lateinit var activityChampsSurveyBinding: ActivityChampsSurveyBinding
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
    var siteName:String?=""
    var sumOfCategoriess:Float?=0f
    private var storeCity:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityChampsSurveyBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_champs_survey

        )
        champsSurveyViewModel = ViewModelProvider(this)[ChampsSurveyViewModel::class.java]
        setUp()
        checkListeners()
    }

    private fun setUp() {
        activityChampsSurveyBinding.callback = this
        getStoreWiseDetails =
            intent.getSerializableExtra("getStoreWiseDetails") as GetStoreWiseDetailsModelResponse?
        surveyRecDetailsList =
            intent.getStringArrayListExtra("surveyRecDetailsList")!!
        surveyCCDetailsList = intent.getStringArrayListExtra("surveyCCDetailsList")!!
        address = intent.getStringExtra("address")!!
        storeId = intent.getStringExtra("storeId")!!
        siteName= intent.getStringExtra("siteName")
        storeCity = intent.getStringExtra("storeCity")!!
        val userData = LoginRepo.getProfile()
        if(userData!=null){
            activityChampsSurveyBinding.employeeName.text = userData.EMPNAME
        }
        activityChampsSurveyBinding.employeeId.text= Preferences.getValidatedEmpId()
        activityChampsSurveyBinding.siteId.text= storeId

        activityChampsSurveyBinding.storeName.text=address

        activityChampsSurveyBinding.storeId.text=storeId

        activityChampsSurveyBinding.address.text=siteName
        activityChampsSurveyBinding.storeCity.text =storeCity
//            activityChampsSurveyBinding.State.text =
        activityChampsSurveyBinding.percentageSum.text =  "0%"

        val currentTime: Date = Calendar.getInstance().getTime()
        activityChampsSurveyBinding.issuedOn.text = currentTime.toString()
        val strDate = currentTime.toString()
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        val date = dateFormat.parse(strDate)
        val dateNewFormat =
            SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
        activityChampsSurveyBinding.issuedOn.text= dateNewFormat



        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            champsSurveyViewModel.getCategoryDetailsChamps(this);

        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }



        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            champsSurveyViewModel.getTrainingAndColorDetails(this, "TECH");

        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            champsSurveyViewModel.getSurveyList(this);

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
//            champsSurveyViewModel.getCategoryDetailsChampsApi(this);
//
//        } else {
//            Toast.makeText(
//                context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }


//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            champsSurveyViewModel.getTrainingAndColorDetailsApi(this, "TECH");
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

    private fun retreiveSubCategoryData(){
        if(getCategoryAndSubCategoryDetails!=null && getCategoryAndSubCategoryDetails!!.emailDetails!=null){
            for(i in getCategoryAndSubCategoryDetails?.emailDetails?.indices!!){
                champsSurveyViewModel.getSubCategoryDetailsChampsApi(this,
                    getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).categoryName!!
                )
            }
        }

    }

    private fun LoadRecyclerView() {
        if(getCategoryAndSubCategoryDetails!=null && getCategoryAndSubCategoryDetails!!.emailDetails!=null && categoryDetailsAdapter!=null){
            categoryDetailsAdapter =
                CategoryDetailsAdapter(
                    getCategoryAndSubCategoryDetails!!.emailDetails,
                    applicationContext,
                    this
                )
            activityChampsSurveyBinding.categoryRecyclerView.setLayoutManager(
                LinearLayoutManager(this)
            )
            activityChampsSurveyBinding.categoryRecyclerView.setAdapter(categoryDetailsAdapter)
        }

        Utlis.hideLoading()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkListeners() {
        activityChampsSurveyBinding.enterTextTechnicalEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (getTrainingAndColorDetailss != null) {
                    var charLeft: String =
                        ((getTrainingAndColorDetailss!!.trainingDetails.get(0).length).toInt() - charSequence.length).toString()
                    activityChampsSurveyBinding.charLeftTechnical.setText(charLeft)
                }

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

    override fun onClickBack() {
        super.onBackPressed()
    }

    override fun onClickCategory(categoryName: String, position: Int) {
        val intent = Intent(context, ChampsDetailsandRatingBarActivity::class.java)
        intent.putExtra("categoryName", categoryName)
        intent.putExtra("getCategoryAndSubCategoryDetails", getCategoryAndSubCategoryDetails)
        intent.putExtra("position", position)
        intent.putExtra("isPending", isPending)
        startActivityForResult(intent, 111)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickSubmit() {
//        ChampsSurveyDialog().show(supportFragmentManager, "")
        saveApiRequest("submit")
    }

    private fun saveApiRequest(type: String) {
        if (NetworkUtil.isNetworkConnected(context)) {
            Utlis.showLoading(this)
            var submit = SaveSurveyModelRequest()
            var headerDetails = SaveSurveyModelRequest.HeaderDetails()
            headerDetails.state=getCategoryAndSubCategoryDetails!!.storeStateP
            headerDetails.city=getCategoryAndSubCategoryDetails!!.storeCityP
            headerDetails.storeId=getCategoryAndSubCategoryDetails!!.storeIdP
            headerDetails.dateOfVisit=getCategoryAndSubCategoryDetails!!.issuedOnP
            headerDetails.emailIdOfTrainer =
                getStoreWiseDetails?.storeWiseDetails?.trainerEmail
            headerDetails.emailIdOfExecutive =
                getStoreWiseDetails!!.storeWiseDetails.executiveEmail
            headerDetails.emailIdOfManager =
                getStoreWiseDetails!!.storeWiseDetails.managerEmail
            headerDetails.emailIdOfRegionalHead =
                getStoreWiseDetails!!.storeWiseDetails.reagionalHeadEmail
            headerDetails.emailIdOfRecipients = surveyRecDetailsList.get(0)
            headerDetails.emailIdOfCc = surveyCCDetailsList.get(0)
            headerDetails.techinalDetails =
                activityChampsSurveyBinding.enterTextTechnicalEdittext.text.toString()
            headerDetails.softSkills =
                activityChampsSurveyBinding.enterSoftSkillsEdittext.text.toString()
            headerDetails.otherTraining =
                activityChampsSurveyBinding.enterOtherTrainingEdittext.text.toString()
            headerDetails.issuesToBeResolved =
                activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.text.toString()
            headerDetails.total = "0"
            headerDetails.createdBy = Preferences.getValidatedEmpId()
            if (type.equals("submit")) {
                headerDetails.status = "1"
            } else {
                headerDetails.status = "0"
            }

            submit.headerDetails=headerDetails

            var categoryDetails = SaveSurveyModelRequest.CategoryDetails()

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                    0
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        0
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.appearanceStore =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        0
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.offerDisplay =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        0
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.storeFrontage =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        0
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.groomingStaff =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        0
                    )?.imageUrls != null
                ) {
                    var commaSeparatorUrls = ""
                    for (i in getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!!.indices) {
                        var indiviualUrl =
                            getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!!.get(
                                i
                            )
                        commaSeparatorUrls = indiviualUrl + "," + commaSeparatorUrls
                    }
                    categoryDetails.cleanlinessImages = commaSeparatorUrls

                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                    1
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        1
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.greetingCustomers =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(1)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        1
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.customerEngagement =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(1)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        1
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.customerHandling =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(1)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        1
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.reminderCalls =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(1)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                    2
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.billingSkusDispensed =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(2)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.interpretationRecheckPrescription =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(2)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.bankDeposits =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(2)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.expiryFifoPolicy =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(2)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(4).givenRating != null
                ) {
                    categoryDetails.rsCheckInternalAuditing =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(2)?.subCategoryDetails!!.get(
                            4
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(5).givenRating != null
                ) {
                    categoryDetails.oneApolloDrConnect =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(2)?.subCategoryDetails!!.get(
                            5
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        2
                    )?.subCategoryDetails!!.get(6).givenRating != null
                ) {
                    categoryDetails.cashCheckingEvery2Hours =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(2)?.subCategoryDetails!!.get(
                            6
                        ).givenRating.toString()
                }


//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                    3
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.stockArrangementRefrigerator =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.acWorkingCondition =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.lighting =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.planogram =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(4).givenRating != null
                ) {
                    categoryDetails.licensesRenewal =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            4
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(5).givenRating != null
                ) {
                    categoryDetails.biometric =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            5
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(6).givenRating != null
                ) {
                    categoryDetails.maintenanceHdRegister =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            6
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(7).givenRating != null
                ) {
                    categoryDetails.dutyRostersAllotment =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            7
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(8).givenRating != null
                ) {
                    categoryDetails.internet =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            8
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(9).givenRating != null
                ) {
                    categoryDetails.swipingMachineWorking =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            9
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(10).givenRating != null
                ) {
                    categoryDetails.theCcCamerasWorking =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            10
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        3
                    )?.subCategoryDetails!!.get(11).givenRating != null
                ) {
                    categoryDetails.printersWorkingCondition =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.subCategoryDetails!!.get(
                            11
                        ).givenRating.toString()
                }


//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                    4
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    categoryDetails.availabilityStockGood =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(4)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.substitutionOfferedRegularly =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(4)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.serviceRecoveryDone90 =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(4)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        4
                    )?.subCategoryDetails!!.get(3).givenRating != null
                ) {
                    categoryDetails.bounceTracking =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(4)?.subCategoryDetails!!.get(
                            3
                        ).givenRating.toString()
                }

//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }

            if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                    5
                )?.subCategoryDetails != null
            ) {
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        5
                    )?.subCategoryDetails!!.get(0).givenRating != null
                ) {
                    submit.categoryDetails.speedService5To10Minutes =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(5)?.subCategoryDetails!!.get(
                            0
                        ).givenRating.toString()
                }
                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        5
                    )?.subCategoryDetails!!.get(1).givenRating != null
                ) {
                    categoryDetails.homeDeliveryCommitmentFulfilledTime =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(5)?.subCategoryDetails!!.get(
                            1
                        ).givenRating.toString()
                }

                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        5
                    )?.subCategoryDetails!!.get(2).givenRating != null
                ) {
                    categoryDetails.salesPromotion =
                        getCategoryAndSubCategoryDetails!!.emailDetails?.get(5)?.subCategoryDetails!!.get(
                            2
                        ).givenRating.toString()
                }

                submit.categoryDetails = categoryDetails


//                if(getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls!=null){
//                    submit.categoryDetails.cleanlinessImages = getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls.get(0)
//
//                }

            }
            Gson().toJson(submit)

            champsSurveyViewModel.getSaveDetailsApi(submit, this)

        }
    }

    override fun onClickSaveDraft() {
//        saveApiRequest("saveDraft")
//        val intent = Intent(context, SurveyListActivity::class.java)
//        startActivity(intent)
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickPreview() {
        getCategoryAndSubCategoryDetails?.storeIdP= activityChampsSurveyBinding.storeId.text.toString()
        getCategoryAndSubCategoryDetails?.addressP=activityChampsSurveyBinding.address.text.toString()
        getCategoryAndSubCategoryDetails?.issuedOnP=activityChampsSurveyBinding.issuedOn.text.toString()
        getCategoryAndSubCategoryDetails?.storeNameP=activityChampsSurveyBinding.storeName.text.toString()
        getCategoryAndSubCategoryDetails?.storeCityP=activityChampsSurveyBinding.storeCity.text.toString()
//        getCategoryAndSubCategoryDetails?.storeStateP=activityChampsSurveyBinding.State.text.toString()
        if(getTrainingAndColorDetailss!=null && getTrainingAndColorDetailss!!.trainingDetails!=null && getTrainingAndColorDetailss!!.trainingDetails.size!=null){
            getCategoryAndSubCategoryDetails?.technicalDetails= getTrainingAndColorDetailss!!.trainingDetails.get(0).name
            getCategoryAndSubCategoryDetails?.softSkills= getTrainingAndColorDetailss!!.trainingDetails.get(1).name
            getCategoryAndSubCategoryDetails?.otherTraining= getTrainingAndColorDetailss!!.trainingDetails.get(2).name
            getCategoryAndSubCategoryDetails?.issuesToBeResolved= getTrainingAndColorDetailss!!.trainingDetails.get(3).name
        }
        getCategoryAndSubCategoryDetails?.technicalText=activityChampsSurveyBinding.enterTextTechnicalEdittext.text.toString()
        getCategoryAndSubCategoryDetails?.softSkillsText=activityChampsSurveyBinding.enterSoftSkillsEdittext.text.toString()
        getCategoryAndSubCategoryDetails?.otherTrainingText=activityChampsSurveyBinding.enterOtherTrainingEdittext.text.toString()
        getCategoryAndSubCategoryDetails?.issuesToBeResolvedText=activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.text.toString()
        getCategoryAndSubCategoryDetails!!.totalProgressP=sumOfCategoriess

        val intent = Intent(context, PreviewActivity::class.java)
        intent.putExtra("getCategoryAndSubCategoryDetails", getCategoryAndSubCategoryDetails)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }


    override fun onSuccessgetCategoryDetails(getCategoryDetails: GetCategoryDetailsModelResponse) {

        if (getCategoryDetails != null && getCategoryDetails.emailDetails != null) {
            getCategoryAndSubCategoryDetails = getCategoryDetails

            if (NetworkUtil.isNetworkConnected(this)) {
                Utlis.showLoading(this)
                champsSurveyViewModel.getSubCategoryDetailsChamps(this, "Cleanliness");

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
        if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null) {
            var sumOfCategories: Float = 0f
            for (i in getCategoryAndSubCategoryDetails!!.emailDetails?.indices!!) {
                if (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).sumOfSubCategoryRating != null) {
                    var indiviualCount: Float =
                        (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).sumOfSubCategoryRating)!!.toFloat()
                    sumOfCategories = indiviualCount + sumOfCategories
                    overallProgressBarCount(sumOfCategories)
                }


            }
            sumOfCategoriess = sumOfCategories
        }

        Utlis.hideLoading()

    }

    private fun overallProgressBarCount(sumOfCategories: Float) {
        if (sumOfCategories <= 100 && sumOfCategories >= 80) {
            activityChampsSurveyBinding.progressBarTotalGreen.progress =
                sumOfCategories.roundToInt()
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    getResources().getDrawable(R.drawable.progress_bar_green)
            activityChampsSurveyBinding.progressBarTotalGreen.visibility = View.VISIBLE
            activityChampsSurveyBinding.progressBarTotalRed.visibility = View.GONE
            activityChampsSurveyBinding.progressBarTotalOrange.visibility = View.GONE
            activityChampsSurveyBinding.percentageSum.text = sumOfCategories.toString() + "%"
        }
        else if (sumOfCategories <= 80 && sumOfCategories >= 60) {
            activityChampsSurveyBinding.progressBarTotalOrange.progress =
                sumOfCategories.roundToInt()
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    getResources().getDrawable(R.drawable.progress_bar_orange)
            activityChampsSurveyBinding.progressBarTotalGreen.visibility = View.GONE
            activityChampsSurveyBinding.progressBarTotalRed.visibility = View.GONE
            activityChampsSurveyBinding.progressBarTotalOrange.visibility = View.VISIBLE
            activityChampsSurveyBinding.percentageSum.text = sumOfCategories.toString() + "%"
        }
        else {
            activityChampsSurveyBinding.progressBarTotalRed.progress =
                sumOfCategories.roundToInt()
            activityChampsSurveyBinding.progressBarTotalGreen.visibility = View.GONE
            activityChampsSurveyBinding.progressBarTotalRed.visibility = View.VISIBLE
            activityChampsSurveyBinding.progressBarTotalOrange.visibility = View.GONE
//                activityChampsSurveyBinding.progressBarTotal.background =
//                    applicationContext.getDrawable(R.drawable.progress_bar_red)
//                   getResources().getDrawable(R.drawable.progress_bar_red)
            activityChampsSurveyBinding.percentageSum.text = sumOfCategories.toString() + "%"
        }
    }

    override fun onSuccessSaveDetailsApi(saveSurveyResponse: SaveSurveyModelResponse) {
        ChampsSurveyDialog().show(supportFragmentManager, "")
    }

    override fun onFailureSaveDetailsApi(saveSurveyResponse: SaveSurveyModelResponse) {
        if (saveSurveyResponse != null && saveSurveyResponse.message != null) {
            Toast.makeText(applicationContext, "" + saveSurveyResponse.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onSuccessSurveyList(getSurveyDetailsResponse: GetSurveyDetailsModelResponse) {
        if (getSurveyDetailsResponse != null && getSurveyDetailsResponse.storeDetails != null && getSurveyDetailsResponse.storeDetails.get(
                0
            ).status != null
        ) {
//            Toast.makeText(applicationContext,""+getSurveyDetailsResponse.storeDetails.get(0).status, Toast.LENGTH_SHORT).show()
            getSurveyDetailsResponse.storeDetails.get(0).status="COMPLETED"
            if (getSurveyDetailsResponse.storeDetails.get(0).status.equals("PENDING")) {
                isPending=true
                if (getSurveyDetailsResponse.storeDetails.get(0).champsRefernceId != null) {
//                    champsSurveyViewModel.getSurveyListByChampsIDApi(
//                        this,
//                        getSurveyDetailsResponse.storeDetails.get(0).champsRefernceId
//                    )
                    Utlis.showLoading(this)
                    champsSurveyViewModel.getSurveyListByChampsID(
                        this
                    )


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
    }

    override fun onSuccessGetSurveyDetailsByChampsId(getSurveyDetailsByChapmpsId: GetSurevyDetailsByChampsIdResponse) {
        if(getSurveyDetailsByChapmpsId!=null && getSurveyDetailsByChapmpsId.headerDetails!=null){
            activityChampsSurveyBinding.siteId.text=getSurveyDetailsByChapmpsId.headerDetails.storeId
            val strDate = getSurveyDetailsByChapmpsId.headerDetails.createdDate
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            val date = dateFormat.parse(strDate)
            val dateNewFormat =
                SimpleDateFormat("dd MMM, yyyy - hh:mm a").format(date)
            activityChampsSurveyBinding.issuedOn.text= dateNewFormat
            activityChampsSurveyBinding.storeName.text =  getSurveyDetailsByChapmpsId.headerDetails.storeId + " " + getSurveyDetailsByChapmpsId.headerDetails.state
//            activityChampsSurveyBinding.State.text = getSurveyDetailsByChapmpsId.headerDetails.state
            activityChampsSurveyBinding.storeCity.text=getSurveyDetailsByChapmpsId.headerDetails.city
            activityChampsSurveyBinding.enterTextTechnicalEdittext.setText(getSurveyDetailsByChapmpsId.headerDetails.techinalDetails.toString())
            activityChampsSurveyBinding.enterSoftSkillsEdittext.setText(getSurveyDetailsByChapmpsId.headerDetails.softSkills.toString())
            activityChampsSurveyBinding.enterOtherTrainingEdittext.setText(getSurveyDetailsByChapmpsId.headerDetails.otherTraining.toString())
            activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.setText(getSurveyDetailsByChapmpsId.headerDetails.issuesToBeResolved.toString())
            overallProgressBarCount((getSurveyDetailsByChapmpsId.headerDetails.total).toFloat())
            sumOfCategoriess= ((getSurveyDetailsByChapmpsId.headerDetails.total).toFloat())
            activityChampsSurveyBinding.employeeId.setText(getSurveyDetailsByChapmpsId.headerDetails.createdBy)
            if(getCategoryAndSubCategoryDetails!=null && getCategoryAndSubCategoryDetails!!.emailDetails!=null){
                getCategoryAndSubCategoryDetails?.emailDetails?.get(0)?.sumOfSubCategoryRating = (getSurveyDetailsByChapmpsId.categoryDetails.appearanceStore).toFloat() +
                        (getSurveyDetailsByChapmpsId.categoryDetails.offerDisplay).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.storeFrontage).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.groomingStaff).toFloat()
                var imageUrlsCleanliness = ArrayList<String>()
                imageUrlsCleanliness!!.add(getSurveyDetailsByChapmpsId.categoryDetails.cleanlinessImages)
                getCategoryAndSubCategoryDetails!!.emailDetails?.get(0)?.imageUrls=imageUrlsCleanliness

                getCategoryAndSubCategoryDetails?.emailDetails?.get(1)?.sumOfSubCategoryRating = (getSurveyDetailsByChapmpsId.categoryDetails.greetingCustomers).toFloat() +
                        (getSurveyDetailsByChapmpsId.categoryDetails.customerEngagement).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.customerHandling).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.reminderCalls).toFloat()

                var imageUrlsHospitality = ArrayList<String>()
                imageUrlsHospitality!!.add(getSurveyDetailsByChapmpsId.categoryDetails.hospitalityImages)
                getCategoryAndSubCategoryDetails!!.emailDetails?.get(1)?.imageUrls=imageUrlsHospitality

                getCategoryAndSubCategoryDetails?.emailDetails?.get(2)?.sumOfSubCategoryRating = (getSurveyDetailsByChapmpsId.categoryDetails.billingSkusDispensed).toFloat() +
                        (getSurveyDetailsByChapmpsId.categoryDetails.interpretationRecheckPrescription).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.bankDeposits).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.expiryFifoPolicy).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.rsCheckInternalAuditing).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.oneApolloDrConnect).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.cashCheckingEvery2Hours).toFloat()
                var imageUrlsAccuracy = ArrayList<String>()
                imageUrlsAccuracy!!.add(getSurveyDetailsByChapmpsId.categoryDetails.accuracyImages)
                getCategoryAndSubCategoryDetails!!.emailDetails?.get(2)?.imageUrls=imageUrlsAccuracy

                getCategoryAndSubCategoryDetails?.emailDetails?.get(3)?.sumOfSubCategoryRating = (getSurveyDetailsByChapmpsId.categoryDetails.stockArrangementRefrigerator).toFloat() +
                        (getSurveyDetailsByChapmpsId.categoryDetails.acWorkingCondition).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.lighting).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.planogram).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.licensesRenewal).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.biometric).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.maintenanceHdRegister).toFloat()
                (getSurveyDetailsByChapmpsId.categoryDetails.dutyRostersAllotment).toFloat()
                (getSurveyDetailsByChapmpsId.categoryDetails.internet).toFloat()
                (getSurveyDetailsByChapmpsId.categoryDetails.swipingMachineWorking).toFloat()
                (getSurveyDetailsByChapmpsId.categoryDetails.theCcCamerasWorking).toFloat()
                (getSurveyDetailsByChapmpsId.categoryDetails.printersWorkingCondition).toFloat()
                var imageUrlsMaintainance = ArrayList<String>()
                imageUrlsMaintainance!!.add(getSurveyDetailsByChapmpsId.categoryDetails.maintenanceImages)
                getCategoryAndSubCategoryDetails!!.emailDetails?.get(3)?.imageUrls=imageUrlsMaintainance

                getCategoryAndSubCategoryDetails?.emailDetails?.get(4)?.sumOfSubCategoryRating = (getSurveyDetailsByChapmpsId.categoryDetails.availabilityStockGood).toFloat() +
                        (getSurveyDetailsByChapmpsId.categoryDetails.substitutionOfferedRegularly).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.serviceRecoveryDone90).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.bounceTracking).toFloat()

                var imageUrlsProduct = ArrayList<String>()
                imageUrlsProduct!!.add(getSurveyDetailsByChapmpsId.categoryDetails.productsImages)
                getCategoryAndSubCategoryDetails!!.emailDetails?.get(4)?.imageUrls=imageUrlsProduct


                getCategoryAndSubCategoryDetails?.emailDetails?.get(5)?.sumOfSubCategoryRating = (getSurveyDetailsByChapmpsId.categoryDetails.speedService5To10Minutes).toFloat() +
                        (getSurveyDetailsByChapmpsId.categoryDetails.homeDeliveryCommitmentFulfilledTime).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.salesPromotion).toFloat()+
                        (getSurveyDetailsByChapmpsId.categoryDetails.speedServiceSalesPromotionImages).toFloat()

                var imageUrlsSpeed = ArrayList<String>()
                imageUrlsSpeed!!.add(getSurveyDetailsByChapmpsId.categoryDetails.speedServiceSalesPromotionImages)
                getCategoryAndSubCategoryDetails!!.emailDetails?.get(5)?.imageUrls=imageUrlsSpeed


                if (getCategoryAndSubCategoryDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails != null && getCategoryAndSubCategoryDetails!!.emailDetails?.get(
                        0
                    )?.subCategoryDetails != null
                ) {
                    getCategoryAndSubCategoryDetails?.emailDetails!!.get(0).subCategoryDetails!!.get(0).givenRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.appearanceStore).toFloat()
                    getCategoryAndSubCategoryDetails?.emailDetails!!.get(0).subCategoryDetails!!.get(1).givenRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.offerDisplay).toFloat()
                    getCategoryAndSubCategoryDetails?.emailDetails!!.get(0).subCategoryDetails!!.get(2).givenRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.storeFrontage).toFloat()
                    getCategoryAndSubCategoryDetails?.emailDetails!!.get(0).subCategoryDetails!!.get(3).givenRating =
                        (getSurveyDetailsByChapmpsId.categoryDetails.groomingStaff).toFloat()
//                    subCategoryAdapter!!.notifyDataSetChanged()
                }

            }

            LoadRecyclerView()


        }
        Utlis.hideLoading()

    }

    override fun onFailureGetSurveyDetailsByChampsId(value: GetSurevyDetailsByChampsIdResponse) {
       if(value!=null && value.message!=null){
           Toast.makeText(applicationContext, ""+value.message, Toast.LENGTH_SHORT).show()
       }
    }

    override fun onSuccessgetSubCategoryDetails(
        getSubCategoryResponse: GetSubCategoryDetailsModelResponse,
        categoryName: String,
    ) {


        if(getCategoryAndSubCategoryDetails!=null && getCategoryAndSubCategoryDetails!!.emailDetails!=null){
            for(i in getCategoryAndSubCategoryDetails?.emailDetails?.indices!!){
//                if(getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).subCategoryDetails!=null &&
//                    getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).subCategoryDetails?.size!=null){
                    if(getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).categoryName.equals(categoryName)){
                        getCategoryAndSubCategoryDetails!!.emailDetails!!.get(i).subCategoryDetails=getSubCategoryResponse.subCategoryDetails
                    }
//                }

        }

        }
        categoryDetailsAdapter =
            CategoryDetailsAdapter(
                getCategoryAndSubCategoryDetails!!.emailDetails,
                applicationContext,
                this
            )
        activityChampsSurveyBinding.categoryRecyclerView.setLayoutManager(
            LinearLayoutManager(this)
        )
        activityChampsSurveyBinding.categoryRecyclerView.setAdapter(categoryDetailsAdapter)
        Utlis.hideLoading()
    }

    override fun onFailuregetSubCategoryDetails(getSubCategoryResponse: GetSubCategoryDetailsModelResponse) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            getCategoryAndSubCategoryDetails =
                data!!.getSerializableExtra("getCategoryAndSubCategoryDetails") as GetCategoryDetailsModelResponse?
//            Toast.makeText(context, ""+ getCategoryAndSubCategoryDetails?.emailDetails?.size,Toast.LENGTH_SHORT).show()
            categoryPosition = data!!.getIntExtra("categoryPosition", 0)
            var sumOfRange: Float = 0f
            for (i in getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!.indices) {
                if (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!.get(
                        i
                    ).givenRating != null
                ) {
                    var indiviualRange: Float =
                        (getCategoryAndSubCategoryDetails!!.emailDetails!!.get(categoryPosition).subCategoryDetails!!.get(
                            i
                        ).givenRating)!!.toFloat()
                    sumOfRange = indiviualRange + sumOfRange
                    getCategoryAndSubCategoryDetails!!.emailDetails?.get(categoryPosition)?.sumOfSubCategoryRating =
                        sumOfRange
                }

            }
            if(getCategoryAndSubCategoryDetails!=null && getCategoryAndSubCategoryDetails!!.emailDetails!=null && categoryDetailsAdapter!=null){
                categoryDetailsAdapter =
                    CategoryDetailsAdapter(
                        getCategoryAndSubCategoryDetails!!.emailDetails,
                        applicationContext,
                        this
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
                Utlis.showLoading(this)
                champsSurveyViewModel.getTrainingAndColorDetails(this, "COLOUR");

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

