package com.apollopharmacy.vishwam.ui.home.drugmodule.druglist

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.FragmentDrugListBinding
import com.apollopharmacy.vishwam.databinding.FragmentRejectedQcBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.home.qcfail.pending.adapter.QcPendingListAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.QcPreviewImageActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.rejected.adapter.QcRejectedListAdapter
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utlis
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DrugListFragment : BaseFragment<DrugListViewModel, FragmentDrugListBinding>() {


    override val layoutRes: Int
        get() = R.layout.fragment_drug_list

    override fun retrieveViewModel(): DrugListViewModel {
        return ViewModelProvider(this).get(DrugListViewModel::class.java)
    }

    override fun setup() {
    }
}





