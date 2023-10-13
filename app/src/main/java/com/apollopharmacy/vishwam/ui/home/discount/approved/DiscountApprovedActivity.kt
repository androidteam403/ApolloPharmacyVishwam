package com.apollopharmacy.vishwam.ui.home.discount.approved

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.PendingOrder
import com.apollopharmacy.vishwam.databinding.ActivityDiscountApprovedBinding
import com.apollopharmacy.vishwam.util.CalculateDiscountAndTotalQuantity

class DiscountApprovedActivity : AppCompatActivity() {
    lateinit var activityDiscountApprovedBinding: ActivityDiscountApprovedBinding
    var itemsList = ArrayList<ApprovalOrderRequest.APPROVEDLISTItem>()

    var position: Int = 0
    var adapter: OrderAdapter? = null
    var isMarginRequired: Boolean = false
    private lateinit var remarksText: EditText
    private var acptRejcIndentNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDiscountApprovedBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_discount_approved)
        setUp()

    }

    private fun setUp() {

        if (intent != null) {

            itemsList = intent.getSerializableExtra("approveList") as ArrayList<ApprovalOrderRequest.APPROVEDLISTItem>
            position = intent.getIntExtra("position", 0)
            isMarginRequired = intent.getBooleanExtra("isMarginRequired", false)
            activityDiscountApprovedBinding.orderid.setText(itemsList.get(position).INDENTNO)
            activityDiscountApprovedBinding.custmerName.setText(itemsList.get(position).CUSTNAME)
            activityDiscountApprovedBinding.storeId.setText(itemsList.get(position).STORE)
            activityDiscountApprovedBinding.postedDate.setText(itemsList.get(position).POSTEDDATE)
            activityDiscountApprovedBinding.storeName.setText(itemsList.get(position).STORENAME)
            activityDiscountApprovedBinding.dcName.setText(itemsList.get(position).DCNAME)
            activityDiscountApprovedBinding.custmerNumber.setText(itemsList.get(position).TELNO)
            activityDiscountApprovedBinding.recyclerViewApproved.visibility = View.VISIBLE


            if (itemsList[position].STATUSLIST.isNullOrEmpty())
                activityDiscountApprovedBinding.statusRecyleview.visibility = View.GONE
            else
                activityDiscountApprovedBinding.statusRecyleview.visibility = View.VISIBLE
            activityDiscountApprovedBinding.statusRecyleview.adapter = StatusAdapter(itemsList[position].STATUSLIST)

            activityDiscountApprovedBinding.total.text = String.format(
                "%.2f",
                CalculateDiscountAndTotalQuantity.calculateTotalCostForApproval(itemsList[position].ITEMS)
            )
            activityDiscountApprovedBinding.discount.text = String.format(
                "%.2f",
                CalculateDiscountAndTotalQuantity.CalculateTotalDiscountForApproval(itemsList[position].ITEMS)
            )
            activityDiscountApprovedBinding.payment.text = String.format(
                "%.2f",
                CalculateDiscountAndTotalQuantity.CalculateNetPaymentForApproval(itemsList[position].ITEMS)
            )

            activityDiscountApprovedBinding.recyclerViewApproved.adapter =
                OrderAdapter(items = itemsList[position].ITEMS, isMarginRequired)
        }

        activityDiscountApprovedBinding.back.setOnClickListener {
            finish()
        }


    }
}

