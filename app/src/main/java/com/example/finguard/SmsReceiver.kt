import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.example.finguard.AESUtils

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle: Bundle? = intent.extras
        try {
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                for (pdu in pdus) {
                    val sms = SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = sms.originatingAddress
                    val messageBody = sms.messageBody

                    // Log the message for testing (remove in production)
                    Log.d("SmsReceiver", "Received SMS from $sender: $messageBody")

                    // Check if the SMS is from M-Pesa
                    if (sender != null && sender.contains("M-Pesa", ignoreCase = true)) {
                        // Process the SMS (e.g., obfuscate, encrypt, store)
                        val obfuscatedMessage = obfuscateSmsContent(messageBody)
                        Log.d("SmsReceiver", "Obfuscated SMS: $obfuscatedMessage")

                        // TODO: Encrypt and save the obfuscated message
                        val encryptedMessage = encryptMessage(obfuscatedMessage)

                        saveToLocalStorage(context, encryptedMessage)
                        Toast.makeText(context, "New M-Pesa SMS intercepted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun obfuscateSmsContent(message: String): String {
        return message.replace(Regex("\\bKES\\s?\\d+[.,]?\\d*\\b"), "KES [REDACTED]")
            .replace(Regex("\\b[A-Za-z]+\\s[A-Za-z]+\\b"), "[NAME REDACTED]")
    }

    private fun encryptMessage(message: String): String {
        // Simple AES encryption (replace with a secure key and proper implementation)
        val key = "1234567890123456" // Must be 16 characters
        return AESUtils.encrypt(key, message)
    }

    private fun saveToLocalStorage(context: Context, encryptedMessage: String) {
        val sharedPreferences = context.getSharedPreferences("FinGuardMessages", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(System.currentTimeMillis().toString(), encryptedMessage)
        editor.apply()
    }
}
