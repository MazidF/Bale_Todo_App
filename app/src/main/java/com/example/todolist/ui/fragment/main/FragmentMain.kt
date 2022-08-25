package com.example.todolist.ui.fragment.main

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.model.entity.Task
import com.example.todolist.databinding.FragmnetMainBinding
import com.example.todolist.databinding.TaskFooterControllerBinding
import com.example.todolist.ui.fragment.adapter.ItemAdapter
import com.example.todolist.ui.fragment.adapter.ItemHolder
import com.example.todolist.ui.fragment.adapter.OnItemClick
import com.example.todolist.ui.fragment.adapter.bindable.Bindable
import com.example.todolist.ui.fragment.adapter.bindable.BindableFactory
import com.example.todolist.ui.fragment.adapter.bindable.HolderSetup
import com.example.todolist.ui.fragment.adapter.diffcallback.TaskDiffCallback
import com.example.todolist.ui.fragment.adapter.factory.TaskFactory
import com.example.todolist.ui.fragment.divider.ItemDivider
import com.example.todolist.ui.fragment.itemtouchhelper.ItemTouchCallback
import com.example.todolist.ui.fragment.itemtouchhelper.TaskItemTouchHelper
import com.example.todolist.utils.inflater
import com.example.todolist.utils.launch
import com.example.todolist.utils.set
import com.example.todolist.utils.setup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.max
import kotlin.math.min


@AndroidEntryPoint
class FragmentMain : Fragment(R.layout.fragmnet_main), OnItemClick<Task>, HolderSetup<Task> {
    private var _binding: FragmnetMainBinding? = null
    private val binding: FragmnetMainBinding get() = _binding!!

    private val viewModel: ViewModelMain by viewModels()
    private lateinit var itemAdapter: ItemAdapter<Task>
    private lateinit var footerAdapter: ItemAdapter.FooterAdapter<Int>

    private var itemMoved = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmnetMainBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() {
        setupAdapter()
        setupRecycler()
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        checkbox.setup()
        input.apply {
            doOnTextChanged { text, _, _, _ ->
                addBtn.isVisible = text.isNullOrBlank().not()
            }
        }
        addBtn.setOnClickListener {
            val task = Task(
                name = input.text.toString().trim(),
                isDone = checkbox.isChecked,
                position = System.currentTimeMillis(),
            )
            viewModel.insert(task)
            resetInputs()
        }
    }

    private fun resetInputs() = with(binding) {
        input.text.clear()
        checkbox.set(false)
    }

    private fun setupRecycler() = binding.list.apply {
        adapter = itemAdapter.withFooter(footerAdapter)
        addItemDecoration(ItemDivider(requireContext()))
        TaskItemTouchHelper.attachToRecyclerView(
            itemTouchCallback = TaskItemCallback(),
            recyclerView = this,
        )
    }

    private fun setupAdapter() {
        itemAdapter = ItemAdapter(
            diffCallback = TaskDiffCallback(),
            onItemClick = this,
            factory = TaskFactory(),
            setup = this,
        )
        footerAdapter = ItemAdapter.FooterAdapter(
            0,
            onItemClick = OnItemClick.fake(),
            factory = TaskFooterControllerFactory(),
        )
    }

    private fun observe() = with(binding) {
        launch {
            viewModel.taskStateFlow.collectLatest { list ->
                itemAdapter.submitList(list)
                footerAdapter.submitItem(list.count { it.isDone.not() })
            }
        }
    }

    private fun disableListAnimation() {
        binding.list.itemAnimator?.apply {
            addDuration = 0
            removeDuration = 0
            changeDuration = 0
            moveDuration = 0
        }
    }

    private fun enableListAnimation() {
        binding.list.itemAnimator?.apply {
            addDuration = 120
            removeDuration = 120
            moveDuration = 250
            changeDuration = 250
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteItem(task: Task) {
        viewModel.delete(task)
        showSnackBar(task)
    }

    private fun showSnackBar(task: Task) {
        val name = if (task.name.length > 30) {
            task.name.substring(0, 26) + "..."
        } else {
            task.name
        }
        Snackbar.make(binding.root, "$name deleted!", Snackbar.LENGTH_LONG).apply {
            setAction("Undo") {
                viewModel.insert(task)
            }
        }.show()
    }

    override fun onClick(item: Task) {

    }

    override fun setup(holder: ItemHolder<Task>, bindable: Bindable<Task>) {
        (bindable as TaskFactory.TaskItem).binding.apply {
            checkbox.setup { checked ->
                holder.t?.let { task ->
                    viewModel.check(task, checked)
                }
            }
            delete.setOnClickListener {
                holder.t?.let { task ->
                    deleteItem(task)
                }
            }
        }
    }

    private inner class TaskItemCallback : ItemTouchCallback {
        private var dragFrom: Int? = null
        private var dropTo: Int? = null

        override fun onSwiped(position: Int, direction: Int) {
            if (position != itemAdapter.itemCount) { // block footer
                val task = itemAdapter.getItem(position)
                itemAdapter.notifyItemChanged(position)
                viewModel.check(task, direction == ItemTouchHelper.LEFT)
            } else {
                footerAdapter.notifyItemChanged(0)
            }
        }

        override fun onMove(fromPosition: Int, toPosition: Int): Boolean {
            if (fromPosition != itemAdapter.itemCount) { // block footer
                itemAdapter.swap(fromPosition, toPosition)
                dropTo = toPosition
            }
            return true
        }

        override fun onDrag(viewHolder: RecyclerView.ViewHolder) {
            dragFrom = viewHolder.bindingAdapterPosition
        }

        override fun onClear() {
            dragFrom?.let { f ->
                dropTo?.let { t ->
                    val from = min(f, t)
                    val to = max(f, t)
                    val list = List(to - from + 1) {
                        itemAdapter.getItem(from + it)
                    }
                    itemMoved = true
                    viewModel.switch(f, t, list)
                }
            }
            dragFrom = null
            dropTo = null
        }
    }

    private inner class TaskFooterControllerFactory : BindableFactory<Int> {
        override fun inflate(parent: ViewGroup, viewType: Int): Bindable<Int> {
            val binding = TaskFooterControllerBinding.inflate(
                parent.inflater(), parent, false
            )
            return FooterItem(binding)
        }

        private inner class FooterItem(
            private val binding: TaskFooterControllerBinding,
        ) : Bindable<Int> {

            init {
                binding.apply {
                    clearBtn.setOnClickListener {
                        viewModel.clearCompleted()
                    }
                }
            }

            override fun getRoot(): View {
                return binding.root
            }

            override fun bind(item: Int): Unit = with(binding) {
                val textResourceId = if (item == 0) {
                    R.string.no_item_left
                } else {
                    tasksCount.text = item.toString()
                    R.string.items_left
                }
                counterLabel.setText(textResourceId)
                tasksCount.isVisible = item != 0
                tasksCount.isVisible = (itemAdapter.itemCount - item) != 0
            }
        }
    }
}