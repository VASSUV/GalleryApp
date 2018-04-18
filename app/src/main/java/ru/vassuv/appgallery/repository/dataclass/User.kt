package ru.vassuv.appgallery.repository.dataclass

import kotlinx.serialization.Serializable

@Serializable
class User(
        val uid: String,
        val display_name: String,
        val login: String,
        val country: String
)