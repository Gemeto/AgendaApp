package app.logic.entities

import java.text.SimpleDateFormat
import java.util.*

class Task {
    var id: String
    var description: String
    var date: Date
    var beginTime: Date
    var endTime: Date
    var format = SimpleDateFormat("yyyy-MM-dd", Locale("Spain"))
    var formatTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("Spain"))
    var alarm: Int
    var priority: Int

    constructor(id: String, description: String, date: String, beginTime: String, endTime: String, alarm: Int, priority:Int){
        this.id = id
        this.description = description
        this.date = format.parse(date)
        this.beginTime = formatTime.parse("$date $beginTime")
        this.endTime = formatTime.parse("$date $endTime")
        this.alarm = alarm
        this.priority = priority
    }

    constructor(id:String, description: String, date: String){
        this.id = id
        this.description = description
        this.date = format.parse(date)
        this.beginTime = formatTime.parse("$date 01:01")
        this.endTime = formatTime.parse("$date 01:05")
        this.alarm = 0
        this.priority = 1
    }
}