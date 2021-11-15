package com.example.tabea

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tabea.adapter.TodoListAdapter
import com.example.tabea.data.DataSource
import com.example.tabea.databinding.FragmentTheListOfTodoBinding


class TheListOfTodoFragment : Fragment() {

    private var _binding: FragmentTheListOfTodoBinding? = null
    private val binding get() = _binding!!

    private val myDataset = DataSource.dataSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTheListOfTodoBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = TodoListAdapter(requireContext(), myDataset)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_theListOfTodoFragment_to_newTodoFragment)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        activity?.menuInflater?.inflate(R.menu.menu_edit_delete_todo, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {


      return return when(item.itemId){
          R.id.menu_edit_todo -> {
              Toast.makeText(context,"Edit",Toast.LENGTH_SHORT).show()
              true
          }else -> {
              Toast.makeText(context,"Delete",Toast.LENGTH_SHORT).show()

              true
          }
      }
    }


    //Prepare the card options menu to be displayed.
    // This is called right before the menu is shown,
//    override fun onPrepareOptionsMenu(menu: Menu) {
//        val delete = menu.findItem(R.id.menu_delete_todo)
//        val edit = menu.findItem(R.id.menu_edit_todo)
//        super.onPrepareOptionsMenu(menu)
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}