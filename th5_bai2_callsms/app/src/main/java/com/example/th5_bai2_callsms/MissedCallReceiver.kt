package com.example.th5_bai2_callsms
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.telephony.SmsManager
import android.util.Log

class MissedCallReceiver : BroadcastReceiver(){
    private var lastState: String = TelephonyManager.EXTRA_STATE_IDLE
    private var lastIncomingNumber: String? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == null) return

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                // Khi có cuộc gọi đến
                lastIncomingNumber = incomingNumber
            } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                // Khi cuộc gọi kết thúc (có thể là cuộc gọi nhỡ)
                if (lastState == TelephonyManager.EXTRA_STATE_RINGING && lastIncomingNumber != null) {
                    sendAutoReplySMS(context, lastIncomingNumber!!)
                }
                lastIncomingNumber = null
            }

            lastState = state
        }
    }

    private fun sendAutoReplySMS(context: Context, phoneNumber: String) {
        try {
            val smsManager = SmsManager.getDefault()
            val message = "Xin chào, tôi đang bận. Tôi sẽ gọi lại cho bạn sau!"
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("MissedCallReceiver", "Đã gửi SMS đến: $phoneNumber")
        } catch (e: Exception) {
            Log.e("MissedCallReceiver", "Gửi SMS thất bại", e)
        }
    }
}