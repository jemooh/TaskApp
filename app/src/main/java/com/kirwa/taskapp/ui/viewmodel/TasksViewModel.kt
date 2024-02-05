package com.kirwa.taskapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.kirwa.taskapp.data.local.model.Tasks
import com.kirwa.taskapp.data.remote.model.TasksResponse
import com.kirwa.taskapp.data.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import com.kirwa.taskapp.data.remote.model.Result

class TasksViewModel(
    private val tasksRepository: TasksRepository,
) :
    ViewModel() {

    suspend fun postCreateTask(tasks: Tasks): Result<TasksResponse> {
        return tasksRepository.postCreateTask(tasks)
    }


    suspend fun closeTasks(taskId: String?): Result<TasksResponse> {
        return tasksRepository.closeTasks(taskId)
    }


    suspend fun updateTasks(taskId: String?): Result<TasksResponse> {
        return tasksRepository.updateTasks(taskId)
    }

    suspend fun reopenTasks(taskId: String?): Result<TasksResponse> {
        return tasksRepository.reopenTasks(taskId)
    }

    suspend fun deleteTasks(taskId: String?): Result<TasksResponse> {
        return tasksRepository.deleteTasks(taskId)
    }

    suspend fun getRemoteTasks(): Result<Boolean> {
        return tasksRepository.getRemoteTasks()
    }

    fun getTasks(): Flow<List<Tasks>> = tasksRepository.getTasks()

    fun searchTasks(searchText: String): Flow<List<Tasks>> =
        tasksRepository.searchTasks(searchText)

}