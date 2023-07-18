package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityDashboardDetailsBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.DashboardCategoryAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.DashboardDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.HorizantalCategoryAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.HorizantalCategoryHeaderAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.model.ReasonWiseTicketCountByRoleResponse
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import java.util.ArrayList

class DashboardDetailsActivity : AppCompatActivity(), DashboardDetailsCallback {
    private lateinit var activityDashboardDetailsBinding: ActivityDashboardDetailsBinding
    private lateinit var viewModel: DashboardDetailsViewModel
    private lateinit var dashboardAdapter: DashboardDetailsAdapter
    private lateinit var dashboardCategoryAdapter: DashboardCategoryAdapter
    var horizantalCategoryHeaderAdapter: HorizantalCategoryHeaderAdapter? = null
    var reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountByRoleResponse? = null
    var categoryList = ArrayList<ReasonWiseTicketCountByRoleResponse.Data.ListData.ZcExtra.Data1>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDashboardDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_dashboard_details
        )
        viewModel = ViewModelProvider(this)[DashboardDetailsViewModel::class.java]
//        activityDashboardDetailsBinding.callback = this@RetroQrUploadActivity
        setUp()
    }

    private fun setUp() {
        Utlis.showLoading(this@DashboardDetailsActivity)
        viewModel.getReasonWiseTicketCountByRole(this@DashboardDetailsActivity,
            "2023-06-05",
            "2023-06-30",
            "EX100011")
        dashboardCategoryAdapter = DashboardCategoryAdapter(categoryList)
        val layoutManager = GridLayoutManager(this, 2)
        activityDashboardDetailsBinding.headerCategoryRecyclerview.setLayoutManager(layoutManager)
        activityDashboardDetailsBinding.headerCategoryRecyclerview.setAdapter(
            dashboardCategoryAdapter
        )

//        horizantalCategoryHeaderAdapter =
//            HorizantalCategoryHeaderAdapter()
//        activityDashboardDetailsBinding.childRecyclerViewHeader.layoutManager =
//            LinearLayoutManager(
//                context, LinearLayoutManager.HORIZONTAL,
//                false
//            )
//        activityDashboardDetailsBinding.childRecyclerViewHeader.adapter =
//            horizantalCategoryHeaderAdapter
//
//        activityDashboardDetailsBinding.childRecyclerViewHeader.adapter =
//            horizantalCategoryHeaderAdapter
    }

    override fun onSuccessGetReasonWiseTicketCountByRoleApiCall(reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountByRoleResponse) {
        Utlis.hideLoading()
        if (reasonWiseTicketCountByRoleResponse != null && reasonWiseTicketCountByRoleResponse.data != null && reasonWiseTicketCountByRoleResponse.data.listData != null && reasonWiseTicketCountByRoleResponse.data.listData.rows.size > 0) {
            this.reasonWiseTicketCountByRoleResponse = reasonWiseTicketCountByRoleResponse
            categoryList =
                reasonWiseTicketCountByRoleResponse.data.listData.zcExtra.data1 as ArrayList<ReasonWiseTicketCountByRoleResponse.Data.ListData.ZcExtra.Data1>

            dashboardAdapter = DashboardDetailsAdapter(this, reasonWiseTicketCountByRoleResponse)
            val layoutManager2 =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            activityDashboardDetailsBinding.dashboardDetailsRecyclerview.layoutManager =
                layoutManager2
            activityDashboardDetailsBinding.dashboardDetailsRecyclerview.adapter = dashboardAdapter
            // Header recyclerview
            horizantalCategoryHeaderAdapter = HorizantalCategoryHeaderAdapter(this, categoryList)
            activityDashboardDetailsBinding.headerRcv.adapter = horizantalCategoryHeaderAdapter
            activityDashboardDetailsBinding.headerRcv.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onFailureGetReasonWiseTicketCountByRoleApiCall(reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountByRoleResponse) {
        Utlis.hideLoading()
        Toast.makeText(this@DashboardDetailsActivity,
            reasonWiseTicketCountByRoleResponse.message,
            Toast.LENGTH_SHORT).show()
    }
}