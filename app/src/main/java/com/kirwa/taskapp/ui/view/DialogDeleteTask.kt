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
import com.kirwa.taskapp.utils.Constants.TASKID
import com.kirwa.taskapp.utils.displayErrorSnackBar
import com.kirwa.taskapp.utils.setErrorMessage
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogDeleteTask : DialogFragment(R.layout.dialog_delete) {

    private val tasksViewModel: TasksViewModel by viewModel()
    private var _binding: DialogDeleteBinding? = null
    private val binding get() = _binding!!
    private var taskId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogDeleteBinding.bind(view)
        setUpView()
    }

    private fun postDeleteTask(taskId: String?) = lifecycleScope.launch {
        tasksViewModel.deleteTasks(taskId)
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
                        this@DialogDeleteTask.dismiss()
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
        binding.btnConfirm.setOnClickListener {
            postDeleteTask(taskId)
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
        fun newInstance(taskId: String?) = DialogDeleteTask().apply {
            val bundle = Bundle().apply {
                putString(TASKID, taskId)
            }
            arguments = bundle
        }

        fun showDialog(taskId: String? = null, fragmentManager: FragmentManager?) {
            fragmentManager?.let {
                newInstance(taskId).show(it, DialogDeleteTask::class.java.simpleName)
            }
        }
    }
}