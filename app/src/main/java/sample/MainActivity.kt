package sample

import activities.BuscadorActivity
import activities.CreateTaskActivity
import adapters.RecyclerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.transition.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import bd.BDManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.collections.ArrayList
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import callbacks.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*
import utils.AlarmUtils
import utils.BDUtils
import utils.CalendarUtils

actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

object AppPreferences{
    private const val NAME = "SpinKotlin"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    // list of app specific preferences
    private val ALARMTIME = Pair("alarmTime", 0.toLong())
    private val DESC = Pair("descripcion", "")
    private val ID = Pair("id", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var alarmTime: Long
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getLong(ALARMTIME.first, ALARMTIME.second)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putLong(ALARMTIME.first, value)
        }

    var id: String

        get() = preferences.getString(ID.first, ID.second) as String

        set(value) = preferences.edit{
            it.putString(ID.first, value)
        }

    var descripcion: String

        get() = preferences.getString(DESC.first, DESC.second) as String

        set(value) = preferences.edit{
            it.putString(DESC.first, value)
        }
}

class MainActivity : AppCompatActivity() {
    private var bdHelper:BDManager = BDManager(this)
    private val MIN_DISTANCE_TO_SLIDE = 150
    private var x1: Float = 0.toFloat()
    var x2: Float = 0.toFloat()
    lateinit var date: Calendar

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> x1 = event.x
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                val deltaX = x2 - x1
                if (deltaX > MIN_DISTANCE_TO_SLIDE && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    date = CalendarUtils.pastWeek(date)
                    refreshLandscapeView()
                } else if (deltaX < MIN_DISTANCE_TO_SLIDE && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    date = CalendarUtils.nextWeek(date)
                    refreshLandscapeView()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sample().checkMe()
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
        val bd = bdHelper.writableDatabase
        //Creamos un contenido de valores con los datos de la tarea
        val values = ContentValues().apply {
            put("descripcion", ((data.extras as Bundle)["taskDescription"] as Any).toString())
            put("beginTime", ((data.extras as Bundle)["taskBeginTime"] as Any).toString())
            put("endTime", ((data.extras as Bundle)["taskEndTime"] as Any).toString())
            put("date", ((data.extras as Bundle)["taskDate"] as Any).toString())
            put("alarm", ((data.extras as Bundle)["alarm"] as Any) as Boolean)
        }
        if (requestCode == RequestCode().create_task) {
            // Insertamos la tarea en la base de datos
            bd?.insert("TASK", null, values)
        } else if (requestCode == RequestCode().modify_task) {
            // Modificamos la tarea en la base de datos
            bd?.update("TASK", values, "key = " + ((data.extras as Bundle)["taskId"] as Any).toString(), null)
        }
        bd.close()
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
        val list = BDUtils().getTasks(this)
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
        val list = BDUtils().getTasksOfWeek(this, date)
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
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val intent = Intent(this, BuscadorActivity::class.java)
            ActivityCompat.startActivityForResult(this as Activity, intent, RequestCode().search, null)
        }
    }

    private fun refreshView(){
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            refreshLandscapeView()
        else
            refreshPortraitView()
    }

    private fun refreshPendingTasks(){
        val n = BDUtils().pendingTasksToday(this)
        if(n>0){
            findViewById<TextView>(R.id.text).textSize = if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 12F else 20F
            findViewById<TextView>(R.id.text).text = "$n tareas pendientes para hoy"
        }else {
            findViewById<TextView>(R.id.text).textSize = if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3F else 3F
            findViewById<TextView>(R.id.text).text = ""
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
        array.add(Task("-2", "Lunes", CalendarUtils.dateToString(CalendarUtils.actualWeekInDay(0))))
        array.add(Task("-2", "Martes", CalendarUtils.dateToString(CalendarUtils.actualWeekInDay(1))))
        array.add(Task("-2", "Miercoles", CalendarUtils.dateToString(CalendarUtils.actualWeekInDay(2))))
        array.add(Task("-2", "Jueves", CalendarUtils.dateToString(CalendarUtils.actualWeekInDay(3))))
        array.add(Task("-2", "Viernes", CalendarUtils.dateToString(CalendarUtils.actualWeekInDay(4))))
        array.add(Task("-2", "Sábado", CalendarUtils.dateToString(CalendarUtils.actualWeekInDay(5))))
        array.add(Task("-2", "Domingo", CalendarUtils.dateToString(CalendarUtils.actualWeekInDay(6))))

        for(j in 0 until if(l[longer].size < 7) 7 else l[longer].size) {
             for (i in 0 until l.size) {
                if(l[i].size>j && l[i].size>0){
                    array.add(l[i][j])
                }else{
                    array.add(Task("-1", "", CalendarUtils.dateToString(CalendarUtils.actualWeekInDay(i))))
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