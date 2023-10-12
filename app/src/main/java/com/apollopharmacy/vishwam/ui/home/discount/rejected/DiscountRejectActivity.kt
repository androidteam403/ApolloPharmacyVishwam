package com.apollopharmacy.vishwam.ui.home.discount.rejected

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.discount.ApprovalOrderRequest
import com.apollopharmacy.vishwam.data.model.discount.RejectedOrderResponse
import com.apollopharmacy.vishwam.databinding.ActivityDiscountRejectBinding
import com.apollopharmacy.vishwam.util.CalculateDiscountAndTotalQuantity

class DiscountRejectActivity : AppCompatActivity() {
    lateinit var activityDiscountApprovedBinding: ActivityDiscountRejectBinding
    var itemsList = ArrayList<RejectedOrderResponse.REJECTEDLISTItem>()

    var position: Int = 0
    var adapter: RejectRecyclerView.OrderAdapter? = null
    var isMarginRequired: Boolean = false
    private lateinit var remarksText: EditText
    private var acptRejcIndentNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDiscountApprovedBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_discount_reject)
        setUp()

    }

    private fun setUp() {

        if (intent != null) {

            itemsList = intent.getSerializableExtra("rejectList") as ArrayList<RejectedOrderResponse.REJECTEDLISTItem>
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
                CalculateDiscountAndTotalQuantity.CalculateNetPaymentForRejected(itemsList[position].ITEMS)
            )
            activityDiscountApprovedBinding.discount.text = String.format(
                "%.2f",
                CalculateDiscountAndTotalQuantity.CalculateNetPaymentForRejected(itemsList[position].ITEMS)
            )
            activityDiscountApprovedBinding.payment.text = String.format(
                "%.2f",
                CalculateDiscountAndTotalQuantity.CalculateNetPaymentForRejected(itemsList[position].ITEMS)
            )

            activityDiscountApprovedBinding.recyclerViewApproved.adapter =
                RejectRecyclerView.OrderAdapter(items = itemsList[position].ITEMS, isMarginRequired)
        }


        activityDiscountApprovedBinding.back.setOnClickListener {
            finish()
        }


    }
}

