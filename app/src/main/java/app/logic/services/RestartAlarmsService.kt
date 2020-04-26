package app.logic.services

import android.app.IntentService
import android.content.Intent
import app.logic.utils.AlarmUtils

class RestartAlarmsService : IntentService("RestartAlarms") {

    override fun onHandleIntent(intent: Intent?) {
        AlarmUtils.setNextAlarm(this, true)
    }
}