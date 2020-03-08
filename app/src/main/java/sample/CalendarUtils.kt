package sample

import java.util.*

object CalendarUtils {
    fun dateToString(c: Calendar): String{
        var tDay:String = if((c.get(Calendar.MONTH)+1) < 10) ""+c.get(Calendar.YEAR)+"-0"+(c.get(Calendar.MONTH)+1) else ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)
        tDay += if(c.get(Calendar.DAY_OF_MONTH) < 10) "-0"+c.get(Calendar.DAY_OF_MONTH) else "-"+c.get(Calendar.DAY_OF_MONTH)
        return tDay
    }

    fun firstDayOfTheWeek(c0: Calendar):String{
        var c = Calendar.getInstance()
        c.time = c0.time
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var fDay:String = if((c.get(Calendar.MONTH)+1) < 10) ""+c.get(Calendar.YEAR)+"-0"+(c.get(Calendar.MONTH)+1) else ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)
        fDay += if(c.get(Calendar.DAY_OF_MONTH) < 10) "-0"+c.get(Calendar.DAY_OF_MONTH) else "-"+c.get(Calendar.DAY_OF_MONTH)
        return fDay
    }

    fun lastDayOfTheWeek(c0: Calendar):String{
        var c = Calendar.getInstance()
        c.time = c0.time
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        var lDay:String = if((c.get(Calendar.MONTH)+1) < 10) ""+c.get(Calendar.YEAR)+"-0"+(c.get(Calendar.MONTH)+1) else ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)
        lDay += if(c.get(Calendar.DAY_OF_MONTH) < 10) "-0"+c.get(Calendar.DAY_OF_MONTH) else "-"+c.get(Calendar.DAY_OF_MONTH)
        return lDay
    }

    fun nextWeek(c0: Calendar):Calendar{
        var c = Calendar.getInstance()
        c.time = c0.time
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+7)
        return c
    }

    fun pastWeek(c0: Calendar):Calendar{
        var c = Calendar.getInstance()
        c.time = c0.time
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-7)
        return c
    }
}