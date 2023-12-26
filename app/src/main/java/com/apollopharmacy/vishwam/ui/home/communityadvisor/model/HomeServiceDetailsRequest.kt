package com.apollopharmacy.vishwam.ui.home.communityadvisor.model

import com.google.gson.annotations.SerializedName

data class HomeServiceDetailsRequest(
    @SerializedName("userId") var userId: String? = null,
)