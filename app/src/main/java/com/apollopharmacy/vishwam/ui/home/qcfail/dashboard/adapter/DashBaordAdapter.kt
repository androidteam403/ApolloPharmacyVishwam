package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DashboardSiteBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.PendingCountResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.util.stream.Collectors


var distinctpendingCountListList = java.util.ArrayList<PendingCountResponse.Pendingcount>()

class DashBaordAdapter(
    val mContext: Context,
    val mCallBack: QcDashBoardCallback,

    var pendingCountResponseList: ArrayList<PendingCountResponse.Pendingcount>,
    var designations: ArrayList<String>,
    var distinctpendingCountList: ArrayList<PendingCountResponse.Pendingcount>,

    ) :
    RecyclerView.Adapter<DashBaordAdapter.ViewHolder>() , Filterable {
    var gmPendingCountResponseList =
        java.util.ArrayList<PendingCountResponse.Pendingcount>()
    var managerPendingCountResponseList =
        java.util.ArrayList<PendingCountResponse.Pendingcount>()
    var executivePendingCountResponseList =
        java.util.ArrayList<PendingCountResponse.Pendingcount>()
    var charString: String? = ""
    var distinctpendingCountFilterList =
        java.util.ArrayList<PendingCountResponse.Pendingcount>()

    var siteIdsGroupedList = (mutableMapOf<String, List<PendingCountResponse.Pendingcount>>())
    var generalManager: Boolean = false
    var maanager: Boolean = false
    var executive: Boolean = false

    init {
        distinctpendingCountListList=distinctpendingCountList
    }
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
        val items = distinctpendingCountList.get(position)
        if (distinctpendingCountList.isEmpty()){
            holder.dashboardSiteBinding.noOrderFoundText.visibility=View.VISIBLE
        }
        else{
            holder.dashboardSiteBinding.noOrderFoundText.visibility=View.GONE

        }

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

        val gmRtCountSum: List<Int> = gmPendingCountResponseList
            .filter { it.ordertype == "REVERSE RETURN" } // Replace "forward" and "reverse" with your actual condition
            .map { it.pendingcount!! }

        val gmFrCountSum: List<Int> = gmPendingCountResponseList
            .filter { it.ordertype == "FORWARD RETURN" } // Replace "forward" and "reverse" with your actual condition
            .map { it.pendingcount!! }

        val managerRtCountSum: List<Int> = managerPendingCountResponseList
            .filter { it.ordertype == "REVERSE RETURN" } // Replace "forward" and "reverse" with your actual condition
            .map { it.pendingcount!! }

        val managerFrCountSum: List<Int> = managerPendingCountResponseList
            .filter { it.ordertype == "FORWARD RETURN" } // Replace "forward" and "reverse" with your actual condition
            .map { it.pendingcount!! }

        val executivePendingRtCountSum: List<Int> = executivePendingCountResponseList
            .filter { it.ordertype == "REVERSE RETURN" } // Replace "forward" and "reverse" with your actual condition
            .map { it.pendingcount!! }

        val executivePendingFrCountSum: List<Int> = executivePendingCountResponseList
            .filter { it.ordertype == "FORWARD RETURN" } // Replace "forward" and "reverse" with your actual condition
            .map { it.pendingcount!! }





        if (items.designation!!.replace(" ", "").equals("GENERALMANAGER", true)) {
            holder.dashboardSiteBinding.noOrderFoundText.visibility = View.GONE
            holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE
            holder.dashboardSiteBinding.parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.pendency_green))

            holder.dashboardSiteBinding.designation.setText(items.designation)

                holder.dashboardSiteBinding.gmEmpname.setText(items.empid!!.split("-").get(1) )
                holder.dashboardSiteBinding.empId.setText(items.empid!!.split("-").get(0))




            holder.dashboardSiteBinding.frCount.setText(gmRtCountSum.stream()
                .mapToInt({ obj: Int -> obj }).sum().toString()
            )
            holder.dashboardSiteBinding.rtCount.setText(gmFrCountSum.stream()
                .mapToInt({ obj: Int -> obj }).sum().toString()
            )

        }
        else  if (items.designation!!.replace(" ", "").equals("MANAGER", true)) {


            holder.dashboardSiteBinding.designation.setText(items.designation)

                holder.dashboardSiteBinding.gmEmpname.setText(items.empid!!.split("-").get(1) )
                holder.dashboardSiteBinding.empId.setText(items.empid!!.split("-").get(0))



            holder.dashboardSiteBinding.parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.pendency_voilet))

            holder.dashboardSiteBinding.rtlayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor(
                "#8e9bc8"
            ))
            holder.dashboardSiteBinding.frLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor(
                "#8e9bc8"
            ))
            holder.dashboardSiteBinding.frCount.setText(managerFrCountSum.stream()
                .mapToInt({ obj: Int -> obj }).sum().toString()
            )
            holder.dashboardSiteBinding.rtCount.setText(managerRtCountSum.stream()
                .mapToInt({ obj: Int -> obj }).sum().toString()
            )

        }
        else  if (items.designation!!.replace(" ", "").equals("EXECUTIVE", true)) {

            holder.dashboardSiteBinding.parentLayout.visibility = View.VISIBLE

            holder.dashboardSiteBinding.noOrderFoundText.visibility = View.GONE


            holder.dashboardSiteBinding.designation.setText(items.designation)

                holder.dashboardSiteBinding.gmEmpname.setText(items.empid!!.split("-").get(1) )
                holder.dashboardSiteBinding.empId.setText(items.empid!!.split("-").get(0))



            holder.dashboardSiteBinding.parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.pendency_orange))
            holder.dashboardSiteBinding.rtlayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor(
                "#D48A2B"
            ))

            holder.dashboardSiteBinding.frLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor(
                "#D48A2B"
            ))
