package com.renad.tabea.ui.inBox

import android.graphics.Paint
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.renad.tabea.core.util.DateUtil
import com.renad.tabea.core.util.DateUtil.getDate
import com.renad.tabea.databinding.TaskItemBinding
import com.renad.tabea.domain.model.Task

class InBoxAdapter(
    val onItemClicked: (String) -> Unit,
    val onTaskChecked: (Task) -> Unit,
    val showPopupMenu: (Task, View) -> Unit,
) : ListAdapter<Task, InBoxAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            TaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private var binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Task) {
            binding.apply {
                taskText.text = todo.task
                bindDateText(todo.date?.getDate()?.let { DateUtil.dateFormatter().format(it) })
                bindDetails(todo.details)
                handelCompleteState(todo.isCompleted)

                // show the menu on the Icon
                ellipsisIcon.setOnClickListener {
                    showPopupMenu(todo, it)
                }

                card.setOnClickListener {
                    todo.id?.let { onItemClicked(it.toString()) }
                }

                taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskChecked(todo)
                    handelCompleteState(isChecked)
                }
            }
        }

        private fun TaskItemBinding.bindDateText(date: String?) {
            if (date != null) {
                dateText.text = date
            } else {
                dateText.isVisible = false
                calenderIcon.isVisible = false
                divider.isVisible = false
            }
        }

        private fun TaskItemBinding.bindDetails(text: String?) {
            if (text.isNullOrEmpty()) {
                details.isVisible = false
            } else {
                details.text = text
            }
        }

        // add lineThrough when CheckBox Checked or remove line.
        private fun TaskItemBinding.handelCompleteState(isCompleted: Boolean) {
            makeLineThroughTask(taskText, isCompleted)
            makeLineThroughTask(details, isCompleted)
            makeLineThroughTask(dateText, isCompleted)
            taskCheckbox.isChecked = isCompleted
        }

        // fun to make LineThrough Task & disEnable CheckBox when task is completed
        private fun makeLineThroughTask(
            taskTextView: TextView,
            isCompleted: Boolean,
        ) {
            if (isCompleted) {
                taskTextView.paintFlags = taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                taskTextView.paintFlags = 0
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(
            oldItem: Task,
            newItem: Task,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.task == newItem.task
        }
    }
}
