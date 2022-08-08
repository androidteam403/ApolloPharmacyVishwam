package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reshootactivity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityReShootBinding
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.reshootactivity.adapters.ImagesCardViewAdapterRes
import com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.uploadnowactivity.UploadNowButtonActivity
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelRequest
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.model.GetImageUrlModelResponse
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.ReShootActivityViewModel
import com.apollopharmacy.vishwam.ui.sampleui.swachuploadmodule.reshootactivity.adapters.OnClickStatusClickAdapter
import java.io.File

class ReShootActivity : AppCompatActivity(), ImagesCardViewAdapterRes.CallbackInterface {
    lateinit var activityreShootBinding: ActivityReShootBinding
    lateinit var viewModel: ReShootActivityViewModel
    private var getImageUrlsList = ArrayList<GetImageUrlModelResponse>()
    var imageFromCameraFile: File? = null
    private lateinit var onClickStatusClickAdapter: OnClickStatusClickAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityreShootBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_re_shoot

        )
        viewModel = ViewModelProvider(this)[ReShootActivityViewModel::class.java]
        val swachId=intent.getStringExtra("swachhid")
        val status=intent.getStringExtra("status")

        activityreShootBinding.statusTop.text = status


        var submit =  GetImageUrlModelRequest()
        submit.storeid = "16001"
        submit.swachhId = swachId
        viewModel.getImageUrl(submit)

        viewModel.getImageUrlsList.observeForever {
            if(it!=null && it.categoryList!=null){
                getImageUrlsList.add(it)
                onClickStatusClickAdapter =
                    OnClickStatusClickAdapter(getImageUrlsList.get(0).categoryList, this)
                val layoutManager = LinearLayoutManager(context)
                activityreShootBinding.imageRecyclerViewRes.layoutManager = layoutManager
                activityreShootBinding.imageRecyclerViewRes.itemAnimator =
                    DefaultItemAnimator()
                activityreShootBinding.imageRecyclerViewRes.adapter = onClickStatusClickAdapter
            }else{

                Toast.makeText(applicationContext, "Please try again", Toast.LENGTH_SHORT).show()
            }
        }



        activityreShootBinding.reshootButton.setOnClickListener {
            val intent = Intent(context, UploadNowButtonActivity::class.java)
            startActivity(intent)


        }
        activityreShootBinding.backButton.setOnClickListener{
            onBackPressed()
        }
    }

    private fun openCamera() {


        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile =
            File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
//        fileNameForCompressedImage = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCameraFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                imageFromCameraFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, Config.REQUEST_CODE_CAMERA)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onClickCamera(position: Int) {
       openCamera()
    }
}