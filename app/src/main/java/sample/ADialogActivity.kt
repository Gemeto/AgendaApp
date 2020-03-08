package sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.cardview_task_in_agenda.*

class ADialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cardview_task_in_agenda)
        AppPreferences.init(this)
        taskDescription.text = AppPreferences.descripcion
        cardBg.setOnClickListener {
            finish()
        }
    }
}