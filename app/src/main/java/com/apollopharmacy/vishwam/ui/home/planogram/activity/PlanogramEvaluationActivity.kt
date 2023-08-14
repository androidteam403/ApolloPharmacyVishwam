package com.apollopharmacy.vishwam.ui.home.planogram.activity

import android.app.Dialog
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityPlanogramEvaluationBinding
import com.apollopharmacy.vishwam.databinding.AreasToFocusonDialogBinding
import com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter.AreasToFocusOnAdapter
import com.apollopharmacy.vishwam.ui.home.planogram.activity.adapter.PlanogramCateoryAdapter
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramCatList
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.PlanogramSurveyQuestionsListResponse
import com.apollopharmacy.vishwam.ui.rider.service.NetworkUtils
import com.apollopharmacy.vishwam.util.Utlis
import com.tomergoldst.tooltips.ToolTipsManager

class PlanogramEvaluationActivity : AppCompatActivity(), PlanogramActivityCallback,
    ToolTipsManager.TipListener {
    lateinit var activityPlanogramEvaluationBinding: ActivityPlanogramEvaluationBinding
    lateinit var planogramActivityViewModel: PlanogramActivityViewModel
    var planogramCategoryAdapter: PlanogramCateoryAdapter? = null
    private lateinit var dialogSubmit: Dialog
    var areasToFocusOnAdapter: AreasToFocusOnAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPlanogramEvaluationBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_planogram_evaluation
        )
        planogramActivityViewModel = ViewModelProvider(this)[PlanogramActivityViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        activityPlanogramEvaluationBinding.callback = this
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

    override fun onClickSubmit() {
        dialogSubmit = Dialog(this)
        val close = dialogSubmit.findViewById<ImageView>(R.id.close_dialog_save_plano)
        val ok = dialogSubmit.findViewById<LinearLayout>(R.id.ok_button_plano)
        dialogSubmit.setContentView(R.layout.success_dialog_plano)
        dialogSubmit.setCancelable(true)
//        close.setOnClickListener {
//            dialogSubmit.dismiss()
//            val intent = Intent()
//            setResult(Activity.RESULT_OK, intent)
//            finish()
//        }
//        ok.setOnClickListener {
//            dialogSubmit.dismiss()
//            val intent = Intent()
//            setResult(Activity.RESULT_OK, intent)
//            finish()
//        }
        dialogSubmit.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogSubmit.show()
    }

    override fun onClickAreasToFocusOn() {
        showDialogPlano("areasToFocusOn")

    }

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
        var areasList = ArrayList<String>()
        if (focusOnName.equals("areasToFocusOn")) {
            areasToFocusOnBinding.name.setText("Areas to Focus on")
            areasList!!.add("Left to right / Front to back")
            areasList!!.add("FIFO")
            areasList!!.add("Condition tags / Other talkers")
            areasToFocusOnAdapter =
                AreasToFocusOnAdapter(applicationContext, areasList, focusOnName)
            val layoutManager = LinearLayoutManager(this)
            areasToFocusOnBinding.areasToFocusOnRecyclerview.setLayoutManager(
                layoutManager
            )
            areasToFocusOnBinding.areasToFocusOnRecyclerview.setAdapter(
                areasToFocusOnAdapter
            )

        } else {
            areasToFocusOnBinding.name.setText("Categories to Focus on")
            areasList!!.add("Oral care")
            areasList!!.add("Men's grooming ")
            areasList!!.add("Immunity")
            areasList!!.add("H&B / Wellness")
            areasList!!.add("Health food and drinks")
            areasList!!.add("Supliments")
            areasToFocusOnAdapter =
                AreasToFocusOnAdapter(applicationContext, areasList, focusOnName)
            val layoutManager = GridLayoutManager(this, 2)
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
        showDialogPlano("categoriesToFocusOn")
    }

    override fun showTillTop() {

    }

    override fun onSuccessPlanogramSurveyQuestionsListApiCall(planogramSurveyQuestionsListResponse: PlanogramSurveyQuestionsListResponse) {
        Utlis.hideLoading()
        if (planogramSurveyQuestionsListResponse != null
            && planogramSurveyQuestionsListResponse.data != null
            && planogramSurveyQuestionsListResponse.data!!.listData != null
            && planogramSurveyQuestionsListResponse.data!!.listData!!.rows != null
            && planogramSurveyQuestionsListResponse.data!!.listData!!.rows!!.size > 0
        ) {
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

    override fun onTipDismissed(view: View?, anchorViewId: Int, byUser: Boolean) {
        TODO("Not yet implemented")
    }
}

