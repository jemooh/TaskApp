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
import com.kirwa.taskapp.utils.Constants.ISEDIT
import com.kirwa.taskapp.utils.Constants.TASKID
import com.kirwa.taskapp.utils.displayErrorSnackBar
import com.kirwa.taskapp.utils.setErrorMessage
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogAddEditTask : DialogFragment(R.layout.dialog_add_task) {

    private val tasksViewModel: TasksViewModel by viewModel()
    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DialogAddTaskBinding.bind(view)
        setUpView()
    }

    private fun postCreateTask(task: Tasks) = lifecycleScope.launch {
        tasksViewModel.postCreateTask(task)
            .flowWithLifecycle(lifecycle)
            .collect { loadingState ->
                when (loadingState) {
                    is Result.Loading -> {
                        binding.btnConfirm.showProgress {
                            progressColor = requireActivity().getColor(R.color.white)
                        }
                    }

                    is Result.Success -> {
                        binding.btnConfirm.hideProgress("Save")
                        this@DialogAddEditTask.dismiss()
                    }

                    is Result.Error -> {
                        binding.btnConfirm.hideProgress("Save")
                        binding.cardViewModule.displayErrorSnackBar(loadingState.exception.message.toString())
                        //this@DialogAddEditTask.dismiss()
                    }
                }
            }
    }


    private fun postEditTask(task: Tasks) = lifecycleScope.launch {
        tasksViewModel.updateTasks(task)
            .flowWithLifecycle(lifecycle)
            .collect { loadingState ->
                when (loadingState) {
                    is Result.Loading -> {
                        binding.btnConfirm.showProgress {
                            progressColor = requireActivity().getColor(R.color.white)
                        }
                    }

                    is Result.Success -> {
                        binding.btnConfirm.hideProgress("Save")
                        this@DialogAddEditTask.dismiss()
                    }

                    is Result.Error -> {
                        binding.btnConfirm.hideProgress("Save")
                        binding.cardViewModule.displayErrorSnackBar(loadingState.exception.message.toString())
                        //this@DialogAddEditTask.dismiss()
                    }
                }
            }
    }


    private fun setUpView() {
        Util.clearTextInputEditText(binding.edtTask, binding.textInputTask)
        Util.clearTextInputEditText(binding.edtDue, binding.textInputDue)
        val taskId = requireArguments().getString(TASKID)
        val isEdit = requireArguments().getBoolean(ISEDIT)
        if (isEdit) {
            tasksViewModel.getTaskById(taskId).onEach { task ->
                binding.edtTask.setText(task?.content.toString())
                binding.edtDue.setText(task?.dueString.toString())
            }.launchIn(lifecycleScope)
        }


        binding.btnConfirm.setOnClickListener {
            if (validateInputs()) {
                if (isEdit) {
                    val task = Tasks(
                        taskId = taskId,
                        content = binding.edtTask.text.toString(),
                        dueString = "tomorrow at 12:00",
                        dueLang = "en"
                    )
                    postEditTask(task)
                } else {
                    val task = Tasks(
                        content = binding.edtTask.text.toString(),
                        dueString = "tomorrow at 12:00",
                        dueLang = "en"
                    )
                    postCreateTask(task)
                }

            }
        }

        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }

    }

    private fun validateInputs(): Boolean {
        if (TextUtils.isEmpty(binding.edtTask.text)) {
            binding.textInputTask.setErrorMessage("Enter Task")
            return false
        }

        if (TextUtils.isEmpty(binding.edtDue.text)) {
            binding.textInputTask.setErrorMessage("Enter Due Date")
            return false
        }
        return true
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
        fun newInstance(taskId: String?, isEdit: Boolean = false) = DialogAddEditTask().apply {
            val bundle = Bundle().apply {
                putString(TASKID, taskId)
                putBoolean(ISEDIT, isEdit)
            }
            arguments = bundle
        }

        fun showDialog(
            taskId: String? = null,
            isEdit: Boolean = false,
            fragmentManager: FragmentManager?
        ) {
            fragmentManager?.let {
                newInstance(taskId, isEdit).show(it, DialogAddEditTask::class.java.simpleName)
            }
        }
    }
}