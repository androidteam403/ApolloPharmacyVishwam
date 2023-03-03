package com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityStartSurvey2Binding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.adapter.EmailAddressAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.adapter.EmailAddressCCAdapter
import com.apollopharmacy.vishwam.util.Utils
import java.util.*

class SurveyDetailsActivity : AppCompatActivity(), SurveyDetailsCallback {

    private lateinit var activityStartSurvey2Binding: ActivityStartSurvey2Binding
    private lateinit var surveyDetailsViewModel: SurveyDetailsViewModel
    private var adapterRec: EmailAddressAdapter? = null
    private var adapterCC: EmailAddressCCAdapter? = null
    val surveyRecDetailsList: MutableList<String> = ArrayList<String>()
    val surveyCCDetailsList: MutableList<String> = ArrayList<String>()

    //    private lateinit var seekbar : SeekBar
    private lateinit var dialog: Dialog

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


//        surveyRecDetailsList.add("kkr@apollopharmacy.org")
//        surveyCCDetailsList.add("kkabcr@apollopharmacy.org")


        adapterRec = EmailAddressAdapter(surveyRecDetailsList, applicationContext, this)
        activityStartSurvey2Binding.emailRecRecyclerView.setLayoutManager(LinearLayoutManager(this))
        activityStartSurvey2Binding.emailRecRecyclerView.setAdapter(adapterRec)

        adapterCC = EmailAddressCCAdapter(surveyCCDetailsList, this, applicationContext)
        activityStartSurvey2Binding.emailCCRecyclerView.setLayoutManager(LinearLayoutManager(this))
        activityStartSurvey2Binding.emailCCRecyclerView.setAdapter(adapterCC)

    }

    override fun onClickBack() {
        super.onBackPressed()
    }


    override fun onClickStartChampsSurvey() {
        val intent = Intent(context, ChampsSurveyActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
    override fun onClickPlusRec() {
        val email: String = activityStartSurvey2Binding.enterEmailEdittextRec.getText().toString().trim()
        if (isValidEmail(email))
        {
            surveyRecDetailsList.add(activityStartSurvey2Binding.enterEmailEdittextRec.text.toString())
            adapterRec!!.notifyDataSetChanged()
            activityStartSurvey2Binding.enterEmailEdittextRec.setText("")
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                "Please enter valid email address",
                Toast.LENGTH_SHORT).show();
            //or
        }


    }

    override fun deleteEmailAddressRec(emailAddressRec: String) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.delete_email_address)
        val close = dialog.findViewById<TextView>(R.id.no_btnEmail)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnEmail)
        ok.setOnClickListener {
            dialog.dismiss()
            surveyRecDetailsList.remove(emailAddressRec)
            adapterRec!!.notifyDataSetChanged()
//            for (i in siteDataList.indices) {
//                if (selectsiteId.equals(siteDataList.get(i).site)) {
//                    pos = i
//                }
//            }


        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    override fun deleteEmailAddressCC(emailAddressCC: String) {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.delete_email_address)
        val close = dialog.findViewById<TextView>(R.id.no_btnEmail)
        close.setOnClickListener {
            dialog.dismiss()
        }
        val ok = dialog.findViewById<TextView>(R.id.yes_btnEmail)
        ok.setOnClickListener {
            dialog.dismiss()
            surveyCCDetailsList.remove(emailAddressCC)
            adapterCC!!.notifyDataSetChanged()
//            for (i in siteDataList.indices) {
//                if (selectsiteId.equals(siteDataList.get(i).site)) {
//                    pos = i
//                }
//            }


        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    override fun onClickPlusCC() {
        val email: String = activityStartSurvey2Binding.enterEmailEdittextCC.getText().toString().trim()

        if (isValidEmail(email))
        {
            surveyCCDetailsList.add(activityStartSurvey2Binding.enterEmailEdittextCC.text.toString())
            adapterCC!!.notifyDataSetChanged()
            activityStartSurvey2Binding.enterEmailEdittextCC.setText("")
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                "Please enter valid email address",
                Toast.LENGTH_SHORT).show();
            //or
        }

    }
}