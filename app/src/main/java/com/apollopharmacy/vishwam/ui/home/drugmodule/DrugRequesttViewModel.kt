package com.apollopharmacy.vishwam.ui.home.drugmodule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.azure.ConnectionAzure
import com.apollopharmacy.vishwam.data.azure.ConnectionToAzure
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.Image
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.data.model.cms.SiteDto
import com.apollopharmacy.vishwam.data.model.cms.StoreListItem
import com.apollopharmacy.vishwam.data.network.*
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.cms.registration.CmsCommand
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugRequest
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException

class DrugFragmentViewModel: ViewModel() {
    val commands = LiveEvent<Commands>()
    val state = MutableLiveData<State>()
//    var command = LiveEvent<CmsCommand>()
var siteLiveData = ArrayList<StoreListItem>()
    private var storeDetailsSend = StoreListItem()

    var drugList = MutableLiveData<DrugResponse>()

    fun getDrugList(drugRequest: DrugRequest) {
        viewModelScope.launch {
            state.postValue(State.SUCCESS)

            val result = withContext(Dispatchers.IO) {
                SwachhListApiResponse.getDrugResponse(drugRequest)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (result.value.requestStatus ?: null == true) {
                        state.value = State.ERROR
                        drugList.value = result.value
                    } else {
                        state.value = State.ERROR
                    }
                }
                is ApiResult.GenericError -> {
                    commands.postValue(
                        result.error?.let {
                            Commands.ShowToast(it)
                        }
                    )
                    state.value = State.ERROR
                }
                is ApiResult.NetworkError -> {
                    commands.postValue(Commands.ShowToast("Network Error"))
                    state.value = State.ERROR
                }
                is ApiResult.UnknownError -> {
                    commands.postValue(Commands.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
                else -> {
                    commands.postValue(Commands.ShowToast("Something went wrong, please try again later"))
                    state.value = State.ERROR
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun connectToAzure(image:ArrayList<Image>) {
        state.value = State.SUCCESS
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                ConnectionToAzure.connectToAzur(
                    image,
                    Config.CONTAINER_NAME,
                    Config.STORAGE_CONNECTION_FOR_CCR_APP
                )

            commands.postValue(Commands.DrugImagesUploadInAzur(response))
        }
    }

    fun siteId() {
        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS GETSITELIST")) {
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
                                    SiteDto::class.java
                                )

                            if (reasonmasterV2Response.status) {
                                siteLiveData.clear()
                                reasonmasterV2Response.siteData?.listData?.rows?.map { siteLiveData.add(it) }
                                // getDepartment()
                                commands.value = Commands.ShowSiteInfo("")
                            } else {
                                commands.value = Commands.ShowToast(
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














    fun getSiteData(): ArrayList<StoreListItem> {
        return siteLiveData
    }

    fun getSelectedStoreDetails(storeDetails: StoreListItem) {
        storeDetailsSend = storeDetails
    }




    fun getNames(): ArrayList<String> {

        var names = ArrayList<String>()

        names.add("Pharma")
        names.add("FMCG")
        names.add("Surgical")
        names.add("Homeopathy")
        names.add("Sports")
        names.add("Ayurveda")


        return names

    }
    fun getGst(): ArrayList<String> {

        var names = ArrayList<String>()

        names.add("5")
        names.add("8")
        names.add("12")



        return names

    }

    sealed class Commands {
        data class ShowToast(val message: String?) : Commands()
        data class DrugImagesUploadInAzur(val filePath: ArrayList<Image>) :
            Commands()


        data class ShowSiteInfo(val message: String) : Commands()

    }
}