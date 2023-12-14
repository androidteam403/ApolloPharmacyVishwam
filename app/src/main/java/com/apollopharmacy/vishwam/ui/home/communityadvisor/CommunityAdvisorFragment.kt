package com.apollopharmacy.vishwam.ui.home.communityadvisor


import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.FragmentCommunityAdvisorBinding
import com.apollopharmacy.vishwam.ui.home.communityadvisor.adapter.ServicesListAdapter
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.ServicesList

class CommunityAdvisorFragment :
    BaseFragment<CommunityAdvisorFragmentViewModel, FragmentCommunityAdvisorBinding>(),
    CommunityAdvisorFragmentCallback {
    lateinit var servicesListAdapter: ServicesListAdapter
    lateinit var fragmentCommunityAdvisorBinding: FragmentCommunityAdvisorBinding

    // var serviceList = ArrayList<ServicesList>()
    var isServicesTab: Boolean = true
    var isCustomerInteractionTab: Boolean = true
    override val layoutRes: Int
        get() = R.layout.fragment_community_advisor


    override fun retrieveViewModel(): CommunityAdvisorFragmentViewModel {
        return ViewModelProvider(this).get(CommunityAdvisorFragmentViewModel::class.java)
    }

    override fun setup() {
        val serviceList = listOf(
            ServicesList("32564", "Customer Interaction"),
            ServicesList("32585", "Services"),
            ServicesList("32599", "Services"),
            ServicesList("32774", "Customer Interaction"),
            ServicesList("32124", "Services"),
            ServicesList("32145", "Services")
        )
        val servicesListAdapter = ServicesListAdapter(serviceList)
        viewBinding.communityAdvisorRecyclerview.adapter = servicesListAdapter
        viewBinding.communityAdvisorRecyclerview.layoutManager =
            LinearLayoutManager(context)


}

}


