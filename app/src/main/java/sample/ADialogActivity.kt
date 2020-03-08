package sample

import android.media.AudioAttributes
import android.media.AudioManager.STREAM_ALARM
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.cardview_task_in_agenda.*

class ADialogActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cardview_task_in_agenda)
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
        AppPreferences.init(this)
        taskDescription.text = AppPreferences.descripcion
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)//Tono de la alarma
        val r = RingtoneManager.getRingtone(this, notification)
        r.audioAttributes = AudioAttributes.Builder().setLegacyStreamType(STREAM_ALARM).build()
        r.play()
        cardBg.setOnClickListener {
            r.stop()
            finish()
        }
    }
}