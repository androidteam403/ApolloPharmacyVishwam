package com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.QcApiRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcRegionList
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcStoreList
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QcSiteActivityViewModel : ViewModel() {

    var command = LiveEvent<CommandQcSiteId>()
    val state = MutableLiveData<State>()
    val qcRegionLists = MutableLiveData<QcRegionList>()
    val qcStoreList = MutableLiveData<QcStoreList>()
    var qcStoreIdList: ArrayList<QcStoreList.Store>? = null
    var qcregionIdList: ArrayList<QcRegionList.Store>? = null
    var siteLiveData = ArrayList<QcStoreList.Store>()
    var regionLiveData = ArrayList<QcRegionList.Store>()


    fun getQcStoreist(qcFilterSiteCallBack: QcFilterSiteCallBack) {
        if (Preferences.isSiteIdListFetchedQcFail()) {
            siteLiveData.clear()
            val gson = Gson()
            val siteIdList = Preferences.getSiteIdListJsonQcFail()
            val type = object : TypeToken<List<QcStoreList.Store?>?>() {}.type

            this.siteLiveData =
                gson.fromJson<List<QcStoreList.Store>>(siteIdList,
                    type) as ArrayList<QcStoreList.Store>
            command.value = CommandQcSiteId.ShowSiteInfo("")
            Utlis.hideLoading()
        } else {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            var baseUrl = ""
            var token = ""
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("QC STORE LIST")) {
                    baseUrl = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }
            viewModelScope.launch {
                state.postValue(State.SUCCESS)

                val result = withContext(Dispatchers.IO) {
                    QcApiRepo.getQcStoreList(baseUrl)
                }
                when (result) {
                    is ApiResult.Success -> {
                        if (result.value.status ?: null == true) {
                            state.value = State.ERROR
                            qcStoreList.value = result.value!!
                            qcFilterSiteCallBack.getSiteIdList(result.value.storelist)
                            siteLiveData = result.value.storelist as ArrayList<QcStoreList.Store>
                            qcStoreIdList = result.value.storelist as ArrayList<QcStoreList.Store>?
                            command.value = CommandQcSiteId.ShowSiteInfo("")

                        } else {
                            state.value = State.ERROR
                            CommandQcSiteId.ShowToast(result.value.message.toString())

                        }
                    }

                    is ApiResult.GenericError -> {
                        command.postValue(
                            result.error?.let {
                                CommandQcSiteId.ShowToast(it)
                            }
                        )
                        state.value = State.ERROR
                    }

                    is ApiResult.NetworkError -> {
                        command.postValue(CommandQcSiteId.ShowToast("Network Error"))
                        state.value = State.ERROR
                    }

                    is ApiResult.UnknownError -> {
                        command.postValue(CommandQcSiteId.ShowToast("Something went wrong, please try again later"))
                        state.value = State.ERROR
                    }

                    else -> {
                        command.postValue(CommandQcSiteId.ShowToast("Something went wrong, please try again later"))
                        state.value = State.ERROR
                    }
                }
            }
        }
    }

    fun siteId() {
        if (Preferences.isSiteIdListFetchedQcFail()) {
            siteLiveData.clear()
            val gson = Gson()
            val siteIdList = Preferences.getSiteIdListJsonQcFail()
            val type = object : TypeToken<List<QcStoreList.Store?>?>() {}.type

            this.siteLiveData =
                gson.fromJson<List<QcStoreList.Store>>(siteIdList,
                    type) as ArrayList<QcStoreList.Store>
            command.value = CommandQcSiteId.ShowSiteInfo("")
            Utlis.hideLoading()
        } else {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var baseUrL = ""
            var token = ""
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                    baseUrL = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("QC STORE LIST")) {
                    val baseUrl = data.APIS[i].URL
                    val token = data.APIS[i].TOKEN
                    viewModelScope.launch {
                        state.value = State.SUCCESS
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.getDetails(
                                baseUrL, token,
                                GetDetailsRequest(
                                    baseUrl,
                                    "GET",
                                    "The",
                                    "",
                                    ""
                                )
                            )
//                        RegistrationRepo.selectSiteId(token, baseUrl)
                        }
                        when (response) {
                            is ApiResult.Success -> {
                                state.value = State.ERROR
                                val resp: String = response.value.string()
                                val res = BackShlash.removeBackSlashes(resp)
                                val reasonmasterV2Response =
                                    Gson().fromJson(
                                        BackShlash.removeSubString(res),
                                        QcStoreList::class.java
                                    )

                                if (reasonmasterV2Response.status!!) {
                                    siteLiveData.clear()
                                    reasonmasterV2Response.storelist?.map {
                                        siteLiveData.add(it)
                                    }
                                    // getDepartment()
                                    command.value = CommandQcSiteId.ShowSiteInfo("")
                                } else {
                                    command.value = CommandQcSiteId.ShowToast(
                                        reasonmasterV2Response.message.toString()
                                    )
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
                }
            }
        }
    }


    fun getQcRegionList() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        var baseUrl = ""
        var token = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("QC REGION LIST")) {
                baseUrl = data.APIS[i].URL
                token = data.APIS[i].TOKEN
                break
            }
        }
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                QcApiRepo.getQcRegionList(baseUrl)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        qcRegionLists.value = result.value!!
                        regionLiveData = result.value.storelist as ArrayList<QcRegionList.Store>
                        qcregionIdList = result.value.storelist as ArrayList<QcRegionList.Store>?
                        command.value = CommandQcSiteId.ShowRegionInfo("")

                    } else {
                        CommandQcSiteId.ShowToast(result.value.message.toString())

                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(
                        result.error?.let {
                            CommandQcSiteId.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    command.postValue(CommandQcSiteId.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    command.postValue(CommandQcSiteId.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    command.postValue(CommandQcSiteId.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    fun regionId() {
        if (Preferences.isRegionIdListFetchedQcFail()) {
            regionLiveData.clear()
            val gson = Gson()
            val siteIdList = Preferences.getRegionIdListJsonQcFail()
            val type = object : TypeToken<List<QcRegionList.Store?>?>() {}.type

            this.regionLiveData =
                gson.fromJson<List<QcRegionList.Store>>(siteIdList,
                    type) as ArrayList<QcRegionList.Store>
            command.value = CommandQcSiteId.ShowRegionInfo("")
            Utlis.hideLoading()
        } else {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)

            var baseUrL = ""
            var token = ""
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("VISW Proxy API URL")) {
                    baseUrL = data.APIS[i].URL
                    token = data.APIS[i].TOKEN
                    break
                }
            }
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("QC REGION LIST")) {
                    val baseUrl = data.APIS[i].URL
                    viewModelScope.launch {
                        state.value = State.SUCCESS
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.getDetails(
                                baseUrL, token, GetDetailsRequest(
                                    baseUrl,
                                    "GET",
                                    "The",
                                    "",
                                    ""
                                )
                            )
//                        RegistrationRepo.selectSiteId(token, baseUrl)
                        }
                        when (response) {
                            is ApiResult.Success -> {
                                state.value = State.ERROR
                                val resp: String = response.value.string()
                                val res = BackShlash.removeBackSlashes(resp)
                                val reasonmasterV2Response =
                                    Gson().fromJson(
                                        BackShlash.removeSubString(res),
                                        QcRegionList::class.java
                                    )

                                if (reasonmasterV2Response.status!!) {
                                    regionLiveData.clear()
                                    reasonmasterV2Response.storelist?.map {
                                        regionLiveData.add(it)
                                    }
                                    // getDepartment()
                                    command.value = CommandQcSiteId.ShowRegionInfo("")
                                } else {
                                    command.value = CommandQcSiteId.ShowToast(
                                        reasonmasterV2Response.message.toString()
                                    )
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
                }
            }
        }
    }


    fun getSiteData(): ArrayList<QcStoreList.Store> {
        if (!Preferences.getQcSite().isEmpty()) {
            for (i in siteLiveData) {
                if (Preferences.getQcSite().contains(i.siteid!!)) {
                    i.isClick = true
                }
            }
            return siteLiveData
        } else {
            return siteLiveData
        }

    }

    fun getRegionData(): ArrayList<QcRegionList.Store> {

        if (!Preferences.getQcSite().isEmpty()) {
            for (i in regionLiveData) {
                if (Preferences.getQcRegion().contains(i.siteid!!)) {
                    i.isClick = true
                }
            }
            return regionLiveData
        } else {
            return regionLiveData
        }

    }

    sealed class CommandQcSiteId {

        data class ShowToast(val message: String) : CommandQcSiteId()

        data class ShowSiteInfo(val message: String) : CommandQcSiteId()

        data class ShowRegionInfo(val message: String) : CommandQcSiteId()
    }
}