package com.kirwa.taskapp.data.remote.api

import com.kirwa.taskapp.data.local.model.Tasks
import com.kirwa.taskapp.data.remote.model.PostTask
import com.kirwa.taskapp.data.remote.model.TasksResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * CommonApi
 */
interface TaskApiService {

    @POST("rest/v2/tasks")
        suspend fun postCreateTask(@Body task: Tasks): Response<Tasks>

    @GET("rest/v2/tasks")
    suspend fun getRemoteTasks(): Response<List<TasksResponse>>

    @POST("rest/v2/tasks/{task_id}")
    suspend fun updateTask(@Path("task_id") taskId: String?): Response<TasksResponse>

    @POST("rest/v2/tasks/{task_id}/reopen")
    suspend fun reopenTask(@Path("task_id") taskId: String?): Response<TasksResponse>

    @POST("rest/v2/tasks/{task_id}")
    suspend fun closeTask(@Path("task_id") taskId: String?): Response<TasksResponse>

    @POST("rest/v2/tasks/{task_id}")
    suspend fun deleteTask(@Path("task_id") taskId: String?): Response<TasksResponse>

}
