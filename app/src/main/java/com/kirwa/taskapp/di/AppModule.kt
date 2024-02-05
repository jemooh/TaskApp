package com.kirwa.taskapp.di


import android.content.Context
import androidx.room.Room
import com.kirwa.taskapp.data.local.datasource.TasksDatabase
import com.kirwa.taskapp.data.remote.api.TaskApiService
import com.kirwa.taskapp.data.repository.TasksRepository
import com.kirwa.taskapp.data.repository.TasksRepositoryImpl
import com.kirwa.taskapp.ui.viewmodel.TasksViewModel
import com.uda.grassrootelection.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

const val baseUrl: String = Constants.BASE_URL_RELEASE
const val apiToken: String = Constants.API_TOKEN

val appModule = module {
    single { createNetworkClient(baseUrl,apiToken) }
    single { (get() as? Retrofit)?.create(TaskApiService::class.java) }

    single {
        Room.databaseBuilder(
            androidContext(),
            TasksDatabase::class.java,
            TasksDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    single {
        get<TasksDatabase>().tasksDao
    }

    factory<TasksRepository> {
        TasksRepositoryImpl(
            tasksDao = get(), taskApiService = get()
        )
    }
    viewModel {
        TasksViewModel(tasksRepository = get())
    }

}
