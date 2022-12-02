package com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.databinding.ActivityQcPreviewImageBinding
import com.apollopharmacy.vishwam.ui.home.qcfail.model.QcItemListResponse
import com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage.adapter.QcPreviewAdapter

class QcPreviewImageActivity : AppCompatActivity(), QcPreviewCallbacks,
    ViewPager.OnPageChangeListener {

    lateinit var activityQcPreviewImageBinding: ActivityQcPreviewImageBinding

    var imageUrl: String? = ""
    var orderNo: String? = ""
    var itemName: String? = ""
    var position: String? = ""

    lateinit var previewImageAdapter: QcPreviewAdapter
    var qcItemList = ArrayList<QcItemListResponse.Item>()
    var list = ArrayList<String>()
    var currentPosition: Int = 0
    lateinit var imageUrls: List<String>
    var imagePosition:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityQcPreviewImageBinding = DataBindingUtil.setContentView(this, R.layout.activity_qc__preview__image)

        imageUrl = intent.getStringExtra("itemList")
        orderNo = intent.getStringExtra("orderid")
        itemName = intent.getStringExtra("itemName")
        position = intent.getStringExtra("position")

        setUp()
    }

    fun setUp() {
        imageUrls = imageUrl!!.split(";")
        //Back button
        activityQcPreviewImageBinding.back.setOnClickListener {
            finish()
        }
        activityQcPreviewImageBinding.itemName.setText(itemName)
        activityQcPreviewImageBinding.orderid.setText(orderNo)


        if (imagePosition == 0) {
            activityQcPreviewImageBinding.startarrow.visibility = View.GONE
        }

        activityQcPreviewImageBinding.totalText.setText("Total Images"+" ( "+(imagePosition+1/imageUrls.size+1).toString()+" / ")
        activityQcPreviewImageBinding.totalimages.setText(imageUrls.size.toString()+" )")

        previewImageAdapter = QcPreviewAdapter(applicationContext, list, qcItemList,this, currentPosition,this,imageUrls)

        activityQcPreviewImageBinding.previewImageViewpager.adapter = previewImageAdapter
        activityQcPreviewImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityQcPreviewImageBinding.previewImageViewpager.setCurrentItem(currentPosition, true)
    }

    override fun onClickBack() {
    }

    override fun statusDisplay(position: Int, status: String?) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (position == 0) {
            activityQcPreviewImageBinding.startarrow.visibility = View.GONE
        } else {
            activityQcPreviewImageBinding.startarrow.visibility = View.VISIBLE
        }

        if (position == imageUrls.size - 1) {
            activityQcPreviewImageBinding.endarrow.visibility = View.GONE
        } else {
            activityQcPreviewImageBinding.endarrow.visibility = View.VISIBLE
        }

        activityQcPreviewImageBinding.totalText.setText("Total Images"+" ( "+(position+1/imageUrls.size+1).toString()+" / ")
        activityQcPreviewImageBinding.totalimages.setText(imageUrls.size.toString()+" )")

    }

    override fun onPageScrollStateChanged(state: Int) {
    }


}