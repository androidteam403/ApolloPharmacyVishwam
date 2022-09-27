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
import com.apollopharmacy.vishwam.data.network.discount.PendingRepo
import com.apollopharmacy.vishwam.ui.home.discount.filter.FilterFragment
import com.apollopharmacy.vishwam.ui.home.discount.filter.FilterFragment.Companion.KEY_PENDING_DATA
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.Utils
import com.google.gson.Gson
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QcRejectedViewModel:ViewModel() {
    val pendingList = MutableLiveData<ArrayList<String>>()
    val state = MutableLiveData<State>()
    val command = LiveEvent<Command>()

    private var arrayList: List<String>? = null

    private var listarrayList= ArrayList<String>()


    fun filterClicked() {

        listarrayList.add("16001")

        arrayList=listarrayList
        command.value = Command.ShowButtonSheet(QcFilterFragment::class.java, bundleOf(Pair(
            QcFilterFragment.KEY_PENDING_DATA_QC, arrayList)))


    }



}