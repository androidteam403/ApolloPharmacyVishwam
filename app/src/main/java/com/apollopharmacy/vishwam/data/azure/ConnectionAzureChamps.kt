package com.apollopharmacy.vishwam.data.azure

import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.File
import java.io.FileInputStream

object ConnectionAzureChamps {

    fun connectToAzur(

        image: File?,
        containerName: String,
        storageConnection: String
    ):String?{
        var imagedtcl: String? = null

        var imageBlob: CloudBlockBlob? = null

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            ReduceSize.reduceImageSize(image!!)
        }
        val cloudStorageAccount = CloudStorageAccount.parse(storageConnection)
        val blobClient = cloudStorageAccount.createCloudBlobClient()
        val container: CloudBlobContainer =
            blobClient.getContainerReference(containerName)
        container.createIfNotExists()
        val containerPermissions = BlobContainerPermissions()
        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
        container.uploadPermissions(containerPermissions);
        imageBlob = container.getBlockBlobReference(image?.toString())
        imageBlob.upload(FileInputStream(image), image?.length()!!)
        val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
        imagedtcl = (imageBlog!!)


        return imagedtcl!!
    }
}