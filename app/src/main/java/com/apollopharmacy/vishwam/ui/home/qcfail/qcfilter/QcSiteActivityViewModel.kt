package com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.data.model.cms.SiteDto
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.QcApiRepo
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcRegionList
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcStoreList
import com.apollopharmacy.vishwam.ui.login.Command
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class QcSiteActivityViewModel : ViewModel() {

    var command = LiveEvent<CommandQcSiteId>()
    val state = MutableLiveData<State>()
    val qcRegionLists = MutableLiveData<QcRegionList>()
    val qcStoreList = MutableLiveData<QcStoreList>()
    var qcStoreIdList: ArrayList<QcStoreList.Store>? = null
    var qcregionIdList: ArrayList<QcRegionList.Store>? = null
    var siteLiveData = ArrayList<QcStoreList.Store>()
    var regionLiveData = ArrayList<QcRegionList.Store>()

    fun getQcRegionList() {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                QcApiRepo.getQcRegionList()
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        qcRegionLists.value = result.value
                        regionLiveData= result.value.storelist as ArrayList<QcRegionList.Store>
                        qcregionIdList = result.value.storelist as ArrayList<QcRegionList.Store>?

                    } else {
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

    fun getQcStoreist(qcFilterSiteCallBack: QcFilterSiteCallBack) {

        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                QcApiRepo.getQcStoreList()
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        qcStoreList.value = result.value
                        qcFilterSiteCallBack.getSiteIdList(result.value.storelist)
                        siteLiveData= result.value.storelist as ArrayList<QcStoreList.Store>
                        qcStoreIdList = result.value.storelist as ArrayList<QcStoreList.Store>?
                    } else {
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

    fun siteId() {
        if (Preferences.isSiteIdListFetchedQcFail()) {
            siteLiveData.clear()
            val gson = Gson()
            val siteIdList = Preferences.getSiteIdListJsonQcFail()
            val type = object : TypeToken<List<QcStoreList.Store?>?>() {}.type

            this.siteLiveData =
                gson.fromJson<List<QcStoreList.Store>>(siteIdList, type) as ArrayList<QcStoreList.Store>
            command.value = CommandQcSiteId.ShowSiteInfo("")
        } else {
            val url = Preferences.getApi()
            val data = Gson().fromJson(url, ValidateResponse::class.java)
            for (i in data.APIS.indices) {
                if (data.APIS[i].NAME.equals("QC STORE LIST")) {
                    val baseUrl = data.APIS[i].URL
                    val token = data.APIS[i].TOKEN
                    viewModelScope.launch {
                        state.value = State.SUCCESS
                        val response = withContext(Dispatchers.IO) {
                            RegistrationRepo.getDetails(
                                "h72genrSSNFivOi/cfiX3A==",
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


    fun getSiteData(): ArrayList<QcStoreList.Store> {
        return siteLiveData
    }

    fun getRegionData(): ArrayList<QcRegionList.Store> {
        return regionLiveData
    }

    sealed class CommandQcSiteId{

        data class ShowToast(val message: String) : CommandQcSiteId()

        data class ShowSiteInfo(val message: String) : CommandQcSiteId()
    }
}