package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DialogSiteAttendenceBinding
import com.apollopharmacy.vishwam.databinding.ViewItemBinding
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView
import com.apollopharmacy.vishwam.dialog.model.DoctorListViewModel
import com.apollopharmacy.vishwam.ui.home.adrenalin.attendance.livedata.DoctorListResponse
import com.apollopharmacy.vishwam.util.Utils

class DoctorListDialog : DialogFragment() {


    val TAG = "SiteDialog"

    init {
        setCancelable(false)
    }

    lateinit var viewBinding: DialogSiteAttendenceBinding
    lateinit var abstractDialogClick: NewDialogSiteClickListner
    var siteDataArrayList = ArrayList<DoctorListResponse.Doctor>()
    lateinit var sitereCyclerView: DoctorListRecyclerView

    companion object {
        const val KEY_DATA = "data"
    }

    fun generateParsedData(data: ArrayList<DoctorListResponse.Doctor>): Bundle {
        return Bundle().apply {
            putSerializable(KEY_DATA, data)
        }
    }

    interface NewDialogSiteClickListner {
        fun selectSite(departmentDto: DoctorListResponse.Doctor)
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
        viewBinding = DialogSiteAttendenceBinding.inflate(inflater, container, false)
        viewBinding.siteNotAvailable.setText("Doctor Not Available")
        viewBinding.textHead.setText("Select Doctor")

        viewBinding.searchSite.setHint("Search Doctor Name")
        var viewModel = ViewModelProviders.of(requireActivity())[DoctorListViewModel::class.java]
        viewBinding.closeDialog.visibility = View.VISIBLE

        viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.fieldRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                sitereCyclerView = DoctorListRecyclerView(it, object : OnSelecteDoctor {
                    override fun onCLickSite(data: DoctorListResponse.Doctor) {
                        abstractDialogClick.selectSite(data)
                        dismiss()
                    }


                })
                viewBinding.fieldRecyclerView.adapter = sitereCyclerView
            }
        })


        viewBinding.closeDialog.setOnClickListener {
            siteDataArrayList.clear()
            dismiss()
        }

        abstractDialogClick = parentFragment as NewDialogSiteClickListner
        siteDataArrayList =
            arguments?.getSerializable(KEY_DATA) as ArrayList<DoctorListResponse.Doctor>
        viewBinding.searchSite.visibility = View.VISIBLE
        viewModel.siteArrayList(siteDataArrayList)







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
                    viewModel.siteArrayList(siteDataArrayList)
                }
            }
        })
        return viewBinding.root
    }
}

class DoctorListRecyclerView(
    departmentListDto: ArrayList<DoctorListResponse.Doctor>,
    var onSelectedListner: OnSelecteDoctor,
) :
    SimpleRecyclerView<ViewItemBinding, DoctorListResponse.Doctor>(
        departmentListDto,
        R.layout.view_item
    ) {
    override fun bindItems(
        binding: ViewItemBinding,
        items: DoctorListResponse.Doctor,
        position: Int,
    ) {
        binding.itemName.text = "${items.doctorname}"
        binding.root.setOnClickListener {
            onSelectedListner.onCLickSite(items)
        }
    }
}

interface OnSelecteDoctor {
    fun onCLickSite(data: DoctorListResponse.Doctor)
}

