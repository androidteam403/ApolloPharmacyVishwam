package com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.Preferences
import com.apollopharmacy.vishwam.data.ViswamApp
import com.apollopharmacy.vishwam.data.ViswamApp.Companion.context
import com.apollopharmacy.vishwam.databinding.ActivityUploadImagesPostretroBinding
import com.apollopharmacy.vishwam.databinding.DialogForImageUploadBinding
import com.apollopharmacy.vishwam.ui.home.apnarectro.approval.previewscreen.adapter.TimeLineListAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.comparisonscreenscreation.ComparisonScreenCreation
import com.apollopharmacy.vishwam.ui.home.apnarectro.model.*
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.postrectrouploadimages.adapter.PreRetroTimeLineAdapter
import com.apollopharmacy.vishwam.ui.home.apnarectro.postrectro.reviewscreen.PostRectroReviewScreen
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.prerecctroreviewactivity.PreRectroReviewActivity
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter.ConfigApnaAdapterPostRetro
import com.apollopharmacy.vishwam.ui.home.apnarectro.prerectro.uploadactivity.adapter.ImagesUploadAdapterPostRetro
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.apollopharmacy.vishwam.util.NetworkUtil
import com.apollopharmacy.vishwam.util.Utlis
import com.apollopharmacy.vishwam.util.Utlis.hideLoading
import com.apollopharmacy.vishwam.util.Utlis.showLoading
import me.echodev.resizer.Resizer
import java.io.File
import java.util.stream.Collectors

