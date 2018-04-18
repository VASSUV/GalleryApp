package ru.vassuv.appgallery.repository.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Files(
        val items: ArrayList<Resource>,
        val limit: Int,
        val offset: Int
)