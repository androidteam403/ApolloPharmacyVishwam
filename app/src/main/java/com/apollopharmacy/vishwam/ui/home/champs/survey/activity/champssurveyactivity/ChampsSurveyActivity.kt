package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurveyactivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityChampsSurveyBinding

class ChampsSurveyActivity : AppCompatActivity(),ChampsSurveyCallBack {

    private lateinit var activityChampsSurveyBinding: ActivityChampsSurveyBinding
    private lateinit var champsSurveyViewModel: ChampsSurveyViewModel


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

    }

    private fun checkListeners(){
        activityChampsSurveyBinding.enterTextTechnicalEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var charLeft:String = (250-charSequence.length).toString()
                activityChampsSurveyBinding.charLeftTechnical.setText(charLeft)
            }
            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterSoftSkillsEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var charLeft:String = (250-charSequence.length).toString()
                activityChampsSurveyBinding.charLeftSoftskills.setText(charLeft)
            }
            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterOtherTrainingEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var charLeft:String = (250-charSequence.length).toString()
                activityChampsSurveyBinding.charLeftOtherTraining.setText(charLeft)
            }
            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var charLeft:String = (750-charSequence.length).toString()
                activityChampsSurveyBinding.charLeftIssuestoBeResolved.setText(charLeft)
            }
            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.technicalCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
               activityChampsSurveyBinding.technicalEdittext.visibility=View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutTechnical.visibility=View.VISIBLE
            }
        }

        activityChampsSurveyBinding.softskillsCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityChampsSurveyBinding.softSkillsEdittext.visibility=View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutSoftskills.visibility=View.VISIBLE
            }
        }

        activityChampsSurveyBinding.otherTrainingCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityChampsSurveyBinding.otherTrainingEdittext.visibility=View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutOtherrTraining.visibility=View.VISIBLE
            }
        }
    }

    override fun onClickBack() {
       super.onBackPressed()
    }
}