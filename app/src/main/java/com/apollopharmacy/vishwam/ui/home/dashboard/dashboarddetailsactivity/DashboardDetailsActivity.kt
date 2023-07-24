package com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.model.EmployeeDetailsResponse
import com.apollopharmacy.vishwam.databinding.ActivityDashboardDetailsBinding
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.DashboardCategoryAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.DashboardDetailsAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.HorizantalCategoryHeaderAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.ReasonWiseTicketCountbyRoleAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.adapter.ReasonWiseTicketCountbyRoleFixedAdapter
import com.apollopharmacy.vishwam.ui.home.dashboard.dashboarddetailsactivity.model.ReasonWiseTicketCountbyRoleResponse
import com.apollopharmacy.vishwam.ui.home.dashboard.model.TicketCountsByStatusRoleResponse
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import org.json.JSONException
import java.util.Collections


class DashboardDetailsActivity : AppCompatActivity(), DashboardDetailsCallback {
    private lateinit var activityDashboardDetailsBinding: ActivityDashboardDetailsBinding
    private lateinit var viewModel: DashboardDetailsViewModel
    private lateinit var dashboardAdapter: DashboardDetailsAdapter
    private lateinit var dashboardCategoryAdapter: DashboardCategoryAdapter
    var horizantalCategoryHeaderAdapter: HorizantalCategoryHeaderAdapter? = null
    var reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountbyRoleResponse? = null
    var categoryList = ArrayList<ReasonWiseTicketCountbyRoleResponse.Data1>()
    val categoryListFixed = ArrayList<ReasonWiseTicketCountbyRoleResponse.Data1>()

    var reasonWiseTicketCountbyRoleAdapter: ReasonWiseTicketCountbyRoleAdapter? = null
    var reasonWiseTicketCountbyRoleFixedAdapter: ReasonWiseTicketCountbyRoleFixedAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDashboardDetailsBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_dashboard_details
        )
        viewModel = ViewModelProvider(this)[DashboardDetailsViewModel::class.java]
