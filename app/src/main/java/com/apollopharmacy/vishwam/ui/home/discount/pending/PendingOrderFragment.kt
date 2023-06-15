package com.apollopharmacy.vishwam.ui.home.discount.pending

import android.content.Intent
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.data.Config.TYPE_ACCEPT
import com.apollopharmacy.vishwam.data.Config.TYPE_REJECT
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.discount.AcceptOrRejectDiscountOrder
import com.apollopharmacy.vishwam.data.model.discount.BulkAcceptOrRejectDiscountOrder
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.MainActivity.userDesignation
import com.apollopharmacy.vishwam.ui.home.discount.filter.FilterFragment
import com.apollopharmacy.vishwam.ui.home.discount.pending.dashboardfilter.DashboardFilterActivity
import com.apollopharmacy.vishwam.ui.home.qcfail.qcfilter.QcFilterActivity
import com.apollopharmacy.vishwam.ui.login.Command
import com.apollopharmacy.vishwam.util.*
import com.valdesekamdem.library.mdtoast.MDToast
import kotlin.math.roundToInt

class PendingOrderFragment : BaseFragment<PendingViewModel, FragmentPendingOrderBinding>(),
    PendingRecyclerView.ClickListener, FilterFragment.FilterClicked {

    private lateinit var pendingRecyclerView: PendingRecyclerView
    private val TAG = "PendingOrderFragment"
    private var acptRejcIndentNo: String = ""
    private var isBulkChecked: Boolean = false

    override val layoutRes: Int
        get() = R.layout.fragment_pending_order

    override fun retrieveViewModel(): PendingViewModel {
        return ViewModelProvider(this)[PendingViewModel::class.java]
    }

    override fun setup() {
        viewBinding.pendingViewModel = viewModel
        viewModel.getDiscountColorDetails()

        if (NetworkUtil.isNetworkConnected(requireContext())) {
            showLoading()
            viewModel.getPendingList(false)
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        viewModel.pendingList.observe(viewLifecycleOwner) {



            if (it.isEmpty() || it.size == 0) {
                hideLoading()
                viewBinding.emptyList.visibility = View.VISIBLE
                viewBinding.bulkAppRejLayout.visibility = View.GONE
                viewBinding.recyclerViewPending.visibility = View.GONE
            } else {
                viewBinding.emptyList.visibility = View.GONE
                viewBinding.recyclerViewPending.visibility = View.VISIBLE


                pendingRecyclerView = PendingRecyclerView(it, this)
                viewBinding.recyclerViewPending.adapter = pendingRecyclerView
                checkSelectedList("")
                hideLoading()
            }
        }
        viewBinding.filter.setOnClickListener {
//            var pendinglistItem = ArrayList<PendingOrder.PENDINGLISTItem>()
//
//            var remarksList = ArrayList<PendingOrder.REMARKSItem>()
//            var statusList = ArrayList<PendingOrder.STATUSItem>()
//            var itemsList = ArrayList<PendingOrder.ITEMSItem>()
//            val pending = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "16001", "", "16301", "", "", "13001", "", "", 0, "", "", "16301", statusList, false)
//            val pending1 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "16002", "", "16902", "", "", "13001", "", "", 0, "", "", "16902", statusList, false)
//            val pending2 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "16031", "", "16231", "", "", "13001", "", "", 0, "", "", "16231", statusList, false)
//            val pending3 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "13251", "", "13051", "", "", "13001", "", "", 0, "", "", "13051", statusList, false)
//            val pending4 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "13051", "", "13851", "", "", "13001", "", "", 0, "", "", "13851", statusList, false)
//            val pending5 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "14051", "", "12051", "", "", "13001", "", "", 0, "", "", "12051", statusList, false)
//            val pending6 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "14351", "", "14151", "", "", "13001", "", "", 0, "", "", "14151", statusList, false)
//            val pending7 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "15002", "", "15402", "", "", "13001", "", "", 0, "", "", "15402", statusList, false)
//            val pending8 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "17001", "", "17801", "", "", "13001", "", "", 0, "", "", "17801", statusList, false)
//            val pending9 = PendingOrder.PENDINGLISTItem("", "", remarksList, "", itemsList, "", "14051", "", "14851", "", "", "13001", "", "", 0, "", "", "", statusList, false)
//
//
//            val pending10 = PendingOrder.PENDINGLISTItem(
//                "",
//                "",
//                remarksList,
//                "",
//                itemsList,
//                "",
//                "14351",
//                "",
//                "14251",
//                "",
//                "",
//                "13001",
//                "",
//                "",
//                0,
//                "",
//                "",
//                "",
//                statusList,
//                false
//            )
//            val pending11 = PendingOrder.PENDINGLISTItem(
//                "",
//                "",
//                remarksList,
//                "",
//                itemsList,
//                "",
//                "15002",
//                "",
//                "15042",
//                "",
//                "",
//                "13001",
//                "",
//                "",
//                0,
//                "",
//                "",
//                "",
//                statusList,
//                false
//            )
//            val pending12 = PendingOrder.PENDINGLISTItem(
//                "",
//                "",
//                remarksList,
//                "",
//                itemsList,
//                "",
//                "17001",
//                "",
//                "13001",
//                "",
//                "",
//                "13001",
//                "",
//                "",
//                0,
//                "",
//                "",
//                "",
//                statusList,
//                false
//            )
//
//
//
//
//            pendinglistItem.add(pending)
//            pendinglistItem.add(pending1)
//            pendinglistItem.add(pending2)
//            pendinglistItem.add(pending3)
//            pendinglistItem.add(pending4)
//            pendinglistItem.add(pending5)
//            pendinglistItem.add(pending6)
//            pendinglistItem.add(pending7)
//            pendinglistItem.add(pending8)
//            pendinglistItem.add(pending9)
//            pendinglistItem.add(pending10)
//            pendinglistItem.add(pending11)
//            pendinglistItem.add(pending12)
//
//
//            val i = Intent(context, DashboardFilterActivity::class.java)
//
//            i.putExtra("storeList", pendinglistItem)
//            startActivityForResult(i, 210)

            viewModel.filterClicked()
        }
        viewModel.acceptRequest.observe(viewLifecycleOwner) {
            if (it.STATUS) {
                if (pendingRecyclerView.pendingOrderList.size <= 10) {
                    viewModel.getPendingList(false)
                } else {
                    hideLoading()
                    val separated: List<String> = acptRejcIndentNo.split(",")
                    val tempPendingArr: ArrayList<PendingOrder.PENDINGLISTItem> = ArrayList()
                    var itemPos: Int = -1
                    for (item in pendingRecyclerView.pendingOrderList) {
                        itemPos++
                        for (orderItem in separated) {
                            if (orderItem.trim().equals(item.INDENTNO)) {
                                tempPendingArr.add(item)
                                //                           pendingRecyclerView.pendingOrderList.removeAt(itemPos)
//                                pendingRecyclerView.notifyItemRemoved(itemPos)
//                                break
                            }
                        }
                    }
                    for (slctItem in tempPendingArr) {
                        for (item in pendingRecyclerView.pendingOrderList) {
                            if (slctItem.INDENTNO.equals(item.INDENTNO)) {
                                pendingRecyclerView.pendingOrderList.remove(item)
                                break
                            }
                        }
                    }
                    checkSelectedList("")
                    pendingRecyclerView.notifyDataSetChanged()
                }
            } else {
                hideLoading()
                ShowError.showToastMessage(it.MESSAGE, requireContext())
            }
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
                    if (command.message == "no data found.please check empid") {
                        viewBinding.emptyList.visibility = View.VISIBLE
                        viewBinding.bulkAppRejLayout.visibility = View.GONE
                        viewBinding.recyclerViewPending.visibility = View.GONE
                        Toast.makeText(requireContext(), "No Pending Data", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), command.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {}
            }
        }
        viewBinding.refreshSwipe.setOnRefreshListener {
            viewModel.getPendingList(true)
        }
        viewBinding.acceptClick.setOnClickListener {
            checkSelectedList(TYPE_ACCEPT)
        }
        viewBinding.rejectClick.setOnClickListener {
            checkSelectedList(TYPE_REJECT)
        }
        viewBinding.selectAllLayout.setOnClickListener {
            if (isBulkChecked) {
                isBulkChecked = false
                for (item in pendingRecyclerView.pendingOrderList) {
                    item.isItemChecked = false
                }
                viewBinding.bulkSelectCheck.setImageResource(R.drawable.icon_item_unchecked)
            } else {
                isBulkChecked = true
                for (item in pendingRecyclerView.pendingOrderList) {
                    item.isItemChecked = true
                }
                viewBinding.bulkSelectCheck.setImageResource(R.drawable.icon_item_checked)
            }
            pendingRecyclerView.notifyDataSetChanged()
            checkSelectedList("")
        }
    }

    override fun orderAccepted(orderdetails: PendingOrder.PENDINGLISTItem, position: Int) {
        sendRequestForAcceptAndReject(orderdetails, TYPE_ACCEPT)
    }

    override fun orderReject(orderdetails: PendingOrder.PENDINGLISTItem) {
        sendRequestForAcceptAndReject(orderdetails, TYPE_REJECT)
    }

    override fun openDialogBoxTOEditDiscount(position: Int, pendingOrder: PendingOrder.ITEMSItem) {
        showEditDiscountDialogBox(pendingOrder, position)
    }

    override fun showMessageToast(message: String) {
        MDToast.makeText(activity, message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show()
    }

    override fun onItemChecked(orderdetails: PendingOrder.PENDINGLISTItem, position: Int) {
        orderdetails.isItemChecked = !orderdetails.isItemChecked
        pendingRecyclerView.notifyItemChanged(position)
        checkSelectedList("")
    }

    private fun checkSelectedList(status: String) {
        var pendingItemChecked: Boolean = false;
        for (item in pendingRecyclerView.pendingOrderList) {
            if (item.isItemChecked) {
                pendingItemChecked = true
                break
            }
        }
        if (pendingItemChecked) {
            val params: RelativeLayout.LayoutParams =
                RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT)
            params.setMargins(0, 0, 0, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60f,
                Resources.getSystem().displayMetrics
            ).roundToInt())
            this.viewBinding.parentLayout.setLayoutParams(params)
            viewBinding.bulkAppRejLayout.visibility = View.VISIBLE
        } else {
//            val params: RelativeLayout.LayoutParams =
//                RelativeLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
//                    ConstraintLayout.LayoutParams.MATCH_PARENT)
//            params.setMargins(0, 0, 0, 0)
//            this.viewBinding.parentLayout.setLayoutParams(params)
//            viewBinding.bulkAppRejLayout.visibility = View.GONE
        }
        if (status.isNotEmpty()) {
            val selectedItemList: List<PendingOrder.PENDINGLISTItem> = ArrayList()
            for (item in pendingRecyclerView.pendingOrderList) {
                if (item.isItemChecked) {
                    (selectedItemList as ArrayList).add(item)
                }
            }
            sendBulkRequestForAcceptAndReject(selectedItemList, status)
        }
    }

    private fun sendRequestForAcceptAndReject(orderdetails: PendingOrder.PENDINGLISTItem, type: String, ) {
        if (orderdetails.REMARK!!.isNotEmpty()) {
            showOrderAcceptationDialog(
                AcceptOrRejectDiscountOrder(AcceptOrRejectDiscountOrder.ORDER(orderdetails.TRACKINGREFNAME,
                        orderdetails.BULKDISCOUNTPER,
                        orderdetails.TRACKINGREFCODE,
                        orderdetails.ITEMS,
                        orderdetails.TERMINALID,
                        orderdetails.STORENAME,
                        orderdetails.STATUS,
                        orderdetails.DCCODE,
                        orderdetails.POSTEDDATE,
                        orderdetails.CUSTNAME,
                        orderdetails.STORE,
                        Preferences.getToken(),
                        orderdetails.INDENTNO,
                        orderdetails.TELNO), orderdetails.REMARK, TYPE = type
                )
            )
        } else {
            showOrderAcceptationDialog(
                AcceptOrRejectDiscountOrder(
                    AcceptOrRejectDiscountOrder.ORDER(
                        orderdetails.TRACKINGREFNAME,
                        orderdetails.BULKDISCOUNTPER,
                        orderdetails.TRACKINGREFCODE,
                        orderdetails.ITEMS,
                        orderdetails.TERMINALID,
                        orderdetails.STORENAME,
                        orderdetails.STATUS,
                        orderdetails.DCCODE,
                        orderdetails.POSTEDDATE,
                        orderdetails.CUSTNAME,
                        orderdetails.STORE,
                        Preferences.getToken(),
                        orderdetails.INDENTNO,
                        orderdetails.TELNO
                    ), null, TYPE = type
                )
            )
        }
    }

    private fun showOrderAcceptationDialog(item: AcceptOrRejectDiscountOrder) {
        var type: String
        if (item.TYPE == TYPE_ACCEPT) {
            type = "accepting"
        } else {
            type = "rejecting"
        }
        acptRejcIndentNo = item.ORDERS.INDENTNO
        AlertDialog
            .Builder(requireContext())
            .setMessage("You are $type the order id  ${item.ORDERS.INDENTNO} for discount  \nDo you want to proceed?")
            .setPositiveButton("YES") { _, _ ->
                if (NetworkUtil.isNetworkConnected(requireContext())) {
                    showLoading()
                    viewModel.callAcceptOrder(item)
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.label_network_error),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            .setNegativeButton("NO") { d, _ -> d.dismiss() }
            .show()
    }

    private fun sendBulkRequestForAcceptAndReject(
        selectedItemList: List<PendingOrder.PENDINGLISTItem>,
        type: String,
    ) {
        val bulkOrders: ArrayList<BulkAcceptOrRejectDiscountOrder.ORDER> = ArrayList()
        for (i in selectedItemList.indices) {
            val bulkItem: BulkAcceptOrRejectDiscountOrder.ORDER =
                BulkAcceptOrRejectDiscountOrder.ORDER(selectedItemList[i].TRACKINGREFNAME,
                    selectedItemList[i].BULKDISCOUNTPER,
                    selectedItemList[i].TRACKINGREFCODE,
                    selectedItemList[i].ITEMS,
                    selectedItemList[i].TERMINALID,
                    selectedItemList[i].STORENAME,
                    selectedItemList[i].STATUS,
                    selectedItemList[i].DCCODE,
                    selectedItemList[i].POSTEDDATE,
                    selectedItemList[i].CUSTNAME,
                    selectedItemList[i].STORE,
                    Preferences.getToken(),
                    selectedItemList[i].INDENTNO,
                    selectedItemList[i].TELNO)
            bulkOrders.add(bulkItem)
        }
        showBulkOrderAcceptationDialog(BulkAcceptOrRejectDiscountOrder(bulkOrders, type), type)
    }

    private fun showBulkOrderAcceptationDialog(
        acptRejItem: BulkAcceptOrRejectDiscountOrder,
        reqType: String,
    ) {
        var type: String
        if (reqType == TYPE_ACCEPT) {
            type = "accepting"
        } else {
            type = "rejecting"
        }
        acptRejcIndentNo = ""
        var orderNumbers: String = ""
        if (acptRejItem.ORDERS.size == 1) {
            orderNumbers = acptRejItem.ORDERS.get(0).INDENTNO
            acptRejcIndentNo = orderNumbers
            AlertDialog
                .Builder(requireContext())
                .setMessage("You are $type the order id  ${orderNumbers} for discount  \nDo you want to proceed?")
                .setPositiveButton("YES") { _, _ ->
                    if (NetworkUtil.isNetworkConnected(requireContext())) {
                        showLoading()
                        viewModel.callBulkAcceptOrder(acptRejItem)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.label_network_error),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                .setNegativeButton("NO") { d, _ -> d.dismiss() }
                .show()
        } else {
            for (item in acptRejItem.ORDERS) {
                if (orderNumbers.isEmpty()) {
                    orderNumbers = acptRejItem.ORDERS.get(0).INDENTNO
                } else {
                    orderNumbers = orderNumbers + ", " + item.INDENTNO
                }
            }
            if (orderNumbers.isEmpty()) {
                Toast.makeText(requireContext(), "Select atleast one order", Toast.LENGTH_SHORT)
                    .show()
            } else {
                acptRejcIndentNo = orderNumbers
                AlertDialog
                    .Builder(requireContext())
                    .setMessage("You are $type the order id's  ${orderNumbers} for discount  \nDo you want to proceed?")
                    .setPositiveButton("YES") { _, _ ->
                        if (NetworkUtil.isNetworkConnected(requireContext())) {
                            showLoading()
                            viewModel.callBulkAcceptOrder(acptRejItem)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.label_network_error),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    .setNegativeButton("NO") { d, _ -> d.dismiss() }
                    .show()
            }
        }
    }

    private fun showEditDiscountDialogBox(pendingOrder: PendingOrder.ITEMSItem, position: Int) {
        val builder = AlertDialog.Builder(
            requireContext()
        )
        builder.setTitle("Edit Discount")
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.edit_discount, view as ViewGroup?, false)
        val input = viewInflated.findViewById<View>(R.id.input) as EditText
        builder.setView(viewInflated)
        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
//            pendingOrder.REQUEST_DISC = input.text.toString().trim().toDoubleOrNull()
//            pendingRecyclerView.notifyDataSetChanged()
        }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun clickedApply(
        selectedData: String,
        pendingList: ArrayList<PendingOrder.PENDINGLISTItem>, tag: Int, toDate: String,
    ) {
        Utils.printMessage(TAG, "Clicked Apply :: " + selectedData)
        filterForStoreId(pendingList, selectedData, tag, toDate)
    }

    private fun filterForStoreId(
        pendingList: ArrayList<PendingOrder.PENDINGLISTItem>,
        selectedData: String, tag: Int, toDate: String,
    ) {
        if (tag == 0) {
            if (toDate.isNullOrEmpty()) {
                Utils.printMessage(TAG, "Date Filter :: " + tag.toString())
                var filterData = pendingList.filter { m ->
                    m.POSTEDDATE.equals(selectedData)
                }
                viewModel.filterData(filterData as ArrayList<PendingOrder.PENDINGLISTItem>)
            } else {
                var filterData = pendingList.filter { m ->
                    (Utlis.filterDateFormate(selectedData)
                        .before(Utlis.filterDateFormate(m.POSTEDDATE)) &&
                            (Utlis.filterDateFormate(toDate)
                                .after(Utlis.filterDateFormate(m.POSTEDDATE)))) ||
                            Utlis.filterDateFormate(selectedData)
                                .equals(Utlis.filterDateFormate(m.POSTEDDATE))
                }
                viewModel.filterData(filterData as ArrayList<PendingOrder.PENDINGLISTItem>)
            }
        } else if (tag == 1) {
            var filterData = pendingList.filter { m -> m.STORE == selectedData }
            viewModel.filterData(filterData as ArrayList<PendingOrder.PENDINGLISTItem>)
        } else {
            var filterData =
                pendingList.filter { m -> !m.DCCODE.isNullOrEmpty() && m.DCCODE == selectedData }
            viewModel.filterData(filterData as ArrayList<PendingOrder.PENDINGLISTItem>)
        }
    }
}

class PendingRecyclerView(
    val pendingOrderList: ArrayList<PendingOrder.PENDINGLISTItem>,
    private val listener: ClickListener,
) :
    SimpleRecyclerView<PendingOrderAdapterBinding, PendingOrder.PENDINGLISTItem>(pendingOrderList,
        R.layout.pending_order_adapter) {
    private val orderItemsId = ArrayList<String>()
    override fun bindItems(
        binding: PendingOrderAdapterBinding,
        items: PendingOrder.PENDINGLISTItem,
        position: Int,
    ) {
        binding.storeIdText.text = items.STORE
        binding.postedDate.text =items.POSTEDDATE
        binding.locations.text = items.DCCODE
        binding.dcLocation.text = items.DCNAME
        binding.storeNameText.text = items.STORENAME
        binding.customerNameText.text = items.CUSTNAME
        binding.customerPhNumber.text = items.TELNO
        binding.orderIdText.text = items.INDENTNO
        binding.statusDetails.text = items.STATUS
        binding.totalCost.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.calculateTotalCost(pendingOrderList[position].ITEMS)
        )
        binding.discountTotal.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.CalculateTotalDiscount(pendingOrderList[position].ITEMS)
        )
        binding.remainingPayment.text = String.format(
            "%.2f",
            CalculateDiscountAndTotalQuantity.CalculateNetPayment(pendingOrderList[position].ITEMS)
        )

        if (userDesignation.isEmpty()) {
            binding.checkBoxLayout.visibility = View.GONE
        } else {
            if (userDesignation.equals("CEO") || userDesignation.equals("GENERAL MANAGER")) {
                binding.checkBoxLayout.visibility = View.VISIBLE
            } else {
                binding.checkBoxLayout.visibility = View.GONE
            }
        }

        if (items.isItemChecked) {
            binding.checkBox.setImageResource(R.drawable.icon_item_checked)
        } else {
            binding.checkBox.setImageResource(R.drawable.icon_item_unchecked)
        }

        val isMarginRequired: Boolean
        isMarginRequired = pendingOrderList[position].ISMARGIN == 1

        updateCatAvgValues(binding, pendingOrderList[position])

        binding.recyclerView.adapter =
            OrderAdapter(items = pendingOrderList[position].ITEMS,
                items,
                isMarginRequired,
                binding,
                object : OrderAdapter.SubItemClick {
                    override fun clickedSubItem(
                        position: Int,
                        pendingOrder: PendingOrder.ITEMSItem,
                    ) {
                        listener.openDialogBoxTOEditDiscount(position, pendingOrder)
                    }

                    override fun showMessage(message: String) {
                        listener.showMessageToast(message)
                    }

                    override fun changedAmountForTotal(pendingOrder: List<PendingOrder.ITEMSItem>) {
                        binding.totalCost.text = String.format(
                            "%.2f",
                            CalculateDiscountAndTotalQuantity.calculateTotalCost(pendingOrder)
                        )
                        binding.discountTotal.text = String.format(
                            "%.2f",
                            CalculateDiscountAndTotalQuantity.CalculateTotalDiscount(pendingOrder)
                        )
                        binding.remainingPayment.text = String.format(
                            "%.2f",
                            CalculateDiscountAndTotalQuantity.CalculateNetPayment(pendingOrder)
                        )
                        //updatePriceValues(binding, pendingOrderList[position].ITEMS)
                    }

                    override fun updateAvgForItemLevel(pendingOrder: List<PendingOrder.ITEMSItem>) {
                        var pharmaTotalDisc: Double = 0.0
                        var fmcgTotalDisc: Double = 0.0
                        var plTotalDisc: Double = 0.0
                        var surgTotalDisc: Double = 0.0
                        var otherTotalDisc: Double = 0.0
                        var pharmaItems: Int = 0
                        var fmcgItems: Int = 0
                        var plItems: Int = 0
                        var surgItmes: Int = 0
                        var otherItmes: Int = 0
                        var pharmaAvgDisc: Double = 0.0
                        var fmcgAvgDisc: Double = 0.0
                        var plAvgDisc: Double = 0.0
                        var surgAvgDisc: Double = 0.0
                        var otherAvgDisc: Double = 0.0
                        for (items in pendingOrder) {
                            if (items.CATEGORY.equals("PHARMA")) {
                                pharmaItems = pharmaItems + 1
                                pharmaTotalDisc = pharmaTotalDisc + items.APPROVED_DISC!!
                            } else if (items.CATEGORY.equals("FMCG")) {
                                fmcgItems = fmcgItems + 1
                                fmcgTotalDisc = fmcgTotalDisc + items.APPROVED_DISC!!
                            } else if (items.CATEGORY.equals("PRIVATE LABEL")) {
                                plItems = plItems + 1
                                plTotalDisc = plTotalDisc + items.APPROVED_DISC!!
                            } else if (items.CATEGORY.equals("SURGICAL")) {
                                surgItmes = surgItmes + 1
                                surgTotalDisc = surgTotalDisc + items.APPROVED_DISC!!
                            } else {
                                otherItmes = otherItmes + 1
                                otherTotalDisc = otherTotalDisc + items.APPROVED_DISC!!
                            }
                        }
                        pharmaAvgDisc = pharmaTotalDisc / pharmaItems
                        fmcgAvgDisc = fmcgTotalDisc / fmcgItems
                        plAvgDisc = plTotalDisc / plItems
                        surgAvgDisc = surgTotalDisc / surgItmes
                        otherAvgDisc = otherTotalDisc / otherItmes

                        Utils.printMessage("PendingOrderFrag",
                            "Pharma : " + pharmaAvgDisc + ", FMCG : " + fmcgAvgDisc + ", PL : " + plAvgDisc + ", Surgical : " + surgAvgDisc + ", Other : " + otherAvgDisc)
                        binding.pharmaBulkDisc.setText(
                            String.format(
                                "%.2f",
                                pharmaAvgDisc
                            )
                        )
                        binding.fmcgBulkDisc.setText(
                            String.format(
                                "%.2f",
                                fmcgAvgDisc
                            )
                        )
                        binding.plBulkDisc.setText(
                            String.format(
                                "%.2f",
                                plAvgDisc
                            )
                        )
                        binding.surgicalBulkDisc.setText(
                            String.format(
                                "%.2f",
                                surgAvgDisc
                            )
                        )
                        binding.otherBulkDisc.setText(
                            String.format(
                                "%.2f",
                                otherAvgDisc
                            )
                        )
                        if (binding.updateAvgLayout.visibility == View.VISIBLE) {
                            binding.updateAvgLayout.visibility = View.GONE
                        }
                    }
                })

        if (pendingOrderList[position].REMARKS.isNullOrEmpty())
            binding.remarkLayout.visibility = View.GONE
        else
            binding.remarkLayout.visibility = View.VISIBLE
        binding.remarksRecycleView.adapter = RemarkAdapter(pendingOrderList[position].REMARKS)
        if (pendingOrderList[position].STATUSLIST.isNullOrEmpty())
            binding.statusLayout.visibility = View.GONE
        else
            binding.statusLayout.visibility = View.VISIBLE
//        binding.trackStatusBtn.setOnClickListener {
//            DiscountTrackDialog().apply {
//                arguments =
//                    DiscountTrackDialog().generateParsedData(pendingOrderList[position].STATUSLIST)
//            }.show(childFragmentManager, "")
//        }
        binding.statusRecycleView.adapter = StatusAdapter(pendingOrderList[position].STATUSLIST)

        binding.extraData.visibility =
            if (orderItemsId.contains(items.INDENTNO)) View.VISIBLE else View.GONE

        if (userDesignation.isEmpty()) {
            binding.pendingLayout.setOnClickListener {
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
        } else {
            if (userDesignation.equals("CEO") || userDesignation.equals("GENERAL MANAGER")) {
                binding.itemHeaderLayout.setOnClickListener {
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
                binding.checkBoxLayout.setOnClickListener {
                    listener.onItemChecked(items, position)
                }
            } else {
                binding.pendingLayout.setOnClickListener {
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
            }
        }

        binding.writeRemarks.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNullOrEmpty())
                    items.REMARK = ""
                else
                    items.REMARK = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.rejectClick.setOnClickListener { listener.orderReject(items) }
        binding.acceptClick.setOnClickListener { listener.orderAccepted(items, position) }
    }

    private fun updateCatAvgValues(
        binding: PendingOrderAdapterBinding,
        pendingOrderListItem: PendingOrder.PENDINGLISTItem,
    ) {
        var otherCatName: String = ""
        var pharmaTotalDisc: Double = 0.0
        var fmcgTotalDisc: Double = 0.0
        var plTotalDisc: Double = 0.0
        var surgTotalDisc: Double = 0.0
        var otherTotalDisc: Double = 0.0
        var pharmaItems: Int = 0
        var fmcgItems: Int = 0
        var plItems: Int = 0
        var surgItmes: Int = 0
        var otherItmes: Int = 0
        var pharmaAvgDisc: Double = 0.0
        var fmcgAvgDisc: Double = 0.0
        var plAvgDisc: Double = 0.0
        var surgAvgDisc: Double = 0.0
        var otherAvgDisc: Double = 0.0

        for (items in pendingOrderListItem.ITEMS) {
            if (items.CATEGORY.equals("PHARMA")) {
                pharmaItems = pharmaItems + 1
                pharmaTotalDisc = pharmaTotalDisc + items.APPROVED_DISC!!
            } else if (items.CATEGORY.equals("FMCG")) {
                fmcgItems = fmcgItems + 1
                fmcgTotalDisc = fmcgTotalDisc + items.APPROVED_DISC!!
            } else if (items.CATEGORY.equals("PRIVATE LABEL")) {
                plItems = plItems + 1
                plTotalDisc = plTotalDisc + items.APPROVED_DISC!!
            } else if (items.CATEGORY.equals("SURGICAL")) {
                surgItmes = surgItmes + 1
                surgTotalDisc = surgTotalDisc + items.APPROVED_DISC!!
            } else {
                otherCatName = items.CATEGORY.toString()
                otherItmes = otherItmes + 1
                otherTotalDisc = otherTotalDisc + items.APPROVED_DISC!!
            }
        }
        pharmaAvgDisc = pharmaTotalDisc / pharmaItems
        fmcgAvgDisc = fmcgTotalDisc / fmcgItems
        plAvgDisc = plTotalDisc / plItems
        surgAvgDisc = surgTotalDisc / surgItmes
        otherAvgDisc = otherTotalDisc / otherItmes

        if (pharmaItems > 0 || fmcgItems > 0 || plItems > 0 || surgItmes > 0 || otherItmes > 0) {
            for (items in pendingOrderListItem.ITEMS) {
                if (items.CATEGORY.equals("PHARMA")) {
                    if (pharmaAvgDisc >= items.ORIGINAL_DISC!!) {
                        items.ORIGINAL_DISC = pharmaAvgDisc
                    }
                } else if (items.CATEGORY.equals("FMCG")) {
                    if (fmcgAvgDisc >= items.ORIGINAL_DISC!!) {
                        items.ORIGINAL_DISC = fmcgAvgDisc
                    }
                } else if (items.CATEGORY.equals("PRIVATE LABEL")) {
                    if (plAvgDisc >= items.ORIGINAL_DISC!!) {
                        items.ORIGINAL_DISC = plAvgDisc
                    }
                } else if (items.CATEGORY.equals("SURGICAL")) {
                    if (surgAvgDisc >= items.ORIGINAL_DISC!!) {
                        items.ORIGINAL_DISC = surgAvgDisc
                    }
                } else {
                    if (otherAvgDisc >= items.ORIGINAL_DISC!!) {
                        items.ORIGINAL_DISC = otherAvgDisc
                    }
                }
            }

            var categoryData: String = ""
//            binding.avgLayout.visibility = View.VISIBLE
            if (pharmaItems > 0) {
                binding.pharmaAvgLayout.visibility = View.VISIBLE
                binding.pharmaBulkDisc.setText(String.format("%.2f", pharmaAvgDisc))
            } else {
                binding.pharmaAvgLayout.visibility = View.GONE
            }
            if (fmcgItems > 0) {
                binding.fmcgAvgLayout.visibility = View.VISIBLE
                binding.fmcgBulkDisc.setText(String.format("%.2f", fmcgAvgDisc))
            } else {
                binding.fmcgAvgLayout.visibility = View.GONE
            }
            if (plItems > 0) {
                binding.plAvgLayout.visibility = View.VISIBLE
                binding.plBulkDisc.setText(String.format("%.2f", plAvgDisc))
            } else {
                binding.plAvgLayout.visibility = View.GONE
            }
            if (surgItmes > 0) {
                binding.surgicalAvgLayout.visibility = View.VISIBLE
                binding.surgicalBulkDisc.setText(String.format("%.2f", surgAvgDisc))
            } else {
                binding.surgicalAvgLayout.visibility = View.GONE
            }
            if (otherItmes > 0) {
                binding.otherAvgLayout.visibility = View.VISIBLE
                binding.otherDiscLabel.setText(otherCatName)
                binding.otherBulkDisc.setText(String.format("%.2f", otherAvgDisc))
            } else {
                binding.otherAvgLayout.visibility = View.GONE
            }
            binding.categoryAvgData.setText(categoryData)
        } else {
            binding.avgLayout.visibility = View.GONE
        }
        var isItemUpdating = false
        binding.pharmaAdd.setOnClickListener {
            isItemUpdating = true
            binding.pharmaAdd.setTag(null);
            addBulkDiscount(binding,
                binding.pharmaBulkDisc.text.toString().toDouble(),
                pharmaAvgDisc,
                pendingOrderListItem, binding.pharmaBulkDisc
            )
        }
        binding.pharmaSub.setOnClickListener {
            isItemUpdating = true
            binding.pharmaAdd.setTag(null);
            subtractBulkDiscount(binding,
                binding.pharmaBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                binding.pharmaBulkDisc
            )
        }

        binding.pharmaBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    binding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(binding,
                        pharmaAvgDisc,
                        binding.pharmaBulkDisc,
                        pendingOrderListItem.ITEMS)
                }
            }
        })

        binding.fmcgAdd.setOnClickListener {
            isItemUpdating = true
            addBulkDiscount(binding,
                binding.fmcgBulkDisc.text.toString().toDouble(),
                fmcgAvgDisc,
                pendingOrderListItem, binding.fmcgBulkDisc
            )
        }
        binding.fmcgSub.setOnClickListener {
            isItemUpdating = true
            subtractBulkDiscount(binding,
                binding.fmcgBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                binding.fmcgBulkDisc
            )
        }
        binding.fmcgBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    binding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(binding,
                        fmcgAvgDisc,
                        binding.fmcgBulkDisc,
                        pendingOrderListItem.ITEMS)
                }
            }
        })
        binding.plAdd.setOnClickListener {
            isItemUpdating = true
            addBulkDiscount(binding,
                binding.plBulkDisc.text.toString().toDouble(),
                plAvgDisc,
                pendingOrderListItem, binding.plBulkDisc
            )
        }
        binding.plSub.setOnClickListener {
            isItemUpdating = true
            subtractBulkDiscount(binding,
                binding.plBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                binding.plBulkDisc
            )
        }
        binding.plBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    binding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(binding,
                        plAvgDisc,
                        binding.plBulkDisc,
                        pendingOrderListItem.ITEMS)
                }
            }
        })
        binding.surgicalAdd.setOnClickListener {
            isItemUpdating = true
            addBulkDiscount(binding,
                binding.surgicalBulkDisc.text.toString().toDouble(),
                surgAvgDisc,
                pendingOrderListItem, binding.surgicalBulkDisc
            )
        }
        binding.surgicalSub.setOnClickListener {
            isItemUpdating = true
            subtractBulkDiscount(binding,
                binding.surgicalBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                binding.surgicalBulkDisc
            )
        }
        binding.surgicalBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    binding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(binding,
                        surgAvgDisc,
                        binding.surgicalBulkDisc,
                        pendingOrderListItem.ITEMS)
                }
            }
        })
        binding.otherAdd.setOnClickListener {
            isItemUpdating = true
            addBulkDiscount(binding,
                binding.otherBulkDisc.text.toString().toDouble(),
                otherAvgDisc,
                pendingOrderListItem, binding.otherBulkDisc
            )
        }
        binding.otherSub.setOnClickListener {
            isItemUpdating = true
            subtractBulkDiscount(binding,
                binding.otherBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                binding.otherBulkDisc
            )
        }
        binding.otherBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    binding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(binding,
                        otherAvgDisc,
                        binding.otherBulkDisc,
                        pendingOrderListItem.ITEMS)
                }
            }
        })
    }

    private fun updateAvgChangedVal(
        binding: PendingOrderAdapterBinding,
        avgDisc: Double,
        changeEditText: EditText,
        items: List<PendingOrder.ITEMSItem>,
    ) {
        binding.updateAvgBtn.setOnClickListener {
            if (changeEditText.text.toString().isNotEmpty()) {
                if (changeEditText.text.toString().toDouble() <= avgDisc) {
                    updatePriceValues(binding, items)
                } else {
                    listener.showMessageToast("You can't update more than average value")
                }
            }
        }
    }

    private fun updatePriceValues(
        binding: PendingOrderAdapterBinding,
        pendingOrder: List<PendingOrder.ITEMSItem>,
    ) {
        val pharmaDisc: Double = binding.pharmaBulkDisc.text.toString().toDouble()
        val fmcgDisc: Double = binding.fmcgBulkDisc.text.toString().toDouble()
        val plDisc: Double = binding.plBulkDisc.text.toString().toDouble()
        val surgicalDisc: Double = binding.surgicalBulkDisc.text.toString().toDouble()
        val otherDisc: Double = binding.otherBulkDisc.text.toString().toDouble()

        for (items in pendingOrder) {
            if (items.CATEGORY.equals("PHARMA")) {
                items.APPROVED_DISC = pharmaDisc
            } else if (items.CATEGORY.equals("FMCG")) {
                items.APPROVED_DISC = fmcgDisc
            } else if (items.CATEGORY.equals("PRIVATE LABEL")) {
                items.APPROVED_DISC = plDisc
            } else if (items.CATEGORY.equals("SURGICAL")) {
                items.APPROVED_DISC = surgicalDisc
            } else {
                items.APPROVED_DISC = otherDisc
            }
        }
        binding.recyclerView.post(Runnable { binding.recyclerView.adapter!!.notifyDataSetChanged() })
//        binding.recyclerView.adapter!!.notifyDataSetChanged()
    }

    interface ClickListener {
        fun orderAccepted(orderdetails: PendingOrder.PENDINGLISTItem, position: Int)
        fun orderReject(orderdetails: PendingOrder.PENDINGLISTItem)
        fun openDialogBoxTOEditDiscount(position: Int, pendingOrder: PendingOrder.ITEMSItem)
        fun showMessageToast(message: String)
        fun onItemChecked(orderdetails: PendingOrder.PENDINGLISTItem, position: Int)
    }

    private fun addBulkDiscount(
        binding: PendingOrderAdapterBinding,
        discount: Double,
        approvedDiscount: Double,
        pendingOrderListItem: PendingOrder.PENDINGLISTItem,
        itemBulkDisc: EditText,
    ) {
        if (discount >= approvedDiscount) {
            MDToast.makeText(
                ViswamApp.context,
                "You have provision for accepting $approvedDiscount Bulk disc%",
                MDToast.LENGTH_SHORT,
                MDToast.TYPE_WARNING
            ).show()
            return
        }
        if (discount == 99.00) return
        val resultDisc = discount + 1
        itemBulkDisc.setText(
            String.format(
                "%.2f",
                resultDisc
            )
        )
        updatePriceValues(binding, pendingOrderListItem.ITEMS)
    }

    fun subtractBulkDiscount(
        binding: PendingOrderAdapterBinding,
        discount: Double,
        pendingOrderListItem: PendingOrder.PENDINGLISTItem,
        itemBulkDisc: EditText,
    ) {
        if (discount >= 1.00) {
            val resultDisc = discount - 1.00
            itemBulkDisc.setText(
                String.format(
                    "%.2f",
                    resultDisc
                )
            )
            updatePriceValues(binding, pendingOrderListItem.ITEMS)
        }
    }
}

