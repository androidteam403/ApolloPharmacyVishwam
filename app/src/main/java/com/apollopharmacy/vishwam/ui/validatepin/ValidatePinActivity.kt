package com.apollopharmacy.vishwam.ui.validatepin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.apollopharmacy.vishwam.ui.home.notification.model.NotificationModelResponse
import com.apollopharmacy.vishwam.ui.home.swach.model.AppLevelDesignationModelResponse
import com.apollopharmacy.vishwam.ui.login.LoginActivity
import com.apollopharmacy.vishwam.util.*
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.google.android.gms.tasks.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson

@Suppress("DEPRECATION")
class ValidatePinActivity : AppCompatActivity(), ValidatePinCallBack {

    lateinit var viewModel: ValidatePinViewModel
    lateinit var validatePinCallBack: ValidatePinCallBack
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
    private var firebaseToken: String? = null
    private var notificationResponse: NotificationModelResponse? = null

    private var module: String? = ""
    private var uniqueId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pin)

        viewModel = ViewModelProvider(this)[ValidatePinViewModel::class.java]

        userData = LoginRepo.getProfile()!!

        if (intent != null) {
            if (intent.getStringExtra("MODULE") != null)
                module = intent.getStringExtra("MODULE") as String
            if (intent.getStringExtra("UNIQUE_ID") != null)
                uniqueId = intent.getStringExtra("UNIQUE_ID") as String
        }

        validatePinCallBack=this
