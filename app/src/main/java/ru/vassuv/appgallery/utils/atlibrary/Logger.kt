package ru.vassuv.appgallery.utils.atlibrary

import android.util.Log

object Logger{
    var debugMode: Boolean = true
    private var tag: String = "Start App"

    fun trace(vararg args: Any) {
        if (debugMode) Log.d(tag, varargToString(args))
    }

    fun traceException(text: String, exception: Throwable) {
        if (debugMode) {
            Log.d(tag, "$text: $exception")
            Log.d(tag, exception.stackTrace.joinToString("\n|   ", "|   ",""))
        }
    }

    private fun varargToString(args: Array<*>): String = args.joinToString(separator = " ") {
        if (it is Array<*> ) varargToString(it) else it.toString()
    }
}