package com.kirwa.taskapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.kirwa.taskapp.data.local.model.Tasks
import com.kirwa.taskapp.data.remote.model.TasksResponse
import com.kirwa.taskapp.data.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import com.kirwa.taskapp.data.remote.model.Result
import kotlinx.coroutines.flow.flow

class TasksViewModel(
    private val tasksRepository: TasksRepository,
) :
    ViewModel() {


    fun postCreateTask(tasks: Tasks): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        emit(tasksRepository.postCreateTask(tasks))
    }


    fun closeTasks(taskId: String?): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        emit(tasksRepository.closeTask(taskId))
    }

    fun updateTasks(task:Tasks): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        emit(tasksRepository.updateTasks(task))
    }


    fun reopenTasks(taskId: String?): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        emit(tasksRepository.reopenTasks(taskId))
    }

    fun deleteTasks(taskId: String?): Flow<Result<Boolean>> = flow {
        emit(Result.Loading)
        emit(tasksRepository.deleteTasks(taskId))
    }

    suspend fun getRemoteTasks(): Result<Boolean> {
        return tasksRepository.getRemoteTasks()
    }

    fun getTasks(): Flow<List<Tasks>> = tasksRepository.getTasks()

    fun getTaskById(taskId: String?): Flow<Tasks?> = tasksRepository.getTaskById(taskId)

    fun searchTasks(searchText: String): Flow<List<Tasks>> =
        tasksRepository.searchTasks(searchText)

}