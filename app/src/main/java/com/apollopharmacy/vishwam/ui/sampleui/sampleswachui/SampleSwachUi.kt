package com.apollopharmacy.vishwam.ui.sampleui.sampleswachui

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentSampleuiSwachBinding
import com.apollopharmacy.vishwam.databinding.FragmentSwacchImagesUploadBinding
import com.apollopharmacy.vishwam.ui.sampleui.reshootactivity.ReShootActivity
import com.apollopharmacy.vishwam.ui.sampleui.uploadnowbutton.UploadNowButtonActivity

class SampleSwachUi : BaseFragment<SampleSwachViewModel, FragmentSampleuiSwachBinding>() {

    override val layoutRes: Int
        get() = R.layout.fragment_sampleui_swach

    override fun retrieveViewModel(): SampleSwachViewModel {
        return ViewModelProvider(this).get(SampleSwachViewModel::class.java)
    }

    override fun setup() {

        viewBinding.reshootCardView.setOnClickListener {
            val intent = Intent(context, ReShootActivity::class.java)
            startActivity(intent)
        }

        viewBinding.uploadNowbtnn.setOnClickListener {
            val intent = Intent(context, UploadNowButtonActivity::class.java)
            startActivity(intent)
        }



    }
}