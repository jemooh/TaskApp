package com.kirwa.taskapp.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["taskId"], unique = true)])
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var taskId: String? = null,
    var content: String? = null,
    var dueString: String? = null,
    var dueLang: String? = null,
    var priority: String? = null,
    var isCompleted: Boolean = false
)

