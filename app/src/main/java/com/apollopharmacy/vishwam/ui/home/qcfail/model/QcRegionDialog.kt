package com.apollopharmacy.vishwam.ui.home.qcfail.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.databinding.QcDialogSiteListBinding
import com.apollopharmacy.vishwam.databinding.QcViewItemRowBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.util.Utils

class QcRegionDialog : DialogFragment() {


    val TAG = "RegionDialog"

    init {
        setCancelable(false)
    }

    var selectsiteIdList = ArrayList<String>()

    lateinit var viewBinding: QcDialogSiteListBinding
    lateinit var abstractDialogClick: NewDialogSiteClickListner
    var regionDataArrayList = ArrayList<UniqueRegionList>()
    lateinit var sitereCyclerView: RegionRecyclerView


    fun generateParsedData(data: ArrayList<UniqueRegionList>): Bundle {
        return Bundle().apply {
            putSerializable(REGION_DATA, data)
        }
    }

    companion object {
        const val REGION_DATA = "data"
    }


    interface NewDialogSiteClickListner {

        fun onselectMultipleSites(list: ArrayList<String>, position: Int)

        fun selectSite(regionId: QcRegionList.Store)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
        viewBinding = QcDialogSiteListBinding.inflate(inflater, container, false)
        var viewModel = ViewModelProviders.of(requireActivity())[QcRegionViewModel::class.java]
        var regionIdList: List<String>
        var uniqueRegionList = ArrayList<UniqueRegionList>()

        var regionList = ArrayList<String>()
        var string = String()
        viewBinding.closeDialog.visibility = View.VISIBLE
        viewBinding.textHead.setText("Select Region Id")
        viewBinding.searchSiteText.setHint("Search Region Id or Region name")
        viewBinding.siteNotAvailable.setText("Region Not Available")
        regionIdList = Preferences.getQcRegion().split(",")

        if (regionIdList != null) {
            for (i in regionIdList.indices) {
                if (regionIdList.get(i).isNullOrEmpty()) {

                } else {
                    val items = UniqueRegionList()
                    items.siteid = regionIdList.get(i)


                    uniqueRegionList.add(items)
                }


            }

        }

        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                sitereCyclerView = RegionRecyclerView(it, object : OnSelectListnerSite {
                    override fun onSelected(data: UniqueRegionList) {
//                        abstractDialogClick.selectSite(data)
//                        dismiss()
                    }


                    override fun onClick(
                        list: ArrayList<UniqueRegionList>,
                        position: Int,
                        regionList: ArrayList<UniqueRegionList>,
                    ) {

                        if (regionList.isNullOrEmpty()) {
                            if (list[position].isClick) {
                                list[position].setisClick(false)

                            } else {
                                list[position].setisClick(true)

                            }
                        } else {
                            Preferences.setQcRegion("")
                            if (list[position].isClick || regionList[position].isClick) {

                                list[position].setisClick(false)
                                regionList[position].setisClick(false)
                            } else {
                                list[position].setisClick(true)
                                regionList[position].setisClick(true)

                            }

                        }


//                        if (!Preferences.getQcRegion().isNullOrEmpty()) {
//                            for (i in regionList.indices) {
//                                if (regionList.get(i).equals(list[position].siteid)) {
//                                    list[position].setisClick(true)
//                                } else {
//                                    list[position].setisClick(false)
//
//                                }
//                            }
//                        }
                        sitereCyclerView.notifyDataSetChanged()

                        for (i in regionDataArrayList.indices) {
                            if (regionDataArrayList[i].isClick) {
                                selectsiteIdList.add(regionDataArrayList[i].siteid.toString())
                            }
                        }


                        abstractDialogClick.onselectMultipleSites(selectsiteIdList, position)
                        dismiss()

                        viewBinding.apply.setOnClickListener {
                            if (selectsiteIdList.isNullOrEmpty()) {
                                Toast.makeText(context,
                                    "No Data Found To Apply",
                                    Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                dismiss()
                            }
                        }

                    }
                }, uniqueRegionList)
                viewBinding.fieldRecyclerView.adapter = sitereCyclerView
            }
        })



        viewBinding.closeDialog.setOnClickListener {


            dismiss()
        }
        abstractDialogClick = activity as NewDialogSiteClickListner


        regionDataArrayList =
            (arguments?.getSerializable(REGION_DATA) as? ArrayList<UniqueRegionList>)!!

        viewBinding.searchSite.visibility = View.VISIBLE
        viewModel.qcRegionArrayList(regionDataArrayList)







        viewBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Utils.printMessage(TAG, "Before Text Changed :: " + s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Utils.printMessage(TAG, "On Text Changed" + s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                var textChanged = s.toString().trim()
                if (s.toString().length > 1) {
                    viewModel.filterDataBySiteId(textChanged)
                } else {
                    viewModel.qcRegionArrayList(regionDataArrayList)
                }
            }
        })
        return viewBinding.root
    }
}

class RegionRecyclerView(
    var departmentListDto: ArrayList<UniqueRegionList>,
    var onSelectedListner: OnSelectListnerSite,
    var regionList: ArrayList<UniqueRegionList>,
) :
    SimpleRecyclerView<QcViewItemRowBinding, UniqueRegionList>(
        departmentListDto,
        R.layout.qc_view_item_row
    ) {
    override fun bindItems(
        binding: QcViewItemRowBinding,
        items: UniqueRegionList,
        position: Int,
    ) {
        binding.itemName.text = "${items.siteid}"

        binding.genderParentLayout.setOnClickListener {
            onSelectedListner.onClick(departmentListDto, position, regionList)

        }
        var i: Int = 0

        if (!Preferences.getQcRegion().isNullOrEmpty()) {

            while (i < regionList.size) {
                if (regionList.get(i).siteid?.replace(" ", "")
                        .equals(departmentListDto[i].siteid)
                ) {
                    departmentListDto[i].setisClick(true)
                    i++
                } else {
                    departmentListDto[i].setisClick(false)
                    i++

                }
            }
//                    if (names[i].isItemChecked) {
//                        names.removeAt(i)
//                        i = 0
//                    } else {
//                        i++
//                    }
//                }

//            for (i in regionList.indices) {
//                if (regionList.get(i).equals(departmentListDto[position].siteid)) {
//                    departmentListDto[position].setisClick(true)
//                } else {
//                    departmentListDto[position].setisClick(false)
//
//                }
//            }
        }

        if (departmentListDto[position].isClick) {
            binding.checkBox.setImageResource(R.drawable.qcright)
        } else {
            binding.checkBox.setImageResource(R.drawable.qc_checkbox)
        }

//        binding.root.setOnClickListener {
//            items.setisClick(true)
//            onSelectedListner.onSelected(items)
//        }
    }
}

interface OnSelectListnerSite {
    fun onSelected(data: UniqueRegionList)
    fun onClick(
        list: ArrayList<UniqueRegionList>,
        position: Int,
        regionList: ArrayList<UniqueRegionList>,
    )

}

interface clickListener {
    fun onClick(list: ArrayList<QcRegionList.Store>, position: Int)
}

