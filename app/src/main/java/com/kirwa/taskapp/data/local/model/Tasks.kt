package com.kirwa.taskapp.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kirwa.taskapp.data.remote.model.ExcludeSerialize

@Entity(indices = [Index(value = ["taskId"], unique = true)])
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    @ExcludeSerialize
    var id: Long = 0,
    @ExcludeSerialize
    var taskId: String? = null,
    var content: String? = null,
    var dueString: String? = null,
    var dueLang: String? = null,
    var priority: Int? = null,
    var createdAt: String? = null,
    @ExcludeSerialize
    var isCompleted: Boolean = false
)

