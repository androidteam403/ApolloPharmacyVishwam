package com.apollopharmacy.vishwam.ui.home.apna

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.base.BaseFragment
import com.apollopharmacy.vishwam.databinding.*
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ApartmentData
import com.apollopharmacy.vishwam.ui.home.apna.model.ChemistData
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.HospitalData
import com.apollopharmacy.vishwam.ui.home.apna.model.ImageData
import java.io.File

class ApnaFormFragment : BaseFragment<ApnaFormViewModel, FragmentApnaFormBinding>() {
    val chemistData = ArrayList<ChemistData>()
    val apartmentData = ArrayList<ApartmentData>()
    val hospitalData = ArrayList<HospitalData>()
    val parkingList = ArrayList<String>()
    val trafficStreetList = ArrayList<String>()
    val trafficGeneratorsList = ArrayList<String>()

    val imageList = ArrayList<ImageData>()
    var imageFile: File? = null
    var videoFile: File? = null

    val REQUEST_CODE_CAMERA = 200
    val REQUEST_CODE_CAMERA_ONE = 2134
    val REQUEST_CODE_CAMERA_TWO = 2361
    val REQUEST_CODE_CAMERA_THREE = 1490
    val REQUEST_CODE_CAMERA_FOUR = 2018
    val REQUEST_CODE_CAMERA_FIVE = 1526
    val REQUEST_CODE_VIDEO = 2156


    override val layoutRes: Int
        get() = R.layout.fragment_apna_form

    override fun retrieveViewModel(): ApnaFormViewModel {
        return ViewModelProvider(this).get(ApnaFormViewModel::class.java)
    }

