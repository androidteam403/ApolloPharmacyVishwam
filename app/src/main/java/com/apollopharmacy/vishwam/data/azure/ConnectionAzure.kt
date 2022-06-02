package com.apollopharmacy.vishwam.data.azure

import com.apollopharmacy.vishwam.data.azure.ReduceSize.reduceImageSize
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.FileInputStream

object ConnectionAzure {

    fun connectToAzur(
        image: ArrayList<ImageDataDto>,
        containerName: String,
        storageConnection: String
    ): ArrayList<ImageDataDto> {
        val imageArrayList = ArrayList<ImageDataDto>()
        var imageBlob: CloudBlockBlob? = null
        for (i in image.indices) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                reduceImageSize(image[i].file)
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
            imageArrayList.add(ImageDataDto(image[i].file, imageBlog))
        }
        return imageArrayList
    }
}