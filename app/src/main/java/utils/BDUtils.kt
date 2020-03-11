package utils

import android.content.Context
import bd.BDManager
import java.util.*

class BDUtils {

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
        val hourToday = ""+CalendarUtils.timeToString(t).toString().split(":")[0]
        val minuteNow = ""+CalendarUtils.timeToString(t).toString().split(":")[1]
        val cursor = db.query(
            "TASK",
            null,
            "date BETWEEN '$today' AND '$todaysEnd' " +
                    "AND CAST(substr(beginTime, 1, 2) as INTEGER) BETWEEN $hourToday AND 23 " +
                    "AND CAST(substr(beginTime, 4, 2) as INTEGER) BETWEEN $minuteNow AND 59",
            null,
            null,
            null,
            "date DESC"
        )
        with(cursor) {
            while (moveToNext()) {
                pending++
            }
        }
        return pending
    }

}