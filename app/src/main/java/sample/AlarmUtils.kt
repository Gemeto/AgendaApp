package sample

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import bd.BDManager
import receivers.AlarmReceiver
import java.util.*

object AlarmUtils {

    fun setNextAlarm(context: Context, booting: Boolean){
        if(booting)
            Thread.sleep(5000)
        AppPreferences.init(context)
        var bd = BDManager(context)
        val db = bd.readableDatabase
        var calendar = Calendar.getInstance()
        var today = CalendarUtils.dateToString(calendar)
        var hourToday = ""+calendar.get(Calendar.HOUR_OF_DAY)
        var minuteToday = ""+calendar.get(Calendar.MINUTE)
        val cursor = db.query(
            "TASK",
            null ,
            "date = '$today' AND " +
                    "CAST(substr(beginTime, 1, 2) as INTEGER) >= CAST($hourToday as INTEGER)",
            null,
            null,
            null,
            "beginTime ASC"
        )

        with(cursor) {
            while (moveToNext()) {
                var task = Task(cursor.getString(0), cursor.getString(1), cursor.getString(4), cursor.getString(2), cursor.getString(3))
                if(cursor.getString(2).trim().split(":")[0].toInt() == hourToday.toInt()
                    && cursor.getString(2).trim().split(":")[1].toInt() < minuteToday.toInt()) {
                    bd.close()
                    db.close()
                    continue
                }
                if(task.beginTime.time == AppPreferences.alarmTime && !booting) {
                    bd.close()
                    db.close()
                    break
                }else {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val alarmTimeAtUTC = task.beginTime.time
                    var pending = PendingIntent.getBroadcast(
                        context,
                        0,
                        Intent(context, AlarmReceiver::class.java).apply {
                            this.action = "FOO_ACTION"
                        },
                        0
                    )
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(
                            alarmTimeAtUTC,
                            //Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 11) set(Calendar.MINUTE, 40) set(Calendar.SECOND, 0) }.timeInMillis,
                            pending
                        ),
                        pending
                    )
                    AppPreferences.alarmTime = task.beginTime.time
                    AppPreferences.descripcion = task.description
                    bd.close()
                    db.close()
                    Log.e("Añadir alarma", "Alarma añadida con exito a las: "+task.beginTime.toString() + " $hourToday:$minuteToday")
                    break
                }
            }
        }
    }

}