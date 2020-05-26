package app.logic.receivers

import app.ui.activities.ADialogActivity
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
import app.logic.AppPreferences


class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, ADialogActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

}