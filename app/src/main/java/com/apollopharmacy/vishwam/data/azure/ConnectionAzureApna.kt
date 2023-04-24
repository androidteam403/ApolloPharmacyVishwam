package com.apollopharmacy.vishwam.data.azure

import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.GetStoreWiseCatDetailsApnaResponse
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.FileInputStream

object ConnectionAzureApna {
    fun connectToAzurList(
        apnaConfigModelResponseList: ArrayList<GetStoreWiseCatDetailsApnaResponse>,
        containerName: String,
        storageConnection: String
    ): ArrayList<GetStoreWiseCatDetailsApnaResponse> {

        if (apnaConfigModelResponseList != null && apnaConfigModelResponseList.size>0){
            for (i in apnaConfigModelResponseList){
                for (j in i.configlist!!){
                    for (k in j.imageDataDto!!){
                        var imagedtcl: SwachModelResponse.Config.ImgeDtcl? = null
//        val imageArrayList = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
                        var imageBlob: CloudBlockBlob? = null

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && k?.file!=null) {
                            ReduceSize.reduceImageSize(k?.file!!)
                        }
                        val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
                        val blobClient = cloudStorageAccount.createCloudBlobClient()
                        val container: CloudBlobContainer =
                            blobClient.getContainerReference(containerName)
                        container.createIfNotExists()
                        val containerPermissions = BlobContainerPermissions()
                        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                        container.uploadPermissions(containerPermissions);
                        imageBlob = container.getBlockBlobReference(k?.file.toString())
                        if(k?.file!=null){
                            imageBlob.upload(FileInputStream(k?.file), k?.file?.length()!!)
                        }

                        val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
                        imagedtcl = (SwachModelResponse.Config.ImgeDtcl(k?.file, k!!.integerButtonCount, imageBlog, k.positionLoop))
                        k.base64Images=imagedtcl.base64Images
                        k.positionLoop=imagedtcl.positionLoop
                        k.file=imagedtcl.file
                        k.integerButtonCount=imagedtcl.integerButtonCount

                    }
                }
            }
        }
        return apnaConfigModelResponseList




//        var conf = config
//        val imageArrayList = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
//        var imageBlob: CloudBlockBlob? = null
//        for (i in config.imageDataDto?.indices!!) {
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                ReduceSize.reduceImageSize(config.imageDataDto!![i].file!!)
//            }
//            val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
//            val blobClient = cloudStorageAccount.createCloudBlobClient()
//            val container: CloudBlobContainer =
//                blobClient.getContainerReference(containerName)
//            container.createIfNotExists()
//            val containerPermissions = BlobContainerPermissions()
//            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
//            container.uploadPermissions(containerPermissions);
//            imageBlob = container.getBlockBlobReference(config.imageDataDto!![i].file.toString())
//            imageBlob.upload(FileInputStream(config.imageDataDto!![i].file), config.imageDataDto!![i].file?.length()!!)
//            val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
//
//
//            imageArrayList?.add(SwachModelResponse.Config.ImgeDtcl(config.imageDataDto!![i].file!!,
//                config.imageDataDto!![i].integerButtonCount, imageBlog, config.imageDataDto!![i].positionLoop ))
//            conf.imageDataDto = imageArrayList
//        }
//        return conf
    }
}