class PostRetroUploadImagesActivity : AppCompatActivity(), PostRetroUploadImagesCallback,
    ImagesUploadAdapterPostRetro.CallbackInterface {
    lateinit var activityUploadImagesPostRetroBinding: ActivityUploadImagesPostretroBinding
    private var configApnaAdapterPostRetro: ConfigApnaAdapterPostRetro? = null
    private var postRetroUploadImagesViewModel: PostRetroUploadImagesViewModel? = null
    private lateinit var dialog: Dialog
    private var fragmentName: String = ""
    private var retroid: String = ""
    private var uploadedOn: String = ""
    private var uploadedBy: String = ""
    private var storeId: String = ""
    private var stage: String = ""
    private var retroStage: String = ""
    private var storeName: String = ""
    private var mainImageUrlList = ArrayList<GetImageUrlsModelApnaResponse.Category>()

    private var uploadStage: String = ""
    var timelineAdapter: PreRetroTimeLineAdapter? = null

    private var fileNameForCompressedImage: String? = null
    var pos: Int = 0
    var imageFromCameraFile: File? = null
    private var uploadedImageCount: Int = 0
    private var uploadedImageCountReset: Int = 0
    private var uploadedReshootImageCountReset: Int = 0

    private var uploadedReshootImageCount: Int = 0
    private var overallImageCount: Int = 0
    private var apnaConfigList = ArrayList<GetStoreWiseCatDetailsApnaResponse>()
    private lateinit var cameraDialog: Dialog
    var approvedby: String? = null
    var approvedDate: String? = null
    var status: String? = null
    var partiallyApprovedDate: String? = null
    var reshootBy: String? = null
    var reshootDate: String? = null
    var storeList: ArrayList<GetStorePendingAndApprovedListRes.Get>? = null

    private var getImageUrlsLists: GetImageUrlsModelApnaResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadImagesPostRetroBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_upload_images_postretro
        )

        setUp()


    }

    private fun setUp() {
        activityUploadImagesPostRetroBinding.callback = this
        postRetroUploadImagesViewModel =
            ViewModelProvider(this)[PostRetroUploadImagesViewModel::class.java]
        if (intent != null) {
            fragmentName = intent.getStringExtra("fragmentName")!!
            retroid = intent.getStringExtra("retroid")!!
            stage = intent.getStringExtra("stage")!!
            uploadedOn = intent.getStringExtra("uploadedOn")!!
            uploadedBy = intent.getStringExtra("uploadedBy")!!
            storeId = intent.getStringExtra("storeId")!!
            status = intent.getStringExtra("status")!!

            retroStage = intent.getStringExtra("retroStage")!!
            uploadStage = intent.getStringExtra("uploadStage")!!
            approvedby = intent.getStringExtra("approvedby")!!
            storeList =
                intent.getSerializableExtra("storeList") as ArrayList<GetStorePendingAndApprovedListRes.Get>?

        }


        activityUploadImagesPostRetroBinding.backButton.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        if (stage == "isPreRetroStage") {
            if (uploadStage == "newUploadStage") {
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.lightt_blue
                    )
                )
                activityUploadImagesPostRetroBinding.stageupdate.text = "Pre Retro Update"
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)
                activityUploadImagesPostRetroBinding.reviewName.text = "Pre Retro Review"
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.reportLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.statusBottom.text = status
                if (status!!.contains("Approved")) {
                    activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                        context.getColor(
                            R.color.greenn
                        )
                    )

                } else {
                    activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                        context.getColor(
                            R.color.color_red
                        )
                    )

                }
                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility = View.GONE

            } else if (uploadStage.equals("approvedStage")) {
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.lightt_blue
                    )
                )
                activityUploadImagesPostRetroBinding.stageupdate.text = "Pre Retro Update"
                activityUploadImagesPostRetroBinding.reviewName.text = "Pre Retro Review"
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.reportLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)
                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.statusBottom.text = status
                if (status!!.contains("Approved")) {
                    activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                        context.getColor(
                            R.color.greenn
                        )
                    )

                } else
                    if (status!!.contains("Reshoot")) {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.color_red
                            )
                        )

                    } else {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.pending_color_for_apna
                            )
                        )

                    }
            } else if (uploadStage.equals("reshootStage")) {
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.lightt_blue
                    )
                )
                activityUploadImagesPostRetroBinding.stageupdate.setText("Pre Retro Update")
                activityUploadImagesPostRetroBinding.reviewName.setText("Pre Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reportLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)

                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.statusBottom.text = status
                if (status!!.contains("Approved")) {
                    activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                        context.getColor(
                            R.color.greenn
                        )
                    )

                } else
                    if (status!!.contains("Reshoot")) {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.color_red
                            )
                        )

                    } else {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.pending_color_for_apna
                            )
                        )

                    }
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility = View.VISIBLE

            }

        } else if (stage.equals("isPostRetroStage")) {


            if (uploadStage == "newUploadStage") {
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )
                activityUploadImagesPostRetroBinding.stageupdate.setText("Post Retro Update")

                activityUploadImagesPostRetroBinding.reviewName.setText("Post Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.reportLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)
                activityUploadImagesPostRetroBinding.statusBottom.text = status

                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.reshootButton.visibility = View.GONE
            } else if (uploadStage == "approvedStage") {
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )

                activityUploadImagesPostRetroBinding.stageupdate.setText("Post Retro Update")
                activityUploadImagesPostRetroBinding.reviewName.setText("Post Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.reportLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)

                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.statusBottom.text = status
                if (status!!.contains("Approved")) {
                    activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                        context.getColor(
                            R.color.greenn
                        )
                    )

                } else
                    if (status!!.contains("Reshoot")) {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.color_red
                            )
                        )

                    } else {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.pending_color_for_apna
                            )
                        )

                    }
            } else if (uploadStage.equals("reshootStage")) {
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.lightt_blue
                    )
                )

                activityUploadImagesPostRetroBinding.stageupdate.setText("Post Retro Update")
                activityUploadImagesPostRetroBinding.reviewName.setText("Post Retro Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reportLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)

                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.statusBottom.text = status
                if (status!!.contains("Approved")) {
                    activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                        context.getColor(
                            R.color.greenn
                        )
                    )

                } else
                    if (status!!.contains("Reshoot")) {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.color_red
                            )
                        )

                    } else {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.pending_color_for_apna
                            )
                        )

                    }
            }

        } else {
            if (uploadStage.equals("newUploadStage")) {

                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )
                activityUploadImagesPostRetroBinding.stageupdate.setText("After Completion Update")
                activityUploadImagesPostRetroBinding.reviewName.setText("After Completion Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)

                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.reshootButton.visibility = View.GONE
            } else if (uploadStage.equals("approvedStage")) {
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )
                activityUploadImagesPostRetroBinding.warningLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reportLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.stageupdate.text = "After Completion Update"
                activityUploadImagesPostRetroBinding.reviewName.text = "After Completion Review"
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)

                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.statusBottom.text = status
                if (status!!.contains("Approved")) {
                    activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                        context.getColor(
                            R.color.greenn
                        )
                    )

                } else
                    if (status!!.contains("Reshoot")) {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.color_red
                            )
                        )

                    } else {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.pending_color_for_apna
                            )
                        )

                    }
            } else if (uploadStage.equals("reshootStage")) {
                activityUploadImagesPostRetroBinding.storeDetailsLayout.setBackgroundColor(
                    context.getColor(
                        R.color.white
                    )
                )
                activityUploadImagesPostRetroBinding.parentLayout.setBackgroundColor(
                    context.getColor(
                        R.color.lightt_blue
                    )
                )
                activityUploadImagesPostRetroBinding.stageupdate.setText("After Completion Update")
                activityUploadImagesPostRetroBinding.reviewName.setText("After Completion Review")
                activityUploadImagesPostRetroBinding.cancelUploadLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroUpdateLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.preRetroPendingLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reportLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.timelineLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadby.text = uploadedBy
                activityUploadImagesPostRetroBinding.uploadon.text = uploadedOn
                activityUploadImagesPostRetroBinding.storeName.text = storeId.split("-").get(1)
                activityUploadImagesPostRetroBinding.storeId.text = storeId.split("-").get(0)
                activityUploadImagesPostRetroBinding.bottomStatusLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.statusBottom.text = status
                if (status!!.contains("Approved")) {
                    activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                        context.getColor(
                            R.color.greenn
                        )
                    )

                } else
                    if (status!!.contains("Reshoot")) {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.color_red
                            )
                        )

                    } else {
                        activityUploadImagesPostRetroBinding.statusBottom.setTextColor(
                            context.getColor(
                                R.color.pending_color_for_apna
                            )
                        )

                    }
                activityUploadImagesPostRetroBinding.uploadnowbutton.visibility = View.GONE
                activityUploadImagesPostRetroBinding.reshootButton.visibility = View.VISIBLE

            }

        }

        if (NetworkUtil.isNetworkConnected(this)) {
            showLoading(this)
            postRetroUploadImagesViewModel!!.getStoreWiseDetailsApna(this)

        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.label_network_error),
                Toast.LENGTH_SHORT
            )
                .show()
        }


    }


    class ImgeDtcl(var file: File?, var categoryName: String, var postRetroUploaded: Boolean)

    override fun onClickUpload() {

        showLoading(this)
        updateButtonValidation()
    }

    override fun onClickCancel() {
        onClickBack()
    }

    override fun onBackPressed() {
        onClickBack()
    }

    fun onClickBack() {
        if (uploadedImageCount > 0 || uploadedReshootImageCount > 0) {
            val imagesStatusAlertDialog = Dialog(this)
            val dialogBackPressedAllert: DialogForImageUploadBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(this), R.layout.dialog_for_image_upload, null, false
                )
            imagesStatusAlertDialog.setContentView(dialogBackPressedAllert.root)
            imagesStatusAlertDialog.getWindow()
                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBackPressedAllert.yesBtn.setOnClickListener {
                imagesStatusAlertDialog.dismiss()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            dialogBackPressedAllert.cancelButton.setOnClickListener {
                imagesStatusAlertDialog.dismiss()
            }
            dialogBackPressedAllert.close.setOnClickListener {
                imagesStatusAlertDialog.dismiss()
            }
            imagesStatusAlertDialog.show()
        } else {
            super.onBackPressed()
        }
    }

    private fun updateButtonValidation() {
        if (uploadedImageCount == overallImageCount) {
            activityUploadImagesPostRetroBinding.uploadnowbutton.background =
                (resources.getDrawable(R.drawable.greenbackground_for_buttons))
            postRetroUploadImagesViewModel!!.connectToAzure(
                getImageUrlsLists, this, uploadStage.equals("reshootStage"), stage
            )
        } else {
            Toast.makeText(applicationContext, "Please upload all Images", Toast.LENGTH_SHORT)
                .show()
            hideLoading()
        }
    }

    var configPositionDel: Int = 0
    var uploadPositionDel: Int = 0

    override fun deleteImageCallBack(
        configPosDelete: Int,
        deleteImagePosDelete: Int,
        get: java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>,
    ) {
        this.configPositionDel = configPosDelete
        this.uploadPositionDel = deleteImagePosDelete
        cameraDialog = Dialog(this)
        cameraDialog.setContentView(R.layout.dialog_camera_delete)
        val close = cameraDialog.findViewById<TextView>(R.id.no_btnN)
        close.setOnClickListener {
            cameraDialog.dismiss()
        }
        val ok = cameraDialog.findViewById<TextView>(R.id.yes_btnN)
        ok.setOnClickListener {
            var pos = -1
            for (i in get) {
                if (stage.equals("isPreRetroStage")) {
                    if (i.stage.equals("1")) {
                        pos = get.indexOf(i)
                    }
                } else if (stage.equals("isPostRetroStage")) {
                    if (i.stage.equals("2")) {
                        pos = get.indexOf(i)
                    }

                } else {
                    if (i.stage.equals("3")) {
                        pos = get.indexOf(i)
                    }
                }
            }
            if (pos != -1) {
                if (get.get(pos).status.equals("9")) {
                    get.removeAt(pos)
                } else {
                    get.get(pos).file = null
                }
            }

            apnaConfigList.get(0).configlist?.get(configPosDelete)?.imageDataDto?.get(
                deleteImagePosDelete
            )?.file =
                null
            apnaConfigList.get(0).configlist!!.get(configPosDelete).imageUploaded = false
            if (!uploadStage.equals("reshootStage")) {
                uploadedImageCount--
                uploadedImageCountReset--
                runOnUiThread(Runnable {
                    activityUploadImagesPostRetroBinding.uploadedCount.text =
                        uploadedImageCount.toString()
                })

            } else {
                uploadedReshootImageCount--
                uploadedReshootImageCountReset--
                runOnUiThread(Runnable {
                    activityUploadImagesPostRetroBinding.uploadedCount.text =
                        uploadedReshootImageCount.toString()
                })
            }

            configApnaAdapterPostRetro?.notifyDataSetChanged()

            if (!uploadStage.equals("reshootStage")) {
                checkAllImagesUploaded()
            } else {
                checkAllImagesUploadedReshoot()
            }
            cameraDialog.dismiss()
        }

        cameraDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cameraDialog.show()
    }

    override fun capturedImageReview(
        capturedImagepos: Int,
        capturedImage: File?,
        view: View,
        position: Int,
        categoryName: String?,
    ) {
    }

    var pendingBottomCount: Int = 0
    var approvedBottomCount: Int = 0
    var reshootBottomCount: Int = 0
    fun updateBottomCount() {
        if (stage.equals("isPreRetroStage")) {
            for (i in getImageUrlsLists!!.categoryList?.indices!!) {
                for (j in getImageUrlsLists!!.categoryList!!.get(i).imageUrls?.indices!!) {
                    if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).stage.equals(
                            "1"
                        )
                    ) {
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "0"
                            )
                        ) {
                            pendingBottomCount++
                        }
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "1"
                            )
                        ) {
                            approvedBottomCount++
                        }
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "2"
                            )
                        ) {
                            reshootBottomCount++
                        }

                    }
                }
            }
        } else if (stage.equals("isPostRetroStage")) {
            for (i in getImageUrlsLists!!.categoryList?.indices!!) {
                for (j in getImageUrlsLists!!.categoryList!!.get(i).imageUrls?.indices!!) {
                    if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).stage.equals(
                            "2"
                        )
                    ) {
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "0"
                            )
                        ) {
                            pendingBottomCount++
                        }
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "1"
                            )
                        ) {
                            approvedBottomCount++
                        }
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "2"
                            )
                        ) {
                            reshootBottomCount++
                        }

                    }
                }
            }
        } else {
            for (i in getImageUrlsLists!!.categoryList?.indices!!) {
                for (j in getImageUrlsLists!!.categoryList!!.get(i).imageUrls?.indices!!) {
                    if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).stage.equals(
                            "3"
                        )
                    ) {
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "0"
                            )
                        ) {
                            pendingBottomCount++
                        }
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "1"
                            )
                        ) {
                            approvedBottomCount++
                        }
                        if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                "2"
                            )
                        ) {
                            reshootBottomCount++
                        }

                    }
                }
            }
        }

        activityUploadImagesPostRetroBinding.uploadedOverallCount.text =
            overallImageCount.toString()
        activityUploadImagesPostRetroBinding.pendingCount.text = pendingBottomCount.toString()
        activityUploadImagesPostRetroBinding.approvedCount.text = approvedBottomCount.toString()
        activityUploadImagesPostRetroBinding.reshootCount.text = reshootBottomCount.toString()

    }

    private fun checkAllImagesUploaded() {
        activityUploadImagesPostRetroBinding.uploadedCount.text = uploadedImageCount.toString()
        activityUploadImagesPostRetroBinding.overAllCount.text = "/" + overallImageCount.toString()
        if (apnaConfigList.get(0).configlist != null) {
            for ((index, value) in apnaConfigList.get(0).configlist!!.withIndex()) {
                for ((index1, value1) in apnaConfigList.get(0).configlist?.get(index)?.imageDataDto?.withIndex()!!) {
                    apnaConfigList.get(0).configlist!!.get(index).imageUploaded =
                        apnaConfigList.get(0).configlist!!.get(index).imageDataDto?.get(index1)?.file != null
                }

            }
        }


        var allImagesUploaded: Boolean = true
        for ((index, value) in apnaConfigList.get(0).configlist!!.withIndex()) {
            if (apnaConfigList.get(0).configlist!!.get(index).imageUploaded == false) {
                allImagesUploaded = false
            }

            if (uploadedImageCount == overallImageCount) {
                activityUploadImagesPostRetroBinding.warningLayout.visibility = View.GONE
                activityUploadImagesPostRetroBinding.completedMessage.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.uploadnowbutton.background =
                    (resources.getDrawable(R.drawable.greenbackground_for_buttons))
                activityUploadImagesPostRetroBinding.uploadedCount.setTextColor(getColor(R.color.dark_green))
            } else {
                activityUploadImagesPostRetroBinding.warningLayout.visibility = View.VISIBLE
                activityUploadImagesPostRetroBinding.completedMessage.visibility = View.GONE
                activityUploadImagesPostRetroBinding.uploadedCount.setTextColor(getColor(R.color.red))
                activityUploadImagesPostRetroBinding.uploadnowbutton.background =
                    (resources.getDrawable(R.drawable.ashbackgrounf_for_buttons))
            }

        }
    }

    override fun onClickEyeImage() {
        if (fragmentName.equals("fromPreRectro")) {
            val intent = Intent(applicationContext, PreRectroReviewActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        } else {
            val intent = Intent(applicationContext, PostRectroReviewScreen::class.java)
            intent.putExtra("fragmentName", fragmentName)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

    }

    override fun onClickImageView() {
        TODO("Not yet implemented")
    }

    var imageClickedPosComp: Int? = 0
    var categoryPosComp: Int? = 0
    override fun onClickImageView(
        stage: String,
        store: String,
        posImageUrlList: java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>,
        apnaConfigList: java.util.ArrayList<GetImageUrlsModelApnaResponse.Category>,

        categoryName: String?,
        categoryid: String?,
        imageClickedPos: Int,
        configPosition: Int,
        categoryGroupResponse: GetImageUrlsModelApnaResponse.Category

    ) {
        imageClickedPosComp = imageClickedPos
        categoryPosComp = configPosition

        val intent = Intent(applicationContext, ComparisonScreenCreation::class.java)
        intent.putExtra("fragmentName", fragmentName)
        intent.putExtra("stage", stage)
        intent.putExtra("store", store)
        if (mainImageUrlList.isNullOrEmpty()) {
            intent.putExtra("mainList", apnaConfigList)

        } else {
            intent.putExtra("mainList", mainImageUrlList)

        }

        intent.putExtra("categoryResponse", categoryGroupResponse)

        intent.putExtra("status", approvedby)
        intent.putExtra("posImageUrlList", posImageUrlList)
        intent.putExtra("retroid", retroid)
        intent.putExtra("categoryName", categoryName)
        intent.putExtra("categoryid", categoryid)
        intent.putExtra("uploadStage", uploadStage)
        intent.putExtra("imageClickedPos", imageClickedPos)
        if (getImageUrlsLists != null && getImageUrlsLists!!.remarks != null && getImageUrlsLists!!.remarks!!.size > 0) {
            intent.putExtra("storeIdFromRemarks", getImageUrlsLists!!.remarks!!.get(0).storeId)

        }
        startActivityForResult(intent, 999)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    var configPosition: Int = 0
    var uploadPosition: Int = 0
    var reshootImageDetails: ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>? = null
    override fun onClickCameraIcon(
        configPosition: Int,
        uploadButtonPosition: Int,
        get: ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>,
    ) {
        this.configPosition = configPosition
        this.uploadPosition = uploadButtonPosition
        reshootImageDetails = get

        if (!checkPermission()) {
            askPermissions(Config.REQUEST_CODE_CAMERA)
            return
        } else {

            openCamera()

        }
    }

    override fun onSuccessImageIsUploadedInAzur(response: GetImageUrlsModelApnaResponse) {
        getImageUrlsLists = response

        uploadApi()
    }

    override fun onSuccessImageIsUploadedInAzurReshoot(response: String?) {
        getImageUrlsLists!!.categoryList?.get(configPosition)?.imageUrls?.get(
            uploadPosition
        )?.url = response
        getImageUrlsLists!!.categoryList?.get(configPosition)?.imageUrls?.get(
            uploadPosition
        )?.isReshootStatus = true
        getImageUrlsLists!!.categoryList?.get(configPosition)?.imageUrls?.get(
            uploadPosition
        )?.position = uploadPosition
        uploadedReshootImageCount++
        uploadedReshootImageCountReset++
        runOnUiThread(Runnable {
            activityUploadImagesPostRetroBinding.uploadedCount.text =
                uploadedReshootImageCount.toString()
        })

        checkAllImagesUploadedReshoot()
        runOnUiThread(Runnable {
            configApnaAdapterPostRetro!!.notifyDataSetChanged()
        })

        if (uploadedReshootImageCount == overallreshootcount) {
            activityUploadImagesPostRetroBinding.reshootButton.background =
                (resources.getDrawable(R.drawable.redbackground_for_buttons))
            activityUploadImagesPostRetroBinding.reshootButton.background =
                (resources.getDrawable(R.drawable.ashbackgrounf_for_buttons))
        }
        hideLoading()
    }

    override fun onCickDownArrow() {
        if (activityUploadImagesPostRetroBinding.downArrow.rotation.equals(0f)) {
            activityUploadImagesPostRetroBinding.downArrow.rotation = 90f
            activityUploadImagesPostRetroBinding.timeLineDetailsLayout.visibility = View.VISIBLE

            val layoutManager = LinearLayoutManager(context)
            activityUploadImagesPostRetroBinding.timeLineRecycleview.layoutManager =
                layoutManager


            timelineAdapter = PreRetroTimeLineAdapter(
                this,
                storeList!!.filter { it.retroid.equals(retroid) && it.stage.equals(retroStage) && it.hierarchystatus != null })
            activityUploadImagesPostRetroBinding.timeLineRecycleview.adapter = timelineAdapter

        } else {
            activityUploadImagesPostRetroBinding.downArrow.rotation = 0f
            activityUploadImagesPostRetroBinding.timeLineDetailsLayout.visibility = View.GONE
        }
    }


    override fun onSuccessSaveImageUrlsApi(saveImageUrlsResponse: SaveImageUrlsResponse) {
        if (saveImageUrlsResponse != null && saveImageUrlsResponse.status == true) {
            hideLoading()
            dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_onsuccessupload_apna)
            val close = dialog.findViewById<LinearLayout>(R.id.close_apna)
            val textMessage = dialog.findViewById<TextView>(R.id.transaction_id_apna)
            if (saveImageUrlsResponse.retroid != null) {
                if (stage.equals("isPreRetroStage")) {
                    textMessage.text =
                        "Pre Retro is Submitted for Review \n Transaction id is: " + saveImageUrlsResponse.retroid
                }
            } else {
                if (stage.equals("isPreRetroStage")) {
                    textMessage.text =
                        "Pre Retro is Submitted for Review for transaction id: " + retroid
                } else if (stage.equals("isPostRetroStage")) {
                    textMessage.text =
                        "Post Retro is Submitted for Review for transaction id: " + retroid
                } else {
                    textMessage.text =
                        "After Completion is Submitted for Review for transaction id: " + retroid

                }
            }

            close.setOnClickListener {
                dialog.dismiss()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            val ok = dialog.findViewById<RelativeLayout>(R.id.ok_apna)
            ok.setOnClickListener {
                dialog.dismiss()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        } else {
            Toast.makeText(context, "" + saveImageUrlsResponse.message, Toast.LENGTH_SHORT).show()
            hideLoading()
        }
    }

    override fun onFailureSaveImageUrlsApi(saveImageUrlsResponse: SaveImageUrlsResponse) {
        Toast.makeText(context, "" + saveImageUrlsResponse.message, Toast.LENGTH_SHORT).show()
        hideLoading()
    }

    override fun onSuccessGetStoreWiseDetails(getStoreWiseResponse: GetStoreWiseCatDetailsApnaResponse) {
        if (getStoreWiseResponse != null && getStoreWiseResponse.message == "success") {
            activityUploadImagesPostRetroBinding.noOrdersFound.visibility = View.GONE
            apnaConfigList.add(getStoreWiseResponse)
            for ((index, value) in getStoreWiseResponse.configlist!!.withIndex()) {
                val countUpload = value.categoryImageUploadCount?.toInt()
                var dtcl_list = ArrayList<GetStoreWiseCatDetailsApnaResponse.Config.ImgeDtcl>()
                for (count in 1..countUpload!!) {
                    overallImageCount++
                    dtcl_list.add(
                        GetStoreWiseCatDetailsApnaResponse.Config.ImgeDtcl(
                            null,
                            count,
                            "",
                            0, "", ""
                        )
                    )

                }
                apnaConfigList.get(0).configlist?.get(index)?.imageDataDto = dtcl_list

            }
//            if(!uploadStage.equals("reshootStage")){
//                activityUploadImagesPostRetroBinding.uploadedCount.text= uploadedImageCount.toString()
//                activityUploadImagesPostRetroBinding.overAllCount.text = "/" +overallImageCount.toString()
//            }else{
//                activityUploadImagesPostRetroBinding.uploadedCount.text= uploadedReshootImageCount.toString()
//                activityUploadImagesPostRetroBinding.overAllCount.text = "/" +overallreshootcount.toString()
//            }

            if (NetworkUtil.isNetworkConnected(this)) {
                showLoading(this)
                var submit = GetImageUrlsModelApnaRequest()
                submit.storeid = Preferences.getApnaSiteId()
                submit.retroId = retroid

                postRetroUploadImagesViewModel!!.getImageUrl(submit, this)

            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.label_network_error),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
//            configApnaAdapterPostRetro =
//                ConfigApnaAdapterPostRetro(getImageUrlsLists!!.categoryList, apnaConfigList.get(0), this, this, stage)
//            val layoutManager = LinearLayoutManager(ViswamApp.context)
//            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.layoutManager = layoutManager
//            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.itemAnimator =
//                DefaultItemAnimator()
//            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.adapter = configApnaAdapterPostRetro
//
//            Utlis.hideLoading()
        } else {
            activityUploadImagesPostRetroBinding.noOrdersFound.visibility = View.VISIBLE
            Toast.makeText(context, "Store ID not Available", Toast.LENGTH_SHORT)
                .show()
            super.onBackPressed()
            hideLoading()
        }


    }

    override fun onFailureStoreWiseDetails(value: GetStoreWiseCatDetailsApnaResponse) {
        activityUploadImagesPostRetroBinding.noOrdersFound.visibility = View.VISIBLE
        Toast.makeText(context, "" + value.message, Toast.LENGTH_SHORT).show()

        hideLoading()
    }

    var overallreshootcount: Int = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onSuccessImageUrlsList(
        getImageUrlsList: GetImageUrlsModelApnaResponse,
        retroId: String,
    ) {
        if (getImageUrlsList != null && getImageUrlsList.status!!.equals(true)) {
            getImageUrlsLists = getImageUrlsList
            hideLoading()
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.visibility =
                View.VISIBLE
            activityUploadImagesPostRetroBinding.noOrdersFound.visibility = View.GONE
            for (i in getImageUrlsList.categoryList!!.indices) {
                val retroIdsGroupedList: Map<Int, List<GetImageUrlsModelApnaResponse.Category.ImageUrl>> =
                    getImageUrlsList.categoryList!!.get(i).imageUrls!!.stream()
                        .collect(Collectors.groupingBy { w -> w.position })
//                Toast.makeText(context, "" + retroIdsGroupedList.size, Toast.LENGTH_SHORT).show()

                var getImageUrlListDummys =
                    java.util.ArrayList<java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>>()
                for (entry in retroIdsGroupedList.entries) {
                    getImageUrlListDummys.addAll(listOf(entry.value as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>))
                }
                getImageUrlsLists!!.categoryList!!.get(i).groupingImageUrlList =
                    getImageUrlListDummys

            }
            if (uploadStage.equals("reshootStage")) {
                if (stage.equals("isPreRetroStage")) {
                    for (i in getImageUrlsLists!!.categoryList?.indices!!) {
                        for (j in getImageUrlsLists!!.categoryList!!.get(i).imageUrls?.indices!!) {
                            if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).stage.equals(
                                    "1"
                                )
                            ) {
                                if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                        "2"
                                    )
                                ) {
                                    overallreshootcount++
                                }

                            }
                        }
                    }
                } else if (stage.equals("isPostRetroStage")) {
                    for (i in getImageUrlsLists!!.categoryList?.indices!!) {
                        for (j in getImageUrlsLists!!.categoryList!!.get(i).imageUrls?.indices!!) {
                            if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).stage.equals(
                                    "2"
                                )
                            ) {
                                if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                        "2"
                                    )
                                ) {
                                    overallreshootcount++
                                }

                            }
                        }
                    }
                } else {
                    for (i in getImageUrlsLists!!.categoryList?.indices!!) {
                        for (j in getImageUrlsLists!!.categoryList!!.get(i).imageUrls?.indices!!) {
                            if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).stage.equals(
                                    "3"
                                )
                            ) {
                                if (getImageUrlsLists!!.categoryList!!.get(i).imageUrls!!.get(j).status.equals(
                                        "2"
                                    )
                                ) {
                                    overallreshootcount++
                                }

                            }
                        }
                    }
                }
                activityUploadImagesPostRetroBinding.uploadedCount.text =
                    uploadedReshootImageCount.toString()
                activityUploadImagesPostRetroBinding.overAllCount.text =
                    "/" + "" + overallreshootcount.toString()

            }

            configApnaAdapterPostRetro =
                ConfigApnaAdapterPostRetro(
                    getImageUrlsLists!!.categoryList as MutableList<GetImageUrlsModelApnaResponse.Category>,
                    apnaConfigList.get(0),

                    this,
                    this,
                    stage, storeId,
                    getImageUrlsLists!!.categoryList
                )
            val layoutManager = LinearLayoutManager(context)
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.layoutManager =
                layoutManager
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.itemAnimator =
                DefaultItemAnimator()
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.adapter =
                configApnaAdapterPostRetro
            updateBottomCount()
            if (!uploadStage.equals("reshootStage")) {
                checkAllImagesUploaded()
            } else {
                checkAllImagesUploadedReshoot()
            }

        } else {
            hideLoading()
            activityUploadImagesPostRetroBinding.categoryNameApnaRecyclerView.visibility = View.GONE
            activityUploadImagesPostRetroBinding.noOrdersFound.visibility = View.VISIBLE
        }
    }

    override fun onFailureImageUrlsList(value: GetImageUrlsModelApnaResponse) {

    }

    override fun onClickReshoot() {
        showLoading(this)
        reshootButtonValidation()
    }


    private fun reshootButtonValidation() {
        if (uploadedReshootImageCount == overallreshootcount) {
            postRetroUploadImagesViewModel!!.connectToAzure(
                getImageUrlsLists, this, uploadStage.equals("reshootStage"), stage
            )
        } else {
            Toast.makeText(applicationContext, "Please upload all Images", Toast.LENGTH_SHORT)
                .show()
            hideLoading()
        }
    }

    private fun uploadApi() {
        var submit = SaveImagesUrlsRequest()
        if (!uploadStage.equals("reshootStage")) {
            submit.actionEvent = "SUBMIT"
        } else {
            submit.actionEvent = "RESHOOT"
        }
        submit.retroautoid = ""
        submit.storeid = Preferences.getApnaSiteId()
        submit.userid = Preferences.getToken()
        if (stage.equals("isPreRetroStage")) {
            submit.stage = "1"
            submit.retroautoid = retroid
        } else if (stage.equals("isPostRetroStage")) {
            submit.stage = "2"
            submit.retroautoid = retroid
        } else {
            submit.stage = "3"
            submit.retroautoid = retroid
        }
        if (!uploadStage.equals("reshootStage")) {
            var imageUrlsList = java.util.ArrayList<SaveImagesUrlsRequest.ImageUrl>()

            for (i in getImageUrlsLists!!.categoryList!!.indices) {
                for (j in getImageUrlsLists!!.categoryList!!.get(i).groupingImageUrlList!!.indices) {

                    var imageUrl = submit.ImageUrl()
                    for (k in getImageUrlsLists!!.categoryList!!.get(i).groupingImageUrlList!!.get(
                        j
                    )) {
                        if (stage.equals("isPreRetroStage")) {
                            if (k.stage.equals("1")) {
                                imageUrl.url =
                                    k.url
                                imageUrl.categoryid =
                                    getImageUrlsLists!!.categoryList!!.get(i).categoryid
                                imageUrl.position = k.position
                                imageUrlsList.add(imageUrl)
                            }

                        } else if (stage.equals("isPostRetroStage")) {
                            if (k.stage.equals("2")) {
                                imageUrl.url =
                                    k.url
                                imageUrl.categoryid =
                                    getImageUrlsLists!!.categoryList!!.get(i).categoryid
                                imageUrl.position = k.position
                                imageUrlsList.add(imageUrl)
                            }

                        } else {
                            if (k.stage.equals("3")) {
                                imageUrl.url =
                                    k.url
                                imageUrl.categoryid =
                                    getImageUrlsLists!!.categoryList!!.get(i).categoryid
                                imageUrl.position = k.position
                                imageUrlsList.add(imageUrl)
                            }

                        }


                    }

                }
            }
            submit.imageUrls = imageUrlsList
            postRetroUploadImagesViewModel!!.onUploadImagesApna(submit, this)

        } else {
            var imageUrlsList = java.util.ArrayList<SaveImagesUrlsRequest.ImageUrl>()

            for (i in getImageUrlsLists!!.categoryList!!.indices) {
                for (j in getImageUrlsLists!!.categoryList!!.get(i).groupingImageUrlList!!.indices) {

                    var imageUrl = submit.ImageUrl()
                    for (k in getImageUrlsLists!!.categoryList!!.get(i).groupingImageUrlList!!.get(
                        j
                    )) {
                        if (stage.equals("isPreRetroStage")) {
                            if (k.stage.equals("1") && k.status.equals("2")) {
                                imageUrl.url =
                                    k.url
                                imageUrl.categoryid =
                                    getImageUrlsLists!!.categoryList!!.get(i).categoryid
                                imageUrl.position = k.position
                                imageUrl.imageId = k.imageid
                                imageUrlsList.add(imageUrl)
                            }

                        } else if (stage.equals("isPostRetroStage")) {
                            if (k.stage.equals("2") && k.status.equals("2")) {
                                imageUrl.url =
                                    k.url
                                imageUrl.categoryid =
                                    getImageUrlsLists!!.categoryList!!.get(i).categoryid
                                imageUrl.position = k.position
                                imageUrl.imageId = k.imageid
                                imageUrlsList.add(imageUrl)
                            }

                        } else {
                            if (k.stage.equals("3") && k.status.equals("2")) {
                                imageUrl.url =
                                    k.url
                                imageUrl.categoryid =
                                    getImageUrlsLists!!.categoryList!!.get(i).categoryid
                                imageUrl.position = k.position
                                imageUrl.imageId = k.imageid
                                imageUrlsList.add(imageUrl)
                            }

                        }


                    }

                }
            }
            submit.imageUrls = imageUrlsList
            postRetroUploadImagesViewModel!!.onUploadImagesApna(submit, this)
        }

//        }
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFromCameraFile =
            File(context.cacheDir, "${System.currentTimeMillis()}.jpg")
        fileNameForCompressedImage = "${System.currentTimeMillis()}.jpg"
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


//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, cameraRequest)
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
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

    var posImageUrlListForReshoot =
        java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>()
    var imageUrlWithData = GetImageUrlsModelApnaResponse.Category.ImageUrl()
    var fileForReshoot: File? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Config.REQUEST_CODE_CAMERA && imageFromCameraFile != null && resultCode == Activity.RESULT_OK) {
//            var capture: File? = null
            imageFromCameraFile?.length()
            val resizedImage = Resizer(this)
                .setTargetLength(1080)
                .setQuality(100)
                .setOutputFormat("JPG")
                .setOutputDirPath(
                    ViswamApp.context.cacheDir.toString()
                )

                .setSourceImage(imageFromCameraFile)
                .resizedFile
            if (resizedImage != null) {
                if (uploadStage.equals("reshootStage")) {
                    if (stage.equals("isPreRetroStage")) {
                        for (i in reshootImageDetails!!) {
                            if (i.stage.equals("1") && i.status.equals("2")) {
                                i.file = resizedImage
                                break
                            }

                        }

                    } else if (stage.equals("isPostRetroStage")) {
                        for (i in reshootImageDetails!!) {
                            if (i.stage.equals("2") && i.status.equals("2")) {
                                i.file = resizedImage

                                break
                            }

                        }
                    } else {
                        for (i in reshootImageDetails!!) {
                            if (i.stage.equals("3") && i.status.equals("2")) {
                                i.file = resizedImage
                                break
                            }

                        }
                    }

                } else {

                    var imageUrl = GetImageUrlsModelApnaResponse.Category.ImageUrl()
                    imageUrl.file = resizedImage
                    imageUrl.status = "9"
                    if (stage.equals("isPreRetroStage")) {
                        imageUrl.stage = "1"
                    } else if (stage.equals("isPostRetroStage")) {
                        imageUrl.stage = "2"
                    } else {
                        imageUrl.stage = "3"
                    }
                    imageUrl.categoryid = reshootImageDetails!!.get(0).categoryid
                    imageUrl.position = uploadPosition
                    getImageUrlsLists!!.categoryList!!.get(configPosition).groupingImageUrlList!!.get(
                        uploadPosition
                    ).add(imageUrl)
                }


            }

//


            configApnaAdapterPostRetro?.notifyDataSetChanged()

            if (!uploadStage.equals("reshootStage")) {
                uploadedImageCount++
                uploadedImageCountReset++
                checkAllImagesUploaded()
            } else {
                uploadedReshootImageCount++
                uploadedReshootImageCountReset++
                checkAllImagesUploadedReshoot()
            }


        } else if (requestCode == 999 && resultCode == Activity.RESULT_OK) {
            val uploadedImageIds = mutableSetOf<String>()
            uploadedReshootImageCount=0
            var stage:String=""
            uploadedImageCount=0
//            getImageUrlsLists!!.categoryList = mainImageUrlList

            mainImageUrlList =
                data!!.getSerializableExtra("mainImageUrlList") as ArrayList<GetImageUrlsModelApnaResponse.Category>
//            posImageUrlListForReshoot =
//                data!!.getSerializableExtra("posImageUrlList") as java.util.ArrayList<GetImageUrlsModelApnaResponse.Category.ImageUrl>
//            imageUrlWithData =
//                data!!.getSerializableExtra("imageUrlWithData") as GetImageUrlsModelApnaResponse.Category.ImageUrl

            stage= data!!.getStringExtra("stage")!!
            for (i in mainImageUrlList.indices) {
                for (j in mainImageUrlList[i].groupingImageUrlList!!.indices) {
                    for (k in mainImageUrlList[i].groupingImageUrlList!![j].indices) {
                        if (mainImageUrlList[i].groupingImageUrlList!![j][k].url.isNullOrEmpty()&&mainImageUrlList[i].groupingImageUrlList!![j][k].stage.equals(stage)) {
                            if (uploadStage.equals("reshootStage")) {
                                uploadedImageCount++
//                            overallreshootcount++
                                uploadedReshootImageCount++
                                checkAllImagesUploaded()
                                checkAllImagesUploadedReshoot()

                                // Add the file from mainImageUrlList to getImageUrlsLists
                                getImageUrlsLists!!.categoryList!!.get(i).groupingImageUrlList!!.get(j)[k].file = mainImageUrlList[i].groupingImageUrlList!![j][k].file
                            }else{
                                uploadedImageCount++
//                            overallreshootcount++
                                uploadedReshootImageCount++
                                checkAllImagesUploaded()
                                checkAllImagesUploadedReshoot()
                                getImageUrlsLists!!.categoryList!!.get(i).groupingImageUrlList!!.get(j).add(mainImageUrlList[i].groupingImageUrlList!![j].get(k))
                            }

                        }
                    }
                }
            }
            configApnaAdapterPostRetro!!.notifyDataSetChanged()

// Notify the adapter of the data change




//            if (uploadStage.equals("reshootStage")) {
//                if (stage.equals("isPreRetroStage")) {
//                    getImageUrlsLists!!.categoryList!!.get(categoryPosComp!!).groupingImageUrlList!!.get(
//                        imageClickedPosComp!!
//                    ).get(0).file = posImageUrlListForReshoot.get(0).file
//
//                } else if (stage.equals("isPostRetroStage")) {
//                    getImageUrlsLists!!.categoryList!!.get(categoryPosComp!!).groupingImageUrlList!!.get(
//                        imageClickedPosComp!!
//                    ).get(1).file = posImageUrlListForReshoot.get(1).file
//
//                } else {
//                    getImageUrlsLists!!.categoryList!!.get(categoryPosComp!!).groupingImageUrlList!!.get(
//                        imageClickedPosComp!!
//                    )[2].file = posImageUrlListForReshoot.get(2).file
//
//                }
//
//                getImageUrlsLists!!.categoryList = mainImageUrlList
//
//                checkAllImagesUploadedReshoot()
//                configApnaAdapterPostRetro!!.notifyDataSetChanged()
//            }
//
//            else {


//                getImageUrlsLists!!.categoryList!!.get(categoryPosComp!!).groupingImageUrlList!!.get(
//                    imageClickedPosComp!!
//                ).add(imageUrlWithData)
//            configApnaAdapterPostRetro!!.notifyDataSetChanged()

//            }

        }


    }

    private fun checkAllImagesUploadedReshoot() {
        runOnUiThread(Runnable {
            activityUploadImagesPostRetroBinding.uploadedCount.text =
                uploadedReshootImageCount.toString()
            activityUploadImagesPostRetroBinding.overAllCount.text =
                "/" + overallreshootcount.toString()
        })

        if (apnaConfigList.get(0).configlist != null) {
            for ((index, value) in apnaConfigList.get(0).configlist!!.withIndex()) {
                for ((index1, value1) in apnaConfigList.get(0).configlist?.get(index)?.imageDataDto?.withIndex()!!) {
                    apnaConfigList.get(0).configlist!!.get(index).imageUploaded =
                        apnaConfigList.get(0).configlist!![index].imageDataDto?.get(index1)?.file != null
                }

            }
        }


        var allImagesUploaded: Boolean = true
        for ((index, value) in apnaConfigList.get(0).configlist!!.withIndex()) {
            if (apnaConfigList.get(0).configlist!!.get(index).imageUploaded == false) {
                allImagesUploaded = false
            }

            if (uploadedReshootImageCount == overallreshootcount) {
                runOnUiThread(Runnable {
                    activityUploadImagesPostRetroBinding.warningLayout.visibility = View.GONE
                    activityUploadImagesPostRetroBinding.completedMessage.visibility = View.VISIBLE
                    activityUploadImagesPostRetroBinding.reshootButton.background =
                        (resources.getDrawable(R.drawable.redbackground_for_buttons))
                    activityUploadImagesPostRetroBinding.uploadedCount.setTextColor(getColor(R.color.dark_green))
                })
            } else {
                runOnUiThread(Runnable {
                    activityUploadImagesPostRetroBinding.warningLayout.visibility = View.VISIBLE
                    activityUploadImagesPostRetroBinding.completedMessage.visibility = View.GONE
                    activityUploadImagesPostRetroBinding.uploadedCount.setTextColor(getColor(R.color.red))
                    activityUploadImagesPostRetroBinding.reshootButton.background =
                        (resources.getDrawable(R.drawable.ashbackgrounf_for_buttons))
                })

            }

        }
    }

}