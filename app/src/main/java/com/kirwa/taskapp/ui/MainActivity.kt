package com.kirwa.taskapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.kirwa.taskapp.R
import com.kirwa.taskapp.databinding.ActivityMainBinding
import com.kirwa.taskapp.ui.viewmodel.TasksViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tasksViewMode: TasksViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchRemoteTasks()

    }

    private fun fetchRemoteTasks() = lifecycleScope.launch {
        tasksViewMode.getRemoteTasks()
    }


}