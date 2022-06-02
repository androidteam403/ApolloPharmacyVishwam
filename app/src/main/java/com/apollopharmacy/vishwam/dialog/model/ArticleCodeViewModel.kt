package com.apollopharmacy.vishwam.dialog.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.data.model.ValidateResponse
import com.apollopharmacy.vishwam.data.model.cms.ArticleCodeRequest
import com.apollopharmacy.vishwam.data.model.cms.ArticleCodeResponse
import com.apollopharmacy.vishwam.data.network.ApiResult
import com.apollopharmacy.vishwam.data.network.RegistrationRepo
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.launch

class ArticleCodeViewModel : ViewModel() {
    val state = MutableLiveData<State>()
    var articleCode = ArrayList<ArticleCodeResponse.DataItem>()
    var mutableLiveData = LiveEvent<ArrayList<ArticleCodeResponse.DataItem>>()

    fun searchArticleCode(articleCodeRequest: ArticleCodeRequest) {
        state.value = State.SUCCESS

        val url = Preferences.getApi()
        val data = Gson().fromJson(url, ValidateResponse::class.java)
        for (i in data.APIS.indices) {
            if (data.APIS[i].NAME.equals("CMS APP")) {
                val baseUrl = data.APIS[i].URL
                val token = data.APIS[i].TOKEN
                viewModelScope.launch {
                    val response =
                        RegistrationRepo.getArticleCodeSearch(token,
                            baseUrl,
                            Config.CMS_Fetch_Code,
                            articleCodeRequest)
                    when (response) {
                        is ApiResult.Success -> {
                            articleCode.clear()
                            articleCode = response.value.data
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
            }
        }

    }
}