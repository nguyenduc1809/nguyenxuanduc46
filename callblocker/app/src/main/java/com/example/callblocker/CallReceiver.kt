package com.example.callblocker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Kiểm tra trạng thái cuộc gọi
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {
                Log.d("CallBlocker", "Cuộc gọi đến từ: $incomingNumber")

                // Danh sách số bị chặn
                val blockedNumbers = listOf("1234567890", "0987654321")

                if (incomingNumber in blockedNumbers) {
                    Log.d("CallBlocker", "Số bị chặn: $incomingNumber")
                    rejectCall(context)
                }
            }
        }
    }

    // Hàm chặn cuộc gọi
    private fun rejectCall(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

            // Kiểm tra quyền trước khi gọi endCall()
            if (context.checkSelfPermission(android.Manifest.permission.ANSWER_PHONE_CALLS) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                telecomManager.endCall()
                Log.d("CallBlocker", "Cuộc gọi đã bị từ chối")
            } else {
                Log.e("CallBlocker", "Thiếu quyền ANSWER_PHONE_CALLS")
            }
        } else {
            Log.d("CallBlocker", "Không thể chặn cuộc gọi trên Android < 9")
        }
    }
}
