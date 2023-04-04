package com.apollopharmacy.vishwam.data.azure

import android.os.Build
import androidx.annotation.RequiresApi
import com.apollopharmacy.vishwam.data.azure.ReduceSize.reduceImageSize
import com.apollopharmacy.vishwam.data.model.Image
import com.apollopharmacy.vishwam.data.model.ImageDataDto
import com.apollopharmacy.vishwam.ui.home.drugmodule.model.DrugRequest
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.BlobContainerPermissions
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList

object ConnectionToAzure {


    @RequiresApi(Build.VERSION_CODES.O)
    fun connectToAzur(
        image: ArrayList<Image>,
        containerName: String,
        storageConnection: String
    ): ArrayList<Image> {
        val imageArrayList = ArrayList<Image>()
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
            imageArrayList.add(Image(image[i].file, imageBlog,""))
        }
        return imageArrayList
    }






}