package com.apollopharmacy.vishwam.ui.home.planogram.fragment

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.databinding.FragmentPlanogramBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramEvaluationActivity
import com.apollopharmacy.vishwam.ui.home.planogram.siteid.SelectPlanogramSiteIDActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.ApproveListActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.previewImage.PreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachuploadmodule.selectswachhid.SelectChampsSiteIDActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis

class PlanogramFragment : BaseFragment<PlanogramViewModel, FragmentPlanogramBinding>(),
    PlanogramCallback, MainActivityCallback {
    var isSiteIdEmpty: Boolean = false

    override val layoutRes: Int
        get() = R.layout.fragment_planogram

    override fun retrieveViewModel(): PlanogramViewModel {
        return ViewModelProvider(this).get(PlanogramViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback=this
        if (Preferences.getPlanogramSiteId().isEmpty()) {
//            showLoading()
            val i = Intent(requireContext(), SelectPlanogramSiteIDActivity::class.java)
            i.putExtra("modulename", "PLANOGRAM")
            startActivityForResult(i, 781)
        } else {
            viewBinding.storeId.setText(Preferences.getPlanogramSiteId())
            viewBinding.siteName.setText(Preferences.getPlanogramSiteName())
            viewBinding!!.siteIdSelect.setText("${Preferences.getPlanogramSiteId()} - ${Preferences.getPlanogramSiteName()}")
        }

        }



    override fun onClickFilterIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickSiteIdIcon() {
        TODO("Not yet implemented")
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

    override fun onClickContinue() {
        val intent = Intent(context, PlanogramEvaluationActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickSiteId() {
        val i = Intent(requireContext(), SelectPlanogramSiteIDActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(i, 781)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            isSiteIdEmpty = data!!.getBooleanExtra("isSiteIdEmpty", isSiteIdEmpty)
            if (requestCode == 781) {
                Utlis.hideLoading()
                Utlis.hideLoading()
                if (isSiteIdEmpty) {
                    MainActivity.mInstance.onBackPressed()
//                    hideLoadingTemp()
                    Utlis.hideLoading()
                } else {
                    viewBinding.storeId.setText(Preferences.getPlanogramSiteId())
                    viewBinding.siteName.setText(Preferences.getPlanogramSiteName())
                    viewBinding!!.siteIdSelect.setText("${Preferences.getPlanogramSiteId()} - ${Preferences.getPlanogramSiteName()}")



                }
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
