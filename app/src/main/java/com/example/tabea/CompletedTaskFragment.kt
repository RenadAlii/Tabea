package com.example.tabea

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tabea.adapter.CompletedTaskAdapter
import com.example.tabea.data.DataSource
import com.example.tabea.databinding.FragmentCompletedTaskBinding
import com.example.tabea.model.TodoViewModel

class CompletedTaskFragment : Fragment() {

    private var _binding: FragmentCompletedTaskBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: TodoViewModel by activityViewModels()

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
//
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            // @ because inside binding.apply this revers to the binding instance
            // not the class CompletedTaskFragment
            theListOfTodoFragment = this@CompletedTaskFragment
        }
        binding.recyclerView.adapter = CompletedTaskAdapter(requireContext(), myDataset)
        registerForContextMenu(binding.recyclerView)
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
