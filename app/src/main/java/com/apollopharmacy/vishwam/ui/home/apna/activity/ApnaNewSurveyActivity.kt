package com.apollopharmacy.vishwam.ui.home.apna.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityApnaNewSurveyBinding
import com.apollopharmacy.vishwam.databinding.DialogQuickGoBinding
import com.apollopharmacy.vishwam.ui.home.apna.activity.adapter.ImageAdapter
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.Image
import kotlinx.android.synthetic.main.activity_champs_survey_reports.*
import java.io.File

class ApnaNewSurveyActivity : AppCompatActivity(), ApnaNewSurveyCallBack {
    private lateinit var activityApnaNewSurveyBinding: ActivityApnaNewSurveyBinding
    private lateinit var apnaNewSurveyViewModel: ApnaNewSurveyViewModel
    var currentPosition: Int = 0
    var imageList = ArrayList<Image>()

    var imageFile: File? = null
    private var compressedImageFileName: String? = null
    var videoFile: File? = null

    lateinit var imageAdapter: ImageAdapter

    val REQUEST_CODE_CAMERA = 2235211
    val REQUEST_CODE_VIDEO = 2156


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityApnaNewSurveyBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_apna_new_survey
        )

        apnaNewSurveyViewModel = ViewModelProvider(this)[ApnaNewSurveyViewModel::class.java]

        setUp()
    }

    private fun setUp() {
        activityApnaNewSurveyBinding.backButton.setOnClickListener {
            finish()
        }

        activityApnaNewSurveyBinding.locationDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.locationDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.locationDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.locationDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.locationDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.siteSpecificationsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.siteSpecificationsExtraData.isVisible) {
                activityApnaNewSurveyBinding.siteSpecificationsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.siteSpecificationsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.siteSpecificationsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.marketInformationExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.marketInformationExtraData.isVisible) {
                activityApnaNewSurveyBinding.marketInformationExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.marketInformationExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.marketInformationExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.competitorsDetailsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.competitorsDetailsExtraData.isVisible) {
                activityApnaNewSurveyBinding.competitorsDetailsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.competitorsDetailsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.competitorsDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.populationAndHousesExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.populationAndHousesExtraData.isVisible) {
                activityApnaNewSurveyBinding.populationAndHousesExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.populationAndHousesExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.populationAndHousesExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.hospitalsExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.hospitalsExtraData.isVisible) {
                activityApnaNewSurveyBinding.hospitalsExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.hospitalsExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.hospitalsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.photoMediaExpand.setOnClickListener {
            if (activityApnaNewSurveyBinding.photoMediaExtraData.isVisible) {
                activityApnaNewSurveyBinding.photoMediaExtraData.visibility = View.GONE
                activityApnaNewSurveyBinding.photoMediaExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                activityApnaNewSurveyBinding.photoMediaExtraData.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.photoMediaExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        activityApnaNewSurveyBinding.filterIcon.setOnClickListener {
            val dialogBinding: DialogQuickGoBinding? =
                DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.dialog_quick_go,
                    null,
                    false)
            val customDialog = android.app.AlertDialog.Builder(this, 0).create()
            customDialog.apply {

                setView(dialogBinding?.root)
                setCancelable(false)
                dialogBinding!!.close.setOnClickListener {
                    dismiss()
                }
            }.show()
        }


        showNext(currentPosition)

        activityApnaNewSurveyBinding.next.setOnClickListener {
            currentPosition++
            activityApnaNewSurveyBinding.next.visibility = View.GONE
            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
            showNext(currentPosition)
        }

        activityApnaNewSurveyBinding.previous.setOnClickListener {
            currentPosition--
            activityApnaNewSurveyBinding.previous.visibility = View.GONE
            activityApnaNewSurveyBinding.previousNextBtn.visibility = View.VISIBLE
            showNext(currentPosition)
        }

        activityApnaNewSurveyBinding.nextBtn.setOnClickListener {
            if (currentPosition == 6) {
                showNext(currentPosition)
            } else {
                currentPosition++
                showNext(currentPosition)
            }
        }

        activityApnaNewSurveyBinding.previousBtn.setOnClickListener {
            currentPosition--
            if (currentPosition == 0) {
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
                activityApnaNewSurveyBinding.next.visibility = View.VISIBLE
            }
            showNext(currentPosition)
        }

        // Capture site photos
        activityApnaNewSurveyBinding.addSitePhoto.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(100)
            } else {
                openCamera()
            }
        }

        imageAdapter = ImageAdapter(context, imageList, this)
        activityApnaNewSurveyBinding.sitePhotosRecyclerView.adapter = imageAdapter
        activityApnaNewSurveyBinding.sitePhotosRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Set control in vdo
        val mediaController = android.widget.MediaController(context)
        mediaController.setAnchorView(activityApnaNewSurveyBinding.afterCapturedVideo)
        mediaController.setPadding(0, 20, 0, 0)
        mediaController.minimumHeight = 5
        activityApnaNewSurveyBinding.afterCapturedVideo.setMediaController(mediaController)

        // Record Video
        activityApnaNewSurveyBinding.videoIcon.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(100)
            } else {
                recordVideo()
            }
        }
        // Delete Video
        activityApnaNewSurveyBinding.deleteVideo.setOnClickListener {
            videoFile = null
            activityApnaNewSurveyBinding.beforeCaptureLayout.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.afterCaptureLayout.visibility = View.GONE
        }
    }

    private fun recordVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
        videoFile = File(context.cacheDir, "${System.currentTimeMillis()}.mp4")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile))
        } else {
            val videoUri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                videoFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivityForResult(intent, REQUEST_CODE_VIDEO)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
        compressedImageFileName = "${System.currentTimeMillis()}.jpg"
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK && imageFile != null) {
            imageList.add(Image(imageFile!!, ""))
            imageAdapter.notifyDataSetChanged()
        } else if (requestCode == REQUEST_CODE_VIDEO && resultCode == Activity.RESULT_OK && videoFile != null) {
            activityApnaNewSurveyBinding.beforeCaptureLayout.visibility = View.GONE
            activityApnaNewSurveyBinding.afterCaptureLayout.visibility = View.VISIBLE
            activityApnaNewSurveyBinding.afterCapturedVideo.setVideoPath(videoFile!!.absolutePath)

            activityApnaNewSurveyBinding.afterCapturedVideo.start()

        }
    }

    private fun showNext(currentPosition: Int) {
        when (currentPosition) {
            0 -> {
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
            }
            1 -> {
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
            }
            2 -> {
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
            }
            3 -> {
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
            }
            4 -> {
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
            }
            5 -> {
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.GONE
            }
            6 -> {
                activityApnaNewSurveyBinding.photosAndMediaLayout.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.locationDetailsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.siteSpecificationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.marketInformationLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.competitorsLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.populationAndHousesLayout.visibility = View.GONE
                activityApnaNewSurveyBinding.hospitalsLayout.visibility = View.GONE

                activityApnaNewSurveyBinding.previous.visibility = View.VISIBLE
                activityApnaNewSurveyBinding.previousNextBtn.visibility = View.GONE
            }
            else -> {
//                Toast.makeText(context, "No item available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askPermissions(PermissonCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA
                ), PermissonCode
            )
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), PermissonCode
            )
        }
    }

    override fun deleteSiteImage(position: Int, file: File) {
        imageList.removeAt(position)
        imageAdapter.notifyDataSetChanged()
    }
}