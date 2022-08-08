package com.apollopharmacy.vishwam.ui.home.sampleui.swachuploadmodule.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apollopharmacy.vishwam.R
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.adapter.CapturedImagesListAdapter
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.adapter.UploadButtonAdapter
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse

class ConfigListAdapterSwach(
    private val configLst: SwachModelResponse,

    private val callbackInterface: UploadButtonAdapter.CallbackInterface,
    private val frontview_fileArrayList: ArrayList<SwachModelResponse>,
) :
    RecyclerView.Adapter<ConfigListAdapterSwach.ViewHolder>() {

    private lateinit var capturedListAdapter: CapturedImagesListAdapter

    private lateinit var uploadImagesRecyclerView: UploadButtonAdapter

    interface ConfigPosition {
        fun ConfigPositions(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_uploadswachh_images, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val SwachModelResponse = configLst.configlist?.get(position)

        // sets the image to the imageview from our itemHolder class
//        holder.imageView.setImageResource(ItemsViewModel.image)
//
//        // sets the text to the textview from our itemHolder class
        holder.textView.text = SwachModelResponse?.categoryName
        holder.imageCount.text = SwachModelResponse?.categoryImageUploadCount

//        val imageCount: String? = SwachModelResponse?.categoryImageUploadCount





//        uploadImagesRecyclerView = UploadButtonAdapter(position, SwachModelResponse?.imageDataDto,callbackInterface, configLst.configlist)
//        val layoutManager1 = LinearLayoutManager(context)
//        holder.uploadButtonRecycler.layoutManager = layoutManager1
//        holder.uploadButtonRecycler.itemAnimator = DefaultItemAnimator()
//        holder.uploadButtonRecycler.adapter = uploadImagesRecyclerView


//        if(SwachModelResponse?.imageDataDto !=null && SwachModelResponse?.imageDataDto!!.get(0).file!=null){
//            capturedListAdapter = CapturedImagesListAdapter(position, SwachModelResponse?.imageDataDto)
//            holder.imageRecyclerView.adapter = capturedListAdapter
//        }

//    }

}

override fun getItemCount(): Int {
    return configLst.configlist!!.size
}

class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

//    val imageView: LinearLayout = itemView.findViewById(R.id.uploadswacImages)
    val textView: TextView = itemView.findViewById(R.id.categoryname)
    val imageCount: TextView = itemView.findViewById(R.id.imageCount)
    val imageRecyclerView : RecyclerView = itemView.findViewById(R.id.imageRecyclerView)
    val uploadButtonRecycler : RecyclerView = itemView.findViewById(R.id.uploadButtonRecyclerView)
    val linearTouch : LinearLayout = itemView.findViewById(R.id.linear_touch)
//        val uploadImageCount : TextView = itemView.findViewById(R.id.uploadImageCount)

}

}