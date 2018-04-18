package ru.vassuv.appgallery.utils.atlibrary.network

import ru.vassuv.appgallery.App
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class Response<out T>(val result: T?,
                           val error: Error = NO_ERROR)

data class Error(val status: Int = 0,
                 override val message: String? = null,
                 val errorBody: String? = null) : Exception()

val NO_ERROR = Error()
val ERROR = Error(1000, "Произошла ошибка")
val ERROR_EMPTY = Error(1001)
val ERROR_INTERNET_NOT_FOUND = Error(800, "Отсутсвует интернет соединение")
val ERROR_TIMEOUT = Error(900, "Превышено время ожидания")

fun Throwable.toError() = when (this) {
    is UnknownHostException -> ERROR_INTERNET_NOT_FOUND
    is SocketTimeoutException -> ERROR_INTERNET_NOT_FOUND
    is Error -> this
    else -> {
        App.logExc("Throwable.toError()", this)
        ERROR
    }
}