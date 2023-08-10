package com.renad.tabea.ui.inBox

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.renad.tabea.R
import com.renad.tabea.core.extensions.collectFlow
import com.renad.tabea.core.extensions.showToast
import com.renad.tabea.databinding.FragmentInBoxBinding
import com.renad.tabea.domain.model.SortType
import com.renad.tabea.domain.model.Task
import com.renad.tabea.ui.inBox.TasksStatus.COMPLETED
import com.renad.tabea.ui.inBox.TasksStatus.IN_BOX
import com.renad.tabea.ui.inBox.TasksStatus.PAST
import com.renad.tabea.ui.inBox.TasksStatus.TODAY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InBoxFragment : Fragment() {

    private var _binding: FragmentInBoxBinding? = null
    private val binding get() = _binding!!

    private var inBoxAdapter: InBoxAdapter? = null
    private val viewModel by viewModels<InBoxViewModel>()

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
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initViews()
        observeState()
    }

    private fun initViews() {
        bindAdapter()
        binding.floatingActionButton.setOnClickListener {
            navigateToUpsertTaskFragment(null)
        }
    }

    private fun observeState() = collectFlow {
        viewModel.uiState.collect(::renderUiState)
    }

    private fun renderUiState(state: TasksUiState) = with(state) {
        binding.bindUi(tasksStatus)
        if (tasks.isNotEmpty()) handelTasksLayout(tasks) else handelLayoutVisibility(showTasks = false)
        binding.showProgress(isLoading)
        errorMsg?.handel { showToast(it) }
    }

    private fun FragmentInBoxBinding.bindUi(screenState: TasksStatus) {
        when (screenState) {
            COMPLETED, PAST -> {
                floatingActionButton.isVisible = false
            }

            TODAY, IN_BOX -> {
                floatingActionButton.isVisible = true
            }
        }
    }

    private fun handelTasksLayout(tasks: List<Task>) {
        inBoxAdapter?.submitList(tasks)
        handelLayoutVisibility()
    }

    private fun handelLayoutVisibility(showTasks: Boolean = true) {
        binding.emptyLayout.isVisible = !showTasks
        binding.recyclerView.isVisible = showTasks
    }

    private fun bindAdapter() {
        inBoxAdapter =
            InBoxAdapter(::navigateToUpsertTaskFragment, ::onTaskChecked, ::onShowPopupMenu)
        binding.recyclerView.adapter = inBoxAdapter
    }

    private fun FragmentInBoxBinding.showProgress(show: Boolean) {
        progressBar.isVisible = show
    }

    private fun onTaskChecked(task: Task) {
        task.id?.let { viewModel.onEvent(InBoxEvent.CompleteTask(it, task.isCompleted)) }
    }

    private fun onShowPopupMenu(taskId: Int, rootView: View) {
        val popupMenu = PopupMenu(context, rootView)
        popupMenu.menuInflater.inflate(R.menu.pop_menu, popupMenu.menu)
        // set the menu item action
        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.menu_edit_todo -> {
                    navigateToUpsertTaskFragment(taskId.toString())
                    true
                }

                else -> {
                    viewModel.onEvent(InBoxEvent.DeleteTask(taskId))
                    true
                }
            }
        }
        popupMenu.show()
    }

    // inflate the sort_delete menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_clear_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // create action when clicking on the menu item
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clearList -> {
                showWarningDialog()
                return true
            }

            R.id.sortByAES -> {
                sendSortTaskEvent(SortType.ASC)
                return true
            }

            R.id.sortByDES -> {
                sendSortTaskEvent(SortType.DES)
                return true
            }

            R.id.sortByDate -> {
                sendSortTaskEvent(SortType.DATE)
                return true
            }

            R.id.sortByTime -> {
                sendSortTaskEvent(SortType.TIME)
                return true
            }
        }
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController(),
        ) || super.onOptionsItemSelected(item)
    }

    private fun sendSortTaskEvent(sortType: SortType) {
        viewModel.onEvent(InBoxEvent.SortTasks(sortType))
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
                viewModel.onEvent(InBoxEvent.DeleteAllTasks)
            }
            .show()
    }

    private fun navigateToUpsertTaskFragment(taskId: String?) {
        findNavController().navigate(
            InBoxFragmentDirections.actionTheListOfTodoFragmentToUpsertTaskFragment(
                taskId,
            ),
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        inBoxAdapter = null
    }
}
