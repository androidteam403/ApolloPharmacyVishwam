package com.apollopharmacy.vishwam.ui.home.adrenalin.attendance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config.CONTAINER_NAME
import com.apollopharmacy.vishwam.data.Config.STORAGE_CONNECTION_FOR_CCR_APP
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.azure.ConnectionAzure
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.attendance.*
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.AttendanceRepo
import com.apollopharmacy.vishwam.data.network.SwachhListApiResponse
import com.apollopharmacy.vishwam.ui.createpin.Command
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.DoctorListRequest
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.DoctorListResponse
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.SiteListRequest
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.SiteListResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.ApproveRejectListRequest
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AttendanceViewModel : ViewModel() {
    var complainLiveData = MutableLiveData<ArrayList<GetTaskListResponse>>()
    var lastLoginData = MutableLiveData<LoginInfoRes>()
    var command = LiveEvent<AttendanceCommand>()
    var siteLiveData = MutableLiveData<SiteListResponse>()
    var doctorLiveData = MutableLiveData<DoctorListResponse>()

    val state = MutableLiveData<State>()
    var departmentData = MutableLiveData<DepartmentListRes>()
    var departmentTaskData = MutableLiveData<DepartmentTaskListRes>()
    val TAG = "AttendanceViewModel"

    fun getLastLogin(empId: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ATTENDENCE ENQUIRY")) {
                val token = data.APIS[i].TOKEN
                val baseUrl = data.APIS[i].URL
                state.value = State.SUCCESS
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        AttendanceRepo.getLastLogin(token, baseUrl, empId)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            lastLoginData.value = response.value
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = AttendanceCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    fun getTaskList(empId: String) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ATTENDENCE GET ALL TASKS BY EMPID")) {
                val token = data.APIS[i].TOKEN
                val baseUrl = data.APIS[i].URL
                state.value = State.SUCCESS
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        AttendanceRepo.getListOfTasks(token, baseUrl, empId)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            complainLiveData.value = response.value
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = AttendanceCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    fun taskInsertUpdateService(taskInfoReq: TaskInfoReq) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ATTENDENCE SAVE TASK DATA")) {
                val token = data.APIS[i].TOKEN
                val baseUrl = data.APIS[i].URL
                state.value = State.SUCCESS
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        AttendanceRepo.taskInsertUpdate(token, baseUrl, taskInfoReq)
                    }
                    when (response) {
                        is ApiResult.Success -> {

                            state.value = State.ERROR
                            command.value =
                                AttendanceCommand.UpdateTaskList(response.value.message.toString())
                        }
                        is ApiResult.NetworkError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = AttendanceCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    fun connectToAzure(image: ArrayList<ImageDataDto>) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ConnectionAzure.connectToAzur(
                    image,
                    CONTAINER_NAME,
                    STORAGE_CONNECTION_FOR_CCR_APP
                )
            command.postValue(AttendanceCommand.ImageIsUploadedInAzur(response))
        }
    }


    fun siteListResponse(siteListRequest: SiteListRequest) {

                val url = Preferences.getApi()
                val data = Gson().fromJson(url, ValidateResponse::class.java)
                var baseUrl = ""
                var token = ""
                for (i in data.APIS.indices) {
                    if (data.APIS[i].NAME.equals("ATTENDENCE GETSITEANDDOCTORLIST")) {
                        baseUrl = data.APIS[i].URL
                        token = data.APIS[i].TOKEN
                        break
                    }
                }

                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        AttendanceRepo.getSiteList(token, baseUrl, siteListRequest)
                    }
                    when (response) {
                        is ApiResult.Success -> {
//                            Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path $
                            state.value = State.ERROR
                            siteLiveData.value = response.value

                        }
                        is ApiResult.NetworkError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = AttendanceCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                    }
                }


    }

    fun doctorListResponse(doctorListRequest: DoctorListRequest) {

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ATTENDENCEGETSITEANDDOCTORLIST")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }

        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                AttendanceRepo.getDoctorList(token, baseUrl, doctorListRequest)
            }
            when (response) {
                is ApiResult.Success -> {
                    state.value = State.ERROR
                    doctorLiveData.value = response.value

                }
                is ApiResult.NetworkError -> {
                    command.value =
                        AttendanceCommand.ShowToast("Please Check Internet Connection")
                    state.value = State.ERROR
                }
                is ApiResult.GenericError -> {
                    command.value = AttendanceCommand.ShowToast("Unknown Error")
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    command.value =
                        AttendanceCommand.ShowToast("Something went wrong, please try again later")
                    state.value = State.ERROR
                }
                is ApiResult.UnknownHostException -> {
                    command.value =
                        AttendanceCommand.ShowToast("Something went wrong, please try again later")
                    state.value = State.ERROR
                }
            }
        }


    }

    fun attendanceSignInOutService(atdLogInOutReq: AtdLogInOutReq) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ATTENDENCE SAVE DATA")) {
                val token = data.APIS[i].TOKEN
                val baseUrl = data.APIS[i].URL
                state.value = State.SUCCESS
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        AttendanceRepo.atdSignInOut(token, baseUrl, atdLogInOutReq)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            state.value = State.ERROR
                            command.value =
                                AttendanceCommand.RefreshPageOnSuccess(
                                    response.value.status,
                                    response.value.message
                                )
                        }
                        is ApiResult.NetworkError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = AttendanceCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    fun getDepartmentList() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ATTENDENCE GETALLDEPARTMENTS")) {
                val token = data.APIS[i].TOKEN
                val baseUrl = data.APIS[i].URL
                state.value = State.SUCCESS
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        AttendanceRepo.getListOfDepartments(token, baseUrl)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            departmentData.value = response.value
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = AttendanceCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }

    fun getDepartmentTaskList(deptId: Int) {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("ATTENDENCE GETALLTASKSBYDEPTID")) {
                val token = data.APIS[i].TOKEN
                val baseUrl = data.APIS[i].URL
                state.value = State.SUCCESS
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        AttendanceRepo.getListOfDepartmentTasks(token, baseUrl, deptId)
                    }
                    when (response) {
                        is ApiResult.Success -> {
                            departmentTaskData.value = response.value
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Please Check Internet Connection")
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
                            command.value = AttendanceCommand.ShowToast("Unknown Error")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownError -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                        is ApiResult.UnknownHostException -> {
                            command.value =
                                AttendanceCommand.ShowToast("Something went wrong, please try again later")
                            state.value = State.ERROR
                        }
                    }
                }
            }
        }
    }
}

sealed class AttendanceCommand {
    data class VisibleLayout(val message: String) : AttendanceCommand()
    data class ShowToast(val message: String) : AttendanceCommand()
    data class UpdateTaskList(val message: String) : AttendanceCommand()
    data class ImageIsUploadedInAzur(val filePath: ArrayList<ImageDataDto>) :
        AttendanceCommand()

    data class RefreshPageOnSuccess(val status: Boolean, val message: String) : AttendanceCommand()
}