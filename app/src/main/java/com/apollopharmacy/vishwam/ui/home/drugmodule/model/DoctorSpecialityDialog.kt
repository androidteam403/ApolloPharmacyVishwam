package com.apollopharmacy.vishwam.ui.home.drugmodule.model

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.model.cms.ReasonmasterV2Response
import com.apollopharmacy.vishwam.databinding.DialogCustomBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.CustomRecyclerViews
import com.apollopharmacy.vishwam.dialog.OnSelectListner
import com.apollopharmacy.vishwam.dialog.SimpleRecyclerView

class DoctorSpecialityDialog: DialogFragment() {



        lateinit var viewBinding: DialogCustomBinding
        lateinit var abstractDialogClick: SelectDoctorDialogListner
        var siteDataArrayList= ArrayList<ItemTypeDropDownResponse.Rows>()
        init {
            setCancelable(false)
        }

        companion object {
            const val KEY_DATA = "data"
        }

        fun generateParsedData(data: ArrayList<ItemTypeDropDownResponse.Rows>): Bundle {
            return Bundle().apply {
                putSerializable(KEY_DATA, data)
            }
        }


        fun generateParsedDatafromreasons(data: ArrayList<String>): Bundle {
            return Bundle().apply {
                putSerializable(KEY_DATA, data)
            }
        }

        interface SelectDoctorDialogListner {
            fun selectDoctorSpecialiity(doctorSpeciality: String)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            viewBinding = DialogCustomBinding.inflate(inflater, container, false)
            viewBinding.textHead.text ="Select Doctor's Speciality"

            viewBinding.closeDialog.setOnClickListener { dismiss() }

            viewBinding.searchSite.visibility = View.VISIBLE
            var viewModel = ViewModelProviders.of(requireActivity())[DoctorSpecialityViewModel::class.java]
            viewBinding.searchSiteText.setHint("Search Doctor's Speciality")
            viewBinding.searchSiteText.inputType = InputType.TYPE_CLASS_TEXT
            siteDataArrayList =
                arguments?.getSerializable(KEY_DATA) as ArrayList<ItemTypeDropDownResponse.Rows>

            viewModel.siteArrayList(siteDataArrayList)

            viewModel.fixedArrayList.observe(viewLifecycleOwner, Observer {
                if (it.size == 0) {
                    viewBinding.siteNotAvailable.text = "Doctor's Speciality not available"
                    viewBinding.siteNotAvailable.visibility = View.VISIBLE
                    viewBinding.fieldRecyclerView.visibility = View.GONE
                } else {
                    viewBinding.siteNotAvailable.visibility = View.GONE
                    viewBinding.fieldRecyclerView.visibility = View.VISIBLE
                    viewBinding.fieldRecyclerView.adapter =
                        DoctorSpecialityRecycleView(it, object : OnSelectDoctorSpecialityListner {
                            override fun onSelected(data:  String) {
                                abstractDialogClick = parentFragment as SelectDoctorDialogListner

                                abstractDialogClick.selectDoctorSpecialiity(data)
                                dismiss()
                            }
                        })
                }
            })

            viewBinding.searchSiteText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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

    class DoctorSpecialityRecycleView(
        departmentListDto: ArrayList<ItemTypeDropDownResponse.Rows>,
        var onSelectedListner: OnSelectDoctorSpecialityListner,
    ) :
        SimpleRecyclerView<ViewListItemBinding, ItemTypeDropDownResponse.Rows>(
            departmentListDto,
            R.layout.view_list_item
        ) {
        override fun bindItems(
            binding: ViewListItemBinding,
            items: ItemTypeDropDownResponse.Rows,
            position: Int,
        ) {
            binding.itemName.text = items.name

            binding.root.setOnClickListener {
                onSelectedListner.onSelected(items.name)
            }
        }
    }

    interface OnSelectDoctorSpecialityListner {
        fun onSelected(data: String)
    }



