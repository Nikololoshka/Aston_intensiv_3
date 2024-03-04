package dev.aston.intensiv.nikolay.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import dev.aston.intensiv.nikolay.databinding.ContactItemBinding
import dev.aston.intensiv.nikolay.model.ContactItem

class ContactAdapter(
    private val tracker: SelectionTracker,
    private val listener: ContactAdapterListener
) : ListAdapter<ContactItem, ContactViewHolder>(
    AsyncDifferConfig
        .Builder(ContactDiffUtil)
        .build()
), ReorderObserver, SelectionObserver {

    init {
        tracker.registerObserver(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding, this::onViewHolderClicked)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position), tracker.isSelected(position))
    }

    private fun onViewHolderClicked(position: Int) {
        if (!tracker.isSelectionActive) {
            listener.onItemClicked(getItem(position))
            return
        }

        tracker.select(position)
    }

    override fun onItemMoved(from: Int, to: Int) {
        listener.onItemMoved(from, to)
        notifyItemMoved(from, to)
    }

    override fun onClearSelection(positions: Iterable<Int>) {
        positions.forEach { pos -> notifyItemChanged(pos) }
    }

    override fun onItemSelected(position: Int) {
        notifyItemChanged(position)
    }
}