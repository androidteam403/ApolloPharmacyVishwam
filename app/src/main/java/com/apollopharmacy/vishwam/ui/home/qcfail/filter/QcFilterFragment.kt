package com.apollopharmacy.vishwam.ui.home.qcfail.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.qc.QcFilterData
import com.apollopharmacy.vishwam.data.model.qc.QcStoreFilterData
import com.apollopharmacy.vishwam.databinding.MainFiltermenuLayoutBinding
import com.apollopharmacy.vishwam.databinding.QcFilterLayoutBinding
import com.apollopharmacy.vishwam.databinding.SubmenuFilterAdapterBinding
import com.apollopharmacy.vishwam.dialog.DatePickerDialog
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcRegionList
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcStoreList
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class QcFilterFragment : BottomSheetDialogFragment(), MenuFilterAdapter.ClickMenuFilter,
    DatePickerDialog.DateSelected {

    companion object {
        const val KEY_PENDING_DATA_QC = "qcpendingdata"
        const val KEY_REGION_DATA_QC = "qcdata"

    }

    private lateinit var filterLayoutBinding: QcFilterLayoutBinding
    private lateinit var pendingList: ArrayList<QcStoreList.Store>
    private lateinit var  qcregionIdList: ArrayList<QcRegionList.Store>
    var regionlist: ArrayList<QcRegionList.Store>? = null
    var regionWisetList = ArrayList<QcRegionList.Store>()

    val fab: FloatingActionButton? = activity?.findViewById(R.id.filter)
    private var arrayList = ArrayList<QcFilterData>()
    private var regionList = ArrayList<QcFilterData>()
    private var arrayStoreList = ArrayList<QcStoreFilterData>()

    private lateinit var filterData: String
    private lateinit var listener: QcFilterClicked
    private var toDate: String = ""
    private var clickedTag: Int? = null

//    fun generateParsedData(data: ArrayList<QcRegionList.Store>): Bundle {
//
//        return Bundle().apply {
//            putSerializable(KEY_REGION_DATA_QC, data)
//            regionlist =data
//            for (i in regionlist!!) {
//                val items = QcRegionList.Store()
//                items.sitename = i.sitename
//                items.siteid=i.siteid
//                regionWisetList.add(items)
//
//
//
//            }
//
//
//        }
//    }

    interface QcFilterClicked {
        fun clickedApply(
            selectedData: String,
            data: ArrayList<QcStoreList.Store>,
            regiondata: ArrayList<QcRegionList.Store>,

            tag: Int,
            toDate: String,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        filterLayoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.qc_filter_layout, container, false)
//        generateParsedData(regionWisetList)
//        regionList=

        if ((arguments?.getSerializable(KEY_REGION_DATA_QC))==null){

        }else{
            qcregionIdList = (arguments?.getSerializable(KEY_REGION_DATA_QC)) as ArrayList<QcRegionList.Store>


        }
        pendingList =
            (arguments?.getSerializable(KEY_PENDING_DATA_QC)) as ArrayList<QcStoreList.Store>


        return filterLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var viewModel = ViewModelProvider(this).get(QcFilterViewModel::class.java)

        viewModel.setFilterMenu()
        listener = targetFragment as QcFilterClicked
//
//        viewModel.listFilterData.observe(viewLifecycleOwner, { filterList ->
//            filterLayoutBinding.itemRecyclerView.adapter = MenuFilterAdapter(filterList, this)
//        })
//        filterLayoutBinding.applybutoon.setOnClickListener {
//            if (::filterData.isInitialized) {
//                regionlist?.let { it1 ->
//                    listener!!.clickedApply(filterData, pendingList,
//                        it1, clickedTag!!, toDate!!)
//                }
//            }
//            dismiss()
//        }
//
//        filterLayoutBinding.fromdate.setOnClickListener {
//            openDatePicker("from")
//        }
//        filterLayoutBinding.toDate.setOnClickListener {
//            openDatePicker("to")
//        }
//        filterLayoutBinding.cancel.setOnClickListener {
//            dismiss()
//        }
    }

    private fun openDatePicker(tag: String) {
        var bundle = Bundle()
        bundle.putString("tag", tag)
        DatePickerDialog().let {
            it.setTargetFragment(this, 1)
            it.arguments = bundle
            it.show(requireFragmentManager().beginTransaction(), "")
        }
    }


    /**
     * if 0 filter by date
     * if 1 filter by store
     * if 2 filter by region
     */
    override fun clickMenu(clickedMenu: Int) {


        if (clickedMenu == 0) {
//            filterLayoutBinding.dateLayout.visibility = View.VISIBLE
//            filterLayoutBinding.storeIdsRecyclerView.visibility = View.GONE
            clickedTag = 0
        } else if (clickedMenu == 1) {
            menuWiseSubMenu(pendingList, qcregionIdList, clickedMenu)
            clickedTag = 1
        } else if (clickedMenu == 2) {
            menuWiseSubMenu(pendingList, qcregionIdList, clickedMenu)
            clickedTag = 2
        }
    }

    private fun menuWiseSubMenu(
        pending: ArrayList<QcStoreList.Store>,
        region: ArrayList<QcRegionList.Store>,
        clickedMenu: Int,

        ) {
        arrayList.clear()
        val hs = HashSet<QcFilterData>()
        if (clickedMenu == 1) {
            for (i in pending.indices) {
                if (pending.isNullOrEmpty())
                    pending[i].siteid+" - "+pending[i].sitename

                arrayList.add(QcFilterData(pending[i].siteid.toString() ))
            }
        } else {
            for (j in region.indices) {
                if (region.isNullOrEmpty())
                    region[j].siteid
                arrayList.add(QcFilterData(region[j].siteid.toString()))

            }
        }
        hs.addAll(arrayList)
        arrayList.clear()
        arrayList.addAll(hs)


//        filterLayoutBinding.storeIdsRecyclerView.addOnScrollListener(object :
//            RecyclerView.OnScrollListener() {
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dy < 0) {
//                    if (fab != null) {
//                        fab.show()
//                    }
//                } else if (dy > 0) {
//                    if (fab != null) {
//                        fab.hide()
//                    }
//                }
//            }
//
//
//        })

//
//filterLayoutBinding.storeIdsRecyclerView.removeAllViews()
//
//        val mLayoutManager1: RecyclerView.LayoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        filterLayoutBinding.storeIdsRecyclerView.layoutManager = mLayoutManager1
//
//        filterLayoutBinding.storeIdsRecyclerView.adapter =
//            SubMenuAdapter(arrayList, object : SubMenuAdapter.UserSelected {
//                override fun clickedItem(subMenu: String) {
//                    Utils.printMessage("subMenuClicked", subMenu)
//                    filterData = subMenu
//                }
//            }, pendingList)
//        filterLayoutBinding.storeIdsRecyclerView.visibility = View.VISIBLE
//        filterLayoutBinding.dateLayout.visibility = View.GONE

    }

    override fun selectDateFrom(dateSelected: String, showingDate: String) {
//        filterLayoutBinding.fromdate.setText(dateSelected)
        filterData = dateSelected
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
//        filterLayoutBinding.toDate.setText(dateSelected)
        toDate = dateSelected
    }
}

