package sample

import android.app.Activity
import android.app.ActivityOptions
import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.transition.*
import android.util.Pair
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import bd.BDManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

class MainActivity : AppCompatActivity() {
    var bd0:BDManager = BDManager(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sample().checkMe()


        val db = bd0.readableDatabase
        val cursor = db.query(
            "TASK",   // The table to query
            null ,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val itemIds = mutableListOf<Long>()

        var list = ArrayList<Task>()

        with(cursor) {
            while (moveToNext()) {
                val itemId = columnCount
                println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                list.add(Task(cursor.getString(1), cursor.getString(4)))
            }
        }

        setContentView(R.layout.activity_main2)
        var toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        var rV = findViewById<RecyclerView>(R.id.recyclerView)
        var adapter = RecyclerAdapter(list, Configuration.ORIENTATION_PORTRAIT)
        //val manager = GridLayoutManager(this, 7,RecyclerView.VERTICAL, false)//Manager en modo landscape
        val manager = LinearLayoutManager( // Manager en modo portrait
            this, RecyclerView.VERTICAL,
            false
        )
        rV.layoutManager = manager
        rV.adapter = adapter

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val t:Transition = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)
            t.duration = 500
            window.exitTransition = t
            startActivity(Intent(this, BuscadorActivity::class.java))
        }
        findViewById<FloatingActionButton>(R.id.addTask).setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            ActivityCompat.startActivityForResult(this as Activity, intent, 1, null)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            initLandscape()
        else
            initPortrait()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initLandscape(){
        setContentView(R.layout.activity_main)
        var list = ArrayList<Task>()
        val db = bd0.readableDatabase
        val cursor = db.query(
            "TASK",   // The table to query
            null ,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val itemIds = mutableListOf<Long>()

        with(cursor) {
            while (moveToNext()) {
                val itemId = columnCount
                println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                list.add(Task(cursor.getString(1), cursor.getString(4)))
            }
        }
        var rV = findViewById<RecyclerView>(R.id.recyclerView)
        var adapter = RecyclerAdapter(list, Configuration.ORIENTATION_LANDSCAPE)
        val manager = GridLayoutManager(this, 7,RecyclerView.VERTICAL, false)//Manager en modo landscape
        rV.layoutManager = manager
        rV.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initPortrait(){
        setContentView(R.layout.activity_main2)
        var toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        var list = ArrayList<Task>()
        var rV = findViewById<RecyclerView>(R.id.recyclerView)
        var adapter = RecyclerAdapter(list, Configuration.ORIENTATION_PORTRAIT)
        val manager = LinearLayoutManager( // Manager en modo portrait
            this, RecyclerView.VERTICAL,
            false
        )
        rV.layoutManager = manager
        rV.adapter = adapter

        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val t:Transition = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)
            t.duration = 500
            window.exitTransition = t
            startActivity(Intent(this, BuscadorActivity::class.java))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //requestCodes -> Create new = 1;
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            var bd = bd0.writableDatabase
            val values = ContentValues().apply {
                put("descripcion", data.extras["taskDescription"].toString())
                put("beginTime", data.extras["taskBeginTime"].toString())
                put("endTime", data.extras["taskEndTime"].toString())
                put("date", data.extras["taskDate"].toString())
            }
            // Insert the new row, returning the primary key value of the new row
            val newRowId = bd?.insert("TASK", null, values)
            bd.close()
        }
    }
}