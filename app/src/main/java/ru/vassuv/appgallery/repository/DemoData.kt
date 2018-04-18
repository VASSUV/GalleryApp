package ru.vassuv.appgallery.repository

import ru.vassuv.appgallery.repository.dataclass.User
import ru.vassuv.appgallery.repository.db.IData

class DemoData : IData {
    override var user: User? = User("ru", "vassuv", "Vassiliy Somov", "123456")
}
