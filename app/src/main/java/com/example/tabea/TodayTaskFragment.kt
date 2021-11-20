package com.example.tabea

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tabea.data.DataSource
import com.example.tabea.model.TodoViewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tabea.adapter.TodayTaskAdapter
import com.example.tabea.databinding.FragmentTodayTaskBinding


class TodayTaskFragment : Fragment() {

    private var _binding: FragmentTodayTaskBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: TodoViewModel by activityViewModels()

    private val myDataset = DataSource.taskList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTodayTaskBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
        binding.apply {

            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            //@ because inside binding.apply this revers to the binding instance
            // not the class TodayTaskFragment
            todayTaskFragment = this@TodayTaskFragment
        }
        binding.recyclerView.adapter = TodayTaskAdapter(requireContext(), myDataset)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_todayTaskFragment_to_newTodoFragment)
        }
        registerForContextMenu(binding.recyclerView)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


