package com.apollopharmacy.vishwam.ui.home.planogram.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.Preferences.getLoginJson
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.databinding.ActivityPlanogramEvaluationBinding
import com.apollopharmacy.vishwam.databinding.AreasToFocusonDialogBinding
import com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter.AreasToFocusOnAdapter
import com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter.PlanogramCateoryAdapter
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramDetailsListResponse
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSaveUpdateRequest
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.signaturepad.NetworkUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.tomergoldst.tooltips.ToolTipsManager
import java.text.SimpleDateFormat
import java.util.*

class PlanogramEvaluationActivity : AppCompatActivity(), PlanogramActivityCallback,
    ToolTipsManager.TipListener {
    lateinit var activityPlanogramEvaluationBinding: ActivityPlanogramEvaluationBinding
    lateinit var planogramActivityViewModel: PlanogramActivityViewModel
    var planogramCategoryAdapter: PlanogramCateoryAdapter? = null
    private lateinit var dialogSubmit: Dialog
    var areasToFocusOnAdapter: AreasToFocusOnAdapter? = null
    var detailsListResponse = PlanogramDetailsListResponse()
    var categoriesToFocusOnList = ArrayList<String>()
    var areasToFocusOnList = ArrayList<String>()
    public var uid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPlanogramEvaluationBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_planogram_evaluation
        )
        planogramActivityViewModel = ViewModelProvider(this)[PlanogramActivityViewModel::class.java]
        setUp()
    }

    @SuppressLint("SetTextI18n")
    private fun setUp() {

        if (intent != null) {
            uid = intent.getStringExtra("uid")!!


        }

        activityPlanogramEvaluationBinding.callback = this
        activityPlanogramEvaluationBinding.areasToFocusText.text = "Areas to focus on (" + 0 + ")"
        activityPlanogramEvaluationBinding.categoresToFocusOnText.text =
            "Categories to focus on (" + 0 + ")"
        val loginJson = getLoginJson()
        var loginData: LoginDetails? = null
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            loginData = gson.fromJson(loginJson, LoginDetails::class.java)
        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        if (loginData != null) {
//            userNameText.setText("JaiKumar Loknathan Mudaliar");

//            userNameText.setText("JaiKumar Loknathan Mudaliar");
            activityPlanogramEvaluationBinding.employeeName.text = loginData.EMPNAME
            activityPlanogramEvaluationBinding.employeeId.text = loginData.EMPID
        }
        activityPlanogramEvaluationBinding.storeName.text = Preferences.getPlanogramSiteName()
        activityPlanogramEvaluationBinding.storeId.text = Preferences.getPlanogramSiteId()
        activityPlanogramEvaluationBinding.storeCity.text = Preferences.getPlanogramSiteCity()
        activityPlanogramEvaluationBinding.state.text = Preferences.getPlanogramSiteState()


        if (uid.isNullOrEmpty()) {
            activityPlanogramEvaluationBinding.startSurveyButton.visibility = View.VISIBLE
            detailsListResponse.data = null

            if (NetworkUtils.isNetworkConnected(this)) {
                Utlis.showLoading(this@PlanogramEvaluationActivity)
                planogramActivityViewModel.planogramSurveyQuestionsListApi(this)
            } else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {

            activityPlanogramEvaluationBinding.startSurveyButton.visibility = View.GONE
            if (NetworkUtils.isNetworkConnected(this)) {
                Utlis.showLoading(this@PlanogramEvaluationActivity)
                planogramActivityViewModel.planogramDetailListApi(this, uid)
            } else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    override fun onClickBack() {
        super.onBackPressed()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onClickSubmit() {
        var allScoresHaving = true
        for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!) {
            for (j in i.questions!!) {
                if (j.value.isNullOrEmpty()) {
                    allScoresHaving = false
                }
            }
        }
        if (allScoresHaving) {
            activityPlanogramEvaluationBinding.startSurveyButton.background =
                resources.getDrawable(R.drawable.greenbackgrounf_forstartsurvey)
            var planogramRequest = PlanogramSaveUpdateRequest()
            var commaSeparatorAreasToFocusOn = ""
            if (areasToFocusOnList != null && areasToFocusOnList!!.size > 0) {
                for (i in areasToFocusOnList!!.indices) {
                    var indiviualCat =
                        areasToFocusOnList!!.get(i)
                    if (!commaSeparatorAreasToFocusOn.isEmpty()) {
                        commaSeparatorAreasToFocusOn =
                            indiviualCat + "," + commaSeparatorAreasToFocusOn
                    } else {
                        commaSeparatorAreasToFocusOn = indiviualCat
                    }

                }
                planogramRequest.areasToFocusOn = commaSeparatorAreasToFocusOn
            } else {
                planogramRequest.areasToFocusOn = ""
            }

            planogramRequest.branchName =
                activityPlanogramEvaluationBinding.storeName.text.toString()
            planogramRequest.siteId = activityPlanogramEvaluationBinding.storeId.text.toString()
            planogramRequest.employeeId = Preferences.getValidatedEmpId()
            var commaSeparatorCategoriesToFocusOn = ""
            if (categoriesToFocusOnList != null && categoriesToFocusOnList!!.size > 0) {
                for (i in categoriesToFocusOnList!!.indices) {
                    var indiviualCat =
                        categoriesToFocusOnList!!.get(i)
                    if (!commaSeparatorCategoriesToFocusOn.isEmpty()) {
                        commaSeparatorCategoriesToFocusOn =
                            indiviualCat + "," + commaSeparatorCategoriesToFocusOn
                    } else {
                        commaSeparatorCategoriesToFocusOn = indiviualCat
                        planogramRequest.categoriesToFocusOn = commaSeparatorCategoriesToFocusOn
                    }
                }
            } else {
                planogramRequest.categoriesToFocusOn = ""
            }


            val currentTime: Date = Calendar.getInstance().getTime()
            val strDate = currentTime.toString()
            val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
            val date = dateFormat.parse(strDate)
            val dateNewFormat =
                SimpleDateFormat("YYYY-MM-dd").format(date)
            planogramRequest.date = dateNewFormat
            planogramRequest.overallScore =
                planogramSurveyQuestionsListResponses!!.overAllScore.toString()
            val planogramSurveyList =
                java.util.ArrayList<PlanogramSaveUpdateRequest.PlanogramSurvey>()
            val planogramChillerList = java.util.ArrayList<PlanogramSaveUpdateRequest.Chiller>()
            val planogramDiaperPodiumList =
                java.util.ArrayList<PlanogramSaveUpdateRequest.DiaperPodium>()
            val planogramOffersGondolaList =
                java.util.ArrayList<PlanogramSaveUpdateRequest.OffersGondola>()
            val planogramPegHooksDisplayList =
                java.util.ArrayList<PlanogramSaveUpdateRequest.PeghooksDisplay>()
            val planogramPostersList = java.util.ArrayList<PlanogramSaveUpdateRequest.Poster>()
            val planogramValueBinList =
                java.util.ArrayList<PlanogramSaveUpdateRequest.ValueDealsBin>()
            for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.indices) {

                if (planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).type.equals(
                        "category"
                    )
                ) {
                    var planogramSurvey = planogramRequest.PlanogramSurvey()
                    planogramSurvey.score =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).categoryScore.toString()
                    var categoryTypeUid = planogramSurvey.CategoryType()
                    categoryTypeUid.uid =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).categoryType!!.uid
                    planogramSurvey.name =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).name
                    planogramSurvey.categoryType = categoryTypeUid
                    for (j in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).questions!!) {

                        if (j.name.equals("Cleanliness")) {
                            var cleanliness = planogramSurvey.Cleanliness()
                            var uid = j.value
                            cleanliness.uid = uid
                            planogramSurvey.cleanliness = cleanliness
                            planogramSurvey.cleanHintText = j.hintText
                        } else if (j.name.equals("Subcategory Flow")) {
                            var subCatFlow = planogramSurvey.SubcatFlow()
                            var uid = j.value
                            subCatFlow.uid = uid
                            planogramSurvey.subcatFlow = subCatFlow
                            planogramSurvey.subcatFlowHint = j.hintText
                        } else if (j.name.equals("Left to right/ Front to Back")) {
                            var leftToRight = planogramSurvey.LftRightFrntBck()
                            var uid = j.value
                            leftToRight.uid = uid
                            planogramSurvey.lftRightFrntBck = leftToRight
                            planogramSurvey.lftRightFrntBckHint = j.hintText
                        } else if (j.name.equals("FIFO")) {
                            var fifo = planogramSurvey.Fifo()
                            var uid = j.value
                            fifo.uid = uid
                            planogramSurvey.fifo = fifo
                            planogramSurvey.fifoHintText = j.hintText
                        } else if (j.name.equals("Empty shelves refilling")) {
                            var emptyShelvesRefill = planogramSurvey.EmptyShelvesRefill()
                            var uid = j.value
                            emptyShelvesRefill.uid = uid
                            planogramSurvey.emptyShelvesRefill = emptyShelvesRefill
                            planogramSurvey.emptyShelRefHint = j.hintText
                        } else if (j.name.equals("Shelf facing")) {
                            var shelvesFacing = planogramSurvey.ShelfFacing()
                            var uid = j.value
                            shelvesFacing.uid = uid
                            planogramSurvey.shelfFacing = shelvesFacing
                            planogramSurvey.shelfFacingHint = j.hintText
                        } else if (j.name.equals("Condition tags/ Offer talkers")) {
                            var condTag = planogramSurvey.CondTagOffTalkers()
                            var uid = j.value
                            condTag.uid = uid
                            planogramSurvey.condTagOffTalkers = condTag
                            planogramSurvey.condTagTalkersHint = j.hintText
                        } else if (j.name.equals("Shelf strips")) {
                            var condTag = planogramSurvey.ShelfStrips()
                            var uid = j.value
                            condTag.uid = uid
                            planogramSurvey.shelfStrips = condTag
                            planogramSurvey.shelfStripHint = j.hintText
                        }

                    }
                    planogramSurveyList.add(planogramSurvey)
                } else if (planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).type.equals(
                        "chiller"
                    )
                ) {
                    var planogramChiller = planogramRequest.Chiller()
                    planogramChiller.name =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).name

                    for (j in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).questions!!) {
                        var type = planogramChiller.Type__1()
                        var uidType =
                            planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).categoryType!!.uid
                        type.uid = uidType
                        planogramChiller.type = type

                        if (j.name.equals("Grouped product wise")) {
                            var groupedProductWise = planogramChiller.GroupedProductWise__1()
                            var uid = j.value
                            groupedProductWise.uid = uid
                            planogramChiller.groupedProductWise = groupedProductWise
                        } else if (j.name.equals("Shelf facing")) {
                            var shelfFacing = planogramChiller.ShelfFacing__1()
                            var uid = j.value
                            shelfFacing.uid = uid
                            planogramChiller.shelfFacing = shelfFacing
                        } else if (j.name.equals("No gaps")) {
                            var noGaps = planogramChiller.NoGaps__1()
                            var uid = j.value
                            noGaps.uid = uid
                            planogramChiller.noGaps = noGaps
                        }

                    }
                    planogramChillerList.add(planogramChiller)
                    planogramRequest.chiller = planogramChillerList
                } else if (planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).type.equals(
                        "diaper_podium"
                    )
                ) {
                    var planogramDiaperPodium = planogramRequest.DiaperPodium()
                    planogramDiaperPodium.name =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).name
                    for (j in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).questions!!) {
                        var type = planogramDiaperPodium.Type__2()
                        var uidType =
                            planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).categoryType!!.uid
                        type.uid = uidType
                        planogramDiaperPodium.type = type
                        if (j.name.equals("All brands displayed")) {
                            var allbrandsdisplay1 = planogramDiaperPodium.AllBrandsDisplay__1()
                            var uid = j.value
                            allbrandsdisplay1.uid = uid
                            planogramDiaperPodium.allBrandsDisplay = allbrandsdisplay1
                        } else if (j.name.equals("Offer Tent card")) {
                            var offerTentCard = planogramDiaperPodium.OfferTentCard__1()
                            var uid = j.value
                            offerTentCard.uid = uid
                            planogramDiaperPodium.offerTentCard = offerTentCard
                        }

                    }
                    planogramDiaperPodiumList.add(planogramDiaperPodium)
                    planogramRequest.diaperPodium = planogramDiaperPodiumList
                } else if (planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).type.equals(
                        "offers_gondola"
                    )
                ) {
                    var planogramOffersGandola = planogramRequest.OffersGondola()
                    planogramOffersGandola.name =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).name
                    for (j in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).questions!!) {
                        var type = planogramOffersGandola.Type__3()
                        var uidType =
                            planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).categoryType!!.uid
                        type.uid = uidType
                        planogramOffersGandola.type = type
                        if (j.name.equals("Correct offer talkers")) {
                            var correctOffersTal = planogramOffersGandola.CorrectOfferTalkers__1()
                            var uid = j.value
                            correctOffersTal.uid = uid
                            planogramOffersGandola.correctOfferTalkers = correctOffersTal
                        } else if (j.name.equals("Shelf facing")) {
                            var shelfFacing = planogramOffersGandola.ShelfFacing__2()
                            var uid = j.value
                            shelfFacing.uid = uid
                            planogramOffersGandola.shelfFacing = shelfFacing
                        }

                    }
                    planogramOffersGondolaList.add(planogramOffersGandola)
                    planogramRequest.offersGondola = planogramOffersGondolaList
                } else if (planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).type.equals(
                        "peghooks_display"
                    )
                ) {
                    var planogramPeghooksDisplay = planogramRequest.PeghooksDisplay()
                    planogramPeghooksDisplay.name =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).name
                    for (j in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).questions!!) {
                        var type = planogramPeghooksDisplay.Type__4()
                        var uidType =
                            planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).categoryType!!.uid
                        type.uid = uidType
                        planogramPeghooksDisplay.type = type
                        if (j.name.equals("Customer facing")) {
                            var customerFacing = planogramPeghooksDisplay.CustomerFacing__1()
                            var uid = j.value
                            customerFacing.uid = uid
                            planogramPeghooksDisplay.customerFacing = customerFacing
                        } else if (j.name.equals("Grouped product wise")) {
                            var groupedProductWise =
                                planogramPeghooksDisplay.GroupedProductWise__2()
                            var uid = j.value
                            groupedProductWise.uid = uid
                            planogramPeghooksDisplay.groupedProductWise = groupedProductWise
                        }

                    }
                    planogramPegHooksDisplayList.add(planogramPeghooksDisplay)
                    planogramRequest.peghooksDisplay = planogramPegHooksDisplayList
                } else if (planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).type.equals(
                        "posters"
                    )
                ) {
                    var planogramPosters = planogramRequest.Poster()
                    planogramPosters.name =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).name
                    for (j in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).questions!!) {
                        var type = planogramPosters.Type__5()
                        var uidType =
                            planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).categoryType!!.uid
                        type.uid = uidType
                        planogramPosters.type = type
                        if (j.name.equals("Monthly offers")) {
                            var monthlyOffers = planogramPosters.MonthlyOffers__1()
                            var uid = j.value
                            monthlyOffers.uid = uid
                            planogramPosters.monthlyOffers = monthlyOffers
                        } else if (j.name.equals("Smart Saver offer")) {
                            var smartSaver = planogramPosters.SmartSaveOffer__1()
                            var uid = j.value
                            smartSaver.uid = uid
                            planogramPosters.smartSaveOffer = smartSaver
                        } else if (j.name.equals("No unathorised/old posters")) {
                            var noAuth = planogramPosters.NoUnauthOldPosters__1()
                            var uid = j.value
                            noAuth.uid = uid
                            planogramPosters.noUnauthOldPosters = noAuth
                        }

                    }
                    planogramPostersList.add(planogramPosters)
                    planogramRequest.posters = planogramPostersList
                } else if (planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).type.equals(
                        "value_deals_bin"
                    )
                ) {
                    var planogramValueDealsBin = planogramRequest.ValueDealsBin()
                    planogramValueDealsBin.name =
                        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).name
                    for (j in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).questions!!) {
                        var type = planogramValueDealsBin.Type__6()
                        var uidType =
                            planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(i).categoryType!!.uid
                        type.uid = uidType
                        planogramValueDealsBin.type = type
                        if (j.name.equals("Filled up to top")) {
                            var fillUpto = planogramValueDealsBin.FillUptoTop__1()
                            var uid = j.value
                            fillUpto.uid = uid
                            planogramValueDealsBin.fillUptoTop = fillUpto
                        } else if (j.name.equals("Grouped product wise")) {
                            var groupedProductWise = planogramValueDealsBin.GroupedProductWise__3()
                            var uid = j.value
                            groupedProductWise.uid = uid
                            planogramValueDealsBin.groupedProductWise = groupedProductWise
                        }

                    }
                    planogramValueBinList.add(planogramValueDealsBin)
                    planogramRequest.valueDealsBin = planogramValueBinList
                }

            }
            planogramRequest.planogramSurvey = planogramSurveyList

            Utlis.showLoading(this)

            planogramActivityViewModel.planogramSaveUpdateApi(this, planogramRequest)
        } else {
            activityPlanogramEvaluationBinding.startSurveyButton.background =
                resources.getDrawable(R.drawable.ashbackground_for_submit_planogram)
            Toast.makeText(
                applicationContext,
                "Please submit all the sub category values",
                Toast.LENGTH_SHORT
            ).show()
        }


