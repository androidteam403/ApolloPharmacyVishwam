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
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityStartSurvey2Binding
import com.apollopharmacy.vishwam.ui.home.model.GetEmailAddressModelResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.champssurvey.ChampsSurveyActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.adapter.EmailAddressAdapter
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.adapter.EmailAddressCCAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.fragment.model.PendingAndApproved
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import java.util.*

class SurveyDetailsActivity : AppCompatActivity(), SurveyDetailsCallback {
    private var getStoreWiseDetails: GetStoreWiseDetailsModelResponse? = null
    private lateinit var activityStartSurvey2Binding: ActivityStartSurvey2Binding
    private lateinit var surveyDetailsViewModel: SurveyDetailsViewModel
    private var adapterRec: EmailAddressAdapter? = null
    private var adapterCC: EmailAddressCCAdapter? = null
    val surveyRecDetailsList= ArrayList<String>()
    val surveyCCDetailsList = ArrayList<String>()
    private var storeId:String =""
    private var storeCity:String =""
    private var address:String=""
    var siteName:String?=""

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

        getStoreWiseDetails =
            intent.getSerializableExtra("getStoreWiseDetailsResponses") as GetStoreWiseDetailsModelResponse?
        address = intent.getStringExtra("address")!!
        storeId = intent.getStringExtra("storeId")!!
        siteName= intent.getStringExtra("siteName")
        storeCity = intent.getStringExtra("storeCity")!!
        if(getStoreWiseDetails!=null && getStoreWiseDetails!!.storeWiseDetails!=null){
            activityStartSurvey2Binding.trainer.text=getStoreWiseDetails!!.storeWiseDetails.trainerEmail
            activityStartSurvey2Binding.regionalHead.text=getStoreWiseDetails!!.storeWiseDetails.reagionalHeadEmail
            activityStartSurvey2Binding.executive.text=getStoreWiseDetails!!.storeWiseDetails.executiveEmail
            activityStartSurvey2Binding.manager.text=getStoreWiseDetails!!.storeWiseDetails.managerEmail
        }

        activityStartSurvey2Binding.storeId.text=storeId
        activityStartSurvey2Binding.address.text=siteName


        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            surveyDetailsViewModel.getEmailDetailsChamps(this);

        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            surveyDetailsViewModel.getEmailDetailsChampsApi(this, "CC");
//
//        } else {
//            Toast.makeText(
//                context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }


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
        intent.putExtra("getStoreWiseDetails", getStoreWiseDetails)
        intent.putExtra("address", address)
        intent.putExtra("storeId", storeId)
        intent.putExtra("siteName", siteName)
        intent.putExtra("storeCity", storeCity)
        intent.putStringArrayListExtra("surveyRecDetailsList", surveyRecDetailsList)
        intent.putStringArrayListExtra("surveyCCDetailsList", surveyCCDetailsList)
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

    override fun onSuccessgetEmailDetails(getEmailAddressResponse: GetEmailAddressModelResponse) {
        if(getEmailAddressResponse!=null && getEmailAddressResponse.emailDetails!=null && getEmailAddressResponse.emailDetails!!.size!=null){
            for(i in getEmailAddressResponse.emailDetails!!.indices){
                surveyRecDetailsList.add(getEmailAddressResponse.emailDetails!!.get(i).email!!)
            }
            adapterRec = EmailAddressAdapter(surveyRecDetailsList, applicationContext, this)
            activityStartSurvey2Binding.emailRecRecyclerView.setLayoutManager(LinearLayoutManager(this))
            activityStartSurvey2Binding.emailRecRecyclerView.setAdapter(adapterRec)
        }
        onSuccessgetEmailDetailsCC(getEmailAddressResponse)
        Utlis.hideLoading()

//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            surveyDetailsViewModel.getEmailDetailsChampsApi(this, "RECIPIENTS");
//
//        } else {
//            Toast.makeText(
//                context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }




    }

    override fun onFailuregetEmailDetails(value: GetEmailAddressModelResponse) {
    Toast.makeText(context, ""+value.message, Toast.LENGTH_SHORT).show()
        Utlis.hideLoading()
//        if (NetworkUtil.isNetworkConnected(this)) {
//            Utlis.showLoading(this)
//            surveyDetailsViewModel.getEmailDetailsChampsApi(this, "RECIPIENTS");
//
//        } else {
//            Toast.makeText(
//                context,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }
    }

    override fun onSuccessgetEmailDetailsCC(getEmailAddressResponse: GetEmailAddressModelResponse) {
        if(getEmailAddressResponse!=null && getEmailAddressResponse.emailDetails!=null && getEmailAddressResponse.emailDetails!!.size!=null){
            for(i in getEmailAddressResponse.emailDetails!!.indices){
                surveyCCDetailsList.add(getEmailAddressResponse.emailDetails!!.get(i).email!!)
            }
            adapterCC = EmailAddressCCAdapter(surveyCCDetailsList, this, applicationContext)
            activityStartSurvey2Binding.emailCCRecyclerView.setLayoutManager(LinearLayoutManager(this))
            activityStartSurvey2Binding.emailCCRecyclerView.setAdapter(adapterCC)
            Utlis.hideLoading()
        }

    }


}