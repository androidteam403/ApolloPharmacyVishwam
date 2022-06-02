package com.apollopharmacy.vishwam.ui.home.discount.rejected

import android.annotation.SuppressLint
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
import com.apollopharmacy.vishwam.data.model.LoginDetails
import com.apollopharmacy.vishwam.data.model.discount.FilterDiscountRequest
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.data.model.discount.RejectedOrderResponse
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.DiscountCalendarDialog
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.CalculateDiscountAndTotalQuantity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utils.getDateDifference
import com.apollopharmacy.vishwam.util.Utlis
import java.util.*
import kotlin.collections.ArrayList

class RejectedFragment : BaseFragment<RejectedViewModel, FragmentRejectedBinding>(),
    DiscountCalendarDialog.DateSelected {

    var isFromDateSelected: Boolean = false

    override val layoutRes: Int
        get() = R.layout.fragment_rejected

    lateinit var userData: LoginDetails

    override fun retrieveViewModel(): RejectedViewModel {
        return ViewModelProvider(this)[RejectedViewModel::class.java]
    }

    override fun setup() {
        viewBinding.rejectedViewModel = viewModel

        viewBinding.fromDateText.setText(Utils.getCurrentDate())
        viewBinding.toDateText.setText(Utils.getCurrentDate())
        userData = LoginRepo.getProfile()!!

        handleRejectListService()
//        getDecryptedFilteredList()
//        if (NetworkUtil.isNetworkConnected(requireContext())) {
//            showLoading()
//            viewModel.getRejectedData()
//        } else {
//            Toast.makeText(
//                requireContext(),
//                resources.getString(R.string.label_network_error),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        }

        viewModel.rejectedList.observe(viewLifecycleOwner, {
            hideLoading()
            if (it.isNullOrEmpty()) {
                viewBinding.emptyList.visibility = View.VISIBLE
                viewBinding.rejectedRecycler.visibility = View.GONE
                Toast.makeText(requireContext(), "No Rejected data", Toast.LENGTH_SHORT).show()
            } else {
                viewBinding.rejectedRecycler.adapter = ApproveRecyclerView(it)
                viewBinding.rejectedRecycler.visibility = View.VISIBLE
                viewBinding.emptyList.visibility = View.GONE
            }
        })

        viewModel.command.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is Command.ShowToast -> {
                    if (it.message.equals("no data found.please check empid")) {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.rejectedRecycler.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Rejected data", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewBinding.fromDateText.setOnClickListener {
            isFromDateSelected = true
            openDateDialog()
        }

        viewBinding.toDateText.setOnClickListener {
            isFromDateSelected = false
            openDateDialog()
        }

        viewBinding.submit.setOnClickListener {
            handleRejectListService()
//            if (NetworkUtil.isNetworkConnected(requireContext())) {
//                val fromDate = viewBinding.fromDateText.text.toString()
//                val toDate = viewBinding.toDateText.text.toString()
//                if (getDateDifference(fromDate, toDate) > 0) {
//                    showLoading()
//                    viewModel.getFilterApprovedList(
//                        FilterDiscountRequest(
//                            userData.EMPID,
//                            Utils.getFilterDate(viewBinding.fromDateText.text.toString()),
//                            Utils.getFilterDate(viewBinding.toDateText.text.toString()),
//                            "REJECTED"
//                        )
//                    )
//                } else {
//                    Toast.makeText(
//                        requireContext(),
//                        resources.getString(R.string.label_check_dates),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                Toast.makeText(
//                    requireContext(),
//                    resources.getString(R.string.label_network_error),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
        }
    }

    @SuppressLint("NewApi")
    private fun handleRejectListService() {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            val fromDate = viewBinding.fromDateText.text.toString()
            val toDate = viewBinding.toDateText.text.toString()
            if (getDateDifference(fromDate, toDate) > 0) {
                showLoading()
                viewModel.getFilterApprovedList(
                    FilterDiscountRequest(
                        userData.EMPID,
                        Utils.getFilterDate(viewBinding.fromDateText.text.toString()),
                        Utils.getFilterDate(viewBinding.toDateText.text.toString()),
                        "REJECTED"
                    )
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.label_check_dates),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun openDateDialog() {
        if (isFromDateSelected) {
            DiscountCalendarDialog().apply {
                arguments = generateParsedData(viewBinding.fromDateText.text.toString(), "")
            }.show(childFragmentManager, "")
        } else {
            DiscountCalendarDialog().apply {
                arguments = generateParsedData(viewBinding.toDateText.text.toString(),
                    viewBinding.fromDateText.text.toString())
            }.show(childFragmentManager, "")
        }
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        if (isFromDateSelected) {
            isFromDateSelected = false
            viewBinding.fromDateText.setText(showingDate)
            var finalDate = ""
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val datePattern = android.icu.text.SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
                val dateDifference =
                    Utils.getDateDiff(datePattern,
                        viewBinding.fromDateText.text.toString(),
                        Utils.getCurrentDate())
                        .toInt()
                if (dateDifference <= 14) {
                    //Nothing to do
                } else {
                    finalDate =
                        Utils.getCustomHalfMonthDate(viewBinding.fromDateText.text.toString())
                    viewBinding.toDateText.setText(finalDate)
                }
            }
        } else {
            viewBinding.toDateText.setText(showingDate)
        }
    }
}

class ApproveRecyclerView(val orderData: List<RejectedOrderResponse.REJECTEDLISTItem>) :
    SimpleRecyclerView<RejectedLayoutBinding, RejectedOrderResponse.REJECTEDLISTItem>(
        orderData,
        R.layout.rejected_layout
    ) {
    var orderItemsId = ArrayList<String>()

    override fun bindItems(
        binding: RejectedLayoutBinding,
        items: RejectedOrderResponse.REJECTEDLISTItem,
        position: Int,
    ) {
        binding.storeText.text = items.STORE
        binding.postedDate.text = Utlis.convertDate(items.POSTEDDATE)
        binding.storeNameText.text = items.STORE
//        binding.postedDate.text = Utlis.approvedConvertDate(items.POSTEDDATE)
        binding.location.text = items.DCCODE
        binding.dcLocation.text = items.DCNAME
        binding.storeNameText.text = items.STORENAME
        binding.customerNameText.text = items.CUSTNAME
        binding.customerPhNumber.text = items.TELNO
        binding.orderIdText.text = items.INDENTNO
        binding.totalCost.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.calculateTotalCostForRejected(orderData[position].ITEMS)
        )
        binding.discountTotal.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.CalculateTotalDiscountForRejected(orderData[position].ITEMS)
        )
        binding.remainingPayment.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.CalculateNetPaymentForRejected(orderData[position].ITEMS)
        )

        binding.extraData.visibility =
            if (orderItemsId.contains(items.INDENTNO)) View.VISIBLE else View.GONE
        binding.orderHead.setOnClickListener {
            if (orderItemsId.contains(items.INDENTNO)) {
                binding.extraData.visibility = View.GONE
                orderItemsId.remove(items.INDENTNO)
                binding.arrowClose.visibility = View.GONE
                binding.arrow.visibility = View.VISIBLE
            } else {
                binding.extraData.visibility = View.VISIBLE
                orderItemsId.add(items.INDENTNO)
                binding.arrowClose.visibility = View.VISIBLE
                binding.arrow.visibility = View.GONE
            }
        }
        var isMarginRequired = false;
        if (orderData[position].ISMARGIN == 1) {
            isMarginRequired = true
        } else {
            isMarginRequired = false
        }
        binding.recyclerView.adapter =
            OrderAdapter(items = orderData[position].ITEMS, isMarginRequired)

        if (orderData[position].REMARKS.isNullOrEmpty())
            binding.remarksLayout.visibility = View.GONE
        else
            binding.remarksLayout.visibility = View.VISIBLE
        binding.remarksRecycleView.adapter = RemarkAdapter(orderData[position].REMARKS)

        if (orderData[position].STATUSLIST.isNullOrEmpty())
            binding.statusLayout.visibility = View.GONE
        else
            binding.statusLayout.visibility = View.VISIBLE
        binding.statusRecycleView.adapter = StatusAdapter(orderData[position].STATUSLIST)
    }

    class OrderAdapter(
        val items: List<RejectedOrderResponse.ITEMSItem>,
        val isMarginRequired: Boolean,
    ) :
        RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val orderAdapterBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.approved_sub_order,
                parent,
                false
            ) as ApprovedSubOrderBinding
            return ViewHolder(orderAdapterBinding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }

        inner class ViewHolder(private val binding: ApprovedSubOrderBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(pendingOrder: RejectedOrderResponse.ITEMSItem) {
                binding.bulkDisc.text = String.format("%.2f", pendingOrder.APPROVEDDISC)
                binding.medicineNameOrg.text = pendingOrder.ITEMNAME
                binding.quantityText.text = pendingOrder.QTY.toString()
                binding.medicineName.text = pendingOrder.CATEGORY
                binding.priceText.text = pendingOrder.PRICE.toString()
                if (isMarginRequired) {
                    binding.marginDiscLayout.visibility = View.VISIBLE
                    binding.marginDiscText.text = String.format("%.2f", pendingOrder.MARGIN) + "%"
                } else {
                    binding.marginDiscLayout.visibility = View.GONE
                }
            }
        }
    }
}

