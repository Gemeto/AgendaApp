package activities

import adapters.RecyclerAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import callbacks.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.buscador.*
import sample.R
import sample.Task
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