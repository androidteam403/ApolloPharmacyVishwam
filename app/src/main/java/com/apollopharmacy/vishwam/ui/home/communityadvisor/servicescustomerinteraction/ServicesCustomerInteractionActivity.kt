package com.apollopharmacy.vishwam.ui.home.communityadvisor.servicescustomerinteraction

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityServicesCustomerInteractionBinding
import com.apollopharmacy.vishwam.databinding.DialogConfirmSavedetailsBinding
import com.apollopharmacy.vishwam.databinding.DialogSubmitCommunityadvisorDetailsBinding
import com.apollopharmacy.vishwam.ui.home.communityadvisor.adapter.ServicesCustomerResponseAdapter
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.GetServicesCustomerResponse
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServicesSaveDetailsRequest
import com.apollopharmacy.vishwam.ui.home.communityadvisor.model.HomeServicesSaveDetailsResponse
import com.apollopharmacy.vishwam.ui.home.communityadvisor.siteid.SelectCommunityAdvisorSiteIdActivity
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.signaturepad.NetworkUtils
import java.util.regex.Pattern
import java.util.stream.Collectors

class ServicesCustomerInteractionActivity : AppCompatActivity(), ServicesCustomerCallback {
    lateinit var activityServicesCustomerInteractionBinding: ActivityServicesCustomerInteractionBinding
    private lateinit var viewModel: ServicesCustomerViewModel
    private lateinit var servicesCustomerResponseAdapter: ServicesCustomerResponseAdapter
    var isServicesTab = true
    lateinit var homeServicesSaveDetailsRequest: HomeServicesSaveDetailsRequest
    var isSiteIdEmpty: Boolean = false
    private lateinit var servicesCustomerReqList: ArrayList<GetServicesCustomerResponse.ListServices>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityServicesCustomerInteractionBinding =
            DataBindingUtil.setContentView(
                this@ServicesCustomerInteractionActivity,
                R.layout.activity_services_customer_interaction
            )

