package com.example.tabea.adapter

import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.tabea.R
import com.example.tabea.model.Todo

 class TodoListAdapter (private val context: Context, dataSet: List<Todo>)
    : RecyclerView.Adapter<TodoListAdapter.ItemViewHolder>() {

    private val toDoItem = dataSet

    //here we take from list_product.xml
    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val todoCheckBox: CheckBox = view.findViewById(R.id.todocheckbox)
     val theMenu: ImageView = view.findViewById(R.id.ellipsisImageButton)

    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
         val adapterLayout = LayoutInflater.from(parent.context)
             .inflate(R.layout.listoftodo, parent, false)
         return ItemViewHolder(adapterLayout)

     }

     override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
      val todo = toDoItem[position]
      holder.todoCheckBox.text = todo.todoText
      holder.theMenu.setOnClickListener {

         }

         //action

     }






     override fun getItemCount(): Int = toDoItem.size
 }
