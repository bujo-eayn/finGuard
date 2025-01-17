package com.example.finguard

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Log

object AESUtils {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/ECB/PKCS5Padding"

    fun encrypt(key: String, data: String): String? {
        return try {
            val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun decrypt(key: String, encryptedData: String): String? {
        return try {
            val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey)

            val decodedBytes = Base64.decode(encryptedData.trim(), Base64.DEFAULT)
            String(cipher.doFinal(decodedBytes))
        } catch (e: IllegalArgumentException) {
            Log.e("AESUtils", "Invalid Base64 input: $encryptedData", e)
            null
        } catch (e: Exception) {
            Log.e("AESUtils", "Decryption failed", e)
            null
        }
    }
}
