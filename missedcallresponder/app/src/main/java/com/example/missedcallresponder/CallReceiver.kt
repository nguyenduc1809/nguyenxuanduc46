package com.example.missedcallresponder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.PHONE_STATE") {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            // Log để debug
            Log.d("CallReceiver", "State: $state Number: $number")

            if (state != null) {
                // Khi điện thoại bắt đầu đổ chuông
                if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                    incomingNumber = number
                    previousState = state
                } else if (state == TelephonyManager.EXTRA_STATE_IDLE
                    && previousState == TelephonyManager.EXTRA_STATE_RINGING
                ) {
                    if (incomingNumber != null) {
                        // Gửi SMS cho số gọi nhỡ
                        sendSMS(context, incomingNumber)
                        incomingNumber = null
                    }
                    previousState = state
                } else if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                    previousState = state
                }
            }
        }
    }

    private fun sendSMS(context: Context, phoneNumber: String?) {
        try {
            val smsManager = SmsManager.getDefault()
            val message = "Xin lỗi, tôi đang bận. Tôi sẽ gọi lại cho bạn sau."
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("CallReceiver", "SMS sent to $phoneNumber")
        } catch (e: Exception) {
            Log.e("CallReceiver", "SMS failed to send: " + e.message)
            e.printStackTrace()
        }
    }

    companion object {
        private var previousState: String = TelephonyManager.EXTRA_STATE_IDLE
        private var incomingNumber: String? = null
    }
}