package com.renad.tabea.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.renad.tabea.data.DataSource
import com.renad.tabea.databinding.FragmentWontDoBinding
import com.renad.tabea.ui.adapter.WontDoAdapter

class WontDoFragment : Fragment() {

    private var _binding: FragmentWontDoBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: TodoViewModel by activityViewModels()

    private val myDataset = DataSource.taskList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWontDoBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
        binding.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            // @ because inside binding.apply this revers to the binding instance
            // not the class WontDoFragment
            wontDoFragment = this@WontDoFragment
        }
        binding.recyclerView.adapter = WontDoAdapter(requireContext(), myDataset)
        registerForContextMenu(binding.recyclerView)
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
