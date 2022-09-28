package com.apollopharmacy.vishwam.ui.home.qcfail.approved

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollopharmacy.vishwam.data.State
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.login.Command
import com.hadilq.liveevent.LiveEvent

class QcApprovedViewModel:ViewModel() {

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