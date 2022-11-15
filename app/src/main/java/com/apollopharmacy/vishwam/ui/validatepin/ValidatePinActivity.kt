package com.apollopharmacy.vishwam.ui.validatepin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.BuildConfig
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.MPinRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.ui.home.MainActivity
import com.apollopharmacy.vishwam.ui.login.LoginActivity
import com.apollopharmacy.vishwam.util.ForgotPinActivity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.google.gson.Gson

@Suppress("DEPRECATION")
class ValidatePinActivity : AppCompatActivity() {

    lateinit var viewModel: ValidatePinViewModel
    private val REQUEST_CODE_ENABLE = 11
    private val REQUEST_CODE_CHANGE = 13
    lateinit var userData: LoginDetails
    private var isAppAvailability: Boolean = false
    private var availabilityMsg: String = ""
    private var isForceDownload: Boolean = false
    private var buildMessage: String = ""
    private var downloadUrl: String = ""
    private var serviceAppVer: Int = 0
    private var currentAppVer: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pin)

        viewModel = ViewModelProvider(this)[ValidatePinViewModel::class.java]
        userData = LoginRepo.getProfile()!!

        onCheckBuildDetails()
        handleMPinService()

        viewModel.commands.observeForever({ command ->
            Utlis.hideLoading()
            when (command) {
                is Command.NavigateTo -> {
                    val intent = Intent(this@ValidatePinActivity, ForgotPinActivity::class.java)
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN)
                    intent.putExtra("validatePin", command.value.Mpin)
                    intent.putExtra("isAppAvailability", isAppAvailability)
                    intent.putExtra("availabilityMsg", availabilityMsg)
                    intent.putExtra("isForceDownload", isForceDownload)
                    intent.putExtra("buildMessage", buildMessage)
                    intent.putExtra("downloadUrl", downloadUrl)
                    intent.putExtra("serviceAppVer", serviceAppVer)
                    intent.putExtra("currentAppVer", currentAppVer)
                    startActivityForResult(intent, REQUEST_CODE_ENABLE)
                }
                is Command.ShowToast -> {
                    ViswamApp.context.let {
                        Toast.makeText(it, command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

//        viewModel.employeeDetails.observeForever {
//            if(it.data?.uploadSwach?.uid!=null){
//                Preferences.setEmployeeRoleUid(it.data?.uploadSwach?.uid!!)
//            }else{
//                Preferences.setEmployeeRoleUid("")
//            }
//
//        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ENABLE) {
            if (resultCode == RESULT_OK) {
                val dialogStatus = data!!.getBooleanExtra("showDialog", false)
                viewModel.getRole(Preferences.getValidatedEmpId())
                viewModel.getApplevelDesignation(Preferences.getValidatedEmpId(), "SWACHH")
                viewModel.getApplevelDesignationQcFail(Preferences.getValidatedEmpId(), "QCFAIL")


                viewModel.appLevelDesignationRespSwach.observeForever {

                    if(it.message!=null && it.status.equals(true)){
                    }else{
//                        Preferences.setAppLevelDesignationSwach("")
                    }

                }

                viewModel.appLevelDesignationRespQCFail.observeForever {
                    if(it.message!=null && it.status.equals(true)){
                        Preferences.setAppLevelDesignationQCFail(it.message)
//                        Toast.makeText(applicationContext, "QcFail: "+Preferences.getAppLevelDesignationQCFail(), Toast.LENGTH_SHORT).show();
                    }else{
                        Preferences.setAppLevelDesignationQCFail("")
                    }
                }
                viewModel.employeeDetails.observeForever {

                    if (it.data != null && it.data?.uploadSwach != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
                        Preferences.storeEmployeeDetailsResponseJson(Gson().toJson(it))
                        if (it.data?.uploadSwach?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                            Preferences.setEmployeeRoleUid(it.data?.uploadSwach?.uid!!)
                            if (it.data?.uploadSwach?.uid!!.equals("Yes",
                                    true)
                            ) {
                                if (it.data?.swacchDefaultSite != null && it.data?.swacchDefaultSite?.site != null) {
                                    Preferences.setSwachhSiteId(it.data?.swacchDefaultSite?.site!!)
                                } else {
                                    Preferences.setSwachhSiteId("")
                                }
                            }

                        } else {
                            Preferences.setEmployeeRoleUid("")
                        }
                    }
                    else {
                        Preferences.setEmployeeRoleUid("")
                    }

                    if (it.data != null && it.data?.newDrugRequest != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
                        Preferences.storeEmployeeDetailsResponseJsonNewDrug(Gson().toJson(it))
                        if (it.data?.newDrugRequest?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                            Preferences.setEmployeeRoleUidNewDrugRequest(it.data?.newDrugRequest?.uid!!)
                            if (it.data?.newDrugRequest?.uid!!.equals("Yes",
                                    true)
                            ) {
                             Preferences.setEmployeeRoleUidNewDrugRequest(it.data?.newDrugRequest?.uid!!)
                            }else{
                                Preferences.setEmployeeRoleUidNewDrugRequest("")
                            }

                        } else {
                            Preferences.setEmployeeRoleUidNewDrugRequest("")
                        }
                    }
                    else {
                        Preferences.setEmployeeRoleUidNewDrugRequest("")
                    }

                    if (dialogStatus) {
//                    viewModel.getRole(Preferences.getValidatedEmpId())
                        handleCreatePinIntent()

//                    handlePlayStoreIntent()
                    } else {
//                    viewModel.getRole(Preferences.getValidatedEmpId())
                        handleNextIntent()

                    }

                }





//                if (dialogStatus) {
////                    viewModel.getRole(Preferences.getValidatedEmpId())
//                    handleCreatePinIntent()
//
////                    handlePlayStoreIntent()
//                } else {
////                    viewModel.getRole(Preferences.getValidatedEmpId())
//                    handleNextIntent()
//
//                }

            } else {
                Toast.makeText(this, "Invalid Pin", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == REQUEST_CODE_CHANGE) {
            if (resultCode == RESULT_OK) {
                val dialogStatus = data!!.getBooleanExtra("showDialog", false)
                Toast.makeText(this, "PinCode Dialog : " + dialogStatus, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun handleNextIntent() {
        Preferences.setIsPinCreated(true)
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
        finish()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun handleCreatePinIntent() {
        val homeIntent = Intent(this, LoginActivity::class.java)
        homeIntent.putExtra("isUpdatePwd", true)
        startActivity(homeIntent)
        finish()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

//        val homeIntent = Intent(this, CreatePinActivity::class.java)
//        homeIntent.putExtra("isUpdatePwd", true)
//        startActivity(homeIntent)
//        finish()
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun handlePlayStoreIntent() {
        Utils.printMessage("ValidatePin", "Download URL : " + downloadUrl)
        val viewIntent = Intent("android.intent.action.VIEW",
            Uri.parse(downloadUrl))
        startActivity(viewIntent)
    }

    private fun handleMPinService() {
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this)
            viewModel.checkMPinLogin(MPinRequest(userData.EMPID, "", "GETDETAILS"))
        } else {
            Toast.makeText(
                this,
                resources?.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun onCheckBuildDetails() {
        val globalRes = Preferences.getGlobalResponse()
        val data = Gson().fromJson(globalRes, ValidateResponse::class.java)
        val buildDetailsEntity = data.BUILDDETAILS
        if (buildDetailsEntity != null) {
            if (buildDetailsEntity.APPAVALIBALITY) {
                isAppAvailability = true
                downloadUrl = buildDetailsEntity.DOWNLOADURL
                buildMessage = buildDetailsEntity.BUILDMESSAGE
                serviceAppVer = buildDetailsEntity.BUILDVERSION.toInt()
                currentAppVer = BuildConfig.VERSION_CODE
                if (buildDetailsEntity.FORCEDOWNLOAD) {
                    isForceDownload = true
                }
            } else {
                isAppAvailability = false
                availabilityMsg = buildDetailsEntity.AVABILITYMESSAGE
            }
        }
    }
}