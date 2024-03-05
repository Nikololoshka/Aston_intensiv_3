package dev.aston.intensiv.nikolay.ui.list.delegate

import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewHolder
import dev.aston.intensiv.nikolay.model.ContactItem
import dev.aston.intensiv.nikolay.ui.list.ContactAdapterListener
import dev.aston.intensiv.nikolay.ui.list.ContactDiffUtil
import dev.aston.intensiv.nikolay.ui.list.ReorderObserver
import dev.aston.intensiv.nikolay.ui.list.SelectionObserver
import dev.aston.intensiv.nikolay.ui.list.SelectionTracker


class ContactDelegationAdapter(
    private val tracker: SelectionTracker,
    private val listener: ContactAdapterListener
) : AsyncListDifferDelegationAdapter<ContactItem>(
    ContactDiffUtil
), ReorderObserver, SelectionObserver {

    init {
        delegatesManager.addDelegate(contactDelegate(this::onItemClicked))
    }

    fun submitList(items: List<ContactItem>) {
        super.setItems(items)
    }

    private fun onItemClicked(position: Int) {
        if (!tracker.isSelectionActive) {
            listener.onItemClicked(items[position])
            return
        }

        tracker.select(position)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        val selectablePayloads = listOf(tracker.isSelected(position))
        super.onBindViewHolder(holder, position, selectablePayloads)
    }

    override fun onClearSelection(positions: Iterable<Int>) {
        positions.forEach { pos -> notifyItemChanged(pos) }
    }

    override fun onItemSelected(position: Int) {
        notifyItemChanged(position)
    }

    override fun onItemMoved(from: Int, to: Int) {
        listener.onItemMoved(from, to)
        notifyItemMoved(from, to)
    }
}