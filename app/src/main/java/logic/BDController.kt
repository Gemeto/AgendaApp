package logic

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import bd.BDManager
import ui.activities.AppPreferences
import logic.entities.Task
import utils.CalendarUtils
import utils.RequestCode
import java.text.SimpleDateFormat
import java.util.*

class BDController {

    fun deleteWithId(context: Context, id: String){
        val bd = BDManager(context)
        val db = bd.writableDatabase
        db.delete("TASK", "key = $id", null)
    }

    fun pendingTasksToday(context: Context):Int {
        var pending = 0
        val bdHelper = BDManager(context)
        val db = bdHelper.readableDatabase
        val t = Calendar.getInstance()
        val today = CalendarUtils.dateToString(Calendar.getInstance())
        val tEnd = Calendar.getInstance().apply { set(Calendar.HOUR, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59) }
        val todaysEnd = CalendarUtils.dateToString(tEnd)
        val hourToday = ""+ CalendarUtils.timeToString(t).split(":")[0]
        val minuteNow = ""+ CalendarUtils.timeToString(t).split(":")[1]
        val cursor = db.query(
            "TASK",
            null,
            "date BETWEEN '$today' AND '$todaysEnd' " +
                    "AND ((CAST(substr(beginTime, 1, 2) as INTEGER) = $hourToday AND CAST(substr(beginTime, 4, 2) as INTEGER) BETWEEN "+(minuteNow.toInt()+1)+" AND 59) "+
                    "OR (CAST(substr(beginTime, 1, 2) as INTEGER)  BETWEEN "+(hourToday.toInt()+1)+" AND 23))",
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                pending++
            }
        }
        return pending
    }

    fun getNextAlarmforToday(context: Context): Task? {
        val bdHelper = BDManager(context)
        val db = bdHelper.readableDatabase
        val t = Calendar.getInstance()
        val today = CalendarUtils.dateToString(Calendar.getInstance())
        val tEnd = Calendar.getInstance().apply { set(Calendar.HOUR, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59) }
        val todaysEnd = CalendarUtils.dateToString(tEnd)
        val hourToday = ""+ CalendarUtils.timeToString(t).split(":")[0]
        val minuteNow = ""+ CalendarUtils.timeToString(t).split(":")[1]
        val cursor = db.query(
            "TASK",
            null,
            "(date BETWEEN '$today' AND '$todaysEnd') AND (alarm = 1) " +
                    "AND ((CAST(substr(beginTime, 1, 2) as INTEGER) = $hourToday AND CAST(substr(beginTime, 4, 2) as INTEGER) BETWEEN $minuteNow AND 59) "+
                    "OR (CAST(substr(beginTime, 1, 2) as INTEGER)  BETWEEN "+(hourToday.toInt()+1)+" AND 23))",
            null,
            null,
            null,
            "beginTime ASC"
        )
        var pending: Task? = null
        with(cursor) {
            while (moveToNext()) {
                pending = Task(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(4),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(5)
                )
                break
            }
        }
        return pending
    }

    fun repeatAlarmNextWeek(context: Context){
        val bd = BDManager(context)
        val db = bd.writableDatabase
        val dbr = bd.readableDatabase
        val cursor = dbr.query(
            "TASK",
            null ,
            "key = ${AppPreferences.id}",
            null,
            null,
            null,
            null
        )
        val calendar = Calendar.getInstance()
        with(cursor) {
            while (moveToNext()) {
                calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale("Spain")).parse(cursor.getString(4))
                db.insert("TASK", null, ContentValues().apply {
                    put("descripcion", cursor.getString(1))
                    put("beginTime", cursor.getString(2))
                    put("endTime", cursor.getString(3))
                    put("date",
                        CalendarUtils.dateToString(
                            CalendarUtils.nextWeek(calendar)
                        )
                    )
                })
            }
        }
    }

    fun getTasksContaining(context: Context, query: String): ArrayList<Task>{
        var list = ArrayList<Task>()
        val bdHelper = BDManager(context)
        val db = bdHelper.readableDatabase
        val cursor = db.query(
            "TASK",
            null ,
            "LOWER(descripcion) LIKE LOWER('%$query%') OR beginTime LIKE '%$query%' OR endTime LIKE '%$query%' OR date lIKE '%$query%'",
            null,
            null,
            null,
            "date ASC, beginTime ASC"
        )
        with(cursor) {
            while (moveToNext()) {
                list.add(
                    Task(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(4),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(5)
                    )
                )
            }
        }
        return list
    }

    fun getTasks(context: Context): ArrayList<Task>{
        var list = ArrayList<Task>()
        val bdHelper = BDManager(context)
        val db = bdHelper.readableDatabase
        val cursor = db.query(
            "TASK",
            null ,
            null,
            null,
            null,
            null,
            "date ASC, beginTime ASC"
        )
        with(cursor) {
            while (moveToNext()) {
                list.add(
                    Task(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(4),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(5)
                    )
                )
            }
        }
        return list
    }

    fun getTasksOfWeek(context: Context, date: Calendar): ArrayList<ArrayList<Task>>{
        val bdHelper = BDManager(context)
        var list = ArrayList<ArrayList<Task>>()
        for (i in 0..6) {
            list.add(ArrayList<Task>())
        }
        val db = bdHelper.readableDatabase
        val monday = CalendarUtils.firstDayOfTheWeek(date)
        val sunday = CalendarUtils.lastDayOfTheWeek(date)
        //findViewById<TextView>(R.id.week).text = "$monday $sunday"
        val cursor = db.query(
            "TASK",
            null,
            "date BETWEEN '$monday' AND '$sunday'",
            null,
            null,
            null,
            "date ASC, beginTime ASC"
        )
        with(cursor) {
            while (moveToNext()) {
                val task = Task(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(4),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(5)
                )
                val calendar: Calendar = Calendar.getInstance()
                calendar.time = task.date
                when (calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> list[0].add(task)
                    Calendar.TUESDAY -> list[1].add(task)
                    Calendar.WEDNESDAY -> list[2].add(task)
                    Calendar.THURSDAY -> list[3].add(task)
                    Calendar.FRIDAY -> list[4].add(task)
                    Calendar.SATURDAY -> list[5].add(task)
                    Calendar.SUNDAY -> list[6].add(task)

                }
            }
        }
        return list
    }

    fun taskCreation(requestCode:Int, data: Intent?, context: Context){
        val bdHelper = BDManager(context)
        val bd = bdHelper.writableDatabase
        //Creamos un contenido de valores con los datos de la tarea
        val values = ContentValues().apply {
            put("descripcion", ((data?.extras as Bundle)["taskDescription"] as Any).toString())
            put("beginTime", ((data.extras as Bundle)["taskBeginTime"] as Any).toString())
            put("endTime", ((data.extras as Bundle)["taskEndTime"] as Any).toString())
            put("date", ((data.extras as Bundle)["taskDate"] as Any).toString())
            put("alarm", ((data.extras as Bundle)["alarm"] as Any) as Boolean)
        }
        if (requestCode == RequestCode().create_task) {
            // Insertamos la tarea en la base de datos
            bd?.insert("TASK", null, values)
        } else if (requestCode == RequestCode().modify_task) {
            // Modificamos la tarea en la base de datos
            bd?.update("TASK", values, "key = " + ((data?.extras as Bundle)["taskId"] as Any).toString(), null)
        }
        bd.close()
    }

}