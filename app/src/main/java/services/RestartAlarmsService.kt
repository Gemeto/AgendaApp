package services

import android.app.IntentService
import android.content.Intent
import utils.AlarmUtils

class RestartAlarmsService : IntentService("RestartAlarms") {

    override fun onHandleIntent(intent: Intent?) {
        AlarmUtils.setNextAlarm(this, true)
    }
}