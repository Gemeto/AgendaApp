package sample

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_task.*
import receivers.AlarmReceiver
import java.util.*

class CreateTaskActivity : AppCompatActivity() {
    private var TIME24HOURS_PATTERN = "([01][0-9]|2[0-3]):[0-5][0-9]"
    private var DATE_PATTERN = "2[0-3][0-9][0-9]-(0[0-9]|1[0-2])-([012][0-9]|3[01])"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        if(intent.getStringExtra("requestCode") == RequestCode().modify_task.toString()) {
            findViewById<TextView>(R.id.taskDescription).text = intent.getStringExtra("descripcion")
            findViewById<TextView>(R.id.taskDate).text = intent.getStringExtra("date")
            findViewById<TextView>(R.id.taskBeginTime).text = intent.getStringExtra("beginTime")
            findViewById<TextView>(R.id.taskEndTime).text = intent.getStringExtra("endTime")
        }
        val transition:Transition = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)
        window.sharedElementEnterTransition = transition
        window.sharedElementReturnTransition = transition
        val f = Explode()
        window.enterTransition = f
        window.returnTransition = f
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {

            }

            override fun onTransitionEnd(transition: Transition) {
                //findViewById<Button>(R.id.createTaskButton).visibility = View.VISIBLE
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        })
        findViewById<Button>(R.id.createTaskButton).setOnClickListener {
            if(Regex(TIME24HOURS_PATTERN).containsMatchIn(taskBeginTime.text.toString())&&
                Regex(TIME24HOURS_PATTERN).containsMatchIn(taskEndTime.text.toString())&&
                Regex(DATE_PATTERN).containsMatchIn(taskDate.text.toString())) {
                AppPreferences.init(this)
                //for(mov in preferences.all){
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val task = Task(
                    "0",
                    taskDescription.text.toString(),
                    taskDate.text.toString(),
                    taskBeginTime.text.toString(),
                    taskEndTime.text.toString()
                )
                val alarmTimeAtUTC = task.beginTime.time
                var pending = PendingIntent.getBroadcast(
                    applicationContext,
                    0,
                    Intent(applicationContext, AlarmReceiver::class.java).apply {
                        putExtra("KEY_FOO_STRING", "Medium AlarmManager Demo")
                        this.action = "FOO_ACTION"
                    },
                    0
                )
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        alarmTimeAtUTC,
                        //Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 11) set(Calendar.MINUTE, 40) set(Calendar.SECOND, 0) }.timeInMillis,
                        pending
                    ),
                    pending
                )
                AppPreferences.alarmTime = alarmTimeAtUTC
                AppPreferences.descripcion = task.description
                //}

                val result = Intent()
                result.putExtra("taskId", intent.getStringExtra("taskId"))
                result.putExtra("taskDescription", taskDescription.text.toString())
                result.putExtra("taskDate", taskDate.text.toString())
                result.putExtra("taskBeginTime", taskBeginTime.text.toString())
                result.putExtra("taskEndTime", taskEndTime.text.toString())
                setResult(Activity.RESULT_OK, result)
                finish()
                overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right)
            }else
                Toast.makeText(applicationContext, "La fecha o la hora són invalidas", Toast.LENGTH_LONG).show()
        }
    }

}