package app.bd

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE TASK (" +
            "key INTEGER PRIMARY KEY," +
            "descripcion TEXT," +
            "beginTime TEXT," + //Formato: HH:MM
            "endTime TEXT," + //Formato: HH:MM
            "date TEXT," + //Formato: yyyy-MM-dd
            "alarm INTEGER," +// 0 no suena la alarma y 1 si que suena
            "priority INTEGER)"//0 sin más, 1 importante, 2 muy importante

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS TASK"

class BDManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 8
        const val DATABASE_NAME = "Task.db"
    }
}