package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ManagerLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.function.Predicate
import java.util.stream.Collectors

class SearchManagerAdapter(
    val mContext: Context,
    val mCallBack: QcDashBoardCallback,
    var dashBoardListMain: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,

    var qcfailDashboardList: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,
    val designation: String,
    var qcfailhierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy>,

    var getqcfailhierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>,

    ) :
    RecyclerView.Adapter<SearchManagerAdapter.ViewHolder>() {
    var rtoSitesAdapter: RtoSitesAdapter? = null
    var rtoExecutiveAdapter: SearchExecutiveAdapter? = null
    var dashBoardList = ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>()
    var empId: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val dashboardSiteBinding: ManagerLayoutBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.manager_layout,
                parent,
                false
            )
        return ViewHolder(dashboardSiteBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        rtoExecutiveAdapter?.notifyDataSetChanged()

        val items = getqcfailhierarchyList.get(position)


        if (dashBoardListMain.isNotEmpty()) {
            for (i in dashBoardListMain.indices) {
                if (dashBoardListMain.get(i).empid.equals(getqcfailhierarchyList.get(position).empid!!.split("-").get(0))) {

                    holder.dashboardSiteBinding.sumOfRtValues.visibility=View.VISIBLE
                    holder.dashboardSiteBinding.sumOfRtValues.setText(NumberFormat.getNumberInstance(Locale.US).format(dashBoardListMain.get(i).rtoamount!! + dashBoardListMain.get(i).rrtoamount!!).toString())
                    holder.dashboardSiteBinding.rtCount.setText(dashBoardListMain.get(i).rtocount.toString())
                    holder.dashboardSiteBinding.rtovalue.setText(
                        DecimalFormat("#,###.00").format(
                            dashBoardListMain.get(i).rtoamount
                        ).toString()
                    )
                    holder.dashboardSiteBinding.rrtoCount.setText(dashBoardListMain.get(i).rrtocount.toString())
                    holder.dashboardSiteBinding.rrtovalue.setText(
                        DecimalFormat("#,###.00").format(
                            dashBoardListMain.get(i).rrtoamount
                        ).toString()
                    )


                }
            }


        }


        if (getqcfailhierarchyList[position].isSearchClick) {

            if (getqcfailhierarchyList[position].designation.equals("EXECUTIVE")) {

            } else {
                holder.dashboardSiteBinding.executiveRecycleview.visibility = View.VISIBLE

            }

            if (items.designation
                    .equals("EXECUTIVE", true)
            ) {
                holder.dashboardSiteBinding.rtoHeader.visibility = View.VISIBLE

            } else {
                holder.dashboardSiteBinding.rtoHeader.visibility = View.GONE

            }

            holder.dashboardSiteBinding.closeArrow.visibility = View.VISIBLE
            holder.dashboardSiteBinding.rtoLayout.visibility = View.VISIBLE
            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.GONE
            holder.dashboardSiteBinding.sitesRecyclerView.visibility = View.VISIBLE
            rtoExecutiveAdapter?.notifyDataSetChanged()
        } else {
            holder.dashboardSiteBinding.closeArrow.visibility = View.GONE
            holder.dashboardSiteBinding.rtoLayout.visibility = View.GONE
            holder.dashboardSiteBinding.executiveRecycleview.visibility = View.GONE
            holder.dashboardSiteBinding.rtoHeader.visibility = View.GONE
            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.VISIBLE
            holder.dashboardSiteBinding.sitesRecyclerView.visibility = View.GONE

        }

        if (qcfailhierarchyList.isNotEmpty()) {
            for (i in qcfailhierarchyList.indices) {
                if (qcfailhierarchyList.get(i).employeId!!.split("-").get(0).equals(
                        getqcfailhierarchyList.get(position).empid!!.split("-").get(0)
                    )
                ) {

                    var item = Getqcfailpendinghistoryforhierarchy()
                    for (i in qcfailhierarchyList) {
                        if (i.employeId!!.equals(getqcfailhierarchyList.get(position).empid)) {
                            item = i

                        }
                    }

                    dashBoardList = item.pendingcount!!.stream()
                        .filter { x -> x.rtocount != 0 || x.rrtocount != 0 } // or `Objects::nonNull`
                        .collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                    dashBoardList.sortWith { o1: Getqcfailpendinghistoryforhierarchy.Pendingcount, o2: Getqcfailpendinghistoryforhierarchy.Pendingcount ->
                        o2.rtoamount!!.compareTo(
                            o1.rtoamount!!
                        )
                    }

                    rtoSitesAdapter = RtoSitesAdapter(
                        mContext,
                        dashBoardList, mCallBack
                    )
                    holder.dashboardSiteBinding.sitesRecyclerView.adapter = rtoSitesAdapter
                    rtoExecutiveAdapter?.notifyDataSetChanged()
                    rtoSitesAdapter!!.notifyDataSetChanged()


                    if (getqcfailhierarchyList.filter { it.siteid!=null }.size>0){
                        rtoExecutiveAdapter = SearchExecutiveAdapter(
                            mContext,
                            mCallBack,dashBoardListMain,
                            qcfailDashboardList, designation,
                            qcfailhierarchyList,
                            item.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                        )
                        holder.dashboardSiteBinding.executiveRecycleview.adapter =
                            rtoExecutiveAdapter

                    }



                }
            }


        }








        holder.dashboardSiteBinding.generalmanagerArrow.setOnClickListener {
            mCallBack.onClickManagerHierarchy(position,getqcfailhierarchyList)

//            for (i in getqcfailhierarchyList.indices) {
//                if (getqcfailhierarchyList[position] == getqcfailhierarchyList[i]) {
//                    getqcfailhierarchyList[position].setiisSearchClick(true)
//
//                } else {
//                    getqcfailhierarchyList[i].setiisSearchClick(false)
//
//                }
//            }
//
//
//            var isContain: Boolean
//            if (items.empid!!.contains("-")){
//                val predicate =
//                    Predicate { qcfailDashboardList: Getqcfailpendinghistorydashboard.Pendingcount ->
//                        qcfailDashboardList.empid.equals(items.empid!!.split("-").get(0))
//                    }
//
//
//                isContain = qcfailDashboardList.stream().anyMatch(predicate)
//
//            }else{
//                val predicate =
//                    Predicate { qcfailDashboardList: Getqcfailpendinghistorydashboard.Pendingcount ->
//                        qcfailDashboardList.empid.equals(items.empid)
//                    }
//
//
//                isContain = qcfailDashboardList.stream().anyMatch(predicate)
//
//            }
//
//
//            if (isContain) {
//                mCallBack.notify(position, false)
//                notifyDataSetChanged()
//
//
//            } else {
//
////                items.designation?.let { it1 ->
////                    items.empid?.let { it2 ->
////                        mCallBack.onClickManagerDashBoard(
////                            position,
////                            it1,
////                            it2
////                        )
////                    }
////                }
//
//                notifyDataSetChanged()
//            }

        }

        holder.dashboardSiteBinding.closeArrow.setOnClickListener {
            mCallBack.onClickManagerHierarchy(position,getqcfailhierarchyList)

        }





        for (i in qcfailhierarchyList.indices) {
            for (j in qcfailhierarchyList[i].pendingcount?.indices!!) {
                if (qcfailhierarchyList[i].pendingcount?.size == getqcfailhierarchyList.size) {
                    if (qcfailhierarchyList[i].pendingcount!!.equals(getqcfailhierarchyList)) {
                        empId = qcfailhierarchyList[i].employeId.toString()
                    }

                }


            }


        }

        if (empId.isNullOrEmpty()) {

        } else {

            if (empId != items.empid) {

                if (items.designation?.replace(" ", "").equals("GENERALMANAGER", true)) {
                    holder.dashboardSiteBinding.gmEmpname.setText(
                        items.empid!!.split("-").get(0) + " (" + items.empid!!.split("-")
                            .get(1) + ")" + "\n" + items.designation
                    )
                    holder.dashboardSiteBinding.managerLayout.setBackgroundColor(Color.parseColor("#00acae"))
                    holder.dashboardSiteBinding.rtoLayout.setBackgroundColor(Color.parseColor("#00acae"))


                } else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
                    holder.dashboardSiteBinding.gmEmpname.setText(
                        items.empid!!.split("-").get(0) + " (" + items.empid!!.split("-")
                            .get(1) + ")" + "\n" + items.designation
                    )
                    holder.dashboardSiteBinding.rtoLayout.setBackgroundColor(Color.parseColor("#606db3"))

                    holder.dashboardSiteBinding.managerLayout.setBackgroundColor(Color.parseColor("#606db3"))
                } else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
                    holder.dashboardSiteBinding.gmEmpname.setText(
                        items.empid!!.split("-").get(0) + " (" + items.empid!!.split("-")
                            .get(1) + ")" + "\n" + items.designation
                    )
                    holder.dashboardSiteBinding.rtoLayout.setBackgroundColor(Color.parseColor("#d48a2b"))

                    holder.dashboardSiteBinding.managerLayout.setBackgroundColor(Color.parseColor("#d48a2b"))

                }
            }
        }

    }


    override fun getItemCount(): Int {
        return getqcfailhierarchyList.size
    }

    class ViewHolder(val dashboardSiteBinding: ManagerLayoutBinding) :
        RecyclerView.ViewHolder(dashboardSiteBinding.root)


}


