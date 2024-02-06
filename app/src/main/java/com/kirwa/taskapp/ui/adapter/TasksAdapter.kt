package com.kirwa.taskapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kirwa.taskapp.data.local.model.Tasks
import com.kirwa.taskapp.databinding.RowItemTaskBinding
import com.kirwa.taskapp.utils.hide
import com.kirwa.taskapp.utils.show

class TasksAdapter(
    private val tasksOnClickListener: TasksOnClickListener
) : ListAdapter<Tasks, TasksAdapter.AspirantDetailHolder>(TaskDiffer) {
    inner class AspirantDetailHolder(private val binding: RowItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tasks: Tasks) {
            tasks.apply {

                binding.tvContent.text = content
                binding.tvDueTime.text = dueString

                if (isCompleted){
                    binding.ivCompleted.show()
                }else{
                    binding.ivCompleted.hide()
                }

                binding.root.setOnClickListener {
                    tasksOnClickListener.invoke(this)
                }

            }
        }

    }

    companion object TaskDiffer : DiffUtil.ItemCallback<Tasks>() {
        override fun areItemsTheSame(oldItem: Tasks, newItem: Tasks): Boolean =
            (oldItem.taskId == newItem.taskId)

        override fun areContentsTheSame(oldItem: Tasks, newItem: Tasks): Boolean =
            (oldItem == newItem)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AspirantDetailHolder {
        return AspirantDetailHolder(
            RowItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AspirantDetailHolder, position: Int) {
        holder.bind(getItem(position))

    }
}


typealias TasksOnClickListener = (Tasks) -> Unit