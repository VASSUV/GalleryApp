package ru.vassuv.appgallery.yandex

import ru.vassuv.appgallery.utils.atlibrary.json.JsonObject
import ru.vassuv.appgallery.utils.atlibrary.network.Error
import ru.vassuv.appgallery.utils.atlibrary.network.ICheckError

fun jsonErrorChecker() = object : ICheckError {
    override fun checkResponseError(jsonObject: JsonObject) {
        val status = jsonObject.string("status") ?: 0
        if (status != "success") throw Error(0, jsonObject.string("message"), jsonObject.toString())
    }
}