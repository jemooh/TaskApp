package com.kirwa.taskapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kirwa.taskapp.R
import com.kirwa.taskapp.databinding.DialogOpenTaskBinding
import com.kirwa.taskapp.databinding.FragmentEditDeleteTaskItemBinding
import com.kirwa.taskapp.ui.viewmodel.TasksViewModel
import com.kirwa.taskapp.utils.Constants
import com.kirwa.taskapp.utils.Util
import com.kirwa.taskapp.utils.hide
import com.kirwa.taskapp.utils.show
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class TasksItemsBottomSheetFragment :
    BottomSheetDialogFragment(R.layout.fragment_edit_delete_task_item) {

    private val tasksViewModel: TasksViewModel by viewModel()
    private var _binding: FragmentEditDeleteTaskItemBinding? = null
    private val binding get() = _binding!!

    private var isComplete: Boolean = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditDeleteTaskItemBinding.bind(view)

        setUpViews()
    }

    private fun setUpViews() {
        val taskId = arguments?.getString(Constants.TASKID)
        if (!taskId.isNullOrEmpty()) {
            tasksViewModel.getTaskById(taskId).onEach { task ->
                if (task != null) {
                    binding.tvCardTitle.text = task?.content
                    val date = task.dueDate?.let { Util.formatTaskDate(it) }
                    val time = task.dueDatetime?.let { Util.formatTaskDateTime(it) }
                    binding.tvCardBody.text = "Due By: $date, $time"
                    this.isComplete = task?.isCompleted ?: false
                    if (task?.isCompleted == true) {
                        binding.btnClose.text = getString(R.string.open)
                        binding.ivCompleted.show()
                    } else {
                        binding.ivCompleted.hide()
                        binding.btnClose.text = getString(R.string.close)
                    }
                } else {
                    dismiss()
                }

            }.launchIn(lifecycleScope)
        }

        binding.btnEdit.setOnClickListener {
            DialogAddEditTask.showDialog(taskId,true, this?.childFragmentManager)
            //dismiss()
        }

        binding.btnClose.setOnClickListener {
            DialogCloseOpenTask.showDialog(taskId, isComplete, this?.childFragmentManager)
            //dismiss()
        }

        binding.btnDelete.setOnClickListener {
            DialogDeleteTask.showDialog(taskId, this?.childFragmentManager)
            //dismiss()
        }


    }

    companion object {
        @JvmStatic
        fun newInstance(
            bundle: Bundle
        ): TasksItemsBottomSheetFragment {
            val fragment = TasksItemsBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
