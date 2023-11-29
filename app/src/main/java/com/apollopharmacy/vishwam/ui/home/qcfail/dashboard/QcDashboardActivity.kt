package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityQcDashboardBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter.RtoSitesAdapter
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.util.Utlis


class QcDashboardActivity : AppCompatActivity() {
    lateinit var activityQcDashboardBinding: ActivityQcDashboardBinding
    lateinit var viewModel: DashBoardViewModel
    var empId: String = ""
    var designation: String = ""
    var isExpand: Boolean = false
    var rtoSitesAdapter: RtoSitesAdapter? = null
    var dashBoardList = ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>()
    var dashBoardHistoryList = ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityQcDashboardBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_qc_dashboard)
        setUp()

    }

    private fun setUp() {
        viewModel = ViewModelProvider(this)[DashBoardViewModel::class.java]

        activityQcDashboardBinding.back.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)

            finish()
        }
        activityQcDashboardBinding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.length >= 2) {
                    if (rtoSitesAdapter != null) {
                        rtoSitesAdapter!!.getFilter()!!.filter(editable)
                    }
                } else if (activityQcDashboardBinding.searchView.getText().toString()
                        .equals("")
                ) {
                    if (rtoSitesAdapter != null) {
                        rtoSitesAdapter!!.getFilter()!!.filter("")
                    }
                } else {
                    if (rtoSitesAdapter != null) {
                        rtoSitesAdapter!!.getFilter()!!.filter("")
                    }
                }
            }
        })


        if (intent != null) {
            empId = intent.getStringExtra("empId").toString()
            designation = intent.getStringExtra("designation").toString()
            dashBoardHistoryList= intent.getSerializableExtra("dashboardList") as ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>
            Utlis.showLoading(this)
            if (dashBoardList.isNullOrEmpty()){
                viewModel.getQcPendingHierarchyHistoryList(empId.split("-").get(0), designation)

            }
            for (i in dashBoardHistoryList!!.indices) {
                activityQcDashboardBinding.designation.setText(dashBoardHistoryList.get(i).designation)
                activityQcDashboardBinding.rtCount.setText(dashBoardHistoryList.get(i).rtocount.toString())
                activityQcDashboardBinding.rtovalue.setText(dashBoardHistoryList.get(i).rtoamount.toString())
                activityQcDashboardBinding.rrtovalue.setText(dashBoardHistoryList.get(i).rrtoamount.toString())
                activityQcDashboardBinding.rrtoCount.setText(dashBoardHistoryList.get(i).rrtocount.toString())
                activityQcDashboardBinding.empId.setText(empId.split("-").get(0))

            }
//            viewModel.getQcPendingDashboardHistoryList(empId.split("-").get(0), designation)
            activityQcDashboardBinding.generalmanagerArrow.setOnClickListener {

                if (dashBoardList.isNullOrEmpty()){
                    Utlis.showLoading(this)

                    viewModel.getQcPendingHierarchyHistoryList(empId.split("-").get(0), designation)

                }else{
                    activityQcDashboardBinding.rtoHeader.visibility=View.VISIBLE
                    activityQcDashboardBinding.closeArrow.visibility=View.VISIBLE
                    activityQcDashboardBinding.generalmanagerArrow.visibility=View.GONE
                    activityQcDashboardBinding.gmsitesRecyclerView.visibility=View.VISIBLE

                    rtoSitesAdapter = RtoSitesAdapter(
                        this,dashBoardList

                    )

                    activityQcDashboardBinding.gmsitesRecyclerView.adapter = rtoSitesAdapter
                }

            }

        }
        viewModel.qcPendingHierarchyHistoryList.observeForever {
            Utlis.hideLoading()

            if (it.status == true) {


                dashBoardList=it.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                activityQcDashboardBinding.notificationText.setText(dashBoardList.size.toString())

                for (i in it.pendingcount!!.indices) {
                    activityQcDashboardBinding.gmEmpname.setText(it.pendingcount!!.get(i).empid!!.split("-").get(1))

                }
                if (dashBoardList.isNullOrEmpty()){

                }else{
                    rtoSitesAdapter = RtoSitesAdapter(
                        this,dashBoardList

                    )

                    activityQcDashboardBinding.gmsitesRecyclerView.adapter = rtoSitesAdapter
                }

            }
        }
        activityQcDashboardBinding.closeArrow.setOnClickListener {
            activityQcDashboardBinding.gmsitesRecyclerView.visibility = View.GONE
            activityQcDashboardBinding.closeArrow.visibility = View.GONE
            activityQcDashboardBinding.rtoHeader.visibility=View.GONE
            activityQcDashboardBinding.generalmanagerArrow.visibility = View.VISIBLE

        }


    }

}

