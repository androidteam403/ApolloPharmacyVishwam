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
import kotlinx.android.synthetic.main.manager_layout.view.*
import java.text.DecimalFormat
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
    var dashBoardList = ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>()
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

        val items = getqcfailhierarchyList.get(position)
        holder.dashboardSiteBinding.empId.setText(qcfailDashboardList.get(0).empid.toString())


        rtoExecutiveAdapter?.notifyDataSetChanged()


        if (qcfailDashboardList.isNotEmpty()) {
            for (i in qcfailDashboardList.indices) {
                if (qcfailDashboardList.get(i).designation.equals(getqcfailhierarchyList.get(
                        position).designation) && qcfailDashboardList.get(i).empid.equals(
                        getqcfailhierarchyList.get(position).empid)
                ) {

                    holder.dashboardSiteBinding.rtocounts.setText(qcfailDashboardList.get(i).rtocount.toString())
                    holder.dashboardSiteBinding.rtovalues.setText(DecimalFormat("#,###.00").format(
                        qcfailDashboardList.get(i).rtoamount).toString())
                    holder.dashboardSiteBinding.rrtocounts.setText(qcfailDashboardList.get(i).rrtocount.toString())
                    holder.dashboardSiteBinding.rrtovalues.setText(DecimalFormat("#,###.00").format(
                        qcfailDashboardList.get(i).rrtoamount).toString())


                }
            }


        }





        if (qcfailhierarchyList.isNotEmpty()) {
            for (i in qcfailhierarchyList.indices) {
                if (qcfailhierarchyList.get(i).designation.equals(getqcfailhierarchyList.get(
                        position).designation) && qcfailhierarchyList.get(i).employeId.equals(
                        getqcfailhierarchyList.get(position).empid)
                ) {

                    var item = Getqcfailpendinghistoryforhierarchy()
                    for (i in qcfailhierarchyList) {
                        if (i.designation!!.equals(getqcfailhierarchyList.get(position).designation)) {
                            item = i

                        }
                    }




                    rtoExecutiveAdapter = RtoExecutiveAdapter(mContext,
                        mCallBack,
                        qcfailDashboardList, designation,
                        qcfailhierarchyList,
                        item.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>)
                    holder.dashboardSiteBinding.executiveRecycleview.adapter =
                        rtoExecutiveAdapter



                    rtoSitesAdapter = RtoSitesAdapter(mContext, mCallBack,
                        item.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>)
                    holder.dashboardSiteBinding.sitesRecyclerView.adapter = rtoSitesAdapter
                    rtoExecutiveAdapter?.notifyDataSetChanged()

                }
            }


        }


        holder.dashboardSiteBinding.generalmanagerArrow.setOnClickListener {
            var isContain: Boolean
            val predicate =
                Predicate { qcfailDashboardList: Getqcfailpendinghistorydashboard.Pendingcount ->
                    qcfailDashboardList.empid.equals(items.empid)
                }


            isContain = qcfailDashboardList.stream().anyMatch(predicate)
            if (isContain) {
                getqcfailhierarchyList[position].setisClick(true)
                mCallBack.notify(position, false)
                notifyDataSetChanged()


            } else {

                getqcfailhierarchyList[position].setisClick(true)
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





        if (getqcfailhierarchyList[position].isClick) {


            holder.dashboardSiteBinding.closeArrow.visibility = View.VISIBLE
            holder.dashboardSiteBinding.rtoLayout.visibility = View.VISIBLE
            holder.dashboardSiteBinding.executiveRecycleview.visibility = View.VISIBLE
            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.GONE
            holder.dashboardSiteBinding.sitesRecyclerView.visibility = View.VISIBLE
            rtoExecutiveAdapter?.notifyDataSetChanged()
        } else {
            holder.dashboardSiteBinding.closeArrow.visibility = View.GONE
            holder.dashboardSiteBinding.rtoLayout.visibility = View.GONE
            holder.dashboardSiteBinding.executiveRecycleview.visibility = View.GONE

            holder.dashboardSiteBinding.generalmanagerArrow.visibility = View.VISIBLE
            holder.dashboardSiteBinding.sitesRecyclerView.visibility = View.GONE

        }


       if (holder.dashboardSiteBinding.empId.text.toString().isNullOrEmpty()){

       }else{

           if (holder.dashboardSiteBinding.empId.text.toString()!=items.empid) {
               holder.dashboardSiteBinding.managerLayout.visibility = View.VISIBLE

               if (items.designation?.replace(" ", "").equals("GENERALMANAGER", true)) {
                   holder.dashboardSiteBinding.managerEmpname.setText(items.empid + "\n" + items.designation)


               } else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
                   holder.dashboardSiteBinding.managerEmpname.setText(items.empid + "\n" + items.designation)

                   holder.dashboardSiteBinding.logo.setImageResource(R.drawable.qc_manager)
                   holder.dashboardSiteBinding.managerLayout.setBackgroundColor(Color.parseColor("#636fc1"))
                   holder.dashboardSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#7e88c7"))
               } else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
                   holder.dashboardSiteBinding.managerEmpname.setText(items.empid + "\n" + items.designation)

                   holder.dashboardSiteBinding.logo.setImageResource(R.drawable.qc_executive)
                   holder.dashboardSiteBinding.managerLayout.setBackgroundColor(Color.parseColor("#f4a841"))
                   holder.dashboardSiteBinding.arrowlayout.setBackgroundColor(Color.parseColor("#f6b968"))

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

