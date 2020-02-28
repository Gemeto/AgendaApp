package sample

import android.os.Build
import android.os.Bundle
import android.transition.*
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class CreateTaskActivity : AppCompatActivity() {
    private val TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]"
    private val DATE_PATTERN = "20[0-9][0-9]-[0-9][0-9]-[0-9][0-9]"
    private var bTimeIsValid = false
    private var eTimeIsValid = false
    private var dateIsValid = false


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        findViewById<TextView>(R.id.taskDescription).hint = intent.getStringExtra("descripcion")
        val transition:Transition = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)
        transition.duration = 1000
        window.sharedElementEnterTransition = transition
        window.sharedElementReturnTransition = transition
        val f = Explode()
        f.duration = 1000
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
    }

}