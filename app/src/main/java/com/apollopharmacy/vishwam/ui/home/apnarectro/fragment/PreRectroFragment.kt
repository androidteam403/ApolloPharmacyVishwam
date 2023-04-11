package com.apollopharmacy.vishwam.ui.home.apnarectro.fragment

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentPreRectroBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.UploadImagesActivity
import com.apollopharmacy.vishwam.ui.home.swach.swachlistmodule.approvelist.ApproveListActivity
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager


class PreRectroFragment() : BaseFragment<PreRectroViewModel, FragmentPreRectroBinding>(),
    PreRectroCallback {

    override val layoutRes: Int
        get() = R.layout.fragment_pre_rectro

    override fun retrieveViewModel(): PreRectroViewModel {
        return ViewModelProvider(this).get(PreRectroViewModel::class.java)
    }

    override fun setup() {
        viewBinding.callback=this
        if(SessionManager(activity).rectroPosition.equals("1")){
            viewBinding.createNewRectroLayout.visibility= View.VISIBLE
            viewBinding.recordsUploaded.visibility=View.VISIBLE
        }else{
            viewBinding.createNewRectroLayout.visibility= View.GONE
            viewBinding.recordsUploaded.visibility=View.GONE
        }
    }

    override fun onClickContinue() {
        val intent = Intent(context, UploadImagesActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}