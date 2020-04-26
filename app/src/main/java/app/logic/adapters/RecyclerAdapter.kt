package app.logic.adapters

import app.ui.activities.CreateTaskActivity
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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cardview_task_in_agenda.view.*
import app.logic.utils.CalendarUtils
import sample.R
import app.logic.utils.RequestCode
import app.logic.entities.Task
import app.logic.BDController
import java.util.*
import kotlin.collections.ArrayList



class RecyclerAdapter(private val tasks: ArrayList<Task>, val orientation: Int) : RecyclerView.Adapter<RecyclerAdapter.TasksHolder>() {

    override fun onBindViewHolder(holder: TasksHolder, position: Int) {
        val itemTask = tasks[position]
        holder.bindTask(itemTask, orientation)
    }

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksHolder {
        val layout = R.layout.cardview_task_in_agenda
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
                putExtra("alarm", task?.alarm)
                putExtra("requestCode", RequestCode().modify_task.toString())
            }
            startActivityForResult(context as Activity, intent, if(task?.id!="-1") RequestCode().modify_task
            else
                RequestCode().create_task,
                ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                    Pair(v.findViewById(R.id.bg) as View, "bgT"))
                    .toBundle())
        }

        fun bindTask(task: Task, orientation: Int) {
            this.task = task
            if(task.id=="-1") {
                view.taskDate.textSize = 0F
            }else if(task.id=="-2"){
                view.taskDescription.text = task.description
                view.taskDate.text =
                    CalendarUtils.dateToString(Calendar.getInstance().apply { time = task.date })
                view.taskDescription.textSize = 20F
                view.isClickable = false
                view.findViewById<FrameLayout>(R.id.borderB).visibility = View.INVISIBLE
                view.findViewById<FrameLayout>(R.id.borderR).visibility = View.INVISIBLE
            }else{
                view.taskDescription.text = task.description
                var calendar = Calendar.getInstance(); calendar.time = task.date
                view.taskDate.text = CalendarUtils.dateToString(calendar)
                if(orientation == Configuration.ORIENTATION_LANDSCAPE)
                    view.taskDate.textSize = 0F
                var beginTime = Calendar.getInstance()
                beginTime.time = task.beginTime
                view.taskBeginTime.text = CalendarUtils.timeToString(beginTime)
                var endTime = Calendar.getInstance()
                endTime.time = task.endTime
                view.taskEndTime.text = CalendarUtils.timeToString(endTime)
                if(task.alarm == 1)
                    view.findViewById<ImageView>(R.id.alarmIcon).visibility = View.VISIBLE
            }
        }
    }

    fun removeAt(context: Context, position: Int) {
        BDController().deleteWithId(context, tasks[position].id)
        tasks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, tasks.size)
    }

}