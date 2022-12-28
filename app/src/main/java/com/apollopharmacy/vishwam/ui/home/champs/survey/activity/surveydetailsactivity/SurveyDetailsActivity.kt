package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetailsactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityStartSurvey2Binding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurveyactivity.ChampsSurveyActivity

class SurveyDetailsActivity : AppCompatActivity(),SurveyDetailsCallback {

    private lateinit var activityStartSurvey2Binding: ActivityStartSurvey2Binding
    private lateinit var surveyDetailsViewModel: SurveyDetailsViewModel
//    private lateinit var seekbar : SeekBar

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_start_survey2)

        activityStartSurvey2Binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_start_survey2

        )
        surveyDetailsViewModel = ViewModelProvider(this)[SurveyDetailsViewModel::class.java]

//        activityStartSurvey2Binding.seekbar.thumb.mutate().alpha=0
//        activityStartSurvey2Binding.seekbar.incrementProgressBy(0.1f)
//        activityStartSurvey2Binding.seekbar.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(
//                seek: SeekBar,
//                progress: Int, fromUser: Boolean,
//            ) {
//                activityStartSurvey2Binding.seekbarValue.setText(getConvertedValue(seek.progress).toString())
//            }
//
//            override fun onStartTrackingTouch(seek: SeekBar) {
//                // write custom code for progress is started
//            }
//
//            @SuppressLint("WrongViewCast")
//            override fun onStopTrackingTouch(seek: SeekBar) {
//                // write custom code for progress is stopped
//
////                Toast.makeText(applicationContext,
////                    "Progress is: " + seek.progress + "%",
////                    Toast.LENGTH_SHORT).show()
//
//                Toast.makeText(applicationContext,
//                    "Value: " + getConvertedValue(seek.progress),
//                    Toast.LENGTH_SHORT).show();
//                activityStartSurvey2Binding.seekbarValue.setText(getConvertedValue(seek.progress).toString())
//
//            }
//        })
        setUp()
    }

    fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .1f * intVal
        return floatVal
    }

    private fun setUp() {
        activityStartSurvey2Binding.callback = this
    }

    override fun onClickBack() {
        super.onBackPressed()
    }


    override fun onClickStartChampsSurvey() {
        val intent = Intent(context, ChampsSurveyActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}