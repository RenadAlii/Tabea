package com.renad.tabea.ui

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.renad.tabea.R
import com.renad.tabea.data.DataSource
import com.renad.tabea.databinding.FragmentInBoxBinding
import com.renad.tabea.ui.adapter.InBoxAdapter

class InBoxFragment : Fragment() {

    private var _binding: FragmentInBoxBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: TodoViewModel by activityViewModels()

    private val myDataset = DataSource.taskList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentInBoxBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = InBoxAdapter(requireContext(), myDataset)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_theListOfTodoFragment_to_newTodoFragment)
        }
    }

    // inflate the sort_delete menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort__clear_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // create action when clicking on the menu item
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clearList -> {
                showWarningDialog()
                binding.recyclerView.removeAllViews()
                return true
            }
            R.id.filterByAES -> {
                sharedViewModel.sortByASC()
                binding.recyclerView.removeAllViews()
                return true
            }
            R.id.filterByDES -> {
                sharedViewModel.sortByDES()
                binding.recyclerView.removeAllViews()
                return true
            }
            R.id.filterByDate -> {
                sharedViewModel.sortByDate()
                binding.recyclerView.removeAllViews()
                return true
            } }
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) ||
            super.onOptionsItemSelected(item)
    }

    // dialog displayed when the user try to clear the data
    private fun showWarningDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.deleteTask))
            .setMessage(getString(R.string.clearTasksDialogMsg))
            .setCancelable(true)
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                sharedViewModel.clearList()
                binding.recyclerView.removeAllViews()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