//        onCheckBuildDetails()
        handleMPinService()
        viewModel.getApplevelDesignation(
            Preferences.getValidatedEmpId(), "SWACHH", applicationContext
        )
        viewModel.getApplevelDesignationQcFail(Preferences.getValidatedEmpId(), "QCFAIL")
        Preferences.setSiteIdListFetchedQcFail(false)
        Preferences.setSiteIdListQcFail("")
        Preferences.setSiteIdListChamps("")
        Preferences.setSiteIdListFetchedChamps(false)
        Preferences.setRegionIdListFetchedQcFail(false)
        Preferences.setRegionIdListQcFail("")

        Preferences.setDoctorSpecialityListFetched(false)
        Preferences.setItemTypeListFetched(false)
        Preferences.setSiteIdListFetched(false)
        Preferences.setQrSiteIdListFetched(false)
        Preferences.setSiteIdListFetchedQrRetro(false)

        Preferences.setSiteRetroListFetched(false)
        Preferences.setReasonListFetched(false)
        viewModel.commands.observeForever { command ->
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

                else -> {}
            }
        }
        FirebaseMessaging.getInstance().token.addOnSuccessListener(OnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                firebaseToken = token
                Log.d("newToken", "retrieve token successful : $token")
            } else {
                Log.w("newToken", "token should not be null...")
            }
        }).addOnFailureListener(OnFailureListener { e: Exception? -> })
            .addOnCanceledListener(OnCanceledListener {})
            .addOnCompleteListener(OnCompleteListener { task: Task<String> ->
                Log.v(
                    "newToken", "This is the token : " + task.result
                )
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
//                handleNextIntent()

                viewModel.getRole(Preferences.getValidatedEmpId(), validatePinCallBack)
                viewModel.getApplevelDesignation(
                    Preferences.getValidatedEmpId(), "SWACHH", applicationContext
                )

                viewModel.getApplevelDesignationApnaRetro(
                    Preferences.getValidatedEmpId(), "RETRO", applicationContext, this
                )
                viewModel.getApplevelDesignationQcFail(Preferences.getValidatedEmpId(), "QCFAIL")


                viewModel.appLevelDesignationRespSwach.observeForever {

                    if (it.message != null && it.status.equals(true)) {

                    } else {
//                        Preferences.setAppLevelDesignationSwach("")
                    }

                }

                viewModel.appLevelDesignationRespQCFail.observeForever {
                    if (it.message != null && it.status.equals(true)) {
                        Preferences.setAppLevelDesignationQCFail(it.message)
//                        Toast.makeText(applicationContext, "QcFail: "+Preferences.getAppLevelDesignationQCFail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Preferences.setAppLevelDesignationQCFail("")
                    }
                }
                viewModel.employeeDetails.observeForever {
                    if(it.success!!){
                        Preferences.setEmployeeApiAvailable(true);
                        if (it.success!! && it.data != null && it.data?.uploadSwach != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"

//                        it.data!!.role!!.code="region_head"
                            Preferences.storeEmployeeDetailsResponseJson(Gson().toJson(it))
                            Preferences.setRoleForCeoDashboard(it.data!!.role!!.code.toString())
//                       Toast.makeText(applicationContext, ""+ Preferences.getRoleForCeoDashboard(), Toast.LENGTH_SHORT).show()
                            if (it.data?.uploadSwach?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                Preferences.setEmployeeRoleUid(it.data?.uploadSwach?.uid!!)
                                if (it.data?.uploadSwach?.uid!!.equals(
                                        "Yes", true
                                    )
                                ) {
                                    if (it.data?.swacchDefaultSite != null && it.data?.swacchDefaultSite?.site != null) {
                                        Preferences.setSwachhSiteId(it.data?.swacchDefaultSite?.site!!)
                                    } else {
                                        Preferences.setSwachhSiteId("")
                                    }
                                }

                            } else {
                                Preferences.setEmployeeRoleUid("No")
                            }
                        } else {
                            Preferences.setEmployeeRoleUid("No")
                        }

                        if (it.data != null && it.data?.uploadApnaRetroQr != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
                            Preferences.storeEmployeeDetailsResponseJsonRetroQr(Gson().toJson(it))
                            if (it.data?.uploadApnaRetroQr?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                Preferences.setEmployeeRoleUidRetroQr(it.data?.uploadApnaRetroQr?.uid!!)
                                if (it.data?.uploadApnaRetroQr?.uid!!.equals(
                                        "Yes", true
                                    )
                                ) {
                                    Preferences.setRetroQrEmployeeRoleUid(it.data?.uploadApnaRetroQr?.uid!!)
                                } else {
                                    Preferences.setRetroQrEmployeeRoleUid("")
                                }

                            } else {
                                Preferences.setRetroQrEmployeeRoleUid("")
                            }
                        } else {
                            Preferences.setRetroQrEmployeeRoleUid("")
                        }

                        if (it.data != null && it.data?.uploadApnaRetro != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
                            Preferences.storeEmployeeDetailsResponseJsonNewDrug(Gson().toJson(it))
                            if (it.data?.uploadApnaRetro?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                Preferences.setEmployeeRoleUidNewDrugRequest(it.data?.uploadApnaRetro?.uid!!)
                                if (it.data?.uploadApnaRetro?.uid!!.equals(
                                        "Yes", true
                                    )
                                ) {
                                    Preferences.setRetroEmployeeRoleUid(it.data?.uploadApnaRetro?.uid!!)
                                } else {
                                    Preferences.setRetroEmployeeRoleUid("")
                                }

                            } else {
                                Preferences.setRetroEmployeeRoleUid("")
                            }
                        } else {
                            Preferences.setRetroEmployeeRoleUid("")
                        }

                        if (it.data != null && it.data?.champs_admin != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
//                        Preferences.storeEmployeeDetailsResponseJsonChampsAdmin(Gson().toJson(it))
                            if (it.data?.champs_admin?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                if (it.data?.champs_admin?.uid != null && it.data?.champs_admin?.uid!!.equals(
                                        "Yes", true
                                    )
                                ) {
                                    Preferences.setEmployeeRoleUidChampsAdmin(it.data?.champs_admin?.uid!!)
                                } else {
                                    Preferences.setEmployeeRoleUidChampsAdmin("")
                                }

                            } else {
                                Preferences.setEmployeeRoleUidChampsAdmin("")
                            }
                        } else {
                            Preferences.setEmployeeRoleUidChampsAdmin("")
                        }

                        if (it.data != null && it.data?.newDrugRequest != null) {
//                        it.data!!.role!!.code = "store_supervisor"
//                        it.data!!.uploadSwach!!.uid = "Yes"
                            Preferences.storeEmployeeDetailsResponseJsonNewDrug(Gson().toJson(it))
                            if (it.data?.newDrugRequest?.uid != null) {
//                            it.data?.uploadSwach?.uid = "Yes"
//                            it.data?.swacchDefaultSite?.site = ""
                                Preferences.setEmployeeRoleUidNewDrugRequest(it.data?.newDrugRequest?.uid!!)
                                if (it.data?.newDrugRequest?.uid!!.equals(
                                        "Yes", true
                                    )
                                ) {
                                    Preferences.setEmployeeRoleUidNewDrugRequest(it.data?.newDrugRequest?.uid!!)
                                } else {
                                    Preferences.setEmployeeRoleUidNewDrugRequest("")
                                }

                            } else {
                                Preferences.setEmployeeRoleUidNewDrugRequest("")
                            }
                        } else {
                            Preferences.setEmployeeRoleUidNewDrugRequest("")
                        }

                        if (dialogStatus) {
//                    viewModel.getRole(Preferences.getValidatedEmpId())
                            handleCreatePinIntent()

//                    handlePlayStoreIntent()
                        } else {
                            viewModel.getNotificationDetailsApi(this)
//                    viewModel.getRole(Preferences.getValidatedEmpId())
//                        handleNextIntent()

                        }

                    }else{
                        Preferences.setEmployeeApiAvailable(false);
                        onHandleNextEvent()
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
                Toast.makeText(this, "Invalid Pin", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_CHANGE) {
            if (resultCode == RESULT_OK) {
                val dialogStatus = data!!.getBooleanExtra("showDialog", false)
                Toast.makeText(this, "PinCode Dialog : " + dialogStatus, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleNextIntent() {//"Kiran99"//Preferences.getValidatedEmpId()//emp-102
//        viewModel.loginApiCall("APL48627",
//            "R1De6#012022",
//            Preferences.getFcmKey(),
//            this,
//            this)
//emp-102//Nagapavan
        Preferences.setIsPinCreated(true)
        val homeIntent = Intent(this, MainActivity::class.java)
        if(notificationResponse!=null){
            homeIntent.putExtra("notificationResponse", notificationResponse)
        }
        homeIntent.putExtra("MODULE", module)
        homeIntent.putExtra("UNIQUE_ID", uniqueId)
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
        val viewIntent = Intent(
            "android.intent.action.VIEW", Uri.parse(downloadUrl)
        )
        startActivity(viewIntent)
    }

    private fun handleMPinService() {
        if (NetworkUtil.isNetworkConnected(this)) {
            Utlis.showLoading(this@ValidatePinActivity)
            viewModel.checkMPinLogin(MPinRequest(userData.EMPID, "", "GETDETAILS"))
        } else {
            Toast.makeText(
                this, resources?.getString(R.string.label_network_error), Toast.LENGTH_SHORT
            ).show()
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


    override fun onFailureLoginApi(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFialureMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onFailureGetProfileDetailsApi(s: String) {
        Toast.makeText(applicationContext, "" + s, Toast.LENGTH_SHORT).show()
    }


    override fun onSuccessAppLevelDesignationApnaRetro(value: AppLevelDesignationModelResponse) {
        if (value.message != null && value.status.equals(true)) {
            Preferences.setAppLevelDesignationApnaRetro(value.message)
//                        Toast.makeText(applicationContext, "QcFail: "+Preferences.getAppLevelDesignationQCFail(), Toast.LENGTH_SHORT).show();
        } else {
            Preferences.setAppLevelDesignationApnaRetro("")
        }
    }

    override fun onFailureAppLevelDesignationApnaRetro(value: AppLevelDesignationModelResponse) {

    }

    override fun onSuccessNotificationDetails(response: NotificationModelResponse) {
        if (response != null && response.data != null && response.data!!.listData != null
            && response.data!!.listData!!.rows != null && response.data!!.listData!!.rows!!.size > 0
        ) {
            notificationResponse = response
//            var res = NotificationModelResponse()
//            var data = res.Data()
//            var listData = data.ListData()
//            var rowssList = ArrayList<NotificationModelResponse.Data.ListData.Row>()
//            var rows = listData.Row()
//            rows.createdTime="2023-11-20 17:16:20"
//            rows.description="Lorem Ipsum is simply"
//            rows.title="SS"
//            rows.link="https://chat.openai.com/c/0fdc32c6-982f-4df1-975c-ffec8596377a"
//            rowssList.add(rows)
//            listData.rows= rowssList
//            data.listData=listData
//            res.data=data
        }
        handleNextIntent()
    }

    override fun onFailureNotificationDetails(value: NotificationModelResponse) {
        handleNextIntent()
    }

    override fun onFailureNotificationDetail() {
        handleNextIntent()
    }

    override fun onHandleNextEvent() {
        Preferences.setEmployeeApiAvailable(false);
        handleNextIntent()
    }

}