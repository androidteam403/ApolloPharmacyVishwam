package com.apollopharmacy.vishwam.data.azure


import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.ui.home.swachhapollomodule.swachupload.model.SwachModelResponse
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.FileInputStream

object ConnectionAzureSwacch {

    fun connectToAzur(

        image: SwachModelResponse.Config.ImgeDtcl?,
        containerName: String,
        storageConnection: String
    ): SwachModelResponse.Config.ImgeDtcl{
        var imagedtcl: SwachModelResponse.Config.ImgeDtcl? = null
//        val imageArrayList = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
        var imageBlob: CloudBlockBlob? = null

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && image?.file!=null) {
                ReduceSize.reduceImageSize(image?.file!!)
            }
            val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
            val blobClient = cloudStorageAccount.createCloudBlobClient()
            val container: CloudBlobContainer =
                blobClient.getContainerReference(containerName)
            container.createIfNotExists()
            val containerPermissions = BlobContainerPermissions()
            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
            container.uploadPermissions(containerPermissions);
            imageBlob = container.getBlockBlobReference(image?.file.toString())
        if(image?.file!=null){
            imageBlob.upload(FileInputStream(image?.file), image?.file?.length()!!)
        }

            val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
        imagedtcl = (SwachModelResponse.Config.ImgeDtcl(image?.file, image!!.integerButtonCount, imageBlog, image.positionLoop))

        return imagedtcl!!
    }



    fun connectToAzurList(
        swachhModelResponseList: ArrayList<SwachModelResponse>,
        containerName: String,
        storageConnection: String
    ): ArrayList<SwachModelResponse> {

        if (swachhModelResponseList != null && swachhModelResponseList.size>0){
            for (i in swachhModelResponseList){
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
        return swachhModelResponseList




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



//    fun connectToAzurList(
//
//        image: SwachModelResponse.Config.ImgeDtcl?,
//        containerName: String,
//        storageConnection: String
//    ): SwachModelResponse.Config.ImgeDtcl{
//        var imagedtcl: SwachModelResponse.Config.ImgeDtcl? = null
//        val imageArrayList = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
//        var imageBlob: CloudBlockBlob? = null
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && image?.file!=null) {
//            ReduceSize.reduceImageSize(image?.file!!)
//        }
//        val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
//        val blobClient = cloudStorageAccount.createCloudBlobClient()
//        val container: CloudBlobContainer =
//            blobClient.getContainerReference(containerName)
//        container.createIfNotExists()
//        val containerPermissions = BlobContainerPermissions()
//        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
//        container.uploadPermissions(containerPermissions);
//        imageBlob = container.getBlockBlobReference(image?.file.toString())
//        if(image?.file!=null){
//            imageBlob.upload(FileInputStream(image?.file), image?.file?.length()!!)
//        }
//
//        val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
//        imagedtcl = (SwachModelResponse.Config.ImgeDtcl(image?.file, image!!.integerButtonCount, imageBlog, image.positionLoop))
//
//        return imagedtcl!!
//    }
}