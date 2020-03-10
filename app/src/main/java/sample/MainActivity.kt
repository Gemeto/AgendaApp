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
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager

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
                    refreshLandscapeLists()
                } else if (deltaX < MIN_DISTANCE_TO_SLIDE && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    date = CalendarUtils.nextWeek(date)
                    refreshLandscapeLists()
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
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            initLandscape()
        else
            initPortrait()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            initLandscape()
        else
            initPortrait()
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
        }
        if (requestCode == RequestCode().create_task) {
            // Insertamos la tarea en la base de datos
            bd?.insert("TASK", null, values)
        } else if (requestCode == RequestCode().modify_task) {
            // Modificamos la tarea en la base de datos
            bd?.update("TASK", values, "key = " + ((data.extras as Bundle)["taskId"] as Any).toString(), null)
        }
        bd.close()
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            refreshLandscapeLists()
        else
            refreshPortraitList()

        AlarmUtils.setNextAlarm(this, false)
        }
    }

    private fun initLandscape(){
        setContentView(R.layout.activity_main)
        refreshLandscapeLists()
    }

    private fun initPortrait(){
        setContentView(R.layout.activity_main)
        refreshPortraitList()
        //CLICK SEARCH BUTTON TO LOOK FOR CONCRETE TASKS
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val t:Transition = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)
            t.duration = 500
            window.exitTransition = t
            startActivity(Intent(this, BuscadorActivity::class.java))
        }
        //CLICK ADD BUTTON TO OPEN ADD TASK ACTIVITY
        findViewById<FloatingActionButton>(R.id.addTask).setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            ActivityCompat.startActivityForResult(this as Activity, intent, RequestCode().create_task, null)
        }
    }

    private fun refreshPortraitList(){
        val list = ArrayList<Task>()
        val db = bdHelper.readableDatabase
        val cursor = db.query(
            "TASK",
            null ,
            null,
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                list.add(Task(cursor.getString(0), cursor.getString(1), cursor.getString(4), cursor.getString(2), cursor.getString(3)))
            }
        }

        val rV = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RecyclerAdapter(list, Configuration.ORIENTATION_PORTRAIT)
        rV.adapter =  adapter
    }

    @SuppressLint("SetTextI18n")
    private fun refreshLandscapeLists(){
        val list = ArrayList<ArrayList<Task>>()
        for(i in 0..6){
            list.add(ArrayList<Task>())
        }
        val db = bdHelper.readableDatabase
        val monday = CalendarUtils.firstDayOfTheWeek(date)
        val sunday = CalendarUtils.lastDayOfTheWeek(date)
        findViewById<TextView>(R.id.week).text = "$monday $sunday"
        val cursor = db.query(
            "TASK",
            null ,
            "date BETWEEN '$monday' AND '$sunday'",
            null,
            null,
            null,
            "date DESC"
        )
        with(cursor) {
            while (moveToNext()) {
                val task = Task(cursor.getString(0), cursor.getString(1), cursor.getString(4), cursor.getString(2), cursor.getString(3))
                println(cursor.getString(4))
                val calendar:Calendar = Calendar.getInstance()
                calendar.time = task.date
                when(calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> list[0].add(task)
                    Calendar.TUESDAY -> list[1].add(task)
                    Calendar.WEDNESDAY -> list[2].add(task)
                    Calendar.THURSDAY -> list[3].add(task)
                    Calendar.FRIDAY -> list[4].add(task)
                    Calendar.SATURDAY -> list[5].add(task)
                    Calendar.SUNDAY -> list[6].add(task)

                }
            }
        }
        val rV = findViewById<RecyclerView>(R.id.recyclerView)
        val manager = GridLayoutManager(this, 7,RecyclerView.VERTICAL, false)//Manager en modo landscape
        rV.layoutManager = manager
        rV.adapter = RecyclerAdapter(obtenerLista(list), Configuration.ORIENTATION_LANDSCAPE)
    }

    private fun obtenerLista(l:ArrayList<ArrayList<Task>>):ArrayList<Task> {
        var longer = 0
        for (i in 1 until l.size) {
            if(l[longer].size<l[i].size)
                longer=i
        }
        val array = ArrayList<Task>()
        for(j in 0 until l[longer].size) {
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
}