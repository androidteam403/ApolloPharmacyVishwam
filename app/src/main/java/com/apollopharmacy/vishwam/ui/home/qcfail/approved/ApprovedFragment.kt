package com.apollopharmacy.vishwam.ui.home.qcfail.approved

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.DialogRejectQcBinding
import com.apollopharmacy.vishwam.databinding.FragmentApprovedQcBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.approved.adapter.QcApproveListAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.*
import com.apollopharmacy.vishwam.ui.login.Command
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ApprovedFragment : BaseFragment<QcApprovedViewModel, FragmentApprovedQcBinding>(),
    QcListsCallback, QcFilterFragment.QcFilterClicked {
    var dialogBinding: DialogRejectQcBinding? = null
    var adapter: QcApproveListAdapter? = null
    public var isBulkChecked: Boolean = false
    var getStatusList: List<ActionResponse.Hsitorydetail>? = null
    var statusList = ArrayList<ActionResponse.Hsitorydetail>()

    var getitemList: List<QcItemListResponse>? = null
    var itemsList = ArrayList<QcItemListResponse>()

    var orderId: String = ""
    var reason: String = ""
    var qcreason: String = ""



    override val layoutRes: Int
        get() = R.layout.fragment_approved_qc

    override fun retrieveViewModel(): QcApprovedViewModel {
        return ViewModelProvider(this).get(QcApprovedViewModel::class.java)
    }

    override fun setup() {
        showLoading()
        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val currentDate: String = simpleDateFormat.format(Date())
        var fromDate = String()


        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)


        fromDate = simpleDateFormat.format(cal.time)


        viewModel.getQcRegionList()
        viewModel.getQcStoreist()
        viewModel.getQcList(Preferences.getToken(), fromDate, currentDate , "16001", "")




        viewModel.qcStatusLists.observe(viewLifecycleOwner, Observer {
            hideLoading()
            getStatusList = it.hsitorydetails
            val status: ActionResponse.Hsitorydetail


            for (i in getStatusList!!) {
                val items = ActionResponse.Hsitorydetail()
                items.status = i.status

                statusList.add(items)
                adapter?.notifyDataSetChanged()
            }
        })


        viewModel.qcItemsLists.observe(viewLifecycleOwner, Observer {
            hideLoading()
            getitemList = listOf(it)
            val getQcItemListResponse: QcItemListResponse
            getQcItemListResponse = it
            getitemList = listOf(getQcItemListResponse)
            for (i in getitemList!!) {
                val items = QcItemListResponse()
                items.itemlist = i.itemlist
                items.setorderno(orderId)
                items.status = i.status
                itemsList.add(items)
                adapter?.notifyDataSetChanged()
            }





            adapter?.notifyDataSetChanged()


        })



        viewModel.qcLists.observe(viewLifecycleOwner, { it ->
            hideLoading()
            if (it.approvedlist.isNullOrEmpty()) {
                viewBinding.emptyList.visibility = View.VISIBLE
                viewBinding.recyclerViewApproved.visibility = View.GONE
                Toast.makeText(requireContext(), "No Approved Data", Toast.LENGTH_SHORT).show()
            } else {
                adapter =
                    context?.let { it1 ->
                        QcApproveListAdapter(it1,it.approvedlist as ArrayList<QcListsResponse.Approved>,
                            this,
                            itemsList, statusList)
                    }
            }
            viewBinding.recyclerViewApproved.adapter = adapter

        })



        viewBinding.filter.setOnClickListener {
            viewModel.filterClicked()

        }
        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is Command.ShowQcButtonSheet -> {
                    var dialog = command.fragment.newInstance()
                    dialog.arguments = command.arguments

                    dialog.setTargetFragment(this, 0)
                    activity?.supportFragmentManager?.let { dialog.show(it, "") }
                }
                is Command.ShowToast -> {
                    hideLoading()
                    if (command.message.equals("no data found.please check empid")) {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.recyclerViewApproved.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    override fun orderno(position: Int, orderno: String) {
        showLoading()
        orderId = orderno
        viewModel.getQcItemsList(orderno)
        viewModel.getQcStatusList(orderno)
        adapter?.notifyDataSetChanged()


    }

    override fun notify(position: Int, orderno: String) {
        adapter?.notifyDataSetChanged()
    }

    override fun accept(position: Int, orderno: String, remarks: String) {
        TODO("Not yet implemented")
    }

    override fun reject(position: Int, orderno: String, remarks: String) {
        TODO("Not yet implemented")
    }

    override fun isChecked(array: ArrayList<QcListsResponse.Pending>, position: Int) {
        TODO("Not yet implemented")
    }


    override fun clickedApply(
        selectedData: String,
        data: ArrayList<QcStoreList.Store>,
        regiondata: ArrayList<QcRegionList.Store>,
        tag: Int,
        toDate: String,
    ) {

    }


}



