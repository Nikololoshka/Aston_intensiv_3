package dev.aston.intensiv.nikolay.ui.list

import dev.aston.intensiv.nikolay.model.ContactItem

interface ContactAdapterListener {
    fun onItemClicked(item: ContactItem)

    fun onItemMoved(from: Int, to: Int)
}