package com.apollopharmacy.vishwam.ui.home.cms.registration

import com.apollopharmacy.vishwam.data.model.cms.ResponseTicktResolvedapi
import java.io.Serializable

interface RegistrationFragmentCallback : Serializable {
    fun onSuccessTicketStatus(responseTicktResolvedapi: ResponseTicktResolvedapi)
}