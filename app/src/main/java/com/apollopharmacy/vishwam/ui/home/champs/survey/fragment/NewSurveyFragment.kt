package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.FragmentChampsSurveyBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.adapter.GetStoreDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse

import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectChampsSiteIDActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis

class NewSurveyFragment : BaseFragment<NewSurveyViewModel, FragmentChampsSurveyBinding>(),
    NewSurveyCallback, MainActivityCallback {
    var getStoreDetailsAdapter: GetStoreDetailsAdapter? = null
    var getSiteDetails: GetStoreWiseDetailsModelResponse? = null
    var storeId: String? = ""
    var address: String? = ""
    var siteName: String? = ""
    var siteCity: String? = ""
    var region: String? = ""
    var isSiteIdEmpty: Boolean = false

    override val layoutRes: Int
        get() = R.layout.fragment_champs_survey

    override fun retrieveViewModel(): NewSurveyViewModel {
        return ViewModelProvider(this).get(NewSurveyViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this
        MainActivity.mInstance.mainActivityCallback = this
//        Utlis.hideLoading()
        if (Preferences.getApnaSiteId().isEmpty()) {
//            showLoading()
            val i = Intent(context, SelectChampsSiteIDActivity::class.java)
            i.putExtra("modulename", "CHAMPS")
            startActivityForResult(i, 781)
        } else {

            viewBinding.enterStoreEdittext.setText(Preferences.getApnaSiteId())


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
                showLoading()
                viewModel.getStoreDetailsChampsApi(
                    this
                )
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                showLoading()
                viewModel.getStoreWiseDetailsChampsApi(
                    this,
                    viewBinding.enterStoreEdittext.text.toString()
                )
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
//


            viewBinding.enterStoreEdittext.addTextChangedListener(object :
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
        viewBinding.searchButton.visibility = View.GONE
        viewBinding.cardViewStore.visibility = View.VISIBLE
    }

    override fun onClickCardView() {
        val intent = Intent(context, SurveyDetailsActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("getStoreWiseDetailsResponses", getSiteDetails)
        intent.putExtra("storeId", storeId)
        intent.putExtra("address", address)
        intent.putExtra("siteName", siteName)
        intent.putExtra("storeCity", siteCity)
        intent.putExtra("region", viewBinding.region.text.toString())
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickCloseIcon() {
        viewBinding.enterStoreEdittext.setText("")
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onSuccessgetStoreDetails(value: List<StoreDetailsModelResponse.Row>) {
        if (value != null ) {
            for (i in value.indices) {
                if (value.get(i).site.equals(viewBinding.enterStoreEdittext.text.toString())) {
                    storeId = value.get(i).site
                    siteName = value.get(i).storeName
                    siteCity = value.get(i).city
                    region =    value.get(i).state!!.name
                    address = value.get(i).address
                    viewBinding.storeId.text = value.get(i).site
                    viewBinding.region.text = value.get(i).city +  ", "+  value.get(i).state!!.name + ", " + value.get(i).district!!.name
                }
            }

        }

        hideLoading()

        if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
            showLoading()
            viewModel.getStoreWiseDetailsChampsApi(
                this,
                Preferences.getValidatedEmpId()
            )
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

    }

    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
        Toast.makeText(activity, "" + value, Toast.LENGTH_SHORT).show();
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
            showLoading()
            viewModel.getStoreDetailsChampsApi(
                this
            )
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onSuccessgetStoreWiseDetails(getStoreWiseDetailsResponses: GetStoreWiseDetailsModelResponse) {
        getSiteDetails = getStoreWiseDetailsResponses
        if (getStoreWiseDetailsResponses != null && getStoreWiseDetailsResponses.success  && getStoreWiseDetailsResponses.data.executive != null) {
            viewBinding.emailId.setText(getStoreWiseDetailsResponses.data.executive.email)
        } else {
            viewBinding.emailId.setText("--")
            Preferences.setApnaSite("")
            val i = Intent(context, SelectChampsSiteIDActivity::class.java)
            i.putExtra("modulename", "CHAMPS")
            startActivityForResult(i, 781)
        }
        hideLoading()
    }

    override fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse) {
        if (value != null && value.message != null) {
            viewBinding.emailId.setText("--")
            Preferences.setApnaSite("")
            val i = Intent(context, SelectChampsSiteIDActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.putExtra("modulename", "CHAMPS")
            startActivityForResult(i, 781)
//            Toast.makeText(context, "" + value.message, Toast.LENGTH_SHORT).show()
        }
        hideLoading()
    }

    override fun onClickFilterIcon() {


    }

    override fun onClickSiteIdIcon() {
        val i = Intent(context, SelectChampsSiteIDActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(i, 781)
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onSelectApprovedFragment(listSize: String?) {
        TODO("Not yet implemented")
    }

    override fun onSelectRejectedFragment() {
        TODO("Not yet implemented")
    }

    override fun onSelectPendingFragment() {
        TODO("Not yet implemented")
    }

    override fun onClickSpinnerLayout() {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            isSiteIdEmpty = data!!.getBooleanExtra("isSiteIdEmpty", isSiteIdEmpty)
            if (requestCode == 781) {
                hideLoading()
                Utlis.hideLoading()
                if (isSiteIdEmpty) {
                    MainActivity.mInstance.onBackPressed()
//                    hideLoadingTemp()
                    hideLoading()
                } else {
                    if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                        showLoading()
                        viewModel.getStoreDetailsChampsApi(
                            this
                        )
                    } else {
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.label_network_error),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
            viewBinding.enterStoreEdittext.setText(Preferences.getApnaSiteId())
            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                showLoading()
                viewModel.getStoreWiseDetailsChampsApi(
                    this,
                    viewBinding.enterStoreEdittext.text.toString()
                )
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
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