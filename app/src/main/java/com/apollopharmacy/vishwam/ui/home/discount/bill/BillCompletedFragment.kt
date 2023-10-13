package com.apollopharmacy.vishwam.ui.home.discount.bill

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.model.discount.BILLDEATILSItem
import com.apollopharmacy.vishwam.data.model.discount.ITEMSItem
import com.apollopharmacy.vishwam.databinding.BillDetailsRowBinding
import com.apollopharmacy.vishwam.databinding.BillRowBinding
import com.apollopharmacy.vishwam.databinding.FragmentBillCompletedBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.discount.approved.ApprovedFragmentCallback
import com.apollopharmacy.vishwam.ui.home.discount.approved.DiscountApprovedActivity
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import java.util.*

class BillCompletedFragment : BaseFragment<BillCompletedViewModel, FragmentBillCompletedBinding>(),BilledFragmentCallback {

    override val layoutRes: Int
        get() = R.layout.fragment_bill_completed

    override fun retrieveViewModel(): BillCompletedViewModel {
        return ViewModelProvider(requireActivity())[BillCompletedViewModel::class.java]
    }

    override fun setup() {
        viewBinding.billViewModel = viewModel

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
            viewModel.getBillDetailsApiCall()
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }

        viewModel.billArrayLiveData.observe(viewLifecycleOwner, Observer {
            hideLoading()
            Utils.printMessage("billData", it.toString())
            viewBinding.recyclerViewPending.adapter = BillRecyclerView(it,this)
        })
        viewModel.command.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is Command.ShowToast -> {
                    if (it.message.equals("No Data Found")) {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.recyclerViewPending.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Bill is completed", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {}
            }
        })
    }

    override fun onClick(orderdetails: ArrayList<BILLDEATILSItem>, position: Int) {
        val i = Intent(context, DiscountBillingActivity::class.java)
        i.putExtra("billList",orderdetails)
        i.putExtra("position",position)

        startActivityForResult(i, 210)    }
}

class BillRecyclerView(val billData: ArrayList<BILLDEATILSItem>, private val listener: BilledFragmentCallback) :
    SimpleRecyclerView<BillRowBinding, BILLDEATILSItem>(
        billData,
        R.layout.bill_row
    ) {
    var orderItemsId = ArrayList<String>()
    override fun bindItems(
        binding: BillRowBinding,
        items: BILLDEATILSItem,
        position: Int,
    ) {
        binding.storeText.text = items.sTOREID
        if (items.aPPROVEDDATE != null) {
            if (items.aPPROVEDDATE.isNotEmpty()) {
                binding.postedDate.text = Utlis.convertDateAddedTimeZone(items.aPPROVEDDATE)
            } else {
                binding.postedDate.text = ""
            }
        } else {
            binding.postedDate.text = ""
        }

        binding.approvedValueText.text = items.aPPROVEDVALUE.toString()
        binding.billedDateValue.text = Utlis.convertDateAddedTimeZone(items.bILLEDDATE)
        binding.indentNoText.text = items.iNDENTNO
        binding.finalBillText.text = items.fINALBILLEDVALUE.toString()

        binding.extraData.visibility =
            if (orderItemsId.contains(items.iNDENTNO)) View.VISIBLE else View.GONE
        binding.orderHead.setOnClickListener {
            listener.onClick(billData,position)

//            if (orderItemsId.contains(items.iNDENTNO)) {
//                binding.extraData.visibility = View.GONE
//                orderItemsId.remove(items.iNDENTNO)
//                binding.arrowClose.visibility = View.GONE
//                binding.arrow.visibility = View.VISIBLE
//            } else {
//                binding.extraData.visibility = View.VISIBLE
//                orderItemsId.add(items.iNDENTNO)
//                binding.arrowClose.visibility = View.VISIBLE
//                binding.arrow.visibility = View.GONE
//            }
        }
        binding.recyclerView.adapter = OrderAdapter(items = billData[position].iTEMS, false)
    }

    class OrderAdapter(val items: List<ITEMSItem>, val isMarginRequired: Boolean) :
        RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val orderAdapterBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.bill_details_row,
                parent,
                false
            ) as BillDetailsRowBinding
            return ViewHolder(orderAdapterBinding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }

        inner class ViewHolder(private val binding: BillDetailsRowBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(pendingOrder: ITEMSItem) {
                binding.reqQuantity.text = pendingOrder.rEQUESTEDQTY.toString()
                binding.medicineNameOrg.text = "${pendingOrder.iTEMCODE}: ${pendingOrder.iTEMNAME}"
                binding.quantityText.text = pendingOrder.bILLEDQTY.toString()
                binding.reqQuantity.text = pendingOrder.rEQUESTEDQTY.toString()
                binding.batchNo.text = pendingOrder.bATCHNO
                binding.approvedPer.text = pendingOrder.aPPROVEDDSICPERCENTAGE.toString()
                binding.billedpercentage.text = pendingOrder.bILLEDDISCPERCENTAGE.toString()
                if (isMarginRequired) {
                    binding.marginDiscLayout.visibility = View.VISIBLE
                    binding.marginDiscText.text = pendingOrder.aPPROVEDDSICPERCENTAGE.toString()
                } else {
                    binding.marginDiscLayout.visibility = View.INVISIBLE
                }
            }
        }
    }
}
