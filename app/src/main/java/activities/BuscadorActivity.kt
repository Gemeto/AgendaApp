package activities

import adapters.RecyclerAdapter
import android.content.ContentValues
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import bd.BDManager
import callbacks.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.buscador.*
import sample.R
import sample.RequestCode
import sample.Task
import utils.AlarmUtils
import utils.BDUtils

class BuscadorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buscador)
        findViewById<Button>(R.id.boton).setOnClickListener {
            var list = if (buscador.text.toString() == "")
                BDUtils().getTasks(this)
            else
                BDUtils().getTasksContaining(this, buscador.text.toString())
            val rV = findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = RecyclerAdapter(list, Configuration.ORIENTATION_PORTRAIT)
            rV.adapter =  adapter
            addSwipeToDeleteFromList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bdHelper = BDManager(this)
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
        findViewById<Button>(R.id.boton).callOnClick()
    }

    private fun addSwipeToDeleteFromList(){
        val context = this
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = findViewById<RecyclerView>(R.id.recyclerView).adapter as RecyclerAdapter
                adapter.removeAt(context, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(findViewById<RecyclerView>(R.id.recyclerView))
    }

}