        viewModel =
            ViewModelProvider(this)[ServicesCustomerViewModel::class.java]
        activityServicesCustomerInteractionBinding.callback =
            this@ServicesCustomerInteractionActivity
        setUp()
    }


    private fun setUp() {
        if (intent != null) {
            isServicesTab = intent.getBooleanExtra("IS_SERVICES", false)
        }
        if (NetworkUtils.isNetworkConnected(this@ServicesCustomerInteractionActivity)) {
            Utlis.showLoading(this@ServicesCustomerInteractionActivity)
            viewModel.getCategoryServicesCustomerDetails(this)
        } else {
            Toast.makeText(
                this@ServicesCustomerInteractionActivity,
                "No Internet Connection.",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (Preferences.getCommunityAdvisorSiteId().isEmpty()) {
            val i = Intent(this, SelectCommunityAdvisorSiteIdActivity::class.java)
            i.putExtra("modulename", "COMMUNITY_ADVISOR")
            startActivityForResult(i, 781)
        } else {
            activityServicesCustomerInteractionBinding.siteId.setText("${Preferences.getCommunityAdvisorSiteId()} - ${Preferences.getCommunityAdvisorStoreName()}")
        }

        activityServicesCustomerInteractionBinding.leftArrow.setOnClickListener {
            onBackPressed()
        }
        activityServicesCustomerInteractionBinding.siteId.setOnClickListener {
            val i = Intent(this, SelectCommunityAdvisorSiteIdActivity::class.java)
            // i.putExtra("modulename", "COMMUNITY_ADVISOR")
            startActivityForResult(i, 781)
        }

        activityServicesCustomerInteractionBinding.submitButton.setOnClickListener {
            if (validationCheck()) {
                submitDetailsDialog()
            }
        }
    }

    override fun onSuccessGetServicesCustomerResponse(getServicesCustomerResponse: GetServicesCustomerResponse) {
        Utlis.hideLoading()
        if (!getServicesCustomerResponse.listServices.isNullOrEmpty()
            && getServicesCustomerResponse.listServices!!.size > 0
        ) {
            if (isServicesTab) {
                this.servicesCustomerReqList = getServicesCustomerResponse.listServices!!.stream()
                    .filter { o -> o.type.equals("CATEGORY") }
                    .collect(Collectors.toList()) as ArrayList<GetServicesCustomerResponse.ListServices>
                activityServicesCustomerInteractionBinding.servicesActionbar.visibility =
                    View.VISIBLE
                activityServicesCustomerInteractionBinding.customerInteractionActionbar.visibility =
                    View.GONE
            } else {
                this.servicesCustomerReqList = getServicesCustomerResponse.listServices!!.stream()
                    .filter { o -> o.type.equals("CUSTOMER INTERACTION") }
                    .collect(Collectors.toList()) as ArrayList<GetServicesCustomerResponse.ListServices>
                activityServicesCustomerInteractionBinding.servicesActionbar.visibility = View.GONE
                activityServicesCustomerInteractionBinding.customerInteractionActionbar.visibility =
                    View.VISIBLE
            }
            activityServicesCustomerInteractionBinding
            servicesCustomerResponseAdapter =
                ServicesCustomerResponseAdapter(
                    context,
                    servicesCustomerReqList,
                    this@ServicesCustomerInteractionActivity
                )
            activityServicesCustomerInteractionBinding.serviceRecycler.adapter =
                servicesCustomerResponseAdapter
            activityServicesCustomerInteractionBinding.serviceRecycler.layoutManager =
                LinearLayoutManager(context)
        }
    }

    override fun onFailureGetServicesCustomerResponse(message: String) {
        Utlis.hideLoading()
        Toast.makeText(this@ServicesCustomerInteractionActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessGetHomeServicesSaveDetailsResponse(homeServicesSaveDetailsResponse: HomeServicesSaveDetailsResponse) {
        Utlis.hideLoading()
        confirmDialog()
    }

    override fun onFailureGetHomeServicesSaveDetailsResponse(message: String) {
        Utlis.hideLoading()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(listServices: GetServicesCustomerResponse.ListServices) {
        if (servicesCustomerReqList.size > 0) {
            for (i in servicesCustomerReqList) {
                if (i.type.equals("CATEGORY")) {
                    if (i.equals(listServices))
                        i.isSelected = !i.isSelected!!
                } else {
                    i.isSelected = i.equals(listServices)
                }
            }
            servicesCustomerResponseAdapter.notifyDataSetChanged()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            isSiteIdEmpty = data!!.getBooleanExtra("isSiteIdEmpty", isSiteIdEmpty)
            if (requestCode == 781) {
                Utlis.hideLoading()
                if (isSiteIdEmpty) {
                    finish()
                } else {
                    activityServicesCustomerInteractionBinding.siteId.setText("${Preferences.getCommunityAdvisorSiteId()} - ${Preferences.getCommunityAdvisorStoreName()}")
                }
            } else {
                activityServicesCustomerInteractionBinding.siteId.setText("${Preferences.getCommunityAdvisorSiteId()} - ${Preferences.getCommunityAdvisorStoreName()}")
            }
        }
    }

    fun validationCheck(): Boolean {
        val siteId = activityServicesCustomerInteractionBinding.siteId.text.toString().trim()
        val customerName =
            activityServicesCustomerInteractionBinding.customerName.text.toString().trim()
        val mobNumber =
            activityServicesCustomerInteractionBinding.mobileNumber.text.toString().trim()
        val comments =
            activityServicesCustomerInteractionBinding.commentsText.text.toString().trim()

        if (customerName.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_customer_name))
            activityServicesCustomerInteractionBinding.customerName.requestFocus()
            return false
        } /*else if (!isValidCustomerName(customerName)) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_invalid_customer_name))
            activityServicesCustomerInteractionBinding.customerName.requestFocus()
            return false
        }*/
        else if (!isValidCustomerName(customerName)) {
          //  if (customerName.matches(".*\\d+.*")) {
            if (Regex("\\d").containsMatchIn(customerName)){
                showErrorMsg(context?.resources?.getString(R.string.err_msg_contains_numbers))
            } else {
                showErrorMsg(context?.resources?.getString(R.string.err_msg_invalid_customer_name))
            }
            activityServicesCustomerInteractionBinding.customerName.requestFocus()
            return false
        }
        else if (mobNumber.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_mob_number))
            activityServicesCustomerInteractionBinding.mobileNumber.requestFocus()
            return false
        } else if (mobNumber.length != 10) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_mob_number_tendigits))
            activityServicesCustomerInteractionBinding.mobileNumber.requestFocus()
            return false
        } else if (!isAtLeastOneServiceSelected()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_services))
            return false
        }
        else if (comments.isEmpty()) {
            showErrorMsg(context?.resources?.getString(R.string.err_msg_enter_comments))
            activityServicesCustomerInteractionBinding.commentsText.requestFocus()
            return false
        } else {
            return true
        }
    }
    private fun isAtLeastOneServiceSelected(): Boolean {
        return servicesCustomerReqList.any { it.isSelected == true }
    }

    private fun isValidCustomerName(name: String): Boolean {
       // val pattern = Pattern.compile("^(?![\\p{Punct}\\s.0-9]+\$)[A-Za-z.]+\$")   //("^(?![\\p{Punct}\\s.]+\$)[a-zA-Z.]+\$")     //("^[a-zA-Z.]+\$")
        //return pattern.matcher(name).matches()
        val regex="^(?![\\p{Punct}\\s.0-9]+\$)[A-Za-z.]+\$"
        return name.matches(regex.toRegex())
    }

    private fun showErrorMsg(errMsg: String?) {
        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
    }

    private fun submitDetailsDialog() {
        val submitDialog = Dialog(this)
        submitDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var dialogSubmitCommunityadvisorDetailsBinding =
            DialogSubmitCommunityadvisorDetailsBinding.inflate(LayoutInflater.from(this))
        submitDialog.setContentView(dialogSubmitCommunityadvisorDetailsBinding.root)
        submitDialog.setCanceledOnTouchOutside(false)
        dialogSubmitCommunityadvisorDetailsBinding.cancelButton.setOnClickListener {
            submitDialog.dismiss()
        }
        dialogSubmitCommunityadvisorDetailsBinding.submitButton.setOnClickListener {
            val customerName =
                activityServicesCustomerInteractionBinding.customerName.text.toString()
            val mobileNumber =
                activityServicesCustomerInteractionBinding.mobileNumber.text.toString()
            val comments = activityServicesCustomerInteractionBinding.commentsText.text.toString()
            val type = "store"
            if (customerName.isEmpty() || mobileNumber.isEmpty()) {
                return@setOnClickListener
            }
            homeServicesSaveDetailsRequest = HomeServicesSaveDetailsRequest().apply {
                this.serviceDate =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        java.time.LocalDateTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"))
                    } else {
                        java.text.SimpleDateFormat(
                            "dd MMM yyyy hh:mm a",
                            java.util.Locale.getDefault()
                        ).format(java.util.Calendar.getInstance().time)
                    }
                this.customername = customerName
                this.customerMobileno = mobileNumber
                this.createdby = Preferences.getValidatedEmpId()
                this.siteid = Preferences.getCommunityAdvisorSiteId()
                if (isServicesTab) {
                    var servicesId = ""
                    var serviceName = ""
                    for (i in servicesCustomerReqList) {
                        if (i.type.equals("CATEGORY")
                            && i.isSelected == true
                        ) {
                            if (servicesId.isEmpty() && serviceName.isEmpty()) {
                                servicesId = i.serviceId!!
                                serviceName = i.serviceName!!
                            } else {
                                servicesId = "$servicesId,${i.serviceId}"
                                serviceName = "$serviceName,${i.serviceName}"
                            }
                        }
                    }
                    this.serviceName = serviceName
                    this.serviceid = servicesId
                    this.others = comments
                    this.customerInteractionid = ""
                    this.customerInteractionremarks = ""
                    this.serviceType = "SERVICE"
                } else {
                    var customerInteractionId = ""
                    var serviceName = ""
                    for (i in servicesCustomerReqList) {
                        if (i.type.equals("CUSTOMER INTERACTION")
                            && i.isSelected == true
                        ) {
                            if (customerInteractionId.isEmpty() && serviceName.isEmpty()) {
                                customerInteractionId = i.serviceId!!
                                serviceName = i.serviceName!!
                            } else {
                                customerInteractionId = "$customerInteractionId,${i.serviceId}"
                                serviceName = "$serviceName,${i.serviceName}"
                            }
                        }
                    }
                    this.serviceName = serviceName
                    this.customerInteractionid = customerInteractionId
                    this.customerInteractionremarks = comments
                    this.serviceid = ""
                    this.others = ""
                    this.serviceType = "CUSTOMER"
                }
                this.type = type
            }

            if (NetworkUtils.isNetworkConnected(this@ServicesCustomerInteractionActivity)) {
                Utlis.showLoading(this@ServicesCustomerInteractionActivity)
                viewModel.getHomeServicesSaveDetails(homeServicesSaveDetailsRequest, this)

            } else {
                Toast.makeText(
                    this@ServicesCustomerInteractionActivity,
                    "No Internet Connection.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            submitDialog.dismiss()
        }
        submitDialog.show()
    }

    private fun confirmDialog() {
        val confirm = Dialog(this)
        confirm.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogConfirmSavedetailsBinding =
            DialogConfirmSavedetailsBinding.inflate(LayoutInflater.from(this))
        confirm.setContentView(dialogConfirmSavedetailsBinding.root)
        confirm.setCanceledOnTouchOutside(false)
        dialogConfirmSavedetailsBinding.yesBtn.setOnClickListener {
            confirm.dismiss()
            setResult(Activity.RESULT_OK)
            finish()
        }
        confirm.show()
    }
}