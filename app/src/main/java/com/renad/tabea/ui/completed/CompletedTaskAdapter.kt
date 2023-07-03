package com.renad.tabea.ui.completed

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.renad.tabea.data.model.Task
import com.renad.tabea.databinding.ListoftodoBinding

class CompletedTaskAdapter(val onItemClicked: () -> Unit, val onTaskCheked: (Task) -> Unit) :
    ListAdapter<Task, CompletedTaskAdapter.ItemViewHolder>(DiffCallback) {

    inner class ItemViewHolder(private var binding: ListoftodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Task) {
            binding.apply {
                todoTaskText.text = todo.todoText
                dateText.text = todo.date
                timeText.text = todo.time
                details.text = todo.details

                // remove the menu
                ellipsisImageButton.visibility = View.GONE

                // make Toast to
                // Notify the user that they cannot change anything here
                card.setOnClickListener {
                    onItemClicked()
                }

                // add lineThrough TodoTask when CheckBox Checked or remove line
                todocheckbox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskCheked(todo)
                    makeLineThroughTask(todoTaskText, isChecked)
                }

                // add lineThrough TodoTask if it's Completed
                if (todo.isCompleted) {
                    todoTaskText.paintFlags = todoTaskText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    todocheckbox.isChecked = todocheckbox.isChecked
                }

                // expand the layout or unexpanded
                expand.setOnClickListener {
                    if (layoutExpand.isGone) {
                        layoutExpand.visibility = View.VISIBLE
                        expand.visibility = View.INVISIBLE
                    }
                    hide.setOnClickListener {
                        if (layoutExpand.isVisible) {
                            layoutExpand.visibility = View.GONE
                            expand.visibility = View.VISIBLE
                        }
                    }
                }
            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ListoftodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(
            oldItem: Task,
            newItem: Task,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.todoText == newItem.todoText
        }
    }
}
