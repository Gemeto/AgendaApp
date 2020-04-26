package utils

import java.util.*

object CalendarUtils {
    fun dateToString(c: Calendar): String{
        var tDay:String = if((c.get(Calendar.MONTH)+1) < 10) ""+c.get(Calendar.YEAR)+"-0"+(c.get(Calendar.MONTH)+1) else ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)
        tDay += if(c.get(Calendar.DAY_OF_MONTH) < 10) "-0"+c.get(Calendar.DAY_OF_MONTH) else "-"+c.get(Calendar.DAY_OF_MONTH)
        return tDay
    }

    fun timeToString(c: Calendar): String{
        return c.get(Calendar.HOUR_OF_DAY).toString().padStart(
            2,
            '0'
        ) + ":" + c.get(Calendar.MINUTE).toString().toString().padStart(2, '0')
    }

    fun firstDayOfTheWeek(c0: Calendar):String{
        val c = Calendar.getInstance()
        c.time = c0.time
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var fDay:String = if((c.get(Calendar.MONTH)+1) < 10) ""+c.get(Calendar.YEAR)+"-0"+(c.get(Calendar.MONTH)+1) else ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)
        fDay += if(c.get(Calendar.DAY_OF_MONTH) < 10) "-0"+c.get(Calendar.DAY_OF_MONTH) else "-"+c.get(Calendar.DAY_OF_MONTH)
        return fDay
    }

    fun lastDayOfTheWeek(c0: Calendar):String{
        val c = Calendar.getInstance()
        c.time = c0.time
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        var lDay:String = if((c.get(Calendar.MONTH)+1) < 10) ""+c.get(Calendar.YEAR)+"-0"+(c.get(Calendar.MONTH)+1) else ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)
        lDay += if(c.get(Calendar.DAY_OF_MONTH) < 10) "-0"+c.get(Calendar.DAY_OF_MONTH) else "-"+c.get(Calendar.DAY_OF_MONTH)
        return lDay
    }

    fun nextWeek(c0: Calendar):Calendar{
        val c = Calendar.getInstance()
        c.time = c0.time
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+7)
        return c
    }

    fun actualWeekInDay(day: Int):Calendar{
        val c = Calendar.getInstance()
        c.firstDayOfWeek = Calendar.MONDAY
        var dow = 0
        when(day){
            0 -> dow = Calendar.MONDAY
            1 -> dow = Calendar.TUESDAY
            2 -> dow = Calendar.WEDNESDAY
            3 -> dow = Calendar.THURSDAY
            4 -> dow = Calendar.FRIDAY
            5 -> dow = Calendar.SATURDAY
            6 -> dow = Calendar.SUNDAY

        }
        c.set(Calendar.DAY_OF_WEEK, dow)
        return c
    }

    fun pastWeek(c0: Calendar):Calendar{
        val c = Calendar.getInstance()
        c.time = c0.time
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-7)
        return c
    }
}