    override fun setup() {
        // Location Details
        viewBinding.locationDetailsExpand.setOnClickListener {
            if (viewBinding.locationDetailsExtraData.isVisible) {
                viewBinding.locationDetailsExtraData.visibility = View.GONE
                viewBinding.locationDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                viewBinding.locationDetailsExtraData.visibility = View.VISIBLE
                viewBinding.locationDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Shop Details
        viewBinding.shopDetailsExpand.setOnClickListener {
            if (viewBinding.shopDetailsExtraData.isVisible) {
                viewBinding.shopDetailsExtraData.visibility = View.GONE
                viewBinding.shopDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                viewBinding.shopDetailsExtraData.visibility = View.VISIBLE
                viewBinding.shopDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Rental Details
        viewBinding.rentalDetailsExpand.setOnClickListener {
            if (viewBinding.rentalDetailsExtraData.isVisible) {
                viewBinding.rentalDetailsExtraData.visibility = View.GONE
                viewBinding.rentalDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                viewBinding.rentalDetailsExtraData.visibility = View.VISIBLE
                viewBinding.rentalDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Existing Business Details
        viewBinding.existingBusinessDetailsExpand.setOnClickListener {
            if (viewBinding.existingBusinessDetailsExtraData.isVisible) {
                viewBinding.existingBusinessDetailsExtraData.visibility = View.GONE
                viewBinding.existingBusinessDetailsExpand.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                viewBinding.existingBusinessDetailsExtraData.visibility = View.VISIBLE
                viewBinding.existingBusinessDetailsExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }

        // Existing Business Details 1
        viewBinding.existingBusinessDetailsExpand1.setOnClickListener {
            if (viewBinding.existingBusinessDetailsExtraData1.isVisible) {
                viewBinding.existingBusinessDetailsExtraData1.visibility = View.GONE
                viewBinding.existingBusinessDetailsExpand1.setImageResource(R.drawable.right_arrow_black_new)
            } else {
                viewBinding.existingBusinessDetailsExtraData1.visibility = View.VISIBLE
                viewBinding.existingBusinessDetailsExpand1.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }
    }
}

//==========================================

        // Tables Data

        /*viewBinding.addChemist.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val addChemistDialogBinding: AddChemistDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.add_chemist_dialog,
                null,
                false
            )
            dialog.setContentView(addChemistDialogBinding.root)

            addChemistDialogBinding.cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            addChemistDialogBinding.addBtn.setOnClickListener {
                val chemist = addChemistDialogBinding.chemistText.text.toString()
                val organised = addChemistDialogBinding.organisedText.text.toString()
                val organisedAvgSale = addChemistDialogBinding.organisedAvgSaleText.text.toString()
                val unorganised = addChemistDialogBinding.unorganisedText.text.toString()
                val unorganisedAvgSale =
                    addChemistDialogBinding.unorganisedAvgSaleText.text.toString()

                chemistData.add(ChemistData(
                    chemist,
                    organised,
                    organisedAvgSale,
                    unorganised,
                    unorganisedAvgSale
                ))

                val chemistAdapter: ChemistAdapter = ChemistAdapter(requireContext(), chemistData)
                viewBinding.chemistRcv.adapter = chemistAdapter
                viewBinding.chemistRcv.layoutManager = LinearLayoutManager(context)

                dialog.dismiss()
            }
            dialog.show()
        }

        viewBinding.addApartments.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val addApartmentDialogBinding: AddApartmentDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.add_apartment_dialog,
                null,
                false
            )
            dialog.setContentView(addApartmentDialogBinding.root)

            addApartmentDialogBinding.cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            addApartmentDialogBinding.addBtn.setOnClickListener {
                val apartments = addApartmentDialogBinding.apartmentsText.text.toString()
                val noOfHouses = addApartmentDialogBinding.noOfHouseText.text.toString()
                val distance = addApartmentDialogBinding.distanceText.text.toString()
                val types = addApartmentDialogBinding.typesText.text.toString()

                apartmentData.add(ApartmentData(
                    apartments,
                    noOfHouses,
                    distance,
                    types
                ))

                val apartmentAdapter: ApartmentAdapter =
                    ApartmentAdapter(requireContext(), apartmentData)
                viewBinding.apartmentRcv.adapter = apartmentAdapter
                viewBinding.apartmentRcv.layoutManager = LinearLayoutManager(context)

                dialog.dismiss()
            }
            dialog.show()
        }

        viewBinding.addHospitals.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val addHospitalDialogBinding: AddHospitalDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.add_hospital_dialog,
                null,
                false
            )
            dialog.setContentView(addHospitalDialogBinding.root)

            addHospitalDialogBinding.cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            addHospitalDialogBinding.addBtn.setOnClickListener {
                val hospital = addHospitalDialogBinding.hospitalNameText.text.toString()
                val beds = addHospitalDialogBinding.bedsText.text.toString()
                val speciality = addHospitalDialogBinding.specialityText.text.toString()
                val noOfPod = addHospitalDialogBinding.noOfOpdText.text.toString()
                val occupancy = addHospitalDialogBinding.occupancyText.text.toString()
                val speciality1 = addHospitalDialogBinding.speciality1Text.text.toString()

                hospitalData.add(HospitalData(
                    hospital,
                    beds,
                    speciality,
                    noOfPod,
                    occupancy,
                    speciality1
                ))

                val hospitalAdapter = HospitalAdapter(requireContext(), hospitalData)
                viewBinding.hospitalsRcv.adapter = hospitalAdapter
                viewBinding.hospitalsRcv.layoutManager = LinearLayoutManager(requireContext())

                dialog.dismiss()
            }
            dialog.show()
        }

        // Upload Site Photos

        viewBinding.plusSysmbol1.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else {
                openCamera()
            }
        }

        viewBinding.plusSysmbol2.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else {
                openCameraTwo()
            }
        }

        viewBinding.plusSysmbol3.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else {
                openCameraThree()
            }
        }

        viewBinding.plusSysmbol4.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else {
                openCameraFour()
            }
        }

        viewBinding.plusSysmbol5.setOnClickListener {
            if (!checkPermission()) {
                askPermissions(REQUEST_CODE_CAMERA)
                return@setOnClickListener
            } else {
                openCameraFive()
            }
        }

        // Delete Image
        viewBinding.redTrash1.setOnClickListener {
            deleteImage("one")
        }

        viewBinding.redTrash2.setOnClickListener {
            deleteImage("two")
        }

        viewBinding.redTrash3.setOnClickListener {
            deleteImage("three")
        }

        viewBinding.redTrash4.setOnClickListener {
            deleteImage("four")
        }

        viewBinding.redTrash5.setOnClickListener {
            deleteImage("five")
        }

        // Preview Image
        viewBinding.eyeImage1.setOnClickListener {
            previewImage("one")
        }

        viewBinding.eyeImage2.setOnClickListener {
            previewImage("two")
        }

        viewBinding.eyeImage3.setOnClickListener {
            previewImage("three")
        }

        viewBinding.eyeImage4.setOnClickListener {
            previewImage("four")
        }

        viewBinding.eyeImage5.setOnClickListener {
            previewImage("five")
        }

        // Capture Video

        val mediaController = android.widget.MediaController(requireContext())
        mediaController.setAnchorView(viewBinding.aftercapturedvideo)
        mediaController.setPadding(0, 20, 0, 0)
        mediaController.minimumHeight = 5
        viewBinding.aftercapturedvideo.setMediaController(mediaController)

        viewBinding.record.setOnClickListener {
            startRecording()
        }

        viewBinding.redTrash.setOnClickListener {
            viewBinding.aftercapturelayout.visibility = View.GONE
            viewBinding.beforecapturelayout.visibility = View.VISIBLE
            viewBinding.redTrash.visibility = View.GONE
            videoFile = null
        }

        parkingList.add("Select Parking")
        parkingList.add("Yes")
        parkingList.add("No")
        if (viewBinding.parkingSpinner != null) {
            val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, R.id.item, parkingList)
            viewBinding.parkingSpinner.adapter = adapter
        }

        viewBinding.parkingSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
//                Toast.makeText(requireContext(), parkingList[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        trafficStreetList.add("Select Type Of Traffic Street")
        trafficStreetList.add("Low")
        trafficStreetList.add("Medium")
        trafficStreetList.add("High")
        trafficStreetList.add("Very High")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, R.id.item, trafficStreetList)
        viewBinding.trafficStreetSpinner.adapter = adapter

        viewBinding.trafficStreetSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
//                Toast.makeText(requireContext(), trafficStreetList[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        trafficGeneratorsList.add("Select Traffic Generators")
        trafficGeneratorsList.add("Mall")
        trafficGeneratorsList.add("Cine")
        trafficGeneratorsList.add("Complex")
        trafficGeneratorsList.add("Restaurant")
//        trafficGeneratorsList.add("Temple")
//        trafficGeneratorsList.add("School")
//        trafficGeneratorsList.add("Education Institute")
//        trafficGeneratorsList.add("Business Centre")
//        trafficGeneratorsList.add("Residential")
//        trafficGeneratorsList.add("Busstand")
//        trafficGeneratorsList.add("Multiple")

        val trafficGeneratorsAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, R.id.item, trafficGeneratorsList)
        viewBinding.trafficGeneratorsSpinner.adapter = trafficGeneratorsAdapter

        viewBinding.trafficGeneratorsSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
//                Toast.makeText(requireContext(), trafficGeneratorsList[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        viewBinding.pharmaText.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text.isNotEmpty() && TextUtils.isDigitsOnly(text)) {
                    viewBinding.pharmaText.setText("$text%")
                    viewBinding.pharmaText.setSelection(viewBinding.pharmaText.text.toString().length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun startRecording() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
        videoFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.mp4")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile))
        } else {
            val videoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                videoFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivityForResult(intent, REQUEST_CODE_VIDEO)
    }

    private fun previewImage(imageState: String) {
        for (i in imageList.indices) {
            if (imageState.equals("one")) {
                if (imageList.get(i).imageState.equals("one")) {
                    showImagePreview(imageList.get(i).file)
                }
            } else if (imageState.equals("two")) {
                if (imageList.get(i).imageState.equals("two")) {
                    showImagePreview(imageList.get(i).file)
                }
            } else if (imageState.equals("three")) {
                if (imageList.get(i).imageState.equals("three")) {
                    showImagePreview(imageList.get(i).file)
                }
            } else if (imageState.equals("four")) {
                if (imageList.get(i).imageState.equals("four")) {
                    showImagePreview(imageList.get(i).file)
                }
            } else if (imageState.equals("five")) {
                if (imageList.get(i).imageState.equals("five")) {
                    showImagePreview(imageList.get(i).file)
                }
            }
        }
    }

    private fun showImagePreview(file: File) {
        var dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var previewImageDialogBinding: PreviewImageDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.preview_image_dialog,
            null,
            false
        )
        dialog.setContentView(previewImageDialogBinding.root)
        Glide
            .with(requireContext())
            .load(file)
            .placeholder(R.drawable.placeholder_image)
            .into(previewImageDialogBinding.previewImage)
        previewImageDialogBinding.close.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun deleteImage(imageState: String) {
        for (i in imageList.indices) {
            if (imageState.equals("one")) {
                if (imageList.get(i).imageState.equals("one")) {
                    imageList.get(i).file = File("", "")
                    viewBinding.beforecapturelayout1.visibility = View.VISIBLE
                    viewBinding.aftercapturelayout1.visibility = View.GONE
                    viewBinding.redTrash1.visibility = View.GONE
                    viewBinding.eyeImage1.visibility = View.GONE
                }
            } else if (imageState.equals("two")) {
                if (imageList.get(i).imageState.equals("two")) {
                    imageList.get(i).file = File("", "")
                    viewBinding.beforecapturelayout2.visibility = View.VISIBLE
                    viewBinding.aftercapturelayout2.visibility = View.GONE
                    viewBinding.redTrash2.visibility = View.GONE
                    viewBinding.eyeImage2.visibility = View.GONE
                }
            } else if (imageState.equals("three")) {
                if (imageList.get(i).imageState.equals("three")) {
                    imageList.get(i).file = File("", "")
                    viewBinding.beforecapturelayout3.visibility = View.VISIBLE
                    viewBinding.aftercapturelayout3.visibility = View.GONE
                    viewBinding.redTrash3.visibility = View.GONE
                    viewBinding.eyeImage3.visibility = View.GONE
                }
            } else if (imageState.equals("four")) {
                if (imageList.get(i).imageState.equals("four")) {
                    imageList.get(i).file = File("", "")
                    viewBinding.beforecapturelayout4.visibility = View.VISIBLE
                    viewBinding.aftercapturelayout4.visibility = View.GONE
                    viewBinding.redTrash4.visibility = View.GONE
                    viewBinding.eyeImage4.visibility = View.GONE
                }
            } else if (imageState.equals("five")) {
                if (imageList.get(i).imageState.equals("five")) {
                    imageList.get(i).file = File("", "")
                    viewBinding.beforecapturelayout5.visibility = View.VISIBLE
                    viewBinding.aftercapturelayout5.visibility = View.GONE
                    viewBinding.redTrash5.visibility = View.GONE
                    viewBinding.eyeImage5.visibility = View.GONE
                }
            }
        }
    }

    private fun openCameraFive() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA_FIVE)
    }

    private fun openCameraFour() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA_FOUR)
    }

    private fun openCameraThree() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA_THREE)
    }

    private fun openCameraTwo() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA_TWO)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = File(ViswamApp.context.cacheDir, "${System.currentTimeMillis()}.jpg")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile))
        } else {
            val photoUri = FileProvider.getUriForFile(
                ViswamApp.context,
                ViswamApp.context.packageName + ".provider",
                imageFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CAMERA_ONE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA_ONE) {
            if (imageFile != null && resultCode == Activity.RESULT_OK) {
                imageList.add(ImageData(imageFile!!, "", "one"))
                viewBinding.beforecapturelayout1.visibility = View.GONE
                viewBinding.aftercapturelayout1.visibility = View.VISIBLE
                viewBinding.aftercapturedimage1.setImageBitmap(BitmapFactory.decodeFile(imageFile!!.absolutePath))
                viewBinding.redTrash1.visibility = View.VISIBLE
                viewBinding.eyeImage1.visibility = View.VISIBLE
            }
        } else if (requestCode == REQUEST_CODE_CAMERA_TWO) {
            if (imageFile != null && resultCode == Activity.RESULT_OK) {
                imageList.add(ImageData(imageFile!!, "", "two"))
                viewBinding.beforecapturelayout2.visibility = View.GONE
                viewBinding.aftercapturelayout2.visibility = View.VISIBLE
                viewBinding.aftercapturedimage2.setImageBitmap(BitmapFactory.decodeFile(imageFile!!.absolutePath))
                viewBinding.redTrash2.visibility = View.VISIBLE
                viewBinding.eyeImage2.visibility = View.VISIBLE
            }
        } else if (requestCode == REQUEST_CODE_CAMERA_THREE) {
            if (imageFile != null && resultCode == Activity.RESULT_OK) {
                imageList.add(ImageData(imageFile!!, "", "three"))
                viewBinding.beforecapturelayout3.visibility = View.GONE
                viewBinding.aftercapturelayout3.visibility = View.VISIBLE
                viewBinding.aftercapturedimage3.setImageBitmap(BitmapFactory.decodeFile(imageFile!!.absolutePath))
                viewBinding.redTrash3.visibility = View.VISIBLE
                viewBinding.eyeImage3.visibility = View.VISIBLE
            }
        } else if (requestCode == REQUEST_CODE_CAMERA_FOUR) {
            if (imageFile != null && resultCode == Activity.RESULT_OK) {
                imageList.add(ImageData(imageFile!!, "", "four"))
                viewBinding.beforecapturelayout4.visibility = View.GONE
                viewBinding.aftercapturelayout4.visibility = View.VISIBLE
                viewBinding.aftercapturedimage4.setImageBitmap(BitmapFactory.decodeFile(imageFile!!.absolutePath))
                viewBinding.redTrash4.visibility = View.VISIBLE
                viewBinding.eyeImage4.visibility = View.VISIBLE
            }
        } else if (requestCode == REQUEST_CODE_CAMERA_FIVE) {
            if (imageFile != null && resultCode == Activity.RESULT_OK) {
                imageList.add(ImageData(imageFile!!, "", "five"))
                viewBinding.beforecapturelayout5.visibility = View.GONE
                viewBinding.aftercapturelayout5.visibility = View.VISIBLE
                viewBinding.aftercapturedimage5.setImageBitmap(BitmapFactory.decodeFile(imageFile!!.absolutePath))
                viewBinding.redTrash5.visibility = View.VISIBLE
                viewBinding.eyeImage5.visibility = View.VISIBLE
            }
        } else if (requestCode == REQUEST_CODE_VIDEO) {
            if (videoFile != null && resultCode == Activity.RESULT_OK) {
                val uri = data!!.data
                viewBinding.beforecapturelayout.visibility = View.GONE
                viewBinding.aftercapturelayout.visibility = View.VISIBLE
                viewBinding.redTrash.visibility = View.VISIBLE
                viewBinding.aftercapturedvideo.setVideoPath(videoFile!!.absolutePath)
                viewBinding.aftercapturedvideo.start()
            }
        }
    }

    private fun askPermissions(requestCodeCamera: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.CAMERA
                ), requestCodeCamera
            )
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), requestCodeCamera
            )
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    */