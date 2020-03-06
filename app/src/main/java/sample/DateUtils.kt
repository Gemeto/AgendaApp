package sample

import java.util.*

object DateUtils {
    fun firstDayOfTheWeek(c: Calendar):String{
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var fDay:String = if((c.get(Calendar.MONTH)+1) < 10) ""+c.get(Calendar.YEAR)+"-0"+(c.get(Calendar.MONTH)+1) else ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)
        fDay += if(c.get(Calendar.DAY_OF_MONTH) < 10) "-0"+c.get(Calendar.DAY_OF_MONTH) else "-"+c.get(Calendar.DAY_OF_MONTH)
        return fDay
    }

    fun lastDayOfTheWeek(c: Calendar):String{
        c.firstDayOfWeek = Calendar.MONDAY
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        var lDay:String = if((c.get(Calendar.MONTH)+1) < 10) ""+c.get(Calendar.YEAR)+"-0"+(c.get(Calendar.MONTH)+1) else ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)
        lDay += if(c.get(Calendar.DAY_OF_MONTH) < 10) "-0"+c.get(Calendar.DAY_OF_MONTH) else "-"+c.get(Calendar.DAY_OF_MONTH)
        return lDay
    }
}