//        dialogSubmit = Dialog(this)
//        val close = dialogSubmit.findViewById<ImageView>(R.id.close_dialog_save_plano)
//        val ok = dialogSubmit.findViewById<LinearLayout>(R.id.ok_button_plano)
//        dialogSubmit.setContentView(R.layout.success_dialog_plano)
//        dialogSubmit.setCancelable(true)
////        close.setOnClickListener {
////            dialogSubmit.dismiss()
////            val intent = Intent()
////            setResult(Activity.RESULT_OK, intent)
////            finish()
////        }
////        ok.setOnClickListener {
////            dialogSubmit.dismiss()
////            val intent = Intent()
////            setResult(Activity.RESULT_OK, intent)
////            finish()
////        }
//        dialogSubmit.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialogSubmit.show()
    }

    override fun onClickAreasToFocusOn() {
        showDialogPlano("Areas to Focus on")

    }

    @SuppressLint("SetTextI18n")
    fun showDialogPlano(focusOnName: String) {
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val areasToFocusOnBinding =
            DataBindingUtil.inflate<AreasToFocusonDialogBinding>(
                LayoutInflater.from(this),
                R.layout.areas_to_focuson_dialog,
                null,
                false
            )
        dialog.setContentView(areasToFocusOnBinding.root)
        if (focusOnName.equals("Areas to Focus on")) {
            areasToFocusOnBinding.name.setText("Areas to Focus on")

            areasToFocusOnAdapter =
                AreasToFocusOnAdapter(applicationContext, areasToFocusOnList!!, focusOnName)
            val layoutManager = LinearLayoutManager(this)
            areasToFocusOnBinding.areasToFocusOnRecyclerview.setLayoutManager(
                layoutManager
            )
            areasToFocusOnBinding.areasToFocusOnRecyclerview.setAdapter(
                areasToFocusOnAdapter
            )
        } else {
            areasToFocusOnBinding.name.setText("Categories to Focus on")


            areasToFocusOnAdapter =
                AreasToFocusOnAdapter(applicationContext, categoriesToFocusOnList!!, focusOnName)
            val layoutManager = LinearLayoutManager(this)
            areasToFocusOnBinding.areasToFocusOnRecyclerview.setLayoutManager(
                layoutManager
            )
            areasToFocusOnBinding.areasToFocusOnRecyclerview.setAdapter(
                areasToFocusOnAdapter
            )
        }





        areasToFocusOnBinding.closeButtonAreas.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.show()

    }

    override fun onClickCategoriesToFocusOn() {
        showDialogPlano("Categories To Focus On")
    }

    override fun showTillTop() {

    }

    override fun onSuccessPlanogramDetailListApiCall(planogramDetailsListResponse: PlanogramDetailsListResponse) {
        Utlis.hideLoading()
        detailsListResponse = planogramDetailsListResponse
        var yesCount: Int = 0
        var noCount: Int = 0
        var naCount: Int = 0

        if (planogramDetailsListResponse.data != null) {
            if (planogramDetailsListResponse.data!!.planogramSurvey != null) {


                val planogramSurvey = planogramDetailsListResponse.data?.planogramSurvey


                if (planogramSurvey != null) {
                    yesCount += planogramSurvey.count { it.categoryType?.uid != null && it.shelfFacing?.uid == "Y" }
                    noCount += planogramSurvey.count { it.categoryType?.uid != null && it.shelfFacing?.uid == "N" }
                    naCount += planogramSurvey.count { it.categoryType?.uid != null && it.shelfFacing?.uid == "NA" }

                    yesCount += planogramSurvey.count { it.categoryType?.uid != null && it.cleanliness?.uid == "Y" }
                    noCount += planogramSurvey.count { it.categoryType?.uid != null && it.cleanliness?.uid == "N" }
                    naCount += planogramSurvey.count { it.categoryType?.uid != null && it.cleanliness?.uid == "NA" }


                    yesCount += planogramSurvey.count { it.categoryType?.uid != null && it.lftRightFrntBck?.uid == "Y" }
                    noCount += planogramSurvey.count { it.categoryType?.uid != null && it.lftRightFrntBck?.uid == "N" }
                    naCount += planogramSurvey.count { it.categoryType?.uid != null && it.lftRightFrntBck?.uid == "NA" }

                    yesCount += planogramSurvey.count { it.categoryType?.uid != null && it.fifo?.uid == "Y" }
                    noCount += planogramSurvey.count { it.categoryType?.uid != null && it.fifo?.uid == "N" }
                    naCount += planogramSurvey.count { it.categoryType?.uid != null && it.fifo?.uid == "NA" }


                    yesCount += planogramSurvey.count { it.categoryType?.uid != null && it.shelfStrips?.uid == "Y" }
                    noCount += planogramSurvey.count { it.categoryType?.uid != null && it.shelfStrips?.uid == "N" }
                    naCount += planogramSurvey.count { it.categoryType?.uid != null && it.shelfStrips?.uid == "NA" }

                    yesCount += planogramSurvey.count { it.categoryType?.uid != null && it.condTagOffTalkers?.uid == "Y" }
                    noCount += planogramSurvey.count { it.categoryType?.uid != null && it.condTagOffTalkers?.uid == "N" }
                    naCount += planogramSurvey.count { it.categoryType?.uid != null && it.condTagOffTalkers?.uid == "NA" }


                    yesCount += planogramSurvey.count { it.categoryType?.uid != null && it.emptyShelvesRefill?.uid == "Y" }
                    noCount += planogramSurvey.count { it.categoryType?.uid != null && it.emptyShelvesRefill?.uid == "N" }
                    naCount += planogramSurvey.count { it.categoryType?.uid != null && it.emptyShelvesRefill?.uid == "NA" }

                    yesCount += planogramSurvey.count { it.categoryType?.uid != null && it.subcatFlow?.uid == "Y" }
                    noCount += planogramSurvey.count { it.categoryType?.uid != null && it.subcatFlow?.uid == "N" }
                    naCount += planogramSurvey.count { it.categoryType?.uid != null && it.subcatFlow?.uid == "NA" }


                }


            }

            if (planogramDetailsListResponse.data!!.diaperPodium != null) {
                val planogramSurvey = planogramDetailsListResponse.data?.diaperPodium
                if (planogramSurvey != null) {
                    yesCount += planogramSurvey.count { it.offerTentCard!!.uid != null && it.offerTentCard?.uid == "Y" }
                    noCount += planogramSurvey.count { it.offerTentCard!!.uid != null && it.offerTentCard?.uid == "N" }
                    naCount += planogramSurvey.count { it.offerTentCard!!.uid != null && it.offerTentCard?.uid == "NA" }


                    yesCount += planogramSurvey.count { it.allBrandsDisplay!!.uid != null && it.allBrandsDisplay?.uid == "Y" }
                    noCount += planogramSurvey.count { it.allBrandsDisplay!!.uid != null && it.allBrandsDisplay?.uid == "N" }
                    naCount += planogramSurvey.count { it.allBrandsDisplay!!.uid != null && it.allBrandsDisplay?.uid == "NA" }
                }
            }

            if (planogramDetailsListResponse.data!!.valueDealsBin != null) {
                val planogramSurvey = planogramDetailsListResponse.data?.valueDealsBin
                if (planogramSurvey != null) {
                    yesCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "Y" }
                    noCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "N" }
                    naCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "NA" }


                    yesCount += planogramSurvey.count { it.fillUptoTop!!.uid != null && it.fillUptoTop?.uid == "Y" }
                    noCount += planogramSurvey.count { it.fillUptoTop!!.uid != null && it.fillUptoTop?.uid == "N" }
                    naCount += planogramSurvey.count { it.fillUptoTop!!.uid != null && it.fillUptoTop?.uid == "NA" }
                }
            }

            if (planogramDetailsListResponse.data!!.posters != null) {
                val planogramSurvey = planogramDetailsListResponse.data?.posters
                if (planogramSurvey != null) {
                    yesCount += planogramSurvey.count { it.monthlyOffers!!.uid != null && it.monthlyOffers?.uid == "Y" }
                    noCount += planogramSurvey.count { it.monthlyOffers!!.uid != null && it.monthlyOffers?.uid == "N" }
                    naCount += planogramSurvey.count { it.monthlyOffers!!.uid != null && it.monthlyOffers?.uid == "NA" }

                    yesCount += planogramSurvey.count { it.smartSaveOffer!!.uid != null && it.smartSaveOffer?.uid == "Y" }
                    noCount += planogramSurvey.count { it.smartSaveOffer!!.uid != null && it.smartSaveOffer?.uid == "N" }
                    naCount += planogramSurvey.count { it.smartSaveOffer!!.uid != null && it.smartSaveOffer?.uid == "NA" }

                    yesCount += planogramSurvey.count { it.noUnauthOldPosters!!.uid != null && it.noUnauthOldPosters?.uid == "Y" }
                    noCount += planogramSurvey.count { it.noUnauthOldPosters!!.uid != null && it.noUnauthOldPosters?.uid == "N" }
                    naCount += planogramSurvey.count { it.noUnauthOldPosters!!.uid != null && it.noUnauthOldPosters?.uid == "NA" }
                }
            }

            if (planogramDetailsListResponse.data!!.chiller != null) {
                val planogramSurvey = planogramDetailsListResponse.data?.chiller
                if (planogramSurvey != null) {
                    yesCount += planogramSurvey.count { it.shelfFacing!!.uid != null && it.shelfFacing?.uid == "Y" }
                    noCount += planogramSurvey.count { it.shelfFacing!!.uid != null && it.shelfFacing?.uid == "N" }
                    naCount += planogramSurvey.count { it.shelfFacing!!.uid != null && it.shelfFacing?.uid == "NA" }


                    yesCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "Y" }
                    noCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "N" }
                    naCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "NA" }

                    yesCount += planogramSurvey.count { it.noGaps!!.uid != null && it.noGaps?.uid == "Y" }
                    noCount += planogramSurvey.count { it.noGaps!!.uid != null && it.noGaps?.uid == "N" }
                    naCount += planogramSurvey.count { it.noGaps!!.uid != null && it.noGaps?.uid == "NA" }
                }
            }

            if (planogramDetailsListResponse.data!!.offersGondola != null) {
                val planogramSurvey = planogramDetailsListResponse.data?.offersGondola
                if (planogramSurvey != null) {
                    yesCount += planogramSurvey.count { it.shelfFacing!!.uid != null && it.shelfFacing?.uid == "Y" }
                    noCount += planogramSurvey.count { it.shelfFacing!!.uid != null && it.shelfFacing?.uid == "N" }
                    naCount += planogramSurvey.count { it.shelfFacing!!.uid != null && it.shelfFacing?.uid == "NA" }


                    yesCount += planogramSurvey.count { it.correctOfferTalkers!!.uid != null && it.correctOfferTalkers?.uid == "Y" }
                    noCount += planogramSurvey.count { it.correctOfferTalkers!!.uid != null && it.correctOfferTalkers?.uid == "N" }
                    naCount += planogramSurvey.count { it.correctOfferTalkers!!.uid != null && it.correctOfferTalkers?.uid == "NA" }
                }
            }
            if (planogramDetailsListResponse.data!!.peghooksDisplay != null) {
                val planogramSurvey = planogramDetailsListResponse.data?.peghooksDisplay
                if (planogramSurvey != null) {
                    yesCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "Y" }
                    noCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "N" }
                    naCount += planogramSurvey.count { it.groupedProductWise!!.uid != null && it.groupedProductWise?.uid == "NA" }


                    yesCount += planogramSurvey.count { it.customerFacing!!.uid != null && it.customerFacing?.uid == "Y" }
                    noCount += planogramSurvey.count { it.customerFacing!!.uid != null && it.customerFacing?.uid == "N" }
                    naCount += planogramSurvey.count { it.customerFacing!!.uid != null && it.customerFacing?.uid == "NA" }
                }
            }



            activityPlanogramEvaluationBinding.overAllPercentage.setText(
                "OverAll Score" + " : " +
                        planogramDetailsListResponse.data!!.overallScore.toString()
            )

            activityPlanogramEvaluationBinding.noCount.setText(noCount.toString())
            activityPlanogramEvaluationBinding.naCount.setText(naCount.toString())

            activityPlanogramEvaluationBinding.yesCount.setText(yesCount.toString())

        }



        if (planogramDetailsListResponse.data!!.categoriesToFocusOn != null) {
            categoriesToFocusOnList =
                ArrayList(planogramDetailsListResponse.data!!.categoriesToFocusOn!!.split(","))
        }

        if (planogramDetailsListResponse.data!!.areasToFocusOn != null) {
            areasToFocusOnList =
                ArrayList((planogramDetailsListResponse.data!!.areasToFocusOn!!.split(",")))
        }

        activityPlanogramEvaluationBinding.categoresToFocusOnText.text =
            "Categories to focus on (" + categoriesToFocusOnList.size + ")"

        activityPlanogramEvaluationBinding.areasToFocusText.text =
            "Areas to focus on (" + areasToFocusOnList.size + ")"



        if (NetworkUtils.isNetworkConnected(this)) {
            Utlis.showLoading(this@PlanogramEvaluationActivity)
            planogramActivityViewModel.planogramSurveyQuestionsListApi(this)
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    var planogramSurveyQuestionsListResponses: PlanogramSurveyQuestionsListResponse? = null
    override fun onSuccessPlanogramSurveyQuestionsListApiCall(planogramSurveyQuestionsListResponse: PlanogramSurveyQuestionsListResponse) {
        activityPlanogramEvaluationBinding.parentLayout.visibility = View.VISIBLE
        Utlis.hideLoading()
        if (planogramSurveyQuestionsListResponse?.data != null
            && planogramSurveyQuestionsListResponse.data!!.listData != null
            && planogramSurveyQuestionsListResponse.data!!.listData!!.rows != null
            && planogramSurveyQuestionsListResponse.data!!.listData!!.rows!!.size > 0
        ) {
            planogramSurveyQuestionsListResponses = planogramSurveyQuestionsListResponse
            planogramCategoryAdapter =
                PlanogramCateoryAdapter(
                    planogramSurveyQuestionsListResponse.data!!.listData!!.rows!!,
                    applicationContext,
                    this, detailsListResponse
                )
            activityPlanogramEvaluationBinding.planogramCategoryRecyclerview.setLayoutManager(
                LinearLayoutManager(this)
            )
            activityPlanogramEvaluationBinding.planogramCategoryRecyclerview.setAdapter(
                planogramCategoryAdapter
            )
        }

    }

    override fun onFailurePlanogramSurveyQuestionsListApiCall(message: String) {
        Utlis.hideLoading()
    }

    override fun countValues(
        questionsList: ArrayList<PlanogramSurveyQuestionsListResponse.Questions>,
        questionsListResponse: ArrayList<PlanogramSurveyQuestionsListResponse.Rows>
    ) {
        var yesCount: Int = 0
        var noCount: Int = 0
        var naCount: Int = 0

        for (i in questionsListResponse.indices) {
            if (questionsListResponse.get(i).questions != null) {
                yesCount += questionsListResponse.get(i).questions!!.count { it.value == "Y" }
                noCount += questionsListResponse.get(i).questions!!.count { it.value == "N" }
                naCount += questionsListResponse.get(i).questions!!.count { it.value == "NA" }
            }


        }

        activityPlanogramEvaluationBinding.noCount.setText(noCount.toString())
        activityPlanogramEvaluationBinding.naCount.setText(naCount.toString())
        activityPlanogramEvaluationBinding.yesCount.setText(yesCount.toString())


    }

    @SuppressLint("SetTextI18n")
    override fun checkAreasToFocusOn(
        questionsList: ArrayList<PlanogramSurveyQuestionsListResponse.Questions>,
        categoryPosition: Int,
    ) {
        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(categoryPosition).questions =
            questionsList
        val areasList = ArrayList<String>()
        for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!) {
            if (i.type.equals("category")) {
                for (j in i.questions!!) {
                    if (j.value.equals("N")) {
                        if (areasList.size > 0) {
                            if (!areasList.contains(j.name))
                                areasList.add(j.name!!)
                        } else {
                            areasList.add(j.name!!)
                        }
                    }
                }
            }

        }

        var allScoresHaving = true
        for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!) {
            for (j in i.questions!!) {
                if (j.value.isNullOrEmpty()) {
                    allScoresHaving = false
                }
            }
        }
        if (allScoresHaving) {
            activityPlanogramEvaluationBinding.startSurveyButton.background =
                resources.getDrawable(R.drawable.greenbackgrounf_forstartsurvey)
        } else {
            activityPlanogramEvaluationBinding.startSurveyButton.background =
                resources.getDrawable(R.drawable.ashbackground_for_submit_planogram)
        }
