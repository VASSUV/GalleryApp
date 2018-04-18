package ru.vassuv.appgallery.repository.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Disk(
        @SerialName("max_file_size")
        val maxFileSize: Long,

        @SerialName("unlimited_autoupload_enabled")
        val unlimitedAutouploadEnabled: Boolean,

        @SerialName("total_space")
        val totalSpace: Long,

        @SerialName("trash_size")
        val trashSize: Long,

        @SerialName("is_paid")
        val isPaid: Boolean,

        @SerialName("used_space")
        val usedSpace: Long,

        @SerialName("system_folders")
        val systemFolders: Map<String, String>,

        val user: User,
        val revision: Long
)