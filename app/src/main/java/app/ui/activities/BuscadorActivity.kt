package app.ui.activities

import app.logic.adapters.RecyclerAdapter
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.logic.callbacks.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.buscador.*
import sample.R
import app.logic.utils.AlarmUtils
import app.logic.BDController

class BuscadorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buscador)
        findViewById<RadioGroup>(R.id.priority).check(R.id.todas)
        findViewById<Button>(R.id.boton).setOnClickListener {
            var priorId = 4
            when(findViewById<RadioGroup>(R.id.priority).checkedRadioButtonId){
                R.id.uno -> priorId = 1
                R.id.dos -> priorId = 2
                R.id.tres -> priorId = 3
            }
            var list = if (buscador.text.toString() == "")
                BDController().getTasks(this, priorId)
            else
                BDController().getTasksContaining(this, buscador.text.toString(), priorId)
            val rV = findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = RecyclerAdapter(list, Configuration.ORIENTATION_PORTRAIT)
            rV.adapter =  adapter
            addSwipeToDeleteFromList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            BDController().taskCreation(requestCode, data, this)
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