//        caluclateCategoryScore()
//        caluclateOverAllScore()
        areasToFocusOnList = areasList
        activityPlanogramEvaluationBinding.areasToFocusText.text =
            "Areas to focus on (" + areasList.size + ")"


    }

    fun caluclateOverAllScore() {
        var totalCategories: Float = 0f
        var havingAllCategoryScore = true
        for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!) {
//            if (i.categoryType!!.equals("Category")) {
            totalCategories++
//            }
        }
        for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!) {
            var valueOfTotalCategories: Float = 0f
            if (i.categoryScore == 0f) {
                havingAllCategoryScore = false
                break
            }
            if (havingAllCategoryScore) {
                valueOfTotalCategories = valueOfTotalCategories + i.categoryScore
                var categoryScore = (valueOfTotalCategories / totalCategories)
                planogramSurveyQuestionsListResponses!!.overAllScore = categoryScore.toFloat()

            }
            var overAllScore =
                ((planogramSurveyQuestionsListResponses!!.overAllScore + i.diaperPodiumScore + i.valueDealsBinScore + i.chillerScore + i.offersGondolaScore + i.peghooksDisplayScore + i.postersScore) / totalCategories)

            planogramSurveyQuestionsListResponses!!.overAllScore = overAllScore
            activityPlanogramEvaluationBinding.overAllPercentage.setText(
                "OVERALL SCORE" + " : " + String.format(
                    Locale.US,
                    "%.2f",
                    overAllScore
                )
            )
        }
    }

    fun caluclateCategoryScore() {
        for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!) {
            if (i.type.equals("category")) {
                var totalSubCategories: Float = 0f
                var totalYes: Float = 0f
                var allValuesUpdated = true
                for (j in i.questions!!) {
                    if (j.value.isNullOrEmpty()) {
                        allValuesUpdated = false
                        break
                    }
                }
                if (allValuesUpdated) {
                    for (j in i.questions!!) {
                        if (!j.value.equals("NA")) {
                            totalSubCategories++
                        }
                        if (j.value.equals("Y")) {
                            totalYes++
                        }
                    }
                    var categoryPercentage = (totalYes / totalSubCategories) * 100
                    i.categoryScore = categoryPercentage.toFloat()
                }
            } else if (i.type.equals("diaper_podium")) {
                var totalSubCategories: Float = 0f
                var totalYes: Float = 0f
                var allValuesUpdated = true
                for (j in i.questions!!) {
                    if (j.value.isNullOrEmpty()) {
                        allValuesUpdated = false
                        break
                    }
                }
                if (allValuesUpdated) {
                    for (j in i.questions!!) {
                        if (!j.value.equals("NA")) {
                            totalSubCategories++
                        }
                        if (j.value.equals("Y")) {
                            totalYes++
                        }
                    }
                    var categoryPercentage = (totalYes / totalSubCategories) * 100
                    i.diaperPodiumScore = categoryPercentage.toFloat()
                }
            } else if (i.type.equals("value_deals_bin")) {
                var totalSubCategories: Float = 0f
                var totalYes: Float = 0f
                var allValuesUpdated = true
                for (j in i.questions!!) {
                    if (j.value.isNullOrEmpty()) {
                        allValuesUpdated = false
                        break
                    }
                }
                if (allValuesUpdated) {
                    for (j in i.questions!!) {
                        if (!j.value.equals("NA")) {
                            totalSubCategories++
                        }
                        if (j.value.equals("Y")) {
                            totalYes++
                        }
                    }
                    var categoryPercentage = (totalYes / totalSubCategories) * 100
                    i.valueDealsBinScore = categoryPercentage.toFloat()
                }
            } else if (i.type.equals("posters")) {
                var totalSubCategories: Float = 0f
                var totalYes: Float = 0f
                var allValuesUpdated = true
                for (j in i.questions!!) {
                    if (j.value.isNullOrEmpty()) {
                        allValuesUpdated = false
                        break
                    }
                }
                if (allValuesUpdated) {
                    for (j in i.questions!!) {
                        if (!j.value.equals("NA")) {
                            totalSubCategories++
                        }
                        if (j.value.equals("Y")) {
                            totalYes++
                        }
                    }
                    var categoryPercentage = (totalYes / totalSubCategories) * 100
                    i.postersScore = categoryPercentage.toFloat()
                }
            } else if (i.type.equals("peghooks_display")) {
                var totalSubCategories: Float = 0f
                var totalYes: Float = 0f
                var allValuesUpdated = true
                for (j in i.questions!!) {
                    if (j.value.isNullOrEmpty()) {
                        allValuesUpdated = false
                        break
                    }
                }
                if (allValuesUpdated) {
                    for (j in i.questions!!) {
                        if (!j.value.equals("NA")) {
                            totalSubCategories++
                        }
                        if (j.value.equals("Y")) {
                            totalYes++
                        }
                    }
                    var categoryPercentage = (totalYes / totalSubCategories) * 100
                    i.peghooksDisplayScore = categoryPercentage.toFloat()
                }
            } else if (i.type.equals("offers_gondola")) {
                var totalSubCategories: Float = 0f
                var totalYes: Float = 0f
                var allValuesUpdated = true
                for (j in i.questions!!) {
                    if (j.value.isNullOrEmpty()) {
                        allValuesUpdated = false
                        break
                    }
                }
                if (allValuesUpdated) {
                    for (j in i.questions!!) {
                        if (!j.value.equals("NA")) {
                            totalSubCategories++
                        }
                        if (j.value.equals("Y")) {
                            totalYes++
                        }
                    }
                    var categoryPercentage = (totalYes / totalSubCategories) * 100
                    i.offersGondolaScore = categoryPercentage.toFloat()
                }
            } else if (i.type.equals("chiller")) {
                var totalSubCategories: Float = 0f
                var totalYes: Float = 0f
                var allValuesUpdated = true
                for (j in i.questions!!) {
                    if (j.value.isNullOrEmpty()) {
                        allValuesUpdated = false
                        break
                    }
                }
                if (allValuesUpdated) {
                    for (j in i.questions!!) {
                        if (!j.value.equals("NA")) {
                            totalSubCategories++
                        }
                        if (j.value.equals("Y")) {
                            totalYes++
                        }
                    }
                    var categoryPercentage = (totalYes / totalSubCategories) * 100
                    i.chillerScore = categoryPercentage.toFloat()
                }
            }


        }
    }

    @SuppressLint("SetTextI18n")
    override fun checkCategoriesToFocusOn(
        questionsList: ArrayList<PlanogramSurveyQuestionsListResponse.Questions>,
        categoryPosition: Int,
    ) {
        planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!.get(categoryPosition).questions =
            questionsList
        val categoriesList = ArrayList<String>()
        for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!) {
            for (j in i.questions!!) {
                if (j.value.equals("N")) {
                    if (j.categoryName != null) {
                        if (categoriesList.size > 0) {
                            if (!categoriesList.contains(j.categoryName))
                                categoriesList.add(j.categoryName!!)
                        } else {
                            categoriesList.add(j.categoryName!!)
                        }
                    }

                }
            }

        }
        categoriesToFocusOnList = categoriesList
        activityPlanogramEvaluationBinding.categoresToFocusOnText.text =
            "Categories to focus on (" + categoriesList.size + ")"

    }

    override fun onSuccessSaveUpdateApi(saveUpdateRequestJsonResponse: PlanogramSaveUpdateResponse?) {


        Toast.makeText(
            applicationContext,
            "" + saveUpdateRequestJsonResponse!!.message,
            Toast.LENGTH_SHORT
        ).show()
        Utlis.hideLoading()
        dialogSubmit = Dialog(this)
        dialogSubmit.setContentView(R.layout.success_dialog_plano) // Set the content view first
        dialogSubmit.setCancelable(true)
        dialogSubmit.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val close = dialogSubmit.findViewById<ImageView>(R.id.close_dialog_save_plano)
        val ok = dialogSubmit.findViewById<LinearLayout>(R.id.ok_button_plano)

        close?.setOnClickListener {
            dialogSubmit.dismiss()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        ok?.setOnClickListener {
            dialogSubmit.dismiss()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        dialogSubmit.show()


    }

    override fun onFailureSaveUpdateApi(saveUpdateRequestJsonResponse: PlanogramSaveUpdateResponse?) {
        Utlis.hideLoading()
    }

    override fun caluclateScore() {
        caluclateCategoryScore()
        caluclateOverAllScore()
    }

    override fun onTipDismissed(view: View?, anchorViewId: Int, byUser: Boolean) {
    }
}

