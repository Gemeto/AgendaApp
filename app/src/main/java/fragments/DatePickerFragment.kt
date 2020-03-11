package fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val c = Calendar.getInstance()
    private var year = c.get(Calendar.YEAR)
    private var month = c.get(Calendar.MONTH)
    private var day = c.get(Calendar.DAY_OF_MONTH)
    private lateinit var listener: DatePickerListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if(arguments?.get("year") != null){
            year = arguments?.get("year").toString().toInt()
            month = arguments?.get("month").toString().toInt()
            day = arguments?.get("day").toString().toInt()
        }
        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity as Context, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, y: Int, m: Int, d: Int) {
        val year = "$y".padStart(4, '0')
        val month = (""+(m+1)).padStart(2, '0')
        val day = "$d".padStart(2, '0')
        listener.applyTextDate("$year-$month-$day")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as DatePickerListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement ExampleDialogListener")
        }

    }

    interface DatePickerListener {
        fun applyTextDate(date: String)
    }
}