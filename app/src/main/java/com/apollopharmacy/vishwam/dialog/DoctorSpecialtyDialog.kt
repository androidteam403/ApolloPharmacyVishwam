package com.apollopharmacy.vishwam.dialog

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
import androidx.lifecycle.ViewModelProviders
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.DialogDoctorSpecialityBinding
import com.apollopharmacy.vishwam.databinding.DialogItemTypeBinding
import com.apollopharmacy.vishwam.databinding.ViewListItemBinding
import com.apollopharmacy.vishwam.dialog.model.DoctorSpecialtyViewModel
import com.apollopharmacy.vishwam.dialog.model.ItemViewModel
import com.apollopharmacy.vishwam.util.Utils

class DoctorSpecialtyDialog : DialogFragment() {
    val TAG = "DoctorSpecialty"

    init {
        setCancelable(false)
    }

    val specialities = ArrayList<String>()
    lateinit var abstractDialogClick: AbstractDialogSpecialityClickListner
    lateinit var viewBinding: DialogDoctorSpecialityBinding
    lateinit var doctorSpecialtyRecyclerView: DoctorSpecialtyRecyclerView

    interface AbstractDialogSpecialityClickListner {
        fun selectSpeciality(item: String)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
        viewBinding = DialogDoctorSpecialityBinding.inflate(inflater, container, false)
        var viewModel = ViewModelProviders.of(requireActivity())[DoctorSpecialtyViewModel::class.java]
        viewBinding.closeDialog.visibility = View.VISIBLE
        viewBinding.closeDialog.setOnClickListener {
            specialities.clear()
            dismiss()
        }
        abstractDialogClick = parentFragment as AbstractDialogSpecialityClickListner
        specialities.clear()

        viewModel.specialtyArrayList(specialities)
        viewModel.fixedArrayList.observe(viewLifecycleOwner, {
            if (it.size == 0) {
                viewBinding.siteNotAvailable.visibility = View.VISIBLE
                viewBinding.doctorSpecialtyRecyclerView.visibility = View.GONE
            } else {
                viewBinding.siteNotAvailable.visibility = View.GONE
                viewBinding.doctorSpecialtyRecyclerView.visibility = View.VISIBLE
                doctorSpecialtyRecyclerView = DoctorSpecialtyRecyclerView(it, object : OnSelectedListnerDoctorSpeciality {
                    override fun onSelected(data: String) {
                        abstractDialogClick.selectSpeciality(data)
                        dismiss()
                    }
                })
                viewBinding.doctorSpecialtyRecyclerView.adapter = doctorSpecialtyRecyclerView
            }
        })

        specialities.add("orthopedics")
        specialities.add("pathology")
        specialities.add("dermatology")

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
                    viewModel.filterSpecialty(textChanged)
                } else {
                    viewModel.specialtyArrayList(specialities)
                }
            }

        })

        return viewBinding.root
    }
}

class DoctorSpecialtyRecyclerView(
    data: ArrayList<String>,
    var onSelectedListner: OnSelectedListnerDoctorSpeciality
) :
    SimpleRecyclerView<ViewListItemBinding, String>(
        data,
        R.layout.view_list_item
    ) {
    override fun bindItems(binding: ViewListItemBinding, items: String, position: Int) {
        binding.itemName.text = items

        binding.root.setOnClickListener {
            onSelectedListner.onSelected(items)
        }
    }
}

interface OnSelectedListnerDoctorSpeciality {
    fun onSelected(data: String)
}