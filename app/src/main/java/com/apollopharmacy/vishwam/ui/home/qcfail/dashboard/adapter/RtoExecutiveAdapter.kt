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
import com.apollopharmacy.vishwam.databinding.ExecutiveLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.util.function.Predicate

class RtoExecutiveAdapter(
        val mContext: Context,
        val mCallBack: QcDashBoardCallback,
        var qcfailDashboardList: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,
        val designation: String,
        var qcfailhierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy>,

        var getqcfailhierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>,

        ) :
    RecyclerView.Adapter<RtoExecutiveAdapter.ViewHolder>() {
    var rtoSitesAdapter: RtoSitesAdapter? = null
    var empId: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val executiveLayoutBinding: ExecutiveLayoutBinding =
            DataBindingUtil.inflate(
                    LayoutInflater.from(mContext),
                    R.layout.executive_layout,
                    parent,
                    false
            )
        return ViewHolder(executiveLayoutBinding)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val items = getqcfailhierarchyList.get(position)

        if (qcfailhierarchyList.isNotEmpty()) {
            for (i in qcfailhierarchyList.indices) {
                if (qcfailhierarchyList.get(i).designation.equals(getqcfailhierarchyList.get(
                                position).designation) && qcfailhierarchyList.get(i).employeId.equals(
                                getqcfailhierarchyList.get(position).empid)
                ) {




                    var item = Getqcfailpendinghistoryforhierarchy()
                    for (i in qcfailhierarchyList) {
                        if (i.employeId!!.equals(getqcfailhierarchyList.get(position).empid)) {
                            item = i

                        }
                    }





                    rtoSitesAdapter = RtoSitesAdapter(mContext, mCallBack,
                            item.pendingcount as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>)
                    holder.executiveLayoutBinding.sitesRecyclerView.adapter = rtoSitesAdapter
                    rtoSitesAdapter!!.notifyDataSetChanged()

                }
            }


        }


        if (qcfailDashboardList.isNotEmpty()) {
            for (i in qcfailDashboardList.indices) {
                if (qcfailDashboardList.get(i).designation.equals(getqcfailhierarchyList.get(
                                position).designation) && qcfailDashboardList.get(i).empid.equals(
                                getqcfailhierarchyList.get(position).empid)
                ) {

                    holder.executiveLayoutBinding.rtocounts.setText(qcfailDashboardList.get(i).rtocount.toString())

                    if(qcfailDashboardList.get(i).rtoamount.toString().isNullOrEmpty()){

                    }else{
                        holder.executiveLayoutBinding.rtovalues.setText((qcfailDashboardList.get(i).rtoamount).toString())
                    }

                    holder.executiveLayoutBinding.rrtocounts.setText(qcfailDashboardList.get(i).rtocount.toString())
                    holder.executiveLayoutBinding.rrtovalues.setText((
                            qcfailDashboardList.get(i).rrtoamount).toString())


                }
            }


        }







        holder.executiveLayoutBinding.generalmanagerArrow.setOnClickListener {

            var isContain: Boolean
            val predicate =
                Predicate { qcfailDashboardList: Getqcfailpendinghistorydashboard.Pendingcount ->
                    qcfailDashboardList.empid.equals(items.empid)
                }


            isContain = qcfailDashboardList.stream().anyMatch(predicate)
            if (isContain) {
                getqcfailhierarchyList[position].setisexecutiveClick(true)

                mCallBack.notify(position, false)
                notifyDataSetChanged()
            } else {
                getqcfailhierarchyList[position].setisexecutiveClick(true)
                items.designation?.let { it1 ->
                    items.empid?.let { it2 ->
                        mCallBack.onClickExecutive(position,
                                it1,
                                it2)
                    }
                }

                notifyDataSetChanged()
            }

        }

        holder.executiveLayoutBinding.closeArrow.setOnClickListener {
            getqcfailhierarchyList[position].setisexecutiveClick(false)
            notifyDataSetChanged()
        }





        if (getqcfailhierarchyList[position].isexecutiveClick) {


            holder.executiveLayoutBinding.closeArrow.visibility = View.VISIBLE
            holder.executiveLayoutBinding.rtoLayout.visibility = View.VISIBLE
            holder.executiveLayoutBinding.generalmanagerArrow.visibility = View.GONE
            holder.executiveLayoutBinding.sitesRecyclerView.visibility = View.VISIBLE
        } else {
            holder.executiveLayoutBinding.closeArrow.visibility = View.GONE
            holder.executiveLayoutBinding.rtoLayout.visibility = View.GONE

            holder.executiveLayoutBinding.generalmanagerArrow.visibility = View.VISIBLE
            holder.executiveLayoutBinding.sitesRecyclerView.visibility = View.GONE

        }


        for (i in qcfailhierarchyList.indices) {
            for (j in qcfailhierarchyList[i].pendingcount?.indices!!) {
                if (qcfailhierarchyList[i].pendingcount?.size == getqcfailhierarchyList.size) {
                    if (qcfailhierarchyList[i].pendingcount!!.equals(getqcfailhierarchyList)){
                    empId=qcfailhierarchyList[i].employeId.toString()
                    }

                }


            }


        }


        if (empId.isNullOrEmpty()) {

        } else {
            if (empId!=items.empid){
            holder.executiveLayoutBinding.executiveLayout.visibility = View.VISIBLE
            if (items.designation?.replace(" ", "").equals("GENERALMANAGER", true)) {
                holder.executiveLayoutBinding.executiveEmpname.setText(items.empid + "\n" + items.designation)


            } else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
                holder.executiveLayoutBinding.executiveEmpname.setText(items.empid + "\n" + items.designation)

                holder.executiveLayoutBinding.logo.setImageResource(R.drawable.qc_manager)
                holder.executiveLayoutBinding.executiveLayout.setBackgroundColor(Color.parseColor(
                        "#636fc1"))
                holder.executiveLayoutBinding.arrowlayout.setBackgroundColor(Color.parseColor("#7e88c7"))
            } else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
                holder.executiveLayoutBinding.executiveEmpname.setText(items.empid + "\n" + items.designation)

                holder.executiveLayoutBinding.logo.setImageResource(R.drawable.qc_executive)
                holder.executiveLayoutBinding.executiveLayout.setBackgroundColor(Color.parseColor(
                        "#f4a841"))
                holder.executiveLayoutBinding.arrowlayout.setBackgroundColor(Color.parseColor("#f6b968"))
            }

            }

        }



    }


    override fun getItemCount(): Int {
        return getqcfailhierarchyList.size
    }

    class ViewHolder(val executiveLayoutBinding: ExecutiveLayoutBinding) :
        RecyclerView.ViewHolder(executiveLayoutBinding.root)
}

