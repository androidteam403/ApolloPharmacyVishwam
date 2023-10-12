package com.apollopharmacy.vishwam.ui.home.discount.bill

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.discount.BILLDEATILSItem
import com.apollopharmacy.vishwam.databinding.ActivityDiscountBillBinding
import com.apollopharmacy.vishwam.util.Utlis

class DiscountBillingActivity : AppCompatActivity() {
    lateinit var activityDiscountApprovedBinding: ActivityDiscountBillBinding
    var itemsList = ArrayList<BILLDEATILSItem>()

    var position: Int = 0
    var adapter: BillRecyclerView.OrderAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDiscountApprovedBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_discount_bill)
        setUp()

    }

    private fun setUp() {

        if (intent != null) {

            itemsList = intent.getSerializableExtra("billList") as ArrayList<BILLDEATILSItem>
            position = intent.getIntExtra("position", 0)
            activityDiscountApprovedBinding.indentNo.setText(itemsList.get(position).iNDENTNO)
            activityDiscountApprovedBinding.storeId.setText(itemsList.get(position).sTOREID)
            activityDiscountApprovedBinding.approvedDate.setText(Utlis.convertDateAddedTimeZone(itemsList.get(position).aPPROVEDDATE))
            activityDiscountApprovedBinding.approvedValue.setText(itemsList.get(position).aPPROVEDVALUE.toString())

            activityDiscountApprovedBinding.billDate.setText(Utlis.convertDateAddedTimeZone(itemsList.get(position).bILLEDDATE))
            activityDiscountApprovedBinding.billedValue.setText(itemsList.get(position).fINALBILLEDVALUE.toString())
            activityDiscountApprovedBinding.recyclerViewApproved.visibility = View.VISIBLE





            activityDiscountApprovedBinding.recyclerViewApproved.adapter =
                BillRecyclerView.OrderAdapter(items = itemsList[position].iTEMS,false)
        }


        activityDiscountApprovedBinding.back.setOnClickListener {
            finish()
        }


    }
}

