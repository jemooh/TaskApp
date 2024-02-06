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
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import com.kirwa.taskapp.utils.hide
import com.kirwa.taskapp.utils.show

import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.Date

class DialogAddEditTask : DialogFragment(R.layout.dialog_add_task) {

    private val tasksViewModel: TasksViewModel by viewModel()
    private var _binding: DialogAddTaskBinding? = null
    private val binding get() = _binding!!
    private val calendar: Calendar = Calendar.getInstance()
    private var dueTime: String? = null
    private var dueDate: String? = null
    private var date: Date? = null
    private var dateTime: Date? = null
    private var isEdit: Boolean = false

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
        val taskId = requireArguments().getString(TASKID)
         isEdit = requireArguments().getBoolean(ISEDIT)
        if (isEdit) {
            tasksViewModel.getTaskById(taskId).onEach { task ->
                binding.edtTask.setText(task?.content.toString())
                binding.tvTime.text = task?.dueDatetime?.let { Util.formatTaskDateTime(it) }
                binding.tvDate.text = task?.dueDate?.let { Util.formatTaskDate(it) }
            }.launchIn(lifecycleScope)
        }


        binding.btnConfirm.setOnClickListener {
            if (validateInputs()) {
                if (isEdit) {
                    val dateServer = Util.dateToStringDateServer(date)
                    val time = Util.dateToStringTimeServer(dateTime)
                    val task = Tasks(
                        taskId = taskId,
                        content = binding.edtTask.text.toString(),
                        dueDate = Util.dateToStringDateServer(date),
                        dueString = "$dateServer at $time",
                        dueLang = "en"
                    )
                    postEditTask(task)
                } else {
                    val dateServer = Util.dateToStringDateServer(date)
                    val time = Util.dateToStringTimeServer(dateTime)
                    val task = Tasks(
                        content = binding.edtTask.text.toString(),
                        dueDate = Util.dateToStringDateServer(date),
                        dueString = "$dateServer at $time",
                        dueLang = "en"
                    )
                    postCreateTask(task)
                }

            }
        }


        binding.tvTime.setOnClickListener {
            selectDueTime()
        }


        binding.tvDate.setOnClickListener {
            selectDueDate()
        }


        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }

    }

    fun updateDueTimeLabel() {
        dateTime = calendar?.time
        binding.tvTime.text = Util.dateToStringTimeLocal(calendar?.time)
        dueTime = Util.dateToStringTimeLocal(calendar?.time)
    }

    fun updateDueDate() {
        date = calendar?.time
        binding.tvDate.text = Util.dateToStringDateLocal(calendar?.time)
        dueDate = Util.dateToStringDateLocal(calendar?.time)
    }


    fun selectDueTime() {
        val time = TimePickerDialog.OnTimeSetListener() { view, hourOfDay, minute ->
            // TODO Auto-generated method stub
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateDueTimeLabel()
            binding.tvError.hide()
        }
        val bidTimeDialog = TimePickerDialog(
            requireContext(),
            R.style.datepicker,
            time,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        bidTimeDialog.setTitle("Select Due time")
        bidTimeDialog.show()
    }

    fun selectDueDate() {
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDueDate()
            binding.tvError.hide()
        }
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        // Allow previous day selection
        //yesterday.add(Calendar.DATE, -1)


        val dateDialog = DatePickerDialog(
            requireContext(),
            R.style.datepicker,
            date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dateDialog.datePicker.minDate = yesterday.time.time
        //dateDialog.datePicker.maxDate = today.time.time
        dateDialog.setTitle("Select Due Date")
        dateDialog.show()
    }

    private fun validateInputs(): Boolean {
        if (TextUtils.isEmpty(binding.edtTask.text)) {
            binding.textInputTask.setErrorMessage("Enter Task")
            return false
        }

        if (!dueTime.isNullOrEmpty() && !dueDate.isNullOrEmpty()) {
            binding.tvError.hide()
        } else {
            binding.tvError.show()
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