package com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.sampleswachui

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentSampleuiSwachBinding
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.adapter.ConfigListAdapter
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.adapter.ConfigListAdapterSwach
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.ReShootActivity
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.uploadnowactivity.UploadNowButtonActivity
import com.apollopharmacy.vishwam.util.NetworkUtil


class SampleSwachUi : BaseFragment<SampleSwachViewModel, FragmentSampleuiSwachBinding>() {
    private var swacchApolloList = ArrayList<SwachModelResponse>()
    private lateinit var configListAdapter: ConfigListAdapterSwach


    override val layoutRes: Int
        get() = R.layout.fragment_sampleui_swach

    override fun retrieveViewModel(): SampleSwachViewModel {
        return ViewModelProvider(this).get(SampleSwachViewModel::class.java)
    }

    override fun setup() {
        viewModel.swachImagesRegisters()

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
        }
        else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        viewModel.swachhapolloModel.observeForever {
            if (it != null && it.status ?: null == true) {
                swacchApolloList.add(it)

                for ((index, value) in it.configlist!!.withIndex()) {
                    val countUpload = value.categoryImageUploadCount?.toInt()
                    var dtcl_list = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
                    for (count in 1..countUpload!!) {

                        dtcl_list.add(SwachModelResponse.Config.ImgeDtcl(null, count, "", 0))

                    }
                    swacchApolloList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

                }

                hideLoading()

            }
        }

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