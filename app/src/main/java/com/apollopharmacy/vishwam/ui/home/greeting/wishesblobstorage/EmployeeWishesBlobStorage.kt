package com.apollopharmacy.vishwam.ui.home.greeting.wishesblobstorage

import com.apollopharmacy.vishwam.data.Config
import com.apollopharmacy.vishwam.data.azure.ReduceSize
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object EmployeeWishesBlobStorage {
    const val storageConnectionString = Config.EMPLOYEE_WISHES_STORAGE_CONNECTIONS

    @Throws(java.lang.Exception::class)
    fun employeeBlobStorage(
        image: InputStream?,
        imageLength: Int,
        imageName: String,
    ): String? {
        val container: CloudBlobContainer = getContainer()
        container.createIfNotExists()
        val imageName = "$imageName.jpg"
        val imageBlob = container.getBlockBlobReference(imageName)
        imageBlob.upload(image, imageLength.toLong())
        return imageBlob.storageUri.primaryUri.toString()
    }

    fun getContainer(): CloudBlobContainer {
        // Retrieve storage account from connection-string.
        val storageAccount = CloudStorageAccount
            .parse(storageConnectionString)
        // Create the blob client.
        val blobClient = storageAccount.createCloudBlobClient()
        return blobClient.getContainerReference(Config.EMPLOYEE_WISHES_CONTAINAER_NAME)
    }


    fun imageBlobStorage(
        file: File, fileName : String
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