package com.apollopharmacy.vishwam.ui.validatepin

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.*
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.network.ApiClient
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.swach.model.AppLevelDesignationModelResponse
import com.apollopharmacy.vishwam.ui.rider.db.SessionManager
import com.apollopharmacy.vishwam.ui.rider.login.BackSlash
import com.apollopharmacy.vishwam.ui.rider.login.LoginActivityController
import com.apollopharmacy.vishwam.ui.rider.login.model.*
import com.apollopharmacy.vishwam.ui.rider.login.model.LoginRequest
import com.apollopharmacy.vishwam.ui.rider.login.model.SaveUserDeviceInfoRequest.DeviceInfo
import com.apollopharmacy.vishwam.ui.rider.orderdelivery.model.DeliveryFailreReasonsResponse
import com.apollopharmacy.vishwam.ui.rider.profile.model.GetRiderProfileResponse
import com.apollopharmacy.vishwam.util.AppConstants
import com.apollopharmacy.vishwam.util.Utlis.getDeviceId
import com.apollopharmacy.vishwam.util.Utlis.getDeviceName
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.signaturepad.ActivityUtils
import com.apollopharmacy.vishwam.util.signaturepad.NetworkUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ValidatePinViewModel : ViewModel() {

    val commands = LiveEvent<Command>()
    val state = MutableLiveData<State>()
    var employeeDetails = MutableLiveData<EmployeeDetailsResponse>()
    var appLevelDesignationRespSwach = MutableLiveData<AppLevelDesignationModelResponse>()
    var appLevelDesignationRespQCFail = MutableLiveData<AppLevelDesignationModelResponse>()
    fun checkMPinLogin(mPinRequest: MPinRequest) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("MPIN")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        state.postValue(State.LOADING)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                LoginRepo.checkMPinDetails(baseUrl, token, mPinRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.Status) {
                        state.value = State.ERROR
//                        commands.postValue(Command.ShowToast("Successfully login"))
//                        Preferences.saveSiteId("")
//                        LoginRepo.saveProfile(result.value)
//                        Preferences.savingToken(result.value.EMPID)
//                        Preferences.storeLoginJson(Gson().toJson(result.value))
                        commands.value = Command.NavigateTo(result.value)
                    } else {
                        state.value = State.ERROR
                        commands.value = Command.ShowToast(result.value.Message)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }


    fun getApplevelDesignation(
        empId: String,
        appType: String,
        applicationContext: Context,

        ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISWAM APP LEVEL DESIGNATION")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }




        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                RegistrationRepo.getApplevelDesignation(baseUrl, empId, appType)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        Preferences.setAppLevelDesignationSwach(result.value.message)
//                       Toast.makeText(applicationContext, ""+Preferences.getAppLevelDesignationSwach(),Toast.LENGTH_SHORT).show()
                    } else {
                        appLevelDesignationRespSwach.value = result.value
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    fun getApplevelDesignationApnaRetro(
        empId: String,
        appType: String,
        applicationContext: Context,
        validatePinCallBack: ValidatePinCallBack

        ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISWAM APP LEVEL DESIGNATION")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }




        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                RegistrationRepo.getApplevelDesignation(baseUrl, empId, appType)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        validatePinCallBack.onSuccessAppLevelDesignationApnaRetro(result.value)
//                       Toast.makeText(applicationContext, ""+Preferences.getAppLevelDesignationSwach(),Toast.LENGTH_SHORT).show()
                    } else {
                        validatePinCallBack.onFailureAppLevelDesignationApnaRetro(result.value)
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    fun getApplevelDesignationQcFail(
        empId: String,
        appType: String,

        ) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISWAM APP LEVEL DESIGNATION")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                RegistrationRepo.getApplevelDesignation(baseUrl, empId, appType)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        appLevelDesignationRespQCFail.value = result.value
                    } else {
                        appLevelDesignationRespQCFail.value = result.value
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(result.error?.let {
                        Command.ShowToast(it)
                    })
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }


    fun getRole(validatedEmpId: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)


        var proxyUrl = ""
        var proxyToken = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                proxyUrl = data.APIS[i].URL
                proxyToken = data.APIS[i].TOKEN
                break
            }
        }

//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS TICKETLIST")) {
//                val baseUrl = data.APIS[i].URL
        // "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/reason/list/reason-list?page=1&rows=100"
        //val token = data.APIS[i].TOKEN