//                holder.dashboardSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#f6b968"))
            holder.dashboardSiteBinding.frCount.setText(executivePendingFrCountSum.stream()
                .mapToInt({ obj: Int -> obj }).sum().toString()
            )
            holder.dashboardSiteBinding.rtCount.setText(executivePendingRtCountSum.stream()
                .mapToInt({ obj: Int -> obj }).sum().toString()
            )
        }
        else {
            holder.dashboardSiteBinding.parentLayout.visibility = View.GONE

        }





        holder.dashboardSiteBinding.generalmanagerArrow.setOnClickListener {

            if (items.designation!!.replace(" ", "").equals("GENERALMANAGER", true)) {
                if (generalManager == false) {
                    siteIdsGroupedList.clear()
                    siteIdsGroupedList =
                        gmPendingCountResponseList.stream()
                            .collect(Collectors.groupingBy { w -> w.siteid })
//           getStorePendingApprovedList.getList.clear()


                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 270f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.adapter =
                        DashboardSitesAdapter(
                            mContext,
                            gmPendingCountResponseList,
                            gmPendingCountResponseList.distinctBy { it.siteid } as ArrayList<PendingCountResponse.Pendingcount>
                        )
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.VISIBLE
                    generalManager = true
                } else if (generalManager == true) {
                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 180f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.GONE
                    generalManager = false
                }
            }
            else if (items.designation!!.replace(" ", "").equals("MANAGER", true)) {
                if (maanager == false) {
                    siteIdsGroupedList.clear()
                    siteIdsGroupedList =
                        managerPendingCountResponseList.stream()
                            .collect(Collectors.groupingBy { w -> w.siteid })
                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 270f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.adapter =
                        DashboardSitesAdapter(
                            mContext,
                            managerPendingCountResponseList,
                            managerPendingCountResponseList.distinctBy { it.siteid } as ArrayList<PendingCountResponse.Pendingcount>
                        )
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.VISIBLE
                    maanager = true
                } else if (maanager == true) {
                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 180f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.GONE
                    maanager = false
                }
            }
            else if (items.designation!!.replace(" ", "").equals("EXECUTIVE", true)) {
                if (executive == false) {
                    siteIdsGroupedList.clear()
                    siteIdsGroupedList =
                        executivePendingCountResponseList.stream()
                            .collect(Collectors.groupingBy { w -> w.siteid })


                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 270f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.adapter =
                        DashboardSitesAdapter(
                            mContext,
                            executivePendingCountResponseList,
                            executivePendingCountResponseList.distinctBy { it.siteid } as ArrayList<PendingCountResponse.Pendingcount>
                        )
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.VISIBLE
                    executive = true
                } else if (executive == true) {
                    holder.dashboardSiteBinding.generalmanagerArrow.rotation = 180f
                    holder.dashboardSiteBinding.gmDashboardrecycleview.visibility = View.GONE
                    executive = false
                }
            }

        }


    }


    override fun getItemCount(): Int {
        return distinctpendingCountList.size
    }

    class ViewHolder(val dashboardSiteBinding: DashboardSiteBinding) :
        RecyclerView.ViewHolder(dashboardSiteBinding.root)

    override fun getFilter(): Filter? {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults? {
                charString = charSequence.toString()
                if (charString!!.isEmpty()) {
                    distinctpendingCountList = distinctpendingCountListList
                } else {
                    distinctpendingCountFilterList.clear()
                    for (row in distinctpendingCountListList) {
                        if (!distinctpendingCountFilterList.contains(row) && row.empid!!.toLowerCase()
                                .contains(charString!!.toLowerCase())
                        ) {
                            distinctpendingCountFilterList.add(row)
                        }
                    }
                    distinctpendingCountList = distinctpendingCountFilterList
                }
                val filterResults = FilterResults()
                filterResults.values = distinctpendingCountList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            protected override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults,
            ) {
                if (distinctpendingCountList != null && !distinctpendingCountList.isEmpty()) {
                    distinctpendingCountList =
                        filterResults.values as java.util.ArrayList<PendingCountResponse.Pendingcount>
                    try {
                        mCallBack.noDataFound(false)

                        notifyDataSetChanged()
                    } catch (e: Exception) {
                    }
                } else {
                    mCallBack.noDataFound(true)
                    notifyDataSetChanged()
                }
            }
        }
    }
}


