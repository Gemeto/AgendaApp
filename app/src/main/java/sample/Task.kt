package sample

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Task {
    var description: String
    var date: LocalDate
    var beginTime: LocalDateTime
    var endTime: LocalDateTime

    constructor(description: String, date: LocalDate, beginTime: LocalDateTime, endTime: LocalDateTime){
        this.description = description
        this.date = date
        this.beginTime = beginTime
        this.endTime = endTime
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(description: String, date: LocalDate){
        this.description = description
        this.date = date
        this.beginTime = localDateTime(date.toString(), "01:01")
        this.endTime = localDateTime(date.toString(), "01:05")
    }
}