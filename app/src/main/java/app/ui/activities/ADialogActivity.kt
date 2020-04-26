package app.ui.activities

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
import androidx.appcompat.app.AppCompatActivity
import app.logic.utils.AlarmUtils
import sample.R
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import app.logic.AppPreferences


class ADialogActivity : AppCompatActivity() {
    private lateinit var r: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //Remove notification bar
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.alarm_popup)
        findViewById<View>(R.id.bg).setBackgroundColor(Color.BLACK)
        findViewById<View>(R.id.bg).alpha = 0.8f
        if(Build.VERSION.SDK_INT > 26) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        window.enterTransition = Fade()
        AppPreferences.init(this)
        findViewById<TextView>(R.id.taskDescription).text = AppPreferences.descripcion +"\n"
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)//Tono de la alarma
        r = RingtoneManager.getRingtone(this, notification)
        r.audioAttributes = AudioAttributes.Builder().setLegacyStreamType(STREAM_ALARM).build()
        if(Build.VERSION.SDK_INT > 27)
            r.isLooping = true
        r.play()
        findViewById<TextView>(R.id.taskDescription).movementMethod = ScrollingMovementMethod()
        findViewById<Button>(R.id.apBtn).setOnClickListener {
            r.stop()
            AlarmUtils.setNextAlarm(this, false)
            finish()
            overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_out)
        }
        findViewById<Button>(R.id.repBtn).setOnClickListener {
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