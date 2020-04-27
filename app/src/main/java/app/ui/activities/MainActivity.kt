package app.ui.activities

import app.logic.adapters.RecyclerAdapter
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.collections.ArrayList
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import app.logic.AppPreferences
import app.logic.callbacks.SwipeToDeleteCallback
import sample.R
import app.logic.utils.RequestCode
import app.logic.entities.Task
import app.logic.utils.AlarmUtils
import app.logic.BDController
import app.logic.utils.CalendarUtils

class MainActivity : AppCompatActivity() {
    lateinit var date: Calendar
    var differenceInWeeks = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferences.init(this)
        AlarmUtils.setNextAlarm(this, false)
        date = Calendar.getInstance()
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val thread = object : Thread() {
            override fun run() {
                try {
                    do{
                        Thread.sleep(1000)
                        runOnUiThread {
                            refreshPendingTasks()
                        }
                    } while (!this.isInterrupted)
                } catch (e: InterruptedException) {
                }

            }
        }

        thread.start()
        initView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        refreshView()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        intent?.putExtra("requestCode", requestCode)//Enviamos el requestCode para diferenciar entre creacion y modificación de tareas
        super.startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            BDController().taskCreation(requestCode, data, this)
            AlarmUtils.setNextAlarm(this, false)
        }
        refreshView()
    }

    private fun initLandscape(){
        setContentView(R.layout.activity_main)
        refreshLandscapeView()
    }

    private fun initPortrait(){
        setContentView(R.layout.activity_main)
        refreshPortraitView()
    }

    private fun refreshPortraitView(){
        setContentView(R.layout.activity_main)
        val list = BDController().getTasks(this)
        val rV = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RecyclerAdapter(list, Configuration.ORIENTATION_PORTRAIT)
        rV.adapter =  adapter
        addSwipeToDeleteFromList()
        refreshPendingTasks()
        //CLICK ADD BUTTON TO OPEN ADD TASK ACTIVITY
        findViewById<FloatingActionButton>(R.id.addTask).setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            ActivityCompat.startActivityForResult(this as Activity, intent, RequestCode().create_task, null)
        }
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val intent = Intent(this, BuscadorActivity::class.java)
            ActivityCompat.startActivityForResult(this as Activity, intent, RequestCode().search, null)
        }
    }

    private fun refreshLandscapeView(){
        setContentView(R.layout.activity_main)
        val list = BDController().getTasksOfWeek(this, date)
        val rV = findViewById<RecyclerView>(R.id.recyclerView)
        val manager = GridLayoutManager(this, 7,RecyclerView.VERTICAL, false)//Manager en modo landscape
        rV.layoutManager = manager
        rV.adapter = RecyclerAdapter(obtenerLista(list), Configuration.ORIENTATION_LANDSCAPE)
        addSwipeToDeleteFromList()
        refreshPendingTasks()
        //CLICK ADD BUTTON TO OPEN ADD TASK ACTIVITY
        findViewById<FloatingActionButton>(R.id.addTask).setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            ActivityCompat.startActivityForResult(this as Activity, intent, RequestCode().create_task, null)
        }
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {//SEARCH BUTTON TO OPEN SEARCH ACTIVITY
            val intent = Intent(this, BuscadorActivity::class.java)
            ActivityCompat.startActivityForResult(this as Activity, intent, RequestCode().search, null)
        }
        findViewById<FloatingActionButton>(R.id.Future).setOnClickListener {//SEE NEXT WEEK
            date = CalendarUtils.nextWeek(date)
            differenceInWeeks+=1;
            refreshLandscapeView()
        }
        findViewById<FloatingActionButton>(R.id.Past).setOnClickListener {//SEE PAST WEEK
            date = CalendarUtils.pastWeek(date)
            differenceInWeeks-=1;
            refreshLandscapeView()
        }
    }

    private fun refreshView(){
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            refreshLandscapeView()
        else
            refreshPortraitView()
    }

    private fun refreshPendingTasks(){
        val n = BDController().pendingTasksToday(this)
        if(n>0){
            findViewById<TextView>(R.id.text).textSize = if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 12F else 20F
            findViewById<TextView>(R.id.text).text = "$n tareas pendientes para hoy"
        }else {
            var adapter = findViewById<RecyclerView>(R.id.recyclerView).adapter as RecyclerAdapter
            if(adapter.itemCount == 0) {
                findViewById<TextView>(R.id.text).textSize =
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 12F else 20F
                findViewById<TextView>(R.id.text).text = "No hay tareas"
            }else {
                findViewById<TextView>(R.id.text).textSize =
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3F else 3F
                findViewById<TextView>(R.id.text).text = ""
            }
        }
    }

    private fun initView(){
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            initLandscape()
        else
            initPortrait()
    }

    private fun obtenerLista(l:ArrayList<ArrayList<Task>>):ArrayList<Task> {
        var longer = 0
        for (i in 1 until l.size) {
            if(l[longer].size<l[i].size)
                longer=i
        }
        val array = ArrayList<Task>()
        val c = Calendar.getInstance()
        c.firstDayOfWeek = Calendar.MONDAY
        c.time = date.time
        var dow = 2//No se porque el dia 2 es Lunes en realidad
        c.set(Calendar.DAY_OF_WEEK, dow)
        array.add(
            Task(
                "-2",
                "Lunes",
                CalendarUtils.dateToString(c)
            )
        )
        dow++
        c.set(Calendar.DAY_OF_WEEK, dow)
        array.add(
            Task(
                "-2",
                "Martes",
                CalendarUtils.dateToString(c)
            )
        )
        dow++
        c.set(Calendar.DAY_OF_WEEK, dow)
        array.add(
            Task(
                "-2",
                "Miercoles",
                CalendarUtils.dateToString(c)
            )
        )
        dow++
        c.set(Calendar.DAY_OF_WEEK, dow)
        array.add(
            Task(
                "-2",
                "Jueves",
                CalendarUtils.dateToString(c)
            )
        )
        dow++
        c.set(Calendar.DAY_OF_WEEK, dow)
        array.add(
            Task(
                "-2",
                "Viernes",
                CalendarUtils.dateToString(c)
            )
        )
        dow=0//Sabado y domingo son los dias 0 y 1
        c.set(Calendar.DAY_OF_WEEK, dow)
        array.add(
            Task(
                "-2",
                "Sábado",
                CalendarUtils.dateToString(c)
            )
        )
        dow++
        c.set(Calendar.DAY_OF_WEEK, dow)
        array.add(
            Task(
                "-2",
                "Domingo",
                CalendarUtils.dateToString(c)
            )
        )

        val minTasks = 7//Si pasa de 7 se buguea la lista al hacer scroll
        for(j in 0 until if(l[longer].size < minTasks) minTasks else l[longer].size) {//Ademas de introducir las tareas ya creadas metemos algunas en blanco para rellenar hasta 7 filas
            for (i in 0 until l.size) {
                 c.set(Calendar.DAY_OF_WEEK, i)
                if(l[i].size>j && l[i].size>0){
                    array.add(l[i][j])
                }else{
                    array.add(
                        Task(
                            "-1",
                            "",
                            CalendarUtils.dateToString(c)
                        )
                    )
                }
            }
        }
        return array
    }

    private fun addSwipeToDeleteFromList(){
        val context = this
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = findViewById<RecyclerView>(R.id.recyclerView).adapter as RecyclerAdapter
                adapter.removeAt(context, viewHolder.adapterPosition)
                refreshView()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(findViewById<RecyclerView>(R.id.recyclerView))
    }
}