package logic.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import logic.receivers.AlarmReceiver
import ui.activities.AppPreferences
import logic.BDController

object AlarmUtils {

    fun setNextAlarm(context: Context, booting: Boolean){
        if(booting)
            Thread.sleep(5000)
        AppPreferences.init(context)
        var task = BDController().getNextAlarmforToday(context)
        if(task!=null)
            if(!(task.beginTime.time == AppPreferences.alarmTime && !booting)) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmTimeAtUTC = task.beginTime.time
                val pending = PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(context, AlarmReceiver::class.java),
                    0
                )
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        alarmTimeAtUTC,
                        pending
                    ),
                    pending
                )
                AppPreferences.alarmTime = task.beginTime.time
                AppPreferences.descripcion = task.description
                AppPreferences.id = task.id
                Log.e("Añadir alarma", "Alarma añadida con exito a las: "+task.beginTime.toString())
            }
    }

    fun repeatActual(context: Context) {
        AppPreferences.init(context)
        BDController().repeatAlarmNextWeek(context)
        setNextAlarm(context, false)
    }

}