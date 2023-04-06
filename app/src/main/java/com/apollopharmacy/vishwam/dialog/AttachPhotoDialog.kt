package com.apollopharmacy.vishwam.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.apollopharmacy.vishwam.databinding.DialogAttachPhotoBinding

class AttachPhotoDialog : DialogFragment() {
    val TAG = "CityDialog"

    init {
        setCancelable(true)
    }

    //    lateinit var abstractDialogClick: AbstractDialogItemClickListner
    lateinit var viewBinding: DialogAttachPhotoBinding

//    interface AbstractDialogItemClickListner {
//        fun selectCity(item: String)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );

        viewBinding = DialogAttachPhotoBinding.inflate(inflater, container, false)

        viewBinding.imageView.clipToOutline = true

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       viewBinding.redCircle.setOnClickListener{

       }

        viewBinding.attachPhoto.setOnClickListener{

        }
    }
}