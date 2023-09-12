package com.apollopharmacy.vishwam.ui.home.planogram.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentPlanogramBinding
import com.apollopharmacy.vishwam.ui.home.MainActivityCallback
import com.apollopharmacy.vishwam.ui.home.MenuModel
import com.apollopharmacy.vishwam.ui.home.planogram.activity.PlanogramEvaluationActivity
import java.util.ArrayList

class PlanogramFragment : BaseFragment<PlanogramViewModel, FragmentPlanogramBinding>(),
    PlanogramCallback, MainActivityCallback {
    override val layoutRes: Int
        get() = R.layout.fragment_planogram

    override fun retrieveViewModel(): PlanogramViewModel {
        return ViewModelProvider(this).get(PlanogramViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback=this

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
        position: Int
    ) {
    }

    override fun onClickContinue() {
        val intent = Intent(context, PlanogramEvaluationActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}