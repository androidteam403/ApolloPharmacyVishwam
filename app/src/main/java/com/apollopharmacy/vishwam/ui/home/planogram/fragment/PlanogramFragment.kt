package com.apollopharmacy.vishwam.ui.home.planogram.fragment

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentPlanogramBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramEvaluationActivity
import com.apollopharmacy.vishwam.ui.home.planogram.activity.model.ListBySiteIdResponse
import com.apollopharmacy.vishwam.ui.home.planogram.fragment.adapter.ListBySiteIdAdapter
import com.apollopharmacy.vishwam.ui.home.planogram.siteid.SelectPlanogramSiteIDActivity
import com.apollopharmacy.vishwam.util.Utlis
import java.util.ArrayList

class PlanogramFragment : BaseFragment<PlanogramViewModel, FragmentPlanogramBinding>(),
    PlanogramCallback, MainActivityCallback {
    var isSiteIdEmpty: Boolean = false
    var listBySiteIdAdapter: ListBySiteIdAdapter? = null
    var uid:String=""

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
//            viewBinding.storeId.setText(Preferences.getPlanogramSiteId())
//            viewBinding.siteName.setText(Preferences.getPlanogramSiteName())
            viewBinding!!.siteIdSelect.setText("${Preferences.getPlanogramSiteId()} - ${Preferences.getPlanogramSiteName()}")
            showLoading()
            viewModel.getList(Preferences.getPlanogramSiteId(), this)
        }


        viewBinding.continueBtn.setOnClickListener {
            val intent = Intent(context, PlanogramEvaluationActivity::class.java)
            intent.putExtra("uid","")
            startActivityForResult(intent, 911)
            activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
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

    override fun onClickSubmenuItem(
        menuName: String?,
        submenus: ArrayList<MenuModel>?,
        position: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onclickHelpIcon() {
        TODO("Not yet implemented")
    }

    override fun onClickContinue(uid:String) {
        this.uid =uid
        val intent = Intent(context, PlanogramEvaluationActivity::class.java)
        intent.putExtra("uid",uid)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onClickSiteId() {
        val i = Intent(requireContext(), SelectPlanogramSiteIDActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(i, 781)
    }

    override fun onSuccessPlanogramSiteIdList(siteIdResponse: ListBySiteIdResponse?) {
        if(siteIdResponse!=null && siteIdResponse!!.data!=null && siteIdResponse!!.data!!.listData!=null
            && siteIdResponse!!.data!!.listData!!.rows!=null && siteIdResponse!!.data!!.listData!!.rows!!.size>0){
            hideLoading()
            viewBinding.siteIdByListRecyclerView.visibility = View.VISIBLE
            viewBinding.noListFound.visibility=View.GONE
            listBySiteIdAdapter =
                ListBySiteIdAdapter(context,siteIdResponse!!.data!!.listData!!.rows, this)
            val layoutManager = LinearLayoutManager(context)
            viewBinding.siteIdByListRecyclerView.setLayoutManager(
                layoutManager
            )
            viewBinding.siteIdByListRecyclerView.setAdapter(
                listBySiteIdAdapter
            )
        }else{
            hideLoading()
            viewBinding.noListFound.visibility=View.VISIBLE
            viewBinding.siteIdByListRecyclerView.visibility = View.GONE
//            Toast.makeText(context, siteIdResponse!!.data!!.listData!!.records!!.length, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onFailurePlanogramSiteIdList(message: Any) {
        hideLoading()
        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show()
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
//                    viewBinding.storeId.setText(Preferences.getPlanogramSiteId())
//                    viewBinding.siteName.setText(Preferences.getPlanogramSiteName())
                    viewBinding!!.siteIdSelect.setText("${Preferences.getPlanogramSiteId()} - ${Preferences.getPlanogramSiteName()}")
                    showLoading()
                    viewModel.getList(Preferences.getPlanogramSiteId(), this)


                }
            }else{
                viewBinding!!.siteIdSelect.setText("${Preferences.getPlanogramSiteId()} - ${Preferences.getPlanogramSiteName()}")
                showLoading()
                viewModel.getList(Preferences.getPlanogramSiteId(), this)
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
