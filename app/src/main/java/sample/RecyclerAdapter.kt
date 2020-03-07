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
                putExtra("descripcion", textDesc.text.toString());
                putExtra("date", textDate.text.toString());
                putExtra("beginTime", textBeginTime.text.toString());
                putExtra("endTime", textEndTime.text.toString());
                putExtra("taskId", task?.id);
                putExtra("requestCode", RequestCode().modify_task.toString())
            }
            startActivityForResult(context as Activity, intent, RequestCode().modify_task,
                ActivityOptions.makeSceneTransitionAnimation(context as Activity,
                    Pair(v.findViewById(R.id.bg) as View, "bgT"))
                    .toBundle())
        }

        fun bindTask(task: Task) {
            this.task = task
            view.taskDescription.text = task.description
            var calendar = Calendar.getInstance(); calendar.time = task.date
            view.taskDate.text = calendar.get(Calendar.YEAR).toString() + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH)
            var beginTime = Calendar.getInstance()
            beginTime.time = task.beginTime
            view.taskBeginTime.text = beginTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + beginTime.get(Calendar.MINUTE).toString()
            var endTime = Calendar.getInstance()
            endTime.time = task.endTime
            view.taskEndTime.text = endTime.get(Calendar.HOUR_OF_DAY).toString() + ":" + endTime.get(Calendar.MINUTE).toString()        }
    }
}