package com.kirwa.taskapp.data.repository

import com.kirwa.taskapp.data.local.model.Tasks
import kotlinx.coroutines.flow.Flow
import com.kirwa.taskapp.data.remote.model.Result
import com.kirwa.taskapp.data.remote.model.TasksResponse

interface TasksRepository {
    suspend fun postCreateTask(tasks: Tasks): Result<Boolean>
    suspend fun closeTask(taskId: String?): Result<Boolean>
    suspend fun updateTasks(task:Tasks): Result<Boolean>
    suspend fun reopenTasks(taskId: String?): Result<Boolean>
    suspend fun deleteTasks(taskId: String?): Result<Boolean>
    suspend fun getRemoteTasks(): Result<Boolean>
    fun getTasks(): Flow<List<Tasks>>
    fun getTaskById(taskId: String?): Flow<Tasks?>
    fun searchTasks(searchString:String): Flow<List<Tasks>>
}
