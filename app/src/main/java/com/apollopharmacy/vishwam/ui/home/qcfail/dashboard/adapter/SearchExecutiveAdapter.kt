package com.apollopharmacy.vishwam.ui.home.qcfail.dashboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ExecutiveLayoutBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistorydashboard
import com.apollopharmacy.vishwam.ui.home.qcfail.model.Getqcfailpendinghistoryforhierarchy
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcDashBoardCallback
import java.text.NumberFormat
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class SearchExecutiveAdapter(
    val mContext: Context,
    val mCallBack: QcDashBoardCallback,
    var qcfailDashboardList: ArrayList<Getqcfailpendinghistorydashboard.Pendingcount>,
    val designation: String,
    var qcfailhierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy>,

    var getqcfailhierarchyList: ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>,

    ) :
    RecyclerView.Adapter<SearchExecutiveAdapter.ViewHolder>() {
    var rtoSitesAdapter: RtoSitesAdapter? = null
    var dashBoardList = ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>()


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


                    dashBoardList= item.pendingcount!!.stream().filter { x -> x.rtocount !=0 || x.rrtocount!=0} // or `Objects::nonNull`
                        .collect(Collectors.toList()) as ArrayList<Getqcfailpendinghistoryforhierarchy.Pendingcount>

                    dashBoardList.sortWith { o1: Getqcfailpendinghistoryforhierarchy.Pendingcount, o2: Getqcfailpendinghistoryforhierarchy.Pendingcount ->
                        o2.rtoamount!!.compareTo(
                            o1.rtoamount!!
                        )
                    }
                    rtoSitesAdapter = RtoSitesAdapter(mContext,
                        dashBoardList, mCallBack)
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

                    holder.executiveLayoutBinding.rtCount.setText(qcfailDashboardList.get(i).rtocount.toString())
                    holder.executiveLayoutBinding.sumOfRtValues.setText(NumberFormat.getNumberInstance(Locale.US).format(qcfailDashboardList.get(i).rtoamount!! + qcfailDashboardList.get(i).rrtoamount!!).toString())
                    if(qcfailDashboardList.get(i).rtoamount.toString().isNullOrEmpty()){

                    }else{

                        holder.executiveLayoutBinding.rtovalue.setText( NumberFormat.getNumberInstance(Locale.US).format(qcfailDashboardList.get(i).rtoamount).toString())
                    }

                    holder.executiveLayoutBinding.rrtoCount.setText(qcfailDashboardList.get(i).rrtocount.toString())
                    holder.executiveLayoutBinding.rrtovalue.setText( NumberFormat.getNumberInstance(Locale.US).format(qcfailDashboardList.get(i).rrtoamount).toString())



                }
            }


        }







        holder.executiveLayoutBinding.generalmanagerArrow.setOnClickListener {
            mCallBack.onClickExecutiveHierarchy(position,getqcfailhierarchyList)
//
//            for ( i in getqcfailhierarchyList.indices){
//                if (getqcfailhierarchyList[position]==getqcfailhierarchyList[i]){
//                    getqcfailhierarchyList[position].setsearchexecutiveClick(true)
//
//                }else{
//                    getqcfailhierarchyList[i].setsearchexecutiveClick(false)
//
//                }
//            }
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
//            if (isContain) {
////                getqcfailhierarchyList[position].setisexecutiveClick(true)
//
//                mCallBack.notify(position, false)
//                notifyDataSetChanged()
//            } else {
////                getqcfailhierarchyList[position].setisexecutiveClick(true)
////                items.designation?.let { it1 ->
////                    items.empid?.let { it2 ->
////                        mCallBack.onClickExecutive(position,
////                            it1,
////                            it2)
////                    }
////                }
//
//                notifyDataSetChanged()
//            }

        }

        holder.executiveLayoutBinding.closeArrow.setOnClickListener {
            mCallBack.onClickExecutiveHierarchy(position,getqcfailhierarchyList)
//            getqcfailhierarchyList[position].setsearchexecutiveClick(false)
//            notifyDataSetChanged()
        }





        if (getqcfailhierarchyList[position].searchexecutiveClick) {


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
                    holder.executiveLayoutBinding.gmEmpname.setText(items.empid!!.split("-").get(0) +" (" +items.empid!!.split("-").get(1)+")"+ "\n"  + items.designation)

                    holder.executiveLayoutBinding.rtoLayout.setBackgroundColor(Color.parseColor(
                        "#00acae"))
                    holder.executiveLayoutBinding.executiveLayout.setBackgroundColor(Color.parseColor(
                        "#00acae"))
                } else if (items.designation?.replace(" ", "").equals("MANAGER", true)) {
                    holder.executiveLayoutBinding.gmEmpname.setText(items.empid!!.split("-").get(0) +" (" +items.empid!!.split("-").get(1)+")"+ "\n"  + items.designation)
                    holder.executiveLayoutBinding.rtoLayout.setBackgroundColor(Color.parseColor(
                        "#d48a2b"))
                    holder.executiveLayoutBinding.executiveLayout.setBackgroundColor(Color.parseColor(
                        "#d48a2b"))
                } else if (items.designation?.replace(" ", "").equals("EXECUTIVE", true)) {
                    holder.executiveLayoutBinding.gmEmpname.setText(items.empid!!.split("-").get(0) +" (" +items.empid!!.split("-").get(1)+")"+ "\n"  + items.designation)
                    holder.executiveLayoutBinding.rtoLayout.setBackgroundColor(Color.parseColor(
                        "#606db3"))
                    holder.executiveLayoutBinding.executiveLayout.setBackgroundColor(Color.parseColor(
                        "#606db3"))
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