//        activityDashboardDetailsBinding.callback = this@RetroQrUploadActivity
        setUp()
    }

    private fun setUp() {
        var row: TicketCountsByStatusRoleResponse.Data.ListData.Row? = null
        if (intent != null) {
            row =
                intent.getSerializableExtra("SELECTED_ITEM") as TicketCountsByStatusRoleResponse.Data.ListData.Row?
        }
        activityDashboardDetailsBinding.name.text = row!!.name
        activityDashboardDetailsBinding.callback = this@DashboardDetailsActivity
        empDetailsMobile()



        Utlis.showLoading(this@DashboardDetailsActivity)/*viewModel.getReasonWiseTicketCountByRole(
            this@DashboardDetailsActivity, "2023-06-05", "2023-06-30", "EX100011"
        )*/
        viewModel.getReasonWiseTicketCountByRole(
            this@DashboardDetailsActivity,
            Utils.getFirstDateOfCurrentMonth(),
            Utils.getCurrentDateCeoDashboard(),
            Preferences.getValidatedEmpId()
        )

        /* dashboardCategoryAdapter = DashboardCategoryAdapter(categoryList)
             val layoutManager = GridLayoutManager(this, 2)
             activityDashboardDetailsBinding.headerCategoryRecyclerview.setLayoutManager(layoutManager)
             activityDashboardDetailsBinding.headerCategoryRecyclerview.setAdapter(
                 dashboardCategoryAdapter
             )*/

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

    var role: String = ""
    private fun empDetailsMobile() {
        var empDetailsResponse = Preferences.getEmployeeDetailsResponseJson()
        var employeeDetailsResponse: EmployeeDetailsResponse? = null
        try {
            val gson = GsonBuilder().setPrettyPrinting().create()
            employeeDetailsResponse = gson.fromJson<EmployeeDetailsResponse>(
                empDetailsResponse, EmployeeDetailsResponse::class.java
            )

        } catch (e: JsonParseException) {
            e.printStackTrace()
        }
        if (employeeDetailsResponse != null && employeeDetailsResponse!!.data != null && employeeDetailsResponse!!.data!!.role != null && employeeDetailsResponse!!.data!!.role!!.code != null) {
            role = employeeDetailsResponse!!.data!!.role!!.code!!
            if (employeeDetailsResponse!!.data!!.role!!.code!!.equals("ceo")) {
                activityDashboardDetailsBinding.empRole.text = "CEO Dashboard"
                activityDashboardDetailsBinding.selectedEmpRole.text = "Regional Head"
            } else if (employeeDetailsResponse!!.data!!.role!!.code!!.equals("regional_head")) {
                activityDashboardDetailsBinding.empRole.text = "Regional Head Dashboard"
                activityDashboardDetailsBinding.selectedEmpRole.text = "Manager"
            } else if (employeeDetailsResponse!!.data!!.role!!.code!!.equals("store_manager")) {
                activityDashboardDetailsBinding.empRole.text = "Manager Dashboard"
                activityDashboardDetailsBinding.selectedEmpRole.text = "Executive"
            } else {
                activityDashboardDetailsBinding.empRole.text = "-"
                activityDashboardDetailsBinding.selectedEmpRole.text = "-"

            }
        }
    }

    override fun onSuccessGetReasonWiseTicketCountByRoleApiCall(reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountbyRoleResponse) {
        Utlis.hideLoading()
        if (reasonWiseTicketCountByRoleResponse != null && reasonWiseTicketCountByRoleResponse.data != null && reasonWiseTicketCountByRoleResponse.data!!.listData != null && reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!.size > 0) {

            var data1Temp = ReasonWiseTicketCountbyRoleResponse.Data1()
            data1Temp.name = "Name"
            data1Temp.code = "name"
            categoryListFixed.add(data1Temp)/* reasonWiseTicketCountByRoleResponse.data!!.listData!!.zcExtra!!.data1!!.add(
                 0, data1Temp
             )*/

            var data1Temp2 = ReasonWiseTicketCountbyRoleResponse.Data1()
            data1Temp2.name = "Total"
            data1Temp2.code = "total"
            categoryListFixed.add(data1Temp2)/*reasonWiseTicketCountByRoleResponse.data!!.listData!!.zcExtra!!.data1!!.add(
                1, data1Temp2
            )*/


            this.reasonWiseTicketCountByRoleResponse = reasonWiseTicketCountByRoleResponse
            categoryList =
                reasonWiseTicketCountByRoleResponse.data!!.listData!!.zcExtra!!.data1 as ArrayList<ReasonWiseTicketCountbyRoleResponse.Data1>


            // Grid category list
            dashboardCategoryAdapter = DashboardCategoryAdapter(
                this@DashboardDetailsActivity, categoryList!!, this@DashboardDetailsActivity,

                reasonWiseTicketCountByRoleResponse.data!!.listData!!.rows!!, role
            )
            val layoutManager = GridLayoutManager(this, 2)
            activityDashboardDetailsBinding.headerCategoryRecyclerview.setLayoutManager(
                layoutManager
            )
            activityDashboardDetailsBinding.headerCategoryRecyclerview.setAdapter(
                dashboardCategoryAdapter
            )


            // Reason wise ticket count count by role fixed list adapter
            reasonWiseTicketCountbyRoleFixedAdapter = ReasonWiseTicketCountbyRoleFixedAdapter(
                this@DashboardDetailsActivity,
                categoryListFixed!!,
                this.reasonWiseTicketCountByRoleResponse!!,
                this@DashboardDetailsActivity,
                role
            )
            val layoutManagerReasonWiseTicketCountbyRoleFixed =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

            activityDashboardDetailsBinding.reasonWiseTicketCountByRoleRecyclerviewFixed.layoutManager =
                layoutManagerReasonWiseTicketCountbyRoleFixed
            activityDashboardDetailsBinding.reasonWiseTicketCountByRoleRecyclerviewFixed.adapter =
                reasonWiseTicketCountbyRoleFixedAdapter


            // Reason wise ticket count count by role list adapter
            reasonWiseTicketCountbyRoleAdapter = ReasonWiseTicketCountbyRoleAdapter(
                this@DashboardDetailsActivity,
                categoryList!!,
                this.reasonWiseTicketCountByRoleResponse!!,
                this@DashboardDetailsActivity,
                role
            )
            val layoutManagerReasonWiseTicketCountbyRole =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

            activityDashboardDetailsBinding.reasonWiseTicketCountByRoleRecyclerview.layoutManager =
                layoutManagerReasonWiseTicketCountbyRole
            activityDashboardDetailsBinding.reasonWiseTicketCountByRoleRecyclerview.adapter =
                reasonWiseTicketCountbyRoleAdapter


            /*  dashboardAdapter = DashboardDetailsAdapter(this, reasonWiseTicketCountByRoleResponse)
              val layoutManager2 =
                  LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
              activityDashboardDetailsBinding.dashboardDetailsRecyclerview.layoutManager =
                  layoutManager2
              activityDashboardDetailsBinding.dashboardDetailsRecyclerview.adapter = dashboardAdapter
              // Header recyclerview
              horizantalCategoryHeaderAdapter = HorizantalCategoryHeaderAdapter(this, categoryList)
              activityDashboardDetailsBinding.headerRcv.adapter = horizantalCategoryHeaderAdapter
              activityDashboardDetailsBinding.headerRcv.layoutManager =
                  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)*/
        }
    }

    override fun onFailureGetReasonWiseTicketCountByRoleApiCall(reasonWiseTicketCountByRoleResponse: ReasonWiseTicketCountbyRoleResponse) {
        Utlis.hideLoading()
        Toast.makeText(
            this@DashboardDetailsActivity,
            reasonWiseTicketCountByRoleResponse.message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onClickCategoryItem(
        data1: ReasonWiseTicketCountbyRoleResponse.Data1,
        pos: Int,
        isCameFromFixed: Boolean,
    ) {
        if (isCameFromFixed) {
            for (i in categoryListFixed) {
                if (i.equals(data1)) {
                    if (i.isSelsected == true) {
                        if (i.isDescending == true) {
                            i.isDescending = false


                            Collections.sort<JsonObject>(reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!,
                                object : Comparator<JsonObject?> {

                                    override fun compare(o1: JsonObject?, o2: JsonObject?): Int {
                                        var valA = String()
                                        var valB = String()
                                        try {
                                            if (data1.name.equals("Name")) {
                                                valA = o1!![data1.name!!.toLowerCase()].asString
                                                valB = o2!![data1.name!!.toLowerCase()].asString
                                            } else {
                                                valA = o1!![data1.name].asString
                                                valB = o2!![data1.name].asString
                                            }
                                        } catch (e: JSONException) {
                                            Log.e(
                                                "TEST",
                                                "JSONException in combineJSONArrays sort section",
                                                e
                                            )
                                        }
                                        if (data1.name.equals("Name")) {
                                            return valA.compareTo(valB)
                                        } else {
                                            return valA.toInt().compareTo(valB.toInt())
                                        }
                                    }

                                })
                        } else {
                            i.isDescending = true
                            Collections.sort<JsonObject>(reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!,
                                object : Comparator<JsonObject?> {

                                    override fun compare(o1: JsonObject?, o2: JsonObject?): Int {
                                        var valA = String()
                                        var valB = String()
                                        try {
                                            if (data1.name.equals("Name")) {
                                                valA = o1!![data1.name!!.toLowerCase()].asString
                                                valB = o2!![data1.name!!.toLowerCase()].asString
                                            } else {
                                                valA = o1!![data1.name].asString
                                                valB = o2!![data1.name].asString
                                            }

                                        } catch (e: JSONException) {
                                            Log.e(
                                                "TEST",
                                                "JSONException in combineJSONArrays sort section",
                                                e
                                            )
                                        }
                                        if (data1.name.equals("Name")) {
                                            return -valA.compareTo(valB)
                                        } else {
                                            return -valA.toInt().compareTo(valB.toInt())
                                        }
                                    }

                                })
                        }
                    } else {
                        i.isSelsected = true
                        if (i.isDescending == true) {
                            Collections.sort<JsonObject>(reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!,
                                object : Comparator<JsonObject?> {

                                    override fun compare(o1: JsonObject?, o2: JsonObject?): Int {
                                        var valA = String()
                                        var valB = String()
                                        try {
                                            if (data1.name.equals("Name")) {
                                                valA = o1!![data1.name!!.toLowerCase()].asString
                                                valB = o2!![data1.name!!.toLowerCase()].asString
                                            } else {
                                                valA = o1!![data1.name].asString
                                                valB = o2!![data1.name].asString
                                            }

                                        } catch (e: JSONException) {
                                            Log.e(
                                                "TEST",
                                                "JSONException in combineJSONArrays sort section",
                                                e
                                            )
                                        }
                                        if (data1.name.equals("Name")) {
                                            return -valA.compareTo(valB)
                                        } else {
                                            return -valA.toInt().compareTo(valB.toInt())
                                        }
                                    }

                                })
                        } else {
                            Collections.sort<JsonObject>(reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!,
                                object : Comparator<JsonObject?> {

                                    override fun compare(o1: JsonObject?, o2: JsonObject?): Int {
                                        var valA = String()
                                        var valB = String()
                                        try {
                                            if (data1.name.equals("Name")) {
                                                valA = o1!![data1.name!!.toLowerCase()].asString
                                                valB = o2!![data1.name!!.toLowerCase()].asString
                                            } else {
                                                valA = o1!![data1.name].asString
                                                valB = o2!![data1.name].asString
                                            }
                                        } catch (e: JSONException) {
                                            Log.e(
                                                "TEST",
                                                "JSONException in combineJSONArrays sort section",
                                                e
                                            )
                                        }
                                        if (data1.name.equals("Name")) {
                                            return valA.compareTo(valB)
                                        } else {
                                            return valA.toInt().compareTo(valB.toInt())
                                        }
                                    }

                                })
                        }
                    }
                } else {
                    i.isSelsected = false
                    i.isDescending = false
                }
            }


            for (i in categoryList) {
                i.isSelsected = false
                i.isDescending = false
            }

            reasonWiseTicketCountbyRoleFixedAdapter!!.notifyDataSetChanged()
            dashboardCategoryAdapter.notifyDataSetChanged()
            reasonWiseTicketCountbyRoleAdapter!!.notifyDataSetChanged()
            activityDashboardDetailsBinding.reasonWiseTicketCountByRoleRecyclerview.scrollToPosition(
                0
            )

        } else {
            for (i in categoryList) {
                if (i.equals(data1)) {
                    if (i.isSelsected == true) {
                        if (i.isDescending == true) {
                            i.isDescending = false


                            Collections.sort<JsonObject>(reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!,
                                object : Comparator<JsonObject?> {

                                    override fun compare(o1: JsonObject?, o2: JsonObject?): Int {
                                        var valA = String()
                                        var valB = String()
                                        try {
                                            if (data1.name.equals("Name")) {
                                                valA = o1!![data1.name!!.toLowerCase()].asString
                                                valB = o2!![data1.name!!.toLowerCase()].asString
                                            } else {
                                                valA = o1!![data1.name].asString
                                                valB = o2!![data1.name].asString
                                            }
                                        } catch (e: JSONException) {
                                            Log.e(
                                                "TEST",
                                                "JSONException in combineJSONArrays sort section",
                                                e
                                            )
                                        }
                                        if (data1.name.equals("Name")) {
                                            return valA.compareTo(valB)
                                        } else {
                                            return valA.toInt().compareTo(valB.toInt())
                                        }
                                    }

                                })
                        } else {
                            i.isDescending = true
                            Collections.sort<JsonObject>(reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!,
                                object : Comparator<JsonObject?> {

                                    override fun compare(o1: JsonObject?, o2: JsonObject?): Int {
                                        var valA = String()
                                        var valB = String()
                                        try {
                                            if (data1.name.equals("Name")) {
                                                valA = o1!![data1.name!!.toLowerCase()].asString
                                                valB = o2!![data1.name!!.toLowerCase()].asString
                                            } else {
                                                valA = o1!![data1.name].asString
                                                valB = o2!![data1.name].asString
                                            }

                                        } catch (e: JSONException) {
                                            Log.e(
                                                "TEST",
                                                "JSONException in combineJSONArrays sort section",
                                                e
                                            )
                                        }
                                        if (data1.name.equals("Name")) {
                                            return -valA.compareTo(valB)
                                        } else {
                                            return -valA.toInt().compareTo(valB.toInt())
                                        }
                                    }

                                })
                        }
                    } else {
                        i.isSelsected = true
                        if (i.isDescending == true) {
                            Collections.sort<JsonObject>(reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!,
                                object : Comparator<JsonObject?> {

                                    override fun compare(o1: JsonObject?, o2: JsonObject?): Int {
                                        var valA = String()
                                        var valB = String()
                                        try {
                                            if (data1.name.equals("Name")) {
                                                valA = o1!![data1.name!!.toLowerCase()].asString
                                                valB = o2!![data1.name!!.toLowerCase()].asString
                                            } else {
                                                valA = o1!![data1.name].asString
                                                valB = o2!![data1.name].asString
                                            }

                                        } catch (e: JSONException) {
                                            Log.e(
                                                "TEST",
                                                "JSONException in combineJSONArrays sort section",
                                                e
                                            )
                                        }
                                        if (data1.name.equals("Name")) {
                                            return -valA.compareTo(valB)
                                        } else {
                                            return -valA.toInt().compareTo(valB.toInt())
                                        }
                                    }

                                })
                        } else {
                            Collections.sort<JsonObject>(reasonWiseTicketCountByRoleResponse!!.data!!.listData!!.rows!!,
                                object : Comparator<JsonObject?> {

                                    override fun compare(o1: JsonObject?, o2: JsonObject?): Int {
                                        var valA = String()
                                        var valB = String()
                                        try {
                                            if (data1.name.equals("Name")) {
                                                valA = o1!![data1.name!!.toLowerCase()].asString
                                                valB = o2!![data1.name!!.toLowerCase()].asString
                                            } else {
                                                valA = o1!![data1.name].asString
                                                valB = o2!![data1.name].asString
                                            }
                                        } catch (e: JSONException) {
                                            Log.e(
                                                "TEST",
                                                "JSONException in combineJSONArrays sort section",
                                                e
                                            )
                                        }
                                        if (data1.name.equals("Name")) {
                                            return valA.compareTo(valB)
                                        } else {
                                            return valA.toInt().compareTo(valB.toInt())
                                        }
                                    }

                                })
                        }
                    }
                } else {
                    i.isSelsected = false
                    i.isDescending = false
                }
            }

            for (i in categoryListFixed) {
                i.isSelsected = false
                i.isDescending = false
            }

            reasonWiseTicketCountbyRoleFixedAdapter!!.notifyDataSetChanged()
            dashboardCategoryAdapter.notifyDataSetChanged()
            reasonWiseTicketCountbyRoleAdapter!!.notifyDataSetChanged()
            activityDashboardDetailsBinding.reasonWiseTicketCountByRoleRecyclerview.scrollToPosition(
                pos
            )
        }


    }

    override fun onClickBack() {
        onBackPressed()
    }

}

