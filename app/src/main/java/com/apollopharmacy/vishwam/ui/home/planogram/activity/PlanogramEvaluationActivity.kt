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
import com.apollopharmacy.vishwam.ui.home.planogram.fragment.PlanogramViewModel
import com.tomergoldst.tooltips.ToolTip
import com.tomergoldst.tooltips.ToolTipsManager

class PlanogramEvaluationActivity : AppCompatActivity(), PlanogramActivityCallback , ToolTipsManager.TipListener {
    lateinit var activityPlanogramEvaluationBinding: ActivityPlanogramEvaluationBinding
    var planogramViewModel: PlanogramViewModel? = null
    var planogramCategoryAdapter: PlanogramCateoryAdapter? = null
    private lateinit var dialogSubmit: Dialog
    var areasToFocusOnAdapter: AreasToFocusOnAdapter? = null
    var toolTip: ToolTipsManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_planogram_evaluation)

        activityPlanogramEvaluationBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_planogram_evaluation

        )
        planogramViewModel = ViewModelProvider(this)[PlanogramViewModel::class.java]
        setUp()
    }

    private fun setUp() {
        activityPlanogramEvaluationBinding.callback = this

        val data: MutableList<PlanogramCatList.CatList> = ArrayList()
        data.add(PlanogramCatList.CatList("Baby Food", "#a77b54"))
        data.add(PlanogramCatList.CatList("Baby Care", "#a77b54"))
        data.add(PlanogramCatList.CatList("Women's Health", "#a77b54"))
        data.add(PlanogramCatList.CatList("Skin Care", "#bca931"))
        data.add(PlanogramCatList.CatList("Hair Care", "#bca931"))
        data.add(PlanogramCatList.CatList("Oral Care", "#bca931"))
        data.add(PlanogramCatList.CatList("Men's Grooming", "#bca931"))
        data.add(PlanogramCatList.CatList("Pain relief", "#00a99c"))
        data.add(PlanogramCatList.CatList("OTC", "#00a99c"))
        data.add(PlanogramCatList.CatList("Immunity", "#00a99c"))
        data.add(PlanogramCatList.CatList("Supports and Braces", "#00a99c"))
        toolTip = ToolTipsManager(this);
        planogramCategoryAdapter =
            PlanogramCateoryAdapter(applicationContext, data, this, toolTip!!, this)
        activityPlanogramEvaluationBinding.planogramCategoryRecyclerview.setLayoutManager(
            LinearLayoutManager(this)
        )
        activityPlanogramEvaluationBinding.planogramCategoryRecyclerview.setAdapter(
            planogramCategoryAdapter
        )
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

    override fun onTipDismissed(view: View?, anchorViewId: Int, byUser: Boolean) {
        TODO("Not yet implemented")
    }
}

