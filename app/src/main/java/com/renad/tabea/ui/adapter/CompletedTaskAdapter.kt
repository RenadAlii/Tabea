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
import androidx.recyclerview.widget.RecyclerView
import com.renad.tabea.R
import com.renad.tabea.data.model.Todo
import com.renad.tabea.ui.TodoViewModel

class CompletedTaskAdapter(private val context: Context, dataSet: List<Todo>) :
    RecyclerView.Adapter<CompletedTaskAdapter.ItemViewHolder>() {

    private val toDoItem = dataSet.filter { it.isCompleted }
    val viewModel = TodoViewModel()

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
    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val todo = toDoItem[position]
        holder.todoTask.text = todo.todoText
        holder.dateText.text = todo.date
        holder.timeText.text = todo.time
        holder.detailsText.text = todo.details

        // remove the menu
        holder.theMenu.visibility = View.GONE
        // set the CheckBox to disable to prevent any change in this fragment
        holder.todoCheckBox.isEnabled = false

        // make Toast to
        // Notify the user that they cannot change anything here
        holder.card.setOnClickListener {
            Toast.makeText(context, "if you want to make any change go to InBox", Toast.LENGTH_LONG).show()
        }

        // add lineThrough TodoTask when CheckBox Checked or remove line
        holder.todoCheckBox.setOnCheckedChangeListener { _, isChecked ->
            makeLineThroughTask(holder.todoTask, isChecked, position)
        }

        // add lineThrough TodoTask if it's Completed
        if (todo.isCompleted) {
            holder.todoTask.paintFlags = holder.todoTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.todoCheckBox.isChecked = !holder.todoCheckBox.isChecked
        }

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
        position: Int,
    ) {
        if (isCompleted) {
            taskTextView.paintFlags = taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            viewModel.setIsCompleted(position, true)
        } else {
            taskTextView.paintFlags = 0

            viewModel.setIsCompleted(position, false)
        }
    }

    override fun getItemCount(): Int = toDoItem.size
}
