package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.R.drawable.progress_drawable_red
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityChampsSurveyBinding
import com.apollopharmacy.vishwam.dialog.ChampsSurveyDialog
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar.ChampsDetailsandRatingBarActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveylist.SurveyListActivity
import kotlinx.android.synthetic.main.activity_champs_survey.*

class ChampsSurveyActivity : AppCompatActivity(), ChampsSurveyCallBack {

    private lateinit var activityChampsSurveyBinding: ActivityChampsSurveyBinding
    private lateinit var champsSurveyViewModel: ChampsSurveyViewModel
    private var i = 0
    private val handler = Handler()

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
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkListeners() {
        activityChampsSurveyBinding.enterTextTechnicalEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var charLeft: String = (250 - charSequence.length).toString()
                activityChampsSurveyBinding.charLeftTechnical.setText(charLeft)
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterSoftSkillsEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var charLeft: String = (250 - charSequence.length).toString()
                activityChampsSurveyBinding.charLeftSoftskills.setText(charLeft)
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterOtherTrainingEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var charLeft: String = (250 - charSequence.length).toString()
                activityChampsSurveyBinding.charLeftOtherTraining.setText(charLeft)
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.enterIssuesTobeResolvedEdittext.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                var charLeft: String = (750 - charSequence.length).toString()
                activityChampsSurveyBinding.charLeftIssuestoBeResolved.setText(charLeft)
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

        activityChampsSurveyBinding.technicalCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityChampsSurveyBinding.technicalEdittext.visibility = View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutTechnical.visibility = View.VISIBLE
            }else{
                activityChampsSurveyBinding.technicalEdittext.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutTechnical.visibility = View.GONE
            }
        }

        activityChampsSurveyBinding.softskillsCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityChampsSurveyBinding.softSkillsEdittext.visibility = View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutSoftskills.visibility = View.VISIBLE
            }else{
                activityChampsSurveyBinding.softSkillsEdittext.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutSoftskills.visibility = View.GONE
            }
        }

        activityChampsSurveyBinding.otherTrainingCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                activityChampsSurveyBinding.otherTrainingEdittext.visibility = View.VISIBLE
                activityChampsSurveyBinding.charLeftLayoutOtherrTraining.visibility = View.VISIBLE
            } else{
                activityChampsSurveyBinding.otherTrainingEdittext.visibility = View.GONE
                activityChampsSurveyBinding.charLeftLayoutOtherrTraining.visibility = View.GONE
            }
        }

        activityChampsSurveyBinding.progressBar.setOnClickListener {
            // Before clicking the button the progress bar will invisible
            // so we have to change the visibility of the progress bar to visible
            // setting the progressbar visibility to visible
            progressBar.visibility = View.VISIBLE
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            i = progressBar.progress

            Thread(Runnable {
                // this loop will run until the value of i becomes 99
                if (i <= 5) {
//                    i += 1
    activityChampsSurveyBinding.progressBar.progressDrawable= getResources().getDrawable(R.drawable.progress_drawable_green)
                    // Update the progress bar and display the current value
                    handler.post(Runnable {
                        progressBar.progress = i
                        // setting current progress to the textview
//                        txtView!!.text = i.toString() + "/" + progressBar.max
                    })
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }else if(i>5 && i<=10){
                    activityChampsSurveyBinding.progressBar.progressDrawable= getResources().getDrawable(R.drawable.progress_bar_orange)
                }else if(i>10 && i==progressBar.max){
                    activityChampsSurveyBinding.progressBar.progressDrawable= getResources().getDrawable(R.drawable.progress_drawable_green)
                }

                // setting the visibility of the progressbar to invisible
                // or you can use View.GONE instead of invisible
                // View.GONE will remove the progressbar
//                progressBar.visibility = View.INVISIBLE

            }).start()
        }
    }

    override fun onClickBack() {
        super.onBackPressed()
    }

    override fun onClickCleanliness() {
        val intent = Intent(context, ChampsDetailsandRatingBarActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickSubmit() {
        ChampsSurveyDialog().show(supportFragmentManager, "")
    }

    override fun onClickSaveDraft() {
        val intent = Intent(context, SurveyListActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}