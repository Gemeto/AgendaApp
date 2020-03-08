package receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.media.RingtoneManager
import androidx.core.content.ContextCompat.startActivity
import sample.ADialogActivity


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Is triggered when alarm goes off, i.e. receiving a system broadcast
        if (intent.action == "FOO_ACTION") {
            val fooString = intent.getStringExtra("KEY_FOO_STRING")
            //Toast.makeText(context, fooString, Toast.LENGTH_LONG).show()
            var i = Intent(context, ADialogActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(context, i, null)
        }
    }
}