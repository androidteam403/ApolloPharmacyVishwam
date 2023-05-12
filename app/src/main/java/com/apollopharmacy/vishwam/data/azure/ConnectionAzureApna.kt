package com.apollopharmacy.vishwam.data.azure

import com.apollopharmacy.vishwam.ui.home.apnarectro.model.GetImageUrlsModelApnaResponse
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
        apnaConfigModelResponseList: GetImageUrlsModelApnaResponse,
        containerName: String,
        storageConnection: String,
        isReshoot: Boolean,
        stage: String,
    ): GetImageUrlsModelApnaResponse {
        var stagePos = "0"
        if (stage.equals("isPreRetroStage")) {
            stagePos = "1"
        } else if (stage.equals("isPostRetroStage")) {
            stagePos = "2"
        } else {
            stagePos = "3"
        }

        if (apnaConfigModelResponseList.categoryList != null && apnaConfigModelResponseList.categoryList!!.size > 0) {
            for (i in apnaConfigModelResponseList.categoryList!!) {
                for (j in i.groupingImageUrlList!!) {
                    for (k in j!!) {
                        if (isReshoot) {
                            if (k.stage.equals(stagePos) && k.status.equals("2")) {
                                var imageUrlObj = storezureBlob(k, containerName, storageConnection)
                                k.position = imageUrlObj.position
                                k.url = imageUrlObj.url
                                k.file = imageUrlObj.file
                                k.stage = imageUrlObj.stage
                                k.status = imageUrlObj.status
                                k.statusStore = imageUrlObj.statusStore
                                k.categoryid = imageUrlObj.categoryid
                                k.isReshootStatus = imageUrlObj.isReshootStatus
                                k.imageid = imageUrlObj.imageid
                                k.remarks = imageUrlObj.remarks
                                k.retorautoid = imageUrlObj.retorautoid


                            }
                        } else {
                            if (k.stage.equals(stagePos)) {
                                var imageUrlObj = storezureBlob(k, containerName, storageConnection)
                                k.position = imageUrlObj.position
                                k.url = imageUrlObj.url
                                k.file = imageUrlObj.file
                                k.stage = imageUrlObj.stage
                                k.status = imageUrlObj.status
                                k.statusStore = imageUrlObj.statusStore
                                k.categoryid = imageUrlObj.categoryid
                                k.isReshootStatus = imageUrlObj.isReshootStatus
                                k.imageid = imageUrlObj.imageid
                                k.remarks = imageUrlObj.remarks
                                k.retorautoid = imageUrlObj.retorautoid


                            }
                        }
                    }
                }
            }
        }
        return apnaConfigModelResponseList


//        if (apnaConfigModelResponseList != null) {
//
//            for (j in apnaConfigModelResponseList.categoryList!!) {
//                for (k in j.groupingImageUrlList!!) {
//                    if (isReshoot) {
//                        if (k.status.equals("2")) {
//                            var imagedtcl: GetImageUrlsModelApnaResponse.Category.ImageUrl? = null
////        val imageArrayList = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
//                            var imageBlob: CloudBlockBlob? = null
//
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && k?.file != null) {
//                                ReduceSize.reduceImageSize(k?.file!!)
//                            }
//                            val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
//                            val blobClient = cloudStorageAccount.createCloudBlobClient()
//                            val container: CloudBlobContainer =
//                                blobClient.getContainerReference(containerName)
//                            container.createIfNotExists()
//                            val containerPermissions = BlobContainerPermissions()
//                            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
//                            container.uploadPermissions(containerPermissions);
//                            imageBlob = container.getBlockBlobReference(k?.file.toString())
//                            if (k?.file != null) {
//                                imageBlob.upload(FileInputStream(k?.file), k?.file?.length()!!)
//                            }
//
//                            val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
//                            imagedtcl = (GetImageUrlsModelApnaResponse.Category.ImageUrl(
//                                k?.file,
//                                k!!.integerButtonCount,
//                                imageBlog,
//                                k.positionLoop
//                            ))
//                            k.base64Images = imagedtcl.base64Images
//                            k.positionLoop = imagedtcl.positionLoop
//                            k.file = imagedtcl.file
//                            k.integerButtonCount = imagedtcl.integerButtonCount
//                        }
//                    } else {
//                        var imagedtcl: SwachModelResponse.Config.ImgeDtcl? = null
////        val imageArrayList = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
//                        var imageBlob: CloudBlockBlob? = null
//
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && k?.file != null) {
//                            ReduceSize.reduceImageSize(k?.file!!)
//                        }
//                        val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
//                        val blobClient = cloudStorageAccount.createCloudBlobClient()
//                        val container: CloudBlobContainer =
//                            blobClient.getContainerReference(containerName)
//                        container.createIfNotExists()
//                        val containerPermissions = BlobContainerPermissions()
//                        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
//                        container.uploadPermissions(containerPermissions);
//                        imageBlob = container.getBlockBlobReference(k?.file.toString())
//                        if (k?.file != null) {
//                            imageBlob.upload(FileInputStream(k?.file), k?.file?.length()!!)
//                        }
//
//                        val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
//                        imagedtcl = (SwachModelResponse.Config.ImgeDtcl(
//                            k?.file,
//                            k!!.integerButtonCount,
//                            imageBlog,
//                            k.positionLoop
//                        ))
//                        k.base64Images = imagedtcl.base64Images
//                        k.positionLoop = imagedtcl.positionLoop
//                        k.file = imagedtcl.file
//                        k.integerButtonCount = imagedtcl.integerButtonCount
//                    }
//
//
//                }
//            }
//
//        }
//        return apnaConfigModelResponseList


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

    fun storezureBlob(
        imageUrl: GetImageUrlsModelApnaResponse.Category.ImageUrl, containerName: String,
        storageConnection: String,
    ): GetImageUrlsModelApnaResponse.Category.ImageUrl {
        var imagedtcl: GetImageUrlsModelApnaResponse.Category.ImageUrl? = null
//        val imageArrayList = ArrayList<SwachModelResponse.Config.ImgeDtcl>()
        var imageBlob: CloudBlockBlob? = null

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && imageUrl?.file != null) {
            ReduceSize.reduceImageSize(imageUrl?.file!!)
        }
        val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
        val blobClient = cloudStorageAccount.createCloudBlobClient()
        val container: CloudBlobContainer =
            blobClient.getContainerReference(containerName)
        container.createIfNotExists()
        val containerPermissions = BlobContainerPermissions()
        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
        container.uploadPermissions(containerPermissions);
        imageBlob = container.getBlockBlobReference(imageUrl?.file.toString())
        if (imageUrl?.file != null) {
            imageBlob.upload(FileInputStream(imageUrl?.file), imageUrl?.file?.length()!!)
        }

        val imageBlog = imageBlob!!.storageUri.primaryUri.toString()

        imageUrl.url = imageBlog
        return imageUrl
    }
}