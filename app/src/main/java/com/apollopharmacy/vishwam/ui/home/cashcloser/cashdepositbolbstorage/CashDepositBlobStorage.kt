package com.apollopharmacy.vishwam.ui.home.cashcloser.cashdepositbolbstorage

import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.azure.ReduceSize
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.File
import java.io.FileInputStream

object CashDepositBlobStorage {
    const val storageConnectionString = Config.EMPLOYEE_WISHES_STORAGE_CONNECTIONS

    fun getContainer(): CloudBlobContainer {
        // Retrieve storage account from connection-string.
        val storageAccount = CloudStorageAccount
            .parse(storageConnectionString)
        // Create the blob client.
        val blobClient = storageAccount.createCloudBlobClient()
        return blobClient.getContainerReference(Config.EMPLOYEE_WISHES_CONTAINAER_NAME)
    }


    fun captureImageBlobStorage(
        file: File, fileName: String,
    ): String {

        var imageBlob: CloudBlockBlob? = null

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N && file != null) {
            ReduceSize.reduceImageSize(file)
        }
        val cloudStorageAccount =
            CloudStorageAccount.parse(Config.EMPLOYEE_WISHES_STORAGE_CONNECTIONS)
        val blobClient = cloudStorageAccount.createCloudBlobClient()
        val container: CloudBlobContainer =
            blobClient.getContainerReference(Config.EMPLOYEE_WISHES_CONTAINAER_NAME)
        container.createIfNotExists()
        val containerPermissions = BlobContainerPermissions()
        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
        container.uploadPermissions(containerPermissions);
        imageBlob = container.getBlockBlobReference(fileName)
        if (file != null) {
            imageBlob.upload(FileInputStream(file), file.length()!!)
        }

        val imageBlog = imageBlob!!.storageUri.primaryUri.toString()

        return imageBlog
    }
}