class OrderAdapter(
    val items: List<PendingOrder.ITEMSItem>,
    var pendingOrderMain: PendingOrder.PENDINGLISTItem,
    val isMarginRequired: Boolean,
    val itemBinding: PendingOrderAdapterBinding,
    val listener: SubItemClick,
) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val orderAdapterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.order_adapter,
            parent,
            false,
        ) as OrderAdapterBinding
        return ViewHolder(orderAdapterBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(private val binding: OrderAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pendingOrder: PendingOrder.ITEMSItem) {
            binding.bulkDisc.setText(String.format("%.2f", pendingOrder.APPROVED_DISC))
            binding.medicineNameOrg.text = pendingOrder.ITEMNAME
            binding.quantityText.text = pendingOrder.QTY.toString()
            binding.medicineName.text = pendingOrder.CATEGORY
            pendingOrder.INDENTNO = pendingOrderMain.INDENTNO //setting item id in subItems
            binding.priceText.text = String.format("%.2f", pendingOrder.PRICE)
            if (binding.bulkDisc.text.toString().toDouble() == 0.0) {
                binding.bulkDisc.isEnabled = false
            } else {
                binding.bulkDisc.isEnabled = true
            }
            if (isMarginRequired) {
                binding.marginLayout.visibility = View.VISIBLE
                binding.marginText.text = String.format("%.2f", pendingOrder.MARGIN) + "%"
            } else {
                binding.marginLayout.visibility = View.GONE
            }
            binding.add.setOnClickListener {
                Utils.printMessage("PendingOrderFrag",
                    "Apr Disc : " + pendingOrder.APPROVED_DISC + ", Original Disc : " + pendingOrder.ORIGINAL_DISC)
                binding.bulkDisc.setText(
                    String.format(
                        "%.2f",
                        addDiscount(
                            binding.bulkDisc.text.toString().toDouble(),
                            pendingOrder.ORIGINAL_DISC!!
                        )
                    )
                )
            }
            binding.subtract.setOnClickListener {
                binding.bulkDisc.setText(
                    String.format(
                        "%.2f",
                        subtractDiscount(binding.bulkDisc.text.toString().toDouble())
                    )
                )
            }
            binding.bulkDisc.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (pendingOrder.CATEGORY!!.equals(items.get(position).CATEGORY)) {
                        if (p0.toString().isNullOrEmpty() || p0.toString().equals("0")) {
//                        CommonUtils.printMessage(TAG,"onTextChangedPhase"+ p0.toString())
                            pendingOrder.APPROVED_DISC = 0.00
                            binding.bulkDisc.removeTextChangedListener(this)
                            binding.bulkDisc.setText(
                                String.format(
                                    "%.2f",
                                    pendingOrder.APPROVED_DISC
                                )
                            )
                            listener.changedAmountForTotal(items)
                            listener.updateAvgForItemLevel(items)
                            binding.bulkDisc.addTextChangedListener(this)
                        } else if (p0.toString().length > 5) {
                            if (Math.abs(p0.toString().toDouble()) >= 99.00) {
                                Utils.printMessage("onTextChanged", p0.toString())
//                                pendingOrder.APPROVED_DISC = pendingOrder.ORIGINAL_DISC
                                binding.bulkDisc.removeTextChangedListener(this)
                                binding.bulkDisc.setText(
                                    String.format(
                                        "%.2f",
                                        pendingOrder.APPROVED_DISC
                                    )
                                )
                                listener.changedAmountForTotal(items)
                                listener.updateAvgForItemLevel(items)
                                binding.bulkDisc.addTextChangedListener(this)
                            } else {
                                if (Math.abs(
                                        p0.toString().toDouble()
                                    ) >= pendingOrder.ORIGINAL_DISC!!
                                ) {
                                    binding.bulkDisc.removeTextChangedListener(this)
                                    binding.bulkDisc.setText(
                                        String.format(
                                            "%.2f",
                                            pendingOrder.ORIGINAL_DISC
                                        )
                                    )
                                    listener.changedAmountForTotal(items)
                                    listener.updateAvgForItemLevel(items)
                                    binding.bulkDisc.addTextChangedListener(this)
                                }
                            }
                        } else {
                            if (Math.abs(p0.toString().toDouble()) > pendingOrder.REQUEST_DISC!!) {
                                pendingOrder.APPROVED_DISC = pendingOrder.REQUEST_DISC
                                binding.bulkDisc.removeTextChangedListener(this)
                                binding.bulkDisc.setText(
                                    String.format(
                                        "%.2f",
                                        pendingOrder.REQUEST_DISC
                                    )
                                )
                                listener.changedAmountForTotal(items)
                                listener.updateAvgForItemLevel(items)
                                binding.bulkDisc.addTextChangedListener(this)
                            } else {
                                pendingOrder.APPROVED_DISC = p0.toString().toDoubleOrNull()
                                Utils.printMessage("PendingOrderFrag",
                                    "Approved Disc : " + pendingOrder.APPROVED_DISC + ", P0 value : " + p0.toString()
                                        .toDoubleOrNull())
                            }
                        }
                        listener.changedAmountForTotal(items)
                        listener.updateAvgForItemLevel(items)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
        }
    }

    interface SubItemClick {
        fun clickedSubItem(position: Int, pendingOrder: PendingOrder.ITEMSItem)
        fun showMessage(message: String)
        fun changedAmountForTotal(pendingOrder: List<PendingOrder.ITEMSItem>)
        fun updateAvgForItemLevel(pendingOrder: List<PendingOrder.ITEMSItem>)
    }

    fun addDiscount(discount: Double, approvedDiscount: Double): Double {
        if (discount >= approvedDiscount) {
            MDToast.makeText(
                ViswamApp.context,
                "You have provision for accepting $approvedDiscount Bulk disc%",
                MDToast.LENGTH_SHORT,
                MDToast.TYPE_WARNING
            ).show()
            return discount
        }
        if (discount == 99.00) return 99.00
        return discount + 1
    }

    fun subtractDiscount(discount: Double): Double {
        if (discount >= 1.00) {
            return discount - 1.00
        }
        return discount
    }
}

class RemarkAdapter(val remarkList: List<PendingOrder.REMARKSItem>) :
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
        fun bind(remarks: PendingOrder.REMARKSItem) {
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