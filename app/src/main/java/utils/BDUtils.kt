package utils

import android.content.Context
import bd.BDManager

class BDUtils {

    fun deleteWithId(context: Context, id: String){
        val bd = BDManager(context)
        val db = bd.writableDatabase
        db.delete("TASK", "key = $id", null)
    }

}