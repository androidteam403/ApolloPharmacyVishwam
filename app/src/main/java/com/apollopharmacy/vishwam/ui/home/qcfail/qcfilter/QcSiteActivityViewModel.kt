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

    fun getQcStoreist() {
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