package com.apollopharmacy.vishwam.ui.home.communityadvisor


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.DialogServicesBinding
import com.apollopharmacy.vishwam.databinding.FragmentCommunityAdvisorBinding
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.home.communityadvisor.adapter.ServicesListAdapter
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServiceDetailsRequest
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServiceDetailsResponse
import com.apollopharmacy.vishwam.ui.home.communityadvisor.servicescustomerinteraction.ServicesCustomerInteractionActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import java.util.stream.Collectors

class CommunityAdvisorFragment :
    BaseFragment<CommunityAdvisorFragmentViewModel, FragmentCommunityAdvisorBinding>(),
    CommunityAdvisorFragmentCallback {
    companion object {
        private const val API_REQUEST_CODE = 123
    }

    var mInstance: MainActivity? = null
    private lateinit var servicesList: ArrayList<HomeServiceDetailsResponse.Detlist>
    lateinit var homeServiceDetailsResponse: HomeServiceDetailsResponse
    lateinit var homeServiceDetailsRequest: HomeServiceDetailsRequest
    private var communityAdvisorFragmentCallback: CommunityAdvisorFragmentCallback = this

    var isServicesTab = true
    override val layoutRes: Int
        get() = R.layout.fragment_community_advisor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.servicesButton.setOnClickListener {
            onClickServicesTab()
        }



        viewBinding.customerInteractionButton.setOnClickListener {
            onClickCustomerInteractionTab()
        }
        setup()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == API_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshApiData()
        }
    }

    private fun refreshApiData() {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            Utlis.showLoading(requireContext())
            viewModel.getHomeServiceDetails(homeServiceDetailsRequest, this)
        } else {
            Toast.makeText(
                context,
                "No Internet Connection.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun retrieveViewModel(): CommunityAdvisorFragmentViewModel {
        viewModel = ViewModelProvider(this).get(CommunityAdvisorFragmentViewModel::class.java)
        return viewModel
    }

      var servicesListAdapter: ServicesListAdapter?=null
    override fun setup() {
        viewBinding.noListFound.visibility=View.GONE
        viewBinding.callback = this
        homeServiceDetailsRequest = HomeServiceDetailsRequest()
        homeServiceDetailsRequest.userId = Preferences.getValidatedEmpId()

        servicesList = ArrayList()
        viewBinding.searchIcon.setOnClickListener {
            viewBinding.searchView.text!!.clear()
            servicesListAdapter?.filterList("")
        }

        viewBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                servicesListAdapter?.filterList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            Utlis.showLoading(requireContext())
            viewBinding.noListFound.visibility=View.GONE
            viewModel.getHomeServiceDetails(homeServiceDetailsRequest, this)

        } else {
            Toast.makeText(
                context,
                "No Internet Connection.",
                Toast.LENGTH_SHORT
            ).show()
        }
        navigatetoServiceCustomerActivity()
    }


    override fun onClickServicesTab() {
        isServicesTab = true
        updateButtonBackground()
        onSuccessHomeServiceDetails(homeServiceDetailsResponse)
    }

    override fun onClickCustomerInteractionTab() {
        isServicesTab = false
        navigatetoServiceCustomerActivity()
        updateButtonBackground()
        onSuccessHomeServiceDetails(homeServiceDetailsResponse)
    }


    private fun updateButtonBackground() {
        if (isServicesTab) {
            viewBinding.servicesButton.setBackgroundResource(R.color.header_blue)
            viewBinding.customerInteractionButton.setBackgroundResource(R.color.grey)
        } else {
            viewBinding.servicesButton.setBackgroundResource(R.color.grey)
            viewBinding.customerInteractionButton.setBackgroundResource(R.color.header_blue)
        }
    }


    override fun onClickServicesItems(serviceItem: HomeServiceDetailsResponse.Detlist) {
        serviceDialog(serviceItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSuccessHomeServiceDetails(homeServiceDetailsResponse: HomeServiceDetailsResponse) {
        this.homeServiceDetailsResponse = homeServiceDetailsResponse
        Utlis.hideLoading()
        if (!homeServiceDetailsResponse.detlist.isEmpty()
            && homeServiceDetailsResponse.detlist.size > 0
        ) {
            if (isServicesTab) {
                this.servicesList = homeServiceDetailsResponse.detlist.stream()
                    .filter { o -> o.serviceType.equals("SERVICE") }
                    .collect(Collectors.toList()) as ArrayList<HomeServiceDetailsResponse.Detlist>
            } else {
                this.servicesList = homeServiceDetailsResponse.detlist.stream()
                    .filter { o -> o.serviceType.equals("CUSTOMER") }
                    .collect(Collectors.toList()) as ArrayList<HomeServiceDetailsResponse.Detlist>
            }
            /* if (servicesList.isNotEmpty()) {
                 val latestItem = servicesList[0]
                 servicesList.add(0, latestItem)
             }*/
            servicesList.reverse()

            viewBinding.noListFound.visibility = View.GONE
            servicesListAdapter =
                ServicesListAdapter(communityAdvisorFragmentCallback, servicesList)
            viewBinding.servicesRecyclerView.adapter =
                servicesListAdapter
            viewBinding.servicesRecyclerView.layoutManager =
                LinearLayoutManager(context)

        } else {
            viewBinding.noListFound.visibility = View.VISIBLE
            Toast.makeText(
                requireContext(),
                "No data available.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onFailureHomeServiceDetails(message: String) {
        Utlis.hideLoading()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun serviceDialog(
        serviceItem: HomeServiceDetailsResponse.Detlist,
    ) {
        val servicesDialog = Dialog(requireContext())
        val dialogServicesBinding =
            DialogServicesBinding.inflate(LayoutInflater.from(requireContext()))
        servicesDialog.setContentView(dialogServicesBinding.root)
        servicesDialog.window!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        servicesDialog.setCanceledOnTouchOutside(false)
        dialogServicesBinding.closeWindow.setOnClickListener {
            servicesDialog.dismiss()
        }
        if (serviceItem.serviceType.equals("Service", ignoreCase = true)) {
            dialogServicesBinding.siteId.text = "(" + serviceItem.siteId + ")"
            dialogServicesBinding.customerName.text = serviceItem.customerName
            dialogServicesBinding.createdBy.text = serviceItem.createdBy
            dialogServicesBinding.serviceName.text =
                generateFormattedServiceNames(serviceItem.serviceName)
            dialogServicesBinding.others.text = serviceItem.others
            dialogServicesBinding.servicesId.text = serviceItem.uniqueId
            dialogServicesBinding.customerMobile.text = "(+91 ${serviceItem.customerMobileno})"
            //  "(+91) ${serviceItem.customerMobileno}"//"("+serviceItem.customerMobileno+")"
            dialogServicesBinding.customerDate.text = serviceItem.serviceDate
            dialogServicesBinding.type.text = "(" + serviceItem.serviceType + ")"
        } else {
            dialogServicesBinding.siteId.text = "(" + serviceItem.siteId + ")"
            dialogServicesBinding.customerName.text = serviceItem.customerName
            dialogServicesBinding.createdBy.text = serviceItem.createdBy
            dialogServicesBinding.serviceName.text =
                generateFormattedServiceNames(serviceItem.serviceName)
            dialogServicesBinding.others.text = serviceItem.customerInteractionremarks
            dialogServicesBinding.customerDate.text = serviceItem.serviceDate
            dialogServicesBinding.customerMobile.text = "(+91 ${serviceItem.customerMobileno})"
            //  "(+91) ${serviceItem.customerMobileno}" //"("+serviceItem.customerMobileno+")"
            dialogServicesBinding.servicesId.text = serviceItem.uniqueId
            dialogServicesBinding.type.text = "(" + serviceItem.serviceType + ")"
        }

        servicesDialog.show()

    }

    private fun generateFormattedServiceNames(serviceNames: String?): CharSequence? {
        val stringBuilder = StringBuilder()

        if (!serviceNames.isNullOrBlank()) {
            val serviceList = serviceNames.split(",").map { it.trim() }

            for ((index, serviceName) in serviceList.withIndex()) {
                stringBuilder.append("${index + 1}. $serviceName")

                if (index < serviceList.size - 1) {
                    stringBuilder.append("\n")
                }
            }
        }

        return stringBuilder.toString()
    }

    private fun navigatetoServiceCustomerActivity() {
        MainActivity.mInstance.plusIconApna.setOnClickListener {
            val intent = Intent(activity, ServicesCustomerInteractionActivity::class.java)
            intent.putExtra("IS_SERVICES", isServicesTab)
            startActivityForResult(intent, API_REQUEST_CODE)
        }
    }

    private fun filterList(query: String) {
        servicesListAdapter?.filterList(query)
        val filteredList = servicesList.filter { item ->
            item.uniqueId!!.contains(query, ignoreCase = true)||
                    item.customerName!!.contains(query,ignoreCase = true)||
                    item.customerMobileno!!.contains(query,ignoreCase = true)
        }
        servicesList.clear()
        servicesList.addAll(filteredList)
    }
}


