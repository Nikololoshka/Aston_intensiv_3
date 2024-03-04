package dev.aston.intensiv.nikolay.ui.list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

interface ReorderObserver {

    fun onItemMoved(from: Int, to: Int)
}


class ReorderItemHelper private constructor(
    private val callback: ReorderItemCallback
): ItemTouchHelper(callback) {

    constructor(observer: ReorderObserver): this(ReorderItemCallback(observer))

    fun enableReorder(enable: Boolean) {
        callback.isReorder = enable
    }
}

private class ReorderItemCallback(
    private val observer: ReorderObserver
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
) {
    var isReorder: Boolean = false

    override fun isLongPressDragEnabled(): Boolean {
        return isReorder
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        observer.onItemMoved(from, to)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}