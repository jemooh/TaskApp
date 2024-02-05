package com.kirwa.taskapp.data.local.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["taskId"], unique = true)])
data class Tasks(
    @PrimaryKey
    @NonNull
    val taskId: String = "",
    val content: String?? = null,
    val dueString: String? = null,
    val dueLang: String? = null,
    val priority: String? = null,
    val isCompleted: Boolean = false
)

