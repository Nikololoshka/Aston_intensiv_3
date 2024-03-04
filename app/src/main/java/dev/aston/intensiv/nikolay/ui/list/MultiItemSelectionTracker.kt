package dev.aston.intensiv.nikolay.ui.list

import android.os.Bundle
import androidx.core.os.bundleOf




interface SelectionObserver {

    fun onClearSelection(positions: Iterable<Int>)

    fun onItemSelected(position: Int)
}

interface SelectionTracker {

    val isSelectionActive: Boolean

    fun isSelected(position: Int): Boolean

    fun select(position: Int)

    fun registerObserver(adapter: SelectionObserver)

    fun unregisterObserver()
}

class MultiItemSelectionTracker(
    private val stateName: String
): SelectionTracker {

    private var selectionObserver: SelectionObserver? = null

    private val selection: MutableSet<Int> = mutableSetOf()

    val currentSelected: Set<Int> get() = selection

    override var isSelectionActive = false
        set(value) {
            field = value
            if (!value) {
                clearSelection()
            }
        }

    override fun registerObserver(adapter: SelectionObserver) {
        selectionObserver = adapter
    }

    private fun clearSelection() {
        selectionObserver?.onClearSelection(selection)
        selection.clear()
    }

    fun onRestoreInstanceState(state: Bundle) {
        val selectionState = state.getBundle(stateName)
        val savedSelection = selectionState?.getIntArray(SELECTION_ARRAY)
        if (savedSelection != null) {
            selection.clear()
            selection.addAll(savedSelection.asIterable())
        }
    }

    fun onSaveInstanceState(state: Bundle) {
        val savedState = bundleOf(SELECTION_ARRAY to selection.toIntArray())
        state.putBundle(stateName, savedState)
    }

    override fun unregisterObserver() {
        selectionObserver = null
    }

    override fun isSelected(position: Int): Boolean {
        return selection.contains(position)
    }

    override fun select(position: Int) {
        if (!selection.contains(position)) {
            selection.add(position)
        } else {
            selection.remove(position)
        }
        selectionObserver?.onItemSelected(position)
    }

    companion object {
        private const val SELECTION_ARRAY = "selection_array"
    }
}