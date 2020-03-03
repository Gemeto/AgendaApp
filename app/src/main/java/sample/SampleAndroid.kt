package sample

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.transition.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import bd.BDManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.collections.ArrayList


actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

class MainActivity : AppCompatActivity() {
    private var bdHelper:BDManager = BDManager(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sample().checkMe()
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
                put("descripcion", data.extras["taskDescription"].toString())
                put("beginTime", data.extras["taskBeginTime"].toString())
                put("endTime", data.extras["taskEndTime"].toString())
                put("date", data.extras["taskDate"].toString())
            }
            if (requestCode == RequestCode().create_task) {
                // Insertamos la tarea en la base de datos
                bd?.insert("TASK", null, values)
            } else if (requestCode == RequestCode().modify_task) {
                // Modificamos la tarea en la base de datos
                bd?.update("TASK", values, "key = " + data.extras["taskId"].toString(), null)
            }
            bd.close()
            if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                refreshLandscapeLists()
            else
                refreshPortraitList()
        }

    }

    private fun initLandscape(){
        setContentView(R.layout.activity_main)
        refreshLandscapeLists()
    }

    private fun initPortrait(){
        setContentView(R.layout.activity_main2)
        val toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
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
        //CLICK ON IMAGE TO SELECT OTHER
        findViewById<ImageView>(R.id.img).setOnClickListener {
            pickImageFromGallery()
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
                list.add(Task(cursor.getString(0), cursor.getString(1), cursor.getString(4)))
            }
        }

        val rV = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = RecyclerAdapter(list, Configuration.ORIENTATION_PORTRAIT)
        rV.adapter =  adapter
    }

    private fun refreshLandscapeLists(){
        val list = ArrayList<ArrayList<Task>>()
        for(i in 0..6){
            list.add(ArrayList<Task>())
        }
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
                var task = Task(cursor.getString(0), cursor.getString(1), cursor.getString(4))
                var calendar:Calendar = Calendar.getInstance()
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

        var rV = findViewById<RecyclerView>(R.id.recyclerViewMo)
        rV.adapter = RecyclerAdapter(list[0], Configuration.ORIENTATION_PORTRAIT)
        rV = findViewById<RecyclerView>(R.id.recyclerViewTu)
        rV.adapter = RecyclerAdapter(list[1], Configuration.ORIENTATION_PORTRAIT)
        rV = findViewById<RecyclerView>(R.id.recyclerViewWe)
        rV.adapter = RecyclerAdapter(list[2], Configuration.ORIENTATION_PORTRAIT)
        rV = findViewById<RecyclerView>(R.id.recyclerViewTh)
        rV.adapter = RecyclerAdapter(list[3], Configuration.ORIENTATION_PORTRAIT)
        rV = findViewById<RecyclerView>(R.id.recyclerViewFr)
        rV.adapter = RecyclerAdapter(list[4], Configuration.ORIENTATION_PORTRAIT)
        rV = findViewById<RecyclerView>(R.id.recyclerViewSa)
        rV.adapter = RecyclerAdapter(list[5], Configuration.ORIENTATION_PORTRAIT)
        rV = findViewById<RecyclerView>(R.id.recyclerViewSu)
        rV.adapter = RecyclerAdapter(list[6], Configuration.ORIENTATION_PORTRAIT)
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"//RUTA DE IMAGENES EN ANDROID
        startActivityForResult(intent, RequestCode().select_image)
    }
}