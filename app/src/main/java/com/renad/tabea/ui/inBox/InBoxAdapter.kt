package com.renad.tabea.ui.inBox

import android.graphics.Paint
import android.view.*
import android.widget.*
import androidx.core.view.isGone
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
    val showPopupMenu: (Int, View) -> Unit,
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
                todoTaskText.text = todo.task
                dateText.text = todo.date?.getDate()?.let { DateUtil.dateFormatter().format(it) }
                timeText.text = todo.time
                details.text = todo.details
                todocheckbox.isChecked = todo.isCompleted

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

                // show the menu on the Icon
                ellipsisImageButton.setOnClickListener {
                    todo.id?.let { id -> showPopupMenu(id, it) }
                }

                card.setOnClickListener {
                    todo.id?.let { onItemClicked(it.toString()) }
                }

                // add lineThrough TodoTask when CheckBox Checked or remove line
                todocheckbox.setOnCheckedChangeListener { _, isChecked ->
                    onTaskChecked(todo)
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
