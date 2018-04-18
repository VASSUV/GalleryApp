package ru.vassuv.appgallery.utils.atlibrary.network

val YANDEX_DISK_V1 = "https://cloud-api.yandex.net/v1/disk"

enum class Api(override val host: String) : ApiMethod {
    APP_FOLDER("$YANDEX_DISK_V1/resources?path=app:/"),
    DISK("$YANDEX_DISK_V1/"),
    FILES("$YANDEX_DISK_V1/resources/files");
}

