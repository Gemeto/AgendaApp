package app.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import app.ui.fragments.DatePickerFragment
import app.ui.fragments.TimePickerFragment
import kotlinx.android.synthetic.main.activity_create_task.*
import sample.*
import app.logic.utils.RequestCode

class CreateTaskActivity : AppCompatActivity(), TimePickerFragment.TimePickerListener, DatePickerFragment.DatePickerListener{
    private var TIME24HOURS_PATTERN = "([01][0-9]|2[0-3]):[0-5][0-9]"
    private var DATE_PATTERN = "2[0-3][0-9][0-9]-(0[0-9]|1[0-2])-([012][0-9]|3[01])"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if(intent.getStringExtra("requestCode") == RequestCode().modify_task.toString()) {
            findViewById<TextView>(R.id.taskDescription).text = intent.getStringExtra("descripcion")
            findViewById<Button>(R.id.taskDate).text = intent.getStringExtra("date")
            findViewById<Button>(R.id.taskBeginTime).text = intent.getStringExtra("beginTime")
            findViewById<Button>(R.id.taskEndTime).text = intent.getStringExtra("endTime")
            findViewById<CheckBox>(R.id.ringAlarm).isChecked = intent.getIntExtra("alarm", 0) != 0
            findViewById<RadioGroup>(R.id.priority).check(intent.getIntExtra("priority", 2))
        }
        val transition:Transition = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)
        window.sharedElementEnterTransition = transition
        window.sharedElementReturnTransition = transition
        val f = Explode()
        window.enterTransition = f
        window.returnTransition = f
        taskBeginTime.setOnClickListener{
            val tPF = TimePickerFragment()
            if(Regex(TIME24HOURS_PATTERN).containsMatchIn(taskBeginTime.text.toString())){
                tPF.arguments = Bundle()
                tPF.arguments?.putString("hour", taskBeginTime.text.split(":")[0])
                tPF.arguments?.putString("minute", taskBeginTime.text.split(":")[1])
            }
            tPF.show(supportFragmentManager, "beginTimePicker")
        }
        taskEndTime.setOnClickListener{
            val tPF = TimePickerFragment()
            if(Regex(TIME24HOURS_PATTERN).containsMatchIn(taskEndTime.text.toString())){
                tPF.arguments = Bundle()
                tPF.arguments?.putString("hour", taskEndTime.text.split(":")[0])
                tPF.arguments?.putString("minute", taskEndTime.text.split(":")[1])
            }
            tPF.show(supportFragmentManager, "endTimePicker")
        }
        taskDate.setOnClickListener {
            val dPF = DatePickerFragment()
            if(Regex(DATE_PATTERN).containsMatchIn(taskDate.text.toString())){
                dPF.arguments = Bundle()
                dPF.arguments?.putString("year", taskDate.text.split("-")[0])
                dPF.arguments?.putString("month", taskDate.text.split("-")[1])
                dPF.arguments?.putString("day", taskDate.text.split("-")[2])
            }
            dPF.show(supportFragmentManager, "endTimePicker")
        }
        findViewById<Button>(R.id.createTaskButton).setOnClickListener {
            if(!Regex(DATE_PATTERN).containsMatchIn(taskDate.text.toString()))
                Toast.makeText(applicationContext, "La fecha esta vacía", Toast.LENGTH_LONG).show()
            else if(!Regex(TIME24HOURS_PATTERN).containsMatchIn(taskBeginTime.text.toString()))
                Toast.makeText(applicationContext, "La hora de inicio está vacía", Toast.LENGTH_LONG).show()
            else if(!Regex(TIME24HOURS_PATTERN).containsMatchIn(taskEndTime.text.toString()))
                Toast.makeText(applicationContext, "La hora de fin está vacía", Toast.LENGTH_LONG).show()
            else{
                val result = Intent()
                result.putExtra("taskId", intent.getStringExtra("taskId"))
                result.putExtra("taskDescription", taskDescription.text.toString())
                result.putExtra("taskDate", taskDate.text.toString())
                result.putExtra("taskBeginTime", taskBeginTime.text.toString())
                result.putExtra("taskEndTime", taskEndTime.text.toString())
                result.putExtra("alarm", ringAlarm.isChecked)
                result.putExtra("priority", priority.checkedRadioButtonId)
                setResult(Activity.RESULT_OK, result)
                finish()
                overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right)
            }
        }
    }

    override fun applyTextBeginTime(time: String) {
        findViewById<Button>(R.id.taskBeginTime).text = time
    }

    override fun applyTextEndTime(time: String) {
        findViewById<Button>(R.id.taskEndTime).text = time
    }

    override fun applyTextDate(date: String) {
        findViewById<Button>(R.id.taskDate).text = date
    }

}