package com.kirwa.taskapp.data.local.dao

import androidx.room.*
import com.kirwa.taskapp.data.local.model.Tasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao : CoroutineBaseDao<Tasks> {
    @Query("SELECT * FROM Tasks ORDER BY priority DESC ")
    fun getTasks(): Flow<List<Tasks>>

    @Query("SELECT * FROM Tasks WHERE taskId=:taskId ")
    fun getTaskById(taskId: String?): Flow<Tasks?>

    @Query("DELETE FROM Tasks WHERE taskId=:taskId ")
    fun deleteTasks(taskId: String?)

    @Query("UPDATE Tasks SET isCompleted=1 WHERE taskId=:taskId ")
    fun closeTasks(taskId: String?)

    @Query("UPDATE Tasks SET isCompleted=0 WHERE taskId=:taskId ")
    fun reopenTasks(taskId: String?)

    @Query("SELECT * FROM Tasks WHERE content  LIKE :searchString ")
    fun searchTasks(searchString: String?): Flow<List<Tasks>>

}
