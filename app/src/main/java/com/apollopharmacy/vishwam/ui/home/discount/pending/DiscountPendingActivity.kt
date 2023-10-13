package com.apollopharmacy.vishwam.ui.home.discount.pending

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.model.discount.AcceptOrRejectDiscountOrder
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.ActivityDiscountPendingBinding
import com.apollopharmacy.vishwam.util.CalculateDiscountAndTotalQuantity
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.ShowError
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import com.valdesekamdem.library.mdtoast.MDToast

class DiscountPendingActivity : AppCompatActivity(), DiscountActivityCallback {
    lateinit var activityDiscountPendingBinding: ActivityDiscountPendingBinding
    var itemsList = ArrayList<PendingOrder.PENDINGLISTItem>()
    lateinit var viewModel: DiscountActivityPendingViewModel
    var TYPE_REJECT = "REJECT"
    var TYPE_ACCEPT = "ACCEPT"
    var position: Int = 0
    var adapter: OrderAdapter? = null
    var isMarginRequired: Boolean = false
    private lateinit var remarksText: EditText
    private var acptRejcIndentNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDiscountPendingBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_discount_pending)
        setUp()

    }

    private fun setUp() {
        activityDiscountPendingBinding.callback = this
        viewModel = ViewModelProvider(this)[DiscountActivityPendingViewModel::class.java]

        if (intent != null) {
            itemsList =
                intent.getSerializableExtra("pendingList") as ArrayList<PendingOrder.PENDINGLISTItem>
            position = intent.getIntExtra("position", 0)
            isMarginRequired = intent.getBooleanExtra("isMarginRequired", false)
            activityDiscountPendingBinding.orderid.setText(itemsList.get(position).INDENTNO)
            activityDiscountPendingBinding.custmerName.setText(itemsList.get(position).CUSTNAME)
            activityDiscountPendingBinding.storeId.setText(itemsList.get(position).STORE)
            activityDiscountPendingBinding.postedDate.setText(itemsList.get(position).POSTEDDATE)
            activityDiscountPendingBinding.storeName.setText(itemsList.get(position).STORENAME)
            activityDiscountPendingBinding.dcName.setText(itemsList.get(position).DCNAME)
            activityDiscountPendingBinding.custmerNumber.setText(itemsList.get(position).TELNO)
            activityDiscountPendingBinding.recyclerViewPending.visibility = View.VISIBLE
            updateCatAvgValues(itemsList.get(position))
            activityDiscountPendingBinding.total.text = String.format(
                "%.2f",
                CalculateDiscountAndTotalQuantity.calculateTotalCost(itemsList[position].ITEMS)
            )
            activityDiscountPendingBinding.discount.text = String.format(
                "%.2f",
                CalculateDiscountAndTotalQuantity.CalculateTotalDiscount(itemsList[position].ITEMS)
            )
            activityDiscountPendingBinding.payment.text = String.format(
                "%.2f",
                CalculateDiscountAndTotalQuantity.CalculateNetPayment(itemsList[position].ITEMS)
            )
            activityDiscountPendingBinding.recyclerViewPending.adapter =
                OrderAdapter(items = itemsList[position].ITEMS,
                    itemsList.get(position),
                    isMarginRequired,
                    object : OrderAdapter.SubItemClick {
                        override fun clickedSubItem(
                            position: Int,
                            pendingOrder: PendingOrder.ITEMSItem,
                        ) {


                            showEditDiscountDialogBox( pendingOrder,position)
                        }

                        override fun showMessage(message: String) {
                            MDToast.makeText( ViswamApp.context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show()
                        }

                        override fun changedAmountForTotal(pendingOrder: List<PendingOrder.ITEMSItem>) {
                            activityDiscountPendingBinding.total.text = String.format(
                                "%.2f",
                                CalculateDiscountAndTotalQuantity.calculateTotalCost(pendingOrder)
                            )
                            activityDiscountPendingBinding.discount.text = String.format(
                                "%.2f",
                                CalculateDiscountAndTotalQuantity.CalculateTotalDiscount(pendingOrder)
                            )
                            activityDiscountPendingBinding.payment.text = String.format(
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

                            Utils.printMessage(
                                "PendingOrderFrag",
                                "Pharma : " + pharmaAvgDisc + ", FMCG : " + fmcgAvgDisc + ", PL : " + plAvgDisc + ", Surgical : " + surgAvgDisc + ", Other : " + otherAvgDisc
                            )
                            activityDiscountPendingBinding.pharmaBulkDisc.setText(
                                String.format(
                                    "%.2f",
                                    pharmaAvgDisc
                                )
                            )
                            activityDiscountPendingBinding.fmcgBulkDisc.setText(
                                String.format(
                                    "%.2f",
                                    fmcgAvgDisc
                                )
                            )
                            activityDiscountPendingBinding.plBulkDisc.setText(
                                String.format(
                                    "%.2f",
                                    plAvgDisc
                                )
                            )
                            activityDiscountPendingBinding.surgicalBulkDisc.setText(
                                String.format(
                                    "%.2f",
                                    surgAvgDisc
                                )
                            )
                            activityDiscountPendingBinding.otherBulkDisc.setText(
                                String.format(
                                    "%.2f",
                                    otherAvgDisc
                                )
                            )
                            if (activityDiscountPendingBinding.updateAvgLayout.visibility == View.VISIBLE) {
                                activityDiscountPendingBinding.updateAvgLayout.visibility = View.GONE
                            }
                        }
                    })

        }
        viewModel.acceptRequest.observe(this) {
            if (it.STATUS) {
                val intent = Intent()

                setResult(Activity.RESULT_OK, intent)
                finish()

            } else {
                ShowError.showToastMessage(it.MESSAGE, this)
            }
        }


    }

    private fun updateCatAvgValues(
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
                activityDiscountPendingBinding.pharmaAvgLayout.visibility = View.VISIBLE
                activityDiscountPendingBinding.pharmaBulkDisc.setText(
                    String.format(
                        "%.2f",
                        pharmaAvgDisc
                    )
                )
            } else {
                activityDiscountPendingBinding.pharmaAvgLayout.visibility = View.GONE
            }
            if (fmcgItems > 0) {
                activityDiscountPendingBinding.fmcgAvgLayout.visibility = View.VISIBLE
                activityDiscountPendingBinding.fmcgBulkDisc.setText(
                    String.format(
                        "%.2f",
                        fmcgAvgDisc
                    )
                )
            } else {
                activityDiscountPendingBinding.fmcgAvgLayout.visibility = View.GONE
            }
            if (plItems > 0) {
                activityDiscountPendingBinding.plAvgLayout.visibility = View.VISIBLE
                activityDiscountPendingBinding.plBulkDisc.setText(String.format("%.2f", plAvgDisc))
            } else {
                activityDiscountPendingBinding.plAvgLayout.visibility = View.GONE
            }
            if (surgItmes > 0) {
                activityDiscountPendingBinding.surgicalAvgLayout.visibility = View.VISIBLE
                activityDiscountPendingBinding.surgicalBulkDisc.setText(
                    String.format(
                        "%.2f",
                        surgAvgDisc
                    )
                )
            } else {
                activityDiscountPendingBinding.surgicalAvgLayout.visibility = View.GONE
            }
            if (otherItmes > 0) {
                activityDiscountPendingBinding.otherAvgLayout.visibility = View.VISIBLE
                activityDiscountPendingBinding.otherDiscLabel.setText(otherCatName)
                activityDiscountPendingBinding.otherBulkDisc.setText(
                    String.format(
                        "%.2f",
                        otherAvgDisc
                    )
                )
            } else {
                activityDiscountPendingBinding.otherAvgLayout.visibility = View.GONE
            }
//            activityDiscountPendingBinding.categoryAvgData.setText(categoryData)
        } else {
//            activityDiscountPendingBinding.avgLayout.visibility = View.GONE
        }
        var isItemUpdating = false
        activityDiscountPendingBinding.pharmaAdd.setOnClickListener {
            isItemUpdating = true
            activityDiscountPendingBinding.pharmaAdd.setTag(null);
            addBulkDiscount(
                activityDiscountPendingBinding.pharmaBulkDisc.text.toString().toDouble(),
                pharmaAvgDisc,
                pendingOrderListItem, activityDiscountPendingBinding.pharmaBulkDisc
            )
        }
        activityDiscountPendingBinding.pharmaSub.setOnClickListener {
            isItemUpdating = true
            activityDiscountPendingBinding.pharmaAdd.setTag(null);
            subtractBulkDiscount(
                activityDiscountPendingBinding.pharmaBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                activityDiscountPendingBinding.pharmaBulkDisc
            )
        }

        activityDiscountPendingBinding.pharmaBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    activityDiscountPendingBinding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(
                        pharmaAvgDisc,
                        activityDiscountPendingBinding.pharmaBulkDisc,
                        pendingOrderListItem.ITEMS
                    )
                }
            }
        })

        activityDiscountPendingBinding.fmcgAdd.setOnClickListener {
            isItemUpdating = true
            addBulkDiscount(
                activityDiscountPendingBinding.fmcgBulkDisc.text.toString().toDouble(),
                fmcgAvgDisc,
                pendingOrderListItem, activityDiscountPendingBinding.fmcgBulkDisc
            )
        }
        activityDiscountPendingBinding.fmcgSub.setOnClickListener {
            isItemUpdating = true
            subtractBulkDiscount(
                activityDiscountPendingBinding.fmcgBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                activityDiscountPendingBinding.fmcgBulkDisc
            )
        }
        activityDiscountPendingBinding.fmcgBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    activityDiscountPendingBinding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(
                        fmcgAvgDisc,
                        activityDiscountPendingBinding.fmcgBulkDisc,
                        pendingOrderListItem.ITEMS
                    )
                }
            }
        })
        activityDiscountPendingBinding.plAdd.setOnClickListener {
            isItemUpdating = true
            addBulkDiscount(
                activityDiscountPendingBinding.plBulkDisc.text.toString().toDouble(),
                plAvgDisc,
                pendingOrderListItem, activityDiscountPendingBinding.plBulkDisc
            )
        }
        activityDiscountPendingBinding.plSub.setOnClickListener {
            isItemUpdating = true
            subtractBulkDiscount(
                activityDiscountPendingBinding.plBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                activityDiscountPendingBinding.plBulkDisc
            )
        }
        activityDiscountPendingBinding.plBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    activityDiscountPendingBinding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(
                        plAvgDisc,
                        activityDiscountPendingBinding.plBulkDisc,
                        pendingOrderListItem.ITEMS
                    )
                }
            }
        })
        activityDiscountPendingBinding.surgicalAdd.setOnClickListener {
            isItemUpdating = true
            addBulkDiscount(
                activityDiscountPendingBinding.surgicalBulkDisc.text.toString().toDouble(),
                surgAvgDisc,
                pendingOrderListItem, activityDiscountPendingBinding.surgicalBulkDisc
            )
        }
        activityDiscountPendingBinding.surgicalSub.setOnClickListener {
            isItemUpdating = true
            subtractBulkDiscount(
                activityDiscountPendingBinding.surgicalBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                activityDiscountPendingBinding.surgicalBulkDisc
            )
        }
        activityDiscountPendingBinding.surgicalBulkDisc.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    activityDiscountPendingBinding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(
                        surgAvgDisc,
                        activityDiscountPendingBinding.surgicalBulkDisc,
                        pendingOrderListItem.ITEMS
                    )
                }
            }
        })
        activityDiscountPendingBinding.otherAdd.setOnClickListener {
            isItemUpdating = true
            addBulkDiscount(
                activityDiscountPendingBinding.otherBulkDisc.text.toString().toDouble(),
                otherAvgDisc,
                pendingOrderListItem, activityDiscountPendingBinding.otherBulkDisc
            )
        }
        activityDiscountPendingBinding.otherSub.setOnClickListener {
            isItemUpdating = true
            subtractBulkDiscount(
                activityDiscountPendingBinding.otherBulkDisc.text.toString().toDouble(),
                pendingOrderListItem,
                activityDiscountPendingBinding.otherBulkDisc
            )
        }
        activityDiscountPendingBinding.otherBulkDisc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isItemUpdating) {
                    isItemUpdating = false
                } else {
                    activityDiscountPendingBinding.updateAvgLayout.visibility = View.VISIBLE
                    updateAvgChangedVal(
                        otherAvgDisc,
                        activityDiscountPendingBinding.otherBulkDisc,
                        pendingOrderListItem.ITEMS
                    )
                }
            }
        })
    }

    private fun addBulkDiscount(

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
        updatePriceValues(pendingOrderListItem.ITEMS)
    }

    fun subtractBulkDiscount(
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
            updatePriceValues(pendingOrderListItem.ITEMS)
        }
    }

    private fun updateAvgChangedVal(
        avgDisc: Double,
        changeEditText: EditText,
        items: List<PendingOrder.ITEMSItem>,
    ) {
        activityDiscountPendingBinding.updateAvgBtn.setOnClickListener {
            if (changeEditText.text.toString().isNotEmpty()) {
                if (changeEditText.text.toString().toDouble() <= avgDisc) {
                    updatePriceValues(items)
                } else {
                    MDToast.makeText(
                        this,
                        "You can't update more than average value",
                        MDToast.LENGTH_SHORT,
                        MDToast.TYPE_ERROR
                    ).show()

                }
            }
        }
    }

    private fun updatePriceValues(
        pendingOrder: List<PendingOrder.ITEMSItem>,
    ) {
        val pharmaDisc: Double =
            activityDiscountPendingBinding.pharmaBulkDisc.text.toString().toDouble()
        val fmcgDisc: Double =
            activityDiscountPendingBinding.fmcgBulkDisc.text.toString().toDouble()
        val plDisc: Double = activityDiscountPendingBinding.plBulkDisc.text.toString().toDouble()
        val surgicalDisc: Double =
            activityDiscountPendingBinding.surgicalBulkDisc.text.toString().toDouble()
        val otherDisc: Double =
            activityDiscountPendingBinding.otherBulkDisc.text.toString().toDouble()

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
        activityDiscountPendingBinding.recyclerViewPending.post(Runnable { activityDiscountPendingBinding.recyclerViewPending.adapter!!.notifyDataSetChanged() })
//        binding.recyclerView.adapter!!.notifyDataSetChanged()
    }


    private fun sendRequestForAcceptAndReject(
        orderdetails: PendingOrder.PENDINGLISTItem,
        type: String,
    ) {
        if (orderdetails.REMARK!!.isNotEmpty()) {
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
                    ), orderdetails.REMARK, TYPE = type
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
        if (item.TYPE == Config.TYPE_ACCEPT) {
            type = "accepting"
        } else {
            type = "rejecting"
        }
        acptRejcIndentNo = item.ORDERS.INDENTNO
        AlertDialog
            .Builder(this)
            .setMessage("You are $type Request Discount ID ${item.ORDERS.INDENTNO} \nDo you want to proceed?")
            .setPositiveButton("YES") { _, _ ->
                if (NetworkUtil.isNetworkConnected(this)) {
                    showLoading(this)
                    viewModel.callAcceptOrder(item)
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.label_network_error),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            .setNegativeButton("NO") { d, _ -> d.dismiss() }
            .show()
    }

    override fun onClickBack() {
        finish()
    }

    override fun onClickRemarks() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_remarks)
        if (dialog.window != null) dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val okButton = dialog.findViewById<LinearLayout>(R.id.okButton)
        val closeIcon = dialog.findViewById<ImageView>(R.id.closeIcon)
        remarksText = dialog.findViewById<EditText>(R.id.selectRemarks)



        okButton.setOnClickListener {
            if (remarksText.text.toString().isNullOrEmpty()) {

            } else {
                activityDiscountPendingBinding.remarksText.setText(remarksText.text.toString())

            }
            dialog.dismiss()
        }
        closeIcon.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }
    public fun showEditDiscountDialogBox(pendingOrder: PendingOrder.ITEMSItem, position: Int) {
        val builder = AlertDialog.Builder(
            ViswamApp.context
        )
        builder.setTitle("Edit Discount")
        val viewInflated: View = LayoutInflater.from( ViswamApp.context)
            .inflate(R.layout.edit_discount, activityDiscountPendingBinding as ViewGroup?, false)
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

    override fun accept() {
        sendRequestForAcceptAndReject(itemsList.get(position), TYPE_ACCEPT)
    }

    override fun reject() {
        sendRequestForAcceptAndReject(itemsList.get(position), TYPE_REJECT)
    }

}

