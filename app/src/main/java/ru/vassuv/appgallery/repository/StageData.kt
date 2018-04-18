package ru.vassuv.appgallery.repository

import ru.vassuv.appgallery.repository.dataclass.User
import ru.vassuv.appgallery.repository.db.IData
import ru.vassuv.appgallery.repository.db.delegate.UserDelegate

class StageData: IData {
    override var user: User? by UserDelegate()
}
