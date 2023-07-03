package com.renad.tabea.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.renad.tabea.ui.InBoxFragmentDirections
import com.renad.tabea.R
import com.renad.tabea.data.model.Todo
import com.renad.tabea.ui.TodoViewModel
import java.text.SimpleDateFormat
import java.util.*

class InBoxAdapter(private val context: Context, dataSet: List<Todo>) :
    RecyclerView.Adapter<InBoxAdapter.ItemViewHolder>() {
    val viewModel = TodoViewModel()
    private val toDoItem = dataSet

    // here we hold the view in listoftodo.xml
    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val todoCheckBox: CheckBox = view.findViewById(R.id.todocheckbox)
        val theMenu: ImageView = view.findViewById(R.id.ellipsisImageButton)
        val todoTask: TextView = view.findViewById(R.id.todoTaskText)
        val linearLayout: LinearLayout = view.findViewById(R.id.layout_expand)
        val timeText: Button = view.findViewById(R.id.timeText)
        val dateText: Button = view.findViewById(R.id.dateText)
        val detailsText: TextView = view.findViewById(R.id.details)
        val expand: ImageView = view.findViewById(R.id.expand)
        val hide: ImageView = view.findViewById(R.id.hide)
        val card: CardView = view.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.listoftodo, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val todo = toDoItem[position]
        holder.todoTask.text = todo.todoText
        holder.dateText.text = todo.date
        holder.timeText.text = todo.time
        holder.detailsText.text = todo.details

        // add lineThrough TodoTask when CheckBox Checked or remove line
        holder.todoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            makeLineThroughTask(holder.todoTask, isChecked, todo)
        }

        // add lineThrough TodoTask if it's Completed
        if (todo.isCompleted) {
            holder.todoTask.paintFlags = holder.todoTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.todoCheckBox.isChecked = !holder.todoCheckBox.isChecked
        }

        // fun return the current date of today
        val nowDate = dateOfToday()
        // parse the date from String to Date
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val taskTime = sdf.parse(todo.date)
        /* check if the task time is past or not */
        if (taskTime.before(nowDate)) {
            // check if the task is not completed before doing anything
            if (!todo.isCompleted) {
                holder.todoCheckBox.isEnabled = false
                holder.todoTask.setTextColor(context.resources.getColor(R.color.error))
                // show Toast when click on expired task
                holder.card.setOnClickListener {
                    Toast.makeText(
                        context,
                        "The task time has expired \n You can edited the time,\n so try to finish in time",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
        }

        // show the menu on the Icon
        holder.theMenu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.pop_menu, popupMenu.menu)
            popupMenu.show()
            // set the menu item action
            popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
                when (item?.itemId) {
                    R.id.menu_edit_todo -> {
                        val action = InBoxFragmentDirections
                            .actionTheListOfTodoFragmentToEditTodoFragment(
                                todoTaskToEdit = todo.todoText,
                                descriptionToEdit = todo.details,
                                timeToEdit = todo.time,
                                dateToEdit = todo.date,
                                positionOfTask = position.toString(),
                                isCompleted = todo.isCompleted,
                            )
                        // perform navigation action
                        notifyItemChanged(position)
                        holder.view.findNavController().navigate(action)
                        true
                    }
                    else -> {
                        viewModel.deleteTask(todo)
                        viewModel.setNotCompletedListSize()
                        notifyItemRemoved(position)
                        true
                    }
                }
            }
        } // end of the popUpMenu

        // expand the layout or unexpand
        holder.expand.setOnClickListener {
            if (holder.linearLayout.isGone) {
                holder.linearLayout.visibility = View.VISIBLE
                holder.expand.visibility = View.INVISIBLE
                notifyItemChanged(position)
            }
            holder.hide.setOnClickListener {
                if (holder.linearLayout.isVisible) {
                    holder.linearLayout.visibility = View.GONE
                    holder.expand.visibility = View.VISIBLE
                    notifyItemChanged(position)
                }
            }
        }
    }

    // fun to make LineThrough Task & disEnable CheckBox when task is completed
    private fun makeLineThroughTask(
        taskTextView: TextView,
        isCompleted: Boolean,
        todo: Todo,
    ) {
        if (isCompleted) {
            taskTextView.paintFlags = taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            taskTextView.paintFlags = 0
        }
    }

    private fun dateOfToday(): Date {
        val calendar = Calendar.getInstance()
        // set time to 00:00:00
        // so you will not face problem with the time when compare 2 date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    override fun getItemCount(): Int = toDoItem.size
}
