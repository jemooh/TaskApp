package com.kirwa.taskapp.data.repository

import com.kirwa.taskapp.data.local.dao.TasksDao
import com.kirwa.taskapp.data.local.model.Tasks
import com.kirwa.taskapp.data.remote.api.TaskApiService
import com.kirwa.taskapp.data.remote.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

class TasksRepositoryImplTest {

    private lateinit var tasksDao: TasksDao
    private lateinit var taskApiService: TaskApiService
    private lateinit var repository: TasksRepositoryImpl

    @Before
    fun setUp() {
        tasksDao = mock()
        taskApiService = mock()
        repository = TasksRepositoryImpl(tasksDao, taskApiService)
    }

    @Test
    fun `test getTasks`() = runBlocking {
        val mockTasks: List<Tasks> = listOf(Tasks(
            taskId = "7669946511",
            content = "Buy Milk",
            dueString = "tomorrow at 12:00"))
        tasksDao.stub {
            onBlocking { getTasks() }.thenReturn(flow { emit(mockTasks) })
        }

        val result = repository.getTasks()

        assertEquals(mockTasks, result.first())
    }

    @Test
    fun `test getTaskById`() = runBlocking {
        val taskId = "7669946511"
        val mockTask: Tasks? = Tasks(
            taskId = "7669946511",
            content = "Buy Milk",
            dueString = "tomorrow at 12:00")
        tasksDao.stub {
            onBlocking { getTaskById(taskId) }.thenReturn(flow { emit(mockTask) })
        }

        val result = repository.getTaskById(taskId)

        assertEquals(mockTask, result.first())
    }
}