//
//                val new = if (status.contains("new")) "new" else ""
//                val inprogress = if (status.contains("inprogress")) "inprogress" else ""
//                val solved = if (status.contains("solved")) "solved" else ""
//                val rejected = if (status.contains("rejected")) "rejected" else ""
//                val reopened = if (status.contains("reopened")) "reopened" else ""
//                val closed = if (status.contains("closed")) "closed" else ""

        //
        //https://apis.v35.dev.zeroco.de
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("SWND employee-details-mobile")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
//        val baseUrl: String =
//            "https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/user/select/employee-details-mobile?emp_id=${validatedEmpId}"

//"https://cmsuat.apollopharmacy.org/zc-v3.1-user-svc/2.0/apollo_cms/api/ticket/list/mobile-ticket-list-by-emp-id?&employee_id=${requestComplainList.empid}&status=${status}&from_date=${requestComplainList.fromDate}&to_date=${requestComplainList.toDate}&page=${requestComplainList.page}&rows=10"
        viewModelScope.launch {
            state.value = State.SUCCESS
            val response = withContext(Dispatchers.IO) {
                RegistrationRepo.getDetails(proxyUrl,proxyToken,
                    GetDetailsRequest(baseUrl+"?emp_id=${validatedEmpId}", "GET", "The", "", ""))
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    if (response != null) {
                        val resp: String = response.value.string()
                        if (resp != null) {
                            val res = BackShlash.removeBackSlashes(resp)
                            val responseNewTicketlist =
                                Gson().fromJson(BackShlash.removeSubString(res),
                                    EmployeeDetailsResponse::class.java)
                            if (responseNewTicketlist.success!!) {
                                employeeDetails.value = responseNewTicketlist

//                                Preferences.getEmployeeRoleUid()
//                                        newcomplainLiveData.value =
//                                            responseNewTicketlist.data.listData.rows
                            } else {
                                employeeDetails.value = responseNewTicketlist
//                                commands.value =
//                                 Command.ShowToast(responseNewTicketlist.message.toString())
                            }
                        }
                        //  unComment it  newcomplainLiveData.value = response.value.data.listData.rows
                        //  Ticketlistdata = response.value
                        //  val reasonlitrows = response.value.data.listData.rows
                        // for (row in reasonlitrows) {
                        //  deartmentlist.add(row.department)
                        // }
                    } else {
                        //  unComment it   command.value = CmsCommand.ShowToast(response.value.message.toString())
                    }
                }
                is ApiResult.GenericError -> {
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    state.value = State.ERROR
                }
            }
        }
