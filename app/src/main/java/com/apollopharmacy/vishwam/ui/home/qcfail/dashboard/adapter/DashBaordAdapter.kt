package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DashboardSiteBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse
import java.util.stream.Collectors

class DashBaordAdapter(
    val mContext: Context,
    var pendingCountResponseList: ArrayList<PendingCountResponse.Pendingcount>,
    var designations: ArrayList<String>,
) :
    RecyclerView.Adapter<DashBaordAdapter.ViewHolder>() {
    var gmPendingCountResponseList =
        java.util.ArrayList<PendingCountResponse.Pendingcount>()
    var managerPendingCountResponseList =
        java.util.ArrayList<PendingCountResponse.Pendingcount>()
    var executivePendingCountResponseList =
        java.util.ArrayList<PendingCountResponse.Pendingcount>()


    var siteIdsGroupedList = (mutableMapOf<String, List<PendingCountResponse.Pendingcount>>())
    var generalManager: Boolean = false
    var maanager: Boolean = false
    var executive: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val dashboardSiteBinding: DashboardSiteBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.dashboard_site,
                parent,
                false
            )
        return ViewHolder(dashboardSiteBinding)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = designations.get(position)

        gmPendingCountResponseList =
            pendingCountResponseList.stream()
                .filter { pendingCountResponseList: PendingCountResponse.Pendingcount ->
                    pendingCountResponseList.designation?.replace(
                        " ",
                        ""
                    ).equals("GENERALMANAGER", true)
                }
                .collect(Collectors.toList()) as java.util.ArrayList<PendingCountResponse.Pendingcount>

        managerPendingCountResponseList =
            pendingCountResponseList.stream()
                .filter { pendingCountResponseList: PendingCountResponse.Pendingcount ->
                    pendingCountResponseList.designation?.replace(
                        " ",
                        ""
                    ).equals("MANAGER", true)
                }
                .collect(Collectors.toList()) as java.util.ArrayList<PendingCountResponse.Pendingcount>

        executivePendingCountResponseList =
            pendingCountResponseList.stream()
                .filter { pendingCountResponseList: PendingCountResponse.Pendingcount ->
                    pendingCountResponseList.designation?.replace(
                        " ",
                        ""
                    ).equals("EXECUTIVE", true)
                }
                .collect(Collectors.toList()) as java.util.ArrayList<PendingCountResponse.Pendingcount>

        val gmPendingCountSum: List<Int> = gmPendingCountResponseList.stream()
            .map<Int>(PendingCountResponse.Pendingcount::pendingcount)
            .collect(Collectors.toList())

        val managerPendingCountSum: List<Int> = managerPendingCountResponseList.stream()
            .map<Int>(PendingCountResponse.Pendingcount::pendingcount)
            .collect(Collectors.toList())
        val executivePendingCountSum: List<Int> = executivePendingCountResponseList.stream()
            .map<Int>(PendingCountResponse.Pendingcount::pendingcount)
            .collect(Collectors.toList())





        if (items.replace(" ", "").equals("GENERALMANAGER", true)) {


            if (items.replace(" ", "").equals("GENERALMANAGER", true)) {
                holder.dashboardSiteBinding.noOrderFoundText.visibility = View.GONE
                holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE

                if (Preferences.getAppLevelDesignationQCFail()
                        .replace(
                            " ",
                            ""
                        )
                        .equals("GENERALMANAGER", true)
                ) {
                    val userData = LoginRepo.getProfile()
                    if (userData != null) {
                        holder.dashboardSiteBinding.gmEmpname.setText(userData.EMPNAME + "\n" + Preferences.getAppLevelDesignationQCFail())
                        holder.dashboardSiteBinding.pendingCountSum.setText(gmPendingCountSum.stream()
                            .mapToInt({ obj: Int -> obj }).sum().toString()
                        )
                    }
                } else {
                    holder.dashboardSiteBinding.generalmanagerLayout.visibility = View.GONE

                }
            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE

                holder.dashboardSiteBinding.noOrderFoundText.visibility = View.VISIBLE
            }


        } else if (items.replace(" ", "").equals("MANAGER", true)) {
            if (items.replace(" ", "").equals("MANAGER", true)) {
                if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                        .equals("EXECUTIVE", true)
                ) {
                    holder.dashboardSiteBinding.generalmanagerLayout.visibility = View.GONE
                    holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE

                    holder.dashboardSiteBinding.noOrderFoundText.visibility = View.GONE
                }

                if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                        .equals("MANAGER", true)
                ) {
                    val userData = LoginRepo.getProfile()
                    if (userData != null) {
                        holder.dashboardSiteBinding.gmEmpname.setText(userData.EMPNAME + "\n" + Preferences.getAppLevelDesignationQCFail())

                    }
                } else {
                    holder.dashboardSiteBinding.gmEmpname.setPadding(0, 20, 0, 0)
                    holder.dashboardSiteBinding.gmEmpname.setText(items)
                }
                holder.dashboardSiteBinding.logo.setImageResource(R.drawable.qc_manager)
                holder.dashboardSiteBinding.empid.setTextColor(Color.parseColor("#636fc1"))
                holder.dashboardSiteBinding.siteId.setTextColor(Color.parseColor("#636fc1"))
                holder.dashboardSiteBinding.rt.setTextColor(Color.parseColor("#636fc1"))

                holder.dashboardSiteBinding.fr.setTextColor(Color.parseColor("#636fc1"))
                holder.dashboardSiteBinding.generalmanagerLayout.setBackgroundColor(
                    Color.parseColor(
                        "#636fc1"
                    )
                )
                holder.dashboardSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#7e88c7"))
                holder.dashboardSiteBinding.pendingCountSum.setText(managerPendingCountSum.stream()
                    .mapToInt({ obj: Int -> obj }).sum().toString()
                )

            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE

                holder.dashboardSiteBinding.noOrderFoundText.visibility = View.VISIBLE
            }


        } else if (items.replace(" ", "").equals("EXECUTIVE", true)) {
            if (items.replace(" ", "").equals("EXECUTIVE", true)) {

                holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE

                holder.dashboardSiteBinding.noOrderFoundText.visibility = View.GONE



                holder.dashboardSiteBinding.logo.setImageResource(R.drawable.qc_executive)

                if (Preferences.getAppLevelDesignationQCFail().replace(" ", "")
                        .equals("EXECUTIVE", true)
                ) {
                    val userData = LoginRepo.getProfile()
                    if (userData != null) {
                        holder.dashboardSiteBinding.gmEmpname.setText(userData.EMPNAME + "\n" + Preferences.getAppLevelDesignationQCFail())

                    }
                } else {

                    holder.dashboardSiteBinding.gmEmpname.setPadding(0, 20, 0, 0)
                    holder.dashboardSiteBinding.gmEmpname.setText(items)
                }
                holder.dashboardSiteBinding.empid.setTextColor(Color.parseColor("#f4a841"))
                holder.dashboardSiteBinding.siteId.setTextColor(Color.parseColor("#f4a841"))
                holder.dashboardSiteBinding.rt.setTextColor(Color.parseColor("#f4a841"))

                holder.dashboardSiteBinding.fr.setTextColor(Color.parseColor("#f4a841"))
                holder.dashboardSiteBinding.generalmanagerLayout.setBackgroundColor(
                    Color.parseColor(
                        "#f4a841"
                    )
                )
                holder.dashboardSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#f6b968"))
                holder.dashboardSiteBinding.pendingCountSum.setText(executivePendingCountSum.stream()
                    .mapToInt({ obj: Int -> obj }).sum().toString()
                )
            } else {
                holder.dashboardSiteBinding.parentLayout.visibility = View.GONE

                holder.dashboardSiteBinding.noOrderFoundText.visibility = View.VISIBLE
            }


        }


        holder.dashboardSiteBinding.generalmanagerArrow.setOnClickListener {

            if (items.replace(" ", "").equals("GENERALMANAGER", true)) {
                if (generalManager == false) {
                    siteIdsGroupedList.clear()
                    siteIdsGroupedList =
                        gmPendingCountResponseList.stream()
                            .collect(Collectors.groupingBy { w -> w.siteid })
//           getStorePendingApprovedList.getList.clear()


                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 90f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.adapter =
                        DashboardSitesAdapter(
                            mContext,
                            gmPendingCountResponseList,
                            siteIdsGroupedList
                        )
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.VISIBLE
                    holder.dashboardSiteBinding.generalmanagerTableLayout.visibility = View.VISIBLE
                    generalManager = true
                } else if (generalManager == true) {
                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 0f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.GONE
                    holder.dashboardSiteBinding.generalmanagerTableLayout.visibility = View.GONE
                    generalManager = false
                }
            } else if (items.replace(" ", "").equals("MANAGER", true)) {
                if (maanager == false) {
                    siteIdsGroupedList.clear()
                    siteIdsGroupedList =
                        managerPendingCountResponseList.stream()
                            .collect(Collectors.groupingBy { w -> w.siteid })
                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 90f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.adapter =
                        DashboardSitesAdapter(
                            mContext,
                            managerPendingCountResponseList,
                            siteIdsGroupedList
                        )
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.VISIBLE
                    holder.dashboardSiteBinding.generalmanagerTableLayout.visibility = View.VISIBLE
                    maanager = true
                } else if (maanager == true) {
                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 0f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.GONE
                    holder.dashboardSiteBinding.generalmanagerTableLayout.visibility = View.GONE
                    maanager = false
                }
            } else if (items.replace(" ", "").equals("EXECUTIVE", true)) {
                if (executive == false) {
                    siteIdsGroupedList.clear()
                    siteIdsGroupedList =
                        executivePendingCountResponseList.stream()
                            .collect(Collectors.groupingBy { w -> w.siteid })


                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 90f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.adapter =
                        DashboardSitesAdapter(
                            mContext,
                            executivePendingCountResponseList,
                            siteIdsGroupedList
                        )
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.VISIBLE
                    holder.dashboardSiteBinding.generalmanagerTableLayout.visibility = View.VISIBLE
                    executive = true
                } else if (executive == true) {
                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 0f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.GONE
                    holder.dashboardSiteBinding.generalmanagerTableLayout.visibility = View.GONE
                    executive = false
                }
            }

        }


    }


    override fun getItemCount(): Int {
        return designations.size
    }

    class ViewHolder(val dashboardSiteBinding: DashboardSiteBinding) :
        RecyclerView.ViewHolder(dashboardSiteBinding.root)
}


