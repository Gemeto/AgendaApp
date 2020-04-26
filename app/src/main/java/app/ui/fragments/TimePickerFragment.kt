package app.ui.fragments

import android. app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val c = Calendar.getInstance()
    private var hour = c.get(Calendar.HOUR_OF_DAY)
    private var minute = c.get(Calendar.MINUTE)
    private lateinit var listener:TimePickerListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create a new instance of TimePickerDialog and return it
        if(arguments?.get("hour") != null){
            hour = arguments?.get("hour").toString().toInt()
            minute = arguments?.get("minute").toString().toInt()
        }
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val h = "$hourOfDay".padStart(2, '0')
        val m = "$minute".padStart(2, '0')
        if(tag == "beginTimePicker")
            listener.applyTextBeginTime("$h:$m")
        else
            listener.applyTextEndTime("$h:$m")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as TimePickerListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement ExampleDialogListener")
        }

    }

    interface TimePickerListener {
        fun applyTextBeginTime(time: String)
        fun applyTextEndTime(time: String)
    }
}