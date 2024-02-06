package com.kirwa.taskapp.data.remote.model


data class TasksResponse(
    val id: String? = null,
    val content: String? = null,
    val createdAt: String? = null,
    val priority: Int? = null,
    val isCompleted: Boolean = false,
    val due: Due? = null
)


data class Due(
    val date: String? = null,
    val string: String? = null,
    val lang: String? = null,
    val isRecurring: Boolean? = false,
    val datetime: String? = null,
)
