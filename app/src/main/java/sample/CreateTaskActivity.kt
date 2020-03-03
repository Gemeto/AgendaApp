package sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_task.*

class CreateTaskActivity : AppCompatActivity() {
    private val TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]"
    private val DATE_PATTERN = "20[0-9][0-9]-[0-9][0-9]-[0-9][0-9]"
    private var bTimeIsValid = false
    private var eTimeIsValid = false
    private var dateIsValid = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        findViewById<TextView>(R.id.taskDescription).text = intent.getStringExtra("descripcion")
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
            val result = Intent()
            result.putExtra("taskDescription", taskDescription.text.toString())
            result.putExtra("taskDate", taskDate.text.toString())
            result.putExtra("taskBeginTime", taskBeginTime.text.toString())
            result.putExtra("taskEndTime", taskEndTime.text.toString())
            setResult(Activity.RESULT_OK, result)
            finish()
            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right)
        }
    }

}