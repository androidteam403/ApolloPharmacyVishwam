package com.apollopharmacy.vishwam.ui.home.qcfail.pending

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.drugmodule.ImagesListner
import com.apollopharmacy.vishwam.ui.home.qcfail.filter.QcFilterFragment
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcList
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity.TAG
import okhttp3.internal.notify


class PendingFragment : BaseFragment<QcPendingViewModel, QcFragmentPendingBinding>(),
    QcImagesListner,
    QcFilterFragment.QcFilterClicked {

    lateinit var adapter: PendingRecycleView
    public var isBulkChecked: Boolean = false

    val names = ArrayList<QcList>();

    override val layoutRes: Int
        get() = R.layout.qc_fragment_pending

    override fun retrieveViewModel(): QcPendingViewModel {
        return ViewModelProvider(this).get(QcPendingViewModel::class.java)
    }

    override fun setup() {
        viewBinding.pendingViewModel = viewModel
        var name=QcList()
        var name1=QcList()
        name1.setIschecked(false)

        name.setIschecked(false)
        names.add(name)
        names.add(name1)
        adapter = PendingRecycleView(names, this)
        viewBinding.recyclerViewPending.adapter = adapter



        viewBinding.filter.setOnClickListener {
            viewModel.filterClicked()

        }
        viewModel.command.observe(viewLifecycleOwner) { command ->
            when (command) {
                is Command.ShowButtonSheet -> {
                    var dialog = command.fragment.newInstance()
                    dialog.arguments = command.arguments
                    dialog.setTargetFragment(this, 0)
                    activity?.supportFragmentManager?.let { dialog.show(it, "") }
                }
                is Command.ShowToast -> {
                    hideLoading()
                    if (command.message.equals("no data found.please check empid")) {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.bulkAppRejLayout.visibility = View.GONE
                        viewBinding.recyclerViewPending.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), command.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    class PendingRecycleView(
        var orderData: ArrayList<QcList>,
        val imageClicklistner: QcImagesListner,
    ) :
        SimpleRecyclerView<QcPendingLayoutBinding, QcList>(orderData, R.layout.qc_pending_layout) {
        override fun bindItems(
            binding: QcPendingLayoutBinding,
            items: QcList,
            position: Int,
        ) {
            val ischecked: Boolean = false

            lateinit var orderadapter: OrderDetailsRecycleView
            val names = ArrayList<String>();
            names.add("a")



            orderadapter = OrderDetailsRecycleView(names)
            binding.recyclerView.adapter = orderadapter

            binding.acceptClick.setOnClickListener {

                imageClicklistner.accept(position)
            }

            binding.rejectClick.setOnClickListener {
                imageClicklistner.reject(position)
            }



            if (!items.isIschecked){
                binding.checkBox.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
            }
            else{
                binding.checkBox.setImageResource(R.drawable.ic_circle_tick)
            }
            binding.checkBoxLayout.setOnClickListener {

                imageClicklistner.isChecked(orderData,position)

            }


//
//            binding.checkBoxLayout.setOnClickListener {
//                if (ischecked == true) {
//                    binding.checkBox.setImageResource(R.drawable.icon_item_unchecked)
//
//                }
//            }
            binding.arrow.setOnClickListener {
                binding.arrowClose.visibility = View.VISIBLE
                binding.arrow.visibility = View.GONE
                binding.extraData.visibility = View.VISIBLE


            }

            binding.arrowClose.setOnClickListener {
                binding.arrowClose.visibility = View.GONE
                binding.arrow.visibility = View.VISIBLE
                binding.extraData.visibility = View.GONE

            }

        }
    }


    class OrderDetailsRecycleView(
        var orderData: ArrayList<String>,

        ) :
        SimpleRecyclerView<QcOrderLayoutBinding, String>(
            orderData,
            R.layout.qc_order_layout
        ) {
        override fun bindItems(
            binding: QcOrderLayoutBinding,
            items: String,
            position: Int,
        ) {


        }
    }

    override fun clickedApply(
        selectedData: String,
        data: ArrayList<String>,
        tag: Int,
        toDate: String,
    ) {
        Utils.printMessage(TAG, "Clicked Apply :: " + selectedData)

    }

    override fun accept(position: Int) {


        val dialogBinding: DialogAcceptQcBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_accept_qc,
                null,
                false
            )
        val customDialog = android.app.AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {


            customDialog.dismiss()
        }
        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun reject(position: Int) {
        val dialogBinding: DialogRejectQcBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.dialog_reject_qc,
                null,
                false
            )
        val customDialog = android.app.AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {

            setView(dialogBinding?.root)
            setCancelable(false)
        }.show()
        dialogBinding?.yesBtn?.setOnClickListener {


            customDialog.dismiss()
        }
        dialogBinding?.cancelButton?.setOnClickListener {
            customDialog.dismiss()
        }
    }

    override fun isChecked(array: ArrayList<QcList>, position: Int) {
        if (array[position].isIschecked){
            names[position].setIschecked(false)
        }
        else{
            names[position].setIschecked(true)
        }
        adapter.notifyDataSetChanged()
    }


}

interface QcImagesListner {

    fun accept(position: Int)
    fun reject(position: Int)
    fun isChecked(array:ArrayList<QcList>,position: Int)
}



