package com.kirwa.taskapp.data.repository

import com.kirwa.taskapp.data.local.model.Tasks
import kotlinx.coroutines.flow.Flow
import com.kirwa.taskapp.data.remote.model.Result
import com.kirwa.taskapp.data.remote.model.TasksResponse

interface TasksRepository {
    suspend fun postCreateTask(tasks: Tasks): Result<TasksResponse>
    suspend fun closeTasks(taskId: String?): Result<TasksResponse>
    suspend fun updateTasks(taskId: String?): Result<TasksResponse>
    suspend fun reopenTasks(taskId: String?): Result<TasksResponse>
    suspend fun deleteTasks(taskId: String?): Result<TasksResponse>
    suspend fun getRemoteTasks(): Result<Boolean>
    fun getTasks(): Flow<List<Tasks>>
    fun searchTasks(searchString:String): Flow<Tasks>
}
