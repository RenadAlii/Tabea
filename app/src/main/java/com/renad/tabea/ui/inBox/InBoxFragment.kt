package com.renad.tabea.ui.inBox

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.renad.tabea.R
import com.renad.tabea.core.extensions.addMenuProvider
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentInBoxBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadTaskList()
        bindMenu()
        initViews()
        observeState()
    }

    private fun bindMenu() {
        addMenuProvider(R.menu.sort_clear_menu) {
            when (it.itemId) {
                R.id.clearList -> {
                    showWarningDialog()
                    true
                }

                R.id.sortByAES -> {
                    sendSortTaskEvent(SortType.ASC)
                    true
                }

                R.id.sortByDES -> {
                    sendSortTaskEvent(SortType.DES)
                    true
                }

                R.id.sortByDate -> {
                    sendSortTaskEvent(SortType.DATE)
                    true
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(
                        it,
                        requireView().findNavController(),
                    )
                }
            }
        }
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
        binding.showProgress(isLoading)
        binding.bindUi(tasksStatus)
        if (tasks?.isNotEmpty() == true) handelTasksLayout(tasks)
        if (tasks?.isEmpty() == true) handelLayoutVisibility(showTasks = false)
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

    private fun onShowPopupMenu(task: Task, rootView: View) {
        val popupMenu = PopupMenu(context, rootView)
        popupMenu.menuInflater.inflate(R.menu.pop_menu, popupMenu.menu)
        // set the menu item action
        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.menu_edit_todo -> {
                    navigateToUpsertTaskFragment(task.id.toString())
                    true
                }

                else -> {
                    viewModel.onEvent(InBoxEvent.DeleteTask(task))
                    true
                }
            }
        }
        popupMenu.show()
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
                viewModel.onEvent(InBoxEvent.DeleteTasks)
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
