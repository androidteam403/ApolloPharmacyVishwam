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

class QcSiteDialog : DialogFragment() {


    val TAG = "SiteDialog"

    init {
        setCancelable(false)
    }

    var selectsiteIdList = ArrayList<String>()

    lateinit var viewBinding: QcDialogSiteListBinding
    lateinit var abstractDialogClick: NewDialogSiteClickListner
    var regionDataArrayList = ArrayList<UniqueStoreList>()
    lateinit var sitereCyclerView: QcSiteRecycleView

    fun generateParsedData(data: ArrayList<UniqueStoreList>): Bundle {
        return Bundle().apply {
            putSerializable(SITE_DATA, data)
        }
    }

    companion object {
        const val SITE_DATA = "data"
    }


    interface NewDialogSiteClickListner {

        fun onselectMultipleSitesStore(list: ArrayList<String>, position: Int)

        fun selectSite(regionId: QcStoreList.Store)
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
        var viewModel = ViewModelProviders.of(requireActivity())[QcSiteViewModel::class.java]
        viewBinding.closeDialog.visibility = View.VISIBLE
        var storeIdList: List<String>

        viewBinding.searchSiteText.setHint("Search Site Id or Site Name")
        storeIdList = Preferences.getQcSite().split(",")
        var uniqueStoreList = ArrayList<UniqueStoreList>()
        if (storeIdList != null) {
            for (i in storeIdList.indices) {
                if (storeIdList.get(i).isNullOrEmpty()) {

                } else {
                    val items = UniqueStoreList()
                    items.siteid = storeIdList.get(i)


                    uniqueStoreList.add(items)
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
                sitereCyclerView = QcSiteRecycleView(it, object : OnSelectListnerSiteId {
                    override fun onSelected(data: UniqueStoreList) {
//                        abstractDialogClick.selectSite(data)
//                        dismiss()
                    }

                    override fun onClick(list: ArrayList<UniqueStoreList>, position: Int,storeList: ArrayList<UniqueStoreList>) {
                        if (storeList.isNullOrEmpty()) {
                            if (list[position].isClick) {
                                list[position].setisClick(false)

                            } else {
                                list[position].setisClick(true)

                            }
                        } else {
                            Preferences.setQcSite("")
                            if (list[position].isClick ) {

                                list[position].setisClick(false)
                            } else {
                                list[position].setisClick(true)

                            }

                        }
                        sitereCyclerView.notifyDataSetChanged()

                        for (i in regionDataArrayList.indices) {
                            if (regionDataArrayList[i].isClick) {
                                selectsiteIdList.add(regionDataArrayList[i].siteid.toString())
                            }
                        }


                        abstractDialogClick.onselectMultipleSitesStore(selectsiteIdList, position)
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
                },uniqueStoreList)
                viewBinding.fieldRecyclerView.adapter = sitereCyclerView
            }
        })



        viewBinding.closeDialog.setOnClickListener {
            dismiss()
        }
        abstractDialogClick = activity as NewDialogSiteClickListner


        regionDataArrayList =
            (arguments?.getSerializable(SITE_DATA) as? ArrayList<UniqueStoreList>)!!

        viewBinding.searchSite.visibility = View.VISIBLE
        viewModel.qcSiteArrayList(regionDataArrayList)







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
                    viewModel.qcSiteArrayList(regionDataArrayList)
                }
            }
        })
        return viewBinding.root
    }
}

class QcSiteRecycleView(
    var departmentListDto: ArrayList<UniqueStoreList>,
    var onSelectedListner: OnSelectListnerSiteId,
    var storeList: ArrayList<UniqueStoreList>,

    ) :
    SimpleRecyclerView<QcViewItemRowBinding, UniqueStoreList>(
        departmentListDto,
        R.layout.qc_view_item_row
    ) {
    override fun bindItems(
        binding: QcViewItemRowBinding,
        items: UniqueStoreList,
        position: Int,
    ) {

        binding.itemName.text = "${items.siteid}"

        binding.genderParentLayout.setOnClickListener {
            onSelectedListner.onClick(departmentListDto, position,storeList)

        }
        var i: Int = 0

        if (!Preferences.getQcSite().isNullOrEmpty()) {
            if (Preferences.getQcSite().contains(",")){
                for (j in departmentListDto.indices) {

                    if (Preferences.getQcSite().split(",").filter { it.contains(departmentListDto.get(i).siteid!!) }.size>0) {

                        departmentListDto[j].setisClick(true)
                    } else {

                        departmentListDto[j].setisClick(false)
                    }

                }
            }

            else{
                for (j in departmentListDto.indices) {

                    if (Preferences.getQcSite().contains(departmentListDto.get(j).siteid!!)
                    ) {

                        departmentListDto[j].setisClick(true)
                    } else {

                        departmentListDto[j].setisClick(false)
                    }
                }
            }

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

interface OnSelectListnerSiteId {
    fun onSelected(data: UniqueStoreList)
    fun onClick(list: ArrayList<UniqueStoreList>, position: Int,storeList: ArrayList<UniqueStoreList>,
    )

}

interface clickListenerSite {
    fun onClick(list: ArrayList<QcStoreList.Store>, position: Int)
}

