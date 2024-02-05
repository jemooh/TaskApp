package com.kirwa.taskapp.data.repository

import com.kirwa.taskapp.data.local.dao.TasksDao
import com.kirwa.taskapp.data.local.model.Tasks
import com.kirwa.taskapp.data.remote.api.TaskApiService
import com.kirwa.taskapp.data.remote.model.Result
import com.kirwa.taskapp.data.remote.model.TasksResponse
import com.uda.grassrootelection.utils.Util.Companion.errorMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.IOException

internal class TasksRepositoryImpl(
    private val tasksDao: TasksDao,
    private val taskApiService: TaskApiService,
    private val isDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksRepository {


    override fun getTasks(): Flow<List<Tasks>> {
        return tasksDao.getTasks()
    }

    override fun searchTasks(searchString:String): Flow<Tasks> {
        return tasksDao.searchTasks(searchString)
    }


    override suspend fun getRemoteTasks(): Result<Boolean> {
        return withContext(isDispatcher) {
            try {
                val response = taskApiService.getRemoteTasks()
                if (response.isSuccessful && response.body() != null) {
                    val results = response.body()
                    results?.forEach { remoteTask ->
                        val task = Tasks(
                            taskId = remoteTask.id.toString(),
                            content = remoteTask.content,
                            dueString = remoteTask.due?.string,
                            dueLang = remoteTask.due?.lang,
                            priority = remoteTask.priority,
                            isCompleted =remoteTask.isCompleted,
                        )
                        tasksDao.insertAsync(task)
                    }

                    Result.Success(true)
                } else {
                    Result.Error(Exception(response.errorBody()?.errorMessage()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }

    override suspend fun postCreateTask(tasks: Tasks): Result<TasksResponse> {
        return withContext(isDispatcher) {
            try {
                val response = taskApiService.postCreateTask(tasks)
                if (response.isSuccessful && response.body() != null) {
                    val remoteTask = response.body() as TasksResponse
                    val task = Tasks(
                        taskId = remoteTask.id.toString(),
                        content = remoteTask.content,
                        dueString = remoteTask.due?.string,
                        dueLang = remoteTask.due?.lang,
                        priority = remoteTask.priority,
                        isCompleted = remoteTask.isCompleted,
                    )
                    tasksDao.insertAsync(task)
                    Result.Success(remoteTask)
                } else {
                    Result.Error(Exception(response.errorBody()?.errorMessage()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }


    override suspend fun deleteTasks(taskId: String?): Result<TasksResponse> {
        return withContext(isDispatcher) {
            try {
                val response = taskApiService.deleteTask(taskId)
                if (response.isSuccessful && response.body() != null) {
                    val results = response.body() as TasksResponse
                    tasksDao.deleteTasks(taskId)
                    Result.Success(results)
                } else {
                    Result.Error(Exception(response.errorBody()?.errorMessage()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }


    override suspend fun closeTasks(taskId: String?): Result<TasksResponse> {
        return withContext(isDispatcher) {
            try {
                val response = taskApiService.closeTask(taskId)
                if (response.isSuccessful && response.body() != null) {
                    val results = response.body() as TasksResponse
                    tasksDao.closeTasks(taskId)
                    Result.Success(results)
                } else {
                    Result.Error(Exception(response.errorBody()?.errorMessage()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }

    override suspend fun reopenTasks(taskId: String?): Result<TasksResponse> {
        return withContext(isDispatcher) {
            try {
                val response = taskApiService.reopenTask(taskId)
                if (response.isSuccessful && response.body() != null) {
                    val results = response.body() as TasksResponse
                    tasksDao.reopenTasks(taskId)
                    Result.Success(results)
                } else {
                    Result.Error(Exception(response.errorBody()?.errorMessage()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }

    override suspend fun updateTasks(taskId: String?): Result<TasksResponse> {
        return withContext(isDispatcher) {
            try {
                val response = taskApiService.updateTask(taskId)
                if (response.isSuccessful && response.body() != null) {
                    val remoteTask = response.body() as TasksResponse
                    val task = Tasks(
                        taskId = remoteTask.id.toString(),
                        content = remoteTask.content,
                        dueString = remoteTask.due?.string,
                        dueLang = remoteTask.due?.lang,
                        priority = remoteTask.priority,
                        isCompleted = remoteTask.isCompleted,
                    )
                    tasksDao.insertAsync(task)
                    Result.Success(remoteTask)
                } else {
                    Result.Error(Exception(response.errorBody()?.errorMessage()))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }


}