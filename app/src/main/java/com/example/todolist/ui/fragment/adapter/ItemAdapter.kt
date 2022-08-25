package com.example.todolist.ui.fragment.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.ui.fragment.adapter.bindable.BindableFactory
import com.example.todolist.ui.fragment.adapter.bindable.HolderSetup

class ItemAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val onItemClick: OnItemClick<T>,
    private val factory: BindableFactory<T>,
    private val setup: HolderSetup<T>? = null,
) : ListAdapter<T, ItemHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<T> {
        return ItemHolder(
            bindable = factory.inflate(parent, viewType),
            onItemClick = onItemClick,
            setup = setup,
        )
    }

    override fun onBindViewHolder(holder: ItemHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    public override fun getItem(position: Int): T {
        return super.getItem(position)
    }

    fun swap(from: Int, to: Int) {
        notifyItemMoved(from, to)
    }

    fun <R : Any> withFooter(
        adapter: FooterAdapter<R>,
    ): ConcatAdapter {
        return ConcatAdapter(this, adapter)
    }

    class FooterAdapter<T : Any>(
        private var item: T,
        private val factory: BindableFactory<T>,
        private val onItemClick: OnItemClick<T>,
        private val setup: HolderSetup<T>? = null,
    ) : RecyclerView.Adapter<ItemHolder<T>>() {

        @SuppressLint("NotifyDataSetChanged")
        fun submitItem(t: T) {
            item = t
            notifyItemChanged(0, null)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<T> {
            return ItemHolder(
                bindable = factory.inflate(parent, viewType),
                onItemClick = onItemClick,
                setup = setup,
            )
        }

        override fun onBindViewHolder(holder: ItemHolder<T>, position: Int) {
            holder.bind(item)
        }

        override fun getItemCount(): Int {
            return 1
        }
    }
}
