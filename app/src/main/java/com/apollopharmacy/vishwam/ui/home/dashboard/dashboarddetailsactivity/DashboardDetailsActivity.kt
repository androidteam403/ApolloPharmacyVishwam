package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity

import android.os.Bundle
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

class DashboardDetailsActivity : AppCompatActivity() {
    private lateinit var activityDashboardDetailsBinding: ActivityDashboardDetailsBinding
    private lateinit var viewModel: DashboardDetailsViewModel
    private lateinit var dashboardAdapter: DashboardDetailsAdapter
    private lateinit var dashboardCategoryAdapter: DashboardCategoryAdapter
    var horizantalCategoryHeaderAdapter: HorizantalCategoryHeaderAdapter? = null
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

        dashboardCategoryAdapter = DashboardCategoryAdapter()
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

        dashboardAdapter = DashboardDetailsAdapter(this)
        var layoutManager2 =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        activityDashboardDetailsBinding.dashboardDetailsRecyclerview.layoutManager = layoutManager2
        activityDashboardDetailsBinding.dashboardDetailsRecyclerview.adapter = dashboardAdapter
    }
}