package com.apollopharmacy.vishwam.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionManager {

    @Throws(Exception::class)
    fun decryptData(text: String, key: String): String {
        val cipher =
            Cipher.getInstance("AES/CBC/PKCS5Padding") //this parameters should not be changed

        val keyBytes = ByteArray(16)
        val b: ByteArray = key.toByteArray(charset("UTF-8"))
        var len = b.size
        if (len > keyBytes.size) len = keyBytes.size
        System.arraycopy(b, 0, keyBytes, 0, len)
        val keySpec = SecretKeySpec(keyBytes, "AES")
        val ivSpec = IvParameterSpec(keyBytes)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        var results: ByteArray? = ByteArray(text.length)
        try {
            results = cipher.doFinal(Base64.decode(text.toByteArray(), Base64.NO_PADDING))
        } catch (e: java.lang.Exception) {
            Utils.printMessage("Error in Decryption", e.toString())
        }
        Utils.printMessage("Data", String(results!!, charset("UTF-8")))
        return String(results, charset("UTF-8")) // it returns the result as a String

    }

    @Throws(Exception::class)
    fun encryptData(text: String, key: String): String? {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val keyBytes = ByteArray(16)
        val b: ByteArray = key.toByteArray(charset("UTF-8"))
        var len = b.size
        if (len > keyBytes.size) len = keyBytes.size
        System.arraycopy(b, 0, keyBytes, 0, len)
        val keySpec = SecretKeySpec(keyBytes, "AES")
        val ivSpec = IvParameterSpec(keyBytes)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        val results = cipher.doFinal(text.toByteArray())
        val data = Base64.encodeToString(results, Base64.DEFAULT)
        return data
    }
}