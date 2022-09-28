package com.apollopharmacy.vishwam.ui.home.qcfail.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.qc.QcFilterData
import com.apollopharmacy.vishwam.databinding.FilterLayoutBinding
import com.apollopharmacy.vishwam.databinding.MainFiltermenuLayoutBinding
import com.apollopharmacy.vishwam.databinding.SubmenuFilterAdapterBinding
import com.apollopharmacy.vishwam.dialog.DatePickerDialog
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.GstDialog
import com.apollopharmacy.vishwam.util.Utils
import com.apollopharmacy.vishwam.util.Utlis
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QcFilterFragment : BottomSheetDialogFragment(), MenuFilterAdapter.ClickMenuFilter,
    DatePickerDialog.DateSelected {

    companion object {
        const val KEY_PENDING_DATA_QC = "qcpendingdata"
    }

    private lateinit var filterLayoutBinding: FilterLayoutBinding
    private lateinit var pendingList: ArrayList<String>
    private var arrayList = ArrayList<QcFilterData>()
    private lateinit var filterData: String
    private lateinit var listener: QcFilterClicked
    private var toDate: String = ""
    private var clickedTag: Int? = null

    interface QcFilterClicked {
        fun clickedApply(
            selectedData: String,
            data: ArrayList<String>,
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
            DataBindingUtil.inflate(inflater, R.layout.filter_layout, container, false)
        pendingList =(arguments?.getSerializable(KEY_PENDING_DATA_QC)) as ArrayList<String>
        return filterLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var viewModel = ViewModelProvider(this).get(QcFilterViewModel::class.java)
        viewModel.setFilterMenu()
        listener = targetFragment as QcFilterClicked
        viewModel.listFilterData.observe(viewLifecycleOwner, { filterList ->
            filterLayoutBinding.itemRecyclerView.adapter = MenuFilterAdapter(filterList, this)
        })
        filterLayoutBinding.applybutoon.setOnClickListener {
            if (::filterData.isInitialized) {
                listener!!.clickedApply(filterData, pendingList, clickedTag!!, toDate!!)
                dismiss()
            }
        }
        filterLayoutBinding.fromdate.setOnClickListener {
            openDatePicker("from")
        }
        filterLayoutBinding.toDate.setOnClickListener {
            openDatePicker("to")
        }
        filterLayoutBinding.cancel.setOnClickListener {
            dismiss()
        }
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
            filterLayoutBinding.dateLayout.visibility = View.VISIBLE
            filterLayoutBinding.searchRecyclerView.visibility = View.GONE
            clickedTag = 0
        } else if (clickedMenu == 1) {
            menuWiseSubMenu(pendingList, clickedMenu)
            filterLayoutBinding.searchRecyclerView.visibility = View.VISIBLE
            filterLayoutBinding.dateLayout.visibility = View.GONE
            clickedTag = 1
        } else if (clickedMenu == 2) {
            menuWiseSubMenu(pendingList, clickedMenu)
            filterLayoutBinding.searchRecyclerView.visibility = View.VISIBLE
            filterLayoutBinding.dateLayout.visibility = View.GONE
            clickedTag = 2
        }
    }

    private fun menuWiseSubMenu(
        pending: ArrayList<String>,
        clickedMenu: Int,
    ) {
        arrayList.clear()
        val hs = HashSet<QcFilterData>()
        if (clickedMenu == 1) {
            for (i in pending.indices) {
                if (!pending[i].isNullOrEmpty())
                    arrayList.add(QcFilterData(pending[i]))
            }
        } else {
            for (i in pending.indices) {
                if (!pending[i].isNullOrEmpty())
                    arrayList.add(QcFilterData(pending[i]))
            }
        }
        hs.addAll(arrayList)
        arrayList.clear()
        arrayList.addAll(hs)
        filterLayoutBinding.searchRecyclerView.adapter =
            SubMenuAdapter(arrayList, object : SubMenuAdapter.UserSelected {
                override fun clickedItem(subMenu: String) {
                    Utils.printMessage("subMenuClicked", subMenu)
                    filterData = subMenu
                }
            })
    }

    override fun selectDateFrom(dateSelected: String, showingDate: String) {
        filterLayoutBinding.fromdate.setText(Utlis.convertDate(dateSelected))
        filterData = dateSelected
    }

    override fun selectedDateTo(dateSelected: String, showingDate: String) {
        filterLayoutBinding.toDate.setText(Utlis.convertDate(dateSelected))
        toDate = dateSelected
    }
}

class MenuFilterAdapter(filterDataList: List<QcFilterData>, var listner: ClickMenuFilter) :
    SimpleRecyclerView<MainFiltermenuLayoutBinding, QcFilterData>(
        filterDataList,
        R.layout.main_filtermenu_layout
    ) {
    var menuFilter = 0
    override fun bindItems(binding: MainFiltermenuLayoutBinding, items: QcFilterData, position: Int) {
        binding.menuTitle.text = items.MenuTitle
        if (menuFilter == position) {
            binding.root.setBackgroundResource(R.color.faded_click)
            binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.white))
            listner.clickMenu(position)
        } else {
            binding.root.setBackgroundResource(R.color.faded_background)
            binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.black))
        }
        binding.root.setOnClickListener {
            if (menuFilter == position) {
                binding.root.setBackgroundResource(R.color.faded_click)
                binding.menuTitle.setTextColor(binding.root.resources.getColor(R.color.white))
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

class SubMenuAdapter(subFilterData: List<QcFilterData>, var listner: UserSelected) :
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