package com.renad.tabea.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.renad.tabea.R
import com.renad.tabea.data.DataSource
import com.renad.tabea.data.model.Todo
import com.renad.tabea.databinding.FragmentTodayTaskBinding
import com.renad.tabea.ui.completed.CompletedTaskAdapter
import java.text.SimpleDateFormat

class TodayTaskFragment : Fragment() {

    private var _binding: FragmentTodayTaskBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: TodoViewModel by activityViewModels()

    private val myDataset = DataSource.taskList
    private var taskAdapter: CompletedTaskAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTodayTaskBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            // @ because inside binding.apply this revers to the binding instance
            // not the class TodayTaskFragment
            todayTaskFragment = this@TodayTaskFragment
        }
        taskAdapter = CompletedTaskAdapter(::onTaskClicked, ::onTaskChecked)
        binding.recyclerView.adapter = taskAdapter
        val todayTasks = myDataset.filter {
            val dateTodo = it.date
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val date = sdf.parse(dateTodo)
            date == (sharedViewModel.dateOfToday())
        }
        taskAdapter?.submitList(todayTasks)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_todayTaskFragment_to_newTodoFragment)
        }
        registerForContextMenu(binding.recyclerView)
    }

    private fun onTaskChecked(task: Todo) {
        sharedViewModel.setIsCompleted(task)
    }

    private fun onTaskClicked() {
        Toast.makeText(context, "if you want to make any change go to InBox", Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) ||
            super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
