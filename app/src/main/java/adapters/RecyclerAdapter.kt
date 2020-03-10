package adapters

import activities.CreateTaskActivity
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cardview_task_in_agenda.view.*
import sample.CalendarUtils
import sample.R
import sample.RequestCode
import sample.Task
import java.util.*
import kotlin.collections.ArrayList


class RecyclerAdapter(private val tasks: ArrayList<Task>, private val orientation: Int) : RecyclerView.Adapter<RecyclerAdapter.TasksHolder>() {

    override fun onBindViewHolder(holder: TasksHolder, position: Int) {
        val itemTask = tasks[position]
        holder.bindTask(itemTask)
    }

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksHolder {
        val layout = if (orientation == Configuration.ORIENTATION_LANDSCAPE) R.layout.cardview_task_in_agenda else R.layout.cardview_task_in_agenda
        val inflatedView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TasksHolder(inflatedView, parent.context)
    }

    //1
    class TasksHolder(v: View, c: Context) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var task: Task? = null
        private var context : Context = c

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            val textDesc : TextView = v.findViewById(R.id.taskDescription)
            val textDate : TextView = v.findViewById(R.id.taskDate)
            val textBeginTime : TextView = v.findViewById(R.id.taskBeginTime)
            val textEndTime : TextView = v.findViewById(R.id.taskEndTime)
            Log.d("RecyclerView", "CLICKED " + textDesc.text.toString() + " !")
            val intent = Intent(context, CreateTaskActivity::class.java).apply {
                putExtra("descripcion", textDesc.text.toString())
                putExtra("date", textDate.text.toString())
                putExtra("beginTime", textBeginTime.text.toString())
                putExtra("endTime", textEndTime.text.toString())
                putExtra("taskId", task?.id)
                putExtra("requestCode", RequestCode().modify_task.toString())
            }
            startActivityForResult(context as Activity, intent, if(task?.id!="-1") RequestCode().modify_task
            else
                RequestCode().create_task,
                ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                    Pair(v.findViewById(R.id.bg) as View, "bgT"))
                    .toBundle())
        }

        fun bindTask(task: Task) {
            this.task = task
            if(task.id!="-1") {
                view.taskDescription.text = task.description
                var calendar = Calendar.getInstance(); calendar.time = task.date
                view.taskDate.text = CalendarUtils.dateToString(calendar)
                var beginTime = Calendar.getInstance()
                beginTime.time = task.beginTime
                view.taskBeginTime.text = beginTime.get(Calendar.HOUR_OF_DAY).toString().padStart(
                    2,
                    '0'
                ) + ":" + beginTime.get(Calendar.MINUTE).toString().padStart(2, '0')
                var endTime = Calendar.getInstance()
                endTime.time = task.endTime
                view.taskEndTime.text = endTime.get(Calendar.HOUR_OF_DAY).toString().padStart(
                    2,
                    '0'
                ) + ":" + endTime.get(Calendar.MINUTE).toString().toString().padStart(2, '0')
            }else{
                view.taskDate.text =
                    CalendarUtils.dateToString(Calendar.getInstance().apply { time = task.date })
            }
        }
    }
}