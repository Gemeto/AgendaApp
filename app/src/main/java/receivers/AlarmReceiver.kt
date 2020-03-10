package receivers

import activities.ADialogActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.Notification
import sample.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import android.content.Context.NOTIFICATION_SERVICE
import utils.AlarmUtils


class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        // Is triggered when alarm goes off, i.e. receiving a system broadcast
        if (intent.action == "FOO_ACTION") {
            val i = Intent(context, ADialogActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
        AlarmUtils.setNextAlarm(context, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(context: Context){
        // This is the Notification Channel ID. Notification Channel is only for Oreo or higher
        val NOTIFICATION_CHANNEL_ID = "channel_id"
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "CHANNEL 1", NotificationManager.IMPORTANCE_DEFAULT)
        //Sets whether notifications from these Channel should be visible on Lockscreen or not
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        builder.setContentTitle(AppPreferences.descripcion)
        builder.setSmallIcon(R.drawable.navigation_empty_icon)
        val notification = builder.build()
        // Unique identifier for notification
        val NOTIFICATION_ID = 101
        //This is what will issue the notification i.e.notification will be visible
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }
}