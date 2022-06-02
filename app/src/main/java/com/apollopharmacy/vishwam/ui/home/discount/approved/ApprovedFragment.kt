package com.apollopharmacy.vishwam.ui.home.discount.approved

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
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
import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.FilterData
import com.apollopharmacy.vishwam.data.model.discount.FilterDiscountRequest
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.CustomFilterDialog
import com.apollopharmacy.vishwam.dialog.DiscountCalendarDialog
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.CalculateDiscountAndTotalQuantity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.valdesekamdem.library.mdtoast.MDToast
import java.util.*
import kotlin.collections.ArrayList

class ApprovedFragment : BaseFragment<ApprovedViewModel, FragmentApprovedBinding>(),
    DiscountCalendarDialog.DateSelected, CustomFilterDialog.AbstractDialogClickListner {

    private lateinit var recyclerViewApproved: ApproveRecyclerView
    private var tempRecyclerViewApproved = arrayListOf<ApprovalOrderRequest.APPROVEDLISTItem>()
    lateinit var userData: LoginDetails
    var isFromDateSelected: Boolean = false
    private val TAG = "ApprovedFragment"

    override val layoutRes: Int
        get() = R.layout.fragment_approved

    override fun retrieveViewModel(): ApprovedViewModel {
        return ViewModelProvider(this).get(ApprovedViewModel::class.java)
    }

    override fun setup() {
        viewBinding.viewModel = viewModel

        viewBinding.fromDateText.setText(Utils.getCurrentDate())
        viewBinding.toDateText.setText(Utils.getCurrentDate())
        userData = LoginRepo.getProfile()!!

        handleApprovedList()

        viewModel.approvedList.observe(viewLifecycleOwner, Observer {
            hideLoading()
            if (it.isNullOrEmpty()) {
                viewBinding.emptyList.visibility = View.VISIBLE
                viewBinding.recyclerViewApproved.visibility = View.GONE
                tempRecyclerViewApproved.clear()
                Toast.makeText(requireContext(), "No Approved Data", Toast.LENGTH_SHORT).show()
            } else {
                recyclerViewApproved = ApproveRecyclerView(it)
                tempRecyclerViewApproved.addAll(recyclerViewApproved.orderData)
                viewBinding.recyclerViewApproved.adapter = recyclerViewApproved
                viewBinding.recyclerViewApproved.visibility = View.VISIBLE
                viewBinding.emptyList.visibility = View.GONE
            }
        })

        viewModel.command.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is Command.ShowToast -> {
                    if (it.message.equals("no data found.please check empid")) {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.recyclerViewApproved.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Approved Data", Toast.LENGTH_SHORT)
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

        viewBinding.filterByList.setOnClickListener {
            if (tempRecyclerViewApproved.size > 0) {
                CustomFilterDialog().apply {
                    arguments =
                        CustomFilterDialog().generateParsedData(viewModel.filterMenuData())
                }.show(childFragmentManager, "")
            }
        }
        viewBinding.filterInputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (viewBinding.filterByList.text.toString().isEmpty()) {
                    showCustomToast("Please select any Search Type")
                } else {
                    if (tempRecyclerViewApproved.size > 0) {
                        if (p0.toString().length > 3) {
                            filterApprovedData(recyclerViewApproved.orderData,
                                viewBinding.filterByList.text.toString(),
                                p0.toString())
                        } else {
                            recyclerViewApproved.orderData.clear()
                            recyclerViewApproved.orderData.addAll(tempRecyclerViewApproved)
                            viewBinding.recyclerViewApproved.adapter?.notifyDataSetChanged()
                            if (recyclerViewApproved.orderData.size == 0) {
                                showCustomToast("No Data Found")
                            }
                        }
                    } else {
                        showCustomToast("No Data Found")
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        viewBinding.submit.setOnClickListener {
            handleApprovedList()
        }
    }

    private fun showCustomToast(message: String) {
        MDToast.makeText(activity, message, MDToast.LENGTH_SHORT, MDToast.TYPE_INFO).show()
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

    override fun selectedItem(selectedItemVal: FilterData) {
        if (viewBinding.filterByList.text.toString().isNotEmpty()) {
            viewBinding.filterInputText.setText("")
        }
        viewBinding.filterByList.setText(selectedItemVal.MenuTitle)
    }

    fun filterApprovedData(
        approvedList: ArrayList<ApprovalOrderRequest.APPROVEDLISTItem>,
        inputType: String,
        filterText: String,
    ) {
        if (inputType.isNotEmpty()) {
            if (inputType.equals("Store ID")) {
                recyclerViewApproved.orderData.clear()
                for (item in tempRecyclerViewApproved) {
                    if (item.STORE.trim().contains(filterText.trim())) {
                        recyclerViewApproved.orderData.add(item)
                    }
                }
                viewBinding.recyclerViewApproved.adapter?.notifyDataSetChanged()
                if (recyclerViewApproved.orderData.size == 0) {
                    showCustomToast("No Data Found")
                }
//                val filterData = approvedList.filter { m -> m.STORE == filterText }
//                viewModel.filterData(filterData as ArrayList<ApprovalOrderRequest.APPROVEDLISTItem>)
            } else if (inputType.equals("DC Code")) {
                recyclerViewApproved.orderData.clear()
                for (item in tempRecyclerViewApproved) {
                    if (item.DCCODE!!.trim().contains(filterText.trim())) {
                        recyclerViewApproved.orderData.add(item)
                    }
                }
                viewBinding.recyclerViewApproved.adapter?.notifyDataSetChanged()
                if (recyclerViewApproved.orderData.size == 0) {
                    showCustomToast("No Data Found")
                }
            } else if (inputType.equals("Order ID")) {
                recyclerViewApproved.orderData.clear()
                for (item in tempRecyclerViewApproved) {
                    if (item.INDENTNO.toUpperCase(Locale.getDefault()).trim()
                            .contains(filterText.toUpperCase(
                                Locale.getDefault()).trim())
                    ) {
                        recyclerViewApproved.orderData.add(item)
                    }
                }
                viewBinding.recyclerViewApproved.adapter?.notifyDataSetChanged()
                if (recyclerViewApproved.orderData.size == 0) {
                    showCustomToast("No Data Found")
                }
            } else if (inputType.equals("Item Name")) {
                recyclerViewApproved.orderData.clear()
                for (item in tempRecyclerViewApproved) {
                    for (itemData in item.ITEMS) {
                        if (itemData.ITEMNAME!!.toUpperCase(Locale.getDefault()).trim()
                                .contains(filterText.toUpperCase(
                                    Locale.getDefault()).trim())
                        ) {
                            recyclerViewApproved.orderData.add(item)
                        }
                    }
                }
                viewBinding.recyclerViewApproved.adapter?.notifyDataSetChanged()
                if (recyclerViewApproved.orderData.size == 0) {
                    showCustomToast("No Data Found")
                }
            } else if (inputType.equals("Item ID")) {
                recyclerViewApproved.orderData.clear()
                for (item in tempRecyclerViewApproved) {
                    for (itemData in item.ITEMS) {
                        if (itemData.ITEMID!!.toUpperCase(Locale.getDefault()).trim()
                                .contains(filterText.toUpperCase(
                                    Locale.getDefault()).trim())
                        ) {
                            recyclerViewApproved.orderData.add(item)
                        }
                    }
                }
                viewBinding.recyclerViewApproved.adapter?.notifyDataSetChanged()
                if (recyclerViewApproved.orderData.size == 0) {
                    showCustomToast("No Data Found")
                }
            }
        } else {
            showCustomToast("Please select any Search Type")
        }
    }

    @SuppressLint("NewApi")
    private fun handleApprovedList() {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            val fromDate = viewBinding.fromDateText.text.toString()
            val toDate = viewBinding.toDateText.text.toString()
            if (Utils.getDateDifference(fromDate, toDate) > 0) {
                showLoading()
                viewModel.getFilterApprovedList(
                    FilterDiscountRequest(
                        userData.EMPID,
                        Utils.getFilterDate(viewBinding.fromDateText.text.toString()),
                        Utils.getFilterDate(viewBinding.toDateText.text.toString()),
                        "APPROVED"
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
}

class ApproveRecyclerView(val orderData: ArrayList<ApprovalOrderRequest.APPROVEDLISTItem>) :
    SimpleRecyclerView<ApprovedOrderBinding, ApprovalOrderRequest.APPROVEDLISTItem>(
        orderData,
        R.layout.approved_order
    ) {
    var orderItemsId = ArrayList<String>()

    override fun bindItems(
        binding: ApprovedOrderBinding,
        items: ApprovalOrderRequest.APPROVEDLISTItem,
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

        binding.extraData.visibility =
            if (orderItemsId.contains(items.INDENTNO)) View.VISIBLE else View.GONE

        binding.totalCost.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.calculateTotalCostForApproval(orderData[position].ITEMS)
        )
        binding.discountTotal.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.CalculateTotalDiscountForApproval(orderData[position].ITEMS)
        )
        binding.remainingPayment.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.CalculateNetPaymentForApproval(orderData[position].ITEMS)
        )

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
}

class OrderAdapter(
    val items: List<ApprovalOrderRequest.ITEMSItem>,
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
        fun bind(pendingOrder: ApprovalOrderRequest.ITEMSItem) {
            binding.bulkDisc.text = String.format("%.2f", pendingOrder.APPROVED_DISC)
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

class RemarkAdapter(val remarkList: List<ApprovalOrderRequest.REMARKSItem>) :
    RecyclerView.Adapter<RemarkAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.remark_layout,
            parent,
            false
        ) as RemarkLayoutBinding
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(remarkList[position])
    }

    override fun getItemCount(): Int {
        return remarkList.size
    }

    inner class ViewHolder(val remarkBinding: RemarkLayoutBinding) :
        RecyclerView.ViewHolder(remarkBinding.root) {
        fun bind(remarks: ApprovalOrderRequest.REMARKSItem) {
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