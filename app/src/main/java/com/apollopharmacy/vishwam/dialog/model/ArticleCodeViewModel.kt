package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.GetDetailsRequest
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.ArticleCodeRequest
import com.apollopharmacy.vishwam.data.model.cms.ArticleCodeResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.apollopharmacy.vishwam.ui.home.cms.complainList.BackShlash
import com.apollopharmacy.vishwam.ui.home.cms.registration.model.FetchItemModel
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch

class ArticleCodeViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    var articleCode = ArrayList<FetchItemModel.Rows>()
    var mutableLiveData = LiveEvent<ArrayList<FetchItemModel.Rows>>()

    fun searchArticleCode(articleCodeRequest: ArticleCodeRequest) {
        state.value = State.SUCCESS
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

        var fetchCodeUrl = ""
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS fetch_item_code")) {
                fetchCodeUrl = data.APIS[i].URL
                break
            }
        }

//        for (i in data.APIS.indices) {
//            if (data.APIS[i].NAME.equals("CMS APP")) {
                val baseUrl = fetchCodeUrl+"?page=1&rows=10&" +
                        "globalFilter%5BfieldName%5D=globalFilter&globalFilter%5Bkey%5D=globalFilter&globalFilter%5Bvalue%5D=" +
                        articleCodeRequest.query +
                        "&globalFilter%5BmatchType%5D=any&sort%5B0%5D%5Bkey%5D=artcode&sort%5B0%5D%5Border%5D=ASC"

                viewModelScope.launch {
                    val response =
                        RegistrationRepo.getDetails(baseUrL,token,
                            GetDetailsRequest(
                                baseUrl,
                                "GET",
                                "The",
                                "",
                                ""
                            ))
                    when (response) {
                        is ApiResult.Success -> {
                            val res = BackShlash.removeBackSlashes(response.value.string())
                            val resString = BackShlash.removeSubString(res)
                            val responseTicktResolvedapi =
                                Gson().fromJson(
                                    resString,
                                    FetchItemModel::class.java
                                )
                            articleCode.clear()
                            articleCode = responseTicktResolvedapi.data.listData.rows
                            mutableLiveData.value = articleCode
                            state.value = State.ERROR
                        }
                        is ApiResult.NetworkError -> {
                            state.value = State.ERROR
                        }
                        is ApiResult.GenericError -> {
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
}