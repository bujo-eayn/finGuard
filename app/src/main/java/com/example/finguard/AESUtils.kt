package com.example.finguard

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AESUtils {
    private const val ALGORITHM = "AES"

    fun encrypt(key: String, data: String): String {
        val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedData = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedData, Base64.DEFAULT)
    }

    fun decrypt(key: String, encryptedData: String): String {
        val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)
        return String(cipher.doFinal(decodedData))
    }
}
