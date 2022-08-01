package com.apollopharmacy.vishwam.ui.sampleui.swachlistmodule.swachlistscreen1

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentSampleuiSwachBinding
import com.apollopharmacy.vishwam.databinding.FragmentSwachlistmoduleuiBinding
import com.apollopharmacy.vishwam.ui.sampleui.sampleswachui.SampleSwachViewModel
import com.apollopharmacy.vishwam.ui.sampleui.swachlistmodule.swachlisrscreen2.ApproveListActivity
import com.apollopharmacy.vishwam.ui.sampleui.uploadnowbutton.UploadNowButtonActivity

class SwachListmoduleUi : BaseFragment<SwachListmoduleViewModel, FragmentSwachlistmoduleuiBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_swachlistmoduleui

    override fun retrieveViewModel(): SwachListmoduleViewModel {
        return ViewModelProvider(this).get(SwachListmoduleViewModel::class.java)
    }

    override fun setup() {

        viewBinding.updateButton.setOnClickListener {
            val intent = Intent(context, ApproveListActivity::class.java)
            startActivity(intent)
        }
    }
}