class MenuFilterAdapter(filterDataList: List<QcFilterData>, var listner: ClickMenuFilter) :
    SimpleRecyclerView<MainFiltermenuLayoutBinding, QcFilterData>(
        filterDataList,
        R.layout.main_filtermenu_layout
    ) {
    var menuFilter = 0
    override fun bindItems(
        binding: MainFiltermenuLayoutBinding,
        items: QcFilterData,
        position: Int,
    ) {
        binding.menuTitle.text = items.MenuTitle
        if (menuFilter == position) {
            binding.root.setBackgroundResource(R.color.white)
            binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.black))
            listner.clickMenu(position)
        } else {
            binding.root.setBackgroundResource(R.color.qc_color)
            binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.black))
        }
        binding.root.setOnClickListener {
            if (menuFilter == position) {
                binding.root.setBackgroundResource(R.color.white)
                binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.black))
            } else {
                binding.root.setBackgroundResource(R.color.faded_click)
                binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.black))
                listner.clickMenu(position)
                menuFilter = position
                notifyDataSetChanged()
            }
        }
    }

    interface ClickMenuFilter {
        fun clickMenu(clickedMenu: Int)
    }
}

class SubMenuAdapter(
    subFilterData: List<QcFilterData>,
    var listner: UserSelected,
    var storeList: ArrayList<QcStoreList.Store>,
) :
    SimpleRecyclerView<SubmenuFilterAdapterBinding, QcFilterData>(
        subFilterData,
        R.layout.submenu_filter_adapter
    ) {
    var menuFilter = -1
    override fun bindItems(
        binding: SubmenuFilterAdapterBinding,
        items: QcFilterData,
        position: Int,
    ) {
        binding.menuTitle.text = items.MenuTitle


        if (menuFilter == position) {
            binding.root.setBackgroundResource(R.color.faded_click)
            binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.white))
        } else {
            binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.black))
            binding.root.setBackgroundResource(R.color.white)
        }
        binding.root.setOnClickListener {
            if (menuFilter == position) {
                binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.white))
            } else {
                binding.root.setBackgroundResource(R.color.faded_click)
                binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.white))
                listner.clickedItem(items.MenuTitle)
                menuFilter = position
                notifyDataSetChanged()
            }
        }
    }



    interface UserSelected {
        fun clickedItem(subMenu: String)
    }
}