package sample

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*
import androidx.core.content.ContextCompat.startActivity
import androidx.transition.*
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.view.*


class RecyclerAdapter(private val tasks: ArrayList<Task>, private val orientation: Int) : RecyclerView.Adapter<RecyclerAdapter.TasksHolder>() {

    override fun onBindViewHolder(holder: TasksHolder, position: Int) {
        val itemTask = tasks[position]
        holder.bindTask(itemTask)
    }

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksHolder {
        val layout = if (orientation == Configuration.ORIENTATION_LANDSCAPE) R.layout.cardview_task_in_agenda else R.layout.recyclerview_item_row
        val inflatedView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TasksHolder(inflatedView, parent.context)
    }

    //1
    class TasksHolder(v: View, c: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var task: Task? = null
        var context : Context = c

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            val textView : TextView = v.findViewById(R.id.taskDescription)
            Log.d("RecyclerView", "CLICKED " + textView.text.toString() + " !")
            val intent = Intent(context, CreateTaskActivity::class.java).apply { putExtra("descripcion", textView.text.toString()) }
            startActivityForResult(context as Activity, intent, 1,
                ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                    Pair(v.findViewById(R.id.bg) as View, "bgT"))
                    .toBundle())
        }

        fun bindTask(task: Task) {
            this.task = task
            view.taskDescription.text = task.description
            //view.taskDate.hint = task.date.toString()
            //view.taskBeginTim.hint = task.beginTime.hour.toString()+":"+task.beginTime.minute.toString()
            //view.taskEndTime.hint = task.endTime.hour.toString()+":"+task.endTime.minute.toString()
        }
    }
}