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
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import java.util.function.Predicate
import java.util.stream.Collectors

class RtoManagerAdapter(
        val mContext: Context,
        val mCallBack: QcDashBoardCallback,
        var qcfailDashboardList: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,
        val designation: String,
        var qcfailhierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy>,

        var getqcfailhierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>,

        ) :
        RecyclerView.Adapter<RtoManagerAdapter.ViewHolder>() {
    var rtoSitesAdapter: RtoSitesAdapter? = null
    var rtoExecutiveAdapter: RtoExecutiveAdapter? = null
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


        if (qcfailDashboardList.isNotEmpty()) {
            for (i in qcfailDashboardList.indices) {
                if (getqcfailhierarchyList.get(position).empid!!.contains("-")){
                    if (qcfailDashboardList.get(i).empid.equals(
                            getqcfailhierarchyList.get(position).empid!!.split("-").get(0))
                    ) {
                        holder.dashboardSiteBinding.sumOfRtValues.setText(NumberFormat.getNumberInstance(Locale.US).format(qcfailDashboardList.get(i).rtoamount!! + qcfailDashboardList.get(i).rrtoamount!!).toString())

                        holder.dashboardSiteBinding.rtCount.setText(qcfailDashboardList.get(i).rtocount.toString())
                        holder.dashboardSiteBinding.rtovalue.setText(DecimalFormat("#,###.00").format(
                            qcfailDashboardList.get(i).rtoamount).toString())
                        holder.dashboardSiteBinding.rrtoCount.setText(qcfailDashboardList.get(i).rrtocount.toString())
                        holder.dashboardSiteBinding.rrtovalue.setText(DecimalFormat("#,###.00").format(
                            qcfailDashboardList.get(i).rrtoamount).toString())
                    }


                }
                else{
                    if (qcfailDashboardList.get(i).empid.equals(
                            getqcfailhierarchyList.get(position).empid)
                    ) {
                        holder.dashboardSiteBinding.sumOfRtValues.setText(NumberFormat.getNumberInstance(Locale.US).format(qcfailDashboardList.get(i).rtoamount!! + qcfailDashboardList.get(i).rrtoamount!!).toString())

                        holder.dashboardSiteBinding.rtCount.setText(qcfailDashboardList.get(i).rtocount.toString())
                        holder.dashboardSiteBinding.rtovalue.setText(DecimalFormat("#,###.00").format(
                            qcfailDashboardList.get(i).rtoamount).toString())
                        holder.dashboardSiteBinding.rrtoCount.setText(qcfailDashboardList.get(i).rrtocount.toString())
                        holder.dashboardSiteBinding.rrtovalue.setText(DecimalFormat("#,###.00").format(
                            qcfailDashboardList.get(i).rrtoamount).toString())


                    }
                }

            }


        }


        if (getqcfailhierarchyList[position].isClick) {

            if(getqcfailhierarchyList[position].designation.equals("EXECUTIVE")){

            }
            else{
                holder.dashboardSiteBinding.executiveRecycleview.visibility = View.VISIBLE

            }



            holder.dashboardSiteBinding.closeArrow.visibility = View.VISIBLE
//            holder.dashboardSiteBinding.rtoLayout.visibility = View.VISIBLE
            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.GONE
            holder.dashboardSiteBinding.sitesRecyclerView.visibility = View.VISIBLE
            rtoExecutiveAdapter?.notifyDataSetChanged()
        } else {
            holder.dashboardSiteBinding.closeArrow.visibility = View.GONE
//            holder.dashboardSiteBinding.rtoLayout.visibility = View.GONE
            holder.dashboardSiteBinding.executiveRecycleview.visibility = View.GONE

            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.VISIBLE
            holder.dashboardSiteBinding.sitesRecyclerView.visibility = View.GONE

        }

        if (qcfailhierarchyList.isNotEmpty()) {
            for (i in qcfailhierarchyList.indices) {
                if (qcfailhierarchyList.get(i).employeId.equals(
                                getqcfailhierarchyList.get(position).empid)
                ) {

                    var item = Getqcfailpendinghistoryforhierarchy()
                    for (i in qcfailhierarchyList) {
                        if (i.employeId!!.equals(getqcfailhierarchyList.get(position).empid)) {
                            item = i

                        }
                    }

                    dashBoardList= item.pendingcount!!.stream().filter { x -> x.rtocount !=0 || x.rrtocount!=0} // or `Objects::nonNull`
                        .collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>
                    dashBoardList.sortWith { o1: Getqcfailpendinghistoryforhierarchy.Pendingcount, o2: Getqcfailpendinghistoryforhierarchy.Pendingcount ->
                        o2.rtoamount!!.compareTo(
                            o1.rtoamount!!
                        )
                    }

                    rtoSitesAdapter = RtoSitesAdapter(mContext,
                            dashBoardList)
                    holder.dashboardSiteBinding.sitesRecyclerView.adapter = rtoSitesAdapter
                    rtoExecutiveAdapter?.notifyDataSetChanged()
                    rtoSitesAdapter!!.notifyDataSetChanged()


                    rtoExecutiveAdapter = RtoExecutiveAdapter(mContext,
                            mCallBack,
                            qcfailDashboardList, designation,
                            qcfailhierarchyList,
                            item.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>)
                    holder.dashboardSiteBinding.executiveRecycleview.adapter =
                            rtoExecutiveAdapter


                }
            }


        }








        holder.dashboardSiteBinding.generalmanagerArrow.setOnClickListener {

            for ( i in getqcfailhierarchyList.indices){
                if (getqcfailhierarchyList[position]==getqcfailhierarchyList[i]){
                    getqcfailhierarchyList[position].setisClick(true)

                }else{
                    getqcfailhierarchyList[i].setisClick(false)

                }
            }



            var isContain: Boolean
            val predicate =
                    Predicate { qcfailDashboardList: Getqcfailpendinghistorydashboard.Pendingcount ->
                        qcfailDashboardList.empid.equals(items.empid)
                    }


            isContain = qcfailDashboardList.stream().anyMatch(predicate)


            if (isContain) {
                mCallBack.notify(position, false)
                notifyDataSetChanged()


            } else {

                items.designation?.let { it1 ->
                    items.empid?.let { it2 ->
                        mCallBack.onClickManagerDashBoard(position,
                                it1,
                                it2)
                    }
                }

                notifyDataSetChanged()
            }

        }

        holder.dashboardSiteBinding.closeArrow.setOnClickListener {
            getqcfailhierarchyList[position].setisClick(false)
            notifyDataSetChanged()
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
                    holder.dashboardSiteBinding.gmEmpname.setText(items.empid!!.split("-").get(0) +" (" +items.empid!!.split("-").get(1)+")"+ "\n" + items.designation)
                    holder.dashboardSiteBinding.managerLayout.setBackgroundColor(Color.parseColor("#00acae"))
                    holder.dashboardSiteBinding.rtoLayout.setBackgroundColor(Color.parseColor("#00acae"))


                } else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
                    holder.dashboardSiteBinding.gmEmpname.setText(items.empid!!.split("-").get(0) +" (" +items.empid!!.split("-").get(1)+")"+ "\n"  + items.designation)
                    holder.dashboardSiteBinding.rtoLayout.setBackgroundColor(Color.parseColor("#606db3"))

                    holder.dashboardSiteBinding.managerLayout.setBackgroundColor(Color.parseColor("#606db3"))
                } else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
                    holder.dashboardSiteBinding.gmEmpname.setText(items.empid!!.split("-").get(0) +" (" +items.empid!!.split("-").get(1)+")"+ "\n" + items.designation)
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


