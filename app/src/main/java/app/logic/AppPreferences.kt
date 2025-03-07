package app.logic

import android.content.Context
import android.content.SharedPreferences

object AppPreferences{
    private const val NAME = "SpinKotlin"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    // list of app specific preferences
    private val ALARMTIME = Pair("alarmTime", 0.toLong())
    private val DESC = Pair("descripcion", "")
    private val ID = Pair("id", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            NAME,
            MODE
        )
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var alarmTime: Long
        get() = preferences.getLong(ALARMTIME.first, ALARMTIME.second)
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