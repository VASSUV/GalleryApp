package ru.vassuv.appgallery.repository.db

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ru.vassuv.appgallery.App

private val CURRENT_VERSION: Int = 20170101
private val dbName: String = "appgallery.db"

val dbHelper = object : ManagedSQLiteOpenHelper(App.context, dbName, null, CURRENT_VERSION){

        override fun onCreate(db: SQLiteDatabase) {
            db.createTable("user", false,
                    "id" to INTEGER + PRIMARY_KEY,
                    "country" to TEXT,
                    "name" to TEXT,
                    "login" to TEXT)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        }
}

fun SQLiteDatabase.raw(name: String, whereSelection: String, vararg columns: String): Cursor {
   return rawQuery("select ${if(columns.isEmpty()) "*" else columns.joinToString()} from $name $whereSelection", null)
}