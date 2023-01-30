package com.apollopharmacy.vishwam.ui.home.greeting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.databinding.ActivityGreetingBinding

class GreetingActivity : AppCompatActivity() {
    lateinit var activityGreetingBinding: ActivityGreetingBinding
    private  val pic_id = 123

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGreetingBinding = DataBindingUtil.setContentView(this, R.layout.activity_greeting)


        activityGreetingBinding.camera.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(Config.REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else
                cameraIntent()

        }



        activityGreetingBinding.writeurtextlayout.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setCancelable(false)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(R.color.black))
            dialog.setContentView(R.layout.dialog_write_text)

            dialog.show()
            var button :Button
            var clearText:ImageView
            clearText=dialog.findViewById(R.id.cleartext)
            var  descriptionText:TextView
            descriptionText=dialog.findViewById(R.id.descriptionText)
            button=dialog.findViewById(R.id.continue_btn)

            descriptionText.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))

            clearText.setOnClickListener {
                descriptionText.setText("")
            }





                button.setOnClickListener {
                    if(descriptionText.text.toString().isNullOrEmpty()){

                    }
                    else{
                        activityGreetingBinding.text.setText(descriptionText.text.toString())
                    }
                    dialog.dismiss()
                }



        }

        activityGreetingBinding.preview.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setCancelable(false)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(R.color.black))
            dialog.setContentView(R.layout.dialog_image_preview)

            dialog.show()
            var button :Button
            button=dialog.findViewById(R.id.continue_btn)



            button.setOnClickListener {
                dialog.dismiss()
            }



        }

    }

    private fun cameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, pic_id);

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            val photo = data!!.extras!!["data"] as Bitmap?
            // Set the image in imageview for display
            activityGreetingBinding.camera.setImageBitmap(photo)
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
           this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askPermissions(PermissonCode: Int) {
        requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ), PermissonCode
        )
    }
}