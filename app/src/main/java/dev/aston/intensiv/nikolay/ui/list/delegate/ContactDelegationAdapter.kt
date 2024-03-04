package dev.aston.intensiv.nikolay.ui.list.delegate

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import dev.aston.intensiv.nikolay.model.ContactItem
import dev.aston.intensiv.nikolay.ui.list.ContactAdapterListener
import dev.aston.intensiv.nikolay.ui.list.ContactDiffUtil
import dev.aston.intensiv.nikolay.ui.list.ReorderObserver
import dev.aston.intensiv.nikolay.ui.list.SelectionObserver
import dev.aston.intensiv.nikolay.ui.list.SelectionTracker

// TODO("Нет визуальной поддержки выделения элементов")
class ContactDelegationAdapter(
    private val tracker: SelectionTracker,
    private val listener: ContactAdapterListener
) : AsyncListDifferDelegationAdapter<ContactItem>(
    ContactDiffUtil
), ReorderObserver, SelectionObserver {

    init {
        tracker.registerObserver(this)
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

    override fun onClearSelection(positions: Iterable<Int>) {

    }

    override fun onItemSelected(position: Int) {

    }

    override fun onItemMoved(from: Int, to: Int) {
        listener.onItemMoved(from, to)
        notifyItemMoved(from, to)
    }
}