package com.kirwa.taskapp.data.local.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kirwa.taskapp.data.local.dao.TasksDao
import com.kirwa.taskapp.data.local.model.Tasks

@Database(
    entities = [Tasks::class],
    version = 1

)
@TypeConverters(Converters::class)
abstract class TasksDatabase : RoomDatabase() {
    abstract val tasksDao: TasksDao

    companion object {
        const val DATABASE_NAME = "task_db"
    }
}
