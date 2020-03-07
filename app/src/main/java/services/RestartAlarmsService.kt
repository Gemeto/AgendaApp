package services

import android.app.IntentService
import android.content.Intent
import android.media.RingtoneManager

class RestartAlarmsService : IntentService("RestartAlarms") {

    override fun onHandleIntent(intent: Intent?) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)//Tono de la alarma
        val r = RingtoneManager.getRingtone(this, notification)
        r.play()

    }
}