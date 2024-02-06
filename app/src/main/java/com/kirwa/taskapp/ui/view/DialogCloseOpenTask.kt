package com.kirwa.taskapp.ui.view

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.kirwa.taskapp.R
import com.kirwa.taskapp.data.local.model.Tasks
import com.kirwa.taskapp.databinding.DialogAddTaskBinding
import com.kirwa.taskapp.ui.viewmodel.TasksViewModel
import com.kirwa.taskapp.utils.Util
import com.kirwa.taskapp.data.remote.model.Result
import com.kirwa.taskapp.databinding.DialogDeleteBinding
import com.kirwa.taskapp.databinding.DialogOpenTaskBinding
import com.kirwa.taskapp.utils.Constants.ISCOMPLETED
import com.kirwa.taskapp.utils.Constants.TASKID
import com.kirwa.taskapp.utils.displayErrorSnackBar
import com.kirwa.taskapp.utils.setErrorMessage
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogCloseOpenTask : DialogFragment(R.layout.dialog_open_task) {

    private val tasksViewModel: TasksViewModel by viewModel()
    private var _binding: DialogOpenTaskBinding? = null
    private val binding get() = _binding!!
    private var taskId: String? = null
    private var isCompleted: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogOpenTaskBinding.bind(view)
        setUpView()
    }

    private fun postOpenTask(taskId: String?) = lifecycleScope.launch {
        tasksViewModel.reopenTasks(taskId)
            .flowWithLifecycle(lifecycle)
            .collect { loadingState ->
                when (loadingState) {
                    is Result.Loading -> {
                        binding.btnConfirm.showProgress {
                            progressColor = requireActivity().getColor(R.color.white)
                        }
                    }

                    is Result.Success -> {
                        binding.btnConfirm.hideProgress("Confirm")
                        this@DialogCloseOpenTask.dismiss()
                    }

                    is Result.Error -> {
                        binding.btnConfirm.hideProgress("Confirm")
                        binding.cardViewModule.displayErrorSnackBar(loadingState.exception.message.toString())
                        //this@DialogAddEditTask.dismiss()
                    }
                }
            }
    }


    private fun postCloseTask(taskId: String?) = lifecycleScope.launch {
        tasksViewModel.closeTasks(taskId)
            .flowWithLifecycle(lifecycle)
            .collect { loadingState ->
                when (loadingState) {
                    is Result.Loading -> {
                        binding.btnConfirm.showProgress {
                            progressColor = requireActivity().getColor(R.color.white)
                        }
                    }

                    is Result.Success -> {
                        binding.btnConfirm.hideProgress("Confirm")
                        this@DialogCloseOpenTask.dismiss()
                    }

                    is Result.Error -> {
                        binding.btnConfirm.hideProgress("Confirm")
                        binding.cardViewModule.displayErrorSnackBar(loadingState.exception.message.toString())
                        //this@DialogAddEditTask.dismiss()
                    }
                }
            }
    }


    private fun setUpView() {
        this.taskId = requireArguments().getString(TASKID).toString()
        this.isCompleted = requireArguments().getBoolean(ISCOMPLETED)
        if (isCompleted) {
            binding.tvCardTitle.text = getString(R.string.open_task)
            binding.tvBody.text = getString(R.string.are_you_sure_you_want_to_re_open_task)
        } else {
            binding.tvCardTitle.text = getString(R.string.close_task)
            binding.tvBody.text = getString(R.string.are_you_sure_you_want_to_close_task)
        }

        binding.btnConfirm.setOnClickListener {
            if (isCompleted) {
                postOpenTask(taskId)
            } else {
                postCloseTask(taskId)
            }
        }

        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimations_SmileWindow
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(taskId: String?, isCompleted: Boolean = false) =
            DialogCloseOpenTask().apply {
                val bundle = Bundle().apply {
                    putString(TASKID, taskId)
                    putBoolean(ISCOMPLETED, isCompleted)
                }
                arguments = bundle
            }

        fun showDialog(
            taskId: String? = null,
            isCompleted: Boolean = false,
            fragmentManager: FragmentManager?
        ) {
            fragmentManager?.let {
                newInstance(taskId).show(it, DialogCloseOpenTask::class.java.simpleName)
            }
        }
    }
}