package logic.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import logic.services.RestartAlarmsService

class BootReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
            if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
                val i = Intent(context, RestartAlarmsService::class.java)
                context.startForegroundService(i)
                context.startService(i)
            } else {
                println("Received unexpected intent $intent")
            }
    }
}