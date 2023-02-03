package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivitySurveyListBinding
import com.apollopharmacy.vishwam.databinding.DialogFilterChampsBinding
import com.apollopharmacy.vishwam.dialog.ChampsSiteDialog
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist.adapter.SurveyListAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist.model.SurveyDetailsModelClass
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcStoreList
import com.apollopharmacy.vishwam.ui.home.qcfail.model.UniqueStoreList
import org.apache.commons.lang3.StringUtils
import kotlin.collections.ArrayList

class SurveyListActivity : AppCompatActivity(), SurveyListCallback,
    ChampsSiteDialog.NewDialogSiteClickListner {

    private lateinit var activitySurveyListBinding: ActivitySurveyListBinding
    private lateinit var dialogFilterChampsBinding: DialogFilterChampsBinding
    private lateinit var surveyListViewModel: SurveyListViewModel
    var completedPendingSelected: Boolean = false
    var completedFilterSelected: Boolean = false
    var pendingFilterSelected: Boolean = false
    var isSelectedPending: Boolean = false
    var isSelectedCompleted: Boolean = false
    private var siteList = ArrayList<String>()
    private var siteId: String = ""
    public var storeStringList = ArrayList<String>()
    var uniqueStoreList = ArrayList<UniqueStoreList>()
    val surveyDetailsList: MutableList<SurveyDetailsModelClass> =
        ArrayList<SurveyDetailsModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_survey_list)
        activitySurveyListBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_survey_list

        )
        surveyListViewModel = ViewModelProvider(this)[SurveyListViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        activitySurveyListBinding.callback = this


        surveyDetailsList.add(SurveyDetailsModelClass(0,
            "16741",
            "Gandhi Nagar,Nellore",
            "Pending",
            "ramesh_p@apollopharmacy.org"))
        surveyDetailsList.add(SurveyDetailsModelClass(0,
            "16741",
            "Moti Nagar,Nellore",
            "Pending",
            "ramesh_p@apollopharmacy.org"))

        surveyDetailsList.add(SurveyDetailsModelClass(1,
            "16742",
            "Gandhi Nagar,Guntur",
            "Completed",
            "ramesh_p@apollopharmacy.org",
            "20 Dec,2022 - 10:45am"))
        surveyDetailsList.add(SurveyDetailsModelClass(1,
            "16742",
            "RamNagar,Guntur",
            "Completed",
            "ramesh_p@apollopharmacy.org",
            "19 Dec,2022 - 03:00am"))


        val adapter = SurveyListAdapter(surveyDetailsList, applicationContext)
        activitySurveyListBinding.surveyListRcv.setLayoutManager(LinearLayoutManager(this))
        activitySurveyListBinding.surveyListRcv.setAdapter(adapter)
    }

    override fun onClickBack() {
        super.onBackPressed()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClickFilter() {
        uniqueStoreList.clear()
        val filterDialog = Dialog(this, R.style.fadeinandoutcustomDialog)
        dialogFilterChampsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(applicationContext),
                R.layout.dialog_filter_champs,
                null,
                false)
        filterDialog.setContentView(dialogFilterChampsBinding.getRoot())
        filterDialog.setCancelable(false)
//        filtersList(dialogFilterChampsBinding)

        if (completedPendingSelected) {
            dialogFilterChampsBinding.pendingFilter.setBackground(applicationContext.getResources()
                .getDrawable(R.drawable.background_filter_selected))
            dialogFilterChampsBinding.completedFilter.setBackground(applicationContext.getResources()
                .getDrawable(R.drawable.background_filter_selected))
        } else if (completedFilterSelected) {
            dialogFilterChampsBinding.completedFilter.setBackground(applicationContext.getResources()
                .getDrawable(R.drawable.background_filter_selected))
        } else if (pendingFilterSelected) {
            dialogFilterChampsBinding.pendingFilter.setBackground(applicationContext.getResources()
                .getDrawable(R.drawable.background_filter_selected))
        } else {
            dialogFilterChampsBinding.completedFilter.setBackground(applicationContext.getResources()
                .getDrawable(R.drawable.background_filter))
            dialogFilterChampsBinding.pendingFilter.setBackground(applicationContext.getResources()
                .getDrawable(R.drawable.background_filter))

        }

        var unique = UniqueStoreList()
        var unique1 = UniqueStoreList()

        unique1.setSiteId("16741")
        unique.setSiteId("16742")

        uniqueStoreList.add(unique1)

        uniqueStoreList.add(unique)
        dialogFilterChampsBinding.siteIdSelect.setOnClickListener {

            ChampsSiteDialog().apply {
                arguments = ChampsSiteDialog().generateParsedData(uniqueStoreList)
            }.show(supportFragmentManager, "")

        }


        dialogFilterChampsBinding.pendingFilter.setOnClickListener {
            if (isSelectedPending) {
                dialogFilterChampsBinding.pendingFilter.setBackground(applicationContext.getResources()
                    .getDrawable(R.drawable.background_filter))
                isSelectedPending = false

            } else {
                dialogFilterChampsBinding.pendingFilter.setBackground(applicationContext.getResources()
                    .getDrawable(R.drawable.background_filter_selected))

                isSelectedPending = true

            }


        }



        dialogFilterChampsBinding.completedFilter.setOnClickListener {
            if (isSelectedCompleted) {
                dialogFilterChampsBinding.completedFilter.setBackground(applicationContext.getResources()
                    .getDrawable(R.drawable.background_filter))
                isSelectedCompleted = false

            } else {
                dialogFilterChampsBinding.completedFilter.setBackground(applicationContext.getResources()
                    .getDrawable(R.drawable.background_filter_selected))
                isSelectedCompleted = true

            }


        }
        dialogFilterChampsBinding.clear.setOnClickListener {
            completedFilterSelected = false
            pendingFilterSelected = false
            completedPendingSelected = false
            dialogFilterChampsBinding.completedFilter.setBackground(applicationContext.getResources()
                .getDrawable(R.drawable.background_filter))
            dialogFilterChampsBinding.pendingFilter.setBackground(applicationContext.getResources()
                .getDrawable(R.drawable.background_filter))
        }


        dialogFilterChampsBinding.filterCloseIcon.setOnClickListener { view ->
//            applyOrderFilters();
//            this.customerTypeFilterList = customerTypeFilterListTemp
//            this.orderTypeFilterList = orderTypeFilterListTemp
//            this.orderCategoryFilterList = orderCategoryFilterListTemp
//            this.paymentTypeFilterList = paymentTypeFilterListTemp
//            this.orderSourceFilterList = orderSourceFilterListTemp
//            this.stockAvailabilityFilterList = stockAvailabilityFilterListTemp
//            this.reverificationList = reverificationListTemp
            filterDialog.dismiss()
//            hideLoading()
        }
        dialogFilterChampsBinding.applyFilters.setOnClickListener { view ->
            surveyDetailsList.clear()

            if (isSelectedPending && isSelectedCompleted) {
                completedPendingSelected = true
                completedFilterSelected = false
                pendingFilterSelected = false
                surveyDetailsList.add(SurveyDetailsModelClass(0,
                    "16741",
                    "Gandhi Nagar,Nellore",
                    "Pending",
                    "ramesh_p@apollopharmacy.org"))
                surveyDetailsList.add(SurveyDetailsModelClass(0,
                    "16741",
                    "Moti Nagar,Nellore",
                    "Pending",
                    "ramesh_p@apollopharmacy.org"))

                surveyDetailsList.add(SurveyDetailsModelClass(1,
                    "16742",
                    "Gandhi Nagar,Guntur",
                    "Completed",
                    "ramesh_p@apollopharmacy.org",
                    "20 Dec,2022 - 10:45am"))
                surveyDetailsList.add(SurveyDetailsModelClass(1,
                    "16742",
                    "RamNagar,Guntur",
                    "Completed",
                    "ramesh_p@apollopharmacy.org",
                    "19 Dec,2022 - 03:00am"))

                filterDialog.dismiss()
                val adapter = SurveyListAdapter(surveyDetailsList, applicationContext)
                activitySurveyListBinding.surveyListRcv.setLayoutManager(LinearLayoutManager(this))
                activitySurveyListBinding.surveyListRcv.setAdapter(adapter)

            } else if (isSelectedPending) {
                pendingFilterSelected = true
                completedPendingSelected = false
                completedFilterSelected = false
                surveyDetailsList.add(SurveyDetailsModelClass(0,
                    "16741",
                    "Gandhi Nagar,Nellore",
                    "Pending",
                    "ramesh_p@apollopharmacy.org"))
                surveyDetailsList.add(SurveyDetailsModelClass(0,
                    "16741",
                    "Moti Nagar,Nellore",
                    "Pending",
                    "ramesh_p@apollopharmacy.org"))
                filterDialog.dismiss()
                val adapter = SurveyListAdapter(surveyDetailsList, applicationContext)
                activitySurveyListBinding.surveyListRcv.setLayoutManager(LinearLayoutManager(this))
                activitySurveyListBinding.surveyListRcv.setAdapter(adapter)

            } else if (isSelectedCompleted) {
                completedFilterSelected = true
                pendingFilterSelected = false
                completedPendingSelected = false
                surveyDetailsList.add(SurveyDetailsModelClass(1,
                    "16742",
                    "Gandhi Nagar,Guntur",
                    "Completed",
                    "ramesh_p@apollopharmacy.org",
                    "20 Dec,2022 - 10:45am"))
                surveyDetailsList.add(SurveyDetailsModelClass(1,
                    "16742",
                    "RamNagar,Guntur",
                    "Completed",
                    "ramesh_p@apollopharmacy.org",
                    "19 Dec,2022 - 03:00am"))

                filterDialog.dismiss()
                val adapter = SurveyListAdapter(surveyDetailsList, applicationContext)
                activitySurveyListBinding.surveyListRcv.setLayoutManager(LinearLayoutManager(this))
                activitySurveyListBinding.surveyListRcv.setAdapter(adapter)

            } else {
                completedFilterSelected = false
                pendingFilterSelected = false
                completedPendingSelected = false
                surveyDetailsList.add(SurveyDetailsModelClass(0,
                    "16741",
                    "Gandhi Nagar,Nellore",
                    "Pending",
                    "ramesh_p@apollopharmacy.org"))
                surveyDetailsList.add(SurveyDetailsModelClass(0,
                    "16741",
                    "Moti Nagar,Nellore",
                    "Pending",
                    "ramesh_p@apollopharmacy.org"))

                surveyDetailsList.add(SurveyDetailsModelClass(1,
                    "16742",
                    "Gandhi Nagar,Guntur",
                    "Completed",
                    "ramesh_p@apollopharmacy.org",
                    "20 Dec,2022 - 10:45am"))
                surveyDetailsList.add(SurveyDetailsModelClass(1,
                    "16742",
                    "RamNagar,Guntur",
                    "Completed",
                    "ramesh_p@apollopharmacy.org",
                    "19 Dec,2022 - 03:00am"))

                filterDialog.dismiss()
                val adapter = SurveyListAdapter(surveyDetailsList, applicationContext)
                activitySurveyListBinding.surveyListRcv.setLayoutManager(LinearLayoutManager(this))
                activitySurveyListBinding.surveyListRcv.setAdapter(adapter)
            }
        }

        filterDialog.show()
    }

    override fun onselectMultipleSitesStore(list: ArrayList<String>, position: Int) {
        dialogFilterChampsBinding.siteIdSelect.setText((StringUtils.substring(list.toString(),
            1,
            list.toString().length - 1)))
//        for (i in list.indices){
//            siteId +=list.get(i)+ ","
//        }

        siteId = dialogFilterChampsBinding.siteIdSelect.text.toString()

    }

    override fun selectSite(regionId: QcStoreList.Store) {

    }


}