class RemarkAdapter(val remarkList: List<RejectedOrderResponse.REMARKSItem>) :
    RecyclerView.Adapter<RemarkAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RemarkAdapter.ViewHolder {
        val view = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.remark_layout,
            parent,
            false
        ) as RemarkLayoutBinding
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RemarkAdapter.ViewHolder, position: Int) {
        holder.bind(remarkList[position])
    }

    override fun getItemCount(): Int {
        return remarkList.size
    }

    inner class ViewHolder(val remarkBinding: RemarkLayoutBinding) :
        RecyclerView.ViewHolder(remarkBinding.root) {
        fun bind(remarks: RejectedOrderResponse.REMARKSItem) {
            remarkBinding.remarksText.text = remarks.REMARKS
            remarkBinding.remarkBy.text = remarks.EMPNAME
            remarkBinding.time.text = Utlis.convertDateAddedTimeZone(remarks.CREATEDDATE)
        }
    }
}

class StatusAdapter(val statusList: ArrayList<PendingOrder.STATUSItem>) :
    RecyclerView.Adapter<StatusAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): StatusAdapter.ViewHolder {
        val view = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.status_layout,
            parent,
            false
        ) as StatusLayoutBinding
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusAdapter.ViewHolder, position: Int) {
        holder.bind(statusList[position])
    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    inner class ViewHolder(val statusBinding: StatusLayoutBinding) :
        RecyclerView.ViewHolder(statusBinding.root) {
        fun bind(status: PendingOrder.STATUSItem) {
            statusBinding.statusBy.text = status.STATUS
            if (status.EMPNAME.isNullOrEmpty()) {
                statusBinding.statusStaffDetails.text = status.EMPID
            } else {
                statusBinding.statusStaffDetails.text = status.EMPNAME + " - " + status.EMPID
            }
        }
    }
}