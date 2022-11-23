package com.apollopharmacy.vishwam.ui.home.qcfail.rejected

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.discount.*
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.QcApiRepo
import com.apollopharmacy.vishwam.data.network.discount.PendingRepo
import com.apollopharmacy.vishwam.ui.home.discount.filter.FilterFragment
import com.apollopharmacy.vishwam.ui.home.discount.filter.FilterFragment.Companion.KEY_PENDING_DATA
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QcRejectedViewModel:ViewModel() {
    val qcRejectLists = MutableLiveData<QcListsResponse>()
    val qcRejectItemsLists = MutableLiveData<QcItemListResponse>()
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()
    var rejectLists = ArrayList<QcListsResponse>()
    val qcRegionLists = MutableLiveData<QcRegionList>()
    val qcStoreList = MutableLiveData<QcStoreList>()
    var qcStoreIdList: ArrayList<QcStoreList.Store>? = null
    var qcregionIdList: ArrayList<QcRegionList.Store>? = null
    val qcStatusLists = MutableLiveData<ActionResponse>()


    private var arrayList: List<String>? = null

    private var listarrayList= ArrayList<String>()

    fun getQcRejectList(empId:String,fromDate:String,toDate:String,storeId:String,region:String) {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                QcApiRepo.getQcLists(empId, fromDate, toDate, storeId,region)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        qcRejectLists.value= result.value
                    }

                    else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(
                        result.error?.let { Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    command.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }
    fun getQcRejectItemsList(orderId:String) {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                QcApiRepo.getQcItemLists(orderId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status?:null == true) {
                        state.value = State.ERROR
                        qcRejectItemsLists.value= result.value
                    }

                    else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(
                        result.error?.let { Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    command.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }
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
                        qcregionIdList = result.value.storelist as ArrayList<QcRegionList.Store>?

                    } else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(
                        result.error?.let {
                            Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    command.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
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
                        qcStoreIdList = result.value.storelist as ArrayList<QcStoreList.Store>?
                    } else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(
                        result.error?.let {
                            Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    command.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }

    fun getQcStatusList(orderId: String) {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                QcApiRepo.getQcStatusLists(orderId)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.status ?: null == true) {
                        state.value = State.ERROR
                        qcStatusLists.value = result.value

                    } else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    command.postValue(
                        result.error?.let {
                            Command.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    command.postValue(Command.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    command.postValue(Command.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }


    fun filterClicked() {

        listarrayList.add("16001")

        arrayList=listarrayList
//        command.value = Command.ShowButtonSheet(QcFilterFragment::class.java, bundleOf(Pair(
//            QcFilterFragment.KEY_PENDING_DATA_QC, arrayList)))


    }



}