package com.apollopharmacy.vishwam.ui.home.cms.complainList.activity

import android.widget.EditText
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.Data
import com.apollopharmacy.vishwam.ui.home.cms.complainList.model.UserListForSubworkflowResponse

interface ComplaintsListDetailsCallback {

    fun onClickBack()
    abstract fun onSucessGetManagersLiveData(data: Data)

    fun onSelectUserListItem(row: UserListForSubworkflowResponse.Rows, userSelect: EditText)
}