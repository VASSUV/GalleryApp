package ru.vassuv.appgallery.repository.db.delegate

import android.content.ContentValues
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.transaction
import ru.vassuv.appgallery.repository.StageData
import ru.vassuv.appgallery.repository.dataclass.User
import ru.vassuv.appgallery.repository.db.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class UserDelegate : ReadWriteProperty<StageData, User?> {
    override fun getValue(thisRef: StageData, property: KProperty<*>): User? {
        val cursor = dbHelper.writableDatabase.raw(USER, "$ID=0", ID, NAME, LOGIN)
        if (!cursor.moveToFirst()) return null

        val user = User (cursor.getString(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(LOGIN)),
                cursor.getString(cursor.getColumnIndex(COUNTRY))
                )
        cursor.close()
        return user
    }

    override fun setValue(thisRef: StageData, property: KProperty<*>, value: User?) {
        if (value == null) return
        dbHelper.writableDatabase.transaction {
           if(raw(USER, "$ID=0").count == 0) {
               insert(USER,
                       ID to 0,
                       NAME to value.display_name,
                       LOGIN to value.login,
                       COUNTRY to value.country)
           } else {
               val contentValues = ContentValues()
               contentValues.put(NAME, value.display_name)
               contentValues.put(LOGIN, value.login)
               contentValues.put(COUNTRY, value.country)
               contentValues.put(LOGIN, value.login)
               update(USER, contentValues, "$ID=0", null)
           }
        }
    }
}

