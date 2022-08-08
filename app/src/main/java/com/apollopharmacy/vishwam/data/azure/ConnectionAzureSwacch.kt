package com.apollopharmacy.vishwam.data.azure


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

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
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
            imageBlob.upload(FileInputStream(image?.file), image?.file?.length()!!)
            val imageBlog = imageBlob!!.storageUri.primaryUri.toString()
        imagedtcl = (SwachModelResponse.Config.ImgeDtcl(image?.file, image.integerButtonCount, imageBlog, image.positionLoop))

        return imagedtcl!!
    }
}