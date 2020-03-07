package receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import sample.AppPreferences
import services.RestartAlarmsService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
                println("starting JobScheduler")
                if (context != null) {
                    AppPreferences.init(context)
                }
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                var pending = PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(context, AlarmReceiver::class.java).apply {
                        putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo")
                        this.action = "FOO_ACTION"
                    },
                    0
                )
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        AppPreferences.alarmTime,
                        //Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 11) set(Calendar.MINUTE, 40) set(Calendar.SECOND, 0) }.timeInMillis,
                        pending),
                    pending
                )
                Toast.makeText(context, "yahoo", Toast.LENGTH_LONG).show()
                val i = Intent(context, RestartAlarmsService::class.java)
                context?.startService(i)
            } else {
                println("Received unexpected intent $intent")
            }
        }
    }
}