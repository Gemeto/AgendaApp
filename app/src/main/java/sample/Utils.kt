package sample

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent



@RequiresApi(Build.VERSION_CODES.O)
fun localDateTime(date:String, time:String): LocalDateTime {
    return LocalDateTime.parse("$date $time", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
}