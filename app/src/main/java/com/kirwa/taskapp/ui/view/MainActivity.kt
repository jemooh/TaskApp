package com.kirwa.taskapp.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.kirwa.taskapp.databinding.ActivityMainBinding
import com.kirwa.taskapp.ui.adapter.TasksAdapter
import com.kirwa.taskapp.ui.viewmodel.TasksViewModel
import com.kirwa.taskapp.utils.Constants
import com.kirwa.taskapp.utils.Util
import com.kirwa.taskapp.utils.hide
import com.kirwa.taskapp.utils.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tasksViewModel: TasksViewModel by viewModel()
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRv()
        fetchTasksAndDisplay()
        fetchRemoteTasks()
        setUpSearchTasks()
        binding.fabAdd.setOnClickListener {
            DialogAddEditTask.showDialog(null, false, this?.supportFragmentManager)
        }
    }

    private fun fetchRemoteTasks() = lifecycleScope.launch {
        tasksViewModel.getRemoteTasks()
    }


    private fun setUpRv() {
        Util.setRecyclerViewNoDivider(this, binding.recyclerView)
        tasksAdapter = TasksAdapter {
            val bundle = Bundle()
            bundle.putString(
                Constants.TASKID,
                it.taskId
            )
            TasksItemsBottomSheetFragment.newInstance(bundle).apply {
                show(supportFragmentManager, tag)
            }
        }

        binding.recyclerView.adapter = tasksAdapter
    }


    private fun fetchTasksAndDisplay() = lifecycleScope.launch {
        tasksViewModel.getTasks()
            .flowWithLifecycle(lifecycle)
            .collectLatest { tasks ->
                if (tasks.isNotEmpty()) {
                    binding.recyclerView.show()
                    binding.layoutEmptyView.hide()
                    tasksAdapter.submitList(tasks)
                } else {
                    binding.recyclerView.hide()
                    binding.layoutEmptyView.show()
                }
            }
    }


    private fun setUpSearchTasks() {
        binding.searchLayout.textInputEditTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    binding.searchLayout.imageViewCancel.hide()
                    fetchTasksAndDisplay()
                } else {
                    binding.searchLayout.imageViewCancel.show()
                    val searchText = s.toString()
                    getTasksFromDb(searchText)
                }

            }
        })

        binding.searchLayout.imageViewCancel.setOnClickListener {
            binding.searchLayout.textInputEditTextSearch.setText("")
        }

    }


    private fun getTasksFromDb(text: String) = lifecycleScope.launch {
        var searchText = text
        searchText = "%$searchText%"
        tasksViewModel.searchTasks(searchText)
            .flowWithLifecycle(lifecycle)
            .collectLatest { tasks ->
                if (tasks.isNotEmpty()) {

                    binding.recyclerView.show()
                    binding.layoutEmptyView.hide()
                    tasksAdapter.submitList(tasks)
                } else {
                    binding.recyclerView.hide()
                    binding.layoutEmptyView.show()
                }
            }
    }

}