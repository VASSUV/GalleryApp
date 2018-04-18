package ru.vassuv.appgallery.repository.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Resource(
        val name: String,
        val antivirus_status: String,
        val file: String,
        val sha256: String,
        val exif: Exif,
        val revision: Long,
        val comment_ids: Comment,
        val preview: String,
        val resource_id: String,
        val created: String,
        val modified: String,
        val path: String,
        val md5: String,
        var type: String? = null,
        val mime_type: String,
        var media_type: String? = null,
        val size: Long
)

@Serializable
data class Comment(
        val private_resource: String,
        val public_resource: String
)
@Serializable
data class Exif(
        val date_time : String
)