package com.apollopharmacy.vishwam.ui.home.champs.survey.fragment

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
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
import com.apollopharmacy.vishwam.ui.home.model.StoreDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.champs.survey.activity.surveydetails.SurveyDetailsActivity
import com.apollopharmacy.vishwam.ui.home.champs.survey.fragment.adapter.GetStoreDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.model.GetStoreWiseDetailsModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectChampsSiteIDActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectSwachhSiteIDActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis

class NewSurveyFragment : BaseFragment<NewSurveyViewModel, FragmentChampsSurveyBinding>(),
    NewSurveyCallback, MainActivityCallback {
    var getStoreDetailsAdapter: GetStoreDetailsAdapter? = null
    var getStoreWiseDetailsResponse: GetStoreWiseDetailsModelResponse? = null
    var storeId: String?=""
    var address:String?=""
    var siteName:String?=""

    override val layoutRes: Int
        get() = R.layout.fragment_champs_survey

    override fun retrieveViewModel(): NewSurveyViewModel {
        return ViewModelProvider(this).get(NewSurveyViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback = this
        MainActivity.mInstance.mainActivityCallback = this
        Utlis.hideLoading()
        if (Preferences.getSwachhSiteId().isEmpty()) {
            showLoading()
            val i = Intent(context, SelectSwachhSiteIDActivity::class.java)
            startActivityForResult(i, 781)
        } else {

            viewBinding.enterStoreEdittext.setText(Preferences.getSwachhSiteId())



            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                showLoading()
                viewModel.getStoreDetailsChamps(this)
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
//                viewModel.getStoreDetailsChampsApi(
//                    this
//                )
//            } else {
//                Toast.makeText(
//                    activity,
//                    resources.getString(R.string.label_network_error),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
//                showLoading()
//                viewModel.getStoreWiseDetailsChampsApi(
//                    this,
//                    viewBinding.enterStoreEdittext.text.toString()
//                )
//            } else {
//                Toast.makeText(
//                    activity,
//                    resources.getString(R.string.label_network_error),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
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
        intent.putExtra("getStoreWiseDetailsResponses", getStoreWiseDetailsResponse)
        intent.putExtra("storeId", storeId)
        intent.putExtra("address", address)
        intent.putExtra("siteName", siteName)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickCloseIcon() {
        viewBinding.enterStoreEdittext.setText("")
    }
    override fun onSuccessgetStoreDetails(storeDetailsResponse: StoreDetailsModelResponse) {
        if(storeDetailsResponse!=null && storeDetailsResponse.storeDetails!=null){
            for(i in storeDetailsResponse.storeDetails.indices){
                if(storeDetailsResponse.storeDetails.get(i).siteid.equals(viewBinding.enterStoreEdittext.text.toString())){
                    storeId =storeDetailsResponse.storeDetails.get(i).siteid
                    siteName = storeDetailsResponse.storeDetails.get(i).sitename
                    address = storeDetailsResponse.storeDetails.get(i).region +", " + storeDetailsResponse.storeDetails.get(i).city
                    viewBinding.storeId.text= storeDetailsResponse.storeDetails.get(i).siteid
                    viewBinding.address.text=storeDetailsResponse.storeDetails.get(i).region +", " + storeDetailsResponse.storeDetails.get(i).city
                }
        }

        }

        hideLoading()
        if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
            showLoading()
            viewModel.getStoreWiseDetailsChamps(this)

        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
//       Toast.makeText(activity, ""+value.storeDetails.size,Toast.LENGTH_SHORT).show();
//        getStoreDetailsAdapter =
//            GetStoreDetailsAdapter(context, value.storeDetails, this, this)
//
//        val layoutManager = LinearLayoutManager(ViswamApp.context)
//        viewBinding.storeDetailsRecyclerview.layoutManager = layoutManager
////                activityreShootBinding.imageRecyclerViewRes.itemAnimator =
////                    DefaultItemAnimator()
//        viewBinding.storeDetailsRecyclerview.adapter = getStoreDetailsAdapter

    }

    override fun onFailuregetStoreDetails(value: StoreDetailsModelResponse) {
        Toast.makeText(activity, "" + value.message, Toast.LENGTH_SHORT).show();
        hideLoading()
        if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
            showLoading()
            viewModel.getStoreWiseDetailsChamps(this)

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
        getStoreWiseDetailsResponse = getStoreWiseDetailsResponses
        if (getStoreWiseDetailsResponses != null && getStoreWiseDetailsResponses.storeWiseDetails != null && getStoreWiseDetailsResponses.storeWiseDetails.executiveEmail != null) {
            viewBinding.emailId.setText(getStoreWiseDetailsResponses.storeWiseDetails.executiveEmail)
        }
        hideLoading()
    }

    override fun onFailuregetStoreWiseDetails(value: GetStoreWiseDetailsModelResponse) {
        if (value != null && value.message != null) {
            Toast.makeText(context, "" + value.message, Toast.LENGTH_SHORT).show()
        }
        hideLoading()
    }

    override fun onClickFilterIcon() {


    }

    override fun onClickSiteIdIcon() {
        val i = Intent(context, SelectChampsSiteIDActivity::class.java)
        startActivityForResult(i, 781)
    }

    override fun onClickQcFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            viewBinding.enterStoreEdittext.setText(Preferences.getSwachhSiteId())
            if (NetworkUtil.isNetworkConnected(ViswamApp.context)) {
                showLoading()
                viewModel.getStoreDetailsChamps(this)
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
}