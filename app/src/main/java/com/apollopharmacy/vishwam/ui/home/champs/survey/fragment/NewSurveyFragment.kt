package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.FragmentChampsSurveyBinding
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.adapter.GetStoreDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectChampsSiteIDActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading

class NewSurveyFragment : AppCompatActivity(), NewSurveyCallback {
    var fragmentChampsSurveyBinding: FragmentChampsSurveyBinding? = null
    var newSurveyViewModel: NewSurveyViewModel? = null
    var getStoreDetailsAdapter: GetStoreDetailsAdapter? = null
    var getSiteDetails: GetStoreWiseDetailsModelResponse? = null
    var storeId: String? = ""
    var address: String? = ""
    var siteName: String? = ""
    var siteCity: String? = ""
    var region: String? = ""
    var isSiteIdEmpty: Boolean = false
    var isNewSurveyCreated = false
    var status = ""
//    var siteId: String? = ""

//    override val layoutRes: Int
//        get() = R.layout.fragment_champs_survey
//
//    override fun retrieveViewModel(): NewSurveyViewModel {
//        return ViewModelProvider(this).get(NewSurveyViewModel::class.java)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_champs_detailsand_rating_bar)

        fragmentChampsSurveyBinding = DataBindingUtil.setContentView(
            this,
            R.layout.fragment_champs_survey

        )
        newSurveyViewModel =
            ViewModelProvider(this)[NewSurveyViewModel::class.java]
//        champsDetailsandRatingBarCallBack = ChampsDetailsandRatingBarCallBack
        setup()
    }


    @SuppressLint("SetTextI18n")
    private fun setup() {
        fragmentChampsSurveyBinding!!.callback = this
//        MainActivity.mInstance.mainActivityCallback = this
//        Utlis.hideLoading()
        if (Preferences.getSaveChampsSurveySiteId().isEmpty()) {
//            showLoading()
            val i = Intent(applicationContext, SelectChampsSiteIDActivity::class.java)
            i.putExtra("modulename", "CHAMPS")
            startActivityForResult(i, 781)
        } else {

            fragmentChampsSurveyBinding!!.enterStoreEdittext.setText("${Preferences.getSaveChampsSurveySiteId()} - ${Preferences.getSaveChampsSurveySiteName()}")


//
//            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//                showLoading()
//                viewModel.getStoreDetailsChamps(this)
//            } else {
//                Toast.makeText(
//                    activity,
//                    resources.getString(R.string.label_network_error),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }


            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                Utlis.showLoading(this)
                newSurveyViewModel!!.getStoreDetailsChampsApi(
                    this
                )
            } else {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
//            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//                Utlis.showLoading(this)
//                if(storeId.isNullOrEmpty()){
//                    newSurveyViewModel!!.getStoreWiseDetailsChampsApi(
//                        this,
//                        Preferences.getSaveChampsSurveySiteId()
//                    )
//                }else{
//                    newSurveyViewModel!!.getStoreWiseDetailsChampsApi(
//                        this,
//                        storeId!!
//                    )
//                }
//            }
//            else {
//                Toast.makeText(
//                    applicationContext,
//                    resources.getString(R.string.label_network_error),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//


            fragmentChampsSurveyBinding!!.enterStoreEdittext.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int,
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    if (editable.length > 0) {
//                        viewBinding.storeIdEdittextLayout.elevation = 0.0f
//                        viewBinding.enterStoreEdittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
//                        viewBinding.enterStoreEdittext.background =
//                            resources.getDrawable(R.drawable.backgrounf_for_blue_edittext)
//                        viewBinding.storeIdEdittextLayout.background =
//                            resources.getDrawable(R.drawable.backgrounf_for_blue_edittext)
//                        viewBinding.closeIcon.visibility = View.VISIBLE
                    } else {
//                        viewBinding.enterStoreEdittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
//                        viewBinding.enterStoreEdittext.background =
//                            resources.getDrawable(R.drawable.edit_comment_bg)
//                        viewBinding.storeIdEdittextLayout.background =
//                            resources.getDrawable(R.drawable.edit_comment_bg)
//                        viewBinding.closeIcon.visibility = View.GONE
                    }


                }
            })
        }
    }


    override fun onClickSearch() {
        fragmentChampsSurveyBinding!!.searchButton.visibility = View.GONE
        fragmentChampsSurveyBinding!!.cardViewStore.visibility = View.VISIBLE
    }

    override fun onClickCardView() {
        if(!Preferences.getChampsSiteForEr().isNullOrEmpty() && !Preferences.getChampsSiteCity().isNullOrEmpty()){
            val intent = Intent(applicationContext, SurveyDetailsActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("getStoreWiseDetailsResponses", getSiteDetails)
            intent.putExtra("storeId", Preferences.getChampsSiteForEr())
            intent.putExtra("address", address)
            intent.putExtra("siteName", siteName)
            intent.putExtra("storeCity", Preferences.getChampsSiteCity())
            intent.putExtra("region", fragmentChampsSurveyBinding!!.region.text.toString())
            startActivityForResult(intent, 761)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

    }


    override fun onClickCloseIcon() {
        fragmentChampsSurveyBinding!!.enterStoreEdittext.setText("")
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>) {
        if (value != null) {
            for (i in value.indices) {
                var siteForDetails = ""
                if(storeId.isNullOrEmpty()){
                    siteForDetails = Preferences.getSaveChampsSurveySiteId()
                }else{
                    siteForDetails= storeId!!
                }
                if (value.get(i).site.equals(siteForDetails)) {
                    storeId = value.get(i).site
                    Preferences.setChampsSiteForEr(storeId!!)
                    siteName = value.get(i).storeName
                    Preferences.setChampsSiteName(siteName!!)
                    Preferences.setRegionUidChamps(value.get(i).region!!.uid!!)
                    Preferences.setChampsSiteCity(value.get(i).city!!)
                    siteCity = value.get(i).city
                    region = value.get(i).state!!.name
                    if (value.get(i).address != null)
                        address = value.get(i).address
                    fragmentChampsSurveyBinding!!.storeId.text =
                        "${value.get(i).site} - ${siteName}"
                    fragmentChampsSurveyBinding!!.region.text =
                        value.get(i).city + ", " + value.get(i).state!!.name + ", " + value.get(i).district!!.name

                    if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//                        Utlis.showLoading(this)
                        if(storeId.isNullOrEmpty()){
                            newSurveyViewModel!!.getStoreWiseDetailsChampsApi(
                                this,
                                Preferences.getSaveChampsSurveySiteId()
                            )
                        }else{
                            newSurveyViewModel!!.getStoreWiseDetailsChampsApi(
                                this,
                                storeId!!
                            )
                        }

                    } else {
                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.label_network_error),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

        }

//        hideLoading()

//        if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//            Utlis.showLoading(this)
//            if(storeId.isNullOrEmpty()){
//                newSurveyViewModel!!.getStoreWiseDetailsChampsApi(
//                    this,
//                    Preferences.getSaveChampsSurveySiteId()
//                )
//            }else{
//                newSurveyViewModel!!.getStoreWiseDetailsChampsApi(
//                    this,
//                    storeId!!
//                )
//            }
//
//        } else {
//            Toast.makeText(
//                applicationContext,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }

    }

    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
        Toast.makeText(applicationContext, "" + value, Toast.LENGTH_SHORT).show();
        hideLoading()
//        if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//            showLoading()
//            viewModel.getStoreWiseDetailsChamps(this)
//
//        }
//        else {
//            Toast.makeText(
//                activity,
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }

        if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
           Utlis.showLoading(this)
            newSurveyViewModel!!.getStoreDetailsChampsApi(
                this
            )
        } else {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onSuccessgetStoreWiseDetails(getStoreWiseDetailsResponses: GetStoreWiseDetailsModelResponse) {
        getSiteDetails = getStoreWiseDetailsResponses
        if (getStoreWiseDetailsResponses != null && getStoreWiseDetailsResponses.success && getStoreWiseDetailsResponses.data.executive != null) {
            if (getStoreWiseDetailsResponses.data.executive.email != null) {
                fragmentChampsSurveyBinding!!.emailId.setText(getStoreWiseDetailsResponses.data.executive.email)
            } else {
                fragmentChampsSurveyBinding!!.emailId.setText("--")
                hideLoading()
            }

        } else {
            fragmentChampsSurveyBinding!!.emailId.setText("--")
            Preferences.setApnaSite("")
            val i = Intent(applicationContext, SelectChampsSiteIDActivity::class.java)
            i.putExtra("modulename", "CHAMPS")
            startActivityForResult(i, 781)
        }
        hideLoading()
    }

    override fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse) {
        if (value != null && value.message != null) {
            fragmentChampsSurveyBinding!!.emailId.setText("--")
            Preferences.setApnaSite("")
            val i = Intent(applicationContext, SelectChampsSiteIDActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.putExtra("modulename", "CHAMPS")
            startActivityForResult(i, 781)
//            Toast.makeText(context, "" + value.message, Toast.LENGTH_SHORT).show()
        }
        hideLoading()
    }

    override fun onClickBack() {
        super.onBackPressed()
    }


    override fun onClickSiteIdIcon() {
        val i = Intent(applicationContext, SelectChampsSiteIDActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(i, 781)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            isSiteIdEmpty = data!!.getBooleanExtra("isSiteIdEmpty", isSiteIdEmpty)
            storeId = data!!.getStringExtra("siteId")
            siteName = data!!.getStringExtra("siteName")
            if(!data!!.getStringExtra("siteId").isNullOrEmpty()){
                Preferences.setChampsSiteForEr(storeId!!)
            }
            if(!data!!.getStringExtra("siteName").isNullOrEmpty()){
                Preferences.setChampsSiteName(siteName!!)
            }

            if (requestCode == 781) {
                hideLoading()
                Utlis.hideLoading()
                if (isSiteIdEmpty) {
                    if(!Preferences.getSaveChampsSurveySiteId().isEmpty()){

                    }else{
                        finish()
                    }

//                    hideLoadingTemp()
                    hideLoading()
                } else {
                    fragmentChampsSurveyBinding!!.enterStoreEdittext.setText("${storeId} - ${siteName}")
                    if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                        Utlis.showLoading(this)
                        newSurveyViewModel!!.getStoreDetailsChampsApi(
                            this
                        )
                    } else {
                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.label_network_error),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }


                }
            } else if (requestCode == 761 && resultCode == RESULT_OK) {
                isNewSurveyCreated = data!!.getBooleanExtra("isNewSurveyCreated", false)
                status = data!!.getStringExtra("status")!!
                if (isNewSurveyCreated && status.equals("NEW")) {
                    val intent = Intent()
                    intent.putExtra("isNewSurveyCreated", isNewSurveyCreated)
                    intent.putExtra("status", status)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

//            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//                showLoading()
//                viewModel.getStoreDetailsChamps(this)
//            }
//            else {
//                Toast.makeText(
//                    activity,
//                    resources.getString(R.string.label_network_error),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }


        }
    }
}