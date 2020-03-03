package sample

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Task {
    var id: String
    var description: String
    var date: Date
    var beginTime: Date
    var endTime: Date
    var format = SimpleDateFormat("yyyy-MM-dd")
    var formatTime = SimpleDateFormat("yyyy-MM-dd HH:mm")

    constructor(id: String, description: String, date: Date, beginTime: Date, endTime: Date){
        this.id = id
        this.description = description
        this.date = date
        this.beginTime = beginTime
        this.endTime = endTime
    }

    constructor(id:String, description: String, date: String){
        this.id = id
        this.description = description
        this.date = format.parse(date)
        this.beginTime = formatTime.parse("$date 01:01")
        this.endTime = formatTime.parse("$date 01:05")
    }
}