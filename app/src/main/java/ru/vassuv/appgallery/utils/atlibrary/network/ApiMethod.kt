package ru.vassuv.appgallery.utils.atlibrary.network

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.serialization.json.JSON
import ru.vassuv.appgallery.repository.dataclass.Disk
import ru.vassuv.appgallery.utils.atlibrary.json.JsonObject

interface ApiMethod {
    val host: String
}

interface ICheckError {
    fun checkResponseError(jsonObject: JsonObject)
}

suspend fun ApiMethod.get(params: Map<String, String> = hashMapOf(),
                          checkErrorMethod: ICheckError?
): Response<JsonObject> = get(params, { response ->
    val jsonObject = JsonObject.readFrom(response)
    checkErrorMethod?.checkResponseError(jsonObject)
    jsonObject
})

suspend fun ApiMethod.get(params: Map<String, String> = hashMapOf()): Response<String> = get(params, { it })

suspend inline fun <reified T : Any> ApiMethod.getData(params: Map<String, String> = hashMapOf()): Response<T> = get(params, {
    println(it)
    JSON.parse<T>(it)
})

suspend fun <T> ApiMethod.get(params: Map<String, String>,
                              postDelay: suspend (String) -> T?
): Response<T> {
    var result: Response<T>? = null
    val job = launch(CommonPool) {
        result = try {
            Response(postDelay(RETROFIT.get(host, params).await()))
        } catch (throwable: Throwable) {
            Response(null, throwable.toError())
        }
    }

    delay(1000)
    job.join()
    return result ?: Response<T>(null, ERROR_EMPTY)
}

suspend fun ApiMethod.post(params: Map<String, String> = hashMapOf(),
                         isCheckError: Boolean = false
): Response<JsonObject> = post(params, { response ->
    JsonObject.readFrom(response).apply {
        if (isCheckError) {
            val status = int("status") ?: 0
            if (status > 0) throw Error(status, string("message"), this["meta"].toString())
        }
    }
})

suspend fun ApiMethod.post(params: Map<String, String>): Response<String> = post(params, { it })

suspend fun <T> ApiMethod.post(params: Map<String, String>,
                              postDelay: suspend (String) -> T?
): Response<T> {
    var result: Response<T>? = null
    val job = launch(CommonPool) {
        result = try {
            Response(postDelay(RETROFIT.post(host, params).await()))
        } catch (throwable: Throwable) {
            Response(null, throwable.toError())
        }
    }

    delay(1000)
    job.join()
    return result ?: Response<T>(null, ERROR_EMPTY)
}
