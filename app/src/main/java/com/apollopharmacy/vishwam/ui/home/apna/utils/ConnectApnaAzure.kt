package com.apollopharmacy.vishwam.ui.home.apna.utils

import com.apollopharmacy.vishwam.data.azure.ReduceSize
import com.apollopharmacy.vishwam.ui.home.apna.activity.model.SurveyCreateRequest
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.File
import java.io.FileInputStream

class ConnectApnaAzure {
    fun connectToAzurListForApna(
        imagePathList: List<File>,
        containerName: String,
        storageConnection: String,
    ): List<SurveyCreateRequest.SiteImageMb.Image> {
        var imageUrlList = ArrayList<SurveyCreateRequest.SiteImageMb.Image>()
        if (imagePathList != null && imagePathList.size > 0) {
            for (file in imagePathList) {
                var imageBlob: CloudBlockBlob? = null

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && file != null) {
                    ReduceSize.reduceImageSize(file!!)
                }
                val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
                val blobClient = cloudStorageAccount.createCloudBlobClient()
                val container: CloudBlobContainer = blobClient.getContainerReference(containerName)
                container.createIfNotExists()
                val containerPermissions = BlobContainerPermissions()
                containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                container.uploadPermissions(containerPermissions);
                imageBlob = container.getBlockBlobReference(file.toString())
                if (file != null) {
                    imageBlob.upload(FileInputStream(file), file?.length()!!)
                }
                val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
                var image = SurveyCreateRequest.SiteImageMb.Image()
                image.url = imageBlog
                imageUrlList.add(image)
            }
        }
        return imageUrlList
    }

    fun videoConnectToAzurListForApna(
        videoPathList: List<File>,
        containerName: String,
        storageConnection: String,
    ): List<SurveyCreateRequest.VideoMb.Video> {
        var videoUrlList = ArrayList<SurveyCreateRequest.VideoMb.Video>()
        if (videoPathList != null && videoPathList.size > 0) {
            for (file in videoPathList) {
                var videoBlob: CloudBlockBlob? = null

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && file != null) {
                    ReduceSize.reduceImageSize(file!!)
                }
                val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
                val blobClient = cloudStorageAccount.createCloudBlobClient()
                val container: CloudBlobContainer = blobClient.getContainerReference(containerName)
                container.createIfNotExists()
                val containerPermissions = BlobContainerPermissions()
                containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                container.uploadPermissions(containerPermissions);
                videoBlob = container.getBlockBlobReference(file.toString())
                if (file != null) {
                    videoBlob.upload(FileInputStream(file), file?.length()!!)
                }
                val videoBlog = videoBlob!!.storageUri.primaryUri.toString()
                var video = SurveyCreateRequest.VideoMb.Video()
                video.url = videoBlog
                videoUrlList.add(video)
                return videoUrlList

            }
        }
        return videoUrlList
    }
}