//            }
//        }
    }


    fun loginApiCall(
        userName: String?,
        password: String?,
        firebaseToken: String?,
        applicationContext: Context,
        validatePinCallBack: ValidatePinCallBack,
    ) {
        if (NetworkUtils.isNetworkConnected(applicationContext)) {
            ActivityUtils.showDialog(applicationContext, "Please wait.")
            val apiInterface = ApiClient.getApiService()
            val loginRequest = LoginRequest()
            loginRequest.appUserName = userName
            loginRequest.appPassword = password
            val gson = Gson()
            val jsonLoginRequest = gson.toJson(loginRequest)
            val getDetailsRequest =
                com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest()
            getDetailsRequest.requesturl = AppConstants.BASE_URL + "login"
            getDetailsRequest.requestjson = jsonLoginRequest
            getDetailsRequest.headertokenkey = ""
            getDetailsRequest.headertokenvalue = ""
            getDetailsRequest.requesttype = "POST"
            val calls = apiInterface.getDetails(
                AppConstants.PROXY_URL,
                AppConstants.PROXY_TOKEN,
                getDetailsRequest
            )
            calls.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>,
                ) {
                    if (response.body() != null) {
                        ActivityUtils.hideDialog()
                        var resp: String? = null
                        try {
                            resp = response.body()!!.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            val res = BackSlash.removeBackSlashes(resp)


                            val gson = Gson()
//                            val req = BackSlash.removeSubString(res!!)
                            val loginResponse: LoginResponse=
                           gson.fromJson(
                                BackSlash.removeSubString(res!!),
                                LoginResponse::class.java
                            )
                            if (loginResponse != null && loginResponse.data!=null && loginResponse.success) {
                                validatePinCallBack.onSuccessLoginApi(loginResponse)
                                orderPaymentTypelistApiCall(applicationContext, validatePinCallBack)
                                saveUserDeviceInfoApiCall(firebaseToken, applicationContext, validatePinCallBack)
                                firebaseToken(firebaseToken, applicationContext, validatePinCallBack)
                            } else if (loginResponse != null && !loginResponse.success) {
                                ActivityUtils.hideDialog()
                                validatePinCallBack.onFailureLoginApi(loginResponse.message)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    validatePinCallBack.onFailureLoginApi(t.message)
                }
            })
        } else {
            validatePinCallBack.onFailureLoginApi("Something went wrong.")
        }
    }

    fun orderPaymentTypelistApiCall(context: Context, validatePinCallBack: ValidatePinCallBack) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.")
            val apiInterface = ApiClient.getApiService()
            val gson = Gson()
            val getDetailsRequest =
                com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest()
            getDetailsRequest.requesturl =
                AppConstants.BASE_URL + "api/choose-data/order_payment_type"
            getDetailsRequest.requestjson = "The"
            getDetailsRequest.headertokenkey = ""
            getDetailsRequest.headertokenvalue = ""
            getDetailsRequest.requesttype = "GET"
            val calls = apiInterface.getDetails(
                AppConstants.PROXY_URL,
                AppConstants.PROXY_TOKEN,
                getDetailsRequest
            )
            calls.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>,
                ) {
                    if (response.body() != null) {
                        ActivityUtils.hideDialog()
                        var resp: String? = null
                        try {
                            resp = response.body()!!.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            val res = BackSlash.removeBackSlashes(resp)
                            val gson = Gson()
                            val orderPaymentTypeResponse = gson.fromJson(
                                BackSlash.removeSubString(res),
                                OrderPaymentTypeResponse::class.java
                            )
                            if (orderPaymentTypeResponse != null && orderPaymentTypeResponse.success) {
                                SessionManager(context).orderPaymentTypeList =
                                    orderPaymentTypeResponse
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    validatePinCallBack.onFialureMessage(t.message)
                }
            })
        } else {
            validatePinCallBack.onFialureMessage("Something went wrong.")
        }
    }

    fun saveUserDeviceInfoApiCall(
        firebaseToken: String?,
        applicationContext: Context,
        validatePinCallBack: ValidatePinCallBack,
    ) {
        if (NetworkUtils.isNetworkConnected(applicationContext)) {
            ActivityUtils.showDialog(applicationContext, "Please wait.")
            val apiInterface = ApiClient.getApiService()
            val saveUserDeviceInfoRequest = SaveUserDeviceInfoRequest()
            val deviceInfo = DeviceInfo()
            deviceInfo.mac_id = getDeviceId(applicationContext)
            deviceInfo.device_name = getDeviceName()
            deviceInfo.manufacture = Build.MANUFACTURER
            deviceInfo.brand = Build.BRAND
            deviceInfo.user = Build.USER
            deviceInfo.version_sdk = Build.VERSION.SDK
            deviceInfo.fingerprint = Build.FINGERPRINT
            deviceInfo.app_version_code = LoginActivityController.VERSION_CODE.toString()
            deviceInfo.app_version_name = LoginActivityController.VERSION_NAME
            saveUserDeviceInfoRequest.deviceInfo = deviceInfo
            saveUserDeviceInfoRequest.firebaseId = firebaseToken
            val gson = Gson()
            val jsonUserDeviceRequest = gson.toJson(saveUserDeviceInfoRequest)
            val getDetailsRequest =
                com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest()
            getDetailsRequest.requesturl =
                AppConstants.BASE_URL + "api/user/save-update/update-user-device-info"
            getDetailsRequest.requestjson = jsonUserDeviceRequest
            getDetailsRequest.headertokenkey = "authorization"
            getDetailsRequest.headertokenvalue = "Bearer " + SessionManager(applicationContext).loginToken
            getDetailsRequest.requesttype = "POST"
            val calls = apiInterface.getDetails(
                AppConstants.PROXY_URL,
                AppConstants.PROXY_TOKEN,
                getDetailsRequest
            )
            calls.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>,
                ) {
                    if (response.body() != null) {
                        var resp: String? = null
                        ActivityUtils.hideDialog()
                        try {
                            resp = response.body()!!.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            val res = BackSlash.removeBackSlashes(resp)
                            val gson = Gson()
                            val saveUserDeviceInfoResponse = gson.fromJson(
                                BackSlash.removeSubString(res),
                                SaveUserDeviceInfoResponse::class.java
                            )
                            if (saveUserDeviceInfoResponse != null && saveUserDeviceInfoResponse.success != null) {
                            } else if (saveUserDeviceInfoResponse != null && !saveUserDeviceInfoResponse.success) {
                                ActivityUtils.hideDialog()
                                validatePinCallBack.onFialureMessage(saveUserDeviceInfoResponse.message)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    validatePinCallBack.onFialureMessage(t.message)
                }
            })
        } else {
            validatePinCallBack.onFialureMessage("Something went wrong.")
        }
    }
    fun firebaseToken(
        firebaseToken: String?,
        applicationContext: Context,
        validatePinCallBack: ValidatePinCallBack,
    ) {
        if (NetworkUtils.isNetworkConnected(applicationContext)) {
            ActivityUtils.showDialog(applicationContext, "Please wait.")
            val apiInterface = ApiClient.getApiService()
            val firebaseTokenRequest = FirebaseTokenRequest()
            firebaseTokenRequest.firebaseToken = firebaseToken
            val gson = Gson()
            val jsonfirebaseTokenRequest = gson.toJson(firebaseToken)
            val getDetailsRequest =
                com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest()
            getDetailsRequest.requesturl = AppConstants.BASE_URL + "updateFirebaseToken"
            getDetailsRequest.requestjson = jsonfirebaseTokenRequest
            getDetailsRequest.headertokenkey = "authorization"
            getDetailsRequest.headertokenvalue = "Bearer " + SessionManager(applicationContext).loginToken
            getDetailsRequest.requesttype = "POST"
            val calls = apiInterface.getDetails(
                AppConstants.PROXY_URL,
                AppConstants.PROXY_TOKEN,
                getDetailsRequest
            )
            calls.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>,
                ) {
                    if (response.body() != null) {
                        var resp: String? = null
                        ActivityUtils.hideDialog()
                        try {
                            resp = response.body()!!.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            val res = BackSlash.removeBackSlashes(resp)
                            val gson = Gson()
                            val `object` = gson.fromJson(
                                BackSlash.removeSubString(res),
                                Any::class.java
                            )
                            if (`object` != null) {
                            } else if (`object` != null) {
                                ActivityUtils.hideDialog()
                                validatePinCallBack.onFialureMessage("Something went wrong")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    hideLoading()
                    validatePinCallBack.onFialureMessage(t.message)
                }
            })
        } else {
            validatePinCallBack.onFialureMessage("Something went wrong.")
        }
    }

    fun getRiderProfileDetailsApi(token: String, applicationContext: Context, validatePinCallBack: ValidatePinCallBack) {
        if (NetworkUtils.isNetworkConnected(applicationContext)) {
            ActivityUtils.showDialog(applicationContext, "Please wait.")
            val apiInterface = ApiClient.getApiService()
            val getDetailsRequest =
                com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest()
            getDetailsRequest.requesturl =
                AppConstants.BASE_URL + "api/user/select/rider-profile-select"
            getDetailsRequest.headertokenkey = "authorization"
            getDetailsRequest.headertokenvalue = "Bearer $token"
            getDetailsRequest.requestjson = "The"
            getDetailsRequest.requesttype = "GET"
            val call = apiInterface.getDetails(
                AppConstants.PROXY_URL,
                AppConstants.PROXY_TOKEN,
                getDetailsRequest
            )
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>,
                ) {
                    ActivityUtils.hideDialog()
                    if (response.body() != null) {
                        var resp: String? = null
                        try {
                            resp = response.body()!!.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            val res = BackSlash.removeBackSlashes(resp)
                            val gson = Gson()
                            val riderProfileResponse = gson.fromJson(
                                BackSlash.removeSubString(res),
                                GetRiderProfileResponse::class.java
                            )
                            if (riderProfileResponse != null && riderProfileResponse.data != null && riderProfileResponse.success) {
                                validatePinCallBack.onSuccessGetProfileDetailsApi(riderProfileResponse)
                            } else {
                                ActivityUtils.hideDialog()
                                validatePinCallBack.onFailureGetProfileDetailsApi("No Data Found")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    validatePinCallBack.onFailureGetProfileDetailsApi(t.message!!)
                }
            })
        } else {
            validatePinCallBack.onFailureGetProfileDetailsApi("Something went wrong.")
        }
    }

    fun deliveryFailureReasonApiCall(context: Context, validatePinCallBack: ValidatePinCallBack) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.")
            val apiInterface = ApiClient.getApiService()
            val gson = Gson()
            val getDetailsRequest =
                com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest()
            getDetailsRequest.requesturl =
                AppConstants.BASE_URL + "api/choose-data/delivery_failure_reasons"
            getDetailsRequest.requestjson = "The"
            getDetailsRequest.headertokenkey = "authorization"
            getDetailsRequest.headertokenvalue = "Bearer " + SessionManager(context).loginToken
            getDetailsRequest.requesttype = "POST"
            val calls = apiInterface.getDetails(
                AppConstants.PROXY_URL,
                AppConstants.PROXY_TOKEN,
                getDetailsRequest
            )
            calls.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>,
                ) {
                    if (response.body() != null) {
                        var resp: String? = null
                        ActivityUtils.hideDialog()
                        try {
                            resp = response.body()!!.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            val res = BackSlash.removeBackSlashes(resp)
                            val gson = Gson()
                            val deliveryFailreReasonsResponse = gson.fromJson(
                                BackSlash.removeSubString(res),
                                DeliveryFailreReasonsResponse::class.java
                            )
                            if (deliveryFailreReasonsResponse != null && deliveryFailreReasonsResponse.isSuccess) {
                                validatePinCallBack.onSuccessDeliveryReasonApiCall(
                                    deliveryFailreReasonsResponse
                                )
                            } else {
                                ActivityUtils.hideDialog()
                                validatePinCallBack.onFialureMessage("No data found.")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    validatePinCallBack.onFialureMessage(t.message)
                }
            })
        } else {
            validatePinCallBack.onFialureMessage("Something went wrong.")
        }
    }

    fun getComplaintReasonsListApiCall(context: Context, validatePinCallBack: ValidatePinCallBack) {
        if (NetworkUtils.isNetworkConnected(context)) {
            ActivityUtils.showDialog(context, "Please wait.")
            val apiInterface = ApiClient.getApiService()
            val gson = Gson()
            val getDetailsRequest =
                com.apollopharmacy.vishwam.ui.rider.login.model.GetDetailsRequest()
            getDetailsRequest.requesturl =
                AppConstants.BASE_URL + "api/choose-data/complaint_reason"
            getDetailsRequest.requestjson = "The"
            getDetailsRequest.headertokenkey = "authorization"
            getDetailsRequest.headertokenvalue = "Bearer " + SessionManager(context).loginToken
            getDetailsRequest.requesttype = "POST"
            val calls = apiInterface.getDetails(
                AppConstants.PROXY_URL,
                AppConstants.PROXY_TOKEN,
                getDetailsRequest
            )
            calls.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>,
                ) {
                    if (response.body() != null) {
                        ActivityUtils.hideDialog()
                        var resp: String? = null
                        try {
                            resp = response.body()!!.string()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (resp != null) {
                            val res = BackSlash.removeBackSlashes(resp)
                            val gson = Gson()
                            val complaintReasonsListResponse = gson.fromJson(
                                BackSlash.removeSubString(res),
                                ComplaintReasonsListResponse::class.java
                            )
                            if (complaintReasonsListResponse != null && complaintReasonsListResponse.success) {
                                SessionManager(context).complaintReasonsListResponse =
                                    complaintReasonsListResponse
                            } else if (response.code() == 401) {
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    ActivityUtils.hideDialog()
                    validatePinCallBack.onFialureMessage(t.message)
                }
            })
        } else {
            validatePinCallBack.onFialureMessage("Something went wrong.")
        }
    }
}

sealed class Command {
    data class ShowToast(val message: String) : Command()
    data class NavigateTo(
        val value: MPinResponse,
    ) : Command()

    data class ShowButtonSheet(
        val fragment: Class<out BottomSheetDialogFragment>,
        val arguments: Bundle,
    ) : Command()
}