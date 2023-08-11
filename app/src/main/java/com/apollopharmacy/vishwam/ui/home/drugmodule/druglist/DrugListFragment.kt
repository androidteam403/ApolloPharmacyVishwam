package com.apollopharmacy.vishwam.ui.home.drugmodule.druglist

import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentDrugListBinding

class DrugListFragment : BaseFragment<DrugListViewModel, FragmentDrugListBinding>() {


    override val layoutRes: Int
        get() = R.layout.fragment_drug_list

    override fun retrieveViewModel(): DrugListViewModel {
        return ViewModelProvider(this).get(DrugListViewModel::class.java)
    }

    override fun setup() {
    }
}





