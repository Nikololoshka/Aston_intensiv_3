package dev.aston.intensiv.nikolay.ui.list

import android.os.Bundle
import androidx.core.os.bundleOf


interface SelectableAdapter {

    fun onClearSelection(positions: Iterable<Int>)

    fun onItemSelected(position: Int)
}

interface SelectionTracker {

    val isSelectionActive: Boolean

    fun isSelected(position: Int): Boolean

    fun select(position: Int)

    fun setup(adapter: SelectableAdapter)
}

// TODO("Избавиться от взаимного хранения ссылок адаптера и селектора")
class MultiItemSelectionTracker(
    private val stateName: String
): SelectionTracker {

    private var selectableAdapter: SelectableAdapter? = null

    private val selection: MutableSet<Int> = mutableSetOf()

    val currentSelected: Set<Int> get() = selection

    override var isSelectionActive = false
        set(value) {
            field = value
            if (!value) {
                clearSelection()
            }
        }

    override fun setup(adapter: SelectableAdapter) {
        selectableAdapter = adapter
    }

    private fun clearSelection() {
        selectableAdapter?.onClearSelection(selection)
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

    fun onDestroy() {
        selectableAdapter = null
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
        selectableAdapter?.onItemSelected(position)
    }

    companion object {
        private const val SELECTION_ARRAY = "selection_array"
    }
}