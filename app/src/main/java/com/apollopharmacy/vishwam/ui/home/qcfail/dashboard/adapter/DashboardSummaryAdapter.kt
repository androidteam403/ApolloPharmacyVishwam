package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.network.LoginRepo
import com.apollopharmacy.vishwam.databinding.DashboardSummaryBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.stream.Collectors

class DashboardSummaryAdapter(
    val mContext: Context,
    val mCallBack: QcDashBoardCallback,
    val designation: String,

    var dashBoardList: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,
    var dashBoardListMain: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,

    var dashboardHierarchyList: java.util.ArrayList<Getqcfailpendinghistoryforhierarchy>,


    ) :
    RecyclerView.Adapter<DashboardSummaryAdapter.ViewHolder>() {

    var rtoManagerAdapter: SearchManagerAdapter? = null
    var rtoSitesAdapter: RtoSitesAdapter? = null
    var dashBoardListNew = java.util.ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val searchSiteBinding: DashboardSummaryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.dashboard_summary,
                parent,
                false
            )
        return ViewHolder(searchSiteBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = dashBoardList.get(position)
        val userData = LoginRepo.getProfile()

        if (items.designation?.replace(" ", "").equals("GENERALMANAGER", true)) {
            holder.searchSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)
            holder.searchSiteBinding.cardView.strokeColor = ContextCompat.getColor(mContext, R.color.qcGmColor)
            if (dashboardHierarchyList!=null){
                for (i in dashboardHierarchyList.indices){
                    if (items.empid.equals(dashboardHierarchyList.get(i).emplId!!.split("-").get(0))){

                        holder.searchSiteBinding.gmEmpname.setText(items.empid+ " ("+ dashboardHierarchyList.get(i).emplId!!.split("-").get(1)+")" + "\n" + items.designation)


                    }
                    else{
                        if (userData != null) {
                            holder.searchSiteBinding.gmEmpname.setText(items.empid+ " ("+ userData.EMPNAME+")" + "\n" + items.designation)

                        }
                    }
                }
            }



            holder.searchSiteBinding.generalmanagerLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.qcGmColor
                )
            )
            holder.searchSiteBinding.rtoLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.qcGmColor
                )
            )

        }
        else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
            holder.searchSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)
            holder.searchSiteBinding.cardView.strokeColor = ContextCompat.getColor(mContext, R.color.qcManagerColor)
            if (dashboardHierarchyList!=null){
                for (i in dashboardHierarchyList.indices){
                    if (items.empid.equals(dashboardHierarchyList.get(i).emplId!!.split("-").get(0))){

                        holder.searchSiteBinding.gmEmpname.setText(items.empid+ " ("+ dashboardHierarchyList.get(i).emplId!!.split("-").get(1)+")" + "\n" + items.designation)


                    }
                    else{
                        if (userData != null) {
                            holder.searchSiteBinding.gmEmpname.setText(items.empid+ " ("+ userData.EMPNAME+")" + "\n" + items.designation)

                        }
                    }
                }
            }


            holder.searchSiteBinding.generalmanagerLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.qcManagerColor
                )
            )
            holder.searchSiteBinding.rtoLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.qcManagerColor
                )
            )
        }
        else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
            holder.searchSiteBinding.gmEmpname.setText(items.empid + "\n" + items.designation)
            holder.searchSiteBinding.cardView.strokeColor = ContextCompat.getColor(mContext, R.color.qcexecutiveColor)
            if (dashboardHierarchyList!=null){
                for (i in dashboardHierarchyList.indices){
                    if (items.empid.equals(dashboardHierarchyList.get(i).emplId!!.split("-").get(0))){

                        holder.searchSiteBinding.gmEmpname.setText(items.empid+ " ("+ dashboardHierarchyList.get(i).emplId!!.split("-").get(1)+")" + "\n" + items.designation)


                    }
                    else{
                        if (userData != null) {
                            holder.searchSiteBinding.gmEmpname.setText(items.empid+ " ("+ userData.EMPNAME+")" + "\n" + items.designation)

                        }
                    }
                }
            }




            holder.searchSiteBinding.generalmanagerLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.qcexecutiveColor
                )
            )
            holder.searchSiteBinding.rtoLayout.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.qcexecutiveColor
                )
            )
        }
        holder.searchSiteBinding.sumOfRtValues.setText(NumberFormat.getNumberInstance(Locale.US).format(items.rtoamount!! + items.rrtoamount!!).toString())

        holder.searchSiteBinding.rtCount.setText(items.rtocount.toString())
        holder.searchSiteBinding.rtovalue.setText(
            DecimalFormat("#,###.00").format(items.rtoamount).toString()
        )
        holder.searchSiteBinding.rrtoCount.setText(items.rrtocount.toString())
        holder.searchSiteBinding.rrtovalue.setText(
            DecimalFormat("#,###.00").format(items.rrtoamount).toString()
        )

        if (dashboardHierarchyList.isNotEmpty()) {
            for (i in dashboardHierarchyList.indices) {
                if (dashboardHierarchyList.get(i).designation.equals(dashBoardList.get(position).designation) && dashboardHierarchyList.get(i).employeId!!.split("-").get(0).equals(dashBoardList.get(position).empid)) {

                    var item = Getqcfailpendinghistoryforhierarchy()
                    for (i in dashboardHierarchyList) {
                        if (i.employeId!!.split("-").get(0)!!.equals(dashBoardList.get(position).empid)) {
                            item = i

                        }
                    }

                    dashBoardListNew= item.pendingcount!!.stream().filter { x -> x.rtocount !=0 || x.rrtocount!=0} // or `Objects::nonNull`
                        .collect(Collectors.toList()) as java.util.ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>

                    dashBoardListNew.sortWith { o1: Getqcfailpendinghistoryforhierarchy.Pendingcount, o2: Getqcfailpendinghistoryforhierarchy.Pendingcount ->
                        o2.rtoamount!!.compareTo(
                            o1.rtoamount!!
                        )
                    }
                    if(items.designation.equals("EXECUTIVE", true)
                    ) {
                        rtoSitesAdapter = RtoSitesAdapter(
                            mContext,
                            dashBoardListNew, mCallBack,
                        )

                        holder.searchSiteBinding.gmsitesRecyclerView.adapter = rtoSitesAdapter

                        rtoSitesAdapter!!.notifyDataSetChanged()
                    }else{
                        rtoManagerAdapter = SearchManagerAdapter(
                            mContext,
                            mCallBack,dashBoardListMain,
                            dashBoardList, designation,
                            dashboardHierarchyList,
                            item.pendingcount as java.util.ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                        )
                        holder.searchSiteBinding.managerRecyclerView.adapter = rtoManagerAdapter
                        rtoManagerAdapter!!.notifyDataSetChanged()

                    }






                }
            }


        }

        holder.searchSiteBinding.generalmanagerArrow.setOnClickListener {
            mCallBack.onSearchClick(position,dashBoardList)
        }


        if (dashBoardList[position].isSearchClick) {
            if (items.designation.equals("EXECUTIVE", true)) {
                holder.searchSiteBinding.rtoHeader.visibility=View.VISIBLE

            }
            holder.searchSiteBinding.managerRecyclerView.visibility = View.VISIBLE
            holder.searchSiteBinding.gmsitesRecyclerView.visibility=View.VISIBLE
            holder.searchSiteBinding.generalmanagerArrow.rotation = 270f

        } else {
            holder.searchSiteBinding.managerRecyclerView.visibility = View.GONE
            holder.searchSiteBinding.generalmanagerArrow.rotation = 180f
            holder.searchSiteBinding.gmsitesRecyclerView.visibility=View.GONE
            holder.searchSiteBinding.rtoHeader.visibility=View.GONE

        }


    }


    override fun getItemCount(): Int {
        return dashBoardList.size
    }

    class ViewHolder(val searchSiteBinding: DashboardSummaryBinding) :
        RecyclerView.ViewHolder(searchSiteBinding.root)


}



