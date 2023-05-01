package com.apollopharmacy.vishwam.data.azure

import com.apollopharmacy.vishwam.ui.home.apna.activity.model.ImageDto
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.FileInputStream

object ConnectionAzureApnaSurvey {
    fun connectToAzur(
        image: ArrayList<ImageDto>,
        containerName: String,
        storageConnection: String
    ): ArrayList<ImageDto> {
        val imageArrayList = ArrayList<ImageDto>()
        var imageBlob: CloudBlockBlob? = null
        for (i in image.indices) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                ReduceSize.reduceImageSize(image[i].file)
            }
            val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
            val blobClient = cloudStorageAccount.createCloudBlobClient()
            val container: CloudBlobContainer =
                blobClient.getContainerReference(containerName)
            container.createIfNotExists()
            val containerPermissions = BlobContainerPermissions()
            containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
            container.uploadPermissions(containerPermissions);
            imageBlob = container.getBlockBlobReference(image[i].file.toString())
            imageBlob.upload(FileInputStream(image[i].file), image[i].file.length())
            val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
            imageArrayList.add(ImageDto(image[i].file, imageBlog))
        }
        return imageArrayList
    }
}