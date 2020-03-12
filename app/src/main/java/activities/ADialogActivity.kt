package activities

import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager.STREAM_ALARM
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.alarm_popup.*
import kotlinx.android.synthetic.main.cardview_task_in_agenda.taskDescription
import utils.AlarmUtils
import sample.AppPreferences
import sample.R
import android.text.method.ScrollingMovementMethod



class ADialogActivity : AppCompatActivity() {
    private lateinit var r: Ringtone

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.alarm_popup)
        bg.setBackgroundColor(Color.BLACK)
        bg.alpha = 0.8f
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        window.enterTransition = Fade()
        AppPreferences.init(this)
        taskDescription.text = AppPreferences.descripcion+"\n"
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)//Tono de la alarma
        r = RingtoneManager.getRingtone(this, notification)
        r.audioAttributes = AudioAttributes.Builder().setLegacyStreamType(STREAM_ALARM).build()
        r.isLooping = true
        r.play()
        taskDescription.movementMethod = ScrollingMovementMethod()
        apBtn.setOnClickListener {
            r.stop()
            AlarmUtils.setNextAlarm(this, false)
            finish()
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_out)
        }
        repBtn.setOnClickListener {
            r.stop()
            AlarmUtils.repeatActual(this)
            AlarmUtils.setNextAlarm(this, false)
            finish()
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_out)
        }
    }

    override fun finish() {
        r.stop()
        AlarmUtils.setNextAlarm(this, false)
        super.finish()
        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_out)
    }

}