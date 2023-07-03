package com.renad.tabea.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.renad.tabea.R
import com.renad.tabea.data.model.Task
import com.renad.tabea.ui.TodoViewModel
import java.text.SimpleDateFormat

class WontDoAdapter(private val context: Context, dataSet: List<Task>) :
    RecyclerView.Adapter<WontDoAdapter.ItemViewHolder>() {
    val viewModel = TodoViewModel()
    private val toDoItem = dataSet.filter {
        val dateTodo = it.date
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = sdf.parse(dateTodo)
        !it.isCompleted && date.before(viewModel.dateOfToday())
    }

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

        // remove the menu
        holder.theMenu.visibility = View.GONE
        // set the CheckBox to disable to prevent any change in this fragment
        holder.todoCheckBox.isEnabled = false

        // make Toast to
        // Notify the user that they cannot change anything here
        holder.card.setOnClickListener {
            Toast.makeText(context, "if you want to make any change go to InBox", Toast.LENGTH_LONG).show()
        }

        // fun return the current date of today
        val nowDate = viewModel.dateOfToday()
        // parse the date from String to Date
        val taskTime = viewModel.dateFormatter().parse(todo.date)
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

    override fun getItemCount(): Int = toDoItem.size
}
