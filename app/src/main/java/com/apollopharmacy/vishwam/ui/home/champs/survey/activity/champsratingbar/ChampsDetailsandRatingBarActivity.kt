package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champsratingbar

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityChampsDetailsandRatingBarBinding
import com.apollopharmacy.vishwam.dialog.AttachPhotoDialog
import com.apollopharmacy.vishwam.dialog.ChampsSurveyDialog
import java.security.AccessController.getContext

class ChampsDetailsandRatingBarActivity : AppCompatActivity(), ChampsDetailsandRatingBarCallBack {

    private lateinit var activityChampsDetailsandRatingBarBinding: ActivityChampsDetailsandRatingBarBinding
    private lateinit var champsDetailsAndRatingBarViewModel: ChampsDetailsAndRatingBarViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_champs_detailsand_rating_bar)

        activityChampsDetailsandRatingBarBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_champs_detailsand_rating_bar

        )
        champsDetailsAndRatingBarViewModel = ViewModelProvider(this)[ChampsDetailsAndRatingBarViewModel::class.java]
        setUp()
        checkListeners()
    }



    private fun setUp() {
        activityChampsDetailsandRatingBarBinding.callback=this
    }

    override fun onClickBack() {
        super.onBackPressed()
    }

    override fun onClickSaveDraft() {

    }

    override fun onClickNext() {
//        startActivity(Intent(getContext(), SurveyListActivity::class.java))
    }

    override fun onClickImageUpload() {
        AttachPhotoDialog().show(supportFragmentManager, "")
    }

    private fun checkListeners() {
        activityChampsDetailsandRatingBarBinding.seekbar1.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Toast.makeText(applicationContext,
                    "Value: " + getConvertedValue(progress),
                    Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        activityChampsDetailsandRatingBarBinding.seekbar2.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Toast.makeText(applicationContext,
                    "Value: " + getConvertedValue(progress),
                    Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        activityChampsDetailsandRatingBarBinding.seekbar3.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Toast.makeText(applicationContext,
                    "Value: " + getConvertedValue(progress),
                    Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        activityChampsDetailsandRatingBarBinding.seekbar4.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Toast.makeText(applicationContext,
                    "Value: " + getConvertedValue(progress),
                    Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun getConvertedValue(intVal: Int): Float {
        var floatVal = 0.0f
        floatVal = .5f * intVal
        return floatVal
    }
}