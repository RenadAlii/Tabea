package com.renad.tabea.ui.completed

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.renad.tabea.data.DataSource
import com.renad.tabea.data.model.Todo
import com.renad.tabea.databinding.FragmentCompletedTaskBinding
import com.renad.tabea.ui.TodoViewModel

class CompletedTaskFragment : Fragment() {

    private var _binding: FragmentCompletedTaskBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: TodoViewModel by activityViewModels()
    private var completedTaskAdapter: CompletedTaskAdapter? = null

    private val myDataset = DataSource.taskList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCompletedTaskBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            // @ because inside binding.apply this revers to the binding instance
            // not the class CompletedTaskFragment
            theListOfTodoFragment = this@CompletedTaskFragment
        }

        completedTaskAdapter = CompletedTaskAdapter(::onTaskClicked, ::onTaskChecked)
        binding.recyclerView.adapter = completedTaskAdapter
        val completedTAsk = myDataset.filter { task -> task.isCompleted }
        completedTaskAdapter?.submitList(completedTAsk)
        registerForContextMenu(binding.recyclerView)
    }

    private fun onTaskChecked(task: Todo) {
        sharedViewModel.setIsCompleted(task)
    }

    private fun onTaskClicked() {
        Toast.makeText(context, "if you want to make any change go to InBox", Toast.LENGTH_LONG)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) ||
            super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
