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
import com.apollopharmacy.vishwam.databinding.ActivityPlanogramEvaluationBinding
import com.apollopharmacy.vishwam.databinding.AreasToFocusonDialogBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.model.SaveUpdateRequest
import com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter.AreasToFocusOnAdapter
import com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter.PlanogramCateoryAdapter
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramCatList
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramDetailsListResponse
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSaveUpdateRequest
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSaveUpdateResponse
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils
import com.apollopharmacy.vishwam.util.Utlis
import com.tomergoldst.tooltips.ToolTipsManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlanogramEvaluationActivity : AppCompatActivity(), PlanogramActivityCallback,
    ToolTipsManager.TipListener {
    lateinit var activityPlanogramEvaluationBinding: ActivityPlanogramEvaluationBinding
    lateinit var planogramActivityViewModel: PlanogramActivityViewModel
    var planogramCategoryAdapter: PlanogramCateoryAdapter? = null
    private lateinit var dialogSubmit: Dialog
    var areasToFocusOnAdapter: AreasToFocusOnAdapter? = null
    var areasToFocusOnList: ArrayList<String>? = null
    var categoriesToFocusOnList: ArrayList<String>? = null
    var uid:String=""

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
        activityPlanogramEvaluationBinding.callback = this
        activityPlanogramEvaluationBinding.areasToFocusText.text = "Areas to focus on (" + 0 + ")"
        activityPlanogramEvaluationBinding.categoresToFocusOnText.text =
            "Categories to focus on (" + 0 + ")"

        if (intent!=null){
            uid= intent.getStringExtra("uid")!!
            if (NetworkUtils.isNetworkConnected(this)) {
                Utlis.showLoading(this@PlanogramEvaluationActivity)
                planogramActivityViewModel.planogramDetailListApi(this,uid)
            } else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        if (NetworkUtils.isNetworkConnected(this)) {
            Utlis.showLoading(this@PlanogramEvaluationActivity)
            planogramActivityViewModel.planogramSurveyQuestionsListApi(this)
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show()
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
            activityPlanogramEvaluationBinding.startSurveyButton.background= resources.getDrawable(R.drawable.greenbackgrounf_forstartsurvey)
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
            planogramRequest.siteId = activityPlanogramEvaluationBinding.siteId.text.toString()
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

            for (i in planogramSurveyQuestionsListResponses!!.data!!.listData!!.rows!!) {
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
                if (i.type.equals("category")) {
                    var planogramSurvey = planogramRequest.PlanogramSurvey()
                    planogramSurvey.score = i.categoryScore.toString()
                    for (j in i.questions!!) {
                        var categoryTypeUid = planogramSurvey.CategoryType()
                        var uid = i.name
                        categoryTypeUid.uid = uid
                        planogramSurvey.categoryType = categoryTypeUid
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
                    planogramRequest.planogramSurvey = planogramSurveyList
                } else if (i.type.equals("chiller")) {
                    var planogramChiller = planogramRequest.Chiller()
                    planogramChiller.name = i.name

                    for (j in i.questions!!) {
                        var type = planogramChiller.Type__1()
                        var uidType = i.categoryType!!.uid
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
                } else if (i.type.equals("diaper_podium")) {
                    var planogramDiaperPodium = planogramRequest.DiaperPodium()
                    planogramDiaperPodium.name = i.name
                    for (j in i.questions!!) {
                        var type = planogramDiaperPodium.Type__2()
                        var uidType = i.categoryType!!.uid
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
                } else if (i.type.equals("offers_gondola")) {
                    var planogramOffersGandola = planogramRequest.OffersGondola()
                    planogramOffersGandola.name = i.name
                    for (j in i.questions!!) {
                        var type = planogramOffersGandola.Type__3()
                        var uidType = i.categoryType!!.uid
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
                } else if (i.type.equals("peghooks_display")) {
                    var planogramPeghooksDisplay = planogramRequest.PeghooksDisplay()
                    planogramPeghooksDisplay.name = i.name
                    for (j in i.questions!!) {
                        var type = planogramPeghooksDisplay.Type__4()
                        var uidType = i.categoryType!!.uid
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
                } else if (i.type.equals("posters")) {
                    var planogramPosters = planogramRequest.Poster()
                    planogramPosters.name = i.name
                    for (j in i.questions!!) {
                        var type = planogramPosters.Type__5()
                        var uidType = i.categoryType!!.uid
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
                } else if (i.type.equals("value_deals_bin")) {
                    var planogramValueDealsBin = planogramRequest.ValueDealsBin()
                    planogramValueDealsBin.name = i.name
                    for (j in i.questions!!) {
                        var type = planogramValueDealsBin.Type__6()
                        var uidType = i.categoryType!!.uid
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
            Utlis.showLoading(this)

            planogramActivityViewModel.planogramSaveUpdateApi(this, planogramRequest)
        } else {
            activityPlanogramEvaluationBinding.startSurveyButton.background= resources.getDrawable(R.drawable.ashbackground_for_submit_planogram)
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

    }

    var planogramSurveyQuestionsListResponses: PlanogramSurveyQuestionsListResponse? = null
    override fun onSuccessPlanogramSurveyQuestionsListApiCall(planogramSurveyQuestionsListResponse: PlanogramSurveyQuestionsListResponse) {
        Utlis.hideLoading()
        if (planogramSurveyQuestionsListResponse != null
            && planogramSurveyQuestionsListResponse.data != null
            && planogramSurveyQuestionsListResponse.data!!.listData != null
            && planogramSurveyQuestionsListResponse.data!!.listData!!.rows != null
            && planogramSurveyQuestionsListResponse.data!!.listData!!.rows!!.size > 0
        ) {
            planogramSurveyQuestionsListResponses = planogramSurveyQuestionsListResponse
            val data: MutableList<PlanogramCatList.CatList> = ArrayList()
            planogramCategoryAdapter =
                PlanogramCateoryAdapter(
                    planogramSurveyQuestionsListResponse.data!!.listData!!.rows!!,
                    applicationContext,
                    this,
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
                    if (j.value.equals("NO")) {
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
            activityPlanogramEvaluationBinding.startSurveyButton.background= resources.getDrawable(R.drawable.greenbackgrounf_forstartsurvey)
        }else{
            activityPlanogramEvaluationBinding.startSurveyButton.background= resources.getDrawable(R.drawable.ashbackground_for_submit_planogram)
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
            var overAllScore =  ((planogramSurveyQuestionsListResponses!!.overAllScore + i.diaperPodiumScore + i.valueDealsBinScore + i.chillerScore + i.offersGondolaScore + i.peghooksDisplayScore + i.postersScore) / totalCategories)

            planogramSurveyQuestionsListResponses!!.overAllScore = overAllScore
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
                        if (j.value.equals("YES")) {
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
                        if (j.value.equals("YES")) {
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
                        if (j.value.equals("YES")) {
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
                        if (j.value.equals("YES")) {
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
                        if (j.value.equals("YES")) {
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
                        if (j.value.equals("YES")) {
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
                        if (j.value.equals("YES")) {
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
                if (j.value.equals("NO")) {
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
        val close = dialogSubmit.findViewById<ImageView>(R.id.close_dialog_save_plano)
        val ok = dialogSubmit.findViewById<LinearLayout>(R.id.ok_button_plano)
        dialogSubmit.setContentView(R.layout.success_dialog_plano)
        dialogSubmit.setCancelable(true)
        close.setOnClickListener {
            dialogSubmit.dismiss()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        ok.setOnClickListener {
            dialogSubmit.dismiss()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        dialogSubmit.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

