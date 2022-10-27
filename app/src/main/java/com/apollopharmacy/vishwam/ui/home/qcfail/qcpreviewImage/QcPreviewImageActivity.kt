package com.apollopharmacy.vishwam.ui.home.qcfail.qcpreviewImage

import android.os.Bundle
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
    private var previewImageAdapter: QcPreviewAdapter? = null
    private var currentPosition: Int = 0
    private var imageUrl: String = ""
    private var orderNo: String = ""
    private var itemName: String = ""

    var qcItemList = ArrayList<QcItemListResponse.Item>()
    var position: Int = 0
    var list = ArrayList<String>()
    lateinit var imageUrlList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityQcPreviewImageBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_qc__preview__image)
        if (intent != null) {
            imageUrl =
                getIntent().getExtras()?.getString("itemList")!!
            orderNo =
                getIntent().getExtras()?.getString("orderid")!!
            itemName =
                getIntent().getExtras()?.getString("itemName")!!
            // currentPosition = getIntent().getExtras()?.getInt("position")!!

        }
        val imageUrlList = imageUrl.split(";")


        setUp()

    }

    private fun setUp() {
        imageUrlList = imageUrl.split(";")

        activityQcPreviewImageBinding.back.setOnClickListener {

            finish()
        }


        if (orderNo.equals("null") || orderNo.isNullOrEmpty()) {
            activityQcPreviewImageBinding.orderid.setText("-")

        } else {
            activityQcPreviewImageBinding.orderid.setText(orderNo)

        }
        activityQcPreviewImageBinding.itemName.setText(itemName)


        activityQcPreviewImageBinding.totalText.setText("Total Images"+" ( "+(position+1/imageUrlList.size+1).toString()+" / ")
        activityQcPreviewImageBinding.totalimages.setText(imageUrlList.size.toString()+" )")

        previewImageAdapter = QcPreviewAdapter(applicationContext,
            list,
            qcItemList,
            this,
            currentPosition,
            imageUrlList)


        activityQcPreviewImageBinding.previewImageViewpager.addOnPageChangeListener(this)
        activityQcPreviewImageBinding.previewImageViewpager.adapter = previewImageAdapter
        activityQcPreviewImageBinding.previewImageViewpager.setCurrentItem(currentPosition, true)

    }

    override fun onClickBack() {
    }

    override fun statusDisplay(position: Int, status: String?) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
//        activityQcPreviewImageBinding.totalText.setText("Total Images"+" ( "+imageUrlList.size.toString()+" / ")
//        activityQcPreviewImageBinding.totalimages.setText((position+1/imageUrlList.size+1).toString()+" )")
        activityQcPreviewImageBinding.totalText.setText("Total Images"+" ( "+(position+1/imageUrlList.size+1).toString()+" / ")
        activityQcPreviewImageBinding.totalimages.setText(imageUrlList.size.toString()+" )")

    }

    override fun onPageScrollStateChanged(state: Int) {
    }
}